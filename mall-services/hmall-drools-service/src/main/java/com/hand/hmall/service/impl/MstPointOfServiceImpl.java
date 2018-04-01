package com.hand.hmall.service.impl;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import com.hand.hmall.dto.MstPointOfService;
import com.hand.hmall.mapper.MstPointOfServiceMapper;
import com.hand.hmall.service.IMstPointofServiceService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author XinyangMei
 * @Title MstPointOfServiceImpl
 * @Description desp
 * @date 2017/10/26 9:07
 */
@Service
public class MstPointOfServiceImpl implements IMstPointofServiceService {
    @Autowired
    private MstPointOfServiceMapper mstPointOfServiceMapper;

    /**
     * 根据门店编码查询门店信息
     *
     * @param code
     * @return
     */
    @Override
    public MstPointOfService queryByCode(String code) {
        List<MstPointOfService> mstPointOfServices = mstPointOfServiceMapper.selectByCode(code);
        if (CollectionUtils.isEmpty(mstPointOfServices)) {
            return null;
        } else {
            return mstPointOfServices.get(0);
        }
    }
}
