package com.hand.hmall.om.dto;


import com.hand.common.util.ExcelVOAttribute;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.Table;

/**
 * @author liuhongxi
 * @version 0.1
 * @name 天猫发货单导出历史dto
 * @description 用于导出excel用
 * @date 2017/5/24
 */
@ExtensionAttribute(disable = true)
@Table(name = "HMALL_TM_CONSIGNMENT_HIS")
public class HmallTmConsignmentHis extends BaseDTO {

    // 主键
    private Long hisId;

    // 导出日期
    private String exportTime;

    public Long getHisId() {
        return hisId;
    }

    public void setHisId(Long hisId) {
        this.hisId = hisId;
    }

    public String getExportTime() {
        return exportTime;
    }

    public void setExportTime(String exportTime) {
        this.exportTime = exportTime;
    }
}