package com.hand.hmall.hr.mapper;

import com.hand.hap.hr.dto.Employee;
import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.hr.dto.MarkorEmployee;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MarkorEmployeeMapper extends Mapper<MarkorEmployee>{

    List<Map> queryByCodeOrName(@Param("codeOrName") String codeOrName, @Param("sysUserId") Long sysUserId);

//    /**
//     * 根据当前员工的user id查询岗位代码
//     *
//     * @param employeeUserId - 当前员工的USER ID
//     * @return
//     */
//    String queryEmployeePosition(@Param("employeeUserId") Long employeeUserId);

    /**
     * 根据当前登录着ID查询其岗位信息
     * 可能具有多个岗位
     * @param userId - 当前登录者ID
     * @return
     */
    List<Map> queryEmployeePositionInfo(@Param("userId") Long userId);

    /**
     * 根据岗位编码查询员工列表
     * @param positionCode - 岗位编码
     * @return
     */
    List<Map> selectByPostionCode(@Param("positionCode") String positionCode);

    /**
     * 根据当前登录着ID查询其岗位信息列表
     * @param userId - 当前登录者ID
     * @return
     */
    List<Map> queryCurrentPositions(@Param("userId") Long userId);
}