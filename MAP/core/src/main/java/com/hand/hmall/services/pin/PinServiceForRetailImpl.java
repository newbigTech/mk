package com.hand.hmall.services.pin;

import com.hand.hmall.pin.mapper.PinMapper;
import com.hand.hmall.pin.service.IPinAlterInfoService;
import com.hand.hmall.services.pin.dto.PinZest;
import com.hand.hmall.services.pin.service.IPinServiceForRetail;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * author: zhangzilong
 * name: PinServiceForRetailImpl.java
 * discription:
 * date: 2018/1/2
 * version: 0.1
 */
public class PinServiceForRetailImpl implements IPinServiceForRetail {

    @Autowired
    private PinMapper pinMapper;


    /**
     * pin码接口Service方法
     *
     * @param dtos
     * @return
     */
    @Override
    public void insertMany(PinZest dtos) {
        pinMapper.insertOne(dtos);
    }

}
