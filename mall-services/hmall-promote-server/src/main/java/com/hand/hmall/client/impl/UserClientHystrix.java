package com.hand.hmall.client.impl;

import com.hand.hmall.client.IUserClient;
import com.hand.hmall.dto.ResponseData;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * Created by shanks on 2017/3/23.
 */
public class UserClientHystrix implements IUserClient{

    @Override
    public ResponseData queryByCondition(@RequestBody Map<String, Object> map) {
        return null;
    }

    @Override
    public Map<String, ?> getUser(@PathVariable("userId") String userId) {
        return null;
    }
}
