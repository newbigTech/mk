package com.hand.hmall.mst.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.mst.dto.SuitlineMapping;
import com.hand.hmall.mst.dto.SuitlineMappingDto;

import java.util.List;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 套件映射对象的service接口
 * @date 2017/7/10 14:37
 */
public interface ISuitlineMappingService extends IBaseService<SuitlineMapping>, ProxySelf<ISuitlineMappingService> {


    List<SuitlineMapping> getCountByProductHeadId(Long productHeaderId);

    /**
     * 推送syncFlag为N的套件商品映射信息至商城当中
     *
     * @return
     */
    public List<SuitlineMappingDto> selectPushingSuitlineMapping();

    /**
     * @param dto
     * @description 更新套件商品关系接口推送标志
     */
    public void updateSuitlineMappingSyncflag(List<SuitlineMappingDto> dto);

    /**
     * 查询商品下的套件编码和名称
     *
     * @param request
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    public List<SuitlineMapping> selectInfo(IRequest request, SuitlineMapping dto, int page,
                                            int pageSize);

    /**
     * 查询商品下的套件编码和名称
     *
     * @param request
     * @param dto
     * @return
     */
    public List<SuitlineMapping> selectInfo(IRequest request, SuitlineMapping dto);

    /**
     * 删除商品时，连带删除套件关系
     *
     * @param dto
     * @return
     */
    public int deleteSuitlineMapping(SuitlineMapping dto);


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