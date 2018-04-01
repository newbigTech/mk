package com.hand.hmall.temp;


import com.hand.hmall.util.ConditionUtil;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;


public class RuleTemp {
    private String ruleTempId;

    private String ruleNum;
    //属性
    private RuleAttributeTemp attribute;
    //模型
    private List<ModelTemp> models;

    private String groups;

    private String container;

    private List<ActionTemp> actions;

    public String getRuleTempId() {
        return ruleTempId;
    }

    public void setRuleTempId(String ruleTempId) {
        ruleTempId = ruleTempId;
    }

    public String getRuleNum() {
        return ruleNum;
    }

    public void setRuleNum(String ruleNum) {
        this.ruleNum = ruleNum;
    }

    public List<ModelTemp> getModels() {
        return models;
    }

    public void setModels(List<ModelTemp> models) {
        this.models = models;
    }

    public List<ActionTemp> getActions() {
        return actions;
    }

    public void setActions(List<ActionTemp> actions) {
        this.actions = actions;
    }

    public RuleAttributeTemp getAttribute() {
        return attribute;
    }

    public void setAttribute(RuleAttributeTemp attribute) {
        this.attribute = attribute;
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    /**
     * 拼接规则文件（书写的过程）
     */

    public String getRulesTemp() {
        //定义规则生成的模板
        StringBuffer orderRulesTmp = new StringBuffer();
        orderRulesTmp.append("package com.hand.hmall.H_" + ruleNum.replace("-", "_") + ";\n");
        //导入模型
        orderRulesTmp.append("import com.hand.hmall.pojo.OrderPojo;\n");
        orderRulesTmp.append("import com.hand.hmall.pojo.OrderEntryPojo;\n");

        //导入工具类
        orderRulesTmp.append("import java.util.Map;\n");
        orderRulesTmp.append("import java.util.HashMap;\n");
        orderRulesTmp.append("import java.util.List;\n");
        orderRulesTmp.append("import java.util.ArrayList;\n");
        orderRulesTmp.append("import java.util.Set;\n");
        orderRulesTmp.append("import java.util.HashSet;\n");
        //结果服务接口
        orderRulesTmp.append("import com.hand.hmall.service.IActionService;\n" +
                "import com.hand.hmall.service.impl.ActionServiceImpl;\n");
        orderRulesTmp.append("global com.hand.hmall.service.IActionService actionService;\n");
        if (attribute.getRuleType().equals("activity")) {
            orderRulesTmp.append("global com.hand.hmall.dto.ResponseData responseData;\n");
        }

        //query,在工作内存中查询所有符合的模型
        orderRulesTmp.append("query query_" + ruleNum.replace("-", "_") + "()\n");
        orderRulesTmp.append("(\n");
        orderRulesTmp.append(getQuery(models, groups, "and"));
        orderRulesTmp.append(")\nend\n");

        //规则名
        orderRulesTmp.append("rule H_" + ruleNum.replace("-", "_") + "\n");
        //是否 执行只一次
        orderRulesTmp.append("lock-on-active true\n");
//        orderRulesTmp.append( "no-loop true\n");
        //方言
        orderRulesTmp.append("dialect \"mvel\" \n");
        //优先级

        orderRulesTmp.append("salience -" + attribute.getPriority() + "\n");
        //设置分组
//        if (attribute.getRuleType().equals("activity")) {
//            if (attribute.getIsOverlay().equals("N")) {
//                orderRulesTmp.append( "activation-group \"" + attribute.getGroup() + "\"\n");
//            }
//            orderRulesTmp.append( "agenda-group \"" + attribute.getGroup() + "\"\n");
//        }
        //有效时间
//        orderRulesTmp.append( "date-effective \""+attribute.getStartDate()+"\"\n");
//        orderRulesTmp.append( "date-expires \""+attribute.getEndDate()+"\"\n");
        if (attribute.getRuleType().equals("coupon")) {
            orderRulesTmp.append("date-effective \"" + attribute.getStartDate() + "\"\n");
            orderRulesTmp.append("date-expires \"" + attribute.getEndDate() + "\"\n");
        }

        orderRulesTmp.append("   when\n        ");
        if (null != container && !container.equals("")) {
            orderRulesTmp.append(container);
        }
        Map<String, ModelTemp> modelMap = new HashMap();
        for (ModelTemp model : models) {
            if (modelMap.containsKey(model.getInstance())) {
                ModelTemp modelTemp = modelMap.get(model.getInstance());
                List<Field> fields = modelTemp.getFields();
                for (Field field : model.getFields()) {
                    fields.add(field);
                }
            } else {
                modelMap.put(model.getInstance(), model);
            }
        }
        for (ModelTemp model : modelMap.values()) {
            if (model.getContainerId().equals("")) {
                String definitionId = CollectionUtils.isNotEmpty(model.getFields()) ? model.getFields().get(0).getDefinitionId() : "";
                String query = model.getInstance() + "_set:Set(size()>0) from accumulate (query_" + ruleNum.replace("-", "_") + "() and " + ConditionUtil.joinModel(model, "&&").replace(":=", ":") + ",collectSet(" + model.getInstance() + "))\n";
                orderRulesTmp.append(query);
                if ("o_type_range".equals(definitionId)) {
                    String joinStr = ConditionUtil.joinModel(model, "&&").replace(":=", ":").replace("])", "],$q:quantity)");
                    joinStr.replace("])", "],$q:quantity)");
                    orderRulesTmp.append("$num:Number($num" + ConditionUtil.getFieldValue(model, "o_type_range_number") + ") from accumulate (" + joinStr + ",sum($q))\n");
                }
            }
        }
        orderRulesTmp.append("   then\n     ");
        //把所有的模型的实例存到map中去
        orderRulesTmp.append("      Map variables = new HashMap();\n");
        orderRulesTmp.append("       variables.put(\"com.hand.hmall.ruleType\",\"" + attribute.getRuleType() + "\");\n");
        if (attribute.getRuleType().equals("activity")) {
            orderRulesTmp.append("       variables.put(\"com.hand.hmall.dto.ResponseData\",responseData);\n");
        }

        Set<String> checkedInstance = new HashSet<>();
        for (ModelTemp model : models) {
            if (model.getContainerId().equals("")) {
                if (!checkedInstance.contains("model.getInstance()")) {
                    orderRulesTmp.append("       variables.put(\"" + model.getClassName() + "s\"," + model.getInstance() + "_set);\n       ");
                    checkedInstance.add(model.getInstance());
                }
            } else {
                if (!checkedInstance.contains("model.getInstance()")) {
                    orderRulesTmp.append("       variables.put(\"" + model.getContainerId() + "\"," + model.getInstance() + "_set);\n       ");
                    checkedInstance.add(model.getInstance());
                }
            }
        }
        //组装结果
        for (ActionTemp action : actions) {
            orderRulesTmp.append(getAction(action));
        }
        //满足条件就修改订单对象的checkActivity或checkActivity的值
        if (attribute.getRuleType().equals("coupon")) {
            orderRulesTmp.append("actionService.checkedCoupon(variables);\n");
        } else {
            orderRulesTmp.append("if(responseData.isSuccess()){\n");
            orderRulesTmp.append("actionService.checkedActivity(variables,\"" + ruleNum + "\");\n");
            orderRulesTmp.append("}\n");
        }

        orderRulesTmp.append("end\n");
        return orderRulesTmp.toString();
    }

    protected String getAction(ActionTemp actionTemp) {
        String action = actionTemp.getActionCode();
        if (null != actionTemp.getActionData()) {
            //替换占位符
            action = action.replace(actionTemp.getDataVariable(), actionTemp.getActionData());
        }
        return action;
    }


    protected String getQuery(List<ModelTemp> models, String groups, String relationOperator) {
        //每个模型对应的条件
        List<String> modelsString = new ArrayList<>();
        if (null != groups && !groups.equals("")) {
            modelsString.add(groups);
        }
        for (ModelTemp model : models) {
            //如果不是容器对应的模型
            if (model.getContainerId().equals("")) {
                //解析每个模型
                String modelString = ConditionUtil.joinModel(model, " && ");
                modelsString.add(modelString);
            }
        }
        return ConditionUtil.joinStringBySeparator(modelsString, relationOperator);
    }
}