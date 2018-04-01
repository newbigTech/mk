package com.hand.hmall.process.engine;

import com.hand.hmall.log.dto.LogManager;

public interface IProcessService<T> {

	/**
	 * 设置数据状态
	 * @param status 状态
	 * @param data 数据
	 */
	void setStatus(String status, T data);

	/**
	 * 添加错误日志
	 * @param logManager 日志
     */
	void logError(LogManager logManager);

	/**
	 * 添加错误日志
	 * @param clazz 事务类
	 * @param programDesc 类描述
	 * @param itemId 操作对象
	 * @param returnMessage 返回消息
	 */
	void logError(Class<?> clazz, String programDesc, Long itemId, String returnMessage);

	/**
	 * 添加追踪日志
	 * @param clazz 事务类
	 * @param programDesc 类描述
	 * @param itemId 操作对象
	 * @param returnMessage 返回消息
	 */
	void logTrace(Class<?> clazz, String programDesc, Long itemId, String returnMessage);
}
