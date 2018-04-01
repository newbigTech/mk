package com.hand.hmall.as.controllers;

import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.as.dto.AsCompensateEntry;
import com.hand.hmall.as.service.IAsCompensateEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author xuxiaoxue
 * @version 0.1
 * @name AsCompensateEntryController
 * @description 销售赔付单行表Controller类
 * @date 2017/10/11
 */
@Controller
public class AsCompensateEntryController extends BaseController {

    @Autowired
    private IAsCompensateEntryService service;


    /**
     * 根据赔付单ID查询赔付单行信息
     *
     * @param asCompensateEntry
     * @return
     */
    @RequestMapping(value = "/hmall/as/compensate/entry/selectCompensateEntryById")
    @ResponseBody
    public ResponseData selectCompensateEntryById(@RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                                  @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, AsCompensateEntry asCompensateEntry) {
        return new ResponseData(service.selectCompensateEntryById(page, pageSize, asCompensateEntry));
    }

    /**
     * 删除赔付单行
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hmall/as/compensate/entry/deleteCompensateEntryById")
    @ResponseBody
    public ResponseData deleteCompensateEntryById(@RequestBody List<AsCompensateEntry> dto) {
        return service.deleteCompensateEntryById(dto);
    }
}