package com.hand.hmall.service;


import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.temp.ActionTemp;
import com.hand.hmall.temp.Field;
import com.hand.hmall.temp.ModelTemp;
import com.hand.hmall.temp.RuleInputTemp;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public interface IRuleTempService {
    Map addModel(ModelTemp model);
    Map addDefinition(Map<String, Object> definition);
    Map addAction(ActionTemp action);
    Map addGroup(Map<String, Object> group);
    Map insertField(Field field);
    ResponseData createRule(RuleInputTemp ruleInputTemp) throws NullPointerException,IllegalArgumentException,InvocationTargetException, IllegalAccessException,ClassCastException;
    ResponseData releaseCoupon(String couponId);
    void removeCoupon(String couponId);
    ResponseData releaseActivity(Map<String, Object> activityMap);
    void removeActivity(String id,String activityId);
}
