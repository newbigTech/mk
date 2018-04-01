package com.hand.hmall.service;

import com.hand.hmall.UserApplication;
import com.hand.hmall.dto.HmallMstUser;
import com.hand.hmall.mapper.HmallMstUserMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 阳赳
 * @Title:
 * @Description:
 * @date 2017/6/5 16:37
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserApplication.class)

public class UserRegisterServiceImplTest {

    @Autowired
    private IUserRegisterService IUserRegisterService;
    @Resource
    private HmallMstUserMapper hmallMstUserMapper;
   /* {
        "mobileNumber":"15616395516",
            "password" :"Qq122143257",
            "sex"    :"M",
            "remark":"备注信息",
            "name" : "阳赳",
            "birthday":"213",
            "country" :"中国",
            "state"  : "湖南省",
            "city" :"长沙市",
            "district" :"岳麓区",
            "label"  :"34",
            "email" :"1062839320@qq.com",
            "userGroupCode" :"9831",
            "checkcode":"BO4D"
    }
*/
    /**下次测试需要用手机号先生成验证码 并保证手机号未被注册 */
    /**手机号 和密码 和验证码为必填字段*/
    @Test
    @Rollback
    public void testValidate() {
    Map params = new HashMap();
        params.put("mobileNumber", "15088888888");
        params.put("password","Qq122143257");
        params.put("sex", "M");
        params.put("remark","备注信息");
        params.put("name", "阳赳");
        params.put("birthday", "213");
        params.put("country", "中国");
        params.put("state", "湖南省");
        params.put("city", "长沙市");
        params.put("district", "岳麓区");
        params.put("label","34");
        params.put("email", "15874230269@qq.com");
        params.put("checkcode","F7KZ");
        try {
            IUserRegisterService.register(params);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        HmallMstUser hmallMstUser=hmallMstUserMapper.selectByMobile((String) params.get("mobileNumber"));
        Assert.assertNotEquals("",hmallMstUser);
    }
}
