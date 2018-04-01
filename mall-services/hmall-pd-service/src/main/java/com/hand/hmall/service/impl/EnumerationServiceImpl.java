package com.hand.hmall.service.impl;

import com.hand.hmall.mapper.CodeMapper;
import com.hand.hmall.mapper.CodeValueMapper;
import com.hand.hmall.model.Code;
import com.hand.hmall.model.CodeValue;
import com.hand.hmall.pojo.LovData;
import com.hand.hmall.service.IEnumerationService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 马君
 * @version 0.1
 * @name EnumerationServiceImpl
 * @description 枚举服务实现类
 * @date 2017/6/23 13:48
 */
@Service
public class EnumerationServiceImpl implements IEnumerationService {

    @Autowired
    private CodeValueMapper codeValueMapper;

    @Autowired
    private CodeMapper codeMapper;

    /**
     * {@inheritDoc}
     *
     * @see IEnumerationService#checkEnumerationValue
     */
    @Override
    public boolean checkEnumerationValue(String code, String value) {
        return codeValueMapper.selectUniqueByCodeAndValue(code, value) == null
                ? false : true;
    }

    /**
     * {@inheritDoc}
     *
     * @see IEnumerationService#addLovIfNotExists
     */
    @Override
    public void addLovIfNotExists(LovData lovData, String enumType) {
        if (!checkEnumerationValue(enumType, lovData.getCode())) {
            Code code = codeMapper.selectUniqueByCode(enumType);
            CodeValue codeValue = new CodeValue();
            codeValue.setValue(lovData.getCode());
            codeValue.setMeaning(lovData.getName());
            codeValue.setCodeId(code.getCodeId());
            codeValueMapper.insertSelective(codeValue);
        }
    }
}
