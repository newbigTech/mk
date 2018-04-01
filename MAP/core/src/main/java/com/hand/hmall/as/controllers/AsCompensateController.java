package com.hand.hmall.as.controllers;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.hand.common.util.ExcelUtil;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.as.dto.AsCompensate;
import com.hand.hmall.as.dto.AsCompensateTemplate;
import com.hand.hmall.as.service.IAsCompensateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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

/**
 * @author xuxiaoxue
 * @version 0.1
 * @name AsCompensateController
 * @description 销售赔付单头表Controller类
 * @date 2017/10/11
 */
@Controller
public class AsCompensateController extends BaseController {


    @Autowired
    private IAsCompensateService service;

    /**
     * 根据赔付单ID查询赔付单信息
     *
     * @param asCompensate
     * @return
     */
    @RequestMapping(value = "/hmall/as/compensate/selectCompensateById")
    @ResponseBody
    public ResponseData selectCompensateById(AsCompensate asCompensate) {
        return new ResponseData(service.selectCompensateById(asCompensate));
    }

    /**
     * 保存赔付单信息 赔付单行信息
     *
     * @param request
     * @param asCompensateList
     * @return
     */
    @RequestMapping(value = "/hmall/as/compensate/saveCompensate")
    @ResponseBody
    public ResponseData saveCompensate(HttpServletRequest request, @RequestBody List<AsCompensate> asCompensateList) {
        IRequest iRequest = this.createRequestContext(request);
        return service.saveCompensate(iRequest, asCompensateList);
    }

    /**
     * 查询单位
     *
     * @return
     */
    @RequestMapping(value = "/hmall/as/compensate/selectMstUnit")
    @ResponseBody
    public ResponseData selectMstUnit() {
        return new ResponseData(service.selectMstUnit());
    }

    /**
     * 销售赔付单导入模板下载
     *
     * @param request
     * @param response
     * @return ResponseData
     */
    @RequestMapping(value = "/hmall/as/compensate/exportTemplate", method = RequestMethod.GET)
    public void exportTemplate(HttpServletRequest request, HttpServletResponse response) {
        List<AsCompensateTemplate> asCompensateTemplates = new ArrayList<>();
        asCompensateTemplates.add(new AsCompensateTemplate());
        new ExcelUtil(AsCompensateTemplate.class)
                .exportExcel(asCompensateTemplates, AsCompensateTemplate.DEFAULT_SHEET_NAME, 0,
                        request, response, AsCompensateTemplate.DEFAULT_EXCEL_FILE_NAME);
    }

    /**
     * 销售赔付单的导入
     *
     * @param request
     * @param files
     * @return ResponseData
     * @throws IOException
     * @throws InvalidFormatException
     */
    @RequestMapping(value = {"/hmall/as/asCompensateImport"}, method = {RequestMethod.POST})
    @ResponseBody
    public ResponseData asCompensateImport(HttpServletRequest request, MultipartFile files) throws Exception {
        ResponseData responseData = null;
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
                    List<AsCompensateTemplate> infos
                            = new ExcelUtil(AsCompensateTemplate.class)
                            .importExcel(AsCompensateTemplate.DEFAULT_EXCEL_FILE_NAME,
                                    AsCompensateTemplate.DEFAULT_SHEET_NAME + "0",
                                    is);
                    if (infos.size() == 0) {
                        message = "请下载Excel模板并输入数据";
                    }
                    responseData = service.importCompensateAndCompensateEntry(infos, iRequest, importResult, message);

                } catch (Exception e) {
                    e.printStackTrace();
                    message = "解析excel文件报错<br/>" + e.getMessage();
                    responseData = new ResponseData(importResult);
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