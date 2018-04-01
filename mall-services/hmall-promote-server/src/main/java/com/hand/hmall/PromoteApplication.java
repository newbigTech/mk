package com.hand.hmall;



import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.ImportResource;


@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
@EnableHystrix
@ImportResource(value = {"classpath:hsf/*.xml"})
public class PromoteApplication {
    public static void main(String[] args){
        new SpringApplicationBuilder(PromoteApplication.class).web(true).run(args);
    }
}
