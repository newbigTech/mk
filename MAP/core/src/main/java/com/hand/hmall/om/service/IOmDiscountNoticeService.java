package com.hand.hmall.om.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.om.dto.OmDiscountNotice;

/**
 * @author zhangmeng
 * @name OmDiscountNoticeService
 * @description 折扣行通知
 * @date 2018/02/09
 */
public interface IOmDiscountNoticeService extends IBaseService<OmDiscountNotice>, ProxySelf<IOmDiscountNoticeService> {

    /**
     * 根据折扣行ID删除
     *
     * @param omDiscountNotice
     * @return
     */
    int deleteByDiscountId(OmDiscountNotice omDiscountNotice);

}