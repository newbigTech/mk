package com.hand.hmall.mapper;

import com.hand.hmall.dto.HmallMstUser;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author 阳赳
 * @name:HmallMstUserMapper
 * @Description:用户Mapper
 * @date 2017/6/1 9:46
 */
public interface HmallMstUserMapper extends Mapper<HmallMstUser> {
    /**
     * 通过手机号码查询对象
     * @param mobileNumber 手机号码
     * @return
     */
    HmallMstUser selectByMobile(String mobileNumber);

    /**
     * 通过用户id查询对象
     * @param customerId 用户id
     * @return
     */
    HmallMstUser selectByCustomerId(String customerId);

    List<HmallMstUser> selectByNotIn(@Param("customerIds") List<String> custometIds);

    List<HmallMstUser> matchByCondition(@Param("user") HmallMstUser user);
}

