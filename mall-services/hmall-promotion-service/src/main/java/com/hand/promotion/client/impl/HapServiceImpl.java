package com.hand.promotion.client.impl;

import com.hand.dto.ResponseData;
import com.hand.promotion.client.IHapService;
import com.hand.promotion.dto.PriceRequestData;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author baihua
 * @version 0.1
 * @name HapServiceImpl$
 * @description $END$
 * @date 2017/12/19$
 */
@Component(value = "hapService")
public class HapServiceImpl implements IHapService {
    @Override
    public ResponseData calculateSalePrice(List<PriceRequestData> priceRequestDataList) {
        return null;
    }
}
