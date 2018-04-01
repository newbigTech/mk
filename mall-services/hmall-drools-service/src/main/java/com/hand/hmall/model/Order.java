package com.hand.hmall.model;

import com.hand.hmall.util.DoubleStringUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by hand on 2016/12/30.
 */
public class Order implements java.io.Serializable{
    private String couponsId;
    private String isPay="N";
    private String distributionId="";
    private String tempId="";
    private String distribution;
    private String userId;
    private Integer version;
    private List<Product> products;
    private Address address;
    private Price price;
    private Integer quantity;
    private Price activityPrice;
    private Map coupon;
    private List<Map> activities;

    private List<Map> gifts;

    private boolean checkedCoupon=false;
    private boolean checkedActivity=false;


    public String getIsPay() {
        return isPay;
    }

    public void setIsPay(String isPay) {
        this.isPay = isPay;
    }

    public String getCouponsId() {
        return couponsId;
    }

    public void setCouponsId(String couponsId) {
        this.couponsId = couponsId;
    }

    public String getDistributionId() {
        return distributionId;
    }

    public void setDistributionId(String distributionId) {
        this.distributionId = distributionId;
    }

    public String getTempId() {
        return tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
    }

    public Map getCoupon() {
        return coupon;
    }

    public void setCoupon(Map coupon) {
        this.coupon = coupon;
    }

    public String getDistribution() {
        return distribution;
    }

    public void setDistribution(String distribution) {
        this.distribution = distribution;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public boolean isCheckedCoupon() {
        return checkedCoupon;

    }

    public void setCheckedCoupon(boolean checkedCoupon) {
        this.checkedCoupon = checkedCoupon;
    }

    public boolean isCheckedActivity() {
        return checkedActivity;
    }

    public void setCheckedActivity(boolean checkedActivity) {
        this.checkedActivity = checkedActivity;
    }

    public Price getActivityPrice() {
        return activityPrice;
    }

    public void setActivityPrice(Price activityPrice) {
        this.activityPrice = activityPrice;
    }


    public List<Map> getActivities() {
        return activities;
    }

    public void setActivities(List<Map> activities) {
        this.activities = activities;
    }

    public List<Map> getGifts() {
        return gifts;
    }

    public void setGifts(List<Map> gifts) {
        this.gifts = gifts;
    }


    public void computePrice(){
        Double total = 0.00;
        Double discount = 0.00;
        int quantity = 0;
        for (Product product : this.products) {
            product.compute();
            if(product.getIsGift().equals("N")){
                total += Double.valueOf(product.getProductDetailInfo().getPrice()) * product.getQuantity();
                discount += Double.valueOf(product.getFees().getDiscountFee());
                quantity += product.getQuantity();
            }
        }
        this.price.setTotal(total);
        this.quantity = quantity;
        this.price.compute();
        // 订单层面的优惠 = 订单总优惠 - 各订单行的优惠
        Double orderDiscount = price.getDiscount() - discount;
        if (orderDiscount>0) {
            Double discountSum =0.00;
            for (int i=0; i<products.size();i++) {
                if(this.products.get(i).getIsGift().equals("N")) {
                    Double orderRowTotal = products.get(i).getQuantity() * Double.valueOf(products.get(i).getProductDetailInfo().getPrice());
                    Double changeTotal = DoubleStringUtil.toDoubleTwoBit(orderRowTotal / total);
                    Double orderRowDiscount;
                    if (i != products.size() - 1) {
                        orderRowDiscount = DoubleStringUtil.toDoubleTwoBit(orderDiscount * changeTotal);
                        discountSum += orderRowDiscount;
                    } else {
                        if (products.size() != 1) {
                            orderRowDiscount = orderDiscount - discountSum;
                        } else {
                            orderRowDiscount = orderDiscount;
                        }
                    }
                    Double discountFees = Double.valueOf(products.get(i).getFees().getDiscountFee()) + orderRowDiscount;
                    products.get(i).getFees().setDiscountFee(DoubleStringUtil.toStringTwoBit(discountFees));
                    products.get(i).compute();
                }
            }
        }
    }

    //计算订单行的总优惠
    public void computeOrderRowDiscount(){
        Double rowTotalDiscount = 0.00;
        for (Product product : this.products){
            rowTotalDiscount += Double.valueOf(product.getFees().getDiscountFee());
        }
        this.price.setDiscount(rowTotalDiscount+this.price.getDiscount());
    }

}