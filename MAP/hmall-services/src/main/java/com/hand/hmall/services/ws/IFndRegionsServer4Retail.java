package com.hand.hmall.services.ws;

import com.hand.hmall.ws.entities.RegionsRequestBody;
import com.markor.map.external.fndregionservice.dto.RegionResultModel;

/**
 * @author chenzhigang
 * @version 0.1
 * @name IFndRegionsServer4Retail
 * @Description retail推送地区表信息至hmall的接口类
 * @date 2018
 */
public interface IFndRegionsServer4Retail {

    RegionResultModel getFndRegionsFromRetail(RegionsRequestBody r);

}

