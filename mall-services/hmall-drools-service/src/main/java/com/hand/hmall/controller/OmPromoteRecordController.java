package com.hand.hmall.controller;

import com.hand.hmall.client.IAfterSaleClientService;
import com.hand.hmall.dto.OmEdPromote;
import com.hand.hmall.dto.OmPromoteRecord;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.menu.Status;
import com.hand.hmall.model.HmallOmOrder;
import com.hand.hmall.service.IOmEdPromoteService;
import com.hand.hmall.service.IOmPromoteRecordService;
import com.hand.hmall.util.ArithUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/afterPromote/operate")
public class OmPromoteRecordController {

    @Autowired
    private IOmPromoteRecordService iOmPromoteRecordService;
    @Autowired
    private IOmEdPromoteService iOmEdPromoteService;
    @Autowired
    private IAfterSaleClientService afterSaleClientService;
    //替补人数
    private static final long SUBSTITUTIONS_NUM = 5;

    /**
     * 事后促销1.订单支付后调用该接口
     * @return
     */
    @RequestMapping("/checkAfterPromote")
    @ResponseBody
    public ResponseData checkAfterPromote(@RequestBody HmallOmOrder hmallOmOrder) throws ParseException {
        synchronized (OmPromoteRecordController.class){
            ResponseData  responseData = new ResponseData();
            ResponseData vaildateParams = vaildateParams(responseData, hmallOmOrder);
            if(StringUtils.isNotEmpty(vaildateParams.getMsg())){
                return responseData;
            }
            Double orderAmount = hmallOmOrder.getOrderAmount();
            Date orderCreationtime = hmallOmOrder.getOrderCreationtime();

            SimpleDateFormat fomat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            OmEdPromote omEdPromote = new OmEdPromote();
            omEdPromote.setOrderAmount(orderAmount);
            omEdPromote.setChannel(hmallOmOrder.getChannelCode());
            omEdPromote.setOrderCreateTime(fomat.format(orderCreationtime));
            omEdPromote.setWebsite(hmallOmOrder.getWebsiteCode());
            omEdPromote.setStore(hmallOmOrder.getStoreCode());
            //查詢所有符合條件促銷 按照優先級排序
            List<OmEdPromote> omEdPromotes = iOmEdPromoteService.queryCondition(omEdPromote);
            List<OmEdPromote> resultList = new ArrayList<OmEdPromote>();
            //该订单符合事后促销
            if(omEdPromotes.size()>0){
                outer :for(OmEdPromote entity : omEdPromotes){
                    if(resultList.size()==0){
                        //判斷該促銷名额是否已满
                        OmPromoteRecord userSelect = new OmPromoteRecord();
                        userSelect.setPromoId(entity.getPromoId());
                        userSelect.setUserId(Long.valueOf(hmallOmOrder.getCustomerId()));
                        //查询该促销用户是否占用
                        int userMax = iOmPromoteRecordService.countByConditon(userSelect);
                        if(userMax>=entity.getMax()){
                            continue;
                        }
                        //查询该促销名额是否已满
                        userSelect.setUserId(null);
                        int userSpace = iOmPromoteRecordService.countByConditon(userSelect);
                        if(entity.getSpace()+SUBSTITUTIONS_NUM >=userSpace){
                            //正常
                            if(entity.getSpace() - userSpace >0){
                                //保存到HMALL_OM_PROMO_RECORD表中 正常
                                OmPromoteRecord saveOmPromoteRecord = new OmPromoteRecord(entity.getPromoId(),Long.valueOf(hmallOmOrder.getCustomerId()),hmallOmOrder.getOrderId(),Status.NEW,"",entity.getCoupon());
                                long resultId = iOmPromoteRecordService.insert(saveOmPromoteRecord);
                                //添加该事后促销内容
                                entity.setRecordId(resultId);
                                entity.setUserStatus(Status.NEW);
                                resultList.add(entity);
                                continue outer;
                            }
                            //替补
                            if((entity.getSpace() - userSpace) <= 0 && (userSpace - entity.getSpace()) < SUBSTITUTIONS_NUM ){
                                OmPromoteRecord saveOmPromoteRecord = new OmPromoteRecord(entity.getPromoId(),Long.valueOf(hmallOmOrder.getCustomerId()),hmallOmOrder.getOrderId(),Status.WATE_NEW,"",entity.getCoupon());
                                long resultId = iOmPromoteRecordService.insert(saveOmPromoteRecord);
                                entity.setRecordId(resultId);
                                entity.setUserStatus(Status.WATE_NEW);
                                resultList.add(entity);
                                continue outer;
                            }
                        }

                    }
                }
            }
            responseData.setResp(resultList);
            return responseData;
        }
    }


    /**
     * 确认收货后校验接口和最后审核
     * @param hmallOmOrder
     * @return
     * @throws ParseException
     */
    @RequestMapping("/checkAfterPromoteWithCofrim")
    @ResponseBody
    public ResponseData checkAfterPromoteWithCofrim(@RequestBody HmallOmOrder hmallOmOrder) throws ParseException {
        synchronized (OmPromoteRecordController.class) {
            ResponseData responseData = new ResponseData();
            ResponseData vaildateParams = vaildateParams(responseData, hmallOmOrder);
            if(StringUtils.isNotEmpty(vaildateParams.getMsg())){
                return responseData;
            }
            List<OmEdPromote> resultList = new ArrayList<OmEdPromote>();
            //订单金额
            Double orderAmount = hmallOmOrder.getOrderAmount();
            //订单编号查询
            OmPromoteRecord selectOne = new OmPromoteRecord();
            selectOne.setOrderId(hmallOmOrder.getOrderId());
            OmPromoteRecord byCondition = iOmPromoteRecordService.findByCondition(selectOne);
            if (byCondition != null) {
                //获取该订单事后促销
                OmEdPromote findOne = new OmEdPromote();
                findOne.setPromoId(byCondition.getPromoId());
                OmEdPromote omEdPromote = iOmEdPromoteService.findByPrimaryKey(findOne);
                // 促销限制金额
                Double omEdPromoteOrderAmount = omEdPromote.getOrderAmount();
                //查询 HMALL_AS_REFUND中STATUS为“FINI”和‘APPROVE’的记录  文档中需要添加待过来参数 orderId
                ResponseData orderResponse = afterSaleClientService.queryByOrderId(hmallOmOrder.getOrderId());
                if (orderResponse.isSuccess()) {
                    double allRefoundSum = 0.0;
                    List<Map<String,?>> resp = (List<Map<String, ?>>) orderResponse.getResp();
                    for (Map<String,?> map : resp) {
                        Double refoundSum = (Double) map.get("refoundSum");
                        allRefoundSum = ArithUtil.add(refoundSum, allRefoundSum);
                    }
                    //1.订单ORDER_AMOUNT减去HMALL_AS_REFUND中STATUS为“FINI”和‘APPROVE’的记录，所有的refund_sum的和   大于等于HMALL_OM_ED_PROMO中的ORDER_AMOUNT金额
                    double sub = ArithUtil.sub(orderAmount, allRefoundSum);
                    //失去资格
                    if (sub < omEdPromoteOrderAmount) {
                        OmPromoteRecord update = new OmPromoteRecord();
                        update.setRecordId(byCondition.getRecordId());
                        //正常失去资格
                        if (byCondition.getStatus().equals(Status.NEW)) {
                            omEdPromote.setUserStatus(Status.CANCEL);
                            update.setStatus(Status.CANCEL);
                            iOmPromoteRecordService.updateOmPromoteRecord(update);
                            omEdPromote.setRecordId(byCondition.getRecordId());
                            resultList.add(omEdPromote);
                        }
                        //替补失去资格
                        if (byCondition.getStatus().equals(Status.WATE_NEW)) {
                            omEdPromote.setUserStatus(Status.WAIT_CANCEL);
                            update.setStatus(Status.WAIT_CANCEL);
                            iOmPromoteRecordService.updateOmPromoteRecord(update);
                            omEdPromote.setRecordId(byCondition.getRecordId());
                            resultList.add(omEdPromote);
                        }

                    } else {
                        omEdPromote.setUserStatus(byCondition.getStatus());
                        omEdPromote.setRecordId(byCondition.getRecordId());
                        resultList.add(omEdPromote);
                    }
                    responseData.setResp(resultList);
                } else {
                    responseData.setSuccess(false);
                    responseData.setMsg("查询退货单错误");
                }
            }
            return responseData;
        }
    }


    public ResponseData vaildateParams(ResponseData responseData,HmallOmOrder hmallOmOrder){
        Date orderCreationtime = hmallOmOrder.getOrderCreationtime();
        Long orderId = hmallOmOrder.getOrderId();
        Double orderAmount = hmallOmOrder.getOrderAmount();
        String channelCode = hmallOmOrder.getChannelCode();
        String customerId = hmallOmOrder.getCustomerId();
        if(orderCreationtime == null){
            responseData.setSuccess(false);
            responseData.setMsg("orderCreationtime参数为空");
        }
        if(orderId == null){
            responseData.setSuccess(false);
            responseData.setMsg("orderId参数为空");
        }
        if(orderAmount == null){
            responseData.setSuccess(false);
            responseData.setMsg("orderAmount参数为空");
        }
        if(channelCode == null){
            responseData.setSuccess(false);
            responseData.setMsg("channelCode参数为空");
        }
        if(customerId == null){
            responseData.setSuccess(false);
            responseData.setMsg("customerId参数为空");
        }
        return responseData;
    }
}