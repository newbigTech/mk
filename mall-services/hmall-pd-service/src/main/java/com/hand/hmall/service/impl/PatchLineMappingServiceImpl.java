package com.hand.hmall.service.impl;

import com.hand.hmall.model.PatchLineMapping;
import com.hand.hmall.model.Product;
import com.hand.hmall.pojo.PatchLineData;
import com.hand.hmall.service.IPatchLineMappingService;
import com.hand.hmall.service.IProductService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name PatchLineMappingServiceImpl
 * @description 补件服务实现类
 * @date 2017/6/30 10:40
 */
@Service
public class PatchLineMappingServiceImpl
        extends BaseServiceImpl<PatchLineMapping> implements IPatchLineMappingService {

    @Autowired
    private IProductService iProductService;

    /**
     * {@inheritDoc}
     *
     * @see IPatchLineMappingService#savePatchLines
     */
    @Override
    public void savePatchLines(List<PatchLineData> patchLineDataList, Long productId) {
        PatchLineMapping patchLineMappingParams = new PatchLineMapping();
        patchLineMappingParams.setPatchLineId(productId);
        this.delete(patchLineMappingParams);
        if (CollectionUtils.isNotEmpty(patchLineDataList)) {
            for (PatchLineData patchLineData : patchLineDataList) {
                if (StringUtils.isNotBlank(patchLineData.getPatchLineCode())) {
                    PatchLineMapping patchLineMapping = new PatchLineMapping();
                    // patchLineCode存储的是父级商品
                    Product parentProdcut = iProductService.selectMasterStagedByCode(patchLineData.getPatchLineCode());
                    patchLineMapping.setProductId(parentProdcut.getProductId());
                    patchLineMapping.setPatchLineId(productId);
                    this.insertSelective(patchLineMapping);
                }
            }
        }
    }
}
