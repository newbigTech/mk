package com.hand.promotion.service;
/*
 * Copyright (C) HAND Enterprise Solutions Company Ltd.
 * All Rights Reserved
 */


import com.hand.promotion.dto.HmallMstInstallation;

/**
 * @author XinyangMei
 * @Title HmallMstInstallationService
 * @Description 商品安装费基础信息查询Service
 * @date 2017/6/28 21:43
 */
public interface HmallMstInstallationService {
    /**
     * 根据商品的分类id 和安装费状态查询商品基础安装费用
     *
     * @param categoryId 商品分类
     * @param status 安装费数据状态 Y为启用,N为停用
     * @return
     */
    HmallMstInstallation getInstallationByCategoryIdAndStatus(Long categoryId, String status);
}
