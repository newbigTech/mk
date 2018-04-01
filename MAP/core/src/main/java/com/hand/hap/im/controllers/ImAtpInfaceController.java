package com.hand.hap.im.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.im.dto.ImAtpInface;
import com.hand.hap.im.service.IImAtpInfaceService;
import com.hand.hap.system.controllers.BaseController;
import com.markor.map.framework.common.interf.entities.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yanjie.zhang@hand-china.com
 * @version 0.1
 * @name ImAtpInfaceController
 * @description ATP基础数据Controller
 * @date 2017/6/21
 **/
@Controller
public class ImAtpInfaceController extends BaseController {

    @Autowired
    private IImAtpInfaceService service;

    /**
     * ATP查询
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/im/atp/inface/query")
    @ResponseBody
    public ResponseData query(ImAtpInface dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryList(requestContext, dto, page, pageSize));
    }

    /**
     * ATP CRUD
     *
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hap/im/atp/inface/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<ImAtpInface> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }


    /*
     */
/**
 * 推送ATP寻源结果到ZMALL
 * add by  yougui.wu@hand-china.com
 *
 * @param request
 * @return
 *//*

    @RequestMapping(value = "/hap/im/item/inface/wsAtpMapToZmall")
    @ResponseBody
    public ResponseData wsExportItem(HttpServletRequest request) {

        ResponseData responseData = new ResponseData();
        IRequest requestCtx = createRequestContext(request);

        try {
            service.wsAtpMapToZmall(requestCtx);
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }
*/

    /**
     * ATPdebug
     *
     * @param request
     * @param dto
     * @return
     * @author qiang.wang01@hand-china.com
     */
    @RequestMapping(value = "/hap/im/atp/inface/startDebug")
    @ResponseBody
    public ResponseData startDebug(HttpServletRequest request, @RequestBody ImAtpInface dto) {
        ResponseData responseData = new ResponseData();
        try {
            dto = service.atpDebug(dto);
            List<ImAtpInface> imAtpInfaceList = new ArrayList<>();
            imAtpInfaceList.add(dto);
            responseData.setRows(imAtpInfaceList);
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }

    /**
     * ATPtest
     *
     * @param request
     * @return
     * @author qiang.wang01@hand-china.com
     */
    @RequestMapping(value = "/hap/im/atp/inface/WQtest")
    @ResponseBody
    public ResponseData startDebug(HttpServletRequest request) {
        IRequest iRequest = RequestHelper.newEmptyRequest();
        try {
            service.MapToZmallPinCode(iRequest);
        } catch (Exception e) {

        }
        return new ResponseData();
    }
}