package com.hand.hmall.om.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.om.dto.Notification;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name NotificationMapper
 * @description 通知Mapper
 * @date 2017/10/19 14:44
 */
public interface NotificationMapper extends Mapper<Notification> {

    List<Notification> selectByNotification(@Param("notification") Notification notification, @Param("noticeTypes") List<String> noticeTypes);
}
