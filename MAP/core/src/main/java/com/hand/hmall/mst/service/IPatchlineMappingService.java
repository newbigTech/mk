package com.hand.hmall.mst.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.mst.dto.PatchlineMapping;
import com.hand.hmall.mst.dto.PatchlineMappingDto;

import java.util.List;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 补件映射对象的service接口
 * @date 2017/7/10 14:37
 */
public interface IPatchlineMappingService extends IBaseService<PatchlineMapping>, ProxySelf<IPatchlineMappingService> {

    /**
     * 推送syncFlag为N的补件商品映射信息到商城当中
     *
     * @return
     */
    public List<PatchlineMappingDto> selectPushingPatchlineMapping();

    /**
     * @param dto
     * @description 更新补件商品关系接口推送标志
     */
    public void updatePatchlineMappingSyncflag(List<PatchlineMappingDto> dto);

    /**
     * 查询商品下的补件编码和名称
     *
     * @param request
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    public List<PatchlineMapping> selectInfo(IRequest request, PatchlineMapping dto, int page,
                                             int pageSize);

    /**
     * 查询商品下的补件编码和名称
     *
     * @param request
     * @param dto
     * @return
     */
    public List<PatchlineMapping> selectInfo(IRequest request, PatchlineMapping dto);

    /**
     * 删除商品时，连带删除补件关系
     *
     * @param dto
     * @return
     */
    public int deletePatchlineMapping(PatchlineMapping dto);

    /**
     * 通过商品ID删除
     *
     * @param dto
     */
    void deleteByProductId(List<PatchlineMapping> dto);

    /**
     * 查询数据
     *
     * @param dto
     * @return
     */
    List<PatchlineMapping> selectAllById(List<PatchlineMapping> dto);

    /**
     * 根据商品ID和补件ID查询对应信息是否存在
     * @param dto
     * @return
     */
    Long selectByPatchineMappingAndProductId(PatchlineMapping dto);
}