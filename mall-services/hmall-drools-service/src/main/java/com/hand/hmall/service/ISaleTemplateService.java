package com.hand.hmall.service;

import com.hand.hmall.dto.ResponseData;

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
     * @param maps
     * @return
     */
    ResponseData delete(List<Map<String, Object>> maps);

    /**
     * 根据id查询促销模板详细内容
     *
     * @param id
     * @return
     */
    Map<String,Object> selectTemplateDetail(String id);

    /**
     * 校验促销模板
     *
     * @param map
     * @return
     */
    List<String> checkedTemplate(Map<String, Object> map);
}
