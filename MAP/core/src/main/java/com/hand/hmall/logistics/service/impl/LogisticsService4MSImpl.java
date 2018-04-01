package com.hand.hmall.logistics.service.impl;

import com.hand.hmall.logistics.code.MessageCode;
import com.hand.hmall.logistics.exception.ImportLogisticsException;
import com.hand.hmall.logistics.mapper.*;
import com.hand.hmall.logistics.pojo.*;
import com.hand.hmall.logistics.service.ILogisticsService4MS;
import com.hand.hmall.logistics.trans.WmsLogisticsInfo;
import com.hand.hmall.logistics.util.RrsStatusNo;
import com.hand.hmall.logistics.util.ThreadLogger;
import com.hand.hmall.om.dto.ConsignmentToRRS;
import com.hand.hmall.om.dto.OrderItem;
import com.hand.hmall.om.mapper.ConsignmentMapper;
import com.markor.map.framework.common.exception.DataProcessException;
import com.markor.map.logistics.service.ILogisticsService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

//import com.markor.map.logistics.entities.ConsignmentToRRS;
//import com.markor.map.logistics.entities.OrderItem;

/**
 * @author chenzhigang
 * @version 0.1
 * @name LogisticsService4MSImpl
 * @description 面向微服务的物流服务接口实现类
 * @date 2017/12/12
 */
public class LogisticsService4MSImpl implements ILogisticsService4MS {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    // 错误日志service
    @Autowired
    LgsLogManagerMapper mapper;
    // 发货单service
    @Autowired
    private LgsConsignmentMapper lgsConsignmentMapper;
    // 发货记录service
    @Autowired
    private LgsTraceMapper traceMapper;
    // WMS物流信息持久层接口
    @Autowired
    private LgsWmsLogisticsMapper wmsMapper;

    @Autowired
    private LgsPinMapper pinMapper;

    @Autowired
    private LgsDeliveryOrderMapper deliveryOrderMapper;

    @Autowired
    private LgsDeliveryOrderEntryMapper deliveryEntryMapper;

    // 接收WMS物流信息并处理后，调用将发货单信息发送到RRS的dubbo服务
    @Autowired
    private ILogisticsService logisticsService;

    // 发货单持久层mapper接口
    @Autowired
    private ConsignmentMapper consignmentMapper;

    /**
     * 日日顺发货记录更新用service
     *
     * @param consignmentInfo
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public Map<String, Object> rrsOrderHfs(ConsignmentInfo consignmentInfo) {
        // 返回结果
        Map maps = new HashMap<String, Object>();
        Long lineNo = RrsStatusNo.getStatusNo(consignmentInfo.getServiceStatus());
        DeliveryOrder dod = deliveryOrderMapper.selectDeliveryOrderByCode(consignmentInfo.getServiceCode());
        if (dod != null) {
            Trace trace = new Trace();
            trace.setLineid(lineNo);
            trace.setConsignmentId(dod.getConsignmentId());
            trace.setStatus(consignmentInfo.getServiceStatus());
            trace.setOperator(consignmentInfo.getOperator());
            trace.setOperatorphone(consignmentInfo.getOperatorPhone());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//小写的mm表示的是分钟
            Date date = null;
            try {
                date = sdf.parse(consignmentInfo.getOperatorDate());
            } catch (Exception e) {

                this.logError(this.getClass(), "接收日日顺传入的物流信息", 9999999L, MessageCode.UR_LOGIN_ERROR_06.getValue());
                maps.put("code", 500);
                maps.put("msg", MessageCode.UR_LOGIN_ERROR_06.getValue());
                return maps;
            }
            trace.setOperatetime(date);
            trace.setContent(consignmentInfo.getOperatorDesc());
            trace.setPictureUrl(consignmentInfo.getPictureUrl());
            trace.setAppointDate(consignmentInfo.getAppointDate());
            trace.setDeliveryDate(consignmentInfo.getDeliveryDate());
            trace.setSignDate(consignmentInfo.getSignDate());
            trace.setChangeAppointDate(consignmentInfo.getChangeAppointDate());
            trace.setThirdServiceCode(consignmentInfo.getThirdServiceCode());
            trace.setDeliveryOrderId(dod.getDeliveryOrderId());
            traceMapper.insertSelective(trace);

            //开始插入pin码表
            List<LgsPin> pinList = pinMapper.selectPinByDelivery(dod);
            for (LgsPin pin : pinList) {
                pin.setEventCode(consignmentInfo.getServiceStatus());
                pin.setEventInfo(consignmentInfo.getOperatorDesc());
                pin.setMobile(consignmentInfo.getOperatorPhone());
                pin.setOperator(consignmentInfo.getOperator());
                pin.setOperationTime(date);
                pinMapper.insertPin(pin);
            }

            // 如果发货单状态为TMS_SIGN，则更新发货单状态为TRADE_BUYER_SIGNED
            LgsConsignment consignment = new LgsConsignment();
            consignment.setConsignmentId(dod.getConsignmentId());
            if ("TMS_SIGN".equals(consignmentInfo.getServiceStatus())) {
                lgsConsignmentMapper.updateConsignmentStatusToSigned(consignment);
            }
        } else {
            maps.put("code", 500);
            maps.put("msg", MessageCode.UR_LOGIN_ERROR_07.getValue());
            return maps;
        }

        maps.put("code", 200);
        maps.put("msg", MessageCode.UR_LOGIN_SUCCESS_200.getValue());
        return maps;
    }

    /**
     * 添加日志
     *
     * @param clazz          所在类
     * @param programDesc    类描述
     * @param itemId         数据主键
     * @param returnMessage  返回消息
     * @param sourcePlatform 平台
     * @param processStatus  状态
     */
    public void log(Class<?> clazz, String programDesc, Long itemId, String returnMessage, String sourcePlatform, String processStatus) {
        LgsLogManager logManager = new LgsLogManager();
        logManager.setDataPrimaryKey(itemId);
        logManager.setProgramName(clazz.toString());
        logManager.setProgramDescription(programDesc);
        logManager.setStartTime(new Date());
        logManager.setEndTime(new Date());
        logManager.setReturnMessage(returnMessage);
        logManager.setProcessStatus(processStatus);
        logManager.setSourcePlatform(sourcePlatform);
        mapper.insertSelective(logManager);
    }

    /**
     * 添加错误日志
     *
     * @param clazz         事务类
     * @param programDesc   类描述
     * @param itemId        操作对象
     * @param returnMessage 返回消息
     */
    public void logError(Class<?> clazz, String programDesc, Long itemId, String returnMessage) {
        log(clazz, programDesc, itemId, returnMessage, "HMALL", "E");
    }

    /**
     * 根据SAP_CODE查询送货单
     *
     * @param sapCode
     * @return
     */
    public List<WmsConsignment> queryConsignmentsBySAPCode(String sapCode) {
        return wmsMapper.queryConsignmentsBySAPCode(sapCode);
    }

    /**
     * 导入特定“销售订单号 & 送货单号”下的物流信息
     *
     * @param soOrderId      - 销售订单号 & 送货单号
     * @param logisticsInfos - WMS导入的物流信息
     */
    private void importConsignmentLogistics(String soOrderId, List<WmsLogisticsInfo> logisticsInfos) {
        ThreadLogger threadLogger = new ThreadLogger();
        threadLogger.append("导入销售订单号为 " + soOrderId + " 的物流数据 begin");

        // 根据销售订单号查询发货单列表
        List<WmsConsignment> consignments = queryConsignmentsBySAPCode(soOrderId);

        if (consignments.isEmpty()) {
            logger.warn("no.consignment.exist - 找不到对应的发货单");
            throw new ImportLogisticsException("no.consignment.exist", "找不到对应的发货单");
        } else if (consignments.size() > 1) {
            logger.warn("repeated.consignment - 根据销售订单匹配到的发货单过多[" + consignments.size() + "]");
            throw new ImportLogisticsException
                    ("repeated.consignment", "根据销售订单匹配到的发货单过多[" + consignments.size() + "]");
        }

        WmsConsignment wmsConsignment = consignments.get(0);

        String consignmentStatus = wmsConsignment.getStatus();
        // WAIT_BUYER_CONFIRM
        if (!("WAIT_FOR_DELIVERY".equalsIgnoreCase(consignmentStatus) || "WAIT_BUYER_CONFIRM".equalsIgnoreCase(consignmentStatus))) {
            logger.warn("consignment.status.error - 发货单[soOrderId=" + wmsConsignment.getSpaCode() + "]状态不为 WAIT_FOR_DELIVERY 或 WAIT_BUYER_CONFIRM");
            throw new ImportLogisticsException("consignment.status.error", "发货单[soOrderId=" + wmsConsignment.getSpaCode() + "]状态不为 WAIT_FOR_DELIVERY 或 WAIT_BUYER_CONFIRM");
        }

        // 已拼接的物流单号
        List<String> existLogisticeNumbers = new ArrayList<>();
        // 初始化发货单的运单号，后续会将物流数据中的运单号拼接到当前字段
        if (wmsConsignment.getLogisticsNumber() != null && !"WAIT_FOR_DELIVERY".equalsIgnoreCase(consignmentStatus)) {
            if (wmsConsignment.getLogisticsNumber().contains(",")) {
                for (String ln : wmsConsignment.getLogisticsNumber().split(",")) {
                    existLogisticeNumbers.add(ln);
                }
            } else {
                existLogisticeNumbers.add(wmsConsignment.getLogisticsNumber());
            }
        }

        // 订单行
        List<Map> orderEntries;
        Map orderEntry;
        for (WmsLogisticsInfo li : logisticsInfos) {

            // 查询销售订单行
            orderEntries = wmsMapper.queryLogisticsOrderEntries(wmsConsignment.getConsignmentId(), li.getSoLineId());

            if (orderEntries.isEmpty()) {
                logger.warn("notFound.orderEntry - 根据销售订单行号[" + li.getSoLineId() + "]查询的订单行不存在");
                throw new ImportLogisticsException("notFound.orderEntry", "根据销售订单行号[" + li.getSoLineId() + "]查询的订单行不存在");
            } else if (orderEntries.size() > 1) {
                logger.warn("orderEntries.tooMany - 根据销售订单行号[" + li.getSoLineId() + "]查询的订单行存在多条[" + orderEntries.size() + "]");
                throw new ImportLogisticsException
                        ("orderEntries.tooMany", "根据销售订单行号[" + li.getSoLineId() + "]查询的订单行存在多条[" + orderEntries.size() + "]");
            }

            // 唯一对应的订单行（发货单行）
            orderEntry = orderEntries.get(0);

            // 已经拼接在订单行上的物流单号列表
            List<String> logisticsNumOnOrderEntry = new ArrayList<>();
            if (orderEntry.get("LOGISTICS_NUMBER") != null && !orderEntry.get("LOGISTICS_NUMBER").toString().trim().isEmpty()) {
                if (orderEntry.get("LOGISTICS_NUMBER").toString().contains(",")) {
                    for (String entryLn : ((String) orderEntry.get("LOGISTICS_NUMBER")).split(",")) {
                        logisticsNumOnOrderEntry.add(entryLn);
                    }
                } else {
                    logisticsNumOnOrderEntry.add(orderEntry.get("LOGISTICS_NUMBER").toString());
                }
            }

            // 添加新的物流单号到发货单行
            if (logisticsNumOnOrderEntry.indexOf(li.getTransOrderCode()) < 0) {
                logisticsNumOnOrderEntry.add(li.getTransOrderCode());
                orderEntry.put("LOGISTICS_NUMBER", StringUtils.join(logisticsNumOnOrderEntry, ","));
            }

            // 更新发货单行（订单行）
            wmsMapper.updateConsignmentEntry(orderEntry);

            // 添加新的物流单号（用于保存到发货单头）
            if (existLogisticeNumbers.indexOf(li.getTransOrderCode()) < 0) {
                existLogisticeNumbers.add(li.getTransOrderCode());
            }

            // 设置发货单的发货日期和物流公司信息
            try {
                wmsConsignment.setShippingDate(new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").parse(li.getSentDate()));
            } catch (ParseException e) {
                throw new ImportLogisticsException("date.format.error", "日期格式错误[" + li.getSentDate() + "]，yyyy/MM/dd hh:mm:ss");
            }
            // 根据物流公司编码查询其对应的ID
            Long logisticsId = wmsMapper.selectLogisticsIdByCode(li.getTransCode());
            wmsConsignment.setLogisticsCompanies(logisticsId);
        }

        // 拼接物流单号到服务单
        wmsConsignment.setLogisticsNumber(StringUtils.join(existLogisticeNumbers, ","));

        wmsConsignment.setStatus("WAIT_BUYER_CONFIRM");
        // 更新送货单的状态和运单号
        wmsMapper.updateConsignment(wmsConsignment);

        threadLogger.append("导入销售订单号为 " + soOrderId + " 的物流数据 success");
    }

    /**
     * 导入特定“销售订单号 & 送货单号”下的物流信息
     * 由于逻辑变更较多，故新建方法，参考importConsignmentLogistics
     *
     * @param soOrderId      - 销售订单号 & 送货单号
     * @param logisticsInfos - WMS导入的物流信息
     */
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    public void saveConsignmentLogistics(String soOrderId, List<WmsLogisticsInfo> logisticsInfos) {

        // 根据销售订单号查询发货单列表
        List<WmsConsignment> consignments = queryConsignmentsBySAPCode(soOrderId);

        // 如果没查到或查到多条则报错
        if (consignments.isEmpty()) {
            throw new ImportLogisticsException("no.consignment.exist", "找不到对应的发货单");
        } else if (consignments.size() > 1) {
            throw new ImportLogisticsException
                    ("repeated.consignment", "根据销售订单匹配到的发货单过多[" + consignments.size() + "]");
        }

        // 找到唯一一条发货单
        WmsConsignment wmsConsignment = consignments.get(0);

        // 订单类型
        String orderType = wmsConsignment.getOrderType();

        // 发货单状态
        String conStatus = wmsConsignment.getStatus();

        // 新增交货单生成逻辑
        addDeliveryEntry(wmsConsignment, logisticsInfos);

        // 判断发货单所关联的订单类型是否为NORMAL（销售单）或SWAP（换发单），如果不是则报错
        if (!("NORMAL".equals(orderType) || "SWAP".equals(orderType))) {
            throw new ImportLogisticsException("invalid.ordertype", "非法的订单类型[" + orderType + "]，必须为NORMAL或SWAP");
        }

        // 发货单状态必须为WAIT_FOR_DELIVERY或WAIT_BUYER_CONFIRM，否则报错
        if (!("WAIT_FOR_DELIVERY".equals(conStatus) || "WAIT_BUYER_CONFIRM".equals(conStatus))) {
            throw new ImportLogisticsException("invalid.consignment.status",
                    "非法的发货单状态[" + conStatus + "]，必须为WAIT_FOR_DELIVERY或WAIT_BUYER_CONFIRM");
        }

        // 将物流单号保存到订单行并插入到发货单头的物流单号集合
        List<String> existLogisticeNumbers = existLogisticeNumbers(wmsConsignment, logisticsInfos, orderType);

        // 拼接物流单号到发货单头
        wmsConsignment.setLogisticsNumber(StringUtils.join(existLogisticeNumbers, ","));

        // 如果发货单状态为WAIT_FOR_DELIVERY，保存发货时间和物流公司
        if ("WAIT_FOR_DELIVERY".equals(conStatus)) {

            // WAIT_FOR_DELIVERY时，表明第一次物流回传，logisticsInfos集合大小为1，不会出现发货时间和物流公司重复赋值的情况
            for (WmsLogisticsInfo logisticsInfo : logisticsInfos) {
                // 设置发货单的发货日期
                setShippingDate(wmsConsignment, logisticsInfo.getSentDate());

                // 设置发货单头的物流公司，根据物流公司编码查询其对应的ID
                setLogisticsCompanies(wmsConsignment, logisticsInfo.getTransCode());
            }

            // 更新发货单状态
            wmsConsignment.setStatus("WAIT_BUYER_CONFIRM");

            // 根据订单id查询订单
            Map order = wmsMapper.selectOrderByOrderId(wmsConsignment.getOrderId());
            if (order == null) {
                throw new ImportLogisticsException("no.order.exist", "订单[" + wmsConsignment.getOrderId() + "]不存在");
            }

            // 如果是换发单，将订单状态更新为TRADE_FINISHED
            if ("SWAP".equals(orderType)) {
                // 将订单状态更新为TRADE_FINISHED
                order.put("ORDER_STATUS", "TRADE_FINISHED");
                order.put("TRADE_FINISH_TIME", new Date());

                //add by liwei 2017-10-13   hmall-logistics-service/MAG-1291

                //将发货单上的【SYNC_FLAG】置为N
                wmsConsignment.setSyncflag("N");

                //将发货单上的【SYNC_ZMALL】置为N
                wmsConsignment.setSyncZmall("N");

                //将发货单关联的订单上的【SYNC_ZMALL】置为N
                order.put("SYNC_ZMALL", "N");
            } else if ("NORMAL".equals(orderType)) {
                //发货单关联的订单为正常订单

                //将发货单上的【SYNC_FLAG】置为N
                wmsConsignment.setSyncflag("N");

                //将发货单上的【SYNC_ZMALL】置为N
                wmsConsignment.setSyncZmall("N");
            }
            //更新订单
            wmsMapper.updateOrderStatus(order);
        }

        // 更新发货单
        wmsMapper.updateConsignment(wmsConsignment);
    }

    /**
     * 创建交货单行处理逻辑
     *
     * @param wmsConsignment - 发货单
     * @param logisticsInfos - WMS物流信息
     */
    private void addDeliveryEntry(WmsConsignment wmsConsignment, List<WmsLogisticsInfo> logisticsInfos) {

        for (WmsLogisticsInfo wmsInfo : logisticsInfos) {

            Map<String, Object> params = new HashMap();
            params.put("deliveryCode", wmsInfo.getDnOrderId());
            params.put("consignmentId", wmsConsignment.getConsignmentId());

            // 根据发货单查询DN单
            DeliveryOrder dn = deliveryOrderMapper.queryByConsignment(params);

            if (dn == null) {
                dn = new DeliveryOrder();
                dn.setDeliveryNote(wmsInfo.getDnOrderId());
//                Map mstLogisticso = wmsMapper.queryMstLogisticsoByCode(wmsInfo.getTransCode());
//                if (mstLogisticso != null) {
//                    dn.setLogisticsCompany(mstLogisticso.get("NAME").toString());
//                }
                dn.setLogisticsCompany(wmsInfo.getTransCode());
                dn.setLogisticsNumber(wmsInfo.getTransOrderCode());
                dn.setConsignmentId(wmsConsignment.getConsignmentId());
                try {
                    dn.setShippingDate(new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").parse(wmsInfo.getSentDate()));
                } catch (ParseException e) {
                    logger.error("日期格式错误: " + wmsInfo.getSentDate());
                }
                dn.setSyncThirdLogistics("N");

                dn.setObjectVersionNumber(1L);
                dn.setCreatedBy(1L);
                dn.setCreationDate(new Date());
                dn.setLastUpdatedBy(1L);
                dn.setLastUpdateDate(new Date());

                List<Map> brothers = deliveryOrderMapper.queryExistDeliveryOrdersByConsignmentId(dn.getConsignmentId());
                dn.setIsMain(brothers.isEmpty() ? "Y" : "N");

                deliveryOrderMapper.insert(dn);
            }

            DeliveryOrderEntry doe = new DeliveryOrderEntry();
            doe.setDeliveryOrderId(dn.getDeliveryOrderId());
            Map orderEntryInfo = queryLogisticsOrderEntry(wmsConsignment.getConsignmentId(), wmsInfo.getSoLineId());
            doe.setOrderEntryId(((BigDecimal) orderEntryInfo.get("ORDER_ENTRY_ID")).longValue());
            doe.setShippedQuantity(wmsInfo.getSentAmount());
            doe.setDeliveryNoteLine(wmsInfo.getDnLineId());

            doe.setObjectVersionNumber(1L);
            doe.setCreatedBy(1L);
            doe.setCreationDate(new Date());
            doe.setLastUpdatedBy(1L);
            doe.setLastUpdateDate(new Date());

            deliveryEntryMapper.insert(doe);
        }

    }

    /**
     * 设置发货时间
     *
     * @param wmsConsignment 发货单
     * @param sendDate       发货时间
     */
    private void setShippingDate(WmsConsignment wmsConsignment, String sendDate) {
        if (StringUtils.isNotBlank(sendDate)) {
            try {
                wmsConsignment.setShippingDate(new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").parse(sendDate));
            } catch (ParseException e) {
                throw new ImportLogisticsException("date.format.error", "日期格式错误[" + sendDate + "]，yyyy/MM/dd hh:mm:ss");
            }
        }
    }

    /**
     * 设置物流公司
     *
     * @param wmsConsignment 发货单
     * @param transCode      物流公司编号
     */
    private void setLogisticsCompanies(WmsConsignment wmsConsignment, String transCode) {
        if (StringUtils.isNotBlank(transCode)) {
            if (!"000000".equals(transCode)) {
                Long logisticsId = wmsMapper.selectLogisticsIdByCode(transCode);
                if (logisticsId == null) {
                    throw new ImportLogisticsException("invalid.logistics.company", "物流公司编号[" + transCode + "]不存在");
                }
                wmsConsignment.setLogisticsCompanies(logisticsId);
            } else {
                wmsConsignment.setLogisticsCompanies(null);
            }
        }
    }

    /**
     * 根据发货单id和销售订单号查询订单行，查到多条或没查到报错
     *
     * @param consignmentId 发货单id
     * @param soLineId      销售订单号
     * @return Map
     */
    private Map queryLogisticsOrderEntry(Long consignmentId, String soLineId) {
        // 查询销售订单行
        List<Map> orderEntries = wmsMapper.queryLogisticsOrderEntries(consignmentId, soLineId);

        if (orderEntries.isEmpty()) {
            throw new ImportLogisticsException("notFound.orderEntry", "根据销售订单行号[" + soLineId + "]查询的订单行不存在");
        } else if (orderEntries.size() > 1) {
            throw new ImportLogisticsException
                    ("orderEntries.tooMany", "根据销售订单行号[" + soLineId + "]查询的订单行存在多条[" + orderEntries.size() + "]");
        }

        // 唯一对应的订单行（发货单行）
        return orderEntries.get(0);
    }

    /**
     * 向订单行的LogisticsNum字段中追加物流单号（如果不存在）
     *
     * @param orderEntry
     * @param transOrderCode
     */
    private void appendLogisticsNum(Map orderEntry, String transOrderCode) {
        // 将订单行上面的物流单号按照","进行分割
        List<String> logisticsNumOnOrderEntry = new ArrayList<>();
        if (orderEntry.get("LOGISTICS_NUMBER") != null && !orderEntry.get("LOGISTICS_NUMBER").toString().trim().isEmpty()) {
            if (orderEntry.get("LOGISTICS_NUMBER").toString().contains(",")) {
                for (String entryLn : ((String) orderEntry.get("LOGISTICS_NUMBER")).split(",")) {
                    logisticsNumOnOrderEntry.add(entryLn);
                }
            } else {
                logisticsNumOnOrderEntry.add(orderEntry.get("LOGISTICS_NUMBER").toString());
            }
        }

        // 添加新的物流单号到发货单行
        if (logisticsNumOnOrderEntry.indexOf(transOrderCode) < 0) {
            logisticsNumOnOrderEntry.add(transOrderCode);
            orderEntry.put("LOGISTICS_NUMBER", StringUtils.join(logisticsNumOnOrderEntry, ","));
        }
    }

    /**
     * 对发货单上面的LogisticsNumber按照","进行分割
     *
     * @param wmsConsignment 发货单
     * @param logisticsInfos 物流信息
     * @return List<String>
     */
    private List<String> existLogisticeNumbers(WmsConsignment wmsConsignment, List<WmsLogisticsInfo> logisticsInfos, String orderType) {

        // 拆分发货单头的LogisticsNumber
        List<String> existLogisticeNumbers = new ArrayList<>();
        if (wmsConsignment.getLogisticsNumber() != null) {
            if (wmsConsignment.getLogisticsNumber().contains(",")) {
                for (String ln : wmsConsignment.getLogisticsNumber().split(",")) {
                    existLogisticeNumbers.add(ln);
                }
            } else {
                existLogisticeNumbers.add(wmsConsignment.getLogisticsNumber());
            }
        }

        // 将物流单号保存到发货单行
        for (WmsLogisticsInfo logisticsInfo : logisticsInfos) {

            // 查询销售订单行
            Map orderEntry = queryLogisticsOrderEntry(wmsConsignment.getConsignmentId(), logisticsInfo.getSoLineId());

            // 将物流单号保存到发货单行
            appendLogisticsNum(orderEntry, logisticsInfo.getTransOrderCode());

            // 更新发货单行（订单行）
            wmsMapper.updateConsignmentEntry(orderEntry);

            // 添加新的物流单号到existLogisticeNumbers（用于保存到发货单头）
            if (!existLogisticeNumbers.contains(logisticsInfo.getTransOrderCode())) {
                existLogisticeNumbers.add(logisticsInfo.getTransOrderCode());
            }
        }

        return existLogisticeNumbers;
    }

    /**
     * 根据销售订单行号soOrderId对物流数据进行分类
     *
     * @param logisticsInfo
     * @return java.lang.Map - key: 销售订单行号, value: 匹配销售订单行号的物流信息
     */
    public Map<String, List<WmsLogisticsInfo>> classifyBySoOrderId(List<WmsLogisticsInfo> logisticsInfo) {
        ThreadLogger threadLogger = new ThreadLogger();
        threadLogger.append("对物流数据进行分类 begin");

        // 按销售订单号分类，同时对销售订单行号进行校验
        Map<String, List<WmsLogisticsInfo>> soOrderIdMap = new HashMap<>();
        for (WmsLogisticsInfo li : logisticsInfo) {
            if (li.getSoOrderId() == null || li.getSoOrderId().trim().isEmpty()) {
                logger.warn("soOrderId.null - 销售订单号为空");
                throw new ImportLogisticsException("soOrderId.null", "销售订单号为空");
            }
            if (!soOrderIdMap.containsKey(li.getSoOrderId())) {
                soOrderIdMap.put(li.getSoOrderId(), new LinkedList<>());
            }
            soOrderIdMap.get(li.getSoOrderId()).add(li);
        }

        threadLogger.append("对物流数据进行分类 success");

        return soOrderIdMap;
    }

    /**
     * 批量导入WMS物流数据
     *
     * @param logisticsInfo
     */
    @Transactional
    @Override
    public void batchImport(List<WmsLogisticsInfo> logisticsInfo) {

        logger.info(">>>>>>>>>>>>>>>>>>>> enter >>>>>>>>>>>>>>>>>>>>");
        logger.info(logisticsInfo.toString());
        logger.info("###############################################");

        /**
         * 1、处理WMS推送过来的物流信息：生成发货单和交货单
         */
        // 将物流信息按照销售订单号分类
        Map<String, List<WmsLogisticsInfo>> soOrderIdMap = classifyBySoOrderId(logisticsInfo);
        for (String soOrderId : soOrderIdMap.keySet()) {
            saveConsignmentLogistics(soOrderId, soOrderIdMap.get(soOrderId));
        }

        /**
         * 2、查询需要推送给RRS的发货单信息，然后调用推送到RRS的相关dubbo
         */
        List<ConsignmentToRRS> consignmentToRRSList;
        try {
            consignmentToRRSList = consignmentMapper.queryConsignmentForRRS();
        } catch (Exception e) {
            logger.error(this.getClass().getCanonicalName(), e);
            logger.warn("data.error - 数据错误：" + e.getMessage());
            throw new ImportLogisticsException("data.error", "数据错误：" + e.getMessage());
        }

        for (ConsignmentToRRS dto : consignmentToRRSList) {
            for (OrderItem odi : dto.getOrder_item_list()) {

                try {
                    /**
                     * 将长 X 宽 X 高的体积转换为立方米
                     */
                    if (odi.getItem_volume_str().indexOf("*") > 0) {
                        String[] volumeStrArr = odi.getItem_volume_str().split("[*]", -1);
                        if (volumeStrArr.length == 3) {
                            odi.setItem_volume(new BigDecimal(volumeStrArr[0]).multiply(new BigDecimal(volumeStrArr[1])).multiply(new BigDecimal(volumeStrArr[2])).divide(new BigDecimal("1000000000"))
                                    .setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue());
                        }
                    }
                    if (odi.getItem_volume_str().indexOf("X") > 0) {
                        String[] volumeStrArr = odi.getItem_volume_str().split("X", -1);
                        if (volumeStrArr.length == 3) {
                            odi.setItem_volume(new BigDecimal(volumeStrArr[0]).multiply(new BigDecimal(volumeStrArr[1])).multiply(new BigDecimal(volumeStrArr[2])).divide(new BigDecimal("1000000000"))
                                    .setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue());
                        }
                    }
                } catch (RuntimeException re) {
                    logger.warn("converted.to.cubic.meters.error - 将长 × 宽 × 高的体积转换为立方米计算错误");
                    throw new ImportLogisticsException("converted.to.cubic.meters.error", "将长 × 宽 × 高的体积转换为立方米计算错误");
                }

                try {
                    /**
                     * 计算总体积
                     */
                    odi.setItem_volume(new BigDecimal(odi.getItem_volume()).multiply(new BigDecimal(odi.getItem_quantity())).doubleValue());
                } catch (RuntimeException re) {
                    logger.warn("calculate.total.volume.error - 计算总体积错误");
                    throw new ImportLogisticsException("calculate.total.volume.error", "计算总体积错误");
                }
            }
        }

        try {
            // 转换：com.hand.hmall.om.dto.ConsignmentToRRS -> com.markor.map.logistics.entities.ConsignmentToRRS
            List<com.markor.map.logistics.entities.ConsignmentToRRS> consignmentToRRSs = new ArrayList<>();
            consignmentToRRSList.stream().forEach((c) -> {
                com.markor.map.logistics.entities.ConsignmentToRRS tc = new com.markor.map.logistics.entities.ConsignmentToRRS();
                try {
                    BeanUtils.copyProperties(tc, c);
                    tc.setOrder_item_list(new ArrayList<>());
                    c.getOrder_item_list().stream().forEach((o) -> {
                        com.markor.map.logistics.entities.OrderItem oi = new com.markor.map.logistics.entities.OrderItem();
                        try {
                            BeanUtils.copyProperties(oi, o);
                            tc.getOrder_item_list().add(oi);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            logger.error(this.getClass().getCanonicalName(), e);
                            logger.warn("data.model.error - 发货单数据模型错误");
                            throw new ImportLogisticsException("data.model.error", "发货单数据模型错误");
                        }
                    });
                } catch (IllegalAccessException | InvocationTargetException e) {
                    logger.error(this.getClass().getCanonicalName(), e);
                    logger.warn("data.model.error - 发货单数据模型错误");
                    throw new ImportLogisticsException("data.model.error", "发货单数据模型错误");
                }
                consignmentToRRSs.add(tc);
            });
            try {
                logisticsService.saveAndSendConsignmentToRRS(consignmentToRRSs);
                for (ConsignmentToRRS c : consignmentToRRSList) {
                    consignmentMapper.updateSyncThirdLogistics(c);
                }
            } catch (DataProcessException e) {
                logger.warn("send.consignment.to.RRS.failed - 发送日日顺失败", e);
                throw new ImportLogisticsException("send.consignment.to.RRS.failed", "发送日日顺失败" + e.getMessage());
            }
        } catch (Exception e) {
            logger.error(this.getClass().getCanonicalName(), e);
            logger.warn("send.consignment.to.RRS.failed - 发送日日顺失败");
            throw new ImportLogisticsException("send.consignment.to.RRS.failed", "发送日日顺失败");
        }
    }
}
