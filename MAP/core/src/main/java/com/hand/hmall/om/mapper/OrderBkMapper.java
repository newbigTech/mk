package com.hand.hmall.om.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.om.dto.Order;
import com.hand.hmall.om.dto.OrderBk;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name OrderBkMapper
 * @description 订单备份Mapper
 * @date 2017/8/4 10:06
 */
public interface OrderBkMapper extends Mapper<OrderBk> {

    /**
     * 订单快照查询
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
     * @return
     */
    List<OrderBk> selectOrderBkList(@Param("code") String code,
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
                                    @Param("pinCode") String pinCode);

    /**
     * 订单详情界面查询订单数据
     * @param dto 订单实体类
     * @return List<Order>
     */
    List<OrderBk> selectInfoByOrderBkId(OrderBk dto);

    /**
     * 根据订单code和目录版本查询对应订单备份
     *
     * @param dto       订单备份实体类
     * @return
     */
    Long queryByVersionAndCode(OrderBk dto);

    /**
     * 计算订单备份的下一个版本
     * @param code 订单版本
     * @return Long
     */
    Long selectNextVersion(String code);
}
