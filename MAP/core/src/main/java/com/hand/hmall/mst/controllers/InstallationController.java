package com.hand.hmall.mst.controllers;

import com.hand.common.util.ExcelUtil;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.mst.dto.Installation;
import com.hand.hmall.mst.service.IInstallationService;
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
import java.util.Map;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 安装费对象的Controller层
 * @date 2017/7/10 14:37
 */
@Controller
public class InstallationController extends BaseController {

    @Autowired
    private IInstallationService service;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/hmall/mst/installation/query")
    @ResponseBody
    public ResponseData query(Installation dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/mst/installation/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<Installation> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hmall/mst/installation/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<Installation> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    /**
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     * @description 安装费维护查询界面
     */
    @RequestMapping(value = "/hmall/mst/installation/selectInstallation")
    @ResponseBody
    public ResponseData selectInstallation(Installation dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                           @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectInstallation(requestContext, dto, page, pageSize));
    }

    /**
     * 下载安装费excel模板
     *
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "/hmall/mst/installation/downloadExcelModel", method = RequestMethod.GET)
    public void importExcel(HttpServletRequest request, HttpServletResponse response) {
        new ExcelUtil(Installation.class).downloadExcelModel(request, response, "安装费列表导入模板.xlsx", "安装费信息列表");
    }

    /**
     * 安装费信息Excel导入
     *
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "/hmall/mst/installation/importExcel", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData importExcel(HttpServletRequest request, MultipartFile file) {
        IRequest iRequest = this.createRequestContext(request);
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);

        List<Installation> list = null;
        try {
            list = new ExcelUtil<Installation>(Installation.class).importExcel(file.getOriginalFilename(), "安装费信息列表", file.getInputStream());
        } catch (Exception e) {
            logger.error(e.getMessage());
            responseData.setSuccess(false);
            responseData.setMessage("excel解析失败,请联系管理员！");
            return responseData;
        }

        if (list != null && list.size() > 0) {
            Map<String,Object> map = service.checkInstallation(iRequest, list);
            String result = (String) map.get("result");
            List<Installation> installationList = (List<Installation>) map.get("installationList");

            if(CollectionUtils.isNotEmpty(installationList)){
                for (Installation installation : installationList) {
                    //插入数据
                    try {
                        if(installation.getInstallationId() != null){
                            service.updateByPrimaryKeySelective(iRequest,installation);
                        }else{
                            service.insertSelective(iRequest,installation);
                        }
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        responseData.setSuccess(false);
                        responseData.setMessage("插入安装费数据失败！");
                    }
                }
            }
            if (result != null) {
                responseData.setSuccess(false);
                responseData.setMessage(result);
                return responseData;
            }
        }
        return responseData;
    }

}