package com.hand.hmall.service.impl;


import com.hand.hmall.dao.*;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.menu.OperatorMenu;
import com.hand.hmall.model.Coupon;
import com.hand.hmall.model.ResourceWrapper;
import com.hand.hmall.service.IRuleTempService;
import com.hand.hmall.temp.*;
import com.hand.hmall.util.ConditionUtil;
import com.hand.hmall.util.DroolsUtils;
import com.hand.hmall.util.KieSessionUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.kie.api.KieServices;
import org.kie.api.builder.ReleaseId;
import org.kie.internal.io.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * 将前台参数转换为drools文件servcie
 */
@Service
public class RuleTempServiceImpl implements IRuleTempService {
    @Autowired
    private ModelTempDao modelTempDao;
    @Autowired
    private FieldTempDao fieldTempDao;
    @Autowired
    private DefinitionDao definitionDao;
    @Autowired
    private RuleDao ruleDao;
    @Autowired
    private SaleCouponDao saleCouponDao;
    @Autowired
    private ActionDao actionDao;

    @Autowired
    private ActionDataDao actionDataDao;

    @Autowired
    private GroupDao groupDao;
    @Autowired
    private SaleActivityDao saleActivityDao;

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    //处理集合的操作符
    private static String[] collectOperator = {"memberOf", "not memberOf", "contains", "not contains", "in", "not in"};


    /**
     * 模型表新增记录
     * @param model
     * @return
     */
    @Override
    public Map addModel(ModelTemp model) {

        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("modelId", UUID.randomUUID().toString());
        modelMap.put("modelName", model.getModelName());
        modelMap.put("className", model.getClassName());
        modelTempDao.add(modelMap);

        return modelMap;
    }

    /**
     * 新增促销条件结果记录
     * @param definition
     * @return
     */
    @Override
    public Map addDefinition(Map<String, Object> definition) {
        definitionDao.add(definition);
        return definition;
    }

    /**
     * 促销结果表新增操作
     * @param action
     * @return
     */
    @Override
    public Map addAction(ActionTemp action) {
        Map<String, Object> actionMap = new HashMap<>();
        actionMap.put("actionId", UUID.randomUUID().toString());
        actionMap.put("actionCode", action.getActionCode());
        actionMap.put("modelId", action.getModelId());
        actionMap.put("actionData", action.getActionData());
        actionMap.put("definitionId", action.getDefinitionId());
        actionMap.put("dataVariable", action.getDataVariable());
        actionDao.add(actionMap);
        return actionMap;
    }

    /**
     * 新增规则分组
     * @param group
     * @return
     */
    @Override
    public Map addGroup(Map<String, Object> group) {
        group.put("id", UUID.randomUUID().toString());
        groupDao.add(group);
        return group;
    }


    /**
     * 新增条件判断字段
     *
     * @param field
     * @return
     */
    @Override
    public Map insertField(Field field) {
        Map<String, Object> fieldMap = new HashMap<>();
        fieldMap.put("fieldId", UUID.randomUUID().toString());
        fieldMap.put("fieldName", field.getFieldName());
        fieldMap.put("meaning", field.getMeaning());
        fieldMap.put("modelId", field.getModelId());
        fieldMap.put("operator", field.getOperator());
        fieldMap.put("value", field.getValue());
        fieldMap.put("definitionId", field.getDefinitionId());
        fieldMap.put("type", field.getType());
        fieldTempDao.add(fieldMap);
        return fieldMap;
    }

    /**
     * 将前台参数转换为对应的DTO
     *
     * @param ruleInputTemp
     * @return
     * @throws NullPointerException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws ClassCastException
     */
    @Override
    public ResponseData createRule(RuleInputTemp ruleInputTemp) throws NullPointerException, IllegalArgumentException, InvocationTargetException, IllegalAccessException, ClassCastException {
        if (null != ruleInputTemp.getCoupon() && null != ruleInputTemp.getActivity()) {
            return new ResponseData(false, "ACTIVITY_AND_COUPON");
        }
        RuleTemp ruleTemp = new RuleTemp();
        //保存Model集合，ModelTemp会保存促销中涉及的Fact对象，使用到的变量，以及比较符号
        List<ModelTemp> modelList = new ArrayList<>();
        //保存结果信息
        List<ActionTemp> actionList = new ArrayList<>();
        //保存model的id
        Set<String> modelSet = new HashSet<>();
        //container用于实现套装促销，生成container对应的drools文件
        if (null != ruleInputTemp.getContainers()) {
            StringBuffer sb = new StringBuffer();
            for (Map container : ruleInputTemp.getContainers()) {
                String containerId = container.get("id").toString();
                //获取container中的条件数据，解析每一个条件数据
                List<Map> definitionList = (List<Map>) container.get("child");
                for (Map definitionMap : definitionList) {
                    DefinitionTemp definition = new DefinitionTemp();
                    definition.setDefinitionId(definitionMap.get("definitionId").toString());
                    definition.setParameters((Map) definitionMap.get("parameters"));
                    ModelTemp modelTemp = this.transformCondition(definition, modelSet, containerId);
                    //拼接条件对应的查询String
                    sb.append(modelTemp.getInstance() + "_set")
                            .append(": Set(size()>0) from accumulate (")
                            .append(ConditionUtil.joinModel(modelTemp, " && ").replace(":=", ":"))
                            .append(",collectSet(")
                            .append(modelTemp.getInstance() + "))\n");
                    modelList.add(modelTemp);
                }
            }
            ruleTemp.setContainer(sb.toString());
        }
        //解析分组数据
        if (null != ruleInputTemp.getGroups()) {
            List<String> groupsString = new ArrayList<>();
            for (Map group : ruleInputTemp.getGroups()) {
                String groupString = this.groupToString(group, modelSet, "");
                groupsString.add(groupString);
            }
            //获得模型组的字符串代码
            String groups = ConditionUtil.joinStringBySeparator(groupsString, " and ");
            ruleTemp.setGroups(groups);
        }

        //解析conditions
        for (DefinitionTemp condition : ruleInputTemp.getConditions()) {
            //把条件转成模型
            ModelTemp modelTemp = this.transformCondition(condition, modelSet, "");
            modelList.add(modelTemp);
        }


        //解析actions
        Set<String> actionModelId = new HashSet<>();
        for (DefinitionTemp action : ruleInputTemp.getActions()) {
            //查询前台选择结果definitionId对应的促销结果定义数据
            Map definitionMap = definitionDao.select(action.getDefinitionId());
            if (definitionMap.get("type").equals("action")) {
                //查询结果对应的action数据
                List<Map<String, ?>> actions = actionDao.selectByEqField("definitionId", action.getDefinitionId());
                for (int i = 0; i < actions.size(); i++) {
                    ActionTemp actionTemp = new ActionTemp();
                    BeanUtils.populate(actionTemp, actions.get(i));
                    if (null != actions.get(i).get("actionData")) {
                        //action中actionData为空 说明结果对应的优惠数据（涉及的数据字段大于一个）较复杂
                        if (actions.get(i).get("actionData").toString().equals("")) {
                            //将促销优惠数据保存到actionData表中，关联促销活动的主键和action的definitionId，将促销活动主键作为入参传给促销处理函数
                            String actionData = ruleInputTemp.getActivity() == null ? ruleInputTemp.getCoupon().getId() : ruleInputTemp.getActivity().getId();
                            actionTemp.setActionData(actionData);
                            Map<String, Object> actionContainer = new HashedMap();
                            actionContainer.put("id", UUID.randomUUID().toString());
                            actionContainer.put("activityId", actionTemp.getActionData());
                            actionContainer.put("definitionId", actionTemp.getDefinitionId());
                            actionContainer.put("parameters", action.getMapValue(actions.get(i).get("actionData").toString()));
                            actionDataDao.add(actionContainer);
                        } else {
                            //促销优惠数据中只有一个字段（如订单减X元），将该数据作为参数传给促销处理函数
                            if (action.getMapValue(actions.get(i).get("actionData").toString()) != null) {
                                actionTemp.setActionData(action.getMapValue(actions.get(i).get("actionData").toString()).toString());
                            } else {
                                actionTemp.setActionData(null);
                            }
                        }
                    }
                    actionList.add(actionTemp);
                    //判断是否在条件中模型集合中，如果不在就加到actionModelId中
                    if (!modelSet.contains(actionTemp.getModelId())) {
                        actionModelId.add(actionTemp.getModelId());
                    }
                }
            }
        }
        ruleTemp.setActions(actionList);

        //把action多出的模型加上
        for (String modelId : actionModelId) {
            ModelTemp modelTemp = this.getModel(modelId, "", null);
            modelList.add(modelTemp);
        }
        ruleTemp.setModels(modelList);


        //获取优惠券对象,如果优惠券对象不为空就创建一个优惠券规则，否则创建促销活动规则
        Coupon coupon = ruleInputTemp.getCoupon();
        Map<String, Object> ruleMap = new HashedMap();
        if (null != coupon) {
            //规则的属性对象赋值
            RuleAttributeTemp attribute = new RuleAttributeTemp();
            attribute.setStartDate(coupon.getStartDate());
            attribute.setEndDate(coupon.getEndDate());

            attribute.setRuleType("coupon");
            ruleTemp.setAttribute(attribute);
            //规则文件的包名和规则名
            ruleTemp.setRuleNum(coupon.getId());
            ruleMap.put("ruleId", UUID.randomUUID().toString());
            ruleMap.put("couponId", coupon.getId());
            ruleMap.put("ruleType", "coupon");
            ruleMap.put("rule", ruleTemp.getRulesTemp());
            System.out.println(ruleTemp.getRulesTemp());
            ruleDao.add(ruleMap);
        }
        if (null != ruleInputTemp.getActivity()) {
            RuleAttributeTemp attribute = new RuleAttributeTemp();
            attribute.setStartDate(ruleInputTemp.getActivity().getStartDate());
            attribute.setEndDate(ruleInputTemp.getActivity().getEndDate());
            attribute.setGroup(ruleInputTemp.getActivity().getGroup());
            attribute.setRuleType("activity");
            attribute.setIsOverlay(ruleInputTemp.getActivity().getIsOverlay());
            attribute.setPriority(ruleInputTemp.getActivity().getPriority());
            ruleTemp.setAttribute(attribute);
            //规则文件的包名和规则名
            ruleTemp.setRuleNum(ruleInputTemp.getActivity().getId());
            ruleMap.put("ruleId", UUID.randomUUID().toString());
            ruleMap.put("activityId", ruleInputTemp.getActivity().getId());
            ruleMap.put("ruleType", "activity");
            //ruleTemp.getRulesTemp()该方法将DTO拼接成对应的drools文件
            ruleMap.put("rule", ruleTemp.getRulesTemp());
            ruleDao.add(ruleMap);
        }
        return new ResponseData(ruleMap);
    }

    /**
     * 根据优惠券主键 将drools规则文件达成jar包，加载到内存中
     * @param couponId
     * @return
     */
    @Override
    public ResponseData releaseCoupon(String couponId) {
        Map couponMap = saleCouponDao.select(couponId);
        ResponseData responseData = new ResponseData();
        if (couponMap != null) {
            //查询出drools数据
            List<Map<String, ?>> ruleMapList = ruleDao.selectByEqField("couponId", couponId);
            if (ruleMapList != null) {
                if (ruleMapList.size() > 0) {
                    Map ruleMap = ruleMapList.get(0);
                    KieServices kieServices = KieServices.Factory.get();
                    logger.info("-----mmmmmm-------优惠券规则--------------\n{}", ruleMap.get("rule"));
                    //将drools数据封装成kie接受的resource
                    ResourceWrapper resourceWrapper = new ResourceWrapper(ResourceFactory.newByteArrayResource(ruleMap.get("rule").toString().getBytes()), ruleMap.get("couponId").toString() + ".drl");
                    DroolsUtils droolsUtils = new DroolsUtils();
                    //获取规则对应的kie中的版本id
                    ReleaseId releaseId = kieServices.newReleaseId("com.hand.hmall", couponMap.get("couponId").toString(), couponMap.get("releaseId").toString());
                    try {
                        //打jar包动态发布规则
                        droolsUtils.createKieJarByStream(kieServices, releaseId, resourceWrapper);
                    } catch (Exception e) {
                        e.printStackTrace();
                        responseData.setSuccess(false);
                        responseData.setMsg("【" + couponMap.get("couponName").toString() + "】 错误，请检验后重新启用");
                    }
                }
            }

        }
        return responseData;
    }


    /**
     * 在drools引擎中删除coupon
     *
     * @param couponId
     */
    @Override
    public void removeCoupon(String couponId) {
        try {
            Map couponMap = saleCouponDao.select(couponId);
            if (couponMap != null) {
                KieServices kieServices = KieServices.Factory.get();
                DroolsUtils droolsUtils = new DroolsUtils();
                ReleaseId releaseId = kieServices.newReleaseId("com.hand.hmall", couponMap.get("couponId").toString(), couponMap.get("releaseId").toString());
                droolsUtils.removeKJar(kieServices, releaseId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发布促销，将drools加载到规则引擎中
     *
     * @param activityMap
     * @return
     */
    @Override
    public ResponseData releaseActivity(Map<String, Object> activityMap) {

        ResponseData responseData = new ResponseData();
//        Map activityMap = saleActivityDao.select(id);
        if (activityMap != null) {
            //获取所有的促销规则
            List<Map<String, ?>> ruleMapList = ruleDao.selectByEqField("activityId", activityMap.get("id").toString());
            if (ruleMapList != null) {
                if (ruleMapList.size() > 0) {
                    Map ruleMap = ruleMapList.get(0);
                    KieServices kieServices = KieServices.Factory.get();
                    //输出生成的规则内容
                    logger.info("-------------生成促销规则--------------\n{}", ruleMap.get("rule"));
                    //加载从数据库中读取的.drl文件
                    ResourceWrapper resourceWrapper = new ResourceWrapper(ResourceFactory.newByteArrayResource(ruleMap.get("rule").toString().getBytes()), ruleMap.get("activityId").toString() + ".drl");
                    DroolsUtils droolsUtils = new DroolsUtils();
                    ReleaseId releaseId = kieServices.newReleaseId("com.hand.hmall", activityMap.get("id").toString(), activityMap.get("activityId").toString());
                    try {
                        //动态创建规则(由于前面加了定时任务,每隔一段时间会扫描一次促销规则,把原有的drl包含进新建的kjar中)
                        droolsUtils.createKieJarByStream(kieServices, releaseId, resourceWrapper);
                    } catch (Exception e) {
                        e.printStackTrace();
                        responseData.setSuccess(false);
                        responseData.setMsg("【" + activityMap.get("activityName").toString() + "】 错误，请检验后重新启用");
                    }
                }
            }

        }
        return responseData;

    }

    /**
     * 规则引擎中删除促销规则
     *
     * @param activityId
     */
    @Override
    public void removeActivity(String id,String activityId) {
        try {
            KieServices kieServices = KieServices.Factory.get();
            DroolsUtils droolsUtils = new DroolsUtils();
            ReleaseId release = kieServices.newReleaseId("com.hand.hmall", id, activityId);
            KieSessionUtil.removeActivityContainerMapping(id + activityId);
            droolsUtils.removeKJar(kieServices, release);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 组装换成字符串
     *
     * @param group
     * @param modelSet
     * @param containerId
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public String groupToString(Map group, Set<String> modelSet, String containerId) throws InvocationTargetException, IllegalAccessException {
        String operator = group.get("operator").toString();
        List<Map> definitionList = (List<Map>) group.get("child");
        List<String> modelListString = new ArrayList<>();
        for (Map map : definitionList) {
            if (!map.get("definitionId").equals("GROUP")) {
                DefinitionTemp definition = new DefinitionTemp();
                definition.setDefinitionId(map.get("definitionId").toString());
                definition.setParameters((Map) map.get("parameters"));
                ModelTemp modelTemp = this.transformCondition(definition, modelSet, containerId);
                String modelString = ConditionUtil.joinModel(modelTemp, " && ");
                modelListString.add(modelString);
            } else {
                if (null != map.get("child")) {
                    //递归
                    String groupString = this.groupToString(map, modelSet, "");
                    modelListString.add(groupString);
                }
            }
        }
        return "(" + ConditionUtil.joinStringBySeparator(modelListString, operator) + ")\n";
    }


    /**
     * 把前台传来的数据装换成模型
     *
     * @param definition
     * @param modelSet
     * @param containerId
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    protected ModelTemp transformCondition(DefinitionTemp definition, Set<String> modelSet, String containerId) throws InvocationTargetException, IllegalAccessException {
        //查询促销预定义的条件，目的是获取
        Map definitionMap = definitionDao.select(definition.getDefinitionId());//查找条件对应的Field（用于解析Condition中的数值）数据
        List<Field> fieldList = new ArrayList<>();//保存Field Map参数对应的DTO集合
        String modelId = "";//定义的促销条件类型是condition，继续解析
        if (definitionMap.get("type").equals("condition")) {
            //根据预定义的条件Id获取解析条件所需的Field记录。（如订单满3000元，Field数据保存了3000在json中的存储路径，以及比较操作符>=）
            List<Map<String, ?>> fields = fieldTempDao.selectByEqField("definitionId", definition.getDefinitionId());
            for (int i = 0; i < fields.size(); i++) {
                Map fieldMap = fields.get(i);
                if (null != definition.getMapValue(fieldMap.get("operator").toString())) {
                    Field field = new Field();//Field Dto
                    BeanUtils.populate(field, fieldMap);
                    //解析fieldMap中的operator和value获取从definition得到操作符的路径。
                    field.setOperator(OperatorMenu.valueOf(definition.getMapValue(fieldMap.get("operator").toString()).toString()).getValue());
                    //促销条件中输入参数的数据类型，如果字段类型是String，需要给数据加上双引号（以商品类别为例，条件中会保存选中类别的集合，对应List<String>;订单满x元对应数值的类型是Integer）
                    if (field.getType().equals("String") || field.getType().equals("List<String>")) {
                        //如果操作符是collectOperator中的一个
                        if (Arrays.asList(collectOperator).contains(field.getOperator())) {
                            //判断传过来的数据是不是list，如果不是抛出类型转化错误
                            ArrayList<String> values = (ArrayList) definition.getMapValue(fieldMap.get("value").toString());//处理List中的元素的数据类型
                            for (int index = 0; index < values.size(); index++) {
                                Object obj = values.get(index);
                                if (obj instanceof Integer) {
                                    values.set(index, "\"" + obj.toString() + "\"");
                                } else if (obj instanceof Long) {
                                    values.set(index, "\"" + (Long) obj + "\"");
                                } else {
                                    values.set(index, "\"" + values.get(index) + "\"");
                                }

                            }
                            field.setValue(values.toString());
                        } else {
                            field.setValue("\"" + definition.getMapValue(fieldMap.get("value").toString()).toString() + "\"");
                        }
                    } else {
                        if (field.getOperator().equals("memberOf")) {
                            //判断传过来的数据是不是list，如果不是抛出类型转化错误
                            ArrayList values = (ArrayList) definition.getMapValue(fieldMap.get("value").toString());
                            field.setValue(values.toString());
                        } else {
                            field.setValue(definition.getMapValue(fieldMap.get("value").toString()).toString());
                        }
                    }

                    fieldList.add(field);
                    modelId = field.getModelId();//保存Field对应的Model的Id
                    modelSet.add(modelId);
                }
            }
        }
        return modelId.equals("") ? null : this.getModel(modelId, containerId, fieldList);
    }


    /**
     * 模型的详细信息，一个Condition参数对应了一个Model Dto 。
     *
     * @param modelId
     * @param containerId
     * @param fieldList
     * @return
     */
    protected ModelTemp getModel(String modelId, String containerId, List<Field> fieldList) {
        //组装模型信息，模型保存的drl规则会涉及到的Fact对象的信息。包括类路径，类名，在drl文件中引用的名称
        Map modelMap = modelTempDao.select(modelId);
        ModelTemp modelTemp = new ModelTemp();
        modelTemp.setModelId(modelMap.get("modelId").toString());
        modelTemp.setModelName(modelMap.get("modelName").toString());
        modelTemp.setClassName(modelMap.get("className").toString());
        if (containerId.equals("")) {//根据containerId判断解析的是容器还是条件参数，拼接不同的参数名称。
            modelTemp.setInstance("$" + modelMap.get("modelName").toString());//instanc为drl中的变量名称
        } else {
            modelTemp.setInstance("$H_" + containerId.replace("-", "_"));
        }
        modelTemp.setContainerId(containerId);
        modelTemp.setFields(fieldList);
        return modelTemp;
    }
}