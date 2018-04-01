package com.hand.promotion.pojo.order;

import java.util.Collections;
import java.util.List;

/**
 * 订单计算时所需要商品的信息
 * 促销计算，用于保存满足商品、商品分类的条件的商品汇总数据
 *
 * @author zhuweifeng
 * @date 2017/9/5
 */
public class OrderMatchedProductInfoPojo implements java.io.Serializable {
    /**
     * 订单中满足条件的sku集合
     */
    private List<OrderEntryPojo> matchedEntrys;
    /**
     * 订单中满足条件的sku的总数量
     */
    private Integer productTotalQty;
    /**
     * 订单中满足条件的sku的总价
     */
    private Double productTotalPrice;


    /**
     * 不返回null,返回一个空集合
     *
     * @return
     */
    public List<OrderEntryPojo> getMatchedEntrys() {
        if (null == matchedEntrys) {
            return Collections.emptyList();
        }
        return matchedEntrys;
    }

    public void setMatchedEntrys(List<OrderEntryPojo> matchedEntrys) {
        this.matchedEntrys = matchedEntrys;
    }

    public Integer getProductTotalQty() {
        return productTotalQty;
    }

    public void setProductTotalQty(Integer productTotalQty) {
        this.productTotalQty = productTotalQty;
    }

    public Double getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(Double productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }


}
