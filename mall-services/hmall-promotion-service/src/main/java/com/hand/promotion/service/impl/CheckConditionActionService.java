package com.hand.promotion.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import com.hand.promotion.pojo.SimpleMessagePojo;
import com.hand.promotion.pojo.activity.ActionPojo;
import com.hand.promotion.pojo.activity.ChildPojo;
import com.hand.promotion.pojo.activity.ConditionPojo;
import com.hand.promotion.pojo.activity.ContainerPojo;
import com.hand.promotion.pojo.activity.GroupPojo;
import com.hand.promotion.pojo.activity.ParameterPojo;
import com.hand.promotion.pojo.activity.TargetValuePojo;
import com.hand.promotion.pojo.enums.ConditionActions;
import com.hand.promotion.pojo.enums.MsgMenu;
import com.hand.promotion.pojo.enums.OperatorMenu;
import com.hand.promotion.pojo.enums.RegexMessage;
import com.hand.promotion.service.ICheckConditionActionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2017/12/19
 * @description
 */
@Service
public class CheckConditionActionService implements ICheckConditionActionService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 校验促销条件是否合法
     *
     * @param conditions
     * @return
     */
    @Override
    public SimpleMessagePojo checkConditionInvalid(List<ConditionPojo> conditions) {
        SimpleMessagePojo simpleMessagePojo = new SimpleMessagePojo();
        for (ConditionPojo condition : conditions) {
            String definitionId = condition.getDefinitionId();

            if (StringUtils.isEmpty(definitionId)) {
                logger.info("基本条件definitionId为空");
                simpleMessagePojo.setFalse(MsgMenu.DEFINITION_ID_CAN_NOT_NULL);
                return simpleMessagePojo;
            }
            ConditionActions conditionActions = ConditionActions.getConditionActionsByValue(definitionId);
            if (conditionActions == null) {
                logger.info("基本条件definitionId:{}无效", condition.getDefinitionId());
                simpleMessagePojo.setFalse(MsgMenu.DEFINITION_ID_INVALIDATED);
                return simpleMessagePojo;
            }
            ParameterPojo parameters = condition.getParameters();
            checkParametersInvalid(parameters, conditionActions);
        }
        return simpleMessagePojo;
    }


    /**
     * 校验促销分组是否合法
     *
     * @param groups
     * @return
     */
    @Override
    public SimpleMessagePojo checkGroupInvalid(List<GroupPojo> groups) {
        SimpleMessagePojo simpleMessagePojo;
        List<ConditionPojo> conditionPojos = new ArrayList<>();
        try {
            for (GroupPojo group : groups) {
                List<ChildPojo> child = group.getChild();
                for (ChildPojo childPojo : child) {
                    ConditionPojo conditionPojo = new ConditionPojo();
                    conditionPojo.setDefinitionId(childPojo.getDefinitionId());
                    conditionPojo.setParameters(childPojo.getParameters());
                    conditionPojos.add(conditionPojo);
                }

            }
            simpleMessagePojo = checkConditionInvalid(conditionPojos);
            if (!simpleMessagePojo.isSuccess()) {
                return simpleMessagePojo;
            }
        } catch (Exception e) {
            logger.error("校验group条件异常", e);
            return new SimpleMessagePojo(false, MsgMenu.GROUP_CONDITION_INVALID, null);
        }

        return simpleMessagePojo;
    }

    /**
     * 校验促销容器是否合法
     *
     * @param containers
     * @return
     */
    @Override
    public SimpleMessagePojo checkContainerInvalid(List<ContainerPojo> containers) {
        SimpleMessagePojo simpleMessagePojo;
        List<ConditionPojo> conditionPojos = new ArrayList<>();
        try {
            for (ContainerPojo container : containers) {
                List<ChildPojo> child = container.getChild();
                for (ChildPojo childPojo : child) {
                    ConditionPojo conditionPojo = new ConditionPojo();
                    conditionPojo.setDefinitionId(childPojo.getDefinitionId());
                    conditionPojo.setParameters(childPojo.getParameters());
                    conditionPojos.add(conditionPojo);
                }

            }
            simpleMessagePojo = checkConditionInvalid(conditionPojos);
            if (!simpleMessagePojo.isSuccess()) {
                return simpleMessagePojo;
            }
        } catch (Exception e) {
            logger.error("校验container条件异常", e);
            return new SimpleMessagePojo(false, MsgMenu.CONTAINER_CONDITION_INVALID, null);
        }

        return simpleMessagePojo;
    }

    /**
     * 校验促销结果是否合法
     *
     * @param action
     * @return
     */
    @Override
    public SimpleMessagePojo checkActionInvalid(ActionPojo action) {
        SimpleMessagePojo checkResult = new SimpleMessagePojo();
        ConditionActions conditionActions = ConditionActions.getConditionActionsByValue(action.getDefinitionId());
        if (conditionActions == null) {
            logger.info("基本条件definitionId:{}无效，跳过该结果", action.getDefinitionId());
            return new SimpleMessagePojo(false, MsgMenu.DEFINITION_ID_INVALIDATED, null);
        }
        ParameterPojo parameters = action.getParameters();
        switch (conditionActions) {
            //订单减X元
            case o_total_discount:
                checkResult = checkParmValue(parameters, RegexMessage.POSITIVE_NUM, null, false);
                if (!checkResult.isSuccess()) {
                    return checkResult;
                }
                break;
            //订单打x折
            case o_total_rate:
                //订单促销价格
                checkResult = checkParmValue(parameters, RegexMessage.ZERO_TEN, null, false);
                if (!checkResult.isSuccess()) {
                    return checkResult;
                }
                break;
            //运费减免
            case o_freight_waiver:

                break;

            //赠品
            case o_giver_product:
                checkResult = checkGiftValue(parameters);
                if (!checkResult.isSuccess()) {
                    return checkResult;
                }
                break;
            //免单X件
            //todo:: 校验逻辑
            case o_free_number:
//                freePiece(activityId, precomputedModel, orderSummaryInfo, Integer.valueOf(value), isCouponFlag);
                break;
            //商品固定价格
            case o_fixed_number:
                checkResult = checkParmValue(parameters, RegexMessage.POSITIVE_NUM, null, false);
                if (!checkResult.isSuccess()) {
                    return checkResult;
                }
                break;
            //商品固定折扣
            case o_fixed_rate:
                checkResult = checkParmValue(parameters, RegexMessage.ZERO_TEN, null, false);
                if (!checkResult.isSuccess()) {
                    return checkResult;
                }
                break;
            //商品购买前X件减Y元
            case o_front_delete:
                checkResult = checkParmValue(parameters, RegexMessage.POSITIVE_NUM, RegexMessage.POSITIVE_INTEGER, true);
                if (!checkResult.isSuccess()) {
                    return checkResult;
                }
                break;
            //商品购买前X件打Y折
            case o_front_rate:
                checkResult = checkParmValue(parameters, RegexMessage.ZERO_TEN, RegexMessage.POSITIVE_INTEGER, true);
                if (!checkResult.isSuccess()) {
                    return checkResult;
                }
                //订单每满X元减Y元
            case o_meet_delete:
                //每满X元
                checkResult = checkParmValue(parameters, RegexMessage.POSITIVE_NUM, RegexMessage.POSITIVE_NUM, true);
                if (!checkResult.isSuccess()) {
                    return checkResult;
                }
                break;
            //订单每满X件减Y元
            case o_meet_number_delete:
                //每满X件
                checkResult = checkParmValue(parameters, RegexMessage.POSITIVE_NUM, RegexMessage.POSITIVE_INTEGER, true);
                if (!checkResult.isSuccess()) {
                    return checkResult;
                }
                break;
//            //固定价格购买其他商品
//            case o_fixed_price_buy:
//                calResult = fixedPriceBuy(activityId, action, precomputedModel, orderSummaryInfo, Double.valueOf(value), PromotionConstants.DISCOUNT_TYPE_NUMBER, containers, isCouponFlag);
//                break;
//            //固定折扣购买其他商品
//            case o_fixed_rate_buy:
//                calResult = fixedPriceBuy(activityId, action, precomputedModel, orderSummaryInfo, Double.valueOf(value), PromotionConstants.DISCOUNT_TYPE_RATE, containers, isCouponFlag);
//                break;
            //目标包价格
            case o_target_price:
                List<TargetValuePojo> targetValue = parameters.getTargetValue();
                if (CollectionUtils.isEmpty(targetValue)) {
                    return new SimpleMessagePojo(false, MsgMenu.NONE_CONTAINER, null);
                }
                checkResult = checkTargetParmValue(parameters, RegexMessage.POSITIVE_NUM);
                if (!checkResult.isSuccess()) {
                    return checkResult;
                }

                break;
            default:
                break;
        }

        return checkResult;
    }


    /**
     * 校验parameterPojo是否合法
     *
     * @param parameterPojo
     */
    @Override
    public SimpleMessagePojo checkParametersInvalid(ParameterPojo parameterPojo, ConditionActions conditionActions) {
        if (null == parameterPojo) {
            return new SimpleMessagePojo(false, MsgMenu.PARAMETER_CAN_NOT_NULL, null);
        }
        SimpleMessagePojo messagePojo = null;
        switch (conditionActions) {
            //订单满X元
            case o_total_reached:
                messagePojo = checkParmOperator(parameterPojo);
                if (!messagePojo.isSuccess()) {
                    return messagePojo;
                }
                messagePojo = checkParmValue(parameterPojo, RegexMessage.POSITIVE_NUM, null, false);
                if (!messagePojo.isSuccess()) {
                    return messagePojo;
                }
                break;
            //订单满X件
            case o_quantity_reached:
                messagePojo = checkParmOperator(parameterPojo);
                if (!messagePojo.isSuccess()) {
                    return messagePojo;
                }
                messagePojo = checkParmValue(parameterPojo, RegexMessage.NONE_NEGITIVE_INTEGER, null, false);
                if (!messagePojo.isSuccess()) {
                    return messagePojo;
                }
                break;
            //商品范围(件)
            case o_product_range:
                messagePojo = checkParmOperator(parameterPojo);
                if (!messagePojo.isSuccess()) {
                    return messagePojo;
                }
                messagePojo = checkParmValue(parameterPojo, RegexMessage.NONE_NEGITIVE_INTEGER, null, false);
                if (!messagePojo.isSuccess()) {
                    return messagePojo;
                }
                messagePojo = checkParmRangeOperator(parameterPojo);
                if (!messagePojo.isSuccess()) {
                    return messagePojo;
                }
                messagePojo = checkParmRangeValue(parameterPojo);
                if (!messagePojo.isSuccess()) {
                    return messagePojo;
                }
                break;
            //商品范围（总价）
            case o_product_range_number:
                messagePojo = checkParmOperator(parameterPojo);
                if (!messagePojo.isSuccess()) {
                    return messagePojo;
                }
                messagePojo = checkParmValue(parameterPojo, RegexMessage.POSITIVE_NUM, null, false);
                if (!messagePojo.isSuccess()) {
                    return messagePojo;
                }
                messagePojo = checkParmRangeOperator(parameterPojo);
                if (!messagePojo.isSuccess()) {
                    return messagePojo;
                }
                messagePojo = checkParmRangeValue(parameterPojo);
                if (!messagePojo.isSuccess()) {
                    return messagePojo;
                }
                break;
            //类别范围(件)
            case o_type_range:
                messagePojo = checkParmOperator(parameterPojo);
                if (!messagePojo.isSuccess()) {
                    return messagePojo;
                }
                messagePojo = checkParmValue(parameterPojo, RegexMessage.NONE_NEGITIVE_INTEGER, null, false);
                if (!messagePojo.isSuccess()) {
                    return messagePojo;
                }
                messagePojo = checkParmRangeOperator(parameterPojo);
                if (!messagePojo.isSuccess()) {
                    return messagePojo;
                }
                messagePojo = checkParmRangeValue(parameterPojo);
                if (!messagePojo.isSuccess()) {
                    return messagePojo;
                }
                break;
            //类别范围(总价)
            case o_type_range_number:
                messagePojo = checkParmOperator(parameterPojo);
                if (!messagePojo.isSuccess()) {
                    return messagePojo;
                }
                messagePojo = checkParmValue(parameterPojo, RegexMessage.POSITIVE_NUM, null, false);
                if (!messagePojo.isSuccess()) {
                    return messagePojo;
                }
                messagePojo = checkParmRangeOperator(parameterPojo);
                if (!messagePojo.isSuccess()) {
                    return messagePojo;
                }
                messagePojo = checkParmRangeValue(parameterPojo);
                if (!messagePojo.isSuccess()) {
                    return messagePojo;
                }
                break;
            //客户范围
            case o_user_range:
                messagePojo = checkParmRangeOperator(parameterPojo);
                if (!messagePojo.isSuccess()) {
                    return messagePojo;
                }
                messagePojo = checkParmRangeValue(parameterPojo);
                if (!messagePojo.isSuccess()) {
                    return messagePojo;
                }
                break;
            //地区范围
            case o_area_range:
                messagePojo = checkParmRangeOperator(parameterPojo);
                if (!messagePojo.isSuccess()) {
                    return messagePojo;
                }
                messagePojo = checkParmRangeValue(parameterPojo);
                if (!messagePojo.isSuccess()) {
                    return messagePojo;
                }
                break;
            //渠道范围
            case o_channel_range:
                messagePojo = checkParmRangeOperator(parameterPojo);
                if (!messagePojo.isSuccess()) {
                    return messagePojo;
                }
                messagePojo = checkParmRangeValue(parameterPojo);
                if (!messagePojo.isSuccess()) {
                    return messagePojo;
                }
                break;
            default:
                break;
        }
        return new SimpleMessagePojo();
    }


    /**
     * 校验parameterPojo 的value对象是否合法
     *
     * @param parameterPojo 要校验的对象
     * @param valueRegex    value要满足的正则表达式
     * @param fontRegex     font要满足的正则表达式
     * @param hasFont       是否存在font字段
     * @return
     */
    public SimpleMessagePojo checkParmValue(ParameterPojo parameterPojo, String valueRegex, String fontRegex, boolean hasFont) {
        try {
            String value = parameterPojo.transValuePojo().getValue();
            String font = parameterPojo.transValuePojo().getFront();
            if (!RegexMessage.matches(value, valueRegex)) {
                return new SimpleMessagePojo(false, MsgMenu.VALUE_INVALID, null);
            }

            if (hasFont && !RegexMessage.matches(font, fontRegex)) {
                return new SimpleMessagePojo(false, MsgMenu.VALUE_INVALID, null);
            }
        } catch (Exception e) {
            logger.error("促销value不合法", e);
            return new SimpleMessagePojo(false, MsgMenu.VALUE_INVALID, null);
        }
        return new SimpleMessagePojo();

    }

    private SimpleMessagePojo checkTargetParmValue(ParameterPojo parameterPojo, String valueRegex) {
        try {
            String value = parameterPojo.getValue();
            if (!RegexMessage.matches(value, valueRegex)) {
                return new SimpleMessagePojo(false, MsgMenu.VALUE_INVALID, null);
            }

        } catch (Exception e) {
            logger.error("促销value不合法", e);
            return new SimpleMessagePojo(false, MsgMenu.VALUE_INVALID, null);
        }
        return new SimpleMessagePojo();
    }

    /**
     * 校验parameterPojo的rangeValue是否合法
     *
     * @param parameterPojo
     * @return
     */
    public SimpleMessagePojo checkParmRangeValue(ParameterPojo parameterPojo) {
        try {
            List<String> values = parameterPojo.getRangeValue().getValue();
            if (CollectionUtils.isEmpty(values)) {
                return new SimpleMessagePojo(false, MsgMenu.RANGE_VALUE_INVALID, null);

            }
            for (String value : values) {
                if (StringUtils.isEmpty(value)) {
                    return new SimpleMessagePojo(false, MsgMenu.RANGE_VALUE_INVALID, null);
                }
            }
        } catch (Exception e) {
            logger.error("促销rangeValue不合法", e);

            return new SimpleMessagePojo(false, MsgMenu.RANGE_VALUE_INVALID, null);
        }
        return new SimpleMessagePojo();

    }

    /**
     * 校验操作符是否合法
     *
     * @param parameterPojo
     * @return
     */
    public SimpleMessagePojo checkParmOperator(ParameterPojo parameterPojo) {
        try {
            String operator = parameterPojo.transOperatorPojo().getValue();
            if (!OperatorMenu.isExist(operator)) {
                return new SimpleMessagePojo(false, MsgMenu.OPERATOR_INVALID, null);
            } else {
                return new SimpleMessagePojo();
            }
        } catch (Exception e) {

            logger.error("促销operator不合法", e);

            return new SimpleMessagePojo(false, MsgMenu.OPERATOR_INVALID, null);

        }
    }

    /**
     * 校验操作符是否合法
     *
     * @param parameterPojo
     * @return
     */
    public SimpleMessagePojo checkParmRangeOperator(ParameterPojo parameterPojo) {
        try {
            String operator = parameterPojo.getRangeOperator().getValue();
            if (!OperatorMenu.isExist(operator)) {
                return new SimpleMessagePojo(false, MsgMenu.RANGE_OPERATOR_INVALID, null);
            } else {
                return new SimpleMessagePojo();
            }
        } catch (Exception e) {

            logger.error("促销operator不合法", e);

            return new SimpleMessagePojo(false, MsgMenu.RANGE_OPERATOR_INVALID, null);

        }
    }

    public SimpleMessagePojo checkGiftValue(ParameterPojo parameters) {
        String value = parameters.transValuePojo().getValue();
        try {
            JSONArray gifts = JSON.parseArray(value);
            if (gifts.isEmpty()) {
                return new SimpleMessagePojo(false, MsgMenu.VALUE_INVALID, null).setCheckMsg("赠品数据为空");
            }
            return new SimpleMessagePojo();
        } catch (Exception e) {
            e.printStackTrace();
            return new SimpleMessagePojo(false, MsgMenu.VALUE_INVALID, null).setCheckMsg("赠品数据异常");
        }

    }


}
