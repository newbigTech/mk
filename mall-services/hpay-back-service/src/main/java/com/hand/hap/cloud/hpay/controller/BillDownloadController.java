package com.hand.hap.cloud.hpay.controller;

import com.hand.hap.cloud.hpay.data.BillDownloadData;
import com.hand.hap.cloud.hpay.data.ReturnData;
import com.hand.hap.cloud.hpay.services.pcServices.alipay.IAlipayBillDownloadService;
import com.hand.hap.cloud.hpay.services.pcServices.union.service.IUnionBillDownloadService;
import com.hand.hap.cloud.hpay.services.pcServices.wechat.IWechatBillDownloadService;
import com.hand.hap.cloud.hpay.utils.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jiaxin.jiang
 * @Title com.hand.hap.cloud.hpay.controller
 * @Description
 * @date 2017/9/4
 */
@RestController
@RequestMapping(value = "/v1")
public class BillDownloadController {

    @Autowired
    private IAlipayBillDownloadService iAlipayBillDownloadService;

    @Autowired
    private IWechatBillDownloadService iWechatBillDownloadService;

    @Autowired
    private IUnionBillDownloadService iUnionBillDownloadService;

    @RequestMapping(value = "/billDownload", method = RequestMethod.POST)
    public ReturnData billDownload(@RequestBody BillDownloadData billDownloadData) {

        //判断支付模式，选择相应的账单下载
        if (PropertiesUtil.getPayInfoValue("mode.alipay").equalsIgnoreCase(billDownloadData.getMode())) {
            return iAlipayBillDownloadService.billDownloadFromAli(billDownloadData);
        } else if (PropertiesUtil.getPayInfoValue("mode.wechatpay").equalsIgnoreCase(billDownloadData.getMode())) {
            return iWechatBillDownloadService.billDownloadFromWX(billDownloadData);
        } else {
            return iUnionBillDownloadService.billDownloadFromUnion(billDownloadData);
        }

    }
}
