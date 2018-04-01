package com.hand.hmall.service;

import com.hand.hmall.model.Pricerow;
import com.hand.hmall.model.Product;
import com.hand.hmall.model.VCodeLine;
import com.hand.hmall.pojo.PriceData;

import java.util.List;
import java.util.Map;

/**
 * @author 马君
 * @version 0.1
 * @name IPriceCalculateService
 * @description 价格计算Service
 * @date 2017/7/5 11:36
 */
public interface IPriceCalculateService {

    /**
     * 获取商品的最优价格，活动价优于原价，若没有原价返回Null
     * @param pricerowList 价格行
     * @return 价格
     */
    Double getOptimalPrice(List<Pricerow> pricerowList);


    /**
     * 根据v码和价格类型计算价格
     * @param vCode v码
     * @param priceType 价格类型
     * @return Double
     */
    Double calculatePrice(String vCode, String priceType);

    /**
     * 根据v码计算采购价格，不考虑套件关系
     * @param vCode v码
     * @param odType 频道
     * @return Double
     */
    @Deprecated
    Double calculateOrderPrice(String vCode, String odType);

    /**
     * 根据v码计算采购价格，不考虑套件关系
     * @param vCode v码
     * @param odType 频道
     * @return Double
     */
    Double calOrderPrice(String vCode, String odType);

    /**
     * 根据v码销售价格计算，不考虑套件关系
     * @param vCode v码
     * @param odType 频道
     * @return Double
     */
    @Deprecated
    Double calculateSalePrice(String vCode, String odType);

    /**
     * 根据v码销售价格计算，不考虑套件关系
     * 该方法与calOrderPrice逻辑基本一致，区别在于：
     * 1. 采购价需要统计特殊工艺价格
     * 2. 采购价的价格类型为2，销售价的价格类型为1
     * @param vCode v码
     * @param odType 频道
     * @return Double
     */
    Double calSalePrice(String vCode, String odType);

    /**
     * 根据商品编码计算价格
     * @param productCode 商品编码
     * @param priceType 价格类型
     * @return Double
     */
    Double calculatePriceByProductCode(String productCode, String priceType);

    /**
     * 计算可选配包的价格
     * @param vCodeLineMap 零部件映射关系
     * @return Double
     */
    Double calculateOptPackPrice(Map<Product, VCodeLine> vCodeLineMap, String priceType);

    /**
     * 统计零部件价格
     * @param vCodeLineMap 零部件映射关系
     * @return Double
     */
    Double calculatePartPrice(Map<Product, VCodeLine> vCodeLineMap, String priceType);

    /**
     * 计算平台号的特殊工艺价格
     * @param platformProduct 平台商品
     * @param odtype 频道
     * @return Double
     */
    Double calculateSpecialPrice(Product platformProduct, String odtype);

    /**
     * 根据v码构造Product与VCodeLine之前的映射关系
     * @param vCodeLines v码行
     * @return Map<Product, VCodeLine>
     */
    Map<Product, VCodeLine> buildProductVCodeLineMap(List<VCodeLine> vCodeLines);

    /**
     * 构造总的价格计算公式
     * @param totalPrice 总价格
     * @param vCode 传入v码
     * @return String
     */
    String buildPriceFormula(Double totalPrice, String vCode);
}
