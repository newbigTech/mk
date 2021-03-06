package com.hand.hmall.mst.dto;

/**
 * Auto Generated By Hap Code Generator
 **/

import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 承运商对象dto
 * @date 2017/7/10 14:37
 */
@ExtensionAttribute(disable = true)
@Table(name = "HMALL_MST_LOGISTICSCO")
public class Logisticsco extends BaseDTO {
    @Id
    @GeneratedValue(generator = GENERATOR_TYPE)
    @Column
    private Long logisticscoId;

    @Column
    private String code;

    @Column
    private String name;


    public void setLogisticscoId(Long logisticscoId) {
        this.logisticscoId = logisticscoId;
    }

    public Long getLogisticscoId() {
        return logisticscoId;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
