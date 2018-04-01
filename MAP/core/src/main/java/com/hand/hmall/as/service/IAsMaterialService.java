package com.hand.hmall.as.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.as.dto.AsMaterial;

import java.util.List;

public interface IAsMaterialService extends IBaseService<AsMaterial>, ProxySelf<IAsMaterialService> {

    /**
     * 同步物耗单到retail
     *
     * @param iRequest
     * @param asMaterial
     * @return
     */
    ResponseData materialSyncRetail(IRequest iRequest, AsMaterial asMaterial);

    ResponseData saveMaterialOrder(IRequest iRequest, AsMaterial asMaterial);

    /**
     * 查询物耗单列表
     *
     * @param requestContext
     * @param code
     * @param serviceOrderCode
     * @param escOrderCode
     * @param customerid
     * @param mobile
     * @param sapCode
     * @param creationDateStart
     * @param creationDateEnd
     * @param finishTimeStart
     * @param finishTimeEnd
     * @param page
     * @param pageSize
     * @return
     */
    List<AsMaterial> selectMaterialList(IRequest requestContext, String code, String serviceOrderCode, String escOrderCode, String customerid, String mobile, String sapCode, String creationDateStart, String creationDateEnd, String finishTimeStart, String finishTimeEnd, String isCharge, String syncRetail, String[] strMaterialStatus, int page, int pageSize);

    /**
     * 查询物耗单列表(不带分页)
     *
     * @param requestContext
     * @param code
     * @param serviceOrderCode
     * @param escOrderCode
     * @param customerid
     * @param mobile
     * @param sapCode
     * @param creationDateStart
     * @param creationDateEnd
     * @param finishTimeStart
     * @param finishTimeEnd
     * @param isCharge
     * @param syncRetail
     * @param strMaterialStatus
     * @return
     */
    List<AsMaterial> selectMaterialList(IRequest requestContext, String code, String serviceOrderCode, String escOrderCode, String customerid, String mobile, String sapCode, String creationDateStart, String creationDateEnd, String finishTimeStart, String finishTimeEnd, String isCharge, String syncRetail, String[] strMaterialStatus);
}