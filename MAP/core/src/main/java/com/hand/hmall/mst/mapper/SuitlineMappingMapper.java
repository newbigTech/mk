package com.hand.hmall.mst.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.mst.dto.SuitlineMapping;
import com.hand.hmall.mst.dto.SuitlineMappingDto;

import java.util.List;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 套件映射对象的Mapper
 * @date 2017/7/10 14:37
 */
public interface SuitlineMappingMapper extends Mapper<SuitlineMapping> {

    List<SuitlineMapping> getCountByProductHeadId(Long productHeaderId);

    /**
     * 推送syncFlag为N的套件商品映射信息至商城当中
     *
     * @return
     */
    List<SuitlineMappingDto> selectPushingSuitlineMapping();

    /**
     * @param dto
     * @description 更新套件商品关系接口推送标志
     */
    void updateSuitlineMappingSyncflag(List<SuitlineMappingDto> dto);

    List<SuitlineMapping> selectInfo(SuitlineMapping dto);

    /**
     * 删除商品时，连带删除套件关系
     *
     * @param dto
     * @return
     */
    int deleteSuitlineMapping(SuitlineMapping dto);

    /**
     * 通过商品ID删除
     *
     * @param dto
     */
    void deleteByProductId(List<SuitlineMapping> dto);

    /**
     * 查询数据
     *
     * @param dto
     * @return
     */
    List<SuitlineMapping> selectAllById(List<SuitlineMapping> dto);

    /**
     * 根据组件Id和商品Id查询指定信息
     * @param dto
     * @return
     */
    Long selectBysuitlineAndProductId(SuitlineMapping dto);
}