package com.hand.hmall.fnd.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hmall.fnd.dto.FndGlobalvariant;
import com.hand.hmall.fnd.service.IFndGlobalvariantService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class FndGlobalvariantServiceImpl extends BaseServiceImpl<FndGlobalvariant> implements IFndGlobalvariantService{

}