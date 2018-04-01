package com.hand.hmall.as.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.as.dto.Receipt;

import java.util.List;

/**
 * @author zhangmeng
 * @version 0.1
 * @name ReceiptMapper
 * @description 售后单mapper
 * @date 2017/7/19
 */
public interface ReceiptMapper extends Mapper<Receipt> {
    /**
     * 根据服务单CODE查询售后单信息
     *
     * @param dto
     * @return List<Receipt>
     */
    List<Receipt> queryReceiptByServiceOrderId(Receipt dto);
}