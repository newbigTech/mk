package com.hand.hmall.service.impl;

import com.hand.hmall.dto.HmallMstUser;
import com.hand.hmall.mapper.HmallMstUserMapper;
import com.hand.hmall.service.IUserService;
import com.hand.hmall.util.MD5EncryptionUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.security.NoSuchAlgorithmException;

/**
 * @author 唐磊
 * @name:UserServiceImpl
 * @Description:用户相关业务逻辑
 * @date 2017/5/23 19:11
 */
@Service("IUserService")
public class UserServiceImpl implements IUserService {

    @Value("${data.salt}")
    private String SALT;

    @Resource
    private HmallMstUserMapper hmallMstUserMapper;

    /**
     * 根据用户名查询用户
     * @param mobileNumber 用户名
     * @return
     */
    @Override
    public HmallMstUser selectByCustomerId(String mobileNumber)
    {
        return hmallMstUserMapper.selectByCustomerId(mobileNumber);
    }

    /**
     * 登陆校验
     * @param mobileNumber 手机号
     * @param pwd 密码
     * @return
     * @throws NoSuchAlgorithmException
     */
    @Override
    public Boolean validate(String mobileNumber, String pwd) throws NoSuchAlgorithmException
    {
        //根据手机号获取一个user
        HmallMstUser user = hmallMstUserMapper.selectByCustomerId(mobileNumber);
        //调用公共MD5加密算法对密码进行MD5加盐加密
        String encodePassword = MD5EncryptionUtil.getMd5AddSalt(pwd,SALT);

        //判断密码是否一致
        if (encodePassword.equals(user.getPassword()))
        {
            return true;
        }
        return false;
    }
}
