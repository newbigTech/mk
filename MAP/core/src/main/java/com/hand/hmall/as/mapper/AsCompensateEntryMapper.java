package com.hand.hmall.as.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.as.dto.AsCompensateEntry;

import java.util.List;

/**
 * @author xuxiaoxue
 * @version 0.1
 * @name AsCompensateEntryMapper
 * @description 销售赔付单行表Mapper接口
 * @date 2017/10/11
 */
public interface AsCompensateEntryMapper extends Mapper<AsCompensateEntry> {
    /**
     * 根据赔付单ID查询赔付单行信息
     *
     * @param dto
     */
    List<AsCompensateEntry> selectCompensateEntryById(AsCompensateEntry dto);

    List<AsCompensateEntry> selectCompensateEntryForRetail(AsCompensateEntry dto);
}