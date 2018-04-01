package com.hand.hmall.as.service.impl;

import com.hand.hmall.as.dto.DeliverOrder;
import com.hand.hmall.as.dto.ServiceorderEntry;
import com.hand.hmall.as.mapper.DeliverOrderMapper;
import com.hand.hmall.as.mapper.ServiceorderEntryMapper;
import com.hand.hmall.as.service.IDeliverOrderService;
import com.hand.hmall.mst.dto.Product;
import com.hand.hmall.mst.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by chenzhigang on 2017/7/19.
 */
@Service
public class DeliverOrderServiceImpl implements IDeliverOrderService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ServiceorderEntryMapper serviceOrderEntryMapper;

    @Autowired
    private DeliverOrderMapper deliverOrderMapper;


    /**
     * 根据订单ID查询其对应的全部商品
     * @param orderId - 订单ID
     * @return
     */
    @Override
    public List<Product> queryProductsByOrderId(long orderId) {
        return productMapper.queryProductsByOrderId(orderId);
    }

    /**
     * 保存送货单对象
     * @param deliverOrder - 送货单对象
     * @return
     */
    @Override
    @Transactional
    public void save(DeliverOrder deliverOrder) {

        // 保存送货单对象到售后单表中
        deliverOrderMapper.insert(deliverOrder);

        // 遍历此次新建的送货单对应的送货单行
        for (ServiceorderEntry entry : deliverOrder.getDeliverOrderEntries()) {

            // 保存送货单行对象到服务单行表中
            serviceOrderEntryMapper.insert(entry);
        }
    }

}
