package com.hand.hmall.model;

import com.hand.hmall.util.DoubleStringUtil;

/**
 * Created by shanks on 2017/2/22.
 */
public class Fees implements  java.io.Serializable {

    private String unitPrice;
    private String discountFee = "0.00";
    private String totalFee;
    private String discountUnitFee;
    private String adjustFee="0.00";

//    private String remainder="0.00";

//    public String getRemainder() {
//        return remainder;
//    }
//
//    public void setRemainder(String remainder) {
//        this.remainder = remainder;
//    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getDiscountFee() {
        return discountFee;
    }

    public void setDiscountFee(String discountFee) {
        this.discountFee = discountFee;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getDiscountUnitFee() {
        return discountUnitFee;
    }

    public void setDiscountUnitFee(String discountUnitFee) {
        this.discountUnitFee = discountUnitFee;
    }

    public String getAdjustFee() {
        return adjustFee;
    }

    public void setAdjustFee(String adjustFee) {
        this.adjustFee = adjustFee;
    }

    public void compute(double originPrice, int quantity){

        double dis = DoubleStringUtil.toDoubleTwoBit(Double.valueOf(discountFee)/quantity);
        //计算单件减免价格
        this.discountUnitFee = DoubleStringUtil.toStringTwoBit(dis);
//        this.remainder=DoubleStringUtil.toStringTwoBit(Double.valueOf(this.discountFee)-(Double.valueOf(this.discountUnitFee)*quantity));
        //计算单件优惠后的价格
        this.unitPrice = DoubleStringUtil.toStringTwoBit(originPrice- dis );
    }
}
