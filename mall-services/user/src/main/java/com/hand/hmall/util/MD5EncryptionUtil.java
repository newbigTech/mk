/*
 *  Copyright (C) HAND Enterprise Solutions Company Ltd.
 *  All Rights Reserved
 *
 */

package com.hand.hmall.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author 李伟
 * @version 1.0
 * @name:MD5EncryptionUtil
 * @Description: MD5加密公共类
 * @date 2017/8/2 11:01
 */
public class MD5EncryptionUtil {

    /**
     * MD5加密算法
     * @param pwd
     * @return
     */
    public static String getMd5(String pwd)
    {
        try {
            //创建信息摘要对象实例
            MessageDigest md = MessageDigest.getInstance("MD5");
            //获取文本明文为字节
            md.update(pwd.getBytes());
            //创建字节摘要数组
            byte b[] = md.digest();
            //创建 int 类型变量i
            int i;
            //创建StringBuffer容器
            StringBuffer buf = new StringBuffer("");
            for (int j = 0; j < b.length; j++) {
                i = b[j];
                if (i < 0)
                    i += 256;   //md5加密最长32位字符.一个字符占8个字节.所以最长允许256个字节的字符串
                if (i < 16)     //一个字符=8个字节 0-15不足字符俩字符则补0拼接
                    buf.append("0");
                buf.append(Integer.toHexString(i));//int类型10进制转16进制
            }
            //32位加密 小写
            String password = buf.toString().toLowerCase();
            return password;
            // 16位的加密 小写
            //return buf.toString().substring(8, 24).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * MD5加盐加密
     * @param pwd
     * @param salt
     * @return
     */
    public static String getMd5AddSalt(String pwd,String salt)
    {
        try {
            //创建信息摘要对象实例
            MessageDigest md = MessageDigest.getInstance("MD5");
            //获取文本明文为字节
            md.update((pwd + salt).getBytes());
            //创建字节摘要数组
            byte b[] = md.digest();
            //创建 int 类型变量i
            int i;
            //创建StringBuffer容器
            StringBuffer buf = new StringBuffer("");
            for (int j = 0; j < b.length; j++)
            {
                i = b[j];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16)
                    buf.append("0");

                buf.append(Integer.toHexString(i));
            }
            //32位加密 小写
            String password = buf.toString().toLowerCase();
            return password;
            // 16位的加密 小写
            //return buf.toString().substring(8, 24).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
