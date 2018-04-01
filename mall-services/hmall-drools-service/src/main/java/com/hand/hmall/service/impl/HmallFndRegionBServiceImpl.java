package com.hand.hmall.service.impl;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import com.hand.hmall.dto.FndRegionB;
import com.hand.hmall.mapper.FndRegionBMapper;
import com.hand.hmall.service.IHmallFndRegionBService;
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
     * 根据名称查询地区Code
     *
     * @param name
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
