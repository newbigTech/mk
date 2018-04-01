package com.hand.hmall.om.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.om.dto.OrderCouponrule;
import com.hand.hmall.om.dto.OrderCouponruleDto;
import com.hand.hmall.om.mapper.OrderCouponruleMapper;
import com.hand.hmall.om.service.IOrderCouponruleService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class OrderCouponruleServiceImpl extends BaseServiceImpl<OrderCouponrule> implements IOrderCouponruleService {

    @Autowired
    private OrderCouponruleMapper orderCouponruleMapper;

    /**
     * 根据订单ID查询对应的优惠劵信息
     * @param orderId
     * @return
     */
    @Override
    public List<OrderCouponruleDto> selectOrderSyncByOrderId(Long orderId) {
        List<OrderCouponruleDto> orderCouponruleDtos = orderCouponruleMapper.selectOrderSyncByOrderId(orderId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (CollectionUtils.isNotEmpty(orderCouponruleDtos)) {
            for (OrderCouponruleDto orderCouponruleDto : orderCouponruleDtos) {
                try {
                    if (orderCouponruleDto.getStartDate() != null) {
                        orderCouponruleDto.setStartDate(sdf.format(sdf.parse(orderCouponruleDto.getStartDate())));
                    }
                    if (orderCouponruleDto.getEndDate() != null) {
                        orderCouponruleDto.setEndDate(sdf.format(sdf.parse(orderCouponruleDto.getEndDate())));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return orderCouponruleDtos;
    }

    /**
     * 根据订单ID更新优惠券信息
     *
     * @param orderCouponrule
     * @return
     */
    @Override
    public int updateOrderCouponruleByOrderId(OrderCouponrule orderCouponrule) {
        return orderCouponruleMapper.updateOrderCouponruleByOrderId(orderCouponrule);
    }

    /**
     * 根据订单ID查询优惠券信息
     *
     * @param orderCouponrule
     * @return
     */
    @Override
    public List<OrderCouponrule> selectOrderCouponruleByOrderId(OrderCouponrule orderCouponrule) {
        return orderCouponruleMapper.selectOrderCouponruleByOrderId(orderCouponrule);
    }

    /**
     * 根据订单ID删除优惠券信息
     *
     * @param orderCouponrule
     * @return
     */
    @Override
    public int deleteOrderCouponruleByOrderId(OrderCouponrule orderCouponrule) {
        return orderCouponruleMapper.deleteOrderCouponruleByOrderId(orderCouponrule);
    }


}