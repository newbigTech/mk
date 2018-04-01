package com.hand.hap.cloud.hpay.services.pcServices.alipay;

import com.hand.hap.cloud.hpay.data.OrderData;
import com.hand.hap.cloud.hpay.data.ReturnData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author jiaxin.jiang
 * @Title
 * @Description
 * @date 2017/7/6
 */
public interface IAlipayPayService {

    /**
     * alipay支付
     *
     * @param orderData 订单信息
     * @return returndata 返回信息
     * @throws Exception exception
     */
    ReturnData pcAlipay(OrderData orderData) throws Exception;

    /**
     * alipay回调
     *
     * @param request  request
     * @param response response
     * @return String fail/success
     * @throws IOException ioexception
     */
    String alipayNotify(HttpServletRequest request, HttpServletResponse response);

}
