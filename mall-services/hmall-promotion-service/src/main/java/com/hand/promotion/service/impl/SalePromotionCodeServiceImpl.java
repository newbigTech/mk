package com.hand.promotion.service.impl;

import com.hand.promotion.dao.SalePromotionCodeDao;
import com.hand.promotion.pojo.activity.ActivityPojo;
import com.hand.promotion.pojo.activity.ChildPojo;
import com.hand.promotion.pojo.activity.ConditionPojo;
import com.hand.promotion.pojo.activity.ContainerPojo;
import com.hand.promotion.pojo.activity.GroupPojo;
import com.hand.promotion.pojo.activity.ParameterPojo;
import com.hand.promotion.pojo.activity.PromotionActivitiesPojo;
import com.hand.promotion.pojo.activity.SalePromotionCodePojo;
import com.hand.promotion.pojo.enums.ConditionActions;
import com.hand.promotion.pojo.enums.OperatorMenu;
import com.hand.promotion.pojo.enums.PromotionCodeType;
import com.hand.promotion.pojo.enums.Status;
import com.hand.promotion.service.ISalePromotionCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2017/12/19
 * @description 促销商品关联关系Service
 */
@Service
public class SalePromotionCodeServiceImpl implements ISalePromotionCodeService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SalePromotionCodeDao salePromotionCodeDao;

    /**
     * 保存促销商品关联关系
     *
     * @param pojo
     */
    @Override
    public void saveProductRelevance(PromotionActivitiesPojo pojo) {
        ActivityPojo activity = pojo.getActivity();
        //如果促销在前台可显示，保存商品-促销关联
        if (activity.getIsExcludeShow().equalsIgnoreCase("Y")) {
            logger.info("----------促销不须在前台显示--------------");
            return;
        }


        //促销关联的商品id
        Set<String> memberIds = new HashSet<>();
        //促销排除的商品id
        Set<String> notMemberIds = new HashSet<>();

        //促销关联的分类id
        Set<String> memberCateIds = new HashSet<>();
        //促销排除的分类id
        Set<String> notMemberCateIds = new HashSet<>();
        //获取促销中涉及到的商品id集合
        doProductRelevance(pojo, memberIds, notMemberIds, memberCateIds, notMemberCateIds);


        if (!CollectionUtils.isEmpty(memberIds)) {
            SalePromotionCodePojo promotionCode = getPromotionCodePojo(activity, PromotionCodeType.CODE, memberIds, OperatorMenu.MEMBER_OF.name());
            salePromotionCodeDao.insertPojo(promotionCode);
        }

        if (!CollectionUtils.isEmpty(notMemberIds)) {
            SalePromotionCodePojo promotionCode = getPromotionCodePojo(activity, PromotionCodeType.CODE, notMemberIds, OperatorMenu.NOT_MEMBER_OF.name());

            salePromotionCodeDao.insertPojo(promotionCode);
        }

        if (!CollectionUtils.isEmpty(memberCateIds)) {
            SalePromotionCodePojo promotionCode = getPromotionCodePojo(activity, PromotionCodeType.CATEGORY, memberCateIds, OperatorMenu.MEMBER_OF.name());
            salePromotionCodeDao.insertPojo(promotionCode);
        }

        if (!CollectionUtils.isEmpty(notMemberCateIds)) {
            SalePromotionCodePojo promotionCode = getPromotionCodePojo(activity, PromotionCodeType.CATEGORY, notMemberCateIds, OperatorMenu.NOT_MEMBER_OF.name());

            salePromotionCodeDao.insertPojo(promotionCode);
        }

        //促销中不涉及商品/商品类别
        if (CollectionUtils.isEmpty(memberIds) && CollectionUtils.isEmpty(notMemberIds) && CollectionUtils.isEmpty(memberCateIds) && CollectionUtils.isEmpty(notMemberCateIds)) {
            SalePromotionCodePojo promotionCode = getPromotionCodePojo(activity, PromotionCodeType.ALL, null, "");
            salePromotionCodeDao.insertPojo(promotionCode);
        }
    }


    /**
     * 获取条件、容器、分组中包含的产品code，产品类别
     */
    public void doProductRelevance(PromotionActivitiesPojo pojo, Set<String> memberIds, Set<String> notMemberIds, Set<String> memberCateIds, Set<String> notMemberCateIds) {
        List<ConditionPojo> conditions = pojo.getConditions();
        List<ContainerPojo> containers = pojo.getContainers();
        List<GroupPojo> groups = pojo.getGroups();


        if (!CollectionUtils.isEmpty(conditions)) {
            for (ConditionPojo condition : conditions) {
                System.out.println("llll");
                ParameterPojo parameters = condition.getParameters();
                getProductData(condition.getDefinitionId(), parameters, memberIds, notMemberIds);
                getCategoryData(condition.getDefinitionId(), parameters, memberCateIds, notMemberCateIds);

            }
        }

        if (!CollectionUtils.isEmpty(containers)) {
            for (ContainerPojo container : containers) {
                List<ChildPojo> childs = container.getChild();
                for (ChildPojo child : childs) {
                    getProductData(child.getDefinitionId(), child.getParameters(), memberIds, notMemberIds);
                    getCategoryData(child.getDefinitionId(), child.getParameters(), memberCateIds, notMemberCateIds);
                }
            }
        }

        if (!CollectionUtils.isEmpty(groups)) {
            for (GroupPojo group : groups) {
                List<ChildPojo> childs = group.getChild();
                for (ChildPojo child : childs) {
                    getProductData(child.getDefinitionId(), child.getParameters(), memberIds, notMemberIds);
                    getCategoryData(child.getDefinitionId(), child.getParameters(), memberCateIds, notMemberCateIds);
                }
            }
        }
    }


    /**
     * 获取条件ParameterPojo中的商品、商品分类信息
     *
     * @param definitionId
     * @param parameters   条件中的parameters POJO
     * @param memberIds    rangeOperator 为member_of的商品id集合
     * @param notMemberIds rangeOperator 为not_member_of的商品id集合
     */
    public void getProductData(String definitionId, ParameterPojo parameters, Set<String> memberIds, Set<String> notMemberIds) {
        if (definitionId.equalsIgnoreCase(ConditionActions.o_product_range.getValue()) || definitionId.equalsIgnoreCase(ConditionActions.o_product_range_number.getValue())) {
            String rangeOperator = parameters.getRangeOperator().getValue();
            List<String> rangeValue = parameters.getRangeValue().getValue();
            if (rangeOperator.equalsIgnoreCase(OperatorMenu.MEMBER_OF.name())) {
                memberIds.addAll(rangeValue);

            } else if (rangeOperator.equalsIgnoreCase(OperatorMenu.NOT_MEMBER_OF.name())) {
                notMemberIds.addAll(rangeValue);
            }

        }


    }

    /**
     * 获取条件ParameterPojo中的商品、商品分类信息
     *
     * @param definitionId
     * @param parameters   条件中的parameters POJO
     * @param memberIds    rangeOperator 为member_of的商品id集合
     * @param notMemberIds rangeOperator 为not_member_of的商品id集合
     */
    public void getCategoryData(String definitionId, ParameterPojo parameters, Set<String> memberIds, Set<String> notMemberIds) {

        if (definitionId.equalsIgnoreCase(ConditionActions.o_type_range.getValue()) || definitionId.equalsIgnoreCase(ConditionActions.o_type_range_number.getValue())) {
            String rangeOperator = parameters.getRangeOperator().getValue();
            List<String> rangeValue = parameters.getRangeValue().getValue();
            if (rangeOperator.equalsIgnoreCase(OperatorMenu.MEMBER_OF.getValue())) {
                memberIds.addAll(rangeValue);

            } else if (rangeOperator.equalsIgnoreCase(OperatorMenu.NOT_MEMBER_OF.getValue())) {
                notMemberIds.addAll(rangeValue);
            }

        }
    }


    /**
     * 获取填充好数据的促销商品关联关系实体
     *
     * @param activity 促销活动描述
     * @param type     促销关联的编码类型， CODE 为商品编码，ALL为所有商品
     * @param codes    涉及到的商品编码
     * @return
     */
    public SalePromotionCodePojo getPromotionCodePojo(ActivityPojo activity, PromotionCodeType type, Set<String> codes, String rangeOperator) {
        SalePromotionCodePojo promotionCode = new SalePromotionCodePojo();
        promotionCode.setId(UUID.randomUUID().toString());
        promotionCode.setActivityId(activity.getActivityId());
        promotionCode.setMeaning(activity.getPageShowMes());
        promotionCode.setGroup(activity.getGroup());
        promotionCode.setIsOverlay(activity.getIsOverlay());
        promotionCode.setPriority(activity.getPriority());
        String status = activity.getStatus();
        promotionCode.setStatus(status);
        //type 为CODE 商品映射
        promotionCode.setType(type.getValue());
        if (!PromotionCodeType.ALL.equals(type)) {
            //definedId关联商品code
            promotionCode.setDefinedIds(codes);
        }
        promotionCode.setStartDate(activity.getStartDate());
        promotionCode.setEndDate(activity.getEndDate());
        promotionCode.setRangeOperator(rangeOperator);
        return promotionCode;
    }


    /**
     * 根据促销活动id查询商品促销关联关系
     *
     * @param activityId 关联的促销活动编码id
     * @return
     */
    @Override
    public List<SalePromotionCodePojo> queryByActivityId(String activityId)  {
        SalePromotionCodePojo salePromotionCodePojo = new SalePromotionCodePojo();
        salePromotionCodePojo.setActivityId(activityId);
        List<SalePromotionCodePojo> result = salePromotionCodeDao.findByPojo(salePromotionCodePojo);
        return result;
    }


    /**
     * 根据关联的促销活动编码id，修改关联关系状态
     *
     * @param activityId 促销活动编码id
     * @param status     要修改的状态
     */
    @Override
    public void changeStatus(String activityId, Status status)  {
        SalePromotionCodePojo queryPojo = new SalePromotionCodePojo();
        queryPojo.setActivityId(activityId);
        SalePromotionCodePojo updatePojo = new SalePromotionCodePojo();
        updatePojo.setStatus(status.name());
        salePromotionCodeDao.updatePojo(queryPojo, updatePojo);
    }

    /**
     * 根据关联的促销活动编码id删除促销商品关联关系
     *
     * @param activityId 关联的促销活动编码Id
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @Override
    public void removeByActivityId(String activityId)  {
        SalePromotionCodePojo salePromotionCodePojo = new SalePromotionCodePojo();
        salePromotionCodePojo.setActivityId(activityId);
        salePromotionCodeDao.removePojo(salePromotionCodePojo);
    }

    /**
     * 根据商品编码查询关联的可用的促销活动
     *
     * @param productCode
     * @return
     */
    @Override
    public List<SalePromotionCodePojo> findCodeUsefulPromo(String productCode) {
        return salePromotionCodeDao.findUsefulPromotionByCode(productCode);
    }
}
