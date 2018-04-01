package com.hand.hmall.service;

import com.hand.hmall.UserApplication;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.NoSuchAlgorithmException;

/**
 * @author 阳赳
 * @Title:
 * @Description:
 * @date 2017/6/7 16:36
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserApplication.class)
public class UserServiceImplTest {
    @Autowired
    private IUserService IUserService;

/**登录方法验证 利用已经注册的账号和密码登录*/
    @Test
    @Rollback
    public void testValidate(){
        String mobileNumber="15096322208";
        String password = "Qq122143257";
        Boolean b = false;
        try {
           b = IUserService.validate(mobileNumber,password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(true,b);
    }
}
