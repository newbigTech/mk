package com.hand.hmall.om.service.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.hand.common.util.Auth;
import com.hand.common.util.SecretKey;
import com.hand.hap.atp.dto.AtpInvSourceInfo;
import com.hand.hap.atp.service.IAtpInvSourceInfoService;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.mybatis.entity.Example;
import com.hand.hap.mybatis.util.StringUtil;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.hr.dto.MarkorEmployee;
import com.hand.hmall.log.dto.LogManager;
import com.hand.hmall.log.service.ILogManagerService;
import com.hand.hmall.om.dto.*;
import com.hand.hmall.om.mapper.ConsignmentMapper;
import com.hand.hmall.om.mapper.OrderMapper;
import com.hand.hmall.om.service.IConsignmentService;
import com.hand.hmall.om.service.IOrderEntryService;
import com.hand.hmall.process.consignment.pojo.SplitHeader;
import com.hand.hmall.process.consignment.pojo.SplitRow;
import com.hand.hmall.process.consignment.service.IConsignmentProcessService;
import com.hand.hmall.util.Constants;
import com.hand.hmall.util.DateUtil;
import com.hand.hmall.ws.entities.GdtCondtion;
import com.hand.hmall.ws.entities.GdtItem;
import com.hand.hmall.ws.entities.OrderRequestBody;
import com.hand.hmall.ws.entities.OrderUpdateRequestbody;
import com.markor.map.framework.common.exception.BusinessException;
import com.markor.map.framework.restclient.RestClient;
import okhttp3.Response;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name ConsignmentServiceImpl
 * @description 发货单实现类
 * @date 2017年6月5日13:54:47
 */
@Service
@Transactional
public class ConsignmentServiceImpl extends BaseServiceImpl<Consignment> implements IConsignmentService {

    private static final Logger logger = LoggerFactory.getLogger(ConsignmentServiceImpl.class);

    @Autowired
    ConsignmentMapper mapper;

    @Autowired
    IConsignmentProcessService consignmentProcessService;

    @Autowired
    IOrderEntryService orderEntryService;

    @Autowired
    RestClient restClient;

    @Autowired
    ILogManagerService logManagerService;

    @Autowired
    IAtpInvSourceInfoService iAtpInvSourceInfoService;

    @Autowired
    IOrderEntryService iOrderEntryService;

    @Autowired
    ILogManagerService iLogManagerService;
    @Autowired
    OrderMapper orderMapper;

    /**
     * 生成发货单推送至Retail的交易完结日期
     *
     * @param c - 发货单
     * @return
     */
    private static String generateZZEDATS(Consignment c) {
        if ("WAIT_FOR_DELIVERY".equalsIgnoreCase(c.getStatus())) {
            return "";
        } else { // WAIT_BUYER_CONFIRM || TRADE_BUYER_SIGNED
            if (c.getTradeFinishTime() == null) {
                return "";
            } else {
                return DateUtil.getdate(c.getTradeFinishTime(), "yyyy-MM-dd");
            }
        }
    }

    @Override
    public List<Consignment> selectSendRetailData(Map map) {
        return mapper.selectSendRetailData(map);
    }

    /**
     * 发货单详情页查询
     *
     * @param iRequest 请求体
     * @param dto      参数封装对象
     * @param page     页数
     * @param pageSize 显示数量
     * @return
     */
    @Override
    public List<Consignment> queryInfo(IRequest iRequest, Consignment dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return mapper.queryInfo(dto);
    }

    /**
     * 发货单列表页面查询
     *
     * @param page             页数
     * @param pagesize         页面显示数量
     * @param logisticsNumber  快递单号
     * @param code
     * @param provice          省份
     * @param city             城市
     * @param receiverMobile   收货人手机号
     * @param startTime        下单开始时间
     * @param endTime          结束时间
     * @param strOrderStatus   订单状态
     * @param strDistribution  配送方式
     * @param strOrderTypes
     * @param corporateName    快递公司
     * @param csApproved       是否审核
     * @param bomApproved
     * @param pause
     * @param escOrderCode     发货单号
     * @param confirmReceiving 是否收货标识（Y已收获；N未收货）
     * @return
     */
    @Override
    public List<Consignment> selectConsignmentList(int page, int pagesize, String logisticsNumber, String code, String provice, String city, String receiverMobile,
                                                   String startTime, String endTime, String[] strOrderStatus, String[] strDistribution, String[] strOrderTypes, String corporateName, String csApproved, String bomApproved, String pause, String escOrderCode,
                                                   String confirmReceiving) {
        PageHelper.startPage(page, pagesize);
        List<Consignment> consignments = mapper.selectConsignmentList(logisticsNumber, code, provice, city, receiverMobile, startTime, endTime, strOrderStatus, strDistribution, strOrderTypes, corporateName, csApproved, bomApproved, pause, escOrderCode, confirmReceiving);
        return consignments;
    }

    /**
     * 根据发货单ID查询
     *
     * @param consignmentId 发货单ID
     * @return
     */
    @Override
    public Consignment queryOne(int consignmentId) {
        return mapper.selectByPrimaryKey(consignmentId);
    }

    /**
     * 根据发货单ID更新状态
     *
     * @param requestCtx
     * @param consignmentId 发货单ID
     * @param status        新状态
     */
    @Override
    public void updateStatus(IRequest requestCtx, int consignmentId, String status) {
        mapper.updateStatus(consignmentId, status);
    }

    /**
     * 更新异常状态为审核
     *
     * @param list
     * @param iRequest
     */
    @Override
    public void examinestatus(List<Consignment> list, IRequest iRequest) {
        if (CollectionUtils.isNotEmpty(list)) {
            List<Consignment> dto = new ArrayList<>();
            for (int i = 0; i < list.size(); i = i + 1000) {
                if ((i + 1000) > list.size()) {
                    dto = list.subList(i, list.size());
                } else {
                    dto = list.subList(i, i + 1000);
                }
                mapper.examinestatus(dto);
            }
        }

    }

    /**
     * 手工拆单
     *
     * @param iRequest 请求对象
     * @param list     选择的发货单数据
     * @return 返回结果
     */
    @Override
    public List<Consignment> split(IRequest iRequest, List<OrderEntry> list) {

        Map<String, Object> resultMap = checkSpiltParam(iRequest, list);

        List<OrderEntry> orderEntries = (List<OrderEntry>) resultMap.get("old");
        List<OrderEntry> orderEntryList = (List<OrderEntry>) resultMap.get("new");

        List<Consignment> result = new ArrayList();

        //获取传递过来的订单行对应的发货单信息
        Long consignmentId = list.get(0).getConsignmentId();
        Consignment consignment = new Consignment();
        consignment.setConsignmentId(consignmentId);
        consignment = this.selectByPrimaryKey(iRequest, consignment);

        //调用发货单拆分微服务，封装请求体
        SplitHeader splitHeader = new SplitHeader();
        splitHeader.setConsignment(consignment);
        splitHeader.setSplitReason(Constants.CONSIGMENT_SPLIT);
        List<SplitRow> splitRows = new ArrayList<>();
        SplitRow splitRow1 = new SplitRow();
        splitRow1.setOrderEntries(orderEntries);
//        splitRow1.setOrderEntries(orderEntryList1);
        splitRow1.setStatus(Constants.CONSIGNMENT_STATUS_ABNORMAL);
        SplitRow splitRow2 = new SplitRow();
        splitRow2.setOrderEntries(orderEntryList);
        splitRow2.setStatus(Constants.CONSIGNMENT_STATUS_ABNORMAL);
        splitRows.add(splitRow1);
        splitRows.add(splitRow2);
        splitHeader.setSplitRows(splitRows);
        result = consignmentProcessService.splitConsignment(splitHeader);

        return result;
    }

    /**
     * 发货单手工拆分时条件判断
     *
     * @return
     */
    @Override
    public String checkSplit(IRequest iRequest, List<OrderEntry> list) {

        if (CollectionUtils.isEmpty(list))
            return null;
        //获取传递过来的订单行对应的发货单信息
        Long consignmentId = list.get(0).getConsignmentId();
        Consignment consignment = new Consignment();
        consignment.setConsignmentId(consignmentId);
        consignment = this.selectByPrimaryKey(iRequest, consignment);

        if (!"ABNORMAL".equals(consignment.getStatus())) {
            return "数据以改变，发货单状态不满足条件";
        }

        Map<String, Object> resultMap = checkSpiltParam(iRequest, list);
        List<OrderEntry> orderEntries = (List<OrderEntry>) resultMap.get("old");
        List<OrderEntry> orderEntryList = (List<OrderEntry>) resultMap.get("new");

        if (orderEntries == null || orderEntryList == null || orderEntries.size() <= 0 || orderEntryList.size() <= 0) {
            return "发货单拆分时需要将发货单对应订单行分为两部分,对应订单行不能全选和不选(注意套件)";
        }
        return null;
    }

    /**
     * 拆分校验
     *
     * @param iRequest
     * @param list
     * @return
     */
    private Map<String, Object> checkSpiltParam(IRequest iRequest, List<OrderEntry> list) {
        Map<String, Object> resultMap = new HashedMap();

        if (CollectionUtils.isEmpty(list))
            return null;

        OrderEntry parameterEntry = new OrderEntry();

        //获取传递过来的订单行对应的发货单信息
        Long consignmentId = list.get(0).getConsignmentId();

        //查询发货单对应的所有订单行
        parameterEntry.setConsignmentId(consignmentId);
        List<OrderEntry> orderEntryList = orderEntryService.select(parameterEntry);

        //遍历前台传递的订单行信息，找出套件，将套装订单行进行捆绑
        List<OrderEntry> orderEntries = new ArrayList<>();
        for (OrderEntry orderEntry : list) {
            OrderEntry orderEntry1 = orderEntryService.selectByPrimaryKey(iRequest, orderEntry);
            orderEntries.add(orderEntry1);
            parameterEntry.setParentLine(orderEntry.getOrderEntryId());
            List<OrderEntry> resultList = orderEntryService.select(parameterEntry);
            if (CollectionUtils.isNotEmpty(resultList))
                orderEntries.addAll(resultList);
        }

        //将所有的订单行分成两组，一组为勾选的，一组为未勾选的
        orderEntryList.removeAll(orderEntries);

        resultMap.put("old", orderEntries);
        resultMap.put("new", orderEntryList);
        return resultMap;
    }

    /**
     * {@inheritDoc}
     * 根据发货单状态查询发货单
     *
     * @see IConsignmentService#selectByStatus(String)
     */
    @Override
    public List<Consignment> selectByStatus(String status) {
        Consignment consignment = new Consignment();
        consignment.setStatus(status);
        return mapper.select(consignment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> sendToZmall() throws Exception {
        List<Consignment> result = mapper.queryForZmall();
        if (result.size() > 0) {
            StringWriter stringWriter = new StringWriter();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            Response response;
            Map map = null;
            try {
                objectMapper.writeValue(stringWriter, result);
                response = restClient.postString(Constants.ZMALL, "/zmallsync/consignment?token=" + Auth.md5(SecretKey.KEY + stringWriter.toString()),
                        stringWriter.toString(), "application/json", new HashMap<>(), new HashMap<>());
                map = objectMapper.readValue(response.body().string(), Map.class);
                map.put("dataJson", stringWriter.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (map != null && "S".equals(map.get("code"))) {
                if (map.get("resp") != null && map.get("resp") instanceof ArrayList) {
                    List<String> list = (ArrayList) map.get("resp");
                    Iterator<Consignment> it = result.iterator();
                    for (String str : list) {
                        while (it.hasNext()) {
                            Consignment consignment = it.next();
                            if (consignment.getConsignmentId().toString().equals(str)) {
                                it.remove();
                                break;
                            }
                        }
                        it = result.iterator();
                    }
                }
                mapper.updateSyncZmall(result);
            }
            return map;
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("code", "S");
            map.put("message", "没有可同步的发货单。");
            return map;
        }
    }

    /**
     * 根据条件查询发货单
     *
     * @param consignment 查询条件
     * @return
     */
    @Override
    public List<Consignment> select(Consignment consignment) {
        return mapper.select(consignment);
    }

    /**
     * 发送发货单信息至日日顺
     *
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<Map> sendConsignmentToRRS() throws Exception {
        List<ConsignmentToRRS> list = mapper.queryConsignmentForRRS();
        List<Map> result = new ArrayList<>();
        Properties properties = getProperties();
        if (list.size() > 0) {
            for (ConsignmentToRRS dto : list) {
                for (OrderItem odi : dto.getOrder_item_list()) {
                    odi.setItem_volume(new BigDecimal(odi.getItem_volume()).multiply(new BigDecimal(odi.getItem_quantity())).doubleValue());
                }
                StringWriter stringWriter = new StringWriter();
                ObjectMapper objectMapper = new ObjectMapper();
                //objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                objectMapper.writeValue(stringWriter, dto);

                //签名
                String sign = new BASE64Encoder().encode(DigestUtils.md5Hex(stringWriter.toString() + properties.getProperty("rrs.secretkey")).getBytes());

                Map<String, String> param = new HashMap<>();
                param.put("partner", properties.getProperty("rrs.partner"));
                param.put("sign_type", "MD5");
                param.put("sign", sign);
                param.put("notify_id", UUID.randomUUID().toString().replace("-", ""));
                param.put("notify_type", "par_tms_order_notify");
                param.put("notify_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                param.put("data_type", "JSON");
                param.put("content", stringWriter.toString());
                System.out.println("partner:" + properties.getProperty("rrs.partner"));
                System.out.println("secretkey:" + properties.getProperty("rrs.secretkey"));
                // http://27.223.99.200/rrs/route/access_orders.do
                Response response = restClient.postString(Constants.RRS, "", stringWriter.toString(), "application/json", param, new HashMap<>());
                Map lineResult = objectMapper.readValue(response.body().string(), HashMap.class);
                lineResult.put("dataJson", stringWriter.toString());
                lineResult.put("dataObj", dto);
                if ("true".equals(lineResult.get("success").toString())) {
                    mapper.updateSyncThirdLogistics(dto);
                }
                result.add(lineResult);
            }
            return result;
        } else {
            HashMap<String, String> lineResult = new HashMap<>();
            lineResult.put("success", "false");
            lineResult.put("errorMsg", "没有可同步的发货单。");
            result.add(lineResult);
            return result;
        }
    }

    /**
     * 供订单推送retail根据发货单生成body方法
     *
     * @param iRequest
     * @param consignment
     * @param log
     * @return
     */
    @Override
    public OrderRequestBody getBodyForOrderToRetail(IRequest iRequest, Consignment consignment, LogManager log) {

        OrderRequestBody body = new OrderRequestBody();
        OrderRequestBody.GdsHeader gdsHeader = new OrderRequestBody.GdsHeader();
        if (log != null && consignment.getSoldParty() == null) {
            log.setReturnMessage("发货单号：" + consignment.getConsignmentId() + "的售达方soldParty为空!");
            logManagerService.logEnd(iRequest, log);
            return body;
        }
        gdsHeader.setKUNNR(consignment.getSoldParty()); //售达方，根据order头中的webSideId去关联HMALL_MST_WEBSITE表中的code得到soldParty
        gdsHeader.setKUNNR2(Constants.ORDER_TO_RETAIL_KUNNR2);//传达方ZEST改为ONE
        gdsHeader.setOrg(Constants.ORDER_TO_RETAIL_ORG);
        gdsHeader.setVTWEG(Constants.ORDER_TO_RETAIL_VTWEG);
        if (log != null && consignment.getSalesOffice() == null) {
            log.setReturnMessage("发货单号：" + consignment.getConsignmentId() + "的销售办公室salesOffice为空!");
            logManagerService.logEnd(iRequest, log);
            return body;
        }
        gdsHeader.setVKBUR(consignment.getSalesOffice());//销售办公室，根据order头中的webSideId去关联HMALL_MST_WEBSITE表中的code得到saleOffice
        gdsHeader.setBrand(Constants.ORDER_TO_RETAIL_BRAND);//consignment.getBrand();
        gdsHeader.setCustomerID(consignment.getCustomerid());
        gdsHeader.setUserLevel(consignment.getUserLevel());
        gdsHeader.setReceiverName(consignment.getReceiverName());
        gdsHeader.setReceiverAddress(consignment.getReceiverAddress());
        String sex = "";
        if ((Constants.USER_SEX_M).equals(consignment.getSex())) {
            sex = Constants.USER_SEX_M_ZH;
        } else {
            sex = Constants.USER_SEX_F_ZH;
        }
        gdsHeader.setSex(sex);
        gdsHeader.setRegionCode(consignment.getRegionCode());

        if (log != null && StringUtils.isEmpty(consignment.getReceiverMobile()) && StringUtils.isEmpty(consignment.getReceiverPhone())) {
            //TODO 联系电话为空，报错
            log.setReturnMessage("发货单号：" + consignment.getConsignmentId() + "的联系方式为空!");
            logManagerService.logEnd(iRequest, log);
            return body;
        }
        gdsHeader.setReceiverMobile(consignment.getReceiverMobile());
        gdsHeader.setReceiverPhone(consignment.getReceiverPhone());
        gdsHeader.setNote(consignment.getNote());

        // 电商完结日期
//        String zzedats = generateZZEDATS(consignment);
//        logger.info("电商完结日期: " + consignment.getStatus() + ", " + consignment.getTradeFinishTime() + ", " + zzedats);
        if (consignment.getTradeFinishedTime() != null) {
            gdsHeader.setZZEDATS(DateUtil.getdate(consignment.getTradeFinishedTime(), "yyyy-MM-dd"));
        } else {
            gdsHeader.setZZEDATS("");
        }


        //四个留空的字段
        gdsHeader.setZZGLQHD("");
        gdsHeader.setAUGRU("");
        gdsHeader.setZZMARKETID1("");
        gdsHeader.setZZMARKETID2("");

        gdsHeader.setOrderType(Constants.ORDER_TO_RETAIL_ORDERTYPE);
        String date = null;
        if (consignment.getEstimateDeliveryTime() != null) {
            date = DateUtil.getdate(consignment.getEstimateDeliveryTime(), "yyyy-MM-dd");
        }
        gdsHeader.setEstimateDeliveryTime(date);
        gdsHeader.setZZHOPEDAY(date);

        String status = Constants.ORDER_TO_RETAIL_PAY_STATUS_YES;
        if ((Constants.ORDER_TO_RETAIL_CAN_DELIVERY).equals(consignment.getCanDelivery())) {
            status = Constants.ORDER_TO_RETAIL_PAY_STATUS_NO;
        }
        gdsHeader.setPayStatus(status);//判断发货单头上的CAN_DELIVERY是否为Y；若为Y则传99（可发货）；若为N则传10（不可发货）

        String shippingType = "";
        if ((Constants.ORDER_TO_RETAIL_SHIPPINGTYPE_PICKUP).equals(consignment.getShippingType())) {
            shippingType = Constants.ORDER_TO_RETAIL_SHIPPINGTYPE_P;
        } else {
            shippingType = Constants.ORDER_TO_RETAIL_SHIPPINGTYPE_L;
        }
        gdsHeader.setShippingType(shippingType); //D 配送  P 自提  如果是“PICKUP”则传P，否则都传D

        if (log != null && consignment.getPointCode() == null) {
            log.setReturnMessage("发货单号：" + consignment.getConsignmentId() + "的提货地点为空!");
            logManagerService.logEnd(iRequest, log);
            return body;
        }
        gdsHeader.setPointCode(consignment.getPointCode());
        // 中台是否分批，对应Retail全部交货
        gdsHeader.setSplitAllowed("N".equals(consignment.getSplitAllowed()) ? Constants.ORDER_TO_RETAIL_SPLIT : "");

        gdsHeader.setEXSYS(Constants.ORDER_TO_RETAIL_EXSYS);
        if(consignment.getStoreCode().equals("GLODMANTIS")){
            gdsHeader.setEXNID("JTL");
        }else{
            gdsHeader.setEXNID(Constants.ORDER_TO_RETAIL_EXSYS);
        }

        gdsHeader.setEXNAM(Constants.ORDER_TO_RETAIL_EXSYS);

        String time = null;
        String date1 = null;
        if (consignment.getOrderCreationtime() != null) {
            date1 = DateUtil.getdate(consignment.getOrderCreationtime(), "yyyy-MM-dd");
            time = DateUtil.getdate(consignment.getOrderCreationtime(), "HH:mm:ss");
        }
        gdsHeader.setOrderCreationday(date1);
        gdsHeader.setOrderCreationtime(time);

        gdsHeader.setOrderNumber(consignment.getOrderNumber());
        gdsHeader.setCode(consignment.getCode());

        body.setGdsHeader(gdsHeader);

        GdtCondtion condtion = new GdtCondtion();
        GdtItem item = new GdtItem();
        List<GdtCondtion.PriceItem> list = new ArrayList<>();
        List<GdtItem.entryItem> itemlist = new ArrayList<>();
        List<OrderEntry> orderEntryList = orderEntryService.selectRetailData(consignment.getConsignmentId());
        if (CollectionUtils.isNotEmpty(orderEntryList)) {
            List<OrderEntry> suitList = new ArrayList<>();  //保存套件订单行
            for (int j = 0; j < orderEntryList.size(); j++) {
                if (orderEntryList.get(j).getParentLine() == null) {
                    suitList.add(orderEntryList.get(j));
                }
            }

            //遍历套件行
            if (CollectionUtils.isNotEmpty(suitList)) {
                for (int k = 0; k < suitList.size(); k++) {

                    double shippingFeeTotal = 0.00;  //保存不是最后一个订单行的运费总和，用于减法，提高准确度
                    double installationFeeTotal = 0.00;//保存不是最后一个订单行的安装费总和，用于减法，提高准确度
                    double basePriceTotal = 0.00;//保存不是最后一个订单行的吊牌价总和，用于减法，提高准确度
                    double totalFeeTotal = 0.00;//保存不是最后一个订单行的应付金额总和，用于减法，提高准确度

                    Map<String, Object> map = new HashMap<>();
                    map.put("shippingFee", shippingFeeTotal);
                    map.put("installationFee", installationFeeTotal);
                    map.put("basePrice", basePriceTotal);
                    map.put("totalFee", totalFeeTotal);
                    map.put("flag", false);
                    map.put("orderEntry", suitList.get(k));
                    handlePriceAndLine(iRequest, map, list, itemlist, log);
                    if (log != null && log.getReturnMessage() != null) {
                        return body;
                    }

                    //获取该套件的所有组件行
                    List<OrderEntry> componentList = new ArrayList<>();   //保存组件订单行
                    for (int p = 0; p < orderEntryList.size(); p++) {
                        if (orderEntryList.get(p).getParentLine() != null && orderEntryList.get(p).getParentLine().equals(suitList.get(k).getOrderEntryId())) {
                            componentList.add(orderEntryList.get(p));
                        }
                    }

                    //遍历组件行
                    if (CollectionUtils.isNotEmpty(componentList)) {
                        //校验组件行的采购价格是否为空
                        for (OrderEntry orderEntry : componentList) {
                            if (log != null && orderEntry.getInternalPrice() == null) {
                                log.setReturnMessage("订单行Id：" + orderEntry.getOrderEntryId() + "为组件行，采购价格不能为空!");
                                logManagerService.logEnd(iRequest, log);
                                return body;
                            }
                        }

                        //对组件行的采购价格进行从低到高的排序，以防最后一行的采购价格为0，导致总价格对应不上
                        Collections.sort(componentList, new Comparator<OrderEntry>() {
                            @Override
                            public int compare(OrderEntry orderEntry1, OrderEntry orderEntry2) {
                                return orderEntry1.getInternalPrice().compareTo(orderEntry2.getInternalPrice());
                            }
                        });
                        for (int i = 0; i < componentList.size(); i++) {
                            if (i != componentList.size() - 1) {
                                //非最后一行
                                map.put("flag", false);
                            } else {
                                //最后一行
                                map.put("flag", true);
                            }
                            map.put("orderEntry", componentList.get(i));
                            handlePriceAndLine(iRequest, map, list, itemlist, log);

                            if (log != null && log.getReturnMessage() != null) {
                                return body;
                            }
                        }
                    }
                }
            }

        }

        condtion.setItems(list);
        item.setItems(itemlist);
        body.setGdtItem(item);
        body.setGdtCondtion(condtion);
        body.setGdtRetuen("");

        return body;
    }

    /**
     * 供订单更新retail根据发货单生成body方法，或者实现订单暂挂申请按钮
     *
     * @param iRequest
     * @param consignment
     * @param log         log为null，则是订单暂挂申请，反之则为订单变更retail
     * @return
     */
    @Override
    public OrderUpdateRequestbody getBodyForOrderUpdateToRetail(IRequest iRequest, Consignment consignment, LogManager log) {
        OrderUpdateRequestbody body = new OrderUpdateRequestbody();
        OrderUpdateRequestbody.GdsHeader gdsHeader = new OrderUpdateRequestbody.GdsHeader();

        gdsHeader.setSapCode(consignment.getSapCode());
        if (log != null && consignment.getSoldParty() == null) {
            log.setReturnMessage("发货单号：" + consignment.getConsignmentId() + "的售达方soldParty为空!");
            logManagerService.logEnd(iRequest, log);
            return body;
        }
        gdsHeader.setKUNNR(consignment.getSoldParty()); //售达方，根据order头中的webSideId去关联HMALL_MST_WEBSITE表中的code得到soldParty
        gdsHeader.setKUNNR2(Constants.ORDER_TO_RETAIL_KUNNR2);//传达方ZEST改为ONE
        gdsHeader.setOrg(Constants.ORDER_TO_RETAIL_ORG);
        gdsHeader.setVTWEG(Constants.ORDER_TO_RETAIL_VTWEG);
        if (log != null && consignment.getSalesOffice() == null) {
            log.setReturnMessage("发货单号：" + consignment.getConsignmentId() + "的销售办公室salesOffice为空!");
            logManagerService.logEnd(iRequest, log);
            return body;
        }
        gdsHeader.setVKBUR(consignment.getSalesOffice());//销售办公室，根据order头中的webSideId去关联HMALL_MST_WEBSITE表中的code得到saleOffice
        gdsHeader.setBrand(Constants.ORDER_TO_RETAIL_BRAND);//consignment.getBrand();
        gdsHeader.setCustomerID(consignment.getCustomerid());
        gdsHeader.setUserLevel(consignment.getUserLevel());
        gdsHeader.setReceiverName(consignment.getReceiverName());
        gdsHeader.setReceiverAddress(consignment.getReceiverAddress());
        String sex = "";
        if ((Constants.USER_SEX_M).equals(consignment.getSex())) {
            sex = Constants.USER_SEX_M_ZH;
        } else {
            sex = Constants.USER_SEX_F_ZH;
        }
        gdsHeader.setSex(sex);
        gdsHeader.setRegionCode(consignment.getRegionCode());

        if (StringUtils.isEmpty(consignment.getReceiverMobile()) && StringUtils.isEmpty(consignment.getReceiverPhone())) {
            //TODO 联系电话为空，报错
            if (log != null) {
                log.setReturnMessage("发货单号：" + consignment.getConsignmentId() + "," + "联系方式为空!");
                logManagerService.logEnd(iRequest, log);
            }
            return body;
        }
        gdsHeader.setReceiverMobile(consignment.getReceiverMobile());
        gdsHeader.setReceiverPhone(consignment.getReceiverPhone());
        gdsHeader.setNote(consignment.getNote());

        // 电商完结日期
//        String zzedats = generateZZEDATS(consignment);
//        logger.info("电商完结日期: " + consignment.getStatus() + ", " + consignment.getTradeFinishTime() + ", " + zzedats);
        if (consignment.getTradeFinishedTime() != null) {
            gdsHeader.setZZEDATS(DateUtil.getdate(consignment.getTradeFinishedTime(), "yyyy-MM-dd"));
        } else {
            gdsHeader.setZZEDATS("");
        }

        //四个留空的字段
        gdsHeader.setZZGLQHD("");
        gdsHeader.setAUGRU("");
        gdsHeader.setZZMARKETID1("");
        gdsHeader.setZZMARKETID2("");

        gdsHeader.setOrderType(Constants.ORDER_TO_RETAIL_ORDERTYPE);
        String date = null;
        if (consignment.getEstimateDeliveryTime() != null) {
            date = DateUtil.getdate(consignment.getEstimateDeliveryTime(), "yyyy-MM-dd");
        }
        gdsHeader.setEstimateDeliveryTime(date);
        gdsHeader.setZZHOPEDAY(date);

        String status = Constants.ORDER_TO_RETAIL_PAY_STATUS_YES;
        //若是订单更新retail则按以下规则赋值
        //若已付则传99（可发货）； 若未全付则传10（不可发货）consignment.getPayRate()

        if (log != null) {
            if ((Constants.ORDER_TO_RETAIL_CAN_DELIVERY).equals(consignment.getCanDelivery())) {
                status = Constants.ORDER_TO_RETAIL_PAY_STATUS_NO;
            }
        } else {
            if (consignment.getPause() != null && consignment.getPause().equals("Y")) {//取消暂挂
                if ((Constants.ORDER_TO_RETAIL_CAN_DELIVERY).equals(consignment.getCanDelivery())) {
                    status = Constants.ORDER_TO_RETAIL_PAY_STATUS_NO;
                } else {
                    status = Constants.ORDER_TO_RETAIL_PAY_STATUS_YES;
                }
            } else {//申请暂挂
                status = Constants.ORDER_TO_RETAIL_PAY_STATUS_YES;
            }

        }
        //若是订单暂挂申请retail则传“10”
        gdsHeader.setPayStatus(status);

        String shippingType = "";
        if ((Constants.ORDER_TO_RETAIL_SHIPPINGTYPE_PICKUP).equals(consignment.getShippingType())) {
            shippingType = Constants.ORDER_TO_RETAIL_SHIPPINGTYPE_P;
        } else {
            shippingType = Constants.ORDER_TO_RETAIL_SHIPPINGTYPE_L;
        }
        gdsHeader.setShippingType(shippingType); //D 配送  P 自提  如果是“PICKUP”则传P，否则都传D

        if (log != null && consignment.getPointCode() == null) {
            log.setReturnMessage("发货单号：" + consignment.getConsignmentId() + "的提货地点为空!");
            logManagerService.logEnd(iRequest, log);
            return body;
        }
        gdsHeader.setPointCode(consignment.getPointCode());
        // 中台是否分批，对应Retail全部交货
        gdsHeader.setSplitAllowed("N".equals(consignment.getSplitAllowed()) ? Constants.ORDER_TO_RETAIL_SPLIT : "");

        gdsHeader.setEXSYS(Constants.ORDER_TO_RETAIL_EXSYS);
        if(consignment.getStoreCode().equals("GLODMANTIS")){
            gdsHeader.setEXNID("JTL");
        }else{
            gdsHeader.setEXNID(Constants.ORDER_TO_RETAIL_EXSYS);
        }
        gdsHeader.setEXNAM(Constants.ORDER_TO_RETAIL_EXSYS);

        String time = null;
        String date1 = null;
        if (consignment.getOrderCreationtime() != null) {
            date1 = DateUtil.getdate(consignment.getOrderCreationtime(), "yyyy-MM-dd");
            time = DateUtil.getdate(consignment.getOrderCreationtime(), "HH:mm:ss");
        }
        gdsHeader.setOrderCreationday(date1);
        gdsHeader.setOrderCreationtime(time);

        gdsHeader.setOrderNumber(consignment.getOrderNumber());
        gdsHeader.setCode(consignment.getCode());

        //added by xiaoli.yu  begin

        gdsHeader.setModifyId(Constants.ORDER_TO_RETAIL_EXSYS); //默认MAP
        gdsHeader.setModifyName(Constants.ORDER_TO_RETAIL_EXSYS);   //默认MAP

        Date lastUpdatedate = consignment.getLastUpdateDateTime();
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
        String modifyDate = formatDate.format(lastUpdatedate);
        String modifyTime = formatTime.format(lastUpdatedate);

        gdsHeader.setModifyDate(modifyDate); // YYYYMMDD
        gdsHeader.setModifyTime(modifyTime);//  HHMMSS

        //added by xiaoli.yu  end

        body.setGdsHeader(gdsHeader);

        GdtCondtion condtion = new GdtCondtion();
        GdtItem item = new GdtItem();
        List<GdtCondtion.PriceItem> list = new ArrayList<>();
        List<GdtItem.entryItem> itemlist = new ArrayList<>();
        List<OrderEntry> orderEntryList = orderEntryService.selectRetailData(consignment.getConsignmentId());
        if (CollectionUtils.isNotEmpty(orderEntryList)) {
            List<OrderEntry> suitList = new ArrayList<>();  //保存套件订单行
            for (int j = 0; j < orderEntryList.size(); j++) {
                if (orderEntryList.get(j).getParentLine() == null) {
                    suitList.add(orderEntryList.get(j));
                }
            }

            //遍历套件行
            if (CollectionUtils.isNotEmpty(suitList)) {
                for (int k = 0; k < suitList.size(); k++) {

                    double shippingFeeTotal = 0.00;  //保存不是最后一个订单行的运费总和，用于减法，提高准确度
                    double installationFeeTotal = 0.00;//保存不是最后一个订单行的安装费总和，用于减法，提高准确度
                    double basePriceTotal = 0.00;//保存不是最后一个订单行的吊牌价总和，用于减法，提高准确度
                    double totalFeeTotal = 0.00;//保存不是最后一个订单行的应付金额总和，用于减法，提高准确度

                    Map<String, Object> map = new HashMap<>();
                    map.put("shippingFee", shippingFeeTotal);
                    map.put("installationFee", installationFeeTotal);
                    map.put("basePrice", basePriceTotal);
                    map.put("totalFee", totalFeeTotal);
                    map.put("flag", false);
                    map.put("orderEntry", suitList.get(k));
                    handlePriceAndLine(iRequest, map, list, itemlist, log);
                    if (log != null && log.getReturnMessage() != null) {
                        return body;
                    }

                    //获取该套件的所有组件行
                    List<OrderEntry> componentList = new ArrayList<>();   //保存组件订单行
                    for (int p = 0; p < orderEntryList.size(); p++) {
                        if (orderEntryList.get(p).getParentLine() != null && orderEntryList.get(p).getParentLine().equals(suitList.get(k).getOrderEntryId())) {
                            componentList.add(orderEntryList.get(p));
                        }
                    }

                    //遍历组件行
                    if (CollectionUtils.isNotEmpty(componentList)) {

                        //校验组件行的采购价格是否为空
                        for (OrderEntry orderEntry : componentList) {
                            if (log != null && orderEntry.getInternalPrice() == null) {
                                log.setReturnMessage("订单行Id：" + orderEntry.getOrderEntryId() + "是组件行，采购价格不能为空!");
                                logManagerService.logEnd(iRequest, log);
                                return body;
                            }
                        }

                        //对组件行的采购价格进行从低到高的排序,以防最后一行的采购价格为0，导致总价格对应不上
                        Collections.sort(componentList, new Comparator<OrderEntry>() {
                            @Override
                            public int compare(OrderEntry orderEntry1, OrderEntry orderEntry2) {
                                return orderEntry1.getInternalPrice().compareTo(orderEntry2.getInternalPrice());
                            }
                        });

                        for (int i = 0; i < componentList.size(); i++) {
                            if (i == componentList.size() - 1) {
                                //最后一行
                                map.put("flag", true);
                            } else {
                                //非最后一行
                                map.put("flag", false);
                            }
                            map.put("orderEntry", componentList.get(i));
                            handlePriceAndLine(iRequest, map, list, itemlist, log);
                            if (log != null && log.getReturnMessage() != null) {
                                return body;
                            }
                        }
                    }
                }
            }

        }
        condtion.setItems(list);
        item.setItems(itemlist);
        body.setGdtItem(item);
        body.setGdtCondtion(condtion);
        body.setGdtRetuen("");

        return body;
    }

    /**
     * 订单暂挂时，获取请求体，同时能够添加操作日志
     *
     * @param iRequest
     * @param consignment
     * @param log
     * @return
     */
    @Override
    public OrderUpdateRequestbody getBodyForOrderUpdateForConsignmentHold(IRequest iRequest, Consignment consignment, LogManager log) {
        return getBodyForOrderUpdateToRetail(iRequest, consignment, log);
    }


    /**
     * @param param
     * @param list
     * @param itemlist
     * @description 订单推送retail时 处理订单行的行项目及价格 避免代码重复
     */
    public void handlePriceAndLine(IRequest request, Map<String, Object> param, List<GdtCondtion.PriceItem> list, List<GdtItem.entryItem> itemlist, LogManager log) {
        OrderEntry orderEntry = (OrderEntry) param.get("orderEntry");
        double shippingFeeTotal = (double) param.get("shippingFee");
        double installationFeeTotal = (double) param.get("installationFee");
        double basePriceTotal = (double) param.get("basePrice");
        double totalFeeTotal = (double) param.get("totalFee");
        boolean flag = (boolean) param.get("flag");

        DecimalFormat df = new DecimalFormat("0.00");
        if (orderEntry.getParentLine() == null) {
            //不为组件行
            GdtCondtion.PriceItem p1 = new GdtCondtion.PriceItem();
            p1.setLineNumber(orderEntry.getLineNumber());
            p1.setPriceType(Constants.ORDER_TO_RETAIL_PRICE_ZD06);
            if (orderEntry.getShippingFee() != null) {
                p1.setPrice(Double.valueOf(df.format(orderEntry.getShippingFee())));
            }
            p1.setCurrency(Constants.ORDER_TO_RETAIL_PRICE_RMB);
            list.add(p1);
            GdtCondtion.PriceItem p2 = new GdtCondtion.PriceItem();
            p2.setLineNumber(orderEntry.getLineNumber());
            p2.setPriceType(Constants.ORDER_TO_RETAIL_PRICE_ZD07);
            if (orderEntry.getInstallationFee() != null) {
                p2.setPrice(Double.valueOf(df.format(orderEntry.getInstallationFee())));
            }
            p2.setCurrency(Constants.ORDER_TO_RETAIL_PRICE_RMB);
            list.add(p2);
            GdtCondtion.PriceItem p3 = new GdtCondtion.PriceItem();
            p3.setLineNumber(orderEntry.getLineNumber());
            p3.setPriceType(Constants.ORDER_TO_RETAIL_PRICE_ZP01);
            if (orderEntry.getBasePrice() != null) {
                p3.setPrice(Double.valueOf(df.format(orderEntry.getBasePrice())));
            }
            p3.setCurrency(Constants.ORDER_TO_RETAIL_PRICE_RMB);
            list.add(p3);
            GdtCondtion.PriceItem p4 = new GdtCondtion.PriceItem();
            p4.setLineNumber(orderEntry.getLineNumber());
            p4.setPriceType(Constants.ORDER_TO_RETAIL_PRICE_ZP00);
            if (orderEntry.getTotalFee() != null) {
                p4.setPrice(Double.valueOf(df.format(orderEntry.getTotalFee())));
            }
            p4.setCurrency(Constants.ORDER_TO_RETAIL_PRICE_RMB);
            list.add(p4);
            GdtCondtion.PriceItem p5 = new GdtCondtion.PriceItem();
            p5.setLineNumber(orderEntry.getLineNumber());
            p5.setPriceType(Constants.ORDER_TO_RETAIL_PRICE_ZD01);
            if (orderEntry.getBasePrice() != null && orderEntry.getUnitFee() != null && orderEntry.getQuantity() != null) {
                p5.setPrice(Double.valueOf(df.format((orderEntry.getBasePrice() - orderEntry.getUnitFee()) * orderEntry.getQuantity())));
            }
            p5.setCurrency(Constants.ORDER_TO_RETAIL_PRICE_RMB);
            list.add(p5);
        } else {
            //为组件行
            double rate = getPriceForComponentLine(request, orderEntry);

            GdtCondtion.PriceItem p1 = new GdtCondtion.PriceItem();
            p1.setLineNumber(orderEntry.getLineNumber());
            p1.setPriceType(Constants.ORDER_TO_RETAIL_PRICE_ZD06);
            p1.setCurrency(Constants.ORDER_TO_RETAIL_PRICE_RMB);

            GdtCondtion.PriceItem p2 = new GdtCondtion.PriceItem();
            p2.setLineNumber(orderEntry.getLineNumber());
            p2.setPriceType(Constants.ORDER_TO_RETAIL_PRICE_ZD07);
            p2.setCurrency(Constants.ORDER_TO_RETAIL_PRICE_RMB);

            GdtCondtion.PriceItem p3 = new GdtCondtion.PriceItem();
            p3.setLineNumber(orderEntry.getLineNumber());
            p3.setPriceType(Constants.ORDER_TO_RETAIL_PRICE_ZP01);
            p3.setCurrency(Constants.ORDER_TO_RETAIL_PRICE_RMB);

            GdtCondtion.PriceItem p4 = new GdtCondtion.PriceItem();
            p4.setLineNumber(orderEntry.getLineNumber());
            p4.setPriceType(Constants.ORDER_TO_RETAIL_PRICE_ZP00);
            p4.setCurrency(Constants.ORDER_TO_RETAIL_PRICE_RMB);

            if (flag) {
                //为最后一行
                if (orderEntry.getpShippingFee() != null) {
                    p1.setPrice(Double.valueOf(df.format(orderEntry.getpShippingFee() - shippingFeeTotal)));
                }
                if (orderEntry.getpInstallationFee() != null) {
                    p2.setPrice(Double.valueOf(df.format(orderEntry.getpInstallationFee() - installationFeeTotal)));
                }
                if (orderEntry.getpBasePrice() != null) {
                    p3.setPrice(Double.valueOf(df.format(orderEntry.getpBasePrice() - basePriceTotal)));
                }
                if (orderEntry.getpTotaFee() != null) {
                    p4.setPrice(Double.valueOf(df.format(orderEntry.getpTotaFee() - totalFeeTotal)));
                }
            } else {
                //为非最后一行
                if (orderEntry.getpShippingFee() != null) {
                    p1.setPrice(Double.valueOf(df.format(orderEntry.getpShippingFee() * rate)));
                    shippingFeeTotal = shippingFeeTotal + p1.getPrice();
                }
                if (orderEntry.getpInstallationFee() != null) {
                    p2.setPrice(Double.valueOf(df.format(orderEntry.getpInstallationFee() * rate)));
                    installationFeeTotal = installationFeeTotal + p2.getPrice();
                }
                if (orderEntry.getpBasePrice() != null) {
                    p3.setPrice(Double.valueOf(df.format(orderEntry.getpBasePrice() * rate)));
                    basePriceTotal = basePriceTotal + p3.getPrice();
                }
                if (orderEntry.getpTotaFee() != null) {
                    p4.setPrice(Double.valueOf(df.format(orderEntry.getpTotaFee() * rate)));
                    totalFeeTotal = totalFeeTotal + p4.getPrice();
                }
            }
            list.add(p1);
            list.add(p2);
            list.add(p3);
            list.add(p4);

            param.put("shippingFee", shippingFeeTotal);
            param.put("installationFee", installationFeeTotal);
            param.put("basePrice", basePriceTotal);
            param.put("totalFee", totalFeeTotal);
        }

        GdtItem.entryItem entryItem = new GdtItem.entryItem();
        entryItem.setLineNumber(orderEntry.getLineNumber());
        entryItem.setParentLine(orderEntry.getParentLineNumber());

        if (Constants.YES.equals(orderEntry.getIsGift())) {
            entryItem.setIsGift("X");
        } else {
            entryItem.setIsGift("");
        }

        if ("Y".equals(orderEntry.getIsSuit())) {
            entryItem.setOrderEntryType(Constants.ORDER_TO_RETAIL_ENTRY_TYPE_Z003);
        } else {
            if (log != null && orderEntry.getEntryType() == null) {
                log.setReturnMessage("订单行Id：" + orderEntry.getOrderEntryId() + "的项目类型为空!");
                logManagerService.logEnd(request, log);
                return;
            }
            entryItem.setOrderEntryType(orderEntry.getEntryType());
        }

        entryItem.setProductCode(orderEntry.getProductCode());
        entryItem.setQuantity(orderEntry.getQuantity());
        entryItem.setNote(orderEntry.getNote());
        //货源工厂
        if (log != null && orderEntry.getPointOfServiceCode() == null) {
            log.setReturnMessage("订单行Id：" + orderEntry.getOrderEntryId() + "的货源工厂为空!");
            logManagerService.logEnd(request, log);
            return;
        }
        entryItem.setZZWERKS(orderEntry.getPointOfServiceCode());
        entryItem.setLGORT(Constants.ORDER_TO_RETAIL_LGORT);

        String shipping = "";// 装运条件 如果是“PICKUP”则传P，否则都传D
        if ((Constants.ORDER_TO_RETAIL_SHIPPINGTYPE_PICKUP).equals(orderEntry.getShippingType())) {
            shipping = Constants.ORDER_TO_RETAIL_SHIPPINGTYPE_P;
        } else {
            shipping = Constants.ORDER_TO_RETAIL_SHIPPINGTYPE_L;
        }
        entryItem.setShippingType(shipping);

        //装运类型
        if (Constants.ORDER_TO_RETAIL_SHIPPINGTYPE_LOGISTICS.equals(orderEntry.getShippingType())) {
            entryItem.setZy04("20");
        } else if (Constants.ORDER_TO_RETAIL_SHIPPINGTYPE_EXPRESS.equals(orderEntry.getShippingType())) {
            entryItem.setZy04("30");
        }

        entryItem.setZY01(orderEntry.getProductSize());
        //当该字段值中存在“/“时，则传空值
        if (StringUtil.isNotEmpty(orderEntry.getProductPackedSize())) {
            String productPackedSize = orderEntry.getProductPackedSize();
            if (productPackedSize.indexOf("/") > 0) {
                entryItem.setZY02("");
            } else {
                entryItem.setZY02(productPackedSize);
            }
        } else {
            entryItem.setZY02("");
        }
        if (log != null && orderEntry.getCustomerMsg() != null && orderEntry.getCustomerMsg().length() > 132) {
            log.setReturnMessage("订单行Id：" + orderEntry.getOrderEntryId() + "的定制信息长度过长!");
            logManagerService.logEnd(request, log);
            return;
        }
        entryItem.setZY03(orderEntry.getCustomerMsg());

        //拒绝原因 如果订单行状态为CANCEL则传“DE”，否则留空
        if (Constants.ORDER_TO_RETAIL_ORDERENTRY_STATUS_C.equals(orderEntry.getOrderEntryStatus())) {
            entryItem.setRejectReason(Constants.ORDER_TO_RETAIL_ORDERENTRY_STATUS_DE);
        } else {
            entryItem.setRejectReason("");
        }

        if (log != null && orderEntry.getVproductCode() != null && orderEntry.getVproductCode().length() > 18) {
            log.setReturnMessage("订单行Id：" + orderEntry.getOrderEntryId() + "的V码长度过长!");
            logManagerService.logEnd(request, log);
            return;
        }
        entryItem.setVproductCode(orderEntry.getVproductCode());

        if (log != null && orderEntry.getPin() == null) {
            log.setReturnMessage("订单行Id：" + orderEntry.getOrderEntryId() + "的PIN码为空!");
            logManagerService.logEnd(request, log);
            return;
        }
        entryItem.setPin(orderEntry.getPin());
        //采购价格
        if (log != null && ((orderEntry.getSupplier() != null && ("MK05".equals(orderEntry.getSupplier()) || "MK10".equals(orderEntry.getSupplier()))) || StringUtil.isNotEmpty(orderEntry.getVproductCode()))
                && (orderEntry.getInternalPrice() == null || orderEntry.getInternalPrice() == 0)) {
            log.setReturnMessage("订单行Id：" + orderEntry.getOrderEntryId() + "的采购价不能为空!");
            logManagerService.logEnd(request, log);
            return;
        }
        if (orderEntry.getInternalPrice() != null) {
            entryItem.setKZWI2(String.valueOf(orderEntry.getInternalPrice()));
        } else {
            entryItem.setKZWI2("");
        }

        entryItem.setZBS("");

        //出厂日期
        String ZCCRQ = null;
        if (orderEntry.getEstimateConTime() != null) {
            ZCCRQ = DateUtil.getdate(orderEntry.getEstimateConTime(), "yyyy-MM-dd");
        }
        entryItem.setZCCRQ(ZCCRQ);

        //留空字段
        entryItem.setVGBEL("");
        entryItem.setVGPOS("");
        entryItem.setZZREQU("");
        entryItem.setZZKFREASON1("");
        entryItem.setZZKFREASON2("");
        entryItem.setZZTHREASON1("");
        entryItem.setZZTHREASON2("");

        itemlist.add(entryItem);
    }


    /**
     * @param request
     * @param dto
     * @description 订单推送retail时，为组建行计算价格比例
     */
    public double getPriceForComponentLine(IRequest request, OrderEntry dto) {
        double rate = 0;

        //根据子行，找出该子行对应的父行的所有子行
        OrderEntry entry = new OrderEntry();
        entry.setParentLine(dto.getParentLine());
        List<OrderEntry> list = orderEntryService.select(entry);
        if (CollectionUtils.isNotEmpty(list)) {
            double totalInternalPrice = 0.00;
            //获取所有子行的internalPrice总和
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getInternalPrice() != null) {
                    totalInternalPrice = totalInternalPrice + list.get(i).getInternalPrice();
                }
            }
            if (totalInternalPrice != 0.00 && dto.getInternalPrice() != null) {
                rate = dto.getInternalPrice() / totalInternalPrice;
            }
        }
        return rate;
    }

    /**
     * 重新加载发货单的所有字段
     *
     * @param consignment 发货单
     * @return
     */
    @Override
    public Consignment reload(Consignment consignment) {
        Assert.notNull(consignment, "consignment is null");
        Assert.notNull(consignment.getConsignmentId(), "consignmentId is null");
        consignment = this.selectByPrimaryKey(RequestHelper.newEmptyRequest(), consignment);
        return consignment;
    }

    /**
     * 发货单保存
     *
     * @param iRequest    request对象
     * @param consignment 发货单数据
     */
    @Override
    public void saveConsignment(IRequest iRequest, Consignment consignment) {

        // ESTIMATE_DELIVERY_TIME 预计交货时间
        // ESTIMATE_CON_TIME 预计发货时间
        Assert.notNull(consignment.getConsignmentId(), "传入发货单id不能为空");

        // 保存订单头信息
        consignment.setSyncflag(Constants.NO);
        consignment.setSyncZmall(Constants.NO);
        this.updateByPrimaryKeySelective(iRequest, consignment);

        // 修改订单行预计发货时间，如果没有订单行信息则无需修改
        if (CollectionUtils.isEmpty(consignment.getOrderEntries())) {
            return;
        }

        // 查询原发货单
        Consignment oriConsignment = this.selectByPrimaryKey(iRequest, consignment);
        Assert.notNull(oriConsignment, "发货单[" + consignment.getConsignmentId() + "]不存在");

        // 保存界面传入修改的订单行id
        List<Long> orderEntryIdList = new ArrayList<>();
        // 修改传入订单行的预计发货时间
        for (OrderEntry orderEntry : consignment.getOrderEntries()) {
            orderEntryIdList.add(orderEntry.getOrderEntryId());
            // 检查订单行参数
            Assert.notNull(orderEntry.getOrderEntryId(), "传入订单行id不能为空");
            Assert.notNull(orderEntry.getEstimateDeliveryTime(), "传入订单行预计交货时间不能为空");

            // 查询原订单行
            OrderEntry oriOrderEntry = iOrderEntryService.selectByPrimaryKey(iRequest, orderEntry);
            Assert.notNull(oriOrderEntry, "订单行[" + oriOrderEntry.getOrderEntryId() + "]不存在");

            // 查询仓库编码
            String pointOfServiceCode = iOrderEntryService.checkAndGetPosCodeOfOrderEntry(oriOrderEntry);

            // 计算预计交货时间
            Integer logisticsLeadTime = getLogisticsLeadTime(oriConsignment.getReceiverCity(), oriConsignment.getReceiverDistrict(), pointOfServiceCode, oriOrderEntry.getShippingType());

            // 计算物流提前期，并更新预计发货时间
            Date estimateConTime = DateUtils.addDays(orderEntry.getEstimateDeliveryTime(), -logisticsLeadTime);
            oriOrderEntry.setEstimateConTime(estimateConTime);
            oriOrderEntry.setEstimateDeliveryTime(orderEntry.getEstimateDeliveryTime());
            iOrderEntryService.updateByPrimaryKeySelective(iRequest, oriOrderEntry);
        }

        // 获取原订单的所有订单行，并计算最晚预计交货时间
        Long orderId = oriConsignment.getOrderId();
        OrderEntry orderEntryParam = new OrderEntry();
        orderEntryParam.setOrderId(orderId);
        List<OrderEntry> orderEntryList = iOrderEntryService.select(orderEntryParam);

        Date maxDate = orderEntryList.stream().filter(orderEntry -> {
            Assert.notNull(orderEntry.getEstimateDeliveryTime(), "订单行[" + orderEntry.getOrderEntryId() + "]预计交货时间不能为空");
            return true;
        }).map(OrderEntry::getEstimateDeliveryTime).max((date1, date2) -> date1.compareTo(date2)).get();

        // 修改原订单的交货时间为最晚预计交货时间
        Order order = new Order();
        order.setOrderId(orderId);
        order = orderMapper.selectByPrimaryKey(order);
        Assert.notNull(order, "订单[" + orderId + "]不存在");
        if (CollectionUtils.isNotEmpty(consignment.getOrderEntries())) {
            // 如果更新了订单行的预计交货时间，更新订单的同步官网标志为N
            order.setSyncZmall(Constants.NO);
        }

        order.setEstimateDeliveryTime(maxDate);
        orderMapper.updateByPrimaryKeySelective(order);

        // 获取发货单下所有订单行
        OrderEntry conOrderEntryParam = new OrderEntry();
        conOrderEntryParam.setConsignmentId(consignment.getConsignmentId());
        List<OrderEntry> conOrderEntries = iOrderEntryService.select(conOrderEntryParam);

        // 获取并修改发货单的最晚交货时间
        Date conMaxDate = conOrderEntries.stream().map(OrderEntry::getEstimateDeliveryTime).max((date1, date2) -> date1.compareTo(date2)).get();
        oriConsignment.setEstimateDeliveryTime(conMaxDate);
        this.updateByPrimaryKeySelective(iRequest, oriConsignment);

        // 更新发货单行下所有行的预计交货时间和预计发货时间
        for (OrderEntry orderEntry : conOrderEntries) {

            // 过滤掉界面传入的订单行，这些订单行的预计交货时间和预计发货时间已经在上面的逻辑中修改
            if (!orderEntryIdList.contains(orderEntry.getOrderEntryId())) {
                // 更新预计交货时间和发货单头一致
                orderEntry.setEstimateDeliveryTime(conMaxDate);

                // 查询订单行上的仓库编码

                String pointOfServiceCode = iOrderEntryService.checkAndGetPosCodeOfOrderEntry(orderEntry);

                // 计算物流提前提
                Integer logisticsLeadTime = getLogisticsLeadTime(oriConsignment.getReceiverCity(), oriConsignment.getReceiverDistrict(), pointOfServiceCode, orderEntry.getShippingType());
                // 预计发货时间等于预计交货时间减去物流提前期
                orderEntry.setEstimateConTime(DateUtils.addDays(conMaxDate, -logisticsLeadTime));
                iOrderEntryService.updateByPrimaryKeySelective(iRequest, orderEntry);
            }

        }

    }

    /**
     * 根据城市编码、区域编码、仓库编码获取物流提前期
     *
     * @param cityCode           城市编码
     * @param areaCode           区域编码
     * @param pointOfServiceCode 仓库编码
     * @param shippingType       发运方式
     * @return
     */
    @Override
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
        List<AtpInvSourceInfo> atpInvSourceInfoList = iAtpInvSourceInfoService.select(RequestHelper.newEmptyRequest(), atpInvSourceInfoParam, 1, Integer.MAX_VALUE);
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
     * 查询可发货的发货单
     *
     * @return
     */
    @Override
    public List<Consignment> selectCanBeShippedConsignments() {
        return mapper.selectCanBeShippedConsignments();
    }

    /**
     * 可发运判断
     */
    @Override
    public void judgeCanBeShipped() {
        // 查询满足条件的发货单
        Example example = new Example(Consignment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status", Constants.CON_STATUS_WAIT_FOR_DELIVERY);  // 待发货
        criteria.andEqualTo("canDelivery", Constants.NO); // 可发运标识为N
        criteria.andIsNotNull("orderId"); // 订单id不能为空

        List<Consignment> consignmentList = mapper.selectByExample(example);
        logManagerService.logTrace(this.getClass(), "全款可发运判断job", null, "发现[" + CollectionUtils.size(consignmentList) + "]张发货单");
        if (CollectionUtils.isEmpty(consignmentList)) {
            return;
        }

        // 根据订单对发货单进行分组，同一订单下的发货单可发运条件是一致的
        Map<Long, List<Consignment>> orderConsignmentMap = consignmentList.stream().collect(Collectors.groupingBy(Consignment::getOrderId));
        for (Long orderId : orderConsignmentMap.keySet()) {
            // 根据订单id查询订单，用来判断付款比例
            Order order = new Order();
            order.setOrderId(orderId);
            order = orderMapper.selectByPrimaryKey(order);

            // 过滤非法订单
            if (order == null) {
                iLogManagerService.logTrace(this.getClass(), "全款可发运判断job", orderId, "订单[" + orderId + "]不存在");
                continue;
            }

            // 判断实际付款金额是否为空
            if (order.getPaymentAmount() == null) {
                iLogManagerService.logTrace(this.getClass(), "全款可发运判断job", orderId, "订单[" + orderId + "]实际付款金额不能为空");
                continue;
            }

            // 计算付款比例
            // 如果HMALL_OM_ORDER.CURRENT_AMOUNT为空，则计算PAYMENT_AMOUNT/ORDER_AMOUNT的值；否则计算PAYMENT_AMOUNT/CURRENT_AMOUNT
            Double payRate;
            if (order.getOrderAmount() != null && order.getOrderAmount().doubleValue() != 0D) {
                if (order.getCurrentAmount() != null && order.getCurrentAmount() != 0D) {
                    payRate = order.getPaymentAmount() / order.getCurrentAmount();
                } else {
                    payRate = order.getPaymentAmount() / order.getOrderAmount().doubleValue();
                }
            } else {//新增如果订单金额为0 2017-12-11
                payRate = 1D;
            }


            // 判断是否全额付款
            if (payRate >= 1D) {
                for (Consignment consignment : orderConsignmentMap.get(orderId)) {
                    consignment.setCanDelivery(Constants.YES);
                    consignment.setSyncflag(Constants.NO);
                    mapper.updateByPrimaryKeySelective(consignment);
                }
            } else {
                String payRatePercent = payRate * 100 + "%";
                iLogManagerService.logTrace(this.getClass(), "全款可发运判断job", orderId, "订单[" + orderId + "]付款比例[" + payRatePercent + "]，未全额付款");
            }
        }
    }

    /**
     * 合批发货单
     *
     * @param iRequest
     * @param consignmentList
     * @return
     */
    @Override
    public String mergeConsignment(IRequest iRequest, List<Consignment> consignmentList) {
        try {
            if (consignmentList.size() > 0) {
                Boolean flag = false;
                for (Consignment consignment : consignmentList) {
                    if ((!consignment.getStatus().equals("ABNORMAL")) && (!consignment.getStatus().equals("WAIT_FOR_DELIVERY"))) {
                        flag = true;
                    }
                }
                if (flag) {
                    return "仅异常和待发货发货单能够合批！";
                }
                Order order = new Order();
                order.setOrderId(consignmentList.get(0).getOrderId());
                Order orderInfo = orderMapper.selectByPrimaryKey(order);
                Random random = new Random();
                int number = random.nextInt(100);
                String code = String.valueOf(number);
                if (number < 10) {
                    code = "0" + code;
                }
                String mergeNumber = orderInfo.getEscOrderCode() + System.currentTimeMillis() + code;
                for (Consignment consignment : consignmentList) {
                    consignment.setMergeConsignment(mergeNumber);
                    mapper.updateByPrimaryKeySelective(consignment);
                }

            }
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }


    }

    /**
     * 取消合批发货单
     *
     * @param iRequest
     * @param consignmentList
     * @return
     */
    @Override
    public String cancelMergeConsignment(IRequest iRequest, List<Consignment> consignmentList) {
        try {

            if (consignmentList.size() > 0) {
                for (Consignment con : consignmentList) {
                    if (con.getMergeConsignment() == null || con.getMergeConsignment().equals("")) {
                        return "合批号不能为空！";
                    }
                }
                Consignment con = new Consignment();
                con.setOrderId(consignmentList.get(0).getOrderId());
                con.setMergeConsignment(consignmentList.get(0).getMergeConsignment());
                List<Consignment> consignments = mapper.select(con);
                String mergeNumber = consignmentList.get(0).getMergeConsignment();
                for (Consignment consignment : consignmentList) {
                    if (!mergeNumber.equals(consignment.getMergeConsignment())) {
                        return "同一合批号才能够取消合批！";
                    }
                }
                for (Consignment consignment : consignments) {
                    if ((!consignment.getStatus().equals("ABNORMAL")) && (!consignment.getStatus().equals("WAIT_FOR_DELIVERY"))) {
                        return "仅同一合批号下的所有发货单都是异常和待发货才能够取消合批！";
                    }
                }


                for (Consignment consignment : consignmentList) {
                    consignment.setMergeConsignment("");
                    mapper.updateByPrimaryKeySelective(consignment);
                }
            }
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private Properties getProperties() {
        Properties properties = new Properties();
        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    /**
     * 发货单审核
     *
     * @param iRequest       请求对象
     * @param consignment    发货单
     * @param markorEmployee 审核人员
     */
    @Override
    public void consignmentCheck(IRequest iRequest, Consignment consignment, MarkorEmployee markorEmployee) {
        consignmentProcessService.abnormalJudgment(consignment, markorEmployee);
    }

    /**
     * 发货单确认收货
     *
     * @param consignment
     * @return
     */
    @Override
    public void consignmentConfirmFinish(Consignment consignment) throws BusinessException {

        Consignment con = mapper.selectByPrimaryKey(consignment);
        if (con == null) {
            throw new BusinessException("发货单不存在");
        }
        if (con.getStatus().equals("TRADE_BUYER_SIGNED")) {
            throw new BusinessException("发货单已确认收货");
        }
        Order od = new Order();
        od.setOrderId(con.getOrderId());
        Order order = orderMapper.selectByPrimaryKey(od);
        if (order == null) {
            throw new BusinessException("发货单对应订单不存在");
        }
        //将系统当前时间记TRADE_FINISHED_TIME ，发货单头状态置为“TRADE_BUYER_SIGNED”；
        con.setStatus("TRADE_BUYER_SIGNED");
        con.setTradeFinishedTime(new Date());

        order.setTradeFinishTime(new Date());
        if (order.getOrderType().equals("NORMAL")) {//ORDER_TYPE=NORMAL(销售单)，则TRADE_FINISHED_TIME记录系统当前时间，订单状态ORDER_STATUS更新为TRADE_BUYER_SIGNED；
            order.setOrderStatus("TRADE_BUYER_SIGNED");

        } else if (order.getOrderType().equals("SWAP")) {//ORDER_TYPE=SWAP(换发单)，则TRADE_FINISHED_TIME记录系统当前时间，订单状态ORDER_STATUS更新为TRADE_FINISHED，并将推送retail的SYNCFLAG更新为N
            order.setOrderStatus("TRADE_FINISHED");
            con.setSyncflag("N");
        }
        mapper.updateByPrimaryKeySelective(con);
        orderMapper.updateByPrimaryKeySelective(order);

    }
}