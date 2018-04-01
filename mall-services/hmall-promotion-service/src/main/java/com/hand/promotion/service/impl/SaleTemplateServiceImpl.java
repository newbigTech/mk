package com.hand.promotion.service.impl;


import com.hand.dto.ResponseData;
import com.hand.promotion.dao.SaleOperatorDao;
import com.hand.promotion.dao.SaleTemplateDao;
import com.hand.promotion.pojo.SaleOperatorPojo;
import com.hand.promotion.pojo.activity.SaleTemplateDesp;
import com.hand.promotion.pojo.activity.SaleTemplatePojo;
import com.hand.promotion.pojo.enums.PromotionConstants;
import com.hand.promotion.service.ISaleTemplateService;
import com.hand.promotion.util.BeanMapExchange;
import com.hand.promotion.util.ResponseReturnUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xinyang.Mei on 2017/1/4.
 * 促销模板service实现类
 */
@Service
public class SaleTemplateServiceImpl implements ISaleTemplateService {
    @Autowired
    private SaleTemplateDao saleTemplateDao;

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
        int pageSize = (int) map.get("pageSize");
        int pageNum = (int) map.get("page");
        Map data = (Map) map.get("data");
        SaleTemplatePojo condition = new SaleTemplatePojo();
        SaleTemplateDesp template = new SaleTemplateDesp();
        template.setTemplateName((String) data.get("templateName"));
        template.setTemplateId((String) data.get("templateId"));
        condition.setTemplate(template);
        saleTemplateDao.queryTemplateDespByCondition(condition, pageNum, pageSize);
        return saleTemplateDao.queryTemplateDespByCondition(condition, pageNum, pageSize);
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


            Date creationDate = new Date(System.currentTimeMillis());
            Long creationTime = creationDate.getTime();

            //将入参转换为对应DTO
            SaleTemplatePojo saleTemplatePojo = BeanMapExchange.mapToObject(map, SaleTemplatePojo.class);
            SaleTemplateDesp template = saleTemplatePojo.getTemplate();

            if (StringUtils.isEmpty(template.getTemplateId())) {
                template.setId(UUID.randomUUID().toString());
                template.setTemplateId(UUID.randomUUID().toString());
                template.setReleaseId(UUID.randomUUID().toString());
                template.setCreationTime(creationTime);
                template.setLastCreationTime(creationTime);
                template.setIsUsing("Y");
                saleTemplatePojo.setId(template.getId());
                saleTemplateDao.insertPojo(saleTemplatePojo);

                //添加版本
                SaleOperatorPojo operatorPojo = new SaleOperatorPojo();
                operatorPojo.setOperator(map.get("userId").toString());
                operatorPojo.setBaseId(template.getTemplateId());
                operatorPojo.setOperation(template.getTemplateDes());
                operatorPojo.setChangeDate(creationTime);
                operatorPojo.setType("TEMPLATE");

                saleOperatorDao.insertPojo(operatorPojo);

            } else {
                template.setId(UUID.randomUUID().toString());
                template.setLastCreationTime(template.getCreationTime());
                template.setCreationTime(creationTime);
                template.setReleaseId(UUID.randomUUID().toString());
                template.setIsUsing("Y");
                saleTemplatePojo.setId(template.getId());

                //查询出修改前最新状态的促销模板，更新其isUsing为N
                SaleTemplatePojo latestTemp = saleTemplateDao.findUsingTempByTempId(template.getTemplateId());
                SaleTemplateDesp tempDesp = latestTemp.getTemplate();
                tempDesp.setIsUsing(PromotionConstants.N);
                saleTemplateDao.updatePojoByPK("id", latestTemp.getId(), latestTemp);

                //插入修改后的促销模板
                saleTemplateDao.insertPojo(saleTemplatePojo);

                //添加版本
                SaleOperatorPojo operatorPojo = new SaleOperatorPojo();
                operatorPojo.setOperator(map.get("userId").toString());
                operatorPojo.setBaseId(template.getTemplateId());
                operatorPojo.setOperation(latestTemp.getTemplate().getTemplateDes() + "---->" + template.getTemplateDes());
                operatorPojo.setChangeDate(creationTime);
                operatorPojo.setType("TEMPLATE");
                saleOperatorDao.insertPojo(operatorPojo);
            }

            return new ResponseData(Arrays.asList(saleTemplatePojo));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseReturnUtil.returnFalseResponse("存值失败", null);
    }

    /**
     * 删除促销模板
     *
     * @param templateIds 要删除的促销模板的编码id集合
     * @return
     */
    @Override
    public ResponseData delete(List<String> templateIds) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        for (String templateId : templateIds) {
            //删除模板描述信息
            saleTemplateDao.removeByTemplateId(templateId);
            //删除模板操作记录
            saleOperatorDao.removeByBaseId(templateId);

        }
        return new ResponseData(templateIds);
    }

    /**
     * 根据模板Id查询模板的详细信息。
     *
     * @param id
     * @return
     */
    @Override
    public SaleTemplatePojo selectTemplateDetail(String id) {
        SaleTemplatePojo detail = saleTemplateDao.findByPK(id, SaleTemplatePojo.class);
        return detail;


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
