package com.hand.hap.cloud.hpay.controller;

import com.hand.hap.cloud.hpay.data.QueryData;
import com.hand.hap.cloud.hpay.data.ReturnData;
import com.hand.hap.cloud.hpay.services.pcServices.alipay.IAlipayQueryService;
import com.hand.hap.cloud.hpay.services.pcServices.union.service.IUnionQueryService;
import com.hand.hap.cloud.hpay.services.pcServices.wechat.IWechatQueryService;
import com.hand.hap.cloud.hpay.utils.PropertiesUtil;
import com.hand.hap.cloud.hpay.validateData.ValidataQueryData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jiaxin.jiang
 * @Title com.hand.hap.cloud.hpay.controller
 * @Description 查询控制
 * @date 2017/7/10
 */
@RestController
@RequestMapping(value = "/v1")
public class QueryController {

    @Autowired
    private ValidataQueryData validataQueryData;

    @Autowired
    private IUnionQueryService unionPayQueryOrderService;

    @Autowired
    private IAlipayQueryService alipayQueryService;

    @Autowired
    private IWechatQueryService ipcWechatQueryService;

    /**
     * 订单查询
     *
     * @param queryData 查询数据
     * @return returndata returndata
     * @throws Exception exception
     */
    @RequestMapping(value = "/Query", method = RequestMethod.POST)
    public ReturnData query(@RequestBody QueryData queryData)
            throws Exception {

        ReturnData rd = validataQueryData.validataQueryData(queryData);
        //验证数据
        if (!rd.isSuccess()) {
            return rd;
        }

        if (PropertiesUtil.getPayInfoValue("mode.unionpay").equals(queryData.getMode())) {
            return unionPayQueryOrderService.unionQuery(queryData);
        } else if (PropertiesUtil.getPayInfoValue("mode.alipay").equals(queryData.getMode())) {
            if (PropertiesUtil.getPayInfoValue("paymentTypeCode.pay").equals(queryData.getPaymentTypeCode())) {
                return alipayQueryService.alipayPayQuery(queryData);
            } else {
                return alipayQueryService.alipayRefundQuery(queryData);
            }
        } else {
            if (PropertiesUtil.getPayInfoValue("paymentTypeCode.pay").equals(queryData.getPaymentTypeCode())) {
                return ipcWechatQueryService.wechatPayQuery(queryData);
            } else {
                return ipcWechatQueryService.wechatRefundQuery(queryData);
            }
        }
    }
}
