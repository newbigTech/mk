package com.hand.hmall.mapper;

import com.hand.hmall.model.CodeValue;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author 马君
 * @version 0.1
 * @name CodeValueMapper
 * @description 块码Mapper
 * @date 2017/6/6 15:58
 */
public interface CodeValueMapper extends Mapper<CodeValue> {

    /**
     * 根据编码和值查询
     * @param code 编码
     * @param value 值
     * @return CodeValue
     */
    CodeValue selectUniqueByCodeAndValue(@Param("code") String code, @Param("value") String value);
}
