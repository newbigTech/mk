package com.hand.hmall.mapper;

import com.hand.hmall.PdApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author 马君
 * @version 0.1
 * @name OdtypeMapperTest
 * @description OdtypeMapperTest
 * @date 2017/8/22 17:13
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PdApplication.class)
public class OdtypeMapperTest {

    @Autowired
    private OdtypeMapper odtypeMapper;

    @Test
    public void testSelectAll() {
        System.out.println("===================" + odtypeMapper.selectAll().size());
    }
}
