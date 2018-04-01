package com.hand.promotion.service;


import com.hand.promotion.pojo.activity.SelectConditionActionPojo;

import java.util.List;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2017/12/20
 * @description 页面展示的条件结果 Service
 */
public interface ISelectConditionActionService {

    /**
     * 根据definitionId判断插入或更新数据
     *
     * @param pojo
     */
    void upsertByDfId(SelectConditionActionPojo pojo);

    /**
     * 根据code 、type 查询条件结果
     *
     * @param condition
     * @return
     */
    List<SelectConditionActionPojo> findByCondition(SelectConditionActionPojo condition);


    /**
     * 根据条件查询中台显示的条件结果 按照优先级从小到大排序
     *
     * @param code
     * @param type
     * @return
     */
    List<SelectConditionActionPojo> selectShowPojo(String code, String type);


}
