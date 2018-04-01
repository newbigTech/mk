package com.hand.hmall.service;

import com.hand.hmall.UserApplication;
import com.hand.hmall.dto.HmallMstCustomerCheck;
import com.hand.hmall.mapper.HmallMstCustomerCheckMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author 阳赳
 * @Title:
 * @Description:
 * @date 2017/6/5 15:37
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserApplication.class)
public class UserCreateCheckcodeServiceImplTest {
    @Autowired
    private IUserCreateCheckCodeService IUserCreateCheckcodeService;
    @Resource
    private HmallMstCustomerCheckMapper hmallMstCustomerCheckMapper;

/**该处仅测试插入操作 下次测试需要更换mobileNumber 更新操作写入Controller层*/
    @Test
    @Rollback
    public void testValidate(){
        String mobileNumber = "15088888888";
        String sendType = "1";
        IUserCreateCheckcodeService.insertOracle(mobileNumber,sendType);
        HmallMstCustomerCheck hmallMstCustomerCheck= hmallMstCustomerCheckMapper.selectByMobileAndSendType(mobileNumber,sendType);
        String checkcode=hmallMstCustomerCheck.getCheckcode();
        Assert.assertNotEquals("",checkcode);
    }
}
