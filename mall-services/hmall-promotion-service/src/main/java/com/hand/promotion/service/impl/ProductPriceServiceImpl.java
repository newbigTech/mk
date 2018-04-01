package com.hand.promotion.service.impl;

import com.hand.dto.ResponseData;
import com.hand.promotion.client.IHapService;
import com.hand.promotion.client.IPdClientService;
import com.hand.promotion.dto.PriceRequestData;
import com.hand.promotion.pojo.order.OrderPojo;
import com.hand.promotion.service.IProductPriceService;
import com.hand.promotion.util.ResponseReturnUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2018/2/6
 * @description
 */
@Service
public class ProductPriceServiceImpl implements IProductPriceService {


    @Autowired
    private IPdClientService pdClientService;

    @Autowired
    private IHapService hapService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 获取订单行商品的真实价格,将订单行参与促销计算的calPrice字段值置为真实金额
     *
     * @param order 要获取真实价格的订单数据
     */
    @Override
    public void getRealPrice(OrderPojo order) {
        order.getOrderEntryList().forEach(entryPojo -> {
            String customSupportType = entryPojo.getCustomSupportType();
            Double realPrice = 0.0;
            if (StringUtils.isNotEmpty(customSupportType)) {
                //非订制品
                if ("1".equals(customSupportType) || "3".equals(customSupportType)) {
                    ResponseData productPrice = pdClientService.getProductPrice(Integer.parseInt(entryPojo.getProductId()));
                    Map price = (Map) ResponseReturnUtil.getRespObj(productPrice);
                    realPrice = (Double) price.get("basePrice");
                } else if ("2".equals(customSupportType)) {
                    //霸王券逻辑 订制品
                    realPrice = getHapCalculateSalePrice(entryPojo.getVproduct(), entryPojo.getOdtype());
                }
            }
            entryPojo.setRealPrice(realPrice);
            entryPojo.setCalPrice(realPrice);
        });
    }


    /**
     * V码获取价格
     *
     * @param vcode
     * @return
     */
    public Double getHapCalculateSalePrice(String vcode, String odtype) {
        List<PriceRequestData> priceRequestDataList = new ArrayList<PriceRequestData>();
        PriceRequestData p = new PriceRequestData();
        p.setvCode(vcode);
        //暂时写死是1 默认订制品  2的超级定制有待解决
        p.setOdtype(odtype);
        priceRequestDataList.add(p);
        ResponseData responseData = hapService.calculateSalePrice(priceRequestDataList);
        Map price = (Map) ResponseReturnUtil.getRespObj(responseData);
        return (Double) price.get("totalPrice");
    }

}
