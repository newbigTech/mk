package com.hand.hap.cloud.hpay.services.pcServices.wechat.impl;

import com.hand.hap.cloud.hpay.data.BillDownloadData;
import com.hand.hap.cloud.hpay.data.ReturnData;
import com.hand.hap.cloud.hpay.services.pcServices.wechat.IWechatBillDownloadService;
import com.hand.hap.cloud.hpay.services.phoneServices.wechat.util.HttpUtil;
import com.hand.hap.cloud.hpay.services.phoneServices.wechat.util.PayCommonUtil;
import com.hand.hap.cloud.hpay.services.phoneServices.wechat.util.WXPayUtil;
import com.hand.hap.cloud.hpay.utils.PropertiesUtil;
import com.hand.hap.cloud.hpay.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static com.hand.hap.cloud.hpay.utils.XmlUtil.xml2map;

/**
 * @author jiaxin.jiang
 * @Title com.hand.hap.cloud.hpay.services.pcServices.alipay.impl
 * @Description
 * @date 2017/9/4
 */
@Service
public class WechatBillDownloadServiceImpl implements IWechatBillDownloadService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 微信对账单下载
     *
     * @param billDownloadData 对账单信息
     * @return 返回信息
     */
    @Override
    public ReturnData billDownloadFromWX(BillDownloadData billDownloadData) {
        // 账号信息
        // appid
        String appId = PropertiesUtil.getHpayValue("wechat.appId");
        // 商业号
        String mchId = PropertiesUtil.getHpayValue("wechat.mchId");
        // key
        String key = PropertiesUtil.getHpayValue("wechat.key");
        //随机串
        String nonceStr = Util.getRandomStringByLength(32);
        //
        String billDate = billDownloadData.getBillDate();
        //
        String billType = billDownloadData.getBillType();
        // 回调接口
        SortedMap<String, String> requestBillDownloadMapToWX = new TreeMap<>();

        requestBillDownloadMapToWX.put("appid", appId);
        requestBillDownloadMapToWX.put("mch_id", mchId);
        requestBillDownloadMapToWX.put("nonce_str", nonceStr);
        requestBillDownloadMapToWX.put("bill_date", billDate);
        requestBillDownloadMapToWX.put("bill_type", billType);

        //签名
        String sign = null;
        try {
            sign = WXPayUtil.generateSignature(requestBillDownloadMapToWX, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        requestBillDownloadMapToWX.put("sign", sign);
        logger.info("微信request-requestMap==》" + requestBillDownloadMapToWX.toString());

        //请求参数
        String requestBillDownloadXMLToWX = PayCommonUtil.getRequestXml(requestBillDownloadMapToWX);
        logger.info("微信请求参数==>" + requestBillDownloadXMLToWX);

        //返回参数
        String responseBillDownloadXmlFromWX = HttpUtil.postData(PropertiesUtil.getHpayValue("wechat.downloadBillApi"), requestBillDownloadXMLToWX);
        logger.info("微信response-responseXml" + responseBillDownloadXmlFromWX);

        if (responseBillDownloadXmlFromWX.startsWith("<xml>")) {
//        转换为map
            Map<String, String> responseBillDownloadMapFromWX = xml2map(responseBillDownloadXmlFromWX, false);
            if ("20002".equals(responseBillDownloadMapFromWX.get("error_code"))
                    && "No Bill Exist".equals(responseBillDownloadMapFromWX.get("return_msg"))) {
                return new ReturnData(PropertiesUtil.getPayInfoValue("noBill"),
                        "return_code:" + responseBillDownloadMapFromWX.get("return_code") + "-return_msg:" + responseBillDownloadMapFromWX.get("return_msg") + "-error_code:" + responseBillDownloadMapFromWX.get("error_code"),
                        true
                );
            }
            logger.info("微信response-map" + responseBillDownloadMapFromWX);
            return new ReturnData(PropertiesUtil.getPayInfoValue("fail"),
                    "return_code:" + responseBillDownloadMapFromWX.get("return_code") + "-return_msg:" + responseBillDownloadMapFromWX.get("return_msg") + "-error_code:" + responseBillDownloadMapFromWX.get("error_code"),
                    false
            );
        } else {
            File file = new File(PropertiesUtil.getHpayValue("wechat.billFile"));
            if (!file.getParentFile().isDirectory()) {
                file.getParentFile().mkdirs();
            }
            byte bytes[] = responseBillDownloadXmlFromWX.getBytes();
            //是字节的长度，不是字符串的长度
            int b = bytes.length;
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                fos.write(bytes, 0, b);

            } catch (FileNotFoundException e) {
                logger.error("文件输出失败", e);
                return new ReturnData(PropertiesUtil.getPayInfoValue("fail"),
                        "文件输出失败",
                        false
                );
            } catch (IOException e) {
                logger.error("账单写出失败", e);
                return new ReturnData(PropertiesUtil.getPayInfoValue("fail"),
                        "账单写出失败",
                        false
                );
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            logger.info("账单下载成功");
            return new ReturnData(PropertiesUtil.getPayInfoValue("success"),
                    "账单下载成功",
                    true
            );
        }
    }
}
