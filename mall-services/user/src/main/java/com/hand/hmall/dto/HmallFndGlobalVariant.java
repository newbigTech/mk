/*
 *  Copyright (C) HAND Enterprise Solutions Company Ltd.
 *  All Rights Reserved
 *
 */

package com.hand.hmall.dto;


import javax.persistence.*;

@Entity
@Table(name = "HMALL_FND_GLOBALVARIANT")
public class HmallFndGlobalVariant {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HMALL_FND_GLOBALVARIANT_S.nextval from dual")
      private Long globalvariantId;

      private String code;

      private String value;

      private String description;

     public Long getGlobalvariantId(){
         return globalvariantId;
     }

     public void setGlobalvariantId(Long globalvariantId){
         this.globalvariantId = globalvariantId;
     }

     public String getCode(){
         return code;
     }

     public void setCode(String code){
         this.code = code;
     }

     public String getValue(){
         return value;
     }

     public void setValue(String value){
         this.value = value;
     }

     public String getDescription(){
         return description;
     }

     public void setDescription(String description){
         this.description = description;
     }

}
