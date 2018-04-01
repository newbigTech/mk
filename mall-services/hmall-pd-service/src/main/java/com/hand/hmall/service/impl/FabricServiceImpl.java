package com.hand.hmall.service.impl;

import com.hand.hmall.model.Fabric;
import com.hand.hmall.service.IFabricService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name FabricServiceImpl
 * @description 物料服务实现类
 * @date 2017/7/5 11:34
 */
@Service
public class FabricServiceImpl extends BaseServiceImpl<Fabric> implements IFabricService {

    /**
     * {@inheritDoc}
     *
     * @see IFabricService#selectByFabricCode(String)
     */
    @Override
    public Fabric selectByFabricCode(String fabricCode) {
        Fabric fabric = new Fabric();
        fabric.setFabricCode(fabricCode);
        return this.selectOne(fabric);
    }
}
