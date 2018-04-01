package com.hand.hap.atp.controllers;

/**
 * @author xiaowei.zhang@hand-china.com
 * @version 0.1
 * @name AtpProductInvInfoController
 * @description 成品库存查询界面Controller类
 * @date 2017/6/21
 */

import com.hand.hap.atp.dto.AtpProductInvInfo;
import com.hand.hap.atp.service.IAtpProductInvInfoService;
import com.hand.hap.system.controllers.BaseController;
import com.markor.map.framework.common.interf.entities.PaginatedList;
import com.markor.map.framework.common.interf.entities.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AtpProductInvInfoController extends BaseController {

    @Autowired
    private IAtpProductInvInfoService service;

    /**
     * 查询可用物料库存总数
     *
     * @param dto     查询参数
     * @param request request
     * @return ResponseData
     */
    @RequestMapping(value = "/hap/atp/product/inv/info/queryAtpProductInvAvailableQuantity")
    @ResponseBody
    public ResponseData queryAtpProductInvAvailableQuantity(AtpProductInvInfo dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                                            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        PaginatedList<AtpProductInvInfo> list;
        //如果不区分仓库
        if (dto.getDiffInventoryLocal() == null || dto.getDiffInventoryLocal().equals("FALSE")) {
            dto.setInventoryLocations(null);
            list = service.queryAtpProductWithoutLocation(dto, page, pageSize);
            //如果区分仓库
        } else {
            list = service.queryAtpProductInvAvailableQuantity(dto, page, pageSize);
        }
        return new ResponseData(list);
    }
}