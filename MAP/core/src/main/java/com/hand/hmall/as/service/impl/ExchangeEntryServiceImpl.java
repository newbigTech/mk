package com.hand.hmall.as.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.as.dto.ExchangeEntry;
import com.hand.hmall.as.service.IExchangeEntryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ExchangeEntryServiceImpl extends BaseServiceImpl<ExchangeEntry> implements IExchangeEntryService {

}