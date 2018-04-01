package com.hand.hmall.mapper;

import com.hand.hmall.model.HmallOmPartsMapping;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
/**
 * @author 梅新养
 * @name:HmallOmPartsMappingMapper
 * @Description:订单行与配件关联信息查询Mapper
 * @version 1.0
 * @date 2017/5/24 14:39
 */
public interface HmallOmPartsMappingMapper extends Mapper<HmallOmPartsMapping> {

    /**
     * 判断订单行与配件是否存在关联关系
     * @param orderEntryId 订单行id
     * @param productId 配件产品id
     * @return
     */
    int countPartMapping(@Param("orderEntryId")Long orderEntryId, @Param("productId")Long productId);
}