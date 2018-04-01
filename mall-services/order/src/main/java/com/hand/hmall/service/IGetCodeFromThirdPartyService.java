/*
 *  Copyright (C) HAND Enterprise Solutions Company Ltd.
 *  All Rights Reserved
 *
 */

package com.hand.hmall.service;

/**
 * @author 李伟
 * @version 1.0
 * @name:IGetCodeFromThirdPartyService
 * @Description: 调用第三方服务,获取订单编码、订单行号和pin码
 * @date 2017/8/22 23:23
 */
public interface IGetCodeFromThirdPartyService {

    /**
     * 调用第三方服务,获取订单编码
     * @return
     */
    String getOrderCode();


    /**
     * 调用第三方服务,获取订单行编码
     * @return
     */
    String getOrderEntryCode();


    /**
     * 调用第三方服务,获取pin码
     * @return
     */
    String getPinCode();
}
