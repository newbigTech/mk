package com.hand.promotion.service;


import com.hand.promotion.pojo.SimpleMessagePojo;
import com.hand.promotion.pojo.activity.ActionPojo;
import com.hand.promotion.pojo.activity.ConditionPojo;
import com.hand.promotion.pojo.activity.ContainerPojo;
import com.hand.promotion.pojo.activity.GroupPojo;
import com.hand.promotion.pojo.activity.ParameterPojo;
import com.hand.promotion.pojo.enums.ConditionActions;

import java.util.List;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2017/12/19
 * @description
 */

public interface ICheckConditionActionService {

    /**
     * 校验促销、优惠券 条件是否合法
     *
     * @param conditions
     * @return
     */
    SimpleMessagePojo checkConditionInvalid(List<ConditionPojo> conditions);

    /**
     * 校验促销组条件是否合法
     *
     * @param groups
     * @return
     */
    SimpleMessagePojo checkGroupInvalid(List<GroupPojo> groups);

    /**
     * 校验容器条件是否合法
     *
     * @param containers
     * @return
     */
    SimpleMessagePojo checkContainerInvalid(List<ContainerPojo> containers);

    /**
     * 校验结果是否合法
     *
     * @param actionPojo
     * @return
     */
    SimpleMessagePojo checkActionInvalid(ActionPojo actionPojo);

    /**
     * 校验parameterPojo是否合法
     *
     * @param parameterPojo
     * @return
     */
    SimpleMessagePojo checkParametersInvalid(ParameterPojo parameterPojo, ConditionActions conditionActions);

}
