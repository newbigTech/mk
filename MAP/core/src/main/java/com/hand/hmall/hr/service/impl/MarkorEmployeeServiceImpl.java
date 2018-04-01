package com.hand.hmall.hr.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.CodeValue;
import com.hand.hap.system.service.ICodeService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.hr.dto.MarkorEmployee;
import com.hand.hmall.hr.mapper.MarkorEmployeeMapper;
import com.hand.hmall.hr.service.IMarkorEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 美克员工对象的Service实现
 * @date 2017/7/10 14:37
 */
@Service("iMarkorEmployeeService")
@Transactional
public class MarkorEmployeeServiceImpl extends BaseServiceImpl<MarkorEmployee> implements IMarkorEmployeeService {

    @Autowired
    private MarkorEmployeeMapper markorEmployeeMapper;

    @Autowired
    private ICodeService codeService;

    @Override
    public MarkorEmployee selectByEmployeeCode(String employeeCode) {

        MarkorEmployee employeeParams = new MarkorEmployee();
        employeeParams.setEmployeeCode(employeeCode);

        return markorEmployeeMapper.selectOne(employeeParams);
    }

    /**
     * 校验员工
     * @param iRequest
     * @param list
     * @return
     */
    @Override
    public String checkEmployee(IRequest iRequest, List<MarkorEmployee> list) {
        //校验输入的值是否在快码表中存在
        List<CodeValue> certificateTypeData = codeService.selectCodeValuesByCodeName(iRequest, "HR.CERTIFICATE_TYPE");
        List<CodeValue> genderData = codeService.selectCodeValuesByCodeName(iRequest, "HR.EMPLOYEE_GENDER");
        List<CodeValue> statusData = codeService.selectCodeValuesByCodeName(iRequest, "HR.EMPLOYEE_STATUS");
        String result = null;
        if (list != null && list.size() > 0) {
            for (MarkorEmployee employee : list) {

                //校验
                if (employee.getEmployeeCode() == null || employee.getName() == null || employee.getCertificateType() == null
                        || employee.getGender() == null || employee.getCertificateId() == null || employee.getStatus() == null) {
                    result = "员工编码,员工姓名，证件类型，性别，证件号，状态不能为空!";
                    return result;
                }

                //验证证件类型
                int flag = 1;
                for (CodeValue codeValue : certificateTypeData) {
                    if (employee.getCertificateType().equals(codeValue.getMeaning())) {
                        employee.setCertificateType(codeValue.getValue());
                        flag = 0;
                        break;
                    }
                }
                if (flag == 1) {
                    result = "您输入的证件类型不存在!";
                    return result;
                }

                //验证性别
                flag = 1;
                for (CodeValue codeValue : genderData) {
                    if (employee.getGender().equals(codeValue.getMeaning())) {
                        employee.setGender(codeValue.getValue());
                        flag = 0;
                        break;
                    }
                }
                if (flag == 1) {
                    result = "您输入的性别不存在!";
                    return result;
                }

                //验证状态
                flag = 1;
                for (CodeValue codeValue : statusData) {
                    if (employee.getStatus().equals(codeValue.getMeaning())) {
                        employee.setStatus(codeValue.getValue());
                        flag = 0;
                        break;
                    }
                }
                if (flag == 1) {
                    result = "您输入的状态不存在!";
                    return result;
                }

                //验证员工编码的唯一性
                MarkorEmployee codeEmployee = this.selectByEmployeeCode(employee.getEmployeeCode());
                if (codeEmployee != null) {
                    result = "您输入的员工编码已存在!";
                    return result;
                }
                //验证证件号的唯一性
                MarkorEmployee markorEmployee = new MarkorEmployee();
                markorEmployee.setCertificateId(employee.getCertificateId());
                List<MarkorEmployee> employeeList = this.select(iRequest, markorEmployee, 1, 10);
                if (employeeList != null && employeeList.size() > 0) {
                    result = "您输入的证件号已存在!";
                    return result;
                }

            }
        }

        return result;
    }

    /**
     * 查询code或者name
     * @param iRequest
     * @param codeOrName - 编码或名称
     * @return
     */
    @Override
    public List<Map> queryByCodeOrName(IRequest iRequest, String codeOrName) {
        List<Map> employees = markorEmployeeMapper
                .queryByCodeOrName("__CURRENT_EMPLOYEE__".equals(codeOrName) ? "" : codeOrName,
                        "__CURRENT_EMPLOYEE__".equals(codeOrName) ? iRequest.getUserId() : null);
                // __CURRENT_EMPLOYEE__ 表示将查询结果限定在当前操作员
        return employees;
    }

    @Override
    public List<Map> queryCurrentPositions(IRequest iRequest) {
        return markorEmployeeMapper.queryCurrentPositions(iRequest.getUserId());
    }
}