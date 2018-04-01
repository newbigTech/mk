package com.hand.hmall.log.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.log.dto.LogManager;
import com.hand.hmall.log.mapper.LogManagerMapper;
import com.hand.hmall.log.service.ILogManagerService;
import com.hand.hmall.util.Constants;
import com.hand.hmall.util.DateUtil;
import com.hand.hmall.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Date;
import java.util.List;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name LogManagerServiceImpl
 * @description 日志实现类
 * @date 2017年6月6日16:31:22
 */
@Service(value = "iLogManagerService")
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class, RuntimeException.class})
public class LogManagerServiceImpl extends BaseServiceImpl<LogManager> implements ILogManagerService{

    @Autowired
    LogManagerMapper mapper;

    /**
     * 开始记录日志的方法，将日志对象新增进表格中
     * @param iRequest  请求对象
     * @param logManager    日志管理对象
     * @return
     */
    @Override
    public LogManager logBegin(IRequest iRequest, LogManager logManager) {
        if (logManager == null) {
            return null;
        }

        logManager.setStartTime(DateUtil.getStrToDateTime(DateUtil.getStartTime()));

        String message = logManager.getMessage();

        logManager.setMessage((!StringUtils.isEmpty(message)
                && message.length() > Constants.MAX_LENGTH_3000) ? message.substring(0, Constants.MAX_LENGTH_3000 - 1) : message);

        String returnMessage = logManager.getReturnMessage();

        logManager.setReturnMessage(!StringUtils.isEmpty(returnMessage) &&
                returnMessage.length() > Constants.MAX_LENGTH_3000 ? returnMessage.substring(0, Constants.MAX_LENGTH_3000 - 1) : returnMessage);

        logManager = this.insertSelective(iRequest, logManager);

        return logManager;
    }

    /**
     * 记录日志结束时调用的方法
     * @param iRequest  请求对象
     * @param logManager    日志管理对象
     * @return
     */
    @Override
    public void logEnd(IRequest iRequest, LogManager logManager) {
        if (logManager != null && logManager.getLogId() != null) {

            logManager.setEndTime(DateUtil.getStrToDateTime(DateUtil.getStartTime()));

            String message = logManager.getMessage();

            logManager.setMessage((!StringUtils.isEmpty(message)
                    && message.length() > Constants.MAX_LENGTH_3000) ? message.substring(0, Constants.MAX_LENGTH_3000 - 1) : message);

            String returnMessage = logManager.getReturnMessage();

            logManager.setReturnMessage(!StringUtils.isEmpty(returnMessage) &&
                    returnMessage.length() > Constants.MAX_LENGTH_3000 ? returnMessage.substring(0, Constants.MAX_LENGTH_3000 - 1) : returnMessage);

            this.updateByPrimaryKeySelective(iRequest, logManager);
        }
    }

    /**
     * 向日志表中新增数据，用于记录日志
     * @param logManager    日志管理对象
     */
    @Override
    public void logNormal(LogManager logManager) {
        IRequest iRequest = RequestHelper.newEmptyRequest();
        iRequest.setRoleId(Constants.JOB_DEFAULT_USERID);
        iRequest.setLocale(Constants.JOB_DEFAULT_LANG);
        this.insertSelective(iRequest, logManager);
    }

    /**
     * 添加日志
     * @param clazz 所在类
     * @param programDesc 类描述
     * @param itemId 数据主键
     * @param returnMessage 返回消息
     * @param sourcePlatform 平台
     * @param processStatus 状态
     */
    @Override
    public void log(Class<?> clazz, String programDesc, Long itemId, String returnMessage, String sourcePlatform, String processStatus) {

        IRequest iRequest = RequestHelper.newEmptyRequest();
        iRequest.setRoleId(Constants.JOB_DEFAULT_USERID);
        iRequest.setLocale(Constants.JOB_DEFAULT_LANG);
        LogManager logManager = new LogManager();
        logManager.setDataPrimaryKey(itemId);
        logManager.setProgramName(clazz.toString());
        logManager.setProgramDescription(programDesc);
        logManager.setStartTime(new Date());
        logManager.setEndTime(new Date());
        logManager.setReturnMessage(returnMessage);
        logManager.setProcessStatus(processStatus);
        logManager.setSourcePlatform(sourcePlatform);

        this.insertSelective(iRequest, logManager);
    }

    /**
     * 添加错误日志
     * @param clazz 事务类
     * @param programDesc 类描述
     * @param itemId 操作对象
     * @param returnMessage 返回消息
     */
    @Override
    public void logError(Class<?> clazz, String programDesc, Long itemId, String returnMessage) {
        log(clazz, programDesc, itemId, returnMessage, Constants.SOURCE_PLATFORM_HAMLL, Constants.JOB_STATUS_ERROR);
    }

    /**
     * 添加成功日志
     * @param clazz 事务类
     * @param programDesc 类描述
     * @param itemId 操作对象
     * @param returnMessage 返回消息
     */
    @Override
    public void logSuccess(Class<?> clazz, String programDesc, Long itemId, String returnMessage) {
        log(clazz, programDesc, itemId, returnMessage, Constants.SOURCE_PLATFORM_HAMLL, Constants.JOB_STATUS_SUCCESS);
    }

    /**
     * 添加追踪日志
     * @param clazz 事务类
     * @param programDesc 类描述
     * @param itemId 操作对象
     * @param returnMessage 返回消息
     */
    @Override
    public void logTrace(Class<?> clazz, String programDesc, Long itemId, String returnMessage) {
        log(clazz, programDesc, itemId, returnMessage, Constants.SOURCE_PLATFORM_HAMLL, Constants.JOB_STATUS_TRACE);
    }

    /**
     * 根据界面查询条件查询对应的日志信息
     * @param iRequest      请求对象
     * @param dto       请求参数对象
     * @param page      界面显示数据页数
     * @param pageSize  每页数据显示条数
     * @return  查询出的数据
     */
    @Override
    public List<LogManager> queryAll(IRequest iRequest,LogManager dto,int page,int pageSize) {
        PageHelper.startPage(page,pageSize);
        return mapper.queryAll(dto);
    }
}