package com.hand.hap.cloud.hpay.services.pcServices.union.service.impl;

import com.hand.hap.cloud.hpay.data.BillDownloadData;
import com.hand.hap.cloud.hpay.data.ReturnData;
import com.hand.hap.cloud.hpay.services.pcServices.union.sdk.AcpService;
import com.hand.hap.cloud.hpay.services.pcServices.union.sdk.http.UnionPayHttp;
import com.hand.hap.cloud.hpay.services.pcServices.union.service.IUnionBillDownloadService;
import com.hand.hap.cloud.hpay.utils.DateFormatUtil;
import com.hand.hap.cloud.hpay.utils.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.hand.hap.cloud.hpay.services.constants.UnionConstants.*;

/**
 * @author jiaxin.jiang
 * @Title com.hand.hap.cloud.hpay.services.pcServices.union.service.impl
 * @Description
 * @date 2017/9/4
 */
@Service
public class UnionBillDownloadServiceImpl implements IUnionBillDownloadService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public ReturnData billDownloadFromUnion(BillDownloadData billDownloadData) {

        HashMap<String, String> data = new HashMap<>();
        /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
        //版本号 全渠道默认值
        data.put(PARAM_VERSION, UnionPayHttp.version);
        //字符集编码 可以使用UTF-8,GBK两种方式
        data.put(PARAM_ENCODING, UnionPayHttp.encoding);
        //签名方法
        data.put(PARAM_SIGNMETHOD, "01");
        //交易类型 76-对账文件下载
        data.put(PARAM_TXNTYPE, "76");
        //交易子类型 01-对账文件下载
        data.put(PARAM_TXNSUBTYPE, "01");
        //业务类型，固定
        data.put(PARAM_BIZTYPE, "000000");
        /***商户接入参数***/
        //接入类型，商户接入填0，不需修改
        data.put(PARAM_ACCESSTYPE, "0");
        //商户代码，请替换正式商户号测试，如使用的是自助化平台注册的777开头的商户号，该商户号没有权限测文件下载接口的，
        // 请使用测试参数里写的文件下载的商户号和日期测。如需777商户号的真实交易的对账文件，请使用自助化平台下载文件。
        data.put(PARAM_MERID, PropertiesUtil.getHpayValue("union.merId"));

        String billDate;
        try {
            DateFormat format = new SimpleDateFormat("yyyyMMdd");
            Date date = format.parse(billDownloadData.getBillDate());
            SimpleDateFormat formatter = new SimpleDateFormat("MMdd");
            billDate = formatter.format(date);
        } catch (ParseException e) {
            logger.error("日期输入有误", e);
            return new ReturnData(
                    PropertiesUtil.getPayInfoValue("fail"),
                    "日期输入有误",
                    false
            );
        }
        //清算日期，如果使用正式商户号测试则要修改成自己想要获取对账文件的日期， 测试环境如果使用700000000000001商户号则固定填写0119
        data.put(PARAM_SETTLE_DATE, billDate);
        //订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
        data.put(PARAM_TXNTIME, DateFormatUtil.getCurrentTime());
        //文件类型，一般商户填写00即可
        data.put(param_fileType, "00");

        /**请求参数设置完毕，以下对请求参数进行签名并发送http post请求，接收同步应答报文------------->**/
        Map<String, String> reqData = AcpService.sign(data,
                PropertiesUtil.getHpayValue("union.certPath"),
                PropertiesUtil.getHpayValue("union.certPwd"),
                UnionPayHttp.encoding);
        //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        String url = PropertiesUtil.getHpayValue("acpsdk.fileTransUrl");
        //获取请求银联的前台地址：对应属性文件acp_sdk.properties文件中的acpsdk.fileTransUrl
        Map<String, String> rspData = AcpService.post(reqData, url, UnionPayHttp.encoding);

        if (!rspData.isEmpty()) {
            if (AcpService.validate(rspData, UnionPayHttp.encoding)) {
                logger.info("验证签名成功");
                String respCode = rspData.get("respCode");
                String respMsg = rspData.get("respMsg");
                if ("00".equals(respCode)) {
                    String outPutDirectory = PropertiesUtil.getHpayValue("union.billFile");
                    //交易成功，解析返回报文中的fileContent并落地
                    File file = new File(outPutDirectory);
                    if (file.exists() && file.isDirectory()) {
                        for (File fi : file.listFiles()) {
                            fi.delete();
                        }
                    }
                    String zipFilePath = AcpService.deCodeFileContent(rspData, outPutDirectory, UnionPayHttp.encoding);
                    logger.info("文件已下载至-" + zipFilePath);
                    ArrayList<HashMap<String, String>> list = new ArrayList<>();
                    HashMap<String, String> map = new HashMap<>();
                    map.put("url", zipFilePath);
                    list.add(map);
                    return new ReturnData(
                            PropertiesUtil.getPayInfoValue("success"),
                            "账单下载成功",
                            list,
                            true
                    );
                } else if ("98".equals(respCode)) {
                    String msgCode = PropertiesUtil.getPayInfoValue("noBill");
                    return new ReturnData(
                            msgCode,
                            respCode + "-" + respMsg,
                            true
                    );
                } else {
                    //其他应答码为失败请排查原因
                    logger.error(respCode);
                    return new ReturnData(
                            PropertiesUtil.getPayInfoValue("fail"),
                            respCode + "-" + respMsg,
                            false
                    );
                }
            } else {
                logger.error("银联验证签名失败");
                return new ReturnData(
                        PropertiesUtil.getPayInfoValue("fail"),
                        "银联验证签名失败",
                        false
                );
            }
        } else {
            //未返回正确的http状态
            logger.error("未获取到返回报文或返回http状态码非200");
            return new ReturnData(
                    PropertiesUtil.getPayInfoValue("fail"),
                    "未获取到返回报文或返回http状态码非200",
                    false
            );
        }
    }
}
