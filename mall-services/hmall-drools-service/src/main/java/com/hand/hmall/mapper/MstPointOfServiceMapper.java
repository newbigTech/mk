package com.hand.hmall.mapper;


import com.hand.hmall.dto.MstPointOfService;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface MstPointOfServiceMapper extends Mapper<MstPointOfService> {

    List<MstPointOfService> selectByCode(@Param(value = "code") String code);

}