package com.hand.hap.cloud.hpay.data;

/**
 * @author jiaxin.jiang
 * @Title com.hand.hap.cloud.hpay.data
 * @Description
 * @date 2017/9/4
 */
public class BillDownloadData {

    /**
     * 账单类型
     */
    private String billType;

    /**
     * 账单时间
     */
    private String billDate;

    /**
     * 支付方式
     */
    private String mode;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }


}
