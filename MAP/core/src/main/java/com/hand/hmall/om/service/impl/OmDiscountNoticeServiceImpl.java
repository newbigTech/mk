package com.hand.hmall.om.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.om.dto.OmDiscountNotice;
import com.hand.hmall.om.mapper.OmDiscountNoticeMapper;
import com.hand.hmall.om.service.IOmDiscountNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhangmeng
 * @name OmDiscountNoticeServiceImpl
 * @description 折扣行通知
 * @date 2018/02/09
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OmDiscountNoticeServiceImpl extends BaseServiceImpl<OmDiscountNotice> implements IOmDiscountNoticeService {

    @Autowired
    private OmDiscountNoticeMapper omDiscountNoticeMapper;

    /**
     * 根据折扣行ID删除
     *
     * @param omDiscountNotice
     * @return
     */
    @Override
    public int deleteByDiscountId(OmDiscountNotice omDiscountNotice) {
        return omDiscountNoticeMapper.deleteByDiscountId(omDiscountNotice);
    }
}