package com.hand.hmall.pojo;

/**
 * Created by qinzhipeng on 2017/9/6.
 */
public class PriceRequestData {
    // 识别码
    private Long cdkey;

    // v码
    private String vCode;

    // 商品code

    private String productCode;

    // 频道
    private String odtype;

    public Long getCdkey() {
        return cdkey;
    }

    public void setCdkey(Long cdkey) {
        this.cdkey = cdkey;
    }

    public String getvCode() {
        return vCode;
    }

    public void setvCode(String vCode) {
        this.vCode = vCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getOdtype() {
        return odtype;
    }

    public void setOdtype(String odtype) {
        this.odtype = odtype;
    }
}
