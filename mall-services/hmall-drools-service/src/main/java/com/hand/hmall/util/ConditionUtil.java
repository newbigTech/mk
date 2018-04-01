package com.hand.hmall.util;

import com.hand.hmall.temp.Field;
import com.hand.hmall.temp.ModelTemp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hand on 2017/3/13.
 */
public class ConditionUtil {

    /**
     * 把模型的字段数据用连接符连接起来
     * @param model 模型
     * @param separator 连接符
     * @return  drl文件条件脚本
     */
    public static String joinModel(ModelTemp model, String separator) {
        //需要取交集的字段
        List<Field> intersectionField = new ArrayList<>();
        StringBuffer s = new StringBuffer();
        s.append( model.getInstance() + ":=" + model.getModelName() + "(");
        //连接模型的各个字段的判断
        if ( null!=model.getFields()) {
            for (int i = 0; i < model.getFields().size() ; i++) {

                if (model.getFields().get(i).getOperator().equals("memberOf") && model.getFields().get(i).getType().equals("List<String>")){

                    String fieldName= model.getFields().get(i).getFieldName();
                    String[] fieldNameList=fieldName.split("\\.");
                    if(fieldNameList.length>1){
                        fieldName="";
                        for(int k =0;k<fieldNameList.length;k++) {
                            if(k!=fieldNameList.length-1) {
                                fieldName += fieldNameList[k] + "_";
                            }else{
                                fieldName+=fieldNameList[k];
                            }
                        }

                    }
                    s.append( "$"+ fieldName +" : "+ model.getFields().get(i).getFieldName());
                    intersectionField.add(model.getFields().get(i));
                }else {
                    if ("o_type_range_number".equals(model.getFields().get(i).getFieldId())) {
                        s.append(model.getFields().get(i).getFieldName() + " " + model.getFields().get(i).getOperator() + " " + 1);
                    } else {
                        s.append(model.getFields().get(i).getFieldName() + " " + model.getFields().get(i).getOperator() + " " + model.getFields().get(i).getValue());
                    }
                }
                //如果不是最后一个就加上连接符
                if (i != model.getFields().size()-1){
                    s.append(separator);
                }
            }
        }
        s.append( ")\n");
        for (int i=0; i<intersectionField.size();i++){
            //取交集的字段不为空的逻辑
            s.append("and\n");
            //变量名和类型

            String fieldName=intersectionField.get(i).getFieldName();
            String[] fieldNameList=fieldName.split("\\.");
            if(fieldNameList.length>1){
                fieldName="";
                for(int k =0;k<fieldNameList.length;k++) {
                    if(k!=fieldNameList.length-1) {
                        fieldName += fieldNameList[k] + "_";
                    }else{
                        fieldName+=fieldNameList[k];
                    }
                }

            }
            s.append(model.getInstance()+"_"+fieldName+"_item:String");
            //模型变量的值和指定的集合是否存在交集
            s.append("(toString() "+ intersectionField.get(i).getOperator() + " " + intersectionField.get(i).getValue() +")");
            s.append("from $"+ fieldName+"\n");
        }
        return s.toString();
    }

    /**
     * 把多个String用连接符连起来
     * @param stringList
     * @param separator 连接符
     * @return 连接后的整条字符串
     */
    public static String joinStringBySeparator(List<String> stringList, String separator){
        StringBuffer s = new StringBuffer();
        for (int i=0; i<=stringList.size()-1; i++){
            s.append(stringList.get(i));
            //如果是stringList的最后一个，就不用加连接符
            if(i!=stringList.size()-1){
                s.append(separator+"\n");
            }
        }
        return s.toString();
    }

    public static String getFieldValue(ModelTemp model, String fieldId) {
        List<Field> fields = model.getFields();
        for (Field field : fields) {
            if (fieldId.equals(field.getFieldId())) {
                return field.getOperator() + field.getValue();
            }
        }
        return null;
    }

}