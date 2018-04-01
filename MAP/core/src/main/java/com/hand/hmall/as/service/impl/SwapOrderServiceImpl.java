package com.hand.hmall.as.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.as.dto.SwapOrder;
import com.hand.hmall.as.mapper.SwapOrderMapper;
import com.hand.hmall.as.service.ISwapOrderService;
import com.hand.hmall.mst.dto.Product;
import com.hand.hmall.mst.dto.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: zhangmeng01
 * @version: 0.1
 * @name: SwapOrderServiceImpl
 * @description:换货单
 * @Date: 2017/7/21
 * \
 */
@Service
@Transactional
public class SwapOrderServiceImpl extends BaseServiceImpl<SwapOrder> implements ISwapOrderService {
    @Autowired
    private SwapOrderMapper swapOrderMapper;

    /**
     * 查询换货单信息
     *
     * @param dto
     * @return
     */
    @Override
    public List<SwapOrder> selectSwapOrderById(SwapOrder dto) {
        return swapOrderMapper.selectSwapOrderById(dto);
    }

    @Override
    public List<Product> queryProduct(ProductCategory dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return swapOrderMapper.queryProduct(dto);
    }
}
