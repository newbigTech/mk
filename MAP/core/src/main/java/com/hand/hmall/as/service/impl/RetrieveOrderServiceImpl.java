package com.hand.hmall.as.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.as.dto.RetrieveOrder;
import com.hand.hmall.as.mapper.RetrieveOrderMapper;
import com.hand.hmall.as.service.IRetrieveOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author qinzhipeng
 * @version 0.1
 * @name RetrieveOrderServiceImpl
 * @description 取回单service
 * @date 2017/7/19
 */
@Service
@Transactional
public class RetrieveOrderServiceImpl extends BaseServiceImpl<RetrieveOrder> implements IRetrieveOrderService {
    @Autowired
    private RetrieveOrderMapper retrieveOrderMapper;

    /**
     * 根据订单ID查询用户信息
     *
     * @param dto
     * @return
     */
    @Override
    public List<RetrieveOrder> selectUserInfoByOrderId(RetrieveOrder dto) {
        return retrieveOrderMapper.selectUserInfoByOrderId(dto);
    }

    /**
     * 查询取回单详细信息
     *
     * @param dto
     * @return
     */
    @Override
    public List<RetrieveOrder> selectRetrieveOrderById(RetrieveOrder dto) {
        return retrieveOrderMapper.selectRetrieveOrderById(dto);
    }
}