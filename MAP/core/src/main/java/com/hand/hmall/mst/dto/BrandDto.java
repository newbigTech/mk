package com.hand.hmall.mst.dto;

import com.hand.hap.mybatis.annotation.ExtensionAttribute;

import javax.persistence.Table;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 推送至商城的品牌对象
 * @date 2017/7/10 14:37
 */
@ExtensionAttribute(disable = true)
@Table(name = "HMALL_MST_BRAND")
public class BrandDto {

    private Long brandId;

    private String code;

    private String name;

    private String mediaId;

    private String description;

    private String syncflag;

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
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

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setSyncflag(String syncflag) {
        this.syncflag = syncflag;
    }

    public String getSyncflag() {
        return syncflag;
    }

}
