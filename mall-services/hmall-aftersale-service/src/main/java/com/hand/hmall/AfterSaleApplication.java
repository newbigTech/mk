package com.hand.hmall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author 马君
 * @version 0.1
 * @name AfterSaleApplication
 * @description 售后服务启动类
 * @date 2017/7/22 9:56
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
@EnableHystrix
@EnableTransactionManagement
@MapperScan(basePackages = "com.hand.hmall.mapper")
@ImportResource(value = {"classpath:hsf/*.xml"})
public class AfterSaleApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(AfterSaleApplication.class).web(true).run(args);
    }
}
