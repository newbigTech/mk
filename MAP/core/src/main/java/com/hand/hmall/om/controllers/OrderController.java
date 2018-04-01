package com.hand.hmall.om.controllers;

import com.alibaba.fastjson.JSON;
import com.hand.common.util.Auth;
import com.hand.common.util.ExcelUtil;
import com.hand.common.util.SecretKey;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.common.service.ISequenceGenerateService;
import com.hand.hmall.mst.dto.MstUser;
import com.hand.hmall.mst.dto.Product;
import com.hand.hmall.mst.service.IMstUserService;
import com.hand.hmall.mst.service.IProductService;
import com.hand.hmall.om.dto.*;
import com.hand.hmall.om.service.IOmPromotionruleService;
import com.hand.hmall.om.service.IOrderCouponruleService;
import com.hand.hmall.om.service.IOrderEntryService;
import com.hand.hmall.om.service.IOrderService;
import com.hand.hmall.process.order.service.IOrderProcessService;
import com.hand.hmall.util.Constants;
import com.markor.map.external.pointservice.dto.PointOfServiceDto;
import com.markor.map.external.pointservice.service.IPointOfServiceExternalService;
import com.markor.map.framework.restclient.RestClient;
import jodd.util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import okhttp3.Response;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhangmeng
 * @version 0.1
 * @name OrderController
 * @description 订单查询页面
 * @date 2017/5/22
 */
@Controller
public class OrderController extends BaseController {

    //订单表中预计交货时间配置参数
    private static final String VIRTUALORDER_ORDER_ESTIMATEDELIVERYTIME = "virtualOrder.order.estimateDeliveryTime";
    //订单表中增加预计发货时间配置参数
    private static final String VIRTUALORDER_ORDER_ESTIMATECONTIME = "virtualOrder.order.estimateConTime";
    //订单行表中预计交货时间配置参数
    private static final String VIRTUALORDER_ORDERENTRY_ESTIMATEDELIVERYTIME = "virtualOrder.orderEntry.estimateDeliveryTime";
    //订单行表中预计发货时间配置参数
    private static final String VIRTUALORDER_ORDERENTRY_ESTIMATECONTIME = "virtualOrder.orderEntry.estimateConTime";
    //订单行表中仓库/门店配置参数
    private static final String VIRTUALORDER_ORDERENTRY_POINTOFSERVICE = "virtualOrder.orderEntry.pointOfService";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    SimpleDateFormat sdfForValidate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private IOrderService service;

    @Autowired
    private IOrderEntryService iOrderEntryService;

    @Autowired
    private IOrderProcessService iOrderProcessService;

    @Autowired
    private IProductService iProductService;

    @Autowired
    private RestClient restClient;

    @Autowired
    private ISequenceGenerateService iSequenceGenerateService;

    @Autowired
    private IMstUserService mstUserService;

    @Autowired
    private IPointOfServiceExternalService iPointOfServiceExternalService;

    private RestTemplate restTemplate = new RestTemplate();

    @Value("#{configProperties['baseUri']}")
    private String baseUri;

    @Autowired
    private IOrderCouponruleService orderCouponruleService;
    @Autowired
    private IOmPromotionruleService omPromotionruleService;

    /**
     * 订单列表查询
     *
     * @param maps
     * @return 订单列表集合
     */
    @RequestMapping(value = "/hmall/om/order/query")
    @ResponseBody
    public ResponseData query(HttpServletRequest request, @RequestParam Map maps) {

        IRequest requestCtx = createRequestContext(request);

        String str = (String) maps.get("data");
        JSONObject jsonObject = JSONObject.fromObject(str);
        JSONObject status = jsonObject.getJSONObject("status");
        //订单状态
        JSONArray orderStatus_ = status.getJSONArray("orderStatus_");
        String[] strOrderStatus = new String[orderStatus_.size()];
        for (int i = 0; i < orderStatus_.size(); i++) {
            strOrderStatus[i] = orderStatus_.get(i).toString();
        }
        //配送方式
        JSONArray distribution = status.getJSONArray("distribution");
        String[] strDistribution = new String[distribution.size()];
        for (int i = 0; i < distribution.size(); i++) {
            strDistribution[i] = distribution.get(i).toString();
        }
        //订单类型
        JSONArray orderTypes = status.getJSONArray("orderTypes__");
        String[] strOrderTypes = new String[orderTypes.size()];
        for (int i = 0; i < orderTypes.size(); i++) {
            strOrderTypes[i] = orderTypes.get(i).toString();
        }

        Map<String, Object> data = (Map<String, Object>) jsonObject.get("pages");
        //订单号
        String code = "";
        if (data.get("code") != null) {
            code = data.get("code").toString();
           /* if (!isNumeric(orderId)) {
                orderId = "";
            }*/
        }
        //用户Id
        String userId = "";
        if (data.get("userId") != null) {
            userId = data.get("userId").toString();
            if (!isNumeric(userId)) {
                userId = "";
            }
        }
        //订单是否锁定
        String locked = "";
        if (data.get("locked") != null) {
            locked = data.get("locked").toString();
        }
        String payRate = "";
        if (data.get("payRate") != null) {
            payRate = data.get("payRate").toString();
        }
        //收货人手机号
        String receiverMobile = "";
        if (data.get("receiverMobile") != null) {
            receiverMobile = data.get("receiverMobile").toString();
            if (!isNumeric(receiverMobile)) {
                receiverMobile = "";
            }
        }
        //开始时间
        String startTime = "";
        if (data.get("startTime") != null) {
            startTime = data.get("startTime").toString();
        }
        //结束时间
        String endTime = "";
        if (data.get("endTime") != null) {
            endTime = data.get("endTime").toString();
        }
        //变式物料号
        String vproduct = "";
        if (data.get("vproduct") != null) {
            vproduct = data.get("vproduct").toString();
        }
        String escOrderCode = "";
        if (data.get("escOrderCode") != null) {
            escOrderCode = data.get("escOrderCode").toString();
        }
        //商品编码
        String productId = "";
        if (data.get("productId") != null) {
            productId = data.get("productId").toString();
        }
        /*add by heng.zhang 20170922 MAG-1200  PIN码*/
        String pinCode = "";
        if (data.get("pinCode") != null) {
            pinCode = data.get("pinCode").toString();
        }
        /*end*/
        int page = 0;
        if (data.get("page") != null) {
            page = Integer.parseInt(data.get("page").toString());
        }
        int pagesize = 0;
        if (data.get("pagesize") != null) {
            pagesize = Integer.parseInt(data.get("pagesize").toString());
        }

        // 网站ID和虚拟订单标识
        String websiteId = data.containsKey("websiteId") ? data.get("websiteId").toString() : null;
        String isIo = data.containsKey("isIo") ? data.get("isIo").toString() : null;

        // 订单归属标识 orderBelong
        String orderBelong = data.containsKey("orderBelong") ? data.get("orderBelong").toString() : null;
        Long affiliationId = null;
        if ("C".equals(orderBelong)) {
            affiliationId = requestCtx.getUserId();
        } else if ("N".equals(orderBelong)) {
            affiliationId = 0L;
        }

        List<Order> list = service.selectOrderList(
                page, pagesize, code, escOrderCode, userId, locked, receiverMobile, startTime, endTime, strOrderStatus,
                strDistribution, strOrderTypes, vproduct, productId, payRate, pinCode, websiteId, isIo, affiliationId);
        return new ResponseData(list);
    }

    @RequestMapping(value = "/hmall/om/order/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<Order> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hmall/om/order/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<Order> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    /**
     * 查询退款单信息
     *
     * @param request
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/hmall/om/order/selectRefundOrderInfoByOrderId")
    @ResponseBody
    public ResponseData selectRefundOrderInfoByOrderId(HttpServletRequest request, @RequestParam("orderId") Long orderId) {
        return new ResponseData(service.selectRefundOrderInfoByOrderId(orderId));
    }

    /**
     * 查询订单
     *
     * @param orderId
     * @param request
     * @return
     */
    @RequestMapping(value = "/hmall/om/order/queryByOrderId")
    @ResponseBody
    public ResponseData queryByOrderId(@RequestParam("orderId") Long orderId, HttpServletRequest request) {
        IRequest requestCtx = createRequestContext(request);
        Order order = new Order();
        order.setOrderId(orderId);
        ResponseData result = new ResponseData(Arrays.asList(service.selectByPrimaryKey(requestCtx, order)));
        return result;
    }

    /**
     * @param str
     * @return
     * @description 判断检索条件是否为数字，数据库中数据类型为数字 kendoui没找到合适的控制方式暂时由后台验证
     */
    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 订单详情解锁功能
     *
     * @param request
     * @param orderList 订单实体类集合
     * @return
     */
    @RequestMapping(value = "/hmall/om/order/releasedLock")
    @ResponseBody
    public ResponseData releasedLock(HttpServletRequest request, @RequestBody List<Order> orderList) {
        IRequest iRequest = createRequestContext(request);
        List<Order> resultList = new ArrayList<Order>();
        if (CollectionUtils.isNotEmpty(orderList)) {
            for (Order order : orderList) {
                Order persistentOrder = service.selectByPrimaryKey(iRequest, order);
                persistentOrder.setLocked("N");
                resultList.add(service.updateByPrimaryKeySelective(iRequest, persistentOrder));
            }
        }
        return new ResponseData(resultList);
    }


    @RequestMapping(value = "/hmall/om/order/queryInfo")
    @ResponseBody
    public ResponseData query(Order dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectInfoByOrderId(dto));
    }

    /**
     * 导出订单列表excel
     *
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "/om/order/exportData", method = RequestMethod.GET)
    public void exportData(HttpServletRequest request, HttpServletResponse response, @RequestParam String code, @RequestParam String userId, @RequestParam String receiverMobile, @RequestParam String startTime, @RequestParam String endTime, @RequestParam String vproduct, @RequestParam String productId, @RequestParam String strOrderStatus, @RequestParam String strDistribution, @RequestParam String pinCode) {
        IRequest iRequest = RequestHelper.createServiceRequest(request);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //开始时间
        String sTime = "";
        if (!"".equals(startTime)) {
            Date sDate = new Date(startTime);
            sTime = sdf.format(sDate);
        }
        //结束时间
        String eTime = "";
        if (!"".equals(endTime)) {
            Date eDate = new Date(endTime);
            eTime = sdf.format(eDate);
        }
        //订单状态
        String[] strOrderStatusArray = strOrderStatus.split(",");
        //配送方式
        String[] distributionArray = strDistribution.split(",");
        if (distributionArray.length > 0) {
            if ("".equals(distributionArray[0])) {
                distributionArray = null;
            }
        }
        if (strOrderStatusArray.length > 0) {
            if ("".equals(strOrderStatusArray[0])) {
                strOrderStatusArray = null;
            }
        }
        List<Order> list = service.selectOrderList(iRequest, code, userId, receiverMobile, sTime, eTime, strOrderStatusArray, distributionArray, vproduct, productId, pinCode);
        new ExcelUtil(Order.class).exportExcel(list, "订单列表", list.size(), request, response, "订单列表.xlsx");
    }

    /**
     * 导出天猫订单的发货单信息
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/om/order/exportTMData", method = RequestMethod.GET)
    public void exportTMData(HttpServletRequest request, HttpServletResponse response) {
        List<TmData> list = service.exportTmData();
        new ExcelUtil(TmData.class).exportExcel(list, "天猫发货单列表", list.size(), request, response, "天猫发货单列表.xlsx");
    }

    /**
     * 诡异的方法，目前没发现这个接口给其他系统提供过
     */
    @RequestMapping(value = "/api/public/hmall/om/order/gensuite")
    @ResponseBody
    @Deprecated
    public ResponseData gensuite(@RequestParam("orderId") Long orderId) {
        ResponseData responseData = new ResponseData();
        Order order = new Order();
        order.setOrderId(orderId);
        order = service.selectByPrimaryKey(RequestHelper.newEmptyRequest(), order);
        try {
            List<OrderEntry> orderEntries = iOrderProcessService.generateSuiteComponents(order);
            responseData.setSuccess(true);
            responseData.setRows(orderEntries);
            return responseData;
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
            return responseData;
        }
    }

    @RequestMapping(value = "/hmall/om/order/querySyncZmallOrder")
    @ResponseBody
    public ResponseData querySyncZmallOrder(HttpServletRequest request, @RequestParam("orderId") Long orderId, @RequestParam("code") String code) {

        String zmallSecretKey = service.getProperties().getProperty("zmall.access.secret.key");

        // 当前用户ID
        Long currentUserId = createRequestContext(request).getUserId();

        ResponseData response = null;

        // 订单信息
        OrderSyncDto order = service.querySyncZmallOrderForAddEntry(orderId);

        if (order == null) {
            response = new ResponseData(false);
            response.setMessage("找不到符合推送要求的订单。" +
                    "\n订单可能处于“已完成”、“已关闭”、“已取消”状态，或者订单类型不是销售单，或者订单已被锁定。" +
                    "\n[订单ID=" + orderId + ";订单编码=" + code + "]");
            return response;
        }

        // 同步订单数据
        String jsonString = JSON.toJSONString(order);

        Map<String, String> map = new HashMap<>();
//        map.put("token", Auth.md5(zmallSecretKey + jsonString));
        map.put("token", Auth.md5(SecretKey.KEY + jsonString));

        // 同步订单响应信息
        Response zmallResponse;

        try {

            zmallResponse = restClient.postString(Constants.ZMALL, "/zmallsync/orderSync", jsonString, "json", map, null);

            String bodyStr = zmallResponse.body().string();

            JSONObject jsonResult = JSONObject.fromObject(bodyStr);

            if ("S".equals(jsonResult.getString("code"))) { // 接口调用成功
                // 调用商城新增订单行接口，将当前操作用户账号和当前操作订单号作为参数传递
                StringBuilder zmallCreateOrderEntryURLFragment
                        = new StringBuilder("type=update&employeeId=").append(currentUserId.toString())
                        .append("&escOrderCode=").append(order.getEscOrderCode())
                        .append("&orderStatus=").append(order.getOrderStatus());

//                String token = Auth.md5(zmallSecretKey + zmallCreateOrderEntryURLFragment.toString());
                String token = Auth.md5(SecretKey.KEY + "_" + zmallCreateOrderEntryURLFragment.toString());

                StringBuilder zmallCreateOrderEntryURL
                        = new StringBuilder(service.getZmallWebsiteAddress()).append("/ordermanager.html?")
                        .append(zmallCreateOrderEntryURLFragment.toString())
                        .append("&token=").append(token);

                // 返回前锁定订单状态
                service.lockOrderStatus(orderId);

                return new ResponseData(Arrays.asList(zmallCreateOrderEntryURL.toString()));

            } else if ("E".equals(jsonResult.getString("code"))) { // 接口调用失败
                response = new ResponseData(false);
                response.setMessage("调用商城接口返回错误信息：" + jsonResult.getString("message"));
            } else { // 接口返回状态码异常
                response = new ResponseData(false);
                response.setMessage("调用商城新增订单行接口返回了不正确的状态码" + jsonResult.getString("code"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    /**
     * 订单详情页面订单保存
     *
     * @param request 请求对象
     * @param order   订单对象
     * @return ResponseData
     */
    @RequestMapping(value = "/hmall/om/order/saveOrder", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData saveOrder(HttpServletRequest request, @RequestBody Order order) {
        ResponseData responseData = new ResponseData();
        IRequest iRequest = RequestHelper.createServiceRequest(request);
        order.setSyncflag(Constants.NO);
        try {
            service.saveOrderFunc(iRequest, order);
            responseData.setSuccess(true);
            responseData.setMessage("保存成功");
            return responseData;
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
            return responseData;
        }
    }

    /**
     * 虚拟订单的导入
     *
     * @param request
     * @param files
     * @return ResponseData
     * @throws IOException
     * @throws InvalidFormatException
     */
    @RequestMapping(value = {"/hmall/om/order/virtualOrderUpdate"}, method = {RequestMethod.POST})
    @ResponseBody
    public ResponseData upload(HttpServletRequest request, MultipartFile files) throws IOException, InvalidFormatException {
        int i = 0;
        List<String> errMsg = new ArrayList<String>();
        List<OrderEntry> orderEntryList = new ArrayList<OrderEntry>();
        boolean importResult = false;
        String message = "success"; // 方法执行结果消息
        IRequest iRequest = this.createRequestContext(request);
        CommonsMultipartResolver multipartResolver
                = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (multipartResolver.isMultipart(request)
                && (request instanceof MultipartHttpServletRequest)) {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            Iterator<String> fileNames = multipartRequest.getFileNames();
            if (fileNames.hasNext()) {
                // 获得上传文件
                MultipartFile file = multipartRequest.getFile(fileNames.next());
                // 文件输入流
                InputStream is = null;
                try {
                    is = file.getInputStream();
                    // 调用工具类解析上传的Excel文件
                    List<VirtualOrderImportDto> infos
                            = new ExcelUtil(VirtualOrderImportDto.class)
                            .importExcel(VirtualOrderImportDto.DEFAULT_EXCEL_FILE_NAME,
                                    VirtualOrderImportDto.DEFAULT_SHEET_NAME + "0",
                                    is);
                    if (infos.size() == 0) {
                        message = "请下载Excel模板并输入数据";
                    }
                    errMsg.add("导入EXCEL文件的");
                    Order order = new Order();
                    //以IO开头,后面跟9位数字
                    String code = iSequenceGenerateService.getNextIOOrderCode();
                    order.setCode(code);
                    order.setEscOrderCode(code);
                    order.setOrderStatus("NEW_CREATE");
                    order.setPostFee(new BigDecimal("0"));
                    order.setEpostFee(new BigDecimal("0"));
                    order.setFixFee(new BigDecimal("0"));
                    order.setPrePostfee(new BigDecimal("0"));
                    order.setPreEpostfee(new BigDecimal("0"));
                    order.setPreFixfee(new BigDecimal("0"));
                    //用户表中customerid字段值为"TMIO"的userId值
                    List<MstUser> mstUsers = mstUserService.selectByCustomerId("TMIO");
                    if (mstUsers.size() == 0) {
                        //如果数据库中customerId为"TMIO"的数据不存在,抛异常
                        throw new Exception("TMIO对应的用户数据不存在");
                    }
                    order.setUserId(mstUsers.get(0).getUserId());
                    order.setCurrencyId("CNY");
                    order.setWebsiteId("TM");
                    order.setSalechannelId("1");
                    order.setStoreId("TMALL-ZEST");
                    order.setPaymentAmount(Double.parseDouble("9000"));
                    order.setOrderAmount(new BigDecimal("9999"));
                    order.setOrderCreationtime(new Date());
                    order.setIsInvoiced("N");
                    order.setInvoiceType("N");
                    order.setTotalcon("N");
                    order.setReceiverName("恣在家天猫");
                    //默认为中国的国家编码
                    order.setReceiverCountry("CN");
                    //默认为天津市的省编码，120000
                    order.setReceiverState("120000");
                    //默认为天津市的市编码，
                    order.setReceiverCity("120100");
                    //默认为滨海新区的区编码
                    order.setReceiverDistrict("120116");
                    order.setReceiverAddress("美克美家");
                    order.setReceiverMobile("13888888888");
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    //推迟时间在配置文件中取,当前时间推后20天
                    Date orderEstimateDeliveryDate = getDelayDate(calendar, VIRTUALORDER_ORDER_ESTIMATEDELIVERYTIME);
                    order.setEstimateDeliveryTime(orderEstimateDeliveryDate);
                    //推迟时间在配置文件中取，当前时间推后18天
                    Date orderEstimateConDate = getDelayDate(calendar, VIRTUALORDER_ORDER_ESTIMATECONTIME);
                    order.setEstimateConTime(orderEstimateConDate);
                    //付款比例为付款金额/订单金额（保留小数点后四位）
                    BigDecimal divide = BigDecimal.valueOf(order.getPaymentAmount()).divide(order.getOrderAmount(), 4, RoundingMode.HALF_UP);
                    order.setPayRate(divide.toString());
                    order.setCouponFee(new BigDecimal("0"));
                    order.setDiscountFee(new BigDecimal("0"));
                    order.setTotalDiscount(new BigDecimal("0"));
                    order.setOrderType("NORMAL");
                    order.setIsIo("Y");
                    //将数据插入order表
                    Order returnOrder = service.insertSelective(iRequest, order);
                    for (VirtualOrderImportDto info : infos) {
                        //虚拟订单导入excel中的商家商品编码
                        String tmallProductCode = info.getTmallProductCode();
                        //虚拟订单导入excel中的商品库存
                        String tmallProductInventory = info.getTmallProductInventory();
                        i++;
                        String orderEntryCode = iSequenceGenerateService.getNextOrderEntryCode();
                        OrderEntry orderEntry = new OrderEntry();
                        orderEntry.setOrderId(returnOrder.getOrderId());
                        orderEntry.setCode(orderEntryCode);
                        orderEntry.setOrderEntryStatus("NORMAL");
                        orderEntry.setBasePrice(Double.parseDouble("999"));
                        orderEntry.setQuantity(Integer.parseInt(tmallProductInventory));
                        orderEntry.setUnitFee(Double.parseDouble("999"));
                        orderEntry.setTotalFee(Double.parseDouble(String.valueOf(999 * Integer.parseInt(tmallProductInventory))));
                        orderEntry.setIsGift("N");
                        //推迟时间在配置文件中取,默认当前时间推后20天
                        Date orderEntryEstimateDeliveryDate = getDelayDate(calendar, VIRTUALORDER_ORDERENTRY_ESTIMATEDELIVERYTIME);
                        orderEntry.setEstimateDeliveryTime(orderEntryEstimateDeliveryDate);
                        //推迟时间在配置文件中取,默认当前时间推后18天
                        Date orderEntryEstimateConTimeDate = getDelayDate(calendar, VIRTUALORDER_ORDERENTRY_ESTIMATECONTIME);
                        orderEntry.setEstimateConTime(orderEntryEstimateConTimeDate);
                        //关联Prodcut表中的字段
                        List<Product> products = iProductService.selectProductByCode(tmallProductCode);
                        if (products.size() != 0) {
                            orderEntry.setProductId(products.get(0).getProductId());
                            orderEntry.setVproductCode(products.get(0).getvProductCode());
                            orderEntry.setShippingType(products.get(0).getDefaultDelivery());
                            orderEntry.setOdtype(products.get(0).getCustomChannelSource());
                            orderEntry.setShippingFee(Double.parseDouble("0"));
                            orderEntry.setInstallationFee(Double.parseDouble("0"));
                            orderEntry.setPreShippingfee(Double.parseDouble("0"));
                            orderEntry.setPreInstallationfee(Double.parseDouble("0"));
                            orderEntry.setDiscountFee(Double.parseDouble("0"));
                            orderEntry.setDiscountFeel(Double.parseDouble("0"));
                            orderEntry.setCouponFee(Double.parseDouble("0"));
                            orderEntry.setTotalDiscount(Double.parseDouble("0"));
                            // 设置订单行LineNumber
                            Long nextLineNumber = iOrderEntryService.getNextLineNumber(order);
                            orderEntry.setLineNumber(nextLineNumber);
                            //默认天津仓
                            PointOfServiceDto dto = iPointOfServiceExternalService.selectByCode((String) service.getProperties().get(VIRTUALORDER_ORDERENTRY_POINTOFSERVICE));
                            if (dto != null) {
                                orderEntry.setPointOfServiceId(dto.getPointOfServiceId());
                            } else {
                                throw new RuntimeException("没有找到对应的服务点信息");
                            }
                            String pin = iSequenceGenerateService.getNextAsCode();
                            orderEntry.setPin(pin);
                            orderEntryList.add(orderEntry);
                        } else {
                            errMsg.add("第" + i + "行");
                        }
                    }
                    if (errMsg.size() == 1) {
                        iOrderEntryService.batchInsertOrderEntry(iRequest, orderEntryList);
                        importResult = true;
                    } else {
                        StringBuffer sb = new StringBuffer();
                        for (String s : errMsg) {
                            sb.append(new StringBuffer(s) + " ");
                        }
                        sb.append("上架商品编码有误");
                        message = "解析excel文件报错<br/>" + sb.toString();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    message = "解析excel文件报错<br/>" + e.getMessage();
                } finally {
                    if (is != null) {
                        try {
                            // 关闭文件输入流
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        ResponseData responseData = new ResponseData(importResult);
        responseData.setMessage(message);
        return responseData;
    }

    /**
     * 导出虚拟订单模板
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/hmall/om/order/downloadExcelTemplate", method = RequestMethod.GET)
    public void importExcel(HttpServletRequest request, HttpServletResponse response) {
        List<VirtualOrderImportDto> virtualOrderImportDtos = new ArrayList<>();
        virtualOrderImportDtos.add(new VirtualOrderImportDto());
        new ExcelUtil(VirtualOrderImportDto.class)
                .exportExcel(virtualOrderImportDtos, VirtualOrderImportDto.DEFAULT_SHEET_NAME, 0,
                        request, response, VirtualOrderImportDto.DEFAULT_EXCEL_FILE_NAME);
    }

    /**
     * 获得推迟的时间
     *
     * @param calendar
     * @param key
     */
    public Date getDelayDate(Calendar calendar, String key) {
        int orderEntryEstimateDeliveryDelay = Integer.parseInt((String) service.getProperties().get(key));
        calendar.add(Calendar.DATE, orderEntryEstimateDeliveryDelay);
        Date delayDate = calendar.getTime();
        calendar.setTime(new Date());
        return delayDate;
    }

    /**
     * @param order
     * @param operation
     * @param request
     * @return
     * @description 订单占用优惠券和释放优惠券微服务
     */
    @RequestMapping(value = "/hmall/om/order/releaseAndOccupationCoupon")
    @ResponseBody
    public com.hand.hmall.dto.ResponseData releaseAndOccupationCoupon(@RequestBody Order order, @RequestParam("operation") String operation, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("customerId", order.getCustomerid());
        map.put("couponId", order.getChosenCoupon());
        map.put("operation", operation);
        try {
            String url = "hmall-drools-service/coupon/operate/operateCustomerCoupon";
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, null);
            com.hand.hmall.dto.ResponseData responseData = restTemplate.exchange(baseUri + url, HttpMethod.POST, entity, com.hand.hmall.dto.ResponseData.class).getBody();

            /*OrderCouponrule orderCouponrule = new OrderCouponrule();
            orderCouponrule.setOrderId(order.getOrderId());
            orderCouponrule.setCouponId(order.getChosenCoupon());
            orderCouponruleService.deleteOrderCouponruleByOrderId(orderCouponrule);


            OmPromotionrule omPromotionrule = new OmPromotionrule();
            omPromotionrule.setOrderId(order.getOrderId());
            omPromotionrule.setActivityId(order.getChosenPromotion());
            omPromotionruleService.deleteOmPromotionruleByOrderId(omPromotionrule);*/

            return responseData;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        com.hand.hmall.dto.ResponseData responseData = new com.hand.hmall.dto.ResponseData();
        responseData.setSuccess(false);
        if ("1".equals(operation)) {
            responseData.setMsg("占用优惠券失败！");
        } else {
            responseData.setMsg("释放优惠券失败！");
        }
        return responseData;
    }

    /**
     * @param request
     * @param order
     * @return
     * @description 订单取消时，占用优惠券后在订单头中存入选择的优惠券id和促销id
     * 并且更新订单同步官网状态为N，将订单下所有订单行关联的发货单同步RETAIL以及同步商城状态置为N
     */
    @RequestMapping(value = "/hmall/om/order/setCouponAndPromotion")
    @ResponseBody
    public ResponseData setCouponAndPromotion(HttpServletRequest request, @RequestBody Order order) {
        IRequest requestCtx = createRequestContext(request);
        service.setCouponAndPromotion(requestCtx, order);
        return new ResponseData();
    }

    /**
     * 天猫订单状态导入模板
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/hmall/om/downloadOrderStatusExcelTemplate", method = RequestMethod.GET)
    public void importdOrderStatusExcel(HttpServletRequest request, HttpServletResponse response) {
        List<OrderStatusImportDto> OrderStatusImportDtos = new ArrayList<OrderStatusImportDto>();
        OrderStatusImportDtos.add(new OrderStatusImportDto());
        new ExcelUtil(OrderStatusImportDto.class)
                .exportExcel(OrderStatusImportDtos, OrderStatusImportDto.DEFAULT_SHEET_NAME, 0,
                        request, response, OrderStatusImportDto.DEFAULT_EXCEL_FILE_NAME);
    }

    /**
     * 天猫订单状态的导入
     *
     * @param request
     * @param files
     * @return ResponseData
     * @throws IOException
     * @throws InvalidFormatException
     */
    @RequestMapping(value = {"/hmall/om/OrderStatusImport"}, method = {RequestMethod.POST})
    @ResponseBody
    public ResponseData orderStatusImport(HttpServletRequest request, MultipartFile files) throws IOException, InvalidFormatException {
        List<Order> orderList = new ArrayList<Order>();
        boolean importResult = false;
        String message = "success"; // 方法执行结果消息
        IRequest iRequest = this.createRequestContext(request);
        CommonsMultipartResolver multipartResolver
                = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (multipartResolver.isMultipart(request)
                && (request instanceof MultipartHttpServletRequest)) {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            Iterator<String> fileNames = multipartRequest.getFileNames();
            if (fileNames.hasNext()) {
                // 获得上传文件
                MultipartFile file = multipartRequest.getFile(fileNames.next());
                // 文件输入流
                InputStream is = null;
                try {
                    is = file.getInputStream();
                    // 调用工具类解析上传的Excel文件
                    List<OrderStatusImportDto> infos
                            = new ExcelUtil(OrderStatusImportDto.class)
                            .importExcel(OrderStatusImportDto.DEFAULT_EXCEL_FILE_NAME,
                                    OrderStatusImportDto.DEFAULT_SHEET_NAME + "0",
                                    is);
                    if (infos.size() == 0) {
                        message = "请下载Excel模板并输入数据";
                    }
                    for (int i = 0; i < infos.size(); i++) {
                        //EXCEL表格平台订单号不能为空
                        String escOrderCode = infos.get(i).getEscOrderCode();
                        if (StringUtil.isEmpty(escOrderCode)) {
                            message = " 第" + (i + 1) + "条数据平台订单号不能为空";
                            throw new Exception(message);
                        }
                        //EXCEL表格成交时间不能为空
                        String tradeFinishTime = infos.get(i).getTradeFinishTime();
                        if (StringUtil.isEmpty(tradeFinishTime)) {
                            message = " 第" + (i + 1) + "条数据成交时间不能为空";
                            throw new Exception(message);
                        }
                        //EXCEL表格订单状态不能为空
                        String orderStatus = infos.get(i).getOrderStatus();
                        if (StringUtil.isEmpty(orderStatus)) {
                            message = " 第" + (i + 1) + "条数据订单状态不能为空";
                            throw new Exception(message);
                        }
                        //EXCEL表格对应平台订单号且WEBSITE_ID为TM的记录必须存在
                        List<Order> orders = service.selectInfoByEscOrderCodeAndWebsiteId(escOrderCode);
                        if (orders.size() == 0) {
                            message = " 第" + (i + 1) + "条数据平台订单号对应的记录不存在";
                            throw new Exception(message);
                        }
                        if (orders.size() > 1) {
                            message = " 第" + (i + 1) + "条数据平台订单号对应多条记录";
                            throw new Exception(message);
                        }
                        boolean flag = validateDateFormat(tradeFinishTime);
                        if (flag == false) {
                            message = " 第" + (i + 1) + "条数据成交时间格式错误";
                            throw new Exception(message);
                        }
                        //订单状态必须为"交易成功"或"交易失败",否则返回错误信息
                        if (!orderStatus.equals("交易成功") && !orderStatus.equals("交易关闭")) {
                            message = " 第" + (i + 1) + "条数据订单状态错误";
                            throw new Exception(message);
                        }

                        Order order = new Order();
                        order.setOrderId(orders.get(0).getOrderId());
                        order.setTradeFinishTime(sdfForValidate.parse(tradeFinishTime));
                        if (orderStatus.equals("交易成功")) {
                            order.setOrderStatus("TRADE_FINISHED");
                        }
                        if (orderStatus.equals("交易关闭")) {
                            order.setOrderStatus("TRADE_CLOSED");
                        }
                        orderList.add(order);
                    }
                    service.updateBatchOrder(orderList);


                    importResult = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    message = "解析excel文件报错<br/>" + e.getMessage();
                } finally {
                    if (is != null) {
                        try {
                            // 关闭文件输入流
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        ResponseData responseData = new ResponseData(importResult);
        responseData.setMessage(message);
        return responseData;
    }


    /**
     * 验证日期格式是否正确
     */
    public boolean validateDateFormat(String dateString) {
        sdfForValidate.setLenient(false);
        try {
            sdfForValidate.parse(dateString);
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    // url: '${base.contextPath}/hmall/om/order/setOrderAssiging?orderIDs=' + orderIDs + '&employeeId=' + employeeId,

    /**
     * 设置订单归属信息
     *
     * @param orderIDs   - 订单ID列表拼接字符串
     * @param employeeId - 员工ID
     * @return
     */
    @GetMapping("/hmall/om/order/setOrderAssiging")
    @ResponseBody
    public ResponseData setOrderAssiging(@RequestParam("orderIDs") String orderIDs, @RequestParam("employeeId") Long employeeId) {
        List<String> orderIdList = Arrays.asList(orderIDs.split(","));
        List<Long> orderIds = new ArrayList<>();
        for (String idStr : orderIdList) {
            orderIds.add(Long.parseLong(idStr));
        }
        service.setOrderAssiging(orderIds, employeeId);
        return new ResponseData();
    }

}
