package com.hand.hmall.as.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.as.dto.AsReturn;
import com.hand.hmall.as.dto.AsReturnEntry;

import java.util.List;

public interface AsReturnEntryMapper extends Mapper<AsReturnEntry> {

    List<AsReturnEntry> queryOrderEntrynotReturnQuantity(AsReturnEntry asReturnEntry);

    List<AsReturnEntry> queryReturnEntryById(AsReturnEntry asReturnEntry);

    int updateOrderEntrynotReturnQuantity(AsReturn asReturn);

    /**
     * 修改退货原因
     *
     * @param asReturnEntry
     * @return
     */
    int updateReturnEntryReturnReason(AsReturnEntry asReturnEntry);
}