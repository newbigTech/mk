package com.hand.hmall.service.impl;

import com.hand.hmall.client.IPdClientService;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.dto.ThreadFactory;
import com.hand.hmall.menu.CreateJarType;
import com.hand.hmall.model.Order;
import com.hand.hmall.pojo.OrderEntryPojo;
import com.hand.hmall.pojo.OrderPojo;
import com.hand.hmall.service.*;
import com.hand.hmall.util.ArithUtil;
import com.hand.hmall.util.ResponseReturnUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @Describe 实现促销执行的业务逻辑，调用促销执行操作
 * @Author xinyang.Mei
 * @Date 2017/6/28 16:58
 */
@Service
public class PromoteServiceImpl implements IPromoteService {

    @Autowired
    private IExcutePromoteService excutePromoteService;

    @Autowired
    private ISaleCouponService saleCouponService;
    @Autowired
    private IActionService actionService;
    @Autowired
    private PromoteCaculateService promoteCaculateService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * @param order 参与促销运算的订单数据
     * @return
     * @throws CloneNotSupportedException
     * @throws ExecutionException
     * @throws InterruptedException
     * @desc 获取订单满足的优惠券与促销活动，执行促销与优惠券，计算订单头行的相关费用
     */
    @Override
    public ResponseData promote(OrderPojo order) throws CloneNotSupportedException, ExecutionException, InterruptedException {
        order.computePrice();
        //保存未执行促销前的订单数据
        OrderPojo clone = order.clone();
        long startTime = System.currentTimeMillis();
        //执行订单行促销
        Future<List<OrderEntryPojo>> orderEntryPromote = ThreadFactory.getThreadPool().submit(new OrderEntryPromote(order));
        orderEntryPromote.get();
        logger.info(" 2.1 ==== ++++ 计算订单行促销 ：" + (System.currentTimeMillis() - startTime));

        //标识计算促销还是获取可用促销
        String isCaculate = order.getIsCaculate();
        //isCaculate为N 为获取订单满足的优惠券与促销活动。
        if ("N".equals(isCaculate)) {
            long t_getpAndC = System.currentTimeMillis();

            //获取订单满足的优惠券与促销活动
            ResponseData promoteAndCoupons = getUsefulPromoteAndCoupon(order);

            logger.info(" 2.2 ==== ++++ 获取可用活动和优惠券 ：" + (System.currentTimeMillis() - t_getpAndC));
            return promoteAndCoupons;
        } else if ("Y".equals(isCaculate)) {

            //isCaculate为Y 为获取订单满足的优惠券与促销活动。同时执行用户选择的促销活动与优惠券
            Long start = System.currentTimeMillis();
            //获取订单满足的优惠券与促销，分别保存到usefulOrder的couponList与activities字段中
            ResponseData usefulOrderResp = getUsefulPromoteAndCoupon(order);

            OrderPojo usefulOrder = (OrderPojo) usefulOrderResp.getResp().get(0);

            ResponseData responseData = new ResponseData();
            //执行用户选择的促销与优惠券
            order.computePrice();
            ResponseData exeResp = executeChosenCouponPromote(order);
            if (!exeResp.isSuccess()) {
                return exeResp;
            }
            String chosenCoupon = order.getChosenCoupon();
            if (StringUtils.isNotEmpty(chosenCoupon) && saleCouponService.checkExclusiveCoupon(chosenCoupon)) {
                //有免邮促销
                if (checkFreeFeight(order)) {
                    Map variable = new HashMap();
                    Set orderSet = new HashSet();
                    orderSet.add(clone);
                    variable.put("com.hand.hmall.pojo.OrderPojos", orderSet);
                    variable.put("com.hand.hmall.ruleType", "");
                    actionService.freeFreight(variable);
                }
                promoteCaculateService.getRealPrice(clone);

                //计算行basePrice字段替换为实际价格行的订单金额
                clone.computePrice();
                excutePromoteService.useCoupon(clone);
                clone.setExclusive(true);
                clone.setActivities(usefulOrder.getActivities());
                clone.setCouponList(usefulOrder.getCouponList());
                responseData.setResp(Arrays.asList(clone));
                return responseData;
            }

            //设置订单满足的可用促销与优惠券信息
            order.setActivities(usefulOrder.getActivities());
            List<Map> couponList = (List<Map>) usefulOrder.getCouponList();
            order.setCouponList(couponList);
            responseData.setResp(Arrays.asList(order));

            logger.info("----isCaculate Y------" + (System.currentTimeMillis() - start));
            return responseData;
        } else {
            ResponseData responseData = new ResponseData(false, "isCaculate字段为空或值异常");
            responseData.setResp(Arrays.asList(order));
            return responseData;
        }
    }


    /**
     * 执行霸王券 无方法调用
     *
     * @param orderPojo
     * @return
     */
    @Override
    public ResponseData promoteForExclusiveCoupon(OrderPojo orderPojo) {
        String isCalculate = orderPojo.getIsCaculate();
        ResponseData responseData = new ResponseData();
        if ("Y".equals(isCalculate)) {
            String chosenCoupon = orderPojo.getChosenCoupon();
            if (StringUtils.isNotEmpty(chosenCoupon)) {
                //校验选择的优惠券是否为霸王券
                boolean isExclusive = saleCouponService.checkExclusiveCoupon(chosenCoupon);
                //选择的优惠券是霸王券，则只执行霸王券
                if (isExclusive) {
                    if (StringUtils.isNotEmpty(chosenCoupon)) {
                        excutePromoteService.useCoupon(orderPojo);
                        if (!orderPojo.isCheckedCoupon()) {
                            responseData.setSuccess(false);
                            responseData.setMsg("优惠券失效");
                        }
                        //判断是否存在商品层级免邮

                    }

                }
            }
        }

        return null;
    }

    /**
     * 执行选择的促销优惠券
     * @param order
     * @return
     */
    @Override
    public ResponseData executeChosenCouponPromote(OrderPojo order) {
        ResponseData responseData = new ResponseData();
        //校验chosenPromotion字段不为空
        String chosenPromotion = order.getChosenPromotion();
        String chosenCoupon = order.getChosenCoupon();
        //执行用户选择的促销活动
        if (!StringUtils.isEmpty(chosenPromotion)) {

            ResponseData promoteResp = excutePromoteService.payByActivitySingle(chosenPromotion, order);
            if (!order.isCheckedActivity()) {
                responseData.setSuccess(false);
                responseData.setMsg("选择的促销不存在，或已失效");
                responseData.setMsgCode(promoteResp.getMsgCode());
            }
        }

        //校验chosenCoupon不为空，执行用户选择优惠券
        if (!StringUtils.isEmpty(chosenCoupon)) {
            excutePromoteService.useCoupon(order);
            if (!order.isCheckedCoupon()) {
                responseData.setSuccess(false);
                responseData.setMsg("优惠券失效");
            }
        }
        return responseData;
    }

    /**
     * 校验是否存在包邮促销
     *
     * @param orderPojo
     * @return
     */
    boolean checkFreeFeight(OrderPojo orderPojo) {
        List<OrderEntryPojo> entrys = orderPojo.getOrderEntryList();
        OrderEntryPojo entryPojo = entrys.get(0);
        if (entryPojo.getPreInstallationFee() > 0 || entryPojo.getPreShippingFee() > 0) {
            return true;
        } else {
            return false;
        }


    }

    /**
     * @param order
     * @return
     * @throws CloneNotSupportedException
     * @desc 获取订单满足的促销和优惠券
     */
    @Override
    public ResponseData getUsefulPromoteAndCoupon(OrderPojo order) throws CloneNotSupportedException {
        Long start = System.currentTimeMillis();
        OrderPojo usefulPromote = order.clone();

        //查询订单可用促销
        Long startP = System.currentTimeMillis();
        //启用查询可用促销线程
        Future<ResponseData> promote = ThreadFactory.getThreadPool().submit(new GetUsefulPromote(order, CreateJarType.ORIGINAL.toString()));
        //启用查询订单满足的优惠券
        Future<ResponseData> coupon = ThreadFactory.getThreadPool().submit(new GetUsefulConpou(order));

        ResponseData promoteResponseData;
        try {
            promoteResponseData = promote.get();
        } catch (Exception e) {
            logger.error("getPromote error", e);
            promoteResponseData = new ResponseData(false);
        }
        //获取可用促销活动返回列表
        List orderPromoteList = promoteResponseData.getResp();
        if (!CollectionUtils.isEmpty(orderPromoteList)) {
            List activityList = new ArrayList();
            for (int i = 0; i < orderPromoteList.size(); i++) {
                OrderPojo orderPromote = (OrderPojo) orderPromoteList.get(i);
                if (!CollectionUtils.isEmpty(orderPromote.getActivities())) {
                    activityList.addAll(orderPromote.getActivities());
                }
            }
            usefulPromote.setActivities(activityList);
        }
        logger.info("----查询可用促销------" + (System.currentTimeMillis() - startP));
        //查询可用优惠券信息
        Long startC = System.currentTimeMillis();
        ResponseData couponResponseData = null;
        try {
            couponResponseData = coupon.get();
        } catch (Exception e) {
            couponResponseData = new ResponseData(false);
        }
        List orderCouponList = couponResponseData.getResp();
        List usefulCouponList = new ArrayList();
        if (!CollectionUtils.isEmpty(orderCouponList)) {
            for (int i = 0; i < orderCouponList.size(); i++) {
                OrderPojo orderCoupon = (OrderPojo) orderCouponList.get(i);
                if (!CollectionUtils.isEmpty(orderCoupon.getCouponList())) {
                    usefulCouponList.addAll(orderCoupon.getCouponList());
                }
            }
        }
        usefulPromote.setCouponList(usefulCouponList);
        logger.info("----促销可用优惠券-----" + (System.currentTimeMillis() - startC));
        logger.info("----getUsefulPromoteAndCoupon-----" + (System.currentTimeMillis() - start));
        return new ResponseData(Arrays.asList(usefulPromote));
    }


    /**
     * 订单行促销线程
     */
    class OrderEntryPromote implements Callable {

        private OrderPojo orderPojo;

        public OrderEntryPromote(OrderPojo orderPojo) {
            this.orderPojo = orderPojo;
        }

        @Override
        public Object call() throws Exception {
            excutePromoteService.orderEntryPromote(orderPojo);
            return null;
        }
    }


    //查询用户可用促销
    class GetUsefulPromote implements Callable<ResponseData> {

        private OrderPojo orderPojo;

        private String type;

        public GetUsefulPromote(OrderPojo order, String type) {
            this.orderPojo = order;
            this.type = type;
        }

        @Override
        public ResponseData call() throws Exception {
            logger.info("start GetUsefulPromote");
            return excutePromoteService.payByActivity(orderPojo, type);
        }
    }

    //查询用户可用优惠券
    class GetUsefulConpou implements Callable<ResponseData> {

        private OrderPojo orderPojo;


        public GetUsefulConpou(OrderPojo order) {
            this.orderPojo = order;
        }

        @Override
        public ResponseData call() throws Exception {
            return excutePromoteService.optionCouponWithoutActivity(orderPojo);
        }
    }
}
