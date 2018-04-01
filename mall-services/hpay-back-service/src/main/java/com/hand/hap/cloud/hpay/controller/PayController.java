package com.hand.hap.cloud.hpay.controller;

import com.hand.hap.cloud.hpay.data.OrderData;
import com.hand.hap.cloud.hpay.data.ReturnData;
import com.hand.hap.cloud.hpay.services.pcServices.alipay.IAlipayPayService;
import com.hand.hap.cloud.hpay.services.pcServices.union.service.IUnionPayService;
import com.hand.hap.cloud.hpay.services.pcServices.wechat.IPCWechatPayService;
import com.hand.hap.cloud.hpay.services.phoneServices.alipay.IPhoneAlipayService;
import com.hand.hap.cloud.hpay.services.phoneServices.union.service.IPhoneUnionService;
import com.hand.hap.cloud.hpay.services.phoneServices.wechat.service.IPhoneWechatService;
import com.hand.hap.cloud.hpay.services.publicsign.wechat.IPublicSignService;
import com.hand.hap.cloud.hpay.utils.PropertiesUtil;
import com.hand.hap.cloud.hpay.validateData.ValidateOrderData;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jiaxin.jiang
 * @Title com.hand.hap.cloud.hpay.controller
 * @Description 支付控制
 * @date 2017/7/6
 */
@RestController
@RequestMapping(value = "/v1")
public class PayController {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    private IUnionPayService unionpayService;

    @Autowired
    private IAlipayPayService alipayPayService;

    @Autowired
    private IPCWechatPayService ipcWechatService;

    @Autowired
    private IPhoneAlipayService iPhoneAlipayService;

    @Autowired
    private IPhoneUnionService iPhoneUnionService;

    @Autowired
    private IPhoneWechatService iPhoneWechatService;

    @Autowired
    private IPublicSignService iPublicSignService;

    /**
     * 支付请求接口
     *
     * @param request   请求
     * @param resp      回应
     * @param orderData 订单信息
     * @return returnData returnData
     * @throws Exception Exception
     */
    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public ReturnData pay(HttpServletRequest request, HttpServletResponse resp, @RequestBody OrderData orderData) {
        logger.info(">>>>>>>>>>>>>>>>>>>>>>/pay<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList();
        ValidateOrderData validate = new ValidateOrderData();
        //调用数据验证
        ReturnData rd = validate.validateOrderData(orderData);

        //数据验证成功
        if (rd.isSuccess()) {
            if (PropertiesUtil.getPayInfoValue("mode.unionpay").equalsIgnoreCase(orderData.getMode())) {
                //mode-union,tradeType-pc
                if (PropertiesUtil.getPayInfoValue("mode.tradeType.pc").equalsIgnoreCase(orderData.getTradeType())) {
                    String submitForm = unionpayService.pcUnion(orderData);
                    map.put("submitForm", submitForm);
                    list.add(map);
                    rd.setMsg("成功请求银联支付");
                    rd.setSuccess(true);
                    rd.setResp(list);
                    return rd;
                }
                //mode-union,tradeType-h5
                if (PropertiesUtil.getPayInfoValue("mode.tradeType.h5").equalsIgnoreCase(orderData.getTradeType())) {
                    String submitForm = null;
                    try {
                        try {
                            submitForm = iPhoneUnionService.phoneH5Unionpay(request, resp, orderData);
                        } catch (IOException e) {
                            rd.setMsgCode(PropertiesUtil.getPayInfoValue("fail"));
                            rd.setMsg("请求银联H5失败");
                            rd.setSuccess(false);
                            logger.error("请求银联H5失败", e);
                            return rd;
                        }
                    } catch (ServletException e) {
                        e.printStackTrace();
                    }
                    map.put("submitForm", submitForm);
                    list.add(map);
                    rd.setMsg("成功请求银联支付");
                    rd.setSuccess(true);
                    rd.setResp(list);
                    return rd;
                }
            }
            if (PropertiesUtil.getPayInfoValue("mode.alipay").equalsIgnoreCase(orderData.getMode())) {
                //mode-alipay,tradeType-pc
                if (PropertiesUtil.getPayInfoValue("mode.tradeType.pc").equalsIgnoreCase(orderData.getTradeType())) {
                    try {
                        rd = alipayPayService.pcAlipay(orderData);
                    } catch (Exception e) {
                        rd.setMsgCode(PropertiesUtil.getPayInfoValue("fail"));
                        rd.setMsg("请求支付宝PC失败");
                        rd.setSuccess(false);
                        logger.error("请求支付宝PC失败", e);
                        return rd;
                    }
                    if (rd.isSuccess()) {
                        map.put("submitForm", rd.getMsg());
                        list.add(map);
                        rd.setMsgCode(PropertiesUtil.getPayInfoValue("success"));
                        rd.setMsg("成功请求支付宝支付 -data.orderData.qrPayModel" + orderData.getQrPayMode());
                        rd.setSuccess(true);
                        rd.setResp(list);
                        return rd;
                    }
                }
                //mode-alipay,tradeType-h5
                if (PropertiesUtil.getPayInfoValue("mode.tradeType.h5").equalsIgnoreCase(orderData.getTradeType())) {
                    try {
                        rd = iPhoneAlipayService.phoneH5Alipay(request, resp, orderData);
                    } catch (Exception e) {
                        rd.setMsgCode(PropertiesUtil.getPayInfoValue("fail"));
                        rd.setMsg("请求支付宝h5失败");
                        rd.setSuccess(false);
                        logger.error("请求支付宝h5失败", e);
                        return rd;
                    }
                    if (rd.isSuccess()) {
                        map.put("submitForm", rd.getMsg());
                        list.add(map);
                        rd.setMsgCode(PropertiesUtil.getPayInfoValue("success"));
                        rd.setMsg("成功请求支付宝支付");
                        rd.setSuccess(true);
                        rd.setResp(list);
                        return rd;
                    }
                }
            }
            if (PropertiesUtil.getPayInfoValue("mode.wechatpay").equalsIgnoreCase(orderData.getMode())) {
                //mode-wechat,tradeType-NATIVE
                if (PropertiesUtil.getPayInfoValue("mode.tradeType.native").equals(orderData.getTradeType())) {
                    try {
                        rd = ipcWechatService.pcWechat(request, resp, orderData);
                    } catch (Exception e) {
                        rd.setMsgCode(PropertiesUtil.getPayInfoValue("fail"));
                        rd.setMsg("请求微信NATIVE失败");
                        rd.setSuccess(false);
                        logger.error("请求微信NATIVE失败", e);
                        return rd;
                    }
                    if (rd.isSuccess()) {
                        map.put("url", rd.getMsg());
                        list.add(map);
                        rd.setMsg("成功请求微信支付");
                        rd.setResp(list);
                        return rd;
                    }
                }
                //mode-wechat,tradeType-MWEB
                if (PropertiesUtil.getPayInfoValue("mode.tradeType.mweb").equals(orderData.getTradeType())) {
                    try {
                        rd = iPhoneWechatService.phoneH5Wechat(request, resp, orderData);
                    } catch (Exception e) {
                        rd.setMsgCode(PropertiesUtil.getPayInfoValue("fail"));
                        rd.setMsg("请求微信MWEB失败");
                        rd.setSuccess(false);
                        logger.error("请求微信MWEB失败", e);
                        return rd;
                    }
                    if (rd.isSuccess()) {
                        map.put("url", rd.getMsg());
                        list.add(map);
                        rd.setMsgCode(PropertiesUtil.getPayInfoValue("success"));
                        rd.setMsg("成功请求微信支付");
                        rd.setSuccess(true);
                        rd.setResp(list);
                        return rd;
                    }
                }
                //mode-wechat,tradeType-JSAPI
                if (PropertiesUtil.getPayInfoValue("mode.tradeType.JSAPI").equals(orderData.getTradeType())) {
                    try {
                        rd = iPublicSignService.publicSign(request, resp, orderData);
                    } catch (Exception e) {
                        rd.setMsgCode(PropertiesUtil.getPayInfoValue("fail"));
                        rd.setMsg("请求微信JSAPI失败");
                        rd.setSuccess(false);
                        logger.error("请求微信JSAPI失败", e);
                        return rd;
                    }
                    if (rd.isSuccess()) {
                        return rd;
                    }
                }
            }
        }
        return rd;
    }

    /**
     * 获取OpenId接口
     * 根据zmall传送过来的Code,调用微信接口获取access_token、openId等信息,并将openId等信息传给Zmall
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getOpenIdByCode", method = RequestMethod.POST)
    public ReturnData getOpenIdByCode(@RequestBody Map<String, String> code) {
        if (StringUtils.isEmpty(code.get("code"))) {
            return new ReturnData(false, "code为空");
        } else {
            return iPublicSignService.getOpenIdByCode(code.get("code"));
        }
    }
}