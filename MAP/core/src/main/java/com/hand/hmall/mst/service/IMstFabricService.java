package com.hand.hmall.mst.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.mst.dto.MstFabric;

import java.io.IOException;
import java.util.List;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name MstFabricController
 * @description 面料等级
 * @date 2017年5月26日10:52:23
 */
public interface IMstFabricService extends IBaseService<MstFabric>, ProxySelf<IMstFabricService> {

    /**
     * 推送给M3D
     * @param mstFabricList
     * @throws IOException
     */
    void postToM3D(List<MstFabric> mstFabricList) throws IOException;

    /**
     * @description 导入面料等级时，验证数据的正确性
     * @param request
     * @param list
     * @return
     */
    String checkFabric(IRequest request, List<MstFabric> list);
}