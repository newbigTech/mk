package com.hand.hmall.log.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.log.dto.LogManager;

import java.util.List;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name ILogManagerService
 * @description 日志service
 * @date 2017年6月6日16:30:58
 */
public interface ILogManagerService extends IBaseService<LogManager>, ProxySelf<ILogManagerService>{
    /**
     * 开始记录日志
     *
     * @author peng.chen
     * @date 2017年6月6日16:30:06
     * @param iRequest
     * @param logManager
     *            管理对象
     * @return 管理对象
     */
    LogManager logBegin(IRequest iRequest, LogManager logManager);

    /**
     * 结束记录日志
     *
     * @author peng.chen
     * @date 2017年6月6日16:30:06
     * @param iRequest
     * @param logManager
     *            管理对象
     */
    void logEnd(IRequest iRequest, LogManager logManager);

    /**
     * 正常的增加日志
     * @param logManager
     */
    void logNormal(LogManager logManager);

    /**
     * 添加日志
     * @param clazz 所在类
     * @param programDesc 类描述
     * @param itemId 数据主键
     * @param returnMessage 返回消息
     * @param sourcePlatform 平台
     * @param processStatus 状态
     */
    void log(Class<?> clazz, String programDesc, Long itemId, String returnMessage, String sourcePlatform, String processStatus);

    /**
     * 添加错误日志
     * @param clazz 事务类
     * @param programDesc 类描述
     * @param itemId 操作对象
     * @param returnMessage 返回消息
     */
    void logError(Class<?> clazz, String programDesc, Long itemId, String returnMessage);

    /**
     * 添加成功日志
     * @param clazz 事务类
     * @param programDesc 类描述
     * @param itemId 操作对象
     * @param returnMessage 返回消息
     */
    void logSuccess(Class<?> clazz, String programDesc, Long itemId, String returnMessage);

    /**
     * 添加追踪日志
     * @param clazz 事务类
     * @param programDesc 类描述
     * @param itemId 操作对象
     * @param returnMessage 返回消息
     */
    void logTrace(Class<?> clazz, String programDesc, Long itemId, String returnMessage);

    /**
     * 根据查询条件查询数据
     * @param dto
     * @return
     */
    List<LogManager> queryAll(IRequest iRequest,LogManager dto,int page,int pageSize);



}