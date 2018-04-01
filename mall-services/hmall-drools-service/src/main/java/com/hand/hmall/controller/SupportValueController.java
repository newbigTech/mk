package com.hand.hmall.controller;

import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.dto.ThreadFactory;
import com.hand.hmall.menu.CreateJarType;
import com.hand.hmall.pojo.OrderEntryPojo;
import com.hand.hmall.pojo.OrderPojo;
import com.hand.hmall.service.IExcutePromoteService;
import com.hand.hmall.service.IPromoteService;
import com.hand.hmall.service.PromoteCaculateService;
import com.hand.hmall.util.ArithUtil;
import com.hand.hmall.util.DateUtil;
import com.hand.hmall.util.MapToBean;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author baihua
 * @version 0.1
 * 促销保价
 * @name SupportValueController$
 * @description $END$
 * @date 2017/11/6$
 */
@RestController
@RequestMapping(value = "/sale/execution", produces = {MediaType.APPLICATION_JSON_VALUE})
public class SupportValueController {

    @Autowired
    private PromoteCaculateService promoteCaculateService;
    @Autowired
    private IExcutePromoteService excutePromoteService;
    @Autowired
    private IPromoteService promoteService;

    /**
     * @param orderMap 前台促销执行参数
     * @return
     * @throws CloneNotSupportedException
     *
     *
     *
     */
    @RequestMapping(value = "/supportValue", method = RequestMethod.POST)
    public ResponseData promoteOrder(@RequestBody Map<String, Object> orderMap) throws CloneNotSupportedException {

        //数据初始化，接收到的参数转换为对应的订单，订单行
        OrderPojo order = new OrderPojo();
        mapToOrder(orderMap, order);
        OrderPojo cloneOrder = order.clone();
        //修改为当前时间执行头促销
        order.setCreated(DateUtil.getdate(new Date(),"yyyy-MM-dd HH:mm:ss"));
        //还原使用优惠券
        order.setChosenCoupon("");
        //还原使用促销
        order.setChosenPromotion("");
        //还原原始优惠金额
        order.setDiscountFee(0.0);
        //原始订单总金额
        Double oldOrderAmount = order.getOrderAmount();
//      1、校验订单是否有物流行
        boolean falg = checkParams(order.getOrderEntryList());
        if(falg){
            ResponseData responseData = new ResponseData(Arrays.asList(order));
            responseData.setMsg("订单无物流行");
            return responseData;
        }
        //处理订单行为空
        if (CollectionUtils.isEmpty(order.getOrderEntryList())) {
            promoteCaculateService.getPromoteResult(order);
            ResponseData responseData = new ResponseData(Arrays.asList(order));
            responseData.setMsg("有效订单行数量为空");
            return responseData;
        }
        List<OrderEntryPojo> orderEntryList = order.getOrderEntryList();
//       2、快递行 执行 baseprice * quitity - discountfee 物流行 执行行促销（优先级最高）
        List<OrderEntryPojo> saveExpressEntryList = new ArrayList<OrderEntryPojo>();
        for(OrderEntryPojo orderEntryPojo : orderEntryList){
            if(orderEntryPojo.getShippingType().equals("EXPRESS")){
                //将快递取出
                saveExpressEntryList.add(orderEntryPojo);
            }
            orderEntryPojo.setDiscountFeel(0.0);
            orderEntryPojo.setDiscountFee(0.0);
        }
        orderEntryList.removeAll(saveExpressEntryList);
        try {
            //执行订单行促销
            Future<List<OrderEntryPojo>> orderEntryPromote = ThreadFactory.getThreadPool().submit(new OrderEntryPromote(order));
            orderEntryPromote.get();
            List<OrderEntryPojo> list = order.getOrderEntryList();
            for(OrderEntryPojo o : saveExpressEntryList){
                list.add(o);
            }
            order.setIsCaculate("N");
            ResponseData usefulPromoteAndCoupon = promoteService.getUsefulPromoteAndCoupon(order);
            OrderPojo newOrder = (OrderPojo) usefulPromoteAndCoupon.getResp().get(0);
            List<Map> activities = newOrder.getActivities();
            if(CollectionUtils.isEmpty(activities)){
                ResponseData responseData = new ResponseData(Arrays.asList(order));
                responseData.setMsg("当前无可用促销");
                return responseData;
            }else{
                //排序取优先级最高促销
                activities.sort(new Comparator<Map>() {
                    @Override
                    public int compare(Map o1, Map o2) {
                        Double amount = Double.valueOf((o1.get("amount"))==null?"0.0":o1.get("amount").toString());
                        Double amount1 = Double.valueOf((o2.get("amount"))==null?"0.0":o2.get("amount").toString());
                        return -amount.compareTo(amount1);
                    }
                });
                Map map = activities.get(0);
                order.setIsCaculate("Y");
                ResponseData promoteResp = excutePromoteService.payByActivitySingle(map.get("activityId").toString(), order);
                Double netAmount = order.getNetAmount();
                //安装费
                Double fixFee = order.getFixFee();
                double add = ArithUtil.add(netAmount, fixFee);
                //物流运费
                Double epostFee = order.getEpostFee();
                double add1 = ArithUtil.add(add, epostFee);
                //快递运费
                Double postFee = order.getPostFee();
                double allOrderAmount = ArithUtil.add(add1, postFee);

                //订单金额对比 订单老金额 - 新金额 = 差价   如果为正数 用户买的便宜 不贵 ！ 订单头原始金额减差价 = 优惠金额减少 （用户选择保价陪）
                //如果为负数用户买贵了  优惠金额增加  退差价
                double sub = ArithUtil.sub(oldOrderAmount,allOrderAmount);
                //分摊给物流 改变物流行字段  unitFee(实际单价), totalFee (行实际支付金额),discountFeel(订单头促销优惠金额分摊到行),totoldiscount(订单行所有优惠);
                List<OrderEntryPojo> saveLogisitcsEntity = new ArrayList<OrderEntryPojo>();
                List<OrderEntryPojo> orderEntryList1 = order.getOrderEntryList();
                for(OrderEntryPojo orderEntryPojo : orderEntryList1){
                    if(orderEntryPojo.getShippingType().equals("LOGISTICS")){
                        saveLogisitcsEntity.add(orderEntryPojo);
                    }
                }
                int apportionNum = saveLogisitcsEntity.size();
                double div = ArithUtil.div(sub, apportionNum, 2);
                double mul = ArithUtil.mul(div, apportionNum);
                //差价
                double difference = ArithUtil.sub(sub, mul);
                for(OrderEntryPojo orderEntryPojo :saveLogisitcsEntity){
                    orderEntryPojo.setDiscountFeel(ArithUtil.add(orderEntryPojo.getDiscountFeel(),div));
                    orderEntryPojo.setTotalDiscount(ArithUtil.add(orderEntryPojo.getDiscountFee(),ArithUtil.add(orderEntryPojo.getDiscountFeel(),orderEntryPojo.getCouponFee())));
                    orderEntryPojo.setTotalFee(ArithUtil.sub(orderEntryPojo.getTotalFee(),orderEntryPojo.getTotalDiscount()));
                    orderEntryPojo.setUnitFee(ArithUtil.div(orderEntryPojo.getTotalFee(),orderEntryPojo.getQuantity(),2));
                }
                //有差价
                if(difference != 0){
                    OrderEntryPojo orderEntryPojo = saveLogisitcsEntity.get(saveLogisitcsEntity.size() - 1);
                    orderEntryPojo.setTotalDiscount(ArithUtil.add(orderEntryPojo.getTotalDiscount(),difference));
                    orderEntryPojo.setDiscountFeel(ArithUtil.add(orderEntryPojo.getDiscountFeel(),difference));
                    orderEntryPojo.setTotalFee(ArithUtil.add(orderEntryPojo.getTotalFee(),difference));
                    orderEntryPojo.setUnitFee(ArithUtil.add(orderEntryPojo.getUnitFee(),difference));
                }
                List<OrderEntryPojo> removeOrderEntry = new ArrayList<OrderEntryPojo>();
                List<OrderEntryPojo> cloneOrderEntry = cloneOrder.getOrderEntryList();
                for(OrderEntryPojo o : cloneOrderEntry){
                    if(o.getShippingType().equals("LOGISTICS")){
                        removeOrderEntry.add(o);
                    }
                }
                cloneOrderEntry.removeAll(removeOrderEntry);
                List<OrderEntryPojo> updateOrderEntry = order.getOrderEntryList();
                for(OrderEntryPojo o :updateOrderEntry){
                    if(o.getShippingType().equals("LOGISTICS")){
                        cloneOrderEntry.add(o);
                    }
                }
                //修改订单头
                cloneOrder.setOrderAmount(ArithUtil.sub(cloneOrder.getOrderAmount(),sub));
                cloneOrder.setDiscountFee(ArithUtil.add(cloneOrder.getDiscountFee(),sub));
                cloneOrder.setTotalDiscount(ArithUtil.add(cloneOrder.getTotalDiscount(),sub));
                cloneOrder.setExtraReduce(sub);
                //添加currentAmount字段
                cloneOrder.setCurrentAmount(getCurrentAmount(cloneOrder));
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        ResponseData responseData = new ResponseData(Arrays.asList(cloneOrder));
        return responseData;
    }

    public boolean checkParams(List<OrderEntryPojo> orderEntryList){
        boolean falg = true;
        for(OrderEntryPojo orderEntryPojo : orderEntryList){
            if(orderEntryPojo.getShippingType().equals("LOGISTICS")){
                falg = false;
            }
        }
        return falg;
    }


    /**
     *12.	额外返回字段CURRENT_AMOUNT=ORDER_AMOUNT-各个订单行的RETURNED_QUANTITY*UNIT_FEE-退货行运费安装费
     *注：状态为NORMAL的订单行的“已退货数量”【returned_quantity】/订单行数量【QUANTITY】*该订单行的（运费【SHIPPING_FEE】+安装费【INSTALLATION_FEE】）
     * @param o
     * @return
     */
    public Double getCurrentAmount(OrderPojo o){

        double allRefundMoney = 0.0;
        List<OrderEntryPojo> orderEntryList = o.getOrderEntryList();
        for(OrderEntryPojo orderEntryPojo : orderEntryList){
            Integer returnQuantity = orderEntryPojo.getReturnQuantity();
            Double unitFee = orderEntryPojo.getUnitFee();
            Integer quantity = orderEntryPojo.getQuantity();
            Double shippingFee = orderEntryPojo.getShippingFee();
            Double installationFee = orderEntryPojo.getInstallationFee();
            double shippingFeeAddInstallationFee = ArithUtil.add(shippingFee, installationFee);
            //计算退货行运费安装费
            /**
             **注：“已退货数量”【returned_quantity】/订单行数量【QUANTITY】*该订单行的（运费【SHIPPING_FEE】+安装费【INSTALLATION_FEE】）
             */
            double mul = ArithUtil.mul(returnQuantity, unitFee);
            double round = ArithUtil.round(ArithUtil.mul(ArithUtil.div(returnQuantity, quantity), shippingFeeAddInstallationFee), 2);
            double sub = ArithUtil.sub(mul, round);
            allRefundMoney = ArithUtil.add(allRefundMoney,sub);
        }
        return ArithUtil.sub(o.getOrderAmount(), allRefundMoney);
    }

    /**
     * 订单行促销线程
     */
    class OrderEntryPromote implements Callable {

        private OrderPojo orderPojo;

        public OrderEntryPromote(OrderPojo orderPojo) {
            this.orderPojo = orderPojo;
        }

        @Override
        public Object call() throws Exception {
            excutePromoteService.orderEntryPromote(orderPojo);
            return null;
        }
    }




    public void mapToOrder(Map orderMap, OrderPojo order) {
        MapToBean.transMap2Bean(orderMap, order);
        List<Map> orderEntryMapList = (List) order.getOrderEntryList();
        List<OrderEntryPojo> list = new ArrayList<>();
        for (Map map : orderEntryMapList) {
            OrderEntryPojo orderEntry = new OrderEntryPojo();
            MapToBean.transMap2Bean(map, orderEntry);
            //擦除入参干扰数据，将订单行相关的fee置为0
            orderEntry.setBundleRemainder(orderEntry.getQuantity());
            //判断传入订单行是否为赠品
            String isGift = orderEntry.getIsGift();
            if (StringUtils.isNotEmpty(isGift) && "N".equals(isGift)) {
                list.add(orderEntry);
            }
        }
        order.setOrderEntryList(list);
    }



}
