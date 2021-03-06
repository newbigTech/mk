package com.hand.hmall.service;
/*
 * Copyright (C) HAND Enterprise Solutions Company Ltd.
 * All Rights Reserved
 */

import com.hand.hmall.dto.FndRegionB;

import java.util.List;

/**
 * @author XinyangMei
 * @Title HmallFndRegionBService
 * @Description 地区serice
 * @date 2017/10/26 9:40
 */
public interface IHmallFndRegionBService {
    /**
     * 根据名称查询地区Code
     *
     * @param name
     * @return
     */
    FndRegionB selectCodeByName(String name);
}
