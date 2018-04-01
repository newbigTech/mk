package com.hand.hmall.service;

import com.hand.hmall.dto.ResponseData;

import java.util.List;
import java.util.Map;

/**
 * Created by shanks on 2017/2/25.
 */
public interface ICheckedConditionActionService {

    /**
     * 获取促销结果中的优惠数据
     *
     * @param map
     * @param detail
     */
    void getDetailForAction(Map<String, Object> map, Map detail);

    /**
     * 获取促销条件的判断数据
     *
     * @param
     * @param detail
     */
    void getDetailForCondition(Map<String, Object> condotion, Map detail);

    /**
     * 获取套装促销对应的套装编码
     *
     * @param activityId
     * @return
     */
    String getBundleCode(String activityId);


}
