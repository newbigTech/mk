package com.hand.promotion.service;


import com.hand.dto.ResponseData;
import com.hand.promotion.pojo.activity.SaleTemplatePojo;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * Created by shanks on 2017/1/4.
 */
public interface ISaleTemplateService {
    /**
     * 查询促销模板
     *
     * @param map
     * @return
     */
    ResponseData query(Map<String, Object> map);

    /**
     * 创建促销模板Service
     *
     * @param map
     * @return
     */
    ResponseData submit(Map<String, Object> map);

    /**
     * 删除促销模板
     *
     * @param ids 要删除的模板集合
     * @return
     */
    ResponseData delete(List<String> ids) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;

    /**
     * 根据id查询促销模板详细内容
     *
     * @param id
     * @return
     */
    SaleTemplatePojo selectTemplateDetail(String id);

    /**
     * 校验促销模板
     *
     * @param map
     * @return
     */
    List<String> checkedTemplate(Map<String, Object> map);
}
