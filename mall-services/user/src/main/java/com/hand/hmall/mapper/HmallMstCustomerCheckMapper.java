package com.hand.hmall.mapper;

import com.hand.hmall.dto.HmallMstCustomerCheck;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
/**
 * @author 阳赳
 * @name:HmallMstCustomerCheckMapper
 * @Description:用户信息校验mapper
 * @date 2017/6/1 9:46
 */
public interface HmallMstCustomerCheckMapper extends Mapper<HmallMstCustomerCheck> {
    /**
     * 通过手机号和业务类型查询对象
     * @param mobileNumber 用户手机号
     * @param sendType 业务类型
     * @return
     */
    HmallMstCustomerCheck selectByMobileAndSendType(@Param("mobileNumber") String mobileNumber, @Param("sendType") String sendType);

    /**
     * 通过手机号查询对象
     * @param mobileNumber 用户手机号
     * @return
     */
    HmallMstCustomerCheck selectByMobile(String mobileNumber);
}