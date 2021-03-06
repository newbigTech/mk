package com.hand.promotion.dubboService;

import com.alibaba.dubbo.config.annotation.Service;

import com.hand.dto.ConvertCouponParm;
import com.hand.dto.OperateCouponParm;
import com.hand.dto.ResponseData;
import com.hand.hpromotion.ISaleCustomerCouponService;
import com.hand.promotion.pojo.SimpleMessagePojo;
import com.hand.promotion.pojo.coupon.CouponsPojo;
import com.hand.promotion.pojo.coupon.CustomerCouponPojo;
import com.hand.promotion.pojo.coupon.PromotionCouponsPojo;
import com.hand.promotion.service.ICouponService;
import com.hand.promotion.service.ICustomerCouponService;
import com.hand.promotion.util.DateFormatUtil;
import com.hand.promotion.util.ResponseReturnUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户已发放优惠券Controller，用于发放，查询优惠券使用情况信息
 * Created by darkdog on 2018/2/2.
 */
public class SaleCustomerCouponService implements ISaleCustomerCouponService {

    @Autowired
    private ICustomerCouponService customerCouponService;
    @Autowired
    private ICouponService saleCouponService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 用户兑换领取优惠券
     *
     * @param convertCouponParm 兑换券信息
     * @return
     */
    @Override
    public ResponseData convert(ConvertCouponParm convertCouponParm) {
        if (null == convertCouponParm) {
            return new ResponseData(false, "NULL_DATA");
        }

        SimpleMessagePojo convertResult = null;
        try {
            convertResult = customerCouponService.convertCoupon(convertCouponParm);
            return ResponseReturnUtil.transSimpleMessage(convertResult);
        } catch (Exception e) {
            logger.error("----兑换优惠券异常---", e);
            e.printStackTrace();
            return ResponseReturnUtil.returnFalseResponse("优惠券兑换异常>>" + e.getMessage(), "CONVERT_COUPON_ERR");

        }
    }

    /**
     * 根据userId查询该用户下的优惠券
     *
     * @param
     * @return
     */
    @Override
    public ResponseData queryByUserId(Map<String, Object> map) {
        try {
            ResponseData responseData = new ResponseData();
            String userId = (String) map.get("userId");
            String status = (String) map.get("status");
            List<CustomerCouponPojo> customerCouponPojos = customerCouponService.queryByUserIdAndStatus(userId, status);
            List<Map> collect = customerCouponPojos.stream().map(customerCouponPojo -> {
                PromotionCouponsPojo promotionCouponsPojo = saleCouponService.selectCouponDetail(customerCouponPojo.getCid());
                if (null == promotionCouponsPojo) {
                    return null;
                }
                Map resultMap = new HashMap();
                CouponsPojo coupon = promotionCouponsPojo.getCoupon();
                resultMap.put("couponName", coupon.getCouponName());
                resultMap.put("quantity", 1);
                resultMap.put("orderId", customerCouponPojo.getOrderId());
                resultMap.put("description", coupon.getCouponDes());
                resultMap.put("benefit", customerCouponPojo.getBenefit());
                resultMap.put("startTime", DateFormatUtil.timeStampToString(customerCouponPojo.getStartDate().toString()));
                resultMap.put("discountType", coupon.getDiscountType());
                resultMap.put("endTime", DateFormatUtil.timeStampToString(customerCouponPojo.getEndDate().toString()));
                resultMap.put("couponCode", customerCouponPojo.getCouponCode());
                resultMap.put("status", customerCouponPojo.getStatus());
                return resultMap;
            }).collect(Collectors.toList());
            responseData.setResp(collect);
            return responseData;

        } catch (Exception e) {
            logger.error("---查询优惠券异常", e);
            return ResponseReturnUtil.returnFalseResponse("查询优惠券异常" + e.getMessage(), "QUERY_COUPON_ERR");
        }
    }

    /**
     * 优惠券占用与释放接口
     *
     * @param parm
     * @return
     */
    @Override
    public ResponseData setUsed(OperateCouponParm parm) {
        SimpleMessagePojo operateResult = customerCouponService.setUsed(parm);
        return ResponseReturnUtil.transSimpleMessage(operateResult);
    }

    /**
     * 生成兑换码
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData generateRedeemCode(Map map) {
        try {
            String code = (String) map.get("couponCode");
            int num = (int) map.get("num");
            if (StringUtils.isEmpty(code) || num <= 0) {
                return new ResponseData(false, "兑换数量要大于0");
            }
            SimpleMessagePojo generateResult = customerCouponService.generateRedeemCode(code, num);
            return ResponseReturnUtil.transSimpleMessage(generateResult);
        } catch (Exception e) {
            return ResponseReturnUtil.returnFalseResponse("生成兑换码异常" + e.getMessage(), "GEN_COUPON_REDEEM_ERR");
        }
    }

    /**
     * 查询优惠券码下的所有兑换码
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData getRedeemCodes(Map map) {
        String couponCode = (String) map.get("couponCode");
        if (StringUtils.isEmpty(couponCode)) {
            return new ResponseData(false, "优惠券码不能为空");
        }
        SimpleMessagePojo redeemCodes = customerCouponService.getRedeemCodes(couponCode);
        return ResponseReturnUtil.transSimpleMessage(redeemCodes);
    }


}
