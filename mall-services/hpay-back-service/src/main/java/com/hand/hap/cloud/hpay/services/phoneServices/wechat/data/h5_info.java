package com.hand.hap.cloud.hpay.services.phoneServices.wechat.data;

/**
 * @author jiaxin.jiang
 * @Title com.hand.hap.cloud.hpay.services.phoneServices.wechat.data
 * @Description
 * @date 2017/8/7
 */
public class h5_info {

    private String type;
    private String wap_url;
    private String wap_name;

    public h5_info(String type, String wap_url, String wap_name) {
        this.type = type;
        this.wap_url = wap_url;
        this.wap_name = wap_name;
    }

    public h5_info() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWap_url() {
        return wap_url;
    }

    public void setWap_url(String wap_url) {
        this.wap_url = wap_url;
    }

    public String getWap_name() {
        return wap_name;
    }

    public void setWap_name(String wap_name) {
        this.wap_name = wap_name;
    }
}
