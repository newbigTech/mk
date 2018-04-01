/*
 *  Copyright (C) HAND Enterprise Solutions Company Ltd.
 *  All Rights Reserved
 *
 */

package com.hand.hmall.dto;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "HMALL_AS_STATUSLOG")
public class HmallAsStatuslog {

      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HMALL_AS_STATUSLOG_S.nextval from dual")
      private Long statuslogId;

      private Long serviceId;

      private String status;

      private Date changeTime;

     public Long getStatuslogId(){
         return statuslogId;
     }

     public void setStatuslogId(Long statuslogId){
         this.statuslogId = statuslogId;
     }

     public Long getServiceId(){
         return serviceId;
     }

     public void setServiceId(Long serviceId){
         this.serviceId = serviceId;
     }

     public String getStatus(){
         return status;
     }

     public void setStatus(String status){
         this.status = status;
     }

     public Date getChangeTime(){
         return changeTime;
     }

     public void setChangeTime(Date changeTime){
         this.changeTime = changeTime;
     }

     }
