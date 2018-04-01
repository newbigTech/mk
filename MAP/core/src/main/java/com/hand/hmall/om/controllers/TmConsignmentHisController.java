package com.hand.hmall.om.controllers;

import com.hand.common.util.ExcelUtil;
import com.hand.hap.core.IRequest;
import com.hand.hap.mdm.item.dto.ItemCondition;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.om.dto.HmallTmConsignmentHis;
import com.hand.hmall.om.dto.TmData;
import com.hand.hmall.om.service.IOrderService;
import com.hand.hmall.om.service.ITmConsignmentHisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 刘宏玺
 * @version 0.1
 * @name TmConsignmentHisController
 * @description 天猫订单发货信息导出结果用Controller
 * @date 2017/6/21
 */

@Controller
public class TmConsignmentHisController extends BaseController {

    @Autowired
    private ITmConsignmentHisService service;

    @Autowired
    private IOrderService orderService;

    /**
     * 查询发货单导出历史信息(倒序)
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/om/order/selectTmExportData")
    @ResponseBody
    public ResponseData query(HmallTmConsignmentHis dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryAll(requestContext, dto, page, pageSize));
    }

    /**
     * 导出发货单历史导出信息
     *
     * @param exportTime
     * @param request
     * @return
     */
    @RequestMapping(value = "/om/order/exportTmExportData")
    public void queryItemCondition(String exportTime, HttpServletRequest request, HttpServletResponse response) {

        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(exportTime);
            List<TmData> list = orderService.exportTmDataHis(date);
            new ExcelUtil(TmData.class).exportExcel(list, "天猫发货单列表", list.size(), request, response, "天猫发货单列表.xlsx");
        }
        catch (ParseException e)
        {
            System.out.println(e.getMessage());
        }



    }
}