package com.hand.hmall.mapper;


import com.hand.hmall.dto.MstBundlesMapping;
import tk.mybatis.mapper.common.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @descrption 商品套装产品产品映射mapper
 * Created by heng.zhang04@hand-china.com
 * 2017/8/30
 */
public interface MstBundlesMappingMapper extends Mapper<MstBundlesMapping> {


    ArrayList<MstBundlesMapping> selectBundlesMappingByBundlesId(MstBundlesMapping mstBundlesMapping);

    /**
     * 查找商品套装对应关系下的商品
     *
     * @param dto
     * @return
     */
    List<MstBundlesMapping> selectProduct(MstBundlesMapping dto);

    /**
     * 通过商品id查找商品对应价格
     *
     * @param mstBundlesMapp
     * @return
     */
    List<MstBundlesMapping> queryPrice(MstBundlesMapping mstBundlesMapp);
}