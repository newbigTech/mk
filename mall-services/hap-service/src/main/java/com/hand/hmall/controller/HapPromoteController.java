package com.hand.hmall.controller;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import com.hand.hmall.client.IPromoteClient;
import com.hand.hmall.dto.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author XinyangMei
 * @Title HapPromoteController
 * @Description desp
 * @date 2017/7/20 16:20
 */
@RestController
@RequestMapping(value = "/h/promote", produces = { MediaType.APPLICATION_JSON_VALUE })
public class HapPromoteController {
    @Autowired
    private IPromoteClient iPromoteClient;
    @RequestMapping(value = "/queryByCidAndUserIds" , method = RequestMethod.POST)
    public ResponseData query(HttpServletRequest httpServletRequest, @RequestBody Map<String,Object> map){
        return iPromoteClient.queryByCidAndUserIds(map);
    }


}
