package com.hand.promotion.job;

import com.hand.promotion.pojo.activity.ActivityPojo;
import com.hand.promotion.pojo.activity.PromotionActivitiesPojo;
import com.hand.promotion.pojo.coupon.CouponsPojo;
import com.hand.promotion.pojo.coupon.PromotionCouponsPojo;
import com.hand.promotion.pojo.enums.Status;
import com.hand.promotion.service.ICouponService;
import com.hand.promotion.service.ICustomerCouponService;
import com.hand.promotion.service.IPromotionActivityService;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2017/12/19
 * @description 定时检测更新促销活动状态
 */
@Component
public class ChangePromotionJob extends BaseJob{

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IPromotionActivityService promotionActivityService;
    @Autowired
    private ICouponService saleCouponService;
    @Autowired
    private ICustomerCouponService customerCouponService;
    //记录更新次数
    private int count = 0;


    /**
     * 定时器，每隔一分钟调用一次该方法
     */
    @Scheduled(fixedDelay = 60 * 1000)
    public void changeStatus() {
        logger.info(">>>>>>>>>>>>>>>>>>第{}次更新", ++count);
        try {
            //更新促销活动状态
            changePromotionStatus();
            //更新优惠券状态
            changeCouponStatus();
        } catch (Exception e) {
            logger.error("---更新促销活动状态异常----", e);
        }
    }

    /**
     * 遍历修改促销活动状态。活动中--》已失效   待生效-----》活动中
     */
    public void changePromotionStatus() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, UnsupportedEncodingException, InterruptedException, RemotingException, MQClientException, MQBrokerException {
        logger.info("----------轮询促销状态-----");
        long startDate = System.currentTimeMillis();

        //查询出待生效的促销活动
        List<PromotionActivitiesPojo> delayPomotions = promotionActivityService.queryPromotionByStatus(Arrays.asList(Status.DELAY.getValue()));
        for (PromotionActivitiesPojo pomotion : delayPomotions) {
            ActivityPojo activityPojo = pomotion.getActivity();
            Long currentTime = System.currentTimeMillis();
            //时间上促销变为活动中，更新促销为活动（Active）状态
            //提前将两分钟将促销加载到缓存
            if (activityPojo.getStartDate() - 2 * 60 * 1000 <= currentTime && activityPojo.getEndDate() > currentTime) {
                if (activityPojo.getStartDate() <= currentTime && activityPojo.getEndDate() > currentTime) {
                    activityPojo.setStatus(Status.ACTIVITY.getValue());
                }

            } else if (activityPojo.getEndDate() < currentTime) {
                //时间上变为已失效，更新促销状态为已失效（FAILURE）
                activityPojo.setStatus(Status.FAILURE.getValue());
            } else {
                continue;
            }

            //更新促销活动状态
            promotionActivityService.updatePromotionStatus(pomotion, Status.valueOf(activityPojo.getStatus()));

        }

        //查询出状态为活动中的促销
        List<PromotionActivitiesPojo> activePomotions = promotionActivityService.queryPromotionByStatus(Arrays.asList(Status.ACTIVITY.getValue()));
        for (PromotionActivitiesPojo pomotion : activePomotions) {
            ActivityPojo activityPojo = pomotion.getActivity();
            Long currentTime = System.currentTimeMillis();
            //时间上促销变为失效，更新促销为失效（FAILURE）状态
            if (activityPojo.getEndDate() < currentTime) {
                //时间上变为已失效，更新促销状态为已失效（FAILURE）
                activityPojo.setStatus(Status.FAILURE.getValue());
            } else {
                continue;
            }
            promotionActivityService.updatePromotionStatus(pomotion, Status.valueOf(activityPojo.getStatus()));

        }

        logger.info("--------促销状态更新完毕-----耗时{} ms", System.currentTimeMillis() - startDate);

    }

    /**
     * 修改优惠券状态. 活动中-->已失效  待生效--->活动中
     */
    public void changeCouponStatus() throws NoSuchMethodException, InterruptedException, UnsupportedEncodingException, IllegalAccessException, MQBrokerException, RemotingException, MQClientException, InvocationTargetException {
        logger.info("----------轮询优惠券状态-----");
        long startDate = System.currentTimeMillis();

        //查询出生效的促销活动
        List<PromotionCouponsPojo> delayCoupons = saleCouponService.queryByStatus(Arrays.asList(Status.DELAY.getValue()));
        for (PromotionCouponsPojo saleCoupon : delayCoupons) {
            CouponsPojo coupon = saleCoupon.getCoupon();
            Long currentTime = System.currentTimeMillis();
            //时间上促销变为活动中，更新促销为活动（Active）状态
            //提前将两分钟将促销加载到缓存
            if (coupon.getStartDate() - 2 * 60 * 1000 <= currentTime && coupon.getEndDate() > currentTime) {
                if (coupon.getStartDate() <= currentTime && coupon.getEndDate() > currentTime) {
                    coupon.setStatus(Status.ACTIVITY.getValue());
                    //更新促销活动状态
                    saleCouponService.updateCouponStatus(saleCoupon);
                }

            } else if (coupon.getEndDate() < currentTime) {
                //时间上变为已失效，更新促销状态为已失效（FAILURE）
                coupon.setStatus(Status.FAILURE.getValue());
                //更新促销活动状态
                saleCouponService.updateCouponStatus(saleCoupon);
            }
        }

        //查询出状态为活动中的促销
        List<PromotionCouponsPojo> activeSaleCoupons = saleCouponService.queryByStatus(Arrays.asList(Status.ACTIVITY.getValue()));
        for (PromotionCouponsPojo activeSaleCoupon : activeSaleCoupons) {
            CouponsPojo coupon = activeSaleCoupon.getCoupon();
            Long currentTime = System.currentTimeMillis();
            //时间上促销变为失效，更新促销为失效（FAILURE）状态
            if (coupon.getEndDate() < currentTime) {
                //时间上变为已失效，更新促销状态为已失效（FAILURE）
                coupon.setStatus(Status.FAILURE.getValue());
                saleCouponService.updateCouponStatus(activeSaleCoupon);
            }
            //优惠券有有效时长,更新已发放的未使用的客户优惠券状态
            Double activeTime = coupon.getActiveTime();
            if (activeTime != null) {
                customerCouponService.updateActiveCouponStatus(coupon.getId());
            }


        }

        logger.info("--------促销状态更新完毕-----耗时{} ms", System.currentTimeMillis() - startDate);
    }

    @Override
    public void doExecute() {

    }
}
