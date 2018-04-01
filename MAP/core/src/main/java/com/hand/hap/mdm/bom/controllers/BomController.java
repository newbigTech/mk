package com.hand.hap.mdm.bom.controllers;/**
 * Created by WangQiang on 2017/5/22.
 * Email: qiang.wang01@hand-china.com
 */

import com.hand.hap.core.IRequest;
import com.hand.hap.mdm.bom.dto.Bom;
import com.hand.hap.mdm.bom.service.IBomService;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qiang.wang01@hand-china.com
 * @version 1.0
 * @name BomController
 * @description system的bom的CRUD类
 * @date 2017/5/22
 */
@Controller
public class BomController extends BaseController {

    @Autowired
    private IBomService iBomService;

    /**
     * 树查询
     *
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/mdm/bom/queryTree")
    @ResponseBody
    public ResponseData queryTree(@RequestBody Bom dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        List<Bom> itemList = iBomService.selectAllTree(requestContext, dto);
        return new ResponseData(itemList);
    }

    /**
     * 浅层bom查询,(不带子树)树查询
     *
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/mdm/bom/queryBomTree")
    @ResponseBody
    public ResponseData queryBomTree(@RequestBody Bom dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        List<Bom> itemList = iBomService.selectAllBomTree(requestContext, dto);
        return new ResponseData(itemList);
    }

    /**
     * 平台树查询
     *
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/mdm/bom/queryPlatform")
    @ResponseBody
    public ResponseData queryPlatForm(@RequestBody Bom dto, HttpServletRequest request) {
        List<Bom> itemList = iBomService.selectPlatform(dto);
        return new ResponseData(itemList);
    }


    /**
     * 平台树查询
     *
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/mdm/bom/querySelectTree")
    @ResponseBody
    public ResponseData querySelectTree(@RequestBody Bom dto, HttpServletRequest request) {
        List<Bom> itemList = iBomService.querySelectTree(dto);
        return new ResponseData(itemList);
    }

    /**
     * 操作树树的提交
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/mdm/bom/submitTree")
    @ResponseBody
    public ResponseData updateTree(@RequestBody Bom dto, BindingResult result, HttpServletRequest request) {
        if (result.hasErrors()) {
            ResponseData rd = new ResponseData(false);
            rd.setMessage(getErrorMessage(result, request));
            return rd;
        }
        IRequest requestCtx = createRequestContext(request);
        List<Bom> itemList = new ArrayList<>();
        itemList.add(iBomService.batchUpdateTree(requestCtx, dto));
        return new ResponseData(itemList);
    }

    /**
     * 操作树删除标记
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/mdm/bom/delete")
    @ResponseBody
    public ResponseData deleteTree(@RequestBody Bom bom, BindingResult result, HttpServletRequest request) {
        if (result.hasErrors()) {
            ResponseData rd = new ResponseData(false);
            rd.setMessage(getErrorMessage(result, request));
            return rd;
        }
        IRequest requestCtx = createRequestContext(request);
        List<Bom> bomList = new ArrayList<>();
        bomList = iBomService.selectSubTree(bom);
        iBomService.updateDelete(bomList);
        return new ResponseData();
    }


    /**
     * 选配约束树查询
     *
     * @param dto
     * @param request
     * @return
     * @author yanjie.zhang@hand-china.com
     * @date 2017/05/26
     */
    @RequestMapping(value = "/hap/mdm/bom/selectRmTree")
    @ResponseBody
    public ResponseData selectRmTree(Bom dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        dto.setItemId(dto.getPlatformId());
        List<Bom> itemList = iBomService.selectRmTree(requestContext, dto);
        return new ResponseData(itemList);
    }


    /**
     * 更新选项值用量数据
     *
     * @param request
     * @return
     * @author yanjie.zhang@hand-china.com
     * @date 2017/06/24
     */
    @RequestMapping(value = "/hap/mdm/bom/updateChooseValue")
    @ResponseBody
    public ResponseData updateChooseValue(@RequestBody Bom bom, HttpServletRequest request) {
        ResponseData rd = new ResponseData();
        IRequest requestCtx = createRequestContext(request);
        boolean flag = true;
        String mess = "更新成功！";
        try {
            iBomService.updateChooseValue(requestCtx, bom);
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
            mess = e.getMessage();
        }
        rd.setMessage(mess);
        rd.setSuccess(flag);
        return rd;
    }
}
