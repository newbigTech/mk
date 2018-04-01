package com.hand.hmall.service;

import com.hand.hmall.UserApplication;
import com.hand.hmall.dto.HmallMstUser;
import com.hand.hmall.exception.UserUpdateHistoryException;
import com.hand.hmall.mapper.HmallMstUserMapper;
import org.apache.commons.collections.map.HashedMap;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author 阳赳
 * @Title:
 * @Description:
 * @date 2017/5/31 17:09
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserApplication.class)
public class UserInfoServiceImplTest {

    @Autowired
    private IUserInfoService IUserInfoService;

    @Resource
    private HmallMstUserMapper hmallMstUserMapper;

    @Test
    @Rollback
    public void testValidate() {
      /*{
            "mobileNumber":"13755086724",
                "sex"    :"M",
                "name" : "孙老师",
                "birthday":"213",
                "country" :"中国",
                "state"  : "湖南省",
                "city" :"长沙市",
                "district" :"岳麓区",
                "email" :"1062839320@qq.com"
        }*/
/**用户信息编辑此处更改邮箱进行方法验证*/
        Map<String, Object> params = new HashedMap();
        params.put("mobileNumber", "13735185554");
        params.put("sex", "M");
        params.put("name", "王");
        params.put("birthday", "816");
        params.put("country", "中国");
        params.put("state", "湖南省");
        params.put("city", "长沙市");
        params.put("district", "岳麓区");
        params.put("email", "15874230269@qq.com");
        try {
            IUserInfoService.update(params);
        } catch (Exception e) {
            //e.printStackTrace();
        }
       HmallMstUser hmallMstUser = hmallMstUserMapper.selectByCustomerId((String) params.get("mobileNumber"));
       String email = hmallMstUser.getEmail();
       Assert.assertEquals("15874230269@qq.com",email);
    }
}
