package com.hand.hmall.client;
import com.hand.hmall.client.impl.ProductClientServiceImpl;
import com.hand.hmall.dto.ResponseData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * Created by shanks on 2017/2/27.
 * 调用product商品微服务
 */
@FeignClient(value = "product",fallback = ProductClientServiceImpl.class)
public interface IProductClientService {

    /**
     * 未使用
     *
     * @param productId
     * @param productCode
     * @return
     */
    @RequestMapping(value = "/i/product/queryForCart/{productId}/{productCode}")
    public Map<String,Object> queryForCart(@PathVariable(value = "productId") String productId,
                                           @PathVariable(value = "productCode") String productCode);

    /**
     * 未使用
     * @param productId
     * @param productCode
     * @return
     */
    @RequestMapping(value = "/i/product/queryForCoupon/{productId}/{productCode}")
    public Map<String,Object> queryForCoupon(@PathVariable(value = "productId") String productId,
                                             @PathVariable(value = "productCode") String productCode);

    /**
     * 根据商品code查询商品详细信息 未使用
     * @param productCode
     * @return
     */
    @RequestMapping(value = "/i/product/selectProductDetailInfo/{productCode}", method = RequestMethod.GET)
    public List<Map<String,?>> selectProductDetailInfo(@PathVariable("productCode") String productCode);

    /**
     * 根据商品code查询商品详细信息
     * @param productCode
     * @return
     */
    @RequestMapping(value = "/i/product/selectProductByCode/{productCode}", method = RequestMethod.GET)
    public ResponseData selectProductByCode(@PathVariable("productCode") String productCode);

    /**
     * 未使用
     * @param codes
     * @return
     */
    @RequestMapping(value = "/i/product/selectProductByCodes", method = RequestMethod.GET)
    public ResponseData selectProductByCodes(@RequestBody List<String> codes);

    /**
     * 查询排除参数中的商品ID外的所有商品
     * @param productIds
     * @return
     */
    @RequestMapping(value = "/i/product/drools/selectSkuByNotIn",method = RequestMethod.POST)
    public ResponseData selectSkuByNotIn(@RequestBody List<String> productIds);

    /**
     * 未使用
     * @param productIds
     * @return
     */
    @RequestMapping(value = "/i/product/drools/selectSpuByNotIn",method = RequestMethod.POST)
    public ResponseData selectSpuByNotIn(@RequestBody List<String> productIds);

    /**
     * 根据产品Id查询商品详细信息
     * @param productIds
     * @return
     */
    @RequestMapping(value = "/i/product/drools/selectByProductIds",method = RequestMethod.POST)
    public ResponseData selectByProductIds(@RequestBody List<String> productIds);
//
//    @RequestMapping(value="/i/product/drools/getProductIdByStoreCategoryId",method=RequestMethod.POST)
//    public ResponseData getProductIdByStoreCategoryId(@RequestBody List<Map<String, Object>> maps);

    /**
     * 查询排除参数中类别ID之外的所有类别信息
     * @param notIn
     * @return
     */
    @RequestMapping(value="/i/product/drools/getStoreCategoryIdNotEqual",method=RequestMethod.POST)
    public ResponseData getStoreCategoryIdNotEqual(@RequestBody List<String> notIn);

    /**
     * 未使用
     * @param map
     * @return
     */
    @RequestMapping(value="/i/product/drools/category/queryByCondition",method=RequestMethod.POST)
    public ResponseData queryByCondition(@RequestBody Map<String, Object> map);

    /**
     * 根据商品code查询商品分类信息 未使用
     * @param productCode
     * @return
     */
    @RequestMapping(value = "/i/product/drools/category/queryCategroyByProductCode",method = RequestMethod.GET)
    public ResponseData queryCategroyByProductCode(@RequestParam("productCode") String productCode);

    /**
     * 查询商品分类的父级分类
     * @param categoryId
     * @return
     */
    @RequestMapping(value = "/i/product/drools/category/queryParentId",method = RequestMethod.GET)
    public ResponseData queryParentId(@RequestParam("categoryId") Long categoryId);

    /**
     * 未使用
     * @param map
     * @return
     */
    @RequestMapping(value = "/i/picture/getPictures", method = RequestMethod.POST)
    public ResponseData getPictures(@RequestBody Map map);

    /**
     * 根据商品id查询商品信息
     *
     * @param productId
     * @return
     */
    @RequestMapping(value = "/i/product/drools/selectByProductId", method = RequestMethod.POST)
    public ResponseData selectByProductId( String productId);
}
