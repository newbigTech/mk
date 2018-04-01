package com.hand.hmall.client.impl;

import com.hand.hmall.client.IUserClientService;
import com.hand.hmall.dto.ResponseData;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by shanks on 2017/2/27.
 */
public class UserClientServiceImpl implements IUserClientService {
    @Override
    public ResponseData queryDefault(@PathVariable("userId") String userId) {
        return null;
    }

}
