package com.hand.promotion.service;


import com.hand.promotion.pojo.activity.PromotionActivitiesPojo;
import com.hand.promotion.pojo.activity.SalePromotionCodePojo;
import com.hand.promotion.pojo.enums.Status;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2017/12/19
 * @description
 */
public interface ISalePromotionCodeService {

    /**
     * 保存促销商品关联关系
     *
     * @param pojo
     */
    void saveProductRelevance(PromotionActivitiesPojo pojo);

    /**
     * 根据促销活动编码id查询商品促销关联关系
     *
     * @return
     */
    List<SalePromotionCodePojo> queryByActivityId(String activityId) ;

    /**
     * 根据关联的促销活动编码id，修改关联关系状态
     *
     * @param activityId
     * @param status
     */
    void changeStatus(String activityId, Status status) ;

    /**
     * 根据促销活动删除促销商品关联关系
     *
     * @param activityId 关联的促销活动编码Id
     */
    void removeByActivityId(String activityId) ;

    /**
     * 根据商品编码查询关联的可用的促销活动
     *
     * @param productCode
     * @return
     */
    List<SalePromotionCodePojo> findCodeUsefulPromo(String productCode);

}
