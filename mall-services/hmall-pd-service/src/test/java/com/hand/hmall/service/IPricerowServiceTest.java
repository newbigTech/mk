package com.hand.hmall.service;

import com.hand.hmall.PdApplication;
import com.hand.hmall.model.Conrel;
import com.hand.hmall.model.Pricerow;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name IPricerowServiceTest
 * @description IPricerowServiceTest
 * @date 2017/7/5 16:25
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PdApplication.class)
public class IPricerowServiceTest {

    @Autowired
    private IPricerowService iPricerowService;

    @Autowired
    private IConrelService iConrelService;

    @Test
    public void testSelectPlatformPricerows() {
//        Conrel conrel = new Conrel();
//        conrel.setPlatform("ZSF051");
//        conrel.setFabric("p1");
        List<Conrel> conrelList = iConrelService.select("ZSF051", "p1");
        System.out.println("=========================================" + conrelList.size());
        Assert.isTrue(conrelList.size() == 2);
    }
}
