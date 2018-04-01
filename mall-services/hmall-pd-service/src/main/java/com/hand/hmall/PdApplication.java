package com.hand.hmall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * <p>...</p>
 *
 * @author 马君
 * @version 0.1
 * @name: Application
 * @date 2017/6/2 11:32
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
@EnableHystrix
@EnableTransactionManagement
@MapperScan(basePackages = "com.hand.hmall.mapper")
public class PdApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(PdApplication.class).web(true).run(args);
    }
}
