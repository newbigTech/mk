package com.hand.hmall.service;

import com.hand.hmall.dto.Catalogs;

/**
 * @author 马君
 * @version 0.1
 * @name ICatalogsService
 * @description ICatalogsService
 * @date 2017/6/30 9:58
 */
public interface ICatalogsService {

    /**
     * 查询master
     * @return 目录
     */
    Catalogs selectMaster();

    /**
     * 查询美克
     * @return 目录
     */
    Catalogs selectMarkor();
}
