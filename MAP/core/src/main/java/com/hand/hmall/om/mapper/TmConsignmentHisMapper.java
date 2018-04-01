package com.hand.hmall.om.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.om.dto.HmallTmConsignmentHis;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author liuhongxi
 * @version 0.1
 * @name TmConsignmentHisMapper
 * @description 天猫订单发货状态导出历史mapper
 * @date 2017/6/22 17:39
 */
public interface TmConsignmentHisMapper extends Mapper<HmallTmConsignmentHis> {

    /**
     * 创建导出历史记录
     * @return
     */
    Integer createExportData(@Param("outTime") Date outTime);

    /**
     * 导出所有的历史记录（倒序）
     * @return
     */
    List<HmallTmConsignmentHis> queryAll();
}
