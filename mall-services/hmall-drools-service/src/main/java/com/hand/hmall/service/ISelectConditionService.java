package com.hand.hmall.service;

import com.hand.hmall.dto.ResponseData;

import java.util.Map;

/**
 * Created by shanks on 2017/3/3.
 */
public interface ISelectConditionService {
    public ResponseData selectByCode(Map<String, Object> map);
}
