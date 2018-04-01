package com.hand.promotion.dubboService;

import com.alibaba.dubbo.config.annotation.Service;

import com.hand.dto.ResponseData;
import com.hand.hpromotion.ISaleOperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 查询促销/优惠券 操作人员信息
 * <p>
 * Created by darkdog on 2018/2/2.
 */
public class SaleOperatorService implements ISaleOperatorService {

    @Autowired
    private com.hand.promotion.service.ISaleOperatorService saleOperatorService;

    /**
     * 根据促销活动的编码Id查询促销操作人员信息
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData queryByBaseId(Map<String, Object> map) {
        ResponseData responseData = saleOperatorService.queryByBaseId(map.get("baseId").toString(),
                Integer.parseInt(map.get("page").toString()), Integer.parseInt(map.get("pageSize").toString()));

        return responseData;
    }


}
