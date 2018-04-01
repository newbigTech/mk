package com.hand.hmall.om.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.om.dto.Order;
import com.hand.hmall.om.dto.OrderSyncDto;
import com.hand.hmall.om.dto.TmData;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zhangmeng
 * @version 0.1
 * @name OrderMapper
 * @description 订单查询页面接口
 * @date 2017/5/22
 */
public interface OrderMapper extends Mapper<Order> {

    /**
     * 订单列表查询
     *
     * @param code            订单编号
     * @param userId          用户ID
     * @param receiverMobile  收货人手机
     * @param startTime       下单开始日期
     * @param endTime         下单结束日期
     * @param strOrderStatus  订单状态
     * @param strDistribution 配送方式
     * @param strOrderTypes   订单类型
     * @param vproduct        变式物料号
     * @param productId       商品编码
     * @param payRate         是否已全部付款
     * @param pinCode         pin码  add by heng.zhang 20170922 MAG-1200  PIN码
     * @param websiteId       网站ID ( "TM" - 天猫 | "1" - 商城 )
     * @param isIo            是否是虚拟订单 ( "Y" | "N" )
     * @param affiliationId   订单归属ID
     * @return
     */
    List<Order> selectOrderList(@Param("code") String code,
                                @Param("escOrderCode") String escOrderCode,
                                @Param("userId") String userId,
                                @Param("locked") String locked,
                                @Param("receiverMobile") String receiverMobile,
                                @Param("startTime") String startTime, @Param("endTime") String endTime,
                                @Param("strOrderStatus") String[] strOrderStatus,
                                @Param("strDistribution") String[] strDistribution,
                                @Param("strOrderTypes") String[] strOrderTypes,
                                @Param("vproduct") String vproduct,
                                @Param("productId") String productId,
                                @Param("payRate") String payRate,
                                @Param("pinCode") String pinCode,
                                @Param("websiteId") String websiteId,
                                @Param("isIo") String isIo,
                                @Param("affiliationId") Long affiliationId);

    /**
     * 订单详情界面查询订单数据
     *
     * @param dto 订单实体类
     * @return List<Order>
     */
    List<Order> selectInfoByOrderId(Order dto);

    /**
     * 订单同步到商城的数据
     *
     * @return
     */
    List<OrderSyncDto> selectOrderSyncInfo();

    /**
     * 根据订单ID查询需要同步到商城的订单数据
     *
     * @param orderId - 订单ID
     * @return
     */
    Order querySyncZmallOrder(@Param("orderId") Long orderId);

    /**
     * 根据订单ID查询需要同步到商城的订单
     *
     * @param orderId - 订单ID
     * @return
     */
    OrderSyncDto selectOrderSyncInfoById(@Param("orderId") Long orderId);

    /**
     * @param orderId - 订单ID
     * @return
     */
    OrderSyncDto selectOrderSyncInfoForAddEntryById(@Param("orderId") Long orderId);

    /**
     * 根据订单状态查询订单
     *
     * @param orderStatus 订单状态编码
     * @return
     */
    List<Order> selectOrderListByOrderStatus(@Param("orderStatus") String orderStatus);

    /**
     * 根据订单ID查询退款单界面信息
     *
     * @param orderId 订单ID
     * @return
     */
    Order selectRefundOrderInfoByOrderId(Long orderId);

    /**
     * 查询待准备（可以进入订单流程）的订单
     *
     * @return List<Order>
     */
    List<Order> selectPendingOrders();

    /**
     * 获取锁定的所有订单信息
     *
     * @return
     */
    List<Order> getLockedOrder();

    /**
     * 锁定订单状态
     *
     * @param orderId - 订单ID
     */
    void lockOrderStatus(@Param("orderId") Long orderId);

    /**
     * 导出天猫订单的发货单
     *
     * @return
     */
    List<TmData> exportTmData(@Param("outTime") Date outTime);

    /**
     * 更新天猫订单的发货推送状态
     *
     * @return
     */
    Integer updateTmData(@Param("outTime") Date outTime);

    /**
     * 根据商城订单编号查询
     *
     * @param escOrderCode - 商城订单编号
     * @return
     */
    List<Order> selectByEscOrderCode(@Param("escOrderCode") String escOrderCode);

    /**
     * 根据指定的订单状态获取订单列表
     * 取订单状态不是NEW_CREATE,PROCESSING_ERROR, TRADE_FINISHED, TRADE_CLOSED, TRADE_CANCELLED的订单
     *
     * @return
     */
    List<Order> selectOrderListByStatus();

    /**
     * 根据平台订单号查询状态为NORMAL的订单表
     *
     * @param escOrderCode
     * @return List<Order>
     */
    List<Order> selectByEscOrderCodeAndOrderType(String escOrderCode);

    /**
     * 根据OrderId获得平台订单编号
     *
     * @param orderId
     * @return
     */
    Order selectOrderByOrderId(Integer orderId);


    /**
     * 判断指定平台订单号且WEBSITE_ID=TM且ORDER_TYPE=NORMAL的记录是否存在
     *
     * @param escOrderCode
     * @return
     */
    List<Order> selectInfoByEscOrderCodeAndWebsiteId(String escOrderCode);

    /**
     * 批量更新订单表
     *
     * @param orderList
     */
    void updateBatchOrder(List<Order> orderList);

    /**
     * 设置订单归属信息
     *
     * @param orderIds   - 订单ID列表
     * @param employeeId - 员工ID
     * @return
     */
    void setOrderAssiging(@Param("orderIds") List<Long> orderIds, @Param("employeeId") Long employeeId);

    /**
     * 查询具有负责人的订单列表
     * item in list : {
     * ORDER_ID: // 订单ID
     * NAME: // 负责人姓名
     * }
     *
     * @return
     */
    List<Map> queryOrderResponsible();

    /**
     * 根据Order表的Code字段查询订单表
     *
     * @param orderCode
     * @return
     */
    List<Order> selectOrderByCode(String orderCode);

    /**
     * 查询订单是否可以保价
     *
     * @param orderId
     * @return
     */
    List<Order> checkInsuredOrder(@Param("orderId") Long orderId);

    /**
     * 查询可以保价的发货单
     *
     * @param orderId
     * @return
     */
    List<Order> selectInsuredOrder(@Param("orderId") Long orderId);

}