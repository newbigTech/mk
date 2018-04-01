package com.hand.hmall.mapper;

import com.hand.hmall.dto.FndRegionB;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface FndRegionBMapper extends Mapper<FndRegionB> {

    /**
     * 根据名称查询地区编码
     *
     * @param name
     * @return
     */
    List<FndRegionB> selectCodeByName(@Param(value = "name") String name);

}