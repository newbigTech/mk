package com.hand.hmall.service.impl;

import com.hand.hmall.client.IProductClientService;
import com.hand.hmall.dao.SalePromotionCodeDao;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.menu.ConditionActions;
import com.hand.hmall.menu.OperatorMenu;
import com.hand.hmall.menu.Status;
import com.hand.hmall.service.ISalePromotionCodeService;
import com.hand.hmall.util.SortUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 商品促销关联操作类
 * Created by shanks on 2017/3/13.
 */
@Service
public class SalePromotionCodeServiceImpl implements ISalePromotionCodeService {
    @Autowired
    private IProductClientService productClientService;
    @Autowired
    private SalePromotionCodeDao salePromotionCodeDao;

    /**
     * 保存商品促销关联信息
     * @param conditions
     * @param groups
     * @param containers
     * @param activity
     */
    @Override
    public  void saveProductRelevance(List<Map<String, Object>> conditions, List<Map<String, Object>> groups, List<Map<String, Object>> containers, Map<String, Object> activity) {
        Set<String> productCodes=new HashSet<>();
        Set<String> storeCategories=new HashSet<>();

        //判断条件部分是否存在商品信息
        if(conditions!=null) {
            if(!conditions.isEmpty()) {
                for (Map<String, Object> condition : conditions) {
                    doProductRelevance(condition,productCodes,storeCategories);

                }
            }
        }

        //判断container
        if(containers!=null) {
            if(!containers.isEmpty()) {
                for (Map<String, Object> container : containers) {
                    List<Map<String, Object>> childList = (List<Map<String, Object>>) container.get("child");
                    if (!childList.isEmpty()) {
                        for (Map<String, Object> children : childList) {
                            doProductRelevance(children,productCodes,storeCategories);

                        }
                    }
                }
            }
        }

        //判断groups
        if(groups!=null){
            if(!groups.isEmpty())
            {
                for(Map<String,Object> group:groups){
                    List<Map<String,Object>> childList= (List<Map<String, Object>>) group.get("child");
                    if(childList!=null)
                    {
                        if(!childList.isEmpty())
                        {
                            groupsProductRelevanceIteration(childList,productCodes,storeCategories);

                        }
                    }
                }
            }
        }
        //如果促销在前台可显示，保存商品-促销关联
        if( activity.get("isExcludeShow").equals("N")) {
            if (!productCodes.isEmpty()) {
                for (String productCode : productCodes) {
                    Map<String, Object> promotionCode = new HashMap<>();
                    promotionCode.put("id", UUID.randomUUID().toString());
                    promotionCode.put("activityId", activity.get("activityId"));
                    promotionCode.put("meaning", activity.get("pageShowMes"));
                    promotionCode.put("group", activity.get("group"));
                    promotionCode.put("isOverlay", activity.get("isOverlay"));
                    promotionCode.put("priority", activity.get("priority"));
                    //definedId关联商品code
                    promotionCode.put("definedId", productCode);
                    String status=activity.get("status").equals(Status.ACTIVITY.getValue())? Status.ACTIVITY.getValue(): Status.INACTIVE.getValue();
                    promotionCode.put("status", status);
                    //type 为CODE 商品映射
                    promotionCode.put("type","CODE");
                    promotionCode.put("startDate", activity.get("startDate"));
                    promotionCode.put("endDate", activity.get("endDate"));
                    salePromotionCodeDao.add(promotionCode);
                }
            }
            if(!storeCategories.isEmpty()){
                for (String category : storeCategories) {
                    Map<String, Object> promotionCode = new HashMap<>();
                    promotionCode.put("id", UUID.randomUUID().toString());
                    promotionCode.put("activityId", activity.get("activityId"));
                    promotionCode.put("meaning", activity.get("pageShowMes"));
                    promotionCode.put("group", activity.get("group"));
                    promotionCode.put("isOverlay", activity.get("isOverlay"));
                    promotionCode.put("priority", activity.get("priority"));
                    //definedId 存储类别code
                    promotionCode.put("definedId", category);
                    //type为CATEGORY 商品类别映射
                    promotionCode.put("type", "CATEGORY");
                    String status=activity.get("status").equals(Status.ACTIVITY.getValue())? Status.ACTIVITY.getValue(): Status.INACTIVE.getValue();
                    promotionCode.put("status", status);
                    promotionCode.put("startDate", activity.get("startDate"));
                    promotionCode.put("endDate", activity.get("endDate"));
                    salePromotionCodeDao.add(promotionCode);
                }
            }
            //促销中不涉及商品/商品类别
            if(storeCategories.isEmpty()&&productCodes.isEmpty()) {
                Map<String, Object> promotionCode = new HashMap<>();
                promotionCode.put("id", UUID.randomUUID().toString());
                promotionCode.put("activityId", activity.get("activityId"));
                promotionCode.put("meaning", activity.get("pageShowMes"));
                promotionCode.put("group", activity.get("group"));
                promotionCode.put("isOverlay", activity.get("isOverlay"));
                promotionCode.put("priority", activity.get("priority"));
                promotionCode.put("definedId", "ALL");
                promotionCode.put("type", "ALL");
                String status=activity.get("status").equals(Status.ACTIVITY.getValue())? Status.ACTIVITY.getValue(): Status.INACTIVE.getValue();
                promotionCode.put("status", status);
                promotionCode.put("startDate", activity.get("startDate"));
                promotionCode.put("endDate", activity.get("endDate"));
                salePromotionCodeDao.add(promotionCode);
            }
        }
    }

    @Override
    public ResponseData selectByProductCode(String productCode) {
        List<Map<String,?>> returnList=new LinkedList<>();
        List<Map<String,?>> mapList=salePromotionCodeDao.selectActivityByCode(productCode,"CODE");

        //获取品类信息
//        ResponseData responseData=productClientService.queryCategroyByProductCode(productCode);
//        List<Map<String,Object>> resList= (List<Map<String, Object>>) responseData.getResp();

        List<String> respCheckedList=new ArrayList<>();
//        for(Map<String,Object> res:resList){
//            String categoryCode=res.get("categoryCode").toString();
//            if(!respCheckedList.contains(categoryCode)) {
//                respCheckedList.add(categoryCode);
//                List<Map<String, ?>> categoryList = salePromotionCodeDao.selectActivityByCode(categoryCode, "CATEGORY");
//                mapList.addAll(categoryList);
//            }
//        }

        try {
            SortUtil.listsort(mapList,"priority",true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<String> groupList=new ArrayList<>();

        //首先循环遍历促销集合
        for(Map<String,?> map:mapList){
            //判断日期
            if((Long)map.get("startDate")<=System.currentTimeMillis()&&(Long)map.get("endDate")>System.currentTimeMillis()) {
//                //组间判断
//                if (!groupList.contains(map.get("group").toString())) {
//                    groupList.add(map.get("group").toString());
//                    returnList.add(map);
//                } else {
//                    //组内判断
//                    if (map.get("isOverlay").equals("Y")) {
                        returnList.add(map);
//                    }
//                }
            }
        }

        return new ResponseData(returnList);
    }

    /**
     * 搜索组中的商品Id
     * @param childs
     * @param productCode
     * @param storeCategories
     */
    public void groupsProductRelevanceIteration(List<Map<String,Object>> childs,Set<String> productCode ,Set<String> storeCategories)
    {
        for(Map<String,Object>groupChild:childs){
            if(groupChild.get("definitionId").equals(ConditionActions.o_product_range.toString())) {
                Map<String, Object> parameters = (Map<String, Object>) groupChild.get("parameters");
                if (!parameters.isEmpty()) {
                    Map<String, Object> rangeOperator = (Map<String, Object>) parameters.get("rangeOperator");
                    Map<String, Object> rangeValue = (Map<String, Object>) parameters.get("rangeValue");
                    if (rangeOperator != null && rangeValue != null) {
                        if (!rangeOperator.isEmpty()) {
                            String operator = rangeOperator.get("value").toString();
                            List<String> productIds = (List<String>) rangeValue.get("value");
                            if (operator.equals(OperatorMenu.MEMBER_OF.toString())) {
                                ResponseData productResponse = productClientService.selectByProductIds(productIds);
                                if (productResponse.isSuccess()) {
                                    List<Map<String, Object>> productList = (List<Map<String, Object>>) productResponse.getResp();
                                    putProductRelevance(productList, productCode);
                                }
                            } else if (operator.equals(OperatorMenu.NOT_CONTAINS.toString())) {
                                ResponseData productResponse = productClientService.selectSkuByNotIn(productIds);
                                if (productResponse.isSuccess()) {
                                    List<Map<String, Object>> productList = (List<Map<String, Object>>) productResponse.getResp();
                                    putProductRelevance(productList, productCode);
                                }
                            }
                        }
                    }
                }
            }else if (groupChild.get("definitionId").equals(ConditionActions.o_type_range.toString())) {
                Map<String, Object> parameters = (Map<String, Object>) groupChild.get("parameters");
                if (!parameters.isEmpty()) {
                    Map<String, Object> rangeOperator = (Map<String, Object>) parameters.get("rangeOperator");
                    Map<String, Object> rangeValue = (Map<String, Object>) parameters.get("rangeValue");
                    if (rangeOperator != null && rangeValue != null) {
                        if (!rangeOperator.isEmpty()) {
                            String operator = rangeOperator.get("value").toString();
                            List<String> storeCategoryIds = (List<String>) rangeValue.get("value");
                            if (operator.equals(OperatorMenu.MEMBER_OF.toString())) {
                                for (String s : storeCategoryIds) {
                                    s.replace("\"", "");
                                    storeCategories.add(s);
                                }
                            } else if (operator.equals(OperatorMenu.NOT_MEMBER_OF.toString())) {
                                List<String> storeCategoryIdList = new ArrayList<>();
                                for (String s : storeCategoryIds) {
                                    s.replace("\"", "");
                                    storeCategoryIdList.add(s);
                                }
                                ResponseData responseData = productClientService.getStoreCategoryIdNotEqual(storeCategoryIdList);
                                if (responseData.isSuccess()) {
                                    storeCategories.addAll((Collection<? extends String>) responseData.getResp());
                                }

                            }
                        }
                    }
                }
            }else if(groupChild.get("definitionId").equals(ConditionActions.GROUP.toString())){
                List<Map<String,Object>> childList= (List<Map<String, Object>>) groupChild.get("child");
                if(childList!=null)
                {
                    if(!childList.isEmpty())
                    {
                        groupsProductRelevanceIteration(childList,productCode,storeCategories);
                    }
                }
            }
        }
    }


    public void putProductRelevance(List<Map<String, Object>> productRelevance,Set<String> productCodes)
    {
        if (productRelevance != null) {
            if (!productRelevance.isEmpty()) {
                for (Map<String, Object> map : productRelevance) {
                    productCodes.add(map.get("code").toString());
                }
            }
        }
    }

    /**
     * 获取条件、容器中包含的产品code，产品类别
     * @param data
     * @param productCodes
     * @param storeCategories
     */
    public void doProductRelevance(Map<String,Object> data,Set<String> productCodes,Set<String> storeCategories )
    {
        Map<String, Object> parameters = (Map<String, Object>) data.get("parameters");
        if (parameters != null) {
            if (!parameters.isEmpty()) {
                //获取比较符
                Map<String, Object> rangeOperator = (Map<String, Object>) parameters.get("rangeOperator");
                //rangeValue 保存condition/container的商品Id
                Map<String, Object> rangeValue = (Map<String, Object>) parameters.get("rangeValue");
                if (rangeOperator != null && rangeValue != null) {
                    if (!rangeOperator.isEmpty()) {

                        String operator = rangeOperator.get("value").toString();
                        //条件是商品范围
                        if (data.get("definitionId").equals(ConditionActions.o_product_range.toString())) {
                            List<String> productIds = (List<String>) rangeValue.get("value");
                            //比较符号为MEMBER_OF，查询rangeValue中所有商品，将商品code保存到code集合中
                            if (operator.equals(OperatorMenu.MEMBER_OF.toString())) {
                                ResponseData productResponse = productClientService.selectByProductIds(productIds);
                                if (productResponse.isSuccess()) {
                                    List<Map<String, Object>> productList = (List<Map<String, Object>>) productResponse.getResp();
                                    //将商品code添加到code集合中
                                    putProductRelevance(productList, productCodes);
                                }
                            } else if (operator.equals(OperatorMenu.NOT_MEMBER_OF.toString())) {
                                ResponseData productResponse = productClientService.selectSkuByNotIn(productIds);
                                if (productResponse.isSuccess()) {
                                    List<Map<String, Object>> productList = (List<Map<String, Object>>) productResponse.getResp();
                                    putProductRelevance(productList, productCodes);
                                }
                            }

                        } else if (data.get("definitionId").equals(ConditionActions.o_type_range.toString())) {
                            List<String> storeCategoryIds = (List<String>) rangeValue.get("value");
                            if (operator.equals(OperatorMenu.MEMBER_OF.toString())) {
                                for (String s : storeCategoryIds) {
                                    s.replace("\"", "");
                                    storeCategories.add(s);
                                }
                            } else if (operator.equals(OperatorMenu.NOT_MEMBER_OF.toString())) {
                                List<String> storeCategoryIdList=new ArrayList<>();
                                for (String s : storeCategoryIds) {
                                    s.replace("\"", "");
                                    storeCategoryIdList.add(s);
                                }
                                ResponseData responseData=productClientService.getStoreCategoryIdNotEqual(storeCategoryIdList);
                                if(responseData.isSuccess()){
                                    storeCategories.addAll((Collection<? extends String>) responseData.getResp());
                                }

                            }
                        }
                    }
                }
            }

        }

    }

}
