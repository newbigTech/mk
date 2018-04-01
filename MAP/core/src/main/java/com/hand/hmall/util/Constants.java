package com.hand.hmall.util;

/**
 * @author xiaoli.yu
 * @Description: 常量类
 * @date 2017年5月19日 上午11:41:58
 **/
public class Constants {

    public final static String JOB_DEFAULT_OPERATOR = "ADMIN";
    public static final String JOB_DEFAULT_LANG = "zh_CN";
    public static final Long JOB_DEFAULT_USERID = Long.valueOf(10001);
    public final static String SOURCE_PLATFORM_JOB = "JOB";
    public final static String SOURCE_PLATFORM_HAMLL = "HMALL";
    public final static String SOURCE_PLATFORM_SYNC = "同步job";
    public final static String SOURCE_PLATFORM_ORDER_JOB = "ORDER";
    public final static String SUCCESS = "SUCCESS";
    public final static String ERROR = "ERROR";
    public final static String CANCEL = "CANCEL";
    public final static String NORMAL = "NORMAL";


    public final static String JOB_STATUS_SUCCESS = "S";
    public final static String JOB_STATUS_ERROR = "E";
    public final static String JOB_STATUS_TRACE = "T";
    /*
     * 字符最大长度常量
     * Created By peng.chen
     */
    public static final int MAX_LENGTH_3000 = 1000;
    public static final int MAX_LENGTH_225 = 225;


    public final static String YES = "Y";
    public final static String NO = "N";
    public final static String X = "X";

    public final static String PROVINCE = "province";
    public final static String CITY = "city";
    public final static String DISTRICT = "district";

    public static final String APPROVAL_STATUS_APPROVED = "APPROVED";
    public static final String APPROVAL_STATUS_CHECKED = "CHECKED";

    public static final String CONSIGMENT_SPLIT = "手工拆单";
    public static final String BATCHUPDATE_STATUS_ADD = "add";
    public static final String BATCHUPDATE_STATUS_UPDATE = "update";


    public static final int DEFAULT_PAGE = 1;
    public static final int DEFAULT_PAGESIZE = 10;


    public static final String CONSIGNMENT_STATUS_ABNORMAL = "ABNORMAL";

    public static final String ZERO = "0";
    public static final String ONE = "1";
    public static final String TWO = "2";
    public static final String THREE = "3";

    public static final String ORDER_PAY_MODE = "HMALL.PAYMENT_TYPE";

    //订单推送retail固定值
    public static final String ORDER_TO_RETAIL_KUNNR = "9520";
    public static final String ORDER_TO_RETAIL_KUNNR2 = "ONE";
    public static final String ORDER_TO_RETAIL_ORG = "0201";
    public static final String ORDER_TO_RETAIL_VTWEG = "20";
    public static final String ORDER_TO_RETAIL_VKBUR = "9520";
    public static final String ORDER_TO_RETAIL_BRAND = "20";
    public static final String ORDER_TO_RETAIL_ORDERTYPE = "S001";
    public static final String ORDER_TO_RETAIL_PAY_STATUS_YES = "10";
    public static final String ORDER_TO_RETAIL_PAY_STATUS_NO = "99";
    public static final String ORDER_TO_RETAIL_PAID_RATE = "1";
    public static final String ORDER_TO_RETAIL_CAN_DELIVERY = "Y";
    public static final String ORDER_TO_RETAIL_SHIPPINGTYPE_PICKUP = "PICKUP";
    public static final String ORDER_TO_RETAIL_SHIPPINGTYPE_LOGISTICS = "LOGISTICS";
    public static final String ORDER_TO_RETAIL_SHIPPINGTYPE_EXPRESS = "EXPRESS";

    public static final String ORDER_TO_RETAIL_SHIPPINGTYPE_P = "P";
    public static final String ORDER_TO_RETAIL_SHIPPINGTYPE_L = "D";

    public static final String ORDER_TO_RETAIL_ORDERENTRY_STATUS_C = "CANCEL";
    public static final String ORDER_TO_RETAIL_ORDERENTRY_STATUS_DE = "DE";

    public static final String ORDER_TO_RETAIL_SPLIT = "X";

    public static final String ORDER_TO_RETAIL_EXSYS = "MAP";

    public static final String ORDER_TO_RETAIL_PRICE_ZD06 = "ZD06";

    public static final String ORDER_TO_RETAIL_PRICE_ZD07 = "ZD07";

    public static final String ORDER_TO_RETAIL_PRICE_ZP00 = "ZP00";

    public static final String ORDER_TO_RETAIL_PRICE_ZP01 = "ZP01";

    public static final String ORDER_TO_RETAIL_PRICE_ZD01 = "ZD01";

    public static final String ORDER_TO_RETAIL_PRICE_RMB = "RMB";


    public static final String ORDER_TO_RETAIL_ENTRY_TYPE_Z003 = "Z003";

    public static final String ORDER_TO_RETAIL_LGORT = "9010";


    //性别
    public static final String USER_SEX_M = "M";
    public static final String USER_SEX_M_ZH = "0002";

    public static final String USER_SEX_F = "F";
    public static final String USER_SEX_F_ZH = "0001";


    public static final String SYNC_JOB_DATE = "2016-01-01 00:00:00";

    //目录版本
    public static final String CATALOG_VERSION = "online";
    public static final String CATALOG = "markor";
    public static final String CATALOG_VERSION_ONLINE = "online";
    public static final String CATALOG_VERSION_STAGED = "staged";
    public static final String CATALOG_VERSION_MARKOR = "markor";
    public static final String CATALOG_VERSION_MASTER = "master";
    /**
     * 订单状态
     */
    public static final String TRADE_CLOSED_BY_UNIQLO = "TRADE_CLOSED_BY_UNIQLO";
    public static final String TRADE_CLOSED = "TRADE_CLOSED";
    public static final String TRADE_FINISHED = "TRADE_FINISHED";
    public static final String WAIT_SELLER_CONFIRM_GOODS = "WAIT_SELLER_CONFIRM_GOODS";
    public static final String WAIT_BUYER_RETURN_GOODS = "WAIT_BUYER_RETURN_GOODS";
    public static final String WAIT_BUYER_TAKE_GOODS = "WAIT_BUYER_TAKE_GOODS";
    public static final String WAIT_SELLER_SEND_GOODS__PICKUP = "WAIT_SELLER_SEND_GOODS__PICKUP";
    public static final String WAIT_BUYER_CONFIRM_GOODS = "WAIT_BUYER_CONFIRM_GOODS";
    public static final String SELLER_CONSIGNED_PART = "SELLER_CONSIGNED_PART";
    public static final String WAIT_SELLER_SEND_GOODS__EXPRESS = "WAIT_SELLER_SEND_GOODS__EXPRESS";
    public static final String WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
    /**
     * 订单状态中文
     */
    public static final String TRADE_CLOSED_BY_UNIQLO_CN = "交易取消";
    public static final String TRADE_CLOSED_CN = "交易关闭";
    public static final String TRADE_FINISHED_CN = "交易成功";
    public static final String WAIT_SELLER_CONFIRM_GOODS_CN = "待卖家收货";
    public static final String WAIT_BUYER_RETURN_GOODS_CN = "待买家寄回商品";
    public static final String WAIT_BUYER_TAKE_GOODS_CN = "待提货";
    public static final String WAIT_SELLER_SEND_GOODS__PICKUP_CN = "已付款";
    public static final String WAIT_BUYER_CONFIRM_GOODS_CN = "已发货";
    public static final String SELLER_CONSIGNED_PART_CN = "部分发货";
    public static final String WAIT_SELLER_SEND_GOODS__EXPRESS_CN = "待发货";
    public static final String WAIT_BUYER_PAY_CN = "待付款";

    // ===================================================================
    // 枚举块码
    // ===================================================================

    // 订单相关
    public static final String ORDER_STATUS_NEW_CREATE = "NEW_CREATE";
    public static final String ORDER_STATUS_PROCESS_ERROR = "PROCESS_ERROR";
    public static final String ORDER_ENTRY_STATUS_NORMAL = "NORMAL";
    public static final String ORDER_ENTRY_STATUS_RETURN = "RETURN";
    public static final String ORDER_ENTRY_STATUS_CANCEL = "CANCEL";
    public static final String ORDER_ENTRY_STATUS_IS_SUIT_Y = "Y";
    public static final String ORDER_IS_LOCKED = "Y";
    public static final String CURRENCY_ID = "CNY";

    // 发货单相关
    public static final String CON_STATUS_NEW_CREATE = "NEW_CREATE";
    public static final String CON_STATUS_PROCESSING = "PROCESSING";
    public static final String CON_STATUS_PROCESS_ERROR = "PROCESS_ERROR";
    public static final String CON_STATUS_SPLIT_CLOSE = "SPLIT_CLOSE";
    public static final String CON_STATUS_WAIT_FOR_DELIVERY = "WAIT_FOR_DELIVERY";
    public static final String LOGISTICS = "LOGISTICS";

    // 商品相关
    public static final String PRODUCT_CUSTOM_TYPE_REGULAR = "A4"; // 常规品定制类型
    public static final String HMALL_HOMEMADE_FACTORY = "HMALL.HOMEMADE_FACTORY";
    public static final String PRODUCT_SUPPLIER_MD10 = "MD10";
    public static final String PRODUCT_SUPPLIER_MK10 = "MK10";
    public static final String PRODUCT_SUPPLIER_MK05 = "MK05";
    public static final String PRODUCT_CUSTOM_CHANNEL_NORMAL = "1"; //普通定制频道
    public static final String PRODUCT_CUSTOM_CHANNEL_SUPER = "2";   //超级定制频道

    // ===================================================================
    // GlobalVariant Code
    // ===================================================================
    public static final String AUTO_BOM_APPROVED = "AUTO_BOM_APPROVED";
    public static final String MEDIA_SYNC_LIMIT = "MEDIA_SYNC_LIMIT";
    public static final String COMMON_SYNC_LIMIT = "COMMON_SYNC_LIMIT";

    // ===================================================================
    // 服务器名称（restclientConfig.json）
    // ===================================================================
    public static final String GENERIC = "generic";
    public static final String MAP = "MAP";
    public static final String ZMALL = "zmall";
    public static final String M3D = "M3D";
    public static final String ECC = "ECC";
    public static final String HMALL = "HMALL";
    public static final String HPAY = "hpay";
    public static final String RRS = "RRS";
    // ===================================================================
    // Client相关
    // ===================================================================
    public static final String MINI_TYPE_JSON = "application/json";

    //服务单 售后单状态
    public static final String FINI = "FINI";
    public static final String PROC = "PROC";
    public static final String NEW = "NEW";

    //售后单类型
    public static final String SERVICE_ORDER = "S001";

    // 多媒体的更新标志
    public static final String UPDATE_FLAG_DEFAULT = "N";
    public static final String UPDATE_FLAG_CREATE = "C";
    public static final String UPDATE_FLAG_UPDATE = "U";
    public static final String UPDATE_FLAG_DEL = "D";

    //MAG-891 主数据、BOM传输接口优化(PLM->MAP) 接口运行超时时间为15分钟 add by zhangyanan 2017-08-07
    public static final int DATA_PROCESS_TIME_OUT = 25;
    public static final int SEQUENCE_PROCESS_TIME_OUT = 1;
    public static final String EMPLOYEE_SYSTEM = "system";

    public static final int SEQUENCE_PROCESS_VCODE_TIME_OUT = 1800;

    // 发货单异常判定原因
    public static final String ABNORMAL_BUYER_MEMO = "BUYER_MEMO";
    public static final String ABNORMAL_ORDER_QUANTITY = "ORDER_QUANTITY";
    public static final String ABNORMAL_EARLIEST_DELIVERY_TIME = "EARLIEST_DELIVERY_TIME";
    public static final String ABNORMAL_ESTIMATE_DELIVERY_TIME = "ESTIMATE_DELIVERY_TIME";
    public static final String ABNORMAL_SWAP_ORDER = "SWAP_ORDER";
    public static final String ABNORMAL_FIRST_TIME = "FIRST_TIME";

    // 网站
    public static final String WEBSITE_TMALL = "TM";

    public static final String WEBSITE_CODE_ZEST = "1";

    public static final String ORDER_TYPE_SWAP = "SWAP";

    // 通知
    public static final String NOTICE_STATUS_PENDDING = "PENDDING";
    public static final String NOTICE_STATUS_PROCCESSED = "PROCCESSED";
    public static final String NOTICE_TYPE_CRAFT_CHECKING = "CRAFT_CHECKING";


    //事后促销相关
    public static final String OM_PROMO_RECODE_STATUS_NEW = "NEW";
    public static final String OM_PROMO_RECODE_STATUS_WAIT_NEW = "WAIT_NEW";
    public static final String OM_PROMO_RECODE_STATUS_FINISH = "FINISH";
    public static final String OM_PROMO_RECODE_STATUS_WAIT_FINI = "WAIT_FINI";
    public static final String OM_PROMO_RECODE_STATUS_CANCEL = "CANCEL";
    public static final String OM_PROMO_RECODE_STATUS_WAIT_CANCEL = "WAIT_CANCEL";
    public static final String OM_PROMO_RECODE_STATUS_ACTIVITY = "ACTIVITY";
    public static final String OM_PROMO_RECODE_STATUS_INACTIVE = "INACTIVE";

    //快码同步相关
    public static final String MQ_TOPIC_SYS_CODE_SYNC = "data_sync";
    public static final String MQ_TAG_PREFIX_SYS_CODE_SYNC = "DATA_SYNC_SYS_CODE.";

}
