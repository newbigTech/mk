package com.hand.promotion.dubboService;

import com.alibaba.dubbo.config.annotation.Service;

import com.hand.dto.ResponseData;
import com.hand.hpromotion.ISaleRangeService;
import com.hand.promotion.pojo.SimpleMessagePojo;
import com.hand.promotion.pojo.activity.SelectConditionActionPojo;
import com.hand.promotion.pojo.enums.MsgMenu;
import com.hand.promotion.pojo.group.SaleGroupPojo;
import com.hand.promotion.service.ISaleGroupService;
import com.hand.promotion.service.ISelectConditionActionService;
import com.hand.promotion.util.BeanMapExchange;
import com.hand.promotion.util.ResponseReturnUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 促销条件、结果、分组信息后台操作入口
 * <p>
 * Created by darkdog on 2018/2/2.
 */
public class SaleRangeService implements ISaleRangeService {

    @Autowired
    private ISaleGroupService groupService;
    @Autowired
    private ISelectConditionActionService selectConditionService;

    /**
     * 根据条件查询可选条件、结果
     * code 用途，用于条件、结果、容器、分组
     * type 用于促销还是优惠券
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData conditionQuery(Map<String, Object> map) {
        String code = (String) map.get("code");
        String type = (String) map.get("selectType");
        List<SelectConditionActionPojo> selectConditionActionPojos = selectConditionService.selectShowPojo(code, type);
        return ResponseReturnUtil.returnTrueResp(selectConditionActionPojos);

    }

    /**
     * 促销编辑页面，分组下拉框调用。查询系统默认的分组信息
     *
     * @return
     */
    @Override
    public ResponseData group() {
        List<Map<String, Object>> mapList = new ArrayList<>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("meaning", "所有分组");
        map.put("value", "ALL");
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("meaning", "默认分组");
        map1.put("value", "DEFAULT");
        Map<String, Object> map2 = new HashMap<String, Object>();

        map2.put("meaning", " + 添加新分组");
        map2.put("value", "ADD_GROUP");
        mapList.add(map);
        mapList.add(map1);
        mapList.add(map2);

        return new ResponseData(mapList);
    }

    /**
     * 批量新建、更新促销分组
     *
     * @param maps
     * @return
     */
    @Override
    public ResponseData groupSubmit(List<Map<String, Object>> maps) {

        try {
            List<SaleGroupPojo> groupPojos = BeanMapExchange.mapsToObjects(maps, SaleGroupPojo.class);
            SimpleMessagePojo simpleMessagePojo = groupService.addNewGroup(groupPojos);
            return ResponseReturnUtil.transSimpleMessage(simpleMessagePojo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseReturnUtil.returnFalseResponse(MsgMenu.CRAETE_GROUP_ERR.getMsg(), MsgMenu.CRAETE_GROUP_ERR.getCode());

        }

    }

    /**
     * 分组无促销活动使用则删除该分组
     *
     * @param maps
     * @return
     */
    @Override
    public ResponseData groupDelete(List<Map<String, Object>> maps) {
        List<String> ids = new ArrayList<>(maps.size());
        maps.forEach(map -> {
            ids.add((String) map.get("id"));
        });
        SimpleMessagePojo simpleMessagePojo = groupService.batchDeleteGroupById(ids);
        return ResponseReturnUtil.transSimpleMessage(simpleMessagePojo);
    }

    /**
     * 根据type查询促销分组 type为CREATE 在所有分组的基础上追加“+ 添加新分组”
     * 不为type追加“所有分组”
     *
     * @param type
     * @return
     */
    @Override
    public ResponseData groupQuery(String type) {
        SimpleMessagePojo simpleMessagePojo = groupService.selectAllGroup(type);
        return ResponseReturnUtil.transSimpleMessage(simpleMessagePojo);
    }

    /**
     * 根据Id（可选）、name（可选）查询促销分组
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData queryByConditions(Map<String, Object> map) {
        String id = (String) map.get("id");
        String name = (String) map.get("name");
        SaleGroupPojo condition = new SaleGroupPojo();
        condition.setId(id);
        condition.setName(name);
        SimpleMessagePojo simpleMessagePojo = groupService.queryByConditions(condition);
        return ResponseReturnUtil.transSimpleMessage(simpleMessagePojo);
    }



}
