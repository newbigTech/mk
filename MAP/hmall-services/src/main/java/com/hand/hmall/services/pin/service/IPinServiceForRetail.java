package com.hand.hmall.services.pin.service;


import com.hand.hmall.services.pin.dto.PinZest;

import java.util.List;

/**
 * @author chenzhigang
 * @version 0.1
 * @name IPinServiceForRetail
 * @description PIN码信息服务接口
 * @date 2017/8/4
 */
public interface IPinServiceForRetail {

    /**
     * pin码接口Service方法
     *
     * @param dto
     * @return
     */
    void insertMany(PinZest dto);

}
