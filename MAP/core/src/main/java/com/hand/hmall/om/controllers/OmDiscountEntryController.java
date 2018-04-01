package com.hand.hmall.om.controllers;

import com.hand.common.util.ExcelUtil;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.ICodeService;
import com.hand.hmall.mst.service.ICatalogversionService;
import com.hand.hmall.mst.service.IProductService;
import com.hand.hmall.om.dto.OmDiscountEntry;
import com.hand.hmall.om.dto.OmDiscountEntryTemplate;
import com.hand.hmall.om.service.IOmDiscountEntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

/**
 * @author zhangmeng
 * @name OmDiscountEntryController
 * @description 折扣行
 * @date 2017/11/28
 */
@Controller
public class OmDiscountEntryController extends BaseController {

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IOmDiscountEntryService service;
    @Autowired
    private IProductService productService;
    @Autowired
    private ICatalogversionService iCatalogversionService;
    @Autowired
    private ICodeService codeService;


    @RequestMapping(value = "/hmall/om/discount/entry/query")
    @ResponseBody
    public ResponseData query(OmDiscountEntry dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        List<OmDiscountEntry> omDiscountEntries = service.queryDiscountEntryInfo(dto, page, pageSize);
        return new ResponseData(omDiscountEntries);
    }

    /**
     * 删除折扣行
     *
     * @param omDiscountEntryList
     * @return
     */
    @RequestMapping(value = "/hmall/om/discount/entry/delDiscountEntry")
    @ResponseBody
    public ResponseData delDiscountEntry(@RequestBody List<OmDiscountEntry> omDiscountEntryList) {
        return service.delDiscountEntry(omDiscountEntryList);
    }

    /**
     * 保存折扣行
     *
     * @param omDiscountEntryList
     * @return
     */
    @RequestMapping(value = "/hmall/om/discount/entry/saveDiscountEntry")
    @ResponseBody
    public ResponseData saveDiscountEntry(@RequestBody List<OmDiscountEntry> omDiscountEntryList) {
        return service.saveDiscountEntry(omDiscountEntryList);
    }

    /**
     * 同步折扣行
     * flag为1 同步选择的折扣行 flag为2全部同步
     *
     * @param omDiscountEntryList
     * @param flag
     * @return
     */
    @RequestMapping(value = "/hmall/om/discount/entry/syncDiscountEntry")
    @ResponseBody
    public ResponseData syncDiscountEntry(HttpServletRequest request, @RequestBody List<OmDiscountEntry> omDiscountEntryList, String flag) {
        IRequest requestContext = createRequestContext(request);
        return service.syncDiscountEntry(requestContext, omDiscountEntryList, flag);
    }

    /**
     * 导出折扣价格行Excel模板
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/hmall/om/discount/entry/downloadExcelTemplate", method = RequestMethod.GET)
    public void downloadExcelTemplate(HttpServletRequest request, HttpServletResponse response) {
        try {
            new ExcelUtil(OmDiscountEntry.class).downloadExcelModel(request, response, "template/discountEntry.xlsx");
        } catch (Exception e) {
            logger.error(OmDiscountEntryController.class.getClass().getName() + ":" + e.getMessage(), e);
        }
    }

    /**
     * 折扣价格行的导入
     *
     * @param request
     * @return ResponseData
     */
    @RequestMapping(value = {"/hmall/om/discount/import"}, method = {RequestMethod.POST})
    @ResponseBody
    public ResponseData importDiscountEntry(HttpServletRequest request) {
        String message = "success"; // 方法执行结果消息
        MultipartHttpServletRequest multipartRequest = null;
        ResponseData responseData = null;
        CommonsMultipartResolver multipartResolver
                = new CommonsMultipartResolver(request.getSession().getServletContext());

        List<OmDiscountEntryTemplate> infos = null;
        if (multipartResolver.isMultipart(request)
                && (request instanceof MultipartHttpServletRequest)) {
            multipartRequest = (MultipartHttpServletRequest) request;
            Iterator<String> fileNames = multipartRequest.getFileNames();
            if (fileNames.hasNext()) {
                // 文件输入流
                InputStream is = null;
                // 获得上传文件
                MultipartFile file = multipartRequest.getFile(fileNames.next());
                try {
                    is = file.getInputStream();
                    // 调用工具类解析上传的Excel文件
                    infos = new ExcelUtil(OmDiscountEntryTemplate.class)
                            .importExcel(OmDiscountEntryTemplate.DEFAULT_EXCEL_FILE_NAME,
                                    OmDiscountEntryTemplate.DEFAULT_SHEET_NAME + "0",
                                    is);
                    service.importDiscountEntry(infos);
                    responseData = new ResponseData(true);
                } catch (Exception e) {
                    message = "解析excel文件报错<br/>" + e.getMessage();
                    logger.error(OmDiscountEntryController.class.getClass().getName() + ":" + message, e);
                    responseData = new ResponseData(false);
                } finally {
                    if (is != null) {
                        try {
                            // 关闭文件输入流
                            is.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }


        }
        responseData.setMessage(message);
        return responseData;
    }
}