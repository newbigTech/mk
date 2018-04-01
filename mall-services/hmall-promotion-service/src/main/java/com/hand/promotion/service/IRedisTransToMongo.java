package com.hand.promotion.service;
/*
 * Copyright (C) HAND Enterprise Solutions Company Ltd.
 * All Rights Reserved
 */

/**
 * @author XinyangMei
 * @Title IRedisTransToMongo
 * @Description 将redis数据转换到MongoDb中
 * @date 2017/12/11 16:25
 */
public interface IRedisTransToMongo {
    /**
     * 装换促销活动
     */
    void transActivity();

    /**
     * 转换优惠券
     */
    void transCoupon();

}
