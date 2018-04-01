package com.hand.hap.cloud.hpay.services.pcServices.wechat;

import com.hand.hap.cloud.hpay.data.QueryData;
import com.hand.hap.cloud.hpay.data.ReturnData;

/**
 * @author jiaxin.jiang
 * @Title com.hand.hap.cloud.hpay.services.pcServices.wechat.service
 * @Description
 * @date 2017/8/21
 */
public interface IWechatQueryService {

    /**
     * 微信支付查询
     *
     * @param queryData 查询数据
     * @return returnData
     * @throws Exception exception
     */
    ReturnData wechatPayQuery(QueryData queryData) throws Exception;

    /**
     * 微信退款查询
     *
     * @param queryData 查询数据
     * @return returnData
     * @throws Exception exception
     */
    ReturnData wechatRefundQuery(QueryData queryData) throws Exception;
}
