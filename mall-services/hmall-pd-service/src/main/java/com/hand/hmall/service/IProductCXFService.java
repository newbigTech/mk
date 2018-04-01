package com.hand.hmall.service;

import com.hand.hmall.pojo.PdResponse;
import com.hand.hmall.pojo.ProductData;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name IProductCXFService
 * @description 商品CXF服务
 * @date 2017/6/23 9:05
 */
@WebService
public interface IProductCXFService {

    /**
     * retail推商品
     * @param productDatas 商品数据
     * @return PdResponse
     */
    @WebMethod
    @WebResult(name = "responseData")
    PdResponse reveiveProductsFromRetail(
            @WebParam(name = "productData") List<ProductData> productDatas);
}
