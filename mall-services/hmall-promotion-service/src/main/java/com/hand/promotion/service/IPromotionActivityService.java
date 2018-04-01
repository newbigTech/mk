package com.hand.promotion.service;
/*
 * Copyright (C) HAND Enterprise Solutions Company Ltd.
 * All Rights Reserved
 */

import com.hand.dto.ResponseData;
import com.hand.promotion.pojo.SimpleMessagePojo;
import com.hand.promotion.pojo.activity.ActivityPojo;
import com.hand.promotion.pojo.activity.PromotionActivitiesPojo;
import com.hand.promotion.pojo.enums.Status;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author XinyangMei
 * @Title IPromotionActivityService
 * @Description 促销活动操作Service
 * @date 2017/12/11 19:01
 */
public interface IPromotionActivityService {

    /**
     * 创建促销活动
     *
     * @param promotionActivitiesPojo 要保存的促销规则pojo;
     * @param userId                  操作人账户id
     * @return SimpleMessagePojo 简单消息对象
     */
    public SimpleMessagePojo createActivity(PromotionActivitiesPojo promotionActivitiesPojo, String userId) ;

    /**
     * 查询要加载到缓存中的促销规则
     */
    public void queryUsefulActivity();

    /**
     * 校验促销规则是否可修改
     *
     * @param pojo
     * @return
     */
    public SimpleMessagePojo checkUsingActivityInvalid(ActivityPojo pojo);

    /**
     * 校验促销规则是否合法
     *
     * @param pojo
     * @return
     */
    public SimpleMessagePojo checkPromotionInvalid(PromotionActivitiesPojo pojo);


    /**
     * 根据条件分页查询促销活动，用于MAP页面促销查询
     *
     * @param promotionActivitiesPojo 查询条件
     * @param pageNum                 页码
     * @param pageSize                单页数据量
     * @return
     */
    public ResponseData queryPromotionByCondition(PromotionActivitiesPojo promotionActivitiesPojo, int pageNum, int pageSize) ;

    /**
     * 根据条件查询促销活动，用于MAP页面促销查询
     *
     * @param promotionActivitiesPojo
     * @return
     */
    public List<PromotionActivitiesPojo> queryPromotionByCondition(PromotionActivitiesPojo promotionActivitiesPojo) ;


    /**
     * 调用DAO，插入促销数据到数据库中，用于AOP监听
     *
     * @param promotionActivitiesPojo
     */
    SimpleMessagePojo insertPromotion(PromotionActivitiesPojo promotionActivitiesPojo) ;

    /**
     * 更新促销活动
     *
     * @param promotionActivitiesPojo
     * @return
     */
    SimpleMessagePojo updatePromotion(PromotionActivitiesPojo promotionActivitiesPojo) ;

    /**
     * 更新促销活动状态
     *
     * @param promotionActivitiesPojo
     * @param status
     * @return
     */
    SimpleMessagePojo updatePromotionStatus(PromotionActivitiesPojo promotionActivitiesPojo, Status status) ;

    /**
     * 根据促销活动状态，查询促销活动
     *
     * @param status 促销活动状态集合
     * @return
     */
    List<PromotionActivitiesPojo> queryPromotionByStatus(List<String> status);

    /**
     * 停用促销活动
     *
     * @param activityId 促销活动主键
     * @return
     */
    SimpleMessagePojo inactiveActivity(String activityId) throws CloneNotSupportedException;


    /**
     * 启用促销活动
     *
     * @param id 促销活动主键
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InterruptedException
     * @throws RemotingException
     * @throws MQClientException
     * @throws MQBrokerException
     * @throws UnsupportedEncodingException
     */
    SimpleMessagePojo enableActivity(String id) ;

    /**
     * 删除促销活动
     *
     * @param id 促销活动主键
     * @return
     */
    SimpleMessagePojo deleteActivity(String id) ;

    /**
     * 删除促销活动批量删除
     *
     * @param ids 促销活动主键集合
     * @return
     */
    public SimpleMessagePojo deleteBatchActivity(List<String> ids);

    /**
     * 根据主键查询数据
     *
     * @param pk
     * @return
     */
    PromotionActivitiesPojo findByPk(String pk);

    /**
     * 查询activity头信息，详细信息用于商城活动数据同步
     *
     * @return
     */
    public ResponseData queryForZmall();

}
