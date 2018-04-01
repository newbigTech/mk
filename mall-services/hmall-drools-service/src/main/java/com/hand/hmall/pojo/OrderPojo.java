package com.hand.hmall.pojo;

import com.hand.hmall.util.CalculateUtil;
import org.apache.commons.collections.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 促销执行订单头DTO
 *
 * @Describe d
 * @Author noob
 * @Date 2017/6/28 16:41
 */
public class OrderPojo implements java.io.Serializable, Cloneable {

    private String escOrderCode;
    //订单状态
    private String orderStatus;
    //用户ID
    private String customerId;
    //货币码
    private String currencyCode;
    private String websiteCode;
    private String channelCode;
    private String storeCode;
    //配送方式
    private String shippingType;
    //下单时间
    private String created;
    //修改时间
    private String modified;
    private String receiverName;
    private String countryCode;
    private String stateCode;
    //城市编码
    private String cityCode;
    //街道编码
    private String districtCode;
    private Double paymentAmount;
    //订单应付金额
    private Double orderAmount;
    //订单净额 不包括运费，安装费
    private Double netAmount;
    //促销活动优惠金额
    private Double discountFee;
    //物流运费
    private Double postFee;
    //快递运费
    private Double epostFee;
    //安装费
    private Double fixFee;
    //物流运费减免
    private Double postReduce;
    //快递运费减免
    private Double epostReduce;
    //安装费减免
    private Double fixReduce;
    //优惠券优惠金额
    private Double couponFee;
    //优惠券与促销活动优惠总额
    private Double totalDiscount;
    //是否计算促销结果，Y为根据所选促销信息计算订单金额，N为获取所有可用促销
    private String isCaculate;
    //所选优惠券ID
    private String chosenCoupon;
    //订单重算字段，保存应用在原订单的优惠券
    private String usedCoupon;
    //所选促销Id
    private String chosenPromotion;
    //可用优惠券集合
    private List<?> couponList;
    //订单行集合
    private List<OrderEntryPojo> orderEntryList;
    //可用促销活动集合
    private List<Map> activities;
    //是否执行优惠券
    private boolean checkedCoupon = false;
    //是否执行促销活动
    private boolean checkedActivity = false;
    private String isPay = "N";
    private String distributionId = "";
    private String distribution;
    //是否执行了霸王券
    private boolean isExclusive;
    //订单商品总量
    private Integer quantity ;
    //额外优惠金额(保价金额)
    private Double extraReduce;
    //保价逻辑返回字段
    /**
     * 12.	额外返回字段CURRENT_AMOUNT=ORDER_AMOUNT-各个订单行的RETURNED_QUANTITY*UNIT_FEE-退货行运费安装费
     *注：状态为NORMAL的订单行的“已退货数量”【returned_quantity】/订单行数量【QUANTITY】*该订单行的（运费【SHIPPING_FEE】+安装费【INSTALLATION_FEE】）
     */
    private Double currentAmount;

    private List<Map<String, Object>> cheaperList;

    //    private List<Map<String,?>> gifts;
    public String getDistributionId() {
        return distributionId;
    }

    public void setDistributionId(String distributionId) {
        this.distributionId = distributionId;
    }

    public String getDistribution() {
        return distribution;
    }

    public void setDistribution(String distribution) {
        this.distribution = distribution;
    }

    public String getIsPay() {
        return isPay;
    }

    public void setIsPay(String isPay) {
        this.isPay = isPay;
    }

    public String getEscOrderCode() {
        return escOrderCode;
    }

    public void setEscOrderCode(String escOrderCode) {
        this.escOrderCode = escOrderCode;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getUsedCoupon() {
        return usedCoupon;
    }

    public void setUsedCoupon(String usedCoupon) {
        this.usedCoupon = usedCoupon;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getWebsiteCode() {
        return websiteCode;
    }

    public void setWebsiteCode(String websiteCode) {
        this.websiteCode = websiteCode;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Double getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(Double netAmount) {
        this.netAmount = netAmount;
    }

    public Double getDiscountFee() {
        return discountFee;
    }

    public void setDiscountFee(Double discountFee) {
        this.discountFee = discountFee;
    }

    public Double getPostFee() {
        return postFee;
    }

    public void setPostFee(Double postFee) {
        this.postFee = postFee;
    }

    public Double getEpostFee() {
        return epostFee;
    }

    public void setEpostFee(Double epostFee) {
        this.epostFee = epostFee;
    }

    public Double getFixFee() {
        return fixFee;
    }

    public void setFixFee(Double fixFee) {
        this.fixFee = fixFee;
    }

    public Double getPostReduce() {
        return postReduce;
    }

    public void setPostReduce(Double postReduce) {
        this.postReduce = postReduce;
    }

    public Double getEpostReduce() {
        return epostReduce;
    }

    public void setEpostReduce(Double epostReduce) {
        this.epostReduce = epostReduce;
    }

    public Double getFixReduce() {
        return fixReduce;
    }

    public void setFixReduce(Double fixReduce) {
        this.fixReduce = fixReduce;
    }

    public List<OrderEntryPojo> getOrderEntryList() {
        return orderEntryList;
    }

    public void setOrderEntryList(List<OrderEntryPojo> orderEntryList) {
        this.orderEntryList = orderEntryList;
    }

    public List<Map> getActivities() {
        return activities;
    }

    public void setActivities(List<Map> activities) {
        this.activities = activities;
    }


    public String getChosenCoupon() {
        return chosenCoupon;
    }

    public void setChosenCoupon(String chosenCoupon) {
        this.chosenCoupon = chosenCoupon;
    }

    public String getChosenPromotion() {
        return chosenPromotion;
    }

    public void setChosenPromotion(String chosenPromotion) {
        this.chosenPromotion = chosenPromotion;
    }

    public List<?> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<?> couponList) {
        this.couponList = couponList;
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

    public Double getCouponFee() {
        return couponFee;
    }

    public void setCouponFee(Double couponFee) {
        this.couponFee = couponFee;
    }

    public Double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(Double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }


    public String getIsCaculate() {
        return isCaculate;
    }

    public void setIsCaculate(String isCaculate) {
        this.isCaculate = isCaculate;
    }


    public boolean isExclusive() {
        return isExclusive;
    }

    public void setExclusive(boolean exclusive) {
        isExclusive = exclusive;
    }

    public List<Map<String, Object>> getCheaperList() {
        return cheaperList;
    }

    public void setCheaperList(List<Map<String, Object>> cheaperList) {
        this.cheaperList = cheaperList;
    }

    public Double getExtraReduce() {
        return extraReduce;
    }

    public void setExtraReduce(Double extraReduce) {
        this.extraReduce = extraReduce;
    }

    public Double getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(Double currentAmount) {
        this.currentAmount = currentAmount;
    }

    /**
     * 计算订单总价，净额，商品数量不包括运费安装费
     */
    public void computePrice() {


        //订单行总价
        Double rawTotal = 0.00;
        //订单行总金额
        int totalQuantity = 0;
        for (OrderEntryPojo orderEntryPojo : this.orderEntryList) {
            //订单总额不包括赠品金额
            if ("N".equals(orderEntryPojo.getIsGift())) {
                orderEntryPojo.caculate();
                rawTotal = CalculateUtil.add(rawTotal, orderEntryPojo.getTotalFee());
                totalQuantity += orderEntryPojo.getQuantity();
            }
        }
        //总折扣金额
        this.totalDiscount = CalculateUtil.add(discountFee, couponFee);
        if (totalDiscount > rawTotal) {
            totalDiscount = rawTotal;
        }
        this.netAmount = CalculateUtil.sub(rawTotal, totalDiscount);
        this.orderAmount = rawTotal;
        this.quantity = totalQuantity;
        this.totalDiscount = CalculateUtil.add(this.couponFee, this.totalDiscount);

    }

    /**
     * 初始化订单金额为0
     */
    public void initOrder() {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                if (field.getType().getName().equals("java.lang.Double")) {
                    field.setAccessible(true);
                    field.set(this, 0D);
                }
                if (field.getType().getName().equals("java.lang.Integer") && field.get(this) == null) {
                    field.setAccessible(true);
                    field.set(this, 0);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public OrderPojo clone() throws CloneNotSupportedException {

        OrderPojo order = (OrderPojo) super.clone();

//        //以下添加订单头信息
//        order.setEscOrderCode(this.getEscOrderCode());
//        order.setOrderStatus(this.getOrderStatus());
//        order.setCurrencyCode(this.getCurrencyCode());
//        order.setCustomerId(this.getCustomerId());
//        order.setWebsiteCode(this.getWebsiteCode());
//        order.setChannelCode(this.getChannelCode());
//        order.setStoreCode(this.getStoreCode());
//        order.setShippingType(this.getShippingType());
//        order.setCreated(this.getCreated());
//        order.setModified(this.getModified());
//        order.setReceiverName(this.getReceiverName());
//        order.setCountryCode(this.getCountryCode());
//        order.setStateCode(this.getStateCode());
//        order.setCityCode(this.getCityCode());
//        order.setCouponFee(couponFee);
//        order.setTotalDiscount(totalDiscount);
//        order.setDistrictCode(this.getDistrictCode());
//        order.setDistribution(this.getDistribution());
//        order.setQuantity(this.getQuantity());
//        //付款金额(支付时的金额,不用计算)
//        order.setPaymentAmount(this.getPaymentAmount());
//        //订单净额(去除运费&安装费后的订单应付总金额)
//        order.setNetAmount(this.getNetAmount());
//        //订单金额
//        order.setOrderAmount(this.getOrderAmount());
//        //优惠总金额
//        order.setDiscountFee(this.getDiscountFee());
//        //物流运费(有待计算)
//        order.setPostFee(this.getPostFee());
//        //快递运费(有待计算)
//        order.setEpostFee(this.getEpostFee());
//        //安装费(有待计算)
//        order.setFixFee(this.getFixFee());
//        order.setPostReduce(this.getPostReduce());
//        order.setEpostReduce(this.getEpostReduce());
//        order.setFixReduce(this.getFixReduce());
//        order.setChosenCoupon(this.getChosenCoupon());
//        order.setIsPay(this.getIsPay());
//        order.setIsCaculate(this.getIsCaculate());
//        order.setUsedCoupon(this.getUsedCoupon());
//        order.setIsExclusive(this.getIsExclusive());
        List<OrderEntryPojo> midEntrys = new ArrayList<>();
        for (OrderEntryPojo orderEntryPojo : orderEntryList) {
            OrderEntryPojo midEntry = orderEntryPojo.clone();
            midEntrys.add(midEntry);
        }
        order.setOrderEntryList(midEntrys);
        order.setCheaperList(this.getCheaperList());
        return order;
    }

}
