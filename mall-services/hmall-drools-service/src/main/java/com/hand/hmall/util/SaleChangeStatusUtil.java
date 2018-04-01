package com.hand.hmall.util;

import com.hand.hmall.client.IPromoteClientService;
import com.hand.hmall.dao.SaleActivityDao;
import com.hand.hmall.dao.SaleCouponDao;
import com.hand.hmall.dao.SalePromotionCodeDao;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.menu.Status;
import com.hand.hmall.service.IRuleTempService;
import com.hand.hmall.service.ISaleActivityService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by shanks on 2017/3/27.
 */
@Component
@Order(value = 2)
public class SaleChangeStatusUtil implements CommandLineRunner, Runnable {

    @Autowired
    private SaleActivityDao saleActivityDao;
    @Autowired
    private ISaleActivityService saleActivityService;
    @Autowired
    private SaleCouponDao saleCouponDao;
    @Autowired
    private IRuleTempService ruleTempService;
    @Autowired
    private SalePromotionCodeDao salePromotionCodeDao;
    @Autowired
    private IPromoteClientService promoteClientService;

    private static boolean isFirstStart = true;
    private List<Map<String, ?>> lastModifyActivities = new ArrayList<>();

    private Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);


    @Override
    public void run() {
        if (isFirstStart) {
            try {
                long sysDate = System.currentTimeMillis();
                //查询所有的促销活动
                List<Map<String, ?>> activities = saleActivityService.selectByStatusAndIsUsing(Arrays.asList(Status.ACTIVITY.getValue(), Status.FAILURE.getValue()), null);
                for (Map<String, ?> activity : activities) {
                    Long endDate = (Long) activity.get("endDate");
                    Long startDate = (Long) activity.get("startDate");

//                    if(startDate<=System.currentTimeMillis()&&endDate>System.currentTimeMillis()) {
                    //生成kjar,drools的处理方式(发布促销规则)
                    ResponseData responseData = ruleTempService.releaseActivity((Map<String, Object>) activity);
                    if (!responseData.isSuccess()) {
                        logger.debug(responseData.getMsg());
                    } else {
                        lastModifyActivities.add(activity);
                    }
                }
//                }

                //获取所有优惠券
                sysDate = System.currentTimeMillis();
                List<Map<String, ?>> maps = saleCouponDao.selectByEqField("status", Status.ACTIVITY.getValue());
                for (int i = 0; i < maps.size(); i++) {
                    Long endDate = (Long) maps.get(i).get("endDate");
                    Long startDate = (Long) maps.get(i).get("startDate");
                    if (startDate <= System.currentTimeMillis() && endDate > System.currentTimeMillis()) {
                        //生成kjar,drools的处理方式(发布优惠券规则)
                        ResponseData responseData = ruleTempService.releaseCoupon(maps.get(i).get("id").toString());
                        if (!responseData.isSuccess()) {
                            logger.debug(responseData.getMsg());
                        }
                    }
                }
                isFirstStart = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {

            List<Map<String, ?>> activities = saleActivityService.selectByStatusAndIsUsing("", "Y");
            logger.info("-----------------更新促销规则，需要更新的促销条数为{}", activities.size());
            for (int i = 0; i < activities.size(); i++) {
                Map<String, Object> activity = (Map<String, Object>) activities.get(i);
                Long startDate = (Long) activity.get("startDate");
                Long endDate = (Long) activity.get("endDate");
                Long sysDate = System.currentTimeMillis();

                String status = activity.get("status").toString();

                //判断促销规则是否停用和删除
                if (!status.equals(Status.INACTIVE.getValue()) && !status.equals(Status.EXPR.getValue())) {
                    if (startDate - 120 * 1000 <= sysDate && sysDate < endDate) {
                        if (!status.equals(Status.ACTIVITY.getValue())) {
                            //到了活动生效时间自动把状态改为活动中了
                            activity.put("status", Status.ACTIVITY.getValue());
                            saleActivityDao.update(activity);
                            updateSalePromotionCode(activity.get("activityId").toString(), status, activity.get("status").toString());
                        }
                    } else if (startDate > sysDate) {
                        if (!status.equals(Status.DELAY.toString())) {
                            activity.put("status", Status.DELAY.toString());
                            saleActivityDao.update(activity);
                            updateSalePromotionCode(activity.get("activityId").toString(), status, activity.get("status").toString());

                        }
                    } else if (endDate <= sysDate) {
                        if (!status.equals(Status.FAILURE.getValue())) {
                            activity.put("status", Status.FAILURE.getValue());
                            saleActivityDao.update(activity);
                            updateSalePromotionCode(activity.get("activityId").toString(), status, activity.get("status").toString());
                        }
                    }
                }
            }

            List<Map<String, ?>> updateActivities = saleActivityService.selectByStatusAndIsUsing(Status.ACTIVITY.getValue(), "Y");

            checkedListContains(lastModifyActivities, updateActivities);
            this.lastModifyActivities = updateActivities;

            /**
             *定时修改优惠券状态
             */
            List<Map<String, ?>> coupons = saleCouponDao.selectAll();

            Map<String, Object> coupon = new HashMap<>();
            for (int i = 0; i < coupons.size(); i++) {
                coupon = (Map<String, Object>) coupons.get(i);

                Long startDate = (Long) coupon.get("startDate");
                Long endDate = (Long) coupon.get("endDate");
                Long sysDate = System.currentTimeMillis();
                String status = coupon.get("status").toString();

                if (!status.equals(Status.INACTIVE.getValue()) && !status.equals(Status.EXPR.getValue())) {
                    if (startDate <= sysDate && sysDate < endDate) {
                        if (!status.equals(Status.ACTIVITY.getValue())) {
                            coupon.put("status", Status.ACTIVITY.getValue());
                            coupon.put("isSyn", "N");
                            ruleTempService.releaseCoupon(coupon.get("id").toString());
                            saleCouponDao.update(coupon);
                        }
                    } else if (startDate > sysDate) {
                        if (status.equals(Status.ACTIVITY.getValue())) {

                            ruleTempService.removeCoupon(coupon.get("id").toString());
                        }
                        if (!status.equals(Status.DELAY.toString())) {
                            coupon.put("status", Status.DELAY.toString());
                            coupon.put("isSyn", "N");
                            saleCouponDao.update(coupon);
                        }
                    } else if (endDate <= sysDate) {
                        if (status.equals(Status.ACTIVITY.getValue())) {
                            ruleTempService.removeCoupon(coupon.get("id").toString());
                        }
                        if (!status.equals(Status.FAILURE.getValue())) {
                            coupon.put("status", Status.FAILURE.getValue());
                            coupon.put("isSyn", "N");
                            saleCouponDao.update(coupon);
                            String couponId = coupon.get("id").toString();
                            /*couponId对应的所有可使用的优惠券设置为已失效*/
                            promoteClientService.setInvalidByCid(couponId);
                        }
                    }
                }
            }
        }
        logger.info("=============================");
        logger.info("=============结束=============");

    }


    public void updateSalePromotionCode(String id, String originalStatus, String updateStatus) {
        if (originalStatus.equals(Status.ACTIVITY.getValue()) && !updateStatus.equals(Status.ACTIVITY.getValue())) {
            List<Map<String, ?>> salePromotionCodeList = salePromotionCodeDao.selectByActivityId(id);
            for (int j = 0; j < salePromotionCodeList.size(); j++) {
                Map<String, Object> data = (Map<String, Object>) salePromotionCodeList.get(j);
                data.put("status", Status.INACTIVE.getValue());
                salePromotionCodeDao.update(data);
            }
        } else if (!originalStatus.equals(Status.ACTIVITY.getValue()) && updateStatus.equals(Status.ACTIVITY.getValue())) {
            List<Map<String, ?>> salePromotionCodeList = salePromotionCodeDao.selectByActivityId(id);
            for (int j = 0; j < salePromotionCodeList.size(); j++) {
                Map<String, Object> data = (Map<String, Object>) salePromotionCodeList.get(j);
                data.put("status", Status.ACTIVITY.getValue());
                salePromotionCodeDao.update(data);
            }
        }
    }


    public void checkedListContains(List<Map<String, ?>> lastModifyActivities, List<Map<String, ?>> updatedList) {
        for (Map<String, ?> lastModify : lastModifyActivities) {
            boolean isDelete = true;
            for (Map<String, ?> update : updatedList) {
                if (lastModify.get("activityId").equals(update.get("activityId"))) {
                    isDelete = false;
                    if (!lastModify.get("releaseId").equals(update.get("releaseId"))) {
//                       ruleTempService.removeActivity(lastModify.get("activityId").toString(),lastModify.get("releaseId").toString());
                        ruleTempService.releaseActivity((Map<String, Object>) update);
                    }
                    break;
                }
            }
            if (isDelete) {
                ruleTempService.removeActivity(lastModify.get("id").toString(), lastModify.get("activityId").toString());
            }
        }

        for (Map<String, ?> update : updatedList) {
            boolean isCreate = true;
            for (Map<String, ?> lastModify : lastModifyActivities) {
                if (lastModify.get("activityId").equals(update.get("activityId"))) {
                    isCreate = false;
                    break;
                }
            }
            if (isCreate) {
                ruleTempService.releaseActivity((Map<String, Object>) update);
            }
        }
    }

    @Override
    public void run(String... strings) throws Exception {
        logger.info("================start==================");
        logger.info("============开始执行定时任务=============");
        /**
         * ScheduledExecutorService: 定时周期执行指定的任务
         */
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
        service.scheduleAtFixedRate(this, 5, 60, TimeUnit.SECONDS);
        logger.info("=======================================");
    }
}
