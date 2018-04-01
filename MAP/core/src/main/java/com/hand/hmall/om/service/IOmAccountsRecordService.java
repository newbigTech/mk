package com.hand.hmall.om.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.om.dto.OmAccountsRecord;

import java.util.Map;

public interface IOmAccountsRecordService extends IBaseService<OmAccountsRecord>, ProxySelf<IOmAccountsRecordService> {

    /**
     * 根据条件查找对应的账单记录
     *
     * @param condition
     * @return
     */
    OmAccountsRecord getOmAccountsRecord(Map<String, String> condition);
}