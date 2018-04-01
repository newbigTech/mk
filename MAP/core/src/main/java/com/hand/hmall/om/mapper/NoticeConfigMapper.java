package com.hand.hmall.om.mapper;


import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.om.dto.NoticeConfig;

import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name NoticeConfigMapper
 * @description 通知配置表
 * @date 2017/10/19
 */
public interface NoticeConfigMapper extends Mapper<NoticeConfig> {
    List<String> selectNoticeTypesOfEmployee(String employeeCode);

    List<NoticeConfig> selectNoticeConfigList(NoticeConfig noticeConfig);
}