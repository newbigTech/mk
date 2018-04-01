package com.hand.promotion.mapper;


import com.hand.promotion.dto.MstPointOfService;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 门店信息Mapper
 *
 * @author xinyangMei
 */
public interface MstPointOfServiceMapper extends Mapper<MstPointOfService> {

    /**
     * 根据门店编码查询门店信息
     *
     * @param code
     * @return
     */
    List<MstPointOfService> selectByCode(@Param(value = "code") String code);

}