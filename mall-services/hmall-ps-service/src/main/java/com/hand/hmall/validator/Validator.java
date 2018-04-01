package com.hand.hmall.validator;

/**
 * @author 马君
 * @version 0.1
 * @name Validator
 * @description Validator
 * @date 2017/6/5 19:49
 */
public interface Validator<T> {
    ValidateResult validator(T t);
}
