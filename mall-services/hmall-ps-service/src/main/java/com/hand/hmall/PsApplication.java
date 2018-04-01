package com.hand.hmall;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author 唐磊
 * @name: PsApplication
 * @description 启动类
 * @date 2017/5/24 15:30
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
@EnableHystrix
@EnableTransactionManagement
@ImportResource(value = {"classpath:hsf/*.xml"})
public class PsApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(PsApplication.class).web(true).run(args);
    }
}
