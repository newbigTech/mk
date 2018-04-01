package com.hand.hmall.mst.dto;

import com.hand.hap.mybatis.annotation.ExtensionAttribute;

import javax.persistence.Table;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 推送至商城的补件映射对象dto
 * @date 2017/7/10 14:37
 */
@ExtensionAttribute(disable = true)
@Table(name = "HMALL_MST_PATCHLINE_MAPPING")
public class PatchlineMappingDto {

    private Long mappingId;

    private String code;

    private String patchlineCode;

    private String syncflag;

    public Long getMappingId() {
        return mappingId;
    }

    public void setMappingId(Long mappingId) {
        this.mappingId = mappingId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPatchlineCode() {
        return patchlineCode;
    }

    public void setPatchlineCode(String patchlineCode) {
        this.patchlineCode = patchlineCode;
    }

    public void setSyncflag(String syncflag) {
        this.syncflag = syncflag;
    }

    public String getSyncflag() {
        return syncflag;
    }

}
