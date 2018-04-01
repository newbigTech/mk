package com.hand.hmall.service;

import com.hand.hmall.pojo.LovData;

/**
 * @author 马君
 * @version 0.1
 * @name IEnumerationService
 * @description 枚举Service
 * @date 2017/6/23 13:46
 */
public interface IEnumerationService {

    /**
     * 检查枚举值是否存在
     * @param code 枚举类code
     * @param value 枚举值
     * @return boolean
     */
    boolean checkEnumerationValue(String code, String value);

    /**
     * 如果枚举值不存在则新增
     * @param lovData lov对象
     * @param enumType 枚举类型
     */
    void addLovIfNotExists(LovData lovData, String enumType);
}
