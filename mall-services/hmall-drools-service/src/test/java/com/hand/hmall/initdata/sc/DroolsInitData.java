package com.hand.hmall.initdata.sc;

import com.hand.hmall.DroolsApplication;
import com.hand.hmall.controller.SaleActivityController;
import com.hand.hmall.dao.*;
import com.hand.hmall.menu.SaleType;
import com.hand.hmall.menu.Status;
import com.hand.hmall.service.IRuleTempService;
import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Describe d
 * @Author noob
 * @Date 2017/6/30 8:42
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DroolsApplication.class)
public class DroolsInitData {

    @Autowired
    private SelectConditionDao selectConditionDao;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private FieldTempDao fieldTempDao;

    @Autowired
    private ModelTempDao modelTempDao;

    @Autowired
    private DefinitionDao definitionDao;

    @Autowired
    private ActionDao actionDao;

    @Test
    public void clearOldData() {
        List<Map<String, ?>> selectConditionList = selectConditionDao.selectAll();
        selectConditionList.stream().forEach(field -> {
                    selectConditionDao.delete((String) field.get("id"));
                }
        );

        List<Map<String, ?>> groupList = groupDao.selectAll();
        groupList.stream().forEach(field -> {
                    groupDao.delete((String) field.get("id"));
                }
        );


        List<Map<String, ?>> fieldList = fieldTempDao.selectAll();
        fieldList.stream().forEach(field -> {
                    fieldTempDao.delete((String) field.get("fieldId"));
                }
        );


        List<Map<String, ?>> modelList = modelTempDao.selectAll();
        modelList.stream().forEach(field -> {
                    modelTempDao.delete((String) field.get("modelId"));
                }
        );

        List<Map<String, ?>> definitionList = definitionDao.selectAll();
        definitionList.stream().forEach(field -> {
                    definitionDao.delete((String) field.get("definitionId"));
                }
        );

        List<Map<String, ?>> actionList = actionDao.selectAll();
        actionList.stream().forEach(field -> {
                    actionDao.delete((String) field.get("actionId"));
                }
        );


    }

    @Test
    public void initModelData() {
        //字段
        Map<String, Object> fieldMap = new HashedMap();
        fieldMap.put("fieldId", "o_total_reached_field");
        fieldMap.put("fieldName", "orderAmount");
        fieldMap.put("meaning", "订单总价");
        fieldMap.put("modelId", "orderPojo_model");
        fieldMap.put("operator", "operator.value");
        fieldMap.put("value", "value.value");
        fieldMap.put("definitionId", "o_total_reached");
        fieldMap.put("type", "Double");
        fieldTempDao.add(fieldMap);

        fieldMap.put("fieldId", "oe_total_reached_field");
        fieldMap.put("fieldName", "totalFee");
        fieldMap.put("meaning", "订单行应付金额");
        fieldMap.put("modelId", "orderEntryPojo_model");
        fieldMap.put("operator", "operator.value");
        fieldMap.put("value", "value.value");
        fieldMap.put("definitionId", "oe_total_reached");
        fieldMap.put("type", "Double");
        fieldTempDao.add(fieldMap);

        fieldMap.put("fieldId", "o_quantity_reached_filed");
        fieldMap.put("fieldName", "quantity");
        fieldMap.put("meaning", "订单行总件数");
        fieldMap.put("modelId", "orderPojo_model");
        fieldMap.put("operator", "operator.value");
        fieldMap.put("value", "value.value");
        fieldMap.put("definitionId", "o_quantity_reached");
        fieldMap.put("type", "Integer");
        fieldTempDao.add(fieldMap);

        fieldMap.put("fieldId", "o_product_range_filed_value");
        fieldMap.put("fieldName", "productId");
        fieldMap.put("meaning", "商品ID");
        fieldMap.put("modelId", "orderEntryPojo_model");
        fieldMap.put("operator", "rangeOperator.value");
        fieldMap.put("value", "rangeValue.value");
        fieldMap.put("definitionId", "o_product_range");
        fieldMap.put("type", "String");
        fieldTempDao.add(fieldMap);

        fieldMap.put("fieldId", "o_product_range_filed_number");
        fieldMap.put("fieldName", "quantity");
        fieldMap.put("meaning", "商品数量");
        fieldMap.put("modelId", "orderEntryPojo_model");
        fieldMap.put("operator", "operator.value");
        fieldMap.put("value", "value.value");
        fieldMap.put("definitionId", "o_product_range");
        fieldMap.put("type", "Number");
        fieldTempDao.add(fieldMap);


//        fieldMap.put("fieldId", "o_area_range_filed_distribution");
//        fieldMap.put("fieldName", "distribution");
//        fieldMap.put("meaning", "市编码");
//        fieldMap.put("modelId", "orderPojo_model");
//        fieldMap.put("operator", "operator.value");
//        fieldMap.put("value", "value.value");
//        fieldMap.put("definitionId", "o_area_range");
//        fieldMap.put("type", "String");
//        fieldTempDao.add(fieldMap);

        fieldMap.put("fieldId", "o_area_range_filed_value");
        fieldMap.put("fieldName", "cityCode");
        fieldMap.put("meaning", "市编码");
        fieldMap.put("modelId", "orderPojo_model");
        fieldMap.put("operator", "rangeOperator.value");
        fieldMap.put("value", "rangeValue.value");
        fieldMap.put("definitionId", "o_area_range");
        fieldMap.put("type", "List<String>");
        fieldTempDao.add(fieldMap);

        fieldMap.put("fieldId", "o_type_range_value");
        fieldMap.put("fieldName", "cateList");
        fieldMap.put("meaning", "商品类别");
        fieldMap.put("modelId", "orderEntryPojo_model");
        fieldMap.put("operator", "rangeOperator.value");
        fieldMap.put("value", "rangeValue.value");
        fieldMap.put("definitionId", "o_type_range");
        fieldMap.put("type", "List<String>");
        fieldTempDao.add(fieldMap);

        fieldMap.put("fieldId", "o_type_range_number");
        fieldMap.put("fieldName", "quantity");
        fieldMap.put("meaning", "商品数量");
        fieldMap.put("modelId", "orderEntryPojo_model");
        fieldMap.put("operator", "operator.value");
        fieldMap.put("value", "value.value");
        fieldMap.put("definitionId", "o_type_range");
        fieldMap.put("type", "Integer");
        fieldTempDao.add(fieldMap);

        //模型
        Map<String, Object> modelMap = new HashedMap();
        modelMap.put("modelId", "orderPojo_model");
        modelMap.put("modelName", "OrderPojo");
        modelMap.put("className", "com.hand.hmall.pojo.OrderPojo");
        modelTempDao.add(modelMap);

        modelMap.put("modelId", "orderEntryPojo_model");
        modelMap.put("modelName", "OrderEntryPojo");
        modelMap.put("className", "com.hand.hmall.pojo.OrderEntryPojo");
        modelTempDao.add(modelMap);


        //条件结果预定义
        Map<String, Object> definitionMap = new HashedMap();
        definitionMap.put("definitionId", "o_total_reached");
        definitionMap.put("meaning", "订单满X元");
        definitionMap.put("type", "condition");
        definitionDao.add(definitionMap);

        Map<String, Object> definitionEntryMap = new HashedMap();
        definitionEntryMap.put("definitionId", "oe_total_reached");
        definitionEntryMap.put("meaning", "订单行满X元");
        definitionEntryMap.put("type", "condition");
        definitionDao.add(definitionEntryMap);

        definitionMap.put("definitionId", "o_quantity_reached");
        definitionMap.put("meaning", "订单满X件");
        definitionMap.put("type", "condition");
        definitionDao.add(definitionMap);

        definitionEntryMap.put("definitionId", "oe_quantity_reached");
        definitionEntryMap.put("meaning", "订单行满X件");
        definitionEntryMap.put("type", "condition");
        definitionDao.add(definitionEntryMap);

        definitionMap.put("definitionId", "o_product_range");
        definitionMap.put("meaning", "商品范围");
        definitionMap.put("type", "condition");
        definitionDao.add(definitionMap);

        definitionMap.put("definitionId", "o_type_range");
        definitionMap.put("meaning", "类别范围");
        definitionMap.put("type", "condition");
        definitionDao.add(definitionMap);

        definitionMap.put("definitionId", "o_front_delete");
        definitionMap.put("meaning", "商品购买前X件减Y元");
        definitionMap.put("type", "action");
        definitionDao.add(definitionMap);

        definitionMap.put("definitionId", "o_user_range");
        definitionMap.put("meaning", "客户范围");
        definitionMap.put("type", "condition");
        definitionDao.add(definitionMap);

        definitionMap.put("definitionId", "o_area_range");
        definitionMap.put("meaning", "地区范围");
        definitionMap.put("type", "condition");
        definitionDao.add(definitionMap);

        definitionMap.put("definitionId", "o_store_range");
        definitionMap.put("meaning", "门店范围");
        definitionMap.put("type", "condition");
        definitionDao.add(definitionMap);

        definitionMap.put("definitionId", "o_channel_range");
        definitionMap.put("meaning", "渠道范围");
        definitionMap.put("type", "condition");
        definitionDao.add(definitionMap);

        definitionMap.put("definitionId", "GROUP");
        definitionMap.put("meaning", "组");
        definitionMap.put("type", "condition");
        definitionDao.add(definitionMap);

        definitionMap.put("definitionId", "CONTAINER");
        definitionMap.put("meaning", "容器");
        definitionMap.put("type", "action");
        definitionDao.add(definitionMap);


        definitionMap.put("definitionId", "o_total_discount");
        definitionMap.put("meaning", "订单减X元");
        definitionMap.put("type", "action");
        definitionDao.add(definitionMap);

        definitionEntryMap.put("definitionId", "oe_total_discount");
        definitionEntryMap.put("meaning", "订单行减X元");
        definitionEntryMap.put("type", "action");
        definitionDao.add(definitionEntryMap);

        definitionMap.put("definitionId", "o_total_rate");
        definitionMap.put("meaning", "订单打x折");
        definitionMap.put("type", "action");
        definitionDao.add(definitionMap);

        definitionEntryMap.put("definitionId", "oe_total_rate");
        definitionEntryMap.put("meaning", "订单行打x折");
        definitionEntryMap.put("type", "action");
        definitionDao.add(definitionEntryMap);

        definitionMap.put("definitionId", "o_giver_coupon");
        definitionMap.put("meaning", "赠送优惠券");
        definitionMap.put("type", "action");
        definitionDao.add(definitionMap);

        definitionMap.put("definitionId", "o_giver_product");
        definitionMap.put("meaning", "赠品");
        definitionMap.put("type", "action");
        definitionDao.add(definitionMap);

        definitionMap.put("definitionId", "o_freight_waiver");
        definitionMap.put("meaning", "运费减免");
        definitionMap.put("type", "action");
        definitionDao.add(definitionMap);

        definitionMap.put("definitionId", "o_free_number");
        definitionMap.put("meaning", "免单X件");
        definitionMap.put("type", "action");
        definitionDao.add(definitionMap);

        definitionMap.put("definitionId", "o_fixed_number");
        definitionMap.put("meaning", "商品固定价格");
        definitionMap.put("type", "action");
        definitionDao.add(definitionMap);

        definitionMap.put("definitionId", "o_fixed_rate");
        definitionMap.put("meaning", "商品固定折扣");
        definitionMap.put("type", "action");
        definitionDao.add(definitionMap);

        definitionMap.put("definitionId", "o_fixed_price_buy");
        definitionMap.put("meaning", "固定价格购买其他商品");
        definitionMap.put("type", "action");
        definitionDao.add(definitionMap);

        definitionMap.put("definitionId", "o_fixed_rate_buy");
        definitionMap.put("meaning", "固定折扣购买其他商品");
        definitionMap.put("type", "action");
        definitionDao.add(definitionMap);

        definitionMap.put("definitionId", "o_target_price");
        definitionMap.put("meaning", "目标包价格");
        definitionMap.put("type", "action");
        definitionDao.add(definitionMap);

        definitionMap.put("definitionId", "o_front_rate");
        definitionMap.put("meaning", "商品购买前X件打Y折");
        definitionMap.put("type", "action");
        definitionDao.add(definitionMap);

        definitionMap.put("definitionId", "o_meet_delete");
        definitionMap.put("meaning", "每满X元减Y元");
        definitionMap.put("type", "action");
        definitionDao.add(definitionMap);

        definitionEntryMap.put("definitionId", "oe_meet_delete");
        definitionEntryMap.put("meaning", "订单行每满X元减Y元");
        definitionEntryMap.put("type", "action");
        definitionDao.add(definitionEntryMap);

        definitionMap.put("definitionId", "o_change_product");
        definitionMap.put("meaning", "换购");
        definitionMap.put("type", "action");
        definitionDao.add(definitionMap);

        //结果
        Map<String, Object> actionMap = new HashedMap();
        actionMap.put("modelId", "orderPojo_model");
        actionMap.put("definitionId", "o_total_discount");
        actionMap.put("meaning", "订单减X元");
        actionMap.put("actionCode", "actionService.orderDiscount(variables,{{data}});\n");
        actionMap.put("actionId", "o_total_discount_action");
        actionMap.put("dataVariable", "{{data}}");
        actionMap.put("actionData", "value.value");
        actionDao.add(actionMap);

        //订单行减元
        Map<String, Object> actionEntryMap = new HashedMap();
        actionEntryMap.put("modelId", "orderEntryPojo_model");
        actionEntryMap.put("definitionId", "oe_total_discount");
        actionEntryMap.put("meaning", "订单行减X元");
        actionEntryMap.put("actionCode", "actionService.orderEntryDiscount(variables,{{data}});\n");
        actionEntryMap.put("actionId", "oe_total_discount_action");
        actionEntryMap.put("dataVariable", "{{data}}");
        actionEntryMap.put("actionData", "value.value");
        actionDao.add(actionEntryMap);

        /**
         * 订单级别满折
         * @Author: noob
         * @Param:  * @param
         * @Date: 2017/6/30 14:38
         */
        actionMap.put("modelId", "orderPojo_model");
        actionMap.put("definitionId", "o_total_rate");
        actionMap.put("meaning", "订单打X折");
        actionMap.put("actionCode", "actionService.orderPercentageDiscount(variables,{{data}});\n");
        actionMap.put("actionId", "o_total_rate_action");
        actionMap.put("dataVariable", "{{data}}");
        actionMap.put("actionData", "value.value");
        actionDao.add(actionMap);

        /**
         * 订单行级别满折
         * @Author: noob
         * @Param:  * @param
         * @Date: 2017/6/30 14:38
         */
        actionEntryMap.put("modelId", "orderEntryPojo_model");
        actionEntryMap.put("definitionId", "oe_total_rate");
        actionEntryMap.put("meaning", "订单行打X折");
        actionEntryMap.put("actionCode", "actionService.orderEntryPercentageDiscount(variables,{{data}});\n");
        actionEntryMap.put("actionId", "oe_total_rate_action");
        actionEntryMap.put("dataVariable", "{{data}}");
        actionEntryMap.put("actionData", "value.value");
        actionDao.add(actionEntryMap);


        actionMap.put("modelId", "orderPojo_model");
        actionMap.put("definitionId", "o_freight_waiver");
        actionMap.put("meaning", "免运费");
        actionMap.put("actionCode", "actionService.freeFreight(variables);\n");
        actionMap.put("actionId", "o_freight_waiver_action");
        actionMap.put("dataVariable", "");
        actionMap.put("actionData", "value.value");
        actionDao.add(actionMap);

//        actionMap.put("modelId", "orderPojo_model");
//        actionMap.put("definitionId", "o_fixed_price_buy");
//        actionMap.put("meaning", "固定价格购买其他商品");
//        actionMap.put("actionCode", "actionService.useContainer(variables,\"{{data}}\",\"fixPrice\");\n");
//        actionMap.put("actionId", "o_fixed_price_buy_action");
//        actionMap.put("dataVariable", "{{data}}");
//        actionMap.put("actionData", "");
//        actionDao.add(actionMap);
//
//        actionMap.put("modelId", "orderPojo_model");
//        actionMap.put("definitionId", "o_fixed_rate_buy");
//        actionMap.put("meaning", "固定折扣购买其他商品");
//        actionMap.put("actionCode", "actionService.useContainer(variables,\"{{data}}\",\"rateDiscount\");\n");
//        actionMap.put("actionId", "o_fixed_rate_buy_action");
//        actionMap.put("dataVariable", "{{data}}");
//        actionMap.put("actionData", "");
//        actionDao.add(actionMap);

        actionMap.put("modelId", "orderPojo_model");
        actionMap.put("definitionId", "o_fixed_rate");
        actionMap.put("meaning", "商品固定折扣");
        actionMap.put("actionCode", "actionService.productDiscount(variables,Double.valueOf({{data}}),\"rateDiscount\");\n");
        actionMap.put("actionId", "o_fixed_rate_action");
        actionMap.put("dataVariable", "{{data}}");
        actionMap.put("actionData", "value.value");
        actionDao.add(actionMap);

        actionMap.put("modelId", "orderPojo_model");
        actionMap.put("definitionId", "o_fixed_number");
        actionMap.put("meaning", "商品固定价格");
        actionMap.put("actionCode", "actionService.productDiscount(variables,Double.valueOf({{data}}),\"fixPrice\");\n");
        actionMap.put("actionId", "o_fixed_number_action");
        actionMap.put("dataVariable", "{{data}}");
        actionMap.put("actionData", "value.value");
        actionDao.add(actionMap);

        actionMap.put("modelId", "orderPojo_model");
        actionMap.put("definitionId", "o_target_price");
        actionMap.put("meaning", "目标包");
        actionMap.put("actionCode", "actionService.targetPackage(variables,\"{{data}}\");\n");
        actionMap.put("actionId", "o_target_price_action");
        actionMap.put("dataVariable", "{{data}}");
        actionMap.put("actionData", "");
        actionDao.add(actionMap);

        actionMap.put("modelId", "orderPojo_model");
        actionMap.put("definitionId", "o_giver_coupon");
        actionMap.put("meaning", "赠送优惠券");
        actionMap.put("actionCode", "actionService.sendCoupon(variables,\"{{data}}\");\n");
        actionMap.put("actionId", "o_giver_coupon_action");
        actionMap.put("dataVariable", "{{data}}");
        actionMap.put("actionData", "");
        actionDao.add(actionMap);

        actionMap.put("modelId", "orderPojo_model");
        actionMap.put("definitionId", "o_giver_product");
        actionMap.put("meaning", "赠品");
        actionMap.put("actionCode", "actionService.sendGift(variables,\"{{data}}\");\n");
        actionMap.put("actionId", "o_giver_product_action");
        actionMap.put("dataVariable", "{{data}}");
        actionMap.put("actionData", "");
        actionDao.add(actionMap);

        actionMap.put("modelId", "orderPojo_model");
        actionMap.put("definitionId", "o_front_delete");
        actionMap.put("meaning", "前X件减价");
        actionMap.put("actionCode", "actionService.scareBuying(variables,\"{{data}}\",\"fixDiscount\");\n");
        actionMap.put("actionId", "o_front_delete_action");
        actionMap.put("dataVariable", "{{data}}");
        actionMap.put("actionData", "");
        actionDao.add(actionMap);

        actionMap.put("modelId", "orderPojo_model");
        actionMap.put("definitionId", "o_front_rate");
        actionMap.put("meaning", "前X件打折");
        actionMap.put("actionCode", "actionService.scareBuying(variables,\"{{data}}\",\"rateDiscount\");\n");
        actionMap.put("actionId", "o_front_rate_action");
        actionMap.put("dataVariable", "{{data}}");
        actionMap.put("actionData", "");
        actionDao.add(actionMap);

        actionMap.put("modelId", "orderPojo_model");
        actionMap.put("definitionId", "o_meet_delete");
        actionMap.put("meaning", "每满X元减Y元");
        actionMap.put("actionCode", "actionService.orderMeetDiscount(variables,\"{{data}}\",\"fixDiscount\");\n");
        actionMap.put("actionId", "o_meet_delete_action");
        actionMap.put("dataVariable", "{{data}}");
        actionMap.put("actionData", "");
        actionDao.add(actionMap);

        actionEntryMap.put("modelId", "orderEntryPojo_model");
        actionEntryMap.put("definitionId", "oe_meet_delete");
        actionEntryMap.put("meaning", "商品每满X元减Y元");
        actionEntryMap.put("actionCode", "actionService.orderEntryMeetDiscount(variables,\"{{data}}\",\"fixDiscount\");\n");
        actionEntryMap.put("actionId", "oe_meet_delete_action");
        actionEntryMap.put("dataVariable", "{{data}}");
        actionEntryMap.put("actionData", "");
        actionDao.add(actionEntryMap);

        actionMap.put("modelId", "orderPojo_model");
        actionMap.put("definitionId", "o_change_product");
        actionMap.put("meaning", "换购");
        actionMap.put("actionCode", "actionService.purchaseOther(variables,\"{{data}}\");\n");
        actionMap.put("actionId", "o_change_product_action");
        actionMap.put("dataVariable", "{{data}}");
        actionMap.put("actionData", "");
        actionDao.add(actionMap);

        actionMap.put("modelId", "order_model");
        actionMap.put("definitionId", "o_free_number");
        actionMap.put("meaning", "免单X件");
        actionMap.put("actionCode", "actionService.orderExempt(variables,{{data}});\n");
        actionMap.put("actionId", "o_freight_number_action");
        actionMap.put("dataVariable", "{{data}}");
        actionMap.put("actionData", "value.value");
        actionDao.add(actionMap);
    }

    @Test
    public void initSelectActionDao() {
        int i = 1;
        Map<String, Object> map = new HashMap<>();
        map.put("definitionId", "o_total_discount");
        map.put("code", "ADD_ACTIONS");
        map.put("meaning", "订单减X元");
        map.put("type", "ALL");
        map.put("priority", i);
        map.put("level", "1");
        i++;
        map.put("id", UUID.randomUUID().toString());
        selectConditionDao.add(map);

        Map<String, Object> mapEntry = new HashMap<>();
        mapEntry.put("definitionId", "oe_total_discount");
        mapEntry.put("code", "ADD_ACTIONS");
        mapEntry.put("meaning", "订单行减X元");
        mapEntry.put("type", "ALL");
        mapEntry.put("priority", i);
        mapEntry.put("level", "2");
        i++;
        mapEntry.put("id", UUID.randomUUID().toString());
        selectConditionDao.add(mapEntry);


        map.put("definitionId", "o_total_rate");
        map.put("code", "ADD_ACTIONS");
        map.put("meaning", "订单打x折");
        map.put("type", "ALL");
        map.put("priority", i);
        map.put("level", "1");
        i++;
        map.put("id", UUID.randomUUID().toString());
        selectConditionDao.add(map);

        mapEntry.put("definitionId", "oe_total_rate");
        mapEntry.put("code", "ADD_ACTIONS");
        mapEntry.put("meaning", "订单行打x折");
        mapEntry.put("type", "ALL");
        mapEntry.put("priority", i);
        map.put("level", "2");

        i++;
        mapEntry.put("id", UUID.randomUUID().toString());
        selectConditionDao.add(mapEntry);
//
        map.put("definitionId", "o_freight_waiver");
        map.put("code", "ADD_ACTIONS");
        map.put("meaning", "运费减免");
        map.put("type", SaleType.ACTIVITY.getValue());
        map.put("priority", i);
        map.put("level", "ALL");
        i++;
        map.put("id", UUID.randomUUID().toString());
        selectConditionDao.add(map);
//
//        map.put("definitionId", "o_giver_coupon");
//        map.put("code", "ADD_ACTIONS");
//        map.put("meaning", "赠送优惠券");
//        map.put("type", SaleType.ACTIVITY.getValue());
//        map.put("priority", i);
//        i++;
//        map.put("id", UUID.randomUUID().toString());
//        selectConditionDao.add(map);

        map.put("definitionId", "o_giver_product");
        map.put("code", "ADD_ACTIONS");
        map.put("meaning", "赠品");
        map.put("type", SaleType.ACTIVITY.getValue());
        map.put("priority", i);
        map.put("level", "1");
        i++;
        map.put("id", UUID.randomUUID().toString());
        selectConditionDao.add(map);
//
        map.put("definitionId", "o_free_number");
        map.put("code", "ADD_ACTIONS");
        map.put("type", SaleType.ACTIVITY.getValue());
        map.put("meaning", "免单X件");
        map.put("priority", i);
        map.put("level", "2");
        i++;
        map.put("id", UUID.randomUUID().toString());
        selectConditionDao.add(map);
//
        map.put("definitionId", "o_fixed_number");
        map.put("code", "ADD_ACTIONS");
        map.put("meaning", "商品固定价格");
        map.put("type", SaleType.ACTIVITY.getValue());
        map.put("level", "2");
        map.put("priority", i);
        i++;
        map.put("id", UUID.randomUUID().toString());
        selectConditionDao.add(map);

//
        map.put("definitionId", "o_fixed_rate");
        map.put("code", "ADD_ACTIONS");
        map.put("meaning", "商品固定折扣");
        map.put("type", SaleType.ACTIVITY.getValue());
        map.put("level", "2");
        map.put("priority", i);
        i++;
        map.put("id", UUID.randomUUID().toString());
        selectConditionDao.add(map);

//        map.put("definitionId", "o_front_delete");
//        map.put("code", "ADD_ACTIONS");
//        map.put("meaning", "前X件减Y元");
//        map.put("type", SaleType.ACTIVITY.getValue());
//        map.put("priority", i);
//        i++;
//        map.put("id", UUID.randomUUID().toString());
//        selectConditionDao.add(map);

//        map.put("definitionId", "o_front_rate");
//        map.put("code", "ADD_ACTIONS");
//        map.put("meaning", "前X件打Y折");
//        map.put("type", SaleType.ACTIVITY.getValue());
//        map.put("priority", i);
//        i++;
//        map.put("id", UUID.randomUUID().toString());
//        selectConditionDao.add(map);

        map.put("definitionId", "o_meet_delete");
        map.put("code", "ADD_ACTIONS");
        map.put("meaning", "订单每满X元减Y元");
        map.put("type", SaleType.ACTIVITY.getValue());
        map.put("priority", i);
        map.put("level", "1");
        i++;
        map.put("id", UUID.randomUUID().toString());
        selectConditionDao.add(map);

        mapEntry.put("definitionId", "oe_meet_delete");
        mapEntry.put("code", "ADD_ACTIONS");
        mapEntry.put("meaning", "订单行每满X元减Y元");
        mapEntry.put("type", SaleType.ACTIVITY.getValue());
        map.put("level", "2");
        mapEntry.put("priority", i);
        i++;
        mapEntry.put("id", UUID.randomUUID().toString());
        selectConditionDao.add(mapEntry);
//
//        map.put("definitionId", "o_fixed_price_buy");
//        map.put("code", "ADD_ACTIONS");
//        map.put("meaning", "固定价格购买其他商品");
//        map.put("type", SaleType.ACTIVITY.getValue());
//        map.put("priority", i);
//        i++;
//        map.put("id", UUID.randomUUID().toString());
//        selectConditionDao.add(map);
//
//        map.put("definitionId", "o_fixed_rate_buy");
//        map.put("code", "ADD_ACTIONS");
//        map.put("meaning", "固定折扣购买其他商品");
//        map.put("type", SaleType.ACTIVITY.getValue());
//        map.put("priority", i);
//        i++;
//        map.put("id", UUID.randomUUID().toString());
//        selectConditionDao.add(map);
//
        map.put("definitionId", "o_target_price");
        map.put("code", "ADD_ACTIONS");
        map.put("meaning", "目标包价格");
        map.put("type", SaleType.ACTIVITY.getValue());
        map.put("priority", i);
        map.put("level", "2");
        i++;
        map.put("id", UUID.randomUUID().toString());
        selectConditionDao.add(map);
        System.out.println(">>>>>>>>>>>>>>>ADD_ACTIONS》》》》" + i);
    }

    @Test
    public void initSelectConditionDao() {

        int i = 1;
        Map<String, Object> map = new HashMap<>();
        map.put("definitionId", "o_total_reached");
        map.put("code", "ADD_CONDITIONS");
        map.put("meaning", "订单满X元");
        map.put("type", "ALL");
        map.put("priority", i);
        map.put("level", "ALL");
        i++;
        map.put("id", UUID.randomUUID().toString());
        selectConditionDao.add(map);

        map.put("definitionId", "oe_total_reached");
        map.put("code", "ADD_CONDITIONS");
        map.put("meaning", "订单行满X元");
        map.put("type", "ALL");
        map.put("priority", i);
        map.put("level", "2");
        i++;
        map.put("id", UUID.randomUUID().toString());
        selectConditionDao.add(map);

        map.put("definitionId", "o_quantity_reached");
        map.put("code", "ADD_CONDITIONS");
        map.put("meaning", "订单满X件");
        map.put("type", "ALL");
        map.put("priority", i);
        map.put("level", "ALL");
        i++;
        map.put("id", UUID.randomUUID().toString());
        selectConditionDao.add(map);

        map.put("definitionId", "o_product_range");
        map.put("code", "ADD_CONDITIONS");
        map.put("meaning", "商品范围");
        map.put("type", "ALL");
        map.put("priority", i);
        map.put("level", "2");
        i++;
        map.put("id", UUID.randomUUID().toString());
        selectConditionDao.add(map);


        map.put("definitionId", "o_type_range");
        map.put("code", "ADD_CONDITIONS");
        map.put("meaning", "类别范围");
        map.put("type", "ALL");
        map.put("level", "2");
        map.put("priority", i);
        i++;
        map.put("id", UUID.randomUUID().toString());
        selectConditionDao.add(map);


//        map.put("definitionId", "o_user_range");
//        map.put("code", "ADD_CONDITIONS");
//        map.put("meaning", "客户范围");
//        map.put("type", "ALL");
//        map.put("priority", i);
//        i++;
//        map.put("id", UUID.randomUUID().toString());
//        selectConditionDao.add(map);
//
//
        map.put("definitionId", "o_area_range");
        map.put("code", "ADD_CONDITIONS");
        map.put("meaning", "地区范围");
        map.put("type", "ALL");
        map.put("priority", i);
        map.put("level", "ALL");
        i++;
        map.put("id", UUID.randomUUID().toString());
        selectConditionDao.add(map);
//
////
//        map.put("definitionId", "o_channel_range");
//        map.put("code", "ADD_CONDITIONS");
//        map.put("meaning", "渠道范围");
//        map.put("type", "ALL");
//        map.put("priority", i);
//        i++;
//        map.put("id", UUID.randomUUID().toString());
//        selectConditionDao.add(map);
//
        map.put("definitionId", "GROUP");
        map.put("code", "ADD_CONDITIONS");
        map.put("meaning", "组");
        map.put("type", SaleType.ACTIVITY.getValue());
        map.put("priority", i);
        map.put("level", "ALL");
        i++;
        map.put("id", UUID.randomUUID().toString());
        selectConditionDao.add(map);
//
        map.put("definitionId", "CONTAINER");
        map.put("code", "ADD_CONDITIONS");
        map.put("meaning", "容器");
        map.put("type", "ALL");
        map.put("priority", i);
        map.put("level", "2");
        i++;
        map.put("id", UUID.randomUUID().toString());
        selectConditionDao.add(map);

//        map.put("definitionId", "CONTAINER_ACTION");
//        map.put("code", "ADD_ACTION");
//        map.put("meaning", "容器");
//        map.put("type", SaleType.ACTIVITY.getValue());
//        map.put("priority", i);
//        i++;
//        map.put("id", UUID.randomUUID().toString());
//        selectConditionDao.add(map);


        map.put("definitionId", "o_total_reached");
        map.put("code", "ADD_GROUPS");
        map.put("meaning", "订单满X元");
        map.put("type", SaleType.ACTIVITY.getValue());
        map.put("priority", i);
        map.put("level", "ALL");
        i++;
        map.put("id", UUID.randomUUID().toString());
        selectConditionDao.add(map);

        map.put("definitionId", "oe_total_reached");
        map.put("code", "ADD_GROUPS");
        map.put("meaning", "订单行满X元");
        map.put("type", SaleType.ACTIVITY.getValue());
        map.put("priority", i);
        map.put("level", "2");
        i++;
        map.put("id", UUID.randomUUID().toString());
        selectConditionDao.add(map);
//
        map.put("definitionId", "o_quantity_reached");
        map.put("code", "ADD_GROUPS");
        map.put("meaning", "订单满X件");
        map.put("type", SaleType.ACTIVITY.getValue());
        map.put("priority", i);
        map.put("level", "ALL");
        i++;
        map.put("id", UUID.randomUUID().toString());
        selectConditionDao.add(map);

        map.put("definitionId", "o_product_range");
        map.put("code", "ADD_GROUPS");
        map.put("meaning", "商品范围");
        map.put("type", SaleType.ACTIVITY.getValue());
        map.put("priority", i);
        map.put("level", "2");
        i++;
        map.put("id", UUID.randomUUID().toString());
        selectConditionDao.add(map);
//
        map.put("definitionId", "o_type_range");
        map.put("code", "ADD_GROUPS");
        map.put("meaning", "类别范围");
        map.put("type", SaleType.ACTIVITY.getValue());
        map.put("priority", i);
        i++;
        map.put("id", UUID.randomUUID().toString());
        selectConditionDao.add(map);

//        map.put("definitionId", "o_user_range");
//        map.put("code", "ADD_GROUPS");
//        map.put("meaning", "客户范围");
//        map.put("type", SaleType.ACTIVITY.getValue());
//        map.put("priority", i);
//        i++;
//        map.put("id", UUID.randomUUID().toString());
//        selectConditionDao.add(map);

//        map.put("definitionId", "o_area_range");
//        map.put("code", "ADD_GROUPS");
//        map.put("meaning", "地区范围");
//        map.put("type", SaleType.ACTIVITY.getValue());
//        map.put("priority", i);
//        i++;
//        map.put("id", UUID.randomUUID().toString());
//        selectConditionDao.add(map);

//        map.put("definitionId", "o_channel_range");
//        map.put("code", "ADD_GROUPS");
//        map.put("meaning", "渠道范围");
//        map.put("type", SaleType.ACTIVITY.getValue());
//        map.put("priority", i);
//        i++;
//        map.put("id", UUID.randomUUID().toString());
//        selectConditionDao.add(map);

        map.put("definitionId", "GROUP");
        map.put("code", "ADD_GROUPS");
        map.put("type", SaleType.ACTIVITY.getValue());
        map.put("meaning", "组");
        map.put("priority", i);
        map.put("level", "ALL");
        i++;
        map.put("id", UUID.randomUUID().toString());
        selectConditionDao.add(map);

        map.put("definitionId", "o_product_range");
        map.put("code", "ADD_CONTAINERS");
        map.put("meaning", "商品范围");
        map.put("type", "ALL");
        map.put("priority", i);
        map.put("level", "2");
        i++;
        map.put("id", UUID.randomUUID().toString());
        selectConditionDao.add(map);
        map.put("definitionId", "o_change_product");
        map.put("code", "ADD_ACTIONS");
        map.put("meaning", "换购");
        map.put("type", SaleType.ACTIVITY.getValue());
        map.put("priority", i);
        map.put("id", UUID.randomUUID().toString());
        map.put("level", "2");
        selectConditionDao.add(map);

//        map.put("definitionId", "o_type_range");
//        map.put("code", "ADD_CONTAINERS");
//        map.put("meaning", "类别范围");
//        map.put("type", SaleType.ACTIVITY.getValue());
//        map.put("priority", i);
//        i++;
//        map.put("id", UUID.randomUUID().toString());
//        selectConditionDao.add(map);

        System.out.println(">>>>>>>>>>ADD_CONDITIONS" + i);

    }

    /**
     * 优惠券使用条件
     */
    @Test
    public void initGroup() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", "DEFAULT");
        map.put("priority", 0);
        map.put("name", "默认分组");
        groupDao.add(map);

        map.put("id", "FREE_FREIGHT");
        map.put("priority", 1);
        map.put("name", "免运费分组");
        groupDao.add(map);

//        map.put("id","BUNDELS");
//        map.put("priority",2);
//        map.put("name","捆绑套装分组");
//        groupDao.add(map);


    }

}
