package com.hand.hmall.service;

import com.hand.hmall.model.Pricerow;

import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name IPricerowService
 * @description 价格行Service
 * @date 2017/7/5 11:31
 */
public interface IPricerowService extends IBaseService<Pricerow> {

    /**
     * 根据商品id，框架等级，价格类型查询价格行
     * @param productId 商品id
     * @param productGrade 框架等级
     * @param priceType 价格类型
     * @return 价格行
     */
    List<Pricerow> selectPricerows(Long productId, String productGrade, String priceType);

    /**
     * 根据商品id，框架等级，价格类型，频道查询价格行
     * @param productId 平台商品id
     * @param productGrade 框架等级
     * @param priceType 价格类型
     * @param odtype 频道
     * @return 价格行
     */
    List<Pricerow> selectPricerows(Long productId, String productGrade, String priceType, String odtype);

    /**
     * 根据商品id，价格类型查询价格行
     * @param productId 商品id
     * @param priceType 价格类型
     * @return 价格行
     */
    List<Pricerow> selectPricerows(Long productId, String priceType);

    /**
     * 获取赠品价格
     * @param productId 产品ID
     * @param priceType 价格类型
     * @param groupType 价目类型
     * @return List<Pricerow>
     */
    List<Pricerow> selectGiftPricerows(Long productId, String priceType,String groupType);
}
