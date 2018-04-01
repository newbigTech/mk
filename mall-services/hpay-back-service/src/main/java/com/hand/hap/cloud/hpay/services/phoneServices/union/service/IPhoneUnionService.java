package com.hand.hap.cloud.hpay.services.phoneServices.union.service;

import com.hand.hap.cloud.hpay.data.OrderData;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author jiaxin.jiang
 * @Title com.hand.hap.cloud.hpay.services.phoneServices.alipay
 * @Description
 * @date 2017/8/3
 */
public interface IPhoneUnionService {
    /**
     * 银联h5支付
     *
     * @param request request
     * @param resp resp
     * @param orderData 订单数据
     * @return string
     * @throws ServletException servletException
     * @throws IOException IOException
     */
    String phoneH5Unionpay(HttpServletRequest request, HttpServletResponse resp, OrderData orderData) throws ServletException, IOException;

}
