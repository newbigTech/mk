package com.hand.hmall.om.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.om.dto.OmAccountsRecord;

import java.util.Map;

public interface OmAccountsRecordMapper extends Mapper<OmAccountsRecord> {

    /**
     * 根据账单记录时间和渠道查找对应的账单记录
     *
     * @param condition
     * @return
     */
    OmAccountsRecord getRecordByCondition(Map<String, String> condition);

}