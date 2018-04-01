package com.hand.hmall.as.controllers;

import com.hand.hap.system.controllers.BaseController;
import com.hand.hmall.as.service.IExchangeService;
import com.hand.hmall.common.service.ISequenceGenerateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @author: zhangmeng01
 * @version: 0.1
 * @name: ExchangeController
 * @description: 换货单controller 上线前不做了 上线之后在做 2017/7/24
 * @Date: 2017/8/24
 */
@Controller
public class ExchangeController extends BaseController {

    @Autowired
    private IExchangeService service;

    @Autowired
    private ISequenceGenerateService sequenceGenerateService;

}