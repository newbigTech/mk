package com.hand.hap.cloud.hpay.services.pcServices.alipay;

import com.hand.hap.cloud.hpay.data.QueryData;
import com.hand.hap.cloud.hpay.data.ReturnData;

/**
 * @author jiaxin.jiang
 * @Title
 * @Description
 * @date 2017/7/6
 */

public interface IAlipayQueryService {

    /**
     * 支付宝查询
     *
     * @param queryData 请求数据
     * @return ReturnData
     */
    ReturnData alipayPayQuery(QueryData queryData);

    /**
     * 支付宝退款查询
     *
     * @param queryData 查询数据
     * @return returnData
     */
    ReturnData alipayRefundQuery(QueryData queryData);
}