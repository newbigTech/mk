package com.hand.hap.cloud.hpay.services.pcServices.union.service;


import com.hand.hap.cloud.hpay.data.OrderData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author jiaxin.jiang
 * @Title
 * @Description
 * @date 2017/7/6
 */
public interface IUnionPayService {

    /**
     * 银联pc支付
     *
     * @param unionData 支付信息
     * @return string
     */
    String pcUnion(OrderData unionData);

    /**
     * 银联回调
     *
     * @param request request
     * @param response response
     * @throws Exception exception
     */
    void unionNotify(HttpServletRequest request, HttpServletResponse response);

    /**
     * 遍历回调参数
     *
     * @param request request
     * @return map map
     */
    Map<String, String> getAllRequestParam(HttpServletRequest request);
}
