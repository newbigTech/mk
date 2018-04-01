package com.hand.hmall.as.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.as.dto.Serviceorder;
import com.hand.hmall.om.dto.Order;

import java.util.List;

/**
 * @author xuxiaoxue
 * @version 0.1
 * @name IServiceorderService
 * @description 服务单列表界面Service接口
 * @date 2017/7/17
 */
public interface IServiceorderService extends IBaseService<Serviceorder>, ProxySelf<IServiceorderService> {

    /**
     * 根据查询条件查询符合条件的服务单列表(分页)
     *
     * @param dto
     * @return List<Serviceorder>
     */
    List<Serviceorder> queryServiceOrderList(Serviceorder dto, int page, int pageSize);

    /**
     * 根据查询条件查询符合条件的服务单列表(不分页)
     *
     * @param dto
     * @return List<Serviceorder>
     */
    List<Serviceorder> queryServiceOrderListWithoutPage(Serviceorder dto);


    /**
     * 根据订单ID查询服务单信息
     *
     * @param dto
     * @return List<Serviceorder>
     */
    List<Serviceorder> queryServiceOrderListBySaleCode(Serviceorder dto, int page, int pageSize);


    /**
     * 查询服务单详细信息
     *
     * @param dto
     * @return
     */
    List<Serviceorder> selectServiceOrderByCode(Serviceorder dto);

    /**
     * 根据订单ID查询用户信息
     *
     * @param dto
     * @return
     */
    List<Serviceorder> selectUserInfoByOrderId(Serviceorder dto);


    Serviceorder saveCategory(IRequest iRequest, List<Serviceorder> dto, String serviceOrderId);


    /**
     * 根据服务单ID查询多媒体中的图片信息
     *
     * @param dto
     * @return
     */
    List<Serviceorder> queryMediaByServiceOrderId(Serviceorder dto, int page, int pageSize);

    /**
     * 设置服务单归属信息
     *
     * @param soIds_     - 服务单ID列表
     * @param employeeId - 员工ID
     */
    void setAssiging(List<Long> soIds_, Long employeeId);

    /**
     * 查询调用保价接口需要的订单数据
     *
     * @param orderId
     * @return
     */
    Order insuredOrder(Long orderId);

    /**
     * 查询订单是否可以保价
     *
     * @param orderId
     * @return
     */
    boolean checkInsuredOrder(Long orderId);

    /**
     * 判断是否需要生成销售赔付单
     *
     * @param order
     * @return
     */
    boolean checkInsuredOrderExtraReduce(Order order);

    /**
     * 生成销售赔付单 更新订单 订单行信息.
     *
     * @param order
     * @param serviceOrderId
     */
    com.hand.hmall.dto.ResponseData insertAsCompensate(Order order, Long serviceOrderId, IRequest requestContext);

}