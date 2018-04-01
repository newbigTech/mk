package com.hand.hmall.pojo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name PriceRequest
 * @description 价格计算请求数据
 * @date 2017/7/19 16:02
 */
@XmlRootElement
public class PriceRequest {
    // v码
    private List<String> vCodeList;

    // 价格类型
    private String priceType;

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    @XmlElementWrapper(name = "vCodeList")
    @XmlElement(name = "vCode")
    public List<String> getvCodeList() {
        return vCodeList;
    }

    public void setvCodeList(List<String> vCodeList) {
        this.vCodeList = vCodeList;
    }
}
