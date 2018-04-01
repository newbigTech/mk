package com.hand.hmall.model;

import javax.persistence.*;

/**
 * @author 马君
 * @version 0.1
 * @description  商品品牌
 * @date 2017/6/2 16:18
 */
@Entity
@Table(name = "HMALL_MST_BRAND")
public class Brand {
    /*
    * 品牌id
    * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HMALL_MST_BRAND_S.nextval from dual")
    private Long brandId;

    /*
    * 品牌编号
    * */
    private String code;

    /*
    * 品牌名称
    * */
    private String name;

    /*
    * 媒体id
    * */
    private Long mediaId;

    /*
    * 品牌描述
    * */
    private String description;

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMediaId() {
        return mediaId;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
