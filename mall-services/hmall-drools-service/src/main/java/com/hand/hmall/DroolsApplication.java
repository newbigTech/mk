package com.hand.hmall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * @author 唐磊
 * @version 0.1
 * @name:
 * @Description:
 * @date 2017/6/19 11:30
 */
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
@SpringBootApplication
@MapperScan(basePackages = "com.hand.hmall.mapper")
public class DroolsApplication {
    public static void main(String[] args) {
        try{
            new SpringApplicationBuilder(DroolsApplication.class).web(true).run(args);
        }catch(Throwable t){
            t.printStackTrace();
        }

    }

}
