package com.hand.promotion.controller;

import com.hand.dto.ResponseData;
import com.hand.promotion.service.ISaleOperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 查询促销/优惠券 操作人员信息
 * <p>
 * Created by shanks on 2017/1/19.
 */
@RestController
@RequestMapping(value = "/h/sale/operator", produces = {MediaType.APPLICATION_JSON_VALUE})
public class SaleOperatorController {

    @Autowired
    private ISaleOperatorService saleOperatorService;

    /**
     * 根据促销活动的编码Id查询促销操作人员信息
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/queryByBaseId", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData queryByBaseId(@RequestBody Map<String, Object> map) {
        ResponseData responseData = saleOperatorService.queryByBaseId(map.get("baseId").toString(),
                Integer.parseInt(map.get("page").toString()), Integer.parseInt(map.get("pageSize").toString()));

        return responseData;
    }
}
