package com.hand.hmall.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hand.hmall.code.MessageCode;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.dto.ThreadFactory;
import com.hand.hmall.pojo.OrderPojo;
import com.hand.hmall.service.ISalePromotionCodeService;
import com.hand.hmall.service.PromoteCaculateService;
import com.hand.hmall.service.IPromoteService;
import com.hand.hmall.util.CaculateFeight;
import com.hand.hmall.util.InstallationPay;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.*;

/**
 * @Describe 订单促销执行入口
 * @Author noob
 * @Date 2017/6/28 16:22
 */
@RestController
@RequestMapping(value = "/sale/execution", produces = {MediaType.APPLICATION_JSON_VALUE})
public class SaleExecutionController {

    @Autowired
    private IPromoteService promoteService;
    @Autowired
    private PromoteCaculateService promoteCaculateService;

    @Autowired
    private ISalePromotionCodeService salePromotionCodeService;

    static Logger logger = LoggerFactory.getLogger(SaleExecutionController.class);

    /**
     * @param orderMap 前台促销执行参数
     * @return
     * @throws CloneNotSupportedException
     */
    @RequestMapping(value = "/promote", method = RequestMethod.POST)
    public ResponseData promoteOrder(@RequestBody Map<String, Object> orderMap) throws CloneNotSupportedException {
        try {

            long startTime = System.currentTimeMillis();
            logger.info("----------------------------start promote-------------------------");
            logger.info("----------------------------request parm-------------------------{}", JSON.toJSONString(orderMap));
            //数据初始化，接收到的参数转换为对应的订单，订单行
            OrderPojo order = new OrderPojo();

            //前台参数转为DTO
            promoteCaculateService.mapToOrder(orderMap, order);

            //处理订单行为空
            if (CollectionUtils.isEmpty(order.getOrderEntryList())) {
                promoteCaculateService.getPromoteResult(order);
                ResponseData responseData = new ResponseData(Arrays.asList(order));
                responseData.setMsg("有效订单行数量为空");
                return responseData;
            }

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
            logger.info(" 1. --计算运费----" + (System.currentTimeMillis() - startTime));

            long t_promote = System.currentTimeMillis();
            //执行促销相关逻辑
            ResponseData promoteResp = promoteService.promote(order);
            if (!promoteResp.isSuccess()) {
                return promoteResp;
            }
            logger.info(" 2. --计算促销----" + (System.currentTimeMillis() - t_promote));

            List promoteRespList = promoteResp.getResp();
            List<JSONObject> respList = new ArrayList<>();
            for (Object o : promoteRespList) {
                OrderPojo orderPojo = (OrderPojo) o;
                //对订单金额进行分摊计算
                promoteCaculateService.apportionPromotion(orderPojo);
                //调整返回参数
                JSONObject orderJson = promoteCaculateService.getPromoteResult(orderPojo);
                respList.add(orderJson);
            }
            promoteResp.setResp(respList);

            logger.info("-----promote result----\n" + JSON.toJSONString(promoteResp));
            //促销分摊
            logger.info("--整体耗时------" + (System.currentTimeMillis() - startTime));
            return promoteResp;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ResponseData responseData = new ResponseData();
            responseData.setSuccess(false);
            responseData.setMsg(e.getMessage());
            return responseData;
        }
    }


    /**
     * 查询商品对应的促销活动
     *
     * @param productMap
     * @return
     */
    @RequestMapping(value = "/getActivity", method = RequestMethod.POST)
    public ResponseData getActivity(@RequestBody Map productMap) {
        ResponseData responseData = new ResponseData();
        String code = productMap.get("code").toString();
        ResponseData promotionResp = salePromotionCodeService.selectByProductCode(code);
        List<Map<String, Object>> activities = (List<Map<String, Object>>) promotionResp.getResp();
        List list = new ArrayList<>();
        for (Map activity : activities) {
            Map activityMid = new HashMap<>();
            activityMid.put("activityId", activity.get("activityId"));
            activityMid.put("activityName", activity.get("meaning"));
            activityMid.put("activityDes", activity.get("meaning"));
            activityMid.put("pageShowMes", activity.get("meaning"));
            list.add(activityMid);
        }
        responseData.setMsg(MessageCode.ACTIVITY_QUERY_01.getValue());
        responseData.setMsgCode("1");
        responseData.setResp(list);
        return responseData;
    }


//    /**
//     * 计算运费的线程
//     */
//    class CaculateFeight implements Callable<ResponseData> {
//
//        private OrderPojo OrderPojo;
//
//        public CaculateFeight(OrderPojo orderPojo) {
//            this.OrderPojo = orderPojo;
//        }
//
//        @Override
//        public ResponseData call() throws Exception {
//            logger.info("计算运费");
//            return promoteCaculateService.caculateExpressAndLogisticsFreight(OrderPojo);
//        }
//    }
//
//
//    class InstallationPay implements Callable<ResponseData> {
//
//        private OrderPojo OrderPojo;
//
//        public InstallationPay(OrderPojo orderPojo) {
//            this.OrderPojo = orderPojo;
//        }
//
//        @Override
//        public ResponseData call() throws Exception {
//            long startTime = System.currentTimeMillis();
//            ResponseData responseData = promoteCaculateService.caculateInstallationPay(OrderPojo);
//            logger.info(" 3. --计算安装费----" + (System.currentTimeMillis() - startTime));
//            return responseData;
//        }
//    }

}
