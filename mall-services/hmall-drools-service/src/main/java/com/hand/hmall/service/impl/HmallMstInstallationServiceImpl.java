package com.hand.hmall.service.impl;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import com.hand.hmall.dto.HmallMstInstallation;
import com.hand.hmall.mapper.HmallMstInstallationMapper;
import com.hand.hmall.service.HmallMstInstallationService;
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
    @Override
    public HmallMstInstallation getInstallationByCategoryIdAndStatus(Long categoryId,String status) {
        return hmallMstInstallationMapper.getInstallationByCategoryIdAndStatus(categoryId,status);
    }
}
