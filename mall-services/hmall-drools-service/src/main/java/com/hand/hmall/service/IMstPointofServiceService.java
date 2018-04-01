package com.hand.hmall.service;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import com.hand.hmall.dto.MstPointOfService;

import java.util.List;

/**
 * @author XinyangMei
 * @Title IMstPointofServiceService
 * @Description 门店查询service
 * @date 2017/10/26 9:06
 */
public interface IMstPointofServiceService {
    MstPointOfService queryByCode(String code);
}
