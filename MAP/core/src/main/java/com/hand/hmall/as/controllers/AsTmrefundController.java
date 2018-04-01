package com.hand.hmall.as.controllers;

import com.github.pagehelper.StringUtil;
import com.hand.common.util.ExcelUtil;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.as.dto.AsTmrefund;
import com.hand.hmall.as.dto.AsTmrefundImportDto;
import com.hand.hmall.as.service.IAsTmrefundService;
import com.hand.hmall.mst.dto.Product;
import com.hand.hmall.mst.service.IProductService;
import com.hand.hmall.om.dto.Order;
import com.hand.hmall.om.dto.OrderEntry;
import com.hand.hmall.om.service.IOrderEntryService;
import com.hand.hmall.om.service.IOrderService;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
/**
 * @author xuxiaoxue
 * @version 0.1
 * @name AsTmrefundController
 * @description 天猫退款单导入Controller类
 * @date 2017/9/14
 */
public class AsTmrefundController extends BaseController {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    SimpleDateFormat sdfForValidate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private IAsTmrefundService service;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IProductService productService;

    @Autowired
    private IOrderEntryService orderEntryService;


    @RequestMapping(value = "/hmall/as/tmrefund/query")
    @ResponseBody
    public ResponseData query(AsTmrefund dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        List<AsTmrefund> select = service.queryAsTmrefundList(dto, page, pageSize);
        return new ResponseData(service.queryAsTmrefundList(dto, page, pageSize));
    }

    /**
     * 天猫退款单查询list
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hmall/as/tmrefund/queryAsTmrefundList")
    @ResponseBody
    public ResponseData queryAsTmrefundList(AsTmrefund dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page, @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        return new ResponseData(service.queryAsTmrefundList(dto, page, pageSize));
    }


    @RequestMapping(value = "/hmall/as/tmrefund/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<AsTmrefund> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hmall/as/tmrefund/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<AsTmrefund> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    /**
     * 导出天猫退款单模板
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/hmall/as/downloadExcelTemplate", method = RequestMethod.GET)
    public void importExcel(HttpServletRequest request, HttpServletResponse response) {
        List<AsTmrefundImportDto> AsTmrefundImportDtos = new ArrayList<AsTmrefundImportDto>();
        AsTmrefundImportDtos.add(new AsTmrefundImportDto());
        new ExcelUtil(AsTmrefundImportDto.class)
                .exportExcel(AsTmrefundImportDtos, AsTmrefundImportDto.DEFAULT_SHEET_NAME, 0,
                        request, response, AsTmrefundImportDto.DEFAULT_EXCEL_FILE_NAME);
    }

    /**
     * 天猫退款单的导入
     *
     * @param request
     * @param files
     * @return ResponseData
     * @throws IOException
     * @throws InvalidFormatException
     */
    @RequestMapping(value = {"/hmall/as/asTmrefundImport"}, method = {RequestMethod.POST})
    @ResponseBody
    public ResponseData upload(HttpServletRequest request, MultipartFile files) throws IOException, InvalidFormatException {
        List<AsTmrefund> asTmrefundList = new ArrayList<AsTmrefund>();
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
                    List<AsTmrefundImportDto> infos
                            = new ExcelUtil(AsTmrefundImportDto.class)
                            .importExcel(AsTmrefundImportDto.DEFAULT_EXCEL_FILE_NAME,
                                    AsTmrefundImportDto.DEFAULT_SHEET_NAME + "0",
                                    is);
                    if (infos.size() == 0) {
                        message = "请下载Excel模板并输入数据";
                    }
                    for (int i = 0; i < infos.size(); i++) {
                        //EXCEL表格退款状态不能为空
                        String status = infos.get(i).getStatus();
                        if (StringUtil.isEmpty(status)) {
                            message = sdf.format(new Date()) + " 第" + (i + 1) + "条数据退款状态不能为空";
                            throw new Exception(message);
                        }
                        //状态必须为退款成功,否则返回失败
                        if (!infos.get(i).getStatus().equals("退款成功")) {
                            message = sdf.format(new Date()) + " 第" + (i + 1) + "条数据退款状态错误";
                            throw new Exception(message);
                        }
                        //EXCEL表格B列字段在HMALL_AS_TMREFUND表中不存在，否则返回失败
                        List<AsTmrefund> asTmrefundByCode = service.selectByCode(infos.get(i).getCode());
                        if (asTmrefundByCode.size() != 0) {
                            message = sdf.format(new Date()) + " 第" + (i + 1) + "条数据退款编号已存在";
                            throw new Exception(message);
                        }

                        //主数据编码在PRODUCT表中必须存在，否则返回失败
                        List<Product> products = productService.selectProByCode(infos.get(i).getMainDataCode());
                        if (products.size() == 0) {
                            message = sdf.format(new Date()) + " 第" + (i + 1) + "条数据主数据编码对应的商品不存在";
                            throw new Exception(message);
                        }
                        //订单编码不能为空
                        String escOrderCode = infos.get(i).getEscOrderCode();
                        if (StringUtils.isEmpty(escOrderCode)) {
                            message = " 第" + (i + 1) + "条数据订单编号列不能为空";
                            throw new Exception(message);
                        }
                        //EXCEL表格A列字段在order表中存在并且ORDER_TYPE的值为NORMAL，如果不存在符合条件的数据，返回失败
                        List<Order> orders = orderService.selectByEscOrderCodeAndOrderType(escOrderCode);
                        if (orders.size() == 0) {
                            message = sdf.format(new Date()) + " 第" + (i + 1) + "条数据订单编号错误";
                            throw new Exception(message);
                        }
                        //导入字段非空校验
                        String code = infos.get(i).getCode();
                        if (StringUtils.isEmpty(code)) {
                            message = " 第" + (i + 1) + "条数据退款编号列不能为空";
                            throw new Exception(message);
                        }
                        BigDecimal refundFee = infos.get(i).getRefundFee();
                        if (refundFee == null) {
                            message = " 第" + (i + 1) + "条数据买家退款金额列不能为空";
                            throw new Exception(message);
                        }
                        String hasGoodReturn = infos.get(i).getHasGoodReturn();
                        if (StringUtils.isEmpty(hasGoodReturn)) {
                            message = " 第" + (i + 1) + "条数据是否需要退货列不能为空";
                            throw new Exception(message);
                        }
                        String created = infos.get(i).getCreated();
                        if (StringUtils.isEmpty(created)) {
                            message = " 第" + (i + 1) + "条数据退货的申请时间列不能为空";
                            throw new Exception(message);
                        }

                        AsTmrefund asTmrefund = new AsTmrefund();
                        asTmrefund.setOrderId(orders.get(0).getOrderId());
                        asTmrefund.setCode(infos.get(i).getCode());
                        asTmrefund.setAlipayNo(infos.get(i).getAlipayNo());
                        String orderPaymentTime = infos.get(i).getOrderPaymentTime();
                        //如果订单付款时间不为空，校验时间格式是否正确
                        if (!StringUtils.isEmpty(orderPaymentTime)) {
                            boolean orderPaymentTimeValidate = validateDateFormat(orderPaymentTime);
                            if (orderPaymentTimeValidate == false) {
                                throw new Exception(sdf.format(new Date()) + " 第" + (i + 1) + "条数据订单付款时间格式错误");
                            }
                            asTmrefund.setOrderPaymentTime(sdfForValidate.parse(orderPaymentTime));
                        }
                        List<Product> productList = productService.selectProductByCode(infos.get(i).getMainDataCode());
                        //根据主数据编码（PRODUCT表的code字段）查询目录版本为markor-online的商品product_id存入天猫退货单表中
                        if (productList.size() == 1) {
                            asTmrefund.setProductId(productList.get(0).getProductId());
                        }
                        if (productList.size() > 1) {
                            message = " 第" + (i + 1) + "条数据主数据编码对应的商品不唯一";
                            throw new Exception(message);
                        }
                        //根据主数据编码(PRODUCT表的code字段)和导入的order_id查询订单行表的order_entry_id存入天猫退货单表中
                        OrderEntry orderEntry = new OrderEntry();
                        orderEntry.setProductCode(infos.get(i).getMainDataCode());
                        orderEntry.setOrderId(orders.get(0).getOrderId());
                        List<OrderEntry> orderEntries = orderEntryService.selectByProductCodeAndOrderId(orderEntry);
                        if (orderEntries.size() == 1) {
                            asTmrefund.setOrderentryId(orderEntries.get(0).getOrderEntryId());
                        }
                        if (orderEntries.size() > 1) {
                            message = " 第" + (i + 1) + "条数据主数据编码对应的订单行不唯一";
                            throw new Exception(message);
                        }
                        String refundFinishTime = infos.get(i).getRefundFinishTime();


//                      如果退款完结时间不为空，校验时间格式是否正确
                        boolean refundFinishTimeValidate = validateDateFormat(refundFinishTime);
                        if (!StringUtils.isEmpty(refundFinishTime)) {
                            if (refundFinishTimeValidate == false) {
                                throw new Exception(sdf.format(new Date()) + " 第" + (i + 1) + "条数据退款完结时间格式错误");
                            }
                            asTmrefund.setRefundFinishTime(sdfForValidate.parse(refundFinishTime));
                        }

                        asTmrefund.setBuyerNick(infos.get(i).getBuyerNick());
                        asTmrefund.setActualPaidAmount(infos.get(i).getActualPaidAmount());
                        asTmrefund.setTitle(infos.get(i).getTitle());
                        asTmrefund.setRefundFee(infos.get(i).getRefundFee());
                        asTmrefund.setManualOrAuto(infos.get(i).getManualOrAuto());
                        asTmrefund.setHasGoodReturn(infos.get(i).getHasGoodReturn());
//                      如果退款的申请时间不为空，校验时间格式是否正确
                        if (!StringUtils.isEmpty(created)) {
                            boolean createdValidate = validateDateFormat(created);
                            if (createdValidate == false) {
                                throw new Exception(sdf.format(new Date()) + " 第" + (i + 1) + "条数据退款申请时间格式错误");
                            }
                            asTmrefund.setCreated(sdfForValidate.parse(created));
                        }
//                      如果超时时间不为空，校验时间格式是否正确
                        String timeout = infos.get(i).getTimeout();
                        if (!StringUtils.isEmpty(timeout)) {
                            boolean timeoutValidate = validateDateFormat(timeout);
                            if (timeoutValidate == false) {
                                throw new Exception(sdf.format(new Date()) + " 第" + (i + 1) + "条数据超时时间格式错误");
                            }
                            asTmrefund.setTimeout(sdfForValidate.parse(timeout));
                        }
                        asTmrefund.setStatus(infos.get(i).getStatus());
                        asTmrefund.setGoodStatus(infos.get(i).getGoodStatus());
                        asTmrefund.setReturnLogistics(infos.get(i).getReturnLogistics());
                        asTmrefund.setConsignmentLogistics(infos.get(i).getConsignmentLogistics());
                        asTmrefund.setCsStatus(infos.get(i).getCsStatus());
                        asTmrefund.setSellerName(infos.get(i).getSellerName());
                        asTmrefund.setSellerAddress(infos.get(i).getSellerAddress());
                        asTmrefund.setSellerZip(infos.get(i).getSellerZip());
                        asTmrefund.setSellerPhone(infos.get(i).getSellerPhone());
                        asTmrefund.setSellerMobile(infos.get(i).getSellerMobile());
                        asTmrefund.setSid(infos.get(i).getSid());
                        asTmrefund.setCompanyName(infos.get(i).getCompanyName());
                        asTmrefund.setReason(infos.get(i).getReason());
                        asTmrefund.setRefundDesc(infos.get(i).getRefundDesc());
                        String goodReturnTime = infos.get(i).getGoodReturnTime();
//                      如果买家退货时间不为空，校验时间格式是否正确
                        if (!StringUtils.isEmpty(goodReturnTime)) {
                            boolean goodReturnTimeValidate = validateDateFormat(goodReturnTime);
                            if (goodReturnTimeValidate == false) {
                                throw new Exception(sdf.format(new Date()) + " 第" + (i + 1) + "条数据买家退货时间格式错误");
                            }
                            asTmrefund.setGoodReturnTime(sdfForValidate.parse(goodReturnTime));
                        }
                        asTmrefund.setResponsibilitySide(infos.get(i).getResponsibilitySide());
                        asTmrefund.setRefundPhase(infos.get(i).getRefundPhase());
                        asTmrefund.setSellerNote(infos.get(i).getSellerNote());
//                      如果买家完结时间不为空，数据库表中FINSH_TIME列=申请退款时间+导入的完结时间(导入的完结时间格式为XX天)
                        String finishTime = null;
                        finishTime = infos.get(i).getFinishTime();
                        finishTime = finishTime.replace("天", "");
                        BigDecimal BigfinishTime = new BigDecimal(finishTime);
                        //一天有86400秒
                        BigDecimal second = new BigDecimal(86400);
                        int intFinishTime = BigfinishTime.multiply(second).intValue();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(sdfForValidate.parse(created));
                        calendar.add(Calendar.SECOND, intFinishTime);
                        finishTime = sdfForValidate.format(calendar.getTime());
                        asTmrefund.setFinishTime(sdfForValidate.parse(finishTime));
                        asTmrefund.setRefundScope(infos.get(i).getRefundScope());
                        asTmrefund.setAuditPerson(infos.get(i).getAuditPerson());
                        asTmrefund.setBurdenTimeout(infos.get(i).getBurdenTimeout());
                        asTmrefund.setAuditAuto(infos.get(i).getAuditAuto());
                        asTmrefund.setRefundPhase(infos.get(i).getRefundPhase());
                        asTmrefundList.add(asTmrefund);
                    }
                    service.batchInsertAsTmrefund(iRequest, asTmrefundList);
                    importResult = true;
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    message = "解析excel文件报错<br/>完结时间格式错误";
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
}