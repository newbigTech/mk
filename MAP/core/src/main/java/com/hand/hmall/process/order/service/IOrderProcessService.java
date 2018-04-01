package com.hand.hmall.process.order.service;

import com.hand.hmall.om.dto.Consignment;
import com.hand.hmall.om.dto.Order;
import com.hand.hmall.om.dto.OrderEntry;
import com.hand.hmall.process.engine.IProcessService;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name IOrderProcessService
 * @description 订单流程Service
 * @date 2017/7/26 9:13
 */
public interface IOrderProcessService extends IProcessService<Order> {

    /**
     * 生成套件组件
     * @param order 订单
     * @return List<OrderEntry> 生成的组件订单行
     */
    List<OrderEntry> generateSuiteComponents(Order order);

    /**
     * 订单行生成采购价格
     * @param order 订单
     */
    void generateOrderPrice(Order order);

    /**
     * 工艺审核推送
     * @param order 订单
     */
    void bomApprove(Order order);

    /**
     * 订单行库存占用
     * @param order 订单
     */
    void inventoryOccupy(Order order) throws IOException, ParseException;

    /**
     * 订单生成发货单
     * @param order 订单
     * @return Consignment
     */
    Consignment generateConsignment(Order order);

    /**
     * 外采皮沙发定制信息生成
     * @param order 订单
     */
    void outsideProcurement(Order order);


    /**
     * 获取产品包装尺寸
     * @param vCode 订单行v码
     * @return String
     */
    String getProductPackageSize(String vCode);

    /**
     * 从完整的仓库名称中获取仓库的id
     * @param fullPosName 完整的仓库名称 类似'W001泰达仓'
     * @return String
     */
    Long extractIdFromFullPosName(String fullPosName);

    /**
     * 判断订单是否属于天猫订单
     * @param order 订单
     * @return boolean
     */
    boolean isTMALLOrder(Order order);

    /**
     * 主推款包装尺寸计算
     * @param order 订单
     */
    void calSizeForRegular(Order order);
}
