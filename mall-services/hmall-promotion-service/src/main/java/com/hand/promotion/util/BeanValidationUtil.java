package com.hand.promotion.util;

import com.hand.promotion.pojo.SimpleMessagePojo;
import com.hand.promotion.pojo.enums.MsgMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Iterator;
import java.util.Set;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2017/12/18
 * @description 校验bean是否合法
 */
public class BeanValidationUtil {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 校验bean是否符合 jsr 303 校验规范
     *
     * @param pojo
     * @param <T>
     * @return
     */
    public static final <T> SimpleMessagePojo validate(T pojo) {
        SimpleMessagePojo simpleMessagePojo = new SimpleMessagePojo();
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<T>> validateResult = validator.validate(pojo);
        if (validateResult.size() == 0) {
            return simpleMessagePojo;
        } else {
            Iterator<ConstraintViolation<T>> iterator = validateResult.iterator();
            while (iterator.hasNext()) {
                ConstraintViolation<T> next = iterator.next();
                simpleMessagePojo.setMessage(MsgMenu.BEAN_INVALID);
                simpleMessagePojo.setFalse(next.getPropertyPath() + next.getMessage());
            }
        }
        return simpleMessagePojo;
    }

}
