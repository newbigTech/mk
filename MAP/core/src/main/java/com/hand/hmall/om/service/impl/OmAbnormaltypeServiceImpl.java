package com.hand.hmall.om.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hmall.om.dto.OmAbnormaltype;
import com.hand.hmall.om.service.IOmAbnormaltypeService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class OmAbnormaltypeServiceImpl extends BaseServiceImpl<OmAbnormaltype> implements IOmAbnormaltypeService{

}