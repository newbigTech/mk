package com.hand.hmall.om.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.om.dto.NoticeConfig;
import java.util.List;

import com.hand.hmall.om.mapper.NoticeConfigMapper;
import com.hand.hmall.om.service.INoticeConfigService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 马君
 * @version 0.1
 * @name NoticeConfigServiceImpl
 * @description NoticeConfigServiceImpl
 * @date 2017/10/19 14:35
 */
@Service
public class NoticeConfigServiceImpl extends BaseServiceImpl<NoticeConfig> implements INoticeConfigService {

    @Autowired
    private NoticeConfigMapper noticeConfigMapper;

    /**
     * 查询通知权限配置界面
     * @param request
     * @param noticeConfig
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<NoticeConfig> noticeConfigList(IRequest request, NoticeConfig noticeConfig, int page, int pageSize) {
        PageHelper.startPage(page,pageSize);
        return noticeConfigMapper.selectNoticeConfigList(noticeConfig);
    }

    /**
     * 添加权限配置
     * @param noticeConfig 配置
     */
    @Override
    public void addConfig(NoticeConfig noticeConfig) {
        IRequest iRequest = RequestHelper.newEmptyRequest();
        List<NoticeConfig> noticeConfigList = this.select(iRequest, noticeConfig, 1, Integer.MAX_VALUE);
        if (CollectionUtils.isEmpty(noticeConfigList)) {
            this.insertSelective(iRequest, noticeConfig);
        }
    }

    /**
     * 删除权限配置
     * @param noticeConfigList 权限配置
     */
    @Override
    public void removeConfigs(List<NoticeConfig> noticeConfigList)  {
        noticeConfigList.stream().forEach(config -> this.deleteByPrimaryKey(config));
    }

    /**
     * 查询员工可见的通知类型
     * @param employeeCode 员工编号
     * @return
     */
    @Override
    public List<String> selectNoticeTypesOfEmployee(String employeeCode) {
        return noticeConfigMapper.selectNoticeTypesOfEmployee(employeeCode);
    }
}
