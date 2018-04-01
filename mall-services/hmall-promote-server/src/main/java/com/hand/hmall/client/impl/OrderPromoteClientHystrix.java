package com.hand.hmall.client.impl;

import com.hand.hmall.client.IOrderPromoteClient;
import com.hand.hmall.dto.ResponseData;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

/**
 * Created by shanks on 2017/4/24.
 */
public class OrderPromoteClientHystrix implements IOrderPromoteClient{
    @Override
    public List<Object> queryTempIdByPOId(@PathVariable String id) {
        return null;
    }

    @Override
    public ResponseData insertPayment(Map map) {
        return null;
    }
}
