package com.hand.hmall.pojo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name PriceData
 * @description 价格对象
 * @date 2017/7/5 10:29
 */
@XmlRootElement
@XmlType(propOrder = {"mainProduct", "priceType", "partList"})

public class PriceData {
    /*
    * 平台号产品CODE
    * */
    private String mainProduct;

    /*
    * 价格类型
    * */
    private String priceType;

    /*
    * 零部件产品CODE
    * */
    private List<PriceRowData> partList;

    @XmlElement(required = true)
    public String getMainProduct() {
        return mainProduct;
    }

    public void setMainProduct(String mainProduct) {
        this.mainProduct = mainProduct;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    @XmlElementWrapper(name = "priceRowList")
    @XmlElement(name = "priceRow", required = true)
    public List<PriceRowData> getPartList() {
        return partList;
    }

    public void setPartList(List<PriceRowData> partList) {
        this.partList = partList;
    }
}
