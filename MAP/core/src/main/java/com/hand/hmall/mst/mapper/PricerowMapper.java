package com.hand.hmall.mst.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.mst.dto.Pricerow;
import com.hand.hmall.mst.dto.PricerowDto;
import com.hand.hmall.mst.dto.Product;

import java.util.List;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 价格行对象的Mapper
 * @date 2017/7/10 14:37
 */
public interface PricerowMapper extends Mapper<Pricerow> {

    /**
     * 推送价格行信息至商城
     *
     * @return
     */
    List<PricerowDto> selectPushingPricerow();

    /**
     * @return
     * @description 推送定制商品的价格行信息到M3D
     */
    List<PricerowDto> pushPricerowToM3D();

    /**
     * @param dto
     * @return
     * @description 通过productId来查找是否含有该商品的促销价的价格行信息
     */
    List<PricerowDto> selectDiscountPricerow(PricerowDto dto);

    /**
     * @param dto
     * @description 更新价格行接口推送标志
     */
    void updatePricerowSyncflag(List<PricerowDto> dto);

    /**
     * 价格详情查询
     *
     * @param dto
     * @return
     */
    List<Pricerow> queryInfo(Pricerow dto);

    /**
     * 商品详情信息中 删除价格行关联
     *
     * @param dto
     * @return
     */
    int updatePricerow(Pricerow dto);

    /**
     * 删除商品时，同时删除价格行记录
     *
     * @param dto
     * @return
     */
    int deletePricerowByProductId(Product dto);

    /**
     * 删除商品版本目录订单行
     *
     * @param dto
     * @return
     */
    int deletePricerowByProductAndVersion(Product dto);

    /**
     * 价格维护界面查询功能
     *
     * @param dto
     * @return
     */
    List<Pricerow> selectPricerow(Pricerow dto);

    /**
     * 通过商品ID删除价格行
     *
     * @param dto
     */
    void deleteByProductId(List<Pricerow> dto);

    /**
     * 查询价格行数据
     *
     * @param dto
     * @return
     */
    List<Pricerow> selectAllById(List<Pricerow> dto);

    /**
     * @param dto
     * @return
     * @description 导入价格行时，若是否覆盖原纪录为Y时，则删除与本条记录商品编号、价格类型、框架等级、用户、销售单位相同的价格行
     */
    int coverPricerow(Pricerow dto);

    /**
     * 查询未同步的价格行
     * @param catalogversionId 目录版本
     * @return List<Pricerow>
     */
    List<Pricerow> selectUnsyncPricerows(Long catalogversionId);
}