package com.hand.hmall.service;

import com.alibaba.fastjson.JSONObject;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.model.Order;
import com.hand.hmall.pojo.OrderPojo;


import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface PromoteCaculateService {

    void mapToOrder(Map map,OrderPojo orderPojo);


    /**
     * 安装费计算逻辑
     */
    ResponseData caculateInstallationPay(OrderPojo order) throws InterruptedException;

    /**
     * 运费计算
     */
    ResponseData caculateExpressAndLogisticsFreight(OrderPojo order) throws ExecutionException, InterruptedException;

    /**
     * 订单优惠分摊
     */
    ResponseData apportionPromotion(OrderPojo orderPojo);

    void addCheaperProduct(OrderPojo orderPojo);

    /**
     * 查询分类的所有父级分类
     *
     * @param categoryId
     * @param parentIds
     */
    void getParentIds(Long categoryId,List<String> parentIds);

    JSONObject getPromoteResult(OrderPojo order);

    /**
     * 将订单行basePrice，替换为数据库只能怪查询的订单行金额
     *
     * @param clone
     */
    public void getRealPrice(OrderPojo clone);

    /**
     * 处理霸王券的优惠金额
     *
     * @param orderPojo
     */
    public void dealExclusiveCoupon(OrderPojo orderPojo);
}
