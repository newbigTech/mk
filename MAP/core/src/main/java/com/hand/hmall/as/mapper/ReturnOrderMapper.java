package com.hand.hmall.as.mapper;

/**
 * Created by xuxiaoxue on 2017/7/20.
 */

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.as.dto.RetrieveOrder;
import com.hand.hmall.as.dto.ReturnOrder;

import java.util.List;

/**
 * @author xuxiaoxue
 * @version 0.1
 * @name ServiceorderMapper
 * @description 退货单详情 Mapper接口
 * @date 2017/7/17
 */
public interface ReturnOrderMapper extends Mapper<ReturnOrder> {
    /**
     * 根据退货单id查询对应的退货单
     *
     * @param dto
     * @return List<ReturnOrder>
     */
    List<ReturnOrder> selectReturnOrderById(ReturnOrder dto);

    /**
     * 根据订单ID查询用户信息
     *
     * @param dto
     * @return
     */
    List<ReturnOrder> selectUserInfoByOrderId(RetrieveOrder dto);
}
