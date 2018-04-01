package com.hand.hmall.om.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.om.dto.OrderEntryBk;
import com.hand.hmall.om.mapper.OrderEntryBkMapper;
import com.hand.hmall.om.service.IOrderEntryBkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name OrderEntryBkServiceImpl
 * @description 订单行备份Service接口实现类
 * @date 2017/8/4 11:39
 */
@Service
public class OrderEntryBkServiceImpl extends BaseServiceImpl<OrderEntryBk> implements IOrderEntryBkService {

    @Autowired
    private OrderEntryBkMapper mapper;

    /**
     * 订单行查询详情
     * @param iRequest
     * @param dto
     * @param page
     * @param pagesize
     * @return
     */
    @Override
    public List<OrderEntryBk> queryInfo(IRequest iRequest, OrderEntryBk dto, int page, int pagesize) {
        PageHelper.startPage(page, pagesize);
        return mapper.queryInfo(dto);
    }
}
