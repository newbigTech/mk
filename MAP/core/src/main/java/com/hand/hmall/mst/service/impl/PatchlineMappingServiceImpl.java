package com.hand.hmall.mst.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.mst.dto.PatchlineMapping;
import com.hand.hmall.mst.dto.PatchlineMappingDto;
import com.hand.hmall.mst.mapper.PatchlineMappingMapper;
import com.hand.hmall.mst.service.IPatchlineMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 补件映射的service实现类
 * @date 2017/7/10 14:37
 */
@Service
@Transactional
public class PatchlineMappingServiceImpl extends BaseServiceImpl<PatchlineMapping> implements IPatchlineMappingService {

    @Autowired
    private PatchlineMappingMapper patchlineMappingMapper;

    /**
     * 推送syncFlag为N的补件商品映射信息到商城当中
     * @return
     */
    @Override
    public List<PatchlineMappingDto> selectPushingPatchlineMapping() {
        return patchlineMappingMapper.selectPushingPatchlineMapping();
    }

    /**
     * 更新补件商品关系接口推送标志
     * @param dto
     */
    @Override
    public void updatePatchlineMappingSyncflag(List<PatchlineMappingDto> dto) {
        patchlineMappingMapper.updatePatchlineMappingSyncflag(dto);
    }

    /**
     * 查询商品下的补件编码和名称
     * @param request
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<PatchlineMapping> selectInfo(IRequest request, PatchlineMapping dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return patchlineMappingMapper.selectInfo(dto);
    }

    /**
     * 查询商品下的补件编码和名称
     * @param request
     * @param dto
     * @return
     */
    @Override
    public List<PatchlineMapping> selectInfo(IRequest request, PatchlineMapping dto) {
        return patchlineMappingMapper.selectInfo(dto);
    }

    /**
     * 删除商品时，连带删除补件关系
     * @param dto
     * @return
     */
    @Override
    public int deletePatchlineMapping(PatchlineMapping dto) {
        return patchlineMappingMapper.deletePatchlineMapping(dto);
    }

    /**
     * 通过商品ID删除
     * @param dto
     */
    @Override
    public void deleteByProductId(List<PatchlineMapping> dto) {
        patchlineMappingMapper.deleteByProductId(dto);
    }

    /**
     * 查询数据
     * @param dto
     * @return
     */
    @Override
    public List<PatchlineMapping> selectAllById(List<PatchlineMapping> dto) {
        return patchlineMappingMapper.selectAllById(dto);
    }

    /**
     * 根据商品ID和补件ID查询对应信息是否存在
     * @param dto
     * @return
     */
    @Override
    public Long selectByPatchineMappingAndProductId(PatchlineMapping dto) {
        return patchlineMappingMapper.selectByPatchineMappingAndProductId(dto);
    }

}