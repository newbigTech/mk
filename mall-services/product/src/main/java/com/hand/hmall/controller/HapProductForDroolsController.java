package com.hand.hmall.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.google.gson.Gson;
import com.hand.hmall.dto.*;
import com.hand.hmall.mapper.HmallMstProductMapper;
import com.hand.hmall.mapper.ProductInvInfoMapper;
import com.hand.hmall.service.ICatalogsService;
import com.hand.hmall.service.ICatalogversionService;
import com.hand.hmall.service.IMstSubcategoryService;
import com.hand.hmall.service.IProductService;
import com.hand.hmall.service.impl.CatalogsServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by shanks on 2017/3/21.
 * 用于促销微服务查询相关商品信息
 */
@RestController
@RequestMapping(value = "/i/product/drools", produces = {MediaType.APPLICATION_JSON_VALUE})
public class HapProductForDroolsController {
    @Autowired
    private IProductService iProductService;
    @Resource
    private ProductInvInfoMapper productInvInfoMapper;
    @Autowired
    private IMstSubcategoryService mstSubcategoryService;
    @Autowired
    private ICatalogversionService catalogversionService;

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    /**
     * 用于HAP查询商品信息
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/selectAllProduct", method = RequestMethod.POST)
    public ResponseData selectAllProduct(@RequestBody Map<String, Object> map) {
        return selectGift(map);
    }

    @RequestMapping(value = "/selectByOmsProductId", method = RequestMethod.GET)
    public ResponseData selectByOmsProductId(@RequestParam("productId") String productId) {
        return null;
    }

    @RequestMapping(value = "/queryByProductIds", method = RequestMethod.POST)
    public ResponseData queryByProductIds(@RequestBody Map<String, Object> map) {
        return null;
    }

    /**
     * 查询商品
     *
     * @param productIds
     * @return
     */
    @RequestMapping(value = "/selectSkuByNotIn", method = RequestMethod.POST)
    public ResponseData selectSkuByNotIn(@RequestBody List<String> productIds) {
        return null;
    }

    @RequestMapping(value = "/selectSpuByNotIn", method = RequestMethod.POST)
    public ResponseData selectSpuByNotIn(@RequestBody List<String> productCodes) {
        return null;
    }

    /**
     * 根据商品id集合批量查询商品数据
     *
     * @param productIds
     * @return
     */
    @RequestMapping(value = "/selectByProductIds", method = RequestMethod.POST)
    public ResponseData selectByProductIds(@RequestBody List<String> productIds) {
        ResponseData responseData = new ResponseData();
        List respList = new ArrayList();
        if (CollectionUtils.isNotEmpty(productIds)) {
            for (String productId : productIds) {
                Long id = 0l;
                try {
                    String repId = productId.replaceAll("\"*", "");
                    id = Long.parseLong(repId);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                HmallMstProduct product = iProductService.selectByProductId(id);
                respList.add(product);
            }
        }
        responseData.setResp(respList);
        return responseData;
    }

    /**
     * 根据商品id查询商品数据
     *
     * @param productId
     * @return
     */
    @RequestMapping(value = "/selectByProductId", method = RequestMethod.POST)
    public ResponseData selectByProductId(@RequestBody String productId) {
        ResponseData responseData = new ResponseData();
        List respList = new ArrayList();
        HmallMstProduct product = iProductService.selectByProductId(Long.parseLong(productId));
        respList.add(product);
        responseData.setResp(respList);
        return responseData;
    }


    /**
     * 查询notIn（商品Code集合）参数中的商品详细信息
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/queryByNotInAndCount", method = RequestMethod.POST)
    public ResponseData queryByNotInAndCount(@RequestBody Map<String, Object> map) {
        Gson gson = new Gson();
        logger.info("-----map---{}", gson.toJson(map));
        JSONObject requestParm = JSON.parseObject(JSON.toJSONString(map));
        int page = requestParm.getInteger("page");
        int pageSize = requestParm.getInteger("pageSize");
        JSONArray notIn = requestParm.getJSONArray("notIn");
        List<Map> notInProduct = new ArrayList<>();
        if (notIn != null && notIn.size() > 0) {
            for (int i = 0; i < notIn.size(); i++) {
                JSONObject productParm = (JSONObject) notIn.get(i);
                HmallMstProduct hmallMstProduct = iProductService.selectMKOnlineProductByCode(productParm.getString("productCode"));
                if (hmallMstProduct == null) {
                    ResponseData responseData = new ResponseData();
                    responseData.setMsg("编码为" + productParm.get("productCode").toString() + "产品不存在");
                    responseData.setSuccess(false);
                    return responseData;
                }
//                ProductInvInfo productInvInfo = productInvInfoMapper.selectByProductCode(hmallMstProduct.getCode());
//                if(productInvInfo==null){
//                    ResponseData responseData= new ResponseData();
//                    responseData.setMsg("编码为"+productParm.get("productCode").toString()+"的库存信息不存在");
//                    responseData.setSuccess(false);
//                    return responseData;
//                }
                Map<String, Object> notInMap = new HashMap<>();
                notInMap.put("productCode", hmallMstProduct.getCode());
                notInMap.put("name", hmallMstProduct.getName());
                notInMap.put("countNumber", productParm.getInteger("countNumber"));
                notInMap.put("totalNumber", productParm.getInteger("totalNumber"));
                notInProduct.add(notInMap);
            }
        }
        ResponseData responseData = new ResponseData();
        responseData.setResp(notInProduct);
        return responseData;
    }

    /**
     * hap中查询促销可用赠品
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/selectGift", method = RequestMethod.POST)
    public ResponseData selectGift(@RequestBody Map<String, Object> map) {
        JSONObject requestParm = JSON.parseObject(JSON.toJSONString(map));
        int page = requestParm.getInteger("page");
        int pageSize = requestParm.getInteger("pageSize");
        JSONObject data = requestParm.getJSONObject("data");
        String productCode = data.getString("productCode");
        String name = (String) data.getOrDefault("name", "");
        String productId = (String) data.getOrDefault("productId", "");
        //保存查询返回数据
        List<HmallMstProduct> productList = new ArrayList<>();
        if (StringUtils.isNotEmpty(productCode) || StringUtils.isNotEmpty(name)) {
            List products = iProductService.matchMKOnlineProductByCode(page, pageSize, productCode, name);
            if (CollectionUtils.isNotEmpty(products))
                productList.addAll(products);
        }
        if (StringUtils.isNotEmpty(productId)) {
            HmallMstProduct product = iProductService.selectByProductId(Long.parseLong(productId));
            if (product != null)
                productList.add(product);
        }
        int total = 0;
        if (StringUtils.isNotEmpty(productCode) || StringUtils.isNotEmpty(name) || StringUtils.isNotEmpty(productId)) {
            ResponseData responseData = new ResponseData();
            total = productList.size();
        } else {
            Catalogversion mkOline = catalogversionService.selectMarkorOnline();
            productList = iProductService.selectProductByCatalogId(page, pageSize, mkOline.getCatalogversionId());
            Page<HmallMstProduct> pages = (Page<HmallMstProduct>) productList;
            total = (int) pages.getTotal();
        }

        List<Map> productMaps = new ArrayList<>();
        for (HmallMstProduct product : productList) {
            Map proudctMap = new HashMap();
            proudctMap.put("productCode", product.getCode());
            proudctMap.put("name", product.getName());
            proudctMap.put("productId", product.getProductId());
            productMaps.add(proudctMap);
        }
        ResponseData responseData = new ResponseData();
        responseData.setResp(productMaps);
        responseData.setTotal(total);
        return responseData;
    }

    /**
     * 促销赠品数量校验
     *
     * @param mapList
     * @return
     */
    @PostMapping("/checkedProductCount")
    ResponseData checkedCouponCount(@RequestBody List<Map<String, Object>> mapList) {

        for (Map<String, Object> map : mapList) {
            if ((int) map.get("countNumber") > 0 && (int) map.get("totalNumber") > 0) {
                if ((int) map.get("totalNumber") < (int) map.get("countNumber")) {
                    ResponseData responseData = new ResponseData();
                    responseData.setSuccess(false);
                    responseData.setMsg("总数应大于赠送数");
                    return responseData;
                }
            } else {
                ResponseData responseData = new ResponseData();
                responseData.setSuccess(false);
                responseData.setMsg("请填写大于0的数字");
                return responseData;
            }
        }
        return new ResponseData(mapList);
    }

    /**
     * 根据条件查询商品分类,用于商品类别促销条件查询商品分类
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/category/queryByCondition", method = RequestMethod.POST)
    public ResponseData queryByCondition(@RequestBody Map<String, Object> map) {
        ResponseData responseData = new ResponseData();
        List<CategoryInfo> categoryInfos = mstSubcategoryService.queryForSaleCategory(map);
        responseData.setResp(categoryInfos);
        return responseData;
    }

    /**
     * 根据商品Code查询产品分类
     *
     * @param productCode
     * @return
     */
    @RequestMapping(value = "/category/queryCategroyByProductCode", method = RequestMethod.GET)
    public ResponseData queryCategroyByProductCode(@RequestParam("productCode") String productCode) {
        return null;
    }

    /**
     * 根据产品Id查询商品分类
     *
     * @param productId
     * @return
     */
    @RequestMapping(value = "/category/queryCategroyByProductId", method = RequestMethod.GET)
    public ResponseData queryCategroyByProductId(@RequestParam("productId") String productId) {
        return null;
    }

    /**
     * 查询分类的上层分类Id
     *
     * @param categoryId
     * @return
     */
    @RequestMapping(value = "/category/queryParentId", method = RequestMethod.GET)
    public ResponseData queryParentId(@RequestParam("categoryId") Long categoryId) {

        List<MstSubcategory> mstSubcategories = mstSubcategoryService.queryParentId(categoryId);
        return new ResponseData(mstSubcategories);
    }

}
