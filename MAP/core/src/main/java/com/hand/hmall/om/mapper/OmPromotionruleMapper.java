package com.hand.hmall.om.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.om.dto.OmPromotionrule;
import com.hand.hmall.om.dto.OmPromotionruleDto;

import java.util.List;

public interface OmPromotionruleMapper extends Mapper<OmPromotionrule> {

    /**
     * 订单同步中根据订单ID获取对应的促销信息
     *
     * @param orderId
     * @return
     */
    List<OmPromotionruleDto> selectOrderSyncByOrderId(Long orderId);

    /**
     * 根据订单ID更新促销信息
     *
     * @param omPromotionrule
     * @return
     */
    int updateOmPromotionruleByOrderId(OmPromotionrule omPromotionrule);

    /**
     * 根据订单ID查询促销信息
     *
     * @param omPromotionrule
     * @return
     */
    List<OmPromotionrule> selectOmPromotionruleByOrderId(OmPromotionrule omPromotionrule);

    /**
     * 根据订单ID删除促销信息
     *
     * @param omPromotionrule
     * @return
     */
    int deleteOmPromotionruleByOrderId(OmPromotionrule omPromotionrule);
}