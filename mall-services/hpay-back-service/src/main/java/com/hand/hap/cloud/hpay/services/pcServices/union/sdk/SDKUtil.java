/**
 * Licensed Property to China UnionPay Co., Ltd.
 * <p>
 * (C) Copyright of China UnionPay Co., Ltd. 2010
 * All Rights Reserved.
 * <p>
 * <p>
 * Modification History:
 * =============================================================================
 * Author         Date          Description
 * ------------ ---------- ---------------------------------------------------
 * xshu       2014-05-28      MPI工具类
 * =============================================================================
 */
package com.hand.hap.cloud.hpay.services.pcServices.union.sdk;

import com.hand.hap.cloud.hpay.services.constants.UnionConstants;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.zip.Inflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class SDKUtil {

    private static Logger logger = LoggerFactory.getLogger(SDKUtil.class);

    /**
     * 生成签名值(SHA1摘要算法)
     *
     * @param data     待签名数据Map键值对形式
     * @param encoding 编码
     * @return 签名是否成功
     */
    public static boolean sign(Map<String, String> data, String encoding) {
        if (isEmpty(encoding)) {
            encoding = "UTF-8";
        }
        // 设置签名证书序列号
        data.put(UnionConstants.PARAM_CERTID, CertUtil.getSignCertId());
        // 将Map信息转换成key1=value1&key2=value2的形式
        String stringData = coverMap2String(data);
        logger.info("待签名请求报文串:[" + stringData + "]");
        /**
         * 签名\base64编码
         */
        byte[] byteSign = null;
        String stringSign = null;
        try {
            // 通过SHA1进行摘要并转16进制
            byte[] signDigest = SecureUtil.sha1X16(stringData, encoding);
            byteSign = SecureUtil.base64Encode(SecureUtil.signBySoft(
                    CertUtil.getSignCertPrivateKey(), signDigest));
            stringSign = new String(byteSign);
            // 设置签名域值
            data.put(UnionConstants.PARAM_SIGNATURE, stringSign);
            return true;
        } catch (Exception e) {
            logger.info("签名异常", e);
            return false;
        }
    }

    /**
     * 通过传入的证书绝对路径和证书密码读取签名证书进行签名并返回签名值<br>
     *
     * @param data     待签名数据Map键值对形式
     * @param encoding 编码
     * @param certPath 证书绝对路径
     * @param certPwd  证书密码
     * @return 签名值
     */
    public static boolean signByCertInfo(Map<String, String> data,
                                         String certPath, String certPwd, String encoding) {
        if (isEmpty(encoding)) {
            encoding = "UTF-8";
        }
        if (isEmpty(certPath) || isEmpty(certPwd)) {
            logger.info("Invalid Parameter:CertPath=[" + certPath
                    + "],CertPwd=[" + certPwd + "]");
            return false;
        }
        // 设置签名证书序列号
        data.put(UnionConstants.PARAM_CERTID,
                CertUtil.getCertIdByKeyStoreMap(certPath, certPwd));
        // 将Map信息转换成key1=value1&key2=value2的形式
        String stringData = coverMap2String(data);
        /**
         * 签名\base64编码
         */
        byte[] byteSign = null;
        String stringSign = null;
        try {
            byte[] signDigest = SecureUtil.sha1X16(stringData, encoding);
            byteSign = SecureUtil.base64Encode(SecureUtil.signBySoft(
                    CertUtil.getSignCertPrivateKeyByStoreMap(certPath, certPwd),
                    signDigest));
            stringSign = new String(byteSign);
            // 设置签名域值
            data.put(UnionConstants.PARAM_SIGNATURE, stringSign);
            return true;
        } catch (Exception e) {
            logger.info("签名异常", e);
            return false;
        }
    }


    /**
     * 将Map中的数据转换成按照Key的ascii码排序后的key1=value1&key2=value2的形式 不包含签名域signature
     *
     * @param data 待拼接的Map数据
     * @return 拼接好后的字符串
     */
    public static String coverMap2String(Map<String, String> data) {
        TreeMap<String, String> tree = new TreeMap<String, String>();
        Iterator<Entry<String, String>> it = data.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, String> en = it.next();
            if (UnionConstants.PARAM_SIGNATURE.equals(en.getKey().trim())) {
                continue;
            }
            tree.put(en.getKey(), en.getValue());
        }
        it = tree.entrySet().iterator();
        StringBuffer sf = new StringBuffer();
        while (it.hasNext()) {
            Entry<String, String> en = it.next();
            sf.append(en.getKey() + UnionConstants.EQUAL + en.getValue()
                    + UnionConstants.AMPERSAND);
        }
        return sf.substring(0, sf.length() - 1);
    }


    /**
     * 兼容老方法 将形如key=value&key=value的字符串转换为相应的Map对象
     *
     * @param result
     * @return
     */
    public static Map<String, String> coverResultString2Map(String result) {
        return convertResultStringToMap(result);
    }

    /**
     * 将形如key=value&key=value的字符串转换为相应的Map对象
     *
     * @param result
     * @return
     */
    public static Map<String, String> convertResultStringToMap(String result) {
        Map<String, String> map = null;
        try {

            if (StringUtils.isNotBlank(result)) {
                if (result.startsWith("{") && result.endsWith("}")) {
                    System.out.println(result.length());
                    result = result.substring(1, result.length() - 1);
                }
                map = parseQString(result);
            }

        } catch (UnsupportedEncodingException e) {
            logger.info(e.getMessage(), e);
        }
        return map;
    }


    /**
     * 解析应答字符串，生成应答要素
     *
     * @param str 需要解析的字符串
     * @return 解析的结果map
     * @throws UnsupportedEncodingException
     */
    public static Map<String, String> parseQString(String str)
            throws UnsupportedEncodingException {

        Map<String, String> map = new HashMap<String, String>();
        int len = str.length();
        StringBuilder temp = new StringBuilder();
        char curChar;
        String key = null;
        boolean isKey = true;
        boolean isOpen = false;//值里有嵌套
        char openName = 0;
        if (len > 0) {
            for (int i = 0; i < len; i++) {// 遍历整个带解析的字符串
                curChar = str.charAt(i);// 取当前字符
                if (isKey) {// 如果当前生成的是key

                    if (curChar == '=') {// 如果读取到=分隔符
                        key = temp.toString();
                        temp.setLength(0);
                        isKey = false;
                    } else {
                        temp.append(curChar);
                    }
                } else {// 如果当前生成的是value
                    if (isOpen) {
                        if (curChar == openName) {
                            isOpen = false;
                        }

                    } else {//如果没开启嵌套
                        if (curChar == '{') {//如果碰到，就开启嵌套
                            isOpen = true;
                            openName = '}';
                        }
                        if (curChar == '[') {
                            isOpen = true;
                            openName = ']';
                        }
                    }
                    if (curChar == '&' && !isOpen) {// 如果读取到&分割符,同时这个分割符不是值域，这时将map里添加
                        putKeyValueToMap(temp, isKey, key, map);
                        temp.setLength(0);
                        isKey = true;
                    } else {
                        temp.append(curChar);
                    }
                }

            }
            putKeyValueToMap(temp, isKey, key, map);
        }
        return map;
    }

    private static void putKeyValueToMap(StringBuilder temp, boolean isKey,
                                         String key, Map<String, String> map)
            throws UnsupportedEncodingException {
        if (isKey) {
            key = temp.toString();
            if (key.length() == 0) {
                throw new RuntimeException("QString format illegal");
            }
            map.put(key, "");
        } else {
            if (key.length() == 0) {
                throw new RuntimeException("QString format illegal");
            }
            map.put(key, temp.toString());
        }
    }

    /**
     * 判断字符串是否为NULL或空
     *
     * @param s 待判断的字符串数据
     * @return 判断结果 true-是 false-否
     */
    public static boolean isEmpty(String s) {
        return null == s || "".equals(s.trim());
    }

    /**
     * 过滤请求报文中的空字符串或者空字符串
     *
     * @param contentData
     * @return
     */
    public static Map<String, String> filterBlank(Map<String, String> contentData) {
        logger.info("打印请求报文域 :");
        Map<String, String> submitFromData = new HashMap<String, String>();
        Set<String> keyset = contentData.keySet();

        for (String key : keyset) {
            String value = contentData.get(key);
            if (StringUtils.isNotBlank(value)) {
                // 对value值进行去除前后空处理
                submitFromData.put(key, value.trim());
                logger.info(key + "-->" + String.valueOf(value));
            }
        }
        return submitFromData;
    }

    public static boolean validate(final Map<String, String> resData, String encoding) {
        logger.info("验签处理开始.");
        if (isEmpty(encoding)) {
            encoding = "UTF-8";
        }

        final String stringSign = (String) resData.get("signature");
        final String certId = (String) resData.get("certId");
        final String stringData = coverMap2String(resData);

        try {
            return SecureUtil.validateSignBySoft(CertUtil.getValidateKey(certId), SecureUtil.base64Decode(stringSign.getBytes(encoding)), SecureUtil.sha1X16(stringData, encoding));
        } catch (final UnsupportedEncodingException var6) {
            logger.info(var6.getMessage(), var6);
        } catch (final Exception var7) {
            logger.info(var7.getMessage(), var7);
        }

        return false;
    }

    /**
     * 解压缩.
     *
     * @param inputByte byte[]数组类型的数据
     * @return 解压缩后的数据
     * @throws IOException
     */
    public static byte[] inflater(final byte[] inputByte) throws IOException {
        int compressedDataLength = 0;
        Inflater compresser = new Inflater(false);
        compresser.setInput(inputByte, 0, inputByte.length);
        ByteArrayOutputStream o = new ByteArrayOutputStream(inputByte.length);
        byte[] result = new byte[1024];
        try {
            while (!compresser.finished()) {
                compressedDataLength = compresser.inflate(result);
                if (compressedDataLength == 0) {
                    break;
                }
                o.write(result, 0, compressedDataLength);
            }
        } catch (Exception ex) {
            System.err.println("Data format error!\n");
            ex.printStackTrace();
        } finally {
            o.close();
        }
        compresser.end();
        return o.toByteArray();
    }

    public static List<String> unzip(String zipFilePath, String outPutDirectory) {
        List<String> fileList = new ArrayList<String>();
        try {
            ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFilePath));//输入源zip路径
            BufferedInputStream bin = new BufferedInputStream(zin);
            BufferedOutputStream bout = null;
            File file = null;
            ZipEntry entry;
            try {
                while ((entry = zin.getNextEntry()) != null && !entry.isDirectory()) {
                    file = new File(outPutDirectory, entry.getName());
                    if (!file.exists()) {
                        (new File(file.getParent())).mkdirs();
                    }
                    bout = new BufferedOutputStream(new FileOutputStream(file));
                    int b;
                    while ((b = bin.read()) != -1) {
                        bout.write(b);
                    }
                    bout.flush();
                    fileList.add(file.getAbsolutePath());
                    System.out.println(file + "解压成功");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    bin.close();
                    zin.close();
                    if (bout != null) {
                        bout.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fileList;
    }

    /**
     * 功能：解析全渠道商户对账文件中的ZM文件并以List<Map>方式返回
     * 适用交易：对账文件下载后对文件的查看
     *
     * @param filePath ZM文件全路径
     * @return 包含每一笔交易中 序列号 和 值 的map序列
     */
    public static List<Map> parseZMFile(String filePath) {
        int lengthArray[] = {3, 11, 11, 6, 10, 19, 12, 4, 2, 21, 2, 32, 2, 6, 10, 13, 13, 4, 15, 2, 2, 6, 2, 4, 32, 1, 21, 15, 1, 15, 32, 13, 13, 8, 32, 13, 13, 12, 2, 1, 32, 98};
        return parseFile(filePath, lengthArray);
    }

    /**
     * 功能：解析全渠道商户对账文件中的ZME文件并以List<Map>方式返回
     * 适用交易：对账文件下载后对文件的查看
     *
     * @param filePath ZME文件全路径
     * @return 包含每一笔交易中 序列号 和 值 的map序列
     */
    public static List<Map> parseZMEFile(String filePath) {
        int lengthArray[] = {3, 11, 11, 6, 10, 19, 12, 4, 2, 2, 6, 10, 4, 12, 13, 13, 15, 15, 1, 12, 2, 135};
        return parseFile(filePath, lengthArray);
    }

    /**
     * 功能：解析全渠道商户 ZM,ZME对账文件
     *
     * @param filePath
     * @param lengthArray 参照《全渠道平台接入接口规范 第3部分 文件接口》 全渠道商户对账文件 6.1 ZM文件和6.2 ZME 文件 格式的类型长度组成int型数组
     * @return
     */
    private static List<Map> parseFile(String filePath, int lengthArray[]) {
        List<Map> ZmDataList = new ArrayList<Map>();
        try {
            String encoding = "UTF-8";
            File file = new File(filePath);
            if (file.isFile() && file.exists()) { //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    //解析的结果MAP，key为对账文件列序号，value为解析的值
                    Map<Integer, String> ZmDataMap = new LinkedHashMap<Integer, String>();
                    //左侧游标
                    int leftIndex = 0;
                    //右侧游标
                    int rightIndex = 0;
                    for (int i = 0; i < lengthArray.length; i++) {
                        rightIndex = leftIndex + lengthArray[i];
                        String filed = lineTxt.substring(leftIndex, rightIndex);
                        leftIndex = rightIndex + 1;
                        ZmDataMap.put(i, filed);
                    }
                    ZmDataList.add(ZmDataMap);
                }
                read.close();
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }

        return ZmDataList;
    }

    public static String getFileContentTable(List<Map> dataList, String file) {
        StringBuffer tableSb = new StringBuffer("对账文件的规范参考 https://open.unionpay.com/ajweb/help/file/ 产品接口规范->平台接口规范:文件接口</br> 文件【" + file + "】解析后内容如下：");
        tableSb.append("<table border=\"1\">");
        if (dataList.size() > 0) {
            Map<Integer, String> dataMapTmp = dataList.get(0);
            tableSb.append("<tr>");
            for (Iterator<Integer> it = dataMapTmp.keySet().iterator(); it.hasNext(); ) {
                Integer key = it.next();
                String value = dataMapTmp.get(key);
                System.out.println("序号：" + (key + 1) + " 值: '" + value + "'");
                tableSb.append("<td>序号" + (key + 1) + "</td>");
            }
            tableSb.append("</tr>");
        }

        for (int i = 0; i < dataList.size(); i++) {
            System.out.println("行数: " + (i + 1));
            Map<Integer, String> dataMapTmp = dataList.get(i);
            tableSb.append("<tr>");
            for (Iterator<Integer> it = dataMapTmp.keySet().iterator(); it.hasNext(); ) {
                Integer key = it.next();
                String value = dataMapTmp.get(key);
                System.out.println("序号：" + (key + 1) + " 值: '" + value + "'");
                tableSb.append("<td>" + value + "</td>");
            }
            tableSb.append("</tr>");
        }
        tableSb.append("</table>");
        return tableSb.toString();
    }
}
