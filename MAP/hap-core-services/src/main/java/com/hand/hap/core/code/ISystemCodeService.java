package com.hand.hap.core.code;

import java.util.List;

/**
 * @author zhangwantao
 * @version 0.1
 * @name ISystemCodeService
 * @description HAP框架块码服务
 * @date 2017/12/13
 */
public interface ISystemCodeService {

    /**
     * 根据条件搜索块码
     *
     * @param codeId          -编码ID
     * @param codeName        -编码名称
     * @param includeDisabled -是否包含禁用的
     * @return
     */
    List<Code> findCodes(Long codeId, String codeName, boolean includeDisabled);

    /**
     * 根据快码ID查询值列表
     *
     * @param codeId          -编码ID
     * @param includeDisabled 是否包含禁用的
     * @return
     */
    List<CodeValue> getCodeValuesByCodeId(Long codeId, boolean includeDisabled);

    /**
     * 根据快码ID查询值列表
     *
     * @param codeName        -编码名称
     * @param includeDisabled 是否包含禁用的
     * @return
     */
    List<CodeValue> getCodeValuesByCodeName(String codeName, boolean includeDisabled);

    /**
     * 根据编码名称和值查询
     *
     * @param codeName
     * @param codeValue
     * @return
     */
    CodeValue getCodeValueByCodeNameAndValue(String codeName, String codeValue);

    /**
     * 根据编码名称和含义查询
     *
     * @param codeName
     * @param meaning
     * @return
     */
    CodeValue getCodeValueByMeaning(String codeName, String meaning);

    /**
     * 根据编码ID和值查询
     *
     * @param codeId
     * @param codeValue
     * @return
     */
    CodeValue getCodeValueByCodeIdAndValue(Long codeId, String codeValue);

    /**
     * 根据编码ID和含义查询
     *
     * @param codeId
     * @param meaning
     * @return
     */
    CodeValue getCodeValueByCodeIdAndMeaning(Long codeId, String meaning);
}
