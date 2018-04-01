package com.hand.hmall.drools.controllers;

import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.util.StringUtils;
import com.markor.map.external.fndregionservice.dto.RegionDto;
import com.markor.map.external.fndregionservice.service.IFndRegionsCommonExternalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by shanks on 2017/1/16.
 * 用于查询促销界面商品、商品分类、地区信息
 */
@Controller
@RequestMapping("/sale/range")
public class SaleRangeController {

    private RestTemplate restTemplate = new RestTemplate();
    @Value("#{configProperties['baseUri']}")
    private String baseUri;
    @Value("#{configProperties['modelUri']}")
    private String modelUri;

    @Autowired
    private IFndRegionsCommonExternalService iFndRegionsCommonExternalService;

    /**
     * 分页查询所有可用商品 用于促销商品选择界面
     *
     * @param httpServletRequest
     * @param map
     * @return
     */
    @RequestMapping(value = "/product/selectAllProduct", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData productQuery(HttpServletRequest httpServletRequest, @RequestBody Map<String, Object> map) {
        try {

            String url = "/h/product/drools/selectAllProduct";
            System.out.println("----------url----" + baseUri + modelUri + url);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, null);
            ResponseData responseData = restTemplate.exchange(baseUri + modelUri + url, HttpMethod.POST, entity, ResponseData.class).getBody();
            return responseData;
        } catch (Exception e) {
            System.err.println(e);
        }
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("读取数据错误");
        return responseData;
    }

    /**
     * 根据产品Id 批量查询商品信息
     *
     * @param httpServletRequest
     * @param map
     * @return
     */
    @RequestMapping(value = "/product/queryByProductIds", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData queryByProductIds(HttpServletRequest httpServletRequest, @RequestBody Map<String, Object> map) {
        try {

            String url = "/h/product/drools/queryByProductIds";
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, null);
            ResponseData responseData = restTemplate.exchange(baseUri + modelUri + url, HttpMethod.POST, entity, ResponseData.class).getBody();
            return responseData;
        } catch (Exception e) {
            System.err.println(e);
        }
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("读取数据错误");
        return responseData;
    }

    /**
     * 用于商品换购页面，校验换购商品的数量价格参数，暂未启用
     *
     * @param httpServletRequest
     * @param maps
     * @return
     */
    @RequestMapping(value = "/product/checkedProductPrice", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData checkedProductPrice(HttpServletRequest httpServletRequest, @RequestBody List<Map<String, Object>> maps) {
        ResponseData responseData = new ResponseData();
        for (Map<String, Object> map : maps) {
            if ((int) map.get("countNumber") < 0 || (int) map.get("totalPrice") < 0) {
                responseData.setSuccess(false);
                responseData.setMsg("换购商品总数或价格应大于0");
                return responseData;
            }
        }

        return responseData;
    }

    @RequestMapping(value = "/product/checkedProductCount", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData checkedProductCount(HttpServletRequest httpServletRequest, @RequestBody List<Map<String, Object>> maps) {
        try {
            String url = "/h/product/drools/checkedProductCount";
            HttpEntity<List<Map<String, Object>>> entity = new HttpEntity<>(maps, null);
            ResponseData responseData = restTemplate.exchange(baseUri + modelUri + url, HttpMethod.POST, entity, ResponseData.class).getBody();
            return responseData;
        } catch (Exception e) {
            System.err.println(e);
        }
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("读取数据错误");
        return responseData;
    }

    /**
     * 用于优惠券发放界面， 用户查询
     * 查询不在待发放区的用户信息
     *
     * @param httpServletRequest
     * @param map
     * @return
     */
    @RequestMapping(value = "/user/queryNotEqual", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData userQueryNotEqual(HttpServletRequest httpServletRequest, @RequestBody Map<String, Object> map) {
        try {
            String url = "/h/user/queryNotEqual";
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, null);
            ResponseData responseData = restTemplate.exchange(baseUri + modelUri + url, HttpMethod.POST, entity, ResponseData.class).getBody();
            return responseData;
        } catch (Exception e) {
            System.err.println(e);
        }
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("读取数据错误");
        return responseData;
    }

    /**
     * 优惠券发放界面 根据待发放区userId查询用户信息
     *
     * @param httpServletRequest
     * @param map
     * @return
     */
    @RequestMapping(value = "/user/queryByUserIds", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData queryByUserIds(HttpServletRequest httpServletRequest, @RequestBody Map<String, Object> map) {
        try {
            String url = "/h/user/queryByUserIds";
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, null);
            ResponseData responseData = restTemplate.exchange(baseUri + modelUri + url, HttpMethod.POST, entity, ResponseData.class).getBody();
            return responseData;
        } catch (Exception e) {
            System.err.println(e);
        }
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("读取数据错误");
        return responseData;
    }

    /**
     * 查询产品分类，用于促销商品类别选择界面
     *
     * @param httpServletRequest
     * @param map
     * @return
     */
    @RequestMapping(value = "/category/query", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData areaQuery(HttpServletRequest httpServletRequest, @RequestBody Map<String, Object> map) {
        try {
            //调用微服务 获取产品分类数据
            String url = "/h/product/drools/category/queryByCondition";
            Map<String, Object> data = (Map<String, Object>) map.get("data");

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(data, null);
            ResponseData responseData = restTemplate.exchange(baseUri + modelUri + url, HttpMethod.POST, entity, ResponseData.class).getBody();
            //页面传来的已选分类参数
            List<String> checkedItems = (List<String>) map.get("checkedItems");
            List<Map<String, Object>> list = (List<Map<String, Object>>) responseData.getResp();
            //组合地区数据，根据checkedItems判断分类是否被选中
            for (Map<String, Object> map2 : list) {
                if (StringUtils.isEmpty((String) map2.get("parentCode"))) {
//                    if(checkedItems.size()==0) {
//                        map2.put("ischecked", true);
//                    }
                    map2.put("parentId", null);
                } else {
                    map2.put("parentId", map2.get("parentCode").toString().trim());
                }
                map2.put("Code", map2.get("code"));
                map2.remove("uid");
                map2.remove("code");
                map2.remove("parentCode");
                boolean flag = false;
                if (checkedItems.size() > 0) {
                    for (String checkedItem : checkedItems) {
                        if (checkedItem.equals(map2.get("Code"))) {
                            flag = true;
                            checkedItems.remove(checkedItem);
                            break;
                        }
                    }
                    if (flag) {
                        map2.put("ischecked", true);
                    } else {
                        map2.put("ischecked", false);
                    }
                }
            }
            responseData.setResp(list);

            return responseData;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e);
        }
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("读取数据错误");
        return responseData;
    }

    /**
     * 促销地区范围查询地区
     * type 标识地区层级 1->国家 2->省份 3->城市 4->地区
     *
     * @param httpServletRequest
     * @param mapData
     * @return
     */
    @RequestMapping(value = "/area/query/checked", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData areaQueryChecked(HttpServletRequest httpServletRequest, @RequestBody Map<String, Object> mapData) {
        List<String> checkedItems = (List<String>) mapData.get("checkedItems");
        String disabled = mapData.get("disabled").toString();
        List<RegionDto> countryList = iFndRegionsCommonExternalService.getCountry();
        List<Map<String, Object>> areaTreeViews = new ArrayList<>();
        boolean enabled = (disabled.equals("true")) ? false : true;
        for (RegionDto country : countryList) {
            //添加国家map，以便完成全选功能
            Map<String, Object> mapCountry = new HashMap<>();
            mapCountry.put("id", country.getRegionCode());
            mapCountry.put("type", 1);
            mapCountry.put("parentId", null);
            mapCountry.put("name", country.getRegionName());
            mapCountry.put("expanded", true);
            List<RegionDto> provenceList = iFndRegionsCommonExternalService.getState(country.getRegionCode());
            //存放非国家所有地区
            List<Map<String, Object>> listMap = new ArrayList<>();
            for (RegionDto provence : provenceList) {
                Map map = new HashMap();
                map.put("id", provence.getRegionCode());
                map.put("type", 2);
                map.put("parentId", provence.getParentId());
                map.put("name", provence.getRegionName());
                List<RegionDto> cityList = iFndRegionsCommonExternalService.getCity(provence.getRegionCode());
                List<Map<String, Object>> itemList = new ArrayList<>();
                boolean flag = false;
                for (RegionDto city : cityList) {
                    Map<String, Object> item = new HashMap<>();
                    //判断是否展开
                    item.put("enabled", enabled);
//                    item.put("checked", true);
                    item.put("id", city.getRegionCode());
                    item.put("type", "3");
                    item.put("name", city.getRegionName());
                    if (checkedItems.contains(item.get("id").toString())) {
                        item.put("checked", true);
                        flag = true;
                    }
                    itemList.add(item);
                }
                if (flag) {
                    map.put("expanded", true);
                }
                map.put("items", itemList);
                map.put("enabled", "true");
                listMap.add(map);
            }
            mapCountry.put("items", listMap);
            areaTreeViews.add(mapCountry);
        }

        ResponseData responseData = new ResponseData(areaTreeViews);
        responseData.setSuccess(false);
        return responseData;
    }

    @RequestMapping(value = "/area/store/query/checked", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData areaStoreQueryChecked(HttpServletRequest httpServletRequest, @RequestBody Map<String, Object> map) {
        try {
            String url = "/i/area/queryAreas/store/checked";
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, null);
            ResponseData responseData = restTemplate.exchange(baseUri + modelUri + url, HttpMethod.POST, entity, ResponseData.class).getBody();
            return responseData;
        } catch (Exception e) {
            System.err.println(e);
        }
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("读取数据错误");
        return responseData;
    }

    /**
     * 根据不同条件查询可选促销条件和可选促销结果
     *
     * @param httpServletRequest
     * @param datas
     * @return
     */
    @RequestMapping(value = "/condition/query", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData conditionQuery(HttpServletRequest httpServletRequest, @RequestBody Map<String, Object> datas) {
        try {
            String url = "/h/sale/range/condition/query";
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(datas, null);
            ResponseData responseData = restTemplate.exchange(baseUri + modelUri + url, HttpMethod.POST, entity, ResponseData.class).getBody();
            return responseData;
        } catch (Exception e) {
            System.err.println(e);
        }
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("读取数据错误");
        return responseData;

    }

    @RequestMapping(value = "/condition/submit", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData conditionSubmit(@RequestBody List<Map<String, Object>> maps, HttpServletRequest httpServletRequest) {

        for (Map<String, Object> map : maps) {
//            if(map.get("id")==null||map.get("id").equals(""))
//            {
            map.put("id", UUID.randomUUID());
//            }
        }
        return new ResponseData(maps);
    }

    /**
     * 查询促销分组信息
     *
     * @param httpServletRequest
     * @param type
     * @return
     */
    @RequestMapping(value = "/group/queryByType", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData groupQuery(HttpServletRequest httpServletRequest, @RequestParam("type") String type) {
        try {
            String url = "/h/sale/range/group/queryByType?type=" + type;
            ResponseData responseData = restTemplate.exchange(baseUri + modelUri + url, HttpMethod.GET, null, ResponseData.class).getBody();
            return responseData;
        } catch (Exception e) {
            System.err.println(e);
        }

        ResponseData responseData = new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("读取分组错误");
        return responseData;
    }

    /**
     * 删除促销分组信息
     *
     * @param httpServletRequest
     * @param maps
     * @return
     */
    @RequestMapping(value = "/group/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData groupDelete(HttpServletRequest httpServletRequest, @RequestBody List<Map<String, Object>> maps) {
        try {
            String url = "/h/sale/range/group/delete";
            HttpEntity<List<Map<String, Object>>> entity = new HttpEntity<>(maps, null);
            ResponseData responseData = restTemplate.exchange(baseUri + modelUri + url, HttpMethod.POST, entity, ResponseData.class).getBody();
            return responseData;
        } catch (Exception e) {
            System.err.println(e);
        }

        ResponseData responseData = new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("读取分组错误");
        return responseData;
    }

    /**
     * 根据条件查询促销分组信息
     *
     * @param httpServletRequest
     * @param map
     * @return
     */
    @RequestMapping(value = "/group/queryByConditions", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData queryByConditions(HttpServletRequest httpServletRequest, @RequestBody Map<String, Object> map) {
        try {
            String url = "/h/sale/range/group/queryByConditions";
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, null);
            ResponseData responseData = restTemplate.exchange(baseUri + modelUri + url, HttpMethod.POST, entity, ResponseData.class).getBody();
            return responseData;
        } catch (Exception e) {
            System.err.println(e);
        }

        ResponseData responseData = new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("读取分组错误");
        return responseData;
    }

    /**
     * 新建促销分组
     *
     * @param httpServletRequest
     * @param maps
     * @return
     */
    @RequestMapping(value = "/group/submit", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData groupSubmit(HttpServletRequest httpServletRequest, @RequestBody List<Map<String, Object>> maps) {
        try {
            String url = "/h/sale/range/group/submit";
            HttpEntity<List<Map<String, Object>>> entity = new HttpEntity<>(maps, null);
            ResponseData responseData = restTemplate.exchange(baseUri + modelUri + url, HttpMethod.POST, entity, ResponseData.class).getBody();
            return responseData;
        } catch (Exception e) {
            System.err.println(e);
        }

        ResponseData responseData = new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("读取分组错误");
        return responseData;
    }

    /**
     * 查询notIn（商品Code集合）参数中的商品详细信息 赠品页面调用
     *
     * @param httpServletRequest
     * @param map
     * @return
     */
    @RequestMapping(value = "/product/queryByNotInAndCount", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData queryByNotInAndCount(HttpServletRequest httpServletRequest, @RequestBody Map<String, Object> map) {
        try {
            String url = "/h/product/drools/queryByNotInAndCount";
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, null);
            ResponseData responseData = restTemplate.exchange(baseUri + modelUri + url, HttpMethod.POST, entity, ResponseData.class).getBody();
            return responseData;
        } catch (Exception e) {
            System.err.println(e);
        }

        ResponseData responseData = new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("读取赠品信息错误");
        return responseData;
    }

    /**
     * 查询可用赠品数据
     *
     * @param httpServletRequest
     * @param map
     * @return
     */
    @RequestMapping(value = "/product/selectGift", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData selectGift(HttpServletRequest httpServletRequest, @RequestBody Map<String, Object> map) {
        try {
            String url = "/h/product/drools/selectGift";
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, null);
            ResponseData responseData = restTemplate.exchange(baseUri + modelUri + url, HttpMethod.POST, entity, ResponseData.class).getBody();
            return responseData;
        } catch (Exception e) {
            System.err.println(e);
        }

        ResponseData responseData = new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("读取赠品信息错误");
        return responseData;
    }

    /**
     * todo：：无页面调用
     *
     * @param httpServletRequest
     * @param maps
     * @return
     */
    @RequestMapping(value = "/container/queryByContainers", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData queryByContainers(HttpServletRequest httpServletRequest, @RequestBody List<Map<String, Object>> maps) {
        return new ResponseData(maps);
    }

    /**
     * 促销 容器页面 校验容器数量
     *
     * @param httpServletRequest
     * @param maps
     * @return
     */
    @RequestMapping(value = "/container/submitCountNumber", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData submitCountNumber(HttpServletRequest httpServletRequest, @RequestBody List<Map<String, Object>> maps) {
        for (Map<String, Object> map : maps) {
            if ((int) map.get("countNumber") <= 0) {
                ResponseData responseData = new ResponseData();
                responseData.setMsg("【" + map.get("meaning") + "】数值必须大于0");
                responseData.setSuccess(false);
                return responseData;
            }
        }

        return new ResponseData(maps);
    }


}
