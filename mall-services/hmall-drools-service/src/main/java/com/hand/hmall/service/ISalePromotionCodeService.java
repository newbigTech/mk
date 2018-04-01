package com.hand.hmall.service;

import com.hand.hmall.dto.ResponseData;

import java.util.List;
import java.util.Map;

/**
 * Created by shanks on 2017/3/13.
 */
public interface ISalePromotionCodeService {
    void saveProductRelevance(List<Map<String, Object>> conditions, List<Map<String, Object>> groups,
                              List<Map<String, Object>> containers, Map<String, Object> activity);
    ResponseData selectByProductCode(String productCode);
}
