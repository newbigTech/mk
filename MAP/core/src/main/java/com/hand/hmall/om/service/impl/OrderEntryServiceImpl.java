package com.hand.hmall.om.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.account.dto.User;
import com.hand.hap.account.service.IUserService;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.im.service.IImAtpInfaceService;
import com.hand.hap.mam.dto.MamSoApproveHis;
import com.hand.hap.mam.service.IMamSoApproveHisService;
import com.hand.hap.mam.service.IMamVcodeHeaderService;
import com.hand.hap.mybatis.entity.Example;
import com.hand.hap.mybatis.util.StringUtil;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.common.service.IGlobalVariantService;
import com.hand.hmall.common.service.ISequenceGenerateService;
import com.hand.hmall.mst.dto.MstUser;
import com.hand.hmall.mst.dto.PriceRequestData;
import com.hand.hmall.mst.dto.Product;
import com.hand.hmall.mst.service.IMstUserService;
import com.hand.hmall.mst.service.IProductService;
import com.hand.hmall.om.dto.*;
import com.hand.hmall.om.mapper.ConsignmentMapper;
import com.hand.hmall.om.mapper.OrderEntryMapper;
import com.hand.hmall.om.service.*;
import com.hand.hmall.pin.dto.Pin;
import com.hand.hmall.pin.dto.PinAlter;
import com.hand.hmall.pin.mapper.PinAlterMapper;
import com.hand.hmall.pin.service.IAlterService;
import com.hand.hmall.pin.service.IPinService;
import com.hand.hmall.util.Constants;
import com.hand.hmall.util.DateUtil;
import com.hand.hmall.util.StringUtils;
import com.markor.map.external.pointservice.dto.PointOfServiceDto;
import com.markor.map.external.pointservice.service.IPointOfServiceExternalService;
import com.markor.map.framework.restclient.RestClient;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import okhttp3.Response;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name OrderEntryServiceImpl
 * @description 订单行
 * @date 2017年5月26日10:52:23
 */
@Service
@Transactional
public class OrderEntryServiceImpl extends BaseServiceImpl<OrderEntry> implements IOrderEntryService {

    private static final Long FIRST_LINE_NUMBER = 10L; // 第一个订单行的lineNumber
    private static final Long LINE_NUMBER_STEP = 10L; // lineNumber以10递增
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String INV_URL = "api/public/hap/im/atp/inface/importAtpData";
    @Autowired
    private OrderEntryMapper mapper;

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IConsignmentService consignmentService;

    @Autowired
    private IPointOfServiceExternalService iPointOfServiceExternalService;
    @Autowired
    private IMstUserService mstUserService;

    @Autowired
    private IProductService productService;
    @Autowired
    private IOrderCouponruleService orderCouponruleService;
    @Autowired
    private IOmPromotionruleService omPromotionruleService;
    @Autowired
    private ISequenceGenerateService sequenceGenerateService;
    @Autowired
    private IGlobalVariantService iGlobalVariantService;
    @Autowired
    private IMamSoApproveHisService iMamSoApproveHisService;

    @Autowired
    private OrderEntryMapper orderEntryMapper;
    @Autowired
    private IAlterService alterService;

    @Autowired
    private IPinService pinService;
    @Autowired
    private IMamVcodeHeaderService mamVcodeHeaderService;
    @Autowired
    private IUserService userService;
    @Autowired
    private PinAlterMapper pinAlterMapper;
    @Autowired
    private IImAtpInfaceService iImAtpInfaceService;

    private RestTemplate restTemplate = new RestTemplate();
    @Value("#{configProperties['baseUri']}")
    private String baseUri;

    @Value("#{configProperties['modelUri']}")
    private String modelUri;
    @Autowired
    private RestClient restClient;
    @Autowired
    private ConsignmentMapper consignmentMapper;

    /**
     * 推送retail行数据查询
     *
     * @param consignmentId
     * @return
     */
    @Override
    public List<OrderEntry> selectRetailData(Long consignmentId) {
        return mapper.selectRetailData(consignmentId);
    }

    /**
     * 订单行查询详情
     *
     * @param iRequest
     * @param dto
     * @param page
     * @param pagesize
     * @return
     */
    @Override
    public List<OrderEntry> queryInfo(IRequest iRequest, OrderEntry dto, int page, int pagesize) {
        PageHelper.startPage(page, pagesize);
        List<OrderEntry> list = mapper.queryInfo(dto);
        for (OrderEntry entry : list) {
            if (entry.getConsignmentId() != null) {
                Consignment con = new Consignment();
                con.setConsignmentId(entry.getConsignmentId());
                Consignment consignment = consignmentMapper.selectByPrimaryKey(con);
                entry.setConsignmentStatus(consignment.getStatus());
            }

        }
        return list;
    }

    /**
     * 订单行查询详情
     *
     * @param iRequest
     * @param dto
     * @return
     */
    @Override
    public List<OrderEntry> queryInfo(IRequest iRequest, OrderEntry dto) {
        return mapper.queryInfo(dto);
    }

    /**
     * 查询服务单关联的订单行
     *
     * @param dto
     * @return
     */
    @Override
    public List<OrderEntry> queryServiceOrderInfo(IRequest iRequest, OrderEntry dto, int page, int pagesize) {
        PageHelper.startPage(page, pagesize);
        return mapper.queryServiceOrderInfo(dto);
    }

    /**
     * 根据一个订单行ID集合查询对应数据
     *
     * @param iRequest
     * @param ids
     * @param page
     * @param pagesize
     * @return
     */
    @Override
    public List<OrderEntry> queryByIds(IRequest iRequest, List<Long> ids, int page, int pagesize) {
        PageHelper.startPage(page, pagesize);
        List<OrderEntry> resultList = new ArrayList<OrderEntry>();
        OrderEntry orderEntry = new OrderEntry();
        if (ids != null && ids.size() > 0) {
            for (Long orderEntryId : ids) {
                orderEntry.setOrderEntryId(orderEntryId);
                resultList.add(mapper.queryById(orderEntry));
            }
        }
        return resultList;
    }


    /**
     * 根据订单头id查询订单行
     *
     * @param orderId
     * @return
     */
    @Override
    public List<OrderEntry> selectByOrderId(Long orderId) {
        OrderEntry orderEntry = new OrderEntry();
        orderEntry.setOrderId(orderId);
        return mapper.select(orderEntry);
    }

    /**
     * 派工单添加功能中订单行详情查询
     *
     * @param iRequest
     * @param dto
     * @param page
     * @param pagesize
     * @return
     */
    @Override
    public List<OrderEntry> queryForDispatchOrder(IRequest iRequest, OrderEntry dto, int page, int pagesize) {
        PageHelper.startPage(page, pagesize);
        return mapper.queryInfo(dto);
    }

    /**
     * 查询未生成组件的套件行
     *
     * @param orderId 订单id
     * @return
     */
    @Override
    public List<OrderEntry> selectSuiteEntriesByOrderId(Long orderId) {
        return mapper.selectSuiteEntriesByOrderId(orderId);
    }

    /**
     * 根据订单获取下一个lineNumber的取值
     *
     * @param order 订单
     * @return
     */
    @Override
    public Long getNextLineNumber(Order order) {
        OrderEntry orderEntry = new OrderEntry();
        orderEntry.setOrderId(order.getOrderId());
        List<OrderEntry> orderEntryList = mapper.select(orderEntry);
        return getNextLineNumber(orderEntryList);
    }

    /**
     * 根据订单下的所有订单行确定下一个LineNumber的取值
     *
     * @param orderEntries 订单下所有订单行
     * @return
     */
    @Override
    public Long getNextLineNumber(List<OrderEntry> orderEntries) {
        if (CollectionUtils.isEmpty(orderEntries)) {
            return FIRST_LINE_NUMBER;
        } else {
            OrderEntry max = orderEntries.stream()
                    .peek(orderEntry -> Assert.notNull(orderEntry.getLineNumber(), "订单行[" + orderEntry.getOrderEntryId() + "]lineNumber不能为空"))
                    .max((orderEntry1, orderEntry2)
                            -> orderEntry1.getLineNumber().compareTo(orderEntry2.getLineNumber())).orElse(null);
            return max == null ? FIRST_LINE_NUMBER : max.getLineNumber() + LINE_NUMBER_STEP;
        }
    }

    /**
     * 根据当前的LineNumber获取下一个lineNumber
     *
     * @param currentLineNumber 当前LineNumber
     * @return
     */
    @Override
    public Long getNextLineNumber(Long currentLineNumber) {
        return currentLineNumber + LINE_NUMBER_STEP;
    }

    /**
     * 根据条件查询所有的订单行
     *
     * @param orderEntry 条件
     * @return
     */
    @Override
    public List<OrderEntry> select(OrderEntry orderEntry) {
        return mapper.select(orderEntry);
    }

    /**
     * 根据Example对象查询
     *
     * @param example 查询条件
     * @return
     */
    @Override
    public List<OrderEntry> selectByExample(Example example) {
        return mapper.selectByExample(example);
    }

    /**
     * 取消订单行时判断该订单下的所有订单行是否都取消了
     *
     * @param iRequest
     * @param orderEntries
     * @return
     */
    @Override
    public List<OrderEntry> isAllCanceled(IRequest iRequest, List<OrderEntry> orderEntries) {
        List<OrderEntry> list = null;
        if (CollectionUtils.isNotEmpty(orderEntries)) {
            //校验数据
            //校验订单是否已锁定和商城订单的cancelRefundUncreate字段是否为"Y"
            Order o = new Order();
            o.setOrderId(orderEntries.get(0).getOrderId());
            Order order = orderService.selectByPrimaryKey(iRequest, o);
            if (order.getLocked() != null && "Y".equals(order.getLocked())) {
                throw new RuntimeException("本订单已锁定，不能取消订单行！");
            }
            if (order.getWebsiteId() != null && "1".equals(order.getWebsiteId()) && order.getCancelRefundUncreate() != null && "Y".equals(order.getCancelRefundUncreate())) {
                throw new RuntimeException("本订单包含取消未生成退款订单行！");
            }
            //校验订单行的发货单状态以及notReturnQuantity字段是否都为0
            for (OrderEntry oe : orderEntries) {
                OrderEntry e = new OrderEntry();
                e.setOrderEntryId(oe.getOrderEntryId());
                OrderEntry orderEntry = mapper.selectByPrimaryKey(e);
                if (orderEntry.getConsignmentId() != null) {
                    Consignment c = new Consignment();
                    c.setConsignmentId(orderEntry.getConsignmentId());
                    Consignment consignment = consignmentService.selectByPrimaryKey(iRequest, c);
                    if (consignment.getStatus() != null && !Constants.CONSIGNMENT_STATUS_ABNORMAL.equals(consignment.getStatus()) && !Constants.CON_STATUS_WAIT_FOR_DELIVERY.equals(consignment.getStatus())) {
                        throw new RuntimeException("订单行id" + orderEntry.getOrderEntryId() + "的发货单状态不符合条件！");
                    }
                }
                if (orderEntry.getNotReturnQuantity() != null && orderEntry.getNotReturnQuantity() != 0) {
                    throw new RuntimeException("订单行id" + orderEntry.getOrderEntryId() + "的未生成退款单数量不为0！");
                }
            }

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("orderId", orderEntries.get(0).getOrderId());

            List<Long> orderEntryIdList = new ArrayList<>();
            for (OrderEntry orderEntry : orderEntries) {
                orderEntryIdList.add(orderEntry.getOrderEntryId());
            }
            paramMap.put("orderEntryIdList", orderEntryIdList);
            list = mapper.isAllCanceled(paramMap);
        }
        return list;
    }

    /**
     * 取消订单行以及取消其子订单行
     *
     * @param request
     * @param dto
     * @return
     */
    @Override
    public List<OrderEntry> cancelOrderEntry(IRequest request, List<OrderEntry> dto) {
        if (CollectionUtils.isNotEmpty(dto)) {
            for (OrderEntry orderEntry : dto) {
                //对数据进行二次验证，判断数据是否依然符合订单取消的规则
                //1.验证该订单是否未加锁
                Order od = new Order();
                od.setOrderId(orderEntry.getOrderId());
                Order order = orderService.selectByPrimaryKey(request, od);
                if (order != null && Constants.ORDER_IS_LOCKED.equals(order.getLocked())) {
                    //说明该订单已经锁定了，不符合取消条件了
                    throw new RuntimeException("该订单已经锁定，无法再取消!");
                }
                //2.验证发货单的状态是否已经改变
                if (orderEntry.getConsignmentId() != null) {
                    Consignment con = new Consignment();
                    con.setConsignmentId(orderEntry.getConsignmentId());
                    Consignment consignment = consignmentService.selectByPrimaryKey(request, con);
                    //若已生成发货单，且发货单状态不在“abnormal”和“wait_for_delivery”,则不满足取消条件
                    if (consignment != null && !Constants.CON_STATUS_WAIT_FOR_DELIVERY.equals(consignment.getStatus()) && !Constants.CONSIGNMENT_STATUS_ABNORMAL.equals(consignment.getStatus())) {
                        throw new RuntimeException("发货单" + consignment.getCode() + "的状态已不满足取消订单的条件!");
                    }
                }

                if (Constants.ORDER_ENTRY_STATUS_IS_SUIT_Y.equals(orderEntry.getIsSuit())) {
                    //若是套装
                    OrderEntry o = new OrderEntry();
                    o.setParentLine(orderEntry.getOrderEntryId());
                    List<OrderEntry> list = select(request, o, 1, Integer.MAX_VALUE);
                    if (CollectionUtils.isNotEmpty(list)) {
                        //取消所有子订单行
                        for (OrderEntry oEntry : list) {
                            OrderEntry entry = new OrderEntry();
                            entry.setOrderEntryId(oEntry.getOrderEntryId());
                            entry.setOrderEntryStatus(Constants.ORDER_ENTRY_STATUS_CANCEL);
                            addPinInfo(request, oEntry, "hmall", "MAP2700", "取消订单行");
                            mapper.updateByPrimaryKeySelective(entry);

                            //释放库存
                            if (oEntry.getPin() != null) {
                                String result = iImAtpInfaceService.releaseAll(oEntry.getPin(), "N");
                                if (!result.equals("")) {
                                    throw new RuntimeException("订单行编码" + oEntry.getCode() + "的pin码" + oEntry.getPin() + "释放库存失败!");
                                }
                            }
                            //增加书面记录
                            HmallSoChangeLog soChangeLog = new HmallSoChangeLog();
                            OrderEntry oInfo = orderEntryMapper.selectByPrimaryKey(o);
                            soChangeLog.setOrderId(oInfo.getOrderId());
                            soChangeLog.setOrderEntryId(oInfo.getOrderEntryId());
                            soChangeLog.setOrderType("1");
                            soChangeLog.setPin(oInfo.getPin());
                            soChangeLog.setProductId(oInfo.getProductId());
                            orderService.addSoChangeLog(soChangeLog);
                        }
                    }
                }
                //取消自身
                addPinInfo(request, orderEntry, "hmall", "MAP2700", "取消订单行");
                orderEntry.setOrderEntryStatus(Constants.ORDER_ENTRY_STATUS_CANCEL);
                mapper.updateByPrimaryKeySelective(orderEntry);

                //释放库存
                if (orderEntry.getPin() != null) {
                    String result = iImAtpInfaceService.releaseAll(orderEntry.getPin(), "N");
                    if (!result.equals("")) {
                        throw new RuntimeException("订单行编码" + orderEntry.getCode() + "的pin码" + orderEntry.getPin() + "释放库存失败!");
                    }
                }
                //增加书面记录
                HmallSoChangeLog soChangeLog = new HmallSoChangeLog();
                OrderEntry orderEntryInfo = orderEntryMapper.selectByPrimaryKey(orderEntry);
                soChangeLog.setOrderId(orderEntryInfo.getOrderId());
                soChangeLog.setOrderEntryId(orderEntryInfo.getOrderEntryId());
                soChangeLog.setOrderType("1");
                soChangeLog.setPin(orderEntryInfo.getPin());
                soChangeLog.setProductId(orderEntryInfo.getProductId());
                orderService.addSoChangeLog(soChangeLog);
            }
            //将字段“取消未生成退款”【CANCEL_REFUND_UNCREATE】flag置为Y 2017-09-20
            Order odr = new Order();
            odr.setOrderId(dto.get(0).getOrderId());
            odr.setCancelRefundUncreate("Y");
            orderService.updateByPrimaryKeySelective(request, odr);
            //判断取消订单行后，对应的发货单里的订单行是否全部取消，若全部取消了，则对应的发货单
            for (OrderEntry orderEntry : dto) {
                if (orderEntry.getConsignmentId() != null) {
                    //先判断该发货单是否已经“TRADE_CLOSED”,若已经"TRADE_CLOSED",则无需再次"TRADE_CLOSED"该发货单
                    Consignment c = new Consignment();
                    c.setConsignmentId(orderEntry.getConsignmentId());
                    List<Consignment> consignmentList = consignmentService.select(c);
                    if (CollectionUtils.isNotEmpty(consignmentList)) {
                        if (Constants.TRADE_CLOSED.equals(consignmentList.get(0).getStatus())) {
                            continue;
                        }
                    }
                    OrderEntry o = new OrderEntry();
                    o.setConsignmentId(orderEntry.getConsignmentId());
                    List<OrderEntry> orderEntryList = mapper.select(o);
                    //判断该发货单下所有的订单行是否取消
                    if (CollectionUtils.isNotEmpty(orderEntryList)) {
                        int flag = 0;
                        for (OrderEntry entry : orderEntryList) {
                            if (!Constants.ORDER_ENTRY_STATUS_CANCEL.equals(entry.getOrderEntryStatus())) {
                                flag = 1;
                                break;
                            }
                        }
                        if (flag == 1) {
                            //不全为取消状态
                            continue;
                        } else {
                            //全为取消状态,则将该发货单的状态置为“TRADE_CLOSED”
                            c.setStatus(Constants.TRADE_CLOSED);
                            consignmentService.updateByPrimaryKeySelective(request, c);
                        }
                    }
                }
            }

        }
        return dto;
    }

    @Override
    public List<OrderEntry> cancelGiftEntry(IRequest request, List<OrderEntry> dto) {
        if (CollectionUtils.isNotEmpty(dto)) {
            for (OrderEntry orderEntry : dto) {
                if (orderEntry.getOrderEntryId() != null) {
                    //取消自身
                    addPinInfo(request, orderEntry, "hmall", "MAP2700", "取消订单行");
                    orderEntry.setOrderEntryStatus(Constants.ORDER_ENTRY_STATUS_CANCEL);
                    orderEntryMapper.updateByPrimaryKeySelective(orderEntry);
                    if (Constants.ORDER_ENTRY_STATUS_IS_SUIT_Y.equals(orderEntry.getIsSuit())) {
                        //若是套装
                        OrderEntry o = new OrderEntry();
                        o.setParentLine(orderEntry.getOrderEntryId());
                        List<OrderEntry> list = select(request, o, 1, Integer.MAX_VALUE);
                        if (CollectionUtils.isNotEmpty(list)) {
                            //取消所有子订单行
                            for (OrderEntry oEntry : list) {
                                OrderEntry entry = new OrderEntry();
                                entry.setOrderEntryId(oEntry.getOrderEntryId());
                                entry.setOrderEntryStatus(Constants.ORDER_ENTRY_STATUS_CANCEL);
                                addPinInfo(request, oEntry, "hmall", "MAP2700", "取消订单行");
                                mapper.updateByPrimaryKeySelective(entry);

                            }
                        }
                    }
                }

            }
            logger.info("*************************取消赠品同步商城*****************************");
            logger.info("" + dto.get(0).getOrderId());
            //取消赠品之后将订单信息同步商城
            orderService.orderSyncZmall(dto.get(0).getOrderId());
            logger.info("*************************取消赠品同步结束*****************************");
        }
        return null;
    }

    @Override
    public List<OrderEntry> addOrderEntry(IRequest request, List<OrderEntry> dto) {
        if (CollectionUtils.isNotEmpty(dto)) {
            for (OrderEntry orderEntry : dto) {
                orderEntry.setCode(sequenceGenerateService.getNextOrderEntryCode());
                orderEntry.setLineNumber(mapper.getMaxLinenumberByOrderId(orderEntry.getOrderId()) == null ? 10L : mapper.getMaxLinenumberByOrderId(orderEntry.getOrderId()) + LINE_NUMBER_STEP);
                orderEntry.setPin(sequenceGenerateService.getNextPin());
                orderEntry.setEscLineNumber(mapper.getMaxEscLinenumberByOrderId(orderEntry.getOrderId()) == null ? "10" : String.valueOf(mapper.getMaxEscLinenumberByOrderId(orderEntry.getOrderId()) + LINE_NUMBER_STEP));

                mapper.insertSelective(orderEntry);
                addPinInfo(request, orderEntry, "hmall", "MAP0100", "订单已生成");
            }
        }
        return dto;
    }

    /**
     * 对于天猫订单，取消订单行以及取消其子订单行
     *
     * @param request
     * @param dto
     * @return
     */
    @Override
    public List<OrderEntry> cancelOrderEntryForTm(IRequest request, List<OrderEntry> dto) {
        if (CollectionUtils.isNotEmpty(dto)) {
            Double allTotalFee = 0D;
            for (OrderEntry orderEntry : dto) {
                if (orderEntry.getTotalFee() != null) {
                    allTotalFee = allTotalFee + orderEntry.getTotalFee();
                }
                if (Constants.ORDER_ENTRY_STATUS_IS_SUIT_Y.equals(orderEntry.getIsSuit())) {
                    //若是套装
                    OrderEntry o = new OrderEntry();
                    o.setParentLine(orderEntry.getOrderEntryId());
                    List<OrderEntry> list = select(request, o, 1, Integer.MAX_VALUE);
                    if (CollectionUtils.isNotEmpty(list)) {
                        //取消所有子订单行
                        for (OrderEntry oEntry : list) {
                            OrderEntry entry = new OrderEntry();
                            entry.setOrderEntryId(oEntry.getOrderEntryId());
                            entry.setOrderEntryStatus(Constants.ORDER_ENTRY_STATUS_CANCEL);
                            mapper.updateByPrimaryKeySelective(entry);

                            //释放库存
                            if (oEntry.getPin() != null) {
                                String result = iImAtpInfaceService.releaseAll(oEntry.getPin(), "N");
                                if (!result.equals("")) {
                                    throw new RuntimeException("订单行编码" + oEntry.getCode() + "的pin码" + oEntry.getPin() + "释放库存失败!");
                                }
                            }
                            //增加书面记录
                            HmallSoChangeLog soChangeLog = new HmallSoChangeLog();
                            OrderEntry oInfo = orderEntryMapper.selectByPrimaryKey(oEntry);
                            soChangeLog.setOrderId(oInfo.getOrderId());
                            soChangeLog.setOrderEntryId(oInfo.getOrderEntryId());
                            soChangeLog.setOrderType("1");
                            soChangeLog.setPin(oInfo.getPin());
                            soChangeLog.setProductId(oInfo.getProductId());
                            orderService.addSoChangeLog(soChangeLog);
                        }
                    }
                }
                //取消自身
                orderEntry.setOrderEntryStatus(Constants.ORDER_ENTRY_STATUS_CANCEL);
                mapper.updateByPrimaryKeySelective(orderEntry);
                //释放库存
                if (orderEntry.getPin() != null) {
                    String result = iImAtpInfaceService.releaseAll(orderEntry.getPin(), "N");
                    if (!result.equals("")) {
                        throw new RuntimeException("订单行编码" + orderEntry.getCode() + "的pin码" + orderEntry.getPin() + "释放库存失败!");
                    }
                }
                //增加书面记录
                HmallSoChangeLog soChangeLog = new HmallSoChangeLog();
                OrderEntry orderEntryInfo = orderEntryMapper.selectByPrimaryKey(orderEntry);
                soChangeLog.setOrderId(orderEntryInfo.getOrderId());
                soChangeLog.setOrderEntryId(orderEntryInfo.getOrderEntryId());
                soChangeLog.setOrderType("1");
                soChangeLog.setPin(orderEntryInfo.getPin());
                soChangeLog.setProductId(orderEntryInfo.getProductId());
                orderService.addSoChangeLog(soChangeLog);
            }
            //判断取消订单行后，对应的发货单里的订单行是否全部取消，若全部取消了，则对应的发货单状态置为“TRADE_CLOSED”
            for (OrderEntry orderEntry : dto) {
                if (orderEntry.getConsignmentId() != null) {
                    //先判断该发货单是否已经“TRADE_CLOSED”,若已经"TRADE_CLOSED",则无需再次"TRADE_CLOSED"该发货单
                    Consignment c = new Consignment();
                    c.setConsignmentId(orderEntry.getConsignmentId());
                    Consignment consignment = consignmentService.selectByPrimaryKey(request, c);
                    if (consignment != null) {
                        if (Constants.TRADE_CLOSED.equals(consignment.getStatus())) {
                            continue;
                        }
                    }
                    OrderEntry o = new OrderEntry();
                    o.setConsignmentId(orderEntry.getConsignmentId());
                    List<OrderEntry> orderEntryList = mapper.select(o);
                    //判断该发货单下所有的订单行是否取消
                    if (CollectionUtils.isNotEmpty(orderEntryList)) {
                        int flag = 0;
                        for (OrderEntry entry : orderEntryList) {
                            if (!Constants.ORDER_ENTRY_STATUS_CANCEL.equals(entry.getOrderEntryStatus())) {
                                flag = 1;
                                break;
                            }
                        }
                        if (flag == 0) {
                            //全为取消状态,则将该发货单的状态置为“TRADE_CLOSED”
                            c.setStatus(Constants.TRADE_CLOSED);
                            consignmentService.updateByPrimaryKeySelective(request, c);
                        } else {
                            //不全为取消状态
                            continue;
                        }
                    }
                }
            }
            //更新订单头字段【ORDER_AMOUNT】，等于order_amount减去当前取消行的total_fee
            Order o = new Order();
            o.setOrderId(dto.get(0).getOrderId());
            Order order = orderService.selectByPrimaryKey(request, o);
            if (order != null) {
                DecimalFormat df = new DecimalFormat("0.00");
                if (order.getOrderAmount() != null) {
                    BigDecimal ordrAmount = order.getOrderAmount().subtract(new BigDecimal(df.format(allTotalFee)));
                    order.setOrderAmount(ordrAmount);
                    orderService.updateByPrimaryKeySelective(request, order);
                }

                //订单下所有订单行关联的发货单同步RETAIL状态置为N
                Consignment con = new Consignment();
                con.setOrderId(order.getOrderId());
                List<Consignment> consignmentList = consignmentService.select(con);
                if (CollectionUtils.isNotEmpty(consignmentList)) {
                    for (Consignment consignment : consignmentList) {
                        consignment.setSyncflag("N");
                        consignmentService.updateByPrimaryKeySelective(request, consignment);
                    }
                }
            }

        }
        return dto;
    }

    /**
     * 更新订单行退货状态  将接口返回的orderAmount更新到订单的current_amount字段
     *
     * @param request
     * @param dto
     * @param currentAmount
     * @return
     */
    @Override
    public ResponseData confirmReturnGoods(IRequest request, List<OrderEntry> dto, Double currentAmount, String chosenCoupon, String chosenPromotion, String websiteId) {
        ResponseData rd = new ResponseData();
        try {
            if (CollectionUtils.isNotEmpty(dto)) {
                long fristConsignmentId = dto.get(0).getConsignmentId();
                ResponseData responseData = new ResponseData();
                responseData.setSuccess(false);
                //订单行状态校验
                for (OrderEntry orderEntry : dto) {
                    OrderEntry checkOrderEntry = selectByPrimaryKey(request, orderEntry);

                    if (orderEntry.getUnReturnedQuantity() == null || orderEntry.getUnReturnedQuantity() == 0) {
                        responseData.setMessage("请填写待退货数量");
                        return responseData;
                    }

                    if (orderEntry.getUnReturnedQuantity() < 0) {
                        responseData.setMessage("退货数量不能小于0");
                        return responseData;
                    }
                    if (checkOrderEntry != null) {
                        if (orderEntry.getUnReturnedQuantity() > (checkOrderEntry.getQuantity() - checkOrderEntry.getReturnedQuantity())) {
                            responseData.setMessage("退货数量不能大于可退货数量");
                            return responseData;
                        }

                        if (checkOrderEntry.getNotReturnQuantity() != null && checkOrderEntry.getNotReturnQuantity() != 0) {
                            responseData.setMessage("未生成退货单数量不为0,请先生成退货单");
                            return responseData;
                        }

                        if (!Constants.ORDER_ENTRY_STATUS_NORMAL.equals(checkOrderEntry.getOrderEntryStatus()) || checkOrderEntry.getParentLine() != null) {
                            responseData.setMessage("请选择可以退货的订单行退货");
                            return responseData;
                        }

                        if (checkOrderEntry.getConsignmentId() == null) {
                            responseData.setMessage("尚未创建发货单");
                            return responseData;
                        }

                        if ((long) fristConsignmentId != (long) checkOrderEntry.getConsignmentId()) {
                            responseData.setMessage("请选择同一发货单下的订单行进行退货");
                            return responseData;
                        }

                        if (orderEntryMapper.selectByConsignmentId(checkOrderEntry).size() > 0) {
                            responseData.setMessage("存在未发货商品，不能退货");
                            return responseData;
                        }
                    }
                }
                //根据orderId更新订单当前需要付款字段 优惠券 促销
                Order order = new Order();
                order.setOrderId(dto.get(0).getOrderId());
                //天猫订单不进行促销信息操作
                if (Constants.ONE.equals(websiteId)) {
                    order.setCurrentAmount(currentAmount);
                    order.setChosenCoupon(chosenCoupon);
                    order.setChosenPromotion(chosenPromotion);
                }
                order.setTradeFinishTime(new Date());
                //更新订单表
                orderService.updateByPrimaryKeySelective(request, order);
                AmountChangeLog changeLog = null;
                int addAmountFlag = 0;
                for (OrderEntry orderEntry : dto) {
                    if (addAmountFlag == 0) {
                        //增加金额变更记录头
                        order = orderService.selectByPrimaryKey(request, order);
                        if (order != null) {
                            order.setPromotionResult(orderEntry.getPromotionResult());
                            if (order.getPromotionResult() != null) {
                                changeLog = orderService.addAmountChangeLog(order);
                            }
                        }
                    }
                    addAmountFlag += 1;
                    //退货自身
                    orderEntryMapper.updateOrderEntryReturnedQuantity(orderEntry);

                    //修改组件退货数量
                    OrderEntry sonOrderEntry = new OrderEntry();
                    sonOrderEntry.setParentLine(orderEntry.getOrderEntryId());
                    sonOrderEntry.setOrderEntryStatus(orderEntry.getOrderEntryStatus());
                    sonOrderEntry.setUnReturnedQuantity(orderEntry.getUnReturnedQuantity());
                    orderEntryMapper.updateSonOrderEntryReturnedQuantity(sonOrderEntry);

                    //added by yuxiaoli@2017/11/04
                    //点击“确认退货”按钮后，如果退货行对应的发货单头表的“确认收货时间”【consignment.trade_finished_time】值为空，则将系统当前时间写入该字段中,并将syncflag置为N
                    if (orderEntry.getConsignmentId() != null) {
                        Consignment c = new Consignment();
                        c.setConsignmentId(orderEntry.getConsignmentId());
                        Consignment consignment = consignmentService.selectByPrimaryKey(request, c);
                        if (consignment.getTradeFinishedTime() == null) {
                            consignment.setTradeFinishedTime(new Date());
                            consignment.setSyncflag("N");
                            consignmentService.updateByPrimaryKeySelective(request, consignment);
                        }
                    }

                    //天猫订单不进行促销信息操作
                    if (Constants.ONE.equals(websiteId)) {
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
                                        if (!StringUtils.isEmpty(omPromotionrule.getPageShowMes())) {
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
                                                                }

                List<OrderEntry> orderEntryList = this.selectByOrderId(order.getOrderId());
                if (CollectionUtils.isNotEmpty(orderEntryList)) {
                    for (OrderEntry orderEntry1 : orderEntryList) {
                        if (changeLog != null) {
                            //增加金额变更记录行
                            orderService.addAmountChangeLogEntry(changeLog, order, orderEntry1);
                        }
                    }
                }
                //更新订单表
                order.setRefundAmount(orderService.getTotalRefundAmount(request, order).getRefundAmount());
                orderService.updateByPrimaryKeySelective(request, order);
            }
            logger.info("*************************退货同步商城*****************************");
            logger.info("" + dto.get(0).getOrderId());
            //退货之后将订单信息同步商城
            orderService.orderSyncZmall(dto.get(0).getOrderId());
            logger.info("*************************退货同步结束*****************************");
            rd.setMessage("退货同步过程结束");
            rd.setRows(dto);
            rd.setSuccess(true);
            return rd;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("系统错误");
        }
    }


    /**
     * 对于天猫订单，订单取消时，对于全部订单行的取消（将所有订单行均置为取消状态），则订单头状态更新为"TRADE_CLOSED"
     *
     * @param request
     * @param dto
     * @return
     */
    @Override
    public List<OrderEntry> cancelAllOrderAndRntry(IRequest request, List<OrderEntry> dto) {
        if (CollectionUtils.isNotEmpty(dto)) {
            //将订单头状态更新为"TRADE_CLOSED"
            Order orderInfo = new Order();
            orderInfo.setOrderId(dto.get(0).getOrderId());
            Order order = orderService.selectInfoByOrderId(orderInfo).get(0);
            order.setCurrentAmount(Double.valueOf(0));
            order.setOrderStatus(Constants.TRADE_CLOSED);
            order.setOrderAmount(new BigDecimal(0));
            order.setPostFee(new BigDecimal(0));
            order.setEpostFee(new BigDecimal(0));

            order.setFixFee(new BigDecimal(0));
            order.setPrePostfee(new BigDecimal(0));
            order.setPreEpostfee(new BigDecimal(0));
            order.setPreFixfee(new BigDecimal(0));
            order.setTotalDiscount(new BigDecimal(0));
            order.setDiscountFee(new BigDecimal(0));
            order.setCouponFee(new BigDecimal(0));


            Map<String, Object> map = new HashMap<>();
            if (order.getChosenCoupon() != null && !order.getChosenCoupon().equals("")) {
                map.put("customerId", order.getCustomerid());
                map.put("couponId", order.getChosenCoupon());
                map.put("operation", "2");
                //释放优惠券
                String url = "hmall-drools-service/coupon/operate/operateCustomerCoupon";
                HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, null);
                try {
                    com.hand.hmall.dto.ResponseData responseData = restTemplate.exchange(baseUri + url, HttpMethod.POST, entity, com.hand.hmall.dto.ResponseData.class).getBody();

                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
                OrderCouponrule orderCouponrule = new OrderCouponrule();
                orderCouponrule.setOrderId(order.getOrderId());
                orderCouponrule.setCouponId(order.getChosenCoupon());
                orderCouponruleService.deleteOrderCouponruleByOrderId(orderCouponrule);

                OmPromotionrule omPromotionrule = new OmPromotionrule();
                omPromotionrule.setOrderId(order.getOrderId());
                omPromotionrule.setActivityId(order.getChosenPromotion());
                omPromotionruleService.deleteOmPromotionruleByOrderId(omPromotionrule);

            }
            order.setChosenCoupon("");
            order.setChosenPromotion("");
            order.setCancelRefundUncreate("Y");
            //更新订单同步官网状态为N
            order.setSyncZmall("N");
            orderService.updateByPrimaryKeySelective(request, order);
            order.setRefundAmount(orderService.getTotalRefundAmount(request, order).getRefundAmount());
            orderService.updateByPrimaryKeySelective(request, order);
            //更新订单行状态
            for (OrderEntry orderEntry : dto) {
                //若该行为套件行，则同时取消它的组件行
                if (Constants.ORDER_ENTRY_STATUS_IS_SUIT_Y.equals(orderEntry.getIsSuit())) {
                    OrderEntry entry = new OrderEntry();
                    entry.setParentLine(orderEntry.getOrderEntryId());
                    List<OrderEntry> list = mapper.select(entry);
                    if (CollectionUtils.isNotEmpty(list)) {
                        //取消所有的组件订单行
                        for (OrderEntry o : list) {
                            o.setOrderEntryStatus(Constants.ORDER_ENTRY_STATUS_CANCEL);
                            addPinInfo(request, o, "hmall", "MAP2700", "取消订单行");

                            mapper.updateByPrimaryKeySelective(o);

                            //释放库存
                            if (o.getPin() != null) {
                                String result = iImAtpInfaceService.releaseAll(o.getPin(), "N");
                                if (!result.equals("")) {
                                    throw new RuntimeException("订单行编码" + o.getCode() + "的pin码" + o.getPin() + "释放库存失败!");
                                }
                            }
                            //增加书面记录
                            HmallSoChangeLog soChangeLog = new HmallSoChangeLog();
                            OrderEntry oInfo = orderEntryMapper.selectByPrimaryKey(o);
                            soChangeLog.setOrderId(oInfo.getOrderId());
                            soChangeLog.setOrderEntryId(oInfo.getOrderEntryId());
                            soChangeLog.setOrderType("1");
                            soChangeLog.setPin(oInfo.getPin());
                            soChangeLog.setProductId(oInfo.getProductId());
                            orderService.addSoChangeLog(soChangeLog);
                        }
                    }
                }
                //取消自身
                orderEntry.setOrderEntryStatus(Constants.ORDER_ENTRY_STATUS_CANCEL);
                addPinInfo(request, orderEntry, "hmall", "MAP2700", "取消订单行");
                updateByPrimaryKeySelective(request, orderEntry);

                //释放库存
                if (orderEntry.getPin() != null) {
                    String result = iImAtpInfaceService.releaseAll(orderEntry.getPin(), "N");
                    if (!result.equals("")) {
                        throw new RuntimeException("订单行编码" + orderEntry.getCode() + "的pin码" + orderEntry.getPin() + "释放库存失败!");
                    }
                }
                //增加书面记录
                HmallSoChangeLog soChangeLog = new HmallSoChangeLog();
                OrderEntry orderEntryInfo = orderEntryMapper.selectByPrimaryKey(orderEntry);
                soChangeLog.setOrderId(orderEntryInfo.getOrderId());
                soChangeLog.setOrderEntryId(orderEntryInfo.getOrderEntryId());
                soChangeLog.setOrderType("1");
                soChangeLog.setPin(orderEntryInfo.getPin());
                soChangeLog.setProductId(orderEntryInfo.getProductId());
                orderService.addSoChangeLog(soChangeLog);
            }

            //若该订单已生成发货单，那么发货单的状态也置成"TRADE_CLOSED"
            //若订单已生成发货单，发货单状态处于（"ABNORMAL"or"WAIT_FOR_DELIVERY"），该订单下的所有发货单状态均更改为‘TRADE_CLOSED’。//2017-9-20
            Consignment c = new Consignment();
            c.setOrderId(dto.get(0).getOrderId());
            List<Consignment> consignmentList = consignmentService.select(c);
            if (CollectionUtils.isNotEmpty(consignmentList)) {
                for (Consignment consignment : consignmentList) {
                    if (consignment.getStatus().equals("ABNORMAL") || consignment.getStatus().equals("WAIT_FOR_DELIVERY")) {
                        consignment.setStatus(Constants.TRADE_CLOSED);
                        //订单下关联的发货单同步RETAIL以及同步商城状态置为N
                        consignment.setSyncflag("N");
                        consignment.setSyncZmall("N");
                        consignmentService.updateByPrimaryKeySelective(request, consignment);
                    }
                }
            }
        }
        if (!ObjectUtils.isEmpty(dto.get(0).getOrderId())) {
            logger.info("*********************订单行全部取消开始同步**************************");
            orderService.orderSyncZmall(dto.get(0).getOrderId());
            logger.info("*********************订单行全部取消同步结束**************************");
        }
        return dto;
    }

    private void addPinInfo(IRequest request, OrderEntry orderEntry, String system, String eventCode, String eventInfo) {
        //PIN码信息表增加节点信息
        Pin pin = new Pin();
        pin.setCode(orderEntry.getPin());
        pin.setEntryCode(orderEntry.getCode());
        pin.setEventCode(eventCode);
        pin.setSystem(system);
        User user = new User();
        user.setUserId(request.getUserId());
        User userInfo = userService.selectByPrimaryKey(request, user);
        if (userInfo != null) {
            pin.setOperator(userInfo.getUserName());
            if (userInfo.getPhone() != null) {
                pin.setMobile(userInfo.getPhone());
            } else {
                pin.setMobile("");
            }
        }
        pin.setOperationTime(new Date());
        pin.setEventInfo(eventInfo);
        PinAlter pinAlter = new PinAlter();
        pinAlter.setEventCode(eventCode);

        List<PinAlter> pinAlterList = pinAlterMapper.select(pinAlter);
        if (pinAlterList != null && pinAlterList.size() > 0) {
            pin.setEventDes(pinAlterList.get(0).getEventDes());
        }

        pinService.insertSelective(request, pin);
    }


    /**
     * 订单同步时根据订单ID查询对应的订单行
     *
     * @param orderId 订单id
     * @return
     */
    @Override
    public List<OrderEntryDto> selectOrderSyncByOrderId(Long orderId) {
        List<OrderEntryDto> resultList = mapper.selectOrderSyncByOrderId(orderId);
        return resultList;
    }

    /**
     * 订单拆分
     * <p>
     * 订单行拆分时，原本的订单行根据前台输入的拆分数量拆分为两行，新增一行，原行修改各种费用和商品数量
     * 新增的订单行要新增Pin吗等信息，还要根据FS给出条件，判断新增的订单行需不需要推送工艺审核，
     *
     * @param iRequest
     * @param orderEntries 要拆分的订单行
     * @return
     */
    @Override
    public List<OrderEntry> spiltOrderEntry(IRequest iRequest, List<OrderEntry> orderEntries) {
        List<OrderEntry> orderEntryList = new ArrayList();
        Order order = new Order();
        Consignment consignment = new Consignment();
        if (CollectionUtils.isNotEmpty(orderEntries)) {

            for (OrderEntry orderEntry : orderEntries) {
                //拆分新增的订单行
                OrderEntry insertEntry = spiltFunAdd(iRequest, orderEntry);
                addPinInfoForSpildOrder(insertEntry);
                OrderEntry insertResult = isNeedBomApproved(insertEntry);
                Assert.notNull(insertResult, "订单拆分新增订单行过程中出现错误");

                orderEntryList.add(insertResult);
                //拆分修改的订单行
                OrderEntry updateEntry = spiltFunUpdate(iRequest, orderEntry, insertResult);
                orderEntry.setSpiltOutEntry(insertResult);
                orderEntryList.add(updateEntry);

                //判断商品是不是套件
                Long productId = orderEntry.getProductId();
                Product product = iProductService.selectByProductId(productId);
                //如果商品是套件商品,对组件进行拆分
                if (product.getIsSuit() != null && (Constants.YES).equals(product.getIsSuit())) { // if (Constants.YES.equals(product.getIsSuit())) {
                    List<OrderEntry> orderEntries1 = mapper.getSuitOrderEntries(orderEntry.getOrderEntryId());
                    for (OrderEntry orderEntry1 : orderEntries1) {
                        OrderEntry insertEntry1 = spiltFunAddSuit(iRequest, orderEntry1, orderEntry, insertEntry);
                        addPinInfoForSpildOrder(insertEntry1);
                        OrderEntry insertResult1 = isNeedBomApproved(insertEntry1);
                        orderEntryList.add(insertResult1);
                        //拆分修改的订单行
                        OrderEntry updateEntry1 = spiltFunUpdateSuit(iRequest, orderEntry1, orderEntry, insertResult1);
                        orderEntry1.setSpiltOutEntry(insertResult1);
                        orderEntryList.add(updateEntry1);
                    }
                }
                //处理成功后，处理标识字段
                if (orderEntry.getConsignmentId() != null) {
                    consignment.setConsignmentId(orderEntry.getConsignmentId());
                    consignment.setSyncZmall("N"); // 更新订单同步官网状态为N
                    consignmentService.updateByPrimaryKeySelective(iRequest, consignment);
                }
            }
            order.setOrderId(orderEntries.get(0).getOrderId());
            order.setSyncZmall("N");
            orderService.updateByPrimaryKeySelective(iRequest, order);
        }
        return orderEntryList;
    }

    /**
     * 判断是否需要推送工艺审核
     *
     * @param orderEntry 待判断是否需要推送工艺审核的订单
     * @return
     */
    private OrderEntry isNeedBomApproved(OrderEntry orderEntry) {

        Boolean flag = null;
        Product product = iProductService.selectByProductId(orderEntry.getProductId());

        if (orderEntry.getConsignmentId() != null && ("N").equals(orderEntry.getBomApproved())) {
            if ((orderEntry.getParentLine() == null && ("Y").equals(product.getIsSuit())) || orderEntry.getVproductCode() == null) {
                flag = false;
            } else {
                flag = true;
            }
        }

        if (flag == null) {
            return this.insertSelective(RequestHelper.newEmptyRequest(), orderEntry);
        } else if (flag == true) {
            return this.bomApprove(orderEntry);
        } else if (flag == false) {
            orderEntry.setBomApproved("Y");
            return this.insertSelective(RequestHelper.newEmptyRequest(), orderEntry);
        }
        return null;
    }

    /**
     * 后台校验订单是否满足拆分条件
     *
     * @param orderEntries 要拆分的订单行
     * @return
     */
    @Override
    public String checkSuitableSplitOrderEntries(List<OrderEntry> orderEntries) {
        String message = null;
        for (OrderEntry orderEntry : orderEntries) {
            OrderEntry oe = mapper.checkSplitOrderEntry(orderEntry.getOrderEntryId());
            if (oe == null || oe.getParentLine() != null) {
                message = "数据已被修改,订单行不存在或是组件行,请刷新重试";
                return message;
            }
            if (("Y").equals(oe.getLocked())) {
                message = "数据已被修改,订单已锁定,请刷新重试";
                return message;
            }
            //订单拆分 fs 订单行上的BOM_APPROVED为Y条件取消
            if (oe.getQuantity() != orderEntry.getQuantity()) {
                message = "数据已被修改,订单行商品数量和数据库不一致,请刷新重试";
                return message;
            }
            if (oe.getConsignmentId() != null && !("ABNORMAL").equals(oe.getStatus()) && !("WAIT_FOR_DELIVERY").equals(oe.getStatus())) {
                message = "数据已被修改,订单行生成的发货单状态不满足,请刷新重试";
                return message;
            }
            if (message != null)
                message += message;
        }
        return message;
    }

    /**
     * 查询可拆分的订单行
     *
     * @param iRequest
     * @param orderId
     * @param page
     * @param pagesize
     * @return
     */
    @Override
    public List<OrderEntry> getSuitableSplitEntries(IRequest iRequest, Long orderId, int page, int pagesize) {
        PageHelper.startPage(page, pagesize);
        return mapper.getSuitableSplitEntries(orderId);
    }

    /**
     * @param iRequest
     * @param orderEntry
     * @return
     * @Descrition: 订单拆分中原有的订单行(套件中的组件行)不需要拆分运费等
     */
    private OrderEntry spiltFunUpdateSuit(IRequest iRequest, OrderEntry orderEntry1, OrderEntry orderEntry, OrderEntry insertEntry1) {
        OrderEntry oe = this.selectByPrimaryKey(iRequest, orderEntry1);
        oe.setQuantity(oe.getQuantity() - oe.getQuantity() * Integer.parseInt(orderEntry.getSpiltQuantity()) / orderEntry.getQuantity());

        oe = this.dataUpdateSpild(oe, insertEntry1);
        return this.updateByPrimaryKeySelective(iRequest, oe);
    }

    /**
     * @param iRequest
     * @param orderEntry
     * @return
     * @Descrition: 订单拆分中组件新增的订单行
     */
    private OrderEntry spiltFunAddSuit(IRequest iRequest, OrderEntry orderEntry1, OrderEntry orderEntry, OrderEntry insertEntry) {
        OrderEntry oe = this.selectByPrimaryKey(iRequest, orderEntry1);
        oe.setQuantity(oe.getQuantity() / orderEntry.getQuantity() * Integer.parseInt(orderEntry.getSpiltQuantity()));
        oe.setOrderEntryId(null);
        oe.setParentLine(insertEntry.getOrderEntryId());
        oe = this.dataAddSpilt(oe, orderEntry);
        //设置pin码 订单行编码 订单行号 平台订单行号
        oe.setPin(sequenceGenerateService.getNextPin());
        oe.setCode(sequenceGenerateService.getNextOrderEntryCode());
        oe.setLineNumber(mapper.getMaxLinenumberByOrderId(oe.getOrderId()) == null ? 10L : mapper.getMaxLinenumberByOrderId(oe.getOrderId()) + LINE_NUMBER_STEP);
        oe.setEscLineNumber(null); // 拆分后的组件行上平台订单行号留空
        return oe;
    }

    /**
     * @param iRequest
     * @param orderEntry
     * @return
     * @Descrition: 订单拆分中原有的订单行
     */
    private OrderEntry spiltFunUpdate(IRequest iRequest, OrderEntry orderEntry, OrderEntry insertEntry) {
        OrderEntry oe = this.selectByPrimaryKey(iRequest, orderEntry);
        oe.setQuantity(orderEntry.getQuantity() - Integer.parseInt(orderEntry.getSpiltQuantity()));

        oe = dataUpdateSpild(oe, insertEntry);
        return this.updateByPrimaryKeySelective(iRequest, oe);
    }

    /**
     * @param iRequest
     * @param orderEntry 要拆分的订单行
     * @return
     * @Descrition: 订单拆分中新增的订单行
     */
    private OrderEntry spiltFunAdd(IRequest iRequest, OrderEntry orderEntry) {
        OrderEntry oe = this.selectByPrimaryKey(iRequest, orderEntry);
        oe.setQuantity(Integer.parseInt(orderEntry.getSpiltQuantity()));
        oe.setOrderEntryId(null);
        oe = dataAddSpilt(oe, orderEntry);
        oe.setPin(sequenceGenerateService.getNextPin());
        // 按规则生成新订单行'编码'、'订单行号（按10/20/30序号规则生成）'和'平台订单行号（按10/20/30序号规则生成）'
        oe.setCode(sequenceGenerateService.getNextOrderEntryCode());
        oe.setLineNumber(mapper.getMaxLinenumberByOrderId(oe.getOrderId()) == null ? 10L : mapper.getMaxLinenumberByOrderId(oe.getOrderId()) + LINE_NUMBER_STEP);
        oe.setEscLineNumber(mapper.getMaxEscLinenumberByOrderId(oe.getOrderId()) == null ? "10" : String.valueOf(mapper.getMaxEscLinenumberByOrderId(oe.getOrderId()) + LINE_NUMBER_STEP));
        return oe;
    }

    /**
     * 订单数据拆分,订单行中的各种费用的拆分
     *
     * @param oe
     * @param insertEntry
     * @return
     */
    private OrderEntry dataUpdateSpild(OrderEntry oe, OrderEntry insertEntry) {
        //原有订单行费用修改
        if (oe.getShippingFee() != null)
            oe.setShippingFee(formatSubtractFee(oe.getShippingFee(), insertEntry.getShippingFee()));
        if (oe.getInstallationFee() != null)
            oe.setInstallationFee(formatSubtractFee(oe.getInstallationFee(), insertEntry.getInstallationFee()));
        if (oe.getPreShippingfee() != null)
            oe.setPreShippingfee(formatSubtractFee(oe.getPreShippingfee(), insertEntry.getPreShippingfee()));
        if (oe.getPreInstallationfee() != null)
            oe.setPreInstallationfee(formatSubtractFee(oe.getPreInstallationfee(), insertEntry.getPreInstallationfee()));
        if (oe.getCouponFee() != null)
            oe.setCouponFee(formatSubtractFee(oe.getCouponFee(), insertEntry.getCouponFee()));
        if (oe.getTotalFee() != null)
            oe.setTotalFee(formatSubtractFee(oe.getTotalFee(), insertEntry.getTotalFee()));
        if (oe.getTotalDiscount() != null)
            oe.setTotalDiscount(formatSubtractFee(oe.getTotalDiscount(), insertEntry.getTotalDiscount()));
        if (oe.getDiscountFee() != null)
            oe.setDiscountFee(formatSubtractFee(oe.getDiscountFee(), insertEntry.getDiscountFee()));
        if (oe.getDiscountFeel() != null)
            oe.setDiscountFeel(formatSubtractFee(oe.getDiscountFeel(), insertEntry.getDiscountFeel()));
        return oe;
    }

    /**
     * 数据拆分，根据比例拆分
     *
     * @param oe         要拆分的订单行
     * @param orderEntry 获取拆分比例
     * @return
     */
    private OrderEntry dataAddSpilt(OrderEntry oe, OrderEntry orderEntry) {
        //运费拆分
        if (oe.getShippingFee() != null)
            oe.setShippingFee(divideFee(oe.getShippingFee(), orderEntry));
        if (oe.getInstallationFee() != null)
            oe.setInstallationFee(divideFee(oe.getInstallationFee(), orderEntry));
        if (oe.getPreShippingfee() != null)
            oe.setPreShippingfee(divideFee(oe.getPreShippingfee(), orderEntry));
        if (oe.getPreInstallationfee() != null)
            oe.setPreInstallationfee(divideFee(oe.getPreInstallationfee(), orderEntry));
        if (oe.getCouponFee() != null)
            oe.setCouponFee(divideFee(oe.getCouponFee(), orderEntry));
        if (oe.getTotalFee() != null)
            oe.setTotalFee(divideFee(oe.getTotalFee(), orderEntry));
        if (oe.getTotalDiscount() != null)
            oe.setTotalDiscount(divideFee(oe.getTotalDiscount(), orderEntry));
        if (oe.getDiscountFee() != null)
            oe.setDiscountFee(divideFee(oe.getDiscountFee(), orderEntry));
        if (oe.getDiscountFeel() != null)
            oe.setDiscountFeel(divideFee(oe.getDiscountFeel(), orderEntry));
        return oe;
    }

    /**
     * @param fee1 原费用
     * @param fee2 新增拆分费用
     * @return
     * @Description:拆分中新增订单行的权重拆分费用
     */
    private Double formatSubtractFee(Double fee1, Double fee2) {
        DecimalFormat df = new DecimalFormat("####0.00");
        return Double.parseDouble(df.format(new BigDecimal(fee1).subtract(new BigDecimal(fee2)).doubleValue()));
    }

    /**
     * @param fee        要拆分的费用
     * @param orderEntry 要拆分的订单行
     * @return
     * @Description:拆分中新增订单行的权重拆分费用
     */
    private Double divideFee(Double fee, OrderEntry orderEntry) {
        DecimalFormat df = new DecimalFormat("####0.00");
        return Double.parseDouble(df.format(fee / orderEntry.getQuantity() * Integer.parseInt(orderEntry.getSpiltQuantity())));
    }

    /**
     * 订单拆分中的pin码信息新增
     *
     * @param orderEntry
     * @return
     */
    private Pin addPinInfoForSpildOrder(OrderEntry orderEntry) {
        Pin pin = new Pin();
        pin.setCode(orderEntry.getPin());
        pin.setEntryCode(orderEntry.getCode());
        pin.setEventCode("MAP0100");
        pin.setEventDes(alterService.selectByEventCode("MAP0100").getEventDes());
        pin.setSystem("hmall");
        pin.setOperator("system");
        pin.setOperationTime(DateUtil.getSpecialNowDate("yyyy/MM/dd HH:mm:ss"));
        return pinService.insertSelective(RequestHelper.newEmptyRequest(), pin);
    }

    /**
     * 批量插入订单行表数据
     *
     * @param iRequest
     * @param orderEntryList
     */
    @Override
    @Transactional
    public void batchInsertOrderEntry(IRequest iRequest, List<OrderEntry> orderEntryList) {
        mapper.batchInsertOrderEntry(orderEntryList);
    }

    /**
     * 订单取消时，查询非取消的normal状态的所有订单行 以便调用促销微服务查询促销信息
     *
     * @param request
     * @param dto
     * @return
     */
    @Override
    public List<OrderEntry> selectUnCancelOrderEntry(IRequest request, List<OrderEntry> dto) {
        Map<String, Object> map = new HashMap<>();
        List<Long> list = new ArrayList<>();

        map.put("orderId", dto.get(0).getOrderId());
        for (OrderEntry orderEntry : dto) {
            list.add(orderEntry.getOrderEntryId());
        }
        map.put("orderEntryIdList", list);
        List<OrderEntry> orderEntryList = mapper.selectUnCancelOrderEntry(map);
        if (CollectionUtils.isNotEmpty(orderEntryList)) {
            for (OrderEntry orderEntry : orderEntryList) {
                orderEntry.setChosenCoupon(dto.get(0).getChosenCoupon());
                orderEntry.setChosenPromotion(dto.get(0).getChosenPromotion());
            }
        }
        return orderEntryList;
    }

    /**
     * 查询不退货的订单行
     *
     * @param request
     * @param dto
     * @return
     */
    @Override
    public List<OrderEntry> selectReturnOrderEntry(IRequest request, List<OrderEntry> dto) {
        if (CollectionUtils.isNotEmpty(dto)) {
            Map<String, Object> map = new HashMap<>();
            map.put("orderId", dto.get(0).getOrderId());
            List<OrderEntry> orderEntryList = mapper.selectReturnOrderEntry(map);
            if (CollectionUtils.isNotEmpty(orderEntryList)) {
                orderEntryList.get(0).setChosenCoupon(dto.get(0).getChosenCoupon());
                orderEntryList.get(0).setChosenPromotion(dto.get(0).getChosenPromotion());
            }
            return orderEntryList;
        }
        return null;
    }

    @Override
    public String checkAndGetPosCodeOfOrderEntry(OrderEntry orderEntry) {
        // 查询订单行上的仓库编码
        Long pointOfServiceId = orderEntry.getPointOfServiceId();
        Assert.notNull(pointOfServiceId, "订单行[" + orderEntry.getOrderEntryId() + "]未关联服务点");
        PointOfServiceDto pointOfServiceDto = new PointOfServiceDto();
        pointOfServiceDto.setPointOfServiceId(pointOfServiceId);
        pointOfServiceDto = iPointOfServiceExternalService.selectByPrimaryKey(pointOfServiceDto);
        Assert.notNull(pointOfServiceDto, "服务点[" + pointOfServiceId + "]不存在");
        return pointOfServiceDto.getCode();
    }


    /**
     * @param dto
     * @param request
     * @return
     * @description 订单取消时，查询非取消的normal状态的所有订单行 以便调用促销微服务查询促销信息
     */
    @Override
    public OrderPojo changePojo(IRequest request, List<OrderEntry> dto, String flag) {
        {
            OrderPojo orderPojo = new OrderPojo();

            if (CollectionUtils.isNotEmpty(dto)) {
                //订单头
                Order o = new Order();
                o.setOrderId(dto.get(0).getOrderId());
                Order order = orderService.selectByPrimaryKey(request, o);

                orderPojo.setEscOrderCode(order.getEscOrderCode());
                orderPojo.setOrderStatus(order.getOrderStatus());

                //查询customerId
                if (order.getUserId() != null) {
                    MstUser mstUser = new MstUser();
                    mstUser.setUserId(order.getUserId());
                    MstUser u = mstUserService.selectByPrimaryKey(request, mstUser);
                    if (u != null) {
                        orderPojo.setCustomerId(u.getCustomerid());
                    }
                }
                orderPojo.setCurrencyCode(order.getCurrencyId());
                orderPojo.setWebsiteCode("1");
                orderPojo.setChannelCode("1");
                orderPojo.setStoreCode("1");
                orderPojo.setReceiverName(order.getReceiverName());

                if (order.getReceiverCountry() != null) {
                    orderPojo.setCountryCode(order.getReceiverCountry());
                }
                if (order.getReceiverState() != null) {
                    orderPojo.setStateCode(order.getReceiverState());
                }
                if (order.getReceiverCity() != null) {
                    orderPojo.setCityCode(order.getReceiverCity());
                }
                if (order.getReceiverDistrict() != null) {
                    orderPojo.setDistrictCode(order.getReceiverDistrict());
                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String created = sdf.format(order.getCreationDate());
                orderPojo.setCreated(created);
                String modified = sdf.format(order.getLastUpdateDate());
                orderPojo.setModified(modified);

                orderPojo.setPaymentAmount(order.getPaymentAmount() != null ? order.getPaymentAmount() : 0);
                orderPojo.setOrderAmount(order.getOrderAmount() != null ? order.getOrderAmount().doubleValue() : 0);
                orderPojo.setNetAmount(null);

                if (order.getDiscountFee() != null) {
                    orderPojo.setDiscountFee(order.getDiscountFee().doubleValue());
                }
                if (order.getCouponFee() != null) {
                    orderPojo.setCouponFee(order.getCouponFee().doubleValue());
                }
                if (order.getTotalDiscount() != null) {
                    orderPojo.setTotalDiscount(order.getTotalDiscount().doubleValue());
                }
                if (order.getPostFee() != null) {
                    orderPojo.setPostFee(order.getPostFee().doubleValue());
                }
                if (order.getEpostFee() != null) {
                    orderPojo.setEpostFee(order.getEpostFee().doubleValue());
                }
                if (order.getFixFee() != null) {
                    orderPojo.setFixFee(order.getFixFee().doubleValue());
                }
                orderPojo.setPostReduce(null);
                orderPojo.setEpostReduce(null);
                orderPojo.setFixReduce(null);
                orderPojo.setShippingType(order.getShippingType());
                orderPojo.setUsedCoupon(order.getChosenCoupon());
                orderPojo.setChosenPromotion(order.getChosenPromotion());
                //“N”为获取用户可用促销   Y为根据所选优惠计算订单金额
                orderPojo.setIsCaculate(flag);
                if ("Y".equals(flag)) {
                    orderPojo.setChosenPromotion(dto.get(0).getChosenPromotion());
                    orderPojo.setChosenCoupon(dto.get(0).getChosenCoupon());
                }

                //处理订单行
                List<OrderEntryPojo> orderEntryList = new ArrayList<>();
                for (OrderEntry oe : dto) {
                    OrderEntry orderEntry = this.selectByPrimaryKey(request, oe);

                    OrderEntryPojo orderEntryPojo = new OrderEntryPojo();
                    orderEntryPojo.setBasePrice(orderEntry.getBasePrice());
                    orderEntryPojo.setOrderEntryId(orderEntry.getOrderEntryId());
                    orderEntryPojo.setQuantity(orderEntry.getQuantity() - orderEntry.getReturnedQuantity());
                    if (orderEntryPojo.getQuantity() == 0) {
                        continue;
                    }
                    orderEntryPojo.setReturnedQuantity(orderEntry.getReturnedQuantity());
                    orderEntryPojo.setDiscountFee(orderEntry.getDiscountFee());
                    orderEntryPojo.setDiscountFeel(orderEntry.getDiscountFeel());
                    orderEntryPojo.setCouponFee(orderEntry.getCouponFee());
                    orderEntryPojo.setTotalDiscount(orderEntry.getTotalDiscount());
                    orderEntryPojo.setUnitFee(orderEntry.getUnitFee());
                    orderEntryPojo.setTotalFee(orderEntry.getTotalFee());
                    orderEntryPojo.setShippingFee(orderEntry.getShippingFee());
                    orderEntryPojo.setInstallationFee(orderEntry.getInstallationFee());
                    orderEntryPojo.setPreShippingFee(orderEntry.getPreShippingfee());
                    orderEntryPojo.setPreInstallationFee(orderEntry.getPreInstallationfee());
                    orderEntryPojo.setIsGift(orderEntry.getIsGift());
                    orderEntryPojo.setOrder(orderEntry.getOrderId());
                    //查找商品编码
                    if (orderEntry.getProductId() != null) {
                        Product product = new Product();
                        product.setProductId(orderEntry.getProductId());
                        Product p = productService.selectByPrimaryKey(request, product);
                        orderEntryPojo.setProduct(p.getCode());
                    }
                    orderEntryPojo.setProductPackageSize(null);
                    orderEntryPojo.setSuitCode(orderEntry.getSuitCode());
                    orderEntryPojo.setVproduct(orderEntry.getVproductCode());
                    orderEntryPojo.setLineNumber(String.valueOf(orderEntry.getLineNumber()));
                    orderEntryPojo.setEstimateDeliveryTime(String.valueOf(orderEntry.getEstimateDeliveryTime()));
                    orderEntryPojo.setEstimateConTime(String.valueOf(orderEntry.getEstimateConTime()));
                    orderEntryPojo.setShippingType(orderEntry.getShippingType());
                    //orderEntryPojo.setPointOfServiceId(orderEntry.getPointOfServiceId());
                    orderEntryPojo.setParts(null);

                    orderEntryList.add(orderEntryPojo);
                }

                orderPojo.setOrderEntryList(orderEntryList);
            }
            return orderPojo;
        }
    }

    /**
     * 根据订单行中的发货单ID和关联订单号查询订单行信息
     *
     * @param orderEntry 封装的参数
     * @return
     */
    @Override
    public List<OrderEntry> selectByParentLineAndConsignmentId(OrderEntry orderEntry) {
        return mapper.selectByParentLineAndConsignmentId(orderEntry);
    }

    /**
     * 根据关联商品CODE和订单ORDER_ID查询对应的订单行
     *
     * @param orderEntry
     * @return
     */
    @Override
    public List<OrderEntry> selectByProductCodeAndOrderId(OrderEntry orderEntry) {
        return mapper.selectByProductCodeAndOrderId(orderEntry);
    }

    /**
     * 获取订单行比较数据
     *
     * @param request
     * @param compare
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<OrderEntryCompare> allOrderEntryCompare(IRequest request, OrderEntryCompare compare, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return exportOrderEntryCompare(request, compare);
    }

    @Override
    public List<OrderEntryCompare> exportOrderEntryCompare(IRequest request, OrderEntryCompare compare) {
        List<OrderEntryCompare> list = mapper.allOrderEntryCompare(compare);
        for (OrderEntryCompare orderEntryCompare : list) {
            calculateSalePrice(orderEntryCompare);
            orderEntryCompare.setPlatformProductSize(mamVcodeHeaderService.volumeCal(orderEntryCompare.getVproductCode()).get(0));
            orderEntryCompare.setPlatformProductPackedSize(mamVcodeHeaderService.outPackVolumeCal(orderEntryCompare.getVproductCode()).get(0));

        }
        return list;
    }

    /**
     * 计算销售价格
     *
     * @param orderEntryCompare
     */
    private void calculateSalePrice(OrderEntryCompare orderEntryCompare) {
        List<PriceRequestData> priceRequestDataList = new ArrayList<>();
        PriceRequestData priceRequestData = new PriceRequestData();
        priceRequestData.setvCode(orderEntryCompare.getVproductCode());
        priceRequestData.setOdtype(orderEntryCompare.getOdtype() == null ? "" : orderEntryCompare.getOdtype());
        priceRequestDataList.add(priceRequestData);
        String url = "/h/price/calculateSalePrice";
        HttpEntity<List<PriceRequestData>> entity = new HttpEntity<>(priceRequestDataList, null);
        com.hand.hmall.dto.ResponseData responseData = restTemplate.exchange(baseUri + modelUri + url, HttpMethod.POST, entity, com.hand.hmall.dto.ResponseData.class).getBody();
        if (responseData.isSuccess()) {
            JSONObject respObject = JSONObject.fromObject(responseData.getResp().get(0));
            if (respObject.optBoolean("success")) {
                orderEntryCompare.setPlatformSalesPrice(respObject.optDouble("totalPrice"));
            }
        }
    }

    @Override
    public List<OrderEntryComparePojo> allOrderEntryComparePart(IRequest request, OrderEntryComparePojo orderEntryComparePojo, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return exportOrderEntryComparePart(request, orderEntryComparePojo);
    }

    @Override
    public List<OrderEntryComparePojo> exportOrderEntryComparePart(IRequest request, OrderEntryComparePojo pojo) {
        List<OrderEntryComparePojo> list = mapper.allOrderEntryComparePart(pojo);
        for (OrderEntryComparePojo orderEntryComparePojo : list) {
            orderEntryComparePojo.setPlatformProductSize(mamVcodeHeaderService.volumeCal(orderEntryComparePojo.getVproductCode()).get(0));
            orderEntryComparePojo.setPlatformProductPackedSize(mamVcodeHeaderService.outPackVolumeCal(orderEntryComparePojo.getVproductCode()).get(0));

        }
        return list;
    }

    /**
     * 根据订单ID获取对应订单行中最大的行序号
     *
     * @param request
     * @param orderId
     * @return
     */
    @Override
    public Long getMaxLinenumberByOrderId(IRequest request, Long orderId) {
        return mapper.getMaxLinenumberByOrderId(orderId);
    }

    /**
     * 根据订单ID查询安装费 运费 数量
     *
     * @param orderEntry
     * @return
     */
    @Override
    public List<OrderEntry> selectFeeByOrderId(OrderEntry orderEntry) {
        return mapper.selectFeeByOrderId(orderEntry);
    }

    /**
     * 对于天猫订单，订单取消时，对于全部订单行的取消（将所有订单行均置为取消状态），则订单头状态更新为"TRADE_CLOSED"
     *
     * @param request
     * @param dto
     * @return
     */
    @Override
    public List<OrderEntry> cancelAllOrderAndRntryForTm(IRequest request, List<OrderEntry> dto) {
        if (CollectionUtils.isNotEmpty(dto)) {
            //将订单头状态更新为"TRADE_CLOSED"
            Order orderInfo = new Order();
            orderInfo.setOrderId(dto.get(0).getOrderId());
            Order order = orderService.selectInfoByOrderId(orderInfo).get(0);
            order.setOrderStatus(Constants.TRADE_CLOSED);

            order.setOrderAmount(new BigDecimal(0));
            order.setPostFee(new BigDecimal(0));
            order.setEpostFee(new BigDecimal(0));
            order.setFixFee(new BigDecimal(0));
            order.setPrePostfee(new BigDecimal(0));
            order.setPreEpostfee(new BigDecimal(0));
            order.setPreFixfee(new BigDecimal(0));
            order.setTotalDiscount(new BigDecimal(0));
            order.setDiscountFee(new BigDecimal(0));
            order.setCouponFee(new BigDecimal(0));

            //更新订单同步官网状态为N
            order.setSyncZmall("N");
            orderService.updateByPrimaryKeySelective(request, order);

            //更新订单行状态
            for (OrderEntry orderEntry : dto) {
                //若该行为套件行，则同时取消它的组件行
                if (Constants.ORDER_ENTRY_STATUS_IS_SUIT_Y.equals(orderEntry.getIsSuit())) {
                    OrderEntry entry = new OrderEntry();
                    entry.setParentLine(orderEntry.getOrderEntryId());
                    List<OrderEntry> list = mapper.select(entry);
                    if (CollectionUtils.isNotEmpty(list)) {
                        //取消所有的组件订单行
                        for (OrderEntry o : list) {
                            o.setOrderEntryStatus(Constants.ORDER_ENTRY_STATUS_CANCEL);
                            mapper.updateByPrimaryKeySelective(o);
                            //释放库存
                            if (o.getPin() != null) {
                                String result = iImAtpInfaceService.releaseAll(o.getPin(), "N");
                                if (!result.equals("")) {
                                    throw new RuntimeException("订单行编码" + o.getCode() + "的pin码" + o.getPin() + "释放库存失败!");
                                }
                            }
                            //增加书面记录
                            HmallSoChangeLog soChangeLog = new HmallSoChangeLog();
                            OrderEntry oInfo = orderEntryMapper.selectByPrimaryKey(o);
                            soChangeLog.setOrderId(oInfo.getOrderId());
                            soChangeLog.setOrderEntryId(oInfo.getOrderEntryId());
                            soChangeLog.setOrderType("1");
                            soChangeLog.setPin(oInfo.getPin());
                            soChangeLog.setProductId(oInfo.getProductId());
                            orderService.addSoChangeLog(soChangeLog);

                        }
                    }
                }
                //取消自身
                orderEntry.setOrderEntryStatus(Constants.ORDER_ENTRY_STATUS_CANCEL);
                mapper.updateByPrimaryKeySelective(orderEntry);

                //释放库存
                if (orderEntry.getPin() != null) {
                    String result = iImAtpInfaceService.releaseAll(orderEntry.getPin(), "N");
                    if (!result.equals("")) {
                        throw new RuntimeException("订单行编码" + orderEntry.getCode() + "的pin码" + orderEntry.getPin() + "释放库存失败!");
                    }
                }
                //增加书面记录
                HmallSoChangeLog soChangeLog = new HmallSoChangeLog();
                OrderEntry orderEntryInfo = orderEntryMapper.selectByPrimaryKey(orderEntry);
                soChangeLog.setOrderId(orderEntryInfo.getOrderId());
                soChangeLog.setOrderEntryId(orderEntryInfo.getOrderEntryId());
                soChangeLog.setOrderType("1");
                soChangeLog.setPin(orderEntryInfo.getPin());
                soChangeLog.setProductId(orderEntryInfo.getProductId());
                orderService.addSoChangeLog(soChangeLog);
            }

            //若该订单已生成发货单，那么发货单的状态也置成"TRADE_CLOSED"
            //若订单已生成发货单，发货单状态处于（"ABNORMAL"or"WAIT_FOR_DELIVERY"），该订单下的所有发货单状态均更改为‘TRADE_CLOSED’。//2017-9-20
            Consignment c = new Consignment();
            c.setOrderId(dto.get(0).getOrderId());
            List<Consignment> consignmentList = consignmentService.select(c);
            if (CollectionUtils.isNotEmpty(consignmentList)) {
                for (Consignment consignment : consignmentList) {
                    if (consignment.getStatus().equals("ABNORMAL") || consignment.getStatus().equals("WAIT_FOR_DELIVERY")) {
                        consignment.setStatus(Constants.TRADE_CLOSED);
                        //订单下关联的发货单同步RETAIL状态置为N
                        consignment.setSyncflag("N");
                        consignmentService.updateByPrimaryKeySelective(request, consignment);
                    }
                }
            }
        }
        return dto;
    }

    /**
     * 订单行拆分时满足条件的工艺审核流程
     *
     * @param orderEntry 待处理的订单行
     */
    private OrderEntry bomApprove(OrderEntry orderEntry) {
        Order paramOrder = new Order();
        MamSoApproveHis result = new MamSoApproveHis();
        paramOrder.setOrderId(orderEntry.getOrderId());
        Order order = orderService.selectByPrimaryKey(RequestHelper.newEmptyRequest(), paramOrder);
        Integer autoBomApproved = iGlobalVariantService.getNumber(Constants.AUTO_BOM_APPROVED, Integer.class);
        if (autoBomApproved == null) {
            throw new RuntimeException("全局变量AUTO_BOM_APPROVED没有维护");
        }
        if (org.apache.commons.lang.StringUtils.isBlank(orderEntry.getVproductCode())) {
            // 没有v码的订单行bom审核自动通过
            orderEntry.setBomApproved(Constants.YES);
            return this.insertSelective(RequestHelper.newEmptyRequest(), orderEntry);
        } else {
            OrderEntry orderEntryParam = new OrderEntry();
            orderEntryParam.setVproductCode(orderEntry.getVproductCode());
            orderEntryParam.setBomApproved(Constants.YES);
            List<OrderEntry> orderEntryList = this.select(orderEntryParam);
            if (CollectionUtils.size(orderEntryList) >= autoBomApproved) {
                orderEntry.setBomApproved(Constants.YES);
                return this.insertSelective(RequestHelper.newEmptyRequest(), orderEntry);
            } else {
                // 检查订单行pin码是否为空
                Assert.notNull(orderEntry.getPin(), "订单行[" + orderEntry.getOrderEntryId() + "]pin码不能为空");

                // 检查该pin码是否已经推送过配置器
                MamSoApproveHis mamSoApproveHisParam = new MamSoApproveHis();
                mamSoApproveHisParam.setPinCode(orderEntry.getPin());

                // 如果没有推送过则推送配置器
                if (CollectionUtils.isEmpty(iMamSoApproveHisService
                        .select(RequestHelper.newEmptyRequest(), mamSoApproveHisParam, 1, Integer.MAX_VALUE))) {
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
                    result = iMamSoApproveHisService.insertSelective(RequestHelper.newEmptyRequest(), mamSoApproveHis);
                    Assert.notNull(result, "配置器推送失败");
                    return this.insertSelective(RequestHelper.newEmptyRequest(), orderEntry);
                }
                return this.insertSelective(RequestHelper.newEmptyRequest(), orderEntry);
            }
        }
    }

    /**
     * 查询订单下的主推款订单行
     *
     * @param orderId 订单id
     * @return
     */
    @Override
    public List<OrderEntry> selectRegularEntries(Long orderId) {
        return orderEntryMapper.selectRegularEntries(orderId);
    }

    @Override
    public List<OrderEntry> getAtpTime(IRequest request, List<OrderEntry> orderEntryList, String receiverCity, String receiverDistrict) {
        JSONArray jsonArray = new JSONArray();
        Map<String, OrderEntry> map = new HashMap<>();
        for (OrderEntry entry : orderEntryList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("vMatnr", org.apache.commons.lang.StringUtils.isBlank(entry.getVproductCode()) ? "-1" : entry.getVproductCode());
            jsonObject.put("matnr", entry.getProductCode());
            jsonObject.put("quantity", entry.getQuantity());
            jsonObject.put("deliveryAddress", receiverCity + receiverDistrict);

            String interfaceId = sequenceGenerateService.getNextInterfaceId();
            jsonObject.put("interfaceId", interfaceId);
            jsonObject.put("occupy", Constants.NO);

            jsonArray.add(jsonObject);
            map.put(interfaceId, entry);
        }
        try {
            logger.info("atp请求报文---------" + jsonArray.toString());
            Response response = restClient.postString(Constants.MAP, INV_URL, jsonArray.toString(), Constants.MINI_TYPE_JSON, null, null);
            if (response.code() == 200) {
                JSONObject jsonObject = RestClient.responseToJSON(response);
                logger.info("atp响应报文---------" + jsonObject.toString());
                if (jsonObject.getBoolean("success")) {
                    JSONArray responseRows = jsonObject.getJSONArray("rows");
                    Iterator iterator = responseRows.iterator();
                    while (iterator.hasNext()) {
                        JSONObject row = (JSONObject) iterator.next();
                        String processFlag = row.getString("processFlag");
                        String atpDate = row.getString("atp");
                        if ("Y".equals(processFlag)) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = sdf.parse(atpDate);
                            String insterfaceId = row.getString("interfaceId");
                            OrderEntry orderEntry = map.get(insterfaceId);
                            orderEntry.setEstimateDeliveryTime(date);
                            orderEntry.setPointOfServiceId(iPointOfServiceExternalService.getPosIdOfPosFullName(row.getString("inventorySourcing")));
                        } else {
                            String errorMassage = row.getString("errorMassage");
                            String insterfaceId = row.getString("interfaceId");
                            if (!"null".equals(insterfaceId)) {
                                OrderEntry orderEntry = map.get(insterfaceId);
                                throw new RuntimeException("商品" + orderEntry.getProductCode() + "调用ATP交期接口失败:" + errorMassage);
                            } else {
                                throw new RuntimeException(errorMassage);
                            }
                        }
                    }
                } else {
                    throw new RuntimeException("库存接口调用失败");
                }
            } else {
                throw new RuntimeException("库存接口访问不通，code[" + response.code() + "]，message[" + response.message() + "]");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return orderEntryList;
    }

    @Override
    public List<OrderEntry> setGiftEntry(IRequest request, List<OrderEntry> orderEntryList, Long orderId) {
        for (OrderEntry entry : orderEntryList) {
            entry.setOrderId(orderId);
            entry.setConsignmentId(null);
            entry.setCode(sequenceGenerateService.getNextOrderEntryCode());
            entry.setLineNumber(mapper.getMaxLinenumberByOrderId(orderId) == null ? 10L : mapper.getMaxLinenumberByOrderId(orderId) + LINE_NUMBER_STEP);
            entry.setParentLine(null);
            entry.setUnitFee(new BigDecimal(0).doubleValue());
            entry.setTotalFee(new BigDecimal(0).doubleValue());
            // entry.setIsGift("Y");
            entry.setOrderEntryType("");
            if (productService.selectProductByCode(entry.getProductCode()).size() > 0) {
                entry.setProductId(productService.selectProductByCode(entry.getProductCode()).get(0).getProductId());
                entry.setOdtype(productService.selectProductByCode(entry.getProductCode()).get(0).getCustomChannelSource());
            }
            //entry.setPin(sequenceGenerateService.getNextPin());
            entry.setShippingFee(new BigDecimal(0).doubleValue());
            entry.setInstallationFee(new BigDecimal(0).doubleValue());
            entry.setSyncflag("N");
            entry.setNote("");
            entry.setBomApproved("N");
            entry.setPreShippingfee(new BigDecimal(0).doubleValue());
            entry.setPreInstallationfee(new BigDecimal(0).doubleValue());
            entry.setDiscountFee(new BigDecimal(0).doubleValue());
            entry.setDiscountFeel(new BigDecimal(0).doubleValue());
            entry.setCouponFee(new BigDecimal(0).doubleValue());
            entry.setTotalFee(new BigDecimal(0).doubleValue());
            entry.setProductSize(mamVcodeHeaderService.volumeCal(entry.getVproductCode()).get(0));
            entry.setProductPackedSize(mamVcodeHeaderService.outPackVolumeCal(entry.getVproductCode()).get(0));
            entry.setOrderEntryStatus("NORMAL");

            entry.setReturnedQuantity(0);
            entry.setNotReturnQuantity(0);
            entry.setEscLineNumber(mapper.getMaxEscLinenumberByOrderId(orderId) == null ? "10" : String.valueOf(mapper.getMaxEscLinenumberByOrderId(orderId) + LINE_NUMBER_STEP));
            entry.setBomRejectReason("");


        }
        return orderEntryList;
    }
}

