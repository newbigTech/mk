package com.hand.hmall.service.impl;

import com.hand.hmall.code.MessageCode;
import com.hand.hmall.dto.HmallMstUser;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.mapper.HmallMstUserMapper;
import com.hand.hmall.service.IUserMobileCheakService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.hand.hmall.dto.MsgCode.*;

/**
 * @author 阳赳
 * @name:IUserMobileCheakServiceImpl
 * @Description:校验用户手机号码
 * @date 2017/6/1 9:19
 */
@Service("IUserMobileCheakService")
public class UserMobileCheakServiceImpl implements IUserMobileCheakService
{
    @Resource
    private HmallMstUserMapper hmallMstUserMapper;

    /**
     * 校验用户id是否合法
     *
     * @param customerID 用户id 等同于手机号
     * @return
     */
    @Override
    public ResponseData check(String customerID)
    {
        ResponseData responseData = new ResponseData();
        if (StringUtils.isEmpty(customerID))
        {
            //手机号不能为空
            responseData.setMsgCode("ur.mobile.mobilenumber.null");
            responseData.setMsg(MessageCode.UR_MOBILE_013.getValue());
            responseData.setSuccess(false);
            return responseData;
        }
        //校检手机号是否合法
        String regex = "^((13[0-9])|(14[5,6,7,9])|(15[^4,\\D])|(16[6])|(17[0,1,3,5-8])|(18[0-9]))\\d{8}$";
        String phone = customerID.replace(" ","");
        if (phone.matches(regex))
        {
            //校检手机号是否已存在
            HmallMstUser hmallMstUser = hmallMstUserMapper.selectByCustomerId(phone);
            if (null == hmallMstUser)
            {
                //手机号合法且未被使用
                responseData.setMsgCode("1");
                responseData.setMsg(MessageCode.UR_MOBILE_014.getValue());
                responseData.setSuccess(true);
                return responseData;
            }
            else
            {
                //手机号合法但被使用
                responseData.setMsgCode("ur.mobile.mobilenumber.isexist");
                responseData.setMsg(MessageCode.UR_MOBILE_015.getValue());
                responseData.setSuccess(false);
                return responseData;
            }
        }
        //手机号不合法
        responseData.setMsgCode("ur.mobile.mobilenumber.illegal");
        responseData.setMsg(MessageCode.UR_MOBILE_022.getValue());
        responseData.setSuccess(false);
        return responseData;
    }
}
