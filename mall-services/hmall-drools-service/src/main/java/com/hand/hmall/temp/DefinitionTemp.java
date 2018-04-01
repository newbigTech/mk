package com.hand.hmall.temp;


import java.util.List;
import java.util.Map;

/**
 * 条件（condition）/结果(action)/容器(container)对应DTO
 * Created by hand on 2017/1/17.
 */
public class DefinitionTemp {

    private String definitionId;

    private String id;

    private Map<String,Map> parameters;

    private String meaning;

    private List<Map> children;


    public String getDefinitionId() {
        return definitionId;
    }

    public void setDefinitionId(String definitionId) {
        this.definitionId = definitionId;
    }

    public Map<String, Map> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Map> parameters) {
        this.parameters = parameters;
    }

    public List<Map> getChildren() {
        return children;
    }

    public void setChildren(List<Map> children) {
        this.children = children;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    /**
     * 获取key路径下param里的值，
     *
     * @param key 保存JSON每一个层级的一个字段，用 ‘.’号分隔
     * @return
     */
    public Object getMapValue(String key) {//
        //如果
        if (key.equals("")){
            return parameters;
        }

        String[] keys = key.split("\\.");
        Map param = parameters;
        if(parameters==null){
            return null;
        }
        //具体值前的层级都是jsonObject能用Map保存
        for (int i = 0; i<=keys.length-2; i++) {
            if (param.get(keys[i])==null){
                return null;
            }
            try {
                param = (Map) param.get(keys[i]);
            }catch (ClassCastException e){
                e.printStackTrace();
            }
        }
        //获取具体的值
        return param.get(keys[keys.length-1]);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
