package com.hand.hap.cloud.hpay.services.pcServices.wechat;

import com.hand.hap.cloud.hpay.data.BillDownloadData;
import com.hand.hap.cloud.hpay.data.ReturnData;

/**
 * @author jiaxin.jiang
 * @Title com.hand.hap.cloud.hpay.services.pcServices.alipay
 * @Description
 * @date 2017/9/4
 */
public interface IWechatBillDownloadService {

    /**
     * 微信对账单下载
     *
     * @param billDownloadData 对账单信息
     * @return 返回信息
     */
    ReturnData billDownloadFromWX(BillDownloadData billDownloadData);
}
