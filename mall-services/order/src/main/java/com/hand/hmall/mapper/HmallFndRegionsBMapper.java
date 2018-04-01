package com.hand.hmall.mapper;


import com.hand.hmall.model.HmallFndRegionsB;
import tk.mybatis.mapper.common.Mapper;
/**
 * @author 梅新养
 * @name:HmallFndRegionsBMapper
 * @Description:查询省份信息Mapper
 * @version 1.0
 * @date 2017/5/24 14:39
 */
public interface HmallFndRegionsBMapper extends Mapper<HmallFndRegionsB> {
    /**
     * 根据省份的code查询省份信息
     *
     * @param code 省份code编码
     * @return
     */
    HmallFndRegionsB selectRegionByRegionCode(String code);
}