package com.hand.hmall.validator;

/**
 * @author 马君
 * @version 0.1
 * @name Validator
 * @description 对目标对象进行合法性检查
 * @date 2017/6/5 19:49
 */
public interface Validator<T> {
    /**
     * 检查对象
     * @param t 目标对象
     * @return 检查结果
     */
    ValidateResult validate(T t);
}
