package com.hand.promotion.client;

import com.hand.hmall.dto.ResponseData;
import com.hand.promotion.client.impl.ProductClientServiceImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


/**
 * Created by shanks on 2017/2/27.
 * 调用product商品微服务
 */
@FeignClient(value = "product", fallback = ProductClientServiceImpl.class)
public interface IProductClientService {

    /**
     * 未使用
     *
     * @param productId
     * @param productCode
     * @return
     */
    @RequestMapping(value = "/i/product/queryForCart/{productId}/{productCode}")
    Map<String, Object> queryForCart(@PathVariable(value = "productId") String productId,
                                     @PathVariable(value = "productCode") String productCode);

    /**
     * 未使用
     *
     * @param productId
     * @param productCode
     * @return
     */
    @RequestMapping(value = "/i/product/queryForCoupon/{productId}/{productCode}")
    Map<String, Object> queryForCoupon(@PathVariable(value = "productId") String productId,
                                       @PathVariable(value = "productCode") String productCode);

    /**
     * 根据商品code查询商品详细信息 未使用
     *
     * @param productCode
     * @return
     */
    @RequestMapping(value = "/i/product/selectProductDetailInfo/{productCode}", method = RequestMethod.GET)
    List<Map<String, ?>> selectProductDetailInfo(@PathVariable("productCode") String productCode);

    /**
     * 根据商品code查询商品详细信息
     *
     * @param productCode
     * @return
     */
    @RequestMapping(value = "/i/product/selectProductByCode/{productCode}", method = RequestMethod.GET)
    ResponseData selectProductByCode(@PathVariable("productCode") String productCode);

    /**
     * 未使用
     *
     * @param codes
     * @return
     */
    @RequestMapping(value = "/i/product/selectProductByCodes", method = RequestMethod.GET)
    ResponseData selectProductByCodes(@RequestBody List<String> codes);

    /**
     * 查询排除参数中的商品ID外的所有商品
     *
     * @param productIds
     * @return
     */
    @RequestMapping(value = "/i/product/drools/selectSkuByNotIn", method = RequestMethod.POST)
    ResponseData selectSkuByNotIn(@RequestBody List<String> productIds);

    /**
     * 未使用
     *
     * @param productIds
     * @return
     */
    @RequestMapping(value = "/i/product/drools/selectSpuByNotIn", method = RequestMethod.POST)
    ResponseData selectSpuByNotIn(@RequestBody List<String> productIds);

    /**
     * 根据产品Id查询商品详细信息
     *
     * @param productIds
     * @return
     */
    @RequestMapping(value = "/i/product/drools/selectByProductIds", method = RequestMethod.POST)
    ResponseData selectByProductIds(@RequestBody List<String> productIds);


    /**
     * 查询排除参数中类别ID之外的所有类别信息
     *
     * @param notIn
     * @return
     */
    @RequestMapping(value = "/i/product/drools/getStoreCategoryIdNotEqual", method = RequestMethod.POST)
    ResponseData getStoreCategoryIdNotEqual(@RequestBody List<String> notIn);

    /**
     * 未使用
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/i/product/drools/category/queryByCondition", method = RequestMethod.POST)
    ResponseData queryByCondition(@RequestBody Map<String, Object> map);

    /**
     * 根据商品code查询商品分类信息 未使用
     *
     * @param productCode
     * @return
     */
    @RequestMapping(value = "/i/product/drools/category/queryCategroyByProductCode", method = RequestMethod.GET)
    ResponseData queryCategroyByProductCode(@RequestParam("productCode") String productCode);

    /**
     * 查询商品分类的父级分类
     *
     * @param categoryId
     * @return
     */
    @RequestMapping(value = "/i/product/drools/category/queryParentId", method = RequestMethod.GET)
    ResponseData queryParentId(@RequestParam("categoryId") Long categoryId);

    /**
     * 未使用
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/i/picture/getPictures", method = RequestMethod.POST)
    ResponseData getPictures(@RequestBody Map map);

    /**
     * 根据商品id查询商品信息
     *
     * @param productId
     * @return
     */
    @RequestMapping(value = "/i/product/drools/selectByProductId", method = RequestMethod.POST)
    ResponseData selectByProductId(String productId);
}
