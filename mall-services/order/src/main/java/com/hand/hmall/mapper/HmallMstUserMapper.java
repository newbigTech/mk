package com.hand.hmall.mapper;


import com.hand.hmall.model.HmallMstUser;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author 梅新养
 * @name:HmallMstUserMapper
 * @Description:会员信息查询Mapper
 * @version 1.0
 * @date 2017/5/24 14:39
 */
public interface HmallMstUserMapper extends Mapper<HmallMstUser> {

    /**
     * 根据会员id查询会员信息
     *
     * @param customerId 会员id
     * @return
     */
    HmallMstUser selectUserByCustomerId(String customerId);
}