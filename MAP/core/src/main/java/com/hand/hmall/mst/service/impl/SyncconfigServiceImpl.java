package com.hand.hmall.mst.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.mst.dto.Syncconfig;
import com.hand.hmall.mst.mapper.SyncconfigMapper;
import com.hand.hmall.mst.service.ISyncconfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name Syncconfig
 * @description 同步配置
 * @date 2017年5月26日10:52:23
 */
@Service
@Transactional
public class SyncconfigServiceImpl extends BaseServiceImpl<Syncconfig> implements ISyncconfigService{

    @Autowired
    SyncconfigMapper syncconfigMapper;

    /**
     * 查询HMALL_MST_SYNCCONFIG 的数据
     * @param dto
     * @return
     */
    @Override
    public List<Syncconfig> selectLovData(Syncconfig dto) {
        return syncconfigMapper.selectLovData(dto);
    }

    /**
     * 根据catalogVersionId查询所有的HMALL_MST_SYNCCONFIG
     * @param catalogVersionId
     * @return
     */
    @Override
    public List<Syncconfig> selectByCatalogVersionId(Long catalogVersionId) {
        return syncconfigMapper.selectByCatalogVersionId(catalogVersionId);
    }
}