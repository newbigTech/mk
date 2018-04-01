package com.hand.hmall.as.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.as.dto.AsDispatchorderEntry;

import java.util.List;

public interface AsDispatchorderEntryMapper extends Mapper<AsDispatchorderEntry>{

    List<AsDispatchorderEntry> queryByReceiptOrderId(Long receiptOrderId);
}