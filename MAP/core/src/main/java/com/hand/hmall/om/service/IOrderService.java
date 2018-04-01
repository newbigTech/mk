package com.hand.hmall.om.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.om.dto.*;
import com.hand.hmall.om.tpl.TmallOrderTemplate;

import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @author zhangmeng
 * @version 0.1
 * @name IOrderService
 * @description 订单查询页面
 * @date 2017/5/22
 */
public interface IOrderService extends IBaseService<Order>, ProxySelf<IOrderService> {
    /**
     * 订单列表查询
     *
     * @param page
     * @param pageSize
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
     * @param payRate         是否全部付款
     * @param pinCode         pin码
     * @param websiteId       网站ID ( "TM" - 天猫 | "1" - 商城 )
     * @param isIo            是否是虚拟订单 ( "Y" | "N" )
     * @param affiliationId   订单归属ID
     * @return
     */
    List<Order> selectOrderList(
            int page, int pageSize, String code, String escOrderCode, String userId, String locked,
            String receiverMobile, String startTime, String endTime, String[] strOrderStatus, String[] strDistribution,
            String[] strOrderTypes, String vproduct, String productId, String payRate, String pinCode,
            String websiteId, String isIo, Long affiliationId);

    /**
     * 订单列表查询 不带分页
     *
     * @param code            订单编号
     * @param userId          用户ID
     * @param receiverMobile  收货人手机
     * @param startTime       下单开始日期
     * @param endTime         下单结束日期
     * @param strOrderStatus  订单状态
     * @param strDistribution 配送方式
     * @param vproduct        变式物料号
     * @param productId       商品编码
     * @return
     */
    List<Order> selectOrderList(IRequest iRequest, String code, String userId,
                                String receiverMobile, String startTime, String endTime,
                                String[] strOrderStatus, String[] strDistribution, String vproduct, String productId, String pinCode);

    /**
     * 订单详情界面查询订单数据
     *
     * @param dto 订单实体类
     * @return List<Order>
     */
    List<Order> selectInfoByOrderId(Order dto);

    /**
     * 根据订单状态查询订单
     *
     * @param orderStatus 订单状态
     * @return List<Order>
     */
    List<Order> selectByStatus(String orderStatus);

    /**
     * 根据订单ID查询退款单界面信息
     *
     * @param orderId 订单Id
     * @return
     */
    List<Order> selectRefundOrderInfoByOrderId(Long orderId);

    /**
     * 查询待处理（存在订单行未关联发货单且未锁定）的订单
     *
     * @return List<Order>
     */
    List<Order> selectPendingOrders();

    /**
     * 获取订单的可备货金额
     *
     * @param order 订单
     * @return Double 可备货金额
     */
    Double getStockUpAmount(Order order);

    /**
     * 订单同步到商城的数据
     *
     * @return
     */
    List<OrderSyncDto> selectOrderSyncInfo();

    /**
     * 获取锁定的所有订单信息
     *
     * @return
     */
    List<Order> getLockedOrder();

    /**
     * 根据订单ID查询需要同步到商城的订单数据
     *
     * @param orderId - 订单ID
     * @return
     */
    OrderSyncDto querySyncZmallOrder(Long orderId);

    /**
     * @param orderId -订单ID
     * @return
     */
    OrderSyncDto querySyncZmallOrderForAddEntry(Long orderId);

    /**
     * 锁定订单状态
     *
     * @param orderId - 订单ID
     */
    void lockOrderStatus(Long orderId);

    /**
     * 返回Zmall商城地址
     *
     * @return
     */
    String getZmallAddress();

    /**
     * 返回zmall商城网站地址
     *
     * @return
     */
    String getZmallWebsiteAddress();

    /**
     * 订单详情界面保存按钮
     *
     * @param iRequest
     * @param order
     */
    void saveOrderFunc(IRequest iRequest, Order order);

    /**
     * 导出天猫订单的发货单
     *
     * @return
     */
    List<TmData> exportTmData();

    /**
     * 导出天猫订单的历史发货单
     *
     * @return
     */
    List<TmData> exportTmDataHis(Date date);

    /**
     * 获取配置文件中的属性值
     *
     * @return Properties
     */
    public Properties getProperties();

    /**
     * 导入天猫订单数据
     *
     * @param iRequest
     * @param tmallOrders
     */
    void importTmallOrderData(IRequest iRequest, List<TmallOrderTemplate> tmallOrders);

    /**
     * 重新加载订单的所有数据
     *
     * @param order 订单(reload后会被更新)
     * @return Order 重新加载的订单
     */
    Order reload(Order order);

    /**
     * @param request
     * @param order
     * @return
     * @description 订单取消时，占用优惠券后在订单头中存入选择的优惠券id和促销id
     * 并且更新订单同步官网状态为N，将订单下所有订单行关联的发货单同步RETAIL以及同步商城状态置为N
     */
    Order setCouponAndPromotion(IRequest request, Order order);


    /**
     * 根据指定的订单状态获取订单列表
     * 取订单状态不是NEW_CREATE,PROCESS_ERROR, TRADE_FINISHED, TRADE_CLOSED, TRADE_CANCELLED的订单
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
     * 判断指定平台订单号且WEBSITE_ID=TM且ORDER_TYPE=NORMAL的记录是否存在
     *
     * @param escOrderCode
     * @return List<Order>
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
    void setOrderAssiging(List<Long> orderIds, Long employeeId);

    /**
     * 计算退货行运费安装费
     */
    Double getInstallationFee(Order order);

    /**
     * 根据订单ID同步指定订单到商城
     *
     * @param orderId
     */
    void orderSyncZmall(Long orderId);

    /**
     * 获取总的建议退款金额
     *
     * @param request
     * @param order
     * @return
     */
    Order getTotalRefundAmount(IRequest request, Order order);

    /**
     * 增加金额更改记录头
     *
     * @param order
     * @return
     */
    AmountChangeLog addAmountChangeLog(Order order);

    /**
     * 增加金额更改记录行
     *
     * @param changeLog
     * @param order
     * @param orderEntry
     */
    void addAmountChangeLogEntry(AmountChangeLog changeLog, Order order, OrderEntry orderEntry);

    /**
     * 增加书面记录
     *
     * @param soChangeLog
     */
    void addSoChangeLog(HmallSoChangeLog soChangeLog);
}