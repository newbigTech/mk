package com.hand.hmall.hr.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.hr.dto.MarkorEmployee;

import java.util.List;
import java.util.Map;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 美克员工对象的Service接口
 * @date 2017/7/10 14:37
 */
public interface IMarkorEmployeeService extends IBaseService<MarkorEmployee>, ProxySelf<IMarkorEmployeeService> {

    /**
     * @param employeeCode 员工编码
     * @return
     * @description 根据员工编码查询
     */
    MarkorEmployee selectByEmployeeCode(String employeeCode);

    /**
     * @param request
     * @param list
     * @return
     * @description 导入员工信息时，校验员工信息的正确性
     */
    String checkEmployee(IRequest request, List<MarkorEmployee> list);

    /**
     * 根据code或name查询员工信息列表
     *
     * @param codeOrName - 编码或名称
     * @return 员工信息列表
     */
    List<Map> queryByCodeOrName(IRequest iRequest, String codeOrName);

    /**
     * 查询当前登录者的岗位信息列表(可能具有多个岗位)
     * @param iRequest
     * @return
     */
    List<Map> queryCurrentPositions(IRequest iRequest);
}