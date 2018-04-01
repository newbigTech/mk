package com.hand.promotion.client.impl;

import com.hand.hmall.dto.ResponseData;
import com.hand.promotion.client.IOrderClientService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

/**
 * Created by shanks on 2017/2/14.
 */
@Component
public class OrderClientServiceImpl implements IOrderClientService {
    @Override
    public ResponseData queryForTempOrders(@RequestBody List<String> tempIds) {
        return null;
    }
}
