package com.hand.promotion.service.impl;

import com.hand.promotion.cache.HepBasicDataCacheInstance;
import com.hand.promotion.pojo.SimpleMessagePojo;
import com.hand.promotion.pojo.coupon.CouponsPojo;
import com.hand.promotion.pojo.coupon.CustomerCouponPojo;
import com.hand.promotion.pojo.coupon.PromotionCouponsPojo;
import com.hand.promotion.pojo.enums.CouponStatus;
import com.hand.promotion.pojo.enums.MsgMenu;
import com.hand.promotion.pojo.enums.PromotionConstants;
import com.hand.promotion.pojo.order.OrderPojo;
import com.hand.promotion.service.IConditionService;
import com.hand.promotion.service.ICustomerCouponService;
import com.hand.promotion.service.IExcuteCouponService;
import com.hand.promotion.service.IOrderCalculateService;
import com.hand.promotion.service.IResultService;
import com.hand.promotion.util.DateFormatUtil;
import com.hand.promotion.util.ThreadPoolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2018/1/16
 * @description 执行优惠券逻辑
 */
@Service
public class ExcuteCouponServiceImpl implements IExcuteCouponService {

    @Autowired
    private HepBasicDataCacheInstance<PromotionCouponsPojo> couponCacheInstance;
    @Autowired
    private IConditionService conditionService;
    @Autowired
    private IResultService resultService;
    @Autowired
    private IOrderCalculateService orderCalculateService;
    @Autowired
    private ICustomerCouponService customerCouponService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 遍历用户账户所有可用优惠券,获得订单数据匹配的优惠券信息
     *
     * @param order
     * @return
     * @throws CloneNotSupportedException
     * @throws InterruptedException
     */
    @Override
    public SimpleMessagePojo optionCoupon(OrderPojo order) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        //获取所有的用户优惠券
        List<CustomerCouponPojo> customerCouponPojos = customerCouponService.queryUsefulCusCoupon(order.getCustomerId());
        if (CollectionUtils.isEmpty(customerCouponPojos)) {
            logger.info("用户没有账户下没有可用优惠券");
            return new SimpleMessagePojo(true, MsgMenu.SUCCESS, null).setCheckMsg("用户没有可用优惠券");
        }
        //为每个优惠券单独开启一个线程,获取执行成功后的订单上的"couponList"数据
        CountDownLatch countDownLatch = new CountDownLatch(customerCouponPojos.size());
        List usefulCoupons = Collections.synchronizedList(new ArrayList<>());
        customerCouponPojos.forEach(customerCouponPojo -> ThreadPoolUtil.submit(() -> {
            try {
                OrderPojo copy = order.copy();
                PromotionCouponsPojo promotionCoupon = couponCacheInstance.getByKey(customerCouponPojo.getCid());
                //处理霸王券的订单数据
                dealOrderForExclu(promotionCoupon.getCoupon(), copy);
                SimpleMessagePojo excuteResult = payByCoupon(customerCouponPojo, copy, order.getCustomerId());
                if (excuteResult.isSuccess()) {
                    usefulCoupons.add(copy.getUsedCouponInfo());
                }
            } catch (Exception e) {
                logger.error(customerCouponPojo.getCouponName() + ">>>>>>遍历可用优惠券时发生异常", e);
            } finally {
                countDownLatch.countDown();
            }
        }));

        try {
            logger.info("countDownLatchCount::{}", countDownLatch.getCount());
            countDownLatch.await();
        } catch (InterruptedException e) {
            logger.error("countDownLatch await err", e);
            return new SimpleMessagePojo(true, MsgMenu.SUCCESS, null);
        }
        //设置订单可选优惠券
        if (CollectionUtils.isEmpty(order.getCouponList())) {
            order.setCouponList(usefulCoupons);
        } else {
            order.getCouponList().addAll(usefulCoupons);
        }
        //处理重算优惠券
        dealReusedCoupon(order);
        return new SimpleMessagePojo(true, MsgMenu.SUCCESS, null);
    }

    /**
     * 执行一条优惠券
     *
     * @param customerCouponPojo 要执行的优惠券数据
     * @param order              要执行优惠券的订单数据
     * @param currentUserId      优惠券所有人
     * @return
     */
    @Override
    public SimpleMessagePojo payByCoupon(CustomerCouponPojo customerCouponPojo, OrderPojo order, String currentUserId) {
        if (null == customerCouponPojo) {
            return new SimpleMessagePojo(false, MsgMenu.COUPON_IS_NULL, null).setCheckMsg("要执行的已发放优惠券信息为空");
        }
        PromotionCouponsPojo promotionCoupon = couponCacheInstance.getByKey(customerCouponPojo.getCid());
        if (null == promotionCoupon) {
            return new SimpleMessagePojo(false, MsgMenu.COUPON_IS_NULL, null).setCheckMsg("要执行的优惠券信息为空");
        }

        //校验订单是否满足优惠券条件
        SimpleMessagePojo checkResult = conditionService.checkCouponIsMatchCondition(order, promotionCoupon);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }
        checkResult = resultService.calCouponResultByOrderAndRules(order, promotionCoupon);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }
        logger.info("执行优惠券{}成功", promotionCoupon.getCoupon().getCouponName());
        orderCalculateService.computePromotPrice(order);
        recordCouponDetail(customerCouponPojo, promotionCoupon.getCoupon(), order);
        return new SimpleMessagePojo(true, MsgMenu.SUCCESS, null);
    }

    /**
     * 执行用户选中的优惠券
     *
     * @param cusCouponId 选中的customerCoupon 主键
     * @param orderPojo   要计算的订单数据
     * @return
     */
    @Override
    public SimpleMessagePojo executeChosenCoupon(String cusCouponId, OrderPojo orderPojo) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        //校验要执行的优惠券
        String usedCoupon = orderPojo.getUsedCoupon();
        SimpleMessagePojo checkResult;
        //校验重算优惠券
        if (cusCouponId.equalsIgnoreCase(usedCoupon)) {
            checkResult = checkReuseCoupon(cusCouponId, orderPojo.getCustomerId());
        } else {
            checkResult = checkChosenCoupon(cusCouponId, orderPojo.getCustomerId());
        }

        if (!checkResult.isSuccess()) {
            return checkResult;
        }

        CustomerCouponPojo cusCoupon = (CustomerCouponPojo) checkResult.getObj();
        PromotionCouponsPojo promotionCoupon = couponCacheInstance.getByKey(cusCoupon.getCid());
        if (null == promotionCoupon) {
            return new SimpleMessagePojo(false, MsgMenu.COUPON_IS_NULL, null).setCheckMsg("要执行的优惠券信息为空");
        }

        //处理霸王券的订单数据
        dealOrderForExclu(promotionCoupon.getCoupon(), orderPojo);
        SimpleMessagePojo executResult = payByCoupon(cusCoupon, orderPojo, orderPojo.getCustomerId());
        if (!executResult.isSuccess()) {
            return executResult;
        }

        return new SimpleMessagePojo(true, MsgMenu.SUCCESS, null);
    }

    /**
     * 校验要执行的优惠券是否合法,合法则返回customerCoupn详细信息
     *
     * @param cusCouponId 已发放优惠券主键
     * @param userId      发券账号
     * @return
     */
    private SimpleMessagePojo checkChosenCoupon(String cusCouponId, String userId) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<CustomerCouponPojo> customerCouponPojos = customerCouponService.queryByUserIdAndId(userId, cusCouponId);
        if (CollectionUtils.isEmpty(customerCouponPojos)) {
            return new SimpleMessagePojo(false, MsgMenu.CUSTOMER_COUPN_NOT_EXIST, null);
        }
        CustomerCouponPojo customerCoupon = customerCouponPojos.get(0);
        if (!CouponStatus.STATUS_01.getValue().equalsIgnoreCase(customerCoupon.getStatus())) {
            return new SimpleMessagePojo(false, MsgMenu.CUSTOMER_COUPN_NOT_VALID, null).setCheckMsg("要执行的优惠券已使用或已过期");
        }
        if (!checkDateInvalid(customerCoupon, System.currentTimeMillis())) {
            return new SimpleMessagePojo(false, MsgMenu.CUSTOMER_COUPN_NOT_VALID, null).setCheckMsg("要执行的优惠券未生效或已过期");
        }

        return new SimpleMessagePojo(true, MsgMenu.SUCCESS, customerCoupon);
    }

    /**
     * 校验要执行的参与从算的优惠券是否合法,合法则返回customerCoupn详细信息
     *
     * @param cusCouponId 参与从算的优惠券
     * @param userId      发券账号
     * @return
     */
    private SimpleMessagePojo checkReuseCoupon(String cusCouponId, String userId) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<CustomerCouponPojo> customerCouponPojos = customerCouponService.queryByUserIdAndId(userId, cusCouponId);
        if (CollectionUtils.isEmpty(customerCouponPojos)) {
            return new SimpleMessagePojo(false, MsgMenu.CUSTOMER_COUPN_NOT_EXIST, null);
        }
        CustomerCouponPojo customerCoupon = customerCouponPojos.get(0);

        if (checkDateInvalid(customerCoupon, System.currentTimeMillis())) {
            return new SimpleMessagePojo(false, MsgMenu.CUSTOMER_COUPN_NOT_VALID, null).setCheckMsg("要执行的优惠券未生效或已过期");
        }

        return new SimpleMessagePojo(true, MsgMenu.SUCCESS, customerCoupon);
    }

    /**
     * 检验已发放优惠券时间是否合法
     *
     * @param customerCouponPojo
     * @param checkMills         比较的时间点(毫秒)
     * @return
     */
    private boolean checkDateInvalid(CustomerCouponPojo customerCouponPojo, Long checkMills) {
        if (customerCouponPojo.getStartDate() < checkMills && checkMills < customerCouponPojo.getEndDate()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检测要执行的优惠券,如果是霸王券,将订单数据置为霸王券所需数据.同时返回优惠券是否是霸王券
     *
     * @param coupon    要执行的优惠券
     * @param orderPojo 做霸王券处理的订单数据
     * @return boolean true 是霸王券 false不是霸王券
     */
    private boolean dealOrderForExclu(CouponsPojo coupon, OrderPojo orderPojo) {
        if (PromotionConstants.Y.equalsIgnoreCase(coupon.getIsExclusive())) {
            orderPojo.getOrderEntryList().forEach(entryPojo -> {
                entryPojo.initEntry(Arrays.asList("basePrice", "shippingFee", "preShippingFee", "installationFee", "preInstallationFee"));
            });
            orderPojo.initOrder(Arrays.asList("postFee", "epostFee", "fixFee", "postReduce", "epostReduce", "fixReduce"));
            //获取订单行的真实价格
            getRealPrice(orderPojo);
            orderPojo.setUsedActivities(new ArrayList<>());
            orderCalculateService.computePromotPrice(orderPojo);
            logger.info("-------执行霸王券-------");
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取订单行的真实价格
     *
     * @param orderPojo
     */
    //todo::调用价格计算服务
    private void getRealPrice(OrderPojo orderPojo) {
        orderPojo.getOrderEntryList().forEach(orderEntryPojo -> {
            orderEntryPojo.setCalPrice(orderEntryPojo.getBasePrice());
        });
    }

    /**
     * 查询可选优惠券时,对订单的优惠券重算做处理
     *
     * @param orderPojo
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private void dealReusedCoupon(OrderPojo orderPojo) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        //获取重算优惠券数据
        String usedCoupon = orderPojo.getUsedCoupon();
        if (StringUtils.isEmpty(usedCoupon)) {
            return;
        }
        SimpleMessagePojo checkResult = checkReuseCoupon(usedCoupon, orderPojo.getCustomerId());
        if (!checkResult.isSuccess()) {
            return;
        }
        CustomerCouponPojo cusCoupon = (CustomerCouponPojo) checkResult.getObj();
        PromotionCouponsPojo promotionCoupon = couponCacheInstance.getByKey(cusCoupon.getCid());
        if (null == promotionCoupon) {
            return;
        }
        //复制订单数据,查看重算优惠券是否匹配订单数据
        OrderPojo copy = orderPojo.copy();
        SimpleMessagePojo executResult = payByCoupon(cusCoupon, copy, orderPojo.getCustomerId());
        if (!executResult.isSuccess()) {
            return;
        }
        List couponList = orderPojo.getCouponList();
        if (CollectionUtils.isEmpty(couponList)) {
            couponList = new ArrayList();
        }
        couponList.addAll(copy.getCouponList());
        orderPojo.setCouponList(couponList);
    }

    /**
     * 记录可选优惠券信息
     *
     * @param customerCoupon
     * @param coupon
     * @param orderPojo
     */
    public void recordCouponDetail(CustomerCouponPojo customerCoupon, CouponsPojo coupon, OrderPojo orderPojo) {
        Map couponInfo = new HashMap();
        couponInfo.put("id", coupon.getId());
        couponInfo.put("couponName", coupon.getCouponName());
        couponInfo.put("couponCode", coupon.getCouponCode());
        couponInfo.put("discountType", coupon.getDiscountType());
        couponInfo.put("couponId", customerCoupon.getId());
        couponInfo.put("benefit", coupon.getBenefit());
        couponInfo.put("startDate", DateFormatUtil.timeStampToString(customerCoupon.getStartDate().toString()));
        couponInfo.put("endDate", DateFormatUtil.timeStampToString(customerCoupon.getEndDate().toString()));
        couponInfo.put("couponDes", coupon.getCouponDes());
        orderPojo.setUsedCouponInfo(couponInfo);
    }

}
