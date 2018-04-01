package com.hand.hmall.mapper;

import com.hand.hmall.dto.HmallMstProduct;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;

/**
 * @author XinyangMei
 * @Title HmallMstProductMapper
 * @Description 查询产品信息Mapper
 * @date 2017/6/28 15:25
 * @version 1.0
 */
public interface HmallMstProductMapper extends Mapper<HmallMstProduct>{

    /**
     * 根据产品code和版本查询商品
     * @param productCode 产品code
     * @param catalogVersionId 产品版本
     * @return
     */
    HmallMstProduct selectByProductCode(String productCode, long catalogVersionId);

    /**
     * 根据版本查询商品
     * @param catalogVersionId
     * @return
     */
    List<HmallMstProduct> selectByCatalog(long catalogVersionId);

    /**
     * 查询产品运费信息
     * @return
     */
    List<HashMap> queryFeightInfo(List<String> productList);

}