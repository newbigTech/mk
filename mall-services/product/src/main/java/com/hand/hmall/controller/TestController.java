package com.hand.hmall.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 马君
 * @version 0.1
 * @name TestController
 * @description 做一些关于服务器的测试
 * @date 2017/7/31 13:48
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public Map<String, Object> test() {
        File file0 = new File(File.separator + "home" + File.separator);
        Map<String, Object> map = new HashMap<>();
        map.put("success", "true");
        map.put("exists", file0.exists());
        map.put("absPath", file0.getAbsoluteFile());
        return map;
    }
}
