package com.hand.hmall.mst.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.mst.dto.Logisticsco;
import com.hand.hmall.mst.mapper.LogisticscoMapper;
import com.hand.hmall.mst.service.ILogisticscoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 承运商对象的service实现类
 * @date 2017/7/10 14:37
 */
@Service
@Transactional
public class LogisticscoServiceImpl extends BaseServiceImpl<Logisticsco> implements ILogisticscoService {

    @Autowired
    private LogisticscoMapper logisticscoMapper;

    /**
     * 运费当中的承运商查询
     * @param request
     * @param dto
     * @return
     */
    @Override
    public List<Logisticsco> logisticsoLov(IRequest request, Logisticsco dto) {
        return logisticscoMapper.logisticsoLov(dto);
    }

    /**
     * 查询唯一的Logisticsco
     * @param logisticsco 查询条件
     * @return
     */
    @Override
    public Logisticsco selectOne(Logisticsco logisticsco) {
        return logisticscoMapper.selectOne(logisticsco);
    }
}