package com.hand.hmall.service.impl;

import com.hand.hmall.client.IPromoteClientService;
import com.hand.hmall.dao.GroupDao;
import com.hand.hmall.dao.RuleDao;
import com.hand.hmall.dao.SaleActivityDao;
import com.hand.hmall.dao.SaleCouponDao;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.dto.ThreadFactory;
import com.hand.hmall.menu.CreateJarType;
import com.hand.hmall.menu.Status;
import com.hand.hmall.model.ResourceWrapper;
import com.hand.hmall.pojo.OrderEntryPojo;
import com.hand.hmall.pojo.OrderPojo;
import com.hand.hmall.service.IActionService;
import com.hand.hmall.service.IExcutePromoteService;
import com.hand.hmall.service.ISaleCouponService;
import com.hand.hmall.service.PromoteCaculateService;
import com.hand.hmall.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CountDownLatch;

import static java.util.Comparator.comparing;

/**
 * @Describe 调用drools，执行促销服务，判断订单是否满足促销
 * @Author xinyang.Mei
 * @Date 2017/6/28 17:09
 */
@Service
public class ExcutePromoteService implements IExcutePromoteService {
    @Autowired
    private GroupDao groupDao;


    @Autowired
    private SaleActivityDao saleActivityDao;

    @Autowired
    private RuleDao ruleDao;

    @Autowired
    private SaleCouponDao saleCouponDao;

    @Autowired
    private IActionService actionService;
    @Autowired
    private IPromoteClientService promoteClientService;
    @Autowired
    private ISaleCouponService saleCouponService;

    private KieServices kieServices = KieServices.Factory.get();

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 检索用户可用促销活动
     *
     * @Author: noob
     * @Param: * @param null
     * @Date: 2017/6/29 8:58
     */
    @Override
    public ResponseData payByActivity(OrderPojo order, String type) {
        logger.info("-------------start payByActivity");
        ResponseData responseData = new ResponseData();
        Long orderCreateTime = DateFormatUtil.stringToTimeStamp(order.getCreated());

        try {
            if (type.equals(CreateJarType.ORIGINAL.toString())) {

                //查询订单层级促销活动，包括活动中和已失效的活动
                List<Map<String, ?>> activities = saleActivityDao.selectActivityForOrder();
                List listMid = Collections.synchronizedList(new ArrayList());
                if (!activities.isEmpty()) {
                    //处理订单头促销
                    CountDownLatch countDownLatch = new CountDownLatch(activities.size());
                    for (Map activity : activities) {
                        Long startDate = (Long) activity.get("startDate");
                        Long endDate = (Long) activity.get("endDate");
                        if (startDate <= orderCreateTime && orderCreateTime <= endDate) {
                            OrderPojo orderMid = order.clone();
//                            listMid.add(orderMid);
                            ThreadFactory.getThreadPool().submit(new FilterUsefulActivityTask(countDownLatch, activity, orderMid, listMid));
                        } else {
                            countDownLatch.countDown();
                        }
                    }
                    countDownLatch.await();

                }

                if (CollectionUtils.isEmpty(listMid)) {
                    listMid.add(order);
                }
                responseData.setResp(listMid);
            }
        } catch (Exception e) {
            logger.error("执行规则出错", e);
            responseData = ResponseReturnUtil.returnFalseResponse("执行规则出错", null);
        }
        return responseData;
    }

    /**
     * 单个执行促销活动规则
     *
     * @param activityId 促销活动对应的activityId，注意此处activityId对应Activity表中的id字段
     * @param order      参与该促销活动对应的订单或订单行
     * @Author: mxy
     * @Date: 2017/6/29 8:57
     */
    public ResponseData payByActivitySingle(String activityId, OrderPojo order) {
        long start = System.currentTimeMillis();
        String threadName = Thread.currentThread().getName();
        logger.info("payByActivitySingle [" + threadName + "] >>>>>> started.");
        try {
            ResponseData responseData = new ResponseData();
            Map<String, ?> activity = saleActivityDao.select(activityId);
            logger.info(threadName + ".A:" + (System.currentTimeMillis() - start));
            long t1 = System.currentTimeMillis();
            KieSession kieSession = KieSessionUtil.getKieSessionByActivityId(kieServices, activity.get("id").toString(), activity.get("activityId").toString());
            if (kieSession == null) {
                List<Map<String, ?>> maps = ruleDao.selectByEqField("activityId", activityId);
                long t2 = System.currentTimeMillis();
                logger.info(threadName + ".B:" + (t2 - t1));
                Map<String, Object> ruleMap = (Map<String, Object>) maps.get(0);
                String rules = ruleMap.get("rule").toString();
                ResourceWrapper resourceWrapper = new ResourceWrapper(ResourceFactory.newByteArrayResource(rules.getBytes()), activityId + ".drl");
                kieSession = KieSessionUtil.kieSessionByStream(kieServices, resourceWrapper, activity.get("id").toString(), activity.get("activityId").toString());
                logger.info(threadName + ".C:" + (System.currentTimeMillis() - t2));
            }

            responseData.setSuccess(false);
            kieSession.setGlobal("actionService", actionService);
            kieSession.setGlobal("responseData", responseData);

            //插入需要执行规则的数据对象
            kieSession.insert(order);
            long t4 = System.currentTimeMillis();
            logger.info(threadName + ".D:" + (t4 - t1));
            for (OrderEntryPojo orderEntryPojo : order.getOrderEntryList()) {
                kieSession.insert(orderEntryPojo);
            }
            long t5 = System.currentTimeMillis();
            logger.info(threadName + ".E:" + (t5 - t4));
            //执行
            kieSession.fireAllRules();
            kieSession.dispose();
            //该判断用于标识订单行满减满折促销是否执行。ActionService中的判断只用于订单头，此处用于不包含订单头的促销
            if (responseData.isSuccess()) {
                order.setCheckedActivity(true);
            }
            order.computePrice();
            long t6 = System.currentTimeMillis();
            logger.info(threadName + ".F:" + (t6 - t5));
            return responseData;
        } catch (Exception e) {
            ResponseData responseData = new ResponseData();
            responseData.setSuccess(false);
            responseData.setMsg("促销执行异常" + e.getMessage());
            logger.error("促销执行异常" + e.getMessage(), e);
            return responseData;
        }
    }


    /**
     * 执行订单行相关促销，订单行促销相较订单头促销，为了实现订单行促销自动计算，增加了优先级，可叠加，分组特性。优先级为1最高
     * 将订单行的促销分成不同的分组
     * 每个分组内的促销，按优先级依次执行，如果当前促销规则不可叠加，但是前一条成功执行的促销可叠加，则执行当前促销。
     * 然后执行下一分组的促销。不同分组间的促销规则不存在叠加冲突。
     *
     * @param order 参与行促销执行的的订单行数据
     */
    @Override
    public void orderEntryPromote(OrderPojo order) {
        Long orderCreateTime = DateFormatUtil.stringToTimeStamp(order.getCreated());
        try {
            //获取所有促销分组
            List<Map<String, ?>> groups = groupDao.selectAll();
            //根据组的优先级对分组进行升序排序
            SortUtil.listsort(groups, "priority", true);
            //遍历促销组
            for (Map<String, ?> group : groups) {
                //不是免邮分组
                if (!group.get("id").equals(CreateJarType.FREE_FREIGHT.getValue())) {
                    //获取该分组下所有促销规则，type为2对应订单行类型促销
//                    List<Map<String, ?>> activities = saleActivityDao.selectByGroup(Status.ACTIVITY.getValue(), null, group.get("id").toString(), "2", null);
                    List<Map<String, ?>> activities = saleActivityDao.selectActivityForEntry(group.get("id").toString());
                    if (!activities.isEmpty()) {
                        //若该分组存在促销，按优先级对促销活动升序排序
                        activities.sort(comparing((Map map) -> {
                            Integer priority = (Integer) map.get("priority");
                            if (priority == null) {
                                priority = 99;
                            }
                            return priority;
                        }).thenComparing(comparing(map -> {
                            Long startTime = (Long) map.get("startTime");
                            if (startTime == null) {
                                startTime = 0L;
                            }
                            return startTime;
                        })).thenComparing(comparing(map -> {
                            String id = map.get("id").toString();
                            return id;
                        })));
                        //获取分组下最高优先级的促销活动
                        Map<String, Object> activityFirst = (Map<String, Object>) activities.get(0);
                        //获取促销生效起始时间
                        Long startDate = (Long) activityFirst.get("startDate");
                        Long endDate = (Long) activityFirst.get("endDate");
                        //若该规则在生效范围，且规则是订单行促销。执行该条促销
                        if (startDate <= orderCreateTime && orderCreateTime <= endDate) {
                            payByActivitySingle((String) activityFirst.get("id"), order);
                        }
                        //isSuccessFlag标识执行的前一条促销是否执行成功
                        boolean isSuccessFlag = false;
                        //当前促销执行成功，isSuccessFlag置为true
                        if (order.isCheckedActivity()) {
                            isSuccessFlag = true;
                            order.setCheckedActivity(false);
                        }
                        //移除最高优先级促销
                        activities.remove(0);

                        //计算余下促销叠加与否
                        for (Map activity : activities) {
                            //第一条促销执行成功，后面的就看叠加状态，可叠加就执行，不可叠加则不执行
                            if (isSuccessFlag) {
                                if (activity.get("isOverlay").equals("Y")) {
                                    Long ostartDate = (Long) activity.get("startDate");
                                    Long oendDate = (Long) activity.get("endDate");
                                    if (ostartDate <= orderCreateTime && orderCreateTime <= oendDate) {
                                        payByActivitySingle((String) activity.get("id"), order);
                                    }
                                    order.setCheckedActivity(false);
                                }
                            } else {
                                //第一条促销执行不成功，将下一条促销视为第一条促销。
                                Long ostartDate = (Long) activity.get("startDate");
                                Long oendDate = (Long) activity.get("endDate");
                                if (ostartDate <= orderCreateTime && orderCreateTime <= oendDate) {
                                    payByActivitySingle((String) activity.get("id"), order);
                                    //该促销执行成功后，叠加flag置为true
//                                    if (order.isCheckedActivity()) {
//                                        isOverlayFlag = true;
//                                        order.setCheckedActivity(false);
//                                    }
                                }
                                if (order.isCheckedActivity()) {
                                    //修改标识符
                                    isSuccessFlag = true;
                                    order.setCheckedActivity(false);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("执行订单行促销失败", e);
        }
    }

    /**
     * 检索订单满足的优惠券
     *
     * @param order 参与优惠券匹配的订单
     * @return 满足该订单的相应优惠券
     * @throws CloneNotSupportedException
     */
    @Override
    public ResponseData optionCouponWithoutActivity(OrderPojo order) throws CloneNotSupportedException, InterruptedException {

        String userId = order.getCustomerId();
        //先获取到当前用户所有的优惠券
        Map<String, Object> parmMap = new HashMap<>();
        parmMap.put("customerId", userId);
        parmMap.put("status", "STATUS_01");
        Long startUC = System.currentTimeMillis();
        //调用promote微服务，查询用户拥有的所有优惠券
        ResponseData responseData = promoteClientService.queryByUserIdAndStatus(parmMap);
        logger.info("----用户所有的优惠券----" + (System.currentTimeMillis() - startUC));
        //该字段用于保存订单满足的优惠券信息
        List usefulCoupon = Collections.synchronizedList(new ArrayList());
        startUC = System.currentTimeMillis();
        if (responseData.isSuccess() && CollectionUtils.isNotEmpty(responseData.getResp())) {
            List<String> customerCoupons = (List<String>) responseData.getResp();
            //订单重算字段，保存应用在原订单的优惠券
            String usedCoupon = order.getUsedCoupon();
            //用于订单重算，重算前订单已应用的优惠券，则判断该优惠券是否合法，若优惠券类型是用户购买，被占用状态。则将该优惠券返回给用户选择
            if (StringUtils.isNotEmpty(usedCoupon)) {
                ResponseData usedCouponResp = promoteClientService.getCusomerCouponByCouponId(usedCoupon);

                if (CollectionUtils.isNotEmpty(usedCouponResp.getResp())) {
                    Map usedCouponMap = (Map) usedCouponResp.getResp().get(0);
                    if (usedCouponMap != null) {
                        String couponType = (String) usedCouponMap.get("type");
                        String couponStatus = (String) usedCouponMap.get("status");
                        //choosenCoupon对应优惠券存在,且为用户购买类型、被占用状态，加入优惠券遍历列表
                        if ("STATUS_02".equals(couponStatus.trim())) {
                            ResponseData payData = payByCoupon(usedCoupon, order.clone(), userId);
                            if (payData.isSuccess()) {
                                List couponList = payData.getResp();
                                usefulCoupon.addAll(couponList);
                            }
                        }
                    }
                }
            }

            CountDownLatch latch = new CountDownLatch(customerCoupons.size());
            for (int i = 0; i < customerCoupons.size(); i++) {
                String couponId = customerCoupons.get(i);
                ThreadFactory.getThreadPool().submit(new FilterUsefulCouponTask(latch, couponId, userId, order.clone(), usefulCoupon));
            }
            latch.await();
            logger.info("----获取可用优惠券：" + (System.currentTimeMillis() - startUC));
            return new ResponseData(usefulCoupon);
        } else {
            return new ResponseData(Arrays.asList(order));
        }


    }


    /**
     * 执行单条优惠券促销
     *
     * @param couponId
     * @param order
     * @param currentUserId
     * @return
     */
    @Override
    public ResponseData payByCoupon(String couponId, OrderPojo order, String currentUserId) {
        //根据要执行的优惠券（customerCoupon）的主键查询其详细信息
        ResponseData responseData = promoteClientService.getByCouponId(couponId);
        if (responseData.isSuccess()) {
            List<Map<String, Object>> customerCouponList = (List<Map<String, Object>>) responseData.getResp();
            if (customerCouponList != null) {
                if (!customerCouponList.isEmpty()) {
                    try {
                        String usedCoupon = order.getUsedCoupon();
                        Map customerCoupon = customerCouponList.get(0);
                        //若所选优惠券是参与重算的优惠券，不去判断优惠券状态；否则判断优惠券状态，不是活动中，返回异常信息
                        if (!couponId.equals(usedCoupon)) {
                            if (!customerCoupon.get("status").equals("STATUS_01")) {
                                return new ResponseData(false, "PROMOTE_COUPON_005");
                            }
                        }
                        //校验优惠券所属用户
                        if (!customerCoupon.get("userId").equals(currentUserId) || !order.getCustomerId().equals(currentUserId)) {
                            return new ResponseData(false, "ERROR_USER");
                            //校验用户所属优惠券是否关联coupon
                        } else if (customerCoupon.get("cid") == null || customerCoupon.get("cid").toString().trim().equals("")) {
                            return new ResponseData(false, "ERROR_COUPON");
                        }
                        String cid = customerCoupon.get("cid").toString();
                        Map<String, ?> coupon = saleCouponDao.select(cid);


                        //根据cid获取优惠券的规则，cid就是规则表的外键,然后获取得到该规则的KieSession
                        List<Map<String, ?>> maps = ruleDao.selectByEqField("couponId", cid);
                        Map<String, Object> ruleMap;
                        if (maps != null) {
                            if (!maps.isEmpty()) {
                                ruleMap = (Map<String, Object>) maps.get(0);
                            } else {
                                return new ResponseData(false, "NULL_POINT");
                            }
                        } else {
                            return new ResponseData(false, "NULL_POINT");
                        }

                        String rules = ruleMap.get("rule").toString();

                        ResourceWrapper resourceWrapper = new ResourceWrapper(ResourceFactory.newByteArrayResource(rules.getBytes()), cid + ".drl");

                        KieSession kieSession = KieSessionUtil.kieSessionByStream(kieServices, resourceWrapper, coupon.get("couponId").toString(), coupon.get("releaseId").toString());
                        try {
                            kieSession.setGlobal("actionService", actionService);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //插入需要执行规则的数据对象
                        kieSession.insert(order);
                        for (OrderEntryPojo product : order.getOrderEntryList()) {
                            kieSession.insert(product);
                        }
                        kieSession.fireAllRules();
                        kieSession.dispose();

                        //执行完毕后，校验，如果没有执行action部分代码，返回CANNOT_USE的错误码
                        if (!order.isCheckedCoupon()) {
                            return new ResponseData(false, "CANNOT_USE");
                        }
                        //将优惠券信息插入到订单中
                        Map<String, Object> couponMap = new HashedMap();

                        couponMap.put("id", coupon.get("id"));
                        couponMap.put("couponName", coupon.get("couponName"));
                        couponMap.put("couponCode", coupon.get("couponCode"));
                        couponMap.put("discountType", coupon.get("discountType"));
                        couponMap.put("couponId", couponId);
                        couponMap.put("releaseId", coupon.get("releaseId"));
                        couponMap.put("benefit", coupon.get("benefit"));
                        couponMap.put("startDate", DateUtil.formMillstToDate(customerCoupon.get("startDate").toString().trim(), "yyyy/MM/dd HH:mm:ss"));
                        couponMap.put("endDate", DateUtil.formMillstToDate(customerCoupon.get("endDate").toString().trim(), "yyyy/MM/dd HH:mm:ss"));
                        couponMap.put("couponDes", coupon.get("couponDes"));
                        List couponList = order.getCouponList();
                        if (CollectionUtils.isEmpty(couponList))
                            couponList = new ArrayList<>();
                        couponList.add(couponMap);
                        order.setCouponList(couponList);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return new ResponseData(false, "ERROR_RULE");
                    }
                }
            }
        } else {
            return responseData;
        }
        List resp = new ArrayList();
        resp.add(order);
        return new ResponseData(resp);

    }

    /**
     * 执行用户所选优惠券
     *
     * @param order
     * @return
     */
    @Override
    public ResponseData useCoupon(OrderPojo order) {

        List returnList = new ArrayList<>();
        List<String> couponList = new ArrayList<>();

        String currentUserId = order.getCustomerId();
        if (order.getChosenCoupon() != null) {
            if (!order.getChosenCoupon().equals("")) {
                String couponId = order.getChosenCoupon();
                //校验用户持有的优惠券是否可用
//                    ResponseData checkedCoupon = checkedCouponLegal(couponId, currentUserId);
//                    if (checkedCoupon.isSuccess()) {
                ResponseData responseData = payByCoupon(couponId, order, currentUserId);
                if (responseData.isSuccess()) {
                    if (order.isCheckedCoupon()) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("couponId", order.getChosenCoupon());
                        String distributionId = (!order.getDistributionId().equals("")) ? order.getDistributionId() : "NULL";
                        map.put("distributionId", (distributionId != null) ? distributionId : "");
                        map.put("userId", currentUserId);
                        map.put("creationTime", System.currentTimeMillis());
                        couponList.add(order.getChosenCoupon());
                    }
                } else {
                    Map<String, Object> couponMap = new HashedMap();
                    order.setCouponList(Arrays.asList(couponMap));
                    return responseData;
                }
            }
        }
        return new ResponseData(returnList);
    }


    //检测订单中优惠券是否能正常使用
    public ResponseData checkedCouponLegal(String couponId, String userId) {
        ResponseData respFalse = ResponseReturnUtil.returnFalseResponse("优惠券错误", null);

        if (couponId == null || userId == null || couponId.trim().equals("") || userId.trim().equals("")) {
            return respFalse;
        } else {
            return promoteClientService.selectCouponById(couponId, userId);
        }
    }



    class FilterUsefulCouponTask implements Runnable {

        private CountDownLatch latch;

        private String couponId;

        private String userId;

        private OrderPojo order;


        private List<OrderPojo> resultList;

        public FilterUsefulCouponTask(CountDownLatch latch, String couponId, String userId, OrderPojo order, List<OrderPojo> resultList) {
            this.latch = latch;
            this.couponId = couponId;
            this.userId = userId;
            this.order = order;
            this.resultList = resultList;
        }

        @Override
        public void run() {

            //校验优惠券与用户关系
            try {
                ResponseData checkedCoupon = checkedCouponLegal(couponId, userId);
                if (checkedCoupon.isSuccess()) {
                    //校验优惠券是否是独占券,是独占券则将订单信息置为未执行任何促销的状态
                    if (saleCouponService.checkExclusiveCoupon(couponId)) {
                        order.getOrderEntryList().forEach(entryPojo -> {
                            entryPojo.initEntry();
                        });
                        order.computePrice();
                    }
                    ResponseData payData = payByCoupon(couponId, order, userId);
                    if (payData.isSuccess()) {
                        List couponList = payData.getResp();
                        resultList.addAll(couponList);
                    }
                }
            } catch (Exception e) {
                logger.error("执行优惠券异常", e);
            } finally {
                this.latch.countDown();
            }

        }
    }

    class FilterUsefulActivityTask implements Runnable {

        private CountDownLatch latch;

        private Map activity;


        private OrderPojo order;


        private List<OrderPojo> listMid;

        public FilterUsefulActivityTask(CountDownLatch latch, Map activity, OrderPojo order, List<OrderPojo> resultList) {
            this.latch = latch;
            this.activity = activity;
            this.order = order;
            this.listMid = resultList;
        }

        @Override
        public void run() {

            //校验优惠券与用户关系
            try {
                OrderPojo orderMid = order.clone();
                //循环执行订单头促销
                ResponseData payData = payByActivitySingle(activity.get("id").toString(), orderMid);
                if (orderMid.isCheckedActivity()) {
                    if (!payData.isSuccess()) {
                        activity.put("msg", payData.getMsg());
                        activity.put("msgCode", payData.getMsgCode());
                    }
                }
                listMid.add(orderMid);
            } catch (Exception e) {
                logger.error("执行促销异常", e);
            } finally {
                latch.countDown();
            }

        }
    }

}
