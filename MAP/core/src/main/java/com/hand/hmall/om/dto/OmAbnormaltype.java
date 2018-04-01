package com.hand.hmall.om.dto;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import javax.persistence.Table;
import com.hand.hap.system.dto.BaseDTO;
@ExtensionAttribute(disable=true)
@Table(name = "HMALL_OM_ABNORMALTYPE")
public class OmAbnormaltype extends BaseDTO {
     @Id
     @GeneratedValue(generator = GENERATOR_TYPE)
     @Column
      private Long abnormaltypeId;

     @Column
      private String abnormalType;

     @Column
      private String description;

     @Column
      private Long approvedtimes;

     @Column
      private String abnormalreason;

     @Column
      private String active;


     public void setAbnormaltypeId(Long abnormaltypeId){
         this.abnormaltypeId = abnormaltypeId;
     }

     public Long getAbnormaltypeId(){
         return abnormaltypeId;
     }

     public void setAbnormalType(String abnormalType){
         this.abnormalType = abnormalType;
     }

     public String getAbnormalType(){
         return abnormalType;
     }

     public void setDescription(String description){
         this.description = description;
     }

     public String getDescription(){
         return description;
     }

     public void setApprovedtimes(Long approvedtimes){
         this.approvedtimes = approvedtimes;
     }

     public Long getApprovedtimes(){
         return approvedtimes;
     }

     public void setAbnormalreason(String abnormalreason){
         this.abnormalreason = abnormalreason;
     }

     public String getAbnormalreason(){
         return abnormalreason;
     }

     public void setActive(String active){
         this.active = active;
     }

     public String getActive(){
         return active;
     }

     }