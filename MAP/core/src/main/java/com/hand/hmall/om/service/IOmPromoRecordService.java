package com.hand.hmall.om.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.om.dto.OmPromoRecord;

import java.util.List;
import java.util.Map;
/**
 * @author yuxiaoli
 * @version 0.1
 * @name IOmPromoRecordService
 * @description 事后促销记录Service类
 * @date 2017/10/13
 */
public interface IOmPromoRecordService extends IBaseService<OmPromoRecord>, ProxySelf<IOmPromoRecordService> {

    /**
     * @param request
     * @param dto
     * @param page
     * @param pageSize
     * @return
     * @description 根据传进来的参数俩判断是符合条件用户还是候补用户
     */
    public List<OmPromoRecord> selectPromoRecord(IRequest request, OmPromoRecord dto, int page, int pageSize);

    /**
     * @param request
     * @param dto
     * @return
     * @description 将订单转化成调用“事后促销资格重新判定”微服务的参数
     */
    public Map<String, Object> changeToParam(IRequest request, OmPromoRecord dto);

    /**
     * @param request
     * @param dto
     * @return
     * @description 查询某事后促销规则下'WAIT_FINI', 'FINISH'两个状态的记录个数
     */
    public List<OmPromoRecord> selectFinishCount(IRequest request, OmPromoRecord dto);

}