package com.hand.hmall.pin.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.pin.dto.Alter;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author 马君
 * @version 0.1
 * @name AlterMapper
 * @description 错误劲暴表Mapper
 * @date 2017/8/10 17:07
 */
public interface AlterMapper extends Mapper<Alter> {

    /**
     * 根据事件编号查询事件信息
     * @param eventCode
     * @return
     */
    Map queryByEventCode(@Param("eventCode") String eventCode);

}
