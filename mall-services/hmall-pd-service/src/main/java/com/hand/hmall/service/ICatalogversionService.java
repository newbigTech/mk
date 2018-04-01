package com.hand.hmall.service;

import com.hand.hmall.model.Catalogversion;

/**
 * @author 马君
 * @version 0.1
 * @name ICatalogversionService
 * @description ICatalogversionService
 * @date 2017/6/30 9:53
 */
public interface ICatalogversionService {

    /**
     * 获取美克master版本
     * @return 目录版本
     */
    Catalogversion selectMasterStaged();

    /**
     * 获取美克staged版本
     * @return 目录版本
     */
    Catalogversion selectMarkorStaged();

    /**
     * 获取美克online版本
     * @return 目录版本
     */
    Catalogversion selectMarkorOnline();
}
