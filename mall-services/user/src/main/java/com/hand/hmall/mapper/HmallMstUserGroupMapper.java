package com.hand.hmall.mapper;

import com.hand.hmall.dto.HmallMstUserGroup;
import tk.mybatis.mapper.common.Mapper;
/**
 * @author 阳赳
 * @name:HmallMstUserGroupMapper
 * @Description:用户组Mapper
 * @date 2017/6/1 9:46
 */
public interface HmallMstUserGroupMapper extends Mapper<HmallMstUserGroup> {
    /**
     * 查询对象通过Code
     * @param Code 用户组编码
     * @return
     */
    HmallMstUserGroup selectBycode(String Code);

}