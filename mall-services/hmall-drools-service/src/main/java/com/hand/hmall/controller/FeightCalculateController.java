package com.hand.hmall.controller;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import com.hand.hmall.client.IProductClientService;
import com.hand.hmall.dto.LogisticsCalculateDto;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.dto.ThreadFactory;
import com.hand.hmall.pojo.OrderEntryPojo;
import com.hand.hmall.pojo.OrderPojo;
import com.hand.hmall.service.PromoteCaculateService;
import com.hand.hmall.util.CaculateFeight;
import com.hand.hmall.util.InstallationPay;
import com.hand.hmall.util.MapToBean;
import com.hand.hmall.util.ResponseReturnUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author XinyangMei
 * @Title FeightCalculateController
 * @Description 运费计算逻辑controller
 * @date 2017/11/24 15:24
 */
@RestController
@RequestMapping(value = "/sale/feight", produces = {MediaType.APPLICATION_JSON_VALUE})
public class FeightCalculateController {
    static Logger logger = LoggerFactory.getLogger(FeightCalculateController.class);
    @Autowired
    private IProductClientService productClientService;
    @Autowired
    private PromoteCaculateService promoteCaculateService;
    /**
     * @param logisticsCalculateDtos 前台促销执行参数
     * @return
     * @throws CloneNotSupportedException
     */
    @RequestMapping(value = "/calculate", method = RequestMethod.POST)
    public ResponseData promoteOrder(@RequestBody List<LogisticsCalculateDto> logisticsCalculateDtos) throws CloneNotSupportedException {
        try {
            //数据初始化，接收到的参数转换为对应的订单，订单行
            for (LogisticsCalculateDto logisticsCalculateDto : logisticsCalculateDtos) {
                OrderPojo order = transToOrder(logisticsCalculateDto);
                //开启安装费计算线程
                Future<ResponseData> installPay = ThreadFactory.getThreadPool().submit(new InstallationPay(order,promoteCaculateService));
                ResponseData installResp = installPay.get();
                if (!installResp.isSuccess())
                    return installResp;

                //开启运费计算线程
                Future<ResponseData> caculateFeight = ThreadFactory.getThreadPool().submit(new CaculateFeight(order,promoteCaculateService));
                //获取运费计算返回结果，用于校验是否计算成功
                ResponseData caculateResp = caculateFeight.get();
                if (!caculateResp.isSuccess())
                    return caculateResp;
                transToLogisticDto(order, logisticsCalculateDto);
            }

            return ResponseReturnUtil.returnTrueResp(logisticsCalculateDtos);


        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ResponseData responseData = new ResponseData();
            responseData.setSuccess(false);
            responseData.setMsg(e.getMessage());
            return responseData;
        }
    }

    /**
     * 将运费DTO转换为计算运费的订单订单行DTO
     *
     * @param logisticsCalculateDto
     * @return
     */
    public OrderPojo transToOrder(LogisticsCalculateDto logisticsCalculateDto) {
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setCityCode(logisticsCalculateDto.getCityCode());
        orderPojo.setDistrictCode(logisticsCalculateDto.getDistrictCode());
        OrderEntryPojo entryPojo = new OrderEntryPojo();
        entryPojo.setQuantity(logisticsCalculateDto.getQuantity());
        entryPojo.setProductPackageSize(logisticsCalculateDto.getProductPackSize());
        entryPojo.setProductId(logisticsCalculateDto.getOrderEntryId().toString());
        entryPojo.setShippingType("LOGISTICS");
        //获取商品编码
        ResponseData productResp = productClientService.selectByProductId(entryPojo.getProductId());
        Map productObj = (Map) ResponseReturnUtil.getRespObj(productResp);
        String productCode = MapUtils.getString(productObj, "code");
        entryPojo.setProduct(productCode);
        List entryList = new ArrayList();
        entryList.add(entryPojo);
        orderPojo.setOrderEntryList(entryList);
        return orderPojo;
    }

    /**
     * 将计算完运费的订单中的运费数据放到LogisticsCalculateDto
     *
     * @param orderPojo
     * @param logisticsCalculateDto
     */
    public void transToLogisticDto(OrderPojo orderPojo, LogisticsCalculateDto logisticsCalculateDto) {
        OrderEntryPojo entryPojo = orderPojo.getOrderEntryList().get(0);
        logisticsCalculateDto.setInstallationFee(entryPojo.getInstallationFee());
        logisticsCalculateDto.setMaincarriage(entryPojo.getMainCarriage());
        logisticsCalculateDto.setSubcarriage(entryPojo.getSubCarriage());
    }


}
