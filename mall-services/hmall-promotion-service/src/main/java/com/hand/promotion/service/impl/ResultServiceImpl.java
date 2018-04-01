package com.hand.promotion.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import com.hand.promotion.pojo.SimpleMessagePojo;
import com.hand.promotion.pojo.activity.ActionPojo;
import com.hand.promotion.pojo.activity.ActivityDetailPojo;
import com.hand.promotion.pojo.activity.ActivityPojo;
import com.hand.promotion.pojo.activity.ChildPojo;
import com.hand.promotion.pojo.activity.ContainerPojo;
import com.hand.promotion.pojo.activity.MatchValuePojo;
import com.hand.promotion.pojo.activity.ParameterPojo;
import com.hand.promotion.pojo.activity.PromotionActivitiesPojo;
import com.hand.promotion.pojo.activity.TargetValuePojo;
import com.hand.promotion.pojo.coupon.CouponsPojo;
import com.hand.promotion.pojo.coupon.PromotionCouponsPojo;
import com.hand.promotion.pojo.enums.ConditionActions;
import com.hand.promotion.pojo.enums.MsgMenu;
import com.hand.promotion.pojo.enums.PromotionConstants;
import com.hand.promotion.pojo.order.GiftPojo;
import com.hand.promotion.pojo.order.OrderEntryPojo;
import com.hand.promotion.pojo.order.OrderMatchedProductInfoPojo;
import com.hand.promotion.pojo.order.OrderPojo;
import com.hand.promotion.service.IOrderCalculateService;
import com.hand.promotion.service.IPromotionActivityService;
import com.hand.promotion.service.IResultService;
import com.hand.promotion.util.CompareUtil;
import com.hand.promotion.util.DateFormatUtil;
import com.hand.promotion.util.DecimalCalculate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author zhuweifeng
 * @date 2017/9/4
 * 结果计算service
 * 用途：用来根据订单和规则来计算结果
 */
@Service
public class ResultServiceImpl implements IResultService {
    private static Logger logger = LoggerFactory.getLogger(ResultServiceImpl.class);

    @Autowired
    private IPromotionActivityService promotionActivityService;
    @Autowired
    private IOrderCalculateService orderCalculateService;


    /**
     * 根据订单和促销规则返回结果
     *
     * @param order 订单
     * @param rule  规则
     */
    @Override
    public SimpleMessagePojo calResultByOrderAndRules(OrderPojo order, PromotionActivitiesPojo rule, Boolean countUpdateFlag) {
        //结果验证
        List<ActionPojo> actions = rule.getActions();
        if (CollectionUtils.isEmpty(actions) || actions.size() > 1) {
            logger.error("结果数量不正确");
            return new SimpleMessagePojo(false, MsgMenu.ACTION_SIZE_ERR, null);
        }
        ActionPojo action = actions.get(0);
        SimpleMessagePojo result = calResultHandler(order, action, null, rule.getActivity(), rule.getContainers(), countUpdateFlag, false);
        return new SimpleMessagePojo(result.isSuccess(), null, null);
    }


    /**
     * 根据订单和优惠券规则返回结果
     *
     * @param order 订单
     * @param rule  规则
     */
    @Override
    public SimpleMessagePojo calCouponResultByOrderAndRules(OrderPojo order, PromotionCouponsPojo rule) {
        //结果验证
        List<ActionPojo> actions = rule.getActions();
        if (CollectionUtils.isEmpty(actions) || actions.size() > 1) {
            logger.error("结果数量正确:{}", actions.size());
            return new SimpleMessagePojo(false, MsgMenu.ACTION_SIZE_ERR, null);
        }
        ActionPojo action = actions.get(0);
        SimpleMessagePojo result = calResultHandler(order, action, rule.getCoupon(), null, rule.getContainers(), false, true);
        return new SimpleMessagePojo(result.isSuccess(), null, null);
    }

    /**
     * 结果计算方法
     *
     * @param isCouponFlag 是否是计算优惠券
     */
    public SimpleMessagePojo calResultHandler(OrderPojo order, ActionPojo action, CouponsPojo couponsPojo, ActivityPojo activityPojo, List<ContainerPojo> containers, Boolean countUpdateFlag, Boolean isCouponFlag) {
        SimpleMessagePojo calResult = null;
        //促销或优惠券主键
        String promotionId;
        //判断计算的是促销还是优惠券
        if (isCouponFlag) {
            promotionId = couponsPojo.getId();
        } else {
            promotionId = activityPojo.getId();
        }
        //校验action数据是否有效
        ConditionActions conditionActions = ConditionActions.getConditionActionsByValue(action.getDefinitionId());
        if (conditionActions == null) {
            logger.info("基本条件definitionId:{}无效，跳过该结果", action.getDefinitionId());
            return new SimpleMessagePojo(false, null, null).setCheckMsg("基本条件definitionId:" + action.getDefinitionId() + "无效，跳过该结果");
        }
        ParameterPojo parameterPojo = action.getParameters();
        if (action.getParameters() == null) {
            logger.info("基本条件definitionId:{}无效，跳过该结果", action.getDefinitionId());
            return new SimpleMessagePojo(false, null, null).setCheckMsg(promotionId + "结果数据不存在跳过该结果");
        }
        switch (conditionActions) {
            //订单减X元
            case o_total_discount:
                //订单促销价格
                calOrderDiscount(promotionId, order, parameterPojo, isCouponFlag);
                break;
            //订单打x折
            case o_total_rate:
                //订单促销价格
                calOrderRate(promotionId, order, parameterPojo, isCouponFlag);
                break;
            //订单行减X元
            case oe_total_discount:
                //订单行折扣/满减促销
                calEntryDiscount(promotionId, order, parameterPojo, isCouponFlag);
                break;

            //订单行打x折
            case oe_total_rate:
                calEntryRate(promotionId, order, parameterPojo, isCouponFlag);
                break;
            //运费减免
            case o_freight_waiver:
                freeFeight(promotionId, order, parameterPojo, isCouponFlag);
                break;
            //赠送优惠券
            case o_giver_coupon:
                logger.info("o_giver_coupon doNothing");
                break;
            //赠品
            case o_giver_product:
                if (PromotionConstants.PICKUP.equals(order.getDistribution())) {
                    logger.info("门店自提不参与赠品活动");
                    return new SimpleMessagePojo(false, MsgMenu.PICK_UP_NO_GIFT, null);
                }
                distributedGifts(promotionId, order, parameterPojo, isCouponFlag);
                //记录明细
                recordActivityDetail(promotionId, 0D, order, isCouponFlag);
                break;
            //免单X件
            case o_free_number:
                freePiece(promotionId, order, parameterPojo, isCouponFlag);
                break;
            //商品固定价格
            case o_fixed_number:
                calResult = fixProductPrice(promotionId, order, parameterPojo, isCouponFlag);
                break;
            //商品固定折扣
            case o_fixed_rate:
                calResult = fixProductRate(promotionId, order, parameterPojo, isCouponFlag);
                break;
            //商品抢购,前X件减Y元
            case o_front_delete:
                logger.info("o_front_delete doNothing");
                break;
            //商品抢购,前X件打Y折
            case o_front_rate:
                logger.info("o_front_delete doNothing");
                break;
            //订单每满X元减Y元
            case o_meet_delete:
                //每满X元
                calAmountMeetDiscount(promotionId, order, parameterPojo, isCouponFlag);
                break;
            //订单每满X件减Y元
            case o_meet_number_delete:
                //每满X件
                calPieceMeetDiscount(promotionId, order, parameterPojo, isCouponFlag);
                break;
            //订单行每满x元减y元
            case oe_meet_delete:
                //每满x元
                calEntryMeetDiscount(promotionId, order, parameterPojo, isCouponFlag);
                break;

            //固定价格购买其他商品
            case o_fixed_price_buy:
                logger.info("o_fixed_price_buy doNothing");
                break;
            //固定折扣购买其他商品
            case o_fixed_rate_buy:
                logger.info("o_fixed_rate_buy doNothing");
                break;
            //目标包价格
            case o_target_price:
                calResult = targetPrice(promotionId, order, parameterPojo, containers, isCouponFlag);
                break;
            //订单阶梯折扣
            case o_discount_ladders:
                ladderDiscount(promotionId, order, parameterPojo, isCouponFlag);
                break;
            //商品前X件固定金额
            case p_number_discount:
                productFixPrice(promotionId, order, parameterPojo, isCouponFlag);
                break;
            //商品前x件固定折扣
            case p_number_rate:
                productRateDiscount(promotionId, order, parameterPojo, isCouponFlag);
                break;
            default:
                break;
        }
        if (calResult != null && !calResult.isSuccess()) {
            return new SimpleMessagePojo(false, null, null);
        }
        return new SimpleMessagePojo();
    }

    /**
     * 前x件商品打y折
     *
     * @param order          参与促销计算的订单数据
     * @param parametersPojo 促销金额信息
     * @param isCouponFlag   是否是优惠券
     * @param promotionId    促销/优惠券主键
     */
    private void productRateDiscount(String promotionId, OrderPojo order, ParameterPojo parametersPojo, Boolean isCouponFlag) {
        //优惠商品件数
        Integer pNum = Integer.parseInt(parametersPojo.transValuePojo().getQuantity());
        //商品折扣
        Double rate = Double.parseDouble(parametersPojo.transValuePojo().getValue());
        //根据商品单价由高到低排序
        List<OrderEntryPojo> orderEntryList = order.getMatchedProduct().getMatchedEntrys();
        entrysSortByPrice(orderEntryList, true);
        //商品参与促销的真实折扣
        Double realRate = DecimalCalculate.div(DecimalCalculate.sub(10, rate), 10);

        //循环终止条件，订单行全部遍历完成或者商品数量为0
        Iterator<OrderEntryPojo> entryPojoIterator = orderEntryList.iterator();
        Double totalDiscountFee = 0d;
        while (entryPojoIterator.hasNext() && pNum > 0) {
            OrderEntryPojo entryPojo = entryPojoIterator.next();
            double entryDiscount = 0.0;
            //比较优惠商品件数与订单行数量，获取订单行要执行优惠的商品数量
            if (pNum >= entryPojo.getQuantity()) {
                entryDiscount = DecimalCalculate.mul(entryPojo.getTotalFee(), realRate);
                pNum = pNum - entryPojo.getQuantity();
            } else {
                //要打折的商品数量占订单行的比率
                double pieceRate = DecimalCalculate.div(pNum, entryPojo.getQuantity());
                entryDiscount = DecimalCalculate.mul(DecimalCalculate.mul(entryPojo.getTotalFee(), pieceRate), realRate);
                pNum = 0;
            }

            //判断执行的是优惠券还是促销活动
            recordEntryPromoteFee(isCouponFlag, entryPojo, entryDiscount);
            totalDiscountFee = DecimalCalculate.add(totalDiscountFee, entryDiscount);
        }
        recordActivityDetail(promotionId, totalDiscountFee, order, isCouponFlag);
    }

    /**
     * 商品前X件单价固定金额 将满足条件的订单行按商品金额由高到低排序，对金额高的商品固定金额促销
     *
     * @param order        参与计算的订单数据
     * @param parameters   促销计算数据
     * @param isCouponFlag 是否是优惠券
     * @param promotionId  促销/优惠券主键
     */
    private void productFixPrice(String promotionId, OrderPojo order, ParameterPojo parameters, Boolean isCouponFlag) {
        //优惠商品件数
        Integer productNum = Integer.parseInt(parameters.transValuePojo().getQuantity());
        //商品固定金额
        Double fixPrice = Double.parseDouble(parameters.transValuePojo().getValue());
        //根据商品单价由高到低排序
        List<OrderEntryPojo> orderEntryList = order.getMatchedProduct().getMatchedEntrys();
        entrysSortByPrice(orderEntryList, true);
        Iterator<OrderEntryPojo> iterator = orderEntryList.iterator();
        //记录促销优惠总金额
        double totalDiscount = 0d;
        //循环终止条件，订单行全部遍历完成或者商品数量为0
        while (iterator.hasNext() && productNum > 0) {
            OrderEntryPojo entryPojo = iterator.next();
            //订单行要进行促销的件数
            int discountPiece;
            //计算订单行固定价格的总金额
            if (productNum >= entryPojo.getQuantity()) {
                productNum = productNum - entryPojo.getQuantity();
                discountPiece = entryPojo.getQuantity();
            } else {
                discountPiece = productNum;
                productNum = 0;
            }
            //固定金额的商品总金额
            double fixTotalFee = DecimalCalculate.mul(discountPiece, fixPrice);
            //参与本次促销的商品总价
            double entryTotalFee = DecimalCalculate.mulround(entryPojo.getTotalFee(), DecimalCalculate.div(discountPiece, entryPojo.getQuantity().intValue()));
            //本次促销对订单行的优惠
            double entryDiscount = DecimalCalculate.sub(entryTotalFee, fixTotalFee);
            //设置减免金额
            recordEntryPromoteFee(isCouponFlag, entryPojo, entryDiscount);
            totalDiscount = DecimalCalculate.add(totalDiscount, entryDiscount);
        }
        recordActivityDetail(promotionId, totalDiscount, order, isCouponFlag);
    }

    /**
     * 订单阶梯折扣
     *
     * @param order        参与促销计算的订单金额
     * @param isCouponFlag 是否是优惠券
     * @param promotionId  促销或优惠券主键
     */
    private void ladderDiscount(String promotionId, OrderPojo order, ParameterPojo parameterPojo, boolean isCouponFlag) {
        String value = parameterPojo.transValuePojo().getValue();
        List<Map<String, String>> mapList = JSON.parseObject(value, new TypeReference<List<Map<String, String>>>() {
        });
        //TreeMap默认按key升序排序
        Map<String, String> map = new TreeMap<>();
        for (Map<String, String> stringObjectMap : mapList) {
            List<String> list = new ArrayList<>();
            for (String s : stringObjectMap.keySet()) {
                logger.info(s + "==>" + stringObjectMap.get(s));
                list.add(stringObjectMap.get(s));
            }
            map.put(list.get(1), list.get(0));
            //300 -> 50  满300减50
        }
        map.size();
        //折扣金额
        Double discountMoney = 0.0;
        Double orderAmount = order.getOrderAmount();
        for (String s : map.keySet()) {
            //300
            double key = Double.parseDouble(s);
            //50
            double discount = Double.parseDouble(map.get(s));
            if (orderAmount >= key && orderAmount <= DecimalCalculate.add(key, discount)) {
                discountMoney = DecimalCalculate.add(discountMoney, DecimalCalculate.sub(orderAmount, key));
            }
            if (orderAmount > DecimalCalculate.add(key, discount)) {
                discountMoney = DecimalCalculate.add(discountMoney, discount);
            }
        }
        recordOrderPromoteFee(isCouponFlag, order, discountMoney);
        //处理记录
        recordActivityDetail(promotionId, discountMoney, order, isCouponFlag);
    }

    /**
     * 订单行每满x元减y元
     *
     * @param order
     * @param parameterPojo
     * @param isCouponFlag
     * @param promotionId
     */
    private void calEntryMeetDiscount(String promotionId, OrderPojo order, ParameterPojo parameterPojo, Boolean isCouponFlag) {
        //条件的x元
        Double frontPrice = Double.valueOf(parameterPojo.transValuePojo().getFront());
        //要减去的y元
        Double discountValue = Double.parseDouble(parameterPojo.transValuePojo().getValue());
        List<OrderEntryPojo> orderEntryList = order.getMatchedProduct().getMatchedEntrys();
        //本次促销优惠总金额
        double entryTotalDiscount = 0d;
        for (OrderEntryPojo orderEntryPojo : orderEntryList) {
            //每行促销总价
            Double discountFee = DecimalCalculate.mul(Math.floor(orderEntryPojo.getTotalFee() / frontPrice), discountValue);
            //记录订单行的 促销或优惠券信息
            recordEntryPromoteFee(isCouponFlag, orderEntryPojo, discountFee);
            //所有订单行总促销价
            entryTotalDiscount = DecimalCalculate.add(entryTotalDiscount, Double.valueOf(discountFee));
        }
        recordActivityDetail(promotionId, entryTotalDiscount, order, isCouponFlag);
    }

    /**
     * 免邮促销
     *
     * @param order        参与促销计算的订单数据
     * @param isCouponFlag 是否是优惠券
     * @param promotionId  促销/优惠券主键
     */
    private void freeFeight(String promotionId, OrderPojo order, ParameterPojo parameterPojo, Boolean isCouponFlag) {
        if (order.getEpostFee() > 0) {
            order.setEpostReduce(order.getEpostFee());
            order.setEpostFee(0.00);
        }
        if (order.getPostFee() > 0) {
            order.setPostReduce(order.getPostFee());
            order.setPostFee(0.0);
        }
        if (order.getFixFee() > 0) {
            order.setFixReduce(order.getFixFee());
            order.setFixFee(0.0);
        }
        List<OrderEntryPojo> entryPojos = order.getOrderEntryList();
        entryPojos.forEach(o -> {
            if (o.getShippingFee() > 0) {
                o.setPreShippingFee(o.getShippingFee());
                o.setShippingFee(0.0);

            }

            if (o.getInstallationFee() > 0) {
                o.setPreInstallationFee(o.getInstallationFee());
                o.setInstallationFee(0.0);
            }

        });

        //记录明细
        recordActivityDetail(promotionId, order.getPostReduce() + order.getEpostReduce(), order, isCouponFlag);
    }

    /**
     * 计算订单行折扣促销
     *
     * @param order        参与促销计算的订单数据
     * @param isCouponFlag 是否是优惠券计算
     * @param promotionId  促销/优惠券主键
     */
    private void calEntryRate(String promotionId, OrderPojo order, ParameterPojo parameterPojo, Boolean isCouponFlag) {
        double entryTotalDiscount = 0d;
        String value = parameterPojo.transValuePojo().getValue();
        //促销条件匹配的订单行
        List<OrderEntryPojo> orderEntryList = order.getMatchedProduct().getMatchedEntrys();
        for (OrderEntryPojo orderEntryPojo : orderEntryList) {
            //折扣
            Double rate = DecimalCalculate.div(Double.valueOf(value), 10);
            //订单行总折扣价格
            Double rateFee = DecimalCalculate.sub(orderEntryPojo.getTotalFee(), DecimalCalculate.mul(orderEntryPojo.getTotalFee(), Double.valueOf(rate)));
            //记录订单行的 促销或优惠券信息
            recordEntryPromoteFee(isCouponFlag, orderEntryPojo, rateFee);
            //所有订单行总折扣价格
            entryTotalDiscount = DecimalCalculate.add(entryTotalDiscount, Double.valueOf(rateFee));
        }
        recordActivityDetail(promotionId, entryTotalDiscount, order, isCouponFlag);
    }

    /**
     * 将订单行上折扣记录到订单行的对应字段
     *
     * @param isCouponFlag
     * @param orderEntryPojo
     * @param discountFee
     */
    private void recordEntryPromoteFee(Boolean isCouponFlag, OrderEntryPojo orderEntryPojo, Double discountFee) {
        //行折扣提供精确的小数位四舍五入处理
        double round = DecimalCalculate.round(discountFee, 3);
        if (!isCouponFlag) {
            //每个订单行促销价格
            orderEntryPojo.setDiscountFee(DecimalCalculate.add(orderEntryPojo.getDiscountFee(), round));
        } else {
            //每个订单行优惠价格
            orderEntryPojo.setCouponFee(DecimalCalculate.add(orderEntryPojo.getCouponFee(), round));
        }
    }

    /**
     * 计算订单行满减促销
     *
     * @param order        参与促销计算的订单信息
     * @param isCouponFlag 计算的是促销活动还是优惠券
     * @param promotionId  促销活动或优惠券主键
     */
    private void calEntryDiscount(String promotionId, OrderPojo order, ParameterPojo parameterPojo, Boolean isCouponFlag) {
        //要减去的金额
        String value = parameterPojo.transValuePojo().getValue();
        //记录本次促销活动的总优惠金额
        double entryTotalDiscount = 0d;
        //促销条件匹配到的订单行信息
        List<OrderEntryPojo> orderEntryList = order.getMatchedProduct().getMatchedEntrys();
        for (OrderEntryPojo orderEntryPojo : orderEntryList) {
            //记录订单行的 促销或优惠券信息
            recordEntryPromoteFee(isCouponFlag, orderEntryPojo, Double.valueOf(value));
            entryTotalDiscount = DecimalCalculate.add(entryTotalDiscount, Double.valueOf(value));
        }
        recordActivityDetail(promotionId, entryTotalDiscount, order, isCouponFlag);
    }


    /**
     * 订单中的订单行按单价升序排列
     *
     * @param entrys   要排序的订单行
     * @param reversed 是否逆序 true为降序排序 false为升序排序
     */
    protected void entrysSortByPrice(List<OrderEntryPojo> entrys, boolean reversed) {

        if (reversed) {
            entrys.sort(Comparator.comparing(OrderEntryPojo::getCalPrice).reversed());
        } else {
            entrys.sort(Comparator.comparing(OrderEntryPojo::getCalPrice));

        }
    }

    /**
     * 订单减X元 - 订单头
     */
    public void calOrderDiscount(String activityId, OrderPojo orderPojo, ParameterPojo parameterPojo, Boolean isCouponFlag) {
        //要减去的金额
        String value = parameterPojo.transValuePojo().getValue();
        //订单在条件范围内的总价
        Double discountMoney = 0D;
        discountMoney = Double.valueOf(value);
        //设置订单头的促销或优惠券信息
        recordOrderPromoteFee(isCouponFlag, orderPojo, discountMoney);

        //记录明细
        recordActivityDetail(activityId, discountMoney, orderPojo, isCouponFlag);
    }

    /**
     * 订单头打折
     * 计算逻辑：
     * 优惠掉的钱 = 总价 - (x折/10)*总价
     */
    public void calOrderRate(String activityId, OrderPojo orderPojo, ParameterPojo parameterPojo, Boolean isCouponFlag) {
        //订单在条件范围内的总价
        Double netAmount = orderPojo.getNetAmount();
        Double rate = Double.valueOf(parameterPojo.transValuePojo().getValue());
        //打折掉的钱 = 订单在条件范围内的总价 - (x折/10)*订单在条件范围内的总价 例如： 100 -（7/10）*100 = 30
        double discountMoney = DecimalCalculate.sub(netAmount, DecimalCalculate.mul(DecimalCalculate.div(rate, 10), netAmount));
        //设置订单头的促销或优惠券信息
        recordOrderPromoteFee(isCouponFlag, orderPojo, discountMoney);
        //todo::分摊行
        //记录明细
        recordActivityDetail(activityId, discountMoney, orderPojo, isCouponFlag);
    }


    /**
     * 免单X件
     *
     * @param activityId    促销或优惠券主键
     * @param orderPojo     参与促销计算的订单数据
     * @param parameterPojo 促销结果参数
     * @param isCouponFlag  是否是优惠券
     */
    public void freePiece(String activityId, OrderPojo orderPojo, ParameterPojo parameterPojo, Boolean isCouponFlag) {
        //条件匹配的订单行信息
        List<OrderEntryPojo> matchedEntrys = orderPojo.getMatchedProduct().getMatchedEntrys();
        //本次促销优惠的钱
        Double activityDiscount = 0D;
        //本次促销要免单的商品件数
        int freePiece = Integer.valueOf(parameterPojo.transValuePojo().getValue());
        entrysSortByPrice(matchedEntrys, false);
        for (OrderEntryPojo entryPojo : matchedEntrys) {
            if (freePiece <= 0) {
                break;
            }
            //订单行要免单的数量
            Integer entryFreeQty;
            //如果订单行数量大于等于减免数量，直接在订单行中减去数量  如：5>4
            if (entryPojo.getQuantity() >= freePiece) {
                //免掉X件后的行数量 = 订单行原数量 - 减免数量
                entryFreeQty = freePiece;
                freePiece = 0;
            } else {
                //如果订单行数量小于减免数量，免掉X件后的行数量变为0  如:4<5
                entryFreeQty = entryPojo.getQuantity();
                //待减免的数量 = 待减免的数量 - 订单行的数量
                freePiece = freePiece - entryPojo.getQuantity();
            }

            //免掉X件后的行总价 = 原单价 * 免掉X件后的行数量
            Double entryDiscount = DecimalCalculate.mulround(entryPojo.getTotalFee(), DecimalCalculate.div(entryFreeQty, entryPojo.getQuantity()));
            recordEntryPromoteFee(isCouponFlag, entryPojo, entryDiscount);
            activityDiscount = DecimalCalculate.add(activityDiscount, entryDiscount);
        }

        //记录明细
        recordActivityDetail(activityId, activityDiscount, orderPojo, isCouponFlag);
    }

    /**
     * 抢购前X件减y元/打y折
     *
     * @param frontPiece 前X件
     * @param discount   减Y元/打Y折
     *                   计算逻辑：
     *                   1.根据activityId查询计数器
     *                   1.1 如果超过范围直接提示不执行结果
     *                   1.2 如果未超过范围获得计入计数器的数量
     *                   2. 优惠后的单价 =（原订单行总价 - 优惠掉的钱）/ 数量
     *                   3.调用通用方法计算fees
     *                   4.调用通用方法计算activityPrice
     */
    public void frontDelete(String activityId, OrderPojo orderPojo, Integer frontPiece, Double discount, String rateOrNumber, Boolean isCouponFlag) {
        List<OrderEntryPojo> entrys = orderPojo.getOrderEntryList();
        //Y元或Y折的钱,即优惠掉的价格
        Double discountMoney = 0D;
        //本次促销优惠的钱
        Double activityDiscount = 0D;
        List<OrderEntryPojo> matchedProducts = orderPojo.getMatchedProduct().getMatchedEntrys();
        for (OrderEntryPojo entryPojo : entrys) {
            if (frontPiece <= 0) {
                break;
            }
            //只分摊到条件范围中的sku
            if (!matchedProducts.contains(entryPojo.getProduct())) {
                continue;
            }


            if (PromotionConstants.DISCOUNT_TYPE_NUMBER.equals(rateOrNumber)) {
                discountMoney = discount;
            } else if (PromotionConstants.DISCOUNT_TYPE_RATE.equals(rateOrNumber)) {
                //打折掉的价格 = 原单价 - (x折/10)*原单价
                discountMoney = DecimalCalculate.sub(entryPojo.getCalPrice(), DecimalCalculate.mul(DecimalCalculate.div(discount, 10), entryPojo.getCalPrice()));
            }

            Double skuUnitPrice = 0D;
            //如果订单行数量大于等于前x件数量，直接在订单行中减去数量  例: 5>3,取frontPiece*减Y元
            if (entryPojo.getQuantity() >= frontPiece) {
                // 优惠后的总价 = 原单价*数量 - 前X件*减y元
                Double skuTotalPrice = DecimalCalculate.sub(DecimalCalculate.mul(entryPojo.getCalPrice(), entryPojo.getQuantity()), DecimalCalculate.mul(frontPiece, discountMoney));
                //优惠后的单价 = 优惠后的总价/数量
                skuUnitPrice = DecimalCalculate.div(skuTotalPrice, entryPojo.getQuantity());
                //待减免的归0
                frontPiece = 0;
            } else {//如果订单行数量大于小于前x件数量，直接在订单行中减去数量 例: 2<6 ，取product.getQuantity()*减Y元
                // 优惠后的总价 = 原单价*数量 - 前X件*减y元
                Double skuTotalPrice = DecimalCalculate.sub(DecimalCalculate.mul(entryPojo.getCalPrice(), entryPojo.getQuantity()), DecimalCalculate.mul(entryPojo.getQuantity(), discountMoney));
                //优惠后的单价 = 优惠后的总价/数量
                skuUnitPrice = DecimalCalculate.div(skuTotalPrice, entryPojo.getQuantity());
                //待减免的数量 = 待减免的数量 - 订单行的数量
                frontPiece = frontPiece - entryPojo.getQuantity();
            }

            //优惠掉的单价 = 原单价 - 现单价
            Double activityUnitPrice = DecimalCalculate.sub(entryPojo.getCalPrice(), skuUnitPrice);
            //订单行优惠的钱 = 优惠掉的单价 * 数量
            Double skuTotalActivityPrice = DecimalCalculate.mul(activityUnitPrice, entryPojo.getQuantity());
            activityDiscount = DecimalCalculate.add(activityDiscount, skuTotalActivityPrice);
        }
        //记录明细
        recordActivityDetail(activityId, activityDiscount, orderPojo, isCouponFlag);
    }

    /**
     * 商品固定价格
     *
     * @param parameterPojo 包含固定的价格
     *                      计算逻辑：
     *                      1. 计算订单行固定金额后的总价fixTotalFee
     *                      2. 用订单行总价减去固定金额的总价(totalFee - fixTotalFee)
     */
    public SimpleMessagePojo fixProductPrice(String activityId, OrderPojo orderPojo, ParameterPojo parameterPojo, Boolean isCouponFlag) {
        List<OrderEntryPojo> orderMatchedProducts = orderPojo.getMatchedProduct().getMatchedEntrys();
        //固定金额
        double fixPrice = Double.valueOf(parameterPojo.transValuePojo().getValue());
        //本次促销总优惠金额
        double totalDiscount = 0d;
        for (OrderEntryPojo entryPojo : orderMatchedProducts) {
            //固定金额后的订单行总价
            double fixTotalFee = DecimalCalculate.mul(fixPrice, entryPojo.getQuantity());
            //订单行优惠金额
            double entryDsicount = DecimalCalculate.sub(entryPojo.getTotalFee(), fixTotalFee);
            totalDiscount = DecimalCalculate.add(totalDiscount, entryDsicount);
            recordEntryPromoteFee(isCouponFlag, entryPojo, entryDsicount);
        }
        recordActivityDetail(activityId, totalDiscount, orderPojo, isCouponFlag);
        return new SimpleMessagePojo(true, MsgMenu.SUCCESS, null);
    }

    /**
     * 商品固定价格/折扣
     *
     * @param parameterPojo 包含 固定的折扣数据
     *                      计算逻辑：
     *                      1.如果是固定价格，优惠后单价为固定价格
     *                      2.如果是固定折扣，优惠后单价为(x折/10)*原单价
     *                      3.调用通用方法计算fees
     *                      4.调用通用方法计算activityPrice
     */
    public SimpleMessagePojo fixProductRate(String activityId, OrderPojo orderPojo, ParameterPojo parameterPojo, Boolean isCouponFlag) {
        //促销条件匹配到的订单行数据
        List<OrderEntryPojo> orderMatchedProducts = orderPojo.getMatchedProduct().getMatchedEntrys();
        double fixDiscount = Double.valueOf(parameterPojo.transValuePojo().getValue());
        //促销的总优惠金额
        double totalDiscount = 0d;
        for (OrderEntryPojo entryPojo : orderMatchedProducts) {

            double discountRate = DecimalCalculate.div(DecimalCalculate.sub(10d, fixDiscount), 10d);
            //打折后的单价 = (x折/10)*原单价  如:7折*100 = 70元
            Double entryDsicount = DecimalCalculate.mul(discountRate, entryPojo.getTotalFee());
            totalDiscount = DecimalCalculate.add(totalDiscount, entryDsicount);
            recordEntryPromoteFee(isCouponFlag, entryPojo, entryDsicount);
        }
        recordActivityDetail(activityId, totalDiscount, orderPojo, isCouponFlag);
        return new SimpleMessagePojo(true, MsgMenu.SUCCESS, null);
    }

    /**
     * 订单每满X元减Y元
     * <p>
     * 计算逻辑：
     * 1.订单总价/X元取整
     * 2.优惠掉的价格 = 取到的整数*Y元
     * 3.按照权重分摊掉订单行
     */
    public void calAmountMeetDiscount(String activityId, OrderPojo orderPojo, ParameterPojo parameterPojo, Boolean isCouponFlag) {
        //满x元
        String meetPrice = parameterPojo.transValuePojo().getFront();
        //减y元
        String fixDiscount = parameterPojo.transValuePojo().getValue();
        //向下取整
        Double meeDelete = Math.floor(DecimalCalculate.div(orderPojo.getNetAmount(), Double.valueOf(meetPrice)));
        //此次优惠订单条件范围内共减掉的钱 = 向下取整得到的数*每减的钱
        double discountAllPrice = DecimalCalculate.mul(meeDelete, Double.valueOf(fixDiscount));

        //设置订单头的促销或优惠券信息
        recordOrderPromoteFee(isCouponFlag, orderPojo, discountAllPrice);
        recordActivityDetail(activityId, discountAllPrice, orderPojo, isCouponFlag);
    }

    /**
     * 订单每满X件减y元
     * <p>
     * 计算逻辑：
     * 1.订单总价/X元取整
     * 2.优惠掉的价格 = 取到的整数*Y元
     * 3.按照权重分摊掉订单行
     */
    public void calPieceMeetDiscount(String activityId, OrderPojo orderPojo, ParameterPojo parameterPojo, Boolean isCouponFlag) {
        //一共减掉的钱
        Double discountAllPrice = 0D;
        //满x件
        String meetPiece = parameterPojo.transValuePojo().getFront();
        //减y元
        String fixDiscount = parameterPojo.transValuePojo().getValue();
        //订单商品数量
        Integer orderQuantity = orderPojo.getQuantity();
        Double meeDelete = Math.floor(DecimalCalculate.div(orderQuantity, Double.valueOf(meetPiece)));
        discountAllPrice = DecimalCalculate.mul(meeDelete, Double.valueOf(fixDiscount));

        //设置订单头的促销或优惠券信息
        recordOrderPromoteFee(isCouponFlag, orderPojo, discountAllPrice);
        //分摊行
        recordActivityDetail(activityId, discountAllPrice, orderPojo, isCouponFlag);
    }

    /**
     * 设置订单头的促销或优惠券信息
     *
     * @param isCouponFlag     是否是优惠券
     * @param orderPojo        订单
     * @param discountAllPrice 优惠或促销金额
     */
    private void recordOrderPromoteFee(Boolean isCouponFlag, OrderPojo orderPojo, Double discountAllPrice) {
        if (!isCouponFlag) {
            //促销
            if (DecimalCalculate.compareTo(orderPojo.getNetAmount(), discountAllPrice) >= 0) {
                //减免价格 = 原减免价格 +  这次减免的价格
                orderPojo.setDiscountFee(DecimalCalculate.add(orderPojo.getDiscountFee(), discountAllPrice));
            } else {
                orderPojo.setDiscountFee(orderPojo.getNetAmount());
            }

        } else {
            //优惠券
            if (DecimalCalculate.compareTo(orderPojo.getNetAmount(), discountAllPrice) > 0) {
                //优惠价格 = 原优惠价格 +  这次优惠的价格
                orderPojo.setCouponFee(DecimalCalculate.add(orderPojo.getCouponFee(), discountAllPrice));
            } else {
                orderPojo.setCouponFee(orderPojo.getNetAmount());
            }
        }
        orderCalculateService.apportionFee(orderPojo);
    }

    /**
     * 固定价格/折扣购买其他商品-配合容器使用
     *
     * @param discount 固定价格/折扣
     *                 计算逻辑：
     *                 1.遍历符合容器、遍历目标容器 计算出可以给目标容器优惠的份数
     *                 2.计算订单行优惠价格,按照前x(目标容器优惠的份数)件，固定价格/折扣计算
     */
    public SimpleMessagePojo fixedPriceBuy(String activityId, ActionPojo action, OrderPojo order, Double discount, String rateOrNumber, List<ContainerPojo> containers, Boolean isCouponFlag) {
        //是否执行结果的标志
        Boolean executeResultFlag = true;
        Integer matchValueMin = null;
        Integer targetValueMin = null;
        //本次促销优惠的钱
        Double activityDiscount = 0D;
        //订单中sku的信息
        List<OrderEntryPojo> entrys = order.getOrderEntryList();
        List<MatchValuePojo> matchValues = action.getParameters().getMatchValue();
        List<TargetValuePojo> targetValues = action.getParameters().getTargetValue();
        if (CollectionUtils.isEmpty(matchValues) || CollectionUtils.isEmpty(targetValues) || CollectionUtils.isEmpty(containers)) {
            logger.error("符合容器或目标容器不能为空");
            return new SimpleMessagePojo(false, null, null);
        }
        //1.遍历符合容器
        for (MatchValuePojo matchValue : matchValues) {
            String containerId = matchValue.getId();
            Integer countNumber = Integer.parseInt(matchValue.getCountNumber());
            ContainerPojo containerMatch = null;
            //根据containerId从条件容器中到对应容器
            for (ContainerPojo container : containers) {
                if (container.getId().equals(containerId)) {
                    containerMatch = container;
                    break;
                }
            }
            //订单中所有sku,用来与条件做交集
            List<String> childrenSkuCods = new ArrayList<>();
            for (OrderEntryPojo entry : entrys) {
                childrenSkuCods.add(entry.getProduct());
            }
            List<ChildPojo> children = containerMatch.getChild();
            OrderMatchedProductInfoPojo orderMatchedSku = new OrderMatchedProductInfoPojo();
            for (ChildPojo child : children) {
                List<OrderEntryPojo> skuCods = new ArrayList<>();
                List<String> rangeCodes = child.getParameters().getRangeValue().getValue();
                String rangOperator = child.getParameters().getRangeOperator().getValue();
                //商品范围
                if (child.getDefinitionId().equals(ConditionActions.o_product_range.getValue())) {
                    //汇总产品在范围中的个数
                    orderMatchedSku = CompareUtil.compareSkuRange(rangOperator, rangeCodes, order);
                    skuCods = orderMatchedSku.getMatchedEntrys();
                } else if (child.getDefinitionId().equals(ConditionActions.o_type_range.getValue())) {
                    //汇总产品在类别范围中的个数
                    orderMatchedSku = CompareUtil.compareSkuGdeptRange(rangOperator, rangeCodes, order);
                    skuCods = orderMatchedSku.getMatchedEntrys();
                }
                //取交集
                childrenSkuCods.retainAll(skuCods);
            }
            //如果订单内符合sku的数量小于容器数量 不执行结果
            if (orderMatchedSku.getProductTotalQty() < countNumber) {
                logger.info("订单内符合sku的数量{}小于匹配容器需要的数量{}，不执行结果", orderMatchedSku.getProductTotalQty(), countNumber);
                return new SimpleMessagePojo(false, null, null);
            }
            Integer containerSkuCount = 0;
            for (OrderEntryPojo entryPojo : entrys) {
                if (childrenSkuCods.contains(entryPojo.getProduct())) {
                    containerSkuCount = containerSkuCount + entryPojo.getQuantity();
                }
            }
            Integer num = containerSkuCount / countNumber;
            if (matchValueMin == null) {
                matchValueMin = num;
            } else if (num < matchValueMin) {
                matchValueMin = num;
            }
        }

        //将目标容器的sku放入map
        Map<String, List<String>> targetMap = new HashMap<>();
        //2.遍历目标容器
        for (TargetValuePojo targetValue : targetValues) {
            String containerId = targetValue.getId();
            Integer countNumber = Integer.parseInt(targetValue.getCountNumber());
            ContainerPojo containerMatch = null;
            //根据containerId从条件容器中到对应容器
            for (ContainerPojo container : containers) {
                if (container.getId().equals(containerId)) {
                    containerMatch = container;
                    break;
                }
            }
            //订单中所有sku,用来与条件做交集
            List<String> childrenSkuCods = new ArrayList<>();
            for (OrderEntryPojo sku : entrys) {
                childrenSkuCods.add(sku.getProduct());
            }
            List<ChildPojo> children = containerMatch.getChild();
            OrderMatchedProductInfoPojo orderMatchedSku = new OrderMatchedProductInfoPojo();
            for (ChildPojo child : children) {
                List<OrderEntryPojo> skuCods = new ArrayList<>();
                List<String> rangeCodes = child.getParameters().getRangeValue().getValue();
                String rangOperator = child.getParameters().getRangeOperator().getValue();
                //商品范围
                if (child.getDefinitionId().equals(ConditionActions.o_product_range.getValue())) {
                    //汇总产品在范围中的个数
                    orderMatchedSku = CompareUtil.compareSkuRange(rangOperator, rangeCodes, order);
                    skuCods = orderMatchedSku.getMatchedEntrys();
                } else if (child.getDefinitionId().equals(ConditionActions.o_type_range.getValue())) {
                    //汇总产品在类别范围中的个数
                    orderMatchedSku = CompareUtil.compareSkuGdeptRange(rangOperator, rangeCodes, order);
                    skuCods = orderMatchedSku.getMatchedEntrys();
                }
                //取交集
                childrenSkuCods.retainAll(skuCods);
            }
            //如果订单内符合sku的数量小于容器数量 不执行结果
            if (orderMatchedSku.getProductTotalQty() < countNumber) {
                logger.info("订单内符合sku的数量{}小于目标容器需要的数量{}，不执行结果", orderMatchedSku.getProductTotalQty(), countNumber);
                return new SimpleMessagePojo(false, null, null);
            }
            Integer containerSkuCount = 0;
            for (OrderEntryPojo productSku : entrys) {
                if (childrenSkuCods.contains(productSku.getProduct())) {
                    containerSkuCount = containerSkuCount + productSku.getQuantity();
                }
            }
            Integer num = containerSkuCount / countNumber;
            if (targetValueMin == null) {
                targetValueMin = num;
            } else if (num < targetValueMin) {
                targetValueMin = num;
            }
            targetMap.put(targetValue.getId(), childrenSkuCods);
        }
        //用于计算目标容器计算价格
        Integer calNum = matchValueMin;
        //取最小值
        if (matchValueMin >= targetValueMin) {
            calNum = targetValueMin;
        }
        for (TargetValuePojo targetValue : targetValues) {
            //可以在这个容器里已X元购买的数量
            Integer limitNum = calNum * Integer.parseInt(targetValue.getCountNumber());
            //这个容器中匹配的sku
            List<String> activitySkus = targetMap.get(targetValue.getId());
            for (OrderEntryPojo entryPojo : entrys) {
                if (activitySkus.contains(entryPojo.getProductId())) {

                    Double discountMoney = 0D;
                    if (PromotionConstants.DISCOUNT_TYPE_NUMBER.equals(rateOrNumber)) {
                        discountMoney = discount;
                    } else if (PromotionConstants.DISCOUNT_TYPE_RATE.equals(rateOrNumber)) {
                        //打折后价格 = (x折/10)*原单价
                        discountMoney = DecimalCalculate.sub(entryPojo.getCalPrice(), DecimalCalculate.mul(DecimalCalculate.div(discount, 10), entryPojo.getCalPrice()));
                    }

                    Double skuUnitPrice = 0D;
                    if (entryPojo.getQuantity() >= limitNum) {
                        // 优惠后的总价 = 原单价*数量 - 前X件*(原单价-y元)
                        Double skuTotalPrice = DecimalCalculate.sub(DecimalCalculate.mul(entryPojo.getCalPrice(), entryPojo.getQuantity()), DecimalCalculate.mul(limitNum, DecimalCalculate.sub(entryPojo.getCalPrice(), discountMoney)));
                        //优惠后的单价 = 优惠后的总价/数量
                        skuUnitPrice = DecimalCalculate.div(skuTotalPrice, entryPojo.getQuantity());
                    } else { // 5 10
                        // 优惠后的总价 = 原单价*数量 - 前X件*减y元
                        Double skuTotalPrice = DecimalCalculate.sub(DecimalCalculate.mul(entryPojo.getCalPrice(), entryPojo.getQuantity()), DecimalCalculate.mul(entryPojo.getQuantity(), discountMoney));
                        //优惠后的单价 = 优惠后的总价/数量
                        skuUnitPrice = DecimalCalculate.div(skuTotalPrice, entryPojo.getQuantity());
                        //待减免的数量 = 待减免的数量 - 订单行的数量
                        limitNum = limitNum - entryPojo.getQuantity();
                    }

                    //优惠掉的单价
                    Double activityUnitPrice = entryPojo.getCalPrice() - skuUnitPrice;
                    //订单行总优惠掉的钱 = 优惠掉的单价 * 数量
                    Double activityAllPrice = DecimalCalculate.mul(activityUnitPrice, entryPojo.getQuantity());
                    activityDiscount = DecimalCalculate.add(activityDiscount, activityAllPrice);
                }
            }
        }
        //更新订单activityPrice
        //记录明细
        recordActivityDetail(activityId, activityDiscount, order, isCouponFlag);
        return new SimpleMessagePojo();
    }

    /**
     * 目标包价格-配合容器使用
     *
     * @param parameters 固定价格
     *                   计算逻辑：
     *                   1.遍历遍历目标容器 计算出可以给目标容器优惠的份数
     */
    public SimpleMessagePojo targetPrice(String activityId, OrderPojo orderPojo, ParameterPojo parameters, List<ContainerPojo> containers, Boolean isCouponFlag) {
        //获取目标包中的目标容器
        List<TargetValuePojo> targetContainers = parameters.getTargetValue();

        //保存容器id与容器的映射关系
        final Map<String, ContainerPojo> idContainerMapping = new HashMap<>(containers.size());
        containers.forEach(containerPojo -> {
            idContainerMapping.put(containerPojo.getId(), containerPojo);

        });

        //所有容器匹配的容器id-->订单行集合 映射
        Map<String, List<OrderEntryPojo>> targetEntryMapping = new HashMap<>();

        //根据目标包中的容器获取容器匹配的订单行
        for (TargetValuePojo targetContainer : targetContainers) {
            String targetContainerId = targetContainer.getId();
            //获取对应的容器
            ContainerPojo containerPojo = idContainerMapping.get(targetContainerId);
            //获取容器中包含的条件集合
            OrderMatchedProductInfoPojo containerMatchedInfo = CompareUtil.findContainerMatchedEntrys(orderPojo, containerPojo);
            if (containerMatchedInfo.getProductTotalQty().intValue() < Integer.parseInt(targetContainer.getCountNumber())) {
                logger.info("订单内符合容器条件的商品数量{}小于目标容器需要的数量{}，不执行结果", containerMatchedInfo.getProductTotalQty(), targetContainer.getCountNumber());
                return new SimpleMessagePojo(false, null, null);
            }
            targetEntryMapping.put(containerPojo.getId(), containerMatchedInfo.getMatchedEntrys());
        }

        //计算“目标容器”的最大优惠次数
        int targetTimes = getMaxTimes(targetEntryMapping, targetContainers);
        //最大执行次数为0，促销异常
        if (targetTimes <= 0) {
            logger.info("订单能参与套装促销{}的次数为{}", activityId, targetTimes);
            return new SimpleMessagePojo(false, MsgMenu.PROMOTE_DATA_ERR, null);
        }

        //获取目标价格
        String value = parameters.getValue();
        //计算目标包优惠金额
        SimpleMessagePojo computeResult = calTargetFee(targetContainers, targetEntryMapping, targetTimes, Double.parseDouble(value));
        if (!computeResult.isSuccess()) {
            return computeResult;
        }
        recordActivityDetail(activityId, (double) computeResult.getObj(), orderPojo, isCouponFlag);
        return new SimpleMessagePojo();

    }


    /**
     * 计算订单能满足套装的最大次数
     *
     * @param targetEntryMapping 目标容器-->容器订单行集合映射
     * @param targetContainer    目标容器
     * @return maxTimes 最大套装计算次数
     */
    protected Integer getMaxTimes(Map<String, List<OrderEntryPojo>> targetEntryMapping, List<TargetValuePojo> targetContainer) {
        Integer maxTimes = Integer.MAX_VALUE;
        for (TargetValuePojo container : targetContainer) {
            Integer count = Integer.parseInt(container.getCountNumber());
            //匹配容器的订单行还能参与套装促销的数量
            Integer entryRemaindQty = 0;
            for (OrderEntryPojo orderEntryPojo : targetEntryMapping.getOrDefault(container.getId(), new ArrayList<>())) {
                entryRemaindQty = DecimalCalculate.add(entryRemaindQty, orderEntryPojo.getBundleRemainder());
            }
            if (maxTimes >= entryRemaindQty / count) {
                //取最小值作为最大优惠次数
                maxTimes = entryRemaindQty / count;
            }
        }
        return maxTimes;
    }

    /**
     * 计算目标包优惠金额,返回优惠总金额
     *
     * @param targetContainers 目标包中包含的容器
     * @param maxTimes         套装最大优惠次数
     * @param value            套装固定金额
     */
    private SimpleMessagePojo calTargetFee(List<TargetValuePojo> targetContainers, Map<String, List<OrderEntryPojo>> targetEntryMapping, int maxTimes, Double value) {

        //所有优惠商品的总价
        Double preEntryTotalFee = 0d;
        //要参与分摊计算的订单行,Map<OrderEntryPojo,Double> 订单行----订单行参与促销计算金额 mapping
        Map<OrderEntryPojo, Double> appronEntryInfo = new HashMap<>();
        //计算参与到套装优惠的所有订单行的商品总金额
        out:
        for (TargetValuePojo targetContainer : targetContainers) {
            //容器要优惠的订单行数量
            Integer discountQty = DecimalCalculate.mul(maxTimes, Integer.parseInt(targetContainer.getCountNumber()));
            //目标容器匹配的订单行
            List<OrderEntryPojo> targetOrderEntryPojos = targetEntryMapping.getOrDefault(targetContainer.getId(), new ArrayList<>());
            //校验容器中的套装值是否还能参与本次促销
            if (!isEnoughRemaind(targetOrderEntryPojos, discountQty)) {
                return new SimpleMessagePojo(false, MsgMenu.CAL_FALSE, null).setCheckMsg("容器中可参与套装计算的商品数量不足");
            }

            //按单价升序排列,统计参与套装计算的订单行,及订单行金额
            this.entrysSortByPrice(targetOrderEntryPojos, false);
            for (OrderEntryPojo orderEntry : targetOrderEntryPojos) {

                // 执行本次促销
                if (orderEntry.getBundleRemainder() <= 0) {
                    logger.info("订单行{}能用于套装计算的数量已归零", orderEntry.getProduct());
                    continue out;
                }
                if (discountQty <= 0) {
                    continue;
                }
                double pieceRate;
                if (discountQty >= orderEntry.getBundleRemainder()) {
                    discountQty = discountQty - orderEntry.getBundleRemainder();
                    pieceRate = DecimalCalculate.div(orderEntry.getBundleRemainder(), orderEntry.getQuantity());
                    orderEntry.setBundleRemainder(0);
                } else {
                    pieceRate = DecimalCalculate.div(discountQty, orderEntry.getQuantity());
                    orderEntry.setBundleRemainder(orderEntry.getBundleRemainder() - discountQty);
                    discountQty = 0;
                }
                //订单行参与促销分摊的金额
                double entryFee = DecimalCalculate.mul(orderEntry.getTotalFee(), pieceRate);
                preEntryTotalFee = DecimalCalculate.add(preEntryTotalFee, entryFee);
                appronEntryInfo.put(orderEntry, entryFee);
            }
        }

        //总优惠金额=上一步的商品总金额-目标包价格*优惠次数
        final double targetDiscount = DecimalCalculate.mul(value, maxTimes);
        final double totalDiscount = DecimalCalculate.sub(preEntryTotalFee, targetDiscount);
        //已分摊金额
        Double usedFee = 0.0;
        //分摊套装优惠到套装中的订单行
        Set<OrderEntryPojo> entryPojoSet = appronEntryInfo.keySet();
        List<OrderEntryPojo> orderEntryPojos = new ArrayList<>(entryPojoSet);
        for (int i = 0; i < orderEntryPojos.size(); i++) {
            OrderEntryPojo entryPojo = orderEntryPojos.get(i);
            if (i == orderEntryPojos.size() - 1) {
                double entryDiscount = DecimalCalculate.sub(totalDiscount, usedFee);
                entryPojo.setDiscountFee(entryDiscount);
            } else {
                double appronRate = DecimalCalculate.div(appronEntryInfo.get(entryPojo), preEntryTotalFee);
                double entryDiscount = DecimalCalculate.mul(appronRate, totalDiscount);
                double roundDiscount = DecimalCalculate.round(entryDiscount, 2);
                entryPojo.setDiscountFee(roundDiscount);
                usedFee = DecimalCalculate.add(usedFee, roundDiscount);
            }
        }
        return new SimpleMessagePojo(true, MsgMenu.SUCCESS, totalDiscount);
    }

    /**
     * 校验容器中的订单行商品数量是否能够参与套装计算
     *
     * @param entryPojos 要校验的容器中的订单行
     * @param countNum   容器中要优惠的商品数量
     * @return
     */
    boolean isEnoughRemaind(List<OrderEntryPojo> entryPojos, int countNum) {
        Integer entryRemaindQty = 0;
        for (OrderEntryPojo orderEntryPojo : entryPojos) {
            entryRemaindQty = DecimalCalculate.add(entryRemaindQty, orderEntryPojo.getBundleRemainder());
        }
        if (entryRemaindQty < countNum) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * 赠品结果
     * action.parameter.value.value 中存储赠品信息,格式为:
     * [{\"totalNumber\":123,\"productCode\":20001,\"name\":\"鸢尾花开1\",\"countNumber\":12}]
     * <p>
     * totalNumber为总赠送数量,productCode为赠品的商品编码 name为赠品名称,countNumber为单次促销赠送的数量
     *
     * @param orderPojo
     * @param activityId
     */
    public SimpleMessagePojo distributedGifts(String activityId, OrderPojo orderPojo, ParameterPojo parameterPojo, Boolean isCouponFlag) {
        //获取赠品数据
        String giftValue = parameterPojo.transValuePojo().getValue();
        JSONArray gifts = JSON.parseArray(giftValue);
        List<GiftPojo> giftPojos = new ArrayList<>();
        for (int i = 0; i < gifts.size(); i++) {
            JSONObject giftObj = gifts.getJSONObject(i);
            GiftPojo giftPojo = new GiftPojo();
            giftPojo.setBasePrice(300d);
            giftPojo.setProductId(giftObj.getString("productCode"));
            giftPojo.setQuantity(giftObj.getInteger("countNumber"));
            giftPojo.setvProductCode("GIFT001");
            giftPojo.setDefaultDelivery("EXPRESS");
            giftPojos.add(giftPojo);
        }

        orderPojo.setGiftPojos(giftPojos);
        recordActivityDetail(activityId, 0d, orderPojo, isCouponFlag);
        return new SimpleMessagePojo();
    }

    /**
     * 计算订单商品范围内的总价
     * 注意：其中单价是去折后的单价
     *
     * @param orderMatchedSkus 范围内的sku
     */

    public Double findPriceBySkuRange(List<OrderEntryPojo> orderMatchedSkus) {
        Double totalPrice = 0D;
        for (OrderEntryPojo entryPojo : orderMatchedSkus) {
            Double unitPrice = 0D;
            //单价-优惠后单价
            unitPrice = Double.valueOf(entryPojo.getCalPrice());
            //sku总价 = 单价*数量
            Double skuTotalPrice = DecimalCalculate.mul(unitPrice, entryPojo.getQuantity());
            totalPrice = DecimalCalculate.add(totalPrice, skuTotalPrice);
        }
        return totalPrice;
    }


    /**
     * 记录促销明细（每个促销优惠掉的总金额）
     */
    public void recordActivityDetail(String promotionId, Double discount, OrderPojo orderPojo, Boolean isCouponFlag) {
        //使用优惠券不记录
        if (isCouponFlag) {
            return;
        } else {
            PromotionActivitiesPojo promotionDetail = promotionActivityService.findByPk(promotionId);
            ActivityPojo activity = promotionDetail.getActivity();
            ActivityDetailPojo detail = new ActivityDetailPojo();
            detail.setActivityId(promotionId);
            detail.setDiscount(discount);
            detail.setStartDate(DateFormatUtil.timeStampToString(activity.getStartDate().toString()));
            detail.setEndDate(DateFormatUtil.timeStampToString(activity.getEndDate().toString()));
            detail.setActivityDesp(activity.getActivityDes());
            detail.setPageShowMes(activity.getPageShowMes());
            List<ActivityDetailPojo> usedActivities = orderPojo.getUsedActivities();
            if (CollectionUtils.isEmpty(usedActivities)) {
                usedActivities = new ArrayList<>();
                orderPojo.setUsedActivities(usedActivities);
            }
            usedActivities.add(detail);
        }
    }


}
