package com.hand.hap.cloud.hpay.controller;

import com.hand.hap.cloud.hpay.data.ReturnData;
import com.hand.hap.cloud.hpay.services.pcServices.alipay.IAlipayPayService;
import com.hand.hap.cloud.hpay.services.pcServices.union.service.IUnionPayService;
import com.hand.hap.cloud.hpay.services.pcServices.wechat.IPCWechatPayService;
import com.hand.hap.cloud.hpay.services.publicsign.wechat.IPublicSignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author jiaxin.jiang
 * @Title com.hand.hap.cloud.hpay.controller
 * @Description 第三方回调接口
 * @date 2017/7/11
 */
@RestController
@RequestMapping(value = "/v1/return")
public class ReturnController {
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    private IAlipayPayService alipayService;

    @Autowired
    private IUnionPayService unionpayPayService;

    @Autowired
    private IPCWechatPayService ipcWechatService;

    /**
     * alipay回调
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/alipayReturn", method = RequestMethod.POST)
    public String alipayReturn(HttpServletRequest request, HttpServletResponse response) {
        return alipayService.alipayNotify(request, response);
    }

    /**
     * union回调
     *
     * @param request  request
     * @param response response
     * @throws Exception exception
     */
    @RequestMapping(value = "/unionpayReturn", method = RequestMethod.POST)
    public void unionpayReturn(HttpServletRequest request, HttpServletResponse response) {
        unionpayPayService.unionNotify(request, response);
    }

    /**
     * wechat回调
     *
     * @param request  request
     * @param response response
     * @throws Exception exception
     */
    @RequestMapping(value = "/wechatpayReturn", method = RequestMethod.POST)
    public void wechatpayReturn(HttpServletRequest request, HttpServletResponse response) {
        try {
            ipcWechatService.weChatNotify(request, response);
        } catch (Exception e) {
            logger.error("微信回调失败", e);
        }
    }
}
