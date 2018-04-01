package com.hand.hmall.service;


import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.model.HmallOmConsignment;
import com.hand.hmall.model.HmallOmOrder;
import com.hand.hmall.model.HmallOmOrderBk;
import com.hand.hmall.model.HmallOmOrderEntry;

import java.util.List;

/**
 * @author 李伟
 * @version 0.1
 * @name OrderBkServiceImpl
 * @description 订单备份Service接口实现类
 * @date 2017/8/24 9:38
 */
public interface IOrderBkService{

    /**
     * 为原订单添加备份
     * @param order 原订单
     */
    ResponseData saveBk(HmallOmOrder order);

    /**
     * 保存订单备份
     * @param oriOrder 原始订单
     * @param consignmentList 原始发货单
     * @param orderEntryList 原始订单行
     * @return HmallOmOrderBk
     */
    HmallOmOrderBk saveOrderBk(HmallOmOrder oriOrder, List<HmallOmConsignment> consignmentList, List<HmallOmOrderEntry> orderEntryList);

    /**
     * 计算订单备份的下一个版本
     * @param code 订单编号
     * @return Long
     */
    Long selectNextVersion(String code);

    /**
     * 通过员工编号查询员工id
     * @param code 员工编号
     * @return Long
     */
    Long selectUserByCode(String code);
}
