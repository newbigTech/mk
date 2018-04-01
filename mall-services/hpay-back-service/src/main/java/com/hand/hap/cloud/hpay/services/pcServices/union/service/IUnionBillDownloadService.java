package com.hand.hap.cloud.hpay.services.pcServices.union.service;

import com.hand.hap.cloud.hpay.data.BillDownloadData;
import com.hand.hap.cloud.hpay.data.ReturnData;

/**
 * @author jiaxin.jiang
 * @Title com.hand.hap.cloud.hpay.services.pcServices.alipay
 * @Description
 * @date 2017/9/4
 */
public interface IUnionBillDownloadService {
    ReturnData billDownloadFromUnion(BillDownloadData billDownloadData);
}
