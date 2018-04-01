package com.hand.hmall.model;

import java.math.BigDecimal;

/**
 * Created by shanks on 2017/2/7.
 */
public class Price implements java.io.Serializable,Cloneable{

    //订单总价
    private double total;
    //运费信息
    private double freight;
    //减免价格
    private double discount;
    //最终价格
    private double account;

    private double transfer;

    public double getTransfer() {
        return transfer;
    }

    public void setTransfer(double transfer) {
        this.transfer = transfer;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getFreight() {
        return freight;
    }

    public void setFreight(double freight) {
        this.freight = freight;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getAccount() {
        return account;
    }

    public void setAccount(double account) {
        this.account = account;
    }

    public void compute(){
        if(this.total>=this.discount) {
            this.transfer=this.total-this.discount;
            this.account = this.total - this.discount+this.freight;
            BigDecimal bg = new BigDecimal(this.account);
            this.account = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }else
        {
            this.account=0;
        }
    }

    public Object clone()
    {
        Object o=null;

        try {
            o=(Price)super.clone();//Object 中的clone()识别出你要复制的是哪一个对象。
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return o;
    }

}
