package com.hand.hmall.client;

import com.hand.hmall.client.impl.PromoteClientServiceImpl;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.model.Coupon;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by tlq on 2017/1/7.
 * 调用promote微服务
 */
@FeignClient(value = "hmall-promote-server", fallback = PromoteClientServiceImpl.class)
public interface IPromoteClientService {
    /**
     * 未使用，"hmall-promote-server"中无对应方法
     *
     * @param coupon
     * @return
     */
    @RequestMapping(value = "/h/promotion/coupon/insert", method = RequestMethod.POST)
    public ResponseData add(@RequestBody Coupon coupon);

    /**
     * 根据用户登陆账号查询在生效范围里的优惠券优惠券
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/promotion/coupon/queryByUserIdAndStatusForPromote", method = RequestMethod.POST)
    public ResponseData queryByUserIdAndStatus(@RequestBody Map<String, Object> map);

    /**
     * 根据用户登陆账号、优惠券状态、优惠券使用渠道查询优惠券
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/promotion/coupon/queryByUserIdAndStatusAndRange", method = RequestMethod.POST)
    public ResponseData queryByUserIdAndStatusAndRange(@RequestBody Map<String, Object> map);

    /**
     * 根据登陆账号优惠券状态查询用户的优惠券
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/h/promotion/coupon/hQueryByUserIdAndStatus", method = RequestMethod.POST)
    public ResponseData hQueryByUserIdAndStatus(@RequestBody Map<String, Object> map);

    /**
     * 根据登陆账号、优惠券状态、使用渠道查询用户的优惠券 未调用
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/h/promotion/coupon/hQueryByUserIdAndStatusAndRange", method = RequestMethod.POST)
    public ResponseData hQueryByUserIdAndStatusAndRange(@RequestBody Map<String, Object> map);

    /**
     * 根据用户已领取的优惠券（customerCoupon）的主键查询已领取优惠券详细信息
     *
     * @param couponId
     * @return
     */
    @RequestMapping(value = "/h/promotion/coupon/getByCouponId", method = RequestMethod.GET)
    public ResponseData getByCouponId(@RequestParam("couponId") String couponId);

    /**
     * 优惠券占用释放接口
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/promotion/coupon/operate/", method = RequestMethod.POST)
    public ResponseData setUsed(@RequestBody Map map);

    /**
     * 根据已领取优惠券（customerCoupon）的主键查询详细信息
     *
     * @param couponId
     * @return
     */
    @RequestMapping(value = "/promotion/coupon/{couponId}/", method = RequestMethod.GET)
    public ResponseData getCusomerCouponByCouponId(@PathVariable("couponId") String couponId);

    /**
     * 保存优惠券（coupon）可兑换剩余量
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/h/redemption/submitRedemption", method = RequestMethod.POST)
    public ResponseData submitRedemption(@RequestBody Map<String, Object> map);

    /**
     * 项目中未使用 抽奖页面调用
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/h/promotion/coupon/convert/draw", method = RequestMethod.POST)
    public ResponseData convertByDraw(@RequestBody Map<String, Object> map);

    /**
     * 下单后，把承诺的优惠券存到 couponTemp表中，优惠券总量扣除承诺的数量 未使用
     */
    @RequestMapping(value = "/promotion/coupon/minusCouponCount", method = RequestMethod.POST)
    public ResponseData minusCouponCount(@RequestBody Map convertMap);

    /**
     * 项目未使用
     *
     * @param couponTempIds
     * @return
     */
    @RequestMapping(value = "/promotion/coupon/usedByTempId", method = RequestMethod.POST)
    public ResponseData usedByTempId(@RequestBody List<Map<String, Object>> couponTempIds);

    /**
     * 删除优惠券最大兑换数量，用于创建优惠券异常时回滚数据
     *
     * @param redemptionId
     * @param type
     */
    @RequestMapping(value = "/h/redemption/deleteRedemption", method = RequestMethod.GET)
    public ResponseData deleteRedemption(@RequestParam("redemptionId") String redemptionId, @RequestParam("type") String type);


    /**
     * 停用优惠券
     * 根据优惠券（coupon）的主键设置该优惠券已发放的优惠券（customerCoupon）状态为已失效
     *
     * @param cid
     * @return
     */
    @RequestMapping(value = "/h/promotion/coupon/setInvalidByCid", method = RequestMethod.GET)
    public ResponseData setInvalidByCid(@RequestParam("cid") String cid);

    /**
     * 启用优惠券
     * 根据优惠券（coupon）的主键设置该优惠券已发放的优惠券（customerCoupon）状态为可以使用
     *
     * @param cid
     * @return
     */
    @RequestMapping(value = "/h/promotion/coupon/startUsingCoupon", method = RequestMethod.GET)
    public ResponseData startUsingCoupon(@RequestParam("cid") String cid);

    /**
     * 校验赠送优惠券中优惠券的张数是否符合预定范围
     *
     * @param mapList
     * @return
     */
    @RequestMapping(value = "/h/redemption/checkedCouponCount", method = RequestMethod.POST)
    ResponseData checkedCouponCount(@RequestBody List<Map<String, Object>> mapList);

    /**
     * 根据已领取优惠券（customerCoupon）的主键和用户Id查询customerCoupon详细信息
     *
     * @param couponId
     * @param userId
     * @return
     */
    @RequestMapping(value = "/promotion/coupon/selectCouponById", method = RequestMethod.GET)
    public ResponseData selectCouponById(@RequestParam("couponId") String couponId, @RequestParam("userId") String userId);

    /**
     * 根据优惠券（coupon）的主键删除该优惠券已发放的优惠券（customerCoupon）
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/h/promotion/coupon/deleteByCid", method = RequestMethod.GET)
    public ResponseData deleteByCid(@RequestParam("id") String id);

    /**
     * 未使用 查询（优惠券）coupon已经发放出去的券（customerCoupon）
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/h/promotion/coupon/queryByCidAndUserIds", method = RequestMethod.POST)
    public ResponseData queryByCidAndUserIds(@RequestBody Map<String, Object> map);
}
