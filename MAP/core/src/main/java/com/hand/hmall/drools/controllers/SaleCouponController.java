package com.hand.hmall.drools.controllers;

import com.hand.common.util.ExcelUtil;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hmall.drools.dto.CouponUserImportDto;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.job.CouponSynJob;
import com.hand.hmall.mst.dto.MstUser;
import com.hand.hmall.mst.service.IMstUserService;
import com.hand.hmall.util.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by shanks on 2017/1/5.
 * @desp 调用促销微服务 用于中台界面操作优惠券信息
 */
@Controller
@RequestMapping("/sale/coupon")
public class SaleCouponController extends BaseController {

    @Autowired
    IMstUserService userService;
    private RestTemplate restTemplate = new RestTemplate();
    @Value("#{configProperties['baseUri']}")
    private String baseUri;

    @Value("#{configProperties['modelUri']}")
    private String modelUri;


    /**
     * 查询优惠券列表 可选字段CouponId(优惠券Id编码)、couponName（优惠券名称）
     * couponCode（优惠券编码）、status（状态）、startDate（生效时间）、endDate（截止时间）
     *
     * @param httpServletRequest
     * @param datas
     * @return
     */
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData query(HttpServletRequest httpServletRequest, @RequestBody Map<String, Object> datas) {


        try {
            String url = "/h/sale/coupon/query";
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(datas, null);
            ResponseData responseData = restTemplate.exchange(baseUri + modelUri + url, HttpMethod.POST, entity, ResponseData.class).getBody();
            return responseData;
        } catch (Exception e) {
            System.err.println(e);
        }
        ResponseData responseData = new ResponseData();

        responseData.setSuccess(false);
        responseData.setMsg("读取数据错误");
        return responseData;
    }

    /**
     * 赠券页面调用，查询要赠送的优惠券 没有页面调用
     *
     * @param httpServletRequest
     * @param datas
     * @return
     */
    @RequestMapping(value = "/queryActivity", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData queryActivity(HttpServletRequest httpServletRequest, @RequestBody Map<String, Object> datas) {
        try {
            String url = "/h/sale/coupon/queryActivity";
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(datas, null);
            ResponseData responseData = restTemplate.exchange(baseUri + modelUri + url, HttpMethod.POST, entity, ResponseData.class).getBody();
            return responseData;
        } catch (Exception e) {
            System.err.println(e);
        }
        ResponseData responseData = new ResponseData();

        responseData.setSuccess(false);
        responseData.setMsg("读取数据错误");
        return responseData;
    }

    /**
     * 查询notIn中包含的couponId对应的优惠券信息 没有页面调用
     *
     * @param httpServletRequest
     * @param datas
     * @return
     */
    @RequestMapping(value = "/queryByNotIn", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData queryByNotIn(HttpServletRequest httpServletRequest, @RequestBody Map<String, Object> datas) {


        try {
            String url = "/h/sale/coupon/queryByNotIn";
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(datas, null);
            ResponseData responseData = restTemplate.exchange(baseUri + modelUri + url, HttpMethod.POST, entity, ResponseData.class).getBody();
            return responseData;
        } catch (Exception e) {
            System.err.println(e);
        }
        ResponseData responseData = new ResponseData();

        responseData.setSuccess(false);
        responseData.setMsg("读取数据错误");
        return responseData;
    }

    /**
     * 赠品发放界面，校验每单可发放赠品及促销总共赠送的赠品
     *
     * @param httpServletRequest
     * @param datas
     * @return
     */
    @RequestMapping(value = "/submitCountNumber", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData submitCountNumber(HttpServletRequest httpServletRequest, @RequestBody List<Map<String, Object>> datas) {


        try {
            String url = "/h/sale/coupon/submitCountNumber";
            HttpEntity<List<Map<String, Object>>> entity = new HttpEntity<>(datas, null);
            ResponseData responseData = restTemplate.exchange(baseUri + modelUri + url, HttpMethod.POST, entity, ResponseData.class).getBody();
            return responseData;
        } catch (Exception e) {
            System.err.println(e);
        }
        ResponseData responseData = new ResponseData();

        responseData.setSuccess(false);
        responseData.setMsg("读取数据错误");
        return responseData;
    }


    /**
     * 创建优惠券
     * @param map
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData submit(@RequestBody Map<String, Object> map, HttpServletRequest httpServletRequest) {
        try {
            IRequest iRequest = createRequestContext(httpServletRequest);
            map.put("userId", iRequest.getUserId());
            String url = "/h/sale/coupon/submit";
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, null);
            ResponseData responseData = restTemplate.exchange(baseUri + modelUri + url, HttpMethod.POST, entity, ResponseData.class).getBody();
            return responseData;
        } catch (Exception e) {
            System.err.println(e);
        }
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("存值失败");
        return responseData;
    }


    /**
     * 启用优惠券
     * @param httpServletRequest
     * @param datas
     * @return
     */
    @RequestMapping(value = "/startUsing", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData startUsing(HttpServletRequest httpServletRequest, @RequestBody List<Map<String, Object>> datas) {
        try {
            String url = "/h/sale/coupon/startUsing";
            HttpEntity<List<Map<String, Object>>> entity = new HttpEntity<>(datas, null);
            ResponseData responseData = restTemplate.exchange(baseUri + modelUri + url, HttpMethod.POST, entity, ResponseData.class).getBody();
            return responseData;
        } catch (Exception e) {
            System.err.println(e);
        }
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("读取数据错误");
        return responseData;
    }

    /**
     * 停用优惠券
     * @param httpServletRequest
     * @param datas
     * @return
     */
    @RequestMapping(value = "/endUsing", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData endUsing(HttpServletRequest httpServletRequest, @RequestBody List<Map<String, Object>> datas) {


        try {
            String url = "/h/sale/coupon/endUsing";
            HttpEntity<List<Map<String, Object>>> entity = new HttpEntity<>(datas, null);
            ResponseData responseData = restTemplate.exchange(baseUri + modelUri + url, HttpMethod.POST, entity, ResponseData.class).getBody();
            return responseData;
        } catch (Exception e) {
            System.err.println(e);
        }
        ResponseData responseData = new ResponseData();

        responseData.setSuccess(false);
        responseData.setMsg("读取数据错误");
        return responseData;
    }

    /**
     * 查询优惠券详细信息
     * @param id
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData detail(@RequestParam("id") String id, HttpServletRequest httpServletRequest) {
        try {
            String url = "/h/sale/coupon/detail?id=" + id;
            ResponseData responseData = restTemplate.exchange(baseUri + modelUri + url, HttpMethod.GET, null, ResponseData.class).getBody();
            return responseData;
        } catch (Exception e) {
            System.err.println(e);
        }

        ResponseData responseData = new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("获取数据失败");
        return responseData;
    }

    /**
     * 删除已失效优惠券
     * @param maps
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData delete(@RequestBody List<Map<String, Object>> maps, HttpServletRequest httpServletRequest) {
        try {
            String url = "/h/sale/coupon/delete";
            HttpEntity<List<Map<String, Object>>> entity = new HttpEntity<>(maps, null);
            ResponseData responseData = restTemplate.exchange(baseUri + modelUri + url, HttpMethod.POST, entity, ResponseData.class).getBody();
            return responseData;
        } catch (Exception e) {
            System.err.println(e);
        }
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("删除数据失败");
        return responseData;
    }

    /**
     * 管理员发放优惠券
     * @param map
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/convert/admin", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData convertByAdmin(@RequestBody Map<String, Object> map, HttpServletRequest httpServletRequest) {
        try {
            String url = "/h/sale/coupon/convert/admin";
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, null);
            ResponseData responseData = restTemplate.exchange(baseUri + modelUri + url, HttpMethod.POST, entity, ResponseData.class).getBody();
            return responseData;
        } catch (Exception e) {
            System.err.println(e);
        }
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("发放失败");
        return responseData;
    }

    /**
     * 导出优惠券分发模板
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/downloadExcelTemplate", method = RequestMethod.GET)
    public void importExcel(HttpServletRequest request, HttpServletResponse response) {
        List<CouponUserImportDto> couponUserImportDtos = new ArrayList<CouponUserImportDto>();
        couponUserImportDtos.add(new CouponUserImportDto());
        new ExcelUtil(CouponUserImportDto.class)
                .exportExcel(couponUserImportDtos, CouponUserImportDto.DEFAULT_SHEET_NAME, 0,
                        request, response, CouponUserImportDto.DEFAULT_EXCEL_FILE_NAME);
    }

    /**
     * 优惠券分发会员的导入
     *
     * @param request
     * @param files
     * @return ResponseData
     * @throws IOException
     * @throws InvalidFormatException
     */
    @RequestMapping(value = {"/couponUserImport"}, method = {RequestMethod.POST})
    @ResponseBody
    public ResponseData upload(HttpServletRequest request, MultipartFile files) throws IOException, InvalidFormatException {
        List<MstUser> mstUsers = new ArrayList<MstUser>();
        List<CouponUserImportDto> asTmrefundList = new ArrayList<CouponUserImportDto>();
        boolean importResult = false;
        String message = "success"; // 方法执行结果消息
        IRequest iRequest = this.createRequestContext(request);
        CommonsMultipartResolver multipartResolver
                = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (multipartResolver.isMultipart(request)
                && (request instanceof MultipartHttpServletRequest)) {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            Iterator<String> fileNames = multipartRequest.getFileNames();
            if (fileNames.hasNext()) {
                // 获得上传文件
                MultipartFile file = multipartRequest.getFile(fileNames.next());
                // 文件输入流
                InputStream is = null;
                try {
                    is = file.getInputStream();
                    // 调用工具类解析上传的Excel文件
                    List<CouponUserImportDto> infos
                            = new ExcelUtil(CouponUserImportDto.class)
                            .importExcel(CouponUserImportDto.DEFAULT_EXCEL_FILE_NAME,
                                    CouponUserImportDto.DEFAULT_SHEET_NAME + "0",
                                    is);
                    if (infos.size() == 0) {
                        message = "请下载Excel模板并输入数据";
                    }
                    for (int i = 0; i < infos.size(); i++) {
                        //非空校验
                        if (StringUtils.isEmpty(infos.get(i).getCustomerId())) {
                            throw new Exception("第" + (i + 1) + "行数据的会员手机号列不能为空");
                        }
                        if (infos.get(i).getCount() == null) {
                            throw new Exception("第" + (i + 1) + "行数据的数量列不能为空");
                        }
                        //校验手机号在USER表中是否存在,如果不存在报错
                        MstUser mstUser = userService.selectMsgByCustomerId(infos.get(i).getCustomerId());
                        if (mstUser == null) {
                            throw new Exception("第" + (i + 1) + "行数据的会员手机号不存在");
                        }
                        MstUser user = userService.selectMsgByCustomerId(infos.get(i).getCustomerId());
                        user.setCount(infos.get(i).getCount());
                        mstUsers.add(user);
                    }
                    importResult = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    message = "解析excel文件报错<br/>" + e.getMessage();
                } finally {
                    if (is != null) {
                        try {
                            // 关闭文件输入流
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        ResponseData responseData = new ResponseData(importResult);
        responseData.setMsg(message);
        responseData.setResp(mstUsers);
        return responseData;
    }

    /**
     * 下载所有券码的excel表格
     *
     * @param request
     * @param couponCode
     * @return
     */
    @RequestMapping(value = "/downloadCouponCodeExcel", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData downloadCouponCodeExcel(HttpServletRequest request,@RequestParam(value = "couponCode") String couponCode){

        try {
            String url= "hmall-promote-server/promotion/coupon/getRedeemCodes";
            Map map = new HashMap();
            map.put("couponCode",couponCode);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, null);
            ResponseData responseData = restTemplate.exchange(baseUri  + url, HttpMethod.POST, entity, ResponseData.class).getBody();
            List respList = responseData.getResp();
            if(CollectionUtils.isEmpty(respList)){
                return new ResponseData(false,"不存在可用兑换码");
            }else {

                List codes = (List)respList.stream().map(resp->{
                    Map respMap = (Map)resp;
                    return respMap.get("code");
                }).collect(Collectors.toList());
                responseData.setResp(codes);
            }

            return responseData;
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("下载兑换码失败");
        return responseData;
    }

    /**
     * 下载所有券码的excel表格
     *
     * @param request
     * @param couponCode
     * @return
     */
    @RequestMapping(value = "/checkSendRedeemNum", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData checkSendRedeemNum(HttpServletRequest request,@RequestParam(value = "couponCode") String couponCode, @RequestParam(value = "redeemNum") int redeemNum){

        try {
            String url= "hmall-promote-server/promotion/coupon/generateRedeemCode";
            Map map = new HashMap();
            map.put("couponCode",couponCode);
            map.put("num",redeemNum);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, null);
            ResponseData responseData = restTemplate.exchange(baseUri  + url, HttpMethod.POST, entity, ResponseData.class).getBody();
            return responseData;
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("生成失败");
        return responseData;
    }

}
