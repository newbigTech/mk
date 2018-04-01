package com.hand.hmall.mapper;

import com.hand.hmall.model.HmallOmOrderBk;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author 李伟
 * @version 0.1
 * @name OrderBkServiceImpl
 * @description 订单备份Service接口实现类
 * @date 2017/8/24 9:38
 */
public interface HmallOmOrderBkMapper extends Mapper<HmallOmOrderBk> {


    /**
     * 计算订单备份的下一个版本
     * @param code 订单编号
     * @return Long
     */
    Long selectNextVersion(String code);

    /**
     * 通过员工编号查询员工id
     * @param code 员工编号
     * @return Long
     */
    Long selectUserByCode(String code);
}
