package com.hand.hmall.mst.controllers;

import com.hand.common.util.ExcelUtil;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.mst.dto.Media;
import com.hand.hmall.mst.dto.Product;
import com.hand.hmall.mst.service.IMediaService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 多媒体对象的Controller层
 * @date 2017/7/10 14:37
 */
@Controller
public class MediaController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IMediaService service;

    @Value("#{configProperties['nfsImageRootPath']}")
    private String nfsImageRootPath;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }


    @RequestMapping(value = "/hmall/mst/media/query")
    @ResponseBody
    public ResponseData query(Media dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/mst/media/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<Media> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hmall/mst/media/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<Media> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    /**
     * @param request
     * @param dto
     * @param page
     * @param pageSize
     * @return
     * @description 商品详情页面中，查询商品相关的各种多媒体图片
     */
    @RequestMapping(value = "/hmall/mst/media/selectMeidaByProduct")
    @ResponseBody
    public ResponseData selectMeidaByProduct(HttpServletRequest request, Media dto,
                                             @RequestParam(defaultValue = DEFAULT_PAGE) int page, @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectMediaByProduct(requestContext, dto, pageSize, page));
    }

    /**
     * @param request
     * @param dto
     * @return
     * @description 商品详情页中删除图片和商品的关系
     */
    @RequestMapping(value = "/hmall/mst/media/deleteRelationWithProduct")
    @ResponseBody
    public ResponseData deleteRelationWithProduct(HttpServletRequest request, @RequestBody List<Media> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.deleteRelationWithProduct(requestCtx, dto));
    }

    @RequestMapping(value = "/hmall/mst/media/createMediaForProduct")
    @ResponseBody
    public ResponseData createMediaForProduct(@RequestBody List<Product> products) {
        ResponseData responseData = new ResponseData();
        String errorProductCode = "";
        if (CollectionUtils.isNotEmpty(products)) {
            for (Product p : products) {
                List<Product> pList = new ArrayList<Product>();
                pList.add(p);

                try {
                    service.createMediaForProduct(pList);
                } catch (Exception e) {
                    logger.error("商品[" + p.getCode() + "]获取图片失败", e);
                    errorProductCode = errorProductCode + p.getCode() + ":" + e.getMessage() + " ;";
                }
            }
        }
        if (errorProductCode != "") {
            responseData.setSuccess(false);
            responseData.setMessage(errorProductCode);
            return responseData;
        } else {
            responseData.setSuccess(true);
            responseData.setMessage("获取图片成功!");
            return responseData;
        }

    }

    /**
     * 下载图片excel模板
     *
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "/hmall/mst/media/downloadExcelModel", method = RequestMethod.GET)
    public void importExcel(HttpServletRequest request, HttpServletResponse response) {
        try {
            new ExcelUtil(Media.class).downloadExcelModel(request, response, "template/media.xlsx");
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 图片信息Excel导入
     *
     * @param request
     * @param file
     * @return
     */
    @RequestMapping(value = "/hmall/mst/media/importExcel", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData importExcel(HttpServletRequest request, MultipartFile file) {
        IRequest iRequest = this.createRequestContext(request);
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);

        List<Media> list = null;
        try {
            list = new ExcelUtil<Media>(Media.class).importExcel(file.getOriginalFilename(), "图片信息列表", file.getInputStream());
        } catch (Exception e) {
            logger.error(e.getMessage());
            responseData.setSuccess(false);
            responseData.setMessage("excel解析失败,请联系管理员！");
            return responseData;
        }

        if (CollectionUtils.isNotEmpty(list)) {
            try {
                String result = service.checkMedia(iRequest, list);
                if (!result.equals("")) {
                    responseData.setSuccess(false);
                    responseData.setMessage(result);
                    return responseData;
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                responseData.setSuccess(false);
                responseData.setMessage(e.getMessage());
                return responseData;
            }
        }
        return responseData;
    }

    /**
     * 显示图片
     *
     * @param request
     * @param path
     * @return
     */
    @RequestMapping(value = "/hmall/mst/media/getImageIoStream")
    @ResponseBody
    public ResponseData getImageIoStream(HttpServletRequest request, @RequestParam("path") String path) {
        String imageRootPath = nfsImageRootPath.substring(0, nfsImageRootPath.length() - 1);
        IRequest requestContext = createRequestContext(request);
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);
        try {
            String result = service.getImageIoStream(requestContext, imageRootPath + path);
            List<String> list = new ArrayList<>();
            list.add(result);
            responseData.setRows(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            responseData.setSuccess(false);
            responseData.setMessage(path + "读取IO流发生异常！");
            return responseData;
        }
        return responseData;
    }

}