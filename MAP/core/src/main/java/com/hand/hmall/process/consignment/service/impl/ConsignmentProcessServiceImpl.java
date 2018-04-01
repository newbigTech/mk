package com.hand.hmall.process.consignment.service.impl;

import com.hand.hap.core.impl.RequestHelper;
import com.hand.hmall.as.dto.AsReturn;
import com.hand.hmall.as.service.IAsReturnService;
import com.hand.hmall.common.service.ISequenceGenerateService;
import com.hand.hmall.hr.dto.MarkorEmployee;
import com.hand.hmall.hr.service.IMarkorEmployeeService;
import com.hand.hmall.log.dto.LogManager;
import com.hand.hmall.log.service.ILogManagerService;
import com.hand.hmall.mst.dto.Brand;
import com.hand.hmall.mst.dto.Product;
import com.hand.hmall.mst.dto.RegionPointOfServiceMapping;
import com.hand.hmall.mst.service.IBrandService;
import com.hand.hmall.mst.service.IProductService;
import com.hand.hmall.mst.service.IRegionPointOfServiceMappingService;
import com.hand.hmall.om.dto.AbnormalType;
import com.hand.hmall.om.dto.Consignment;
import com.hand.hmall.om.dto.Order;
import com.hand.hmall.om.dto.OrderEntry;
import com.hand.hmall.om.service.IAbnormalTypeService;
import com.hand.hmall.om.service.IConsignmentService;
import com.hand.hmall.om.service.IOrderEntryService;
import com.hand.hmall.om.service.IOrderService;
import com.hand.hmall.pin.service.IPinService;
import com.hand.hmall.process.consignment.pojo.SplitHeader;
import com.hand.hmall.process.consignment.pojo.SplitRow;
import com.hand.hmall.process.consignment.service.IConsignmentProcessService;
import com.hand.hmall.util.Constants;
import com.markor.map.external.pointservice.dto.PointOfServiceDto;
import com.markor.map.external.pointservice.service.IPointOfServiceExternalService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 马君
 * @version 0.1
 * @name ConsignmentProcessServiceImpl
 * @description 发货单流程Service
 * @date 2017/6/26 11:30
 */
@Service(value = "iConsignmentProcessService")
@Transactional(rollbackFor = {Exception.class, RuntimeException.class})
public class ConsignmentProcessServiceImpl implements IConsignmentProcessService {

    private static final String ORDER_CS_APPROVE = "MAP0300"; // 订单客服审核
    private static final String ABNORMAL_REASON_SPLIT_CHAR = ",";
    private static final String SPLIT_GROUP_CHAR = "@";

    @Autowired
    private IConsignmentService iConsignmentService;

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private IOrderEntryService iOrderEntryService;

    @Autowired
    private IAbnormalTypeService iAbnormalTypeService;

    @Autowired
    private IBrandService iBrandService;

    @Autowired
    private IProductService iProductService;

    @Autowired
    private ISequenceGenerateService iSequenceGenerateService;

    @Autowired
    private ILogManagerService iLogManagerService;

    @Autowired
    private IMarkorEmployeeService iMarkorEmployeeService;

    @Autowired
    private IPinService iPinService;

    @Autowired
    private IAsReturnService iAsReturnService;

    @Autowired
    private IRegionPointOfServiceMappingService regionPointOfServiceMappingService;

    @Autowired
    private IPointOfServiceExternalService pointOfServiceService;

    @Override
    @Transactional
    public boolean abnormalJudgment(Consignment consignment, MarkorEmployee approvedBy) {
        Assert.notNull(consignment, "发货单不能为空");
        Assert.notNull(approvedBy, "审核人不能为空");

        Integer conApprTimes = consignment.getApprovedTimes() == null ? 0 : consignment.getApprovedTimes();

        List<AbnormalType> abnormalTypes = iAbnormalTypeService.selectAll(RequestHelper.newEmptyRequest());
        Assert.notEmpty(abnormalTypes, "没有可用的异常判定类型");

        // 记录异常原因，异常原因可以为空，比如初审异常的异常原因就为空
        List<String> abnormalReasons = new ArrayList<>();
        for (AbnormalType abnormalType : abnormalTypes) {
            if (Constants.YES.equals(abnormalType.getActive()) && conApprTimes < abnormalType.getApprovedtimes()) {
                switch (abnormalType.getAbnormalType()) {
                    case Constants.ABNORMAL_FIRST_TIME:
                        if (isFirstTimeAbnormal(consignment)) {
                            abnormalReasons.add(abnormalType.getAbnormalreason());
                        }
                        break;
                    case Constants.ABNORMAL_SWAP_ORDER:
                        if (isSwapOrderAbnormal(consignment)) {
                            abnormalReasons.add(abnormalType.getAbnormalreason());
                        }
                        break;
                    case Constants.ABNORMAL_BUYER_MEMO:
                        if (isBuyerMemoAbnormal(consignment)) {
                            abnormalReasons.add(abnormalType.getAbnormalreason());
                        }
                        break;
                    case Constants.ABNORMAL_ORDER_QUANTITY:
                        if (isOrderQuantityAbnormal(consignment, abnormalType)) {
                            abnormalReasons.add(abnormalType.getAbnormalreason());
                        }
                        break;
                    case Constants.ABNORMAL_EARLIEST_DELIVERY_TIME:
                        if (isEarliestDeliveryTimeAbnormal(consignment)) {
                            abnormalReasons.add(abnormalType.getAbnormalreason());
                        }
                        break;
                    case Constants.ABNORMAL_ESTIMATE_DELIVERY_TIME:
                        if (isEstimateDeliveryTimeAbnormal(consignment)) {
                            abnormalReasons.add(abnormalType.getAbnormalreason());
                        }
                        break;
                }
            }
        }

        boolean abnormal;
        if (CollectionUtils.isNotEmpty(abnormalReasons)) {
            consignment.setAbnormalReason(abnormalReasons.stream().filter(reason -> reason != null).collect(Collectors.joining(ABNORMAL_REASON_SPLIT_CHAR)));
            consignment.setStatus(Constants.CONSIGNMENT_STATUS_ABNORMAL);
            consignment.setApprovedTimes(conApprTimes + 1);
            abnormal = true;
        } else {
            consignment.setApprovedBy(approvedBy.getEmployeeId());
            consignment.setApprovedDate(new Date());
            consignment.setApprovedTimes(conApprTimes + 1);
            consignment.setCsApproved(Constants.YES);

            OrderEntry orderEntryParams = new OrderEntry();
            orderEntryParams.setConsignmentId(consignment.getConsignmentId());
            orderEntryParams.setOrderEntryStatus(Constants.ORDER_ENTRY_STATUS_NORMAL);
            List<OrderEntry> orderEntries = iOrderEntryService.select(orderEntryParams);
            Assert.notEmpty(orderEntries, "发货单[" + consignment.getConsignmentId() + "]为关联订单行");

            // 保存PIN码信息
            iPinService.savePinInfos(orderEntries, approvedBy.getEmployeeCode(), ORDER_CS_APPROVE, "客服已审核");

            if (orderEntries.stream().allMatch(orderEntry -> Constants.YES.equals(orderEntry.getBomApproved()))) {
                consignment.setStatus(Constants.CON_STATUS_WAIT_FOR_DELIVERY);
                // modified by majun @2017/9/22  consignment.setAbnormalReason(null); 无法更新此字段
                consignment.setAbnormalReason("");
            } else {
                iLogManagerService.logTrace(this.getClass(), "异常判定流程节点", consignment.getConsignmentId(),
                        "发货单[" + consignment.getConsignmentId() + "]异常判定通过，但自动BOM审失败，将置为异常");
                consignment.setStatus(Constants.CONSIGNMENT_STATUS_ABNORMAL);
                consignment.setAbnormalReason("等待BOM审");
            }

            abnormal = false;
        }

        iConsignmentService.updateByPrimaryKeySelective(RequestHelper.newEmptyRequest(), consignment);
        return abnormal;
    }

    /**
     * 判断发货单是否为初次审核异常
     *
     * @param consignment 发货单
     * @return boolean
     */
    private boolean isFirstTimeAbnormal(Consignment consignment) {
        Order orderParams = new Order();
        orderParams.setOrderId(consignment.getOrderId());
        Order order = iOrderService.selectByPrimaryKey(RequestHelper.newEmptyRequest(), orderParams);
        Assert.notNull(order, "发货单[" + consignment.getConsignmentId() + "]关联的订单[" + consignment.getOrderId() + "]不存在");
        return Constants.WEBSITE_CODE_ZEST.equals(order.getWebsiteId());
    }

    /**
     * 换发单关联退货单未入库
     *
     * @param consignment 发货单
     * @return boolean
     */
    private boolean isSwapOrderAbnormal(Consignment consignment) {
        Order orderParams = new Order();
        orderParams.setOrderId(consignment.getOrderId());
        Order order = iOrderService.selectByPrimaryKey(RequestHelper.newEmptyRequest(), orderParams);
        Assert.notNull(order, "发货单[" + consignment.getConsignmentId() + "]关联的订单[" + consignment.getOrderId() + "]不存在");
        if (Constants.ORDER_TYPE_SWAP.equals(order.getOrderType())) {
            AsReturn asReturnParam = new AsReturn();
            asReturnParam.setSwapOrderId(consignment.getOrderId());
            List<AsReturn> asReturnList = iAsReturnService.select(RequestHelper.newEmptyRequest(), asReturnParam, 1, Integer.MAX_VALUE);
            if (CollectionUtils.isNotEmpty(asReturnList)) {
                AsReturn asReturn = asReturnList.get(0);
                if (!Constants.FINI.equals(asReturn.getStatus())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断发货单是否满足买家留言异常
     *
     * @param consignment 发货单
     * @return boolean
     */
    private boolean isBuyerMemoAbnormal(Consignment consignment) {
        Order orderParams = new Order();
        orderParams.setOrderId(consignment.getOrderId());
        Order order = iOrderService.selectByPrimaryKey(RequestHelper.newEmptyRequest(), orderParams);
        Assert.notNull(order, "发货单[" + consignment.getConsignmentId() + "]关联的订单[" + consignment.getOrderId() + "]不存在");
        return StringUtils.isNotBlank(order.getBuyerMemo());
    }

    /**
     * 判断发货单是否满足订单数量异常
     *
     * @param consignment  发货单
     * @param abnormalType 异常类型
     * @return boolean
     */
    private boolean isOrderQuantityAbnormal(Consignment consignment, AbnormalType abnormalType) {

        // 检查发货单所关联的订单是否存在
        Order orderParams = new Order();
        orderParams.setOrderId(consignment.getOrderId());
        Order order = iOrderService.selectByPrimaryKey(RequestHelper.newEmptyRequest(), orderParams);
        Assert.notNull(order, "发货单[" + consignment.getConsignmentId() + "]关联的订单[" + consignment.getOrderId() + "]不存在");

        // 虚拟订单不校验订购数量异常
        if (Constants.YES.equals(order.getIsIo())) {
            return false;
        }

        // 查询订单下面所有订单行
        OrderEntry orderEntryParams = new OrderEntry();
        orderEntryParams.setOrderId(order.getOrderId());
        List<OrderEntry> orderEntries = iOrderEntryService.select(orderEntryParams);
        Assert.notEmpty(orderEntries, "订单[" + order.getOrderId() + "]未关联订单行");

        // 订单数量只考虑套件头和非套件，且订单行状态为NORMAL
        List<OrderEntry> orderEntryList = orderEntries.stream()
                .filter(orderEntry -> orderEntry.getParentLine() == null && Constants.ORDER_ENTRY_STATUS_NORMAL.equals(orderEntry.getOrderEntryStatus()))
                .collect(Collectors.toList());

        Assert.notEmpty(orderEntryList, "订单[" + order.getOrderId() + "]不存在状态为NORMAL的套件头或非套件行");

        // 取出订购数量异常预设值
        int defaultValue = Integer.parseInt(abnormalType.getParameter());

        // 统计订单的总数量
        Integer orderQuantity = orderEntryList.stream()
                .filter(orderEntry -> orderEntry.getQuantity() != null)
                .collect(Collectors.summingInt(OrderEntry::getQuantity));

        // 订单数量超过预设值则为异常
        return orderQuantity > defaultValue;
    }

    /**
     * 判断发货单是否满足最早可用交期异常
     *
     * @param consignment 发货单
     * @return boolean
     */
    private boolean isEarliestDeliveryTimeAbnormal(Consignment consignment) {
        // 查询发货单下所有订单行
        OrderEntry orderEntryParams = new OrderEntry();
        orderEntryParams.setConsignmentId(consignment.getConsignmentId());
        List<OrderEntry> orderEntries = iOrderEntryService.select(orderEntryParams);

        Assert.notEmpty(orderEntries, "发货单[" + consignment.getConsignmentId() + "]未关联订单行");

        // 若存在一个状态为NORMAL且ATP答复交期晚于预计交货时间，则为异常
        return orderEntries.stream()
                .filter(orderEntry -> Constants.ORDER_ENTRY_STATUS_NORMAL.equals(orderEntry.getOrderEntryStatus()))
                .peek(orderEntry -> {
                    Assert.notNull(orderEntry.getAtpCalculateTime(), "订单行[" + orderEntry.getOrderEntryId() + "]ATP答复最早交期不能为空");
                    Assert.notNull(orderEntry.getEstimateDeliveryTime(), "订单行[" + orderEntry.getOrderEntryId() + "]预计交货时间不能为空");
                })
                .collect(Collectors.toList()).stream()
                .anyMatch(orderEntry -> orderEntry.getAtpCalculateTime().after(orderEntry.getEstimateDeliveryTime()));
    }

    /**
     * 判断发货单是否满足预计交货时间异常
     *
     * @param consignment 发货单
     * @return boolean
     */
    private boolean isEstimateDeliveryTimeAbnormal(Consignment consignment) {

        // 查询发货单下所有订单行
        OrderEntry orderEntryParams = new OrderEntry();
        orderEntryParams.setConsignmentId(consignment.getConsignmentId());
        List<OrderEntry> orderEntries = iOrderEntryService.select(orderEntryParams);
        Assert.notEmpty(orderEntries, "发货单[" + consignment.getConsignmentId() + "]未关联订单行");

        // 若存在一个状态为NORMAL且预计交货时间不等于客户原始需求日期，则为异常
        return orderEntries.stream()
                .filter(orderEntry -> Constants.ORDER_ENTRY_STATUS_NORMAL.equals(orderEntry.getOrderEntryStatus()))
                .peek(orderEntry -> {
                    Assert.notNull(orderEntry.getEstimateDeliveryTime(), "订单行[" + orderEntry.getOrderEntryId() + "]预计交货时间不能为空");
                    Assert.notNull(orderEntry.getOriRequirementTime(), "订单行[" + orderEntry.getOrderEntryId() + "]客户原始需求日期不能为空");
                }).collect(Collectors.toList()).stream()
                .anyMatch(orderEntry -> !orderEntry.getEstimateDeliveryTime().equals(orderEntry.getOriRequirementTime()));
    }

    @Override
    @Transactional
    public void selectCarrier(Consignment consignment) {
        // todo 逻辑待修改
        /*if (consignment.getLogisticsCompanies() == null) {
            Logisticsco logisticsCoParams = new Logisticsco();
            // 默认选择日日顺
            logisticsCoParams.setCode("RRS");
            Logisticsco logisticsCo = iLogisticscoService.selectOne(logisticsCoParams);
            if (logisticsCo == null) {
                throw new RuntimeException("快递公司RRS不存在");
            }
            consignment.setLogisticsCompanies(logisticsCo.getLogisticscoId());
        }*/
    }

    @Override
    public List<Consignment> splitConsignment(Consignment sourceConsignment, String splitReason) {

        OrderEntry orderEntryParams = new OrderEntry();
        orderEntryParams.setConsignmentId(sourceConsignment.getConsignmentId());
        List<OrderEntry> orderEntryList = iOrderEntryService.select(orderEntryParams);

        Assert.notEmpty(orderEntryList, "发货单[" + sourceConsignment.getConsignmentId() + "]未关联订单行");

        if (CollectionUtils.size(orderEntryList) == 1) {
            iLogManagerService.logTrace(this.getClass(), "发货单拆分流程节点", sourceConsignment.getConsignmentId(),
                    "发货单[" + sourceConsignment.getConsignmentId() + "]只有一个订单行，无需拆单");

            // 回写发运方式、品牌、服务点到原发货单
            writeBackConsignment(sourceConsignment, orderEntryList.get(0));
            iConsignmentService.updateByPrimaryKeySelective(RequestHelper.newEmptyRequest(), sourceConsignment);
            return Collections.emptyList();
        }

        // 检查是否满足拆单条件
        for (OrderEntry orderEntry : orderEntryList) {
            Assert.notNull(orderEntry.getProductId(), "订单行[" + orderEntry.getOrderEntryId() + "]不满足拆单条件，商品id不能为空)");
            Assert.notNull(orderEntry.getPointOfServiceId(), "订单行[" + orderEntry.getOrderEntryId() + "]不满足拆单条件，服务点id不能为空)");
            Assert.notNull(orderEntry.getShippingType(), "订单行[" + orderEntry.getOrderEntryId() + "]不满足拆单条件，发运方式不能为空)");
        }

        // 对订单行按照发运方式、品牌、服务点进行分组
        Map<String, List<OrderEntry>> orderEntryGroups = orderEntryList.stream().collect(Collectors.groupingBy(orderEntry -> joinGroupStr(orderEntry)));

        if (CollectionUtils.size(orderEntryGroups) == 1) {
            iLogManagerService.logTrace(this.getClass(), "发货单拆分流程节点", sourceConsignment.getConsignmentId(),
                    "发货单[" + sourceConsignment.getConsignmentId() + "]下订单行发运方式、品牌、服务点一致，无需拆单");

            // 回写发运方式、品牌、服务点到原发货单
            writeBackConsignment(sourceConsignment, orderEntryList.get(0));
            iConsignmentService.updateByPrimaryKeySelective(RequestHelper.newEmptyRequest(), sourceConsignment);
            return Collections.emptyList();
        }

        // 组装拆分参数
        SplitHeader splitHeader = new SplitHeader();
        splitHeader.setConsignment(sourceConsignment);
        splitHeader.setSplitReason(splitReason);

        List<SplitRow> splitRows = new ArrayList<>();
        splitHeader.setSplitRows(splitRows);
        for (List<OrderEntry> orderEntrieGroup : orderEntryGroups.values()) {
            SplitRow splitRow = new SplitRow();
            splitRow.setOrderEntries(orderEntrieGroup);
            splitRow.setStatus(Constants.CONSIGNMENT_STATUS_ABNORMAL);
            splitRows.add(splitRow);
        }

        return splitConsignment(splitHeader);
    }

    /**
     * 发货单无需拆单时，将发运方式、品牌、服务点回写到头
     *
     * @param consignment 发货单头
     * @param orderEntry  头下订单行
     */
    private void writeBackConsignment(Consignment consignment, OrderEntry orderEntry) {
        consignment.setShippingType(orderEntry.getShippingType());
        consignment.setBrand(getBrandCodeOfProduct(orderEntry.getProductId()));
        if (Constants.ORDER_TO_RETAIL_SHIPPINGTYPE_LOGISTICS.equals(orderEntry.getShippingType())) {
            RegionPointOfServiceMapping mapping = regionPointOfServiceMappingService.findByRegionCode(consignment.getReceiverDistrict());
            if (mapping != null) {
                PointOfServiceDto pos = pointOfServiceService.selectByCode(mapping.getPointOfService());
                consignment.setPointOfServiceId((pos != null && pos.getPointOfServiceId() != null) ? pos.getPointOfServiceId() : orderEntry.getPointOfServiceId());
            } else {
                consignment.setPointOfServiceId(orderEntry.getPointOfServiceId());
            }
        } else {
            consignment.setPointOfServiceId(orderEntry.getPointOfServiceId());
        }
    }

    /**
     * 拼接发运方式、品牌、服务点
     *
     * @param orderEntry 订单行
     * @return String
     */
    private String joinGroupStr(OrderEntry orderEntry) {
        return orderEntry.getShippingType() + SPLIT_GROUP_CHAR
                + getBrandCodeOfProduct(orderEntry.getProductId()) + SPLIT_GROUP_CHAR
                + orderEntry.getPointOfServiceId();
    }

    /**
     * 根据商品获取品牌
     *
     * @param productId 商品id
     * @return String
     */
    private String getBrandCodeOfProduct(Long productId) {
        Product product = new Product();
        product.setProductId(productId);
        product = iProductService.selectByProductId(productId);
        Assert.notNull(product, "商品[" + productId + "]不存在");
        Assert.notNull(product.getBrandId(), "商品[" + productId + "]未关联品牌");
        Brand brand = new Brand();
        brand.setBrandId(product.getBrandId());
        brand = iBrandService.selectByPrimaryKey(RequestHelper.newEmptyRequest(), brand);
        Assert.notNull(brand, "品牌[" + product.getBrandId() + "]不存在");
        return brand.getCode();
    }

    @Override
    public List<Consignment> splitConsignment(SplitHeader splitHeader) {
        Consignment sourceConsignment = splitHeader.getConsignment();
        String splitReason = splitHeader.getSplitReason();
        List<Consignment> splitConsingmnetList = new ArrayList<>();

        // 如果订单行分组只有一组，则不需要拆单
        if (CollectionUtils.size(splitHeader.getSplitRows()) <= 1) {
            return Collections.emptyList();
        }

        for (SplitRow splitRow : splitHeader.getSplitRows()) {
            List<OrderEntry> orderEntryGroup = splitRow.getOrderEntries();
            Consignment splitConsignment = new Consignment();
            BeanUtils.copyProperties(sourceConsignment, splitConsignment);
            splitConsignment.setConsignmentId(null);
            splitConsignment.setCode(iSequenceGenerateService.getNextConsignmentCode());
            splitConsignment.setStatus(splitRow.getStatus());
            splitConsignment.setSplitReason(splitReason);

            // 发货单头预期交货时间取其下订单行最晚
            Date maxDate = orderEntryGroup.stream()
                    .peek(orderEntry -> Assert.notNull(orderEntry.getEstimateDeliveryTime(),
                            "订单行[" + orderEntry.getOrderEntryId() + "]预计交货时间不能为空"))
                    .map(OrderEntry::getEstimateDeliveryTime)
                    .sorted((time1, time2) -> time2.compareTo(time1)).findFirst().orElse(null);

            splitConsignment.setEstimateDeliveryTime(maxDate);

            // 品牌、服务点、发运方式
            writeBackConsignment(splitConsignment, orderEntryGroup.get(0));

            iConsignmentService.insertSelective(RequestHelper.newEmptyRequest(), splitConsignment);
            iLogManagerService.logTrace(this.getClass(), "发货单拆分流程节点", sourceConsignment.getConsignmentId(), "生成新发货单[" + splitConsignment.getConsignmentId() + "]");

            // 订单行关联新生成的发货单, 并将预计交货时间和头一致
            for (OrderEntry orderEntry : orderEntryGroup) {
                orderEntry.setConsignmentId(splitConsignment.getConsignmentId());
                orderEntry.setEstimateDeliveryTime(splitConsignment.getEstimateDeliveryTime());
                iOrderEntryService.updateByPrimaryKeySelective(RequestHelper.newEmptyRequest(), orderEntry);
            }

            splitConsingmnetList.add(splitConsignment);
        }

        // 原单设为拆单关闭
        sourceConsignment.setStatus(Constants.CON_STATUS_SPLIT_CLOSE);
        iConsignmentService.updateByPrimaryKeySelective(RequestHelper.newEmptyRequest(), sourceConsignment);
        iLogManagerService.logTrace(this.getClass(), "发货单拆分流程节点", sourceConsignment.getConsignmentId(), "发货单[" + sourceConsignment.getConsignmentId() + "]拆单关闭");

        return splitConsingmnetList;
    }

    @Override
    public void setStatus(String status, Consignment data) {
        data.setStatus(status);
        iConsignmentService.updateByPrimaryKeySelective(RequestHelper.newEmptyRequest(), data);
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
    public MarkorEmployee selectMarkorEmployeeByCode(String code) {
        return iMarkorEmployeeService.selectByEmployeeCode(code);
    }

    @Override
    public void calculateEstimateConTime(Consignment consignment) {
        // 查询发货单下所有订单行
        OrderEntry orderEntryParam = new OrderEntry();
        orderEntryParam.setConsignmentId(consignment.getConsignmentId());
        List<OrderEntry> orderEntryList = iOrderEntryService.select(orderEntryParam);
        Assert.notEmpty(orderEntryList, "发货单[" + consignment.getConsignmentId() + "]未关联订单行");

        // 获取订单行的仓库编码，拆分后的发货单订单行的仓库是一致的
        String pointOfServiceCode = iOrderEntryService.checkAndGetPosCodeOfOrderEntry(orderEntryList.get(0));
        Integer logisticsLeadTime = iConsignmentService.getLogisticsLeadTime(consignment.getReceiverCity(), consignment.getReceiverDistrict(), pointOfServiceCode, consignment.getShippingType());

        // 计算物流提前期
        for (OrderEntry orderEntry : orderEntryList) {
            Assert.notNull(orderEntry.getEstimateDeliveryTime(), "订单行[" + orderEntry.getOrderEntryId() + "]预计交货时间不能为空");
            orderEntry.setEstimateConTime(DateUtils.addDays(orderEntry.getEstimateDeliveryTime(), -logisticsLeadTime));
            iOrderEntryService.updateByPrimaryKeySelective(RequestHelper.newEmptyRequest(), orderEntry);
        }

        // 检查订单行(NORMAL)上面的预计发货时间是否一致
        Set<Date> estimateConTimeSet = orderEntryList.stream()
                .filter(orderEntry -> Constants.ORDER_ENTRY_STATUS_NORMAL.equals(orderEntry.getOrderEntryStatus()))
                .map(OrderEntry::getEstimateConTime).collect(Collectors.toSet());

        Assert.isTrue(estimateConTimeSet.size() == 1, "发货单[" + consignment.getConsignmentId() + "]上订单行的预计发货时间不一致");
    }
}
