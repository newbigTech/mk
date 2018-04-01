package com.hand.hmall.service.impl;

import com.hand.hmall.code.MessageCode;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.mapper.*;
import com.hand.hmall.model.*;
import com.hand.hmall.service.*;
import com.markor.map.configurator.api.IVCodeService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;

/**
 * 订单下载
 *
 * @Author: noob
 * @Param: * @param order
 * @Date: 2017/5/31 9:18
 */
@Service("orderCreateService")
@Transactional(rollbackFor = {Exception.class, RuntimeException.class})
public class OrderCreateServiceImpl implements IOrderCreateService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private HmallOmOrderMapper hmallOmOrderMapper;
    @Resource
    private HmallOmOrderEntryMapper hmallOmOrderEntryMapper;

    @Resource
    private HmallOmPaymentInfoMapper hmallOmPaymentInfoMapper;

    @Resource
    private HmallMstProductMapper hmallMstProductMapper;

    @Resource
    private HmallMstUserMapper hmallMstUserMapper;

    @Resource
    private HmallMstChannelMapper hmallMstChannelMapper;

    @Resource
    private HmallMstWebsiteMapper hmallMstWebsiteMapper;

    @Resource
    private HmallMstBaseStoreMapper hmallMstBaseStoreMapper;

    @Resource
    private SysCodeValueBMapper sysCodeValueBMapper;

    @Resource
    private HmallFndRegionsBMapper regionsMapper;

    @Resource
    private HmallMstPointOfServiceMapper hmallMstPointOfServiceMapper;

    @Resource
    private HmallOmPromotionRuleMapper hmallOmPromotionRuleMapper;

    @Resource
    private HmallOmCouponRuleMapper hmallOmCouponRuleMapper;

    @Autowired
    private IOrderCreatePinInfoService iOrderCreatePinInfoService;

    @Autowired
    private HmallOmConsignmentMapper hmallOmConsignmentMapper;

    @Autowired
    private IGetCodeFromThirdPartyService iGetCodeFromThirdPartyService;

    @Autowired
    private IPinAlterInfoService pinAlterInfoService;

    @Autowired
    private IOrderBkService iOrderBkService;

    @Autowired
    private IAfterPromoteService iAfterPromoteService;

    @Autowired
    private INotificationService iNotificationService;
    @Resource
    private HmallPinInfoMapper hmallPinInfoMapper;

    @Resource
    private HmallPinAlterMapper hmallPinAlterMapper;
    @Autowired
    private HapAtpInvSourceInfoMapper atpInvSourceInfoMapper;
    @Autowired
    private HmallCustomTypeMapper customTypeMapper;
    @Autowired
    private AmountChangeLogMapper amountChangeLogMapper;
    @Autowired
    private AmountChangeLogEntryMapper amountChangeLogEntryMapper;

    @Autowired
    private IVCodeService vCodeService;
    @Autowired
    private HmallSoChangeLogMapper soChangeLogMapper;

    /**
     * 订单下载
     *
     * @Author: noob
     * @Param: * @param order
     * @Date: 2017/5/31 9:18
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResponseData addOrder(HmallOmOrder order) {
        ResponseData responseData = new ResponseData();
        List<HmallOmOrderEntry> entryList = order.getEntryList(); //订单行信息
        List<HmallOmPaymentInfo> payList = order.getPayList(); //订单支付信息
        List<HmallOmPromotionRule> promotionRuleList = order.getOrderPromotionList(); //订单促销信息
        List<HmallOmCouponRule> couponRuleList = order.getOrderCouponList(); //订单优惠券信息
        List<String> vcodeList = new ArrayList<>();
        // 检验字段
        responseData = validateAndTransferOrder(order);
        if (!responseData.isSuccess()) {
            return responseData;
        }

        //获取订单编码
        String orderCode = iGetCodeFromThirdPartyService.getOrderCode();
        logger.info("====orderCode====:" + orderCode);
        if (StringUtils.isEmpty(orderCode)) {
            throw new RuntimeException("调用第三方服务,获取订单编码为空");
        } else {
            order.setCode(orderCode);
        }

        //订单状态 默认:NEW_CREATE
        order.setOrderStatus("NEW_CREATE");

        //订单已锁定 默认:N
        order.setLocked("N");

        //订单类型 默认:NORMAL(销售单)
        order.setOrderType("NORMAL");
        //订单插入
        hmallOmOrderMapper.insertSelective(order);
        //增加金额更改记录
        AmountChangeLog changeLog = addAmountChangeLog(order);
        for (HmallOmOrderEntry orderEntry : entryList) {
            //获取订单Id
            Long orderId = order.getOrderId();
            logger.info("---orderId---:" + orderId);
            // 将订单行与订单头关联
            orderEntry.setOrderId(orderId);

            //获取订单行号
            Long lineNumber = getLineNumber(orderId);
            logger.info("----addOrder lineNumber---:" + lineNumber);
            orderEntry.setLineNumber(lineNumber);

            //获取orderEntryCode
            String orderEntryCode = iGetCodeFromThirdPartyService.getOrderEntryCode();
            logger.info("====orderEntryCode====:" + orderEntryCode);
            if (StringUtils.isEmpty(orderEntryCode)) {
                throw new RuntimeException("调用第三方服务,获取订单行号为空");
            } else {
                orderEntry.setCode(orderEntryCode);
            }

            //获取pin码
            String pinCode = iGetCodeFromThirdPartyService.getPinCode();
            logger.info("====pinCode====:" + pinCode);
            if (StringUtils.isEmpty(pinCode)) {
                throw new RuntimeException("调用第三方服务,获取pin码为空");
            } else {
                orderEntry.setPin(pinCode);
            }
            orderEntry.setEstimateConTime(DateUtils.addDays(orderEntry.getEstimateDeliveryTime(), -getLogisticsLeadTime(order.getCityCode(), order.getDistrictCode(), orderEntry.getPointOfServiceCode(), orderEntry.getShippingType())));

            //插入订单行
            hmallOmOrderEntryMapper.insertSelective(orderEntry);

            //根据orderId和平台订单行号获取订单行
            HmallOmOrderEntry hmallOmOrderEntry = hmallOmOrderEntryMapper.selectByEscLineNumber(order.getOrderId(), orderEntry.getEscLineNumber());

            //在PIN码表中插入信息
            responseData = iOrderCreatePinInfoService.orderCreatePinInfo(hmallOmOrderEntry);
            if (!responseData.isSuccess()) {
                throw new RuntimeException("根据eventCode'MAP0100'找不到对应的警报信息");
            }

            // 插入订单行促销信息
            HmallOmPromotionRule promotionRule = orderEntry.getOrderEntryPromotion();
            if (null != promotionRule) {
                //促销信息关联订单行
                promotionRule.setOrderEntryId(orderEntry.getOrderEntryId());
                // 插入行促销信息
                hmallOmPromotionRuleMapper.insertSelective(promotionRule);
            }
            vcodeList.add(orderEntry.getVproductCode());
            //增加定制来源
            addCustomType(orderEntry);
            //增加金额变更行
            addAmountChangeLogEntry(changeLog, orderEntry);
            //增加书面记录
            addSoChangeLog(order, orderEntry);


        }
        // 插入/更新支付信息
        for (HmallOmPaymentInfo payInfo : payList) {
            //根据支付渠道、流水号得到支付信息
            HmallOmPaymentInfo hmallPayInfo = hmallOmPaymentInfoMapper.selectByPaymodeAndNumbercode(payInfo.getPayMode(), payInfo.getNumberCode());
            if (null != hmallPayInfo) {
                payInfo.setLastUpdateDate(new Date());
                payInfo.setPaymentinfoId(hmallPayInfo.getPaymentinfoId());
                hmallOmPaymentInfoMapper.updateByPrimaryKeySelective(payInfo);
            } else {
                //支付信息关联订单头
                payInfo.setOrderId(order.getOrderId());
                //插入支付信息
                hmallOmPaymentInfoMapper.insertSelective(payInfo);
            }
        }

        // 插入订单头促销信息
        if (null != promotionRuleList) {
            for (HmallOmPromotionRule promotionRule : promotionRuleList) {
                //促销信息关联订单头
                promotionRule.setOrderId(order.getOrderId());
                // 插入订单头促销信息
                hmallOmPromotionRuleMapper.insertSelective(promotionRule);
            }
        }

        // 插入订单头优惠券信息
        if (null != couponRuleList) {
            for (HmallOmCouponRule couponRule : couponRuleList) {
                //优惠券信息关联订单头
                couponRule.setOrderId(order.getOrderId());
                // 插入订单头优惠券信息
                hmallOmCouponRuleMapper.insertSelective(couponRule);
            }
        }

        //事后促销
        responseData = iAfterPromoteService.checkAfterPromote(order);
        //返回事后促销和pin信息
        Map respMap = iAfterPromoteService.returnAfterPromoteAndPinInfo(order, responseData);

        List entries = new ArrayList();
        List list = new ArrayList();
        for (HmallOmOrderEntry orderEntry : order.getEntryList()) {
            Map<String, Object> map = new HashMap();
            //在resp中将 "订单行号":"pin码" 返回给前台
            map.put("lineNumber", orderEntry.getEscLineNumber());
            map.put("pin", orderEntry.getPin());
            entries.add(map);
        }

        //返回信息中增加 行号:pin 信息
        respMap.put("entries", entries);
        list.add(respMap);

        if (vcodeList.size() > 0) {
            try {
                vCodeService.downloadVCodeImages(vcodeList);
            } catch (Exception e) {
            }
        }

        // 添加新订单下载至中台通知
        iNotificationService.addOrderNewNotice(order);

        responseData.setMsgCode("1");
        responseData.setMsg(MessageCode.OR_ADD_01.getValue());
        responseData.setResp(list);
        responseData.setSuccess(true);
        return responseData;
    }

    /**
     * 订单更新
     *
     * @Author: noob
     * @Param: * @param order
     * @Date: 2017/6/1 9:47
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResponseData updateOrder(HmallOmOrder order) {
        ResponseData responseData = new ResponseData();
        List<HmallOmOrderEntry> entryList = order.getEntryList(); //订单行信息
        List<HmallOmPaymentInfo> payList = order.getPayList(); //订单支付信息
        List<HmallOmPromotionRule> promotionRuleList = order.getOrderPromotionList(); //订单促销信息
        List<HmallOmCouponRule> couponRuleList = order.getOrderCouponList(); //订单优惠券信息
        List<String> vcodeList = new ArrayList<>();
        Map respMap = new HashMap();
        // 对订单数据进行检查和校验
        responseData = validateAndTransferOrder(order);
        if (!responseData.isSuccess()) {
            return responseData;
        }

        logger.info("---code---:" + order.getCode());
        // 查询待更新订单
        HmallOmOrder hmallOmOrder = hmallOmOrderMapper.selectByCode(order.getCode());

        //获取订单id
        Long orderId = hmallOmOrder.getOrderId();

        //判断订单上订单已锁定字段如果原本是Y，则更新为N
        String locked = order.getLocked();
        if ("Y".equals(locked)) {
            order.setLocked("N");
        }

        //判断传入的订单状态为“TRADE_FINISHED”或者“TRADE_CANCELLED”则更新订单状态字段，否则不更新
        String orderStatus = order.getOrderStatus();
        logger.info("---orderStatus---:" + orderStatus);

        if (!("TRADE_FINISHED".equals(orderStatus)) && !("TRADE_CANCELLED".equals(orderStatus))) {
            order.setOrderStatus(null);
        }

        //如果订单确认收货时间不为空,则判断订单更新状态为"TRADE_FINISHED"
        //订单确认收货时间
        Date tradeFinishTime = order.getTradeFinishTime();
        if (tradeFinishTime != null) {
            if ("TRADE_FINISHED".equals(orderStatus)) {
                //确认收货时间
                order.setTradeFinishTime(tradeFinishTime);
                //事后促销
                responseData = iAfterPromoteService.checkAfterPromoteWithCofrim(order);
                //返回事后促销和pin信息
                respMap = iAfterPromoteService.returnAfterPromoteAndPinInfo(order, responseData);

                List<HmallOmConsignment> consignments = hmallOmConsignmentMapper.getConsignmentByOrderId(orderId);
                if (CollectionUtils.isNotEmpty(consignments)) {
                    for (HmallOmConsignment con : consignments) {
                        //将发货单同步RETAIL标记更新为"N"
                        con.setSyncflag("N");
                        con.setLastUpdateDate(new Date());
                        hmallOmConsignmentMapper.updateByPrimaryKeySelective(con);
                    }
                }

                //当订单状态变更为“TRADE_FINISHED”的时候，将订单上的收货时间【order.trade_finish_time】时间
                // 写入与该订单关联的所有“确认收货时间”【consignment.trade_finished_time】值为空，且【consignment.status】=WAIT_BUYER_CONFIRM，的发货单的该字段中
                List<HmallOmConsignment> consignmentList = hmallOmConsignmentMapper.queryConsignmentByOrderId(orderId);
                if (CollectionUtils.isNotEmpty(consignmentList)) {
                    for (HmallOmConsignment con : consignmentList) {
                        con.setTradeFinishedTime(tradeFinishTime);
                        con.setLastUpdateDate(new Date());
                        hmallOmConsignmentMapper.updateByPrimaryKeySelective(con);
                    }
                }

                //增加PIN码信息
                List<HmallOmOrderEntry> orderEntryList = hmallOmOrderEntryMapper.selectByOrderId(order.getOrderId());
                for (HmallOmOrderEntry entry : orderEntryList) {
                    try {
                        addPinInfo(entry);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            }
        }

        //更新订单
        order.setLastUpdateDate(new Date());
        hmallOmOrderMapper.updateByPrimaryKeySelective(order);
        //增加金额变更记录头
        AmountChangeLog changeLog = addAmountChangeLog(order);

        // 更新订单头促销信息
        // 整个促销结构不推送则该订单的促销信息不更新,否则执行更新操作
        if (CollectionUtils.isNotEmpty(promotionRuleList)) {
            // 根据orderId删除原促销信息
            hmallOmPromotionRuleMapper.deleteByOrderId(orderId);
            for (HmallOmPromotionRule promotionRule : promotionRuleList) {
                // 插入新的促销信息
                promotionRule.setOrderId(orderId);
                hmallOmPromotionRuleMapper.insertSelective(promotionRule);
            }
        }

        // 更新订单优惠券信息
        // 整个优惠券结构不推送则该订单的优惠券信息不更新,否则执行更新操作
        if (CollectionUtils.isNotEmpty(couponRuleList)) {
            // 根据orderId删除原优惠券信息
            hmallOmCouponRuleMapper.deleteByOrderId(orderId);
            for (HmallOmCouponRule couponRule : couponRuleList) {
                // 插入新的优惠券信息
                couponRule.setOrderId(orderId);
                hmallOmCouponRuleMapper.insertSelective(couponRule);
            }
        }

        // 判断订单行是否为空
        if (CollectionUtils.isNotEmpty(entryList)) {
            for (HmallOmOrderEntry orderEntry : entryList) {
                // 将订单行与订单头关联
                orderEntry.setOrderId(orderId);

                //根据orderId、行号查询订单行
                HmallOmOrderEntry hmallOrderEntry = hmallOmOrderEntryMapper.selectByEscLineNumber(orderId, orderEntry.getEscLineNumber());

                Long orderEntryId; //订单行id

                if (null == hmallOrderEntry) {
                    //获取订单行号
                    Long lineNumber = getLineNumber(orderId);
                    logger.info("----updateOrder lineNumber---:" + lineNumber);
                    orderEntry.setLineNumber(lineNumber);

                    //获取orderEntryCode
                    String orderEntryCode = iGetCodeFromThirdPartyService.getOrderEntryCode();
                    logger.info("====orderEntryCode====:" + orderEntryCode);
                    if (StringUtils.isEmpty(orderEntryCode)) {
                        throw new RuntimeException("调用第三方服务,获取订单行编码为空");
                    } else {
                        orderEntry.setCode(orderEntryCode);
                    }

                    //获取pin码
                    String pinCode = iGetCodeFromThirdPartyService.getPinCode();
                    logger.info("====pinCode====:" + pinCode);
                    if (StringUtils.isEmpty(pinCode)) {
                        throw new RuntimeException("调用第三方服务,获取pin码为空");
                    } else {
                        orderEntry.setPin(pinCode);
                    }
                    //更新订单新增过来得订单行
                    orderEntry.setEstimateConTime(DateUtils.addDays(orderEntry.getEstimateDeliveryTime(), -getLogisticsLeadTime(order.getCityCode(), order.getDistrictCode(), orderEntry.getPointOfServiceCode(), orderEntry.getShippingType())));


                    //插入订单行
                    hmallOmOrderEntryMapper.insertSelective(orderEntry);

                    //获取订单行id
                    orderEntryId = orderEntry.getOrderEntryId();

                    HmallOmOrderEntry hmallOmOrderEntry = hmallOmOrderEntryMapper.selectByEscLineNumber(order.getOrderId(), orderEntry.getEscLineNumber());

                    //在PIN码表中插入信息
                    responseData = iOrderCreatePinInfoService.orderCreatePinInfo(hmallOmOrderEntry);
                    if (!responseData.isSuccess()) {
                        throw new RuntimeException("根据eventCode'MAP0100'找不到对应的警报信息");
                    }
                    //增加定制来源
                    addCustomType(orderEntry);
                    //增加金额变更记录行
                    addAmountChangeLogEntry(changeLog, orderEntry);
                    //增加书面记录
                    addSoChangeLog(order, orderEntry);
                } else {
                    orderEntry.setLineNumber(hmallOrderEntry.getLineNumber());
                    orderEntry.setPin(hmallOrderEntry.getPin());
                    orderEntry.setLastUpdateDate(new Date());
                    orderEntryId = hmallOrderEntry.getOrderEntryId();
                    orderEntry.setOrderEntryId(orderEntryId);
                    hmallOmOrderEntryMapper.updateByPrimaryKeySelective(orderEntry);
                    //更新定制来源
                    HmallCustomType customType = new HmallCustomType();
                    customType.setPinCode(orderEntry.getPin());
                    customType.setVproductCode(orderEntry.getVproductCode());
                    HmallCustomType hmallCustomType = customTypeMapper.selectOne(customType);
                    if (hmallCustomType != null) {
                        hmallCustomType.setCustomType(orderEntry.getCustomType());
                        customTypeMapper.updateByPrimaryKeySelective(hmallCustomType);
                    } else {
                        //增加定制来源
                        addCustomType(orderEntry);
                    }
                    //增加金额变更记录行
                    addAmountChangeLogEntry(changeLog, orderEntry);
                    //增加书面记录
                    addSoChangeLog(order, orderEntry);

                }

                // 插入/更新订单行促销信息
                HmallOmPromotionRule promotionRule = orderEntry.getOrderEntryPromotion();
                if (null != promotionRule) {
                    // 根据orderEntryId删除原订单行促销信息
                    hmallOmPromotionRuleMapper.deleteByOrderEntryId(orderEntryId);
                    // 插入新的促销信息
                    promotionRule.setOrderEntryId(orderEntryId);
                    hmallOmPromotionRuleMapper.insertSelective(promotionRule);
                }
                vcodeList.add(orderEntry.getVproductCode());
            }
        }

        // 支付信息插入或更新操作
        for (HmallOmPaymentInfo payInfo : payList) {
            HmallOmPaymentInfo hmallPaymentInfo = hmallOmPaymentInfoMapper.selectByPaymodeAndNumbercode(payInfo.getPayMode(), payInfo.getNumberCode());

            if (null == hmallPaymentInfo) {
                payInfo.setOrderId(orderId);
                hmallOmPaymentInfoMapper.insertSelective(payInfo);
            } else {
                payInfo.setLastUpdateDate(new Date());
                payInfo.setPaymentinfoId(hmallPaymentInfo.getPaymentinfoId());
                hmallOmPaymentInfoMapper.updateByPrimaryKeySelective(payInfo);
            }
        }

        List entries = new ArrayList();
        List list = new ArrayList();
        for (HmallOmOrderEntry orderEntry : entryList) {
            Map<String, Object> map = new HashMap();
            //在resp中将 "订单行号":"pin码" 返回给前台
            map.put("lineNumber", orderEntry.getEscLineNumber());
            map.put("pin", orderEntry.getPin());
            entries.add(map);
        }

        //返回信息中增加 行号:pin 信息
        respMap.put("entries", entries);
        list.add(respMap);

        //订单备份
//        responseData =  iOrderBkService.saveBk(hmallOmOrder);
//        if(!responseData.isSuccess())
//        {
//            throw new RuntimeException("订单备份失败");
//        }

        if (vcodeList.size() > 0) {
            vCodeService.downloadVCodeImages(vcodeList);
        }
        responseData.setResp(list);
        responseData.setMsg(MessageCode.OR_UPDATE_01.getValue());
        responseData.setMsgCode("1");
        responseData.setSuccess(true);
        return responseData;
    }

    //增加定制类型
    private void addCustomType(HmallOmOrderEntry orderEntry) {
        HmallCustomType customType = new HmallCustomType();
        customType.setPinCode(orderEntry.getPin());
        customType.setVproductCode(orderEntry.getVproductCode());
        customType.setCustomType(orderEntry.getCustomType());
        customTypeMapper.insertSelective(customType);
    }

    /**
     * 订单下载  根据 平台订单号、网站、渠道、店铺、订单类型(NORMAL) 获取唯一订单
     *
     * @param websiteId
     * @param channelId
     * @param storeId
     * @param escOrderCode
     * @Date: 2017/6/13 11:16
     */
    @Override
    public HmallOmOrder selectByMutiItems(String escOrderCode, String websiteId, String channelId, String storeId) {
        HmallOmOrder order = hmallOmOrderMapper.selectByMutiItems(escOrderCode, websiteId, channelId, storeId);
        return order;
    }

    /**
     * 订单更新  根据 平台订单号、网站、渠道、店铺、订单类型(NORMAL) 获取唯一订单
     *
     * @param websiteId
     * @param channelId
     * @param storeId
     * @param escOrderCode
     * @Date: 2017/6/13 11:16
     */
    @Override
    public HmallOmOrder selectByMutiItemsForUpdate(String escOrderCode, String websiteId, String channelId, String storeId) {
        HmallOmOrder order = hmallOmOrderMapper.selectByMutiItemsForUpdate(escOrderCode, websiteId, channelId, storeId);
        return order;
    }

    /**
     * 校验订单数据是否合法，校验的同时会更新订单的数据
     *
     * @param order 待校验订单
     * @return
     */
    private ResponseData validateAndTransferOrder(HmallOmOrder order) {
        ResponseData responseData = new ResponseData();

        // 检查是否包含订单行信息
        if (CollectionUtils.isEmpty(order.getEntryList())) {
            responseData.setMsg("订单[" + order.getEscOrderCode() + "]不存在订单行");
            responseData.setMsgCode("od.addorder.entryList.empty");

            logger.info("==validate order===:od.addorder.entryList.empty,escOrderCode:" + order.getEscOrderCode() != null ? order.getEscOrderCode() : "");
            responseData.setSuccess(false);
            return responseData;
        }

        //订单状态的非空判断
        if (StringUtils.isEmpty(order.getOrderStatus())) {
            responseData.setMsg("订单状态为空");
            responseData.setMsgCode("od.addorder.orderstatus.null");
            responseData.setSuccess(false);
            return responseData;
        }

        // 检验用户是否为空
        if (StringUtils.isEmpty(order.getCustomerId())) {
            responseData.setMsg("用户为空");
            responseData.setMsgCode("od.addorder.customerid.null");

            logger.info("==validate order===:od.addorder.customerid.null,escOrderCode:" + order.getEscOrderCode() != null ? order.getEscOrderCode() : "");
            responseData.setSuccess(false);
            return responseData;
        } else {
            // 校验用户是否存在
            HmallMstUser hmallMstUser = hmallMstUserMapper.selectUserByCustomerId(order.getCustomerId());
            if (null != hmallMstUser) {
                order.setUserId(hmallMstUser.getUserId());
            } else {
                responseData.setMsg("用户[" + order.getCustomerId() + "]不存在");
                responseData.setMsgCode("od.addorder.customerid.notexist");
                logger.info(order.getEscOrderCode() != null ? order.getEscOrderCode() : "" + ">>>>>>>>>用户不存在");
                responseData.setSuccess(false);
                return responseData;
            }
        }

        // 校验网站是否存在
        HmallMstWebsite hmallMstWebsite = hmallMstWebsiteMapper.selectWebsiteByWebsitecode(order.getWebsiteCode());
        if (null != hmallMstWebsite) {
            order.setWebsiteId(hmallMstWebsite.getCode());
        } else {
            responseData.setMsg("网站[" + order.getWebsiteCode() + "]不存在");
            responseData.setMsgCode("od.addorder.websitecode.notexist");

            logger.info("==validate order===:od.addorder.websitecode.notexist,escOrderCode:" + order.getEscOrderCode() != null ? order.getEscOrderCode() : "");
            responseData.setSuccess(false);
            return responseData;
        }

        // 检查渠道是否存在
        HmallMstChannel hmallMstChannel = hmallMstChannelMapper.selectChannelByChannelCode(order.getChannelCode());
        if (null != hmallMstChannel) {
            order.setSalechannelId(hmallMstChannel.getCode());
        } else {
            responseData.setMsg("渠道[" + order.getChannelCode() + "]不存在");
            responseData.setMsgCode("od.addorder.channelcode.notexist");

            logger.info("==validate order===:od.addorder.channelcode.notexist,escOrderCode:" + order.getEscOrderCode() != null ? order.getEscOrderCode() : "");
            responseData.setSuccess(false);
            return responseData;
        }

        // 检查店铺是否存在
        HmallMstBaseStore hmallMstBaseStore = hmallMstBaseStoreMapper.selectStoreByStoreCode(order.getStoreCode());
        if (null != hmallMstBaseStore) {
            order.setStoreId(hmallMstBaseStore.getCode());
        } else {
            responseData.setMsg("店铺[" + order.getStoreCode() + "]不存在");
            responseData.setMsgCode("od.addorder.storecode.notexist");

            logger.info("==validate order===:od.addorder.storecode.notexist,escOrderCode:" + order.getEscOrderCode() != null ? order.getEscOrderCode() : "");
            responseData.setSuccess(false);
            return responseData;
        }

        // 检验国家是否为空
        if (StringUtils.isEmpty(order.getCountryCode())) {
            responseData.setMsg("国家代码为空");
            responseData.setMsgCode("od.addorder.countrycode.null");

            logger.info("==validate order===:od.addorder.countrycode.null,escOrderCode:" + order.getEscOrderCode() != null ? order.getEscOrderCode() : "");
            responseData.setSuccess(false);
            return responseData;
        } else {
            // 检查国家是否存在
            HmallFndRegionsB country = regionsMapper.selectRegionByRegionCode(order.getCountryCode());
            if (null != country) {
                order.setReceiverCountry(country.getRegionCode());
            } else {
                responseData.setMsg("国家代码[" + order.getCountryCode() + "]不存在");
                responseData.setMsgCode("od.addorder.countrycode.notexist");

                logger.info("==validate order===:od.addorder.countrycode.notexist,escOrderCode:" + order.getEscOrderCode() != null ? order.getEscOrderCode() : "");
                responseData.setSuccess(false);
                return responseData;
            }
        }

        // 检验省是否为空
        if (StringUtils.isEmpty(order.getStateCode())) {
            responseData.setMsg("省代码为空");
            responseData.setMsgCode("od.addorder.statecode.null");

            logger.info("==validate order===:od.addorder.statecode.null,escOrderCode:" + order.getEscOrderCode() != null ? order.getEscOrderCode() : "");
            responseData.setSuccess(false);
            return responseData;
        } else {
            // 检查省是否存在
            HmallFndRegionsB state = regionsMapper.selectRegionByRegionCode(order.getStateCode());
            if (null != state) {
                order.setReceiverState(state.getRegionCode());
            } else {
                responseData.setMsg("省代码[" + order.getStateCode() + "]不存在");
                responseData.setMsgCode("od.addorder.statecode.notexist");

                logger.info("==validate order===:od.addorder.statecode.notexist,escOrderCode:" + order.getEscOrderCode() != null ? order.getEscOrderCode() : "");
                responseData.setSuccess(false);
                return responseData;
            }
        }

        // 检验城市是否为空
        if (StringUtils.isEmpty(order.getCityCode())) {
            responseData.setMsg("城市代码为空");
            responseData.setMsgCode("od.addorder.citycode.null");

            logger.info("==validate order===:od.addorder.citycode.null,escOrderCode:" + order.getEscOrderCode() != null ? order.getEscOrderCode() : "");
            responseData.setSuccess(false);
            return responseData;
        } else {
            // 检查城市是否存在
            HmallFndRegionsB city = regionsMapper.selectRegionByRegionCode(order.getCityCode());
            if (null != city) {
                order.setReceiverCity(city.getRegionCode());
            } else {
                responseData.setMsg("城市代码[" + order.getCityCode() + "]不存在");
                responseData.setMsgCode("od.addorder.citycode.notexist");

                logger.info("==validate order===:od.addorder.citycode.notexist,escOrderCode:" + order.getEscOrderCode() != null ? order.getEscOrderCode() : "");
                responseData.setSuccess(false);
                return responseData;
            }
        }

        // 检验区是否为空
        if (StringUtils.isEmpty(order.getDistrictCode())) {
            responseData.setMsg("区代码为空");
            responseData.setMsgCode("od.addorder.districtcode.null");

            logger.info("==validate order===:od.addorder.districtcode.null,escOrderCode:" + order.getEscOrderCode() != null ? order.getEscOrderCode() : "");
            responseData.setSuccess(false);
            return responseData;
        } else {
            // 检查区是否存在
            HmallFndRegionsB district = regionsMapper.selectRegionByRegionCode(order.getDistrictCode());
            if (null != district) {
                order.setReceiverDistrict(district.getRegionCode());
            } else {
                responseData.setMsg("区代码[" + order.getDistrictCode() + "]不存在");
                responseData.setMsgCode("od.addorder.districtcode.notexist");

                logger.info("==validate order===:od.addorder.districtcode.notexist,escOrderCode:" + order.getEscOrderCode() != null ? order.getEscOrderCode() : "");
                responseData.setSuccess(false);
                return responseData;
            }
        }

        //检验是否开票
        String isInvoiced = order.getIsInvoiced();
        if (StringUtils.isEmpty(isInvoiced)) {
            responseData.setMsg("是否开票为空");
            responseData.setMsgCode("od.addorder.isinvoiced.null");

            logger.info("==validate order===:od.addorder.isinvoiced.null,escOrderCode:" + order.getEscOrderCode() != null ? order.getEscOrderCode() : "");
            responseData.setSuccess(false);
            return responseData;
        }

        // 如果开票,则校验发票类型是否存在
        String invoiceType = order.getInvoiceType();
        if ("Y".equals(isInvoiced)) {
            if (StringUtils.isEmpty(invoiceType)) {
                responseData.setMsg("发票类型为空");
                responseData.setMsgCode("od.addorder.invoicetype.null");

                logger.info("==validate order===:od.addorder.invoicetype.null,escOrderCode:" + order.getEscOrderCode() != null ? order.getEscOrderCode() : "");
                responseData.setSuccess(false);
                return responseData;
            } else {
                int invoiceTypeNum = sysCodeValueBMapper.checkInvoiceType(invoiceType);
                if (invoiceTypeNum == 0) {
                    responseData.setMsg("发票类型[" + invoiceType + "]不存在");
                    responseData.setMsgCode("od.addorder.invoicetype.notexist");

                    logger.info("==validate order===:od.addorder.invoicetype.notexist,escOrderCode:" + order.getEscOrderCode() != null ? order.getEscOrderCode() : "");
                    responseData.setSuccess(false);
                    return responseData;
                }
            }
        }

        //检验是否齐套
        String totalCon = order.getTotalcon();
        if (StringUtils.isEmpty(totalCon)) {
            responseData.setMsg("是否齐套为空");
            responseData.setMsgCode("od.addorder.totalcon.null");

            logger.info("==validate order===:od.addorder.totalcon.null,escOrderCode:" + order.getEscOrderCode() != null ? order.getEscOrderCode() : "");
            responseData.setSuccess(false);
            return responseData;
        }

        // 检验货币是否为空
        if (StringUtils.isEmpty(order.getCurrencyCode())) {
            responseData.setMsg("货币为空");
            responseData.setMsgCode("od.addorder.currencycode.notexist");

            logger.info("==validate order===:od.addorder.currencycode.notexist,escOrderCode:" + order.getEscOrderCode() != null ? order.getEscOrderCode() : "");
            responseData.setSuccess(false);
            return responseData;
        } else {
            // 校验货币是否存在
            int currencyNum = sysCodeValueBMapper.checkCurrencyCode(order.getCurrencyCode());
            if (currencyNum == 0) {
                responseData.setMsg("货币[" + order.getCurrencyCode() + "]不存在");
                responseData.setMsgCode("od.addorder.currencycode.notexist");

                logger.info("==validate order===:od.addorder.currencycode.notexist,escOrderCode:" + order.getEscOrderCode() != null ? order.getEscOrderCode() : "");
                responseData.setSuccess(false);
                return responseData;
            } else {
                order.setCurrencyId(order.getCurrencyCode());
            }
        }


        //行数据的检验
        if (CollectionUtils.isNotEmpty(order.getEntryList())) {
            for (HmallOmOrderEntry entry : order.getEntryList()) {
                //检验平台订单行号是否为空
                Long lineNumber = entry.getLineNumber();
                if (lineNumber == null) {
                    responseData.setMsg("平台订单行号为空");
                    responseData.setMsgCode("od.addorder.linenumber.null");

                    logger.info("==validate order===:od.addorder.linenumber.null,escOrderCode:" + order.getEscOrderCode() != null ? order.getEscOrderCode() : "");
                    responseData.setSuccess(false);
                    return responseData;
                } else {
                    entry.setEscLineNumber(String.valueOf(lineNumber));
                }

                //检验是否赠品是否为空
                String isGift = entry.getIsGift();
                if (StringUtils.isEmpty(isGift)) {
                    responseData.setMsg("是否赠品为空");
                    responseData.setMsgCode("od.addorder.isgift.null");

                    logger.info("==validate order===:od.addorder.isgift.null,escOrderCode:" + order.getEscOrderCode() != null ? order.getEscOrderCode() : "");
                    responseData.setSuccess(false);
                    return responseData;
                }

                //检验订单行状态是否存在
                //订单行状态(NORMAL、CANCEL),如果前台传的值为空则默认为:NORMAL，反之存入前台传入的值
                String orderEntryStatus = entry.getOrderEntryStatus();
                logger.info("---orderEntryStatus---:" + orderEntryStatus);
                if (StringUtils.isNotEmpty(orderEntryStatus)) {
                    int orderEntryStatusNum = sysCodeValueBMapper.checkOrderEntryStatus(orderEntryStatus);
                    if (orderEntryStatusNum == 0) {
                        logger.info("==validate order===:od.addorder.orderentrystatus.notexist,escOrderCode:" + order.getEscOrderCode() == null ? "" : order.getEscOrderCode());

                        responseData.setMsg("订单行状态[" + orderEntryStatus + "]不存在");
                        responseData.setMsgCode("od.addorder.orderentrystatus.notexist");
                        responseData.setSuccess(false);
                        return responseData;
                    } else {
                        entry.setOrderEntryStatus(orderEntryStatus);
                    }
                } else {
                    entry.setOrderEntryStatus("NORMAL");
                }

                // 校验订单行配送方式是否存在
                if (StringUtils.isEmpty(entry.getShippingType())) {
                    responseData.setMsg("订单行配送方式为空");
                    responseData.setMsgCode("od.addorder.shippingtype.null");

                    logger.info("==validate order===:od.addorder.shippingtype.null,escOrderCode:" + order.getEscOrderCode() != null ? order.getEscOrderCode() : "");
                    responseData.setSuccess(false);
                    return responseData;
                } else {
                    int shippingTypeNum = sysCodeValueBMapper.checkShippingType(entry.getShippingType());
                    if (shippingTypeNum == 0) {
                        responseData.setMsg("订单行配送方式[" + entry.getShippingType() + "]不存在");
                        responseData.setMsgCode("od.addorder.shippingtype.noexist");

                        logger.info("==validate order===:od.addorder.shippingtype.noexist,escOrderCode:" + order.getEscOrderCode() != null ? order.getEscOrderCode() : "");
                        responseData.setSuccess(false);
                        return responseData;
                    }
                }

                //预计交货时间是否为空
                Date estimateDeliveryTime = entry.getEstimateDeliveryTime();
                if (estimateDeliveryTime == null) {
                    responseData.setMsg("行上预计交货时间式为空");
                    responseData.setMsgCode("od.addorder.estimatedeliverytime.null");

                    logger.info("==validate order===:od.addorder.estimatedeliverytime.null,escOrderCode:" + order.getEscOrderCode() != null ? order.getEscOrderCode() : "");
                    responseData.setSuccess(false);
                    return responseData;
                }

                // 检查订单行上的商品是否存在
                String productCode = entry.getProductCode();
                if (StringUtils.isEmpty(productCode)) {
                    responseData.setMsg("商品为空");
                    responseData.setMsgCode("od.addorder.productcode.null");

                    logger.info("==validate order===:od.addorder.productcode.null,escOrderCode:" + order.getEscOrderCode() != null ? order.getEscOrderCode() : "");
                    responseData.setSuccess(false);
                    return responseData;
                } else {
                    HmallMstProduct productCheck = hmallMstProductMapper.selectMarkorOnlineProductByCode(productCode);
                    if (null != productCheck) {
                        entry.setProductId(productCheck.getProductId());
                    } else {
                        responseData.setMsg("商品[" + productCode + "]不存在");
                        responseData.setMsgCode("od.addorder.productcode.notexist");

                        logger.info("==validate order===:od.addorder.productcode.notexist,escOrderCode:" + order.getEscOrderCode() != null ? order.getEscOrderCode() : "");
                        responseData.setSuccess(false);
                        return responseData;
                    }
                }

                //根据pointOfService的code找到对应的服务点ID写入
                String code = entry.getPointOfServiceCode();
                if (StringUtils.isEmpty(code)) {
                    responseData.setMsg("服务点为空");
                    responseData.setMsgCode("od.addorder.pointofservicecode.null");

                    logger.info("==validate order===:od.addorder.pointofservicecode.null,escOrderCode:" + order.getEscOrderCode() != null ? order.getEscOrderCode() : "");
                    responseData.setSuccess(false);
                    return responseData;
                } else {
                    HmallMstPointOfService pointOfService = hmallMstPointOfServiceMapper.selectByPointOfServiceCode(code);
                    if (pointOfService != null) {
                        entry.setPointOfServiceId(pointOfService.getPointOfServiceId());
                    } else {
                        responseData.setMsg("服务点[" + code + "]不存在");
                        responseData.setMsgCode("od.addorder.pointofservicecode.notexist");

                        logger.info("==validate order===:od.addorder.pointofservicecode.notexist,escOrderCode:" + order.getEscOrderCode() != null ? order.getEscOrderCode() : "");
                        responseData.setSuccess(false);
                        return responseData;
                    }
                }
                //定制来源判断
                if (StringUtils.isEmpty(entry.getCustomType())) {
                    responseData.setMsg("定制来源为空");
                    responseData.setMsgCode("od.addorder.customType.null");
                    responseData.setSuccess(false);
                    return responseData;
                }

            }
        }

        // 校验支付信息是否存在
        if (CollectionUtils.isNotEmpty(order.getPayList())) {
            for (HmallOmPaymentInfo pay : order.getPayList()) {
                String payMode = pay.getPayMode();
                Double payAmount = pay.getPayAmount();
                Date payTime = pay.getPayTime();
                String outTradeNo = pay.getOutTradeNo();
                String numberCode = pay.getNumberCode();

                //订单类型:默认为自动写入ORDER-销售订单
                pay.setOrderType("ORDER");

                //检验支付渠道是否为空
                if (StringUtils.isEmpty(payMode)) {
                    responseData.setMsg("支付渠道为空");
                    responseData.setMsgCode("od.addorder.paymode.null");

                    logger.info("==validate order===:od.addorder.paymode.null,escOrderCode:" + order.getEscOrderCode() != null ? order.getEscOrderCode() : "");
                    responseData.setSuccess(false);
                    return responseData;
                } else {
                    //检验支付类型是否存在
                    int payModeNum = sysCodeValueBMapper.checkPayMode(payMode);
                    if (payModeNum == 0) {
                        responseData.setMsg("支付渠道[" + payMode + "]不存在");
                        responseData.setMsgCode("od.addorder.paymode.notexist");

                        logger.info("==validate order===:od.addorder.paymode.notexist,escOrderCode:" + order.getEscOrderCode() != null ? order.getEscOrderCode() : "");
                        responseData.setSuccess(false);
                        return responseData;
                    }
                }

                //检验支付金额是否为空
                if (null == payAmount) {
                    responseData.setMsg("支付金额为空");
                    responseData.setMsgCode("od.addorder.payamount.null");

                    logger.info("==validate order===:od.addorder.payamount.null,escOrderCode:" + order.getEscOrderCode() != null ? order.getEscOrderCode() : "");
                    responseData.setSuccess(false);
                    return responseData;
                }

                //检验支付时间是否为空
                if (null == payTime) {
                    responseData.setMsg("支付时间为空");
                    responseData.setMsgCode("od.addorder.paytime.null");

                    logger.info("==validate order===:od.addorder.paytime.null,escOrderCode:" + order.getEscOrderCode() != null ? order.getEscOrderCode() : "");
                    responseData.setSuccess(false);
                    return responseData;
                }

                //检验外部交易号是否为空
                if (StringUtils.isEmpty(outTradeNo)) {
                    responseData.setMsg("支付信息外部交易号为空");
                    responseData.setMsgCode("od.addorder.payouttradeno.null");

                    logger.info("==validate order===:od.addorder.payouttradeno.null,escOrderCode:" + order.getEscOrderCode() != null ? order.getEscOrderCode() : "");
                    responseData.setSuccess(false);
                    return responseData;
                }

                //检验流水号是否为空
                if (StringUtils.isEmpty(numberCode)) {
                    responseData.setMsg("支付信息流水号为空");
                    responseData.setMsgCode("od.addorder.paynumbercode.null");

                    logger.info("==validate order===:od.addorder.paynumbercode.null,escOrderCode:" + order.getEscOrderCode() != null ? order.getEscOrderCode() : "");
                    responseData.setSuccess(false);
                    return responseData;
                }
            }
        }

        responseData.setSuccess(true);
        return responseData;
    }

    /**
     * 获取订单行号
     *
     * @param orderId
     * @return
     */
    public Long getLineNumber(Long orderId) {
        //根据orderId获取最大的平台订单行号
        Long lineNumber = hmallOmOrderEntryMapper.selectMaxLineNumberByOrderId(orderId);

        if (lineNumber == null) {
            lineNumber = Long.parseLong(String.valueOf(10));
        } else {
            lineNumber = lineNumber + Long.parseLong(String.valueOf(10));
        }

        return lineNumber;
    }

    //增加PIN码信息
    private void addPinInfo(HmallOmOrderEntry orderEntry) throws ParseException {
        HmallPinInfo hmallPinInfo = new HmallPinInfo();
        //PIN码
        hmallPinInfo.setCode(orderEntry.getPin());
        //订单行号
        hmallPinInfo.setEntryCode(orderEntry.getCode());
        //事件编号
        hmallPinInfo.setEventCode("MAP2600");

        HmallPinAlter hmallPinAlter = hmallPinAlterMapper.getHmallPinAlter("MAP2600");
        String evevtDes = "";
        if (hmallPinAlter != null) {
            evevtDes = hmallPinAlter.getEventDes() == null ? "" : hmallPinAlter.getEventDes();
        }
        //事件描述
        hmallPinInfo.setEventDes(evevtDes);
        //系统
        hmallPinInfo.setSystem("ZMALL");
        //操作人员（账号)
        hmallPinInfo.setOperator("买家");
        //操作人电话
        hmallPinInfo.setMobile("");
        //操作时间
        hmallPinInfo.setOperationTime(orderEntry.getCreationDate());
        //节点信息
        hmallPinInfo.setEventInfo("订单已签收");
        //一级预警标记
        hmallPinInfo.setFlagLevel1("");
        //二级预警标记
        hmallPinInfo.setFlagLevel2("");
        //三级预警标记
        hmallPinInfo.setFlagLevel3("");
        //在PIN码表中插入信息
        hmallPinInfoMapper.insertSelective(hmallPinInfo);

        pinAlterInfoService.insertPinAlterInfo(hmallPinInfo);
    }

    public Integer getLogisticsLeadTime(String cityCode, String areaCode, String pointOfServiceCode, String shippingType) {

        // 检查发运方式
        if (!("LOGISTICS".equals(shippingType) || "EXPRESS".equals(shippingType))) {
            throw new IllegalArgumentException("发运方式不合法");
        }
        // 查询物流提前期
        AtpInvSourceInfo atpInvSourceInfoParam = new AtpInvSourceInfo();
        atpInvSourceInfoParam.setCityCode(cityCode);
        atpInvSourceInfoParam.setAreaCode(areaCode);
        atpInvSourceInfoParam.setStorageCode(pointOfServiceCode);
        List<AtpInvSourceInfo> atpInvSourceInfoList = atpInvSourceInfoMapper.select(atpInvSourceInfoParam);
        if (CollectionUtils.isEmpty(atpInvSourceInfoList)) {
            throw new RuntimeException("城市编码[" + cityCode + "]、区域编码[" + areaCode + "]、仓库编码[" + pointOfServiceCode + "]无法获取物流提前期");
        }

        Integer size = CollectionUtils.size(atpInvSourceInfoList);
        if (size > 1) {
            throw new RuntimeException("城市编码[" + cityCode + "]、区域编码[" + areaCode + "]、仓库编码[" + pointOfServiceCode + "]发现" + size + "条物流提前期");
        }

        if ("LOGISTICS".equals(shippingType)) {
            if (atpInvSourceInfoList.get(0).getLogisticsLeadTime() == null) {
                return 0;
            }
            return atpInvSourceInfoList.get(0).getLogisticsLeadTime();
        } else {
            if (atpInvSourceInfoList.get(0).getExpressLeadTime() == null) {
                return 0;
            }
            return atpInvSourceInfoList.get(0).getExpressLeadTime();
        }
    }

    /**
     * 增加金额更改记录头
     *
     * @param order
     * @return
     */
    private AmountChangeLog addAmountChangeLog(HmallOmOrder order) {
        AmountChangeLog changeLog = new AmountChangeLog();
        changeLog.setOrderId(order.getOrderId());
        changeLog.setEscOrderCode(order.getEscOrderCode());
        changeLog.setCode(order.getCode());
        changeLog.setPostFee(order.getPostFee());
        changeLog.setEpostFee(order.getEpostFee());
        changeLog.setFixFee(order.getFixFee());
        changeLog.setCouponFee(order.getCouponFee());
        changeLog.setDiscountFee(order.getDiscountFee());
        changeLog.setTotalDiscount(order.getTotalDiscount());
        changeLog.setNetAmount(new BigDecimal(order.getOrderAmount()).subtract(new BigDecimal(order.getPostFee())).subtract(new BigDecimal(order.getEpostFee())).subtract(new BigDecimal(order.getFixFee())).doubleValue());
        changeLog.setInsertTime(System.currentTimeMillis());
        amountChangeLogMapper.insertSelective(changeLog);
        return changeLog;
    }

    /**
     * 增加金额更改记录行
     *
     * @param changeLog
     * @param orderEntry
     */
    private void addAmountChangeLogEntry(AmountChangeLog changeLog, HmallOmOrderEntry orderEntry) {
        AmountChangeLogEntry changeLogEntry = new AmountChangeLogEntry();
        changeLogEntry.setOrderEntryId(orderEntry.getOrderEntryId());
        changeLogEntry.setOrderId(changeLog.getOrderId());
        changeLogEntry.setCode(changeLog.getCode());
        changeLogEntry.setUnitFee(orderEntry.getUnitFee());
        changeLogEntry.setTotalFee(orderEntry.getTotalFee());
        changeLogEntry.setShippingFee(orderEntry.getShippingFee());
        changeLogEntry.setInstallationFee(orderEntry.getInstallationFee());
        changeLogEntry.setDiscountFee(orderEntry.getDiscountFee());
        changeLogEntry.setDiscountFeel(orderEntry.getDiscountFeel());
        changeLogEntry.setCouponFee(orderEntry.getCouponFee());
        changeLogEntry.setTotalDiscount(orderEntry.getTotalDiscount());
        changeLogEntry.setInsertTime(changeLog.getInsertTime());
        amountChangeLogEntryMapper.insertSelective(changeLogEntry);
    }

    /**
     * 增加书面记录
     *
     * @param order
     * @param orderEntry
     */
    private void addSoChangeLog(HmallOmOrder order, HmallOmOrderEntry orderEntry) {
        HmallOmOrder orderInfo = hmallOmOrderMapper.selectByPrimaryKey(order);

        HmallSoChangeLog soChangeLog = new HmallSoChangeLog();
        soChangeLog.setOrderId(order.getOrderId());
        soChangeLog.setOrderEntryId(orderEntry.getOrderEntryId());
        soChangeLog.setPin(orderEntry.getPin());
        soChangeLog.setOrderType("1");
        soChangeLog.setOrderEntryType("1");
        soChangeLog.setOrderCreateDate(orderInfo.getCreationDate());
        soChangeLog.setProductId(orderEntry.getProductId());
        soChangeLog.setParentOrderId(order.getOrderId());
        HmallMstProduct mstProduct = new HmallMstProduct();
        mstProduct.setProductId(orderEntry.getProductId());
        HmallMstProduct product = hmallMstProductMapper.selectByPrimaryKey(mstProduct);
        Example example = new Example(HmallSoChangeLog.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId", order.getOrderId());
        criteria.andEqualTo("orderEntryId", orderEntry.getOrderEntryId());
        example.setOrderByClause("CREATION_DATE desc");
        List<HmallSoChangeLog> changeLogList = soChangeLogMapper.selectByExample(example);
        if (orderEntry.getParentLine() != null) {
            HmallOmOrderEntry parentEntry = new HmallOmOrderEntry();
            parentEntry.setOrderId(orderEntry.getParentLine());
            HmallOmOrderEntry entryInfo = hmallOmOrderEntryMapper.selectByPrimaryKey(parentEntry);
            Double compPrice = getPriceForComponentLine(orderEntry, entryInfo.getInternalPrice());
            soChangeLog.setProductType("3");
            soChangeLog.setQuantity(orderEntry.getQuantity().intValue());
            soChangeLog.setTotalFee(new BigDecimal(compPrice.toString()));
            soChangeLog.setParentProductId(entryInfo.getProductId());
            soChangeLog.setParentOrderEntryId(entryInfo.getOrderEntryId());
            if (changeLogList.size() > 0) {
                soChangeLog.setChangeQuantity(orderEntry.getQuantity().intValue() - changeLogList.get(0).getQuantity());
                soChangeLog.setChangeFee(new BigDecimal(compPrice.toString()).subtract(changeLogList.get(0).getTotalFee()));
            }
        } else {
            if (product.getIsSuit().equals("Y")) {
                soChangeLog.setProductType("1");
            } else {
                soChangeLog.setProductType("2");
            }
            soChangeLog.setQuantity(orderEntry.getQuantity().intValue());
            soChangeLog.setTotalFee(new BigDecimal(orderEntry.getTotalFee().toString()));
            soChangeLog.setParentProductId(orderEntry.getProductId());
            soChangeLog.setParentOrderEntryId(orderEntry.getOrderEntryId());
            if (changeLogList.size() > 0) {
                soChangeLog.setChangeQuantity(orderEntry.getQuantity().intValue() - changeLogList.get(0).getQuantity());
                soChangeLog.setChangeFee(new BigDecimal(orderEntry.getTotalFee().toString()).subtract(changeLogList.get(0).getTotalFee()));
            }
        }
        soChangeLogMapper.insertSelective(soChangeLog);
    }

    /**
     * 获取套件子件的价格
     *
     * @param dto
     * @return
     */
    public Double getPriceForComponentLine(HmallOmOrderEntry dto, double price) {
        Double compPrice = 0.0, allRate = 0.0;
        DecimalFormat df = new DecimalFormat("######0.00");
        //根据子行，找出该子行对应的父行的所有子行
        HmallOmOrderEntry entry = new HmallOmOrderEntry();
        entry.setParentLine(dto.getParentLine());
        List<HmallOmOrderEntry> list = hmallOmOrderEntryMapper.select(entry);
        if (CollectionUtils.isNotEmpty(list)) {
            double totalInternalPrice = 0.00;
            //获取所有子行的internalPrice总和
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getInternalPrice() != null) {
                    totalInternalPrice = totalInternalPrice + list.get(i).getInternalPrice();
                }
            }
            if (totalInternalPrice != 0.00 && dto.getInternalPrice() != null) {
                //rate = dto.getInternalPrice() / totalInternalPrice;
                compPrice = Double.valueOf(df.format(dto.getInternalPrice() / totalInternalPrice * price));
            }
            if (list.get(list.size() - 1).getOrderEntryId().equals(dto.getOrderEntryId())) {
                for (int i = 0; i < list.size() - 1; i++) {
                    allRate += Double.valueOf(df.format(list.get(i).getInternalPrice() / totalInternalPrice * price));
                }
                compPrice = Double.valueOf(df.format(price - allRate));
            }
        }
        return compPrice;
    }
}
