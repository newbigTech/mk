package com.hand.hmall.as.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.im.dto.ImAtpInface;
import com.hand.hap.im.dto.ImAtpInfaceResponse;
import com.hand.hap.im.service.IImAtpInfaceService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.as.dto.*;
import com.hand.hmall.as.mapper.ServiceorderEntryMapper;
import com.hand.hmall.as.service.IAsReturnEntryService;
import com.hand.hmall.as.service.IAsReturnService;
import com.hand.hmall.as.service.IServiceorderEntryService;
import com.hand.hmall.as.service.IServiceorderService;
import com.hand.hmall.common.service.ISequenceGenerateService;
import com.hand.hmall.mst.dto.Product;
import com.hand.hmall.mst.service.IProductService;
import com.hand.hmall.om.dto.HmallSoChangeLog;
import com.hand.hmall.om.dto.Order;
import com.hand.hmall.om.dto.OrderEntry;
import com.hand.hmall.om.service.IOrderEntryService;
import com.hand.hmall.om.service.IOrderService;
import com.hand.hmall.pin.dto.Pin;
import com.hand.hmall.pin.dto.PinAlter;
import com.hand.hmall.pin.service.IPinAlterService;
import com.hand.hmall.pin.service.IPinService;
import com.hand.hmall.util.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zhangmeng
 * @version 0.1
 * @name ServiceorderEntryServiceImpl
 * @description 服务单行ServiceImpl
 * @date 2017/7/19
 */
@Service
@Transactional
public class ServiceorderEntryServiceImpl extends BaseServiceImpl<ServiceorderEntry> implements IServiceorderEntryService {

    private static final Logger logger = LoggerFactory.getLogger(ServiceorderEntryServiceImpl.class);

    @Autowired
    private ServiceorderEntryMapper serviceorderEntryMapper;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IOrderEntryService orderEntryService;

    @Autowired
    private IServiceorderService serviceorderService;

    @Autowired
    private IAsReturnService asReturnService;

    @Autowired
    private IAsReturnEntryService asReturnEntryService;

    @Autowired
    private ISequenceGenerateService sequenceGenerateService;

    @Autowired
    private IProductService productService;

    @Autowired
    private IPinAlterService pinAlterService;

    @Autowired
    private IPinService pinService;

    @Autowired
    private IImAtpInfaceService atpInfaceService;

    /**
     * 根据派工单ID查询其对应的全部服务单行
     *
     * @param serviceOrderId - 派工单ID
     * @return
     */
    @Override
    public List<ServiceorderEntry> selectDispatchOrderEntry(Long serviceOrderId) {
        return serviceorderEntryMapper.selectDispatchOrderEntry(serviceOrderId);
    }

    /**
     * 根据退货单ID查询其对应的全部售后单行
     *
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<ServiceorderEntry> queryReturnOrder(ServiceorderEntry dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return serviceorderEntryMapper.queryReturnOrder(dto);
    }

    @Override
    public List<ServiceorderEntry> getServiceOrderListExcludeProductId(ServiceorderEntry dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return serviceorderEntryMapper.getServiceOrderListExcludeProductId(dto);
    }

    @Override
    public List<ServiceorderEntry> selectByOrderServiceOrderEntryIdList(IRequest request, List<ServiceorderEntry> serviceorderEntryList) {
        return serviceorderEntryMapper.selectByOrderServiceOrderEntryIdList(serviceorderEntryList);
    }

    @Override
    public Map<String, Object> createChangeGoodOrder(IRequest request, ChangeGoodDto changeGoodDto) {
        Map<String, Object> map = new HashMap<>();
        map.put("totalReturnFee", 0D);
        map.put("totalFee", 0D);
        map.put("totalShippingFee", 0D);
        map.put("totalInstallationFee", 0D);
        map.put("totalCouponFee", 0D);
        map.put("totalDiscountFee", 0D);
        map.put("totalDiscount", 0D);
        DecimalFormat df = new DecimalFormat("0.00");

        if (changeGoodDto != null) {
            //生成换发单头
            Order order = createChangeOrder(request, changeGoodDto);
            if (order != null) {
                order = orderService.insertSelective(request, order);
                map.put("orderCode", order.getCode());
                map.put("orderId", order.getOrderId());
            }

            List<ServiceorderEntry> serviceorderEntruList = changeGoodDto.getServiceorderEntryList();
            if (CollectionUtils.isNotEmpty(serviceorderEntruList)) {
                for (ServiceorderEntry serviceorderEntry : serviceorderEntruList) {
                    //生成换发单行
                    OrderEntry orderEntry = changeGoodOrderEntry(request, serviceorderEntry, changeGoodDto.getCustomerDemandTime(), map);
                    orderEntry.setOrderId(order.getOrderId());
                    orderEntryService.insertSelective(request, orderEntry);
                    //增加书面记录
                    HmallSoChangeLog soChangeLog = new HmallSoChangeLog();
                    soChangeLog.setOrderId(orderEntry.getOrderId());
                    soChangeLog.setOrderEntryId(orderEntry.getOrderEntryId());
                    soChangeLog.setOrderType("1");
                    soChangeLog.setPin(orderEntry.getPin());
                    soChangeLog.setProductId(orderEntry.getProductId());
                    orderService.addSoChangeLog(soChangeLog);

                    //生成PIN码节点信息
                    Pin pin = createPinInfo(request, orderEntry);
                    pinService.insertSelective(request, pin);
                }
            }

            //更新换发单头的几个行字段总和
            if (order.getOrderId() != null) {
                Double totalFee = (Double) map.get("totalFee");
                Double totalShippingFee = (Double) map.get("totalShippingFee");
                Double totalInstallationFee = (Double) map.get("totalInstallationFee");
                Double totalCouponFee = (Double) map.get("totalCouponFee");
                Double totalDiscountFee = (Double) map.get("totalDiscountFee");
                Double totalDiscount = (Double) map.get("totalDiscount");

                order.setPaymentAmount(Double.valueOf(df.format(totalFee)));
                order.setPostFee(new BigDecimal(df.format(totalShippingFee)));
                order.setFixFee(new BigDecimal(df.format(totalInstallationFee)));
                order.setOrderAmount(new BigDecimal(df.format(totalFee)));
                order.setCouponFee(new BigDecimal(df.format(totalCouponFee)));
                order.setDiscountFee(new BigDecimal(df.format(totalDiscountFee)));
                order.setTotalDiscount(new BigDecimal(df.format(totalDiscount)));
                order.setCurrentAmount(Double.valueOf(df.format(totalFee)));
                orderService.updateByPrimaryKeySelective(request, order);
            }

            //生成换退单头
            AsReturn asReturn = changeReturnOrder(request, changeGoodDto, serviceorderEntruList.get(0).getServiceOrderId());
            if (asReturn != null) {
                asReturn.setSwapOrderId(order.getOrderId());
                asReturnService.insertSelective(request, asReturn);
                map.put("returnCode", asReturn.getCode());
            }

            if (CollectionUtils.isNotEmpty(serviceorderEntruList)) {
                for (ServiceorderEntry serviceorderEntry : serviceorderEntruList) {
                    //生成退换单行
                    AsReturnEntry returnEntry = changeReturnEntry(request, serviceorderEntry, map);
                    returnEntry.setAsReturnId(asReturn.getAsReturnId());
                    asReturnEntryService.insertSelective(request, returnEntry);
                }
            }
            //更新退换单头的Returnfee，保留2位有效数字
            if (asReturn.getAsReturnId() != null) {
                Double totalReturnFee = (Double) map.get("totalReturnFee");
                asReturn.setReturnFee(Double.valueOf(df.format(totalReturnFee)));
                asReturnService.updateByPrimaryKeySelective(request, asReturn);
            }
        }
        return map;
    }

    @Override
    public List<Date> getAtp(IRequest request, ChangeGoodDto changeGoodDto) {

        List<Date> dateList = new ArrayList<>(); // result

        List<ImAtpInface> imAtpInfaces = new ArrayList<>();

        List<ImAtpInfaceResponse> atpInfaceResponses;

        List<ServiceorderEntry> list = changeGoodDto.getServiceorderEntryList();

        if (CollectionUtils.isNotEmpty(list)) {

            for (ServiceorderEntry serviceorderEntry : list) {

                ImAtpInface atpInface = new ImAtpInface();

                atpInface.setvMatnr(StringUtils.isBlank(serviceorderEntry.getVproductCode()) ? "-1" : serviceorderEntry.getVproductCode());

                Product product = productService.selectByProductId(serviceorderEntry.getProductId());
                Assert.notNull(product, "服务单行[" + serviceorderEntry.getServiceOrderEntryId() + "]的商品[" + serviceorderEntry.getProductId() + "]不存在");
                atpInface.setMatnr(product.getCode());

                Assert.notNull(serviceorderEntry.getQuantity(), "服务单行" + serviceorderEntry.getServiceOrderEntryId() + "的数量不能为空!");
                atpInface.setQuantity(serviceorderEntry.getQuantity().doubleValue());

                Assert.notNull(changeGoodDto.getReceiverCity(), "收货人市不能为空!");
                Assert.notNull(changeGoodDto.getReceiverDistrict(), "收货人区不能为空!");
                atpInface.setDeliveryAddress(changeGoodDto.getReceiverCity() + changeGoodDto.getReceiverDistrict());

                String interfaceId = sequenceGenerateService.getNextInterfaceId();
                atpInface.setInterfaceId(interfaceId);
                atpInface.setOccupy(Constants.NO);

                imAtpInfaces.add(atpInface);
            }
        }

        atpInfaceResponses = atpInfaceService.importAtpData(imAtpInfaces);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            for (ImAtpInfaceResponse atpResponse : atpInfaceResponses) {
                if (Constants.NO.equals(atpResponse.getProcessFlag())) {
                    logger.error("交期计算失败:\n" + atpResponse.getErrorMassage());
                    throw new RuntimeException("交期计算失败:\n" + atpResponse.getErrorMassage());
                }
                dateList.add(dateFormat.parse(atpResponse.getAtp()));
            }
        } catch (ParseException e) {
            // e.printStackTrace();
            logger.error("日期格式解析错误:\n" + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

        return dateList;
    }

    /**
     * 根据退款单ID查询其对应的全部服务单行
     *
     * @param serviceOrderId - 退款单ID
     * @return
     */
    @Override
    public List<ServiceorderEntry> selectRefundOrderEntry(Long serviceOrderId) {
        return serviceorderEntryMapper.selectRefundOrderEntry(serviceOrderId);
    }

    /**
     * 查询售后单关联的售后单行
     *
     * @param dto
     * @return
     */
    @Override
    public List<ServiceorderEntry> queryServiceOrderInfo(IRequest iRequest, ServiceorderEntry dto, int page, int pagesize) {
        return serviceorderEntryMapper.queryServiceOrderInfo(dto);
    }

    /**
     * 根据服务单ID查询其对应的全部服务单行
     *
     * @param serviceOrderId - 服务单ID
     * @return
     */
    @Override
    public List<ServiceorderEntry> queryOrderEntriesByOrderId(long serviceOrderId) {
        ServiceorderEntry entry = new ServiceorderEntry();
        entry.setServiceOrderId(serviceOrderId);
        return serviceorderEntryMapper.queryServiceOrderInfo(entry);
    }

    //生成换发单头对象
    Order createChangeOrder(IRequest request, ChangeGoodDto changeGoodDto) {
        Order order = new Order();
        //查询该订单
        Order o = new Order();
        o.setOrderId(changeGoodDto.getOrderId());
        Order result = orderService.selectByPrimaryKey(request, o);
        if (result != null) {
            order.setCode(sequenceGenerateService.getNextOrderCode()); //订单编号生成规则
            order.setEscOrderCode(result.getEscOrderCode());
            order.setOrderStatus(Constants.ORDER_STATUS_NEW_CREATE);
            order.setUserId(result.getUserId());
            order.setCurrencyId(Constants.CURRENCY_ID);
            order.setWebsiteId(result.getWebsiteId());
            order.setSalechannelId(result.getSalechannelId());
            order.setStoreId(result.getStoreId());
            order.setShippingType(result.getShippingType());
            order.setOrderCreationtime(getDate(0));
            order.setIsInvoiced("N");
            order.setInvoiceType("N");
            order.setTotalcon("Y");
            order.setReceiverName(changeGoodDto.getReceiverName());
            order.setReceiverCountry("CN");
            order.setReceiverState(changeGoodDto.getReceiverState());
            order.setReceiverCity(changeGoodDto.getReceiverCity());
            order.setReceiverDistrict(changeGoodDto.getReceiverDistrict());
            order.setReceiverAddress(changeGoodDto.getReceiverAddress());
            order.setReceiverZip(changeGoodDto.getReceiverZip());
            order.setReceiverMobile(changeGoodDto.getReceiverMobile());
            order.setReceiverPhone(changeGoodDto.getReceiverPhone());
            order.setEstimateDeliveryTime(changeGoodDto.getCustomerDemandTime());
            order.setPayRate("1");
            order.setSyncflag("N");
            order.setSplitAllowed("Y");
            order.setLocked("N");
            order.setIsIo("N");
            order.setIsOut("N");
            order.setSyncZmall("N");
            order.setOrderType("SWAP");
        } else {
            throw new RuntimeException("该服务单对应的订单" + changeGoodDto.getOrderId() + "已被删除!");
        }

        return order;
    }

    //生成换发单行对象
    OrderEntry changeGoodOrderEntry(IRequest request, ServiceorderEntry serviceorderEntry, Date customerDemandTime, Map<String, Object> map) {
        OrderEntry orderEntry = new OrderEntry();
        DecimalFormat df = new DecimalFormat("0.00");
        OrderEntry o = new OrderEntry();
        o.setOrderEntryId(serviceorderEntry.getOrderEntryId());
        OrderEntry result = orderEntryService.selectByPrimaryKey(request, o);
        if (result != null) {
            orderEntry.setCode(sequenceGenerateService.getNextOrderEntryCode());//系统自动生成
            orderEntry.setLineNumber(orderEntryService.getMaxLinenumberByOrderId(request, (Long) map.get("orderId")) == null ? 10L : orderEntryService.getMaxLinenumberByOrderId(request, (Long) map.get("orderId")) + 10L); //按照规则生成
            orderEntry.setParentLine(result.getParentLine());
            orderEntry.setQuantity(Integer.parseInt(serviceorderEntry.getQuantity().toString()));
            orderEntry.setUnit(result.getUnit());
            orderEntry.setBasePrice(result.getBasePrice());
            orderEntry.setUnitFee(result.getUnitFee());

            if (result.getUnitFee() != null) {
                orderEntry.setTotalFee(Double.valueOf(df.format(result.getUnitFee() * serviceorderEntry.getQuantity())));
            } else {
                orderEntry.setTotalFee(0D);
            }
            Double totalFee = (Double) map.get("totalFee");
            totalFee = totalFee + orderEntry.getTotalFee();
            map.put("totalFee", totalFee);

            orderEntry.setIsGift(result.getIsGift());
            orderEntry.setEstimateDeliveryTime(customerDemandTime);
            orderEntry.setProductId(result.getProductId());
            orderEntry.setVproductCode(result.getVproductCode());
            orderEntry.setSuitCode(result.getSuitCode());
            orderEntry.setPin(sequenceGenerateService.getNextPin()); //系统自动生成

            if (result.getShippingFee() != null && result.getQuantity() != null && result.getQuantity() != 0) {
                orderEntry.setShippingFee(Double.valueOf(df.format(result.getShippingFee() * serviceorderEntry.getQuantity() / result.getQuantity())));
            } else {
                orderEntry.setShippingFee(0.00);
            }
            Double totalShippingFee = (Double) map.get("totalShippingFee");
            totalShippingFee = totalShippingFee + orderEntry.getShippingFee();
            map.put("totalShippingFee", totalShippingFee);

            if (result.getInstallationFee() != null && result.getQuantity() != null && result.getQuantity() != 0) {
                orderEntry.setInstallationFee(Double.valueOf(df.format(result.getInstallationFee() * serviceorderEntry.getQuantity() / result.getQuantity())));
            } else {
                orderEntry.setInstallationFee(0.00);
            }
            Double totalInstallationFee = (Double) map.get("totalInstallationFee");
            totalInstallationFee = totalInstallationFee + orderEntry.getInstallationFee();
            map.put("totalInstallationFee", totalInstallationFee);

            orderEntry.setSyncflag("N");
            orderEntry.setShippingType(result.getShippingType());
            orderEntry.setPointOfServiceId(result.getPointOfServiceId());
            orderEntry.setBomApproved("N");

            if (result.getPreShippingfee() != null && result.getQuantity() != null && result.getQuantity() != 0) {
                orderEntry.setPreShippingfee(Double.valueOf(df.format(result.getPreShippingfee() * serviceorderEntry.getQuantity() / result.getQuantity())));
            } else {
                orderEntry.setPreShippingfee(0.00);
            }

            if (result.getPreInstallationfee() != null && result.getQuantity() != null && result.getQuantity() != 0) {
                orderEntry.setPreInstallationfee(Double.valueOf(df.format(result.getPreInstallationfee() * serviceorderEntry.getQuantity() / result.getQuantity())));
            } else {
                orderEntry.setPreInstallationfee(0.00);
            }

            if (result.getDiscountFee() != null && result.getQuantity() != null && result.getQuantity() != 0) {
                orderEntry.setDiscountFee(Double.valueOf(df.format(result.getDiscountFee() * serviceorderEntry.getQuantity() / result.getQuantity())));
            } else {
                orderEntry.setDiscountFee(0.00);
            }

            if (result.getDiscountFeel() != null && result.getQuantity() != null && result.getQuantity() != 0) {
                orderEntry.setDiscountFeel(Double.valueOf(df.format(result.getDiscountFeel() * serviceorderEntry.getQuantity() / result.getQuantity())));
            } else {
                orderEntry.setDiscountFeel(0.00);
            }
            Double totalDiscountFee = (Double) map.get("totalDiscountFee");
            totalDiscountFee = totalDiscountFee + orderEntry.getDiscountFee() + orderEntry.getDiscountFeel();
            map.put("totalDiscountFee", totalDiscountFee);

            if (result.getCouponFee() != null && result.getQuantity() != null && result.getQuantity() != 0) {
                orderEntry.setCouponFee(Double.valueOf(df.format(result.getCouponFee() * serviceorderEntry.getQuantity() / result.getQuantity())));
            } else {
                orderEntry.setCouponFee(0.00);
            }
            Double totalCouponFee = (Double) map.get("totalCouponFee");
            totalCouponFee = totalCouponFee + orderEntry.getCouponFee();
            map.put("totalCouponFee", totalCouponFee);

            if (result.getTotalDiscount() != null && result.getQuantity() != null && result.getQuantity() != 0) {
                orderEntry.setTotalDiscount(Double.valueOf(df.format(result.getTotalDiscount() * serviceorderEntry.getQuantity() / result.getQuantity())));
            } else {
                orderEntry.setTotalDiscount(0.00);
            }
            Double totalDiscount = (Double) map.get("totalDiscount");
            totalDiscount = totalDiscount + orderEntry.getTotalDiscount();
            map.put("totalDiscount", totalDiscount);

            orderEntry.setProductSize(result.getProductSize());
            orderEntry.setProductPackedSize(result.getProductPackedSize());
            orderEntry.setOrderEntryStatus(Constants.ORDER_ENTRY_STATUS_NORMAL);

            if (result.getInternalPrice() != null && result.getQuantity() != null && result.getQuantity() != 0) {
                orderEntry.setInternalPrice(Double.valueOf(df.format(result.getInternalPrice() * serviceorderEntry.getQuantity() / result.getQuantity())));
            } else {
                orderEntry.setInternalPrice(0.00);
            }

            orderEntry.setInvOccupyFlag("N");
            orderEntry.setOdtype(result.getOdtype());
            orderEntry.setReturnedQuantity(new Integer(0));
            orderEntry.setNotReturnQuantity(new Integer(0));
            orderEntry.setEscLineNumber(result.getEscLineNumber());
        } else {
            throw new RuntimeException("该服务单行对应的订单行" + serviceorderEntry.getOrderEntryId() + "已被删除!");
        }
        return orderEntry;
    }

    //生成PIN码节点
    Pin createPinInfo(IRequest request, OrderEntry orderEntry) {
        Pin pin = new Pin();
        pin.setCode(orderEntry.getPin());
        pin.setEntryCode(orderEntry.getCode());
        pin.setEventCode("MAP0100");

        PinAlter pinAlter = new PinAlter();
        pinAlter.setEventCode(pin.getEventCode());
        List<PinAlter> pinAlterList = pinAlterService.select(request, pinAlter, 1, 10);
        if (CollectionUtils.isNotEmpty(pinAlterList)) {
            pin.setEventDes(pinAlterList.get(0).getEventDes());
        }
        pin.setSystem("hmall");
        pin.setOperator("system");
        pin.setOperationTime(orderEntry.getCreationDate());
        pin.setEventInfo("订单已生成");

        return pin;
    }

    //生成换退单头对象
    AsReturn changeReturnOrder(IRequest request, ChangeGoodDto changeGoodDto, Long serviceOrderId) {
        AsReturn asReturn = new AsReturn();

        Serviceorder s = new Serviceorder();
        s.setServiceOrderId(serviceOrderId);
        Serviceorder result = serviceorderService.selectByPrimaryKey(request, s);
        if (result != null) {
            asReturn.setCode(sequenceGenerateService.getNextReturnCode()); //系统自动生成
            asReturn.setStatus("NEW");
            asReturn.setOrderId(changeGoodDto.getOrderId());
            asReturn.setServiceOrderId(serviceOrderId);
            asReturn.setName(result.getName());
            asReturn.setMobile(result.getMobile());
            asReturn.setAddress(result.getAddress());
            //换退单建议退款金额默认为0
            asReturn.setReferenceFee(0d);
            asReturn.setCs(request.getUserName());
            asReturn.setSyncflag("N");
            asReturn.setAppointmentDate(changeGoodDto.getCustomerDemandTime());
            asReturn.setReturnType("R03");

            asReturn.setResponsibleParty(changeGoodDto.getResponsibleParty());
        } else {
            throw new RuntimeException("该服务单" + serviceOrderId + "已被删除!");
        }
        return asReturn;
    }

    //生成退换单行对象
    AsReturnEntry changeReturnEntry(IRequest request, ServiceorderEntry serviceorderEntry, Map<String, Object> map) {
        AsReturnEntry returnEntry = new AsReturnEntry();

        OrderEntry orderEntry = new OrderEntry();
        orderEntry.setOrderEntryId(serviceorderEntry.getOrderEntryId());
        OrderEntry result = orderEntryService.selectByPrimaryKey(request, orderEntry);
        if (result != null) {
            returnEntry.setOrderEntryId(serviceorderEntry.getOrderEntryId());
            returnEntry.setServiceOrderId(serviceorderEntry.getServiceOrderId());
            if (result.getLineNumber() != null) {
                returnEntry.setLineNumber(String.valueOf(result.getLineNumber()));
            }
            returnEntry.setParentLine(result.getParentLine());
            returnEntry.setQuantity(serviceorderEntry.getQuantity());
            returnEntry.setUnit(result.getUnit());
            returnEntry.setBasePrice(result.getBasePrice());

            //获取所有订单行的retrunFee
            if (result.getUnitFee() != null) {
                DecimalFormat df = new DecimalFormat("0.00");
                returnEntry.setReturnFee(Double.valueOf(df.format(result.getUnitFee() * serviceorderEntry.getQuantity())));
            } else {
                returnEntry.setReturnFee(0D);
            }
            Double totalReturnFee = (Double) map.get("totalReturnFee");
            totalReturnFee = totalReturnFee + returnEntry.getReturnFee();
            map.put("totalReturnFee", totalReturnFee);

            returnEntry.setIsGift(result.getIsGift());
            if (result.getProductId() != null) {
                returnEntry.setProductId(String.valueOf(result.getProductId()));
            }
            returnEntry.setVproduct(result.getVproductCode());
            returnEntry.setSuitCode(result.getSuitCode());
            returnEntry.setReturnReason1(serviceorderEntry.getSvproReason1());
            returnEntry.setReturnReason2(serviceorderEntry.getSvproReason2());
            returnEntry.setInternalPrice(result.getInternalPrice());
            returnEntry.setReturnentryStatus("NORMAL");
        } else {
            throw new RuntimeException("该服务单行对应的订单行" + serviceorderEntry.getOrderEntryId() + "已被删除!");
        }

        return returnEntry;
    }

    /**
     * @param lengthDay
     * @return
     * @description 获取当前时间的lengthDay天之后的时间
     */
    public Date getDate(int lengthDay) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, lengthDay);
        date = calendar.getTime();
        return date;
    }


}