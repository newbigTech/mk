package com.hand.promotion.dao;

import com.hand.dto.ResponseData;
import com.hand.promotion.pojo.SaleOperatorPojo;
import com.hand.promotion.pojo.activity.SaleTemplateDesp;
import com.hand.promotion.pojo.activity.SaleTemplatePojo;
import com.hand.promotion.pojo.enums.PromotionConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2017/12/25
 * @description 促销模板Dao
 */
@Repository
public class SaleTemplateDao extends BaseMongoDao<SaleTemplatePojo> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * 分页查询促销模板描述信息
     *
     * @param saleTemplatePojo
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ResponseData queryTemplateDespByCondition(SaleTemplatePojo saleTemplatePojo, int pageNum, int pageSize) {
        Criteria criteria = null;
        SaleTemplateDesp template = saleTemplatePojo.getTemplate();
        if (StringUtils.isNotBlank(template.getTemplateName())) {
            criteria = Criteria.where("template.templateName").regex(template.getTemplateName());
        }

        if (StringUtils.isNotBlank(template.getTemplateId())) {
            if (null == criteria) {
                criteria = Criteria.where("template.templateId").regex(template.getTemplateId());
            } else {
                criteria.and("template.templateId").regex(template.getTemplateId());
            }
        }

        if (StringUtils.isNotBlank(template.getIsUsing())) {
            if (null == criteria) {
                criteria = Criteria.where("template.isUsing").regex(template.getIsUsing());
            } else {
                criteria.and("template.isUsing").regex(template.getIsUsing());
            }
        } else {
            if (null == criteria) {
                criteria = Criteria.where("template.isUsing").is(PromotionConstants.Y);
            } else {
                criteria.and("template.isUsing").is(PromotionConstants.Y);
            }
        }
        Query query = null;
        if (criteria == null) {
            query = new Query();
        } else {
            query = new Query(criteria);
        }
        long count = count(query, SaleOperatorPojo.class);
        Pageable pageRequest = getPageRequest(count, pageSize, pageNum);
        query.with(pageRequest);
        List<SaleTemplatePojo> templatePojos = mongoTemplate.find(query, SaleTemplatePojo.class);
        List<SaleTemplateDesp> templateDesps = templatePojos.stream().map(SaleTemplatePojo::getTemplate).collect(Collectors.toList());
        ResponseData responseData = new ResponseData(templateDesps);
        responseData.setTotal((int) count);
        return responseData;
    }


    /**
     * 根据模板id查询模板信息，查询促销活动的所有版本
     *
     * @param templateId
     * @return
     */
    public List<SaleTemplatePojo> findByTemplateId(String templateId) {
        Criteria criteria = Criteria.where("template.templateId").is(templateId);
        Query query = new Query(criteria);
        List<SaleTemplatePojo> saleTemplatePojos = mongoTemplate.find(query, SaleTemplatePojo.class);
        return saleTemplatePojos;
    }

    /**
     * 根据templateId查询isUsing为 Y 的促销模板。查询最新状态的促销模板
     *
     * @param templateId
     * @return
     */
    public SaleTemplatePojo findUsingTempByTempId(String templateId) {
        Criteria criteria = Criteria.where("template.templateId").is(templateId);
        criteria.and("template.isUsing").is("Y");
        Query query = new Query(criteria);
        SaleTemplatePojo saleTemplatePojo = mongoTemplate.findOne(query, SaleTemplatePojo.class);
        return saleTemplatePojo;
    }

    /**
     * 根据模板编码删除促销模板
     * @param templateId
     * @return
     */
    public void removeByTemplateId(String templateId) {
        Query query = new Query(Criteria.where("template.templateId").is(templateId));
        mongoTemplate.remove(query, SaleTemplatePojo.class);

    }
}
