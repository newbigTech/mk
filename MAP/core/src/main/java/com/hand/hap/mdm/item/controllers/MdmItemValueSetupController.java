package com.hand.hap.mdm.item.controllers;

import com.hand.hap.mdm.item.dto.MdmItemValueSetupCondition;
import com.hand.hap.mdm.item.exception.ItemAttrGroupSetupException;
import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.mdm.item.service.IMdmItemValueSetupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @author chenzhigang@markor.com.cn
 * @version 0.1
 * @name
 * @description 物料平台属性组配置规则控制器
 * @date
 */
@Controller
public class MdmItemValueSetupController extends BaseController {

    @Autowired
    private IMdmItemValueSetupService service;

    /**
     * 查询可用于配置的属性组列表和物料平台的属性组配置信息
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hap/mdm/item/value/setup/query", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData query(HttpServletRequest request, @RequestBody MdmItemValueSetupCondition dto) {
        IRequest requestContext = createRequestContext(request);
        try {
            return new ResponseData(Arrays.asList(service.queryMdmItemAttrGroupSetupCondition(dto)));
        } catch (ItemAttrGroupSetupException e) {
            e.printStackTrace();
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(e.getMessage());
            return responseData;
        }
    }

    /**
     * 新建或更新物料平台的属性组配置信息
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hap/mdm/item/value/setup/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody MdmItemValueSetupCondition dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(Arrays.asList(service.batchUpdateItemAttrGroupSetup(dto)));
    }

}
