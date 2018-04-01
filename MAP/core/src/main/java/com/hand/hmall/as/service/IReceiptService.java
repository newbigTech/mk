package com.hand.hmall.as.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.as.dto.Receipt;

import java.util.List;

/**
 * @author zhangmeng
 * @version 0.1
 * @name IReceiptService
 * @description 售后单Service接口
 * @date 2017/7/19
 */
public interface IReceiptService extends IBaseService<Receipt>, ProxySelf<IReceiptService> {
    /**
     * 根据服务单CODE查询售后单信息
     *
     * @param dto
     * @return List<Receipt>
     */
    List<Receipt> queryReceiptByServiceOrderId(Receipt dto, int page, int pageSize);

}