package com.hand.hmall.mapper;

import com.hand.hmall.model.HmallMstProduct;
import tk.mybatis.mapper.common.Mapper;
/**
 * @author 梅新养
 * @name:HmallMstProductMapper
 * @Description:商品信息查询Mapper
 * @version 1.0
 * @date 2017/5/24 14:39
 */
public interface HmallMstProductMapper extends Mapper<HmallMstProduct> {

    /**
     * 根据产品编码查询商品
     *
     * @param code 产品编码
     * @return
     */
    HmallMstProduct selectByCode(String code);

    /**
     * 查询版本为美克Online的商品
     *
     * @param code 商品编码
     * @return
     */
    HmallMstProduct selectMarkorOnlineProductByCode(String code);
}