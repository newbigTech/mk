package com.hand.hmall.service;

import com.hand.hmall.pojo.PriceRequest;
import com.hand.hmall.pojo.PriceRequestData;
import com.hand.hmall.pojo.PriceResponse;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name IPriceCalculateCXFService
 * @description 价格计算CXF服务
 * @date 2017/7/19 15:53
 */
@WebService
public interface IPriceCalculateCXFService {

    /**
     * 价格计算，该方法为最逻辑，因为测试环境仍然为修改，这个这段逻辑再用，
     * 后面会被替换为calculateOrderPrice,calculateSalePrice两个方法
     * @param priceRequest 价格请求数据
     * @return PriceResponse 价格计算结果
     */
    @Deprecated
    @WebMethod
    @WebResult(name = "priceResponse")
    PriceResponse calculatePrice(@WebParam(name = "priceRequest")
                                         PriceRequest priceRequest);

    /**
     * 采购价格计算
     * @param priceRequestDataList 价格请求数据
     * @return PriceResponse 价格计算结果
     */
    @WebMethod
    @WebResult(name = "priceResponse")
    PriceResponse calculateOrderPrice(@WebParam(name = "priceRequest")
                                              List<PriceRequestData> priceRequestDataList);

    /**
     * 销售价格计算
     * @param priceRequestDataList 价格请求数据
     * @return PriceResponse 价格计算结果
     */
    @WebMethod
    @WebResult(name = "priceResponse")
    PriceResponse calculateSalePrice(@WebParam(name = "priceRequest")
                                              List<PriceRequestData> priceRequestDataList);
}
