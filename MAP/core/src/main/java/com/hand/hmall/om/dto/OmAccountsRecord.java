package com.hand.hmall.om.dto;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import javax.persistence.Table;
import com.hand.hap.system.dto.BaseDTO;
import java.util.Date;
@ExtensionAttribute(disable=true)
@Table(name = "HMALL_OM_ACCOUNTS_RECORD")
public class OmAccountsRecord extends BaseDTO {
     @Id
     @GeneratedValue(generator = GENERATOR_TYPE)
     @Column
      private Long recordId;

     @Column
      private String channel;

     @Column
      private Date recordDate;


     public void setRecordId(Long recordId){
         this.recordId = recordId;
     }

     public Long getRecordId(){
         return recordId;
     }

     public void setChannel(String channel){
         this.channel = channel;
     }

     public String getChannel(){
         return channel;
     }

     public void setRecordDate(Date recordDate){
         this.recordDate = recordDate;
     }

     public Date getRecordDate(){
         return recordDate;
     }

     }