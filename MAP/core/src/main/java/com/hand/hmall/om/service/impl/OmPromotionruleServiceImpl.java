package com.hand.hmall.om.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.om.dto.OmPromotionrule;
import com.hand.hmall.om.dto.OmPromotionruleDto;
import com.hand.hmall.om.mapper.OmPromotionruleMapper;
import com.hand.hmall.om.service.IOmPromotionruleService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
@Transactional
public class OmPromotionruleServiceImpl extends BaseServiceImpl<OmPromotionrule> implements IOmPromotionruleService {

    @Autowired
    private OmPromotionruleMapper promotionruleMapper;

    /**
     * 订单同步中根据订单ID获取对应的促销信息
     * @param orderId
     * @return
     */
    @Override
    public List<OmPromotionruleDto> selectOrderSyncByOrderId(Long orderId) {
        List<OmPromotionruleDto> promotionruleDtos = promotionruleMapper.selectOrderSyncByOrderId(orderId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (CollectionUtils.isNotEmpty(promotionruleDtos)) {
            for (OmPromotionruleDto promotionruleDto : promotionruleDtos) {
                try {
                    if (promotionruleDto.getStartTime() != null) {
                        promotionruleDto.setStartTime(sdf.format(sdf.parse(promotionruleDto.getStartTime())));
                    }
                    if (promotionruleDto.getEndTime() != null) {
                        promotionruleDto.setEndTime(sdf.format(sdf.parse(promotionruleDto.getEndTime())));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return promotionruleDtos;
    }

    /**
     * 根据订单ID更新促销信息
     *
     * @param omPromotionrule
     * @return
     */
    @Override
    public int updateOmPromotionruleByOrderId(OmPromotionrule omPromotionrule) {
        return promotionruleMapper.updateOmPromotionruleByOrderId(omPromotionrule);
    }

    /**
     * 根据订单ID查询促销信息
     *
     * @param omPromotionrule
     * @return
     */
    @Override
    public List<OmPromotionrule> selectOmPromotionruleByOrderId(OmPromotionrule omPromotionrule) {
        return promotionruleMapper.selectOmPromotionruleByOrderId(omPromotionrule);
    }

    /**
     * 根据订单ID删除促销信息
     *
     * @param omPromotionrule
     * @return
     */
    @Override
    public int deleteOmPromotionruleByOrderId(OmPromotionrule omPromotionrule) {
        return promotionruleMapper.deleteOmPromotionruleByOrderId(omPromotionrule);
    }
}