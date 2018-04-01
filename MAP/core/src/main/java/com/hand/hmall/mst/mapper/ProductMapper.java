package com.hand.hmall.mst.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.mst.dto.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * @author zhangmeng
 * @version 0.1
 * @name ProductServiceImpl
 * @description 商品列表查询
 * @date 2017/5/26
 */
public interface ProductMapper extends Mapper<Product> {
    /**
     * @return
     * @description 推送catalogVersion类型的商品信息接口
     */
    List<ProductDto> selectPushingProduct();

    /**
     * 商品相关块码推送
     *
     * @return
     */
    List<ProductCodeDto> selectProductCode();

    /**
     * @param dto
     * @description 更新商品接口推送标志
     */
    void updateProductSyncflag(List<ProductDto> dto);

    /**
     * 根据订单ID查询其对应的全部商品
     *
     * @param orderId - 订单ID
     * @return
     */
    List<Product> queryProductsByOrderId(@Param("orderId") long orderId);

    /**
     * @param dto 商品实体类
     * @return
     * @description 查询商品列表接口
     */
    List<Product> selectProductList(Product dto);

    /**
     * @param
     * @return
     * @description 查询商品列表接口
     */
    List<Product> selectProductListByServiceCode(Product dto);


    /**
     * @param productId 商品ID
     * @return
     * @description 商品失效
     */
    public int unabledProduct(@Param("productId") String[] productId);

    /**
     * @param dto 商品实体类
     * @return
     * @description 查询当前商品可选补件
     */
    public List<Product> selectPatchProduct(Product dto);

    /**
     * 删除基础商品和变体商品之间的联系
     *
     * @param dto
     * @return
     */
    int deleteRelation(Product dto);

    /**
     * 根据基础商品Id查询商品列表
     *
     * @param baseProductId
     * @return
     */
    List<Product> queryProductList(Long baseProductId);

    /**
     * @param dto 商品实体类
     * @return
     * @description 查询当前商品可选套件
     */
    List<Product> selectSuitProduct(Product dto);

    /**
     * 根据code和版本目录的组合来查询是否有重复的数据
     *
     * @param dto
     * @return
     */
    List<Product> selectProductLov(Product dto);

    /**
     * @param dto
     * @return
     * @description 新增商品和修改商品时确定商品编码和版本目录唯一性
     */
    List<Product> selectByCodeAndCatalogVersion(Product dto);


    /**
     * 通过code和目录版本查询数据是否存在
     *
     * @param dto
     * @return
     */
    int selectCountByCodeAndVersion(Product dto);

    /**
     * 通过目录版本和code查询ID
     *
     * @param dto
     * @return
     */
    Product selectProductByCodeAndVersion(Product dto);

    /**
     * 通过目录版本和code查询ID
     *
     * @param dto
     * @return
     */
    Long selectIdByCodeAndVersion(Product dto);

    /**
     * 根据目录版本查询满足条件的数据
     *
     * @param catalogversion
     * @return
     */
    List<Product> selectBatchSyncProduct(Catalogversion catalogversion);

    List<Product> selectBatchSyncProductCase1(Catalogversion catalogversion);

    List<Product> selectBatchSyncProductCase2(Catalogversion catalogversion);

    List<Product> selectBatchSyncProductCase3(Catalogversion catalogversion);

    /**
     * 查询需要同步的数据
     *
     * @param dto
     * @return
     */
    List<Product> selectJobData(Product dto);

    /**
     * 促销界面lov
     *
     * @return
     */
    List<Product> selectCodeLov();

    /**
     * 根据map中key value值查询商品
     *
     * @param map
     * @return List<Product>
     */
    List<Product> selectProductByOptionMap(@Param("paramMap") Map<String, Object> map);

    /**
     * 根据商品编码和目录版本查询定制品
     *
     * @param code             商品编码
     * @param catalogversionId 目录版本
     * @return Product
     */
    Product selectCustomByCode(@Param("code") String code, @Param("catalogversionId") Long catalogversionId);

    /**
     * 查询有图片更新的商品
     *
     * @param catalogversionId 目录版本
     * @return List<Product>
     */
    List<Product> selectProductWithMediaUpdated(Long catalogversionId);

    /**
     * 查询商品基础关联关系，推送ZMALL
     *
     * @param dtos
     * @return
     */
    List<ProductDto> queryProductRelationForZmall(List<ProductDto> dtos);

    /**
     * d
     * 根据CODE字段查询版本状态为markor-online的商品
     *
     * @param code
     * @return List<Product>
     */
    List<Product> selectProductByCode(@Param("code") String code);

    /**
     * 商品同步时根据ID查询指定同步字段 master--stage
     *
     * @param productId
     * @return
     */
    Product selectStagedSpecifiedById(Long productId);

    /**
     * 根据商品V码&版本号查询商品商品信息（SAP码）
     *
     * @param map
     * @return
     */
    List<Product> getProductByVcode(Map map);

    /**
     * 商品同步时根据ID查询指定同步字段
     *
     * @param productId stage--online
     * @return
     */
    Product selectOnlineSpecifiedById(Long productId);

    /**
     * @return
     * @description 获取图片按钮，筛选满足条件的商品
     */
    List<Product> selectProductForFetchMedia();

    /**
     * 根据code查询商品是否存在
     *
     * @param code
     * @return
     */
    List<Product> selectProByCode(String code);


    List<Product> queryInfo(Product dto);

    /**
     * 查询可单独销售的商品code
     *
     * @return
     */
    List<AtpProductInvInfoExport> selectIsSinSaleCode();

    /**
     * 查询可用库存
     *
     * @return
     */
    List<AtpProductInvInfoExport> selectProcuctInvInfo(AtpProductInvInfoExport atpProductInvInfoExport);

    /**
     * 查询可单独销售的商品在套件表中的套件商品
     *
     * @return
     */
    List<AtpProductInvInfoExport> selectInfoByProductHeadId(AtpProductInvInfoExport atpProductInvInfoExport);

    /**
     * 查询库存表中是否有商品库存
     *
     * @return
     */
    List<AtpProductInvInfoExport> selectProcuctInvInfoByMatnr(AtpProductInvInfoExport atpProductInvInfoExport);

    /**
     * 设置HMALL_MST_PRODUCT表中的是否定制字段为空
     * */
     int  updateSetHascustomIsNull(Long productId);
}