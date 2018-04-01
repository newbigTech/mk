package com.hand.hmall.service;

import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.model.HmallOmOrder;

import java.util.Map;

/**
 * @author 李伟
 * @version 1.0
 * @name AfterPromoteService
 * @Description 事后促销的返回信息
 * @date 2017/10/18 16:23
 */
public interface IAfterPromoteService {

    /**
     * 事后促销 订单支付后调用
     * @return
     */
    ResponseData checkAfterPromote(HmallOmOrder order);

    /**
     * 事后促销 确认收货后调用
     * @return
     */
    ResponseData checkAfterPromoteWithCofrim(HmallOmOrder order);


    /**
     * 返回事后促销和pin信息
     * @param omOrder
     * @param responseData
     */
    Map returnAfterPromoteAndPinInfo(HmallOmOrder omOrder, ResponseData responseData);

}
