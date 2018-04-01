package com.hand.hmall.drools.dto;

import com.hand.common.util.ExcelVOAttribute;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;

/**
 * @author xuxiaoxue
 * @version 0.1
 * @name CouponUserImportDto
 * @description 优惠卷分发导入DTO
 * @date 2017/9/21
 */
@ExtensionAttribute(disable = true)
public class CouponUserImportDto {

    public static final String DEFAULT_SHEET_NAME = "优惠券分发会员导入";
    public static final String DEFAULT_EXCEL_FILE_NAME = DEFAULT_SHEET_NAME + ".xlsx";

    @ExcelVOAttribute(name = "会员手机号", column = "A", isExport = true)
    private String customerId;
    @ExcelVOAttribute(name = "数量", column = "B", isExport = true)
    private Double count;

    private String name;

    private String userGroupName;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserGroupName() {
        return userGroupName;
    }

    public void setUserGroupName(String userGroupName) {
        this.userGroupName = userGroupName;
    }
}
