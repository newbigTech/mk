package com.hand.promotion.client;

import com.hand.hmall.dto.ResponseData;
import com.hand.promotion.client.impl.UserClientServiceImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by shanks on 2017/2/27.
 */
@FeignClient(value = "user", fallback = UserClientServiceImpl.class)
public interface IUserClientService {
    /**
     * 该方法未使用
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/o/customer/address/default/{userId}", method = RequestMethod.GET)
    public ResponseData queryDefault(@PathVariable("userId") String userId);
}
