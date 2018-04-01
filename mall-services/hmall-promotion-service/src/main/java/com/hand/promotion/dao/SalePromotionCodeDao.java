package com.hand.promotion.dao;


import com.hand.promotion.pojo.activity.SalePromotionCodePojo;
import com.hand.promotion.pojo.enums.OperatorMenu;
import com.hand.promotion.pojo.enums.PromotionCodeType;
import com.hand.promotion.pojo.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2017/12/19
 * @description 促销商品关联关系DAO
 */
@Repository
public class SalePromotionCodeDao extends BaseMongoDao<SalePromotionCodePojo> {


    /**
     * 根据商品code查询出商品的可用促销
     * 查询状态（status）为活动中，编码类型（type） 为CODE，关联符号(rangeOperator) 为  MEMBER_OF,NOT_MEMBER_OF的数据
     *
     * @param code 商品编码
     * @return
     */
    public List<SalePromotionCodePojo> findUsefulPromotionByCode(String code) {

        Criteria criteria = Criteria.where("rangeOperator").is(OperatorMenu.MEMBER_OF.name())
            .and("status").is(Status.ACTIVITY.name())
            .and("type").is(PromotionCodeType.CODE.getValue())
            .and("definedIds").is(code);
        Query queryMemberOf = new Query(criteria);
        List<SalePromotionCodePojo> memberPojos = mongoTemplate.find(queryMemberOf, SalePromotionCodePojo.class);

        Criteria notCriteria = Criteria.where("rangeOperator").is(OperatorMenu.MEMBER_OF.name())
            .and("status").is(Status.ACTIVITY.name())
            .and("type").is(PromotionCodeType.CODE.getValue())
            .and("definedIds").ne(code);
        Query queryNotMemberOf = new Query(notCriteria);
        List<SalePromotionCodePojo> notMemberPojos = mongoTemplate.find(queryNotMemberOf, SalePromotionCodePojo.class);
        memberPojos.addAll(notMemberPojos);
        return memberPojos;

    }


}
