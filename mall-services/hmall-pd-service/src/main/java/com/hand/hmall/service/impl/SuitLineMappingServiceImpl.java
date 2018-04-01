package com.hand.hmall.service.impl;

import com.hand.hmall.model.Product;
import com.hand.hmall.model.SuitLineMapping;
import com.hand.hmall.pojo.SuitLineData;
import com.hand.hmall.service.ICatalogsService;
import com.hand.hmall.service.IProductService;
import com.hand.hmall.service.ISuitLineMappingService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name SuitLineMappingServiceImpl
 * @description 套件服务实现类
 * @date 2017/6/30 9:41
 */
@Service
public class SuitLineMappingServiceImpl extends
        BaseServiceImpl<SuitLineMapping> implements ISuitLineMappingService {

    @Autowired
    private IProductService iProductService;

    /**
     * {@inheritDoc}
     *
     * @see ISuitLineMappingService#saveSuitLines
     */
    @Override
    public void saveSuitLines(List<SuitLineData> suitLineMappingList, Long productId) {
        SuitLineMapping suitLineMappingParams = new SuitLineMapping();
        suitLineMappingParams.setProductHeadId(productId);
        this.delete(suitLineMappingParams);
        if (CollectionUtils.isNotEmpty(suitLineMappingList)) {
            for (SuitLineData suitLineData : suitLineMappingList) {
                if (StringUtils.isNotBlank(suitLineData.getComponentCode()) && suitLineData.getQuantity() != null) {
                    SuitLineMapping suitLineMapping = new SuitLineMapping();
                    suitLineMapping.setProductHeadId(productId);
                    suitLineMapping.setQuantity(suitLineData.getQuantity());
                    Product component = iProductService.selectMasterStagedByCode(suitLineData.getComponentCode());
                    suitLineMapping.setComponentId(component.getProductId());
                    this.insertSelective(suitLineMapping);
                }
            }
        }
    }
}
