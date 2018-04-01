package com.hand.hmall.mst.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.mst.dto.Installation;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 安装费对象的service接口
 * @date 2017/7/10 14:37
 */
public interface IInstallationService extends IBaseService<Installation>, ProxySelf<IInstallationService> {

    /**
     * @param request
     * @param dto
     * @param page
     * @param pageSize
     * @return
     * @description 安装费维护界面查询功能
     */
    public List<Installation> selectInstallation(IRequest request, Installation dto, int page, int pageSize);

    /**
     * @param request
     * @param list
     * @return
     * @throws ParseException
     * @description 安装费导入时，校验数据的正确性
     */
    public Map<String,Object> checkInstallation(IRequest request, List<Installation> list);
}