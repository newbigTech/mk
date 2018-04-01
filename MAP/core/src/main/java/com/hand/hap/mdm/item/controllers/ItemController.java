package com.hand.hap.mdm.item.controllers;/**
 * Created by WangQiang on 2017/5/22.
 * Email: qiang.wang01@hand-china.com
 */

import com.hand.hap.core.IRequest;
import com.hand.hap.mdm.bom.dto.Bom;
import com.hand.hap.mdm.bom.service.IBomService;
import com.hand.hap.mdm.item.dto.Item;
import com.hand.hap.mdm.item.dto.ItemCondition;
import com.hand.hap.mdm.item.dto.MdmItemValue;
import com.hand.hap.mdm.item.service.IItemService;
import com.hand.hap.system.controllers.BaseController;
import com.markor.map.framework.common.interf.entities.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


/**
 * @author qiang.wang01@hand-china.com
 * @version 1.0
 * @name ItemController
 * @description 物料主数据controller
 * @date 2017/5/22
 */
@Controller
public class ItemController extends BaseController {
    @Autowired
    private IItemService iItemService;

    @Autowired
    private IBomService iBomService;

    /**
     * 查找平台
     *
     * @param item
     * @param page
     * @param pageSize
     * @param request
     * @return
     * @author yanjie.zhang@hand-china.com
     * @date 2017/05/23
     */
    @RequestMapping(value = "/hap/mdm/item/selectPlatform")
    @ResponseBody
    public ResponseData selectPlatform(Item item, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                       @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        if ("ALL".equals(item.getBrand())) {
            item.setBrand(null);
        }
        return new ResponseData(iItemService.selectPlatform(item, requestContext, page, pageSize));

    }


    /**
     * 物料属性的查询
     *
     * @param itemId
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/mdm/item/queryItemCondition")
    @ResponseBody
    public ResponseData queryItemCondition(Long itemId, Long platformId, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        ItemCondition itemCondition = new ItemCondition();
        itemCondition = iItemService.queryItemCondition(itemId, platformId);
        List<ItemCondition> itemConditionList = new ArrayList<>();
        itemConditionList.add(itemCondition);
        return new ResponseData(itemConditionList);
    }

    /**
     * 物料属性的提交
     *
     * @param itemCondition
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/mdm/item/ItemConditionSubmit")
    @ResponseBody
    public ResponseData ItemConditionSubmt(@RequestBody ItemCondition itemCondition, BindingResult result, HttpServletRequest request) {
        ResponseData rd = new ResponseData();
        Boolean flag = true;
        String mess = "保存成功！";
        long n = 0l;
        try {
            iItemService.itemConditionSubmit(itemCondition);
        } catch (Exception e) {
            flag = false;
            mess = e.getMessage();
        }
        rd.setMessage(mess);
        rd.setSuccess(flag);
        List<ItemCondition> itemConditionList = new ArrayList<>();
        itemConditionList.add(itemCondition);
        if (flag) {
            n = itemConditionList.size();
        }
        rd.setRows(itemConditionList);
        rd.setTotal(n);
        return rd;
    }

    /**
     * 子节点选配属性的批量覆盖
     *
     * @param itemCondition
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/mdm/item/CoverSubItem")
    @ResponseBody
    public ResponseData CoverSubItem(@RequestBody ItemCondition itemCondition, BindingResult result, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        getValidator().validate(itemCondition, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        Bom bom = new Bom();
        bom.setItemId(itemCondition.getItemId());
        bom.setBomId(itemCondition.getBomId());
        List<Bom> bomList = new ArrayList<>();
        bomList = iBomService.selectSubTree(bom);
        iItemService.coverSubItem(itemCondition, bomList);
        return new ResponseData();
    }

    /**
     * 子节点选配属性的批量覆盖
     *
     * @param itemCondition
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/mdm/item/CoverBroItem")
    @ResponseBody
    public ResponseData CoverBroItem(@RequestBody ItemCondition itemCondition, BindingResult result, HttpServletRequest request) {
        List<MdmItemValue> mdmItemValueList = iItemService.coverBroItem(itemCondition);
        return new ResponseData(mdmItemValueList);
    }
}
