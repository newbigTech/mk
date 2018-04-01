package hmall;

import com.hand.hmall.dto.ResponseData;
import hmall.filter.InternetAccessFilter;
import hmall.filter.ProxyHeaderFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 唐磊
 * @Title:
 * @Description:
 * @date 2017/5/24 17:09
 */
@EnableZuulProxy
@RestController
@ComponentScan({"com.markor.unilog"})
@SpringBootApplication
@ImportResource(value = {"classpath:hsf/*.xml"})
public class GatewayApplication {
    private static Logger logger = LogManager.getLogger(GatewayApplication.class);

    @Autowired
    private DiscoveryClient discoveryClient;

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @GetMapping("error")
    public ResponseData error() {
        ResponseData responseData = new ResponseData(false);
        responseData.setMsgCode("Time_Out");
        return responseData;
    }

    @GetMapping(value = "services", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseData getServiceName() {
        List<String> services = discoveryClient.getServices();
        logger.info("Get all services:");
        for (String service : services) {
            logger.info("service :" + service);
        }
        return new ResponseData(services);
    }

    /**
     * 外网访问权限过滤器
     *
     * @return
     */
    @Bean
    public InternetAccessFilter internetAccessFilter() {
        return new InternetAccessFilter();
    }

    @Bean
    public ProxyHeaderFilter proxyHeaderFilter() {
        return new ProxyHeaderFilter();
    }

}
