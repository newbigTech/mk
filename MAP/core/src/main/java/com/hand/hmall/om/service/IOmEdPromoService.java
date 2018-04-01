package com.hand.hmall.om.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.om.dto.OmEdPromo;

import java.util.List;
import java.util.Map;

public interface IOmEdPromoService extends IBaseService<OmEdPromo>, ProxySelf<IOmEdPromoService> {

    /**
     * 查询事后促销规则
     *
     * @param omEdPromo
     * @return
     */
    List<OmEdPromo> selectOmEdPromo(int page, int pageSize, OmEdPromo omEdPromo);

    /**
     * 保存新增事后促销规则
     *
     * @param dto
     * @return
     */
    ResponseData saveOmEdPromo(IRequest iRequest, List<OmEdPromo> dto);

    /**
     * 启用停用
     *
     * @param dto
     * @return
     */
    ResponseData updateStatus(List<OmEdPromo> dto, String flag);

    /**
     * 保存促销规则
     *
     * @param dto
     * @return
     */
    ResponseData deleteOmEdPromoById(List<OmEdPromo> dto);

    /**
     * @param request
     * @param omEdPromo
     * @return
     * @description 赠品发放列表查询
     */
    public List<OmEdPromo> queryEdPromoListInfo(IRequest request, OmEdPromo omEdPromo, int page, int pageSize);


}