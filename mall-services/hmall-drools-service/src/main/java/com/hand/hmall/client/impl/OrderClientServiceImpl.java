package com.hand.hmall.client.impl;

import com.hand.hmall.client.IOrderClientService;
import com.hand.hmall.dto.ResponseData;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by shanks on 2017/2/14.
 */
public class OrderClientServiceImpl implements IOrderClientService {
    @RequestMapping(value = "/order/queryForTempOrders", method = RequestMethod.POST)
    public ResponseData queryForTempOrders(@RequestBody List<String> tempIds) {
        return null;
    }
}
