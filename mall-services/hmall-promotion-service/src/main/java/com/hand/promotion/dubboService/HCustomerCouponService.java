package com.hand.promotion.dubboService;

import com.alibaba.fastjson.JSON;
import com.hand.dto.ResponseData;
import com.hand.hpromotion.IHCustomerCouponService;
import com.hand.promotion.pojo.SimpleMessagePojo;
import com.hand.promotion.pojo.coupon.AdminConvertParm;
import com.hand.promotion.service.ICustomerCouponService;
import com.hand.promotion.util.BeanMapExchange;
import com.hand.promotion.util.ResponseReturnUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 用于MAP调用查询优惠券相关数据
 * Created by darkdog on 2018/2/2.
 */

public class HCustomerCouponService implements IHCustomerCouponService {

    @Autowired
    private ICustomerCouponService customerCouponService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 管理员发放优惠券
     *
     * @param convertMap 发券参数
     * @return
     */
    @Override
    public ResponseData convertByAdmin(Map<String, Object> convertMap) {
        try {
            AdminConvertParm adminConvertParm = BeanMapExchange.mapToObject(convertMap, AdminConvertParm.class);
            SimpleMessagePojo resultMsg = customerCouponService.convertCouponByAdmin(adminConvertParm);
            return ResponseReturnUtil.transSimpleMessage(resultMsg);
        } catch (Exception e) {
            logger.error("-------管理员发券异常---", e);
            return ResponseReturnUtil.returnFalseResponse(e.getMessage(), "CONVERT_COUPON_ERR");
        }
    }

    /**
     * HAP 根据userId查询该用户下的优惠券
     *
     * @param userId
     * @return
     */
    @Override
    public ResponseData queryByUserId(String userId, int page, int pagesize) {
        return customerCouponService.queryByUserId(userId, page, pagesize);
    }

    /**
     * 根据优惠券（coupon）id和用户id查询优惠券
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData queryByCidAndUserIds(Map<String, Object> map) {
        logger.info("--------------------queryByCidAndUserIds----------------------\n{}", JSON.toJSONString(map));

        Map data = (Map) map.get("data");
        ResponseData responseData = customerCouponService.queryByCidAndUserIds((String) data.get("mobileNumber"), (String) data.get("cid"),  (String) data.get("name"),(int) map.get("page"),(int) map.get("pageSize"));
        logger.info("--------------------resp----------------------\n{}", JSON.toJSONString(responseData));
        return responseData;
    }


}
