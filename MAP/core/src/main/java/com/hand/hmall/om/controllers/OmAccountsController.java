package com.hand.hmall.om.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.util.ExcelUtil;
import com.hand.hmall.om.dto.OmAccounts;
import com.hand.hmall.om.service.IOmAccountsService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class OmAccountsController extends BaseController {

    @Autowired
    private IOmAccountsService service;

    /***
     *
     * 模板下载
     * 2017/10/23
     * @param response
     * @param request
     * @throws IOException
     * @author shoupeng.wei@hand-china.com
     */
    @RequestMapping(value = {"/hmall/om/accounts/downloadAccounts"}, method = {RequestMethod.GET})
    public void printTemplate(HttpServletResponse response, HttpServletRequest request) throws IOException {
        String sheetName = "财务账单导入";//sheet名
        String fileName = "财务账单导入.xlsx";//文件名
        String[] nameArray = new String[]{"记账时间","微信支付业务单号","资金流水单号", "业务名称", "业务类型", "收支类型", "收支金额(元)","账户结余(元)",
                "资金变更提交申请人","备注","业务凭证号"};//字段数据，模板数据
        int[] lengthArray = new int[]{15, 15, 30, 15, 15, 15, 10, 10, 15, 25, 25};//表格中对应字段宽度
        /**简单下拉框的生成**/
        HashMap<Integer, String[]> dropDownMap = new HashMap();
        String relation[] = new String[]{"交易","退款","服务费","服务费退款","提现","其他"};//下拉框内容
        dropDownMap.put(3, relation);//map中下拉框生成位置下标作为key，下拉内容作为value

        /***生成excel模板***/
        ExcelUtil.creatExecelTemplate(sheetName, fileName, nameArray, lengthArray, dropDownMap, response, request);
    }

    /**
     * 文件上传
     *
     * @param request
     * @param files
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     * @author hengzhang04@hand-china.com
     * <p>
     * 2017/8/30
     */
    @RequestMapping(value = {"/hmall/om/accounts/uploadAccounts"}, method = {RequestMethod.POST})
    @ResponseBody
    public ResponseData upload(HttpServletRequest request, MultipartFile files) throws IOException, InvalidFormatException {
        IRequest requestContext = this.createRequestContext(request);
        ResponseData responseData1 = new ResponseData();

        try {
            //调用excel工具类对传入文件进行处理
            ArrayList excelList = service.FilesAnalysis(files);
            if (excelList == null) {
                responseData1.setMessage(files.getOriginalFilename() + "是一个空文件！");
                responseData1.setSuccess(false);
            } else {
                this.service.insertAllValue(requestContext, excelList);
                responseData1.setSuccess(true);
            }

        } catch (RuntimeException e1) {
            e1.printStackTrace();
            responseData1.setMessage("导入失败！出现异常错误:" + e1.getLocalizedMessage());
            responseData1.setSuccess(false);
        } catch (Exception e) {
            responseData1.setMessage(e.getMessage());
            responseData1.setSuccess(false);

        }

        return responseData1;
    }

    @RequestMapping(value = "/hmall/om/accounts/queryAccountsList")
    @ResponseBody
    public ResponseData queryAccountsList(OmAccounts dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                          @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request){
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryAccountsList(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/om/accounts/query")
    @ResponseBody
    public ResponseData query(OmAccounts dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/om/accounts/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<OmAccounts> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hmall/om/accounts/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<OmAccounts> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    @RequestMapping(value = "/hmall/om/accounts/getAccountsForBalance")
    @ResponseBody
    public ResponseData getAccountsForBalance(HttpServletRequest request, OmAccounts dto,  @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.getAccountsForBalance(requestContext, dto, page, pageSize));
    }
}