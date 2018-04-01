package com.hand.hmall.om.controllers;

import com.hand.common.util.ExcelUtil;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.om.service.IOrderService;
import com.hand.hmall.om.tpl.TmallOrderTemplate;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
 * @author chenzhigang
 * @version 0.1
 * @name ImportTmallOrderExcelController
 * @description 导入天猫订单控制器
 * @date 2017年8月7日
 */
@Controller
public class ImportTmallOrderExcelController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IOrderService orderService;

    /**
     * 导出天猫订单Excel模板
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/hmall/om/tmallOrder/exportTemplate", method = RequestMethod.GET)
    public void exportTemplate(HttpServletRequest request, HttpServletResponse response) {
        List<TmallOrderTemplate> tmallOrderTemplates = new ArrayList<>();
        tmallOrderTemplates.add(new TmallOrderTemplate());
        new ExcelUtil(TmallOrderTemplate.class)
                .exportExcel(tmallOrderTemplates, "天猫订单数据", 0,
                        request, response, "天猫订单数据导入模板.xlsx");
    }

    /**
     * 导入天猫订单数据
     *
     * @param request
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    @RequestMapping(value = {"/hmall/om/tmallOrder/importData"}, method = {RequestMethod.POST})
    @ResponseBody
    public ResponseData importData(HttpServletRequest request/*, MultipartFile files*/) throws IOException, InvalidFormatException {

        IRequest iRequest = createRequestContext(request);

        CommonsMultipartResolver cmr = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (cmr.isMultipart(request) && (request instanceof MultipartHttpServletRequest)) {

            MultipartHttpServletRequest mhr = (MultipartHttpServletRequest) request;
            Iterator<String> fileNames = mhr.getFileNames();

            if (fileNames.hasNext()) {

                // 获得上传文件
                MultipartFile file = mhr.getFile(fileNames.next());

                // 文件输入流
                InputStream is = file.getInputStream();

                // 调用工具类解析上传的Excel文件
                List<TmallOrderTemplate> tmallOrders = null;
                try {
                    tmallOrders = new ExcelUtil(TmallOrderTemplate.class)
                            .importExcel(file.getOriginalFilename(), null, is);
                } catch (Exception e) {
                    logger.error("\n解析天猫订单excel文件出错\n异常信息:" + e.getMessage());
                    ResponseData responseData = new ResponseData(false);
                    responseData.setMessage("excel文件格式或数据错误");
                    return responseData;
                } finally {
                    if (is != null) {
                        try {
                            // 关闭文件输入流
                            is.close();
                        } catch (IOException e) {
                            // e.printStackTrace();
                            logger.error("订单导入程序执行错误(关闭文件输入流异常): " + e.getMessage());
                            ResponseData responseData = new ResponseData(false);
                            responseData.setMessage("订单导入程序执行错误(关闭文件输入流异常):\n" + e.getMessage());
                            return responseData;
                        }
                    }
                }

                // 没有有效的订单数据
                if (tmallOrders.isEmpty()) {
                    logger.error("文件中没有有效的订单数据");
                    ResponseData responseData = new ResponseData(false);
                    responseData.setMessage("文件中没有有效的订单数据");
                    return responseData;
                }

                try {
                    orderService.importTmallOrderData(iRequest, tmallOrders);
                } catch (RuntimeException e) {
                    logger.error("导入数据不符合要求: " + e.getMessage());
                    ResponseData responseData = new ResponseData(false);
                    responseData.setMessage("导入数据不符合要求:\n" + e.getMessage());
                    return responseData;
                }

            }
        }

        ResponseData responseData = new ResponseData();
        responseData.setMessage("成功导入天猫订单数据");
        return responseData;

    }

}
