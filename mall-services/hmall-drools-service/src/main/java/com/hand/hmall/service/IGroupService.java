package com.hand.hmall.service;

import com.hand.hmall.dto.ResponseData;

import java.util.List;
import java.util.Map;

/**
 * Created by shanks on 2017/2/17.
 */
public interface IGroupService {
    public ResponseData addNewGroup(List<Map<String, Object>> map);
    public ResponseData groupDelete(List<Map<String, Object>> map);
    public ResponseData selectAllGroup(String type);
    public ResponseData queryByConditions(Map<String, Object> map);
}
