package com.hand.hmall.mst.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.mst.dto.MstBundlesMapping;

import java.util.ArrayList;
import java.util.List;

public interface IMstBundlesMappingService extends IBaseService<MstBundlesMapping>, ProxySelf<IMstBundlesMappingService> {
    ArrayList<MstBundlesMapping> selectBundlesMappingByBundlesId(MstBundlesMapping mstBundlesMapping);

    /**
     * 查找商品套装中对应数据
     *
     * @param requestContext
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    List<MstBundlesMapping> selectProduct(IRequest requestContext, MstBundlesMapping dto, int page, int pageSize);

    /**
     * 套装组成的提交
     * @param dto
     * @param requestCtx
     * @throws Exception
     */
    List<MstBundlesMapping> batchUpdateMappingData(List<MstBundlesMapping> dto, IRequest requestCtx) throws Exception;

    /**
     * 批量删除
     * @param dto
     * @throws Exception
     */
    void batchDeleteMappnigData(List<MstBundlesMapping> dto)throws Exception;
}