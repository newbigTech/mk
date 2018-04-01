package com.hand.hap.cloud.hpay.services.pcServices.wechat;

import com.hand.hap.cloud.hpay.data.OrderData;
import com.hand.hap.cloud.hpay.data.ReturnData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jiaxin.jiang
 * @Title com.hand.hap.cloud.hpay.services.pcServices.wechat.service
 * @Description
 * @date 2017/8/21
 */
public interface IPCWechatPayService {
    /**
     * 微信电脑支付
     *
     * @param request request
     * @param resp resp
     * @param orderData 订单数据
     * @return returnData
     * @throws Exception exception
     */
    ReturnData pcWechat(HttpServletRequest request, HttpServletResponse resp, OrderData orderData) throws Exception;

    /**
     * 微信回调
     *
     * @param request request
     * @param response response
     * @throws Exception exception
     */
    void weChatNotify(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
