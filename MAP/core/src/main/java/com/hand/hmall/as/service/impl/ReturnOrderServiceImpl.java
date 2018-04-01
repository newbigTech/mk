package com.hand.hmall.as.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.as.dto.RetrieveOrder;
import com.hand.hmall.as.dto.ReturnOrder;
import com.hand.hmall.as.mapper.ReturnOrderMapper;
import com.hand.hmall.as.service.IReturnOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author xuxiaoxue
 * @version 0.1
 * @name ReturnOrderServiceImpl 退货单详情Service实现类
 * @description
 * @date 2017/7/20
 */
@Service
@Transactional
public class ReturnOrderServiceImpl extends BaseServiceImpl<ReturnOrder> implements IReturnOrderService {
    @Autowired
    private ReturnOrderMapper returnOrderMapper;

    @Override
    public List<ReturnOrder> selectReturnOrderById(ReturnOrder dto) {
        return this.returnOrderMapper.selectReturnOrderById(dto);
    }

    @Override
    public List<ReturnOrder> selectUserInfoByOrderId(RetrieveOrder dto) {
        return returnOrderMapper.selectUserInfoByOrderId(dto);
    }
}
