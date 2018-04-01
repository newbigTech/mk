package com.hand.hmall.as.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.mybatis.util.StringUtil;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.as.dto.AsRefund;
import com.hand.hmall.as.dto.AsReturn;
import com.hand.hmall.as.dto.AsReturnEntry;
import com.hand.hmall.as.mapper.AsReturnEntryMapper;
import com.hand.hmall.as.mapper.AsReturnMapper;
import com.hand.hmall.as.service.IAsRefundService;
import com.hand.hmall.as.service.IAsReturnEntryService;
import com.hand.hmall.as.service.IAsReturnService;
import com.hand.hmall.common.service.ISequenceGenerateService;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.om.dto.*;
import com.hand.hmall.om.mapper.ConsignmentMapper;
import com.hand.hmall.om.service.*;
import com.hand.hmall.util.Constants;
import com.hand.hmall.ws.client.IAsOrderPushClient;
import com.hand.hmall.ws.entities.*;
import com.markor.map.external.pointservice.dto.PointOfServiceDto;
import com.markor.map.external.pointservice.service.IPointOfServiceExternalService;
import com.markor.map.framework.restclient.RestClient;
import com.markor.map.framework.soapclient.exceptions.WSCallException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author liuhongxi
 * @version 0.1
 * @name AsReturnServiceImpl
 * @description 退回单ServiceImpl
 * @date 2017/5/24
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class AsReturnServiceImpl extends BaseServiceImpl<AsReturn> implements IAsReturnService {

    //退货单Mapper
    @Autowired
    private AsReturnMapper asReturnMapper;
    @Autowired
    private AsReturnEntryMapper asReturnEntryMapper;
    @Autowired
    private IOrderCouponruleService orderCouponruleService;
    @Autowired
    private IOmPromotionruleService omPromotionruleService;
    @Autowired
    private IAsReturnService asReturnService;

    @Autowired
    private IAsReturnEntryService asReturnEntryService;

    @Autowired
    private IOrderEntryService orderEntryService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IAsRefundService asRefundService;

    @Autowired
    private IAsOrderPushClient orderPushClient;

    @Autowired
    private ISequenceGenerateService sequenceGenerateService;

    @Autowired
    private IConsignmentService consignmentService;

    @Autowired
    private ConsignmentMapper consignmentMapper;

    @Autowired
    private IPointOfServiceExternalService iPointOfServiceExternalService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestClient restClient;

    @Override
    public List<AsReturn> selectReturnById(AsReturn dto) {
        return asReturnMapper.selectReturnById(dto);
    }

    /**
     * 保存退货单信息 退货单行信息
     *
     * @param dto
     * @return
     */
    public ResponseData saveReturn(IRequest iRequest, @RequestBody List<AsReturn> dto, Long asReturnId) {
        AsReturn asReturn = null;
        ResponseData responseData = new ResponseData();
        if (dto.get(0).getAsReturnId() != null) {
            asReturn = asReturnService.selectByPrimaryKey(iRequest, dto.get(0));
            //更新
            if (asReturn != null) {
                if (Constants.FINI.equals(asReturn.getStatus())) {
                    responseData.setSuccess(false);
                    responseData.setMsg("退货单已完成，不能再次修改");
                    return responseData;
                }
                if (!dto.get(0).getReturnType().equals(asReturn.getReturnType())) {
                    responseData.setSuccess(false);
                    responseData.setMsg("退货类型不可修改");
                    return responseData;
                }

                if (Constants.FINI.equals(dto.get(0).getStatus()) && dto.get(0).getFinishTime() == null) {
                    dto.get(0).setFinishTime(new Date());
                }
                dto.get(0).setSyncflag("N");
                asReturn = asReturnService.updateByPrimaryKeySelective(iRequest, dto.get(0));
                asReturnId = asReturn.getAsReturnId();
            }
        }//新增
        else {
            dto.get(0).setCode(sequenceGenerateService.getNextReturnCode());
            if (Constants.FINI.equals(dto.get(0).getStatus()) && dto.get(0).getFinishTime() == null) {
                dto.get(0).setFinishTime(new Date());
            }
            asReturn = asReturnService.insertSelective(iRequest, dto.get(0));
            asReturnId = asReturn.getAsReturnId();

        }
        String isReturnFlag = "";
        //保存订单行
        if (asReturnId != null) {
            if (CollectionUtils.isNotEmpty(dto.get(0).getAsReturnEntryList())) {
                if (dto.get(0).getReturnFee() != null) {
                    isReturnFlag = saveReturnEntry(dto.get(0).getAsReturnEntryList(), iRequest, asReturnId, dto.get(0).getReturnFee(), dto.get(0).getStatus());
                } else {
                    isReturnFlag = saveReturnEntry(dto.get(0).getAsReturnEntryList(), iRequest, asReturnId, 0, dto.get(0).getStatus());
                }
            }
        }

        //判断返回状态
        if (asReturn == null) {
            responseData.setSuccess(false);
            responseData.setMsg("系统错误");
        } else if (Constants.ZERO.equals(isReturnFlag)) {
            responseData.setSuccess(false);
            responseData.setMsg("退货单已关联退款单不能取消");
        } else if (Constants.ONE.equals(isReturnFlag)) {
            responseData.setSuccess(false);
            responseData.setMsg("不能将已取消的退货单行改为正常状态");
        } else if (Constants.TWO.equals(isReturnFlag)) {
            //修改订单行未分配退货单数量为0
            asReturnEntryService.updateOrderEntrynotReturnQuantity(asReturn);
            asReturn.setStatus("CANC");
            asReturnService.updateByPrimaryKeySelective(iRequest, asReturn);
            List<AsReturn> list = new ArrayList<>();
            list.add(asReturn);
            responseData.setSuccess(true);
            responseData.setResp(list);
        } else {
            //修改订单行未分配退货单数量为0
            asReturnEntryService.updateOrderEntrynotReturnQuantity(asReturn);
            List<AsReturn> list = new ArrayList<>();
            list.add(asReturn);
            responseData.setSuccess(true);
            responseData.setResp(list);
        }
        if (responseData.isSuccess()) {
            Order order = new Order();
            AsReturn returnInfo = asReturnMapper.selectByPrimaryKey(asReturn);
            order.setOrderId(returnInfo.getOrderId());
            order.setRefundAmount(orderService.getTotalRefundAmount(iRequest, order).getRefundAmount());
            orderService.updateByPrimaryKeySelective(iRequest, order);
            logger.info("*************************新建退货同步商城*****************************");
            logger.info("" + returnInfo.getOrderId());
            //新建退货同步商城
            orderService.orderSyncZmall(returnInfo.getOrderId());
            logger.info("*************************新建退货同步商城结束*****************************");
        }

        return responseData;
    }

    /**
     * @param dto
     * @param flag
     * @return
     * @description 获取非取消的normal状态的所有订单行 以便调用促销微服务查询促销信息
     */
    @Override
    public OrderPojo selectOrderEntryByPromote(List<OrderEntry> dto, String flag) {
        //获取非取消的normal状态的所有订单行 以便调用促销微服务查询促销信息
        List<OrderEntry> list = orderEntryService.selectReturnOrderEntry(RequestHelper.newEmptyRequest(), dto);
        //组装调用促销接口需要的数据
        OrderPojo orderPojo = orderEntryService.changePojo(RequestHelper.newEmptyRequest(), list, flag);
        //将退货
        List<OrderEntryPojo> orderEntryPojoList = orderPojo.getOrderEntryList();
        if (orderEntryPojoList != null && orderEntryPojoList.size() > 0) {
            List<OrderEntryPojo> orderEntryList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(dto)) {
                //换转退调用促销接口
                if ("Y".equals(dto.get(0).getChangeToReturn())) {
                    //查询退货单行
                    AsReturnEntry asReturnEntryCondition = new AsReturnEntry();
                    asReturnEntryCondition.setAsReturnId(dto.get(0).getAsReturnId());
                    List<AsReturnEntry> asReturnEntryList = asReturnEntryMapper.queryReturnEntryById(asReturnEntryCondition);
                    //在调用时，将“normal”订单行的当前数量传给微服务（当前数量=原订单数量-已退货数量-待退货数量【待退货数量的计算逻辑为：
                    // （1）查找该换退单行id并与该换退单关联销售订单行id进行匹配，若匹配成功，则将换退单行数量加到该销售订单的待退货数量上】
                    for (OrderEntryPojo orderEntryPojo : orderEntryPojoList) {
                        for (AsReturnEntry asReturnEntry : asReturnEntryList) {
                            if ((long) asReturnEntry.getOrderEntryId() == (long) orderEntryPojo.getOrderEntryId()) {
                                orderEntryPojo.setQuantity(orderEntryPojo.getQuantity() - (asReturnEntry.getQuantity() != null ? asReturnEntry.getQuantity().intValue() : 0));
                            }
                        }
                    }
                } else {
                    //将已退货数量去掉
                    for (int i = 0; i < orderEntryPojoList.size(); i++) {
                        if (orderEntryPojoList.get(i).getReturnedQuantity() != null && orderEntryPojoList.get(i).getReturnedQuantity() > 0) {
                            orderEntryPojoList.get(i).setQuantity(orderEntryPojoList.get(i).getQuantity() - orderEntryPojoList.get(i).getReturnedQuantity());
                        }
                        for (int j = 0; j < dto.size(); j++) {
                            if (dto.get(j).getOrderEntryId() != null) {
                                if ((long) orderEntryPojoList.get(i).getOrderEntryId() == (long) dto.get(j).getOrderEntryId()) {
                                    orderEntryPojoList.get(i).setQuantity(dto.get(j).getQuantity());
                                }
                            }
                        }
                    }
                }
            }
            //判断订单行剩余数量是否为0
            for (int i = 0; i < orderEntryPojoList.size(); i++) {
                if (orderEntryPojoList.get(i).getQuantity() != 0) {
                    orderEntryList.add(orderEntryPojoList.get(i));
                }
            }
            orderPojo.setOrderEntryList(orderEntryList);
        }
        return orderPojo;
    }

    /**
     * 保存退货单行
     *
     * @param list
     * @param iRequest
     */
    private String saveReturnEntry(List<AsReturnEntry> list, IRequest iRequest, Long asReturnId, double returnFee, String returnStatus) {
        //查询是否有关联的状态不是已取消的退款单
        //如果该退货单已经关联状态不为“已取消”的退款单，即退款单表中存在【refund.return_id】=该退货单，且【refund.status】不为已取消“CANC”的退款单，则不能修改退货单行状态为“CANCEL”
        boolean isReturnFlag = true;
        AsRefund asRefund = new AsRefund();
        asRefund.setReturnId(BigDecimal.valueOf(asReturnId));
        List<AsRefund> refundList = asRefundService.queryRefundOrderByReturnId(asRefund);
        if (CollectionUtils.isNotEmpty(refundList)) {
            isReturnFlag = false;
        }
        for (int i = 0; i < list.size(); i++) {
            if ((Constants.CANCEL.equals(list.get(i).getReturnentryStatus()) || Constants.CANCEL.equals(returnStatus)) && !isReturnFlag) {
                return Constants.ZERO;
            }
        }
        //判断退货单行是否已取消 已取消的不能再改为正常的
        for (int i = 0; i < list.size(); i++) {
            AsReturnEntry asReturnEntry = asReturnEntryService.selectByPrimaryKey(iRequest, list.get(i));
            if (asReturnEntry != null) {
                if (Constants.CANCEL.equals(asReturnEntry.getReturnentryStatus() != null ? asReturnEntry.getReturnentryStatus() : "") && Constants.NORMAL.equals(list.get(i).getReturnentryStatus() != null ? list.get(i).getReturnentryStatus() : "")) {
                    return Constants.ONE;
                }
            }
        }

        DecimalFormat df = new DecimalFormat("######0.00");
        //所有退货单行总销售金额
        double totalFee = 0;
        for (int i = 0; i < list.size(); i++) {
            totalFee = totalFee + ((list.get(i).getBasePrice() != null ? list.get(i).getBasePrice() : 0) * (list.get(i).getQuantity() != null ? list.get(i).getQuantity() : 0));
        }
        //分摊给每行的金额
        double returnEntryFee = 0;
        //每行退款金额累加，当最后一行时用总退款金额减去累加值
        double returnTotalFee = 0;

        for (int i = 0; i < list.size(); i++) {
            if (totalFee > 0) {
                if (i == list.size() - 1) {
                    list.get(i).setReturnFee(Double.parseDouble(df.format(returnFee - returnTotalFee)));
                } else {
                    returnEntryFee = ((list.get(i).getBasePrice() != null ? list.get(i).getBasePrice() : 0) * (list.get(i).getQuantity() != null ? list.get(i).getQuantity() : 0) / totalFee) * returnFee;
                    list.get(i).setReturnFee(Double.parseDouble(df.format(returnEntryFee)));
                    returnTotalFee = returnTotalFee + returnEntryFee;
                }
            }
            if (list.get(i).getAsReturnEntryId() != null) {
                //如果退货单状态为取消则将所有退货单行状态改为取消
                if ("CANC".equals(returnStatus)) {
                    list.get(i).setReturnentryStatus(Constants.CANCEL);
                    AsReturnEntry asReturnEntry = asReturnEntryService.selectByPrimaryKey(iRequest, list.get(i));
                    if (Constants.NORMAL.equals(asReturnEntry.getReturnentryStatus())) {
                        updateReturnedQuantity(iRequest, list.get(i));
                    }
                } else {
                    //退货单行状态为取消 修改关联的订单行已退货数量
                    if (Constants.CANCEL.equals(list.get(i).getReturnentryStatus())) {
                        AsReturnEntry asReturnEntry = asReturnEntryService.selectByPrimaryKey(iRequest, list.get(i));
                        if (Constants.NORMAL.equals(asReturnEntry.getReturnentryStatus())) {
                            updateReturnedQuantity(iRequest, list.get(i));
                        }
                    }
                }
                asReturnEntryService.updateByPrimaryKeySelective(iRequest, list.get(i));
            } else {
                if ("CANC".equals(returnStatus)) {
                    list.get(i).setAsReturnId(asReturnId);
                    list.get(i).setReturnentryStatus(Constants.CANCEL);
                    updateReturnedQuantity(iRequest, list.get(i));
                    asReturnEntryService.insertSelective(iRequest, list.get(i));
                } else {
                    list.get(i).setAsReturnId(asReturnId);
                    list.get(i).setReturnentryStatus(Constants.NORMAL);
                    asReturnEntryService.insertSelective(iRequest, list.get(i));
                    //增加书面记录
                    AsReturn asReturn = new AsReturn();
                    asReturn.setAsReturnId(list.get(i).getAsReturnId());
                    AsReturn asReturnInfo = asReturnMapper.selectByPrimaryKey(asReturn);
                    AsReturnEntry returnEntryInfo = asReturnEntryMapper.selectByPrimaryKey(list.get(i));
                    HmallSoChangeLog soChangeLog = new HmallSoChangeLog();
                    soChangeLog.setOrderId(returnEntryInfo.getAsReturnId());
                    soChangeLog.setOrderEntryId(returnEntryInfo.getAsReturnEntryId());
                    soChangeLog.setOrderType("2");
                    soChangeLog.setQuantity(-returnEntryInfo.getQuantity().intValue());
                    soChangeLog.setChangeQuantity(-returnEntryInfo.getQuantity().intValue());
                    soChangeLog.setTotalFee(new BigDecimal("0").subtract(new BigDecimal(returnEntryInfo.getReturnFee().toString())));
                    soChangeLog.setChangeFee(new BigDecimal("0").subtract(new BigDecimal(returnEntryInfo.getReturnFee().toString())));
                    soChangeLog.setParentOrderId(asReturnInfo.getOrderId());
                    soChangeLog.setParentOrderEntryId(returnEntryInfo.getOrderEntryId());
                    soChangeLog.setProductId(Long.valueOf(returnEntryInfo.getProductId()));
                    soChangeLog.setPin(returnEntryInfo.getPin());
                    orderService.addSoChangeLog(soChangeLog);

                }
            }
        }
        //将组件的退货原因变成套件头的退货原因
        for (AsReturnEntry asReturnEntry : list) {
            if (asReturnEntry.getParentLine() == null) {
                asReturnEntry = asReturnEntryService.selectByPrimaryKey(iRequest, asReturnEntry);
                AsReturnEntry con = new AsReturnEntry();
                con.setParentLine(asReturnEntry.getOrderEntryId());
                con.setReturnReason1(StringUtils.isEmpty(asReturnEntry.getReturnReason1()) ? "" : asReturnEntry.getReturnReason1());
                con.setReturnReason2(StringUtils.isEmpty(asReturnEntry.getReturnReason2()) ? "" : asReturnEntry.getReturnReason2());
                asReturnEntryService.updateReturnEntryReturnReason(con);
            }
        }

        //判断是否所有退货单行都已经取消 如果都取消将退货单状态改为取消
        boolean isAllReturn = true;
        for (int i = 0; i < list.size(); i++) {
            if (Constants.NORMAL.equals(list.get(i).getReturnentryStatus())) {
                isAllReturn = false;
            }
        }
        if (isAllReturn) {
            return Constants.TWO;
        }
        return "";
    }

    private void updateReturnedQuantity(IRequest iRequest, AsReturnEntry asReturnEntry) {
        OrderEntry orderEntry = new OrderEntry();
        orderEntry.setOrderEntryId(asReturnEntry.getOrderEntryId());
        orderEntry = orderEntryService.selectByPrimaryKey(iRequest, orderEntry);
        //由于换转退没有修改订单行已退货数量，需要添加判断
        if (orderEntry.getReturnedQuantity() != null && orderEntry.getReturnedQuantity() > 0) {
            orderEntry.setReturnedQuantity(orderEntry.getReturnedQuantity() - asReturnEntry.getQuantity().intValue());
            orderEntryService.updateByPrimaryKey(iRequest, orderEntry);
            //增加书面记录
            AsReturn asReturn = new AsReturn();
            asReturn.setAsReturnId(asReturnEntry.getAsReturnId());
            AsReturn asReturnInfo = asReturnMapper.selectByPrimaryKey(asReturn);
            AsReturnEntry returnEntryInfo = asReturnEntryMapper.selectByPrimaryKey(asReturnEntry);
            HmallSoChangeLog soChangeLog = new HmallSoChangeLog();
            soChangeLog.setOrderId(returnEntryInfo.getAsReturnId());
            soChangeLog.setOrderEntryId(returnEntryInfo.getAsReturnEntryId());
            soChangeLog.setOrderType("2");
            soChangeLog.setQuantity(returnEntryInfo.getQuantity().intValue());
            soChangeLog.setChangeQuantity(returnEntryInfo.getQuantity().intValue());
            soChangeLog.setTotalFee(new BigDecimal(returnEntryInfo.getReturnFee().toString()));
            soChangeLog.setChangeFee(new BigDecimal(returnEntryInfo.getReturnFee().toString()));
            soChangeLog.setParentOrderId(asReturnInfo.getOrderId());
            soChangeLog.setParentOrderEntryId(returnEntryInfo.getOrderEntryId());
            soChangeLog.setProductId(Long.valueOf(returnEntryInfo.getProductId()));
            soChangeLog.setPin(returnEntryInfo.getPin());
            orderService.addSoChangeLog(soChangeLog);
        }
    }

    /**
     * 发送至Retail
     *
     * @param asReturnId
     * @param iRequest
     * @return ResponseData
     */
    public ResponseData sendToRetail(Long asReturnId, IRequest iRequest) {
        //查出数据
        AsReturn asReturn = asReturnMapper.queryForRetail(asReturnId);
        //准备结果
        ResponseData responseData = new ResponseData();
        if (StringUtils.isEmpty(asReturn.getSapCode())) {
            try {
                String sapCode = sequenceGenerateService.getNextNumber("sapCode", 9L, "D");
                AsCreateResponseBody responseBody = orderPushClient.orderPush(createRequestConstructor(asReturn, iRequest, sapCode));
                List<AsCreateGdtReturnItems> items = responseBody.getAsCreateGdtReturn().getItems();
                if ("S".equals(items.get(0).getType()) || (items.size() > 1 && "S".equals(items.get(1).getType()))) {
                    //更新标识位和SAP_CODE
                    asReturn.setSapCode(sapCode);
                    asReturnMapper.updateSyncFlag(asReturn);
                    responseData.setSuccess(true);
                    responseData.setMsgCode(sapCode);
                    responseData.setMsg("同步成功！");
                } else {
                    responseData.setSuccess(false);
                    String msg = "";
                    if (!StringUtils.isEmpty(items.get(0).getMessage())) {
                        msg = msg + items.get(0).getMessage();
                    }
                    if (items.size() > 1 && !StringUtils.isEmpty(items.get(1).getMessage())) {
                        msg = msg + items.get(1).getMessage();
                    }
                    responseData.setMsg(msg);
                }
            } catch (WSCallException e) {
                logger.error(this.getClass().getName(), e);
                responseData.setSuccess(false);
                responseData.setMsg("同步过程中发生异常。");
            }
        } else {
            String sapCode = asReturn.getSapCode();
            try {
                AsChangeResponseBody responseBody = orderPushClient.orderUpdate(changeRequestConstructor(asReturn, iRequest));
                List<AsChangeGdtReturnItems> items = responseBody.getAsChangeGdtReturn().getAsChangeGdtReturnItems();
                if ("S".equals(items.get(0).getType1()) || (items.size() > 1 && "S".equals(items.get(1).getType1()))) {
                    responseData.setSuccess(true);
                    responseData.setMsg("同步成功！");
                    responseData.setMsgCode(sapCode);
                    asReturnMapper.updateSyncFlag(asReturn);
                } else {
                    responseData.setSuccess(false);
                    String msg = "";
                    if (!StringUtils.isEmpty(items.get(0).getMsg())) {
                        msg = msg + items.get(0).getMsg();
                    }
                    if (items.size() > 1 && !StringUtils.isEmpty(items.get(1).getMsg())) {
                        msg = msg + items.get(1).getMsg();
                    }
                    responseData.setMsgCode(sapCode);
                    responseData.setMsg(msg);
                }
            } catch (WSCallException e) {
                logger.error(this.getClass().getName(), e);
                responseData.setSuccess(false);
                responseData.setMsgCode(sapCode);
                responseData.setMsg("同步过程中发生异常。");
            }
        }

        return responseData;
    }

    /**
     * 根据订单ID查询用户信息
     *
     * @param asReturn
     * @return
     */
    @Override
    public List<AsReturn> selectUserInfoByOrderId(AsReturn asReturn) {
        DecimalFormat df = new DecimalFormat("######0.00");
        /*
        “建议退款金额”【refund.reference_sum】=“已支付金额”【order.payment_amount】-“当前应付金额”【order.current_amount】-退货行运费安装费（值见备注1）-∑（【refund.order_id】=该订单ID，
         且状态【REFUND.STATUS】不为“CANC”的退款单的“建议退款金额”【reference_sum】）
        备注1：
        退货行运费安装费=状态为NORMAL的订单行的（“已退货数量”【returned_quantity】)/订单行数量【QUANTITY】*该订单行的（运费【SHIPPING_FEE】+安装费【INSTALLATION_FEE】）
        */
        List<AsReturn> asReturnList = asReturnMapper.selectUserInfoByOrderId(asReturn);
        //天猫退货单不计算建议退款金额
        if (Constants.ONE.equals(asReturn.getWebsiteId())) {
            //退款单的“建议退款金额”
            AsRefund asRefund = new AsRefund();
            asRefund.setOrderId(asReturn.getOrderId());
            List<AsRefund> asRefundList = asRefundService.queryRefundOrderByOrderId(asRefund);
            double refundReferenceSum = 0;
            if (CollectionUtils.isNotEmpty(asRefundList)) {
                for (AsRefund obj : asRefundList) {
                    refundReferenceSum = refundReferenceSum + (obj.getReferenceSum() != null ? obj.getReferenceSum().doubleValue() : 0);
                }
            }
            double returnReferenceSum = 0;
            //退货单的建议退款金额
            BigDecimal bigDecimal = asReturnMapper.selectReturnByOrderId(asReturn);
            if (bigDecimal != null) {
                returnReferenceSum = bigDecimal.doubleValue();
            }

            //计算退货行安装费
            double returnFee = 0;
            OrderEntry orderEntry = new OrderEntry();
            orderEntry.setOrderId(asReturn.getOrderId());
            List<OrderEntry> orderEntryList = orderEntryService.selectFeeByOrderId(orderEntry);
            for (OrderEntry obj : orderEntryList) {
                if (obj.getReturnedQuantity() != null && obj.getReturnedQuantity() > 0) {
                    returnFee = returnFee + ((obj.getReturnedQuantity() / obj.getQuantity().doubleValue()) * ((obj.getShippingFee() != null ? obj.getShippingFee() : 0) + (obj.getInstallationFee() != null ? obj.getInstallationFee() : 0)));
                }
            }
            if (CollectionUtils.isNotEmpty(asReturnList)) {
                double referenceSum = (asReturnList.get(0).getPaymentAmount() != null ? asReturnList.get(0).getPaymentAmount() : 0) - (asReturnList.get(0).getCurrentAmount() != null ? asReturnList.get(0).getCurrentAmount() : 0);
                asReturnList.get(0).setReferenceFee(Double.valueOf(df.format((referenceSum - refundReferenceSum - returnFee - returnReferenceSum) > 0 ? (referenceSum - refundReferenceSum - returnFee - returnReferenceSum) : 0)));
            }

            //带出发货单上的服务点
            //查询订单关联的所有发货单
            Consignment consignment = new Consignment();
            consignment.setOrderId(asReturn.getOrderId());
            List<Consignment> consignments = consignmentMapper.queryInfo(consignment);
            //判断其中是否有物流类型的发货单，如果有，取出其中的服务中心，如果没有，则取第一个发货单的服务中心
            if (consignments.size() > 0) {
                for (Consignment c : consignments) {
                    if ("LOGISTICS".equals(c.getShippingType())) {
                        asReturnList.get(0).setReceivePosition(c.getPointCode());
                        break;
                    }
                }
                asReturnList.get(0).setReceivePosition(!StringUtils.isEmpty(asReturnList.get(0).getReceivePosition()) ? asReturnList.get(0).getReceivePosition() : consignments.get(0).getPointCode());
                PointOfServiceDto ps = iPointOfServiceExternalService.selectByCode(asReturnList.get(0).getReceivePosition());
                if (ps != null) {
                    asReturnList.get(0).setReceivePositionText(ps.getDisplayname());
                }
            }
        }
        if (!CollectionUtils.isEmpty(asReturnList) && asReturnList.get(0).getReturnFee() == null) {
            asReturnList.get(0).setReturnFee(asReturnList.get(0).getReferenceFee());
        }
        return asReturnList;
    }

    @Override
    public void changeToReturn(IRequest request, AsReturn asReturn) {
        //修改退货类型为“退货”
        asReturn.setReturnType("R01");
        asReturnMapper.updateByPrimaryKeySelective(asReturn);
        //关闭换发单
        if (asReturn.getSwapOrderId() != null) {
            Order o = new Order();
            o.setOrderId(asReturn.getSwapOrderId());
            Order order = orderService.selectByPrimaryKey(request, o);
            order.setOrderStatus("TRADE_CANCELLED");
            orderService.updateByPrimaryKeySelective(request, order);
            logger.info(asReturn.getSwapOrderId() + "换发单关闭成功!");

            //关闭发货单
            Consignment c = new Consignment();
            c.setOrderId(asReturn.getSwapOrderId());
            List<Consignment> consignmentList = consignmentService.select(c);
            if (CollectionUtils.isNotEmpty(consignmentList)) {
                for (Consignment consignment : consignmentList) {
                    consignment.setStatus("TRADE_CLOSED");
                    consignmentService.updateByPrimaryKeySelective(request, consignment);
                }
                logger.info("订单" + asReturn.getOrderId() + "关联的所有发货单关闭成功!");
            }

            //关闭订单行
            OrderEntry oe = new OrderEntry();
            oe.setOrderId(asReturn.getSwapOrderId());
            List<OrderEntry> orderEntryList = orderEntryService.select(oe);
            if (CollectionUtils.isNotEmpty(orderEntryList)) {
                for (OrderEntry orderEntry : orderEntryList) {
                    orderEntry.setOrderEntryStatus("CANCEL");
                    orderEntryService.updateByPrimaryKeySelective(request, orderEntry);
                    //增加书面记录
                    HmallSoChangeLog soChangeLog = new HmallSoChangeLog();
                    OrderEntry orderEntryInfo = orderEntryService.selectByPrimaryKey(request, orderEntry);
                    soChangeLog.setOrderId(orderEntryInfo.getOrderId());
                    soChangeLog.setOrderEntryId(orderEntryInfo.getOrderEntryId());
                    soChangeLog.setOrderType("1");
                    soChangeLog.setPin(orderEntryInfo.getPin());
                    soChangeLog.setProductId(orderEntryInfo.getProductId());
                    orderService.addSoChangeLog(soChangeLog);
                }
                logger.info("订单" + asReturn.getOrderId() + "关联的所有订单行关闭成功!");
            }

        }
    }

    @Override
    public void cancelReturnGood(IRequest request, AsReturn asReturn) {
        //关闭退货单
        if ("N".equals(asReturn.getSyncflag())) {
            asReturn.setStatus("CANC");
            asReturnMapper.updateByPrimaryKeySelective(asReturn);
        } else if ("Y".equals(asReturn.getSyncflag())) {
            //先将所有的退货单的行状态置为取消状态
            List<AsReturnEntry> asReturnEntryList = asReturn.getAsReturnEntryList();
            if (CollectionUtils.isNotEmpty(asReturnEntryList)) {
                for (AsReturnEntry asReturnEntry : asReturnEntryList) {
                    asReturnEntry.setReturnentryStatus("CANCEL");
                    asReturnEntryService.updateByPrimaryKeySelective(request, asReturnEntry);
                    //增加书面记录
                    HmallSoChangeLog soChangeLog = new HmallSoChangeLog();
                    AsReturn asReturnInfo = asReturnMapper.selectByPrimaryKey(asReturn);
                    AsReturnEntry returnEntryInfo = asReturnEntryMapper.selectByPrimaryKey(asReturnEntry);
                    soChangeLog.setOrderId(asReturnInfo.getAsReturnId());
                    soChangeLog.setOrderEntryId(returnEntryInfo.getAsReturnEntryId());
                    soChangeLog.setOrderType("2");
                    soChangeLog.setQuantity(returnEntryInfo.getQuantity().intValue());
                    soChangeLog.setChangeQuantity(returnEntryInfo.getQuantity().intValue());
                    soChangeLog.setTotalFee(new BigDecimal(returnEntryInfo.getReturnFee().toString()));
                    soChangeLog.setChangeFee(new BigDecimal(returnEntryInfo.getReturnFee().toString()));
                    soChangeLog.setParentOrderId(asReturnInfo.getOrderId());
                    soChangeLog.setParentOrderEntryId(returnEntryInfo.getOrderEntryId());
                    soChangeLog.setProductId(Long.valueOf(returnEntryInfo.getProductId()));
                    soChangeLog.setPin(returnEntryInfo.getPin());
                    orderService.addSoChangeLog(soChangeLog);
                }
            }
            //调用retail接口
            ResponseData responseData = this.sendToRetail(asReturn.getAsReturnId(), request);
            if (responseData.isSuccess()) {
                asReturn.setStatus("CANC");
                asReturnMapper.updateByPrimaryKeySelective(asReturn);
            } else {
                throw new RuntimeException(responseData.getMsg());
            }
        } else {
            throw new RuntimeException("本退货单同步retail标记syncflag字段为空!");
        }

        if ("R01".equals(asReturn.getReturnType())) {
            List<AsReturnEntry> asReturnEntryList = asReturn.getAsReturnEntryList();
            if (CollectionUtils.isNotEmpty(asReturnEntryList)) {
                for (AsReturnEntry asReturnEntry : asReturnEntryList) {
                    OrderEntry oe = new OrderEntry();
                    oe.setOrderEntryId(asReturnEntry.getOrderEntryId());
                    OrderEntry orderEntry = orderEntryService.selectByPrimaryKey(request, oe);
                    if (orderEntry != null) {
                        //更新原订单行上的字段“已退货数量”
                        if (asReturnEntry.getQuantity() != null) {
                            orderEntry.setReturnedQuantity(orderEntry.getReturnedQuantity().intValue() - asReturnEntry.getQuantity().intValue());
                            orderEntryService.updateByPrimaryKeySelective(request, orderEntry);
                        }
                    } else {
                        throw new RuntimeException("订单行" + asReturnEntry.getOrderEntryId() + "不存在!");
                    }
                }
            }
        } else if ("R03".equals(asReturn.getReturnType())) {
            Order o = new Order();
            o.setOrderId(asReturn.getSwapOrderId());
            Order order = orderService.selectByPrimaryKey(request, o);
            if (order != null) {
                //关闭换发单
                order.setOrderStatus("TRADE_CANCELLED");
                orderService.updateByPrimaryKeySelective(request, order);

                //关闭换发单对应的发货单
                Consignment con = new Consignment();
                con.setOrderId(order.getOrderId());
                List<Consignment> consignmentList = consignmentService.select(con);
                if (CollectionUtils.isNotEmpty(consignmentList)) {
                    for (Consignment consignment : consignmentList) {
                        consignment.setStatus("TRADE_CLOSED");
                        consignmentService.updateByPrimaryKeySelective(request, consignment);
                    }
                }

                //关闭换发单关联的订单行
                OrderEntry oe = new OrderEntry();
                oe.setOrderId(order.getOrderId());
                List<OrderEntry> orderEntryList = orderEntryService.select(oe);
                if (CollectionUtils.isNotEmpty(orderEntryList)) {
                    for (OrderEntry orderEntry : orderEntryList) {
                        orderEntry.setOrderEntryStatus("CANCEL");
                        orderEntryService.updateByPrimaryKeySelective(request, orderEntry);
                        //增加书面记录
                        HmallSoChangeLog soChangeLog = new HmallSoChangeLog();
                        OrderEntry orderEntryInfo = orderEntryService.selectByPrimaryKey(request, orderEntry);
                        soChangeLog.setOrderId(orderEntryInfo.getOrderId());
                        soChangeLog.setOrderEntryId(orderEntryInfo.getOrderEntryId());
                        soChangeLog.setOrderType("1");
                        soChangeLog.setPin(orderEntryInfo.getPin());
                        soChangeLog.setProductId(orderEntryInfo.getProductId());
                        orderService.addSoChangeLog(soChangeLog);
                    }
                }
            } else {
                throw new RuntimeException("本退货单找不到关联的换发单!");
            }
            Order orderInfo = new Order();
            AsReturn returnInfo = asReturnMapper.selectByPrimaryKey(asReturn);
            orderInfo.setOrderId(returnInfo.getOrderId());
            orderInfo.setRefundAmount(orderService.getTotalRefundAmount(request, orderInfo).getRefundAmount());
            orderService.updateByPrimaryKeySelective(request, orderInfo);
            logger.info("*************************取消退货同步商城*****************************");
            logger.info("" + returnInfo.getOrderId());
            //新建退货同步商城
            orderService.orderSyncZmall(returnInfo.getOrderId());
            logger.info("*************************取消退货同步商城结束*****************************");

        }

    }

    @Override
    public List<AsReturn> selectDateForReturnToRetail(IRequest request) {
        return asReturnMapper.selectDateForReturnToRetail();
    }

    /**
     * 换转退后续操作，更新促销信息，订单行数量，重新计算建议退款金额
     *
     * @param request
     * @param dto
     * @param currentAmount
     * @param chosenCoupon
     * @param chosenPromotion
     * @return
     */
    @Override
    public ResponseData changeToReturnDetail(IRequest request, List<OrderEntry> dto, Double currentAmount, String chosenCoupon, String chosenPromotion) {
        ResponseData responseData = new ResponseData();
        try {
            //根据orderId更新订单当前需要付款字段 优惠券 促销
            Order order = new Order();
            order.setCurrentAmount(currentAmount);
            order.setChosenCoupon(chosenCoupon);
            order.setChosenPromotion(chosenPromotion);
            if (dto != null && dto.size() > 0) {
                OrderEntry orderEntry = dto.get(0);
                order.setOrderId(orderEntry.getOrderId());

                AsReturn asReturn = new AsReturn();
                asReturn.setAsReturnId(orderEntry.getAsReturnId());
                asReturn = asReturnMapper.selectByPrimaryKey(asReturn);

                if (asReturn != null && !"R03".equals(asReturn.getReturnType())) {
                    responseData.setMsg("操作单据不为换退类型");
                    responseData.setSuccess(false);
                    return responseData;
                }

                asReturn.setOrderId(orderEntry.getOrderId());
                asReturn.setSwapOrderId(orderEntry.getSwapOrderId());
                //执行原还转退操作
                changeToReturn(request, asReturn);
                //更新促销信e息
                updateSale(request, chosenCoupon, chosenPromotion, order, orderEntry);
                //更新订单表
                orderService.updateByPrimaryKeySelective(request, order);

                AmountChangeLog changeLog = null;
                //增加金额变更记录头
                order = orderService.selectByPrimaryKey(request, order);
                if (order != null) {
                    order.setPromotionResult(orderEntry.getPromotionResult());
                    if (order.getPromotionResult() != null) {
                        changeLog = orderService.addAmountChangeLog(order);
                    }
                }
                //根据退货单ID查询退货单行
                AsReturnEntry asReturnEntryCondition = new AsReturnEntry();
                asReturnEntryCondition.setAsReturnId(orderEntry.getAsReturnId());
                List<AsReturnEntry> asReturnEntryList = asReturnEntryMapper.queryReturnEntryById(asReturnEntryCondition);

                //根据退货单行表的字段【return_entry.orer_entry_id】找到对应的订单行ID，将退货行数量【return.entry.quantity】累加到对应订单行的“已退货数量”【order_entry.returned_quantity】中
                for (AsReturnEntry asReturnEntry : asReturnEntryList) {
                    OrderEntry oe = new OrderEntry();
                    oe.setOrderEntryId(asReturnEntry.getOrderEntryId());
                    oe = orderEntryService.selectByPrimaryKey(request, oe);
                    if (oe != null) {
                        oe.setReturnedQuantity(oe.getReturnedQuantity() + asReturnEntry.getQuantity().intValue());
                        orderEntryService.updateByPrimaryKeySelective(request, oe);
                    }
                    //增加书面记录
                    HmallSoChangeLog soChangeLog = new HmallSoChangeLog();
                    AsReturn returnInfo = asReturnMapper.selectByPrimaryKey(asReturn);
                    AsReturnEntry returnEntryInfo = asReturnEntryMapper.selectByPrimaryKey(asReturnEntry);
                    soChangeLog.setOrderId(returnInfo.getAsReturnId());
                    soChangeLog.setOrderEntryId(returnEntryInfo.getAsReturnEntryId());
                    soChangeLog.setOrderType("2");
                    soChangeLog.setQuantity(-returnEntryInfo.getQuantity().intValue());
                    soChangeLog.setChangeQuantity(-returnEntryInfo.getQuantity().intValue());
                    soChangeLog.setTotalFee(new BigDecimal("0").subtract(new BigDecimal(returnEntryInfo.getReturnFee().toString())));
                    soChangeLog.setChangeFee(new BigDecimal("0").subtract(new BigDecimal(returnEntryInfo.getReturnFee().toString())));
                    soChangeLog.setParentOrderId(returnInfo.getOrderId());
                    soChangeLog.setParentOrderEntryId(returnEntryInfo.getOrderEntryId());
                    soChangeLog.setProductId(Long.valueOf(returnEntryInfo.getProductId()));
                    soChangeLog.setPin(returnEntryInfo.getPin());
                    orderService.addSoChangeLog(soChangeLog);
                }
                //增加金额变更记录行
                List<OrderEntry> orderEntryList = orderEntryService.selectByOrderId(order.getOrderId());
                if (CollectionUtils.isNotEmpty(orderEntryList) && changeLog != null) {
                    for (OrderEntry orderEntry1 : orderEntryList) {
                        orderService.addAmountChangeLogEntry(changeLog, order, orderEntry1);
                    }
                }
                //重新计算建议退款金额 并存入退货单中
                asReturn.setWebsiteId("1");
                List<AsReturn> asReturnList = selectUserInfoByOrderId(asReturn);
                if (asReturnList != null && asReturnList.size() > 0) {
                    AsReturn asReturnFee = new AsReturn();
                    asReturnFee.setAsReturnId(asReturn.getAsReturnId());
                    asReturnFee.setReferenceFee(asReturnList.get(0).getReferenceFee());
                    asReturnFee.setReturnFee(asReturnList.get(0).getReferenceFee());
                    asReturnMapper.updateByPrimaryKeySelective(asReturnFee);

                    //将建议退款金额分摊到退货单行上
                    DecimalFormat df = new DecimalFormat("######0.00");
                    BigDecimal returnFee = new BigDecimal(asReturnList.get(0).getReferenceFee().toString());
                    //所有退货单行总销售金额
                    BigDecimal totalFee = new BigDecimal("0");
                    for (int i = 0; i < asReturnEntryList.size(); i++) {
                        totalFee = totalFee.add((new BigDecimal(asReturnEntryList.get(i).getBasePrice().toString()).multiply(new BigDecimal(asReturnEntryList.get(i).getQuantity().toString()))));
                    }
                    //分摊给每行的金额
                    BigDecimal returnEntryFee;
                    //每行退款金额累加，当最后一行时用总退款金额减去累加值
                    BigDecimal returnTotalFee = new BigDecimal("0");
                    for (int i = 0; i < asReturnEntryList.size(); i++) {
                        if (asReturnList.get(0).getReferenceFee() > 0) {
                            if (i == asReturnEntryList.size() - 1) {
                                asReturnEntryList.get(i).setReturnFee(Double.parseDouble(df.format(returnFee.subtract(returnTotalFee))));
                            } else {
                                returnEntryFee = (new BigDecimal(asReturnEntryList.get(i).getBasePrice().toString()).multiply(new BigDecimal(asReturnEntryList.get(i).getQuantity().toString())).divide(totalFee)).multiply(returnFee);
                                asReturnEntryList.get(i).setReturnFee(Double.parseDouble(df.format(returnEntryFee)));
                                returnTotalFee = returnTotalFee.add(returnEntryFee);
                            }
                        }
                        asReturnEntryMapper.updateByPrimaryKeySelective(asReturnEntryList.get(i));
                    }
                }
            }
            logger.info("*********************换转退订单同步开始同步**************************");
            orderService.orderSyncZmall(order.getOrderId());
            logger.info("*********************换转退订单同步同步结束**************************");
            responseData.setSuccess(true);
        } catch (Exception e) {
            responseData.setMsg(e.getMessage());
            responseData.setSuccess(false);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return responseData;
    }


    /**
     * 拼装创建用的requestBody
     *
     * @param asReturn
     * @param iRequest
     * @return
     */
    private AsCreateRequestBody createRequestConstructor(AsReturn asReturn, IRequest iRequest, String sapCode) {
        AsCreateRequestBody requestBody = new AsCreateRequestBody();
        //拼装LV_SO
        requestBody.setLvSo(sapCode);
        //拼装Header
        AsCreateGdtHeader header = new AsCreateGdtHeader();
        header.setAuart(asReturn.getReturnTypeToRetail());
        header.setVkorg("0201");
        header.setVtweg(asReturn.getVtweg());
        header.setSpart("20");
        header.setAugru("");
        header.setVsbed("D");
        header.setAutlf("X");
        header.setLifsk("99");
        header.setKunnr1(asReturn.getSoldParty());
        header.setKunnr2("ONE");
        header.setVkbur(asReturn.getSalesOffice());
        header.setName1(asReturn.getName());
        header.setTitle(asReturn.getSex());
        header.setTranspZone(asReturn.getReceiverDistrict());
        header.setStreet(asReturn.getAddress());
        header.setTelephone(asReturn.getMobile());
        header.setVdatu(asReturn.getAppointmentDateString());
        header.setWerks(asReturn.getReceivePosition());
        header.setOrderNote(asReturn.getNote());
        header.setZzclubid(asReturn.getCustomerid());
        header.setStoriesNo(asReturn.getCode());
        header.setZzglqhd("");
        header.setZzhopeday(asReturn.getAppointmentDateString());
        header.setZzyydat(asReturn.getAppointmentDateString());
        header.setZy021(asReturn.getResponsibleParty());
        header.setExord(asReturn.getEscOrderCode());
        header.setExsys("MAP");
        header.setExnid(iRequest.getUserId().toString());
        header.setExnam(iRequest.getUserName());
        header.setExdat(asReturn.getCreationDateString());
        header.setExtim(asReturn.getCreationTimeString());
        header.setExnidc("");
        header.setExnamc("");
        header.setExdatc("");
        header.setExtimc("");
        requestBody.setAsCreateGdtHeader(header);
        //拼装单行和价格行
        AsGdtCondtions condtions = new AsGdtCondtions();
        AsGdtItems items = new AsGdtItems();
        List<AsGdtCondtionItem> condtionItems = new ArrayList<>();
        List<AsGdtItemsItem> itemsItems = new ArrayList<>();
        Map<Integer, List<Integer>> headReturnFee = new HashMap<>();
        // 解析退货单行，将套件与组件关系记录下来
        for (int i = 0; i < asReturn.getAsReturnEntryList().size(); i++) {
            AsReturnEntry entry = asReturn.getAsReturnEntryList().get(i);
            if (StringUtils.isEmpty(entry.getParentLineNumber())) {
                List<Integer> list = new ArrayList<>();
                for (int j = 0; j < asReturn.getAsReturnEntryList().size(); j++) {
                    AsReturnEntry entryLine = asReturn.getAsReturnEntryList().get(j);
                    if (entry.getLineNumber().equals(entryLine.getParentLineNumber())) {
                        list.add(j);
                    }
                }
                headReturnFee.put(i, list);
            }
        }
        //根据套件与组件关系，将套件中的最后一个组件的returnFee修改为套件的returnFee与其他组件的returnFee之和的差
        for (Map.Entry<Integer, List<Integer>> entry : headReturnFee.entrySet()) {
            if (entry.getValue().size() > 0) {
                Double totalFee = asReturn.getAsReturnEntryList().get(entry.getKey()).getReturnFee();
                Double partTotalFee = 0.00;
                for (int i = 0; i < entry.getValue().size() - 1; i++) {
                    partTotalFee = this.add(partTotalFee, asReturn.getAsReturnEntryList().get(entry.getValue().get(i)).getReturnFee());
                }
                asReturn.getAsReturnEntryList().get(entry.getValue().get(entry.getValue().size() - 1)).setReturnFee(this.sub(totalFee, partTotalFee));
            }
        }


        for (AsReturnEntry entry : asReturn.getAsReturnEntryList()) {
            AsGdtCondtionItem condtionItem = new AsGdtCondtionItem();
            condtionItem.setPosnr(entry.getLineNumber());
            condtionItem.setKschl("ZP00");
            condtionItem.setKbetr(entry.getReturnFee() != null ? entry.getReturnFee().toString() : "0");
            condtionItem.setCurrency("RMB");
            condtionItems.add(condtionItem);
            AsGdtItemsItem item = new AsGdtItemsItem();
            item.setPosnr(entry.getLineNumber());
            item.setUepos(entry.getParentLineNumber());
            item.setPstyv(entry.getEntryReturnType());
            item.setMatnr(entry.getProductCode());
            item.setKwmeng(entry.getQuantity().toString());
            item.setVrkme(entry.getUnit());
            item.setZzxmbz(entry.getNote());
            item.setLgort("9030");
            item.setZy04(entry.getShippingType());
            item.setSdabw("D");
            item.setAbgru(entry.getReturnEntryStatus());
            item.setZzwerks(asReturn.getReceivePosition());
            item.setVgbel(entry.getEntrySapCode());
            item.setVgpos(entry.getEntryOrderLineNumber());
            item.setZzthreason1(entry.getReturnReason1());
            item.setZzthreason2(entry.getReturnReason2());
            item.setZy010(entry.getTradeFinishTime());
            item.setZzrefno(entry.getvCode());
            item.setZzihrez(entry.getPin());
            item.setZccrq("");
            item.setZy01(entry.getProductSize());
            item.setZy02((StringUtils.isEmpty(entry.getProductPackedSize()) || entry.getProductPackedSize().contains("/")) ? "" : entry.getProductPackedSize());
            item.setZy03(entry.getCustomerMsg());
            itemsItems.add(item);
        }
        condtions.setItems(condtionItems);
        items.setItems(itemsItems);
        requestBody.setAsGdtCondtions(condtions);
        requestBody.setAsGdtItems(items);
        //拼装return
        AsCreateGdtReturn gdtReturn = new AsCreateGdtReturn();
        ArrayList<AsCreateGdtReturnItems> returnItems = new ArrayList<>();
        AsCreateGdtReturnItems returnItem = new AsCreateGdtReturnItems();
        returnItems.add(returnItem);
        gdtReturn.setItems(returnItems);
        requestBody.setAsCreateGdtReturn(gdtReturn);
        return requestBody;
    }


    /**
     * 拼装创建用的requestBody
     *
     * @param asReturn
     * @param iRequest
     * @return
     */
    private AsChangeRequestBody changeRequestConstructor(AsReturn asReturn, IRequest iRequest) {
        AsChangeRequestBody requestBody = new AsChangeRequestBody();
        //拼装Header
        AsChangeGdtHeader header = new AsChangeGdtHeader();
        header.setVbeln(asReturn.getSapCode());
        header.setAuart(asReturn.getReturnTypeToRetail());
        header.setVkorg("0201");
        header.setVtweg(asReturn.getVtweg());
        header.setSpart("20");
        header.setAugru("");
        header.setVsbed("D");
        header.setAutlf("X");
        header.setLifsk("99");
        header.setKunnr1(asReturn.getSoldParty());
        header.setKunnr2("ONE");
        header.setVkbur(asReturn.getSalesOffice());
        header.setName1(asReturn.getName());
        header.setTitle(asReturn.getSex());
        header.setTranspZone(asReturn.getReceiverDistrict());
        header.setStreet(asReturn.getAddress());
        header.setTelephone(asReturn.getMobile());
        header.setVdatu(asReturn.getAppointmentDateString());
        header.setWerks(asReturn.getReceivePosition());
        header.setOrderNote(asReturn.getNote());
        header.setZzclubid(asReturn.getCustomerid());
        header.setStoriesNo(asReturn.getCode());
        header.setZzglqhd("");
        header.setZzhopeday(asReturn.getAppointmentDateString());
        header.setZzyydat(asReturn.getAppointmentDateString());
        header.setZy021(asReturn.getResponsibleParty());
        header.setExord(asReturn.getEscOrderCode());
        header.setExsys("MAP");
        header.setExnid("");
        header.setExnam("");
        header.setExdat("");
        header.setExtim("");
        header.setExnidc(iRequest.getUserId().toString());
        header.setExnamc(iRequest.getUserName());
        header.setExdatc(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        header.setExtimc(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        requestBody.setAsChangeGdtHeader(header);
        //拼装单行和价格行
        AsGdtCondtions condtions = new AsGdtCondtions();
        AsGdtItems items = new AsGdtItems();
        List<AsGdtCondtionItem> condtionItems = new ArrayList<>();
        List<AsGdtItemsItem> itemsItems = new ArrayList<>();
        Map<Integer, List<Integer>> headReturnFee = new HashMap<>();

        // 解析退货单行，将套件与组件关系记录下来
        for (int i = 0; i < asReturn.getAsReturnEntryList().size(); i++) {
            AsReturnEntry entry = asReturn.getAsReturnEntryList().get(i);
            if (StringUtils.isEmpty(entry.getParentLineNumber())) {
                List<Integer> list = new ArrayList<>();
                for (int j = 0; j < asReturn.getAsReturnEntryList().size(); j++) {
                    AsReturnEntry entryLine = asReturn.getAsReturnEntryList().get(j);
                    if (entry.getLineNumber().equals(entryLine.getParentLineNumber())) {
                        list.add(j);
                    }
                }
                headReturnFee.put(i, list);
            }
        }
        //根据套件与组件关系，将套件中的最后一个组件的returnFee修改为套件的returnFee与其他组件的returnFee之和的差
        for (Map.Entry<Integer, List<Integer>> entry : headReturnFee.entrySet()) {
            if (entry.getValue().size() > 0) {
                Double totalFee = asReturn.getAsReturnEntryList().get(entry.getKey()).getReturnFee();
                Double partTotalFee = 0.00;
                for (int i = 0; i < entry.getValue().size() - 1; i++) {
                    partTotalFee = this.add(partTotalFee, asReturn.getAsReturnEntryList().get(entry.getValue().get(i)).getReturnFee());
                }
                asReturn.getAsReturnEntryList().get(entry.getValue().get(entry.getValue().size() - 1)).setReturnFee(this.sub(totalFee, partTotalFee));
            }
        }

        for (AsReturnEntry entry : asReturn.getAsReturnEntryList()) {
            AsGdtItemsItem item = new AsGdtItemsItem();
            item.setPosnr(entry.getLineNumber());
            item.setUepos(entry.getParentLineNumber());
            item.setPstyv(entry.getEntryReturnType());
            item.setMatnr(entry.getProductCode());
            item.setKwmeng(entry.getQuantity().toString());
            item.setVrkme(entry.getUnit());
            item.setZzxmbz(entry.getNote());
            item.setLgort("9030");
            item.setZy04(entry.getShippingType());
            item.setSdabw("D");
            item.setAbgru(entry.getReturnEntryStatus());
            item.setZzwerks(asReturn.getReceivePosition());
            item.setVgbel(entry.getEntrySapCode());
            item.setVgpos(entry.getEntryOrderLineNumber());
            item.setZzthreason1(entry.getReturnReason1());
            item.setZzthreason2(entry.getReturnReason2());
            item.setZy010(entry.getTradeFinishTime());
            item.setZzrefno(entry.getvCode());
            item.setZzihrez(entry.getPin());
            item.setZccrq("");
            item.setZy01(entry.getProductSize());
            item.setZy02((StringUtils.isEmpty(entry.getProductPackedSize()) || entry.getProductPackedSize().contains("/")) ? "" : entry.getProductPackedSize());
            item.setZy03(entry.getCustomerMsg());
            itemsItems.add(item);
            AsGdtCondtionItem condtionItem = new AsGdtCondtionItem();
            condtionItem.setPosnr(entry.getLineNumber());
            condtionItem.setKschl("ZP00");
            condtionItem.setKbetr(entry.getReturnFee().toString());
            condtionItem.setCurrency("RMB");
            condtionItems.add(condtionItem);
        }
        condtions.setItems(condtionItems);
        requestBody.setAsGdtCondtions(condtions);
        items.setItems(itemsItems);
        requestBody.setAsGdtItems(items);
        //拼装return
        AsChangeGdtReturn gdtReturn = new AsChangeGdtReturn();
        ArrayList<AsChangeGdtReturnItems> returnItems = new ArrayList<>();
        AsChangeGdtReturnItems returnItem = new AsChangeGdtReturnItems();
        returnItems.add(returnItem);
        gdtReturn.setAsChangeGdtReturnItems(returnItems);
        requestBody.setAsChangeGdtReturn(gdtReturn);
        return requestBody;
    }

    @Override
    public List<AsReturn> selectOrderAndServiceOrderInfoByReturnId(AsReturn dto) {
        return asReturnMapper.selectOrderAndServiceOrderInfoByReturnId(dto);
    }

    private Double add(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    private Double sub(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    private void updateSale(IRequest request, String chosenCoupon, String chosenPromotion, Order order, OrderEntry orderEntry) {
        //更新优惠券表
        if (StringUtil.isEmpty(chosenCoupon)) {
            //没选优惠券信息将优惠卷信息删除
            if (order.getOrderId() != null) {
                OrderCouponrule orderCouponrule = new OrderCouponrule();
                orderCouponrule.setOrderId(order.getOrderId());
                orderCouponruleService.deleteOrderCouponruleByOrderId(orderCouponrule);
            }
        } else {
            if (CollectionUtils.isNotEmpty(orderEntry.getCouponList())) {
                for (OrderCouponrule orderCouponrule : orderEntry.getCouponList()) {
                    if (chosenCoupon.equals(orderCouponrule.getCouponId())) {
                        orderCouponrule.setOrderId(order.getOrderId());
                        if (CollectionUtils.isNotEmpty(orderCouponruleService.selectOrderCouponruleByOrderId(orderCouponrule))) {
                            orderCouponruleService.updateOrderCouponruleByOrderId(orderCouponrule);
                        } else {
                            orderCouponruleService.insertSelective(request, orderCouponrule);
                        }
                    }
                }
            }
        }
        // 更新促销信息
        if (StringUtil.isEmpty(chosenPromotion)) {
            //没选促销信息将促销信息删除
            if (order.getOrderId() != null) {
                OmPromotionrule omPromotionrule = new OmPromotionrule();
                omPromotionrule.setOrderId(order.getOrderId());
                omPromotionruleService.deleteOmPromotionruleByOrderId(omPromotionrule);
            }
        } else {
            if (CollectionUtils.isNotEmpty(orderEntry.getActivities())) {
                for (OmPromotionrule omPromotionrule : orderEntry.getActivities()) {
                    if (chosenPromotion.equals(omPromotionrule.getActivityId())) {
                        omPromotionrule.setOrderId(order.getOrderId());
                        if (!com.hand.hmall.util.StringUtils.isEmpty(omPromotionrule.getPageShowMes())) {
                            omPromotionrule.setPageShowmes(omPromotionrule.getPageShowMes());
                        }
                        if (CollectionUtils.isNotEmpty(omPromotionruleService.selectOmPromotionruleByOrderId(omPromotionrule))) {
                            omPromotionruleService.updateOmPromotionruleByOrderId(omPromotionrule);
                        } else {
                            omPromotionruleService.insertSelective(request, omPromotionrule);
                        }
                    }
                }
            }
        }
    }

    /**
     * 根据服务单ID，查找关联的（唯一）退货单实例并返回
     *
     * @param serviceOrderId 服务单ID
     * @return
     */
    @Override
    public AsReturn queryReturnByServiceOrderId(Long serviceOrderId) {
        List<AsReturn> returnOrders = asReturnMapper.selectReturnOrdersByServiceOrderId(serviceOrderId);
        // 业务逻辑要求服务单下只能创建一个退货单，若列表不为空则返回index[0]
        return returnOrders.isEmpty() ? null : returnOrders.get(0);
    }
}