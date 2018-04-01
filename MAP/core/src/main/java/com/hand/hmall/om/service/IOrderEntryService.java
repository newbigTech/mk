package com.hand.hmall.om.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.mybatis.entity.Example;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.om.dto.*;

import java.util.Date;
import java.util.List;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name IOrderEntryService
 * @description 订单行
 * @date 2017年5月26日10:52:23
 */
public interface IOrderEntryService extends IBaseService<OrderEntry>, ProxySelf<IOrderEntryService> {

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
    List<OrderEntry> queryInfo(IRequest iRequest, OrderEntry dto, int page, int pagesize);

    /**
     * 订单行查询详情
     *
     * @param dto
     * @return
     */
    List<OrderEntry> queryInfo(IRequest iRequest, OrderEntry dto);


    /**
     * 查询服务单关联的订单行
     *
     * @param dto
     * @return
     */
    List<OrderEntry> queryServiceOrderInfo(IRequest iRequest, OrderEntry dto, int page, int pagesize);

    /**
     * 根据一个订单行ID集合查询对应数据
     *
     * @param iRequest
     * @param ids
     * @param page
     * @param pagesize
     * @return
     */
    List<OrderEntry> queryByIds(IRequest iRequest, List<Long> ids, int page, int pagesize);

    /**
     * 根据订单头id查询订单行
     *
     * @param orderId
     * @return
     */
    List<OrderEntry> selectByOrderId(Long orderId);

    /**
     * 派工单添加功能中订单行详情查询
     *
     * @param dto
     * @return
     */
    List<OrderEntry> queryForDispatchOrder(IRequest iRequest, OrderEntry dto, int page, int pagesize);

    /**
     * 查询未生成组件的套件行
     *
     * @param orderId 订单id
     * @return List<OrderEntry>
     */
    List<OrderEntry> selectSuiteEntriesByOrderId(Long orderId);

    /**
     * 根据订单获取下一个lineNumber的取值
     *
     * @param order 订单
     * @return Long
     */
    Long getNextLineNumber(Order order);

    /**
     * 根据订单下的所有订单行确定下一个LineNumber的取值
     *
     * @param orderEntries 订单下所有订单行
     * @return Long
     */
    Long getNextLineNumber(List<OrderEntry> orderEntries);

    /**
     * 根据当前的LineNumber获取下一个lineNumber
     *
     * @param currentLineNumber 当前LineNumber
     * @return Long
     */
    Long getNextLineNumber(Long currentLineNumber);

    /**
     * 根据条件查询所有的订单行
     *
     * @param orderEntry 条件
     * @return List<OrderEntry>
     */
    List<OrderEntry> select(OrderEntry orderEntry);

    /**
     * 根据Example对象查询
     *
     * @param example 查询条件
     * @return List<OrderEntry>
     */
    List<OrderEntry> selectByExample(Example example);

    /**
     * 订单同步时根据订单ID查询对应的订单行
     *
     * @param orderId 订单id
     * @return List<OrderEntry>
     */
    List<OrderEntryDto> selectOrderSyncByOrderId(Long orderId);

    /**
     * 订单详情界面拆单功能
     *
     * @param iRequest
     * @param orderEntries 要拆分的订单行
     * @return
     */
    List<OrderEntry> spiltOrderEntry(IRequest iRequest, List<OrderEntry> orderEntries);

    /**
     * 订单拆分后台校验
     *
     * @param orderEntries 要拆分的订单行
     * @return
     */
    String checkSuitableSplitOrderEntries(List<OrderEntry> orderEntries);

    /**
     * 订单拆分，查询满足拆分条件的订单
     *
     * @param orderId
     * @return
     */
    List<OrderEntry> getSuitableSplitEntries(IRequest iRequest, Long orderId, int page, int pagesize);

    /**
     * @param iRequest
     * @param orderEntries
     * @return
     * @description 取消订单行时判断该订单下的所有订单行是否都取消了
     */
    List<OrderEntry> isAllCanceled(IRequest iRequest, List<OrderEntry> orderEntries);


    /**
     * @param request
     * @param dto
     * @return
     * @description 取消订单行以及取消其子订单行
     */
    List<OrderEntry> cancelOrderEntry(IRequest request, List<OrderEntry> dto);
    /**
     * @param request
     * @param dto
     * @return
     * @description 取消订单行以及取消其子订单行
     */
    List<OrderEntry> cancelGiftEntry(IRequest request, List<OrderEntry> dto);
    /**
     * @param request
     * @param dto
     * @return
     * @description 新增赠品行
     */
    List<OrderEntry> addOrderEntry(IRequest request, List<OrderEntry> dto);

    /**
     * @param request
     * @param dto
     * @return
     * @description 对于天猫订单，取消订单行以及取消其子订单行
     */
    List<OrderEntry> cancelOrderEntryForTm(IRequest request, List<OrderEntry> dto);

    /**
     * @param request
     * @param dto
     * @return
     * @description 退货订单行以及退货其子订单行
     */
    ResponseData confirmReturnGoods(IRequest request, List<OrderEntry> dto, Double currentAmount, String chosenCoupon, String chosenPromotion, String websiteId);

    /**
     * @param request
     * @param dto
     * @return
     * @description 订单取消时，对于全部订单行的取消（将所有订单行均置为取消状态），则订单头状态更新为"TRADE_CLOSED"
     */
    List<OrderEntry> cancelAllOrderAndRntry(IRequest request, List<OrderEntry> dto);

    /**
     * 批量插入订单行表数据
     *
     * @param iRequest
     * @param orderEntryList
     */
    void batchInsertOrderEntry(IRequest iRequest, List<OrderEntry> orderEntryList);

    /**
     * @param dto
     * @param request
     * @return
     * @description 订单取消时，查询非取消的normal状态的所有订单行 以便调用促销微服务查询促销信息
     */
    public List<OrderEntry> selectUnCancelOrderEntry(IRequest request, List<OrderEntry> dto);


    /**
     * @param dto
     * @param request
     * @return
     * @description 订单退货时查询非取消的normal状态的所有订单行
     */
    public List<OrderEntry> selectReturnOrderEntry(IRequest request, List<OrderEntry> dto);

    /**
     * @param orderEntry
     * @return
     */
    String checkAndGetPosCodeOfOrderEntry(OrderEntry orderEntry);

    /**
     * @param request
     * @param dto
     * @param flag    标志位 “N”为获取用户可用促销,"Y" 为根据所选优惠计算订单金额
     * @return
     * @description 将数据转化成促销微服务接口的对象Map
     */
    OrderPojo changePojo(IRequest request, List<OrderEntry> dto, String flag);

    /**
     * 根据订单行中的发货单ID和关联订单号查询订单行信息
     *
     * @param orderEntry 封装的参数
     * @return 返回的查询结果
     */
    List<OrderEntry> selectByParentLineAndConsignmentId(OrderEntry orderEntry);

    /**
     * 根据关联商品CODE和订单ORDER_ID查询对应的订单行
     *
     * @param orderEntry
     * @return
     */
    List<OrderEntry> selectByProductCodeAndOrderId(OrderEntry orderEntry);

    /**
     * 获取订单行比较数据
     *
     * @param
     * @return
     */
    List<OrderEntryCompare> allOrderEntryCompare(IRequest request,OrderEntryCompare orderEntryCompare,int page, int pageSize);List<OrderEntryCompare> exportOrderEntryCompare(IRequest request,OrderEntryCompare orderEntryCompare);
    List<OrderEntryComparePojo> allOrderEntryComparePart(IRequest request,OrderEntryComparePojo orderEntryComparePojo,int page, int pageSize);List<OrderEntryComparePojo> exportOrderEntryComparePart(IRequest request,OrderEntryComparePojo orderEntryComparePojo);

    /**
     * @param request
     * @param orderId
     * @return
     * @description 根据订单ID获取对应订单行中最大的行序号
     */
    Long getMaxLinenumberByOrderId(IRequest request, Long orderId);

    /**
     * 根据订单ID查询安装费 运费 数量
     *
     * @param orderEntry
     * @return
     */
    List<OrderEntry> selectFeeByOrderId(OrderEntry orderEntry);

    /**
     * @param request
     * @param dto
     * @return
     * @description 对于天猫订单，订单取消时，对于全部订单行的取消（将所有订单行均置为取消状态），则订单头状态更新为"TRADE_CLOSED"
     */
    List<OrderEntry> cancelAllOrderAndRntryForTm(IRequest request, List<OrderEntry> dto);

    /**
     * 查询订单下的主推款订单行
     * @param orderId 订单id
     * @return List<OrderEntry>
     */
    List<OrderEntry> selectRegularEntries(Long orderId);


    /**
     * @param request
     * @param orderEntry
     * @return
     * @description 获取新赠品时调用atp接口，获得交付日期
     */
    List<OrderEntry> getAtpTime(IRequest request,List<OrderEntry> orderEntry,String receiverCity,String receiverDistrict);

    /**
     * @param request
     * @param orderEntry
     * @return
     * @description 构造赠品行数据
     */
    List<OrderEntry> setGiftEntry(IRequest request,List<OrderEntry> orderEntry,Long orderId);
}