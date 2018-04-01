package com.hand.hmall.mst.controllers;

import com.hand.common.util.ExcelUtil;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.mst.dto.MstFabric;
import com.hand.hmall.mst.service.IMstFabricService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name MstFabricController
 * @description 面料等级
 * @date 2017年5月26日10:52:23
 */

@Controller
public class MstFabricController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IMstFabricService service;


    @RequestMapping(value = "/hmall/mst/fabric/query")
    @ResponseBody
    public ResponseData query(MstFabric dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/mst/fabric/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<MstFabric> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hmall/mst/fabric/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<MstFabric> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    /**
     * 下载面料等级excel模板
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/hmall/mst/fabric/downloadExcelModel", method = RequestMethod.GET)
    public void importExcel(HttpServletRequest request, HttpServletResponse response) {
        try {
            new ExcelUtil(MstFabric.class).downloadExcelModel(request, response, "template/fabric.xlsx");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * @param request
     * @param file
     * @return
     * @description 面料等级信息导入
     */
    @RequestMapping(value = "/hmall/mst/fabric/importExcel", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData importExcel(HttpServletRequest request, MultipartFile file) {
        IRequest iRequest = this.createRequestContext(request);
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);

        List<MstFabric> list = null;
        try {
            list = new ExcelUtil<MstFabric>(MstFabric.class).importExcel(file.getOriginalFilename(), "面料等级列表", file.getInputStream());
        } catch (Exception e) {
            logger.error(e.getMessage());
            responseData.setSuccess(false);
            responseData.setMessage("excel解析失败,请联系管理员！");
            return responseData;
        }

        if (CollectionUtils.isNotEmpty(list)) {
            try {
                String result = service.checkFabric(iRequest, list);
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
}