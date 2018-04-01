package com.hand.hmall.as.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.as.dto.AsReturn;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.om.dto.OrderEntry;
import com.hand.hmall.om.dto.OrderPojo;

import java.util.List;

/**
 * @author liuhongxi
 * @version 0.1
 * @name IAsReturnServiceForRetail
 * @description 退回单Service
 * @date 2017/5/24
 */
public interface IAsReturnService extends IBaseService<AsReturn>, ProxySelf<IAsReturnService> {


    /**
     * 根据退货单id查询对应的退货单
     *
     * @param dto
     */
    List<AsReturn> selectReturnById(AsReturn dto);

    ResponseData saveReturn(IRequest iRequest, List<AsReturn> dto, Long serviceOrderId);

    /**
     * 查询符合促销条件的订单行数据
     */
    OrderPojo selectOrderEntryByPromote(List<OrderEntry> dto, String flag);

    /**
     * 发送至Retail
     *
     * @param asReturnId
     * @param iRequest
     * @return ResponseData
     */
    ResponseData sendToRetail(Long asReturnId, IRequest iRequest);

    /**
     * 根据退货单ID查询订单和服务单信息
     *
     * @param dto
     * @return
     */
    List<AsReturn> selectOrderAndServiceOrderInfoByReturnId(AsReturn dto);


    /**
     * 根据订单ID查询用户信息
     *
     * @param asReturn
     * @return
     */
    List<AsReturn> selectUserInfoByOrderId(AsReturn asReturn);

    /**
     * @param request
     * @param asReturn
     * @description 换转退逻辑
     */
    void changeToReturn(IRequest request, AsReturn asReturn);

    /**
     * @param request
     * @param asReturn
     * @description 取消换货单
     */
    void cancelReturnGood(IRequest request, AsReturn asReturn);

    /**
     * @param request
     * @return
     * @description 筛选退货单同步retail的数据
     */
    List<AsReturn> selectDateForReturnToRetail(IRequest request);

    /**
     * 换转退后续操作，更新促销信息，订单行数量，重新计算建议退款金额
     *
     * @param request
     * @param dto
     * @param currentAmount
     * @param chosenCoupon
     * @param chosenPromotion
     * @return
     */
    ResponseData changeToReturnDetail(IRequest request, List<OrderEntry> dto, Double currentAmount, String chosenCoupon, String chosenPromotion);

    /**
     * 根据服务单ID，查找关联的（唯一）退货单实例并返回
     *
     * @param serviceOrderId 服务单ID
     * @return
     */
    AsReturn queryReturnByServiceOrderId(Long serviceOrderId);
}