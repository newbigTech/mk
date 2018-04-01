package com.hand.hmall.service;

import com.hand.hmall.dto.ResponseData;

import java.util.List;
import java.util.Map;

/**
 * Created by cw on 2017/2/28.
 */
public interface ISaleDrawService {
    ResponseData query(Map<String, Object> map);
    ResponseData getAward(Map<String, Object> map);
    ResponseData submit(Map<String, Object> map);
    ResponseData delete(List<Map<String, Object>> maps);
    ResponseData active(List<Map<String, Object>> maps);
    ResponseData inactive(List<Map<String, Object>> maps);
    Map<String, Object> selectDrawDetail(String id);
    ResponseData awardPro(Map<String, Object> map);
    ResponseData submitAwardPro(List<Map<String, Object>> list);
    ResponseData startUsingAwardPro(Map<String, Object> map);
    ResponseData endUsingAwardPro(Map<String, Object> map);
    ResponseData getAwardpro(String drawId);
    ResponseData addAwardRecord(Map<String, Object> map);
}
