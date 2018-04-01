package com.hand.hmall.service;

import com.hand.hmall.dto.ResponseData;

import java.util.List;
import java.util.Map;

/**
 * Created by shanks on 2017/1/5.
 */
public interface ISaleActivityService {
    ResponseData query(Map<String, Object> map);

    ResponseData submit(Map<String, Object> map);

    ResponseData delete(List<Map<String, Object>> maps);

    void deleteReal(List<Map<String, Object>> maps);

    ResponseData activity(Map<String, Object> map);

    ResponseData inactive(Map<String, Object> map);

    Map<String, Object>  selectActivityDetail(String id);

    List<String> checkedActivity(Map<String, Object> map);

    List<Map<String,?>> selectByStatusAndIsUsing(String status, String isUsing);

    List<Map<String,?>> selectByStatusAndIsUsing(List status, String isUsing);

    List<Map<String,?>> selectByStatusAndIsUsingAndGroups(String status, String isUsing, String type);

    ResponseData submitActivity(Map<String, Object> map);

    void dealForBundle(Map map);

    Map<String, ?> getActivityDetail(Map activity);


    Map<String, ?> getSynToZmallActivity(Map<String, ?> activity);


}
