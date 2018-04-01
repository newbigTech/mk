package com.hand.promotion.service.impl;

import com.hand.promotion.dao.SelectConditionActionDao;
import com.hand.promotion.pojo.activity.SelectConditionActionPojo;
import com.hand.promotion.service.ISelectConditionActionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2017/12/20
 * @description 页面展示的条件结果 Service
 */
@Service
public class SelectConditionActionServiceImpl implements ISelectConditionActionService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SelectConditionActionDao selectConditionActionDao;

    /**
     * 根据definitionId判断插入或更新数据
     *
     * @param pojo
     */
    @Override
    public void upsertByDfId(SelectConditionActionPojo pojo) {
        try {
            selectConditionActionDao.insertOrUpdateByDefIdAndCode(pojo);
        } catch (Exception e) {
            logger.error("---------插入异常-----", e);
        }
    }

    /**
     * 根据code 、type 查询条件结果
     *
     * @param condition
     * @return
     */
    @Override
    public List<SelectConditionActionPojo> findByCondition(SelectConditionActionPojo condition) {
        List<SelectConditionActionPojo> resultList = null;
        try {
            resultList = selectConditionActionDao.findByPojo(condition);
        } catch (Exception e) {
            logger.error("---------查询异常-----", e);
            return Collections.emptyList();
        }
        return resultList;
    }

    /**
     * 根据条件查询中台显示的条件结果 按照优先级从小到大排序
     * @param code 条件是基础条件（ADD_CONDITION）、还是组内可选条件（ADD_GROUP）、还是容器（ADD_CONTAINER）可选条件
     * @param type 条件、结果用于促销活动（ACTIVITY）还是优惠券（COUPON）
     *
     * @return
     */
    @Override
    public List<SelectConditionActionPojo> selectShowPojo(String code, String type) {
        List<SelectConditionActionPojo> resultPojos = selectConditionActionDao.findByCodeAndType(code, type);

        //按照优先级排序
        resultPojos.sort(Comparator.comparing(selectConditionActionPojo -> {
            return selectConditionActionPojo.getPriority();
        }));

        return resultPojos;
    }
}
