package com.hand.hmall.mapper;

import com.hand.hmall.model.HmallMstBaseStore;
import tk.mybatis.mapper.common.Mapper;
/**
 * @author 梅新养
 * @name:HmallMstBaseStoreMapper
 * @Description:基础店铺信息查询Mapper
 * @version 1.0
 * @date 2017/5/24 14:39
 */
public interface HmallMstBaseStoreMapper extends Mapper<HmallMstBaseStore> {
    /**
     * 根据店铺编码code查询店铺信息
     *
     * @param storeCode 店铺编码
     * @return
     */
    HmallMstBaseStore selectStoreByStoreCode(String storeCode);
}