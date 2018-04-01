package com.hand.hmall.pin.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hmall.pin.dto.PinSendinfo;
import com.hand.hmall.pin.service.IPinSendinfoService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class PinSendinfoServiceImpl extends BaseServiceImpl<PinSendinfo> implements IPinSendinfoService{

}