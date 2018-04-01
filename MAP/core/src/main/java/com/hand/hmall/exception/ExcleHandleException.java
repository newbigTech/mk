package com.hand.hmall.exception;

import com.hand.hap.core.exception.BaseException;

public class ExcleHandleException extends BaseException{
	
	public static final String ERROR_CODE = "WFL_SECURITY_ERROR";
	
    public static final String NEED_List_Not_Empty = "要导出的Excle表格不能为null！";
	
	public ExcleHandleException(String code, String descriptionKey, Object[] parameters) {
        super(code, descriptionKey, parameters);
    }

    public ExcleHandleException(String descriptionKey) {
        this(ERROR_CODE, descriptionKey, null);
    }
}
