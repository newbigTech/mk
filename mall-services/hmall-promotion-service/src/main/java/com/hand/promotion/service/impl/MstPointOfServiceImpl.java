package com.hand.promotion.service.impl;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import com.hand.promotion.dto.MstPointOfService;
import com.hand.promotion.mapper.MstPointOfServiceMapper;
import com.hand.promotion.service.IMstPointofServiceService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author XinyangMei
 * @Title MstPointOfServiceImpl
 * @Description 门店信息相关接口
 * @date 2017/10/26 9:07
 */
@Service
public class MstPointOfServiceImpl implements IMstPointofServiceService {
    @Autowired
    private MstPointOfServiceMapper mstPointOfServiceMapper;

    /**
     * 根据门店编码查询门店信息
     *
     * @param code 门店编码
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
