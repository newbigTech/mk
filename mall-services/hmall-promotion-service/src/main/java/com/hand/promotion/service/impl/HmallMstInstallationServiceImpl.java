package com.hand.promotion.service.impl;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import com.hand.promotion.dto.HmallMstInstallation;
import com.hand.promotion.mapper.HmallMstInstallationMapper;
import com.hand.promotion.service.HmallMstInstallationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author XinyangMei
 * @Title HmallMstInstallationServiceImpl
 * @Description 查询安装费基础数据
 * @date 2017/6/28 21:44
 */
@Service
public class HmallMstInstallationServiceImpl implements HmallMstInstallationService {
    @Resource
    private HmallMstInstallationMapper hmallMstInstallationMapper;

    /**
     * 根据商品的分类id 和安装费状态查询商品基础安装费用
     *
     * @param categoryId 商品分类
     * @param status     安装费数据状态 Y为启用,N为停用
     * @return
     */
    @Override
    public HmallMstInstallation getInstallationByCategoryIdAndStatus(Long categoryId, String status) {
        return hmallMstInstallationMapper.getInstallationByCategoryIdAndStatus(categoryId, status);
    }
}
