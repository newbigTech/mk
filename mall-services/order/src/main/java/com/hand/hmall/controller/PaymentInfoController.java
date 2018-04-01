package com.hand.hmall.controller;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.mapper.HmallOmPaymentInfoMapper;
import com.hand.hmall.model.HmallOmPaymentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * @author XinyangMei
 * @Title PaymentInfoController
 * @Description 用户购买优惠券支付信息插入接口
 * @date 2017/7/31 15:11
 */
@RestController
@RequestMapping("/p/paymentInfo")
public class PaymentInfoController {
    @Autowired
    private HmallOmPaymentInfoMapper hmallOmPaymentInfoMapper;

    @RequestMapping("/insertPayment")
    public ResponseData insertPaymentInfo(@RequestBody Map map) {
        HmallOmPaymentInfo paymentInfo = new HmallOmPaymentInfo();
        paymentInfo.setCouponId((String)map.get("couponId"));
        paymentInfo.setPayMode((String)map.get("payModel"));
        paymentInfo.setPayAmount(Double.parseDouble((String)map.get("payAmount")));
        paymentInfo.setPayTime(new Date((String)map.get("payTime")));
        paymentInfo.setNumberCode((String)map.get("numberCode"));
        int result = hmallOmPaymentInfoMapper.insertSelective(paymentInfo);
        ResponseData responseData = new ResponseData();
        if(result>0){
            responseData.setSuccess(true);
            responseData.setResp(Arrays.asList(result));
        }else {
            responseData.setSuccess(false);
            responseData.setMsg("插入支付信息失败");
        }
        return responseData;
    }
}
