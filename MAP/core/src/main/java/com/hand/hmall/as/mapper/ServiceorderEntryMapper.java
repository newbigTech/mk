package com.hand.hmall.as.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.as.dto.ServiceorderEntry;

import java.util.List;

/**
 * @author zhangmeng
 * @version 0.1
 * @name ServiceorderEntryMapper
 * @description 服务单行mapper
 * @date 2017/7/19
 */
public interface ServiceorderEntryMapper extends Mapper<ServiceorderEntry> {


    /**
     * 根据服务单CODE查询售后单信息
     *
     * @param dto
     * @return
     */
    List<ServiceorderEntry> queryServiceOrderInfo(ServiceorderEntry dto);

    /**
     * 根据退款单ID查询其对应的全部服务单行
     *
     * @param serviceOrderId - 退款单ID
     * @return
     */
    List<ServiceorderEntry> selectRefundOrderEntry(Long serviceOrderId);

    /**
     * 根据派工单ID查询其对应的全部服务单行
     *
     * @param serviceOrderId - 退款单ID
     * @return
     */
    List<ServiceorderEntry> selectDispatchOrderEntry(Long serviceOrderId);

    /**
     * 根据服务单id查询退货单对应的所有服务单行
     *
     * @param dto
     * @return
     */
    List<ServiceorderEntry> queryReturnOrder(ServiceorderEntry dto);

    /**
     * 获取服务单对应的订单行排除不想要的产品
     * @param dto
     * @return
     */
    List<ServiceorderEntry> getServiceOrderListExcludeProductId(ServiceorderEntry dto);

    /**
     * @description 创建退换货界面中，通过勾选的服务单行来查找商品编码，商品名称以及数量
     * @param serviceorderEntryList
     * @return
     */
    List<ServiceorderEntry> selectByOrderServiceOrderEntryIdList(List<ServiceorderEntry> serviceorderEntryList);
}