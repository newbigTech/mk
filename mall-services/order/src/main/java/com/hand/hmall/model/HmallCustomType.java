package com.hand.hmall.model;
import javax.persistence.*;

/**
 * 定制来源表
 * Created by qinzhipeng on 2017/12/11.
 */
@Entity
@Table(name = "HMALL_CUSTOMTYPE")
public class HmallCustomType {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HMALL_CUSTOMTYPE_S.nextval from dual")
    private Long customtypeId;
    /**
     * PIN码
     */
    private String pinCode;
    /**
     * v码
     */
    private String vproductCode;
    /**
     * 定制来源
     */
    private String customType;

    public Long getCustomtypeId() {
        return customtypeId;
    }

    public void setCustomtypeId(Long customtypeId) {
        this.customtypeId = customtypeId;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getVproductCode() {
        return vproductCode;
    }

    public void setVproductCode(String vproductCode) {
        this.vproductCode = vproductCode;
    }

    public String getCustomType() {
        return customType;
    }

    public void setCustomType(String customType) {
        this.customType = customType;
    }
}