package com.hand.hmall.as.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.as.dto.SwapOrder;
import com.hand.hmall.mst.dto.Product;
import com.hand.hmall.mst.dto.ProductCategory;

import java.util.List;

/**
 * @author zhangmeng
 * @version 0.1
 * @name SwapOrderMapper
 * @description 换货单mapper
 * @date 2017/7/21
 */
public interface SwapOrderMapper extends Mapper<SwapOrder> {
    /**
     * 查询换货单信息
     *
     * @param dto
     * @return
     */
    List<SwapOrder> selectSwapOrderById(SwapOrder dto);

    /**
     * 查询商品
     *
     * @param dto
     * @return
     */
    List<Product> queryProduct(ProductCategory dto);
}
