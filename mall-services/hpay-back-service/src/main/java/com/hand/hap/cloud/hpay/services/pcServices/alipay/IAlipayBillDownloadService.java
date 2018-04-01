package com.hand.hap.cloud.hpay.services.pcServices.alipay;

import com.hand.hap.cloud.hpay.data.BillDownloadData;
import com.hand.hap.cloud.hpay.data.ReturnData;

/**
 * @author jiaxin.jiang
 * @Title com.hand.hap.cloud.hpay.services.pcServices.alipay
 * @Description
 * @date 2017/9/4
 */
public interface IAlipayBillDownloadService {
    /**
     * 支付宝账单下载
     *
     * @param billDownloadData 账单下载信息
     * @return 账单下载结果
     */
    ReturnData billDownloadFromAli(BillDownloadData billDownloadData);
}
