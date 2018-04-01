package com.hand.hmall.service.impl;

import com.google.gson.Gson;
import com.hand.hmall.dto.*;
import com.hand.hmall.mapper.HmallMstUserGroupMapper;
import com.hand.hmall.mapper.HmallMstUserMapper;
import com.hand.hmall.mapper.HmallMstUserMappingMapper;
import com.hand.hmall.service.IUserRegisterService;
import com.hand.hmall.util.MD5EncryptionUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


/**
 * @author 唐磊
 * @name:UserRegisterServiceImpl
 * @Description:实现用户注册相关逻辑
 * @date 2017/5/23 19:10
 * @version:1.0
 */
@Service("IUserRegisterService")
public class UserRegisterServiceImpl implements IUserRegisterService
{
    @Resource
    private HmallMstUserMapper hmallMstUserMapper;

    @Resource
    private HmallMstUserMappingMapper hmallMstUserMappingMapper;

    @Resource
    private HmallMstUserGroupMapper hmallMstUserGroupMapper;

    @Value("${data.salt}")
    private String SALT;

    /**
     * 校验手机号
     *
     * @param mobileNumber 手机号码 值与用户id一致
     * @return
     */
    @Override
    public String check(String mobileNumber)
    {
        //手机号不能为空
        if (StringUtils.isEmpty(mobileNumber))
        {
            return "UR_MOBILE_04";
        }
        //手机号码检验正则表达式
        String regex = "^((13[0-9])|(14[5,6,7,9])|(15[^4,\\D])|(16[6])|(17[0,1,3,5-8])|(18[0-9]))\\d{8}$";
        if (mobileNumber.matches(regex))
        {
            //校检手机号是否已存在
            HmallMstUser hmallMstUser = hmallMstUserMapper.selectByCustomerId(mobileNumber);
            if (null == hmallMstUser)
            {
                return MsgCode.UR_MOBILE_01;  //手机号合法且未被使用
            }
            else
            {
                return MsgCode.UR_MOBILE_03; //手机号已存在
            }
        }
        else
        {
            return MsgCode.UR_MOBILE_02;  //手机号不合法
        }
    }


    /**
     * 实现注册逻辑
     *
     * @param map
     * @throws NoSuchAlgorithmException
     */
    public void register(Map<String, Object> map) throws NoSuchAlgorithmException
    {
        //获取前台输入的密码
        String password = (String) map.get("password");
        //调用MD5公共类对密码进行MD5加盐加密
        String encodePassword = MD5EncryptionUtil.getMd5AddSalt(password,SALT);

        HmallMstUser hmallMstUser = new HmallMstUser();
        hmallMstUser.setCustomerid((String) map.get("mobileNumber"));
        hmallMstUser.setSex((String) map.get("sex"));
        hmallMstUser.setRemark((String) map.get("remark"));
        hmallMstUser.setMobileNumber((String) map.get("mobileNumber"));
        hmallMstUser.setName((String) map.get("name"));

        if (StringUtils.isNotEmpty((String) map.get("qqOpenId")))
        {
            hmallMstUser.setQqOpenId((String) map.get("qqOpenId"));
        }
        if (StringUtils.isNotEmpty((String) map.get("wxOpenId")))
        {
            hmallMstUser.setWxOpenId((String) map.get("wxOpenId"));
        }
        String birthStr = (String) map.get("birthday");
        if (StringUtils.isNotBlank(birthStr))
        {
            Date birthday = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                //对生日进行格式化
                birthday = sdf.parse(birthStr);
                hmallMstUser.setBirthday(birthday);
            } catch (ParseException e) {
                throw new IllegalArgumentException("时间格式错误");
            }
        }
        hmallMstUser.setCountry((String) map.get("country"));
        hmallMstUser.setRegion((String) map.get("state"));
        hmallMstUser.setCity((String) map.get("city"));
        hmallMstUser.setDistrict((String) map.get("district"));
        hmallMstUser.setPassword(encodePassword);
        hmallMstUser.setLabel((String) map.get("label"));
        hmallMstUser.setEmail((String) map.get("email"));
        hmallMstUser.setUserLevel("1");

        System.out.println("===========================" + new Gson().toJson(hmallMstUser));
        hmallMstUserMapper.insertSelective(hmallMstUser);
        System.out.println("===========================" + new Gson().toJson(hmallMstUser));

        //获取userId
        String mobileNumber = (String) map.get("mobileNumber");
        Long userId = hmallMstUserMapper.selectByCustomerId(mobileNumber).getUserId();

        String userGroupCode = "customerGroup";
        HmallMstUserGroup hmallMstUserGroup = hmallMstUserGroupMapper.selectBycode(userGroupCode);
        //用户组表如果不存在会员组就再用户组表中新增该条数据
        if (hmallMstUserGroup == null)
        {
            HmallMstUserGroup userGroup = new HmallMstUserGroup();
            userGroup.setCode(userGroupCode);
            userGroup.setName("会员组");
            userGroup.setLastUpdateDate(new Date());
            hmallMstUserGroupMapper.insertSelective(userGroup);
            hmallMstUserGroup = hmallMstUserGroupMapper.selectBycode(userGroupCode);
        }

        Long userGroupId = hmallMstUserGroup.getUsergroupId();
        HmallMstUserMapping hmallMstUserMapping = new HmallMstUserMapping();
        hmallMstUserMapping.setUsergroupId(userGroupId);
        hmallMstUserMapping.setUserId(userId);
        hmallMstUserMappingMapper.insertSelective(hmallMstUserMapping);
    }
}
