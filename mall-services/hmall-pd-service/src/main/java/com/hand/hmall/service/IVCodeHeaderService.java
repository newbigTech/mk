package com.hand.hmall.service;

import com.hand.hmall.model.VCodeHeader;

/**
 * @author 马君
 * @version 0.1
 * @name IVCodeHeaderService
 * @description VCodeHeader Service
 * @date 2017/7/6 18:20
 */
public interface IVCodeHeaderService extends IBaseService<VCodeHeader> {

    /**
     * 通过vCode查找记录，查到多条只返回第一条
     * @param vCode v码
     * @return VCodeHeader
     */
    VCodeHeader selectOneByVCode(String vCode);
}
