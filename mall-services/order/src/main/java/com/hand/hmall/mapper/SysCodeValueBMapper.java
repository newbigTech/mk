package com.hand.hmall.mapper;

import com.hand.hmall.model.SysCodeValueB;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author 梅新养
 * @name:SysCodeValueBMapper
 * @Description:系统code查询Mapper
 * @version 1.0
 * @date 2017/5/24 14:39
 */
public interface SysCodeValueBMapper extends Mapper<SysCodeValueB> {

    /**
     * 检验订单状态是否存在
     * @param orderStatus
     * @return
     */
    int checkOrderStatus(String orderStatus);
    /**
     * 检验发票类型是否存在
     * @param shippingType2
     * @return
     */
    int checkShippingType(String shippingType2);
    /**
     * 检验配送方式是否存在
     * @param invoiceType
     * @return
     */
    int checkInvoiceType(String invoiceType);
    /**
     * 检验支付类型是否存在
     * @param payMode
     * @return
     */
    int checkPayMode(String payMode);
    /**
     * 检验货币是否存在
     * @param currencyCode
     * @return
     */
    int checkCurrencyCode(String currencyCode);
    /**
     * 检验单位是否存在
     * @param unit
     * @return
     */
    int checkUnit(String unit);

    /**
     * 检验订单类型是否存在
     * @param orderType
     * @return
     */
    int checkPayInfoOrderType(String orderType);

    /**
     * 检验订单行状态是否存在
     * @param orderEntryStatus
     * @return
     */
    int checkOrderEntryStatus(String orderEntryStatus);
}