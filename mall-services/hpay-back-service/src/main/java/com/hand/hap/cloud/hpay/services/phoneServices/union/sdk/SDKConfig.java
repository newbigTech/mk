/**
 *
 * Licensed Property to China UnionPay Co., Ltd.
 * 
 * (C) Copyright of China UnionPay Co., Ltd. 2010
 *     All Rights Reserved.
 *
 * 
 * Modification History:
 * =============================================================================
 *   Author         Date          Description
 *   ------------ ---------- ---------------------------------------------------
 *   xshu       2014-05-28       MPI基本参数工具类
 * =============================================================================
 */
package com.hand.hap.cloud.hpay.services.phoneServices.union.sdk;


import com.hand.hap.cloud.hpay.utils.PropertiesUtil;

/**
 * @author jianjun.tan
 * @Title PropertiesUtil
 * @date 2017/05/25 19:08
 */
public class SDKConfig {

	/** 前台请求URL. */
	private static String frontRequestUrl = PropertiesUtil.getHpayValue("acpsdk.frontTransUrl");
	/** 后台请求URL. */
	private static String backRequestUrl = PropertiesUtil.getHpayValue("acpsdk.backTransUrl");
	/** 单笔查询 */
	private static String singleQueryUrl = PropertiesUtil.getHpayValue("acpsdk.singleQueryUrl");
	/** 批量查询 */
	private static String batchQueryUrl = PropertiesUtil.getHpayValue("acpsdk.batchQueryUrl");
	/** 批量交易 */
	private static String batchTransUrl = PropertiesUtil.getHpayValue("acpsdk.batchTransUrl");
	/** 文件传输 */
	private static String fileTransUrl = PropertiesUtil.getHpayValue("acpsdk.fileTransUrl");
	/** 签名证书路径. */
	private static String signCertPath = PropertiesUtil.getHpayValue("acpsdk.signCert.path");
	/** 签名证书密码. */
	private static String signCertPwd = PropertiesUtil.getHpayValue("acpsdk.signCert.pwd");
	/** 签名证书类型. */
	private static String signCertType = PropertiesUtil.getHpayValue("acpsdk.signCert.type");
	/** 加密公钥证书路径. */
	private static String encryptCertPath = PropertiesUtil.getHpayValue("acpsdk.encryptCert.path");
	/** 验证签名公钥证书目录. */
	private static String validateCertDir = PropertiesUtil.getHpayValue("acpsdk.validateCert.dir");
	/** 按照商户代码读取指定签名证书目录. */
	//private static String signCertDir = PropertiesUtil.getHpayValue("KEY");
	/** 磁道加密证书路径. */
	private static String encryptTrackCertPath = PropertiesUtil.getHpayValue("acpsdk.encryptTrackCert.path");
	/** 磁道加密公钥模数. */
	private static String encryptTrackKeyModulus = PropertiesUtil.getHpayValue("acpsdk.encryptTrackKey.modulus");
	/** 磁道加密公钥指数. */
	private static String encryptTrackKeyExponent = PropertiesUtil.getHpayValue("acpsdk.encryptTrackKey.exponent");
	/** 有卡交易. */
	private static String cardRequestUrl = PropertiesUtil.getHpayValue("acpsdk.cardTransUrl");
	/** app交易 */
	private static String appRequestUrl = PropertiesUtil.getHpayValue("acpsdk.appTransUrl");
	/** 证书使用模式(单证书/多证书) */
	private static String singleMode = PropertiesUtil.getHpayValue("acpsdk.singleMode");

	/*缴费相关地址*/
	private static String jfFrontRequestUrl = PropertiesUtil.getHpayValue("acpsdk.jfFrontTransUrl");
	private static String jfBackRequestUrl = PropertiesUtil.getHpayValue("acpsdk.jfBackTransUrl");
	private static String jfSingleQueryUrl = PropertiesUtil.getHpayValue("acpsdk.jfSingleQueryUrl");
	private static String jfCardRequestUrl = PropertiesUtil.getHpayValue("acpsdk.jfCardTransUrl");
	private static String jfAppRequestUrl = PropertiesUtil.getHpayValue("acpsdk.jfAppTransUrl");


	/** 操作对象. */
	private static SDKConfig config;
	/**
	 * 获取config对象.
	 *
	 * @return
	 */
	public static SDKConfig getConfig() {
		if (null == config) {
			config = new SDKConfig();
		}
		return config;
	}

	public static String getFrontRequestUrl() {
		return frontRequestUrl;
	}

	public static void setFrontRequestUrl(String frontRequestUrl) {
		SDKConfig.frontRequestUrl = frontRequestUrl;
	}

	public static String getBackRequestUrl() {
		return backRequestUrl;
	}

	public static void setBackRequestUrl(String backRequestUrl) {
		SDKConfig.backRequestUrl = backRequestUrl;
	}

	public static String getSingleQueryUrl() {
		return singleQueryUrl;
	}

	public static void setSingleQueryUrl(String singleQueryUrl) {
		SDKConfig.singleQueryUrl = singleQueryUrl;
	}

	public static String getBatchQueryUrl() {
		return batchQueryUrl;
	}

	public static void setBatchQueryUrl(String batchQueryUrl) {
		SDKConfig.batchQueryUrl = batchQueryUrl;
	}

	public static String getBatchTransUrl() {
		return batchTransUrl;
	}

	public static void setBatchTransUrl(String batchTransUrl) {
		SDKConfig.batchTransUrl = batchTransUrl;
	}

	public static String getFileTransUrl() {
		return fileTransUrl;
	}

	public static void setFileTransUrl(String fileTransUrl) {
		SDKConfig.fileTransUrl = fileTransUrl;
	}

	public static String getSignCertPath() {
		return signCertPath;
	}

	public static void setSignCertPath(String signCertPath) {
		SDKConfig.signCertPath = signCertPath;
	}

	public static String getSignCertPwd() {
		return signCertPwd;
	}

	public static void setSignCertPwd(String signCertPwd) {
		SDKConfig.signCertPwd = signCertPwd;
	}

	public static String getSignCertType() {
		return signCertType;
	}

	public static void setSignCertType(String signCertType) {
		SDKConfig.signCertType = signCertType;
	}

	public static String getEncryptCertPath() {
		return encryptCertPath;
	}

	public static void setEncryptCertPath(String encryptCertPath) {
		SDKConfig.encryptCertPath = encryptCertPath;
	}

	public static String getValidateCertDir() {
		return validateCertDir;
	}

	public static void setValidateCertDir(String validateCertDir) {
		SDKConfig.validateCertDir = validateCertDir;
	}

	public static String getEncryptTrackCertPath() {
		return encryptTrackCertPath;
	}

	public static void setEncryptTrackCertPath(String encryptTrackCertPath) {
		SDKConfig.encryptTrackCertPath = encryptTrackCertPath;
	}

	public static String getEncryptTrackKeyModulus() {
		return encryptTrackKeyModulus;
	}

	public static void setEncryptTrackKeyModulus(String encryptTrackKeyModulus) {
		SDKConfig.encryptTrackKeyModulus = encryptTrackKeyModulus;
	}

	public static String getEncryptTrackKeyExponent() {
		return encryptTrackKeyExponent;
	}

	public static void setEncryptTrackKeyExponent(String encryptTrackKeyExponent) {
		SDKConfig.encryptTrackKeyExponent = encryptTrackKeyExponent;
	}

	public static String getCardRequestUrl() {
		return cardRequestUrl;
	}

	public static void setCardRequestUrl(String cardRequestUrl) {
		SDKConfig.cardRequestUrl = cardRequestUrl;
	}

	public static String getAppRequestUrl() {
		return appRequestUrl;
	}

	public static void setAppRequestUrl(String appRequestUrl) {
		SDKConfig.appRequestUrl = appRequestUrl;
	}

	public static String getSingleMode() {
		return singleMode;
	}

	public static void setSingleMode(String singleMode) {
		SDKConfig.singleMode = singleMode;
	}

	public static String getJfFrontRequestUrl() {
		return jfFrontRequestUrl;
	}

	public static void setJfFrontRequestUrl(String jfFrontRequestUrl) {
		SDKConfig.jfFrontRequestUrl = jfFrontRequestUrl;
	}

	public static String getJfBackRequestUrl() {
		return jfBackRequestUrl;
	}

	public static void setJfBackRequestUrl(String jfBackRequestUrl) {
		SDKConfig.jfBackRequestUrl = jfBackRequestUrl;
	}

	public static String getJfSingleQueryUrl() {
		return jfSingleQueryUrl;
	}

	public static void setJfSingleQueryUrl(String jfSingleQueryUrl) {
		SDKConfig.jfSingleQueryUrl = jfSingleQueryUrl;
	}

	public static String getJfCardRequestUrl() {
		return jfCardRequestUrl;
	}

	public static void setJfCardRequestUrl(String jfCardRequestUrl) {
		SDKConfig.jfCardRequestUrl = jfCardRequestUrl;
	}

	public static String getJfAppRequestUrl() {
		return jfAppRequestUrl;
	}

	public static void setJfAppRequestUrl(String jfAppRequestUrl) {
		SDKConfig.jfAppRequestUrl = jfAppRequestUrl;
	}
}
