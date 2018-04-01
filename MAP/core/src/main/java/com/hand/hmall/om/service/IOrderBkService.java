package com.hand.hmall.om.service;

import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.om.dto.OrderBk;

import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name IOrderBkService
 * @description 订单备份表Service接口
 * @date 2017/8/4 11:35
 */
public interface IOrderBkService extends IBaseService<OrderBk> {

    /**
     * 保存操作记录
     * @param operationUser 操作人
     * @param operationType 操作类型
     * @param version 备份版本
     * @param orderId 订单id
     */
    void saveBusinessLog(Long operationUser, String operationType, Long version, Long orderId);

    /**
     * 订单快照查询
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
     * @return
     */
    List<OrderBk> selectOrderBkList(int page, int pageSize, String code, String escOrderCode, String userId, String locked,
                                    String receiverMobile, String startTime, String endTime,
                                    String[] strOrderStatus, String[] strDistribution, String[] strOrderTypes, String vproduct, String productId, String payRate, String pinCode);

    /**
     * 订单详情界面查询订单数据
     *
     * @param dto 订单实体类
     * @return List<Order>
     */
    List<OrderBk> selectInfoByOrderBkId(OrderBk dto);

    /**
     * 计算订单备份的下一个版本
     * @param code 订单版本
     * @return Long
     */
    Long selectNextVersion(String code);

    /**
     * 保存订单备份和日志
     * @param operationUser 操作人
     * @param operationType 操作类型
     */
    void saveOrderBkAndLog(Long operationUser, String operationType);


    /**
     * 从线程cache中保存备份
     */
    OrderBk saveBkFromCache();
    /**
     * 根据订单code和目录版本查询对应订单备份
     *
     * @param dto       订单备份实体类
     * @return
     */
    OrderBk queryByVersionAndCode(OrderBk dto);
}
