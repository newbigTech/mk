package com.hand.hmall.config;

import com.hand.hmall.service.IPriceCalculateCXFService;
import com.hand.hmall.service.IPriceCalculateService;
import com.hand.hmall.service.IProductCXFService;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

/**
 * @author 马君
 * @version 0.1
 * @name: CxfConfig
 * @Description: springboot和cxf整合配置
 * @date 2017/5/26 14:29
 */
@Configuration
@ComponentScan(basePackages = {"com.hand.hmall.service"})
public class CXFConfig {

    @Bean(name = "CXFServlet")
    public ServletRegistrationBean dispatcherServlet() {
        return new ServletRegistrationBean(new CXFServlet(), "/soap/*");
    }

    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {
        return new SpringBus();
    }

    @Bean(name = "productWebservice")
    public Endpoint productWebservice(IProductCXFService iProductCXFService) {
        EndpointImpl endpoint = new EndpointImpl(springBus(), iProductCXFService);
        endpoint.publish("/product");
        return endpoint;
    }

    @Bean(name = "priceWebservice")
    public Endpoint priceWebservice(IPriceCalculateCXFService iPriceCalculateCXFService) {
        EndpointImpl endpoint = new EndpointImpl(springBus(), iPriceCalculateCXFService);
        endpoint.publish("/price");
        return endpoint;
    }
}

