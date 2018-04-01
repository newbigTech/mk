package com.hand.hap.cloud.hpay.services.pcServices.union.service;

import com.hand.hap.cloud.hpay.data.QueryData;
import com.hand.hap.cloud.hpay.data.ReturnData;

/**
 * @author jiaxin.jiang
 * @Title
 * @Description
 * @date 2017/7/6
 */
public interface IUnionQueryService {

    /**
     * UNION查询
     *
     * @param queryData 查询数据
     * @return ReturnData
     */
    ReturnData unionQuery(QueryData queryData);
}