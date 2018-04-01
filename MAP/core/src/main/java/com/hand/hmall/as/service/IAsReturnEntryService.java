package com.hand.hmall.as.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.as.dto.AsReturn;
import com.hand.hmall.as.dto.AsReturnEntry;

import java.util.List;

public interface IAsReturnEntryService extends IBaseService<AsReturnEntry>, ProxySelf<IAsReturnEntryService> {
    /**
     * 根据订单ID查询订单行中未创建发货单数量大于零的订单行
     *
     * @param asReturnEntry
     * @return
     */
    List<AsReturnEntry> queryOrderEntrynotReturnQuantity(AsReturnEntry asReturnEntry);

    /**
     * 根据退货单id查询退货单行
     *
     * @param asReturnEntry
     * @return
     */
    List<AsReturnEntry> queryReturnEntryById(AsReturnEntry asReturnEntry);

    /**
     * 更新订单行未生成发货单数量为0
     *
     * @return
     */
    int updateOrderEntrynotReturnQuantity(AsReturn asReturn);

    /**
     * 修改退货原因
     *
     * @param asReturnEntry
     * @return
     */
    int updateReturnEntryReturnReason(AsReturnEntry asReturnEntry);
}