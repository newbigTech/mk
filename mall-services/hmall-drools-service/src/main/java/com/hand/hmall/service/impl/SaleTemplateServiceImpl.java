package com.hand.hmall.service.impl;

import com.hand.hmall.dao.SaleConditionActionDao;
import com.hand.hmall.dao.SaleOperatorDao;
import com.hand.hmall.dao.SaleTemplateDao;
import com.hand.hmall.dto.PagedValues;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.menu.SaleType;
import com.hand.hmall.service.ISaleTemplateService;
import com.hand.hmall.util.DateFormatUtil;
import com.hand.hmall.util.ResponseReturnUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shanks on 2017/1/4.
 * 促销模板service实现类
 */
@Service
public class SaleTemplateServiceImpl implements ISaleTemplateService {
    @Autowired
    private SaleTemplateDao saleTemplateDao;

    @Autowired
    private SaleConditionActionDao saleConditionActionDao;
    @Autowired
    private SaleOperatorDao saleOperatorDao;

    /**
     * 分页查询所有促销模板
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData query(Map<String, Object> map) {
        PagedValues pagedValues = saleTemplateDao.querySaleTemplate(map);
        ResponseData responseData = new ResponseData();
        responseData.setResp(pagedValues.getValues());
        responseData.setTotal((int) pagedValues.getTotal());
        return responseData;
    }

    /**
     * 保存促销模板
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData submit(Map<String, Object> map) {


        try {

            List<String> checked = checkedTemplate(map);

            if (!checked.isEmpty()) {
                StringBuilder stringBuilder = new StringBuilder();
                for (String s : checked) {
                    stringBuilder.append(s + "</p>");
                }
                ResponseData responseData = new ResponseData();
                responseData.setSuccess(false);
                responseData.setMsg(stringBuilder.toString());
                return responseData;
            }


            Map<String, Object> template = (Map<String, Object>) map.get("template");
            Map<String, Object> conditionsActions = new HashMap<>();
            conditionsActions.put("conditions", map.get("conditions"));
            conditionsActions.put("actions", map.get("actions"));
            conditionsActions.put("groups", map.get("groups"));
            conditionsActions.put("containers", map.get("containers"));

//            SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date creationDate = new Date(System.currentTimeMillis());
            Long creationTime = creationDate.getTime();

            if (template.get("templateId") == null || template.get("templateId").equals("")) {


                template.put("creationTime", creationTime);
                template.put("templateId", UUID.randomUUID());
                template.put("releaseId", UUID.randomUUID());
                template.put("lastCreationTime", creationTime);
                template.put("isUsing", 'Y');
                saleTemplateDao.submitSaleTemplate(template);

                conditionsActions.put("detailId", template.get("id"));
                conditionsActions.put("containerFlag", map.get("containerFlag"));
                conditionsActions.put("type", SaleType.TEMPLATE.getValue());
                saleConditionActionDao.submitSaleCondition(conditionsActions);

                //添加版本
                Map<String, Object> operatorMap = new HashMap<>();
                operatorMap.put("operator", map.get("userId"));
                operatorMap.put("operation", template.get("templateDes"));
                operatorMap.put("changeDate", creationTime);
                operatorMap.put("type", SaleType.TEMPLATE.getValue());
                operatorMap.put("baseId", template.get("templateId"));
                operatorMap.put("parentId", template.get("releaseId"));
                saleOperatorDao.submit(operatorMap);

            } else {
                template.put("lastCreationTime", DateFormatUtil.stringToTimeStamp(template.get("creationTime").toString()));
                template.put("creationTime", creationTime);
                template.put("releaseId", UUID.randomUUID());
                template.put("isUsing", "Y");

                List<Map<String, ?>> templates = saleTemplateDao.selectByTemplateId(template.get("templateId").toString());
                for (int i = 0; i < templates.size(); i++) {
                    Map<String, Object> updateTemp = (Map<String, Object>) templates.get(i);
                    if (updateTemp.get("isUsing") != null) {
                        updateTemp.put("isUsing", 'N');
                    }
                    saleTemplateDao.updateSaleTemplate(updateTemp);
                }
                saleTemplateDao.submitSaleTemplate(template);


                conditionsActions.put("detailId", template.get("id"));
                conditionsActions.put("containerFlag", map.get("containerFlag"));
                conditionsActions.put("type", SaleType.TEMPLATE.getValue());
                saleConditionActionDao.submitSaleCondition(conditionsActions);

                //添加版本
                Map<String, Object> operatorMap = new HashMap<>();
                operatorMap.put("operator", map.get("userId"));
                operatorMap.put("changeDate", creationTime);
                operatorMap.put("operation", template.get("templateDes"));
                operatorMap.put("type", SaleType.TEMPLATE.getValue());
                operatorMap.put("baseId", template.get("templateId"));
                operatorMap.put("parentId", template.get("releaseId"));
                saleOperatorDao.submit(operatorMap);
            }

            return new ResponseData(template);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseReturnUtil.returnFalseResponse("存值失败", null);
    }

    /**
     * 删除促销模板
     *
     * @param maps
     * @return
     */
    @Override
    public ResponseData delete(List<Map<String, Object>> maps) {
        for (Map<String, Object> map : maps) {
//            删除模板描述信息
            saleTemplateDao.deleteSaleTemplate(map);
            Map<String, Object> conditionActionMap = saleConditionActionDao.selectByDetailIdAndType(map.get("id").toString(), SaleType.TEMPLATE.getValue());
//            删除模板条件结果数据
            saleConditionActionDao.delete(conditionActionMap.get("id").toString());

            List<Map<String, ?>> operators = saleOperatorDao.selectByBaseId(map.get("templateId").toString());
            for (Map<String, ?> operator : operators) {
                saleOperatorDao.delete(operator.get("id").toString());
            }
        }
        return new ResponseData(maps);
    }

    /**
     * 根据模板Id查询模板的详细信息。
     *
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> selectTemplateDetail(String id) {
        Map<String, Object> template = (Map<String, Object>) saleTemplateDao.selectTemplateDetail(id);
        Map<String, Object> conditionActions = (Map<String, Object>) saleConditionActionDao.selectByDetailIdAndType(id, SaleType.TEMPLATE.getValue());
//        template.put("creationTime",new Date(Long.parseLong(template.get("creationTime").toString())));
//        template.put("lastCreationTime",new Date(Long.parseLong(template.get("lastCreationTime").toString())));

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("template", template);
        if (conditionActions.get("conditions") != null) {
            map.put("conditions", conditionActions.get("conditions"));
        }
        if (conditionActions.get("actions") != null) {
            map.put("actions", conditionActions.get("actions"));
        }
        if (conditionActions.get("groups") != null) {
            map.put("groups", conditionActions.get("groups"));
        }
        if (conditionActions.get("containers") != null) {
            map.put("containers", conditionActions.get("containers"));
        }
        if (conditionActions.get("id") != null) {
            map.put("conditionsId", conditionActions.get("id"));
        }
        return map;


    }

    /**
     * 校验促销模板字段是否合法，（模板名称）templateName不能为空，且不能存在非法字符
     *
     * @param map
     * @return
     */
    @Override
    public List<String> checkedTemplate(Map<String, Object> map) {
        List<String> message = new ArrayList<>();

        Map<String, Object> data = (Map<String, Object>) map.get("template");
        if (data.get("templateName") == null || data.get("templateName").toString().trim().equals("")) {
            message.add("模板名称不能为空");
        } else {
            String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(data.get("templateName").toString());
            if (m.find()) {
                message.add("模板名称存在非法字符");
            }
        }
        return message;
    }


}
