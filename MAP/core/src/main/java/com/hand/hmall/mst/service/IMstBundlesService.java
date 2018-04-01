package com.hand.hmall.mst.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.mst.dto.MstBundles;

import java.util.ArrayList;
import java.util.List;

/**
 * @descrption 商品套装产品service
 * Created by heng.zhang04@hand-china.com
 * 2017/8/30
 */
public interface IMstBundlesService extends IBaseService<MstBundles>, ProxySelf<IMstBundlesService> {
    /**
     * 根据SyncFlag查询Bundles
     *
     * @param mstBundles mstBundles
     * @return ArrayList<MstBundles>
     */
    ArrayList<MstBundles> selectBundlesBySyncFlag(MstBundles mstBundles);

    /**
     * 查找计算商品套装数据
     *
     * @param requestContext
     * @param dto
     * @return
     */
    List<MstBundles> selectMappingData(IRequest requestContext, MstBundles dto);

    /**
     * 导入excel数据
     *
     * @param requestContext
     * @param excelList
     */
    void insertAllValue(IRequest requestContext, ArrayList<List<String>> excelList) throws Exception;

    /**
     * 将状态更新为停用，并将数据同步到远端接口
     *
     * @param mstBundlesList
     */
    void batchEndUsing(List<MstBundles> mstBundlesList) throws Exception;

    /**
     * 将状态更新为启说用，并将数据同步到远端接口
     *
     * @param dto
     * @throws Exception
     */
    void batchstartUsing(List<MstBundles> dto) throws Exception;

    /**
     * 对数据的增改操作
     *
     * @param dto
     * @throws Exception
     */
    List<MstBundles> batchUpdateData(List<MstBundles> dto) throws Exception;

    /**
     * 批量删除数据
     *
     * @param dto
     */
    void batchDeleteData(List<MstBundles> dto) throws Exception;
}