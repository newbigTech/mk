package com.hand.hmall.mapper;

import com.hand.hmall.model.Code;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author 马君
 * @version 0.1
 * @name CodeMapper
 * @description 块码表Mapper
 * @date 2017/6/6 14:21
 */
public interface CodeMapper extends Mapper<Code> {

    /**
     * 根据块码id查询块码
     * @param codeId 主键
     * @return Code
     */
    Code selectUniqueByCodeId(@Param("codeId") String codeId);

    /**
     * 根据code查询
     * @param code 编码
     * @return Code
     */
    Code selectUniqueByCode(@Param("code") String code);
}
