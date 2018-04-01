package com.hand.hmall.om.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.hr.dto.MarkorEmployee;
import com.hand.hmall.log.dto.LogManager;
import com.hand.hmall.om.dto.Consignment;
import com.hand.hmall.om.dto.OrderEntry;
import com.hand.hmall.ws.entities.OrderRequestBody;
import com.hand.hmall.ws.entities.OrderUpdateRequestbody;
import com.markor.map.framework.common.exception.BusinessException;

import java.util.List;
import java.util.Map;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name IConsignmentService
 * @description 发货单service
 * @date 2017年6月5日13:54:47
 */
public interface IConsignmentService extends IBaseService<Consignment>, ProxySelf<IConsignmentService> {
    /**
     * 推送retail接口数据查询
     *
     * @return
     */
    List<Consignment> selectSendRetailData(Map map);

    /**
     * 发货单列表页面查询
     *
     * @param page             页数
     * @param pagesize         页面显示数量
     * @param logisticsNumber  快递单号
     * @param escOrderCode     发货单号
     * @param provice          省份
     * @param city             城市
     * @param receiverMobile   收货人手机号
     * @param startTime        下单开始时间
     * @param endTime          结束时间
     * @param strOrderStatus   订单状态
     * @param strDistribution  配送方式
     * @param corporateName    快递公司
     * @param csApproved       是否审核
     * @param confirmReceiving 是否收货标识（Y已收获；N未收货）
     * @return
     */
    List<Consignment> selectConsignmentList(int page, int pagesize, String logisticsNumber, String code, String provice,
                                            String city, String receiverMobile, String startTime, String endTime, String[] strOrderStatus,
                                            String[] strDistribution, String[] strOrderTypes, String corporateName, String csApproved, String bomApproved, String pause, String escOrderCode,
                                            String confirmReceiving);

    /**
     * 发货单详情页查询
     *
     * @param iRequest 请求体
     * @param dto      参数封装对象
     * @param page     页数
     * @param pageSize 显示数量
     * @return 查询结果
     */
    List<Consignment> queryInfo(IRequest iRequest, Consignment dto, int page, int pageSize);

    /**
     * 根据发货单ID查询
     *
     * @param consignmentId 发货单ID
     * @return
     */
    Consignment queryOne(int consignmentId);

    /**
     * 根据发货单ID更新状态
     *
     * @param requestCtx
     * @param consignmentId 发货单ID
     * @param status        新状态
     */
    void updateStatus(IRequest requestCtx, int consignmentId, String status);

    /**
     * 审核按钮
     *
     * @param list
     */
    void examinestatus(List<Consignment> list, IRequest iRequest);

    /**
     * 手工拆单
     *
     * @param iRequest
     * @param list
     * @return
     */
    List<Consignment> split(IRequest iRequest, List<OrderEntry> list);

    /**
     * 校验拆单分单处理后是否满足条件
     *
     * @param iRequest
     * @param list
     * @return
     */
    String checkSplit(IRequest iRequest, List<OrderEntry> list);

    /**
     * 根据发货单状态查询发货单
     *
     * @param status 发货单状态
     * @return List<Consignment>
     */
    List<Consignment> selectByStatus(String status);

    Map<String, String> sendToZmall() throws Exception;

    /**
     * 根据条件查询发货单
     *
     * @param consignment 查询条件
     * @return List<Consignment>
     */
    List<Consignment> select(Consignment consignment);

    /**
     * 发送发货单信息至日日顺
     *
     * @return
     */
    List<Map> sendConsignmentToRRS() throws Exception;

    /**
     * @param iRequest
     * @param consignment
     * @param log
     * @return
     * @description 供订单推送retail根据发货单生成body方法
     */
    OrderRequestBody getBodyForOrderToRetail(IRequest iRequest, Consignment consignment, LogManager log);

    /**
     * @param iRequest
     * @param consignment
     * @param log         log为null，则是订单暂挂申请，反之则为订单变更retail
     * @return
     * @description 供订单更新retail根据发货单生成body方法，或者实现订单暂挂申请按钮
     */
    OrderUpdateRequestbody getBodyForOrderUpdateToRetail(IRequest iRequest, Consignment consignment, LogManager log);

    /**
     * 订单暂挂时，获取请求体，同时能够添加操作日志
     *
     * @param iRequest
     * @param consignment
     * @param log
     * @return
     */
    OrderUpdateRequestbody getBodyForOrderUpdateForConsignmentHold(IRequest iRequest, Consignment consignment, LogManager log);

    /**
     * 重新加载发货单的所有字段
     *
     * @param consignment 发货单
     * @return Consignment 重新加载后的发货单
     */
    Consignment reload(Consignment consignment);

    /**
     * 发货单保存
     *
     * @param iRequest    request对象
     * @param consignment 发货单数据
     */
    void saveConsignment(IRequest iRequest, Consignment consignment);

    /**
     * 根据城市编码、区域编码、仓库编码获取物流提前期
     *
     * @param cityCode           城市编码
     * @param areaCode           区域编码
     * @param pointOfServiceCode 仓库编码
     * @param shippingType       发运方式
     * @return Integer
     */
    Integer getLogisticsLeadTime(String cityCode, String areaCode, String pointOfServiceCode, String shippingType);

    /**
     * 查询可发货的发货单
     *
     * @return List<Consignment>
     */
    List<Consignment> selectCanBeShippedConsignments();

    /**
     * 可发运判断
     */
    void judgeCanBeShipped();

    /**
     * 合批发货单
     *
     * @return
     */
    String mergeConsignment(IRequest iRequest, List<Consignment> consignmentList);

    /**
     * 取消合批发货单
     *
     * @return
     */
    String cancelMergeConsignment(IRequest iRequest, List<Consignment> consignmentList);

    /**
     * 发货单审核
     *
     * @param iRequest       请求对象
     * @param consignment    发货单
     * @param markorEmployee 审核人员
     */
    void consignmentCheck(IRequest iRequest, Consignment consignment, MarkorEmployee markorEmployee);

    /**
     * 发货单确认收货
     *
     * @param consignment
     * @return
     */
    void consignmentConfirmFinish(Consignment consignment) throws BusinessException;

}