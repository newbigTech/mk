package com.hand.promotion.service;

import com.hand.dto.ConvertCouponParm;
import com.hand.dto.OperateCouponParm;
import com.hand.dto.ResponseData;
import com.hand.promotion.pojo.SimpleMessagePojo;
import com.hand.promotion.pojo.coupon.AdminConvertParm;
import com.hand.promotion.pojo.coupon.CouponsPojo;
import com.hand.promotion.pojo.coupon.CustomerCouponPojo;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2018/1/11
 * @description 已发放优惠券service
 */
public interface ICustomerCouponService {

    /**
     * 用户领取优惠券
     *
     * @return
     */
    SimpleMessagePojo convertCoupon(ConvertCouponParm convertCouponParm) ;

    /**
     * 管理员发放优惠券
     *
     * @return
     */
    SimpleMessagePojo convertCouponByAdmin(AdminConvertParm adminConvertParm) ;

    /**
     * 根据用户账户查询用户已拥有的优惠券
     *
     * @param userId
     * @param page
     * @param pagesize
     * @return
     */
    ResponseData queryByUserId(String userId, int page, int pagesize);

    /**
     * 根据发券账户和优惠券状态查询已发放优惠券
     *
     * @param userId 发券账户
     * @param status 优惠券状态
     * @return
     */
    List<CustomerCouponPojo> queryByUserIdAndStatus(String userId, String status) ;

    /**
     * 根据发券账户和关联的优惠券主键查询已发放优惠券
     *
     * @param userId 发券账户
     * @param id     已发放优惠券主键
     * @return
     */
    List<CustomerCouponPojo> queryByUserIdAndId(String userId, String id) ;


    /**
     * 执行优惠券时查询用户使用的的优惠券
     * 查询标准: status 为STATUS_01(可使用) startDate(生效时间)<currentTime(当前时间)<endDate(失效时间)
     *
     * @param userId 发券账户
     * @return 可用优惠券
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    List<CustomerCouponPojo> queryUsefulCusCoupon(String userId);

    /**
     * 查询优惠券发放情况
     *
     * @param mobileNum 优惠券发放账户
     * @param cid       优惠券主键
     * @param name      领券人姓名
     * @return
     */
    ResponseData queryByCidAndUserIds(String mobileNum, String cid, String name, int page, int pagesize);

    /**
     * 根据优惠券状态更新已发放优惠券状态
     *
     * @param couponsPojo 要更新的已发放优惠券关联的优惠券
     */
    void updateCustomerCouponStatus(CouponsPojo couponsPojo) ;

    /**
     * 更新活动中的,可使用状态的已发放优惠券状态
     *
     * @param cid 要更新的已发放优惠券关联的优惠券信息
     */
    void updateActiveCouponStatus(String cid) ;

    /**
     * 占用释放优惠券
     *
     * @param operateCouponParm 占用释放优惠券相关参数
     * @return
     */
    SimpleMessagePojo setUsed(OperateCouponParm operateCouponParm);

    /**
     * 生成优惠券兑换码
     *
     * @param code 要生成兑换码的优惠券编码
     * @param num  生成的兑换码数量
     * @return
     */
    SimpleMessagePojo generateRedeemCode(String code, int num) ;

    /**
     * 根据优惠券编码couponCode查询优惠券的兑换码
     *
     * @param couponCode 优惠券编码
     * @return
     */
    SimpleMessagePojo getRedeemCodes(String couponCode) ;

    void insert(CustomerCouponPojo customerCouponPojo);

    void batchInsert(List<CustomerCouponPojo> customerCouponPojos);

}
