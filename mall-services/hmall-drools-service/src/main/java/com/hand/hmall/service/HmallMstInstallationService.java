package com.hand.hmall.service;
/*
 * Copyright (C) HAND Enterprise Solutions Company Ltd.
 * All Rights Reserved
 */

import com.hand.hmall.dto.HmallMstInstallation;

/**
 * @author XinyangMei
 * @Title HmallMstInstallationService
 * @Description desp
 * @date 2017/6/28 21:43
 */
public interface HmallMstInstallationService {
    HmallMstInstallation getInstallationByCategoryIdAndStatus(Long categoryId,String status);
}
