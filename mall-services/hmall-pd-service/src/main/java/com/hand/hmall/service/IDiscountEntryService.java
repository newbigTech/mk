package com.hand.hmall.service;

import com.hand.hmall.model.DiscountEntry;
import com.hand.hmall.pojo.PriceRequestData;

/**
 * @author zhangmeng
 * @version 0.1
 * @name IDiscountEntryService
 * @description 价格折扣行
 * @date 2017/11/27
 */
public interface IDiscountEntryService extends IBaseService<DiscountEntry> {

    /**
     * 计算折扣价
     *
     * @param priceRequestData
     * @return
     */
    DiscountEntry discountPrice(PriceRequestData priceRequestData, Double totalPrice, String flag);

}