package com.hand.hmall.om.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.hand.common.util.Auth;
import com.hand.common.util.SecretKey;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.mybatis.entity.Example;
import com.hand.hap.system.service.ICodeService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.as.dto.AsRefund;
import com.hand.hmall.as.dto.AsReturn;
import com.hand.hmall.as.dto.AsReturnEntry;
import com.hand.hmall.as.mapper.AsRefundMapper;
import com.hand.hmall.as.mapper.AsReturnEntryMapper;
import com.hand.hmall.as.mapper.AsReturnMapper;
import com.hand.hmall.as.service.IAsRefundService;
import com.hand.hmall.as.service.IAsReturnService;
import com.hand.hmall.common.service.ISequenceGenerateService;
import com.hand.hmall.mst.dto.MstUser;
import com.hand.hmall.mst.dto.Product;
import com.hand.hmall.mst.mapper.MstUserMapper;
import com.hand.hmall.mst.mapper.ProductMapper;
import com.hand.hmall.mst.service.IProductService;
import com.hand.hmall.om.dto.*;
import com.hand.hmall.om.mapper.*;
import com.hand.hmall.om.service.*;
import com.hand.hmall.om.tpl.TmallOrderTemplate;
import com.hand.hmall.pin.dto.Pin;
import com.hand.hmall.pin.mapper.AlterMapper;
import com.hand.hmall.pin.mapper.PinMapper;
import com.hand.hmall.pin.service.IPinAlterInfoService;
import com.hand.hmall.util.Constants;
import com.hand.hmall.util.StringUtils;
import com.markor.map.external.fndregionservice.service.IFndRegionsCommonExternalService;
import com.markor.map.external.pointservice.dto.PointOfServiceDto;
import com.markor.map.external.pointservice.service.IPointOfServiceExternalService;
import com.markor.map.framework.restclient.RestClient;
import net.sf.json.JSONObject;
import okhttp3.Response;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zhangmeng
 * @version 0.1
 * @name OrderServiceImpl
 * @description 订单查询页面接口
 * @date 2017/5/22
 */
@Service
@Transactional
public class OrderServiceImpl extends BaseServiceImpl<Order> implements IOrderService {

    // 预计交货时间延长天数配置参数
    private static final String ESTIMATE_DELIVERY_TIME_DAYS = "estimate.delivery.time.days";
    // 预计发货时间延长天数配置参数
    private static final String ESTIMATE_CON_TIME_DAYS = "estimate.con.time.days";
    // 订单行表中仓库/门店默认配置参数CODE
    private static final String VIRTUAL_ORDER_ORDER_ENTRY_POINT_OF_SERVICE = "virtualOrder.orderEntry.pointOfService";
    // 订单行表中仓库/门店默认配置参数ID
    private static final String VIRTUAL_ORDER_ORDER_ENTRY_POINT_OF_SERVICE_ID = "virtualOrder.orderEntry.pointOfService.id";
    // 天猫订单数据导入：付款比例默认值
    private static final String PAY_RATE_DEFAULT = "tmall.order.default.pay.rate";

    // 中国地区代码配置参数名
    private static final String CHINA_CODE = "region.country.china.code";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Properties pro = new Properties();
    @Autowired
    ISequenceGenerateService sequenceGenerateService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private TmConsignmentHisMapper tmConsignmentHisMapper;
    @Autowired
    private IOrderEntryService orderEntryService;
    @Autowired
    private IOmPromotionruleService promotionruleService;
    @Autowired
    private IOrderCouponruleService orderCouponruleService;
    @Autowired
    private IEntryTypeMappingService iEntryTypeMappingService;
    @Autowired
    private IOrderEntryService iOrderEntryService;
    @Autowired
    private IProductService iProductService;
    @Autowired
    private ISequenceGenerateService sequenceService;

    @Autowired
    private IFndRegionsCommonExternalService iFndRegionsCommonExternalService;

    @Autowired
    private MstUserMapper userMapper;

    @Autowired
    private IConsignmentService consignmentService;

    @Autowired
    private IPointOfServiceExternalService iPointOfServiceExternalService;

    @Autowired
    private IAsRefundService refundService;

    @Autowired
    private PinMapper pinMapper;

    @Autowired
    private AlterMapper alterMapper;

    @Autowired
    private RestClient restClient;

    @Autowired
    private ICodeService iCodeService;

    @Autowired
    private IPinAlterInfoService iPinAlterInfoService;
    @Autowired
    private IAsReturnService asReturnService;
    @Autowired
    private AsRefundMapper asRefundMapper;
    @Autowired
    private AmountChangeLogMapper amountChangeLogMapper;
    @Autowired
    private AmountChangeLogEntryMapper amountChangeLogEntryMapper;
    @Autowired
    private AsReturnMapper asReturnMapper;
    @Autowired
    private AsReturnEntryMapper asReturnEntryMapper;
    @Autowired
    private OrderEntryMapper orderEntryMapper;
    @Autowired
    private HmallSoChangeLogMapper soChangeLogMapper;
    @Autowired
    private ProductMapper productMapper;

    /**
     * 订单列表查询
     *
     * @param page
     * @param pageSize
     * @param code            订单编号
     * @param userId          用户ID
     * @param receiverMobile  收货人手机
     * @param startTime       下单开始日期
     * @param endTime         下单结束日期
     * @param strOrderStatus  订单状态
     * @param strDistribution 配送方式
     * @param vproduct        变式物料号
     * @param productId       商品编码
     * @param websiteId       网站ID ( "TM" - 天猫 | "1" - 商城 )
     * @param isIo            是否是虚拟订单 ( "Y" | "N" )
     * @param affiliationId   订单归属ID
     * @return
     */
    @Override
    public List<Order> selectOrderList(
            int page, int pageSize, String code, String escOrderCode, String userId, String locked, String receiverMobile,
            String startTime, String endTime, String[] strOrderStatus, String[] strDistribution, String[] strOrderTypes,
            String vproduct, String productId, String payRate, String pinCode, String websiteId, String isIo, Long affiliationId) {

        PageHelper.startPage(page, pageSize);
        List<Order> list = orderMapper.selectOrderList(code, escOrderCode, userId, locked, receiverMobile, startTime, endTime, strOrderStatus, strDistribution, strOrderTypes, vproduct, productId, payRate, pinCode, websiteId, isIo, affiliationId);
        for (Order a : list) {
            String orderStatus = a.getOrderStatus();
            if (orderStatus != null) {
                switch (orderStatus) {
                    case "TRADE_CLOSED_BY_UNIQLO":
                        a.setOrderStatus("交易取消");
                        break;
                    case "TRADE_CLOSED":
                        a.setOrderStatus("交易关闭");
                        break;
                    case "TRADE_FINISHED":
                        a.setOrderStatus("交易关闭");
                        break;
                    case "WAIT_SELLER_CONFIRM_GOODS":
                        a.setOrderStatus("待卖家收货");
                        break;
                    case "WAIT_BUYER_RETURN_GOODS":
                        a.setOrderStatus("待买家寄回商品");
                        break;
                    case "WAIT_BUYER_TAKE_GOODS":
                        a.setOrderStatus("待提货");
                        break;
                    case "WAIT_SELLER_SEND_GOODS__PICKUP":
                        a.setOrderStatus("已付款");
                        break;
                    case "WAIT_BUYER_CONFIRM_GOODS":
                        a.setOrderStatus("已发货");
                        break;
                    case "SELLER_CONSIGNED_PART":
                        a.setOrderStatus("部分发货");
                        break;
                    case "WAIT_SELLER_SEND_GOODS__EXPRESS":
                        a.setOrderStatus("待发货");
                        break;
                    case "WAIT_BUYER_PAY":
                        a.setOrderStatus("待付款");
                        break;
                    default:
                        a.setOrderStatus(" ");
                }
            }
        }

        Map<Long, String> employeeMap = new HashMap<>();
        for (Map m : orderMapper.queryOrderResponsible()) {
            employeeMap.put(Long.parseLong(m.get("ORDER_ID").toString()), m.get("NAME").toString());
        }
        for (Order o : list) {
            o.setResponsibleName(employeeMap.get(o.getOrderId()));
        }

        return list;
    }

    /**
     * 获取所有的锁定的订单信息
     *
     * @return 查询出的已锁定的订单信息
     */
    @Override
    public List<Order> getLockedOrder() {
        return orderMapper.getLockedOrder();
    }

    @Override
    public OrderSyncDto querySyncZmallOrder(Long orderId) {
        OrderSyncDto osd = orderMapper.selectOrderSyncInfoById(orderId);
        return osd == null ? null : exchange(osd);
//        if (osd != null) {
//            String status = osd.getOrderStatus();
//            exchange(osd);
//            osd.setOrderStatus(status);
//        }
//        return osd;
    }

    @Override
    public OrderSyncDto querySyncZmallOrderForAddEntry(Long orderId) {
        OrderSyncDto osd = orderMapper.selectOrderSyncInfoForAddEntryById(orderId);
        return osd == null ? null : exchange(osd);
    }

    @Override
    public void lockOrderStatus(Long orderId) {
        orderMapper.lockOrderStatus(orderId);
    }

    @Override
    public String getZmallAddress() {
        return getRestAddress("zmall");
    }

    @Override
    public String getZmallWebsiteAddress() {
        return getRestAddress("zmall_website");
    }

    /**
     * @param key - restClientConfig配置文件中的配置项名称
     * @return
     */
    private String getRestAddress(String key) {
        // 从json配置文件读取系统访问地址清单
        JSONObject jsonFromClasspath;
        InputStream configFileStream = this.getClass().getClassLoader().getResourceAsStream("restclientConfig.json");
        try {
            String configFileContent = IOUtils.toString(configFileStream, "UTF-8");
            jsonFromClasspath = JSONObject.fromObject(configFileContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return jsonFromClasspath.getString(key);
    }

    /**
     * 订单列表查询 不带分页
     *
     * @param code            订单编号
     * @param userId          用户ID
     * @param receiverMobile  收货人手机
     * @param startTime       下单开始日期
     * @param endTime         下单结束日期
     * @param strOrderStatus  订单状态
     * @param strDistribution 配送方式
     * @param vproduct        变式物料号
     * @param productId       商品编码
     * @return
     */
    @Override
    public List<Order> selectOrderList(IRequest iRequest, String code, String userId, String receiverMobile, String startTime, String endTime, String[] strOrderStatus, String[] strDistribution, String vproduct, String productId, String pinCode) {

        List<Order> list = orderMapper.selectOrderList(code, null, userId, null, receiverMobile, startTime, endTime, strOrderStatus, strDistribution, null, vproduct, productId, "", pinCode, null, null, null);
        for (Order order : list) {
            String a = order.getOrderSta();
            System.out.println(a);
            if (order.getOrderSta() != null) {
                String meaning = iCodeService.getCodeMeaningByValue(iRequest, "HMALL.ORDER.STATE", order.getOrderSta());
                order.setMeaning(meaning);
            }

        }
        for (Order a : list) {
            String orderStatus = a.getOrderStatus();
            if (orderStatus != null) {
                switch (orderStatus) {
                    case "TRADE_CLOSED_BY_UNIQLO":
                        a.setOrderStatus("交易取消");
                        break;
                    case "TRADE_CLOSED":
                        a.setOrderStatus("交易关闭");
                        break;
                    case "TRADE_FINISHED":
                        a.setOrderStatus("交易关闭");
                        break;
                    case "WAIT_SELLER_CONFIRM_GOODS":
                        a.setOrderStatus("待卖家收货");
                        break;
                    case "WAIT_BUYER_RETURN_GOODS":
                        a.setOrderStatus("待买家寄回商品");
                        break;
                    case "WAIT_BUYER_TAKE_GOODS":
                        a.setOrderStatus("待提货");
                        break;
                    case "WAIT_SELLER_SEND_GOODS__PICKUP":
                        a.setOrderStatus("已付款");
                        break;
                    case "WAIT_BUYER_CONFIRM_GOODS":
                        a.setOrderStatus("已发货");
                        break;
                    case "SELLER_CONSIGNED_PART":
                        a.setOrderStatus("部分发货");
                        break;
                    case "WAIT_SELLER_SEND_GOODS__EXPRESS":
                        a.setOrderStatus("待发货");
                        break;
                    case "WAIT_BUYER_PAY":
                        a.setOrderStatus("待付款");
                        break;
                    default:
                        a.setOrderStatus(" ");
                }
            }
        }
        Map<Long, String> employeeMap = new HashMap<>();
        for (Map m : orderMapper.queryOrderResponsible()) {
            employeeMap.put(Long.parseLong(m.get("ORDER_ID").toString()), m.get("NAME").toString());
        }
        for (Order o : list) {
            o.setResponsibleName(employeeMap.get(o.getOrderId()));
        }
        return list;
    }

    /**
     * 订单详情界面查询订单
     *
     * @param dto 订单实体类
     * @return
     */
    @Override
    public List<Order> selectInfoByOrderId(Order dto) {
        return orderMapper.selectInfoByOrderId(dto);
    }

    /**
     * 根据订单状态查询订单
     *
     * @param orderStatus 订单状态
     * @return List<Order>
     */
    @Override
    public List<Order> selectByStatus(String orderStatus) {
        Order order = new Order();
        order.setOrderStatus(orderStatus);
        return orderMapper.select(order);
    }

    /**
     * 根据订单ID查询退款单界面信息
     *
     * @param orderId 订单ID
     * @return 退款单界面根据订单ID查询部分界面信息
     */
    @Override
    public List<Order> selectRefundOrderInfoByOrderId(Long orderId) {
        return Arrays.asList(orderMapper.selectRefundOrderInfoByOrderId(orderId));
    }

    /**
     * 订单同步商城信息查询
     *
     * @return 查询出的同步订单信息
     */
    @Override
    public List<OrderSyncDto> selectOrderSyncInfo() {
        List<OrderSyncDto> orderList = new ArrayList<>();
        List<OrderSyncDto> dtos = orderMapper.selectOrderSyncInfo();
        if (CollectionUtils.isNotEmpty(dtos)) {
            for (OrderSyncDto order : dtos) {
                //调用的接口指定了数据格式，进行格式转换
                orderList.add(exchange(order));
            }
        }
        return orderList;
    }

    /**
     * 订单同步商城信息封装
     *
     * @param orderSyncDto 要同步的订单信息
     * @return 返回封装后的订单
     */

    private OrderSyncDto exchange(OrderSyncDto orderSyncDto) {
        //接口中日期格式的转换
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        SimpleDateFormat sdfe = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        //订单对应的订单行
        List<OrderEntryDto> orderEntries = new ArrayList<>();
        //订单对应的促销信息行
        List<OmPromotionruleDto> promotionrules = new ArrayList<>();
        //订单对应的优惠劵信息行
        List<OrderCouponruleDto> orderCouponrules = new ArrayList<>();
        //订单对应建议退款金额
        //BigDecimal referenceSum = null;
        orderSyncDto.setNetAmount(orderSyncDto.getOrderAmount().subtract(orderSyncDto.getPostFee()).subtract(orderSyncDto.getEpostFee()).subtract(orderSyncDto.getFixFee()));
        Example example = new Example(AmountChangeLog.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId", orderSyncDto.getOrderId());
        example.setOrderByClause("INSERT_TIME desc");
        List<AmountChangeLog> changeLogList = amountChangeLogMapper.selectByExample(example);
        AmountChangeLog changeLog = null;
        if (changeLogList.size() > 0) {
            changeLog = changeLogList.get(0);
            if (orderSyncDto.getOrderStatus().equals(Constants.TRADE_CLOSED)) {
                orderSyncDto.setPostFee(new BigDecimal("0"));
                orderSyncDto.setEpostFee(new BigDecimal("0"));
                orderSyncDto.setFixFee(new BigDecimal("0"));
                orderSyncDto.setCouponFee(new BigDecimal("0"));
                orderSyncDto.setDiscountFee(new BigDecimal("0"));
                orderSyncDto.setTotalDiscount(new BigDecimal("0"));
                orderSyncDto.setNetAmount(new BigDecimal("0"));
            } else {
                orderSyncDto.setPostFee(new BigDecimal(changeLog.getPostFee()));
                orderSyncDto.setEpostFee(new BigDecimal(changeLog.getEpostFee()));
                orderSyncDto.setFixFee(new BigDecimal(changeLog.getFixFee()));
                orderSyncDto.setCouponFee(new BigDecimal(changeLog.getCouponFee()));
                orderSyncDto.setDiscountFee(new BigDecimal(changeLog.getDiscountFee()));
                orderSyncDto.setTotalDiscount(new BigDecimal(changeLog.getTotalDiscount()));
                orderSyncDto.setNetAmount(new BigDecimal(changeLog.getNetAmount()));
            }
        }
        //根据订单ID查询对应的订单行、促销行、优惠券行信息
        if (orderSyncDto.getOrderId() != null && orderSyncDto.getOrderId() > 0) {
            orderEntries = orderEntryService.selectOrderSyncByOrderId(orderSyncDto.getOrderId());
            promotionrules = promotionruleService.selectOrderSyncByOrderId(orderSyncDto.getOrderId());
            orderCouponrules = orderCouponruleService.selectOrderSyncByOrderId(orderSyncDto.getOrderId());
            // referenceSum = refundService.getReferenceSumByOrderId(orderSyncDto.getOrderId());
        }

        if (orderEntries != null) {
            //订单行中的金额从金额变更记录中取 2017-12-20
            if (changeLog != null) {
                orderEntries = modifyOrderEntryList(changeLog, orderEntries);
            }
            //将订单行中最晚发货时间放入订单头回传给商城
            try {
                if (orderEntries.size() > 0) {
                    Date firstTime = null;
                    for (OrderEntryDto entryDto : orderEntries) {
                        if (!StringUtils.isEmpty(entryDto.getEstimateConTime())) {
                            firstTime = sdfe.parse(entryDto.getEstimateConTime());
                        }
                    }
                    if (firstTime != null) {
                        for (OrderEntryDto entry : orderEntries) {
                            if ((!StringUtils.isEmpty(entry.getEstimateConTime())) && (!entry.getOrderEntryStatus().equals("CANCEL"))) {
                                if (sdfe.parse(entry.getEstimateConTime()).getTime() > firstTime.getTime()) {
                                    firstTime = sdfe.parse(entry.getEstimateConTime());
                                }
                            }
                        }
                        orderSyncDto.setEstimateConTime(sdf.format(firstTime));
                    }

                }
            } catch (ParseException e) {
                logger.error("日期格式转换异常：" + e.getMessage());
            }

            orderSyncDto.setEntryList(orderEntries);
        }

        if (promotionrules != null)
            orderSyncDto.setOrderPromotionList(promotionrules);
        if (orderCouponrules != null)
            orderSyncDto.setOrderCouponList(orderCouponrules);

        //订单应付金额设置
        if (orderSyncDto.getRealOrderAmount() == null) {
            if (orderSyncDto.getOrderAmount() != null)
                orderSyncDto.setRealOrderAmount(orderSyncDto.getOrderAmount());
            else
                orderSyncDto.setRealOrderAmount(new BigDecimal(0));
        }

        //获取已退款金额信息
        /*if (referenceSum != null && referenceSum.compareTo(new BigDecimal(0)) >= 0) {
            orderSyncDto.setRefundAmount(referenceSum);
        } else {
            orderSyncDto.setRefundAmount(new BigDecimal(0));
        }*/
        if (orderSyncDto.getPostFee() == null) {
            orderSyncDto.setPostFee(new BigDecimal(0));
        }
        if (orderSyncDto.getEpostFee() == null) {
            orderSyncDto.setEpostFee(new BigDecimal(0));
        }
        if (orderSyncDto.getFixFee() == null) {
            orderSyncDto.setFixFee(new BigDecimal(0));
        }
        //封装Order订单状态，订单状态不在以下状态的置为 null
        if (orderSyncDto.getOrderStatus() != null && (("PART_DELIVERY").equals(orderSyncDto.getOrderStatus()) || ("WAIT_BUYER_CONFIRM").equals(orderSyncDto.getOrderStatus()) ||
                ("TRADE_BUYER_SIGNED").equals(orderSyncDto.getOrderStatus()) || ("TRADE_CLOSED").equals(orderSyncDto.getOrderStatus()))) {

        } else {
            orderSyncDto.setOrderStatus(null);
        }
        try {
            if (orderSyncDto.getEstimateDeliveryTime() != null) {
                orderSyncDto.setEstimateDeliveryTime(sdf.format(sdfe.parse(orderSyncDto.getEstimateDeliveryTime())));
            }
            /*if (orderSyncDto.getEstimateConTime() != null) {
                orderSyncDto.setEstimateConTime(sdf.format(sdfe.parse(orderSyncDto.getEstimateConTime())));
            }*/
            if (orderSyncDto.getOrderCreationtime() != null) {
                orderSyncDto.setOrderCreationtime(sdf.format(sdfe.parse(orderSyncDto.getOrderCreationtime())));
            }
        } catch (ParseException e) {
            logger.error("日期格式转换异常：" + e.getMessage());
        }
        return orderSyncDto;
    }

    /**
     * 查询待处理（存在订单行未关联发货单且未锁定）的订单
     *
     * @return List<Order>
     */
    @Override
    public List<Order> selectPendingOrders() {
        return orderMapper.selectPendingOrders();
    }

    /**
     * 获取订单的可备货金额
     *
     * @param order 订单
     * @return Double 可备货金额
     */
    @Override
    public Double getStockUpAmount(Order order) {
        OrderEntry orderEntryParam = new OrderEntry();
        orderEntryParam.setOrderId(order.getOrderId());
        // added by majun @2017/9/20 备货金金额不考虑已取消订单行
        orderEntryParam.setOrderEntryStatus(Constants.ORDER_ENTRY_STATUS_NORMAL);
        List<OrderEntry> orderEntryList = iOrderEntryService.select(orderEntryParam);

        Assert.notEmpty(orderEntryList, "订单[" + order.getOrderId() + "]不存在状态为NORMAL的订单行");

        BigDecimal totalAmount = new BigDecimal("0.0");
        for (OrderEntry orderEntry : orderEntryList) {

            Assert.notNull(orderEntry.getProductId(), "订单行[" + orderEntry.getOrderEntryId() + "]未关联商品");

            Product product = iProductService.selectByProductId(orderEntry.getProductId());
            Assert.notNull(product, "商品[" + orderEntry.getProductId() + "]不存在");

            String customType = product.getCustomType();
            Assert.notNull(product.getCustomType(), "商品[" + product.getProductId() + "]的定制类型未知");

            EntryTypeMapping entryTypeMapping = iEntryTypeMappingService.selectOneByProductType(customType);
            Assert.notNull(entryTypeMapping, "定制类型[" + customType + "]所对应行类型映射不存在");

            Assert.notNull(entryTypeMapping.getStockupPercent(), "行类型映射[" + entryTypeMapping.getMappingId() + "]未维护启动备货金额比例");

            Double totalFee = orderEntry.getTotalFee() == null ? 0D : orderEntry.getTotalFee();

            // updated by majun @2017/9/20 备货金额考虑部分退货
            // updated by majun @2017/10/10 备货金额解决double精度问题
            if (orderEntry.getReturnedQuantity() != null) {
                Assert.notNull(orderEntry.getQuantity(), "订单行[" + orderEntry.getOrderEntryId() + "]数量不能为空");
                BigDecimal quanPercent = new BigDecimal(Double.valueOf((orderEntry.getQuantity() - orderEntry.getReturnedQuantity()) * 1.0 / orderEntry.getQuantity()).toString());
                BigDecimal entryFee = quanPercent.multiply(new BigDecimal(totalFee.toString())).multiply(new BigDecimal(entryTypeMapping.getStockupPercent().toString()));
                totalAmount = totalAmount.add(entryFee);
            } else {
                BigDecimal entryFee = new BigDecimal(totalFee.toString()).multiply(new BigDecimal(entryTypeMapping.getStockupPercent().toString()));
                totalAmount = totalAmount.add(entryFee);
            }

        }
        return totalAmount.doubleValue();
    }

    @Override
    public void saveOrderFunc(IRequest iRequest, Order order) {

        Assert.notNull(order.getOrderId(), "传入订单id不能为空");
        this.updateByPrimaryKeySelective(iRequest, order);

        if (CollectionUtils.isEmpty(order.getOrderEntries())) {
            return;
        }

        // 查询源订单
        Order oriOrder = this.selectByPrimaryKey(iRequest, order);
        Assert.notNull(oriOrder, "原订单[" + order.getOrderId() + "]不存在");

        // 保存被修改行所关联的发货单，如果已经生成发货单
        List<Consignment> consignmentList = new ArrayList<>();
        // 保存界面传入的订单行
        List<Long> orderEntryIdList = new ArrayList<>();
        for (OrderEntry orderEntry : order.getOrderEntries()) {

            // 检查订单行参数
            Assert.notNull(orderEntry.getOrderEntryId(), "传入订单行id不能为空");
            Assert.notNull(orderEntry.getEstimateDeliveryTime(), "传入订单行预计交货时间不能为空");

            // 查询原订单行，进而确认原订单行是否关联发货单
            OrderEntry oriOrderEntry = iOrderEntryService.selectByPrimaryKey(iRequest, orderEntry);
            Assert.notNull(oriOrderEntry, "订单行[" + oriOrderEntry.getOrderEntryId() + "]不存在");

            // 查询订单行所关联的发货单
            if (oriOrderEntry.getConsignmentId() != null) {
                Consignment consignment = new Consignment();
                consignment.setConsignmentId(oriOrderEntry.getConsignmentId());
                consignment = consignmentService.selectByPrimaryKey(iRequest, consignment);
                Assert.notNull(consignment, "发货单[" + oriOrderEntry.getConsignmentId() + "]不存在");
                if (consignmentList.stream().mapToLong(Consignment::getConsignmentId)
                        .noneMatch(consignmentId -> consignmentId == oriOrderEntry.getConsignmentId())) {
                    consignmentList.add(consignment);
                }
            }
            orderEntryIdList.add(orderEntry.getOrderEntryId());

            // 查询仓库编码
            Long pointOfServiceId = oriOrderEntry.getPointOfServiceId();
            Assert.notNull(pointOfServiceId, "订单行[" + orderEntry.getOrderEntryId() + "]未关联仓库");

            PointOfServiceDto pointOfServiceDto = new PointOfServiceDto();
            pointOfServiceDto.setPointOfServiceId(pointOfServiceId);
            pointOfServiceDto = iPointOfServiceExternalService.selectByPrimaryKey(pointOfServiceDto);
            Assert.notNull(pointOfServiceDto, "服务点[" + pointOfServiceId + "]不存在");

            // 计算物流提前期
            Integer logisticsLeadTime = consignmentService.getLogisticsLeadTime(oriOrder.getReceiverCity(), oriOrder.getReceiverDistrict(), pointOfServiceDto.getCode(), oriOrderEntry.getShippingType());

            // 计算物流提前期，并更新预计发货时间
            Date estimateConTime = DateUtils.addDays(orderEntry.getEstimateDeliveryTime(), -logisticsLeadTime);
            oriOrderEntry.setEstimateConTime(estimateConTime);
            // 保存界面传入预计交货时间
            oriOrderEntry.setEstimateDeliveryTime(orderEntry.getEstimateDeliveryTime());

            iOrderEntryService.updateByPrimaryKeySelective(iRequest, oriOrderEntry);
        }

        // 获取原订单的所有订单行，并计算最晚预计交货时间
        OrderEntry orderEntryParam = new OrderEntry();
        orderEntryParam.setOrderId(order.getOrderId());
        List<OrderEntry> orderEntryList = iOrderEntryService.select(orderEntryParam);

        Date maxDate = orderEntryList.stream().filter(orderEntry -> {
            Assert.notNull(orderEntry.getEstimateDeliveryTime(), "订单行[" + orderEntry.getOrderEntryId() + "]预计交货时间不能为空");
            return true;
        }).map(OrderEntry::getEstimateDeliveryTime).max((date1, date2) -> date1.compareTo(date2)).get();

        oriOrder.setEstimateDeliveryTime(maxDate);
        oriOrder.setSyncZmall(Constants.NO);
        this.updateByPrimaryKeySelective(iRequest, oriOrder);

        //更新发货单头（如果已生成发货单)为所有关联行中的最晚预计交货日期，该发货单下的所有订单行的预计交货日期更新为和头一致，同时更新所有行的预计发货日期
        if (CollectionUtils.isEmpty(consignmentList)) {
            return;
        }

        for (Consignment consignment : consignmentList) {
            // 查询该发货单下所有订单行
            OrderEntry conOrderEntryParam = new OrderEntry();
            conOrderEntryParam.setConsignmentId(consignment.getConsignmentId());
            List<OrderEntry> conOrderEntryList = iOrderEntryService.select(conOrderEntryParam);

            // 更新发货单头为所有关联行中的最晚预计交货日期
            Date conMaxDate = orderEntryList.stream().map(OrderEntry::getEstimateDeliveryTime).max((date1, date2) -> date1.compareTo(date2)).get();
            consignment.setEstimateDeliveryTime(conMaxDate);

            // 更新同步Retail和同步商城标志为N
            consignment.setSyncflag(Constants.NO);
            consignment.setSyncZmall(Constants.NO);

            consignmentService.updateByPrimaryKeySelective(iRequest, consignment);

            for (OrderEntry orderEntry : conOrderEntryList) {
                if (orderEntryIdList.contains(orderEntry.getOrderEntryId())) {
                    continue;
                }

                // 更新预计交货时间和发货单头一致
                orderEntry.setEstimateDeliveryTime(conMaxDate);
                // 预计发货时间等于预计交货时间减去物流提前期

                // 查询订单行上的仓库编码
                String pointOfServiceCode = iOrderEntryService.checkAndGetPosCodeOfOrderEntry(orderEntry);

                // 计算物流提前提
                Integer logisticsLeadTime = consignmentService.getLogisticsLeadTime(consignment.getReceiverCity(), consignment.getReceiverDistrict(), pointOfServiceCode, orderEntry.getShippingType());
                orderEntry.setEstimateConTime(DateUtils.addDays(conMaxDate, -logisticsLeadTime));
                iOrderEntryService.updateByPrimaryKeySelective(iRequest, orderEntry);
            }
        }
    }

    /**
     * 导出天猫订单的发货单
     *
     * @return
     */
    @Override
    public List<TmData> exportTmData() {

        // 获取当前时间
        Date date = new Date();
        // 更新订单头状态信息
        Integer upCount = orderMapper.updateTmData(date);
        // 导出发货单数据
        List<TmData> tmList = orderMapper.exportTmData(date);
        if (tmList.size() > 0) {
            // 插入导出历史
            Integer createCount = tmConsignmentHisMapper.createExportData(date);
        }

        return tmList;
    }

    /**
     * 导出天猫订单的历史发货单
     *
     * @return
     */
    @Override
    public List<TmData> exportTmDataHis(Date date) {

        // 导出发货单数据
        List<TmData> tmList = orderMapper.exportTmData(date);

        return tmList;
    }

    public Properties getProperties() {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("bussiness.properties");
        try {
            pro.load(in);
        } catch (IOException e) {
            logger.error(e.getMessage());
            logger.error("取properties参数异常");
            return pro;
        }
        return pro;
    }


    /**
     * 导入天猫订单数据
     *
     * @param iRequest
     * @param tmallOrders
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void importTmallOrderData(IRequest iRequest, List<TmallOrderTemplate> tmallOrders) {

        // 按订单头对订单行进行归纳
        // 订单编码与订单行匹配映射关系
        Map<String, List<TmallOrderTemplate>> codeEntityMap = new HashMap<>();

        for (TmallOrderTemplate tot : tmallOrders) {
            if (tot.getOrderNumber() == null || tot.getOrderNumber().trim().isEmpty()) {
                throw new RuntimeException("excel文件中可能存在无效的空白行，请删除空白行后再次导入。");
            }
            if (!codeEntityMap.containsKey(tot.getOrderNumber())) {
                codeEntityMap.put(tot.getOrderNumber(), new ArrayList<>());
            }
            codeEntityMap.get(tot.getOrderNumber()).add(tot);
        }

        Order order;
        OrderEntry orderEntry;

        List<TmallOrderTemplate> tmallOrderTemplates;
        for (String orderCode : codeEntityMap.keySet()) {
            tmallOrderTemplates = codeEntityMap.get(orderCode);

            // 创建订单数据并导入
            order = transform2Order(iRequest, tmallOrderTemplates.get(0));
            try {
                order.setOrderType("NORMAL");
                order = this.insertSelective(iRequest, order);
            } catch (RuntimeException e) {
                logger.error("导入excel天猫订单数据 <订单头> 时数据库报错:" + e.getMessage());
                throw new RuntimeException("导入excel天猫订单数据 <订单头> 时数据库报错:" + e.getMessage(), e);
            }

            for (TmallOrderTemplate tot : tmallOrderTemplates) {

                // 创建订单行数据并导入
                orderEntry = transform2OrderEntry(order, iRequest, tot);
                orderEntry.setOrderId(order.getOrderId());
                orderEntry.setOrderEntryStatus("NORMAL");
                try {
                    // 设置订单行LineNumber
                    orderEntry.setLineNumber(iOrderEntryService.getNextLineNumber(order));
                    // 设置订单行CODE
                    orderEntry.setCode(sequenceGenerateService.getNextOrderEntryCode());
                    iOrderEntryService.insertSelective(iRequest, orderEntry);

                    // 天猫订单导入增加PIN码节点生成
                    Pin pin = new Pin();
                    pin.setCode(orderEntry.getPin());
                    pin.setEntryCode(orderEntry.getCode());
                    pin.setEventCode("MAP0100");
                    Map alterInfo = alterMapper.queryByEventCode("MAP0100");
                    if (alterInfo != null && alterInfo.get("EVENT_DES") != null) {
                        pin.setEventDes(alterInfo.get("EVENT_DES").toString());
                    }
                    pin.setSystem("hmall");
                    pin.setOperator("system");
                    // pin.setMobile(null);
                    pin.setOperationTime(new Date());
                    pin.setEventInfo("订单已生成");
                    //pin.setFlag_level1
                    //pin.setFlag_level2
                    //pin.setFlag_level3

                    pin.setCreatedBy(iRequest.getUserId());
                    pin.setCreationDate(new Date());
                    pin.setLastUpdatedBy(iRequest.getUserId());
                    pin.setLastUpdateDate(new Date());
                    pin.setObjectVersionNumber(1L);

                    pinMapper.insert(pin);

                    iPinAlterInfoService.insertPinAlterInfo(pin);

                } catch (RuntimeException e) {
                    logger.error("导入excel天猫订单数据 <订单行> 时数据库报错:" + e.getMessage());
                    throw new RuntimeException("导入excel天猫订单数据 <订单行> 时数据库报错:" + e.getMessage(), e);
                } catch (ParseException e) {
                    logger.error("导入excel天猫订单数据 <订单行> 时读取PIN码预警忽略时间区间报错:" + e.getMessage());
                    throw new RuntimeException("导入excel天猫订单数据 <订单行> 时读取PIN码预警忽略时间区间报错:" + e.getMessage(), e);
                }

            }

        }

    }

    private Order transform2Order(IRequest iRequest, TmallOrderTemplate tpl) {

        List<Order> orders = orderMapper.selectByEscOrderCode(tpl.getOrderNumber());
        if (!orders.isEmpty()) {
            throw new RuntimeException("已存在商城订单号: " + tpl.getOrderNumber());
        }

        Order order = new Order();

        //    Order	CODE	订单编号	字符			HMALL自行生成
        order.setCode(sequenceService.getNextOrderCode());

        //    Order	ESC_ORDER_CODE	平台订单编号	字符	Y	1	导入表格第一列信息
        order.setEscOrderCode(tpl.getOrderNumber());

        //    Order	ORDER_STATUS	订单状态	orderstatus	Y		生成的库存订单初始状态为“NEW_CREATE”
        order.setOrderStatus("NEW_CREATE");

        //    Order	POST_FEE	物流运费	数字	Y	4	导入表格第四列信息
        order.setPostFee(tpl.getTransportationCosts());

        //    Order	EPOST_FEE	快递运费	数字	Y		默认为0
        //    Order	FIX_FEE	安装费	数字	Y		默认为0
        //    Order	PRE_POSTFEE	物流运费减免	数字	Y		默认为0
        //    Order	PRE_EPOSTFEE	快递运费减免	数字	Y		默认为0
        //    Order	PRE_FIXFEE	安装费减免	数字	Y		默认为0
        order.setEpostFee(new BigDecimal(0));
        order.setFixFee(new BigDecimal(0));
        order.setPrePostfee(new BigDecimal(0));
        order.setPreEpostfee(new BigDecimal(0));
        order.setPreFixfee(new BigDecimal(0));

        //    Order	customerId	用户账号	User	Y	6	"导入第六列信息，根据第六列买家昵称生成对应的customer记录：
        //    MAPPING对应字段TM_NICKNAME。如果存在关联对应记录。
        //    如果不存在自动生成新的用户记录"
        List<MstUser> users = userMapper.queryUserByTMNickName(tpl.getBuyerNickname());
        if (users.isEmpty()) {
            MstUser user = new MstUser();
            user.setTmNickname(tpl.getBuyerNickname());
            user.setCreationDate(new Date());
            user.setCreatedBy(iRequest.getUserId());
            user.setLastUpdateDate(new Date());
            user.setLastUpdatedBy(iRequest.getUserId());
            user.setObjectVersionNumber(1L);
            user.setSyncflag("N");
            userMapper.insert(user);
            order.setUserId(user.getUserId());
        } else {
            order.setUserId(users.get(0).getUserId());
        }

        //    Order	currencyId	币种	currency	Y		默认CNY
        order.setCurrencyId("CNY");

        //    Order	websiteId	网站	Website	Y		默认TM
        order.setWebsiteId("TM");

        //    Order	channelId	渠道	Channel	Y		默认1
        order.setSalechannelId("1");

        //    Order	storeId	店铺	Store	Y		默认TMALL-ZEST
        order.setStoreId("TMALL-ZEST");

        //    Order	PAYMENT_AMOUNT	付款金额	数字		3	导入表格第三列
        //    Order	ORDER_AMOUNT	订单金额	数字		3	导入表格第三列
        order.setPaymentAmount(new Double(tpl.getTotalSales().doubleValue()));
        order.setOrderAmount(tpl.getTotalSales());

        //    Order	ORDER_CREATIONTIME	订单创建时间	日期		2	表格第二列
        order.setOrderCreationtime(tpl.getCreateTime());

        //    Order	BUYER_MEMO	买家备注	字符		5	表格第五列
        order.setBuyerMemo(tpl.getBuyerMessage());

        //    Order	IS_INVOICED	是否开票	字符	Y	16	表格十六列为空，则为N。否则为Y。
        //    Order	INVOICE_TYPE	发票类型	字符	Y	16	表格十六列为空，则为N。否则为C。
        //    Order	INVOICE_NAME	发票抬头	字符		16	表格第十六列
        order.setIsInvoiced((tpl.getInvoice() != null && !tpl.getInvoice().trim().isEmpty()) ? "Y" : "N");
        order.setInvoiceType((tpl.getInvoice() != null && !tpl.getInvoice().trim().isEmpty()) ? "C" : "N");
        order.setInvoiceName(tpl.getInvoice());

        //    Order	TOTALCON	是否齐套	Flag	Y		默认 是
        order.setTotalcon("Y");

        //    Order	RECEIVER_NAME	收货人姓名	字符		7	表格第七列
        order.setReceiverName(tpl.getTheRecipient());

        //    Order	RECEIVER_COUNTRY	收货人国家	area	Y		中国
        order.setReceiverCountry(getProperties().getProperty(CHINA_CODE));

        //    Order	RECEIVER_STATE	收货人省	area	Y	8	表格第八列。按照前两位MAPPING“HMALL_FND_REGIONS_TL”REGION_NAME。同时关联HMALL_FND_REGIONS_B的REGION_TYPE为province
        //    Order	RECEIVER_CITY	收货人市	area	Y	9	表格第九列。按照前两位MAPPING“HMALL_FND_REGIONS_TL”REGION_NAME。同时关联HMALL_FND_REGIONS_B的REGION_TYPE为city
        //    Order	RECEIVER _DISTRICT	收货人区	area	Y	10	表格第十列。按照前两位MAPPING“HMALL_FND_REGIONS_TL”REGION_NAME。同时关联HMALL_FND_REGIONS_B的REGION_TYPE为district
        order.setReceiverState(iFndRegionsCommonExternalService.findRegionCodeByNamePrefix("PROVINCE", tpl.getProvince(), null));
        order.setReceiverCity(iFndRegionsCommonExternalService.findRegionCodeByNamePrefix("CITY", tpl.getCity(), order.getReceiverState()));
        order.setReceiverDistrict(iFndRegionsCommonExternalService.findRegionCodeByNamePrefix("AREA", tpl.getArea(), order.getReceiverCity()));

        //    Order	RECEIVER_ADDRESS	收货人地址	字符		11	表格第十一列
        order.setReceiverAddress(tpl.getAddress());

        //    Order	RECEIVER_MOBILE	收货人手机号	字符		17	表格十七列
        //    Order	RECEIVER_PHONE	收货人电话	字符		18	表格十八列
        order.setReceiverMobile(tpl.getTelephone());
        order.setReceiverPhone(tpl.getLandline());

        //    Order	ESTIMATE_DELIVERY_TIME	预计交货时间	时间			默认当前时间后推7天
        //    Order	ESTIMATE_CON_TIME	增加预计发货时间	时间			默认当前时间后推5天
        order.setEstimateDeliveryTime(
                new Date(System.currentTimeMillis()
                        + 3600000 * 24 * Long.parseLong(getProperties().getProperty(ESTIMATE_DELIVERY_TIME_DAYS))));
        order.setEstimateConTime(
                new Date(System.currentTimeMillis()
                        + 3600000 * 24 * Long.parseLong(getProperties().getProperty(ESTIMATE_CON_TIME_DAYS))));

        //    Order	PAY_RATE	付款比例	数字	Y		1
        order.setPayRate(getProperties().getProperty(PAY_RATE_DEFAULT));

        //    Order	COUPON_FEE	优惠券优惠总金额	数字			默认为0
        //    Order	DISCOUNT_FEE	促销优惠总金额	数字			默认为0
        //    Order	TOTAL_DISCOUNT	总优惠金额	数字			默认为0
        order.setCouponFee(new BigDecimal(0));
        order.setDiscountFee(new BigDecimal(0));
        order.setTotalDiscount(new BigDecimal(0));

        order.setCreatedBy(iRequest.getUserId());
        order.setCreationDate(new Date());
        order.setLastUpdatedBy(iRequest.getUserId());
        order.setLastUpdateDate(new Date());
        order.setObjectVersionNumber(1L);

        return order;
    }

    private OrderEntry transform2OrderEntry(Order order, IRequest iRequest, TmallOrderTemplate tpl) {

        OrderEntry oe = new OrderEntry();

        // 	OrderEntry	ORDER	订单头号	Order	Y	关联对应订单号订单头
        oe.setOrderId(order.getOrderId());

        // 	OrderEntry	STATUS	订单状态	orderstatus	Y	默认为 NORMAL - 正常
        oe.setStatus("NORMAL");
        // 	OrderEntry	BASE_PRICE	单价	数字	N	导入表格十四列
        oe.setBasePrice(new Double(tpl.getActualUnitPrice().doubleValue()));
        // 	OrderEntry	QUANTITY	数量	数字	N	导入表格十三列
        oe.setQuantity(tpl.getPurchaseQuantity());
        // 	OrderEntry	UNIT_FEE	实际价格	数字	N	导入表格十四列
        oe.setUnitFee(new Double(tpl.getActualUnitPrice().doubleValue()));
        // 	OrderEntry	TOTAL_FEE	订单行应付金额	数字	N	导入表格十五列
        oe.setTotalFee(new Double(tpl.getAmountsPayable().doubleValue()));
        // 	OrderEntry	IS_GIFT	是否赠品	FLAG	Y	默认为 N-否
        oe.setIsGift("N");

        // 	OrderEntry	ESTIMATE_DELIVERY_TIME	预计交货时间	时间	Y	默认当前时间后推7天
        oe.setEstimateDeliveryTime(
                new Date(System.currentTimeMillis()
                        + 3600000 * 24 * Long.parseLong(getProperties().getProperty(ESTIMATE_DELIVERY_TIME_DAYS))));
        // 	OrderEntry	ESTIMATE_CON_TIME	预计发货时间	时间	Y	默认当前时间后推5天
        oe.setEstimateConTime(
                new Date(System.currentTimeMillis()
                        + 3600000 * 24 * Long.parseLong(getProperties().getProperty(ESTIMATE_CON_TIME_DAYS))));

        // 关联Prodcut表中的字段
        List<Product> ps = iProductService.selectProductByCode(tpl.getProductBusinessCode());
        if (ps.size() == 1) {
            // 	OrderEntry	PRODUCT	框架产品	product	Y	导入表格第十二列
            oe.setProductId(ps.get(0).getProductId());
            // 	OrderEntry	VPRODUCT	变式物料号	字符	N	对应导入表格商品编码，关联系统对应商品上的V码值
            oe.setVproductCode(ps.get(0).getvProductCode());
            // 	OrderEntry	SHIPPING_TYPE	配送方式	ZoneDeliveryMode	Y	默认LOGISTICS--物流
            oe.setShippingType(ps.get(0).getDefaultDelivery());
            // 设置频道关系
            oe.setOdtype(ps.get(0).getCustomChannelSource());
        } else if (ps.isEmpty()) {
            throw new RuntimeException("根据商品商家编码[" + tpl.getProductBusinessCode() + "]找不到对应的框架产品");
        } else {
            throw new RuntimeException("根据商品商家编码[" + tpl.getProductBusinessCode() + "]找到的框架产品过多" + ps.size());
        }

        // 	OrderEntry	SHIPPING_FEE	运费	字符	N	默认为0
        oe.setShippingFee(0D);
        // 	OrderEntry	INSTALLATION_FEE	安装费	字符	N	默认为0
        oe.setInstallationFee(0D);
        // 	OrderEntry	PRE_SHIPPINGFEE	运费减免	字符	N	默认为0
        oe.setPreShippingfee(0D);
        // 	OrderEntry	PRE_INSTALLATIONFEE	安装费减免	字符	N	默认为0
        oe.setPreInstallationfee(0D);
        // 	OrderEntry	DISCOUNT_FEE	订单行促销优惠金额	数字	N	默认为0
        oe.setDiscountFee(0D);
        // 	OrderEntry	DISCOUNT_FEEL	订单促销分摊优惠金额	数字	N	默认为0
        oe.setDiscountFeel(0D);
        // 	OrderEntry	COUPON_FEE	订单行优惠券优惠金额	数字	N	默认为0
        oe.setCouponFee(0D);
        // 	OrderEntry	TOTAL_DISCOUNT	优惠总金额	数字	N	默认为0
        oe.setTotalDiscount(0D);

        // 	OrderEntry	POINT_OF_SERVICE	门店/仓库	PointOfService	Y	默认天津仓
        oe.setPointOfService(getProperties().getProperty(VIRTUAL_ORDER_ORDER_ENTRY_POINT_OF_SERVICE));

        PointOfServiceDto dto = iPointOfServiceExternalService.selectByCode("W001");
        if (dto != null) {
            oe.setPointOfServiceId(dto.getPointOfServiceId());
        } else {
            throw new RuntimeException("没有找到对应的服务点信息");
        }

        // 	OrderEntry	PIN	PIN码	字符	Y	调用PIN码服务生成PIN码
        oe.setPin(sequenceService.getNextPin());

        oe.setCreatedBy(iRequest.getUserId());
        oe.setCreationDate(new Date());
        oe.setLastUpdatedBy(iRequest.getUserId());
        oe.setLastUpdateDate(new Date());
        oe.setObjectVersionNumber(1L);

        return oe;
    }

    @Override
    public Order reload(Order order) {
        Assert.notNull(order, "order is null");
        Assert.notNull(order.getOrderId(), "orderId is null");
        order = this.selectByPrimaryKey(RequestHelper.newEmptyRequest(), order);
        return order;
    }

    /**
     * 订单取消时，占用优惠券后在订单头中存入选择的优惠券id和促销id
     * 并且更新订单同步官网状态为N，将订单下所有订单行关联的发货单同步RETAIL以及同步商城状态置为N
     *
     * @param request
     * @param order
     * @return
     */
    @Override
    public Order setCouponAndPromotion(IRequest request, Order order) {
        //将促销id和优惠券id保存至订单头中，并更新订单同步官网状态为N
        order.setSyncZmall("N");
        if (order.getTradeFinishTime() != null) {
            order.setOrderAmount(null);
        }
        //更新促销优惠券关联表
        if (!StringUtils.isEmpty(order.getChosenCoupon())) {
            for (OrderCouponrule orderCouponrule : order.getCouponList()) {
                if (order.getChosenCoupon().equals(orderCouponrule.getCouponId())) {
                    orderCouponrule.setOrderId(order.getOrderId());
                    if (CollectionUtils.isNotEmpty(orderCouponruleService.selectOrderCouponruleByOrderId(orderCouponrule))) {
                        orderCouponruleService.updateOrderCouponruleByOrderId(orderCouponrule);
                    } else {
                        orderCouponruleService.insertSelective(request, orderCouponrule);
                    }
                }
            }

        } else {
            OrderCouponrule orderCouponrule = new OrderCouponrule();
            orderCouponrule.setOrderId(order.getOrderId());
            orderCouponruleService.deleteOrderCouponruleByOrderId(orderCouponrule);
        }
        if (!StringUtils.isEmpty(order.getChosenPromotion())) {
            for (OmPromotionrule omPromotionrule : order.getActivities()) {
                if (order.getChosenPromotion().equals(omPromotionrule.getActivityId())) {
                    omPromotionrule.setOrderId(order.getOrderId());
                    if (CollectionUtils.isNotEmpty(promotionruleService.selectOmPromotionruleByOrderId(omPromotionrule))) {
                        promotionruleService.updateOmPromotionruleByOrderId(omPromotionrule);
                    } else {
                        promotionruleService.insertSelective(request, omPromotionrule);
                    }
                }
            }
        } else {
            OmPromotionrule omPromotionrule = new OmPromotionrule();
            omPromotionrule.setOrderId(order.getOrderId());
            promotionruleService.deleteOmPromotionruleByOrderId(omPromotionrule);
        }
        updateByPrimaryKeySelective(request, order);
        order.setRefundAmount(getTotalRefundAmount(request, order).getRefundAmount());
        updateByPrimaryKeySelective(request, order);
        //增加金额变更记录头
        Order orderInfo = orderMapper.selectByPrimaryKey(order);
        orderInfo.setPromotionResult(order.getPromotionResult());
        AmountChangeLog changeLog = null;
        if (order.getPromotionResult() != null) {
            changeLog = addAmountChangeLog(orderInfo);
        }
        //将订单下所有订单行关联的发货单同步RETAIL以及同步商城状态置为N
        OrderEntry o = new OrderEntry();
        o.setOrderId(order.getOrderId());
        List<OrderEntry> list = orderEntryService.select(o);
        if (CollectionUtils.isNotEmpty(list)) {
            for (OrderEntry orderEntry : list) {
                if (orderEntry.getConsignmentId() != null) {
                    Consignment consignment = new Consignment();
                    consignment.setConsignmentId(orderEntry.getConsignmentId());
                    consignment.setSyncflag("N");
                    consignment.setSyncZmall("N");
                    consignmentService.updateByPrimaryKeySelective(request, consignment);
                }
                //增加金额变更记录行
                if (changeLog != null) {
                    addAmountChangeLogEntry(changeLog, orderInfo, orderEntry);
                }

            }
        }
        if (order.getOrderId() != null && order.getOrderId() > 0) {
            logger.info("*********************订单行取消开始同步**************************");
            orderSyncZmall(order.getOrderId());
            logger.info("*********************订单行取消同步结束**************************");
        }
        return order;
    }

    /**
     * 根据指定的订单状态获取订单列表
     * 取订单状态不是NEW_CREATE,PROCESS_ERROR, TRADE_FINISHED, TRADE_CLOSED, TRADE_CANCELLED的订单
     *
     * @return
     */
    @Override
    public List<Order> selectOrderListByStatus() {
        return orderMapper.selectOrderListByStatus();
    }

    /**
     * 根据平台订单号查询状态为NORMAL的订单表
     *
     * @param escOrderCode
     * @return
     */
    @Override
    public List<Order> selectByEscOrderCodeAndOrderType(String escOrderCode) {
        return orderMapper.selectByEscOrderCodeAndOrderType(escOrderCode);
    }

    /**
     * 判断指定平台订单号且WEBSITE_ID=TM且ORDER_TYPE=NORMAL的记录是否存在
     *
     * @param escOrderCode
     * @return
     */
    @Override
    public List<Order> selectInfoByEscOrderCodeAndWebsiteId(String escOrderCode) {
        return orderMapper.selectInfoByEscOrderCodeAndWebsiteId(escOrderCode);
    }

    /**
     * 批量更新订单表
     *
     * @param orderList
     */
    @Override
    public void updateBatchOrder(List<Order> orderList) {
        orderMapper.updateBatchOrder(orderList);
    }

    /**
     * 设置订单归属信息
     *
     * @param orderIds   - 订单ID列表
     * @param employeeId - 员工ID
     */
    @Override
    public void setOrderAssiging(List<Long> orderIds, Long employeeId) {
        orderMapper.setOrderAssiging(orderIds, employeeId);
    }


    /**
     * （商城）退货、取消订单行后将订单信息同步商城
     *
     * @param orderId
     */
    public void orderSyncZmall(Long orderId) {

        if (orderId == null) {
            logger.error("OrderServiceImpl类中orderSyncZmall方法参数订单ID为空,无法同步");
            return;
        }

        // 订单信息
        OrderSyncDto order = this.querySyncZmallOrder(orderId);

        if (order == null) {
            logger.error("找不到符合推送要求的订单。");
            return;
        }

        // 同步订单数据
        String jsonString = JSON.toJSONString(order);

        Map<String, String> map = new HashMap<>();
        map.put("token", Auth.md5(SecretKey.KEY + jsonString));

        // 同步订单响应信息
        Response zmallResponse;

        try {
            zmallResponse = restClient.postString(Constants.ZMALL, "/zmallsync/orderSync", jsonString, "json", map, null);

            String bodyStr = zmallResponse.body().string();

            JSONObject jsonResult = JSONObject.fromObject(bodyStr);

            if ("S".equals(jsonResult.getString("code"))) { // 接口调用成功
                logger.info("订单推送商城成功！");
            } else {         //接口调用异常
                logger.error("订单推送商城失败,异常状态码：" + jsonResult.getString("code") + ";异常信息：" + jsonResult.getString("message"));
                throw new RuntimeException("订单推送商城失败,异常状态码：" + jsonResult.getString("code") + ";异常信息：" + jsonResult.getString("message"));
            }
        } catch (IOException e) {
            logger.error("订单推送商城失败,抛出异常:" + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("订单推送商城失败,抛出异常:" + e.getMessage());
        } catch (Exception e) {
            logger.error("订单推送商城失败,抛出异常:" + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("订单推送商城失败,抛出异常:" + e.getMessage());
        }
    }

    /**
     * 计算退货行运费安装费
     *
     * @param order
     * @return
     */
    @Override
    public Double getInstallationFee(Order order) {
        double returnFee = 0;
        OrderEntry orderEntry = new OrderEntry();
        orderEntry.setOrderId(order.getOrderId());
        List<OrderEntry> orderEntryList = orderEntryService.selectFeeByOrderId(orderEntry);
        for (OrderEntry obj : orderEntryList) {
            if (obj.getReturnedQuantity() != null && obj.getReturnedQuantity() > 0) {
                returnFee = returnFee + ((obj.getReturnedQuantity() / obj.getQuantity().doubleValue()) * ((obj.getShippingFee() != null ? obj.getShippingFee() : 0) + (obj.getInstallationFee() != null ? obj.getInstallationFee() : 0)));
            }
        }
        return new Double(returnFee);
    }

    /**
     * 获取总的退款金额
     *
     * @param order
     * @return
     */
    @Override
    public Order getTotalRefundAmount(IRequest request, Order order) {
        BigDecimal cancelUnCreateAmount = new BigDecimal(0), cancelCreateAmount = new BigDecimal(0);//取消未生成退款金额，取消生成退款金额
        BigDecimal returnUnCreateAmount = new BigDecimal(0), returnCreateAmount = new BigDecimal(0);//退货未生成退款金额，退货生成退款金额
        BigDecimal totalRefundAmount = new BigDecimal(0);//总退款金额
        //1）取消未生成退款单退款金额计算逻辑：
        //  取order id=当前订单，在订单表【hmall_om_order】中,校验取消未生成退款【CANCEL_REFUND_UNCREATE】字段
        //①若‘取消未生成退款’字段为‘Y’，则计算该行的建议退款金额（若计算建议退款金额为负值，仍用于逻辑计算）
        //②若‘取消未生成退款’字段为'N'或为空时，则视建议退款金额字段的值为0
        Order orderInfo = orderMapper.selectByPrimaryKey(order);
        if (orderInfo.getCancelRefundUncreate() != null && orderInfo.getCancelRefundUncreate().equals("Y")) {
            AsReturn asReturn = new AsReturn();
            asReturn.setWebsiteId("1");
            asReturn.setOrderId(order.getOrderId());
            List<AsReturn> asReturnList = asReturnService.selectUserInfoByOrderId(asReturn);
            if (asReturnList != null && asReturnList.size() > 0) {
                cancelUnCreateAmount = new BigDecimal(asReturnList.get(0).getReferenceFee().toString());
            }
        }
        //2）取消已生成退款单退款金额计算逻辑：
        //取order id=当前订单，在退款单表【hmall_as_refund】中，校验退款单状态即【STATUS】字段
        //①若退款单状态为取消状态，即【STATUSb】=‘CANC’，则视退款单建议退款金额字段的值为0。
        //②若退款单状态不为取消状态，即【STATUS】≠‘CANC’,则判定该退款单是否关联退货单。
        //若【RETURN_ID】为空或为0时，取该退款单的建议退款金额【REFERENCE_SUM】字段
        //若【RETURN_ID】不为空且不为0时，则视该退款单的建议退款金额字段值为0。
        AsRefund asRefund = new AsRefund();
        asRefund.setOrderId(order.getOrderId());
        List<AsRefund> refundList = asRefundMapper.select(asRefund);
        for (AsRefund refund : refundList) {
            if (!refund.getStatus().equals("CANC")) {
                if (refund.getReturnId() == null || refund.getReturnId().compareTo(new BigDecimal(0)) == 0) {
                    cancelCreateAmount = cancelCreateAmount.add(refund.getReferenceSum() != null ? refund.getReferenceSum() : new BigDecimal("0"));
                }
            }
        }
        // 1）退货（含退款不退货）未生成退货单退款金额计算逻辑：
        // 取order id=当前订单，在订单行表【hmall_om_orderentry】表中，校验未生成退货单数量【NOT_RETURN_QUANTITY】字段
        //①若‘未生成退货单’数量为0或为空时，则视建议退款金额字段为0
        //②若‘未生成退货单’数量不为0且不为空时，则计算该行的建议退款金额。
        OrderEntry orderEntry = new OrderEntry();
        orderEntry.setOrderId(order.getOrderId());
        List<OrderEntry> orderEntryList = orderEntryService.select(orderEntry);
        for (OrderEntry entry : orderEntryList) {
            if (entry.getNotReturnQuantity() != null) {
                if (entry.getNotReturnQuantity() != 0) {
                    AsReturn asr = new AsReturn();
                    asr.setWebsiteId("1");
                    asr.setOrderId(order.getOrderId());
                    List<AsReturn> asrList = asReturnService.selectUserInfoByOrderId(asr);
                    if (asrList != null && asrList.size() > 0) {
                        returnUnCreateAmount = returnUnCreateAmount.add(new BigDecimal(asrList.get(0).getReferenceFee().toString()));
                    }
                }
            }
        }
        // 2）退货（含退款不退货）已生成退货单金额计算逻辑：
        // 取order id=当前订单，且在退货单表【hmall_as_return】中，校验退货单状态，即【STATUS】字段
        //①若退货单状态为取消状态，即【STATUS】=‘CANC’，则视退货单建议退款金额字段的值为0。
        //②若退货单状态不为取消状态，即【STATUS】≠‘CANC’,取该退货单的建议退款金额【REFERENCE_FEE】的值。
        AsReturn turn = new AsReturn();
        turn.setOrderId(order.getOrderId());
        List<AsReturn> turnList = asReturnService.select(request, turn, 1, Integer.MAX_VALUE);
        for (AsReturn as : turnList) {
            if (!as.getStatus().equals("CANC")) {
                returnCreateAmount = returnCreateAmount.add(new BigDecimal(as.getReferenceFee() != null ? as.getReferenceFee().toString() : "0"));
            }
        }
        totalRefundAmount = cancelUnCreateAmount.add(cancelCreateAmount).add(returnUnCreateAmount).add(returnCreateAmount);
        if (totalRefundAmount.compareTo(new BigDecimal("0")) > 0) {
            orderInfo.setRefundAmount(totalRefundAmount);
        } else {
            orderInfo.setRefundAmount(new BigDecimal("0"));
        }
        return orderInfo;
    }


    /**
     * 增加金额更改记录头
     *
     * @param order
     * @return
     */
    public AmountChangeLog addAmountChangeLog(Order order) {
        AmountChangeLog changeLog = new AmountChangeLog();
        changeLog.setOrderId(order.getOrderId());
        changeLog.setEscOrderCode(order.getEscOrderCode());
        changeLog.setCode(order.getCode());
        changeLog.setPostFee(order.getPromotionResult().getPostFee());
        changeLog.setEpostFee(order.getPromotionResult().getEpostFee());
        changeLog.setFixFee(order.getPromotionResult().getFixFee());
        changeLog.setCouponFee(order.getPromotionResult().getCouponFee());
        changeLog.setDiscountFee(order.getPromotionResult().getDiscountFee());
        changeLog.setTotalDiscount(order.getPromotionResult().getTotalDiscount());
        changeLog.setNetAmount(order.getPromotionResult().getNetAmount());
        changeLog.setInsertTime(System.currentTimeMillis());
        amountChangeLogMapper.insertSelective(changeLog);
        return changeLog;
    }

    /**
     * 增加金额更改记录行
     *
     * @param changeLog
     * @param order
     * @param orderEntry
     */
    public void addAmountChangeLogEntry(AmountChangeLog changeLog, Order order, OrderEntry orderEntry) {
        for (PromotionResult.OrderEntryResult orderEntryResult : order.getPromotionResult().getOrderEntryList()) {
            if (orderEntryResult.getIsGift().equals("N")) {
                if (orderEntryResult.getLineNumber().equals(orderEntry.getLineNumber().toString())) {
                    AmountChangeLogEntry changeLogEntry = new AmountChangeLogEntry();
                    changeLogEntry.setOrderEntryId(orderEntry.getOrderEntryId());
                    changeLogEntry.setOrderId(order.getOrderId());
                    changeLogEntry.setCode(changeLog.getCode());
                    changeLogEntry.setUnitFee(orderEntryResult.getUnitFee());
                    changeLogEntry.setTotalFee(orderEntryResult.getTotalFee());
                    changeLogEntry.setShippingFee(orderEntryResult.getShippingFee());
                    changeLogEntry.setInstallationFee(orderEntryResult.getInstallationFee());
                    changeLogEntry.setDiscountFee(orderEntryResult.getDiscountFee());
                    changeLogEntry.setDiscountFeel(orderEntryResult.getDiscountFeel());
                    changeLogEntry.setCouponFee(orderEntryResult.getCouponFee());
                    changeLogEntry.setTotalDiscount(orderEntryResult.getTotalDiscount());
                    changeLogEntry.setInsertTime(changeLog.getInsertTime());
                    amountChangeLogEntryMapper.insertSelective(changeLogEntry);
                }
            }

        }
    }

    /**
     * 修改订单行中的金额，从金额变更记录中取值
     *
     * @param changeLog
     * @param list
     */
    private List<OrderEntryDto> modifyOrderEntryList(AmountChangeLog changeLog, List<OrderEntryDto> list) {
        AmountChangeLogEntry logEntry = new AmountChangeLogEntry();
        logEntry.setOrderId(changeLog.getOrderId());
        logEntry.setInsertTime(changeLog.getInsertTime());
        List<AmountChangeLogEntry> changeLogEntryList = amountChangeLogEntryMapper.select(logEntry);
        if (changeLogEntryList.size() > 0) {
            for (OrderEntryDto entryDto : list) {
                for (AmountChangeLogEntry changeLogEntry : changeLogEntryList) {
                    if (changeLogEntry.getOrderEntryId().equals(entryDto.getOrderEntryId())) {
                        entryDto.setUnitFee(new BigDecimal(changeLogEntry.getUnitFee().toString()));
                        entryDto.setTotalFee(new BigDecimal(changeLogEntry.getTotalFee().toString()));
                        entryDto.setShippingFee(new BigDecimal(changeLogEntry.getShippingFee().toString()));
                        entryDto.setInstallationFee(new BigDecimal(changeLogEntry.getInstallationFee().toString()));
                        entryDto.setDiscountFee(new BigDecimal(changeLogEntry.getDiscountFee().toString()));
                        entryDto.setDiscountFeel(new BigDecimal(changeLogEntry.getDiscountFeel().toString()));
                        entryDto.setCouponFee(new BigDecimal(changeLogEntry.getCouponFee().toString()));
                        entryDto.setTotalDiscount(new BigDecimal(changeLogEntry.getTotalDiscount().toString()));
                    }
                }
            }
        }
        return list;
    }

    /**
     * 增加书面记录
     *
     * @param soChangeLog
     */
    @Override
    public void addSoChangeLog(HmallSoChangeLog soChangeLog) {
        Product mstProduct = new Product();
        mstProduct.setProductId(soChangeLog.getProductId());
        Product product = productMapper.selectByPrimaryKey(mstProduct);
        if (soChangeLog.getOrderType().equals("1")) {
            Order orderInfo = new Order();
            orderInfo.setOrderId(soChangeLog.getOrderId());
            Order order = orderMapper.selectByPrimaryKey(orderInfo);
            soChangeLog.setOrderCreateDate(order.getCreationDate());//订单创建时间
            OrderEntry entry = new OrderEntry();
            entry.setOrderEntryId(soChangeLog.getOrderEntryId());
            OrderEntry orderEntry = orderEntryMapper.selectByPrimaryKey(entry);

            //查询书面表并排序，获取最新一条
            Example example = new Example(HmallSoChangeLog.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("orderId", soChangeLog.getOrderId());
            criteria.andEqualTo("orderEntryId", soChangeLog.getOrderEntryId());
            example.setOrderByClause("CREATION_DATE desc");
            List<HmallSoChangeLog> changeLogList = soChangeLogMapper.selectByExample(example);
            if (orderEntry.getParentLine() != null) {//判断是否是套件子件
                OrderEntry parentEntry = new OrderEntry();
                parentEntry.setOrderEntryId(orderEntry.getParentLine());
                OrderEntry entryInfo = orderEntryMapper.selectByPrimaryKey(parentEntry);
                Double compPrice = getPriceForComponentLine(orderEntry, entryInfo.getInternalPrice());
                soChangeLog.setProductType("3");//商品类型
                if (orderEntry.getOrderEntryStatus().equals("CANCEL")) {
                    soChangeLog.setQuantity(0);//销售量
                    soChangeLog.setTotalFee(new BigDecimal("0"));//销售额（含税）
                } else {
                    soChangeLog.setQuantity(orderEntry.getQuantity().intValue());//销售量
                    soChangeLog.setTotalFee(new BigDecimal(compPrice.toString()).multiply(new BigDecimal(orderEntry.getQuantity())));//销售额（含税）
                }

                soChangeLog.setParentProductId(entryInfo.getProductId());//套件子件取套件产品id
                soChangeLog.setParentOrderEntryId(entryInfo.getOrderEntryId());//套件子件取套件订单行id
                if (changeLogList.size() > 0) {
                    //如果是取消的行变化用0减，不是取消行用当前数量-书面表最新的一次数量
                    soChangeLog.setChangeQuantity(orderEntry.getOrderEntryStatus().equals("CANCEL") ? -orderEntry.getQuantity().intValue() : orderEntry.getQuantity().intValue() - changeLogList.get(0).getQuantity());
                    BigDecimal price = orderEntry.getOrderEntryStatus().equals("CANCEL") ? new BigDecimal("0").subtract(new BigDecimal(compPrice.toString()).multiply(new BigDecimal(orderEntry.getQuantity()))) : new BigDecimal(compPrice.toString()).multiply(new BigDecimal(orderEntry.getQuantity())).subtract(changeLogList.get(0).getTotalFee());
                    soChangeLog.setChangeFee(price);
                }
            } else {
                if (product.getIsSuit().equals("Y")) {//判断是套件头还是常规品
                    soChangeLog.setProductType("1");
                } else {
                    soChangeLog.setProductType("2");
                }
                if (orderEntry.getOrderEntryStatus().equals("CANCEL")) {
                    soChangeLog.setQuantity(0);//销售量
                    soChangeLog.setTotalFee(new BigDecimal("0"));//销售额（含税）
                } else {
                    soChangeLog.setQuantity(orderEntry.getQuantity().intValue());
                    soChangeLog.setTotalFee(new BigDecimal(orderEntry.getTotalFee().toString()));
                }

                soChangeLog.setParentProductId(orderEntry.getProductId());//套件头或者常规品取自己的商品Id
                soChangeLog.setParentOrderEntryId(orderEntry.getOrderEntryId());//套件头或者常规品取自己的订单行Id
                if (changeLogList.size() > 0) {
                    soChangeLog.setChangeQuantity(orderEntry.getOrderEntryStatus().equals("CANCEL") ? -orderEntry.getQuantity().intValue() : orderEntry.getQuantity().intValue() - changeLogList.get(0).getQuantity());
                    soChangeLog.setChangeFee(orderEntry.getOrderEntryStatus().equals("CANCEL") ? new BigDecimal("0").subtract(new BigDecimal(orderEntry.getTotalFee().toString())) : new BigDecimal(orderEntry.getTotalFee().toString()).subtract(changeLogList.get(0).getTotalFee()));
                }
            }

        } else if (soChangeLog.getOrderType().equals("2")) {//退换货单
            Order orderInfo = new Order();
            orderInfo.setOrderId(soChangeLog.getParentOrderId());
            Order order = orderMapper.selectByPrimaryKey(orderInfo);
            soChangeLog.setOrderCreateDate(order.getCreationDate());
            AsReturnEntry asReturnEntry = new AsReturnEntry();
            asReturnEntry.setAsReturnEntryId(soChangeLog.getOrderEntryId());
            AsReturnEntry returnEntry = asReturnEntryMapper.selectByPrimaryKey(asReturnEntry);
            OrderEntry entry = new OrderEntry();
            entry.setOrderEntryId(returnEntry.getOrderEntryId());
            OrderEntry orderEntry = orderEntryMapper.selectByPrimaryKey(entry);
            if (orderEntry.getParentLine() != null) {//判断是否是套件子件
                soChangeLog.setProductType("3");//商品类型
                OrderEntry parentEntry = new OrderEntry();
                parentEntry.setOrderEntryId(orderEntry.getParentLine());
                OrderEntry entryInfo = orderEntryMapper.selectByPrimaryKey(parentEntry);
                soChangeLog.setParentProductId(entryInfo.getProductId());//套件子件取套件产品id
                soChangeLog.setParentOrderEntryId(entryInfo.getOrderEntryId());//套件子件取套件订单行id
            } else {
                if (product.getIsSuit().equals("Y")) {//判断是套件头还是常规品
                    soChangeLog.setProductType("1");
                } else {
                    soChangeLog.setProductType("2");
                }
                soChangeLog.setParentProductId(orderEntry.getProductId());//套件子件取套件产品id
                soChangeLog.setParentOrderEntryId(orderEntry.getOrderEntryId());//套件子件取套件订单行id
            }

        } else if (soChangeLog.getOrderType().equals("3")) {//赔付单
            Order orderInfo = new Order();
            orderInfo.setOrderId(soChangeLog.getParentOrderId());
            Order order = orderMapper.selectByPrimaryKey(orderInfo);
            soChangeLog.setOrderCreateDate(order.getCreationDate());
        }
        soChangeLog.setOrderEntryType("1");//区分正品销售：1、非正品销售：2
        soChangeLogMapper.insertSelective(soChangeLog);
    }

    /**
     * 获取套件子件的价格
     *
     * @param dto
     * @return
     */
    public Double getPriceForComponentLine(OrderEntry dto, double price) {
        Double compPrice = 0.0, allRate = 0.0;
        DecimalFormat df = new DecimalFormat("######0.00");
        //根据子行，找出该子行对应的父行的所有子行
        OrderEntry entry = new OrderEntry();
        entry.setParentLine(dto.getParentLine());
        List<OrderEntry> list = orderEntryMapper.select(entry);
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
