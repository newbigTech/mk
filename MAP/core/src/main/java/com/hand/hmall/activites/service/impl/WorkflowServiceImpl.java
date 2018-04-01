package com.hand.hmall.activites.service.impl;

import com.hand.hap.activiti.custom.IActivitiBean;
import com.hand.hap.hr.dto.Employee;
import com.hand.hap.hr.mapper.EmployeeMapper;
import com.hand.hmall.activites.mapper.WorkflowMapper;
import com.hand.hmall.activites.service.IWorkflowService;
import com.hand.hmall.hr.mapper.MarkorEmployeeMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author chenzhigang
 * @version 0.1
 * @name WorkflowServiceImpl
 * @description 工作流逻辑实现类
 * @date 2017/11/15
 */
@Service
public class WorkflowServiceImpl implements IWorkflowService, IActivitiBean {

    @Autowired
    private WorkflowMapper mapper;

    @Autowired
    private MarkorEmployeeMapper employeeMapper;

    @Override
    public Map queryAsRefundInfo(Long asRefundOrderId) {
        return mapper.queryAsRefundInfo(asRefundOrderId);
    }

    @Override
    public Map queryAsRefundInfoByProcinstId(Long procinstId) {

        Map refundInfo = mapper.queryAsRefundInfoByProcinstId(procinstId);
        if (refundInfo == null) {
            refundInfo = mapper.queryAsRefundInfoByProcinstId_hi(procinstId);
        }

        return refundInfo;
    }

    /**
     * 根据岗位编码查询匹配的雇员编码（不受主岗位约束限制）
     *
     * @param positionCode - 岗位编码
     * @return
     */
    @Override
    public String getPositionEmp(String positionCode) {
        List<Map> employees = employeeMapper.selectByPostionCode(positionCode);
        Object[] emps = new Object[employees.size()];
        for (int i = 0; i < employees.size(); i++) {
            emps[i] = employees.get(i).get("EMPLOYEE_CODE").toString();
        }
        String employeeCodes = org.springframework.util.StringUtils.arrayToCommaDelimitedString(emps);
        return employeeCodes;
    }

    @Override
    public String getBeanName() {
        return "CustomWorkflowService";
    }
}
