package com.hand.hmall;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

/**
 * @author 唐磊
 * @Title:
 * @Description:
 * @date 2017/5/24 16:28
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
@EnableHystrix
public class OdApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(OdApplication.class).web(true).run(args);
    }
}
