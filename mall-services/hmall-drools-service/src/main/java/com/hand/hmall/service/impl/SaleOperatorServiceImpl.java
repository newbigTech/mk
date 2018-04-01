package com.hand.hmall.service.impl;

import com.hand.hmall.dao.SaleOperatorDao;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.service.ISaleOperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by shanks on 2017/1/19.
 * 促销活动、促销模板、优惠券 修改人信息查询
 */
@Service
public class SaleOperatorServiceImpl implements ISaleOperatorService {
    @Autowired
    private SaleOperatorDao saleOperatorDao;

    @Override
    public ResponseData queryByBaseId(String baseId, int page, int pageSize) {
        return new ResponseData(saleOperatorDao.queryByBaseId(baseId,page,pageSize));
    }
}
