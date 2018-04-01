package com.hand.hap.cloud.hpay;

import com.hand.hap.cloud.hpay.utils.PropertiesUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

/**
 * @author jiaxin.jiang
 * @Title
 * @Description
 * @date 2017/7/6
 */
@EnableFeignClients
@EnableHystrix
@EnableDiscoveryClient
@SpringBootApplication
public class HpayBackServiceApplication {
    public static void main(String[] args) {
        PropertiesUtil.init();
        SpringApplication.run(HpayBackServiceApplication.class, args);
    }
}
