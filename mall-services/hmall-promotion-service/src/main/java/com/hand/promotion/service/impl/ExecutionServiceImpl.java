package com.hand.promotion.service.impl;


import com.alibaba.fastjson.JSON;
import com.hand.dto.ResponseData;
import com.hand.promotion.cache.HepBasicDataCacheInstance;
import com.hand.promotion.pojo.SimpleMessagePojo;
import com.hand.promotion.pojo.activity.PromotionActivitiesPojo;
import com.hand.promotion.pojo.activity.SalePromotionCodePojo;
import com.hand.promotion.pojo.enums.MsgMenu;
import com.hand.promotion.pojo.order.MallPromotionResult;
import com.hand.promotion.pojo.order.OrderPojo;
import com.hand.promotion.service.*;
import com.hand.promotion.util.ResponseReturnUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2018/2/5
 * @description 商城, 中台促销执行, 重算接口. 查询商品关联的促销活动
 */
@Service
public class ExecutionServiceImpl implements IExecutionService {
    @Autowired
    private ISalePromotionCodeService salePromotionCodeService;

    @Autowired
    private IExcutePromoteService excutePromoteService;

    @Autowired
    private IExcuteCouponService excuteCouponService;

    @Autowired
    private IOrderCalculateService orderCalculateService;

    @Autowired
    private HepBasicDataCacheInstance<PromotionActivitiesPojo> orderActivityCacheInstance;

    @Autowired
    private ICalInstalltionFeeService calInstalltionFeeService;

    @Autowired
    private ICalShippingFeeService calShippingFeeService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 商城\中台调用促销执行接口
     *
     * @param order 进行促销计算的订单数据
     * @return
     */
    @Override
    public ResponseData promoteOrder(OrderPojo order) {
        ResponseData responseData = new ResponseData();
        SimpleMessagePojo checkResult = orderCalculateService.ckeckOrderInvalid(order);
        if (!checkResult.isSuccess()) {
            return ResponseReturnUtil.transSimpleMessage(checkResult);
        }

        //保留入参数据的备份
        OrderPojo copy = order.copy();

        //添加订单必须字段
        SimpleMessagePojo appendResult = orderCalculateService.appendNecessaryField(order);
        if (!appendResult.isSuccess()) {
            logger.error("-----订单数据不合法,返回入参数据---");
            return ResponseReturnUtil.transSimpleMessage(appendResult);
        }

        //计算安装费
        calInstalltionFeeService.calOrderInstallFee(order);

        //计算运费
        ResponseData calShipRest = calShippingFeeService.caculateFreight(order);
        if (!calShipRest.isSuccess()) {
            return calShipRest;
        }

        //计算订单金额
        orderCalculateService.computePromotPrice(order);
        try {
            //执行商品层级促销活动
            excutePromoteService.orderEntryPromote(order);

            //查询可用订单层级促销
            excutePromoteService.payByActivity(order);

            //查询可用优惠券
            excuteCouponService.optionCoupon(order);

            //执行选择的促销活动
            String chosenPromotionId = order.getChosenPromotion();
            if (!StringUtils.isEmpty(chosenPromotionId)) {
                PromotionActivitiesPojo chosenPromotion = orderActivityCacheInstance.getByKey(chosenPromotionId);
                if (null != chosenPromotion) {
                    SimpleMessagePojo executeResult = excutePromoteService.executePromotion(chosenPromotion, order);
                    responseData.setMsg(executeResult.getCheckMsg());
                } else {
                    responseData.setMsg("选择的促销活动不存在");
                    responseData.setSuccess(false);
                }
            }

            //执行选择的优惠券
            String chosenCouponId = order.getChosenCoupon();
            if (!StringUtils.isEmpty(chosenCouponId)) {
                SimpleMessagePojo executeResult = excuteCouponService.executeChosenCoupon(chosenCouponId, order);
                responseData.setMsg(responseData.getMsg() + executeResult.getCheckMsg());
            }
        } catch (Exception e) {
            logger.error("-----执行促销活动异常,返回原订单数据---", e);
            MallPromotionResult mallPromotionResult = orderCalculateService.transToReturnPojo(copy);
            responseData.setResp(Arrays.asList(mallPromotionResult));
            responseData.setSuccess(false);
            responseData.setMsg("执行促销活动异常,返回原订单数据" + e.getMessage());
        }
        logger.info("--------执行促销结果--------{}", JSON.toJSONString(responseData));

        //促销金额分摊
        orderCalculateService.apportionFee(order);

        //转换返回数据到商城
        MallPromotionResult mallPromotionResult = orderCalculateService.transToReturnPojo(order);
        responseData.setResp(Arrays.asList(mallPromotionResult));
        return responseData;
    }


    /**
     * 查询商品对应的促销活动
     *
     * @param productMap
     * @return
     */
    @Override
    public ResponseData getActivity(Map productMap) {
        ResponseData responseData = new ResponseData();
        String code = productMap.get("code").toString();
        List<SalePromotionCodePojo> codeUsefulPromos = salePromotionCodeService.findCodeUsefulPromo(code);
        List list = new ArrayList<>();
        for (SalePromotionCodePojo codeUsefulPromo : codeUsefulPromos) {
            Map activityMid = new HashMap<>();
            activityMid.put("activityId", codeUsefulPromo.getActivityId());
            activityMid.put("activityName", codeUsefulPromo.getMeaning());
            activityMid.put("activityDes", codeUsefulPromo.getMeaning());
            activityMid.put("pageShowMes", codeUsefulPromo.getMeaning());
            list.add(activityMid);
        }
        responseData.setMsg(MsgMenu.SUCCESS.getMsg());
        responseData.setMsgCode(MsgMenu.SUCCESS.getCode());
        responseData.setResp(list);
        return responseData;
    }
}
