package com.hand.promotion.controller;

import com.alibaba.fastjson.JSON;

import com.hand.dto.ResponseData;
import com.hand.promotion.pojo.activity.SaleTemplatePojo;
import com.hand.promotion.service.ISaleTemplateService;
import com.hand.promotion.util.ResponseReturnUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 促销/优惠券 模板
 * <p>
 * Created by shanks on 2017/1/4.
 */
@RestController
@RequestMapping(value = "/h/sale/template", produces = {MediaType.APPLICATION_JSON_VALUE})
public class SaleTemplateController {
    @Autowired
    private ISaleTemplateService saleTemplateService;
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData query(@RequestBody Map<String, Object> map) {
        logger.info("-----------query-----------\n{}", JSON.toJSONString(map));
        return saleTemplateService.query(map);

    }

    /**
     * 新建模板
     *
     * @param data
     * @return
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData submit(@RequestBody Map<String, Object> data) {
        logger.info("-----------submit-----------\n{}", JSON.toJSONString(data));

        return saleTemplateService.submit(data);

    }

    /**
     * 删除促销活动模板
     *
     * @param datas
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData delete(@RequestBody List<Map<String, Object>> datas) {
        logger.info("-----------delete-----------\n{}", JSON.toJSONString(datas));
        try {
            List<String> templateId = datas.stream().map(data -> {
                return (String) data.get("templateId");
            }).collect(Collectors.toList());
            return saleTemplateService.delete(templateId);
        } catch (Exception e) {
            logger.error("删除模板异常", e);
            return ResponseReturnUtil.returnFalseResponse("删除异常", "DEL_TEMPLATE_ERR");
        }
    }


    /**
     * 查询模板详细信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData detail(@RequestParam("id") String id) {
        logger.info("-----------dtail-----------\n{}", JSON.toJSONString(id));
        SaleTemplatePojo saleTemplatePojo = saleTemplateService.selectTemplateDetail(id);
        return ResponseReturnUtil.returnTrueResp(Arrays.asList(saleTemplatePojo));
    }
}
