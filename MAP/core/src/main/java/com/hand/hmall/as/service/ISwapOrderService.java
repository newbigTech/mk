package com.hand.hmall.as.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.as.dto.SwapOrder;
import com.hand.hmall.mst.dto.Product;
import com.hand.hmall.mst.dto.ProductCategory;

import java.util.List;

/**
 * @author: zhangmeng01
 * @version: 0.1
 * @name: ISwapOrderService
 * @description: 换货单
 * @Date: 2017/7/21
 */
public interface ISwapOrderService extends IBaseService<SwapOrder>, ProxySelf<ISwapOrderService> {
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
     * @param page
     * @param pageSize
     * @return
     */
    List<Product> queryProduct(ProductCategory dto, int page, int pageSize);
}
