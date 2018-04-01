package com.hand.hmall.util;

import com.hand.hmall.dto.ResponseData;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by shanks on 2017/3/2.
 *
 * @desp 校验前台创建优惠券、促销活动的参数是否合法
 */
public class SaleCheckedLegalUtil {

    private static Logger logger = LoggerFactory.getLogger("SaleCheckedLegalUtil");

    /**
     * 校验促销与优惠券的条件，结果参数是否为空
     *
     * @param map 前台参数
     * @return
     */
    public static List<String> checkedSaleConditionAction(Map<String, Object> map) {
        List<Map<String, Object>> actions = (List<Map<String, Object>>) map.get("actions");
        List<String> conditionAction = new ArrayList<>();
        if (CollectionUtils.isEmpty(actions)) {
            conditionAction.add("请先添加结果");
            return conditionAction;
        }
//        conditionAction = checkedActionRule(actions);
        return conditionAction;
    }


    /**
     * 校验优惠数据是否合法
     *
     * @param actionList
     * @return
     */
    public static List<String> checkedActionRule(List<Map<String, Object>> actionList) {
        for (Map<String, Object> map : actionList) {
            switch (map.get("definitionId").toString()) {
                //订单减X元
                case "o_total_discount":
                    if (map.get("parameters") != null) {
                        Map<String, Object> parameters = (Map<String, Object>) map.get("parameters");
                        if (parameters.get("value") != null) {

                            Map<String, Object> value = (Map<String, Object>) parameters.get("value");
                            if (value.get("value") == null) {
                                return checkInfo("【订单减X元】需要定义参数");
                            }

                        } else {
                            return checkInfo("【订单减X元】需要定义参数");
                        }
                    } else {
                        return checkInfo("【订单减X元】需要定义参数");
                    }
                    break;

                //订单打x折
                case "o_total_rate":
                    if (map.get("parameters") != null) {
                        Map<String, Object> parameters = (Map<String, Object>) map.get("parameters");
                        if (parameters.get("value") != null) {
                            Map<String, Object> value = (Map<String, Object>) parameters.get("value");
                            if (value.get("value") != null) {
                                Double valueData = (Double) value.get("value");
                                if (valueData > 10 || valueData <= 0) {
                                    return checkInfo("【订单打x折】数必须是1-10区间");
                                }
                            } else {
                                return checkInfo("【订单打x折】需要定义参数");
                            }
                        } else {
                            return checkInfo("【订单打x折】需要定义参数");
                        }
                    } else {
                        return checkInfo("【订单打x折】需要定义参数");
                    }
                    break;

                case "o_fixed_number":
                    if (map.get("parameters") != null) {
                        Map<String, Object> parameters = (Map<String, Object>) map.get("parameters");
                        if (parameters.get("value") != null) {
                            Map value = (Map) parameters.get("value");
                            if (null == value) {
                                return checkInfo("【商品固定价格】需要添加参数");
                            }
                        } else {
                            return checkInfo("【商品固定价格】需要添加参数");
                        }
                    } else {
                        return checkInfo("【商品固定价格】需要添加参数");
                    }
                    break;
                case "o_fixed_rate":
                    if (map.get("parameters") != null) {
                        Map<String, Object> parameters = (Map<String, Object>) map.get("parameters");
                        if (parameters.get("value") != null) {
                            Map value = (Map) parameters.get("value");
                            if (null == value) {
                                return checkInfo("【商品固定折扣】需要添加参数");
                            }
                        } else {
                            return checkInfo("【商品固定折扣】需要添加参数");
                        }
                    } else {
                        return checkInfo("【商品固定折扣】需要添加参数");
                    }
                    break;


                case "o_target_price":
                    if (map.get("parameters") != null) {
                        Map<String, Object> parameters = (Map<String, Object>) map.get("parameters");
                        if (parameters.get("value") == null) {
                            return checkInfo("【目标包价格】需要添加价格");
                        }
                        if (parameters.get("operator") == null) {
                            return checkInfo("【目标包价格】需要添加策略");
                        }

                        if (parameters.get("targetValue") == null) {
                            return checkInfo("【目标包价格】需要添加目标商品容器");
                        }
                    } else {
                        return checkInfo("【目标包价格】需要添加参数");
                    }
                    break;


            }
        }
        return new ArrayList<>(1);

    }

    public static List<String> checkInfo(String info) {
        List<String> checkInfo = new ArrayList(4);
        checkInfo.add(info);
        return checkInfo;
    }
    /**
     * 校验描述促销及优惠券时间是否有效
     *
     * @param data
     * @param message
     */
    public static void checkedDateLegal(Map<String, Object> data, List<String> message) {
        Long sysDate = System.currentTimeMillis();
        if (data.get("startDate") == null || data.get("startDate").toString().trim().equals("")) {
            message.add("开始时间不能为空");
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            simpleDateFormat.setLenient(false);
            try {
                Date startDate = simpleDateFormat.parse(data.get("startDate").toString());
                Date endDate = simpleDateFormat.parse(data.get("endDate").toString());
                if (startDate.getTime() > endDate.getTime()) {
                    message.add("结束时间不能大于开始时间");
                }
                if (startDate.getTime() < sysDate - 1000 * 20) {
                    message.add("促销生效时间要大于当前时间");
                }
            } catch (ParseException e) {
                e.printStackTrace();
                message.add("开始时间格式错误");
            }

        }
        if (data.get("endDate") == null || data.get("endDate").toString().trim().equals("")) {
            message.add("结束时间不能为空");
        } else {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            simpleDateFormat.setLenient(false);
            try {
                simpleDateFormat.parse(data.get("endDate").toString());
            } catch (ParseException e) {
                e.printStackTrace();
                message.add("结束时间格式错误");
            }

        }

    }

    /**
     * 整合错误信息进行返回
     *
     * @param checked
     * @param conditionActionsChecked
     * @return
     */
    public static ResponseData returnCheckedMessage(List<String> checked, List<String> conditionActionsChecked) {
        if (!checked.isEmpty() || !conditionActionsChecked.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : checked) {
                stringBuilder.append(s + "</p>");
            }
            if (!conditionActionsChecked.isEmpty()) {
                for (String s : conditionActionsChecked) {
                    stringBuilder.append(s + "</p>");
                }
            }
            ResponseData responseData = new ResponseData();
            responseData.setSuccess(false);
            responseData.setMsg(stringBuilder.toString());
            return responseData;
        } else {
            return new ResponseData();
        }
    }
}
