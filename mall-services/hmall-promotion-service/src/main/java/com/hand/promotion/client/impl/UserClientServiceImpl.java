package com.hand.promotion.client.impl;

import com.hand.hmall.dto.ResponseData;
import com.hand.promotion.client.IUserClientService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by shanks on 2017/2/27.
 */
@Component
public class UserClientServiceImpl implements IUserClientService {
    @Override
    public ResponseData queryDefault(String userId) {
        return null;
    }

}
