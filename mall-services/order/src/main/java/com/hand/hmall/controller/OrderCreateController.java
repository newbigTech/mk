package com.hand.hmall.controller;


import com.hand.hmall.code.MessageCode;
import com.hand.hmall.model.HmallOmOrder;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.service.IOrderCreateService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 唐磊
 * @name:OrderCreateController
 * @Description:底层订单下载调用Controller，用于为上层提供服务
 * @version 1.0
 * @date 2017/5/24 14:39
 */
@RestController
@RequestMapping(value = "/o/order")
public class OrderCreateController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IOrderCreateService orderCreateService;

    /**
     * 订单下载
     * @Author: noob
     * @Param:  * @param order
     * @Date: 2017/5/25 10:02
     */
    @RequestMapping(value = "/addOrder", method = RequestMethod.POST )
    @ResponseBody
    public ResponseData addOrder(@RequestBody HmallOmOrder order)
    {
        ResponseData responseData = new ResponseData();

        //检验 平台订单号(escOrderCode),网站(webSiteCode),渠道(channelCode),店铺(storeCode)是否为空
        responseData = validateEWCS(order);
        if(!responseData.isSuccess())
        {
            return responseData;
        }

        //根据平台订单编号,网站,渠道,店铺确定一个唯一订单
        HmallOmOrder orderCheck = orderCreateService.selectByMutiItems(order.getEscOrderCode(),order.getWebsiteCode(),order.getChannelCode(),order.getStoreCode());

        if(orderCheck == null)
        {
            try{
                //订单下载
                return  orderCreateService.addOrder(order);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                logger.error("---msg---:" + e.getMessage());
                responseData.setMsg(e.getMessage());
                responseData.setMsgCode("od.addorder.saving.error");
                responseData.setSuccess(false);
                return responseData;
            }
        }
        else
        {
            responseData.setMsg("订单已存在");
            responseData.setMsgCode("od.addorder.exist");
            responseData.setSuccess(false);
            return responseData;
        }
    }

    /**
     * 订单更新
     * @Author: 李伟
     * @Param:  * @param order
     * @Date: 2017/7/21 09:00
     */
      @RequestMapping(value = "/updateOrder" , method = RequestMethod.POST)
      @ResponseBody
      public ResponseData updateOrder(@RequestBody HmallOmOrder order)
      {
          ResponseData responseData = new ResponseData();

          //检验 平台订单号(escOrderCode),网站(webSiteCode),渠道(channelCode),店铺(storeCode)是否为空
          responseData = validateEWCS(order);
          if(!responseData.isSuccess())
          {
              return responseData;
          }

          //根据平台订单编号,网站,渠道,店铺,订单类型(NORMAL)确定一个唯一订单
          HmallOmOrder orderCheck = orderCreateService.selectByMutiItemsForUpdate(order.getEscOrderCode(),order.getWebsiteCode(),order.getChannelCode(),order.getStoreCode());

          if(orderCheck == null)
          {
              responseData.setMsg(MessageCode.OR_UPDATE_02.getValue());
              responseData.setMsgCode("od.updateorder.notexist");
              responseData.setSuccess(false);
              return responseData;
          }
          else
          {
              order.setOrderId(orderCheck.getOrderId());
              order.setCode(orderCheck.getCode());
              try
              {
                  //订单更新
                  return orderCreateService.updateOrder(order);
              }
              catch (Exception e)
              {
                  e.printStackTrace();
                  logger.error("===msg===:" + e.getMessage());
                  responseData.setMsg(e.getMessage());
                  responseData.setMsgCode("od.updateorrder.error");
                  responseData.setSuccess(false);
                  return responseData;
              }
          }
      }

    /**
     * 检验 平台订单号(escOrderCode)、网站(webSiteCode)、渠道(channelCode)、店铺(storeCode) 是否为空
     * @param order
     * @return
     */
    private ResponseData validateEWCS(HmallOmOrder order)
    {
        ResponseData responseData = new ResponseData();
        String escOrderCode = order.getEscOrderCode();
        String webSiteCode = order.getWebsiteCode();
        String channelCode = order.getChannelCode();
        String storeCode = order.getStoreCode();

        //检验平台订单号是否为空
        if(StringUtils.isEmpty(escOrderCode))
        {
            responseData.setMsg("平台订单号为空");
            responseData.setMsgCode("order.escordercode.null");
            responseData.setSuccess(false);
            return responseData;
        }

        //检验网站是否为空
        if(StringUtils.isEmpty(webSiteCode))
        {
            responseData.setMsg("网站为空");
            responseData.setMsgCode("order.websitecode.null");
            responseData.setSuccess(false);
            return responseData;
        }

        //检验渠道是否为空
        if(StringUtils.isEmpty(channelCode))
        {
            responseData.setMsg("渠道为空");
            responseData.setMsgCode("order.channelcode.null");
            responseData.setSuccess(false);
            return responseData;
        }

        //检验店铺是否为空
        if(StringUtils.isEmpty(storeCode))
        {
            responseData.setMsg("店铺为空");
            responseData.setMsgCode("order.storecode.null");
            responseData.setSuccess(false);
            return responseData;
        }

        responseData.setMsg("检验成功");
        responseData.setMsgCode("1");
        responseData.setSuccess(true);
        return responseData;
    }
}

