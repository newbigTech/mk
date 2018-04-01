package com.hand.hmall.hr.controllers;

import com.hand.common.util.ExcelUtil;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.hr.dto.MarkorEmployee;
import com.hand.hmall.hr.service.IMarkorEmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 重写的员工管理页面中的导入相关的controller类
 * @date 2017/7/10 14:37
 */
@Controller
public class MarkorEmployeeController extends BaseController {

    @Autowired
    private IMarkorEmployeeService service;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * 下载员工excel模板
     *
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "/hmall/hr/employee/downloadExcelModel", method = RequestMethod.GET)
    public void importExcel(HttpServletRequest request, HttpServletResponse response) {
        new ExcelUtil<>(MarkorEmployee.class).downloadExcelModel(request, response, "员工列表导入模板.xlsx", "员工信息列表");
    }


    /**
     * 员工信息Excel导入
     *
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "/hmall/hr/employee/importExcel", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData importExcel(HttpServletRequest request, MultipartFile file) {
        IRequest iRequest = this.createRequestContext(request);
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);

        List<MarkorEmployee> list;
        try {
            list = new ExcelUtil<>(MarkorEmployee.class).importExcel(file.getOriginalFilename(), "员工信息列表", file.getInputStream());
        } catch (Exception e) {
            logger.error(e.getMessage());
            responseData.setSuccess(false);
            responseData.setMessage("excel解析失败,请联系管理员！");
            return responseData;
        }

        if (list != null && list.size() > 0) {
            String result = service.checkEmployee(iRequest, list);
            if (result != null) {
                responseData.setSuccess(false);
                responseData.setMessage(result);
                return responseData;
            }
            for (MarkorEmployee employee : list) {
                try {
                    service.insertSelective(iRequest, employee);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    responseData.setSuccess(false);
                    responseData.setMessage("插入员工信息数据失败！");
                    return responseData;
                }
            }
        }

        return responseData;
    }

    /**
     * @param codeOrName - 编码或名称
     * @return 员工列表
     */
    @GetMapping(value = "/hmall/hr/employee/query")
    @ResponseBody
    public ResponseData query(HttpServletRequest request, @RequestParam(name = "codeOrName", defaultValue = "") String codeOrName) {
        IRequest iRequest = createRequestContext(request);
        return new ResponseData(service.queryByCodeOrName(iRequest, codeOrName));
    }

    /**
     * 查询当前登录者的岗位信息列表(可能具有多个岗位)
     * @param request
     * @return
     */
    @GetMapping(value = "/hmall/hr/currentPositions")
    @ResponseBody
    public ResponseData queryCurrentPositions(HttpServletRequest request) {
        IRequest iRequest = createRequestContext(request);
        return new ResponseData(service.queryCurrentPositions(iRequest));
    }

}