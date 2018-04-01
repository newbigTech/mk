package com.hand.hmall.service;

import com.hand.hmall.model.Conrel;

import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name IConrelService
 * @description 伙伴关系Service
 * @date 2017/9/12 14:03
 */
public interface IConrelService extends IBaseService<Conrel> {

    /**
     * 根据fabric字段查询
     * @param platformCode 平台编码
     * @param fabric 物料编码
     * @return List<Conrel>
     */
    List<Conrel> select(String platformCode, String fabric);
}
