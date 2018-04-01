package com.hand.hmall.om.mapper;

import com.hand.hap.mam.dto.PinVcodeDto;
import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.om.dto.OrderEntry;
import com.hand.hmall.om.dto.OrderEntryCompare;
import com.hand.hmall.om.dto.OrderEntryComparePojo;
import com.hand.hmall.om.dto.OrderEntryDto;

import java.util.List;
import java.util.Map;

/**
 * @author peng.chen
 * @version 0.1
 * @name OrderEntryMapper
 * @description 订单行mapper
 * @date 2017年5月25日18:53:32
 */

public interface OrderEntryMapper extends Mapper<OrderEntry> {

    /**
     * 推送retail行数据查询
     *
     * @return
     */
    List<OrderEntry> selectRetailData(Long consignmentId);

    /**
     * 订单行查询详情
     *
     * @param dto
     * @return
     */
    List<OrderEntry> queryInfo(OrderEntry dto);

    /**
     * 查询服务单关联的订单行
     *
     * @param dto
     * @return
     */
    List<OrderEntry> queryServiceOrderInfo(OrderEntry dto);

    /**
     * 派工单添加功能中订单行详情查询
     *
     * @param dto
     * @return
     */
    OrderEntry queryById(OrderEntry dto);

    /**
     * 查询未生成组件的套件行
     *
     * @param orderId 订单id
     * @return List<OrderEntry>
     */
    List<OrderEntry> selectSuiteEntriesByOrderId(Long orderId);

    /**
     * 订单同步时根据订单ID查询对应的订单行
     *
     * @param orderId 订单id
     * @return List<OrderEntry>
     */
    List<OrderEntryDto> selectOrderSyncByOrderId(Long orderId);

    /**
     * 订单拆分时，如果商品是套件，获取对应的套件订单行
     *
     * @param orderEntryId 订单行ID
     * @return
     */
    List<OrderEntry> getSuitOrderEntries(Long orderEntryId);

    /**
     * 订单拆分，查询满足拆分条件的订单
     *
     * @param orderId
     * @return
     */
    List<OrderEntry> getSuitableSplitEntries(Long orderId);

    /**
     * @param paramMap
     * @return
     * @description 取消订单行时判断该订单下的所有订单行是否都取消了
     */
    List<OrderEntry> isAllCanceled(Map paramMap);

    /**
     * 批量插入订单行表数据
     *
     * @param orderEntryList
     */
    void batchInsertOrderEntry(List<OrderEntry> orderEntryList);

    /**
     * @param map
     * @return
     * @description 订单取消时，查询非取消的normal状态的所有订单行 以便调用促销微服务查询促销信息
     */
    List<OrderEntry> selectUnCancelOrderEntry(Map<String, Object> map);

    List<OrderEntry> selectReturnOrderEntry(Map<String, Object> map);

    /**
     * 根据订单ID获取对应订单行中最大的行序号
     *
     * @param orderId 订单ID
     * @return 平台行序号ID
     */
    Long getMaxLinenumberByOrderId(Long orderId);

    /**
     * 根据订单ID获取对应订单行中最大的平台订单行序号
     *
     * @param orderId 订单ID
     * @return 平台行序号ID
     */
    Long getMaxEscLinenumberByOrderId(Long orderId);

    /**
     * 根据套件的Vcode查询套件下所有子套件的pin码
     *
     * @param map
     * @return
     */
    List<OrderEntry> selectOrderEntryByVCode(Map<String, Object> map);

    /**
     * 后台校验要拆分的订单行
     *
     * @return
     */
    OrderEntry checkSplitOrderEntry(Long orderEntryId);

    /**
     * 更新订单行已退货数量和未生成发货单数量
     *
     * @param orderEntry
     * @return
     */
    int updateOrderEntryReturnedQuantity(OrderEntry orderEntry);

    /**
     * 修改组件已退货数量和未生成发货单数量
     *
     * @param orderEntry
     * @return
     */
    int updateSonOrderEntryReturnedQuantity(OrderEntry orderEntry);


    /**
     * 根据订单行中的发货单ID和关联订单号查询订单行信息
     *
     * @param orderEntry 封装的参数
     * @return 返回的查询结果
     */
    List<OrderEntry> selectByParentLineAndConsignmentId(OrderEntry orderEntry);

    /**
     * 查询关联发货单行是否发货
     *
     * @return 返回的查询结果
     */
    List<OrderEntry> selectByConsignmentId(OrderEntry orderEntry);

    /**
     * 根据关联商品CODE和订单ORDER_ID查询对应的订单行
     *
     * @param orderEntry
     * @return
     */
    List<OrderEntry> selectByProductCodeAndOrderId(OrderEntry orderEntry);

    List<OrderEntryCompare> allOrderEntryCompare(OrderEntryCompare compare);

    /**
     * 根据订单ID查询安装费 运费 数量
     *
     * @param orderEntry
     * @return
     */
    List<OrderEntry> selectFeeByOrderId(OrderEntry orderEntry);

    List<OrderEntryComparePojo> allOrderEntryComparePart(OrderEntryComparePojo compare);

    /**
     * 查询订单下的主推款订单行
     *
     * @param orderId 订单id
     * @return List<OrderEntry>
     */
    List<OrderEntry> selectRegularEntries(Long orderId);

    /**
     * 查询订单行的套件头V码，不存在则返回自身V码
     *
     * @param pin
     * @param vcode
     * @return
     */
    OrderEntry selectParentVCode(PinVcodeDto dto);

    /**
     * 生成保价单时，查询normal状态的所有订单行
     */
    List<OrderEntry> selectInsuredOrderEntry(Long orderId);
}