package com.hand.hmall.service;

import com.hand.hmall.pojo.SuitLineData;

import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name ISuitLineMappingService
 * @description ISuitLineMappingService
 * @date 2017/6/30 9:30
 */
public interface ISuitLineMappingService {

    /**
     * 保存套件关系
     * @param suitLineMappingList 套件关系列表
     * @param productId 头商品
     */
    void saveSuitLines(List<SuitLineData> suitLineMappingList, Long productId);
}
