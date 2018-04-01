package com.hand.hmall.services.ws;

import com.hand.hmall.ws.entities.MstFabricRequstBody;
import com.hand.hmall.ws.entities.MstFabricResponseBody;

/**
 * @author chenzhigang
 * @version 0.1
 * @name IRetailFabricServer4Retail
 * @description hmall提供的获取Retial面料等级信息（Dubbo）接口，map-api-adapter调用
 * @date 2018/1/4
 */
public interface IRetailFabricServer4Retail {

    /**
     * 获取面料（等级）信息
     *
     * @param request 面料（等级）信息
     * @return
     */
    MstFabricResponseBody getFabricInfo(MstFabricRequstBody request);

}
