package com.hand.hmall.service.impl;

import com.hand.hmall.client.IPdClientService;
import com.hand.hmall.client.IProductClientService;
import com.hand.hmall.dao.ActionDataDao;
import com.hand.hmall.dao.SaleActivityDao;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.mapper.ProductInvInfoMapper;
import com.hand.hmall.model.ProductInvInfo;
import com.hand.hmall.pojo.OrderEntryPojo;
import com.hand.hmall.pojo.OrderPojo;
import com.hand.hmall.service.IActionService;
import com.hand.hmall.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.*;


/**
 * 促销结果处理类，对订单执行优惠操作
 */
@Service
public class ActionServiceImpl implements IActionService {

    @Autowired
    private ActionDataDao actionDataDao;

    @Autowired
    private SaleActivityDao saleActivityDao;
    @Autowired
    private IProductClientService productClientService;
    @Autowired
    private IPdClientService pdClientService;

    @Resource
    private ProductInvInfoMapper productInvInfoMapper;
    //固定减价
    private static final String FIX_DISCOUNT = "fixDiscount";
    //固定折扣
    private static final String RATE_DISCOUNT = "rateDiscount";
    //固定价格
    private static final String FIX_PRICE = "fixPrice";
    //所有类型
    private static final String[] ACTIVITY_TYPE = {FIX_PRICE, FIX_DISCOUNT, RATE_DISCOUNT};

//    private AtomicDouble signleDiscountFee = new AtomicDouble(0.0);
//
//    private AtomicDouble signleCouponFee = new AtomicDouble(0.0);
//
//    private List<Map> gifts = Collections.synchronizedList(new ArrayList());

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 订单减元 用于促销活动与优惠券
     *
     * @param variables  数据模型（订单、商品、responseData）
     * @param actionData 简单类型的前台数据
     */
    @Override
    public void orderDiscount(Map variables, double actionData) {
        if (null != variables.get("com.hand.hmall.pojo.OrderPojos")) {
            String ruleType = variables.get("com.hand.hmall.ruleType").toString();
            double signleDiscountFee = 0.0;
            Set<OrderPojo> orders = (Set<OrderPojo>) variables.get("com.hand.hmall.pojo.OrderPojos");
            for (OrderPojo order : orders) {
                //保留两位小数 优惠券减元
                if (StringUtils.isNotEmpty(ruleType) && "coupon".equals(ruleType.trim())) {
                    order.setCouponFee(order.getCouponFee() + actionData);
                    if (order.getCouponFee() > order.getNetAmount()) {
                        order.setCouponFee(order.getNetAmount());
                    }
                } else {
                    //促销活动减圆
                    order.setDiscountFee(order.getDiscountFee() + actionData);
                    signleDiscountFee = actionData;
                    if (order.getDiscountFee() > order.getNetAmount()) {
                        order.setDiscountFee(order.getNetAmount());
                        signleDiscountFee = order.getNetAmount();
                    }
                    variables.put("discountFee", signleDiscountFee);

                    ResponseData responseData = (ResponseData) variables.get("com.hand.hmall.dto.ResponseData");
                    responseData.setSuccess(true);
                }
                order.computePrice();

            }
        }
    }

    /**
     * 订单行减元
     *
     * @param variables  数据模型（订单、商品、responseData）
     * @param actionData 简单类型的前台数据
     */
    @Override
    public void orderEntryDiscount(Map variables, double actionData) {
        logger.info("-----------------------计算订单行优惠-----------------");
        if (null != variables.get("com.hand.hmall.pojo.OrderEntryPojos")) {
            Set<OrderEntryPojo> orderEntrys = (Set<OrderEntryPojo>) variables.get("com.hand.hmall.pojo.OrderEntryPojos");
            DecimalFormat df = new DecimalFormat("#.00");
            double signleDiscountFee = 0.0;
            for (OrderEntryPojo orderEntry : orderEntrys) {
                //保留两位小数
                double totalFee = orderEntry.getTotalFee();
                if (orderEntry.getTotalFee() < 0) {
                    actionData = totalFee;
                }
                setEntryDiscount(variables, actionData, orderEntry);
            }
            dealRespForPromote(variables);
        }
    }

    /**
     * 订单每满X元 减元、打折
     *
     * @param variables  数据模型（订单、商品、responseData）
     * @param activityId 根据促销的id查询出复杂的数据，然后解析
     * @param type       类型（减元或者打折）
     */
    @Override
    public void orderMeetDiscount(Map variables, String activityId, String type) {
        if (null != variables.get("com.hand.hmall.pojo.OrderPojos") && Arrays.asList(ACTIVITY_TYPE).contains(type)) {
            Set<OrderPojo> orders = (Set<OrderPojo>) variables.get("com.hand.hmall.pojo.OrderPojos");
            Map<String, Object> query = new HashMap();
            query.put("activityId", activityId);
            if (type.equals(FIX_DISCOUNT)) {
                //固定减价
                query.put("definitionId", "o_meet_delete");
            }
            Map actionData = (Map) actionDataDao.selectByMutilEqField(query).get(0);
            for (OrderPojo order : orders) {
                Map<String, Map> parameters = (Map<String, Map>) actionData.get("parameters");
                Map valueMap = parameters.get("value");
                Double front = Double.valueOf(valueMap.get("front").toString());
                Double value = Double.valueOf(valueMap.get("value").toString());
                //取减元的次数
                double times = Math.floor((order.getOrderAmount() - order.getDiscountFee()) / front);
                double signleCouponFee = 0.0;
                double signleDiscountFee = 0.0;
                if (type.equals(FIX_DISCOUNT)) {
                    String ruleType = variables.get("com.hand.hmall.ruleType").toString();
                    if (StringUtils.isNotEmpty(ruleType) && "coupon".equals(ruleType.trim())) {
                        order.setCouponFee(order.getCouponFee() + value * times);
                        signleCouponFee = value * times;
                    } else {
                        signleDiscountFee = (value * times);
                        variables.put("discountFee", signleDiscountFee);
                        order.setDiscountFee(order.getDiscountFee() + value * times);
                        ResponseData responseData = (ResponseData) variables.get("com.hand.hmall.dto.ResponseData");
                        responseData.setSuccess(true);
                    }
                }
                order.computePrice();
            }
            String ruleType = variables.get("com.hand.hmall.ruleType").toString();
            if (StringUtils.isNotEmpty(ruleType) && !"coupon".equals(ruleType.trim())) {
                ResponseData responseData = (ResponseData) variables.get("com.hand.hmall.dto.ResponseData");
                responseData.setSuccess(true);
            }

        }
    }

    /**
     * 订单行每满X元 减元、打折
     *
     * @param variables  数据模型（订单、商品、responseData）
     * @param activityId 根据促销的id查询出复杂的数据，然后解析
     * @param type       类型（减元或者打折）
     */
    @Override
    public void orderEntryMeetDiscount(Map variables, String activityId, String type) {
        if (null != variables.get("com.hand.hmall.pojo.OrderEntryPojos") && Arrays.asList(ACTIVITY_TYPE).contains(type)) {
            Set<OrderEntryPojo> orderEntrys = (Set<OrderEntryPojo>) variables.get("com.hand.hmall.pojo.OrderEntryPojos");
            Map<String, Object> query = new HashMap();
            query.put("activityId", activityId);
            if (type.equals(FIX_DISCOUNT)) {
                //固定减价
                query.put("definitionId", "oe_meet_delete");
            }
            Map actionData = (Map) actionDataDao.selectByMutilEqField(query).get(0);
            for (OrderEntryPojo orderEntry : orderEntrys) {
                Map<String, Map> parameters = (Map<String, Map>) actionData.get("parameters");
                Map valueMap = parameters.get("value");
                Double front = Double.valueOf(valueMap.get("front").toString());
                Double value = Double.valueOf(valueMap.get("value").toString());
                //取减元的次数
                double times = Math.floor((Double.valueOf(orderEntry.getTotalFee()) - Double.valueOf(orderEntry.getDiscountFee())) / front);

                if (type.equals(FIX_DISCOUNT)) {
                    orderEntry.setDiscountFee(orderEntry.getDiscountFee() + value * times);
                }
                orderEntry.caculate();
            }

            String ruleType = variables.get("com.hand.hmall.ruleType").toString();
            if (StringUtils.isNotEmpty(ruleType) && !"coupon".equals(ruleType.trim())) {
                ResponseData responseData = (ResponseData) variables.get("com.hand.hmall.dto.ResponseData");
                responseData.setSuccess(true);
            }
        }
    }

    /**
     * 订单打折
     *
     * @param variables  数据模型（订单、商品、responseData）
     * @param actionData 简单类型的前台数据
     */
    @Override
    public void orderPercentageDiscount(Map variables, double actionData) {
        if (null != variables.get("com.hand.hmall.pojo.OrderPojos")) {
            Set<OrderPojo> orders = (Set<OrderPojo>) variables.get("com.hand.hmall.pojo.OrderPojos");
            Double signleCouponFee = 0.0;
            double signleDiscountFee = 0.0;
            for (OrderPojo order : orders) {
                DecimalFormat df = new DecimalFormat("#.00");
                String ruleType = variables.get("com.hand.hmall.ruleType").toString();
                if (StringUtils.isNotEmpty(ruleType) && "coupon".equals(ruleType.trim())) {
                    order.setCouponFee(order.getCouponFee() + order.getNetAmount() * (10.0 - actionData) / 10.0);
                    signleCouponFee = (order.getNetAmount()) * (10.0 - actionData) / 10.0;
                } else {
                    order.setDiscountFee(Double.valueOf(NumFormatUtil.format((order.getNetAmount()) * (10.0 - actionData) / 10.0 + order.getDiscountFee())));
                    signleDiscountFee = Double.parseDouble(NumFormatUtil.format((order.getNetAmount()) * (10.0 - actionData) / 10.0));
                    ResponseData responseData = (ResponseData) variables.get("com.hand.hmall.dto.ResponseData");
                    responseData.setSuccess(true);
                }

            }
            variables.put("discountFee", signleDiscountFee);
        }
    }

    /**
     * 订单行打折
     *
     * @param variables  数据模型（订单、商品、responseData）
     * @param actionData 简单类型的前台数据
     */
    @Override
    public void orderEntryPercentageDiscount(Map variables, double actionData) {
        if (null != variables.get("com.hand.hmall.pojo.OrderEntryPojos")) {
            Set<OrderEntryPojo> orderEntrys = (Set<OrderEntryPojo>) variables.get("com.hand.hmall.pojo.OrderEntryPojos");
            Double realRate = ArithUtil.div(ArithUtil.sub(10, actionData), 10);
            for (OrderEntryPojo orderEntry : orderEntrys) {
                double discountFee = ArithUtil.mul(orderEntry.getTotalFee(), realRate);
                setEntryDiscount(variables, discountFee, orderEntry);
                orderEntry.caculate();
            }
            dealRespForPromote(variables);
        }
    }


    /**
     * 免邮 将订单、订单行的运费安装费置为0，减免金额置为原运费安装费 。
     *
     * @param variables 数据模型（订单、商品、responseData）
     */
    @Override
    public void checkedCoupon(Map variables) {
        if (null != variables.get("com.hand.hmall.pojo.OrderPojos")) {
            Set<OrderPojo> orders = (Set<OrderPojo>) variables.get("com.hand.hmall.pojo.OrderPojos");
            for (OrderPojo order : orders) {
                order.setCheckedCoupon(true);
            }
        }
    }

    @Override
    public void orderExempt(Map variables, Integer number) {
        try {

            if (null != variables.get("com.hand.hmall.pojo.OrderPojos")) {
                Set<OrderPojo> orders = (Set<OrderPojo>) variables.get("com.hand.hmall.pojo.OrderPojos");
                if (null != variables.get("com.hand.hmall.pojo.OrderEntryPojos")) {
                    List<OrderEntryPojo> products = (List<OrderEntryPojo>) variables.get("com.hand.hmall.pojo.OrderEntryPojos");
                    SortUtil.productsSortByPrice(products, true);
                    for (OrderEntryPojo product : products) {
                        double total = product.getBasePrice() * product.getQuantity();
                        if (number >= product.getQuantity()) {
                            product.setDiscountFee(total);
                            product.caculate();
                            number -= product.getQuantity();
                        } else {
                            double discount = Double.valueOf(product.getBasePrice()) * number;
                            discount += product.getDiscountFee();
                            if (discount > total) {
                                product.setDiscountFee(total);
                            } else {
                                product.setDiscountFee(discount);
                            }
                            product.caculate();
                            number = 0;
                        }
                        if (number == 0) {
                            break;
                        }
                    }
                } else {
                    for (OrderPojo order : orders) {
                        List<OrderEntryPojo> products = order.getOrderEntryList();
                        SortUtil.productsSortByPrice(products, true);
                        if (order.getQuantity() >= number) {
                            for (OrderEntryPojo product : products) {
                                double total = Double.valueOf(product.getBasePrice()) * product.getQuantity();
                                if (number >= product.getQuantity()) {
                                    product.setDiscountFee(total);
                                    product.caculate();
                                    number -= product.getQuantity();
                                } else {
                                    double discount = product.getBasePrice() * number;
                                    discount += product.getDiscountFee();
                                    if (discount > total) {
                                        product.setDiscountFee(total);
                                    } else {
                                        product.setDiscountFee(discount);
                                    }
                                    product.caculate();
                                    number = 0;
                                }
                                if (number == 0) {
                                    break;
                                }
                            }
                        } else {
                            for (OrderEntryPojo product : products) {
                                double total = product.getBasePrice() * product.getQuantity();
                                product.setDiscountFee(total);
                                product.caculate();

                            }
                        }
                    }

                }
                for (OrderPojo order : orders) {
                    order.computePrice();
                }

            }
            String ruleType = variables.get("com.hand.hmall.ruleType").toString();
            if (StringUtils.isNotEmpty(ruleType) && "coupon".equals(ruleType.trim())) {
                ResponseData responseData = (ResponseData) variables.get("com.hand.hmall.dto.ResponseData");
                responseData.setSuccess(true);
            }
        } catch (Exception e) {
            logger.error("免单X减执行异常", e);
        }
    }

    /**
     * 是否执行了促销优惠逻辑
     *
     * @param variables 数据模型（订单、商品、responseData）
     */
    @Override
    public void checkedActivity(Map variables, String ruleNum) {
        Set<OrderPojo> orders = (Set<OrderPojo>) variables.get("com.hand.hmall.pojo.OrderPojos");
        Set<OrderEntryPojo> orderEntrys = new HashSet<>();
        Map saleActivity = saleActivityDao.select(ruleNum);
        Double signleDiscountFee = (Double) variables.get("discountFee");
        List<Map<String, ?>> gifts = (List) variables.get("gifts");
        String type = (String) saleActivity.get("type");
        Collection varEntrySet = variables.values();
        //遍历出参与促销的订单行
        for (Object varEntry : varEntrySet) {
            if (varEntry instanceof Set) {
                Set oset = (Set) varEntry;
                for (Object o1 : oset) {
                    if (o1 instanceof OrderEntryPojo)
                        orderEntrys.add((OrderEntryPojo) o1);
                }
            }
        }
        //为行或头添加促销信息，2对应订单行，1对应订单头
        switch (Integer.parseInt(type)) {
            case 2:
                if (CollectionUtils.isNotEmpty(orderEntrys)) {
                    for (OrderEntryPojo orderEntryPojo : orderEntrys) {
                        Map<String, Object> activity = new HashedMap();
                        //添加该促销的id
                        activity.put("activityName", saleActivity.get("activityName"));
                        activity.put("activityId", ruleNum);
                        activity.put("startDate", DateUtil.formMillstToDate(saleActivity.get("startDate").toString().trim(), "yyyy/MM/dd HH:mm:ss"));
                        activity.put("releaseId", saleActivity.get("releaseId"));
                        activity.put("endDate", DateUtil.formMillstToDate(saleActivity.get("endDate").toString().trim(), "yyyy/MM/dd HH:mm:ss"));
                        activity.put("activityDes", saleActivity.get("activityDes"));
                        activity.put("pageShowMes", saleActivity.get("pageShowMes"));
                        activity.put("type", type);
                        List activityList = orderEntryPojo.getActivitie();
                        if (CollectionUtils.isEmpty(activityList))
                            activityList = new ArrayList();
                        activityList.add(activity);
                        orderEntryPojo.setActivitie(activityList);
                    }
                    if (CollectionUtils.isNotEmpty(orders)) {
                        orders.forEach(o -> o.setCheckedActivity(true));
                    }
                }
                break;
            case 1:
                if (CollectionUtils.isNotEmpty(orders)) {
                    for (OrderPojo order : orders) {

                        Map<String, Object> activity = new HashedMap();
                        //添加该促销的id
                        activity.put("activityName", saleActivity.get("activityName"));
                        activity.put("activityId", ruleNum);
                        activity.put("startDate", DateUtil.formMillstToDate(saleActivity.get("startDate").toString().trim(), "yyyy/MM/dd HH:mm:ss"));
                        activity.put("releaseId", saleActivity.get("releaseId"));
                        activity.put("endDate", DateUtil.formMillstToDate(saleActivity.get("endDate").toString().trim(), "yyyy/MM/dd HH:mm:ss"));
                        activity.put("activityDes", saleActivity.get("activityDes"));
                        activity.put("pageShowMes", saleActivity.get("pageShowMes"));
                        activity.put("type", type);
                        activity.put("amount", signleDiscountFee);
                        List giftList = new ArrayList();
                        Integer giftQuantity = 0;
                        if (CollectionUtils.isNotEmpty(gifts)) {
                            for (Map<String, ?> gift : gifts) {
                                giftList.add(gift.get("product"));
                                giftQuantity += (Integer) gift.get("quantity");
                            }
                        }

                        activity.put("gift", giftList);
                        activity.put("giftQuantity", giftQuantity);
                        List activityList = order.getActivities();
                        if (CollectionUtils.isEmpty(activityList))
                            activityList = new ArrayList();
                        activityList.add(activity);
                        order.setActivities(activityList);
                        //是否使用了促销规则
                        order.setCheckedActivity(true);

                    }
                }
        }


    }

    /**
     * 抢购，针对前X件商品打折或者减价
     *
     * @param variables  数据模型（订单、商品、responseData）
     * @param activityId 根据促销的id查询出复杂的数据，然后解析
     * @param type       抢购的优惠的类型（打折和减元）
     */
    @Override
    public void scareBuying(Map variables, String activityId, String type) {
        if (Arrays.asList(ACTIVITY_TYPE).contains(type)) {
            Integer total = 0;
            Map<String, Object> query = new HashMap();
            query.put("activityId", activityId);
            if (type.equals(FIX_DISCOUNT)) {
                //固定减价
                query.put("definitionId", "o_front_delete");
            } else if (type.equals(RATE_DISCOUNT)) {
                //固定折扣
                query.put("definitionId", "o_front_rate");
            }
            Map<String, Map<String, Map>> actionData = (Map<String, Map<String, Map>>) actionDataDao.selectByMutilEqField(query).get(0);
            if (null != variables.get("com.hand.hmall.pojo.OrderEntryPojos")) {
                Set<OrderEntryPojo> products = (Set<OrderEntryPojo>) variables.get("com.hand.hmall.pojo.OrderEntryPojos");
                for (OrderEntryPojo p : products) {
                    total += p.getQuantity();
                }
                Map<String, Map> parameters = actionData.get("parameters");
                Map valueMap = parameters.get("value");
                //剩余量
                Integer remaining = 0;
                if (!valueMap.containsKey("remaining")) {
                    remaining = (Integer) valueMap.get("front");
                    valueMap.put("remaining", remaining);
                } else {
                    remaining = (Integer) valueMap.get("remaining");
                }
                Double value = Double.valueOf(valueMap.get("value").toString());

            }
            if (null != variables.get("com.hand.hmall.pojo.OrderPojos")) {
                Set<OrderPojo> orders = (Set<OrderPojo>) variables.get("com.hand.hmall.pojo.OrderPojos");
                for (OrderPojo order : orders) {
                    //计算行上的总优惠
//                    order.computeOrderRowDiscount();
                    //计算整个订单
                    order.computePrice();

                    if (order.getIsPay() != null && order.getIsPay().equals("Y")) {
                        actionDataDao.update(actionData);
                    }
                }
            }
        }
        String ruleType = variables.get("com.hand.hmall.ruleType").toString();
        if (StringUtils.isNotEmpty(ruleType) && "coupon".equals(ruleType.trim())) {
            ResponseData responseData = (ResponseData) variables.get("com.hand.hmall.dto.ResponseData");
            responseData.setSuccess(true);
        }
    }

    @Override
    public void freeFreight(Map variables) {
        if (null != variables.get("com.hand.hmall.pojo.OrderPojos")) {
            Set<OrderPojo> orders = (Set<OrderPojo>) variables.get("com.hand.hmall.pojo.OrderPojos");
            for (OrderPojo order : orders) {
                order.setEpostReduce(order.getEpostFee());
                order.setPostReduce(order.getPostFee());
                order.setFixReduce(order.getFixFee());
                order.setFixFee(0.0);
                order.setEpostFee(0.00);
                order.setPostFee(0.0);
                order.computePrice();
                List<OrderEntryPojo> orderEntryList = order.getOrderEntryList();
                orderEntryList.forEach(o -> {
                    o.setPreShippingFee(o.getShippingFee());
                    o.setShippingFee(0.0);
                    o.setPreInstallationFee(o.getInstallationFee());
                    o.setInstallationFee(0.0);
                });
            }
        }

        String ruleType = variables.get("com.hand.hmall.ruleType").toString();
        if (StringUtils.isNotEmpty(ruleType) && !"coupon".equals(ruleType.trim())) {
            ResponseData responseData = (ResponseData) variables.get("com.hand.hmall.dto.ResponseData");
            responseData.setSuccess(true);
        }
    }

    /**
     * 赠送优惠券促销 暂未使用
     *
     * @param variables  数据模型（订单、商品、responseData）
     * @param activityId 根据促销的id查询出复杂的数据，然后解析
     */
    @Override
    public void sendCoupon(Map variables, String activityId) {
        if (null != variables.get("com.hand.hmall.pojo.OrderPojos")) {
            List<Map> couponList = new ArrayList<>();
            ResponseData responseData = (ResponseData) variables.get("com.hand.hmall.dto.ResponseData");
            Set<OrderPojo> orders = (Set<OrderPojo>) variables.get("com.hand.hmall.pojo.OrderPojos");
            try {
                Map<String, Object> query = new HashMap();
                query.put("activityId", activityId);
                query.put("definitionId", "o_giver_coupon");
                Map<String, Map<String, Object>> actionData = (Map<String, Map<String, Object>>) actionDataDao.selectByMutilEqField(query).get(0);
                Map<String, Object> parameters = actionData.get("parameters");
                Map<String, Object> value = (Map<String, Object>) parameters.get("value");
                couponList = (List<Map>) value.get("value");
            } catch (Exception e) {
                responseData.setSuccess(false);
                responseData.setMsgCode("RULES_ERROR");
                responseData.setMsg("规则 : " + activityId + "  错误");
                logger.error("规则 : " + activityId + "  错误", e);
            }
            /*for (OrderPojo order : orders) {
                String tempId = order.getTempId();
                //下单
                if (order.getIsPay() != null && order.getIsPay().equals("Y")) {
                    Map<String, Object> convertMap = new HashMap();
                    convertMap.put("tempId", tempId);
                    convertMap.put("userId", order.getUserId());
                    convertMap.put("couponList", couponList);
                    //扣除发放的优惠券的数量，支付后再发放到用户账户下
                    ResponseData response = promoteClientService.minusCouponCount(convertMap);
                    if (!response.isSuccess()) {
                        responseData.setSuccess(response.isSuccess());
                        responseData.setMsg("优惠券发放失败;\n");
                        responseData.setMsgCode(response.getMsgCode());
                    }
                }
            }*/
        }
        String ruleType = variables.get("com.hand.hmall.ruleType").toString();
        if (StringUtils.isNotEmpty(ruleType) && !"coupon".equals(ruleType.trim())) {
            ResponseData responseData = (ResponseData) variables.get("com.hand.hmall.dto.ResponseData");
            responseData.setSuccess(true);
        }
    }

    @Override
    /**
     * 固定价格、折扣购买商品
     *  @param variables 数据模型（订单、商品、responseData）
     *
     * @param value 简单类型的结果数据
     *
     * @param type 抢购的优惠的类型（打折和固定价格）
     */
    public void productDiscount(Map variables, Double value, String type) {
        if (Arrays.asList(ACTIVITY_TYPE).contains(type)) {
            if (null != variables.get("com.hand.hmall.pojo.OrderEntryPojos")) {
                Set<OrderEntryPojo> set = (Set<OrderEntryPojo>) variables.get("com.hand.hmall.pojo.OrderEntryPojos");
                ArrayList<OrderEntryPojo> entryPojos = new ArrayList<>(set);
                //按单价从高到低顺序排序
                this.productsSortByPrice(entryPojos);
                //保留两位小数
                for (OrderEntryPojo product : entryPojos) {
                    //选择要做执行的 逻辑类型和商品的数量
                    chooseType(type, value, product, product.getQuantity());
                    //计算订单行
//                    product.compute();
                }
            }

            if (null != variables.get("com.hand.hmall.pojo.OrderPojos")) {
                Set<OrderPojo> orders = (Set<OrderPojo>) variables.get("com.hand.hmall.pojo.OrderPojos");
                for (OrderPojo order : orders) {
                    //计算行上的总优惠
//                    order.computeOrderRowDiscount();
                    //计算整个订单
                    order.computePrice();
                }
            }
            String ruleType = variables.get("com.hand.hmall.ruleType").toString();
            if (StringUtils.isNotEmpty(ruleType) && !"coupon".equals(ruleType.trim())) {
                ResponseData responseData = (ResponseData) variables.get("com.hand.hmall.dto.ResponseData");
                responseData.setSuccess(true);
            }
        }
    }

    /**
     * 赠品分为两个部分
     * 1.查询赠送的赠品供给前端选择
     * 2.对赠品进行虚拟库存减少
     *
     * @param variables  数据模型（订单、商品、responseData）
     * @param activityId 根据促销的id查询出复杂的数据，然后解析
     */
    @Override
    public void sendGift(Map variables, String activityId) {
        if (null != variables.get("com.hand.hmall.pojo.OrderPojos")) {
            Set<OrderPojo> orders = (Set) variables.get("com.hand.hmall.pojo.OrderPojos");
            boolean isSucces = false;
//            ResponseData responseData = (ResponseData) variables.get("com.hand.hmall.dto.ResponseData");
            //找到当前定义的虚拟库存以及赠送列表详情
            Map query = new HashMap();
            query.put("activityId", activityId);
            query.put("definitionId", "o_giver_product");
            Map<String, Object> actionData = (Map<String, Object>) actionDataDao.selectByMutilEqField(query).get(0);
            for (OrderPojo order : orders) {
                //查询所有的赠品
                List<Map> actionValue = new ArrayList<>();
                try {
                    Map<String, Object> parameters = (Map<String, Object>) actionData.get("parameters");
                    Map<String, Object> value = (Map<String, Object>) parameters.get("value");
                    actionValue = (List<Map>) value.get("value");
                } catch (ClassCastException e) {
                    logger.error("规则 : " + activityId + "  错误", e);
                }

                //返回的赠品列表
                List<OrderEntryPojo> entryList = order.getOrderEntryList();
                if (null != actionValue && actionValue.size() > 0) {
                    //遍历
                    List<Map> gifts = new ArrayList();
                    for (Integer i = 0; i < actionValue.size(); i++) {
                        String productCode = actionValue.get(i).get("productCode").toString();
                        //赠品的数量
                        Integer quantity = (Integer) actionValue.get(i).get("countNumber");
                        //剩余数量
                        Integer remaining;
                        if (!actionValue.get(i).containsKey("remaining")) {
                            //如果是第一次操作
                            remaining = (Integer) actionValue.get(i).get("totalNumber") - quantity;
                        } else {
                            remaining = (Integer) actionValue.get(i).get("remaining") - quantity;
                        }

                        if (remaining >= 0) {
                            ResponseData productResp = productClientService.selectProductByCode(productCode);
                            List productInfoList = productResp.getResp();

                            for (Object aProductInfoList : productInfoList) {
                                Map product = (Map) aProductInfoList;
                                List<ProductInvInfo> invInfos = productInvInfoMapper.selectByProductCode(productCode);
                                int stock = 0;
                                if (CollectionUtils.isNotEmpty(invInfos)) {
                                    stock = invInfos.stream().mapToInt(ProductInvInfo::getAvailableQuantity).sum();
                                } else {
                                    logger.error("----------赠品库存数据不存在--------------");
                                    break;
                                }

                                if (quantity <= stock) {
                                    Map priceReqMap = new HashMap();
                                    priceReqMap.put("productId", product.get("productId"));
                                    ResponseData priceResp = pdClientService.getProductPrice((Integer) product.get("productId"));
                                    Map gift = new HashMap<>();
                                    gift.put("product", productCode);
                                    gift.put("quantity", quantity);
                                    gift.put("productName", product.get("productName"));
                                    gifts.add(gift);
                                    OrderEntryPojo entryPojo = new OrderEntryPojo();
                                    if (priceResp.isSuccess()) {
                                        List<?> resp = priceResp.getResp();
                                        if (CollectionUtils.isNotEmpty(resp)) {
                                            Map price = (Map) resp.get(0);
                                            entryPojo.setBasePrice((Double) price.get("basePrice"));
                                        }
                                    } else {
                                        entryPojo.setBasePrice(0D);
                                    }
                                    entryPojo.setIsGift("Y");
                                    entryPojo.setProductName((String)product.get("productName"));
                                    entryPojo.setQuantity(quantity);
                                    entryPojo.setVproduct((String) product.get("vProductCode"));
                                    entryPojo.setProduct(productCode);
                                    entryPojo.setShippingType((String) product.get("defaultDelivery"));
                                    entryList.add(entryPojo);
                                    isSucces = true;
                                } else {
                                    logger.error("---------赠品库存不足----------------");
                                }
                            }
                        }
                    }
                    variables.put("gifts", gifts);
                }
                order.setOrderEntryList(entryList);

            }

            String ruleType = variables.get("com.hand.hmall.ruleType").toString();
            if (StringUtils.isNotEmpty(ruleType) && !"coupon".equals(ruleType.trim()) && isSucces) {
                ResponseData responseData = (ResponseData) variables.get("com.hand.hmall.dto.ResponseData");
                responseData.setSuccess(true);
            }
        }

    }


    //获得容器列表中能够满足所有条件的最大优惠次数
    protected Integer getMaxTimes(List<Map> containers, Map variables, Integer maxTimes) {
        for (Map container : containers) {
            String id = container.get("id").toString();
            Integer count = (Integer) container.get("countNumber");
            if (null != variables.get(id)) {
                Set<OrderEntryPojo> list = (Set<OrderEntryPojo>) variables.get(id);
                //获取所有满足条件的订单行
                List<OrderEntryPojo> targetOrderEntryPojos = new ArrayList<>(list);
                Integer quantity = 0;
                //计算商品数量
                for (OrderEntryPojo orderEntryPojo : targetOrderEntryPojos) {
                    quantity += orderEntryPojo.getBundleRemainder();
                }
                if (maxTimes >= quantity / count) {
                    //取最小值作为最大优惠次数
                    maxTimes = quantity / count;
                }
            }
        }
        return maxTimes;
    }

    @Override
    public void useContainer(Map variables, String activityId, String type) {
        ResponseData responseData = (ResponseData) variables.get("com.hand.hmall.dto.ResponseData");
        if (Arrays.asList(ACTIVITY_TYPE).contains(type)) {
            Map<String, Object> query = new HashMap();
            query.put("activityId", activityId);
            if (type.equals(FIX_PRICE)) {
                //固定价格
                query.put("definitionId", "o_fixed_price_buy");
            } else if (type.equals(RATE_DISCOUNT)) {
                //固定折扣
                query.put("definitionId", "o_fixed_rate_buy");
            }
            Map<String, Map> actionData = actionDataDao.selectByQuery(query);
            if (null != actionData) {
                Map parameters = (Map) actionData.get("parameters");
                List<Map> matchContainers = (List<Map>) parameters.get("matchValue");
                List<Map> targetContainers = (List<Map>) parameters.get("targetValue");

                Double value = Double.valueOf(parameters.get("value").toString());

                //“目标容器”的最大优惠次数,初始值为最大的Integer
                Integer targetTimes = Integer.MAX_VALUE;
                targetTimes = getMaxTimes(targetContainers, variables, targetTimes);

                //“符合容器”的最大优惠次数,初始值为最大的Integer
                Integer maxTimes = Integer.MAX_VALUE;
                maxTimes = getMaxTimes(matchContainers, variables, maxTimes);

                if (maxTimes <= 0 || targetTimes < 0) {
                    responseData.setSuccess(false);
                }

                for (Map targetContainer : targetContainers) {
                    String targetId = targetContainer.get("id").toString();
                    Integer targetCount = (Integer) targetContainer.get("countNumber");
                    if (null != variables.get(targetId)) {
                        Set<OrderEntryPojo> set = (Set<OrderEntryPojo>) variables.get(targetId);
                        ArrayList<OrderEntryPojo> targetProducts = new ArrayList<>(set);
                        //对商品按单价价升序排列
                        this.productsSortByPrice(targetProducts);
                        //商品固定价格、折扣逻辑
                        if (maxTimes > targetTimes) {
                            //如果“符合容器”最大优惠次数 > “目标容器”的最大优惠次数
                            for (OrderEntryPojo product : targetProducts) {
                                //选择商品 逻辑类型和商品的数量
                                chooseType(type, value, product, product.getQuantity());
                                product.caculate();
                            }
                        } else {
                            //应该优惠的总件数
                            if (maxTimes > 0) {
                                Integer targetSum = maxTimes * targetCount;
                                for (OrderEntryPojo product : targetProducts) {
                                    if (product.getQuantity() >= targetSum) {
                                        //选择容器逻辑的类型，以及减免商品的数量
                                        chooseType(type, value, product, targetSum);
                                        product.caculate();
                                        targetSum = 0;
                                    } else {
                                        //选择容器逻辑的类型，以及减免商品的数量
                                        chooseType(type, value, product, product.getQuantity());
                                        product.caculate();
                                        targetSum = targetSum - product.getQuantity();
                                    }
                                }
                            } else {
                                responseData.setSuccess(false);
                            }
                        }

                    }
                }
                if (responseData.isSuccess()) {
                    if (null != variables.get("com.hand.hmall.pojo.OrderPojos")) {
                        Set<OrderPojo> orders = (Set<OrderPojo>) variables.get("com.hand.hmall.pojo.OrderPojos");
                        for (OrderPojo order : orders) {
                            //计算整个订单
                            order.computePrice();
                        }
                    }
                }
            }
        }
    }


    /**
     * 计算商品单价优惠，体现在订单行的discountFee字段
     *
     * @param type      优惠类型，固定价格，固定折扣，固定减价
     * @param value     不同优惠类型对应的值
     * @param entryPojo 订单行
     * @param quantity  订单行商品数量
     */
    protected void chooseType(String type, Double value, OrderEntryPojo entryPojo, Integer quantity) {
        Double discountFee = Double.valueOf(entryPojo.getDiscountFee());
        Double price = Double.valueOf(entryPojo.getBasePrice());

        if (type.equals(FIX_PRICE) && price >= value) {
            //固定价格
            entryPojo.setDiscountFee(((price - value) * quantity + discountFee));
        } else if (type.equals(RATE_DISCOUNT) && value <= 10) {
            //固定折扣
            entryPojo.setDiscountFee((((10 - value) / 10) * price * quantity + discountFee));
        } else if (type.equals(FIX_DISCOUNT)) {
            //固定减价
            if (price >= value) {
                entryPojo.setDiscountFee((value * quantity + discountFee));
            } else {
                entryPojo.setDiscountFee((price * quantity));
            }
        }

    }

    //订单中的订单行按单价升序排列
    protected void productsSortByPrice(ArrayList<OrderEntryPojo> entryPojos) {
        Integer i, j;
        Integer length = entryPojos.size();
        //插入排序
        for (i = 1; i < length; i++) {
            j = i;
            OrderEntryPojo entryPojo = entryPojos.get(i);
            while (j > 0 && Double.valueOf(entryPojo.getBasePrice()) < Double.valueOf(entryPojos.get(j - 1).getBasePrice())) {
                entryPojos.set(j, entryPojos.get(j - 1));
                j--;
            }
            entryPojos.set(j, entryPojo);
        }
    }

    @Override
    /**
     * 目标包商品
     *
     *  @param variables 数据模型（订单、商品、responseData）
     *
     * @param activityId 根据促销的id查询出复杂的数据，然后解析
     */
    public void targetPackage(Map variables, String activityId) throws Exception {
        Map<String, Object> query = new HashMap();
        query.put("activityId", activityId);
        Map<String, Map> actionData = actionDataDao.selectByQuery(query);
        if (null != actionData) {
            Map parameters = actionData.get("parameters");
            //获取目标包中的目标容器
            List<Map> targetContainers = (List<Map>) parameters.get("targetValue");
            //获取目标价格
            Double value = Double.valueOf(parameters.get("value").toString());
            ResponseData responseData = (ResponseData) variables.get("com.hand.hmall.dto.ResponseData");

            //“目标容器”的最大优惠次数,初始值为最大的Integer
            Integer targetTimes = Integer.MAX_VALUE;
            //计算“目标容器”的最大优惠次数
            targetTimes = getMaxTimes(targetContainers, variables, targetTimes);
            //最大执行次数为0，促销异常
            if (targetTimes <= 0) {
                responseData.setSuccess(false);
            } else {
                //计算订单行
                this.computeOrderRow(targetContainers, responseData, variables, targetTimes, value);
                responseData.setSuccess(true);
            }

            if (null != variables.get("com.hand.hmall.pojo.OrderPojos")) {
                Set<OrderPojo> orders = (Set<OrderPojo>) variables.get("com.hand.hmall.pojo.OrderPojos");
                for (OrderPojo order : orders) {
                    //计算整个订单
                    order.computePrice();
                }
            }

        }
    }

    /**
     * 加价换购 逻辑暂未使用
     *
     * @param variables
     * @param activityId
     */
    @Override
    public void purchaseOther(Map variables, String activityId) {
        if (null != variables.get("com.hand.hmall.pojo.OrderPojos")) {
            Set<OrderPojo> orders = (Set<OrderPojo>) variables.get("com.hand.hmall.pojo.OrderPojos");
            ResponseData responseData = (ResponseData) variables.get("com.hand.hmall.dto.ResponseData");
            //找到当前定义的换购商品
            Map query = new HashMap();
            query.put("activityId", activityId);
            query.put("definitionId", "o_change_product");
            Map<String, Object> actionData = (Map<String, Object>) actionDataDao.selectByMutilEqField(query).get(0);
            for (OrderPojo order : orders) {
                //查询所有的赠品
                List<Map> actionValue = new ArrayList<>();
                try {
                    Map<String, Object> parameters = (Map<String, Object>) actionData.get("parameters");
                    Map<String, Object> value = (Map<String, Object>) parameters.get("value");
                    actionValue = (List<Map>) value.get("value");
                } catch (ClassCastException e) {
                    responseData.setSuccess(false);
                    responseData.setMsgCode("RULES_ERROR");
                    responseData.setMsg("规则 : " + activityId + "  错误");
                    logger.error("规则 : " + activityId + "  错误", e);
                }

                List<OrderEntryPojo> entryList = order.getOrderEntryList();
                if (null != actionValue && actionValue.size() > 0) {
                    //返回换购商品信息
                    List cheaperList = order.getCheaperList();
                    for (Integer i = 0; i < actionValue.size(); i++) {
                        String productCode = actionValue.get(i).get("productCode").toString();
                        Map cheaperProduct = new HashMap();
                        //商品编码
                        cheaperProduct.put("productCode", productCode);
                        //商品数量
                        cheaperProduct.put("quantity", actionValue.get(i).get("countNumber"));
                        //商品换购价格
                        cheaperProduct.put("payPrice", actionValue.get(i).get("totalPrice"));
                        //活动Id
                        cheaperProduct.put("activityId", activityId);
                        cheaperList.add(cheaperProduct);
                    }
                }
                order.setOrderEntryList(entryList);

            }
        }
    }

    /**
     * 订单阶梯促销
     *
     * @param variables
     * @param activityId
     */
    @Override
    public void laddersDiscount(Map variables, String activityId) {

        if (null != variables.get("com.hand.hmall.pojo.OrderPojos")) {
            Set<OrderPojo> orders = (Set<OrderPojo>) variables.get("com.hand.hmall.pojo.OrderPojos");
            ResponseData responseData = (ResponseData) variables.get("com.hand.hmall.dto.ResponseData");
            //找到当前定义的促销
            Map query = new HashMap();
            query.put("activityId", activityId);
            query.put("definitionId", "o_discount_ladders");
            Map<String, Object> actionData = (Map<String, Object>) actionDataDao.selectByMutilEqField(query).get(0);
            for (OrderPojo order : orders) {
                //查询所有的格式信息
                List<Map<String, Object>> actionValue = new ArrayList<Map<String, Object>>();
                try {
                    Map<String, Object> parameters = (Map<String, Object>) actionData.get("parameters");
                    Map<String, Object> value = (Map<String, Object>) parameters.get("value");
                    actionValue = (List<Map<String, Object>>) value.get("value");
                } catch (ClassCastException e) {
                    responseData.setSuccess(false);
                    responseData.setMsgCode("RULES_ERROR");
                    responseData.setMsg("规则 : " + activityId + "  错误");
                    logger.error("规则 : " + activityId + "  错误", e);
                }
                //订单金额
                Double orderAmount = order.getOrderAmount();
                Double discountMoney = 0.0;
                //阶梯促销逻辑
                for (Map<String, Object> map : actionValue) {

                    double key = Double.parseDouble(map.get("key").toString());
                    double value = Double.parseDouble(map.get("value").toString());
                    if (orderAmount >= key && orderAmount <= ArithUtil.add(key, value)) {
                        discountMoney = ArithUtil.add(discountMoney, ArithUtil.sub(orderAmount, key));
                    }
                    if (orderAmount > ArithUtil.add(key, value)) {
                        discountMoney = ArithUtil.add(discountMoney, value);
                    }
                }
                order.setDiscountFee(discountMoney);
            }
            String ruleType = variables.get("com.hand.hmall.ruleType").toString();
            if (StringUtils.isNotEmpty(ruleType) && !"coupon".equals(ruleType.trim())) {
                responseData.setSuccess(true);
            }
        }
    }

    /**
     * 商品前X件单价固定金额 将满足条件的订单行按商品金额由高到低排序，对金额高的商品固定金额促销
     *
     * @param variables
     * @param activityId
     */
    @Override
    public void productFixDiscount(Map variables, String activityId) {
        if (null != variables.get("com.hand.hmall.pojo.OrderEntryPojos")) {
            Set<OrderEntryPojo> orderEntrys = (Set<OrderEntryPojo>) variables.get("com.hand.hmall.pojo.OrderEntryPojos");
            //获取优惠数据
            Map actionData = getActionData(activityId, "p_number_discount");
            //优惠商品件数
            Integer productNum = (Integer) actionData.get("quantity");
            //商品减免金额
            Double fixPrice = Double.valueOf(actionData.get("value").toString());
            //根据商品单价由高到低排序
            List<OrderEntryPojo> entryList = sortEntryByBasePrice(orderEntrys, false);
            Iterator<OrderEntryPojo> iterator = entryList.iterator();
            //循环终止条件，订单行全部遍历完成或者商品数量为0
            while (iterator.hasNext() && productNum > 0) {
                OrderEntryPojo entryPojo = iterator.next();
                double entryDiscount = 0.0;
                //每一个商品实际折扣金额
                double realDiscount = entryPojo.getBasePrice() - fixPrice;
                //productNum 还有剩余
                if (productNum >= entryPojo.getQuantity()) {
                    entryDiscount = ArithUtil.mul(entryPojo.getQuantity(), realDiscount);
                    productNum = productNum - entryPojo.getQuantity();
                } else {
                    entryDiscount = ArithUtil.mul(productNum, realDiscount);
                    productNum = 0;
                }
                //设置减免金额
                setEntryDiscount(variables, entryDiscount, entryPojo);
            }
            dealRespForPromote(variables);
        }
    }


    /**
     * 前X件商品打Y折 将满足条件的订单行按商品金额由高到低排序，对金额高的商品打折
     *
     * @param variables
     * @param activityId
     */
    @Override
    public void productPercentageDiscount(Map variables, String activityId) {
        if (null != variables.get("com.hand.hmall.pojo.OrderEntryPojos")) {
            //获取满足条件的订单行
            Set<OrderEntryPojo> orderEntrys = (Set<OrderEntryPojo>) variables.get("com.hand.hmall.pojo.OrderEntryPojos");
            //获取优惠数据
            Map actionData = getActionData(activityId, "p_number_rate");
            //要打折的商品数量
            Integer productNum = (Integer) actionData.get("quantity");
            //折扣
            Double rate = Double.valueOf(actionData.get("value").toString());
            //根据商品单价由高到低排序
            List<OrderEntryPojo> entryList = sortEntryByBasePrice(orderEntrys, false);
            Double realRate = ArithUtil.div(ArithUtil.sub(10, rate), 10);

            //循环终止条件，订单行全部遍历完成或者商品数量为0
            Iterator<OrderEntryPojo> iterator = entryList.iterator();
            while (iterator.hasNext() && productNum > 0) {
                OrderEntryPojo entryPojo = iterator.next();
                double entryDiscount = 0.0;
                //比较优惠商品件数与订单行数量，获取订单行要执行优惠的商品数量
                if (productNum >= entryPojo.getQuantity()) {
                    entryDiscount = ArithUtil.mul(ArithUtil.mul(entryPojo.getQuantity(), entryPojo.getBasePrice()), realRate);
                    productNum = productNum - entryPojo.getQuantity();
                } else {
                    entryDiscount = ArithUtil.mul(ArithUtil.mul(productNum, entryPojo.getBasePrice()), realRate);
                    productNum = 0;
                }
                //判断执行的是优惠券还是促销活动
                setEntryDiscount(variables, entryDiscount, entryPojo);
            }
            dealRespForPromote(variables);
        }
    }


    //计算目标包每个订单行
    protected void computeOrderRow(List<Map> containers, ResponseData responseData,Map variables, Integer maxTimes, Double value) throws Exception {
        Double total = 0.0;
        //所有优惠商品的总价
        Double orderEntrysAccount = 0.0;
        boolean caculateFlag = false;
        //计算参与到套装优惠的所有订单行的商品总金额
        for (Map container : containers) {
            String id = container.get("id").toString();
            //订单行能优惠的总件数
            Integer targetQuantity = maxTimes * (Integer) container.get("countNumber");
            if (null != variables.get(id)) {
                Set<OrderEntryPojo> set = (Set<OrderEntryPojo>) variables.get(id);
                //把商品列表转成数组列表
                ArrayList<OrderEntryPojo> targetOrderEntryPojos = new ArrayList<>(set);
                //按单价升序排列
                this.productsSortByPrice(targetOrderEntryPojos);
                for (OrderEntryPojo orderEntry : targetOrderEntryPojos) {
                    total += orderEntry.getQuantity() * Double.valueOf(orderEntry.getBasePrice());
                    //如果该类商品达到优惠总件数，则用总件数计算，优惠总件数清0，否则该订单行全部优惠，优惠总件数件数减少该商品的总件数
                    //如果订单行能参与参与捆绑促销的次数小于0，不执行本次促销
                    if (orderEntry.getBundleRemainder() > 0) {
                        if (targetQuantity >= orderEntry.getBundleRemainder()) {
                            orderEntrysAccount += orderEntry.getBundleRemainder() * Double.valueOf(orderEntry.getBasePrice());
                            targetQuantity = targetQuantity - orderEntry.getBundleRemainder();
                            orderEntry.setBundleRemainder(0);
                        } else {
                            orderEntrysAccount += targetQuantity * Double.valueOf(orderEntry.getBasePrice());
                            orderEntry.setBundleRemainder(orderEntry.getBundleRemainder() - targetQuantity);
                            targetQuantity = 0;
                        }
                    } else {
                        responseData.setSuccess(false);
                        return;
                    }
                }
            }
        }

        //总优惠金额=上一步的商品总金额-目标包价格*优惠次数
        Double discount = orderEntrysAccount - value * maxTimes;
        //累加每一行的优惠金额，最后一行的优惠金额为 总金额 - 前i-1行累加金额
        Double sumDiscount = 0.0;
        for (Integer i = 0; i <= containers.size() - 1; i++) {
            String id = containers.get(i).get("id").toString();
            //优惠的总件数
            if (null != variables.get(id)) {
                Set<OrderEntryPojo> set = (Set<OrderEntryPojo>) variables.get(id);
                ArrayList<OrderEntryPojo> targetOrderEntryPojos = new ArrayList<>(set);
                //按原价升序排列
                this.productsSortByPrice(targetOrderEntryPojos);
                for (Integer j = 0; j <= targetOrderEntryPojos.size() - 1; j++) {
                    Double rowTotal = targetOrderEntryPojos.get(j).getQuantity() * Double.valueOf(targetOrderEntryPojos.get(j).getBasePrice());
                    Double rowDiscount = DoubleStringUtil.toDoubleTwoBit(discount * rowTotal / total);
                    OrderEntryPojo targetEntry = targetOrderEntryPojos.get(j);
                    if (i == containers.size() - 1 && j == targetOrderEntryPojos.size() - 1) {
                        //计算最后一个订单行的优惠金额
                        targetEntry.setDiscountFee(discount - sumDiscount + targetEntry.getDiscountFee());
                    } else {
                        targetEntry.setDiscountFee(rowDiscount + targetEntry.getDiscountFee());
                    }
                    sumDiscount += rowDiscount;
                    targetOrderEntryPojos.get(j).caculate();
                }
            }
        }
        responseData.setSuccess(true);
    }

    /**
     * 对订单行根据double型字段进行排序
     *
     * @param entrySet 要排序的订单行
     * @param isAsc    是否降序
     */
    protected List<OrderEntryPojo> sortEntryByBasePrice(Set<OrderEntryPojo> entrySet, boolean isAsc) {

        List entryList = entrySetToList(entrySet);
        //降序
        if (!isAsc) {
            entryList.sort(Comparator.comparing((OrderEntryPojo entry) -> {
                return entry.getBasePrice();
            }).reversed());
        } else {
            entryList.sort(Comparator.comparing((OrderEntryPojo entry) -> {
                return entry.getBasePrice();
            }));
        }
        return entryList;
    }

    /**
     * 将Set转换为List
     *
     * @param entryPojoSet
     * @return
     */
    protected List<OrderEntryPojo> entrySetToList(Set<OrderEntryPojo> entryPojoSet) {
        List entryList = new ArrayList();
        Iterator<OrderEntryPojo> iterator = entryPojoSet.iterator();
        while (iterator.hasNext()) {
            entryList.add(iterator.next());
        }
        return entryList;
    }

    /**
     * 获取订单行集合中商品数量。
     *
     * @param entrySet
     * @return
     */
    protected int getProductNum(Set entrySet) {
        Integer productNum = 0;
        Iterator<OrderEntryPojo> iterator = entrySet.iterator();
        while (iterator.hasNext()) {
            productNum += iterator.next().getQuantity();
        }
        return productNum;
    }

    /**
     * 设置促销执行成功标识为 true
     *
     * @param variables
     */
    protected void dealRespForPromote(Map variables) {
        String ruleType = variables.get("com.hand.hmall.ruleType").toString();
        if (StringUtils.isNotEmpty(ruleType) && !"coupon".equals(ruleType.trim())) {
            ResponseData responseData = (ResponseData) variables.get("com.hand.hmall.dto.ResponseData");
            responseData.setSuccess(true);
        }
    }

    /**
     * 获取actionData（优惠数据）数据
     *
     * @param activityId
     * @param definitionId
     * @return
     */
    protected Map getActionData(String activityId, String definitionId) {
        Map<String, Object> query = new HashMap();
        query.put("activityId", activityId);
        query.put("definitionId", definitionId);
        List<Map<String, ?>> maps = actionDataDao.selectByMutilEqField(query);
        if (CollectionUtils.isNotEmpty(maps)) {
            Map actionData = actionDataDao.selectByMutilEqField(query).get(0);
            Map parm = (Map) actionData.get("parameters");
            Map value = (Map) parm.get("value");
            return value;
        } else {
            throw new RuntimeException("【促销对应的actionData（优惠数据）不存在】");
        }

    }

    /**
     * 校验执行的规则是促销活动还是优惠券
     *
     * @param variables
     * @return
     */
    protected boolean isCoupon(Map variables) {
        String ruleType = variables.get("com.hand.hmall.ruleType").toString();
        if ("coupon".equals(ruleType.trim())) {
            return true;
        } else {
            return false;
        }
    }

    protected void setEntryDiscount(Map variables, Double entryDiscount, OrderEntryPojo entryPojo) {
        if (isCoupon(variables)) {
            Set<OrderPojo> orders = (Set<OrderPojo>) variables.get("com.hand.hmall.pojo.OrderPojos");
            entryDiscount = ArithUtil.add(entryDiscount, entryPojo.getCouponFee());
            entryPojo.setCouponFee(entryDiscount);
        } else {
            entryDiscount = ArithUtil.add(entryDiscount, entryPojo.getDiscountFee());
            entryPojo.setDiscountFee(entryDiscount);
        }
    }
}
