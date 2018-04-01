package com.hand.hmall.pojo;

/**
 * @author 马君
 * @version 0.1
 * @name PriceRequestData
 * @description 价格计算请求数据
 * @date 2017/8/15 16:15
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
