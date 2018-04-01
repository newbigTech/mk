package com.hand.hmall.om.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.om.dto.OmEdPromo;

import java.util.List;

public interface OmEdPromoMapper extends Mapper<OmEdPromo> {

    /**
     * @param omEdPromo
     * @return
     * @description 赠品发放列表查询
     */
    List<OmEdPromo> queryEdPromoListInfo(OmEdPromo omEdPromo);

    /**
     * 查询事后促销规则
     *
     * @param omEdPromo
     * @return
     */
    List<OmEdPromo> selectOmEdPromo(OmEdPromo omEdPromo);
}