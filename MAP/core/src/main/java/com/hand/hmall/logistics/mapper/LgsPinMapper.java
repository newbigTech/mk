package com.hand.hmall.logistics.mapper;

import com.hand.hmall.logistics.pojo.DeliveryOrder;
import com.hand.hmall.logistics.pojo.LgsPin;
//import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

/**
 * author: zhangzilong
 * name: PinMapper.java
 * discription: PIN码信息Mapper
 * date: 2017/11/9
 * version: 0.1
 */
public interface LgsPinMapper /*extends BaseMapper<LgsPin>*/ {

    void insertPin(LgsPin pin);

    List<LgsPin> selectPinByDelivery(DeliveryOrder delivery);

}
