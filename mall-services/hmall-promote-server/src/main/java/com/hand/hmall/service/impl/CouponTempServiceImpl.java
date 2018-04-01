package com.hand.hmall.service.impl;

import com.hand.hmall.dao.CouponTempDao;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.service.ICouponTempService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by shanks on 2017/3/20.
 */
@Service
public class CouponTempServiceImpl implements ICouponTempService{

    @Autowired
    private CouponTempDao couponTempDao;

    @Override
    public ResponseData addCouponTemp(List<Map<String, Object>> maps) {
        for(Map<String,Object> map:maps){
            couponTempDao.submit(map);
        }
        return new ResponseData(maps);
    }
}
