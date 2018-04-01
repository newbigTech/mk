package com.hand.hmall.as.controllers;

import com.hand.common.util.ExcelUtil;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.as.dto.AsMaterial;
import com.hand.hmall.as.service.IAsMaterialService;
import com.hand.hmall.common.service.ISequenceGenerateService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class AsMaterialController extends BaseController {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private IAsMaterialService service;
    @Autowired
    private ISequenceGenerateService sequenceGenerateService;

    @RequestMapping(value = "/hmall/as/material/query")
    @ResponseBody
    public ResponseData query(AsMaterial dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    /**
     * @author xuxiaoxue
     * @version 0.1
     * @name AsMaterialController
     * @description 物耗单列表页查询
     * @date 2017/12/6
     */
    @RequestMapping(value = "/hmall/as/material/list/query")
    @ResponseBody
    public ResponseData query(HttpServletRequest request, @RequestParam Map maps) {
        AsMaterial dto = new AsMaterial();
        String str = (String) maps.get("data");
        JSONObject jsonObject = JSONObject.fromObject(str);
        JSONObject status = jsonObject.getJSONObject("status");
        Map<String, Object> data = (Map<String, Object>) jsonObject.get("pages");
        IRequest requestContext = createRequestContext(request);
        int page = 0;
        if (data.get("page") != null) {
            page = Integer.parseInt(data.get("page").toString());
        }
        int pageSize = 0;
        if (data.get("pagesize") != null) {
            pageSize = Integer.parseInt(data.get("pagesize").toString());
        }
        //获得物耗单状态
        JSONArray materialStatus = status.getJSONArray("materialStatus");
        String[] strMaterialStatus = new String[materialStatus.size()];
        for (int i = 0; i < materialStatus.size(); i++) {
            strMaterialStatus[i] = materialStatus.get(i).toString();
        }
        //物耗单单号
        String code = null;
        if (data.get("code") != null) {
            code = data.get("code").toString();
        }
        //服务单单号
        String serviceOrderCode = null;
        if (data.get("serviceOrderCode") != null) {
            serviceOrderCode = data.get("serviceOrderCode").toString();
        }
        //平台订单编号
        String escOrderCode = null;
        if (data.get("escOrderCode") != null) {
            escOrderCode = data.get("escOrderCode").toString();
        }
        //用户账号
        String customerid = null;
        if (data.get("customerid") != null) {
            customerid = data.get("customerid").toString();
        }
        //收货人手机号
        String mobile = null;
        if (data.get("mobile") != null) {
            mobile = data.get("mobile").toString();
        }
        //SAP系统单号
        String sapCode = null;
        if (data.get("sapCode") != null) {
            sapCode = data.get("sapCode").toString();
        }
        //创建时间从
        String creationDateStart = null;
        if (data.get("creationDateStart") != null) {
            creationDateStart = data.get("creationDateStart").toString();
        }
        //创建时间至
        String creationDateEnd = null;
        if (data.get("creationDateEnd") != null) {
            creationDateEnd = data.get("creationDateEnd").toString();
        }
        //完结时间从
        String finishTimeStart = null;
        if (data.get("finishTimeStart") != null) {
            finishTimeStart = data.get("finishTimeStart").toString();
        }
        //完结时间至
        String finishTimeEnd = null;
        if (data.get("finishTimeEnd") != null) {
            finishTimeEnd = data.get("finishTimeEnd").toString();
        }
        //是否收费
        String isCharge = null;
        if (data.get("isCharge") != null) {
            isCharge = data.get("isCharge").toString();
        }
        //同步Retail标识
        String syncRetail = null;
        if (data.get("syncRetail") != null) {
            syncRetail = data.get("syncRetail").toString();
        }
        List<AsMaterial> asMaterials = service.selectMaterialList(requestContext, code, serviceOrderCode, escOrderCode, customerid, mobile, sapCode, creationDateStart, creationDateEnd, finishTimeStart, finishTimeEnd, isCharge, syncRetail, strMaterialStatus, page, pageSize);
        return new ResponseData(asMaterials);
    }


    /**
     * @author xuxiaoxue
     * @version 0.1
     * @name AsMaterialController
     * @description 导出物耗单列表
     * @date 2017/12/6
     */
    @RequestMapping(value = "/hmall/as/material/exportExcel")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response, @RequestParam String code, @RequestParam String serviceOrderCode, @RequestParam String escOrderCode, @RequestParam String customerid,
                            @RequestParam String mobile, @RequestParam String sapCode, @RequestParam String sCreationDateStart, @RequestParam String sCreationDateEnd, @RequestParam String sFinishTimeStart, @RequestParam String sFinishTimeEnd,
                            @RequestParam String isCharge, @RequestParam String syncRetail, @RequestParam String[] materialStatus) {
        IRequest requestContext = createRequestContext(request);
        //创建时间从
        if (!"".equals(sCreationDateStart)) {
            sCreationDateStart = sdf.format(new Date(sCreationDateStart));
        }
        //创建时间至
        if (!"".equals(sCreationDateEnd)) {
            sCreationDateEnd = sdf.format(new Date(sCreationDateEnd));
        }
        //完结时间从
        if (!"".equals(sFinishTimeStart)) {
            sFinishTimeStart = sdf.format(new Date(sFinishTimeStart));
        }
        //完结时间至
        if (!"".equals(sFinishTimeEnd)) {
            sFinishTimeEnd = sdf.format(new Date(sFinishTimeEnd));
        }
        List<AsMaterial> list = service.selectMaterialList(requestContext, code, serviceOrderCode, escOrderCode, customerid, mobile, sapCode, sCreationDateStart, sCreationDateEnd, sFinishTimeStart, sFinishTimeEnd, isCharge, syncRetail, materialStatus);
        new ExcelUtil(AsMaterial.class).exportExcel(list, "物耗单列表", list.size(), request, response, "物耗单列表.xlsx");
    }

    @RequestMapping(value = "/hmall/as/material/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<AsMaterial> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hmall/as/material/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<AsMaterial> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    /**
     * 保存物耗单信息
     *
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "/hmall/as/material/saveMaterialOrder")
    @ResponseBody
    public ResponseData saveMaterialOrder(HttpServletRequest request, @RequestBody AsMaterial asMaterial) {
        IRequest iRequest = this.createRequestContext(request);
        return service.saveMaterialOrder(iRequest, asMaterial);
    }

    /**
     * 同步到retail
     *
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hmall/as/material/materialSyncRetail")
    @ResponseBody
    public ResponseData materialSyncRetail(HttpServletRequest request, @RequestBody AsMaterial dto) {
        IRequest iRequest = this.createRequestContext(request);
        return service.materialSyncRetail(iRequest, dto);

    }
}