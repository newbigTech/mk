package com.hand.hmall.pin.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.pin.dto.Pin;
import com.hand.hmall.pin.dto.PinInfo4SendZmall;
import com.hand.hmall.services.pin.dto.PinZest;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author chenzhigang
 * @version 0.1
 * @name PinMapper
 * @description PIN码查询接口
 * @date 2017/8/4
 */
public interface PinMapper extends Mapper<Pin> {

    List<Pin> queryByCode(@Param("code") String code);

    void insertOne(PinZest dto);

    /**
     * 查询全部非同步状态PIN码
     *
     * @return
     */
    List<PinInfo4SendZmall> queryNotSyncPinInfo();

    /**
     * 更新Zmall同步标志位
     *
     * @param syncFlag
     */
    void updateSynvZmallFlag(@Param("syncFlag") String syncFlag);
}
