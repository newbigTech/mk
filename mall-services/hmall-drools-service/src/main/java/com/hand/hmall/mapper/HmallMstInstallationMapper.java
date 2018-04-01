package com.hand.hmall.mapper;

import com.hand.hmall.dto.HmallMstInstallation;

public interface HmallMstInstallationMapper {


    /**
     *
     * @mbggenerated 2017-06-28
     */
    HmallMstInstallation getInstallationByCategoryIdAndStatus(Long categoryId,String status);

}