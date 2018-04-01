package com.hand.hmall.common;

/**
 * @author 马君
 * @version 0.1
 * @name Constants
 * @description 全局常量
 * @date 2017/6/5 19:44
 */
public interface Constants {
    // 销售状态
    String PRODUCT_SALE_STATUS = "HMALL.PRODUCT.SALE_STATUS";
    // 商品类型
    String PRODUCT_PRODUCT_TYPE = "HMALL.PRODUCT.PRODUCT_TYPE";
    // 商品定制类型
    String PRODUCT_CUSTOM_TYPE = "HMALL.PRODUCT.CUSTOM_TYPE";
    // 产品状态
    String PRODUCT_PRODUCT_STATUS = "HMALL.PRODUCT_STATUS";
    // 风格
    String PRODUCT_STYPE = "HMALL.PRODUCT.STYPE";
    // 新产品设计类型
    String PRODUCT_DESIGN_TYPE = "HMALL.PRODUCT.DESIGN_TYPE";
    // 单位
    String UNIT = "HMALL.UNIT";

    // 采购价格
    String PRICE_TYPE_ORDER_PRICE = "ORDER_PRICE";
    // 销售价格
    String PRICE_TYPE_SALE_PRICE = "SALE_PRICE";

    // 活动价
    String PRICE_GROUP_ACTIVITY_PRICE = "2";
    // 原价
    String PRICE_GROUP_ORIGN_PRICE = "1";
    // 采购价标记
    String ORDER_PRICE_VALUE = "2";
    // 销售价标记
    String SALE_PRICE_VALUE = "1";
    // 物料类型
    String MATERIAL = "Z08";  // 物料类型

    String YES = "Y";
    String NO = "N";

    String SEGMENT_SPLIT_CHAR = "@";

    // 超级订制
    String ODTYPE_SUPER = "2";
    // 普通定制
    String ODTYPE_REGULAR = "1";

    String APPROVAL_STATUS_CHECKED = "CHECKED";

    String CUSTOM_SUPPORT_TYPE_GNO = "6";

    // 通知类型
    String NOTICE_TYPE_PRODUCT_NEW = "PRODUCT_NEW"; // 商品新增
    String NOTICE_TYPE_PRODUCT_CHANGE = "PRODUCT_CHANGE"; // 商品名称变更

    // 通知状态
    public static final String NOTICE_STATUS_PENDDING = "PENDDING";
    public static final String NOTICE_STATUS_PROCCESSED = "PROCCESSED";
}
