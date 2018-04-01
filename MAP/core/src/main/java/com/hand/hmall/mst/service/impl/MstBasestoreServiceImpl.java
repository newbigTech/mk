package com.hand.hmall.mst.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hmall.mst.dto.MstBasestore;
import com.hand.hmall.mst.service.IMstBasestoreService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class MstBasestoreServiceImpl extends BaseServiceImpl<MstBasestore> implements IMstBasestoreService{

}