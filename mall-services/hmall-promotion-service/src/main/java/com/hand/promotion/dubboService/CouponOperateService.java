package com.hand.promotion.dubboService;

import com.hand.dto.ResponseData;
import com.hand.hpromotion.ICouponOperateService;
import com.hand.promotion.service.ICouponService;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Created by changbingquan on 2018/2/8.
 * 优惠券推送接口调用
 */
public class CouponOperateService implements ICouponOperateService {

    @Autowired
    private ICouponService couponService;

    /**
     * 优惠券占用释放接口
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData operateCustomerCoupon(Map map) {
        return null;
    }

    /**
     * 获取需同步的coupon
     *
     * @return
     */
    @Override
    public ResponseData getSynCoupon() throws ParseException {
        return couponService.getSynCoupon();
    }

    /**
     * 设置优惠券同步标识
     *
     * @param couponCodes 优惠卷码
     * @return
     */
    @Override
    public ResponseData setCouponSyn(List<String> couponCodes) {
        couponService.setCouponSyn(couponCodes);
        return new ResponseData();
    }
}
