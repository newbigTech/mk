package com.hand.hmall.convertor;

/**
 * @author 马君
 * @version 0.1
 * @name Convertor
 * @description 对象转换器，将源对象S转换成目标T
 * @date 2017/6/3 11:21
 */
public interface Convertor<S, T> {

    /**
     * 将源对象S转换成目标T
     * @param s 源对象
     * @return 目标对象
     */
    T convert(S s);

    /**
     * 将源对象S转换成目标T
     * @param s 源对象
     * @param t 目标对象
     * @return 目标对象
     */
    T convert(S s, T t);
}
