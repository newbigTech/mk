package com.hand.hmall.common.service;
import java.util.Date;

/**
 * @author 马君
 * @version 0.1
 * @name IGlobalVariantService
 * @description 全部变量service
 * @date 2017/6/28 20:30
 */
public interface IGlobalVariantService {

    /**
     * 根据code查询value
     * @param code
     * @return
     */
    String getValue(String code);

    /**
     * 根据code查询时间
     * @param code
     * @return
     */
    Date getDate(String code);

    /**
     * 根据code查询数字
     * @param code
     * @return
     */
    <T extends Number> T getNumber(String code, Class<T> tClass);

    /**
     * 设置或修改value
     * @param code
     * @param value
     * @return
     */
    void setValue(String code, String value);

    /**
     * 设置或修改value
     * @param code
     * @param value
     * @param description
     */
    void setValue(String code, String value, String description);

    /**
     * 设置或修改时间参数
     * @param code
     * @param value
     */
    void setDate(String code, Date value);

    /**
     * 设置或修改时间参数
     * @param code
     * @param value
     * @param description
     */
    void setDate(String code, Date value, String description);

    /**
     * 设置或修改数字参数
     * @param code
     * @param value
     */
    void setNumber(String code, Number value);

    /**
     * 设置或修改数字参数
     * @param code
     * @param value
     * @param description
     */
    void setNumber(String code, Number value, String description);
}
