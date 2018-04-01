package com.hand.hmall.mst.service;
/**
 * @author zhangmeng
 * @version 0.1
 * @name ProductServiceImpl
 * @description 商品列表查询
 * @date 2017/5/26
 */

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.mst.dto.*;

import java.util.List;
import java.util.Map;

public interface IProductService extends IBaseService<Product>, ProxySelf<IProductService> {

    /**
     * @return
     * @description 推送catalogVersion类型的商品信息接口
     */
    public List<ProductDto> selectOnlineProduct();

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
    public void updateProductSyncflag(List<ProductDto> dto);

    /**
     * @param dto      商品实体类
     * @param page
     * @param pageSize
     * @return
     * @description 查询商品列表接口
     */
    public List<Product> selectProductList(Product dto, int page, int pageSize);

    public List<Product> queryInfo(IRequest request, Product dto, int page, int pageSize);

    Product selectByPrimaryKey(Product product, int page, int pageSize);

    /**
     * @param
     * @param page
     * @param pageSize
     * @return
     * @description 查询商品列表接口
     */
    public List<Product> selectProductListByServiceCode(Product dto, int page, int pageSize);

    /**
     * @param dto 商品实体类
     * @return
     * @description 查询商品列表接口
     */
    public List<Product> selectProductList(Product dto);

    /**
     * @param productId 商品ID
     * @return
     * @description 商品失效
     */
    public int unabledProduct(String[] productId);

    /**
     * @param dto      商品实体类
     * @param page
     * @param pageSize
     * @return
     * @description 查询当前商品可选补件
     */
    public List<Product> selectPatchProduct(Product dto, int page, int pageSize);

    /**
     * 删除基础商品和变体商品之间的联系
     *
     * @param dto
     * @return
     */
    public int deleteRelation(Product dto);

    /**
     * 根据基础商品Id查询商品列表
     *
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    List<Product> queryProductList(Long baseProductId, Product dto, int page, int pageSize);

    /**
     * @param dto      商品实体类
     * @param page
     * @param pageSize
     * @return
     * @description 查询当前商品可选套件
     */
    List<Product> selectSuitProduct(Product dto, int page, int pageSize);

    /**
     * 商品详情页面和列表页面中，删除商品以及和商品相关的所有关系
     *
     * @param productList
     * @return
     */
    List<Product> deleteProduct(List<Product> productList);

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
     * 查询记录是否存在
     *
     * @param iRequest
     * @param list
     * @return
     */
    List<Product> selectCount(IRequest iRequest, List<Product> list);

    /**
     * 同步物料到商品主数据
     */
    void syncItemToProduct();

    /**
     * 单条同步s
     *
     * @param iRequest
     * @param dto
     * @return
     */

    List<SyncData> sync(IRequest iRequest, List<SyncData> dto);

    /**
     * 全量同步
     *
     * @param iRequest
     * @param dto
     * @return
     */

    List<SyncData> batchSync(IRequest iRequest, List<SyncData> dto);

    /**
     * 强制同步
     *
     * @param iRequest
     * @param dto
     * @return
     */

    List<SyncData> forceSync(IRequest iRequest, List<SyncData> dto);

    /**
     * 查询可同步的商品
     *
     * @param dto
     * @return
     */
    List<Product> jobData(Product dto);

    /**
     * JOB同步
     *
     * @param iRequest
     * @param list
     */
    List<Product> syncJob(IRequest iRequest, List<Product> list, Long versionId);

    /**
     * 根据map中key value值查询商品
     *
     * @param map
     * @return List<Product>
     */
    List<Product> selectProductByOptionMap(Map<String, Object> map);

    /**
     * 通过商品id查询商品
     *
     * @param productId 商品id
     * @return Product
     */
    Product selectByProductId(Long productId);


    /**
     * 自动上架
     *
     * @param productList
     */
    public String autoUpShelf(List<Product> productList, String odtype);

    /**
     * 自动下架
     *
     * @param productList
     */
    public String autoDownShelf(List<Product> productList, String odtype);

    /**
     * 上架
     *
     * @param productList
     */
    public String upShelf(List<Product> productList, String odtype);

    /**
     * 下架
     *
     * @param productList
     */
    public String downShelf(List<Product> productList, String odtype);

    /**
     * @param iRequest
     * @param list
     * @return
     * @description 导入商品时，验证输入的商品的数据的正确性
     */
    public String checkProduct(IRequest iRequest, List<Product> list);

    /**
     * 通过v码查询商品
     *
     * @param vCode            v码
     * @param catalogversionId 目录版本id
     * @return Product
     */
    Product selectUniqueByVCode(String vCode, Long catalogversionId);

    /**
     * 通过商品编码查询订制品
     *
     * @param code             商品编码
     * @param catalogversionId 目录版本id
     * @return Product
     */
    Product selectCustomByCode(String code, Long catalogversionId);

    /**
     * 查询有图片更新的商品
     *
     * @param catalogversionId 目录版本
     * @param limit            查询条数
     * @return List<Product>
     */
    List<Product> selectProductWithMediaUpdated(Long catalogversionId, int limit);

    /**
     * 推送商品基础关联关系至ZMALL
     * Author: zhangzilong
     *
     * @param dtos
     * @return 接口返回信息
     */
    Map sendProductRelationToZmall(List<ProductDto> dtos) throws Exception;

    /**
     * 根据CODE字段查询版本状态为markor-online的商品
     *
     * @param code
     * @return List<Product>
     */
    List<Product> selectProductByCode(String code);

    /**
     * 通过编码和目录版本查询
     *
     * @param code             商品编码
     * @param catalogversionId 目录版本
     * @return Product
     */
    Product selectUniqueByCode(String code, Long catalogversionId);

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
    List<AtpProductInvInfoExport> selectProcuctInvInfo();

    /**
     * 根据商品CODE和目录版本获得商品ID
     *
     * @param product
     * @return
     */
    Long selectIdByCodeAndVersion(Product product);

    /**
     * 根据商品code查询商品信息
     *
     * @param product
     * @return
     */
    Product selectProductByCodeAndVersion(Product product);

    /**
     * 根据productId设置
     * 设置HMALL_MST_PRODUCT表中的是否定制字段为空
     * */
   /* boolean  updateSethascustomIsNull(Long productId);*/
}