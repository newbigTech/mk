package com.hand.hmall.mapper;

import com.hand.hmall.PdApplication;
import com.hand.hmall.model.Product;
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
 * @name ProductMapperTest
 * @description ProductMapperTest
 * @date 2017/6/30 13:19
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PdApplication.class)
public class ProductMapperTest {

    @Autowired
    private ProductMapper productMapper;

    @Test
    public void testSelectAll() {
        Product p = new Product();
        p.setCode("13579");
        List<Product> productList = productMapper.select(p);
        System.out.println(">>>>>>>>>>>>>>>>>" + productList);
        System.out.println(">>>>>>>>>>>>>>>>>" + productList.size());
    }
}
