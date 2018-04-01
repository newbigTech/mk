package com.hand.hmall.client;

import com.hand.hmall.client.impl.UserClientHystrix;
import com.hand.hmall.dto.ResponseData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * Created by shanks on 2017/3/23.
 */
@FeignClient(value = "user", fallback = UserClientHystrix.class)
public interface IUserClient {

    @RequestMapping(value = "/i/customer/info/queryByCondition" , method = RequestMethod.POST)
    public ResponseData queryByCondition(@RequestBody Map<String, Object> map);

    @RequestMapping(value = "/i/query/{customerId}", method = RequestMethod.GET)
    public Map<String, ?> getUser(@PathVariable("customerId") String userId);
}
