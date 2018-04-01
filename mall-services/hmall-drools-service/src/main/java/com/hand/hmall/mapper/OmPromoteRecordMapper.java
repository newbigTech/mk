package com.hand.hmall.mapper;

import com.hand.hmall.dto.OmPromoteRecord;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OmPromoteRecordMapper extends Mapper<OmPromoteRecord> {

    public long createRecord(OmPromoteRecord omPromoteRecord);

    public int countByConditon(OmPromoteRecord omPromoteRecord);

    public OmPromoteRecord selectByCondition(OmPromoteRecord omPromoteRecord);

    public OmPromoteRecord selectByPromoId(OmPromoteRecord omPromoteRecord);
}