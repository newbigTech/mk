package com.hand.promotion.mapper;

import com.hand.promotion.dto.OmPromoteRecord;
import tk.mybatis.mapper.common.Mapper;

public interface OmPromoteRecordMapper extends Mapper<OmPromoteRecord> {

    public long createRecord(OmPromoteRecord omPromoteRecord);

    public int countByConditon(OmPromoteRecord omPromoteRecord);

    public OmPromoteRecord selectByCondition(OmPromoteRecord omPromoteRecord);

    public OmPromoteRecord selectByPromoId(OmPromoteRecord omPromoteRecord);
}