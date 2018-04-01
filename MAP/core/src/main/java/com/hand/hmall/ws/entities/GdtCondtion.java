package com.hand.hmall.ws.entities;/**
 * @author peng.chen
 * @version 0.1
 * @name:
 * @Description:
 * @date 2017/6/7 0007 下午 7:48
 */

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import java.util.List;

/**
 * GdtCondtion
 *
 * @author
 * @create 2017-06-07 下午 7:48
 **/
@XmlAccessorType(XmlAccessType.FIELD)
public class GdtCondtion {

    @XmlElements(value = @XmlElement(name = "item"))
    private List<PriceItem> items;

    public List<PriceItem> getItems() {
        return items;
    }

    public void setItems(List<PriceItem> items) {
        this.items = items;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class PriceItem {
        @XmlElement(name = "POSNR")
        private Long lineNumber;

        @XmlElement(name = "KSCHL")
        private String priceType;

        @XmlElement(name = "KBETR")
        private Double price;

        @XmlElement(name = "CURRENCY")
        private String currency;


        public Long getLineNumber() {
            return lineNumber;
        }

        public void setLineNumber(Long lineNumber) {
            this.lineNumber = lineNumber;
        }

        public String getPriceType() {
            return priceType;
        }

        public void setPriceType(String priceType) {
            this.priceType = priceType;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }
    }
}
