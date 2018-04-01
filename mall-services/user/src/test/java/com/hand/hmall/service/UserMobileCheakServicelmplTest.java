package com.hand.hmall.service;

import com.hand.hmall.UserApplication;
import com.hand.hmall.dto.ResponseData;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author 阳赳
 * @Title:
 * @Description:
 * @date 2017/6/5 14:48
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserApplication.class)
public class UserMobileCheakServicelmplTest {
   @Autowired
   private IUserMobileCheakService IUserMobileCheakService;

    @Test
    @Rollback
    public void testCheakMobile(){
        String customerId = "13743232";
       ResponseData responseData = IUserMobileCheakService.check(customerId);
        Boolean test = responseData.isSuccess();
        Assert.assertEquals(false,test);
    }
}
