package com.hand.promotion.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.google.common.util.concurrent.AtomicDouble;
import com.hand.hmall.dto.ResponseData;
import com.hand.promotion.client.IProductClientService;
import com.hand.promotion.dto.CategoryMapping;
import com.hand.promotion.dto.HmallMstInstallation;
import com.hand.promotion.pojo.SimpleMessagePojo;
import com.hand.promotion.pojo.order.OrderEntryPojo;
import com.hand.promotion.pojo.order.OrderPojo;
import com.hand.promotion.service.HmallMstInstallationService;
import com.hand.promotion.service.ICalInstalltionFeeService;
import com.hand.promotion.service.ICategoryMappingService;
import com.hand.promotion.util.DecimalCalculate;
import com.hand.promotion.util.ThreadPoolUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2018/2/6
 * @description 订单安装费计算逻辑
 */
@Service
public class CalInstalltionFeeServiceImpl implements ICalInstalltionFeeService {

    @Autowired
    private IProductClientService productClientService;
    @Autowired
    private ICategoryMappingService categoryMappingService;

    @Autowired
    private HmallMstInstallationService hmallMstInstallationService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * 计算订单数据的安装费
     *
     * @param order 要计算安装费的订单数据
     * @return
     */
    @Override
    public SimpleMessagePojo calOrderInstallFee(OrderPojo order) {
        long startTime = System.currentTimeMillis();
        SimpleMessagePojo resp = new SimpleMessagePojo();
        List<OrderEntryPojo> orderEntryPojos = order.getOrderEntryList();

        AtomicDouble totalFee = new AtomicDouble(0);
        CountDownLatch latch = new CountDownLatch(orderEntryPojos.size());
        List<String> errorList = Collections.synchronizedList(new ArrayList<>());
        AtomicBoolean errorFlag = new AtomicBoolean(true);
        for (OrderEntryPojo orderEntryPojo : orderEntryPojos) {
            ThreadPoolUtil.submit(() -> calMulEntryInstallFee(orderEntryPojo, latch, errorList, totalFee, errorFlag));
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            resp.setSuccess(false);
            e.printStackTrace();
            return resp;
        }
        logger.info("--caculateInstallationPay errFlag--{}", errorFlag.get());
        if (errorFlag.get() == false) {
            resp.setSuccess(false);
            resp.setCheckMsg(JSON.toJSONString(errorList));
            return resp;
        }
        order.setFixFee(totalFee.get());
        resp.setSuccess(true);
        resp.setObj(new ArrayList<>(Arrays.asList(order)));
        logger.info("####计算安装费耗时:{}", System.currentTimeMillis() - startTime);
        return resp;
    }


    /**
     * 使用多线程计算订单行的安装费
     *
     * @param orderEntryPojo 要计算安装费的订单行
     * @param latch          线程计数器
     * @param errors         计算错误信息
     * @param totalFee       总安装费
     * @param errorFlag      是否计算错误
     */
    private void calMulEntryInstallFee(OrderEntryPojo orderEntryPojo, CountDownLatch latch, List<String> errors, AtomicDouble totalFee, AtomicBoolean errorFlag) {
        try {


            HmallMstInstallation hmallMstInstallation = hmallMstInstallationService.getInstallationByCategoryIdAndStatus(Long.parseLong(orderEntryPojo.getCategoryId()), "Y");
            //查询商品对应分类下的安装费
            if (null == hmallMstInstallation) {
                errors.add("查询不到类别ID为" + orderEntryPojo.getCategoryId() + "的安装费");
                logger.info("查询不到类别ID为" + orderEntryPojo.getCategoryId() + "的安装费");
                errorFlag.set(false);
                return;
            }

            double entryFee = DecimalCalculate.mul(hmallMstInstallation.getInstallationFee(), orderEntryPojo.getQuantity());
            totalFee.addAndGet(entryFee);
            orderEntryPojo.setInstallationFee(entryFee);

        } catch (Exception e) {
            errorFlag.set(false);
            errors.add("查询" + orderEntryPojo.getProduct() + "的安装费异常");
            logger.info("查询" + orderEntryPojo.getProduct() + "的安装费异常");
            logger.error("安装费计算异常" + e.getMessage(), e);
        } finally {
            latch.countDown();
        }
    }


}
