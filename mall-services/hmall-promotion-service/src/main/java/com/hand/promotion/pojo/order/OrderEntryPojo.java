package com.hand.promotion.pojo.order;


import com.hand.promotion.util.DecimalCalculate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Describe 订单行对应实体类
 * @Author noob
 * @Date 2017/6/28 16:43
 */
public class OrderEntryPojo implements java.io.Serializable {

    /**
     *
     */
    private String lineNumber;


    /**
     * 产品编码
     */
    private String product;

    /**
     * 产品id
     */
    private String productId;


    /**
     * 配送方式
     */
    private String shippingType;

    /**
     * 门店编码
     */
    private String pointOfService;

    /**
     * 套件编码
     */
    private String suitCode;


    /**
     * 前台传入商品单价
     */
    private Double basePrice;


    /**
     * 商品真实金额
     */
    private Double realPrice;

    /**
     * 参与促销计算的金额，取自basePrice、realPrice
     */
    private Double calPrice;

    /**
     * 订单行商品数量
     */
    private Integer quantity;

    /**
     * 订单行优惠金额
     */
    private Double discountFee;

    /**
     * 订单头优惠分摊到行的金额
     */
    private Double discountFeel;

    /**
     * 订单行总优惠金额
     */
    private Double totalDiscount;

    /**
     * 优惠后行商品单价
     */
    private Double unitFee;

    /**
     * 订单行应付金额
     */
    private Double totalFee;

    /**
     * 订单头优惠券促销分摊到行上的金额
     */
    private Double couponFee;

    /**
     * 行是否为赠品
     */
    private String isGift = "N";


    /**
     * 运费
     */
    private Double shippingFee;

    /**
     * 安装费
     */
    private Double installationFee;

    /**
     * 运费见面
     */
    private Double preShippingFee;
    /**
     * 安装费减免
     */
    private Double preInstallationFee;

    /**
     * 商品包装体积
     */
    private String productPackageSize;

    /**
     * 商品门店编码
     */
    private String pointOfServiceCode;

    /**
     * 订单行促销信息
     */
    private List<Map> activitie;

    /**
     * 商品行分类ID
     */
    private String categoryId;


    /**
     * 订单行能用于套装计算的次数，用于解决同一个订单行参与多个捆绑促销问题
     */
    private Integer bundleRemainder;


    /**
     * 是否支持定制
     */
    private String customSupportType;


    /**
     * 订单行主线运费数据
     */
    private Double mainCarriage;

    /**
     * 订单行支线运费数据
     */
    private Double subCarriage;

    /**
     * 预计发货时间
     */
    private String estimateDeliveryTime;

    /**
     * 预计到货时间
     */
    private String estimateConTime;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 变体产品编码
     */
    private String vproduct;

    /**
     * 霸王券查询添加查询条件  频道
     */
    private String odtype;

    private String parts;
    //商品包装体积

    /**
     * 退货数量 (保价逻辑传递参数)
     */
    private Integer returnQuantity;


    private String activityId;

    /**
     * 订单行Id
     */
    private String orderEntryId;



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

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    public String getPointOfService() {
        return pointOfService;
    }

    public void setPointOfService(String pointOfService) {
        this.pointOfService = pointOfService;
    }

    public String getSuitCode() {
        return suitCode;
    }

    public void setSuitCode(String suitCode) {
        this.suitCode = suitCode;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public Double getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(Double realPrice) {
        this.realPrice = realPrice;
    }

    public Double getCalPrice() {
        return calPrice;
    }

    public void setCalPrice(Double calPrice) {
        this.calPrice = calPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getDiscountFee() {
        return discountFee;
    }

    public void setDiscountFee(Double discountFee) {
        this.discountFee = discountFee;
    }

    public Double getDiscountFeel() {
        return discountFeel;
    }

    public void setDiscountFeel(Double discountFeel) {
        this.discountFeel = discountFeel;
    }

    public Double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(Double totalDiscount) {
        this.totalDiscount = totalDiscount;
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

    public Double getCouponFee() {
        return couponFee;
    }

    public void setCouponFee(Double couponFee) {
        this.couponFee = couponFee;
    }

    public String getIsGift() {
        return isGift;
    }

    public void setIsGift(String isGift) {
        this.isGift = isGift;
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

    public String getProductPackageSize() {
        return productPackageSize;
    }

    public void setProductPackageSize(String productPackageSize) {
        this.productPackageSize = productPackageSize;
    }

    public List<Map> getActivitie() {
        return activitie;
    }

    public void setActivitie(List<Map> activitie) {
        this.activitie = activitie;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }


    public Integer getBundleRemainder() {
        return bundleRemainder;
    }

    public void setBundleRemainder(Integer bundleRemainder) {
        this.bundleRemainder = bundleRemainder;
    }

    public String getCustomSupportType() {
        return customSupportType;
    }

    public void setCustomSupportType(String customSupportType) {
        this.customSupportType = customSupportType;
    }

    public String getPointOfServiceCode() {
        return pointOfServiceCode;
    }

    public void setPointOfServiceCode(String pointOfServiceCode) {
        this.pointOfServiceCode = pointOfServiceCode;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getVproduct() {
        return vproduct;
    }

    public void setVproduct(String vproduct) {
        this.vproduct = vproduct;
    }

    public String getOdtype() {
        return odtype;
    }

    public void setOdtype(String odtype) {
        this.odtype = odtype;
    }

    public String getParts() {
        return parts;
    }

    public void setParts(String parts) {
        this.parts = parts;
    }

    public Integer getReturnQuantity() {
        return returnQuantity;
    }

    public void setReturnQuantity(Integer returnQuantity) {
        this.returnQuantity = returnQuantity;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getOrderEntryId() {
        return orderEntryId;
    }

    public void setOrderEntryId(String orderEntryId) {
        this.orderEntryId = orderEntryId;
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

    /**
     * 计算订单行应付金额，减去行促销金额
     */
    public void computerPriceForPromot() {
        //订单行总价
        Double entryTotalPrice = DecimalCalculate.mul(this.getQuantity(), this.getCalPrice());
        if (discountFee > entryTotalPrice) {
            discountFee = entryTotalPrice;
        }
        //订单行总折扣金额
        this.totalDiscount = DecimalCalculate.arraySum(this.discountFee, this.discountFeel, this.couponFee);
        if (totalDiscount > entryTotalPrice) {
            totalDiscount = entryTotalPrice;
        }
        totalFee = DecimalCalculate.sub(entryTotalPrice, this.totalDiscount);
    }

    /**
     * 初始化订单行金额为0
     */
    public void initEntry() {
        initEntry(Arrays.asList("basePrice"));
    }

    /**
     * 初始化enCludeFields字段外的Double类型数据为0
     */
    public void initEntry(List<String> enCloudFilds) {
        if (null == enCloudFilds) {
            enCloudFilds = new ArrayList<>();
        }
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (field.getType().getName().equals("java.lang.Double") && !enCloudFilds.contains(field.getName())) {
                    field.set(this, 0D);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public OrderEntryPojo copy() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(this);
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
            return (OrderEntryPojo) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }


}
