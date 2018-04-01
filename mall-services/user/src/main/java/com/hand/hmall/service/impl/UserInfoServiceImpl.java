package com.hand.hmall.service.impl;

import com.hand.hmall.code.MessageCode;
import com.hand.hmall.dto.HmallMstUser;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.exception.UserUpdateHistoryException;
import com.hand.hmall.mapper.HmallMstUserMapper;
import com.hand.hmall.service.IUserInfoService;
import com.hand.hmall.util.MD5EncryptionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 阳赳
 * @name:IUserInfoServiceImpl
 * @Description:实现用户信息相关业务逻辑
 * @date 2017/5/25 16:56
 * @version: 1.0
 */
@Service
public class UserInfoServiceImpl implements IUserInfoService
{
    @Resource
    private HmallMstUserMapper hmallMstUserMapper;
    @Value("${data.salt}")
    private String SALT;

    /**
     * 更新用户信息
     *
     * @param info 更新信息
     * @return
     * @throws UserUpdateHistoryException
     */
    @Override
    public ResponseData update(Map info) throws UserUpdateHistoryException, NoSuchAlgorithmException
    {
        ResponseData responseData = new ResponseData();
        String mobileNumber = (String) info.get("mobileNumber");
        if (StringUtils.isBlank(mobileNumber))
        {
            //手机号不能为空
            responseData.setMsg(MessageCode.UR_MOBILE_013.getValue());
            responseData.setMsgCode("ur.update.mobilenumber.null");
            responseData.setSuccess(false);
            return responseData;
        }

        //用户信息不存在,无法更新
        HmallMstUser hmallMstUser = hmallMstUserMapper.selectByMobile(mobileNumber);
        if (null == hmallMstUser)
        {
            //用户名不存在
            responseData.setSuccess(false);
            responseData.setMsgCode("ur.update.mobilenumber.noexist");
            responseData.setMsg(MessageCode.UR_LOGIN_03.getValue());
            return responseData;
        }
        else
        {
            //用户信息存在就更新
            HmallMstUser mstUser = new HmallMstUser();
            String name = (String) info.get("name"); //用户名
            String sex = (String) info.get("sex"); //性别
            String pwd = (String) info.get("password"); //密码
            String birthday = (String) info.get("birthday"); //生日
            String email = (String) info.get("email"); //邮箱
            String label = (String) info.get("label"); //标签
            String remark = (String) info.get("remark"); //标签
            String country = (String) info.get("country"); //国家
            String state = (String) info.get("state"); //省
            String city = (String) info.get("city"); //市
            String district = (String) info.get("district"); //区
            String qqOpenId = (String) info.get("qqOpenId"); //qqOpenId
            String wxOpenId = (String) info.get("wxOpenId"); //wxOpenId

            //更新用户名
            mstUser.setName(name);

            //性别验证
            if (!StringUtils.isBlank(sex))
            {
                if (!sex.equals("M") && !sex.equals("F") && !sex.equals("N"))
                {
                    responseData.setMsgCode("ur.update.sex.illegal");
                    responseData.setMsg(MessageCode.UR_SEX_007.getValue());
                    responseData.setSuccess(false);
                    return responseData;
                }
            }
            //更新性别
            mstUser.setSex(sex);

            //校验密码
            if(StringUtils.isNotEmpty(pwd)){
                /**要求6-20位，合法字符：大小写字母和数字，并且大小写字母和数字都要有*/
                String regex = "(?!^\\\\d+$)(?!^[a-zA-Z]+$)(?!^[_#@]+$).{6,}";
                if (!pwd.matches(regex))
                {
                    responseData.setSuccess(false);
                    responseData.setMsg(MessageCode.UR_PASSWORD_004.getValue());
                    responseData.setMsgCode("ur.register.password.illegal");
                    return responseData;
                }
                //加盐加密
                String encodePassword = MD5EncryptionUtil.getMd5AddSalt(pwd,SALT);
                //更新密码
                mstUser.setPassword(encodePassword);
            }


            if (StringUtils.isNotBlank(birthday))
            {
                DateFormat dateFormat = DateFormat.getDateInstance();
                try {
                    Date date = dateFormat.parse(birthday);
                    //更新生日
                    mstUser.setBirthday(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            //检验邮箱格式
            if (!StringUtils.isBlank(email))
            {
                //String rex = "([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})";
                //邮箱的正则表达式
                String rex  = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
                if (!email.matches(rex))
                {
                    responseData.setMsgCode("ur.update.email.illegal");
                    responseData.setMsg(MessageCode.UR_EMAIL_025.getValue());
                    responseData.setSuccess(false);
                    return responseData;
                }
            }
            //更新邮箱
            mstUser.setEmail(email);

            mstUser.setLabel(label);
            mstUser.setRemark(remark);
            mstUser.setCountry(country);
            mstUser.setRegion(state);
            mstUser.setCity(city);
            mstUser.setDistrict(district);

            if (StringUtils.isNotEmpty(qqOpenId))
            {
                mstUser.setQqOpenId(qqOpenId);
            }
            if (StringUtils.isNotEmpty(wxOpenId))
            {
                mstUser.setWxOpenId(wxOpenId);
            }

            Long userId = hmallMstUser.getUserId();
            mstUser.setUserId(userId);
            hmallMstUserMapper.updateByPrimaryKeySelective(mstUser);

            //更新成功
            responseData.setMsgCode("1");
            responseData.setMsg(MessageCode.UR_UPDATE_012.getValue());
            responseData.setSuccess(true);
            return responseData;
        }
    }

    @Override
    public List<HmallMstUser> selectUserByCondition(Map map)
    {
        HmallMstUser user = new HmallMstUser();
        if(StringUtils.isNotEmpty((String)map.get("mobileNumber")))
        {
            user.setCustomerid(map.get("mobileNumber").toString());
        }
        if(StringUtils.isNotEmpty((String)map.get("name")))
        {
            user.setName(map.get("name").toString());
        }
        return hmallMstUserMapper.select(user);
    }

    @Override
    public List<HmallMstUser> selectByUserIds(List<String> ids) {
        List<HmallMstUser> userList = new ArrayList<>();
        for (String id : ids) {
            HmallMstUser user = hmallMstUserMapper.selectByMobile(id);
            userList.add(user);
        }
        return userList;
    }

    @Override
    public List<HmallMstUser> selectByNotIn(List<String> notIn) {

        return hmallMstUserMapper.selectByNotIn(notIn);
    }

    /**
     * 模糊查询用户信息
     * @param map
     * @return
     */
    @Override
    public List<HmallMstUser> matchUserByCondition(Map map) {
        HmallMstUser user = new HmallMstUser();
        if(StringUtils.isNotEmpty((String)map.get("mobileNumber")))
        {
            user.setCustomerid(map.get("mobileNumber").toString());
        }
        if(StringUtils.isNotEmpty((String)map.get("name")))
        {
            user.setName(map.get("name").toString());
        }
        if(map.get("groups")!=null&&(int)map.get("groups")!=0){
            user.setUserLevel(map.get("groups").toString());
        }
        return hmallMstUserMapper.matchByCondition(user);
    }
}
