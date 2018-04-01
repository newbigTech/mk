package com.hand.hmall.util;

/**
 * 分页异常
 * @author tiantao
 *
 */
public class PageException extends Exception {	
	/** 序列化Id */
	private static final long serialVersionUID = -671210495100437351L;
	
	public static final String GET_PAGE_ERROR="获取当前页面编号失败！";
	public static final String GET_PAGESIZE_ERROR="获取每页显示数据条数失败！";
	public static final String DATE_FORMAT_ERROR="日期格式不正确";
	public static final String ILLEGAL_LISTOBJECT="该字段必须是List<Object>类型的！";
	
	
	
	private String message;

	public PageException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
	
	
}
