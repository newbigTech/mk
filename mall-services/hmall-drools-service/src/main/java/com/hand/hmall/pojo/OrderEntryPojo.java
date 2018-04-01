package com.hand.hmall.pojo;

import com.hand.hmall.util.CalculateUtil;
import com.hand.hmall.util.DoubleStringUtil;
import com.hand.hmall.util.NumFormatUtil;
import org.codehaus.plexus.util.PropertyUtils;
import org.springframework.beans.BeanUtils;

import javax.ws.rs.DefaultValue;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * @Describe 订单行对应实体类
 * @Author noob
 * @Date 2017/6/28 16:43
 */
public class OrderEntryPojo implements java.io.Serializable, Cloneable {
    private Long order;
    private String lineNumber;
    private String estimateDeliveryTime;
    private String estimateConTime;
    //产品编码
    private String product;
    private String productId;
    private String productName;
    //变体产品编码
    private String vproduct;
    private String shippingType;
    //霸王券查询添加查询条件  频道
    private String odtype;
    //门店编码
    private String pointOfServiceCode;
    private String suitCode;
    //商品单价
    private Double basePrice;
    //商品实际金额
    private Double prePrice;
    //订单行商品数量
    private Integer quantity;
    //订单行优惠金额
    private Double discountFee;
    //订单头优惠分摊到行的金额
    private Double discountFeel;
    //订单行总优惠金额
    private Double totalDiscount;
    //优惠后行商品单价
    private Double unitFee;
    //订单行应付金额
    private Double totalFee;
    //订单头优惠券促销分摊到行上的金额
    private Double couponFee;
    //行是否为赠品
    private String isGift = "N";
    //运费
    private Double shippingFee;
    //安装费
    private Double installationFee;
    //运费减免
    private Double preShippingFee;
    //安装费减免
    private Double preInstallationFee;
    private String parts;
    //商品包装体积
    private String productPackageSize;
    //退货数量 (保价逻辑传递参数)
    private Integer returnQuantity;
    //订单行促销
    private List<Map> activitie;
    //商品行分类ID
    private String categoryId;

    private String cateList;

    private String activityId;
    //订单行能用于套装计算的次数，用于解决同一个订单行参与多个捆绑促销问题
    private int bundleRemainder;
    //订单行ID
    private String orderEntryId;

    private String customSupportType;
    //主干运费
    private Double mainCarriage;
    //枝干运费
    private Double subCarriage;

    public OrderEntryPojo() {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (field.getType().getName().equals("java.lang.Double") && field.get(this) == null) {
                    field.set(this, 0D);
                }
                if (field.getType().getName().equals("java.lang.Integer") && field.get(this) == null) {
                    field.set(this, 0);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public List<Map> getActivitie() {
        return activitie;
    }

    public void setActivitie(List<Map> activitie) {
        this.activitie = activitie;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductPackageSize() {
        return productPackageSize;
    }

    public void setProductPackageSize(String productPackageSize) {
        this.productPackageSize = productPackageSize;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getEstimateDeliveryTime() {
        return estimateDeliveryTime;
    }

    public void setEstimateDeliveryTime(String estimateDeliveryTime) {
        this.estimateDeliveryTime = estimateDeliveryTime;
    }

    public String getEstimateConTime() {
        return estimateConTime;
    }

    public void setEstimateConTime(String estimateConTime) {
        this.estimateConTime = estimateConTime;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getVproduct() {
        return vproduct;
    }

    public void setVproduct(String vproduct) {
        this.vproduct = vproduct;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    public String getOrderEntryId() {
        return orderEntryId;
    }

    public void setOrderEntryId(String orderEntryId) {
        this.orderEntryId = orderEntryId;
    }

    public String getSuitCode() {
        return suitCode;
    }

    public Integer getReturnQuantity() {
        return returnQuantity;
    }

    public void setReturnQuantity(Integer returnQuantity) {
        this.returnQuantity = returnQuantity;
    }

    public Double getCouponFee() {
        if (couponFee == null) {
            couponFee = 0D;
        }
        return couponFee;
    }

    public void setCouponFee(Double couponFee) {
        this.couponFee = couponFee;
    }

    public void setSuitCode(String suitCode) {
        this.suitCode = suitCode;
    }

    public Double getBasePrice() {
        if (basePrice == null) {
            basePrice = 0D;
        }
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        if (basePrice == null) {
            basePrice = 0.0;
        }
        this.basePrice = basePrice;
    }

    public Integer getQuantity() {
        if (quantity == null) {
            quantity = 0;
        }
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getDiscountFee() {
        if (discountFee == null) {
            discountFee = 0D;
        }
        return discountFee;
    }

    public void setDiscountFee(Double discountFee) {

        this.discountFee = discountFee;
    }

    public Double getDiscountFeel() {
        if (discountFeel == null) {
            discountFeel = 0D;
        }
        return discountFeel;
    }

    public void setDiscountFeel(Double discountFeel) {

        this.discountFeel = discountFeel;
    }

    public Double getUnitFee() {
        return unitFee;
    }

    public void setUnitFee(Double unitFee) {
        this.unitFee = unitFee;
    }

    public Double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Double totalFee) {
        this.totalFee = totalFee;
    }

    public String getIsGift() {
        return isGift;
    }

    public void setIsGift(String isGift) {
        this.isGift = isGift;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public String getPointOfServiceCode() {
        return pointOfServiceCode;
    }

    public void setPointOfServiceCode(String pointOfServiceCode) {
        this.pointOfServiceCode = pointOfServiceCode;
    }

    public void setTotalDiscount(Double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public Double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(Double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public Double getInstallationFee() {
        return installationFee;
    }

    public void setInstallationFee(Double installationFee) {
        this.installationFee = installationFee;
    }

    public Double getPreShippingFee() {
        return preShippingFee;
    }

    public void setPreShippingFee(Double preShippingFee) {
        this.preShippingFee = preShippingFee;
    }

    public Double getPreInstallationFee() {
        return preInstallationFee;
    }

    public void setPreInstallationFee(Double preInstallationFee) {
        this.preInstallationFee = preInstallationFee;
    }

    public String getParts() {
        return parts;
    }

    public void setParts(String parts) {
        this.parts = parts;
    }

    public int getBundleRemainder() {
        return bundleRemainder;
    }

    public void setBundleRemainder(int bundleRemainder) {
        this.bundleRemainder = bundleRemainder;
    }

    public Double getPrePrice() {
        return prePrice;
    }

    public void setPrePrice(Double prePrice) {
        this.prePrice = prePrice;
    }

    public Double getMainCarriage() {
        return mainCarriage;
    }

    public void setMainCarriage(Double mainCarriage) {
        this.mainCarriage = mainCarriage;
    }

    public Double getSubCarriage() {
        return subCarriage;
    }

    public void setSubCarriage(Double subCarriage) {
        this.subCarriage = subCarriage;
    }

    public String getOdtype() {
        return odtype;
    }

    public void setOdtype(String odtype) {
        this.odtype = odtype;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * 计算订单行应付金额，减去行促销金额
     */
    public void caculate() {
        //订单行原价
        Double entryPrice = CalculateUtil.mul(this.getQuantity(), this.getBasePrice(), 2, "UP");
        if (discountFee > entryPrice) {
            discountFee = entryPrice;
        }
        this.totalDiscount = CalculateUtil.add(this.discountFee, this.discountFeel);
        if (totalDiscount > entryPrice) {
            totalDiscount = entryPrice;
        }
        totalFee = CalculateUtil.sub(entryPrice, this.totalDiscount);
    }

    public String getCustomSupportType() {
        return customSupportType;
    }

    public void setCustomSupportType(String customSupportType) {
        this.customSupportType = customSupportType;
    }

    /**
     * 初始化订单行金额为0
     */
    public void initEntry() {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (field.getType().getName().equals("java.lang.Double") && !"basePrice".equals(field.getName())) {
                    field.set(this, 0D);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public OrderEntryPojo clone() throws CloneNotSupportedException {
        OrderEntryPojo entryModel = (OrderEntryPojo) super.clone();
//        entryModel.setOrder(this.getOrder());
//        entryModel.setLineNumber(this.getLineNumber());
//        entryModel.setEstimateDeliveryTime(this.getEstimateDeliveryTime());
//        entryModel.setEstimateConTime(this.getEstimateConTime());
//        entryModel.setProduct(this.getProduct());
//        entryModel.setProductId(this.getProductId());
//        entryModel.setVproduct(this.getVproduct());
//        entryModel.setShippingType(this.getShippingType());
//        entryModel.setPointOfService(this.getPointOfService());
//        entryModel.setSuitCode(this.getSuitCode());
//        entryModel.setBasePrice(this.getBasePrice());
//        entryModel.setQuantity(this.getQuantity());
//        entryModel.setDiscountFee(this.getDiscountFee());
//        entryModel.setDiscountFeel(this.getDiscountFeel());
//        entryModel.setTotalDiscount(this.getTotalDiscount());
//        entryModel.setUnitFee(this.getUnitFee());
//        entryModel.setTotalFee(this.getTotalFee());
//        entryModel.setCouponFee(this.getCouponFee());
//        entryModel.setIsGift(this.getIsGift());
//        entryModel.setShippingFee(this.getShippingFee());
//        entryModel.setInstallationFee(this.getInstallationFee());
//        entryModel.setPreShippingFee(this.getPreShippingFee());
//        entryModel.setPreInstallationFee(this.getPreInstallationFee());
//        entryModel.setParts(this.getParts());
//        entryModel.setProductPackageSize(this.getProductPackageSize());
//        entryModel.setCategoryId(this.getCategoryId());
//        entryModel.setCateList(this.getCateList());
//        entryModel.setBundleRemainder(this.getBundleRemainder());
//        entryModel.setActivitie(this.getActivitie());
//        entryModel.setRealPrice(this.getRealPrice());
        return entryModel;
    }


    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCateList() {
        return cateList;
    }

    public void setCateList(String cateList) {
        this.cateList = cateList;
    }
}
