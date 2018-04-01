package com.hand.hmall.log.dto;

/**
 * Auto Generated By Hap Code Generator
 **/

import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.*;
import java.util.Date;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name LogManager
 * @description 日志dto，定义了多个构造方法
 * @date 2017年5月26日10:52:23
 */

@ExtensionAttribute(disable = true)
@Table(name = "HMALL_LOG_MANAGER")
public class LogManager extends BaseDTO {

    public LogManager(String programName,
                      String programDescription,
                      String sourcePlatform) {
        this.programName = programName;
        this.programDescription = programDescription;
        this.sourcePlatform = sourcePlatform;
    }

    public LogManager() {

    }

    public LogManager(String programName,
                      String programDescription,
                      String sourcePlatform,
                      String processStatus,
                      String processDescription) {
        this.programName = programName;
        this.programDescription = programDescription;
        this.sourcePlatform = sourcePlatform;
        this.processStatus = processStatus;
        this.processDescription = processDescription;
    }

    @Id
    @GeneratedValue(generator = GENERATOR_TYPE)
    @Column
    private Long logId;

    @Column
    private String programName;

    @Column
    private String programDescription;

    @Column
    private String processStatus;

    @Column
    private String processDescription;

    @Column
    private Date startTime;

    @Column
    private Date endTime;

    @Column
    private String message;

    @Column
    private String returnMessage;

    @Column
    private Long dataPrimaryKey;

    @Column
    private String sourcePlatform;

    @Transient
    private Date creationDate;

    @Transient
    private String dateTo;

    @Transient
    private String dateFrom;

    public Long getDataPrimaryKey() {
        return dataPrimaryKey;
    }

    public void setDataPrimaryKey(Long dataPrimaryKey) {
        this.dataPrimaryKey = dataPrimaryKey;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    @Override
    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public Long getLogId() {
        return logId;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramDescription(String programDescription) {
        this.programDescription = programDescription;
    }

    public String getProgramDescription() {
        return programDescription;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessDescription(String processDescription) {
        this.processDescription = processDescription;
    }

    public String getProcessDescription() {
        return processDescription;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setSourcePlatform(String sourcePlatform) {
        this.sourcePlatform = sourcePlatform;
    }

    public String getSourcePlatform() {
        return sourcePlatform;
    }

}
