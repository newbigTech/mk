package com.hand.hmall.convertor;

/**
 * @author 马君
 * @version 0.1
 * @name Convertor
 * @description 对象转换器，可以将DTO转化成实体类（Model）
 * @date 2017/6/3 11:21
 */
public interface Convertor<S, T> {

    /**
     * 将原对象S，转化成目标对象T
     * @param s 源对象
     * @return 目标对象
     */
    T convert(S s);

    /**
     * 将原对象S，转化成目标对象T
     * @param s 源对象
     * @param t 目标对象
     * @return 目标对象
     */
    T convert(S s, T t);
}
