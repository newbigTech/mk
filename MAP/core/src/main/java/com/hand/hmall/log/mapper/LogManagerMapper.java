package com.hand.hmall.log.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.log.dto.LogManager;

import java.util.List;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name LogManagerMapper
 * @description 日志
 * @date 2017年5月26日10:52:23
 */
public interface LogManagerMapper extends Mapper<LogManager>{


    /**
     * 根据查询条件查询数据
     * @param dto
     * @return
     */
    List<LogManager> queryAll(LogManager dto);

}