package com.hand.promotion.service.impl;

import com.hand.promotion.cache.HepBasicDataCacheInstance;
import com.hand.promotion.pojo.SimpleMessagePojo;
import com.hand.promotion.pojo.activity.ActivityDetailPojo;
import com.hand.promotion.pojo.activity.ActivityPojo;
import com.hand.promotion.pojo.activity.PromotionActivitiesPojo;
import com.hand.promotion.pojo.enums.MsgMenu;
import com.hand.promotion.pojo.group.SaleGroupPojo;
import com.hand.promotion.pojo.order.OrderPojo;
import com.hand.promotion.service.IConditionService;
import com.hand.promotion.service.IExcutePromoteService;
import com.hand.promotion.service.IOrderCalculateService;
import com.hand.promotion.service.IResultService;
import com.hand.promotion.service.ISaleGroupService;
import com.hand.promotion.util.DateFormatUtil;
import com.hand.promotion.util.ThreadPoolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2017/12/27
 * @description 调用促销引擎，执行促销活动
 */
@Service
public class ExcutePromoteServiceImpl implements IExcutePromoteService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IConditionService conditionService;
    @Autowired
    private IResultService resultService;
    @Autowired
    private HepBasicDataCacheInstance<PromotionActivitiesPojo> orderActivityCacheInstance;
    @Autowired
    private HepBasicDataCacheInstance<PromotionActivitiesPojo> entryActivityCacheInstance;
    @Autowired
    private IOrderCalculateService orderCalculateService;
    @Autowired
    private ISaleGroupService saleGroupService;

    /**
     * 查询可用订单层级促销活
     * 对于每一个订单层级的促销活动,用订单的copy(数据一致但是不是同一个对象)对象去执行
     * 获取执行和copy的返回结果中的usedActivity(保存订单满足的促销)集合,放到真实订单的activities(保存订单可选促销)对象
     *
     * @param order
     * @return
     */
    @Override
    public SimpleMessagePojo payByActivity(OrderPojo order) {
        long startTime = System.currentTimeMillis();
        //获取订单层级促销活动
        List<PromotionActivitiesPojo> cache = orderActivityCacheInstance.getCache();
        if (CollectionUtils.isEmpty(cache)) {
            order.setActivities(new ArrayList<>());
            return new SimpleMessagePojo(true, MsgMenu.SUCCESS, null).setCheckMsg("没有可用促销活动");

        }
        //从时间上过滤促销规则
        List<PromotionActivitiesPojo> promotionActivitiesPojos = filterTimeValidPromotion(cache, order.getCreated());
        //保存与订单匹配的促销信息
        List<ActivityDetailPojo> usefulActivities = Collections.synchronizedList(new ArrayList<>());
        //为每一个订单层级促销的执行开启一个线程
        CountDownLatch countDownLatch = new CountDownLatch(promotionActivitiesPojos.size());
        logger.info("1countDownLatchCount::{}", countDownLatch.getCount());
        promotionActivitiesPojos.forEach(promotionActivitiesPojo -> {
            ThreadPoolUtil.submit(() -> {
                try {
                    OrderPojo copy = order.copy();
                    copy.setUsedActivities(null);
                    orderCalculateService.computePromotPrice(copy);
                    SimpleMessagePojo executeResult = payByActivitySingle(promotionActivitiesPojo, copy);
                    List<ActivityDetailPojo> usedActivities = copy.getUsedActivities();
                    if (!CollectionUtils.isEmpty(usedActivities) && executeResult.isSuccess()) {
                        usefulActivities.addAll(usedActivities);
                    }
                } catch (Exception e) {
                    logger.error("查询订单层级促销异常", e);
                } finally {
                    countDownLatch.countDown();
                }
            });

        });
        try {
            logger.info("countDownLatchCount::{}", countDownLatch.getCount());
            countDownLatch.await();
        } catch (InterruptedException e) {
            logger.error("countDownLatch await err", e);
            return new SimpleMessagePojo(false, MsgMenu.SYSTEM_ERR, null).setCheckMsg("countDownLatch await异常>>>" + e.getMessage());
        }
        order.setActivities(usefulActivities);
        logger.info("----查询用户可用订单层级促销{}ms", System.currentTimeMillis() - startTime);

        return new SimpleMessagePojo(true, MsgMenu.SUCCESS, null);
    }

    /**
     * 根据促销活动Id执行一条促销活动
     *
     * @param promotion 要执行的促销活动
     * @param order
     * @return
     */
    @Override
    public SimpleMessagePojo payByActivitySingle(PromotionActivitiesPojo promotion, OrderPojo order) {
        //判断订单是否在促销活动期间
        if (!filterTimeValidPromotion(promotion, order.getCreated())) {
            return new SimpleMessagePojo(false, MsgMenu.PROMOTION_DATE_INVALID, null);
        }
        //初始化订满足促销条件的商品信息
        order.setMatchedProduct(null);
        orderCalculateService.collectMatchedProduct(order);
        //执行判定条件
        SimpleMessagePojo conditionResult = conditionService.checkIsMatchCondition(order, promotion);
        //条件匹配失败，返回
        if (!conditionResult.isSuccess()) {
            return conditionResult;
        }

        //执行结果
        SimpleMessagePojo result = resultService.calResultByOrderAndRules(order, promotion, false);
        if (!result.isSuccess()) {
            return result;
        }
        logger.info("执行促销活动{}成功", promotion.getActivity().getActivityName());
        orderCalculateService.computePromotPrice(order);
        return new SimpleMessagePojo();
    }

    /**
     * 执行选中的订单层级促销,并计算订单金额
     *
     * @param promotion
     * @param order
     * @return
     */
    @Override
    public SimpleMessagePojo executePromotion(PromotionActivitiesPojo promotion, OrderPojo order) {
        SimpleMessagePojo executResult = payByActivitySingle(promotion, order);
        if (executResult.isSuccess()) {
            orderCalculateService.computeOrderPromotPrice(order);
            return new SimpleMessagePojo(true, MsgMenu.SUCCESS, order);
        } else {
            return executResult;
        }
    }

    /**
     * 执行商品层级促销活动
     *
     * @param orderPojo
     */
    @Override
    public void orderEntryPromote(OrderPojo orderPojo) {
        List<SaleGroupPojo> saleGroupPojos = saleGroupService.selectAllGroup();
        //根据分组优先级对促销分组升序排序
        saleGroupPojos.sort(Comparator.comparing(SaleGroupPojo::getPriority));
        for (SaleGroupPojo saleGroupPojo : saleGroupPojos) {
            //获取促销分组对应的促销规则
            List<PromotionActivitiesPojo> groupPromotionCache = entryActivityCacheInstance.getListByKey(saleGroupPojo.getId());
            //根据时间过滤促销
            List<PromotionActivitiesPojo> filtedPromotion = filterTimeValidPromotion(groupPromotionCache, orderPojo.getCreated());
            //对促销规则按照优先级进行升序排序
            filtedPromotion.sort(Comparator.comparing(promotionActivitiesPojo -> promotionActivitiesPojo.getActivity().getPriority()));

            groupEntryExecute(filtedPromotion, orderPojo);
        }

    }

    /**
     * 执行同一分组的促销活动
     */
    public void groupEntryExecute(List<PromotionActivitiesPojo> groupPromotion, OrderPojo orderPojo) {
        if (CollectionUtils.isEmpty(groupPromotion)) {
            return;
        }
        //执行第一条促销活动
        boolean firstExecute = false;
        for (PromotionActivitiesPojo promotionActivitiesPojo : groupPromotion) {
            if (!firstExecute) {
                orderCalculateService.computePromotPrice(orderPojo);
                SimpleMessagePojo executeResult = payByActivitySingle(promotionActivitiesPojo, orderPojo);
                if (executeResult.isSuccess()) {
                    firstExecute = true;
                }
            } else {
                ActivityPojo activity = promotionActivitiesPojo.getActivity();
                String isOverlay = activity.getIsOverlay();
                if ("Y".equalsIgnoreCase(isOverlay)) {
                    orderCalculateService.computePromotPrice(orderPojo);
                    payByActivitySingle(promotionActivitiesPojo, orderPojo);
                }
            }
        }


    }

    /**
     * 根据订单创建时间过滤促销活动,只保留startDate<订单创建时间<endDate的促销活动
     *
     * @return
     */
    List<PromotionActivitiesPojo> filterTimeValidPromotion(List<PromotionActivitiesPojo> promotionActivitiesPojos, String createTime) {
        List<PromotionActivitiesPojo> fiterResult = promotionActivitiesPojos.parallelStream().filter(promotionActivitiesPojo -> {
            return filterTimeValidPromotion(promotionActivitiesPojo, createTime);
        }).collect(Collectors.toList());
        return fiterResult;
    }

    /**
     * 根据订单创建时间判断促销活动时间是否有效,只保留startDate<订单创建时间<endDate的促销活动
     *
     * @return
     */
    public boolean filterTimeValidPromotion(PromotionActivitiesPojo promotionActivitiesPojos, String createTime) {
        ActivityPojo activity = promotionActivitiesPojos.getActivity();
        long orderCreateTime = DateFormatUtil.stringToTimeStamp(createTime);

        if (activity.getStartDate() < orderCreateTime && orderCreateTime < activity.getEndDate()) {
            return true;
        } else {
            return false;
        }
    }
}
