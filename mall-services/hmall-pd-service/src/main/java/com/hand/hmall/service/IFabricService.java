package com.hand.hmall.service;

import com.hand.hmall.model.Fabric;

import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name IFabricService
 * @description 面料Service
 * @date 2017/7/5 11:33
 */
public interface IFabricService extends IBaseService<Fabric> {

    /**
     * 根据物料编号查询物料等级
     * @param fabricCode 物料编号
     * @return 物料对象
     */
    Fabric selectByFabricCode(String fabricCode);
}
