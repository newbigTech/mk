package com.hand.hmall.as.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.as.dto.Svsales;
import com.hand.hmall.as.dto.SvsalesEntry;

import java.util.List;

/**
 * @author zhangzilong
 * @version 0.1
 * @name SvsalesEntryMapper
 * @description 服务单行mapper
 * @date 2017/7/28
 */
public interface SvsalesEntryMapper extends Mapper<SvsalesEntry> {


    /**
     * 根据服务单CODE查询销售服务单行信息
     *
     * @param dto
     * @return
     */
    List<SvsalesEntry> querySvsalesEntyiedInfo(Svsales dto);

    /**
     * 根据传入的id数组查询服务单行信息
     * @param ids
     * @return
     */
    List<SvsalesEntry> selectSvsalesEntriesById(List<Long> ids);

}