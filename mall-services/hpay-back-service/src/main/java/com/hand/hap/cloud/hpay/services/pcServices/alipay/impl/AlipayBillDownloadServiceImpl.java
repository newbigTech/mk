package com.hand.hap.cloud.hpay.services.pcServices.alipay.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayDataDataserviceBillDownloadurlQueryRequest;
import com.alipay.api.response.AlipayDataDataserviceBillDownloadurlQueryResponse;
import com.hand.hap.cloud.hpay.data.BillDownloadData;
import com.hand.hap.cloud.hpay.data.ReturnData;
import com.hand.hap.cloud.hpay.services.pcServices.alipay.IAlipayBillDownloadService;
import com.hand.hap.cloud.hpay.utils.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * @author jiaxin.jiang
 * @Title com.hand.hap.cloud.hpay.services.pcServices.alipay.impl
 * @Description 账单下载实现类
 * @date 2017/9/4
 */
@Service
public class AlipayBillDownloadServiceImpl implements IAlipayBillDownloadService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 支付宝账单下载
     *
     * @param billDownloadData 账单下载信息
     * @return 账单下载结果
     */
    @Override
    public ReturnData billDownloadFromAli(BillDownloadData billDownloadData) {

        logger.info("----------支付宝账单下载---------");

        AlipayClient alipayClient = new DefaultAlipayClient(
                PropertiesUtil.getHpayValue("alipay.gatewayUrl"),
                PropertiesUtil.getHpayValue("alipay.appId"),
                PropertiesUtil.getHpayValue("alipay.merchantPrivatekey"),
                PropertiesUtil.getHpayValue("alipay.format"),
                PropertiesUtil.getHpayValue("alipay.charset"),
                PropertiesUtil.getHpayValue("alipay.publicKey"),
                PropertiesUtil.getHpayValue("alipay.signType"));
        AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();

        String billDate;
        //将收到的yyyyMMdd格式的日期转化为yyyy-MM-dd
        try {
            DateFormat format = new SimpleDateFormat("yyyyMMdd");
            Date date = format.parse(billDownloadData.getBillDate());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            billDate = formatter.format(date);
        } catch (ParseException e) {
            logger.error("日期输入有误", e);
            return new ReturnData(
                    PropertiesUtil.getPayInfoValue("fail"),
                    "日期输入有误",
                    false
            );
        }
        logger.info("账单请求数据-" + billDownloadData.getBillDate() + "--" + billDownloadData.getBillType());
        logger.info("转换后日期-" + billDate);
        request.setBizContent("{" +
                "\"bill_type\":\"" + billDownloadData.getBillType() + "\"," +
                "\"bill_date\":\"" + billDate + "\"" +
                "  }");

        AlipayDataDataserviceBillDownloadurlQueryResponse billDownloadurlQueryResponse;

        try {
            //向支付宝请求下载账单
            billDownloadurlQueryResponse = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            //请求发送失败
            logger.error("请求支付宝失败", e);
            return new ReturnData(
                    PropertiesUtil.getPayInfoValue("fail"),
                    "请求支付宝失败",
                    false
            );
        }
        if (billDownloadurlQueryResponse.isSuccess()) {

            logger.info("调用成功-url-" + billDownloadurlQueryResponse.getBillDownloadUrl());
            FileOutputStream outstream = null;
            InputStream fis = null;
            try {
                URL url = new URL(billDownloadurlQueryResponse.getBillDownloadUrl());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                //设置链接参数
                connection.setConnectTimeout(5 * 1000);
                connection.setDoInput(true);//打开输入输出流
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Charsert", "UTF-8");
                connection.setRequestProperty("Connection", "Keep-Alive");
                //建立链接
                connection.connect();
                //获得输入流，文件为zip格式，
                //支付宝提供
                //20886126836996110156_20160906.csv.zip内包含
                //20886126836996110156_20160906_业务明细.csv
                //20886126836996110156_20160906_业务明细(汇总).csv
                //20886126836996110156_20160906_财务明细.csv
                //20886126836996110156_20160906_财务明细(汇总).csv
                fis = connection.getInputStream();
                //从url中获得文件名downloadFileName=20886126836996110156_20160909.csv.zip
                //直接将流转换成字符串出现zip内最后一个文件异常，只能先保存到本地，然后进行转换
                File file = new File(PropertiesUtil.getHpayValue("alipay.billFile"));
                if (!file.getParentFile().isDirectory()) {
                    file.getParentFile().mkdirs();
                }
                outstream = new FileOutputStream(file);
                //将获得数据流转换成byte，然后转成String，向内网传送
                byte[] temp = new byte[1024];
                int b;
                while ((b = fis.read(temp)) != -1) {
                    outstream.write(temp, 0, b);
                }

            } catch (IOException e) {
                logger.error("账单下载失败", e);
                return new ReturnData(
                        PropertiesUtil.getPayInfoValue("fail"),
                        "账单下载失败",
                        false
                );
            } finally {
                try {
                    outstream.flush();
                    outstream.close();
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            ArrayList<HashMap<String, String>> list = new ArrayList<>();
            HashMap<String, String> map = new HashMap<>();
            map.put("url", billDownloadurlQueryResponse.getBillDownloadUrl());
            list.add(map);
            return new ReturnData(
                    PropertiesUtil.getPayInfoValue("success"),
                    "账单下载成功",
                    list,
                    true
            );
        } else {
            if ("isp.bill_not_exist".equals(billDownloadurlQueryResponse.getSubCode())) {
                logger.info("账单下载调用成功，" + billDownloadurlQueryResponse.getSubCode());
                return new ReturnData(
                        PropertiesUtil.getPayInfoValue("noBill"),
                        billDownloadurlQueryResponse.getSubMsg(),
                        true
                );
            }
            logger.error("调用失败", billDownloadurlQueryResponse);
            return new ReturnData(
                    PropertiesUtil.getPayInfoValue("fail"),
                    billDownloadurlQueryResponse.getSubMsg(),
                    false
            );
        }
    }
}
