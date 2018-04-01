package com.hand.hap.core.code;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.system.service.ICodeService;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author alaowan
 * Created at 2017/12/13 19:30
 * @description
 */
public class SystemCodeServiceProviderImpl implements ISystemCodeService {

    private static final Logger logger = LoggerFactory.getLogger(SystemCodeServiceProviderImpl.class);

    @Autowired
    private ICodeService codeService;

    @Override
    public List<Code> findCodes(Long codeId, String codeName, boolean includeDisabled) {
        List<Code> resultList = new ArrayList<>();
        IRequest iRequest = RequestHelper.newEmptyRequest();
        iRequest.setLocale("zh_CN");
        com.hand.hap.system.dto.Code condition = new com.hand.hap.system.dto.Code();
        condition.setCode(codeName);
        condition.setCodeId(codeId);
        if (!includeDisabled) {
            condition.setEnabledFlag("Y");
        }
        List<com.hand.hap.system.dto.Code> codes = codeService.selectCodes(iRequest, condition, 1, 9999);
        if (!codes.isEmpty())
            copyArray(resultList, codes, Code.class);
        return resultList;
    }

    @Override
    public List<CodeValue> getCodeValuesByCodeId(Long codeId, boolean includeDisabled) {
        List<CodeValue> resultList = new ArrayList<>();
        IRequest iRequest = RequestHelper.newEmptyRequest();
        iRequest.setLocale("zh_CN");
        com.hand.hap.system.dto.CodeValue condition = new com.hand.hap.system.dto.CodeValue();
        condition.setCodeId(codeId);
        if (!includeDisabled) {
            condition.setEnabledFlag("Y");
        }
        List<com.hand.hap.system.dto.CodeValue> list = codeService.selectCodeValues(iRequest, condition);
        if (!list.isEmpty())
            copyArray(resultList, list, CodeValue.class);
        return resultList;
    }

    @Override
    public List<CodeValue> getCodeValuesByCodeName(String codeName, boolean includeDisabled) {
        List<CodeValue> resultList = new ArrayList<>();
        IRequest iRequest = RequestHelper.newEmptyRequest();
        iRequest.setLocale("zh_CN");
        List<com.hand.hap.system.dto.CodeValue> codeValues = codeService.selectCodeValuesByCodeName(iRequest, codeName);
        if (!codeValues.isEmpty())
            copyArray(resultList, codeValues, CodeValue.class);
        return resultList;
    }

    @Override
    public CodeValue getCodeValueByCodeNameAndValue(String codeName, String codeValue) {
        List<CodeValue> values = getCodeValuesByCodeName(codeName, false);
        for (CodeValue val : values) {
            if (val.getValue().equals(codeValue))
                return val;
        }
        return null;
    }

    @Override
    public CodeValue getCodeValueByMeaning(String codeName, String meaning) {
        List<CodeValue> values = getCodeValuesByCodeName(codeName, false);
        for (CodeValue val : values) {
            if (val.getMeaning().equals(meaning))
                return val;
        }
        return null;
    }

    @Override
    public CodeValue getCodeValueByCodeIdAndValue(Long codeId, String codeValue) {
        List<CodeValue> values = getCodeValuesByCodeId(codeId, false);
        for (CodeValue val : values) {
            if (val.getValue().equals(codeValue))
                return val;
        }
        return null;
    }

    @Override
    public CodeValue getCodeValueByCodeIdAndMeaning(Long codeId, String meaning) {
        List<CodeValue> values = getCodeValuesByCodeId(codeId, false);
        for (CodeValue val : values) {
            if (val.getMeaning().equals(meaning))
                return val;
        }
        return null;
    }

    private void copyArray(List destList, List<?> sourceList, Class destObjectClass) {
        for (Object o : sourceList) {
            try {
                Object dest = destObjectClass.newInstance();
                BeanUtils.copyProperties(dest, o);
                destList.add(dest);
            } catch (Exception e) {
                logger.error("copyArray error", e);
            }
        }
    }
}
