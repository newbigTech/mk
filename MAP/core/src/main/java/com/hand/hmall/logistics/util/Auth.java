package com.hand.hmall.logistics.util;

import java.security.MessageDigest;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name Auth
 * @description 推送官网加密方法
 * @date 2017年5月26日10:52:23
 */
public class Auth {

    private static final String MD5 = "MD5";
    private static final String UTF8 = "utf-8";
    private static final char[] HEXDIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};


    public static String md5(String input) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(MD5);
            byte[] inputByteArray = input.getBytes(UTF8);
            messageDigest.update(inputByteArray);
            byte[] resultByteArray = messageDigest.digest();
            return byteArrayToHex(resultByteArray);
        } catch (Exception e) {
            return null;
        }
    }

    private static String byteArrayToHex(byte[] byteArray) {
        char[] resultCharArray = new char[byteArray.length * 2];
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = HEXDIGITS[b >>> 4 & 0xf];
            resultCharArray[index++] = HEXDIGITS[b & 0xf];
        }
        return new String(resultCharArray);
    }
}
