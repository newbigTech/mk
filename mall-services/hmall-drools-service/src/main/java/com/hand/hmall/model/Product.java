package com.hand.hmall.model;

import com.hand.hmall.util.DoubleStringUtil;

import java.util.List;

/**
 * Created by hand on 2016/12/30.
 */
public class Product implements java.io.Serializable{

    private String productId;
    private String productCode;
    private String approval;
    private String activityId;
    private String isGift="N";
    private Integer lineNumber;
    private SummaryInfo summaryInfo;
    private ProductDetailInfo productDetailInfo;
    private List<String> cateList;
    private Integer quantity;
    private Fees fees;
    private String shipType;
    public String getIsGift() {
        return isGift;
    }

    public void setIsGift(String isGift) {
        this.isGift = isGift;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public SummaryInfo getSummaryInfo() {
        return summaryInfo;
    }

    public void setSummaryInfo(SummaryInfo summaryInfo) {
        this.summaryInfo = summaryInfo;
    }

    public ProductDetailInfo getProductDetailInfo() {
        return productDetailInfo;
    }

    public void setProductDetailInfo(ProductDetailInfo productDetailInfo) {
        this.productDetailInfo = productDetailInfo;
    }

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Fees getFees() {
        return fees;
    }

    public void setFees(Fees fees) {
        this.fees = fees;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public List<String> getCateList() {
        return cateList;
    }

    public void setCateList(List<String> cateList) {
        this.cateList = cateList;
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public void compute(){
        if(this.isGift.equals("Y")){
            if (null == fees) {
                this.fees = new Fees();
                //单价为原价
                fees.setUnitPrice("0.00");
                fees.setDiscountUnitFee("0.00");
                fees.setTotalFee("0.00");
            }
        }else {
            if (null == fees) {
                this.fees = new Fees();
                //单价为原价
                fees.setUnitPrice(productDetailInfo.getPrice());
            }
            Double totalFee = Double.valueOf(productDetailInfo.getPrice()) * quantity - Double.valueOf(fees.getDiscountFee());
            this.fees.setTotalFee(DoubleStringUtil.toStringTwoBit(totalFee));
            fees.compute(Double.valueOf(productDetailInfo.getPrice()), quantity);
        }
    }
}
