package com.hand.hmall.as.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.as.dto.Receipt;
import com.hand.hmall.as.mapper.ReceiptMapper;
import com.hand.hmall.as.service.IReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhangmeng
 * @version 0.1
 * @name ReceiptServiceImpl
 * @description 售后单ServiceImpl
 * @date 2017/7/19
 */
@Service
@Transactional
public class ReceiptServiceImpl extends BaseServiceImpl<Receipt> implements IReceiptService {

    @Autowired
    private ReceiptMapper receiptMapper;

    /**
     * 根据服务单CODE查询售后单信息
     *
     * @param dto
     * @return List<Receipt>
     */
    @Override
    public List<Receipt> queryReceiptByServiceOrderId(Receipt dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return receiptMapper.queryReceiptByServiceOrderId(dto);
    }
}