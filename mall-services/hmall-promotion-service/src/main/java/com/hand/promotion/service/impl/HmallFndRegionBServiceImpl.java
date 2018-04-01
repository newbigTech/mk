package com.hand.promotion.service.impl;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import com.hand.promotion.dto.FndRegionB;
import com.hand.promotion.mapper.FndRegionBMapper;
import com.hand.promotion.service.IHmallFndRegionBService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author XinyangMei
 * @Title HmallFndRegionBServiceImpl
 * @Description 地区Service实现类
 * @date 2017/10/26 9:41
 */
@Service
public class HmallFndRegionBServiceImpl implements IHmallFndRegionBService {
    @Autowired
    private FndRegionBMapper fndRegionBMapper;

    /**
     * 根据地区名称查询地区Code
     *
     * @param name 地区名称
     * @return
     */
    @Override
    public FndRegionB selectCodeByName(String name) {
        List<FndRegionB> fndRegionBS = fndRegionBMapper.selectCodeByName(name);
        if (CollectionUtils.isNotEmpty(fndRegionBS)) {
            return fndRegionBS.get(0);
        } else {
            return null;
        }

    }
}
