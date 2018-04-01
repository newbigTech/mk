package com.hand.hmall.mapper;

import com.hand.hmall.model.HmallOmOrder;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
/**
 * @author 梅新养
 * @name:HmallOmOrderMapper
 * @Description:订单查询Mapper
 * @version 1.0
 * @date 2017/5/24 14:39
 */
public interface HmallOmOrderMapper extends Mapper<HmallOmOrder> {
    /**
     * 根据订单编号查询订单
     * @param code 订单编号
     * @return
     */
    HmallOmOrder selectByCode(String code);

    /**
     * 订单下载  根据 平台订单号、网站、渠道、店铺 确定一个唯一订单
     * @param escOrderCode 订单平台编号
     * @param websiteId 网站
     * @param salechannelId 渠道
     * @param storeId 店铺
     * @return
     */
    HmallOmOrder selectByMutiItems(@Param(value = "escOrderCode") String escOrderCode, @Param(value = "websiteId") String websiteId, @Param(value = "salechannelId") String salechannelId, @Param(value = "storeId") String storeId);


    /**
     * 订单更新  根据 平台订单号、网站、渠道、店铺 订单类型(NORMAL) 确定一个唯一订单
     * @param escOrderCode
     * @param websiteId
     * @param salechannelId
     * @param storeId
     * @return
     */
    HmallOmOrder selectByMutiItemsForUpdate(@Param(value = "escOrderCode") String escOrderCode, @Param(value = "websiteId") String websiteId, @Param(value = "salechannelId") String salechannelId, @Param(value = "storeId") String storeId);
}