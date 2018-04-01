package com.hand.hap.mdm.bom.controllers;

/**
 * @author xiaowei.zhang@hand-china.com
 * @version 0.1
 * @name MdmBomQuantityController
 * @description bom用量卷算controller
 * @date 2017/6/21
 */

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hand.common.util.BeanUtilsExtends;
import com.hand.common.util.ExcelUtil;
import com.hand.hap.core.IRequest;
import com.hand.hap.excel.dto.ColumnInfo;
import com.hand.hap.excel.dto.ExportConfig;
import com.hand.hap.excel.service.IExportService;
import com.hand.hap.mdm.bom.dto.MdmBomQuantity;
import com.hand.hap.mdm.bom.dto.MdmBomQuantityDto;
import com.hand.hap.mdm.bom.service.IMdmBomQuantityService;
import com.hand.hap.system.controllers.BaseController;
import com.markor.map.framework.common.interf.entities.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class MdmBomQuantityController extends BaseController {

    //add by yougui.wu@hand-china.com
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    IExportService excelService;
    @Autowired
    private IMdmBomQuantityService service;

    /**
     * bom卷算用量数据查询
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/mdm/bom/quantity/query")
    @ResponseBody
    public ResponseData query(MdmBomQuantity dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectQuantity(requestContext, dto, page, pageSize));
    }


    /**
     * 选项值用量卷算导出
     *
     * @param request
     * @param config
     * @param httpServletResponse
     * @throws IOException
     * @author yougui.wu@hand-china.com
     * @date 2017/08/3
     */
    @RequestMapping(value = "/hap/mdm/bom/quantity/export")
    public void createXLS(HttpServletRequest request, @RequestParam String config, HttpServletResponse httpServletResponse) throws IOException {
        IRequest requestContext = this.createRequestContext(request);
        JavaType type = this.objectMapper.getTypeFactory().constructParametrizedType(ExportConfig.class, ExportConfig.class, new Class[]{MdmBomQuantity.class, ColumnInfo.class});
        ExportConfig exportConfig = (ExportConfig) this.objectMapper.readValue(config, type);
        MdmBomQuantity mdmBomQuantity = (MdmBomQuantity) exportConfig.getParam();
        List<MdmBomQuantity> list = service.selectQuantity(requestContext, mdmBomQuantity, 0, 0).getRows();
        List<MdmBomQuantityDto> excelList = BeanUtilsExtends.copyListProperties(list, MdmBomQuantityDto.class);
        new ExcelUtil(MdmBomQuantityDto.class).exportExcel(excelList, "选项值用量卷算导出", excelList.size(), request, httpServletResponse, "选项值用量卷算导出.xlsx");
    }
}