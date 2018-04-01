package com.hand.hmall.om.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.om.dto.Consignment;
import com.hand.hmall.om.dto.ConsignmentBk;
import com.hand.hmall.om.mapper.ConsignmentBkMapper;
import com.hand.hmall.om.service.IConsignmentBkService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name ConsignmentBkServiceImpl
 * @description 发货单备份Service接口实现类
 * @date 2017/8/4 11:40
 */
@Service
public class ConsignmentBkServiceImpl extends BaseServiceImpl<ConsignmentBk> implements IConsignmentBkService {

    @Autowired
    private ConsignmentBkMapper mapper;

    @Override
    public void saveBk(ConsignmentBk consignmentBk) {

    }

    @Override
    public List<ConsignmentBk> queryInfo(IRequest iRequest, ConsignmentBk dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<ConsignmentBk> cBKs = mapper.queryInfo(dto);
        return cBKs;
    }

    /**
     * 通过ID查询发货单快照信息
     * @param consignmentId
     * @return
     */
    @Override
    public ConsignmentBk queryById(@Param("consignmentId") Long consignmentId) {
        return mapper.queryById(consignmentId);
    }
}
