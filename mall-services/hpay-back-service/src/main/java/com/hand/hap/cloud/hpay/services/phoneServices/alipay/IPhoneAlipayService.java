package com.hand.hap.cloud.hpay.services.phoneServices.alipay;

import com.hand.hap.cloud.hpay.data.OrderData;
import com.hand.hap.cloud.hpay.data.ReturnData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jiaxin.jiang
 * @Title com.hand.hap.cloud.hpay.services.phoneServices.alipay
 * @Description
 * @date 2017/8/3
 */
public interface IPhoneAlipayService {

    /**
     * alipayH5支付
     *
     * @param request request
     * @param resp resp
     * @param orderData 订单数据
     * @return returnData
     * @throws Exception exception
     */
    ReturnData phoneH5Alipay(HttpServletRequest request, HttpServletResponse resp, OrderData orderData) throws Exception;
}
