package com.hand.hmall.service;

import com.hand.hmall.dto.ResponseData;

/**
 * @author 阳赳
 * @name:IUserMobileCheakServiceImpl
 * @Description:校验用户手机号码
 * @date 2017/6/1 9:19
 */
public interface IUserMobileCheakService {

    /**
     * 校验用户id是否合法
     *
     * @param customerID 用户id 等同于手机号
     * @return
     */
    ResponseData check(String customerID);
}
