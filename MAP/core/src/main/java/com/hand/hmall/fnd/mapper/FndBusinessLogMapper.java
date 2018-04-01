package com.hand.hmall.fnd.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.fnd.dto.FndBusinessLog;

import java.util.List;

public interface FndBusinessLogMapper extends Mapper<FndBusinessLog>{

    /**
     * 操作日志标签页根据订单ID查询数据
     * @param dto   请求参数，包含订单ID
     * @return  查询结果
     */
    List<FndBusinessLog> selectByOrderId(FndBusinessLog dto);
}