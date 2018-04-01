package com.hand.hmall.as.controllers;

import com.hand.common.util.ExcelUtil;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hmall.as.dto.Svsales;
import com.hand.hmall.as.dto.SvsalesTemplate;
import com.hand.hmall.as.service.ISvsaleOrderService;
import com.hand.hmall.as.service.ISvsalesEntryService;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.util.StringUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * author: zhangzilong
 * name: ServiceSaleOrderController.java
 * discription: 服务销售单 Controller
 * date: 2017/7/18
 * version: 0.1
 */
@Controller
public class SvsaleController extends BaseController {

    @Autowired
    ISvsaleOrderService service;
    @Autowired
    ISvsalesEntryService entryService;

    @RequestMapping("/hmall/as/svsales/saveSvsales")
    @ResponseBody
    public ResponseData saveServiceSaleOrder(HttpServletRequest request, @RequestBody List<Svsales> dto) {
        IRequest iRequest = this.createRequestContext(request);
        ResponseData rsd = new ResponseData();
        List<Svsales> result = new ArrayList<>();
        try {
            result.add(service.saveOrUpdate(dto.get(0), iRequest));
            result.get(0).setSvsalesEntries(entryService.querySvsalesEntriesInfo(iRequest, result.get(0), Integer.parseInt(DEFAULT_PAGE), Integer.parseInt(DEFAULT_PAGE_SIZE)));
        } catch (Exception e) {
            rsd.setSuccess(false);
            return rsd;
        }
        return new ResponseData(result);
    }

    /**
     * @author xuxiaoxue
     * @version 0.1
     * @name SvsaleController
     * @description 查询服务销售单列表
     * @date 2017/12/6
     */
    @RequestMapping(value = "/hmall/as/svsales/list/query")
    @ResponseBody
    public com.hand.hap.system.dto.ResponseData querySvsales(HttpServletRequest request, @RequestParam Map maps) {
        Svsales dto = new Svsales();
        String str = (String) maps.get("data");
        JSONObject jsonObject = JSONObject.fromObject(str);
        JSONObject status = jsonObject.getJSONObject("status");
        Map<String, Object> data = (Map<String, Object>) jsonObject.get("pages");
        IRequest requestContext = createRequestContext(request);
        int page = 0;
        if (data.get("page") != null) {
            page = Integer.parseInt(data.get("page").toString());
        }
        int pageSize = 0;
        if (data.get("pagesize") != null) {
            pageSize = Integer.parseInt(data.get("pagesize").toString());
        }
        String code = null;
        if (!StringUtils.isEmpty((String) data.get("code"))) {
            code = (String) data.get("code");
        }
        String serviceOrderCode = null;
        if (!StringUtils.isEmpty((String) data.get("serviceOrderCode"))) {
            serviceOrderCode = (String) data.get("serviceOrderCode");
        }
        String escOrderCode = null;
        if (!StringUtils.isEmpty((String) data.get("escOrderCode"))) {
            escOrderCode = (String) data.get("escOrderCode");
        }
        String sapCode = null;
        if (!StringUtils.isEmpty((String) data.get("sapCode"))) {
            sapCode = (String) data.get("sapCode");
        }
        String customerId = null;
        if (!StringUtils.isEmpty((String) data.get("customerId"))) {
            customerId = (String) data.get("customerId");
        }
        String mobileNumber = null;
        if (!StringUtils.isEmpty((String) data.get("mobileNumber"))) {
            mobileNumber = (String) data.get("mobileNumber");
        }
        String payStatus = null;
        if (!StringUtils.isEmpty((String) data.get("payStatus"))) {
            payStatus = (String) data.get("payStatus");
        }
        String syncflag = null;
        if (!StringUtils.isEmpty((String) data.get("syncflag"))) {
            syncflag = (String) data.get("syncflag");
        }
        JSONArray svsaleStatus = status.getJSONArray("svsaleStatus");
        String[] strSvsaleStatus = new String[svsaleStatus.size()];
        for (int i = 0; i < svsaleStatus.size(); i++) {
            strSvsaleStatus[i] = svsaleStatus.get(i).toString();
        }
        List<Svsales> svsalesList = service.querySvsales(requestContext, code, serviceOrderCode, escOrderCode, sapCode, customerId, mobileNumber, payStatus, syncflag, strSvsaleStatus, page, pageSize);
        return new com.hand.hap.system.dto.ResponseData(svsalesList);
    }

    /**
     * 导出服务销售单
     *
     * @param request
     * @param response
     * @param code
     * @param serviceOrderCode
     * @param escOrderCode
     * @param sapCode
     * @param customerId
     * @param mobile
     * @param svsaleStatus
     * @param payStatus
     * @param syncflag
     */
    @RequestMapping(value = "/hmall/as/svsales/exportExcel")
    @ResponseBody
    public void exportExcel(HttpServletRequest request, HttpServletResponse response, @RequestParam String code, @RequestParam String serviceOrderCode, @RequestParam String escOrderCode, @RequestParam String sapCode, @RequestParam String customerId, @RequestParam String mobile, @RequestParam String[] svsaleStatus, @RequestParam String payStatus, @RequestParam String syncflag) {
        IRequest requestContext = createRequestContext(request);
        List<Svsales> svsalesList = service.querySvsales(requestContext, code, serviceOrderCode, escOrderCode, sapCode, customerId, mobile, payStatus, syncflag, svsaleStatus);
        new ExcelUtil(Svsales.class).exportExcel(svsalesList, "服务销售单列表", svsalesList.size(), request, response, "服务销售单列表.xlsx");
    }

    @RequestMapping("/hmall/as/servicesaleorder/queryBySvsalesId")
    @ResponseBody
    public ResponseData queryById(HttpServletRequest request, @RequestParam Long asSvsalesId) {
        IRequest iRequest = this.createRequestContext(request);
        List<Svsales> list;
        try {
            list = service.queryBySvsalesId(asSvsalesId, iRequest);
        } catch (Exception e) {
            return new ResponseData(false);
        }
        return new ResponseData(list);
    }

    @RequestMapping("/hmall/as/servicesaleorder/newSvsale")
    @ResponseBody
    public ResponseData newSvsale(HttpServletRequest request, @RequestParam Long serviceOrderId) {
        IRequest iRequest = this.createRequestContext(request);
        List<Svsales> list = new ArrayList<>();
        try {
            list.add(service.newSvsale(serviceOrderId, iRequest));
        } catch (Exception e) {
            return new ResponseData(false);
        }
        return new ResponseData(list);
    }

    /**
     * 同步到retail
     *
     * @param asSvsalesId
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/hmall/as/serviceSaleOrder/syncRetail")
    public ResponseData sendToRetail(@Param(value = "asSvsalesId") Long asSvsalesId, HttpServletRequest httpServletRequest) {
        IRequest iRequest = createRequestContext(httpServletRequest);
        return service.sendToRetail(asSvsalesId, iRequest);
    }


    /**
     * 更新标识位到处理中
     *
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping("/hmall/as/svsales/amountConfirm")
    @ResponseBody
    public ResponseData amountConfirm(HttpServletRequest request, @RequestBody List<Svsales> dto) {
        return service.updateStatusToProc(dto.get(0));
    }

    @RequestMapping("/hmall/as/svsales/cancelOrder")
    @ResponseBody
    public ResponseData cancelOrder(HttpServletRequest request, @RequestBody List<Svsales> dto) {
        return service.updateStatusToCanc(dto.get(0));
    }

    /**
     * 服务销售单模板下载
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/hmall/as/svsales/exportTemplate", method = RequestMethod.GET)
    public void exportTemplate(HttpServletRequest request, HttpServletResponse response) {
        List<SvsalesTemplate> svsalesTemplates = new ArrayList<>();
        svsalesTemplates.add(new SvsalesTemplate());
        new ExcelUtil(SvsalesTemplate.class)
                .exportExcel(svsalesTemplates, SvsalesTemplate.DEFAULT_SHEET_NAME, 0,
                        request, response, SvsalesTemplate.DEFAULT_EXCEL_FILE_NAME);
    }

    /**
     * 服务销售单导入
     *
     * @param request
     * @param files
     * @return ResponseData
     * @throws Exception
     */
    @RequestMapping(value = {"/hmall/as/svsalesImport"}, method = {RequestMethod.POST})
    @ResponseBody
    public com.hand.hap.system.dto.ResponseData svsalesImport(HttpServletRequest request, MultipartFile files) throws Exception {
        com.hand.hap.system.dto.ResponseData responseData = null;
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
                    List<SvsalesTemplate> infos
                            = new ExcelUtil(SvsalesTemplate.class)
                            .importExcel(SvsalesTemplate.DEFAULT_EXCEL_FILE_NAME,
                                    SvsalesTemplate.DEFAULT_SHEET_NAME + "0",
                                    is);
                    if (infos.size() == 0) {
                        message = "请下载Excel模板并输入数据";
                    }
                    responseData = service.importSvsalesAndSvsalesEntry(infos, iRequest, importResult, message);

                } catch (Exception e) {
                    e.printStackTrace();
                    message = "解析excel文件报错<br/>" + e.getMessage();
                    responseData = new com.hand.hap.system.dto.ResponseData(importResult);
                    responseData.setMessage(message);
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

        return responseData;
    }
}
