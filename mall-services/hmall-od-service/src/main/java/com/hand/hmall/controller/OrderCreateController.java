package com.hand.hmall.controller;

import com.hand.hmall.client.OrderCreateFeignService;
import com.hand.hmall.client.OrderUpdateFeignService;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.model.HmallOmOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


/**
 * @name GetCodeFeginServiceHystrix
 * @Describe 提供订单号得获取及pin码获取的功能
 * @Author noob
 * @Date 2017/6/12 13:53
 * @version 1.0
 */

@RestController
public class OrderCreateController {

    @Autowired
    private OrderCreateFeignService orderCreateFeignService;
    @Autowired
    private OrderUpdateFeignService orderUpdateFeignService;

    /**
     * 订单下载接口
     * @Author: noob
     * @param order 保存前台传来的订单相关数据
     * @Date: 2017/5/10 11:06
     */
    @RequestMapping(value="/addOrder",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseData addOrder(@RequestBody HmallOmOrder order)
    {
        //订单下载
        return orderCreateFeignService.addOrder(order);
    }

    /**
     * 订单更新接口
     * @Author: 李伟
     * @param order 更新前台传来的订单相关数据
     * @Date: 2017/7/21 9:27
     */
    @RequestMapping(value="/updateOrder",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseData updateOrder(@RequestBody HmallOmOrder order)
    {
        //订单更新
        return orderUpdateFeignService.updateOrder(order);
    }

}
