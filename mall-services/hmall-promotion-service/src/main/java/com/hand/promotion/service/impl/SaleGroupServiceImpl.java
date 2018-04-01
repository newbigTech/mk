package com.hand.promotion.service.impl;

import com.hand.promotion.dao.GroupDao;
import com.hand.promotion.dao.PromotionActivityDao;
import com.hand.promotion.pojo.SimpleMessagePojo;
import com.hand.promotion.pojo.activity.PromotionActivitiesPojo;
import com.hand.promotion.pojo.enums.MsgMenu;
import com.hand.promotion.pojo.group.SaleGroupPojo;
import com.hand.promotion.service.ISaleGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2017/12/20
 * @description
 */
@Service
public class SaleGroupServiceImpl implements ISaleGroupService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private GroupDao groupDao;

    @Autowired
    private PromotionActivityDao promotionActivityDao;


    /**
     * 新建促销分组
     *
     * @param groupPojos
     * @return
     */
    @Override
    public SimpleMessagePojo addNewGroup(List<SaleGroupPojo> groupPojos) {
        List<SaleGroupPojo> groups = groupDao.findAll(SaleGroupPojo.class);
        int maxPriority = 0;
        //获取已创建分组的最高优先级
        for (SaleGroupPojo group : groups) {
            Integer priority = group.getPriority();
            if (maxPriority < priority) {
                maxPriority = priority;
            }
        }
        //处理要新增或更新的分组
        for (SaleGroupPojo group : groupPojos) {

            //id不存在则新增，优先级+1
            if (StringUtils.isEmpty(group.getId())) {
                maxPriority += 1;
                group.setPriority(maxPriority);
                group.setId(UUID.randomUUID().toString());
            }
            upsertByGroupId(group);
        }
        return new SimpleMessagePojo(true, MsgMenu.SUCCESS, groupPojos);
    }

    /**
     * 插入更新单个促销分组
     *
     * @param groupPojo
     */
    @Override
    public SimpleMessagePojo upsertByGroupId(SaleGroupPojo groupPojo) {
        try {
            groupDao.upsertByGroupId(groupPojo);
        } catch (Exception e) {
            logger.error("-------插入分组异常---", e);
            return new SimpleMessagePojo(false, MsgMenu.CRAETE_GROUP_ERR, null);
        }
        return new SimpleMessagePojo();
    }

    /**
     * 根据促销分组id删除促销分组
     * 校验是否有活动中促销使用该分组，存在则不删除
     *
     * @param groupId
     * @return
     */
    @Override
    public SimpleMessagePojo deleteGroupById(String groupId) {
        SaleGroupPojo condition = new SaleGroupPojo();
        condition.setId(groupId);
        //校验促销活动中是否使用该分组,使用则不删除
        List<PromotionActivitiesPojo> groupActivities = promotionActivityDao.findByGroupId(groupId);
        if (CollectionUtils.isEmpty(groupActivities)) {
            try {
                groupDao.removePojo(condition);
                return new SimpleMessagePojo();
            } catch (Exception e) {
                logger.error("-------删除分组异常---", e);
                return new SimpleMessagePojo(false, MsgMenu.DEL_GROUP_ERR, null);
            }
        } else {
            return new SimpleMessagePojo(true, MsgMenu.GROUP_USED, null);
        }
    }

    /**
     * 批量插入更新促销分组
     *
     * @param pojos
     * @return
     */
    @Override
    public SimpleMessagePojo batchUpsertGroupById(List<SaleGroupPojo> pojos) {
        SimpleMessagePojo simpleMessagePojo = new SimpleMessagePojo();
        pojos.forEach(group -> {
            group.setId(UUID.randomUUID().toString());
            upsertByGroupId(group);
        });
        return simpleMessagePojo;
    }

    @Override
    public SimpleMessagePojo batchDeleteGroupById(List<String> pojoIds) {
        List<String> errMsg = new ArrayList<>();
        pojoIds.forEach(id -> {
            SimpleMessagePojo deleteResult = deleteGroupById(id);
            if (!deleteResult.isSuccess()) {
                errMsg.add(id);
            }
        });
        if (CollectionUtils.isEmpty(errMsg)) {
            return new SimpleMessagePojo(true, MsgMenu.SUCCESS, null);
        }
        return new SimpleMessagePojo(true, MsgMenu.DEL_GROUP_ERR, errMsg);
    }

    /**
     * 根据type查询促销分组 type为CREATE 在所有分组的基础上追加“+ 添加新分组”
     * 不为type追加“所有分组”
     *
     * @param type
     * @return
     */
    @Override
    public SimpleMessagePojo selectAllGroup(String type) {

        List<SaleGroupPojo> groups = groupDao.findAll(SaleGroupPojo.class);
        groups.sort(Comparator.comparing(SaleGroupPojo::getPriority));


        if (type.equals("CREATE")) {
            //新增分组
            SaleGroupPojo addGroup = new SaleGroupPojo();
            addGroup.setId("ADD_GROUP");
            addGroup.setName("+ 添加新分组");
            groups.add(addGroup);
        } else {
            SaleGroupPojo addGroup = new SaleGroupPojo();
            addGroup.setId("ALL");
            addGroup.setName("+所有分组");
            groups.add(addGroup);
        }

        return new SimpleMessagePojo(true, MsgMenu.SUCCESS, groups);
    }

    /**
     * 根据condition查询促销活动分组,返回结果按照优先级升序排序
     *
     * @param condition
     * @return
     */
    @Override
    public SimpleMessagePojo queryByConditions(SaleGroupPojo condition) {
        try {
            List<SaleGroupPojo> byPojo = groupDao.matchByIdAndName(condition.getId(),condition.getName());
            byPojo.sort(Comparator.comparing(SaleGroupPojo::getPriority));
            return new SimpleMessagePojo(true, MsgMenu.SUCCESS, byPojo);
        } catch (Exception e) {
            logger.error("-----查询异常------", e);
            return new SimpleMessagePojo();
        }
    }

    @Override
    public List<SaleGroupPojo> selectAllGroup() {
        List<SaleGroupPojo> all = groupDao.findAll(SaleGroupPojo.class);
        return all;

    }
}
