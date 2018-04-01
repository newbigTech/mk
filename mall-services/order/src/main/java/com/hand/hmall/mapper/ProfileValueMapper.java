package com.hand.hmall.mapper;

import com.hand.hmall.model.ProfileValue;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * author: zhangzilong
 * name: ProfileValueMapper.java
 * discription:
 * date: 2017/11/17
 * version: 0.1
 */
public interface ProfileValueMapper {

    List<ProfileValue> selectPriorityValues(@Param("profileName") String profileName);

}
