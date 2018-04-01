package com.hand.hap.cloud.thirdParty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * @author jiaxin.jiang
 * @Title com.hand.hap.cloud.thirdParty
 * @Description
 * @date 2017/7/21
 */
@EnableFeignClients
//允许被eureka发现
@EnableEurekaClient
@SpringBootApplication
public class HapThirdPartyServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(HapThirdPartyServiceApplication.class, args);
    }
}
