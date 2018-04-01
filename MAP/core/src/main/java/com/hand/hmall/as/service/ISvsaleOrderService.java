package com.hand.hmall.as.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.as.dto.Svsales;
import com.hand.hmall.as.dto.SvsalesTemplate;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.om.dto.Paymentinfo;

import java.util.List;

/**
 * author: zhangzilong
 * name: ISvsaleOrderService.java
 * discription: 服务销售单Service接口
 * date: 2017/7/18
 * version: 0.1
 */
public interface ISvsaleOrderService extends IBaseService<Svsales>, ProxySelf<ISvsaleOrderService> {

    Svsales saveOrUpdate(Svsales dto, IRequest request) throws Exception;

    List<Svsales> queryBySvsalesId(Long asSvsalesId, IRequest iRequest);

    Svsales newSvsale(Long serviceOrderId, IRequest iRequest);

    /**
     * 服务销售单支付信息接收并保存
     *
     * @param dto
     * @return
     */
    void saveSvsalesPaymentinfo(Paymentinfo dto) throws RuntimeException;

    /**
     * 发送至Retail
     *
     * @param asSvsalesId
     * @param iRequest
     * @return ResponseData
     */
    ResponseData sendToRetail(Long asSvsalesId, IRequest iRequest);

    ResponseData updateStatusToProc(Svsales dto);

    ResponseData updateStatusToCanc(Svsales svsales);

    /**
     * 导入服务销售单
     *
     * @param infos
     * @param iRequest
     * @param importResult
     * @param message
     * @return
     */
    com.hand.hap.system.dto.ResponseData importSvsalesAndSvsalesEntry(List<SvsalesTemplate> infos, IRequest iRequest, boolean importResult, String message) throws Exception;

    /**
     * @author xuxiaoxue
     * @version 0.1
     * @name ISvsaleOrderService
     * @description 查询服务销售单列表(带分页)
     * @date 2017/12/6
     */
    List<Svsales> querySvsales(IRequest requestContext, String code, String serviceOrderCode, String escOrderCode, String sapCode, String customerId, String mobileNumber, String payStatus, String syncflag, String[] svsaleStatus, int page, int pageSize);

    /**
     * 查询服务销售单列表(不带分页)
     *
     * @param requestContext
     * @param code
     * @param serviceOrderCode
     * @param escOrderCode
     * @param sapCode
     * @param customerId
     * @param mobile
     * @param payStatus
     * @param syncflag
     * @param svsaleStatus
     * @return
     */
    List<Svsales> querySvsales(IRequest requestContext, String code, String serviceOrderCode, String escOrderCode, String sapCode, String customerId, String mobile, String payStatus, String syncflag, String[] svsaleStatus);
}
