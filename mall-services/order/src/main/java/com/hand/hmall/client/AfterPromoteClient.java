package com.hand.hmall.client;

import com.hand.hmall.client.impl.AfterPromoteClientHystrix;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.model.HmallOmOrder;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author 李伟
 * @version 1.0
 * @name:AfterPromoteClient
 * @Description: 订单下载返回事后促销信息
 * @date 2017/10/18 16:08
 */
@FeignClient(value = "hmall-drools-service",fallback = AfterPromoteClientHystrix.class)
public interface AfterPromoteClient
{
    /**
     * 事后促销 订单支付后调用
     * @return
     */
    @RequestMapping(value = "/afterPromote/operate/checkAfterPromote", method = RequestMethod.GET)
    ResponseData checkAfterPromote(HmallOmOrder order);


    /**
     * 事后促销 确认收货后调用
     * @return
     */
    @RequestMapping(value = "/afterPromote/operate/checkAfterPromoteWithCofrim",method = RequestMethod.GET)
    ResponseData checkAfterPromoteWithCofrim(HmallOmOrder order);
}

