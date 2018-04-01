package com.hand.hmall.service;
/*
 * Copyright (C) HAND Enterprise Solutions Company Ltd.
 * All Rights Reserved
 */

/**
 * @author XinyangMei
 * @Title IDroolsInitDateService
 * @Description desp
 * @date 2017/9/27 23:32
 */
public interface IDroolsInitDateService {
    /**
     * 清除所有drool静态脚本数据
     */
    void clearOldData();

    /**
     * 导入模板类相关信息
     */
    void initModelData();

    /**
     * 导入前台可选促销结果
     */
    void initSelectActionDao();

    /**
     * 导入前台可选促销条件
     */
    void initSelectConditionDao();

    /**
     * 导入促销分组
     */
    void initGroup();
}
