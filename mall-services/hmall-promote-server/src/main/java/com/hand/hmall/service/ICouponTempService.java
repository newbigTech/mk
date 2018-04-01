package com.hand.hmall.service;

import com.hand.hmall.dto.ResponseData;

import java.util.List;
import java.util.Map;

/**
 * Created by shanks on 2017/3/20.
 */
public interface ICouponTempService {
    ResponseData addCouponTemp(List<Map<String, Object>> map);
}
