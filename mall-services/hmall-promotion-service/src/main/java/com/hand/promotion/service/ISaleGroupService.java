package com.hand.promotion.service;


import com.hand.promotion.pojo.SimpleMessagePojo;
import com.hand.promotion.pojo.group.SaleGroupPojo;

import java.util.List;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2017/12/20
 * @description
 */
public interface ISaleGroupService {

    /**
     * 添加新的促销分组
     *
     * @param groupPojo
     * @return
     */
    SimpleMessagePojo addNewGroup(List<SaleGroupPojo> groupPojo);

    /**
     * 根据分组id单个插入或更新分组
     *
     * @param groupPojo
     * @return
     */
    SimpleMessagePojo upsertByGroupId(SaleGroupPojo groupPojo);

    /**
     * 删除单个促销分组
     *
     * @return
     */
    SimpleMessagePojo deleteGroupById(String groupId);

    /**
     * 根据分组id批量插入或更新分组
     *
     * @param pojos
     * @return
     */
    SimpleMessagePojo batchUpsertGroupById(List<SaleGroupPojo> pojos);

    /**
     * 根据分组id批量删除分组
     *
     * @param pojoIds
     * @return
     */
    SimpleMessagePojo batchDeleteGroupById(List<String> pojoIds);

    /**
     * 查询
     *
     * @param type
     * @return
     */
    SimpleMessagePojo selectAllGroup(String type);

    /**
     * 根据简单字段查询促销分组
     *
     * @param condition
     * @return
     */
    SimpleMessagePojo queryByConditions(SaleGroupPojo condition);

    List<SaleGroupPojo> selectAllGroup();


}
