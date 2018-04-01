package com.hand.hap.cloud.hpay.services.publicsign.wechat;

import com.hand.hap.cloud.hpay.data.OrderData;
import com.hand.hap.cloud.hpay.data.ReturnData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author:zhangyanan
 * @Description:微信公众号支付接口
 * @Date:Crated in 15:31 2017/10/21
 * @Modified By:
 */
public interface IPublicSignService {

    /**
     * 微信公众号支付
     *
     * @param request   request
     * @param resp      resp
     * @param orderData 订单数据
     * @return returnData
     * @throws Exception exception
     */
    ReturnData publicSign(HttpServletRequest request, HttpServletResponse resp, OrderData orderData) throws Exception;

    /**
     * 根据zmall传送过来的Code,调用微信接口获取access_token、openId等信息,并将openId等信息传给Zmall
     *
     * @param code
     */
    ReturnData getOpenIdByCode(String code);
}
