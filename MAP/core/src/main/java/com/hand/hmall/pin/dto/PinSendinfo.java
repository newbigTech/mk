package com.hand.hmall.pin.dto;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import javax.persistence.Table;
import com.hand.hap.system.dto.BaseDTO;
@ExtensionAttribute(disable=true)
@Table(name = "HMALL_PIN_SENDINFO")
public class PinSendinfo extends BaseDTO {
     @Id
     @GeneratedValue(generator = GENERATOR_TYPE)
     @Column
      private Long pinSendinfoId;

     @Column
      private String eventCode;

     @Column
      private String eventLevel;

     @Column
      private String employeeId;


     public void setPinSendinfoId(Long pinSendinfoId){
         this.pinSendinfoId = pinSendinfoId;
     }

     public Long getPinSendinfoId(){
         return pinSendinfoId;
     }

     public void setEventCode(String eventCode){
         this.eventCode = eventCode;
     }

     public String getEventCode(){
         return eventCode;
     }

     public void setEventLevel(String eventLevel){
         this.eventLevel = eventLevel;
     }

     public String getEventLevel(){
         return eventLevel;
     }

     public void setEmployeeId(String employeeId){
         this.employeeId = employeeId;
     }

     public String getEmployeeId(){
         return employeeId;
     }

     }
