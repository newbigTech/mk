package com.hand.promotion.mapper;

import com.hand.promotion.dto.FndRegionB;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 地区编码查询Mapper
 *
 * @author xinyangMei
 */
public interface FndRegionBMapper extends Mapper<FndRegionB> {

    /**
     * 根据地区名称查询地区编码
     *
     * @param name 地区名称
     * @return
     */
    List<FndRegionB> selectCodeByName(@Param(value = "name") String name);

}