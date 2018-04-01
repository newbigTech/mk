package com.hand.promotion.dubboService;

import com.alibaba.fastjson.JSON;
import com.hand.dto.ResponseData;
import com.hand.hpromotion.ISaleTemplateService;
import com.hand.promotion.pojo.activity.SaleTemplatePojo;
import com.hand.promotion.util.ResponseReturnUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by darkdog on 2018/2/2.
 * 促销模板接口
 */
public class SaleTemplateService implements ISaleTemplateService {

    @Autowired
    private com.hand.promotion.service.ISaleTemplateService saleTemplateService;
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    /*
    *  查询模板多条信息
     */
    @Override
    public ResponseData query(Map<String, Object> map) {
        logger.info("-----------query-----------\n{}", JSON.toJSONString(map));
        return saleTemplateService.query(map);
    }

    /**
     * 新建模板
     *
     * @param data
     * @return
     */
    @Override
    public ResponseData submit(Map<String, Object> data) {
        logger.info("-----------submit-----------\n{}", JSON.toJSONString(data));

        return saleTemplateService.submit(data);
    }

    /**
     * 删除促销活动模板
     *
     * @param datas
     * @return
     */
    @Override
    public ResponseData delete(List<Map<String, Object>> datas) {
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
    @Override
    public ResponseData detail(String id) {
        logger.info("-----------dtail-----------\n{}", JSON.toJSONString(id));
        SaleTemplatePojo saleTemplatePojo = saleTemplateService.selectTemplateDetail(id);
        return ResponseReturnUtil.returnTrueResp(Arrays.asList(saleTemplatePojo));
    }
}
