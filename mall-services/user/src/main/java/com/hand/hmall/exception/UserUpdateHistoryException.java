package com.hand.hmall.exception;

/**
 * 用户修改数据的历史记录
 * @author tiantao
 *
 */
public class UserUpdateHistoryException extends Exception {
	private static final long serialVersionUID = -9085182411018426767L;
	
	/**
	 * 用户Id，不能为null
	 */
	public static final String ADD_NOT_NULL_USERID="新增一条用户修改数据的历史记录，userId不能为null。";
	
	/**
	 * 黑名单字段不能为null
	 */
	public static final String ADD_NOT_NULL_ISBLACKLIST="新增一条用户修改数据的历史记录，isBlackList不能为null。";
	
	/**
	 * 用户手机号不能为null
	 */
	public static final String ADD_NOT_NULL_CUSTOMERID="新增一条用户修改数据的历史记录，customerid不能为null。";
	
	/**
	 * 用户名
	 */
	public static final String ADD_NOT_NULL_NAME="新增一条用户修改数据的历史记录，name不能为null。";
	
	/**
	 * 用户黑名单的字段必须是“N”或者“Y”
	 */
	public static final String ADD_ISBLACKLIST_FORMAT="黑名单字段的值必须是“N”或者“Y”。";
	
	
	
	
	
	
	
	
	private String message;

	public UserUpdateHistoryException(String message) {
		super(message);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
}
