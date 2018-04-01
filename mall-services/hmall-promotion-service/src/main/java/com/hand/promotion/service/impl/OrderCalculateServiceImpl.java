package com.hand.promotion.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.hand.hmall.dto.ResponseData;
import com.hand.promotion.client.IProductClientService;
import com.hand.promotion.dto.CategoryMapping;
import com.hand.promotion.pojo.SimpleMessagePojo;
import com.hand.promotion.pojo.enums.MsgMenu;
import com.hand.promotion.pojo.enums.PromotionConstants;
import com.hand.promotion.pojo.order.GiftPojo;
import com.hand.promotion.pojo.order.MallEntryPojo;
import com.hand.promotion.pojo.order.MallPromotionResult;
import com.hand.promotion.pojo.order.OrderEntryPojo;
import com.hand.promotion.pojo.order.OrderMatchedProductInfoPojo;
import com.hand.promotion.pojo.order.OrderPojo;
import com.hand.promotion.service.ICategoryMappingService;
import com.hand.promotion.service.IOrderCalculateService;
import com.hand.promotion.util.DateFormatUtil;
import com.hand.promotion.util.DecimalCalculate;
import com.hand.promotion.util.ThreadPoolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2017/12/27
 * @description 订单促销计算过程中用于处理金额计算，条件数据汇总
 */
@Service
public class OrderCalculateServiceImpl implements IOrderCalculateService {

    @Autowired
    private IProductClientService productClientService;

    @Autowired
    private ICategoryMappingService categoryMappingService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * 校验进行促销计算的订单数据是否合法
     *
     * @param orderPojo
     * @return
     */
    @Override
    public SimpleMessagePojo ckeckOrderInvalid(OrderPojo orderPojo) {
        //校验订单是否为空
        if (null == orderPojo) {
            return new SimpleMessagePojo(false, MsgMenu.ORDER_DATA_ERR, null);
        }
        //校验订单创建时间
        if (StringUtils.isEmpty(orderPojo.getCreated())) {
            return new SimpleMessagePojo(false, MsgMenu.ORDER_CREATED_CAN_NOT_NULL, null);
        } else {
            try {
                DateFormatUtil.stringToTimeStamp(orderPojo.getCreated());
            } catch (Exception e) {
                logger.error("订单创建时间格式异常", e);
                return new SimpleMessagePojo(false, MsgMenu.ORDER_CREATED_FORMATE_ERR
                        , null);
            }
        }
        //检验订单行是否为空
        List<OrderEntryPojo> orderEntryList = orderPojo.getOrderEntryList();
        if (CollectionUtils.isEmpty(orderEntryList)) {
            return new SimpleMessagePojo(false, MsgMenu.ORDER_ENTRY_CAN_NOT_NULL, null);
        } else {
            List<OrderEntryPojo> validEntryList = new ArrayList<>(orderEntryList.size());
            //过滤掉赠品订单行
            orderEntryList.forEach(orderEntryPojo -> {
                if (!PromotionConstants.Y.equalsIgnoreCase(orderEntryPojo.getIsGift())) {
                    validEntryList.add(orderEntryPojo);
                }
            });
            //再次判断订单行是否为空
            if (CollectionUtils.isEmpty(validEntryList)) {
                return new SimpleMessagePojo(false, MsgMenu.ORDER_ENTRY_CAN_NOT_NULL, null);
            }

            orderPojo.setOrderEntryList(validEntryList);
        }


        return new SimpleMessagePojo(true, MsgMenu.SUCCESS, null);
    }

    /**
     * 为参与促销计算的订单添加促销计算所必须的数据
     *
     * @param orderPojo
     * @return
     */
    @Override
    public SimpleMessagePojo appendNecessaryField(OrderPojo orderPojo) {
        List<OrderEntryPojo> orderEntryList = orderPojo.getOrderEntryList();
        //订单商品数量
        AtomicInteger totalQty = new AtomicInteger(0);
        //初始换促销条件匹配的订单行
        OrderMatchedProductInfoPojo matchedProductInfoPojo = new OrderMatchedProductInfoPojo();
        List<OrderEntryPojo> matchedEntrys = Collections.synchronizedList(new ArrayList(orderPojo.getOrderEntryList().size()));

        //开启多线程,初始化订单必须数据
        CountDownLatch countDownLatch = new CountDownLatch(orderEntryList.size());
        List<String> errList = Collections.synchronizedList(new ArrayList<>());
        orderEntryList.forEach(orderEntryPojo -> ThreadPoolUtil.submit(() -> {
            SimpleMessagePojo appendResult = appendEntryField(orderEntryPojo, matchedEntrys, countDownLatch, totalQty);
            if (!appendResult.isSuccess()) {
                errList.add(appendResult.getCheckMsg());
            }
        }));
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            logger.error("countdownLatch Await err", e);
            return new SimpleMessagePojo(false, MsgMenu.APPEND_FIELD_ERR, null).setCheckMsg("countdownLatch Await err" + e.getMessage());

        }
        if (!CollectionUtils.isEmpty(errList)) {
            return new SimpleMessagePojo(false, MsgMenu.APPEND_FIELD_ERR, null).setCheckMsg(errList.toString());
        }

        matchedProductInfoPojo.setMatchedEntrys(matchedEntrys);
        orderPojo.setMatchedProduct(matchedProductInfoPojo);
        orderPojo.initOrder();
        orderPojo.setQuantity(totalQty.get());
        return new SimpleMessagePojo(true, MsgMenu.SUCCESS, orderPojo);
    }

    /**
     * 添加订单行的必输字段
     *
     * @param entryPojo     要添加字段的订单行
     * @param matchedEntrys 促销匹配的订单行
     * @param latch         计数器
     * @param totalQty      订单商品总量
     * @return
     */
    public SimpleMessagePojo appendEntryField(OrderEntryPojo entryPojo, List<OrderEntryPojo> matchedEntrys, CountDownLatch latch, AtomicInteger totalQty) {
        try {
            entryPojo.initEntry();
            totalQty.addAndGet(entryPojo.getQuantity());
            //设置订单行参与计算的金额
            entryPojo.setCalPrice(entryPojo.getBasePrice());
            //设置订单行能参与套装计算的价格
            entryPojo.setBundleRemainder(entryPojo.getQuantity());
            matchedEntrys.add(entryPojo);
            SimpleMessagePojo appendResult;
            appendResult = appendProductField(entryPojo);
            if (!appendResult.isSuccess()) {
                return appendResult;
            }

            appendResult = appendCategoryField(entryPojo);
            if (!appendResult.isSuccess()) {
                return appendResult;
            }
            return new SimpleMessagePojo(true, MsgMenu.SUCCESS, null);
        } catch (Exception e) {
            logger.info("添加订单必须字段异常:{}", e.getMessage());
            e.printStackTrace();
            return new SimpleMessagePojo(false, MsgMenu.APPEND_FIELD_ERR, null);
        } finally {
            latch.countDown();
        }
    }

    /**
     * 订单行添加商品必须数据
     *
     * @param orderEntryPojo 要添加数据的订单行
     */
    public SimpleMessagePojo appendProductField(OrderEntryPojo orderEntryPojo) {
        ResponseData productInfo = productClientService.selectProductByCode(orderEntryPojo.getProduct());
        List resp = productInfo.getResp();
        //数据校验
        if (CollectionUtils.isEmpty(resp)) {
            logger.info("code为" + orderEntryPojo.getProduct() + "的商品不存在");
            SimpleMessagePojo errPojo = new SimpleMessagePojo(false, MsgMenu.PRODUCT_ERR, null).setCheckMsg("code为" + orderEntryPojo.getProduct() + "的商品不存在");
            return errPojo;
        }

        Object productResp = resp.get(0);
        String hmallMstProductString = JSON.toJSONString(productResp);
        JSONObject hmallMstProduct = JSON.parseObject(hmallMstProductString);

        //设置商品定制类型
        orderEntryPojo.setCustomSupportType(hmallMstProduct.getString("customSupportType"));
        //设置获取商品主键
        orderEntryPojo.setProductId(hmallMstProduct.getString("productId"));
        //设置商品门店信息
        orderEntryPojo.setPointOfServiceCode(hmallMstProduct.getString("warehouse"));
        return new SimpleMessagePojo(true, MsgMenu.SUCCESS, null);
    }

    /**
     * 订单行添加商品分类必须数据
     *
     * @param orderEntryPojo 要添加字段的订单行
     */
    public SimpleMessagePojo appendCategoryField(OrderEntryPojo orderEntryPojo) {
        CategoryMapping categoryMapping = categoryMappingService.getCategoryByProductId(Long.parseLong(orderEntryPojo.getProductId()));
        if (categoryMapping == null) {
            logger.info("prodcutId为" + orderEntryPojo.getProductId() + "品类映射数据不存在");
            return new SimpleMessagePojo(false, MsgMenu.CATEGORY_ERR, null);
        }

        //初始化cateList
        orderEntryPojo.setCategoryId(categoryMapping.getCategoryId().toString());
        return new SimpleMessagePojo(true, MsgMenu.SUCCESS, null);
    }


    /**
     * 用于执行订单层级促销金额计算，其中netAmount订单未执行促销的总金额减去商品、订单层级优惠金额，该字段用于促销执行
     * orderAmount 订单未执行促销的总金额减去商品层级优惠金额，该字段用于促销条件判断
     */
    @Override
    public void computeOrderPromotPrice(OrderPojo orderPojo) {
        //订单行总价
        Double rawTotal = 0.00;
        for (OrderEntryPojo orderEntryPojo : orderPojo.getOrderEntryList()) {
            //订单总额不包括赠品金额
            orderEntryPojo.computerPriceForPromot();
            rawTotal = DecimalCalculate.add(rawTotal, orderEntryPojo.getTotalFee());
        }
        //总折扣金额
        Double totalDiscount = DecimalCalculate.add(orderPojo.getDiscountFee(), orderPojo.getCouponFee());
        if (totalDiscount > rawTotal) {
            totalDiscount = rawTotal;
        }
        //订单净额
        Double netAmount = DecimalCalculate.sub(rawTotal, totalDiscount);
        Double orderAmount = rawTotal;
        orderPojo.setNetAmount(netAmount);
        orderPojo.setOrderAmount(orderAmount);
        orderPojo.setTotalDiscount(totalDiscount);
    }

    /**
     * 初始化订单条件匹配的商品,将订单的订单行转换成满足促销条件的商品集合对象.
     *
     * @param orderPojo
     */
    @Override
    public void collectMatchedProduct(OrderPojo orderPojo) {

        List<OrderEntryPojo> orderEntryList = orderPojo.getOrderEntryList();
        OrderMatchedProductInfoPojo matchedProduct = orderPojo.getMatchedProduct();
        if (null == matchedProduct) {
            matchedProduct = new OrderMatchedProductInfoPojo();
            List<OrderEntryPojo> matchedEntrys = new ArrayList<>(orderEntryList.size());
            orderEntryList.forEach(orderEntryPojo -> {
                matchedEntrys.add(orderEntryPojo);
            });
            matchedProduct.setMatchedEntrys(matchedEntrys);
            matchedProduct.setProductTotalPrice(calSumPriceInCodes(matchedEntrys));
            matchedProduct.setProductTotalQty(calSumQuantInCodes(matchedEntrys));
        } else {
            List<OrderEntryPojo> matchedEntrys = matchedProduct.getMatchedEntrys();
            matchedProduct.setProductTotalPrice(calSumPriceInCodes(matchedEntrys));
            matchedProduct.setProductTotalQty(calSumQuantInCodes(matchedEntrys));
        }
        orderPojo.setMatchedProduct(matchedProduct);
    }

    /**
     * 用于商品层级促销金额计算，netAmount与orderAmount保持一致
     */
    @Override
    public void computePromotPrice(OrderPojo orderPojo) {
        computeOrderPromotPrice(orderPojo);
        orderPojo.setOrderAmount(orderPojo.getNetAmount());
    }

    /**
     * 促销优惠金额分摊,精度为小数点后三位
     *
     * @param orderPojo
     */
    @Override
    public void apportionFee(OrderPojo orderPojo) {
        Double orderAmount = DecimalCalculate.arraySum(orderPojo.getNetAmount(), orderPojo.getEpostFee(), orderPojo.getPostFee());
        orderPojo.setOrderAmount(orderAmount);
        //订单行减去订单行促销金额后的总金额
        Double entryTotalFee = DecimalCalculate.arraySum(orderPojo.getNetAmount(), orderPojo.getDiscountFee(), orderPojo.getCouponFee());
        //分摊订单头促销金额到行
        apporDiscountFee(orderPojo.getDiscountFee(), entryTotalFee, orderPojo.getOrderEntryList());
        //分摊订单头优惠券金额到行
        apporCouponFee(orderPojo.getCouponFee(), entryTotalFee, orderPojo.getOrderEntryList());
        //计算订单促销活动总优惠金额
        Double activityDiscount = calActivityDiscountFee(orderPojo.getDiscountFee(), orderPojo.getOrderEntryList());
        orderPojo.setDiscountFee(activityDiscount);
        //计算优惠券总优惠金额
        Double couponDiscountFee = calCouponDiscountFee(orderPojo.getOrderEntryList());
        orderPojo.setCouponFee(couponDiscountFee);
        //计算订单总优惠金额
        orderPojo.setTotalDiscount(DecimalCalculate.add(activityDiscount, couponDiscountFee));
        //从新计算订单行金额
        orderPojo.getOrderEntryList().forEach(orderEntryPojo -> {
            orderEntryPojo.computerPriceForPromot();
            //设置unitFee
            orderEntryPojo.setUnitFee(DecimalCalculate.round(DecimalCalculate.div(orderEntryPojo.getTotalFee(), orderEntryPojo.getQuantity()), 3));
        });

    }

    /**
     * 分摊订单层级优惠金额到订单行
     *
     * @param orderDiscount  订单头优惠金额
     * @param entryTotalFee  订单行减去商品层级促销,优惠金额后的总金额
     * @param orderEntryList 要分摊的订单行
     */
    public void apporDiscountFee(Double orderDiscount, Double entryTotalFee, List<OrderEntryPojo> orderEntryList) {
        if (orderDiscount > 0) {
            Double usedFee = 0d;
            for (int i = 0; i < orderEntryList.size(); i++) {
                OrderEntryPojo entryPojo = orderEntryList.get(i);
                //最后一个订单行
                if (i == orderEntryList.size() - 1) {
                    entryPojo.setDiscountFeel(DecimalCalculate.sub(orderDiscount, usedFee));
                } else {
                    Double discountFeel = DecimalCalculate.mul(orderDiscount, DecimalCalculate.div(entryPojo.getTotalFee(), entryTotalFee));
                    discountFeel = DecimalCalculate.round(discountFeel, 2);
                    usedFee = DecimalCalculate.add(usedFee, discountFeel);
                    entryPojo.setDiscountFeel(discountFeel);
                }
            }
        }
    }

    /**
     * 分摊订单头上的优惠券优惠金额到订单行
     *
     * @param couponFee      订单头上的优惠券减免金额
     * @param entryTotalFee  订单行减去商品层级促销,优惠金额后的总金额
     * @param orderEntryList 要分摊的订单行
     */
    public void apporCouponFee(Double couponFee, Double entryTotalFee, List<OrderEntryPojo> orderEntryList) {
        if (couponFee > 0) {
            Double usedFee = 0d;
            for (int i = 0; i < orderEntryList.size(); i++) {
                OrderEntryPojo entryPojo = orderEntryList.get(i);
                //最后一个订单行
                if (i == orderEntryList.size() - 1) {
                    entryPojo.setCouponFee(DecimalCalculate.add(entryPojo.getCouponFee(), DecimalCalculate.sub(couponFee, usedFee)));
                } else {
                    Double couponFeeL = DecimalCalculate.mul(couponFee, DecimalCalculate.div(entryPojo.getTotalFee(), entryTotalFee));
                    double roundCouponFeel = DecimalCalculate.round(couponFeeL, 2);
                    usedFee = DecimalCalculate.add(usedFee, roundCouponFeel);
                    entryPojo.setCouponFee(DecimalCalculate.add(entryPojo.getCouponFee(), roundCouponFeel));
                }
            }
        }
    }

    /**
     * 计算订单促销活动总的优惠金额
     *
     * @param orderDiscount   订单层级优惠金额
     * @param orderEntryPojos 要汇总促销金额的订单行
     * @return activityDiscount 促销活动优惠总金额
     */
    public Double calActivityDiscountFee(Double orderDiscount, List<OrderEntryPojo> orderEntryPojos) {
        //商品层级总优惠金额
        Double entryTotalDiscount = 0d;
        for (OrderEntryPojo orderEntryPojo : orderEntryPojos) {
            entryTotalDiscount = DecimalCalculate.add(entryTotalDiscount, orderEntryPojo.getDiscountFee());
        }
        Double activityDiscount = DecimalCalculate.add(orderDiscount, entryTotalDiscount);
        return activityDiscount;
    }

    /**
     * 计算优惠券总的优惠金额
     *
     * @param orderEntryPojos 要汇总优惠券金额的订单行
     * @return activityDiscount 促销活动优惠总金额
     */
    public Double calCouponDiscountFee(List<OrderEntryPojo> orderEntryPojos) {
        //商品层级优惠券总优惠金额
        Double couponTotalDiscount = 0d;
        for (OrderEntryPojo orderEntryPojo : orderEntryPojos) {
            couponTotalDiscount = DecimalCalculate.add(couponTotalDiscount, orderEntryPojo.getCouponFee());
        }
        return couponTotalDiscount;
    }

    /**
     * 将促销计算后的orderPojo转换成商城需要的pojo
     *
     * @param orderPojo 促销计算后的订单数据
     * @return mallPromotionResult 商城需要的订单数据
     */
    @Override
    public MallPromotionResult transToReturnPojo(OrderPojo orderPojo) {
        String orderJson = JSON.toJSONString(orderPojo);
        MallPromotionResult mallPromotionResult = JSON.parseObject(orderJson, MallPromotionResult.class);
        List<MallEntryPojo> mallEntryPojos = new ArrayList<>(orderPojo.getOrderEntryList().size());
        orderPojo.getOrderEntryList().forEach(orderEntryPojo -> {
            MallEntryPojo mallEntryPojo = JSON.parseObject(JSON.toJSONString(orderEntryPojo), MallEntryPojo.class);
            mallEntryPojos.add(mallEntryPojo);
        });
        List<GiftPojo> giftPojos = orderPojo.getGiftPojos();
        if (!CollectionUtils.isEmpty(giftPojos)) {
            giftPojos.forEach(giftPojo -> {
                MallEntryPojo mallEntryPojo = new MallEntryPojo();
                mallEntryPojo.setProductId(giftPojo.getProductId());
                mallEntryPojo.setIsGift("Y");
                mallEntryPojo.setQuantity(giftPojo.getQuantity());
                mallEntryPojo.setBasePrice(giftPojo.getBasePrice());
                mallEntryPojo.setShippingType(giftPojo.getDefaultDelivery());
                mallEntryPojos.add(mallEntryPojo);
            });
        }
        mallPromotionResult.setOrderEntryList(mallEntryPojos);
        return mallPromotionResult;
    }

    /**
     * 初始化订单金额为0
     */
    public void initOrder() {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                if (field.getType().getName().equals("java.lang.Double")) {
                    field.setAccessible(true);
                    field.set(this, 0D);
                }
                if (field.getType().getName().equals("java.lang.Integer") && field.get(this) == null) {
                    field.setAccessible(true);
                    field.set(this, 0);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取productCodes中的商品的数量总和
     *
     * @param matchedEntrys 订单行集合
     * @return
     */
    public Integer calSumQuantInCodes(List<OrderEntryPojo> matchedEntrys) {

        //所有参与促销活动的商品的数量之和
        Integer allActivityQuantity = 0;
        for (OrderEntryPojo entry : matchedEntrys) {
            allActivityQuantity = allActivityQuantity + entry.getQuantity();
        }
        return allActivityQuantity;
    }

    /**
     * 获取matchEntrys中的商品的金额总和
     *
     * @param matchEntrys 订单行集合
     * @return
     */
    public Double calSumPriceInCodes(List<OrderEntryPojo> matchEntrys) {
        //所有参与促销活动的商品的价格之和
        Double allActivityPrice = 0.0;
        for (OrderEntryPojo entry : matchEntrys) {
            allActivityPrice = DecimalCalculate.add(allActivityPrice, DecimalCalculate.mul(entry.getQuantity(), entry.getBasePrice()));
        }
        return allActivityPrice;
    }

}
