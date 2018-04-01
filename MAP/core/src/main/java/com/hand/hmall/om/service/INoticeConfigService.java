package com.hand.hmall.om.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.om.dto.NoticeConfig;

import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name INoticeConfigService
 * @description INoticeConfigService
 * @date 2017/10/19 14:35
 */
public interface INoticeConfigService extends IBaseService<NoticeConfig> {

    /**
     * 查询通知权限配置界面
     * @param request
     * @param noticeConfig
     * @param page
     * @param pageSize
     * @return
     */
    List<NoticeConfig> noticeConfigList(IRequest request,NoticeConfig noticeConfig,int page,int pageSize);

    /**
     * 添加权限配置
     * @param noticeConfig 配置
     */
    void addConfig(NoticeConfig noticeConfig);

    /**
     * 删除权限配置
     * @param noticeConfigList 权限配置
     */
    void removeConfigs(List<NoticeConfig> noticeConfigList);

    /**
     * 查询员工可见的通知类型
     * @param employeeCode 员工编号
     * @return List<String>
     */
    List<String> selectNoticeTypesOfEmployee(String employeeCode);
}
