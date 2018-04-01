package com.hand.hmall.service;

import com.hand.hmall.dto.HmallMstCustomerCheck;
import com.hand.hmall.dto.ResponseData;

/**
 * @author 阳赳
 * @name:IUserCheckcodeService
 * @Description:用户验证码校验
 * @date 2017/6/1 19:35
 */
public interface IUserCheckCodeService
{
    /**
     * 验证码检验
     * @param hmallMstCustomerCheck
     * @return
     */
    ResponseData checkCode(HmallMstCustomerCheck hmallMstCustomerCheck);
}
