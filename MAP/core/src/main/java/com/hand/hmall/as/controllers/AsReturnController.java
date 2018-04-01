package com.hand.hmall.as.controllers;

import com.alibaba.fastjson.JSON;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hmall.as.dto.AsReturn;
import com.hand.hmall.as.dto.AsReturnEntry;
import com.hand.hmall.as.mapper.AsReturnEntryMapper;
import com.hand.hmall.as.service.IAsReturnEntryService;
import com.hand.hmall.as.service.IAsReturnService;
import com.hand.hmall.common.service.ISequenceGenerateService;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.om.dto.OrderEntry;
import com.hand.hmall.om.dto.OrderPojo;
import com.hand.hmall.om.service.IOrderEntryService;
import com.hand.hmall.util.Constants;
import com.markor.map.framework.restclient.RestClient;
import net.sf.json.JSONObject;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author zhangmeng
 * @version 0.1
 * @name AsReturnController
 * @description 退货单
 * @date 2017/8/30
 */
@Controller
public class AsReturnController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IAsReturnService asReturnService;

    @Autowired
    private IAsReturnEntryService asReturnEntryService;

    @Autowired
    private ISequenceGenerateService sequenceGenerateService;

    @Autowired
    private AsReturnEntryMapper asReturnEntryMapper;

    @Autowired
    private IOrderEntryService orderEntryService;


    @Autowired
    private RestClient restClient;

    @RequestMapping(value = "/hmall/as/return/selectReturnById")
    @ResponseBody
    public ResponseData query(AsReturn dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(asReturnService.selectReturnById(dto));
    }


    /**
     * 根据订单ID查询用户信息
     *
     * @param asReturn
     * @return
     */
    @RequestMapping(value = "/hmall/as/return/selectUserInfoByOrderId")
    @ResponseBody
    public ResponseData selectUserInfoByOrderId(AsReturn asReturn) {
        return new ResponseData(asReturnService.selectUserInfoByOrderId(asReturn));
    }


    /**
     * 保存退货单信息 退货单行信息
     *
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hmall/as/return/saveReturn")
    @ResponseBody
    public ResponseData saveReturn(HttpServletRequest request, @RequestBody List<AsReturn> dto, Long serviceOrderId) {
        IRequest iRequest = this.createRequestContext(request);
        return asReturnService.saveReturn(iRequest, dto, serviceOrderId);
    }

    /**
     * @param dto
     * @param flag
     * @return
     * @description 获取用户可用促销（flag='N'） 或者 根据所选优惠计算订单金额（flag='Y'）
     */
    @RequestMapping(value = "/hmall/as/return/querySaleActivityOptions")
    @ResponseBody
    public ResponseData querySaleActivityOptions(@RequestBody List<OrderEntry> dto, @RequestParam("flag") String flag) {
        ResponseData responseData = new ResponseData();
        //组装调用促销接口需要的数据
        OrderPojo orderPojo = asReturnService.selectOrderEntryByPromote(dto, flag);

        //判断所有订单行是否全部退货
        if (orderPojo.getOrderEntryList().size() == 0) {
            responseData.setSuccess(true);
            return responseData;
        } else {
            try {
                //调用的相对地址
                String url = "/hmall-drools-service/sale/execution/promote";
                //请求参数
                String jsonString = JSON.toJSONString(orderPojo);
                Response response;
                response = restClient.postString(Constants.HMALL, url, jsonString, "application/json", null, null);
                if (response.code() == 200) {
                    JSONObject respObject = RestClient.responseToJSON(response);
                    if (respObject.getBoolean("success")) {
                        responseData.setResp((List<?>) respObject.get("resp"));
                    } else {
                        responseData.setSuccess(false);
                    }
                } else {
                    responseData.setSuccess(false);
                }
            } catch (Exception e) {
                logger.error(this.getClass().getName(), e);
                responseData.setSuccess(false);
            }
        }

        if (!responseData.isSuccess()) {
            if ("N".equals(flag)) {
                responseData.setMsg("获取用户可用促销失败！");
            } else {
                responseData.setMsg("优惠计算订单金额失败！");
            }
        }
        return responseData;
    }

    /**
     * 删除服务单行
     *
     * @return
     */
    @RequestMapping(value = "/hmall/as/return/deleteReturnEntry")
    @ResponseBody
    public String deleteReturnEntry(@RequestBody List<AsReturnEntry> dto) {
        if (dto.size() > 0) {
            for (int i = 0; i < dto.size(); i++) {
                if (dto.get(i).getServiceOrderId() != null) {
                    asReturnEntryService.deleteByPrimaryKey(dto.get(i));
                }
            }
            return "success";
        }
        return "fail";
    }

    @ResponseBody
    @RequestMapping(value = "/hmall/as/return/syncRetail")
    public ResponseData sendToRetail(@Param(value = "asReturnId") Long asReturnId, HttpServletRequest httpServletRequest) {
        IRequest iRequest = createRequestContext(httpServletRequest);
        return asReturnService.sendToRetail(asReturnId, iRequest);
    }

    /**
     * 根据退货单ID查询订单和服务单信息
     *
     * @param dto
     * @return ResponseData
     */
    @RequestMapping(value = "/hmall/as/returnOrder/selectOrderAndServiceOrderInfoByReturnId")
    @ResponseBody
    public ResponseData selectOrderAndServiceOrderInfoByReturnId(AsReturn dto) {
        List<AsReturn> returns = asReturnService.selectOrderAndServiceOrderInfoByReturnId(dto);
        return new ResponseData(asReturnService.selectOrderAndServiceOrderInfoByReturnId(dto));
    }


    /**
     * @param request
     * @param dto
     * @return
     * @description 换转退逻辑
     */
    @RequestMapping(value = "/hmall/as/return/changeToReturn")
    @ResponseBody
    public ResponseData changeToReturn(HttpServletRequest request, @RequestBody AsReturn dto) {
        IRequest iRequest = this.createRequestContext(request);
        ResponseData responseData = new ResponseData();
        try {
            asReturnService.changeToReturn(iRequest, dto);
        } catch (Exception e) {
            logger.info(e.getMessage());
            responseData.setMsg(e.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    /**
     * @param request
     * @param dto
     * @return
     * @description 取消换货逻辑
     */
    @RequestMapping(value = "/hmall/as/return/cancelReturnGood")
    @ResponseBody
    public ResponseData cancelReturnGood(HttpServletRequest request, @RequestBody AsReturn dto) {
        IRequest iRequest = this.createRequestContext(request);
        ResponseData responseData = new ResponseData();
        try {
            asReturnService.cancelReturnGood(iRequest, dto);
        } catch (Exception e) {
            logger.info(e.getMessage());
            responseData.setMsg(e.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    /**
     * 换转退后续操作，更新促销信息，订单行数量，重新计算建议退款金额
     *
     * @param request
     * @param dto
     * @param currentAmount
     * @param chosenCoupon
     * @param chosenPromotion
     * @return
     */
    @RequestMapping(value = "/hmall/as/return/changeToReturnDetail")
    @ResponseBody
    public ResponseData changeToReturnDetail(HttpServletRequest request, @RequestBody List<OrderEntry> dto, Double currentAmount, String chosenCoupon, String chosenPromotion) {
        IRequest iRequest = this.createRequestContext(request);
        ResponseData responseData = new ResponseData();
        try {
            responseData = asReturnService.changeToReturnDetail(iRequest, dto, currentAmount, chosenCoupon, chosenPromotion);
        } catch (Exception e) {
            logger.info(e.getMessage());
            responseData.setMsg(e.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    /**
     * 根据服务单ID，查找关联的（唯一）退货单实例并返回
     *
     * @param serviceOrderId 服务单ID
     * @return
     */
    @RequestMapping(value = "/hmall/as/return/queryReturnByServiceOrderId")
    @ResponseBody
    public ResponseData queryReturnByServiceOrderId(@RequestParam("serviceOrderId") Long serviceOrderId) {
        AsReturn asReturn = asReturnService.queryReturnByServiceOrderId(serviceOrderId);
        ResponseData result = new ResponseData(true);
        result.setResp(Arrays.asList(asReturn));
        return result;
    }

}