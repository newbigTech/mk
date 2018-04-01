package com.hand.hmall.pin.controllers;

import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.pin.service.IPinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.Map;

/**
 * @author chenzhigang
 * @version 0.1
 * @name PinController
 * @description PIN码信息Controller
 * @date 2017/8/4
 */
@Controller
public class PinController {

    @Autowired
    private IPinService pinService;

    /**
     * 查询PIN码列表
     *
     * @param code - PIN码
     * @return
     */
    @RequestMapping(value = "/hmall/pin/queryByCode")
    @ResponseBody
    public ResponseData queryByCode(@RequestParam("code") String code) {
        return new ResponseData(pinService.queryByCode(code));
    }

    /**
     * 触发推送PIN码至ZMall事件
     *
     * @return
     */
    @GetMapping("/hmall/pin/sendPin2ZMall")
    @ResponseBody
    public ResponseData sendPin2ZMall() {
        Map result = pinService.sendPin2ZMall();
        ResponseData response;
        try {
            response = new ResponseData(Arrays.asList(result));
        } catch (RuntimeException e) {
            response = new ResponseData(false);
            response.setMessage(e.getMessage());
        }

        return response;
    }
}
