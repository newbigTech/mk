package com.hand.hmall.pin.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hmall.pin.dto.PinAlter;
import com.hand.hmall.pin.service.IPinAlterService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class PinAlterServiceImpl extends BaseServiceImpl<PinAlter> implements IPinAlterService{

}