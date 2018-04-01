package com.hand.hmall.service.impl;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import com.hand.hmall.mapper.HmallMstUnitMapper;
import com.hand.hmall.model.HmallMstUnit;
import com.hand.hmall.service.HmallMstUnitService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 运费计算 单位数据查询
 */
@Service
public class HmallUnitServiceImpl implements HmallMstUnitService {
    @Autowired
    private HmallMstUnitMapper hmallMstUnitMapper;

    /**
     * 根据单位编码查询单位
     * @param code 单位编码
     * @return
     */
    @Override
    public HmallMstUnit getUnitByCode(String code) {
        if(StringUtils.isEmpty(code)){
            return null;
        }
        return hmallMstUnitMapper.selectByUnitCode(code);
    }
}
