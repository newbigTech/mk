package com.hand.hmall.controller;

import com.hand.hmall.dto.Catalogversion;
import com.hand.hmall.dto.HmallMstProduct;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.service.ICatalogversionService;
import com.hand.hmall.service.IProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import java.util.*;

/**
 * @author XinyangMei
 * @Title ProductController
 * @Description 下层产品接口的入口
 * @date 2017/6/28 15:25
 * @version 1.0
 */
@Controller
@RequestMapping("/i/product")
public class ProductController {
    @Resource
    private IProductService iProductService;

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    @RequestMapping("/selectProductByCode/{productCode}")
    @ResponseBody
    public ResponseData selectProductByCode(@PathVariable("productCode") String productCode){
        logger.info(">>>>>>>>>ProductService 根据产品code{}查询产品",productCode);
        ResponseData responseData = new ResponseData();
        HmallMstProduct product = iProductService.selectMKOnlineProductByCode(productCode);
        if(product!=null){
            Map productMap = new HashMap();
            productMap.put("productId",product.getProductId());
            productMap.put("packingVolume",product.getPackingVolume());
            productMap.put("productName",product.getName());
            productMap.put("packageVolUnit",product.getPackingVolUnit());
            productMap.put("defaultDelivery",product.getDefaultDelivery());
            productMap.put("vProductCode",product.getvProductCode());
            productMap.put("grossWeight", product.getGrossWeight());
            productMap.put("weightUnit", product.getWeightUnit());
            productMap.put("warehouse", product.getWarehouse());
            productMap.put("customSupportType", product.getCustomSupportType());
            responseData.setResp(new ArrayList(Arrays.asList(productMap)));
        }
        return responseData;
    }
    @RequestMapping("/selectProductByCodes")
    @ResponseBody
    public ResponseData selectProductByCodes(@RequestBody List codeList){
        ResponseData responseData = new ResponseData();
        List<HmallMstProduct> products = new ArrayList<>();
        for (Object o : codeList) {
            HmallMstProduct product = iProductService.selectMKOnlineProductByCode((String)o);
            products.add(product);
        }
        responseData.setResp(products);
        return responseData;
    }

}
