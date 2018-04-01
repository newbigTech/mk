package com.hand.hmall.services.as.service;

import com.hand.hmall.services.as.dto.AsReturnBack;
import com.hand.hmall.services.as.dto.AsReturnForRetail;

import java.util.List;

/**
 * @author liuhongxi
 * @version 0.1
 * @name IAsReturnServiceForRetail
 * @description 退回单Service
 * @date 2017/5/24
 */
public interface IAsReturnServiceForRetail {

    /**
     * 通过sap单号更新退货单状态
     *
     * @param arl
     * @return
     */
    List<AsReturnBack> updateStatus(List<AsReturnForRetail> arl);


}