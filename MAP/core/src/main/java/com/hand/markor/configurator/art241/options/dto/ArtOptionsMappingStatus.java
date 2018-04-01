package com.hand.markor.configurator.art241.options.dto;

/**
 * @author baihua
 * @version 0.1
 * @name ItemValueOptionsStatus$
 * @description $END$ 选项定义枚举
 * @date 2018/2/1$
 */
public enum ArtOptionsMappingStatus {

    /**
     * 选项定义面料类型
     */
    FN("FN"), FB("FB");
    String status;

    ArtOptionsMappingStatus(String status) {
        this.status = status;
    }
}
