package com.hand.promotion.service;

import com.hand.hmall.dto.ResponseData;
import com.hand.promotion.pojo.SimpleMessagePojo;
import com.hand.promotion.pojo.order.OrderEntryPojo;
import com.hand.promotion.pojo.order.OrderPojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2018/2/6
 * @description 订单安装费计算逻辑
 */
public interface ICalInstalltionFeeService {

    /**
     * 计算订单的安装费
     *
     * @param order
     * @return
     */
    public SimpleMessagePojo calOrderInstallFee(OrderPojo order);


}
