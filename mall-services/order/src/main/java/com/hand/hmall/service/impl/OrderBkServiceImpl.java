package com.hand.hmall.service.impl;

import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.mapper.ConsignmentBkMapper;
import com.hand.hmall.mapper.HmallOmOrderBkMapper;
import com.hand.hmall.mapper.HmallOmOrderEntryBkMapper;
import com.hand.hmall.mapper.HmallOmOrderEntryMapper;
import com.hand.hmall.model.*;
import com.hand.hmall.service.IOrderBkService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 李伟
 * @version 0.1
 * @name OrderBkServiceImpl
 * @description 订单备份Service接口实现类
 * @date 2017/8/24 9:38
 */
@Service
@Transactional
public class OrderBkServiceImpl implements IOrderBkService
{

    private static final Logger logger = LoggerFactory.getLogger(OrderBkServiceImpl.class);

    @Autowired
    private HmallOmOrderEntryBkMapper hmallOmOrderEntryBkMapper;

    @Autowired
    private HmallOmOrderEntryMapper hmallOmOrderEntryMapper;

    @Autowired
    private HmallOmOrderBkMapper hmallOmOrderBkMapper;

    @Autowired
    private ConsignmentBkMapper consignmentBkMapper;

    @Override
    public ResponseData saveBk(HmallOmOrder order) {

        ResponseData responseData = new ResponseData();
        HmallOmOrderEntry orderEntryParam = new HmallOmOrderEntry();
        orderEntryParam.setOrderId(order.getOrderId());
        //根据订单头获取订单行
        List<HmallOmOrderEntry> originOrderEntryList = hmallOmOrderEntryMapper.select(orderEntryParam);
        HmallOmOrderBk orderBk = new HmallOmOrderBk();
        BeanUtils.copyProperties(order, orderBk);

        try
        {
            //订单头保存
            hmallOmOrderBkMapper.insertSelective(orderBk);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("---msg---:" + e.getMessage());
            responseData.setMsg(e.getMessage());
            responseData.setMsgCode("od.order.saving.error");
            responseData.setSuccess(false);
            return responseData;
        }


        if (CollectionUtils.isNotEmpty(originOrderEntryList))
        {
            for (HmallOmOrderEntry orderEntry : originOrderEntryList)
            {
                HmallOmOrderEntryBk orderEntryBk = new HmallOmOrderEntryBk();
                BeanUtils.copyProperties(orderEntry, orderEntryBk);
                try
                {
                    //订单行保存
                    hmallOmOrderEntryBkMapper.insertSelective(orderEntryBk);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    logger.error("---msg---:" + e.getMessage());
                    responseData.setMsg(e.getMessage());
                    responseData.setMsgCode("od.orderentry.saving.error");
                    responseData.setSuccess(false);
                    return responseData;
                }
            }
        }

        responseData.setMsg("保存成功");
        responseData.setMsgCode("1");
        responseData.setSuccess(true);
        return responseData;
    }

    @Override
    public HmallOmOrderBk saveOrderBk(HmallOmOrder oriOrder, List<HmallOmConsignment> consignmentList, List<HmallOmOrderEntry> orderEntryList) {

        // 备份订单头
        HmallOmOrderBk orderBk = new HmallOmOrderBk();
        BeanUtils.copyProperties(oriOrder, orderBk);
        orderBk.setVersion(this.selectNextVersion(oriOrder.getCode()));
        hmallOmOrderBkMapper.insertSelective(orderBk);

        // 保存原发货单和发货单备份之间的映射关系
        Map<Long, Long> consignmentMap = new HashMap<>();

        // 对发货单进行备份
        if (CollectionUtils.isNotEmpty(consignmentList)) {
            for (HmallOmConsignment consignment : consignmentList) {
                ConsignmentBk consignmentBk = new ConsignmentBk();
                BeanUtils.copyProperties(consignment, consignmentBk);
                consignmentBk.setConsignmentId(null);
                consignmentBk.setOrderId(orderBk.getOrderId());
                consignmentBkMapper.insertSelective(consignmentBk);
                consignmentMap.put(consignment.getConsignmentId(), consignmentBk.getConsignmentId());
            }
        }

        if (CollectionUtils.isNotEmpty(orderEntryList)) {
            for (HmallOmOrderEntry orderEntry : orderEntryList) {
                HmallOmOrderEntryBk orderEntryBk = new HmallOmOrderEntryBk();
                BeanUtils.copyProperties(orderEntry, orderEntryBk);
                orderEntryBk.setOrderEntryId(null);
                orderEntryBk.setOrderId(orderBk.getOrderId());
                if (orderEntry.getConsignmentId() != null) {
                    // 将订单行备份和对应的发货单备份做关联
                    orderEntryBk.setConsignmentId(consignmentMap.get(orderEntry.getConsignmentId()));
                }
                hmallOmOrderEntryBkMapper.insertSelective(orderEntryBk);
            }
        }

        return orderBk;
    }

    @Override
    public Long selectNextVersion(String code) {
        return hmallOmOrderBkMapper.selectNextVersion(code);
    }

    @Override
    public Long selectUserByCode(String code) {
        return hmallOmOrderBkMapper.selectUserByCode(code);
    }
}
