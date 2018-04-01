package com.hand.hmall.model;

import javax.persistence.*;

/**
 * @author 马君
 * @version 0.1
 * @name Conrel
 * @description 价格伙伴关系
 * @date 2017/9/12 11:26
 */
@Entity
@Table(name = "HMALL_MST_CONREL")
public class Conrel {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HMALL_MST_CONREL_S.nextval from dual")
    private Long conrelId;

    /**
     * 平台号
     */
    private String platform;

    /**
     * 面料包
     */
    private String fabric;

    /**
     * 零部件包
     */
    private String part;

    public Long getConrelId() {
        return conrelId;
    }

    public void setConrelId(Long conrelId) {
        this.conrelId = conrelId;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getFabric() {
        return fabric;
    }

    public void setFabric(String fabric) {
        this.fabric = fabric;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }
}
