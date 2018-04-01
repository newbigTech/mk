package com.hand.promotion.pojo.purchase;


/**
 * 查询指定商品的限购信息
 *
 * @author zhuweifeng
 * @date 2017/9/5
 */
public class QueryProductPurchaseInfoPojo implements java.io.Serializable{
	/**
	 * skuCode
	 */
	private String productId;
	/**
	 * 购买数量
	 */
	private Integer number = 0;
	/**
	 * 产品名称
	 */
	private String productName;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
}
