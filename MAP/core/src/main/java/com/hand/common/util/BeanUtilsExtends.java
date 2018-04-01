package com.hand.common.util;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * 重写BeanUtils.copyProperties
 *
 * @author monkey
 */
public class BeanUtilsExtends extends BeanUtils {
    static {
        ConvertUtils.register(new DateConvert(), java.util.Date.class);
        ConvertUtils.register(new DateConvert(), String.class);
    }

    public static void copyProperties(Object dest, Object orig) {
        try {
            BeanUtils.copyProperties(dest, orig);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        }
    }

    public static List copyListProperties(List<? extends Object> providerList, Class trnClass) {

        List trnList = new ArrayList();

        Object trn = null;
        for (Object provider : providerList) {
            try {
                trn = trnClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            copyProperties(trn, provider);
            trnList.add(trn);
        }
        return trnList;

    }

}