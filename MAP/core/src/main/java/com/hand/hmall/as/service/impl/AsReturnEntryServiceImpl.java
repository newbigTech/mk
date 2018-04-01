package com.hand.hmall.as.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.as.dto.AsReturn;
import com.hand.hmall.as.dto.AsReturnEntry;
import com.hand.hmall.as.mapper.AsReturnEntryMapper;
import com.hand.hmall.as.service.IAsReturnEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class AsReturnEntryServiceImpl extends BaseServiceImpl<AsReturnEntry> implements IAsReturnEntryService {
    @Autowired
    private AsReturnEntryMapper asReturnEntryMapper;

    /**
     * 根据订单ID查询订单行中未创建发货单数量大于零的订单行
     *
     * @param asReturnEntry
     * @return
     */
    @Override
    public List<AsReturnEntry> queryOrderEntrynotReturnQuantity(AsReturnEntry asReturnEntry) {
        return asReturnEntryMapper.queryOrderEntrynotReturnQuantity(asReturnEntry);
    }

    /**
     * 根据退货单id查询退货单行
     *
     * @param asReturnEntry
     * @return
     */
    @Override
    public List<AsReturnEntry> queryReturnEntryById(AsReturnEntry asReturnEntry) {
        return asReturnEntryMapper.queryReturnEntryById(asReturnEntry);
    }

    /**
     * 更新订单行未生成发货单数量为0
     *
     * @return
     */
    @Override
    public int updateOrderEntrynotReturnQuantity(AsReturn asReturn) {
        return asReturnEntryMapper.updateOrderEntrynotReturnQuantity(asReturn);
    }

    /**
     * 修改退货原因
     *
     * @param asReturnEntry
     * @return
     */
    @Override
    public int updateReturnEntryReturnReason(AsReturnEntry asReturnEntry) {
        return asReturnEntryMapper.updateReturnEntryReturnReason(asReturnEntry);
    }
}