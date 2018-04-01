package com.hand.hmall.model;

/**
 * @author 马君
 * @version 0.1
 * @name FndBusinessLog
 * @description 操作日志
 * @date 2017/6/6 14:25
 */

import javax.persistence.*;
import java.util.Date;

@Table(name = "HMALL_FND_BUSINESS_LOG")
public class FndBusinessLog  {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HMALL_FND_BUSINESS_LOG_S.nextval from dual")
     @Column
      private Long businessLogId;

     @Column
      private Long orderId;

     @Column
      private String operationType;

     @Column
      private Long operationUser;

     @Column
      private Date operationTime;

     @Column
      private String operationContent;

     @Column
      private Long lastVersion;

     @Column
      private Long currentVersion;

     @Transient
     private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setBusinessLogId(Long businessLogId){
         this.businessLogId = businessLogId;
     }

     public Long getBusinessLogId(){
         return businessLogId;
     }

     public void setOrderId(Long orderId){
         this.orderId = orderId;
     }

     public Long getOrderId(){
         return orderId;
     }

     public void setOperationType(String operationType){
         this.operationType = operationType;
     }

     public String getOperationType(){
         return operationType;
     }

     public void setOperationUser(Long operationUser){
         this.operationUser = operationUser;
     }

     public Long getOperationUser(){
         return operationUser;
     }

     public void setOperationTime(Date operationTime){
         this.operationTime = operationTime;
     }

     public Date getOperationTime(){
         return operationTime;
     }

     public void setOperationContent(String operationContent){
         this.operationContent = operationContent;
     }

     public String getOperationContent(){
         return operationContent;
     }

     public void setLastVersion(Long lastVersion){
         this.lastVersion = lastVersion;
     }

     public Long getLastVersion(){
         return lastVersion;
     }

     public void setCurrentVersion(Long currentVersion){
         this.currentVersion = currentVersion;
     }

     public Long getCurrentVersion(){
         return currentVersion;
     }

     }
