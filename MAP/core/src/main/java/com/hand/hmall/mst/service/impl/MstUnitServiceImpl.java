package com.hand.hmall.mst.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hmall.mst.dto.MstUnit;
import com.hand.hmall.mst.service.IMstUnitService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author xiaoyu.ran
 * @version 0.1
 * @name:
 * @Description: 商品单位维护对象的Service实现类
 * @date 2017/9/5 14:54
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MstUnitServiceImpl extends BaseServiceImpl<MstUnit> implements IMstUnitService{

}