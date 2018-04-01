/*
 *  Copyright (C) HAND Enterprise Solutions Company Ltd.
 *  All Rights Reserved
 *
 */

package com.hand.hmall.service.impl;

import com.hand.hmall.client.GetCodeFeignClient;
import com.hand.hmall.service.IGetCodeFromThirdPartyService;
import com.markor.map.external.setupservice.dto.SetupSequenceConditionDto;
import com.markor.map.external.setupservice.service.CreateSetupSequenceExternalService;
import com.markor.map.external.setupservice.service.ISetupSequenceHeaderExternalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 李伟
 * @version 1.0
 * @name:GetCodeFromThirdPartyServiceImpl
 * @Description: 调用第三方服务, 获取订单编码、订单行号和pin码 实现类
 * @date 2017/8/22 23:26
 */

@Service
public class GetCodeFromThirdPartyServiceImpl implements IGetCodeFromThirdPartyService {

    @Autowired
    private GetCodeFeignClient getCodeFeignClient;

    @Autowired
    private ISetupSequenceHeaderExternalService iSetupSequenceHeaderExternalService;

    @Autowired
    private CreateSetupSequenceExternalService createSetupSequenceExternalService;

    /**
     * 调用第三方服务,获取订单编码
     *
     * @return
     */

    @Override
    public String getOrderCode() {
        SetupSequenceConditionDto setupSequenceConditionDto = createSetupSequenceExternalService.createSetupSequence(36L, 9L, "OD", "", "OD", "CP"
                , "", "", "", "", "", "", "", "", "",
                "");
        return iSetupSequenceHeaderExternalService.encode(setupSequenceConditionDto).getCode();
    }

    /**
     * 调用第三方服务,获取订单行编码
     *
     * @return
     */
    @Override
    public String getOrderEntryCode() {
        SetupSequenceConditionDto setupSequenceConditionDto = createSetupSequenceExternalService.createSetupSequence(36L, 9L, "ODE", "", "ODE", "CP"
                , "", "", "", "", "", "", "", "", "",
                "");
        return iSetupSequenceHeaderExternalService.encode(setupSequenceConditionDto).getCode();
    }

    /**
     * 调用第三方服务,获取pin码
     *
     * @return
     */
    @Override
    public String getPinCode() {
        SetupSequenceConditionDto setupSequenceConditionDto = createSetupSequenceExternalService.createSetupSequence(36L, 9L, "P1", "", "P1", "CP"
                , "", "", "", "", "", "", "", "", "",
                "");
        return iSetupSequenceHeaderExternalService.encode(setupSequenceConditionDto).getCode();
    }
}
