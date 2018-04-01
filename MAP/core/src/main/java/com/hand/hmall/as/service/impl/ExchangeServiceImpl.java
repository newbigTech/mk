package com.hand.hmall.as.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.as.dto.Exchange;
import com.hand.hmall.as.service.IExchangeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ExchangeServiceImpl extends BaseServiceImpl<Exchange> implements IExchangeService {

}