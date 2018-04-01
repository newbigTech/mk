package com.hand.hmall.common.service.impl;

import com.hand.hmall.common.service.IGlobalVariantService;
import com.hand.hmall.fnd.dto.GlobalVariant;
import com.hand.hmall.fnd.mapper.GlobalVariantMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.NumberUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 马君
 * @version 0.1
 * @name GlobalVariantServiceImpl
 * @description GlobalVariantServiceImpl
 * @date 2017/6/28 20:32
 */
@Service("iGlobalVariantService")
public class GlobalVariantServiceImpl implements IGlobalVariantService {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private GlobalVariantMapper globalVariantMapper;

    /**
     * 根据code查询value
     * @param code
     * @return
     */
    @Override
    public String getValue(String code) {
        GlobalVariant globalVariantParams = new GlobalVariant();
        globalVariantParams.setCode(code);
        GlobalVariant globalVariant = globalVariantMapper.selectOne(globalVariantParams);
        if (globalVariant == null) {
            return null;
        } else {
            return globalVariant.getValue();
        }
    }

    /**
     * 设置或修改value
     * @param code
     * @param value
     */
    @Override
    public void setValue(String code, String value) {
        setValue(code, value, null);
    }

    /**
     * 设置或修改value
     * @param code
     * @param value
     * @param description
     */
    @Override
    public void setValue(String code, String value, String description) {
        GlobalVariant globalVariantParams = new GlobalVariant();
        globalVariantParams.setCode(code);
        GlobalVariant globalVariant = globalVariantMapper.selectOne(globalVariantParams);
        if (globalVariant != null) {
            globalVariant.setValue(value);
            globalVariant.setDescription(description);
            globalVariantMapper.updateByPrimaryKeySelective(globalVariant);
        } else {
            globalVariantParams.setValue(value);
            globalVariantParams.setDescription(description);
            globalVariantMapper.insertSelective(globalVariantParams);
        }
    }

    /**
     * 根据code查询时间
     * @param code
     * @return
     */
    @Override
    public Date getDate(String code) {
        String value = getValue(code);
        if (StringUtils.isBlank(value)) {
            return null;
        } else {
            try {
                return sdf.parse(value);
            } catch (ParseException e) {
                return null;
            }
        }
    }

    /**
     * 根据code查询数字
     * @param code
     * @param tClass
     * @param <T>
     * @return
     */
    @Override
    public <T extends Number> T getNumber(String code, Class<T> tClass) {
        String value = getValue(code);
        if (StringUtils.isBlank(value)) {
            return null;
        } else {
            return NumberUtils.parseNumber(value, tClass);
        }
    }

    /**
     * 设置或修改时间参数
     * @param code
     * @param value
     */
    @Override
    public void setDate(String code, Date value) {
        setDate(code, value, null);
    }

    /**
     * 设置或修改时间参数
     * @param code
     * @param value
     * @param description
     */
    @Override
    public void setDate(String code, Date value, String description) {
        String dateStr = sdf.format(value);
        setValue(code, dateStr, description);
    }

    /**
     * 设置或修改数字参数
     * @param code
     * @param value
     */
    @Override
    public void setNumber(String code, Number value) {
        setNumber(code, value, null);
    }

    /**
     * 设置或修改数字参数
     * @param code
     * @param value
     * @param description
     */
    @Override
    public void setNumber(String code, Number value, String description) {
        setValue(code, value.toString(), description);
    }
}
