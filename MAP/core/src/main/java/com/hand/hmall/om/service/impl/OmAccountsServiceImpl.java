package com.hand.hmall.om.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.markor.map.framework.restclient.RestClient;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.core.impl.ServiceRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.ExcelUtil;
import com.hand.hap.util.FormatUtils;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.om.dto.OmAccounts;
import com.hand.hmall.om.dto.OmAccountsRecord;
import com.hand.hmall.om.mapper.OmAccountsMapper;
import com.hand.hmall.om.mapper.OmAccountsRecordMapper;
import com.hand.hmall.om.service.IOmAccountsService;
import com.hand.hmall.util.Constants;
import com.hand.hmall.util.UnionBillAnalysisUtil.SDKUtil;
import com.hand.hmall.util.zipUtil.ZipEntry;
import com.hand.hmall.util.zipUtil.ZipInputStream;
import com.hand.hmall.ws.entities.BillRequestBody;
import com.hand.hmall.ws.entities.BillRequestItem;
import com.hand.hmall.ws.entities.CheckBillRequestBody;
import com.hand.hmall.ws.entities.CheckBillRequestItem;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import okhttp3.Response;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class OmAccountsServiceImpl extends BaseServiceImpl<OmAccounts> implements IOmAccountsService {

    //支付方式
    private static final String WECHAT_PAYMETHOD = "WECHAT";
    private static final String UNIONPAY_PAYMETHOD = "UNIONPAY";
    private static final String ALIPAY_PAYMETHOD = "ALIPAY";
    private static String billDirectoryPath;
    //商户代码
    private static String unionMerId;

    static {
        billDirectoryPath = ResourceBundle.getBundle("bussiness", Locale.getDefault()).getString("billDirectory.path");
        unionMerId = ResourceBundle.getBundle("bussiness", Locale.getDefault()).getString("union.merId");
    }

    @Autowired
    private OmAccountsMapper omAccountsMapper;

    @Autowired
    private OmAccountsRecordMapper omAccountsRecordMapper;

    @Autowired
    private RestClient restClient;

    /**
     * 支付宝账单解析
     *
     * @return 返回信息
     */
    @Override
    public ResponseData AlipayBillAnalysisService(Date billDate) {

        IRequest iRequest = RequestHelper.newEmptyRequest();
        iRequest.setRoleId(Constants.JOB_DEFAULT_USERID);
        iRequest.setLocale(Constants.JOB_DEFAULT_LANG);

        HashMap<String, String> contentType = new HashMap<>();
        contentType.put("Content-Type", "application/json");

        //请求支付宝账单下载接口
        Map<String, String> requestHpayMap = new HashMap<>();
        requestHpayMap.put("mode", "ALIPAY");
        requestHpayMap.put("billType", "signcustomer");
        requestHpayMap.put("billDate", new SimpleDateFormat("yyyyMMdd").format(billDate));

        String alipayBillDownloadData = JSON.toJSONString(requestHpayMap);

        Response responseFromAlipay;
        JSONObject alipayResult;
        try {
            responseFromAlipay = restClient.postString(Constants.HPAY, "/hpay/v1/billDownload", alipayBillDownloadData, "json", null, contentType);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseData(false, "请求HPAY失败");
        }
        String success;
        String msg;
        String msgCode;
        try {
            alipayResult = JSONObject.fromObject(responseFromAlipay.body().string());
            success = alipayResult.getString("success");
            msg = alipayResult.getString("msg");
            msgCode = alipayResult.getString("msgCode");
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseData(false, "获得HPAY信息失败");
        }
        //下载成功
        if ("true".equalsIgnoreCase(success)
                && !"noBill".equalsIgnoreCase(msgCode)) {
            //读取账单文件
            ArrayList<String> lineList = new ArrayList<>();
            File billFile = new File(billDirectoryPath + "alipayBill" + File.separator + "alipayBill.csv.zip");
//            File billFile = new File("C:\\bill\\alipayBill\\alipayBill.csv.zip");

            ZipInputStream in = null;
            BufferedReader br = null;
            List<OmAccounts> lists = new ArrayList<>();
            if (billFile.isFile() && billFile.exists()) {
                try {
                    in = new ZipInputStream(new FileInputStream(billFile));
                    br = new BufferedReader(new InputStreamReader(in, "GBK"));
                    //不解压直接读取,加上gbk解决乱码问题
                    ZipEntry zipFile;
                    //循环读取zip中的cvs文件，无法使用jdk自带，因为文件名中有中文
                    while ((zipFile = in.getNextEntry()) != null) {
                        if (zipFile.isDirectory()) {
                            //如果是目录，不处理
                            return new ResponseData(false, "解压为目录");
                        }
                        //获得cvs名字
                        String fileName = zipFile.getName();
                        //检测文件是否存在
                        if (fileName != null && fileName.indexOf(".") != -1) {
                            String line;
                            if (fileName.endsWith("账务明细.csv")) {
                                while ((line = br.readLine()) != null) {
                                    lineList.add(line);
                                }
                            }
                        } else {
                            return new ResponseData(false, "文件不存在");
                        }
                    }
                    //解析账单
                    SimpleDateFormat sdy = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    for (int i = 5; i < lineList.size() - 4; i++) {
                        String[] row = lineList.get(i).split(",");
                        OmAccounts omAccounts = new OmAccounts();
                        omAccounts.set__status("add");
                        //第三方交易号
                        String transactionId = row[1].trim();
                        //财务交易号
                        String serialNum = row[0].trim();
                        //商户拼接号
                        String outTradeNumber = row[2].trim();
                        //商品名称
                        String product = row[3].trim();
                        //发生时间
                        String time = row[4].trim();
                        //账号
                        String account = row[5].trim();
                        //金额
                        String amountStrFromAli = row[6].trim();
                        //渠道
                        String channel = "ALIPAY";
                        //业务类型
                        String type = "1";
                        //备注
                        String remark = row[11].trim();
                        //原始类型
                        String preType = row[10].trim();
                        if ("在线支付".equals(preType)) {
                            BigDecimal amountBigDecimalFromAli = new BigDecimal(amountStrFromAli);
                            omAccounts.setTransaction(transactionId);
                            omAccounts.setSerialNum(serialNum);
                            omAccounts.setOutTradeNo(outTradeNumber);
                            omAccounts.setProduct(product);
                            omAccounts.setTime(sdy.parse(time));
                            omAccounts.setAccount(account);
                            omAccounts.setAmount(amountBigDecimalFromAli.abs());
                            omAccounts.setType(type);
                            omAccounts.setRemark(remark);
                            omAccounts.setChannel(channel);
                            omAccounts.setPreType(preType);
                            lists.add(omAccounts);
                        } else if ("交易退款".equals(preType)) {
                            type = "2";
                            amountStrFromAli = row[7].trim();
                            BigDecimal amountBigDecimalFromAli = new BigDecimal(amountStrFromAli);
                            omAccounts.setTransaction(transactionId);
                            omAccounts.setSerialNum(serialNum);
                            omAccounts.setOutTradeNo(outTradeNumber);
                            omAccounts.setProduct(product);
                            omAccounts.setTime(sdy.parse(time));
                            omAccounts.setAccount(account);
                            omAccounts.setAmount(amountBigDecimalFromAli.abs());
                            omAccounts.setType(type);
                            omAccounts.setRemark(remark);
                            omAccounts.setChannel(channel);
                            omAccounts.setPreType(preType);
                            lists.add(omAccounts);
                        } else if ("收费".equals(preType)) {
                            type = "3";
                            amountStrFromAli = row[7].trim();
                            BigDecimal amountBigDecimalFromAli = new BigDecimal(amountStrFromAli);
                            omAccounts.setTransaction(transactionId);
                            omAccounts.setSerialNum(serialNum);
                            omAccounts.setOutTradeNo(outTradeNumber);
                            omAccounts.setProduct(product);
                            omAccounts.setTime(sdy.parse(time));
                            omAccounts.setAccount(account);
                            omAccounts.setAmount(amountBigDecimalFromAli.abs());
                            omAccounts.setType(type);
                            omAccounts.setRemark(remark);
                            omAccounts.setChannel(channel);
                            omAccounts.setPreType(preType);
                            lists.add(omAccounts);
                        } else if ("提现".equals(preType)) {
                            type = "5";
                            amountStrFromAli = row[7].trim();
                            BigDecimal amountBigDecimalFromAli = new BigDecimal(amountStrFromAli);
                            omAccounts.setTransaction(transactionId);
                            omAccounts.setSerialNum(serialNum);
                            omAccounts.setOutTradeNo(outTradeNumber);
                            omAccounts.setProduct(product);
                            omAccounts.setTime(sdy.parse(time));
                            omAccounts.setAccount(account);
                            omAccounts.setAmount(amountBigDecimalFromAli.abs());
                            omAccounts.setType(type);
                            omAccounts.setRemark(remark);
                            omAccounts.setChannel(channel);
                            omAccounts.setPreType(preType);
                            lists.add(omAccounts);
                        } else {//原始业务类型为其他-type为"7" add by zhangyanan 2017-11-10
                            type = "7";
                            BigDecimal amountBigDecimalFromAli = new BigDecimal(amountStrFromAli);
                            omAccounts.setTransaction(transactionId);
                            omAccounts.setSerialNum(serialNum);
                            omAccounts.setOutTradeNo(outTradeNumber);
                            omAccounts.setProduct(product);
                            omAccounts.setTime(sdy.parse(time));
                            omAccounts.setAccount(account);
                            omAccounts.setAmount(amountBigDecimalFromAli.abs());
                            omAccounts.setType(type);
                            omAccounts.setRemark(remark);
                            omAccounts.setChannel(channel);
                            omAccounts.setPreType(preType);
                            lists.add(omAccounts);
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return new ResponseData(false, "文件读取失败");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return new ResponseData(false, "编码方式有误");
                } catch (IOException e) {
                    e.printStackTrace();
                    return new ResponseData(false, "IO流异常");
                } catch (ParseException e) {
                    e.printStackTrace();
                    return new ResponseData(false, "交易日期转换失败");
                } finally {
                    //关闭流
                    try {
                        br.close();
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                return new ResponseData(false, "未找到文件");
            }
            //批量新增
            batchUpdate(iRequest, lists);
            OmAccountsRecord omAccountsRecord = new OmAccountsRecord();
            omAccountsRecord.setChannel("ALIPAY");
            omAccountsRecord.setRecordDate(billDate);
            omAccountsRecordMapper.insertSelective(omAccountsRecord);
            return new ResponseData(true);
        } else if ("true".equalsIgnoreCase(success)
                && "noBill".equalsIgnoreCase(msgCode)) {
            return new ResponseData(true, msg);
        } else {
            //下载失败
            return new ResponseData(false, msg);
        }
    }

    /**
     * 微信账单解析
     *
     * @return 返回信息
     */
    @Override
    public ResponseData WechatBillAnalysisService(Date billDate) {
        IRequest iRequest = RequestHelper.newEmptyRequest();
        iRequest.setRoleId(Constants.JOB_DEFAULT_USERID);
        iRequest.setLocale(Constants.JOB_DEFAULT_LANG);

        HashMap<String, String> contentType = new HashMap<>();
        contentType.put("Content-Type", "application/json");

        //请求微信账单下载接口
        Map<String, String> requestHpayMap = new HashMap<>();
        requestHpayMap.put("mode", "WECHAT");
        requestHpayMap.put("billType", "ALL");
        requestHpayMap.put("billDate", new SimpleDateFormat("yyyyMMdd").format(billDate));

        String wechatBillDownloadData = JSON.toJSONString(requestHpayMap);

        Response wechatBillDownloadResponse;
        JSONObject wechatBillDownloadResult;

        try {
            wechatBillDownloadResponse = restClient.postString(Constants.HPAY, "/hpay/v1/billDownload", wechatBillDownloadData, "json", null, contentType);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseData(false, "请求HPAY失败");
        }
        String success;
        String msg;
        String msgCode;
        try {
            wechatBillDownloadResult = JSONObject.fromObject(wechatBillDownloadResponse.body().string());
            success = wechatBillDownloadResult.getString("success");
            msg = wechatBillDownloadResult.getString("msg");
            msgCode = wechatBillDownloadResult.getString("msgCode");
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseData(false, "获得HPAY信息失败");
        }
        //下载成功
        if ("true".equalsIgnoreCase(success)
                && !"noBill".equalsIgnoreCase(msgCode)) {
            //读取账单文件
            InputStreamReader read = null;
            ArrayList<String> lineList = new ArrayList<>();
            File billFile = new File(billDirectoryPath + "wechatBill" + File.separator + "wechatBill.txt");
            if (billFile.isFile() && billFile.exists()) {
                try {
                    read = new InputStreamReader(new FileInputStream(billFile), "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(read);
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        lineList.add(line);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return new ResponseData(false, "编码方式有误");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return new ResponseData(false, "文件读取失败");
                } catch (IOException e) {
                    e.printStackTrace();
                    return new ResponseData(false, "IO流异常");
                } finally {
                    try {
                        if (read != null) {
                            read.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                return new ResponseData(false, "未找到文件");
            }
            //解析账单
            SimpleDateFormat sdy = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            List<OmAccounts> list = new ArrayList<>();
            try {
                for (int i = 1; i < lineList.size() - 2; i++) {
                    String[] row = lineList.get(i).split(",");
                    //第三方交易号
                    String transactionId = row[5].substring(1).trim();
                    //财务流水号
                    String serialNum = row[5].substring(1).trim();
                    //商户拼接号
                    String outTradeNumber = row[6].substring(1).trim();
                    //商品名称
                    String product = row[20].substring(1).trim();
                    //发生时间
                    String time = row[0].substring(1).trim();
                    //账号
                    String account = row[7].substring(1).trim();
                    //交易金额
                    String tradeAmountStr = row[12].substring(1).trim();
                    BigDecimal tradeAmount = new BigDecimal(tradeAmountStr);
                    //手续费
                    String poundageStr = row[22].substring(1).trim();
                    BigDecimal poundage = new BigDecimal(poundageStr);
                    //默认手续费 用于比对 默认为0
                    BigDecimal defaultPoundage = new BigDecimal("0.00000");
                    String channel = "WECHAT";
                    //业务类型
                    String preType = row[8].substring(1).trim();
                    //交易状态
                    String tradeStatus = row[9].substring(1).trim();
                    //业务类型
                    String type = "";
                    OmAccounts omAccounts = new OmAccounts();
                    omAccounts.set__status("add");
                    BigDecimal amount = new BigDecimal("0");
                    //交易状态(SUCCESS:普通交易收款   REFUND:退款)
                    if ("SUCCESS".equalsIgnoreCase(tradeStatus)) {
                        //判断交易服务费
                        if (poundage.compareTo(defaultPoundage) == 1) {
                            OmAccounts poundageOmAccounts = new OmAccounts();
                            poundageOmAccounts.set__status("add");
                            //交易服务费
                            type = "3";
                            poundageOmAccounts.setTransaction(transactionId);
                            poundageOmAccounts.setSerialNum(serialNum);
                            poundageOmAccounts.setOutTradeNo(outTradeNumber);
                            poundageOmAccounts.setProduct(product);
                            poundageOmAccounts.setTime(sdy.parse(time));
                            poundageOmAccounts.setAccount(account);
                            poundageOmAccounts.setAmount(poundage.abs());
                            poundageOmAccounts.setChannel(channel);
                            poundageOmAccounts.setType(type);
                            poundageOmAccounts.setRemark(product);
                            poundageOmAccounts.setPreType(type);
                            list.add(poundageOmAccounts);
                        }
                        //交易收款
                        type = "1";
                        amount = tradeAmount.abs();
                        //交易类型:退款
                    } else if ("REFUND".equalsIgnoreCase(tradeStatus)) {
                        //退款状态
                        String refundStatus = row[19].substring(1).trim();
                        //退款金额
                        String strAmount = row[16].substring(1).trim();
                        BigDecimal refundAmount = new BigDecimal(strAmount);
                        //退款状态"SUCCESS" type=2(退款)
                        if ("SUCCESS".equalsIgnoreCase(refundStatus)) {
                            //判断退款服务费
                            if (poundage.compareTo(defaultPoundage) == -1) {
                                //退款服务费
                                type = "4";
                                OmAccounts refundPoundAge = new OmAccounts();
                                refundPoundAge.set__status("add");
                                refundPoundAge.setTransaction(transactionId);
                                refundPoundAge.setSerialNum(serialNum);
                                refundPoundAge.setOutTradeNo(outTradeNumber);
                                refundPoundAge.setProduct(product);
                                refundPoundAge.setTime(sdy.parse(time));
                                refundPoundAge.setAccount(account);
                                refundPoundAge.setAmount(poundage.abs());
                                refundPoundAge.setChannel(channel);
                                refundPoundAge.setType(type);
                                refundPoundAge.setRemark(product);
                                refundPoundAge.setPreType(type);
                                list.add(refundPoundAge);
                            }
                            //普通退款
                            type = "2";
                            amount = refundAmount.abs();
                        }
                    } else {//业务类型为其他-type为"7" modify by zhangyanan 2017-11-10
                        type = "7";
                    }
                    omAccounts.setTransaction(transactionId);
                    omAccounts.setSerialNum(serialNum);
                    omAccounts.setOutTradeNo(outTradeNumber);
                    omAccounts.setProduct(product);
                    omAccounts.setTime(sdy.parse(time));
                    omAccounts.setAccount(account);
                    omAccounts.setAmount(amount.abs());
                    omAccounts.setChannel(channel);
                    omAccounts.setType(type);
                    omAccounts.setRemark(product);
                    omAccounts.setPreType(type);
                    list.add(omAccounts);

                }
            } catch (ParseException e) {
                e.printStackTrace();
                return new ResponseData(false, "交易日期转换失败");
            }
            //批量新增
            batchUpdate(iRequest, list);
            OmAccountsRecord omAccountsRecord = new OmAccountsRecord();
            omAccountsRecord.setChannel("WECHAT");
            omAccountsRecord.setRecordDate(billDate);
            omAccountsRecordMapper.insertSelective(omAccountsRecord);
            return new ResponseData(true);
        } else if ("true".equalsIgnoreCase(success)
                && "noBill".equalsIgnoreCase(msgCode)) {
            return new ResponseData(true, msg);
        } else {
            return new ResponseData(false, msg);
        }
    }

    /**
     * 银联账单解析
     * @param billDate
     * @return
     */
    @Override
    public ResponseData UnionBillAnalysisService(Date billDate) {
        IRequest iRequest = RequestHelper.newEmptyRequest();
        iRequest.setRoleId(Constants.JOB_DEFAULT_USERID);
        iRequest.setLocale(Constants.JOB_DEFAULT_LANG);

        HashMap<String, String> contentType = new HashMap<>();
        contentType.put("Content-Type", "application/json");

        //请求银联账单下载接口
        Map<String, String> requestHpayMap = new HashMap<>();
        requestHpayMap.put("mode", "UNIONPAY");
        requestHpayMap.put("billType", "ALL");
        requestHpayMap.put("billDate", new SimpleDateFormat("yyyyMMdd").format(billDate));

        String unionBillDownloadData = JSON.toJSONString(requestHpayMap);

        Response unionBillDownloadResponse;
        JSONObject unionBillDownloadResult;
        JSONArray respArray;
        JSONObject respObject;
        try {
            unionBillDownloadResponse = restClient.postString(Constants.HPAY, "/hpay/v1/billDownload", unionBillDownloadData, "json", null, contentType);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseData(false, "请求HPAY失败");
        }
        String success;
        String msg;
        String msgCode;
        try {
            unionBillDownloadResult = JSONObject.fromObject(unionBillDownloadResponse.body().string());
            success = unionBillDownloadResult.getString("success");
            msg = unionBillDownloadResult.getString("msg");
            msgCode = unionBillDownloadResult.getString("msgCode");
            respArray = JSONArray.fromObject(unionBillDownloadResult.get("resp"));
            respObject = JSONObject.fromObject(respArray.get(0));
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseData(false, "获得HPAY信息失败");
        }
        //下载成功
        if ("true".equalsIgnoreCase(success)
                && !"noBill".equalsIgnoreCase(msgCode)) {
//        if ("true".equalsIgnoreCase("true")) {
            //读取账单文件
            String zipFilePath = respObject.getString("url");
            String outPutDirectory = billDirectoryPath + "unionBill" + File.separator;
//            String zipFilePath = "D:\\bank_bills\\unionBill\\802120054110561_20170928.zip";
//            String outPutDirectory = billDirectoryPath + "unionBill" + File.separator;
//            String outPutDirectory = "D:\\bank_bills" + File.separator + "unionBill" + File.separator;
            //对落地的zip文件解压缩并解析
            List<String> fileList = SDKUtil.unzip(zipFilePath, outPutDirectory);
            List<Map> ZmDataList;
            //解析ZM，ZME文件
            for (String file : fileList) {
                if (file.indexOf("ZM_") != -1) {
                    ZmDataList = SDKUtil.parseZMFile(file);
                    List<OmAccounts> list = new ArrayList<>();
                    if (ZmDataList.size() > 0) {
                        //结算金额
                        BigDecimal settlementAmount = new BigDecimal("0");
                        //商户代码
                        String merchantCode = "";
                        for (int i = 0; i < ZmDataList.size(); i++) {
                            Map<Integer, String> dataMapTmp = ZmDataList.get(i);
                            OmAccounts omAccounts = new OmAccounts();
                            OmAccounts omAccountsCharge = new OmAccounts();
                            SimpleDateFormat sdy = new SimpleDateFormat("yyyyMMddHHmmss");
                            //第三方交易号(付款)
                            String transaction = dataMapTmp.get(9).trim();
                            //账务流水号
                            String serialNum = dataMapTmp.get(9).trim();
                            //商户拼接号
                            String outTradeNo = dataMapTmp.get(11).trim();
                            //产品名称
                            String product = "ZEST";
                            //发生时间
                            String timeStr = dataMapTmp.get(4).trim();
                            //默认为当前时间
                            Date time;
                            try {
                                //将日期转换换位标准格式
                                DateFormat dateFormat = new SimpleDateFormat("MMddHHmmss");
                                //获得当前时间（下载对账单时间）
                                Date billDownLoadDate = new Date();
                                //格式化当前时间
                                billDownLoadDate = dateFormat.parse(dateFormat.format(billDownLoadDate));
                                //格式化银联对账单对应的交易传输时间
                                Date timeFromUnion = dateFormat.parse(timeStr);
                                //获得当前年份
                                Calendar calendar = new GregorianCalendar();
                                String year = String.valueOf(calendar.get(Calendar.YEAR));
                                //当交易时间大于当前时间则代表跨年下载
                                if (timeFromUnion.getTime() > billDownLoadDate.getTime()) {
                                    //将年份+1,为支付年份的第二年
                                    Integer yearInt = calendar.get(Calendar.YEAR) + 1;
                                    year = String.valueOf(yearInt);
                                }
                                //将日期转换为年月日的格式
                                timeStr = year + timeStr;
                                time = sdy.parse(timeStr);
                            } catch (ParseException e) {
                                e.printStackTrace();
                                return new ResponseData(false, "交易日期转换失败");
                            }
                            merchantCode = dataMapTmp.get(27).trim();
                            //账户
                            String account = dataMapTmp.get(5).trim();
                            //交易金额
                            String amountStr = dataMapTmp.get(6).trim();
                            BigDecimal amount = new BigDecimal(amountStr);
                            amount = amount.divide(new BigDecimal("100")).setScale(2);

                            //获得手续费金额字符串 银联以D/C开头，去掉D/C之后则为手续费
                            String chargeStr = dataMapTmp.get(15).substring(1).trim();
                            BigDecimal charge = new BigDecimal(chargeStr);
                            charge = charge.divide(new BigDecimal("100")).setScale(2);
                            /**
                             //结算列
                             String settlementStr = dataMapTmp.get(16).trim();
                             //C表示贷记
                             if (settlementStr.startsWith("C")) {
                             settlementAmount = settlementAmount.add(new BigDecimal(settlementStr.substring(1)));
                             } else {//D表示借记 settlementStr.startsWith("D")
                             settlementAmount = settlementAmount.add(new BigDecimal(settlementStr.substring(1)).multiply(new BigDecimal(-1)));
                             }
                             **/
                            //渠道
                            String channel = "UNIONPAY";
                            //业务类型
                            String type;
                            String chargeType;
                            //备注
                            String remark = "ZEST";
                            //原始类型
                            String preType = dataMapTmp.get(19).trim();
                            if ("01".equals(preType)) {
                                //如果业务类型为“01：消费”，将“n12”存储在AMOUNT中，存储为“1-交易”。
                                //如果业务类型为“01：消费”，将“X+n12”存储在AMOUNT中，存储为“3-服务费”。
                                type = "1";
                                chargeType = "3";
                                settlementAmount = settlementAmount.add(new BigDecimal(amountStr));
                                settlementAmount = settlementAmount.subtract(new BigDecimal(chargeStr));
                            } else if ("04".equals(preType)) {
                                //如果业务类型为“04：退货”，将“n12”存储在AMOUNT中，存储为“2-退款”。
                                //如果业务类型为“04：退货”，将“X+n12”存储在AMOUNT中，存储为“4-退款服务费”。
                                type = "2";
                                chargeType = "4";
                                outTradeNo = dataMapTmp.get(40).trim();
                                //第三方交易号(退款)
                                transaction = dataMapTmp.get(26).trim();
                                settlementAmount = settlementAmount.subtract(new BigDecimal(amountStr));
                            } else {
                                //原始业务类型为其他-type为"7" modify by zhangyanan 2017-11-10
                                type = "7";
                                chargeType = "7";
                            }
                            omAccounts.set__status("add");
                            omAccounts.setTransaction(transaction);
                            omAccounts.setSerialNum(serialNum);
                            omAccounts.setOutTradeNo(outTradeNo);
                            omAccounts.setProduct(product);
                            omAccounts.setTime(time);
                            omAccounts.setAccount(account);
                            omAccounts.setAmount(amount);
                            omAccounts.setChannel(channel);
                            omAccounts.setType(type);
                            omAccounts.setRemark(remark);
                            omAccounts.setPreType(preType);

                            omAccountsCharge.set__status("add");
                            omAccountsCharge.setTransaction(transaction);
                            omAccountsCharge.setSerialNum(serialNum);
                            omAccountsCharge.setOutTradeNo(outTradeNo);
                            omAccountsCharge.setProduct(product);
                            omAccountsCharge.setTime(time);
                            omAccountsCharge.setAccount(account);
                            omAccountsCharge.setAmount(charge);
                            omAccountsCharge.setChannel(channel);
                            omAccountsCharge.setType(chargeType);
                            omAccountsCharge.setRemark(remark);
                            omAccountsCharge.setPreType(preType);
                            list.add(omAccounts);
                            list.add(omAccountsCharge);
                        }
                        //结算金额虚拟一条记录添加数据库
                        OmAccounts settlementOmAccounts = new OmAccounts();
                        Calendar calendar = new GregorianCalendar();
                        calendar.setTime(billDate);
                        calendar.add(Calendar.DATE, 1);
                        settlementOmAccounts.setTime(calendar.getTime());
                        //YL+商户代码8位+年月日
                        String transactionNum = "YL" + merchantCode.substring(merchantCode.length() - 8) + new SimpleDateFormat("yyyyMMdd").format(calendar.getTime());
                        settlementOmAccounts.setTransaction(transactionNum);
                        settlementOmAccounts.setSerialNum(transactionNum);
                        settlementOmAccounts.setOutTradeNo("");
                        settlementOmAccounts.setProduct("ZEST");
                        settlementOmAccounts.setAccount("");
                        settlementOmAccounts.setAmount((settlementAmount.divide(new BigDecimal("100"))));
                        settlementOmAccounts.setChannel("UNIONPAY");
                        //将每日清算金额数据type设置为"6" modify by zhangyanan 2017-11-10 begin
                        settlementOmAccounts.setType("6");
                        settlementOmAccounts.setPreType("6");
                        //将每日清算金额数据type设置为"6" modify by zhangyanan 2017-11-10 end
                        settlementOmAccounts.setRemark("ZEST");
                        settlementOmAccounts.set__status("add");
                        list.add(settlementOmAccounts);
                        //批量新增
                        batchUpdate(iRequest, list);
                        OmAccountsRecord omAccountsRecord = new OmAccountsRecord();
                        omAccountsRecord.setChannel("UNIONPAY");
                        omAccountsRecord.setRecordDate(billDate);
                        omAccountsRecordMapper.insertSelective(omAccountsRecord);
                        return new ResponseData(true);
                    } else {
                        return new ResponseData(false, "账单无内容");
                    }
                }
            }
        } else if ("true".equalsIgnoreCase(success)
                && "noBill".equalsIgnoreCase(msgCode)) {
            return new ResponseData(true, msg);
        } else {
            return new ResponseData(false, msg);
        }
        return null;
    }

    /**
     * 组织将要发送到Retail的账单数据并返回
     *
     * @return
     */
    @Override
    public Map<String, Object> organizationBillDataToRetail() {
        //银联账单清算求和
        unionLiquidationSum();
        //获得未发送的账单记录
        OmAccounts omAccounts = new OmAccounts();
        omAccounts.setSyncFlag("N");
        List<OmAccounts> omAccountsList = omAccountsMapper.getBillListBySyncFlag(omAccounts);
        Map<String, Object> map = new HashMap<>();
        if (null != omAccountsList && !omAccountsList.isEmpty()) {
            //财务类型符合并且需要将发送标志位更新为"Y"的数据
            List<OmAccounts> newOmAccountsList = new ArrayList<>();
            BillRequestItem billRequestItem = new BillRequestItem();
            for (OmAccounts account : omAccountsList) {
                //账务类型
                if ("1".equals(account.getType()) || "2".equals(account.getType()) || "5".equals(account.getType())) {
                    newOmAccountsList.add(account);
                    BillRequestItem.BillEntryItem billEntryItem = new BillRequestItem.BillEntryItem();
                    //第三方业务单据号
                    billEntryItem.setZshddh(account.getTransaction().substring(account.getTransaction().length() - 18, account.getTransaction().length()));
                    //HMALL单据号
                    billEntryItem.setZckh(String.valueOf(account.getAccountsId()));
                    String financialType = "";
                    switch (account.getType()) {
                        case "1":
                            financialType = "收款";
                            break;
                        case "2":
                            financialType = "退款";
                            break;
                        case "5":
                            financialType = "提现";
                            break;
                    }
                    billEntryItem.setZywlx(financialType);
                    //单据类型（目前预留）
                    billEntryItem.setZy01("");
                    //预留
                    billEntryItem.setZy02("");
                    //公司代码
                    billEntryItem.setBukrs("0201");
                    /**
                     * 用户代码
                     *
                     * 微信和银联所有记录都传输 9520
                     * 支付宝记录
                     * 1.通过TRANSACTION字段寻找HMALL_OM_PAYMENTINFO表中的NUMBER_CODE字段。
                     * 2.找到对应记录，根据关联订单id找到订单
                     * 根据订单头号[HMALL_MST_ORDER.ORDER_ID]，找到订单表中的字段WEBSITE_ID,根据WEBSITE_ID的值在网站表【HMALL_MST_WEBSITE】中取sold_party的值
                     * 3.找不到对应记录即传9519
                     */
                    String zkunnr = "";
                    //支付宝、微信、银联：取账务流水号
                    String serialNum = account.getSerialNum();
                    //支付方式
                    String payChannel = "";
                    //备注 支付宝：备注；微信：商品名称；银联：
                    String remark = "";
                    if ("ALIPAY".equals(account.getChannel())) {
                        payChannel = "支付宝";
                        remark = account.getRemark();
                        String soldParty = omAccountsMapper.getSoldPartyByTransaction(account.getTransaction());
                        if (StringUtils.isNotEmpty(soldParty)) {
                            zkunnr = soldParty;
                        } else {
                            //目前天猫未启用,所有没找到售达方的传9520
//                            zkunnr = "9519";
                            zkunnr = "9520";
                        }
                    } else if ("WECHAT".equals(account.getChannel())) {
                        payChannel = "微信";
                        zkunnr = "9520";
                        remark = account.getProduct();
                    } else {
                        payChannel = "银联";
                        zkunnr = "9520";
                    }
                    //支付方式
                    billEntryItem.setZzflx(payChannel);
                    //用户代码  如果是提现业务传空，否则传用户代码
                    if ("5".equals(account.getType())) {
                        zkunnr = "";
                    }
                    billEntryItem.setZkunnr(zkunnr);
                    //账单流水号
                    billEntryItem.setZy08(serialNum);
                    //备注
                    billEntryItem.setZy07(remark);

                    /**
                     * 商城订单号
                     *
                     * 1.通过TRANSACTION字段寻找HMALL_OM_PAYMENTINFO表中的NUMBER_CODE字段。
                     * 2.找到对应记录。
                     * 3.将关联的订单的对应字段赋值。
                     * 4.找不到对应记录即传OUT_TRADE_NO
                     */
                    String code = "";
                    String escOrderCode = omAccountsMapper.getEscOrderCodeBytransaction(account.getTransaction());
                    if (StringUtils.isNotEmpty(escOrderCode)) {
                        code = escOrderCode;
                    } else {
                        if (StringUtils.isEmpty(account.getOutTradeNo())) {//此条账单为清算信息,商城拼接号传""
                            code = "";
                        } else if (account.getOutTradeNo().startsWith("SV") || account.getOutTradeNo().startsWith("RFE")) {//如果是服务销售单或者是退款单号不做处理
                            code = account.getOutTradeNo();
                        } else if (account.getOutTradeNo().length() < 18) {//测试数据,单独做处理
                            code = account.getOutTradeNo();
                        } else {
                            code = account.getOutTradeNo().substring(account.getOutTradeNo().length() - 18, account.getOutTradeNo().length());
                        }
                    }
                    billEntryItem.setExord(code);
                    //预留
                    billEntryItem.setZmatnr("");
                    //发生时间yyyy-MM-dd HH:mm:ss
                    Date time = account.getTime();
                    //交易日期 年月日
                    billEntryItem.setZjyrq(new DateTime(time.getTime()).toString("yyyy-MM-dd"));
                    //交易时间 时分秒
                    billEntryItem.setZjysj(new DateTime(time.getTime()).toString("HH:mm:ss"));
                    //记账金额
                    billEntryItem.setZzje(account.getAmount().toString());
                    //预留
                    billEntryItem.setZjyms("");
                    //商城拼接号
                    billEntryItem.setZy06(account.getOutTradeNo());
                    //预留
                    billEntryItem.setZy09("");
                    //金额预留
                    billEntryItem.setZy03("");
                    //日期预留
                    billEntryItem.setZy04("");
                    //时间预留
                    billEntryItem.setZy05("");
                    //来源标识
                    String SourceIdentifier = "";
                    if ("Y".equals(account.getSource())) {
                        SourceIdentifier = "手工上载";
                    } else {
                        SourceIdentifier = "第三方接口";
                    }
                    billEntryItem.setZy10(SourceIdentifier);
                    //预留
                    billEntryItem.setZy11("");
                    //预留
                    billEntryItem.setZy12("");
                    billRequestItem.getItems().add(billEntryItem);
                }
                BillRequestBody billRequestBody = new BillRequestBody();
                billRequestBody.setBillRequestItem(billRequestItem);
                map.put("requestBody", billRequestBody);
                map.put("omAccountsList", newOmAccountsList);
            }
        }
        return map;
    }

    /**
     * 组织将要发送到Retail账单的对账数据并返回
     *
     * @return
     */
    @Override
    public Map<String, Object> checkBillDataToRetail() {
        //获得将要对账的记录
        OmAccounts omAccounts = new OmAccounts();
        omAccounts.setSyncFlag("Y");
        List<OmAccounts> omAccountsList = omAccountsMapper.getBillListBySyncFlag(omAccounts);
        Map<String, Object> map = new HashMap<>();
        if (null != omAccountsList && !omAccountsList.isEmpty()) {
            List<OmAccounts> newOmAccountList = new ArrayList<>();
            List<CheckBillRequestItem> checkBillRequestItemList = new CheckBillRequestBody().getCheckBillRequestItemList();
            for (OmAccounts omAccount : omAccountsList) {
                //只对收款和退款做为对账推送数据
                if ("1".equals(omAccount.getType()) || "2".equals(omAccount.getType())) {
                    CheckBillRequestItem checkBillRequestItem = new CheckBillRequestItem();
                    checkBillRequestItem.setZshddh(omAccount.getTransaction().substring(omAccount.getTransaction().length() - 18, omAccount.getTransaction().length()));
                    checkBillRequestItem.setZckh(String.valueOf(omAccount.getAccountsId()));
                    //账务类型
                    String financialType = "";
                    switch (omAccount.getType()) {
                        case "1":
                            financialType = "收款";
                            break;
                        case "2":
                            financialType = "退款";
                            break;
                    }
                    checkBillRequestItem.setZywlx(financialType);
                    //支付方式
                    String payChannel = "";
                    if ("ALIPAY".equals(omAccount.getChannel())) {
                        payChannel = "支付宝";
                    } else if ("WECHAT".equals(omAccount.getChannel())) {
                        payChannel = "微信";
                    } else {
                        payChannel = "银联";
                    }
                    checkBillRequestItem.setZzflx(payChannel);
                    checkBillRequestItemList.add(checkBillRequestItem);
                    newOmAccountList.add(omAccount);
                }
            }
            CheckBillRequestBody requestBody = new CheckBillRequestBody();
            requestBody.setCheckBillRequestItemList(checkBillRequestItemList);
            map.put("requestBody", requestBody);
        }
        return map;
    }

    /**
     * 财务上载界面列表查询
     * @param request
     * @param dto
     * @param page
     * @param pagesize
     * @return
     */
    @Override
    public List<OmAccounts> queryAccountsList(IRequest request, OmAccounts dto, int page, int pagesize) {
        PageHelper.startPage(page, pagesize);
        if (dto != null && dto.getTradeDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr = null;
            try {
                dateStr = sdf.format(sdf.parse(dto.getTradeDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            dto.setTradeDate(dateStr);
        }

        List<OmAccounts> result = omAccountsMapper.queryAccountsList(dto);
        if (CollectionUtils.isEmpty(result)) {
            return new ArrayList<OmAccounts>();
        }
        result.stream().map(new Function<OmAccounts, OmAccounts>() {
            @Override
            public OmAccounts apply(OmAccounts omAccounts) {
                if (omAccounts == null)
                    return null;
                if ((WECHAT_PAYMETHOD).equalsIgnoreCase(omAccounts.getChannel()) || (UNIONPAY_PAYMETHOD).equalsIgnoreCase(omAccounts.getChannel())) {
                    omAccounts.setUserNumber("9520");
                } else if (ALIPAY_PAYMETHOD.equalsIgnoreCase(omAccounts.getChannel())) {
                    if (StringUtils.isNotBlank(omAccounts.getTransaction())) {
                        String userNumber = omAccountsMapper.getUserNumber(omAccounts.getAccountsId());
                        if (StringUtils.isNotEmpty(userNumber))
                            omAccounts.setUserNumber(userNumber);
                        else
                            omAccounts.setUserNumber("9519");
                    }
                } else {
                    omAccounts.setUserNumber("9520");
                }
                omAccounts.setCompanyCode("0201");
                return omAccounts;
            }
        }).collect(Collectors.toList());
        return result;
    }

    /**
     * excel数据导入
     *
     * @param requestContext
     * @param excelList
     */
    @Override
    @Transactional(rollbackFor = Exception.class)//保存 异常时回滚
    public void insertAllValue(IRequest requestContext, ArrayList<List<String>> excelList) throws Exception {
        ExcelUtil excelUtil = new ExcelUtil();
        FormatUtils formatUtils = new FormatUtils();
        StringBuffer errMsg = new StringBuffer("");

        int n;
        List<String> data;
        BigDecimal bAmount;
        List<com.alibaba.fastjson.JSONObject> successAccounts = new ArrayList<>();
        for (int i = 0; i < excelList.size(); i++) {
            String __status = "add";
            n = i + 2;
            data = excelList.get(i);
            //校验excel中哪个字段不能为空
//            if (excelUtil.chekckNullCol(data)) {
//                continue;
//            }
            /*获取数据*/
            String time = (data.get(0)).trim();         //记账时间 time
            String transaction = (data.get(1)).trim();      //微信支付业务单号 transaction
            String serialNum = (data.get(2)).trim();       //资金流水单号  serialNum
            String type = (data.get(3)).trim();         //业务名称  type
            String preType = (data.get(4)).trim();      //业务类型  preType
            String amount = (data.get(6)).trim();       //收支金额  amount
            String remark = (data.get(9)).trim();       //备注 remark
            String voucherno = (data.get(10)).trim();    //业务凭证号  voucherno
            OmAccounts omAccounts = new OmAccounts();

            if (StringUtils.isNotEmpty(time)) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = formatter.parse(time);
                omAccounts.setTime(date);
            } else {
                errMsg.append("第" + n + "行，记账时间为空;");
            }

            if (StringUtils.isNotEmpty(transaction)) {
                String s1 = transaction.substring(0, 1);
                if ("'".equals(transaction.substring(0, 1)) || "‘".equals(transaction.substring(0, 1))) {
                    omAccounts.setTransaction(transaction.substring(1));
                } else {
                    omAccounts.setTransaction(transaction);
                }
            } else {
                errMsg.append("第" + n + "行，微信支付业务单号为空;");
            }

            if (serialNum != null && !"".equals(serialNum)) {
                if ("'".equals(serialNum.substring(0, 1)) || "‘".equals(serialNum.substring(0, 1))) {
                    omAccounts.setSerialNum(serialNum.substring(1));
                } else {
                    omAccounts.setSerialNum(serialNum);
                }

//                if (serialNum.length() >= 18) {
//                    omAccounts.setTransaction(serialNum.substring(serialNum.length() - 18));
//                } else {
//                    omAccounts.setTransaction(serialNum);
//                }
            } else {
                errMsg.append("第" + n + "行，资金流水单号为空;");
            }

            if (type != null && !"".equals(type)) {
                String typeReal = "";
                if ("交易".equals(type)) {
                    typeReal = "1";
                } else if ("退款".equals(type)) {
                    typeReal = "2";
                } else if ("服务费".equals(type)) {
                    typeReal = "3";
                } else if ("服务费退款".equals(type)) {
                    typeReal = "4";
                } else if ("提现".equals(type)) {
                    typeReal = "5";
                } else if ("其他".equals(type)) {
                    typeReal = "6";
                }
                omAccounts.setType(typeReal);
            } else {
                errMsg.append("第" + n + "行，业务名称为空;");
            }

            if (preType != null && !"".equals(preType)) {
                omAccounts.setPreType(preType);
            } else {
                errMsg.append("第" + n + "行，业务类型为空;");
            }

            if (amount != null && !"".equals(amount)) {
                //价格转换,如果为负的，则保存值
                if (formatUtils.isNum(amount)) {
                    bAmount = BigDecimal.valueOf(Double.parseDouble(amount));
                    if (bAmount.compareTo(new BigDecimal("0")) == -1) {
                        omAccounts.setAmount(bAmount.abs());
                    } else {
                        omAccounts.setAmount(bAmount);
                    }

                } else {
                    errMsg.append("第" + n + "行，收支金额格式不对;");
                }

            } else {
                errMsg.append("第" + n + "行，收支金额为空;");
            }


            if (voucherno != null && !"".equals(voucherno)) {
                if ("'".equals(voucherno.substring(0, 1)) || "‘".equals(voucherno.substring(0, 1))) {
                    omAccounts.setVoucherno(voucherno.substring(1));
                } else {
                    omAccounts.setVoucherno(voucherno);
                }
            }
            omAccounts.setRemark(remark);
            //导入的数据默认wie微信支付
            omAccounts.setChannel(WECHAT_PAYMETHOD);
            //导入数据默认手工上载
            omAccounts.setSource("Y");

            if (errMsg.length() == 0) {
                Long accountsId = omAccountsMapper.checkIsExict(omAccounts);
                if (accountsId != null && accountsId > 0) {
                    throw new Exception("第" + n + "行数据重复.");
                }
                com.alibaba.fastjson.JSONObject accounts = insertData(omAccounts, __status, errMsg, n);
                successAccounts.add(accounts);
            } else {
                throw new Exception(errMsg.toString());
            }

        }
    }

    private com.alibaba.fastjson.JSONObject insertData(OmAccounts omAccounts, String __status, StringBuffer errMsg, int n) {
        if ("add".equalsIgnoreCase(__status)) {
            omAccountsMapper.insertSelective(omAccounts);
        }
        com.alibaba.fastjson.JSONObject accountsObj = JSON.parseObject(JSON.toJSONString(omAccounts));
        return accountsObj;
    }

    /**
     * Excel文本解析
     *
     * @param files
     * @return
     * @throws IOException
     */
    public ArrayList FilesAnalysis(MultipartFile files) throws IOException {
        String fileName = files.getOriginalFilename();
        XSSFWorkbook wb = new XSSFWorkbook(files.getInputStream());
        XSSFSheet sheet = wb.getSheetAt(0);

        XSSFRow nRow = null;
        Cell nCell = null;
        byte beginRowNo = 1;//定义拿取数据的起始行0开始
        int endRowNo = sheet.getLastRowNum();//拿到文件中数据总条数
        nRow = sheet.getRow(0);
        short endColNo = nRow.getLastCellNum();
        ArrayList excelList;
        if (endRowNo == 0) {
            wb.close();
            return null;
        } else {
            excelList = new ArrayList();
            for (int rData = beginRowNo; rData <= endRowNo; ++rData) {
                nRow = sheet.getRow(rData);
                ArrayList fileList = new ArrayList();
                for (int j = 0; j <= endColNo; ++j) {
                    nCell = nRow.getCell(j);
                    String val = "";
                    if (nCell != null) {
                        switch (nCell.getCellType()) {
                            case Cell.CELL_TYPE_STRING:
                                val = nCell.getStringCellValue();
                                break;
                            case Cell.CELL_TYPE_BOOLEAN:
                                Boolean val1 = nCell.getBooleanCellValue();
                                val = val1.toString();
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(nCell)) {
                                    Date theDate = nCell.getDateCellValue();
                                    SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    val = dff.format(theDate);
                                } else {
                                    DecimalFormat df = new DecimalFormat("##.##");
                                    val = df.format(nCell.getNumericCellValue());
                                }
                                break;
                            case Cell.CELL_TYPE_BLANK:
                                break;
                            default:
                                val = "数据类型配置不正确";
                        }

                        if (StringUtils.isNotBlank(val)) {
                            fileList.add(val);//拿到文件单行数据
                        } else {
                            fileList.add("");
                        }


                    } else {
                        fileList.add("");
                    }
                }

                excelList.add(fileList);//拿到文件中所有的值
            }
            wb.close();

        }
        return excelList;
    }


    @Override
    public List<OmAccounts> getAccountsForBalance(IRequest request, OmAccounts dto, int page, int pagesize) {
        PageHelper.startPage(page, pagesize);
        List<OmAccounts> result = omAccountsMapper.getAccountsForBalance(dto);
        if (CollectionUtils.isEmpty(result)) {
            return new ArrayList();
        }

        //界面表格中的用户代码字段，如果是微信和银联支付，直接设为9520；
        //通过TRANSACTION字段关联HMALL_OM_PAYMENTINFO表中的NUMBER_CODE字段，找到对应记录后，根据支付信息表中的订单id关联HMALL_OM_ORDER表，找到对应订单信息；
        // 然后用订单表中的WEBSITE_ID与网站表HMALL_MST_WEBSITE表中主键关联，取网站表HMALL_MST_WEBSITE中sold_party的值,如果没值默认为9519
        result.stream().map(new Function<OmAccounts, OmAccounts>() {
            @Override
            public OmAccounts apply(OmAccounts omAccounts) {
                if (omAccounts == null) {
                    return null;
                }
                if ((WECHAT_PAYMETHOD).equalsIgnoreCase(omAccounts.getChannel()) || (UNIONPAY_PAYMETHOD).equalsIgnoreCase(omAccounts.getChannel())) {
                    omAccounts.setUserNumber("9520");
                } else if (ALIPAY_PAYMETHOD.equalsIgnoreCase(omAccounts.getChannel())) {
                    if (StringUtils.isNotBlank(omAccounts.getTransaction())) {
                        String userNumber = omAccountsMapper.getUserNumber(omAccounts.getAccountsId());
                        if (StringUtils.isNotEmpty(userNumber)) {
                            omAccounts.setUserNumber(userNumber);
                        } else {
                            omAccounts.setUserNumber("9519");
                        }
                    }
                } else {
                    omAccounts.setUserNumber("9520");
                }
                return omAccounts;
            }
        }).collect(Collectors.toList());
        return result;
    }

    /**
     * 银联清算求和
     * 1、将每日清算的金额TYPE为"6"并且SYNC_FLAG为"N"的AMOUNT求和，如果大于0，生成一条TYPE为"5"的清算数据插入数据库
     * 2、将TYPE为"6"的数据的SYNC_FLAG设为"Y"
     */
    @Override
    public void unionLiquidationSum() {
        //查出银联提现需要求和的数据
        OmAccounts omAccounts = new OmAccounts();
        omAccounts.setChannel("UNIONPAY");
        omAccounts.setType("6");
        omAccounts.setSyncFlag("N");
        omAccounts.setSortname("time");
        omAccounts.setSortorder("asc");
        List<OmAccounts> omAccountsList = omAccountsMapper.select(omAccounts);
        if (null != omAccountsList && !omAccountsList.isEmpty()) {
            BigDecimal sum = new BigDecimal("0");
            //存储已求和但仍sum为负数的提现记录
            List<OmAccounts> negativeAccountsList = new ArrayList<>();
            for (OmAccounts account : omAccountsList) {
                //如果sum为初始值并且当天提现记录为正数,不求和,生成一条type为5的提现记录
                if (sum.compareTo(BigDecimal.ZERO) == 0 && account.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                    //生成一条type为5的提现记录
                    OmAccounts sumOmAccounts = new OmAccounts();
                    //YL+商户代码8位+最后一笔年月日
                    String transactionNum = "YL" + unionMerId.substring(unionMerId.length() - 8) + new DateTime(account.getTime()).toString("yyyyMMdd");
                    sumOmAccounts.setTransaction(transactionNum);
                    sumOmAccounts.setSerialNum(transactionNum);
                    sumOmAccounts.setTime(new DateTime(account.getTime()).toDate());
                    sumOmAccounts.setOutTradeNo("");
                    sumOmAccounts.setProduct("ZEST");
                    sumOmAccounts.setAccount("");
                    sumOmAccounts.setAmount(account.getAmount());
                    sumOmAccounts.setChannel("UNIONPAY");
                    sumOmAccounts.setType("5");
                    sumOmAccounts.setPreType("5");
                    sumOmAccounts.setRemark("ZEST");
                    int result = omAccountsMapper.insertSelective(sumOmAccounts);
                    if (result > 0) {
                        //更新为已求和状态
                        account.setSyncFlag("Y");
                        account.set__status("update");
                        omAccountsMapper.updateByPrimaryKeySelective(account);
                    }
                } else {//否则sum或提现金额为负数,进行求和
                    sum = sum.add(account.getAmount());
                    if (sum.compareTo(BigDecimal.ZERO) >= 0) {//求和为正数,成一条type为5的提现记录
                        OmAccounts sumOmAccounts = new OmAccounts();
                        //YL+商户代码8位+最后一笔年月日
                        String transactionNum = "YL" + unionMerId.substring(unionMerId.length() - 8) + new DateTime(account.getTime()).toString("yyyyMMdd");
                        sumOmAccounts.setTransaction(transactionNum);
                        sumOmAccounts.setSerialNum(transactionNum);
                        sumOmAccounts.setTime(new DateTime(account.getTime()).toDate());
                        sumOmAccounts.setOutTradeNo("");
                        sumOmAccounts.setProduct("ZEST");
                        sumOmAccounts.setAccount("");
                        sumOmAccounts.setAmount(sum);
                        sumOmAccounts.setChannel("UNIONPAY");
                        sumOmAccounts.setType("5");
                        sumOmAccounts.setPreType("5");
                        sumOmAccounts.setRemark("ZEST");
                        int result = omAccountsMapper.insertSelective(sumOmAccounts);
                        if (result > 0) {
                            //更新本条提现记录状态
                            account.setSyncFlag("Y");
                            account.set__status("update");
                            omAccountsMapper.updateByPrimaryKeySelective(account);
                            //将之前求和数据sum仍为负数的数据更新为以求和状态
                            for (OmAccounts negativeAccount : negativeAccountsList) {
                                negativeAccount.setSyncFlag("Y");
                                negativeAccount.set__status("update");
                            }
                            this.batchUpdate(new ServiceRequest(), negativeAccountsList);
                        }
                        negativeAccountsList = new ArrayList<>();
                        //求和为正数,将sum值初始为0,以便下笔提现数据求和判断
                        sum = new BigDecimal("0");
                    } else {//记录当前accountsId
                        negativeAccountsList.add(account);
                    }
                }
            }
        }

    }
}
