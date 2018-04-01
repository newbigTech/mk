package com.hand.hmall.service.impl;

import com.hand.hmall.code.MessageCode;
import com.hand.hmall.dto.HmallMstCustomerCheck;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.mapper.HmallMstCustomerCheckMapper;
import com.hand.hmall.service.IUserCheckCodeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 阳赳
 * @name:IUserCheckcodeServiceImpl
 * @Description:用户验证码校验
 * @date 2017/6/1 19:37
 */
@Service
public class UserCheckCodeServiceImpl implements IUserCheckCodeService
{
    @Autowired
    private HmallMstCustomerCheckMapper hmallMstCustomerCheckMapper;
    //验证码有效时间
    private final long VALID = 3 * 60 * 1000;

    @Override
    public ResponseData checkCode(HmallMstCustomerCheck hmallMstCustomerCheck)
    {
        ResponseData responseData = new ResponseData();

        //传入的手机号和业务类型,用户输入的验证码
        String mobileNumber = hmallMstCustomerCheck.getMobile(); //手机号
        String sendType = hmallMstCustomerCheck.getSendType(); //业务类型
        System.out.println(mobileNumber + "---" + sendType);
        String checkCode = hmallMstCustomerCheck.getCheckcode(); //验证码

        //检验手机号是否为空
        if(StringUtils.isEmpty(mobileNumber))
        {
            responseData.setMsgCode("ur.mobilenumber.null");
            responseData.setMsg("手机号为空");
            responseData.setSuccess(false);
            return responseData;
        }

        //检验业务类型是否为空
        if(StringUtils.isEmpty(sendType))
        {
            responseData.setMsgCode("ur.sendtype.null");
            responseData.setMsg("业务类型为空");
            responseData.setSuccess(false);
            return responseData;
        }

        //检验验证码是否为空
        if(StringUtils.isEmpty(checkCode))
        {
            responseData.setMsgCode("ur.checkcode.null");
            responseData.setMsg("验证码为空");
            responseData.setSuccess(false);
            return responseData;
        }

        //根据手机号和业务类型获取存储的验证码
        HmallMstCustomerCheck customerCheck = hmallMstCustomerCheckMapper.selectByMobileAndSendType(mobileNumber,sendType);
        if (customerCheck == null)
        {
            responseData.setMsgCode("ur.checkdoce.mobilenumber.noexist");
            responseData.setMsg(MessageCode.UR_LOGIN_03.getValue());
            responseData.setSuccess(false);
            return responseData;
        }

        //存储的验证码
        String code = customerCheck.getCheckcode();
        //检验验证码是否为空
        if(StringUtils.isEmpty(code))
        {
            responseData.setMsgCode("ur.code.null");
            responseData.setMsg("验证码为空");
            responseData.setSuccess(false);
            return responseData;
        }

        //短信验证码有效性标记
        String flag = customerCheck.getAttribute1();
        //短信验证码有效性标记是否为空
        if(StringUtils.isEmpty(flag))
        {
            responseData.setMsgCode("ur.flag.null");
            responseData.setMsg("短信验证码有效性标记为空");
            responseData.setSuccess(false);
            return responseData;
        }

        //如果验证码符合则判断是否失效
        if (checkCode.equals(code))
        {
            //获取系统当前时间
            Long currentTime = System.currentTimeMillis();
            //获取数据库验证码最后更新时间
            Long updateDate = customerCheck.getLastUpdateDate().getTime();
            //如果 (系统当前时间-生成验证码的时间 > 有效验证码时间) || (短信验证码有效性标记:true),则验证码失效
            if ((currentTime - updateDate > VALID) || ("true".equals(flag)))
            {
                //验证码失效
                responseData.setMsgCode("ur.checkdoce.checkcode.Invalid");
                responseData.setMsg(MessageCode.UR_CHECK_011.getValue());
                responseData.setSuccess(false);
                return responseData;
            }
            else
            {
                //验证码正确
                responseData.setMsgCode("1");
                responseData.setMsg(MessageCode.UR_CHECK_019.getValue());
                responseData.setSuccess(true);

                //短信验证码成功后,将短信验证码有效性flag设置为:true
                customerCheck.setAttribute1("true");
                hmallMstCustomerCheckMapper.updateByPrimaryKeySelective(customerCheck);
                return responseData;
            }
        }
        else
        {
            //验证码错误
            responseData.setMsgCode("ur.checkdoce.checkcode.error");
            responseData.setMsg(MessageCode.UR_CHECK_020.getValue());
            responseData.setSuccess(false);
            return responseData;
        }
    }
}
