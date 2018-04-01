/*
 *  Copyright (C) HAND Enterprise Solutions Company Ltd.
 *  All Rights Reserved
 *
 */

package com.hand.hmall.mapper;


import com.hand.hmall.dto.HmallFndGlobalVariant;
import tk.mybatis.mapper.common.Mapper;

public interface HmallFndGlobalVariantMapper extends Mapper<HmallFndGlobalVariant> {

    /**
     * 根据code获取任信了账号和密码
     * @param code
     * @return
     */
    HmallFndGlobalVariant getUserOrPwdByCode(String code);
}