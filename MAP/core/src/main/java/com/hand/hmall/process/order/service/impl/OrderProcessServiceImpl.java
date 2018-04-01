package com.hand.hmall.process.order.service.impl;

import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.im.dto.ImAtpInface;
import com.hand.hap.im.dto.ImAtpInfaceResponse;
import com.hand.hap.im.service.IImAtpInfaceService;
import com.hand.hap.mam.dto.MamSoApproveHis;
import com.hand.hap.mam.dto.MamVcodeHeader;
import com.hand.hap.mam.dto.MamVcodeLines;
import com.hand.hap.mam.service.IMamSoApproveHisService;
import com.hand.hap.mam.service.IMamVcodeHeaderService;
import com.hand.hap.mam.service.IMamVcodeLinesService;
import com.hand.hap.mybatis.entity.Example;
import com.hand.hap.system.dto.CodeValue;
import com.hand.hap.system.service.ICodeService;
import com.hand.hmall.common.service.IGlobalVariantService;
import com.hand.hmall.common.service.ISequenceGenerateService;
import com.hand.hmall.log.dto.LogManager;
import com.hand.hmall.log.service.ILogManagerService;
import com.hand.hmall.mst.dto.Catalogversion;
import com.hand.hmall.mst.dto.Product;
import com.hand.hmall.mst.service.ICatalogversionService;
import com.hand.hmall.mst.service.IProductService;
import com.hand.hmall.om.dto.Consignment;
import com.hand.hmall.om.dto.HmallSoChangeLog;
import com.hand.hmall.om.dto.Order;
import com.hand.hmall.om.dto.OrderEntry;
import com.hand.hmall.om.mapper.HmallSoChangeLogMapper;
import com.hand.hmall.om.mapper.OrderEntryMapper;
import com.hand.hmall.om.mapper.OrderMapper;
import com.hand.hmall.om.service.IConsignmentService;
import com.hand.hmall.om.service.INotificationService;
import com.hand.hmall.om.service.IOrderEntryService;
import com.hand.hmall.om.service.IOrderService;
import com.hand.hmall.pin.service.IPinService;
import com.hand.hmall.process.order.service.IOrderProcessService;
import com.hand.hmall.util.Constants;
import com.markor.map.external.pointservice.dto.PointOfServiceDto;
import com.markor.map.external.pointservice.service.IPointOfServiceExternalService;
import com.markor.map.framework.common.interf.entities.PaginatedList;
import com.markor.map.framework.restclient.RestClient;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import okhttp3.Response;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.Assert;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 马君
 * @version 0.1
 * @name OrderProcessServiceImpl
 * @description 订单流程服务实现
 * @date 2017/7/26 9:15
 */
@Service
@Transactional(rollbackFor = {Exception.class, RuntimeException.class})
public class OrderProcessServiceImpl implements IOrderProcessService {

    private static final String ORDER_PRICE_CAL_URL = "/hap-service/h/price/calculateOrderPrice";
    private static final String SEGMENT_SPLIT_CHAR = "@";
    private static final String DEFAULT_BRAND = "ZEST";   // 恣在家
    private static final String DEFAULT_PS_CODE = "W001"; // 泰达仓
    private static final String SERVICE_DESC = "订单流程服务实现";
    private static final String ORDER_TRIGGER_GOODS = "MAP0200";  //订单触发备货
    private static final String ORDER_BOM_APPROVED = "MAP0400";  //BOM审核
    private static final String CUSTOM_TYPE_REGULAR = "A4";
    private static final String REQUIRED = "B";
    private static final String COLUMN_SPLIT_CHAR = "/";
    private static final String RECORD_SPLIT_CHAR = ";";
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IImAtpInfaceService atpInfaceService;

    @Autowired
    private IOrderEntryService iOrderEntryService;

    @Autowired
    private IPointOfServiceExternalService iPointOfServiceExternalService;

    @Autowired
    private IConsignmentService iConsignmentService;

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IMamVcodeHeaderService iMamVcodeHeaderService;

    @Autowired
    private IMamVcodeLinesService iMamVcodeLinesService;

    @Autowired
    private ICatalogversionService iCatalogversionService;

    @Autowired
    private ISequenceGenerateService iSequenceGenerateService;

    @Autowired
    private ILogManagerService iLogManagerService;

    @Autowired
    private IGlobalVariantService iGlobalVariantService;

    @Autowired
    private IPinService iPinService;

    @Autowired
    private ICodeService iCodeService;

    @Autowired
    private RestClient restClient;

    @Autowired
    private IMamSoApproveHisService iMamSoApproveHisService;

    @Autowired
    private INotificationService iNotificationService;

    @Autowired
    @Qualifier("transactionManager")
    private DataSourceTransactionManager transactionManager;

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 生成套件组件
     *
     * @param order 订单
     * @return List<OrderEntry> 生成的组件订单行
     */
    @Override
    public List<OrderEntry> generateSuiteComponents(Order order) {

        Catalogversion catalogversion = new Catalogversion();
        catalogversion.setCatalogName(Constants.CATALOG_VERSION_MARKOR);
        catalogversion.setCatalogversion(Constants.CATALOG_VERSION_ONLINE);
        Long markorOnlineId = iCatalogversionService.selectCatalogversionId(catalogversion);

        List<OrderEntry> orderEntries = iOrderEntryService.selectSuiteEntriesByOrderId(order.getOrderId());
        if (CollectionUtils.isEmpty(orderEntries)) {
            iLogManagerService.logTrace(this.getClass(), "套件组件生成流程节点", order.getOrderId(), "订单[" + order.getOrderId() + "]未发现套件行");
            return Collections.emptyList();
        }

        // 对lineNumber进行非空校验，并从小到大进行排序
        orderEntries = orderEntries.stream()
                .peek(orderEntry -> Assert.notNull(orderEntry.getLineNumber(), "订单行[" + orderEntry.getOrderEntryId() + "]lineNumber不能为空"))
                .sorted((oe1, oe2) -> oe1.getLineNumber().compareTo(oe2.getLineNumber()))
                .collect(Collectors.toList());

        Long nextLineNumber = iOrderEntryService.getNextLineNumber(order);

        List<OrderEntry> generatedEntries = new ArrayList<>();
        for (OrderEntry orderEntry : orderEntries) {
            Long productId = orderEntry.getProductId();
            Product product = iProductService.selectByProductId(productId);
            String customType = product.getCustomType();
            String vCode = orderEntry.getVproductCode();

            Assert.notNull(vCode, "订单行[" + orderEntry.getOrderEntryId() + "]上v码不存在");

            MamVcodeHeader mamVcodeHeader = selectUniqueVcodeHeaderByVCode(vCode);
            Assert.notNull(mamVcodeHeader, "v码[" + vCode + "]在v码头表中不存在");

            Assert.notNull(mamVcodeHeader.getSegment(), "v码[" + vCode + "]在v码头表中的组件拼接不存在");
            String[] vCodeArr = mamVcodeHeader.getSegment().split(SEGMENT_SPLIT_CHAR);
            for (String vCodeEle : vCodeArr) {
                Product lineProduct;
                if (CUSTOM_TYPE_REGULAR.equals(customType)) {
                    // 根据v码查询常规品
                    lineProduct = iProductService.selectUniqueByVCode(vCodeEle, markorOnlineId);
                    Assert.notNull(lineProduct, "v码[" + vCodeEle + "]所对应常规品不存在");
                } else {
                    // 根据平台号查询定制品
                    MamVcodeHeader lineVcodeHeader = selectUniqueVcodeHeaderByVCode(vCodeEle);

                    Assert.notNull(lineVcodeHeader, "v码[" + vCodeEle + "]在v码头表中不存在");
                    String platformCode = lineVcodeHeader.getPlatformCode();
                    Assert.notNull(platformCode, "v码头表中v码[" + vCodeEle + "]所对应平台号不能为空");

                    lineProduct = iProductService.selectCustomByCode(platformCode, markorOnlineId);

                    // 如果定制品不存在，判断是否为床板，若为床板则查询常规品
                    if (lineProduct == null) {
                        if (REQUIRED.equals(lineVcodeHeader.getTypeCode())) {
                            lineProduct = iProductService.selectUniqueByVCode(vCodeEle, markorOnlineId);
                            Assert.notNull(lineProduct, "v码[" + vCodeEle + "]所对应常规品（床板）不存在");
                        } else {
                            throw new RuntimeException("定制品[" + platformCode + "]不存在");
                        }
                    }
                }
                // 生成行信息
                OrderEntry newEntry = new OrderEntry();

                // 按照生成规则取值
                newEntry.setProductId(lineProduct.getProductId());
                newEntry.setVproductCode(vCodeEle);
                newEntry.setParentLine(orderEntry.getOrderEntryId());
                newEntry.setCode(iSequenceGenerateService.getNextOrderEntryCode());
                // 从订单行复制
                newEntry.setOrderId(orderEntry.getOrderId());
                newEntry.setConsignmentId(orderEntry.getConsignmentId());
                newEntry.setLineNumber(nextLineNumber);

                // 获取下一个行的lineNumber
                nextLineNumber = iOrderEntryService.getNextLineNumber(nextLineNumber);

                // 平台订单行号留空
                newEntry.setEscLineNumber(null);

                newEntry.setParentLine(orderEntry.getOrderEntryId());
                newEntry.setQuantity(orderEntry.getQuantity());
                newEntry.setUnit(orderEntry.getUnit());
                newEntry.setIsGift(orderEntry.getIsGift());

                newEntry.setEstimateDeliveryTime(orderEntry.getEstimateDeliveryTime());
                newEntry.setEstimateConTime(orderEntry.getEstimateConTime());

                newEntry.setSuitCode(orderEntry.getSuitCode());
                newEntry.setPin(iSequenceGenerateService.getNextPin());
                newEntry.setPointOfServiceId(orderEntry.getPointOfServiceId());
                newEntry.setShippingType(orderEntry.getShippingType());
                newEntry.setNote(orderEntry.getNote());
                newEntry.setAtpStage(orderEntry.getAtpStage());
                newEntry.setBomApproved(orderEntry.getBomApproved());
                newEntry.setOrderEntryStatus(orderEntry.getOrderEntryStatus());

                // 套件上新增odtype字段，取值源自套件头
                newEntry.setOdtype(orderEntry.getOdtype());

                // 组件行需要计算并占用库存，套件头只需计算交期，无需占用库存
                newEntry.setInvOccupyFlag(Constants.NO);

                // 包装尺寸
                newEntry.setProductPackedSize(getProductPackageSize(vCodeEle));

                // 净尺寸
                newEntry.setProductSize(getProductSize(vCodeEle));

                iOrderEntryService.insertSelective(RequestHelper.newEmptyRequest(), newEntry);
                iLogManagerService.logTrace(this.getClass(), "套件组件生成流程节点", order.getOrderId(), "生成组件行[" + newEntry.getOrderEntryId() + "]");
                generatedEntries.add(newEntry);
            }

        }
        return generatedEntries;
    }

    /**
     * 调用配置器服务获取净尺寸
     *
     * @param vCode 订单行v码
     * @return String
     */
    private String getProductSize(String vCode) {
        List<String> list = iMamVcodeHeaderService.volumeCal(vCode);
        if (!list.isEmpty())
            return list.get(0);
        return null;
    }

    /**
     * 根据v码查询唯一的MamVcodeHeader
     *
     * @param vCode v码
     * @return MamVcodeHeader
     */
    private MamVcodeHeader selectUniqueVcodeHeaderByVCode(String vCode) {
        MamVcodeHeader mamVcodeHeader = new MamVcodeHeader();
        mamVcodeHeader.setVcode(vCode);
        PaginatedList<MamVcodeHeader> vcodeHeaderList = iMamVcodeHeaderService.selectList(mamVcodeHeader, RequestHelper.newEmptyRequest(), 1, Integer.MAX_VALUE);
        if (vcodeHeaderList.getRows().size() != 1) {
            throw new RuntimeException("v码[" + vCode + "]在v码头表中不存在或多于一条");
        }
        return (MamVcodeHeader) vcodeHeaderList.getRows().get(0);
    }


    @Override
    public void generateOrderPrice(Order order) {

        OrderEntry orderEntryParam = new OrderEntry();
        orderEntryParam.setOrderId(order.getOrderId());
        List<OrderEntry> orderEntries = iOrderEntryService.select(orderEntryParam);
        Assert.notEmpty(orderEntries, "订单[ " + order.getOrderId() + " ]未关联订单行");

        // 保存订单行id和订单行之间的映射关系
        Map<Long, OrderEntry> orderEntryMap = new HashMap<>();
        orderEntries.stream()
                .filter(orderEntry -> orderEntry.getInternalPrice() == null)
                .forEach(orderEntry ->
                        orderEntryMap.put(orderEntry.getOrderEntryId(), orderEntry));

        if (orderEntryMap.isEmpty()) {
            iLogManagerService.logTrace(this.getClass(), "采购价格生成流程节点", order.getOrderId(), "订单[" + order.getOrderId() + "]没有需要进行采购价格计算的订单行");

            return;
        }

        iLogManagerService.logTrace(this.getClass(), "采购价格生成流程节点", null, "test--------------------------");

        try {
            Response response = restClient.postString(Constants.HMALL, ORDER_PRICE_CAL_URL,
                    getOrderPriceJSONBody(orderEntryMap), Constants.MINI_TYPE_JSON, null, null);

            iLogManagerService.logTrace(this.getClass(), "采购价格生成流程节点", null, "test res--------------------------" + response);

            if (response.code() == 200) {

                iLogManagerService.logTrace(this.getClass(), "采购价格生成流程节点", null, "test res 200 --------------------------");

                JSONObject respObject = RestClient.responseToJSON(response);
                if (respObject.getBoolean("success")) {

                    iLogManagerService.logTrace(this.getClass(), "采购价格生成流程节点", null, "test res success --------------------------");

                    JSONArray priceRows = respObject.getJSONArray("resp");
                    iLogManagerService.logTrace(this.getClass(), "采购价格生成流程节点", null, "priceRow size:" + priceRows.size());
                    priceRows.stream().forEach(priceObj -> {
                        JSONObject priceRow = (JSONObject) priceObj;
                        OrderEntry orderEntry = orderEntryMap.get(priceRow.getLong("cdkey"));
                        if (priceRow.getBoolean("success")) {
                            orderEntry.setInternalPrice(priceRow.getDouble("totalPrice"));
                            iOrderEntryService.updateByPrimaryKeySelective(RequestHelper.newEmptyRequest(), orderEntry);
                            iLogManagerService.logTrace(this.getClass(), "采购价格生成流程节点", orderEntry.getOrderEntryId(), priceRow.toString() + "," + orderEntry);
                        } else {
                            throw new RuntimeException("订单行[" + orderEntry.getOrderEntryId() + "]采购价格计算失败，" + priceRow.getString("message"));
                        }
                    });
                    OrderEntry entryParam = new OrderEntry();
                    entryParam.setOrderId(order.getOrderId());
                    List<OrderEntry> orderEntryList = iOrderEntryService.select(entryParam);
                    orderEntryList.stream()
                            .forEach(newEntry -> {
                                //增加书面记录
                                HmallSoChangeLog soChangeLog = new HmallSoChangeLog();
                                soChangeLog.setOrderId(newEntry.getOrderId());
                                soChangeLog.setOrderEntryId(newEntry.getOrderEntryId());
                                soChangeLog.setOrderType("1");
                                soChangeLog.setPin(newEntry.getPin());
                                soChangeLog.setProductId(newEntry.getProductId());
                                iOrderService.addSoChangeLog(soChangeLog);
                            });

                } else {
                    throw new RuntimeException("采购价格计算失败, " + respObject.getString("msg"));
                }
            } else {
                throw new RuntimeException("采购价格计算服务访问不通，code[" + response.code() + "]，message[" + response.message() + "]");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    /**
     * 获取采购价格计算的JSON串
     *
     * @param orderEntryMap 订单行信息集合
     * @return String
     */
    private String getOrderPriceJSONBody(Map<Long, OrderEntry> orderEntryMap) {

        JSONArray jsonArray = new JSONArray();
        for (Long orderEntryId : orderEntryMap.keySet()) {

            JSONObject jsonObject = new JSONObject();
            OrderEntry orderEntry = orderEntryMap.get(orderEntryId);

            jsonObject.put("cdkey", orderEntryId);

            if (StringUtils.isBlank(orderEntry.getVproductCode())) {
                // 如果v码不存在传商品编码
                Assert.notNull(orderEntry.getProductId(), "订单行[" + orderEntry.getOrderEntryId() + "]未关联商品");
                Product product = iProductService.selectByProductId(orderEntry.getProductId());
                Assert.notNull(product, "商品[" + orderEntry.getProductId() + "]不存在");
                jsonObject.put("productCode", product.getCode());
            } else {
                // 如果v码存在则传v码
                jsonObject.put("vCode", orderEntry.getVproductCode());
            }

            // 频道
            jsonObject.put("odtype", orderEntry.getOdtype());
            jsonArray.add(jsonObject);
        }

        return jsonArray.toString();
    }

    @Override
    public void setStatus(String status, Order order) {
        order.setOrderStatus(status);
        iOrderService.updateByPrimaryKeySelective(RequestHelper.newEmptyRequest(), order);
    }

    @Override
    public void logError(Class<?> clazz, String programDesc, Long itemId, String returnMessage) {
        iLogManagerService.logError(clazz, programDesc, itemId, returnMessage);
    }

    @Override
    public void logTrace(Class<?> clazz, String programDesc, Long itemId, String returnMessage) {
        iLogManagerService.logTrace(clazz, programDesc, itemId, returnMessage);
    }

    @Override
    public void logError(LogManager logManager) {
        iLogManagerService.logNormal(logManager);
    }

    @Override
    public void bomApprove(Order order) {
        Example example = new Example(OrderEntry.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId", order.getOrderId());
        criteria.andIsNull("consignmentId");
        criteria.andEqualTo("orderEntryStatus", Constants.ORDER_ENTRY_STATUS_NORMAL);
        List<OrderEntry> orderEntries = iOrderEntryService.selectByExample(example);
        if (CollectionUtils.isEmpty(orderEntries)) {
            iLogManagerService.logTrace(this.getClass(), "工艺审核信息推送", order.getOrderId(), "订单[" + order.getOrderId() + "]未发现待审订单行");
            return;
        }

        Integer autoBomApproved = iGlobalVariantService.getNumber(Constants.AUTO_BOM_APPROVED, Integer.class);
        if (autoBomApproved == null) {
            throw new RuntimeException("全局变量AUTO_BOM_APPROVED没有维护");
        }

        for (OrderEntry orderEntry : orderEntries) {
            if (StringUtils.isBlank(orderEntry.getVproductCode()) || isSuiteHeader(orderEntry)) {
                // 没有v码的订单行bom审核自动通过
                orderEntry.setBomApproved(Constants.YES);
                iOrderEntryService.updateByPrimaryKeySelective(RequestHelper.newEmptyRequest(), orderEntry);
                // 保存pin码消息
                iPinService.savePinInfos(Arrays.asList(orderEntry), Constants.EMPLOYEE_SYSTEM, ORDER_BOM_APPROVED, "已进行工艺审核");
                iLogManagerService.logTrace(this.getClass(), "工艺审核信息推送", orderEntry.getOrderEntryId(), "订单行[" + orderEntry.getOrderEntryId() + "]bom审核通过");
            } else {
                OrderEntry orderEntryParam = new OrderEntry();
                orderEntryParam.setVproductCode(orderEntry.getVproductCode());
                orderEntryParam.setBomApproved(Constants.YES);
                List<OrderEntry> orderEntryList = iOrderEntryService.select(orderEntryParam);
                if (CollectionUtils.size(orderEntryList) >= autoBomApproved) {
                    orderEntry.setBomApproved(Constants.YES);
                    iOrderEntryService.updateByPrimaryKeySelective(RequestHelper.newEmptyRequest(), orderEntry);
                    // 保存pin码消息
                    iPinService.savePinInfos(Arrays.asList(orderEntry), Constants.EMPLOYEE_SYSTEM, ORDER_BOM_APPROVED, "已进行工艺审核");
                    iLogManagerService.logTrace(this.getClass(), "工艺审核信息推送", orderEntry.getOrderEntryId(), "订单行[" + orderEntry.getOrderEntryId() + "]bom审核通过");
                } else {
                    // 检查订单行pin码是否为空
                    Assert.notNull(orderEntry.getPin(), "订单行[" + orderEntry.getOrderEntryId() + "]pin码不能为空");

                    // 检查该pin码是否已经推送过配置器
                    MamSoApproveHis mamSoApproveHisParam = new MamSoApproveHis();
                    mamSoApproveHisParam.setPinCode(orderEntry.getPin());

                    PaginatedList<MamSoApproveHis> historyList = iMamSoApproveHisService.queryHistory(RequestHelper.newEmptyRequest(), mamSoApproveHisParam, 1, Integer.MAX_VALUE);
                    // 如果没有推送过则推送配置器
                    if (CollectionUtils.isEmpty(historyList.getRows())) {

                        // bom审核未通过，推送配置器
                        MamSoApproveHis mamSoApproveHis = new MamSoApproveHis();
                        mamSoApproveHis.setOrderNumber(order.getCode());
                        mamSoApproveHis.setPinCode(orderEntry.getPin());
                        mamSoApproveHis.setVcode(orderEntry.getVproductCode());
                        mamSoApproveHis.setApproveStatus(Constants.NO);
                        if (orderEntry.getProductId() != null) {
                            Product product = iProductService.selectByProductId(orderEntry.getProductId());
                            if (product != null) {
                                mamSoApproveHis.setProductCode(product.getCode());
                                mamSoApproveHis.setProductDesc(product.getName());
                            }
                        }
                        iMamSoApproveHisService.insertSelective(RequestHelper.newEmptyRequest(), mamSoApproveHis);
                        iLogManagerService.logTrace(this.getClass(), "工艺审核信息推送", orderEntry.getOrderEntryId(), "工艺审核信息[" + mamSoApproveHis.getOrderNumber() + "]已推送配置器");

                        // 添加bom审核推送配置器通知
                        iNotificationService.addBomApprovedNotice(orderEntry.getPin(), mamSoApproveHis.getHistoryId());
                    }
                }
            }
        }
    }

    @Override
    public void inventoryOccupy(Order order) {

        // 查询待库存占用的订单行，需满足条件：库存占用标志N，订单行状态NORMAL
        OrderEntry orderEntryParam = new OrderEntry();
        orderEntryParam.setOrderId(order.getOrderId());
        orderEntryParam.setInvOccupyFlag(Constants.NO);
        orderEntryParam.setOrderEntryStatus(Constants.ORDER_ENTRY_STATUS_NORMAL);
        List<OrderEntry> orderEntryList = iOrderEntryService.select(orderEntryParam);
        if (CollectionUtils.isEmpty(orderEntryList)) {
            iLogManagerService.logTrace(this.getClass(), SERVICE_DESC, order.getOrderId(), "订单[" + order.getOrderId() + "]没有可以进行订单行库存占用的行");
            return;
        }

        // 建立interfaceId与订单行之间的关系，库存占用成功后根据库存接口返回的interfaceId得到对应的订单行
        Map<String, OrderEntry> interfaceIdMap = new HashMap<>();

        // 筛选套件行和普通订单行（非套件头）
        List<OrderEntry> suitLines = orderEntryList.stream()
                .filter(orderEntry -> !isSuiteHeader(orderEntry))
                .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(suitLines)) {
            // 构造套建行和普通行的库存占用请求数据
            List<ImAtpInface> atpInfaceList = buildInvRequestData(order, suitLines, true, interfaceIdMap);
            //iLogManagerService.logTrace(this.getClass(), SERVICE_DESC, order.getOrderId(), "订单[" + order.getOrderId() + "]套件行库存占用请求报文，" + suitLinesArr.toString());

            // 套件行需要独立事务控制（PROPAGATION_REQUIRES_NEW），避免重复占用库存
            DefaultTransactionDefinition def = new DefaultTransactionDefinition(); // TODO: 为何不在方法上配置 @Transactional ?
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            TransactionStatus status = transactionManager.getTransaction(def);

            boolean suitLinesFlag = false;
            try {
                List<ImAtpInfaceResponse> atpInfaceResponses = atpInfaceService.importAtpData(atpInfaceList);
                invResponseHandle(atpInfaceResponses, order, interfaceIdMap);
                suitLinesFlag = true;
            } catch (Exception e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            } finally {
                // 根据成功标志，决定事务提交或回滚
                if (suitLinesFlag) {
                    transactionManager.commit(status);
                } else {
                    transactionManager.rollback(status);
                }
            }
        }

        // 筛选套件头，对套件头进行交期计算
        List<OrderEntry> suitHeads = orderEntryList.stream()
                .filter(orderEntry -> isSuiteHeader(orderEntry))
                .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(suitHeads)) {

            // 构造套建头的库存占用请求数据
            List<ImAtpInface> atpInfaceList = buildInvRequestData(order, suitHeads, false, interfaceIdMap);
            //iLogManagerService.logTrace(this.getClass(), SERVICE_DESC, order.getOrderId(), "订单[" + order.getOrderId() + "]套件头库存占用请求报文，" + jsonArray.toString());

            try {
                List<ImAtpInfaceResponse> atpInfaceResponses = atpInfaceService.importAtpData(atpInfaceList);
                invResponseHandle(/*response*/atpInfaceResponses, order, interfaceIdMap);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }

    }

    /**
     * 判断订单行是否是套件头
     *
     * @param orderEntry 订单行
     * @return boolean
     */
    private boolean isSuiteHeader(OrderEntry orderEntry) {
        if (orderEntry.getParentLine() != null) {
            return false;
        }
        Assert.notNull(orderEntry.getProductId(), "订单行[" + orderEntry.getOrderId() + "]未关联商品");
        Product product = iProductService.selectByProductId(orderEntry.getProductId());
        Assert.notNull(product, "商品[" + orderEntry.getProductId() + "]不存在");
        return Constants.YES.equals(product.getIsSuit());
    }

    /**
     * 库存占用接口返回处理
     *
     * @param atpInfaceResponses atp接口调用
     * @param order              订单
     * @param interfaceIdMap     interfaceId映射
     * @throws IOException
     * @throws ParseException
     */
    private void invResponseHandle(/*Response response,*/List<ImAtpInfaceResponse> atpInfaceResponses, Order order, Map<String, OrderEntry> interfaceIdMap)
            throws IOException, ParseException {

        for (ImAtpInfaceResponse response : atpInfaceResponses) {
            if (Constants.NO.equals(response.getProcessFlag())) {
                throw new RuntimeException("订单行库存占用失败，" + response.getErrorMassage());
            } else {
                OrderEntry orderEntry = interfaceIdMap.get(response.getInterfaceId());
                invOrderEntryHandle(orderEntry, response, order);
            }
        }
    }

    /**
     * 订单行库存占用成功后对订单行进行后续处理
     *
     * @param orderEntry        订单行
     * @param atpInfaceResponse atp导入结果信息
     * @param order             订单
     * @throws ParseException
     */
    private void invOrderEntryHandle(OrderEntry orderEntry, /*JSONObject jsonObject,*/ImAtpInfaceResponse atpInfaceResponse, Order order) throws ParseException {

        orderEntry.setAtpStage(atpInfaceResponse.getAtpStage());
        orderEntry.setInvOccupyFlag(Constants.YES);
        orderEntry.setAtpCalculateTime(DateUtils.parseDate(atpInfaceResponse.getAtp(), DATE_FORMAT));
        orderEntry.setOriRequirementTime(orderEntry.getEstimateDeliveryTime());

        // 天猫订单回写服务点
        if (isTMALLOrder(order)) {
            orderEntry.setPointOfServiceId(extractIdFromFullPosName(atpInfaceResponse.getInventorySourcing()));
        }

        iOrderEntryService.updateByPrimaryKeySelective(RequestHelper.newEmptyRequest(), orderEntry);
    }

    /**
     * 构造库存占用的请求数据
     *
     * @param order          订单头
     * @param orderEntries   需要进行库存占用订单行
     * @param occupy         是否实际占用库存
     * @param interfaceIdMap interfaceId和订单行之间的映射关系
     * @return result
     */
    private /*JSONArray*/List<ImAtpInface> buildInvRequestData(Order order, List<OrderEntry> orderEntries, boolean occupy, Map<String, OrderEntry> interfaceIdMap) {
        List<ImAtpInface> result = new ArrayList<>();
        for (OrderEntry orderEntry : orderEntries) {
            ImAtpInface atpInface = new ImAtpInface();
            atpInface.setvMatnr(StringUtils.isBlank(orderEntry.getVproductCode()) ? "-1" : orderEntry.getVproductCode());

            Product product = iProductService.selectByProductId(orderEntry.getProductId());
            Assert.notNull(product, "订单行[" + orderEntry.getOrderEntryId() + "]商品[" + orderEntry.getProductId() + "]不存在");
            atpInface.setMatnr(product.getCode());

            Assert.notNull(orderEntry.getQuantity(), "订单行[" + orderEntry.getOrderEntryId() + "]数量不能为空");
            atpInface.setQuantity(orderEntry.getQuantity().doubleValue());

            Assert.notNull(order.getReceiverCity(), "订单行[" + orderEntry.getOrderEntryId() + "]收货人市不能为空");
            Assert.notNull(order.getReceiverDistrict(), "订单行[" + orderEntry.getOrderEntryId() + "]收货人区不能为空");
            atpInface.setDeliveryAddress(order.getReceiverCity() + order.getReceiverDistrict());

            String interfaceId = iSequenceGenerateService.getNextInterfaceId();
            atpInface.setInterfaceId(interfaceId);

            interfaceIdMap.put(interfaceId, orderEntry);

            Assert.notNull(orderEntry.getPin(), "订单行[" + orderEntry.getOrderEntryId() + "]pin码不能为空");
            atpInface.setPinCode(orderEntry.getPin());
            atpInface.setOccupy(occupy ? Constants.YES : Constants.NO);

            result.add(atpInface);
        }
        return result;
    }

    @Override
    public Consignment generateConsignment(Order order) {

        // 订单行上面挂配货单行
        List<OrderEntry> orderEntries = iOrderEntryService.selectByOrderId(order.getOrderId());
        if (CollectionUtils.isEmpty(orderEntries)) {
            throw new RuntimeException("订单[" + order.getOrderId() + "]未关联订单行");
        }

        // 筛选待生成发货单的订单行，需满足条件：发货单id为null，订单行状态为NORMAL
        List<OrderEntry> newEntryList = orderEntries.stream()
                .filter(orderEntry -> orderEntry.getConsignmentId() == null && Constants.ORDER_ENTRY_STATUS_NORMAL.equals(orderEntry.getOrderEntryStatus()))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(newEntryList)) {
            iLogManagerService.logTrace(this.getClass(), "订单生成发货单流程节点", order.getOrderId(), "订单[" + order.getOrderId() + "]没有待生成发货单的行");
            return null;
        }

        // 初始化一个新的发货单，设置默认值
        Consignment consignment = initGetConsignment();

        consignment.setReceiverName(order.getReceiverName());
        consignment.setReceiverCountry(order.getReceiverCountry());
        consignment.setOrderId(order.getOrderId());
        consignment.setReceiverState(order.getReceiverState());
        consignment.setReceiverCity(order.getReceiverCity());
        consignment.setReceiverDistrict(order.getReceiverDistrict());
        consignment.setReceiverAddress(order.getReceiverAddress());
        consignment.setReceiverZip(order.getReceiverZip());
        consignment.setReceiverMobile(order.getReceiverMobile());
        consignment.setReceiverPhone(order.getReceiverPhone());
        consignment.setEstimateDeliveryTime(order.getEstimateDeliveryTime());
        consignment.setSplitAllowed(Constants.YES.equals(order.getTotalcon()) ? Constants.NO : Constants.YES);

        // 设置预计交货时间和预计发货时间
        consignment.setEstimateDeliveryTime(order.getEstimateDeliveryTime());

        consignment.setReceiverTown(null);
        consignment.setNote(null);
        iConsignmentService.insertSelective(RequestHelper.newEmptyRequest(), consignment);

        // 修改订单状态
        order.setOrderStatus(Constants.CON_STATUS_PROCESSING);
        iOrderService.updateByPrimaryKeySelective(RequestHelper.newEmptyRequest(), order);

        // 订单行上面挂配货单行
        newEntryList.stream().forEach(orderEntry -> {
            orderEntry.setConsignmentId(consignment.getConsignmentId());
            iOrderEntryService.updateByPrimaryKeySelective(RequestHelper.newEmptyRequest(), orderEntry);
        });

        // 记录pin码信息
        iPinService.savePinInfos(newEntryList, Constants.EMPLOYEE_SYSTEM, ORDER_TRIGGER_GOODS, "发货单已生成");

        iLogManagerService.logTrace(this.getClass(), "订单生成发货单流程节点", order.getOrderId(), "新的发货单[" + consignment.getConsignmentId() + "]已经生成");

        return consignment;
    }

    /**
     * 初始化一个Consignment对象，设置默认值
     *
     * @return Consignment
     */
    private Consignment initGetConsignment() {
        Consignment consignment = new Consignment();
        consignment.setCode(iSequenceGenerateService.getNextConsignmentCode());
        consignment.setStatus(Constants.CON_STATUS_NEW_CREATE);
        consignment.setShippingType(Constants.LOGISTICS);
        consignment.setBrand(DEFAULT_BRAND);
        consignment.setLogisticsCompanies(null);
        consignment.setLogisticsNumber(null);

        PointOfServiceDto dto = iPointOfServiceExternalService.selectByCode(DEFAULT_PS_CODE);
        if (dto != null) {
            consignment.setPointOfServiceId(dto.getPointOfServiceId());
        } else {
            throw new RuntimeException("没有找到对应的服务点信息");
        }

        consignment.setShippingDate(null);
        return consignment;
    }

    @Override
    public void outsideProcurement(Order order) {
        Example example = new Example(OrderEntry.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId", order.getOrderId());
        List<OrderEntry> orderEntries = iOrderEntryService.selectByExample(example);
        if (CollectionUtils.isEmpty(orderEntries)) {
            throw new RuntimeException("订单[" + order.getOrderId() + "]没有关联订单行");
        }

        // 查询自制工厂
        List<CodeValue> homemadeFactories = iCodeService.selectCodeValuesByCodeName(RequestHelper.newEmptyRequest(), Constants.HMALL_HOMEMADE_FACTORY);
        Assert.notEmpty(homemadeFactories, "自制工厂块码[" + Constants.HMALL_HOMEMADE_FACTORY + "]没有维护");

        List<String> homemadeFactoriesCode = homemadeFactories.stream().map(CodeValue::getValue).collect(Collectors.toList());

        List<OrderEntry> filterEntries = new ArrayList<>();
        for (OrderEntry orderEntry : orderEntries) {
            if (orderEntry.getProductId() == null) {
                throw new RuntimeException("订单行[" + orderEntry.getOrderEntryId() + "]没有关联商品");
            }
            Product product = iProductService.selectByProductId(orderEntry.getProductId());
            if (product == null) {
                throw new RuntimeException("订单行[" + orderEntry.getOrderEntryId() + "]关联商品[" + orderEntry.getProductId() + "]不存在");
            }
            if (!CUSTOM_TYPE_REGULAR.equals(product.getCustomType())
                    && (StringUtils.isNotBlank(product.getSupplier()) && !homemadeFactoriesCode.contains(product.getSupplier()))) {
                filterEntries.add(orderEntry);
            }
        }
        if (CollectionUtils.isEmpty(filterEntries)) {
            iLogManagerService.logTrace(this.getClass(), "外采皮沙发定制信息生成", order.getOrderId(), "订单[" + order.getOrderId() + "]没要满足需要外采生成的订单行");
            return;
        }

        for (OrderEntry orderEntry : filterEntries) {
            String vCode = orderEntry.getVproductCode();
            if (StringUtils.isBlank(vCode)) {
                throw new RuntimeException("订单行[" + orderEntry.getOrderEntryId() + "]v码不能为空");
            }
            MamVcodeHeader mamVcodeHeader = selectUniqueVcodeHeaderByVCode(vCode);
            if (mamVcodeHeader == null) {
                throw new RuntimeException("v码[" + vCode + "]在v码头表中不存在");
            }
            MamVcodeLines mamVcodeLinesParam = new MamVcodeLines();
            mamVcodeLinesParam.setHeaderId(mamVcodeHeader.getHeaderId());
            PaginatedList<MamVcodeLines> mamVcodeLines = iMamVcodeLinesService.selectList(mamVcodeLinesParam, RequestHelper.newEmptyRequest(), 1, Integer.MAX_VALUE);
            List<MamVcodeLines> rows = mamVcodeLines.getRows();
            if (CollectionUtils.isNotEmpty(rows)) {
                String customerMsg = rows.stream().filter(mamVcodeLine -> StringUtils.isNotBlank(mamVcodeLine.getPotx1())).map(
                        mamVcodeLine -> mamVcodeLine.getIdnrk() + COLUMN_SPLIT_CHAR
                                + mamVcodeLine.getPotx1() + COLUMN_SPLIT_CHAR + mamVcodeLine.getPotx2())
                        .collect(Collectors.joining(RECORD_SPLIT_CHAR));
                orderEntry.setCustomerMsg(customerMsg);
                iOrderEntryService.updateByPrimaryKeySelective(RequestHelper.newEmptyRequest(), orderEntry);
            } else {
                iLogManagerService.logTrace(this.getClass(), "外采皮沙发定制信息生成", null, "v码[" + vCode + "]不存在v码行");
            }
        }
    }

    @Override
    public String getProductPackageSize(String vCode) {
        List<String> list = iMamVcodeHeaderService.outPackVolumeCal(vCode);
        if (!list.isEmpty())
            return list.get(0);
        return null;
    }

    @Override
    public Long extractIdFromFullPosName(String fullPosName) {
        return iPointOfServiceExternalService.getPosIdOfPosFullName(fullPosName);
    }


    @Override
    public boolean isTMALLOrder(Order order) {
        return Constants.WEBSITE_TMALL.equals(order.getWebsiteId());
    }

    @Override
    public void calSizeForRegular(Order order) {

        List<OrderEntry> regularEntries = iOrderEntryService.selectRegularEntries(order.getOrderId());
        if (CollectionUtils.isNotEmpty(regularEntries)) {
            for (OrderEntry orderEntry : regularEntries) {
                String packedSize = getProductPackageSize(orderEntry.getVproductCode());
                String productSize = getProductSize(orderEntry.getVproductCode());
                orderEntry.setProductPackedSize(packedSize);
                orderEntry.setProductSize(productSize);
                iOrderEntryService.updateByPrimaryKeySelective(RequestHelper.newEmptyRequest(), orderEntry);
            }
        }

    }
}
