package com.hand.hap.cloud.hpay.services.phoneServices.wechat.service;

import com.hand.hap.cloud.hpay.data.OrderData;
import com.hand.hap.cloud.hpay.data.ReturnData;
import org.dom4j.DocumentException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jiaxin.jiang
 * @Title com.hand.hap.cloud.hpay.services.phoneServices.alipay
 * @Description
 * @date 2017/8/3
 */
public interface IPhoneWechatService {

    /**
     * wechatH5支付
     *
     * @param request request
     * @param resp resp
     * @param orderData 支付数据
     * @return returnData
     * @throws Exception exception
     */
    ReturnData phoneH5Wechat(HttpServletRequest request, HttpServletResponse resp, OrderData orderData);
}
