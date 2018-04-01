package com.hand.hmall.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hand.hmall.dto.HmallMstProduct;
import com.hand.hmall.dto.MstBundles;
import com.hand.hmall.dto.MstCategory;
import com.hand.hmall.mapper.HmallMstBundlesMapper;
import com.hand.hmall.service.ICheckedConditionActionService;
import com.hand.hmall.service.IMstCategoryService;
import com.hand.hmall.service.IProductService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by shanks on 2017/2/25.
 * 促销活动推送商城调用
 */
@Service
public class CheckedConditionActionServiceImpl implements ICheckedConditionActionService {
    @Autowired
    private IMstCategoryService mstCategoryService;
    @Autowired
    private IProductService productService;
    @Autowired
    private HmallMstBundlesMapper mstBundlesMapper;

    /**
     * 获取促销结果中的优惠数据
     *
     * @param map
     * @param detail
     */
    @Override
    public void getDetailForAction(Map<String, Object> map, Map detail) {
        JSONObject action = JSONObject.parseObject(JSON.toJSONString(map));
        switch (map.get("definitionId").toString()) {
            //订单减X元
            case "o_total_discount":
                String orderReduceFee = getActionValue(action);
                detail.put("orderReduceFee", orderReduceFee);
                detail.put("activityType", map.get("definitionId").toString());
                break;

            //订单打x折
            case "o_total_rate":
                String orderDiscountReduce = getActionValue(action);
                detail.put("orderDiscountReduce", orderDiscountReduce);
                detail.put("activityType", map.get("definitionId").toString());
                break;

            //订单每满X元减Y元
            case "o_meet_delete":
                JSONObject rangeParms = action.getJSONObject("parameters");
                JSONObject value = rangeParms.getJSONObject("value");
                //获取类别比价符，包含（MEMBER_OF）、不包含(NOT_MEMBER_OF)
                String oValue = value.getString("value");
                String ofront = value.getString("front");
                detail.put("orderAmountNeed", ofront);
                detail.put("orderReduceFee", oValue);
                detail.put("activityType", map.get("definitionId").toString());
                break;
            //订单行每满X元减Y元
            case "oe_meet_delete":
                JSONObject oeRangeParms = action.getJSONObject("parameters");
                JSONObject oeValue = oeRangeParms.getJSONObject("value");
                //获取类别比价符，包含（MEMBER_OF）、不包含(NOT_MEMBER_OF)
                String oeValueStringValue = oeValue.getString("value");
                String oeValueStringfront = oeValue.getString("front");
                detail.put("orderAmountNeed", oeValueStringfront);
                detail.put("orderReduceFee", oeValueStringValue);
                detail.put("activityType", map.get("definitionId").toString());
                break;
            //订单行减X元
            case "oe_total_discount":
                String oeReduce = getActionValue(action);
                detail.put("orderReduceFee", oeReduce);
                detail.put("activityType", map.get("definitionId").toString());
                break;
            //订单行X折
            case "oe_total_rate":
                String oeDiscountReduce = getActionValue(action);
                detail.put("orderDiscountReduce", oeDiscountReduce);
                detail.put("activityType", map.get("definitionId").toString());
                break;
            //商品固定折扣
            case "o_fixed_rate":
                String productFixRate = getActionValue(action);
                detail.put("orderDiscountReduce", productFixRate);
                detail.put("activityType", map.get("definitionId").toString());
                break;
            //商品固定价格
            case "o_fixed_number":
                String productFixNum = getActionValue(action);
                detail.put("orderReduceFee", productFixNum);
                detail.put("activityType", map.get("definitionId").toString());
                break;

            //目标包价格
            case "o_target_price":

                detail.put("activityType", map.get("definitionId").toString());
                break;
            //赠品
            case "o_giver_product":
                JSONArray giftList = getActionValues(action);
                StringBuffer gifts = new StringBuffer();
                for (int i = 0; i < giftList.size(); i++) {
                    JSONObject gift = giftList.getJSONObject(i);
                    if (i == giftList.size() - 1) {
                        gifts.append(gift.getString("productCode")).append(":").append(gift.getString("countNumber"));
                    } else {
                        gifts.append(gift.getString("productCode")).append(":").append(gift.getString("countNumber")).append(",");
                    }
                }
                detail.put("gift", gifts.toString());
                detail.put("activityType", map.get("definitionId").toString());

                break;
            //免邮免安装费
            case "o_freight_waiver":
                detail.put("fixFeeReduce", "ALL");
                detail.put("shippingFeeReduce", "ALL");
                detail.put("activityType", map.get("definitionId").toString());
                break;
            //订单阶梯折扣
            case "o_discount_ladders":
                JSONArray disocuntList = getActionValues(action);
                StringBuffer discounts = new StringBuffer();
                for (int i = 0; i < disocuntList.size(); i++) {
                    JSONObject discount = disocuntList.getJSONObject(i);
                    if (i == disocuntList.size() - 1) {
                        discounts.append(discount.getString("key")).append(":").append(discount.getString("value"));
                    } else {
                        discounts.append(discount.getString("key")).append(":").append(discount.getString("value")).append(",");
                    }
                }
                detail.put("orderReduceFee", discounts.toString());
                detail.put("activityType", map.get("definitionId").toString());
            default:
                detail.put("activityType", map.get("definitionId").toString());


        }


    }

    /**
     * 获取促销条件的判断数据
     * @param condition
     * @param detail
     */
    @Override
    public void getDetailForCondition(Map<String, Object> condition, Map detail) {
        JSONObject conditionObj = JSONObject.parseObject(JSON.toJSONString(condition));
        switch (conditionObj.getString("definitionId")) {
            //订单满X元
            case "o_total_reached":
                JSONObject totalParameters = conditionObj.getJSONObject("parameters");
                JSONObject value = totalParameters.getJSONObject("value");
                String orderAmountNeed = value.getString("value");
                detail.put("orderAmountNeed", orderAmountNeed);
                break;
            //订单行满X元
            case "oe_total_reached":
                String oeAmount = getConditionValue(conditionObj);
                detail.put("orderAmountNeed", oeAmount);
                break;
            //订单满X件
            case "o_quantity_reached":
                String oProductNumber = getConditionValue(conditionObj);
                detail.put("productNumber", Integer.parseInt(oProductNumber));
                break;

            //商品范围
            case "o_product_range":

                List productIds = getConditionRangeValue(conditionObj);
                StringBuffer productCodes = new StringBuffer();
                //查询产品分类，拼接成字符串
                for (int i = 0; i < productIds.size(); i++) {
                    Integer id = (Integer) productIds.get(i);
                    HmallMstProduct product = productService.selectByProductId(id.longValue());
                    if (i == productIds.size() - 1) {
                        productCodes.append(product.getCode());
                    } else {
                        productCodes.append(product.getCode()).append(",");
                    }
                }

                //获取商品范围比较符，包含（MEMBER_OF）、不包含(NOT_MEMBER_OF)
                String productOperator = getConditionRangeOperator(conditionObj);
                if ("MEMBER_OF".equals(productOperator.trim())) {
                    detail.put("productCodes", productCodes.toString());
                } else {
                    detail.put("productExcluCodes", productCodes.toString());
                }
                break;
            //类别范围
            case "o_type_range":
                JSONArray categoryIds = getConditionRangeValue(conditionObj);
                StringBuffer categoryCodes = new StringBuffer();
                //查询产品分类，拼接成字符串
                for (int i = 0; i < categoryIds.size(); i++) {
                    Long id = categoryIds.getLong(i);
                    MstCategory category = new MstCategory();
                    category.setCategoryId(id);
                    category = mstCategoryService.selectByPrimaryKey(category);
                    if (i == categoryIds.size() - 1) {
                        categoryCodes.append(category.getCategoryCode());
                    } else {
                        categoryCodes.append(category.getCategoryCode()).append(",");
                    }
                }

                //获取类别比价符，包含（MEMBER_OF）、不包含(NOT_MEMBER_OF)
                String categoryOperator = getConditionRangeOperator(conditionObj);
                if ("MEMBER_OF".equals(categoryOperator.trim())) {
                    detail.put("categoryEn", categoryCodes.toString());
                } else {
                    detail.put("categoryExcluEn", categoryCodes.toString());
                }
                break;
            //地区范围
            case "o_area_range":
                JSONArray regionIds = getConditionRangeValue(conditionObj);
                StringBuffer regionCodes = new StringBuffer();
                //查询产品分类，拼接成字符串
                for (int i = 0; i < regionIds.size(); i++) {
                    String code = regionIds.getString(i);
                    if (i == regionIds.size() - 1) {
                        regionCodes.append(code);
                    } else {
                        regionCodes.append(code).append(",");
                    }
                }

                //获取类别比价符，包含（MEMBER_OF）、不包含(NOT_MEMBER_OF)
                String regionOperator = getConditionRangeOperator(conditionObj);
                if ("MEMBER_OF".equals(regionOperator.trim())) {
                    detail.put("regionCode", regionCodes.toString());
                } else {
                    detail.put("regionExcluCode", regionCodes.toString());
                }
                break;
        }
    }


    /**
     * 获取condition参数中的rangeValue
     *
     * @param condition
     * @return
     */
    public JSONArray getConditionRangeValue(JSONObject condition) {
        JSONObject rangeParms = condition.getJSONObject("parameters");
        //获取类别
        JSONObject rangeValue = rangeParms.getJSONObject("rangeValue");
        JSONArray value = rangeValue.getJSONArray("value");
        return value;
    }

    /**
     * 获取condition参数中的rangeOperator字段
     *
     * @param condition
     * @return
     */
    public String getConditionRangeOperator(JSONObject condition) {
        JSONObject rangeParms = condition.getJSONObject("parameters");
        JSONObject rangeOperator = rangeParms.getJSONObject("rangeOperator");
        //获取类别比价符，包含（MEMBER_OF）、不包含(NOT_MEMBER_OF)
        String operator = rangeOperator.getString("value");
        return operator;
    }

    /**
     * 获取condition参数中的value字段
     *
     * @param condition
     * @return
     */
    public String getConditionValue(JSONObject condition) {
        JSONObject rangeParms = condition.getJSONObject("parameters");
        JSONObject value = rangeParms.getJSONObject("value");
        //获取类别比价符，包含（MEMBER_OF）、不包含(NOT_MEMBER_OF)
        String conditionValue = value.getString("value");
        return conditionValue;
    }

    /**
     * 获取action参数中的类型为单个的value字段
     *
     * @param action
     * @return
     */
    public String getActionValue(JSONObject action) {
        return getConditionValue(action);
    }

    /**
     * 获取action参数中的类型为单个的value字段
     *
     * @param action
     * @return
     */
    public JSONArray getActionValues(JSONObject action) {
        JSONObject rangeParms = action.getJSONObject("parameters");
        JSONObject value = rangeParms.getJSONObject("value");
        //获取类别比价符，包含（MEMBER_OF）、不包含(NOT_MEMBER_OF)
        JSONArray actionValues = value.getJSONArray("value");
        return actionValues;
    }

    @Override
    public String getBundleCode(String activityId) {
        MstBundles bundles = new MstBundles();
        bundles.setPromotionCode(activityId);
        List<MstBundles> mstBundles = mstBundlesMapper.select(bundles);
        if (CollectionUtils.isNotEmpty(mstBundles)) {
            return mstBundles.get(0).getCode();
        } else {
            return null;
        }

    }
}
