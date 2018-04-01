package com.hand.hmall.mapper;

import com.hand.hmall.dto.AfterSaleOrder;
import com.hand.hmall.dto.ServiceOrder;
import com.hand.hmall.dto.ServiceOrderEntry;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @name ServiceOrderMapper
 * @Describe 服务单持久层接口
 * @Author chenzhigang
 * @Date 2017/7/23
 */
public interface ServiceOrderMapper extends Mapper<ServiceOrder> {

    /**
     * 根据订单ID查询服务单列表
     *
     * @param orderId - 订单ID
     * @return
     */
    List<ServiceOrder> queryByOrderId(@Param("orderId") long orderId);

    /**
     * 根据服务单ID查询服务单行
     *
     * @param serviceOrderId - 服务单ID
     * @return
     */
    List<ServiceOrderEntry> selectServiceOrderEntries(@Param("serviceOrderId") long serviceOrderId);

    /**
     * 根据服务单ID查询售后单
     *
     * @param serviceOrderId - 服务单ID
     * @return
     */
    List<AfterSaleOrder> selectAfterSaleOrders(@Param("serviceOrderId") long serviceOrderId);

    /**
     * 根据订单单号查询对应的全部服务单
     *
     * @param orderCode - 订单单号
     * @return
     */
    List<ServiceOrder> queryByOrderCode(@Param("orderCode") String orderCode);

    /**
     * 根据订单单号查询订单
     *
     * @param orderCode
     * @return
     */
    Map<String, Object> queryOrderByCode(@Param("orderCode") String orderCode);

    /**
     * 查询服务单对应的媒体信息列表
     *
     * @param so
     * @return
     */
    List<Map> selectMediaURLs(ServiceOrder so);

    /**
     * 根据订单编号查询服务单列表
     *
     * @param orderCode
     * @return
     */
    List<ServiceOrder> selectByOrderCode(@Param("orderCode") String orderCode);

    /**
     * 根据escOrderCode和外部网站名称查询对应的服务单列表
     *
     * @param escOrderCode
     * @param webDisplay   - 外部网站名称
     * @return
     */
    List<ServiceOrder> queryWebsiteServiceOrder(@Param("escOrderCode") String escOrderCode, @Param("webDisplay") String webDisplay);

    /**
     * 根据用户ID查询用户信息，以Map结构返回
     *
     * @param userId
     * @return
     */
    Map queryUserInfoByUserId(@Param("userId") String userId);

    /**
     * 根据escOrderCode查询订单信息
     *
     * @param escOrderCode
     * @return
     */
    Map queryByEscOrderCode(@Param("escOrderCode") String escOrderCode);

    /**
     * 根据PIN码查询订单行相关信息
     * @param pin
     * @return
     */
    Map queryOrderEntryInfoByPin(@Param("pin") String pin);
}
