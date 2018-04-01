package com.hand.hmall.mst.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.mst.dto.Logisticsco;

import java.util.List;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 承运商对象的service接口
 * @date 2017/7/10 14:37
 */
public interface ILogisticscoService extends IBaseService<Logisticsco>, ProxySelf<ILogisticscoService> {

    /**
     * 运费当中的承运商查询
     *
     * @param dto
     * @return
     */
    List<Logisticsco> logisticsoLov(IRequest request, Logisticsco dto);

    /**
     * 查询唯一的Logisticsco
     * @param logisticsco 查询条件
     * @return Logisticsco
     */
    Logisticsco selectOne(Logisticsco logisticsco);
}