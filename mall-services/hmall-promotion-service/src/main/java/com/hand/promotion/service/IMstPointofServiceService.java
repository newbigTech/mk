package com.hand.promotion.service;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */


import com.hand.promotion.dto.MstPointOfService;

/**
 * @author XinyangMei
 * @Title IMstPointofServiceService
 * @Description 门店信息相关接口
 * @date 2017/10/26 9:06
 */
public interface IMstPointofServiceService {
    /**
     * 根据门店编码查询门店信息
     *
     * @param code 门店编码
     * @return
     */
    MstPointOfService queryByCode(String code);
}
