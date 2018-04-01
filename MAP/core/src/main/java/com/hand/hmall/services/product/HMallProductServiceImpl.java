package com.hand.hmall.services.product;


import com.hand.hmall.mst.mapper.ProductMapper;
import com.hand.hmall.services.product.dto.Product;
import com.hand.hmall.services.product.service.IHMallProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author alaowan
 * Created at 2017/12/26 13:50
 * @description
 */
public class HMallProductServiceImpl implements IHMallProductService {

    private static final Logger logger = LoggerFactory.getLogger(HMallProductServiceImpl.class);

    @Autowired
    private ProductMapper productMapper;

    @Value("#{configProperties['catalogsCode']}")
    private String catalogsCode;
    @Value("#{configProperties['catalogversionCode']}")
    private String catalogversionCode;

    @Override
    public List<Product> getProductByVCode(String vcode) {
        List<Product> result = new ArrayList<>();
        HashMap<String, String> paraMap = new HashMap<>();
        paraMap.put("vProductCode", vcode);
        paraMap.put("catalogsCode", catalogsCode);
        paraMap.put("catalogversionCode", catalogversionCode);
        List<com.hand.hmall.mst.dto.Product> list = productMapper.getProductByVcode(paraMap);
        for (com.hand.hmall.mst.dto.Product p : list) {
            Product product = new Product();
            product.setCode(p.getCode());
            product.setName(p.getName());
            product.setWarehouse(p.getWarehouse());
            result.add(product);
        }
        return result;
    }
}
