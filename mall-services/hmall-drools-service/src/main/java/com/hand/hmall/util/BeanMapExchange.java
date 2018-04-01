package com.hand.hmall.util;

import com.hand.hmall.model.*;
import com.hand.hmall.temp.DefinitionTemp;
import com.hand.hmall.temp.RuleInputTemp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanMap;

import java.util.*;

/**
 * 将map转换成对应的DTO
 * Created by shanks on 2017/1/10.
 */
public class BeanMapExchange {
    private static Logger logger = LoggerFactory.getLogger("BeanMapExchange");

    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = new HashMap<String,Object>();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(key+"", beanMap.get(key));
            }
        }
        return map;
    }

    /**
     * 将map转换为javabean对象
     * @param map
     * @param bean
     * @return
     */
    public static <T> T mapToBean(Map<String, Object> map,T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }

    /**
     * 将List<T>转换为List<Map<String, Object>>
     * @param objList
     * @return

     */
    public static <T> List<Map<String, Object>> objectsToMaps(List<T> objList) {
        List<Map<String, Object>> list = new ArrayList<>();
        if (objList != null && objList.size() > 0) {
            Map<String, Object> map = null;
            T bean = null;
            for (int i = 0,size = objList.size(); i < size; i++) {
                bean = objList.get(i);
                map = beanToMap(bean);
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 将List<Map<String,Object>>转换为List<T>
     * @param maps
     * @param clazz
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static <T> List<T> mapsToObjects(List<Map<String, Object>> maps,Class<T> clazz) throws InstantiationException, IllegalAccessException {
        List<T> list = new ArrayList<>();
        if (maps != null && maps.size() > 0) {
            Map<String, Object> map = null;
            T bean = null;
            for (int i = 0,size = maps.size(); i < size; i++) {
                map = maps.get(i);
                bean = clazz.newInstance();
                mapToBean(map, bean);
                list.add(bean);
            }
        }
        return list;
    }

    public static void exchangeConditionAction(Map<String,Object> map, RuleInputTemp ruleInputTemp)
    {
        //将条件condition转换为对应dto
        List<Map<String,Object>> conditions= (List<Map<String, Object>>) map.get("conditions");
        if(conditions!=null){
            List<DefinitionTemp> definitionTemps = new ArrayList<>();
            //condition不为空，转成DefinitionTemp;为空，设置条件为订单满0元，即对所有订单都适用
            if(!conditions.isEmpty()) {
                try {
                    definitionTemps = BeanMapExchange.mapsToObjects(conditions, DefinitionTemp.class);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }else{
                DefinitionTemp definitionTemp=new DefinitionTemp();
                definitionTemp.setDefinitionId("o_total_reached");
                definitionTemp.setId(UUID.randomUUID().toString());
                definitionTemp.setMeaning("订单减X元");
                Map<String,Map> parameters=new HashMap<>();
                Map<String,Object> operator=new HashMap<>();
                Map<String,Object> value=new HashMap<>();
                operator.put("value","GEATER_THAN_OR_EQUAL");
                value.put("value",0);
                parameters.put("operator",operator);
                parameters.put("value",value);
                definitionTemp.setParameters(parameters);

                definitionTemps.add(definitionTemp);
            }
            ruleInputTemp.setConditions(definitionTemps);
        }else{
            List<DefinitionTemp> definitionTemps=new ArrayList<>();

            DefinitionTemp definitionTemp=new DefinitionTemp();
            definitionTemp.setDefinitionId("o_total_reached");
            definitionTemp.setId(UUID.randomUUID().toString());
            definitionTemp.setMeaning("订单满X元");
            Map<String,Map> parameters=new HashMap<>();
            Map<String,Object> operator=new HashMap<>();
            Map<String,Object> value=new HashMap<>();
            operator.put("value","GEATER_THAN_OR_EQUAL");
            value.put("value",0);
            parameters.put("operator",operator);
            parameters.put("value",value);
            definitionTemp.setParameters(parameters);

            definitionTemps.add(definitionTemp);
            ruleInputTemp.setConditions(definitionTemps);

        }

        if (map.get("containers")!=null){
            ruleInputTemp.setContainers((List<Map>)map.get("containers"));
        }
        if (map.get("groups")!=null){
            ruleInputTemp.setGroups((List<Map>)map.get("groups"));
        }


        if(map.get("actions")!=null)
        {
            List<DefinitionTemp> definitionTemps=new ArrayList<>();
            try {
                definitionTemps= BeanMapExchange.mapsToObjects((List<Map<String, Object>>) map.get("actions"), DefinitionTemp.class);
            } catch (InstantiationException e) {
                logger.error("促销ActionMap转DTO异常",e);
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                logger.error("促销ActionMap转DTO异常",e);
                e.printStackTrace();
            }
            ruleInputTemp.setActions(definitionTemps);
        }
    }

    public static Order exchangeOrder(Map<String,Object> map)
    {
        Order order=new Order();
        List<Product> products=new ArrayList<>();
        Address address=new Address();
        Price price=new Price();

        order.setDistributionId((map.get("distributionId")!=null)?map.get("distributionId").toString():"");
        order.setTempId((map.get("tempId")!=null)?map.get("tempId").toString():"");
        order.setDistribution((map.get("distribution")!=null)?map.get("distribution").toString():"");
        order.setUserId((map.get("userId")!=null)?map.get("userId").toString():"");
        order.setCouponsId((map.get("couponsId")==null)?null:map.get("couponsId").toString());

        if(map.get("price")!=null)
        {
            Map<String,Object> priceMap= (Map<String, Object>) map.get("price");
            mapToBean(priceMap,price);
        }
        order.setPrice(price);


        if(map.get("address")!=null)
        {
            Map<String,Object> addressMap= (Map<String, Object>) map.get("address");
            mapToBean(addressMap,address);
        }
        order.setAddress(address);

        if(map.get("products")!=null)
        {
            List<Map<String,Object>> productMapList= (List<Map<String, Object>>) map.get("products");

            for(Map<String,Object> productMap:productMapList ) {
                Product product = new Product();
                product.setProductId((productMap.get("productId")==null)?"":productMap.get("productId").toString());
                product.setProductCode((productMap.get("productCode")==null)?"":productMap.get("productCode").toString());
                product.setApproval((productMap.get("approval")==null)?"":productMap.get("approval").toString());
                product.setLineNumber((productMap.get("lineNumber")==null)?0:(int)productMap.get("lineNumber"));
                product.setActivityId((productMap.get("activityId")==null)?"":productMap.get("activityId").toString());
                if(productMap.get("isGift")!=null){
                    if(!productMap.get("isGift").toString().trim().equals("")&&productMap.get("isGift").toString().trim().equals("Y"))
                    {
                        product.setIsGift("Y");
                    }
                }
                //设置商品summaryInfo
                SummaryInfo summaryInfo = new SummaryInfo();
                Map<String, Object> summaryInfoMap = (Map<String, Object>) productMap.get("summaryInfo");
//                summaryInfoMap.put("creationTime",(summaryInfoMap.get("creationTime")==null)?0:Long.parseLong(summaryInfoMap.get("creationTime").toString()));
//                summaryInfoMap.put("firstListTime",(summaryInfoMap.get("firstListTime")==null)?0:Long.parseLong(summaryInfoMap.get("firstListTime").toString()));
//                summaryInfoMap.put("listTime",(summaryInfoMap.get("listTime")==null)?0:Long.parseLong(summaryInfoMap.get("listTime").toString()));
//                summaryInfoMap.put("lastUpdate",(summaryInfoMap.get("lastUpdate")==null)?0:Long.parseLong(summaryInfoMap.get("lastUpdate").toString()));
//                summaryInfoMap.put("delistTime",(summaryInfoMap.get("delistTime")==null)?0:Long.parseLong(summaryInfoMap.get("delistTime").toString()));
                if(null!=summaryInfoMap){
                    mapToBean(summaryInfoMap, summaryInfo);
                }
                product.setSummaryInfo(summaryInfo);

                ProductDetailInfo productDetailInfo = new ProductDetailInfo();
                Map<String, Object> productDetailInfoMap = (Map<String, Object>) productMap.get("productDetailInfo");
                if (null!=productDetailInfoMap) {

                    mapToBean(productDetailInfoMap,productDetailInfo);
                }
                product.setProductDetailInfo(productDetailInfo);

                product.setCateList((List<String>) productMap.get("cateList"));

                product.setQuantity(Integer.parseInt(productMap.get("quantity").toString()));

                products.add(product);
            }

        }
        order.setProducts(products);
        return order;

    }

}
