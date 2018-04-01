package com.hand.hmall.service;

import com.hand.hmall.dto.OmPromoteRecord;

/**
 * 事后促销满足记录
 */
public interface IOmPromoteRecordService{

    public long insert(OmPromoteRecord omPromoteRecord);

    public int countByConditon(OmPromoteRecord omPromoteRecord);

    public OmPromoteRecord findByCondition(OmPromoteRecord omPromoteRecord);

    public int updateOmPromoteRecord(OmPromoteRecord omPromoteRecord);



}