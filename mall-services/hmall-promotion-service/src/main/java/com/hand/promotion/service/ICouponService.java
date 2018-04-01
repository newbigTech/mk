package com.hand.promotion.service;

import com.hand.dto.ResponseData;
import com.hand.promotion.pojo.SimpleMessagePojo;
import com.hand.promotion.pojo.coupon.PromotionCouponsPojo;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2018/1/8
 * @description 优惠券操作service
 */
public interface ICouponService {
    /**
     * 创建优惠券
     *
     * @param couponsPojo 要保存的优惠券信息
     * @param userId      修改人信息
     * @return
     */
    ResponseData submit(PromotionCouponsPojo couponsPojo, String userId);

    /**
     * 更新优惠券数据,同时同步到缓存
     *
     * @param couponsPojo
     * @return
     */
    SimpleMessagePojo updateCoupon(PromotionCouponsPojo couponsPojo);


    /**
     * 更新优惠券状态
     *
     * @param couponsPojo
     * @return
     */
    public SimpleMessagePojo updateCouponStatus(PromotionCouponsPojo couponsPojo);

    /**
     * 分页查询优惠券数据
     *
     * @param pojo
     * @param pageNum
     * @param pageSize
     * @return
     * @throws ParseException
     */
    ResponseData query(PromotionCouponsPojo pojo, int pageNum, int pageSize);

    /**
     * @param id 主键
     * @return
     */
    PromotionCouponsPojo selectCouponDetail(String id);

    /**
     * 根据优惠券主键启用优惠券
     *
     * @param id
     * @return
     */
    SimpleMessagePojo startUsing(String id);

    /**
     * 根据优惠券主键停用优惠券
     *
     * @param id 优惠券主键
     * @return
     */
    SimpleMessagePojo endUsing(String id);

    /**
     * 根据优惠券编码查询isUsing为Y的优惠券(最新版本)
     *
     * @param couponCode
     * @return
     */
    PromotionCouponsPojo queryByCouponCode(String couponCode, String isUsing);

    /**
     * 根据优惠券状态查询优惠券信息
     *
     * @param status 状态集合
     * @return
     */
    List<PromotionCouponsPojo> queryByStatus(List<String> status);

    ResponseData queryAll();

    ResponseData queryActivity(Map<String, Object> map) throws ParseException;

    ResponseData queryByNotIn(Map<String, Object> map);


    /**
     * 根据优惠券主键删除单个优惠券
     *
     * @param id
     * @return
     */
    ResponseData delete(String id);

    /**
     * 根据优惠券主键集合批量删除优惠券
     *
     * @param ids
     * @return
     */
    ResponseData delete(List<String> ids);

    void deleteReal(List<Map<String, Object>> maps);


    ResponseData selectByCode(String couponCode);

    ResponseData selectByCodeCanUse(String couponCode);

    ResponseData selectById(String id);

    ResponseData selectCouponIdById(String id);

    ResponseData selectByCouponId(String couponId);

    /**
     * 校验用户选择的优惠券是否是排它券（isExclusive 为Y）
     *
     * @return
     */
    boolean checkExclusiveCoupon(String couponId);

    List<String> checkedCoupon(Map<String, Object> map);

    /**
     * 优惠券占用释放接口
     *
     * @param map
     * @return
     */
    ResponseData operateCustomerCoupon(Map map);

    /**
     * 获取需同步的coupon
     *
     * @return
     */
    ResponseData getSynCoupon();

    /**
     * 设置优惠券同步标识
     *
     * @param couponCodes 优惠卷码
     * @return
     */
    void setCouponSyn(List<String> couponCodes);
}
