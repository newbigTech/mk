package com.hand.hmall.om.controllers;

import com.alibaba.fastjson.JSON;
import com.hand.hmall.log.dto.LogManager;
import com.hand.hmall.log.service.ILogManagerService;
import com.hand.hmall.om.dto.Order;
import com.hand.hmall.om.service.IOrderService;
import com.hand.hmall.util.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.om.dto.OmPromoRecord;
import com.hand.hmall.om.service.IOmPromoRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author yuxiaoli
 * @version 0.1
 * @name OmPromoRecordController
 * @description 事后促销记录Controller
 * @date 2017/10/13
 */
@Controller
public class OmPromoRecordController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private IOmPromoRecordService service;

    @Autowired
    private ILogManagerService logManagerService;

    @Autowired
    private IOrderService orderService;

    @Value("#{configProperties['baseUri']}")
    private String baseUri;

    @RequestMapping(value = "/hmall/om/promo/record/query")
    @ResponseBody
    public ResponseData query(OmPromoRecord dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/om/promo/record/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<OmPromoRecord> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hmall/om/promo/record/update")
    @ResponseBody
    public ResponseData update(@RequestBody List<OmPromoRecord> dto, HttpServletRequest request) {
        IRequest requestCtx = createRequestContext(request);
        ResponseData responseData = new ResponseData();
        if(CollectionUtils.isNotEmpty(dto)){
            for(OmPromoRecord omPromoRecord : dto){
                try{
                    service.updateByPrimaryKeySelective(requestCtx,omPromoRecord);
                }catch (Exception e){
                    responseData.setMessage(e.getMessage());
                    responseData.setSuccess(false);
                }
            }
        }
        return responseData;
    }

    @RequestMapping(value = "/hmall/om/promo/record/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<OmPromoRecord> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    /**
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     * @description 赠品发放详情界面，根据传进来的参数俩判断是符合条件用户还是候补用户
     */
    @RequestMapping(value = "/hmall/om/promo/record/queryByCondition")
    @ResponseBody
    public ResponseData queryByCondition(OmPromoRecord dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                         @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectPromoRecord(requestContext, dto, page, pageSize));
    }

    /**
     * @param request
     * @param dtoList
     * @return
     * @description 调用“事后促销资格重新判定”微服务
     */
    @RequestMapping(value = "/hmall/om/promo/record/checkAfterPromoteWithCofrim")
    @ResponseBody
    public ResponseData checkAfterPromoteWithCofrim(HttpServletRequest request, @RequestBody List<OmPromoRecord> dtoList) {
        IRequest requestContext = createRequestContext(request);
        ResponseData responseDataResult = new ResponseData();
        if (CollectionUtils.isNotEmpty(dtoList)) {
            for (OmPromoRecord omPromoRecord : dtoList) {
                //校验数据
                if(omPromoRecord.getOrderId()!=null){
                    Order o = new Order();
                    o.setOrderId(omPromoRecord.getOrderId());
                    Order order = orderService.selectByPrimaryKey(requestContext,o);
                    if(!"TRADE_FINISHED".equals(order.getOrderStatus())){
                        responseDataResult.setSuccess(false);
                        responseDataResult.setMessage("用户"+omPromoRecord.getCustomerid()+",订单号"+omPromoRecord.getCode()+":订单状态不是交易成功状态!");
                        return responseDataResult;
                    }
                }
                if(omPromoRecord.getStatus() != null && !"NEW".equals(omPromoRecord.getStatus()) && !"WAIT_NEW".equals(omPromoRecord.getStatus())){
                    responseDataResult.setSuccess(false);
                    responseDataResult.setMessage("用户"+omPromoRecord.getCustomerid()+",订单号"+omPromoRecord.getCode()+":促销状态不满足发放条件!");
                    return responseDataResult;
                }
            }
        }

        String resultMessage = "";
        if (CollectionUtils.isNotEmpty(dtoList)) {
            for (OmPromoRecord omPromoRecord : dtoList) {
                Map<String, Object> map = new HashMap<>();
                try {
                    map = service.changeToParam(requestContext, omPromoRecord);
                    logger.info("=======事后促销资格重新判定服务请求参数："+ JSON.toJSONString(map));
                    LogManager logManager = new LogManager("checkAfterPromoteWithCofrim","调用事后促销资格重新判定微服务","HMALL");
                    logManager.setMessage(JSON.toJSONString(map));
                    logManagerService.logBegin(requestContext,logManager);
                    String url = "hmall-drools-service/afterPromote/operate/checkAfterPromoteWithCofrim";
                    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, null);
                    com.hand.hmall.dto.ResponseData responseData = restTemplate.exchange(baseUri + url, HttpMethod.POST, entity, com.hand.hmall.dto.ResponseData.class).getBody();
                    if(responseData != null){
                        if(responseData.isSuccess()){
                            if(responseData.getResp() != null && CollectionUtils.isNotEmpty(responseData.getResp())){
                                String userStatus = (String) ((Map<String,Object>) responseData.getResp().get(0)).get("userStatus");
                                if(userStatus != null && (Constants.OM_PROMO_RECODE_STATUS_CANCEL.equals(userStatus) || Constants.OM_PROMO_RECODE_STATUS_WAIT_CANCEL.equals(userStatus))){
                                    resultMessage = resultMessage + "用户"+omPromoRecord.getCustomerid()+",订单号"+omPromoRecord.getCode()+":资格已被取消!\n";
                                }else if(userStatus != null && (Constants.OM_PROMO_RECODE_STATUS_FINISH.equals(userStatus) || Constants.OM_PROMO_RECODE_STATUS_WAIT_FINI.equals(userStatus))){
                                    resultMessage = resultMessage + "用户"+omPromoRecord.getCustomerid()+",订单号"+omPromoRecord.getCode()+":优惠券已经领取!\n";
                                }else if(userStatus != null && (Constants.OM_PROMO_RECODE_STATUS_NEW.equals(userStatus) || Constants.OM_PROMO_RECODE_STATUS_WAIT_NEW.equals(userStatus))){
                                    //有资格
                                    if(omPromoRecord.getCouponId() != null){
                                        //调用领取优惠券微服务
                                        Map<String, Object> param = new HashMap<>();
                                        param.put("customerId", omPromoRecord.getCustomerid());
                                        param.put("couponCode", omPromoRecord.getCouponId());
                                        String couponUrl = "/hmall-promote-server/promotion/coupon/convert";
                                        logger.info("===========领取优惠券请求参数："+JSON.toJSONString(param));
                                        HttpEntity<Map<String, Object>> object = new HttpEntity<>(param, null);
                                        com.hand.hmall.dto.ResponseData couponResponseData = restTemplate.exchange(baseUri + couponUrl, HttpMethod.POST, object, com.hand.hmall.dto.ResponseData.class).getBody();
                                        if(couponResponseData!= null){
                                            if(! couponResponseData.isSuccess()){
                                                resultMessage = resultMessage + "用户"+omPromoRecord.getCustomerid()+",订单号"+omPromoRecord.getCode()+":"+couponResponseData.getMsg()+"\n";
                                                continue;
                                            }
                                        }
                                    }
                                    if(omPromoRecord.getStatus()!= null && Constants.OM_PROMO_RECODE_STATUS_NEW.equals(omPromoRecord.getStatus())){
                                        omPromoRecord.setStatus(Constants.OM_PROMO_RECODE_STATUS_FINISH);
                                    }else if(omPromoRecord.getStatus()!= null && Constants.OM_PROMO_RECODE_STATUS_WAIT_NEW.equals(omPromoRecord.getStatus())){
                                        omPromoRecord.setStatus(Constants.OM_PROMO_RECODE_STATUS_WAIT_FINI);
                                    }
                                    service.updateByPrimaryKeySelective(requestContext,omPromoRecord);
                                }
                            }else{
                                resultMessage = resultMessage + "用户"+omPromoRecord.getCustomerid()+",订单号"+omPromoRecord.getCode()+":"+"没有资格!\n";
                            }
                        }else{
                            resultMessage = resultMessage + "用户"+omPromoRecord.getCustomerid()+",订单号"+omPromoRecord.getCode()+":"+responseData.getMsg()+"\n";
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    resultMessage = resultMessage + "用户"+omPromoRecord.getCustomerid()+",订单号"+omPromoRecord.getCode()+":"+e.getMessage()+"\n";
                }
            }
        }
        if(resultMessage != ""){
            responseDataResult.setSuccess(false);
            responseDataResult.setMessage(resultMessage);
        }
        return responseDataResult;
    }

    @RequestMapping(value = "/hmall/om/promo/record/selectFinishCount")
    @ResponseBody
    public ResponseData quselectFinishCountery(OmPromoRecord dto,HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectFinishCount(requestContext, dto));
    }

}