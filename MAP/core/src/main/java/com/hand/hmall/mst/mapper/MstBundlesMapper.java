package com.hand.hmall.mst.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.mst.dto.MstBundles;

import java.util.ArrayList;

/**
 * @descrption 商品套装产品mapper
 * Created by heng.zhang04@hand-china.com
 * 2017/8/30
 */
public interface MstBundlesMapper extends Mapper<MstBundles> {

    /**
     * 根据SyncFlag查询Bundles
     *
     * @param mstBundles mstBundles
     * @return ArrayList<MstBundles>
     */
    ArrayList<MstBundles> selectBundlesBySyncFlag(MstBundles mstBundles);
}