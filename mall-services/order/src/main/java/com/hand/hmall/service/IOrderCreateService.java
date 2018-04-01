package com.hand.hmall.service;

import com.hand.hmall.model.HmallOmOrder;
import com.hand.hmall.dto.ResponseData;

/**
 * @author 唐磊
 * @Title:
 * @Description:订单下载/更新
 * @date 2017/5/24 14:40
 */
public interface IOrderCreateService {

    /**
     * 订单下载
     * @param order
     * @return
     */
    ResponseData addOrder(HmallOmOrder order);

    /**
     * 订单更新
     * @param order
     * @return
     */
    ResponseData updateOrder(HmallOmOrder order);


    /**
     * 订单下载  根据 平台订单号、网站、渠道、店铺、订单类型(NORMAL) 确定一个唯一订单
     * @param escOrderCode
     * @param websiteId
     * @param channelId
     * @param storeId
     * @return
     */
    HmallOmOrder selectByMutiItems(String escOrderCode, String websiteId, String channelId, String storeId);

    /**
     * 订单更新  根据 平台订单号、网站、渠道、店铺 订单类型(NORMAL) 确定一个唯一订单
     * @param escOrderCde
     * @param websiteId
     * @param channelId
     * @param storeId
     * @return
     */
    HmallOmOrder selectByMutiItemsForUpdate(String escOrderCde, String websiteId, String channelId, String storeId);

}
