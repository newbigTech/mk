package com.hand.promotion.client.impl;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import com.hand.dto.ResponseData;
import com.hand.promotion.client.IPdClientService;
import org.springframework.stereotype.Component;

/**
 * @author XinyangMei
 * @Title PdClientServiceImpl
 * @Description desp
 * @date 2017/8/4 10:56
 */
@Component(value = "pdClientService")
public class PdClientServiceImpl implements IPdClientService {

    @Override
    public ResponseData getProductPrice(Integer productId) {
        return null;
    }
}
