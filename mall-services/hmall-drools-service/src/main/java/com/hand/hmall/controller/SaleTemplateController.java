package com.hand.hmall.controller;

import com.alibaba.fastjson.JSON;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.service.ISaleTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 促销/优惠券 模板
 *
 * Created by shanks on 2017/1/4.
 */
@RestController
@RequestMapping(value = "/h/sale/template", produces = { MediaType.APPLICATION_JSON_VALUE })
public class SaleTemplateController {
    @Autowired
    private ISaleTemplateService saleTemplateService;
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    @RequestMapping(value = "/query" , method = RequestMethod.POST)
    @ResponseBody
    public ResponseData query(@RequestBody Map<String,Object> map)
    {
        logger.info("-----------query-----------\n{}", JSON.toJSONString(map));
        return saleTemplateService.query(map);

    }

    /**
     * 新建模板
     *
     * @param data
     * @return
     */
    @RequestMapping(value = "/submit" , method = RequestMethod.POST)
    @ResponseBody
    public ResponseData submit(@RequestBody Map<String,Object> data) {
        logger.info("-----------submit-----------\n{}", JSON.toJSONString(data));

        return saleTemplateService.submit(data);

    }

    /**
     * 删除促销活动模板
     *
     * @param datas
     * @return
     */
    @RequestMapping(value = "/delete" , method = RequestMethod.POST)
    @ResponseBody
    public ResponseData delete(@RequestBody List<Map<String,Object>> datas) {
        logger.info("-----------delete-----------\n{}", JSON.toJSONString(datas));

        return saleTemplateService.delete(datas);
    }


    /**
     * 查询模板详细信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/detail" , method = RequestMethod.GET)
    @ResponseBody
    public ResponseData detail(@RequestParam("id")String id) {
        logger.info("-----------dtail-----------\n{}", JSON.toJSONString(id));

        return  new ResponseData(saleTemplateService.selectTemplateDetail(id));
    }
}
