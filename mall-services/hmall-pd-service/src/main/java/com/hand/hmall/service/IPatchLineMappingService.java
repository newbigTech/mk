package com.hand.hmall.service;

import com.hand.hmall.pojo.PatchLineData;

import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name IPatchLineMappingService
 * @description 补件关系Service
 * @date 2017/6/30 10:38
 */
public interface IPatchLineMappingService {

    /**
     * 保存补件关系
     * @param patchLineDataList 补件关系
     * @param productId 商品id
     */
    void savePatchLines(List<PatchLineData> patchLineDataList, Long productId);
}
