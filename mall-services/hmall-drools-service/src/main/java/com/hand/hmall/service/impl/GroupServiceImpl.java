package com.hand.hmall.service.impl;

import com.hand.hmall.dao.GroupDao;
import com.hand.hmall.dao.SaleActivityDao;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.service.IGroupService;
import com.hand.hmall.util.SortUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shanks on 2017/2/17.
 * 操作促销分组
 */
@Service
public class GroupServiceImpl implements IGroupService {

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private SaleActivityDao saleActivityDao;

    /**
     * 批量新建或更新促销分组
     *
     * @param maps
     * @return
     */
    @Override
    public ResponseData addNewGroup(List<Map<String, Object>> maps) {
        List<Map<String, ?>> groups = groupDao.selectAll();
        int maxPriority = 0;
        //获取已创建分组的最高优先级
        for (Map<String, ?> group : groups) {
            Integer priority = (Integer) group.get("priority");
            if (maxPriority < priority) {
                maxPriority = priority;
            }
        }
        //处理要新增或更新的分组
        for (Map<String, Object> map : maps) {

            if (map.get("__status") != null) {
                map.remove("__status");

            }
            //id不存在则新增，优先级+1
            if (map.get("id") == null || map.get("id").toString().trim().equals("")) {
                maxPriority += 1;
                map.put("priority", maxPriority);
                groupDao.submit(map);
            } else {
                //更新分组
                groupDao.update(map);
            }
        }
        return new ResponseData(maps);
    }

    /**
     * 分组无促销活动使用则删除该分组
     *
     * @param maps
     * @return
     */
    @Override
    public ResponseData groupDelete(List<Map<String, Object>> maps) {
        for (Map<String, Object> map : maps) {
            if (map.get("id") != null) {
                //改分组无促销活动使用则删除该分组
                if (!saleActivityDao.checkedGroupIsUsing(map.get("id").toString()))
                    groupDao.deleteGroup(map);
            }
        }
        return new ResponseData(maps);
    }

    /**
     * 根据type查询促销分组 type为CREATE 在所有分组的基础上追加“+ 添加新分组”
     * 不为type追加“所有分组”
     *
     * @param type
     * @return
     */
    @Override
    public ResponseData selectAllGroup(String type) {

        List<Map<String, ?>> mapList = groupDao.selectAll();
        try {
            SortUtil.listsort(mapList, "priority", true);
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (type.equals("CREATE")) {
            //新增分组
            Map<String, Object> addGroup = new HashMap<String, Object>();
            addGroup.put("id", "ADD_GROUP");
            addGroup.put("name", "+ 添加新分组");
            mapList.add(addGroup);
        } else {
            Map<String, Object> allMap = new HashMap<String, Object>();
            allMap.put("id", "ALL");
            allMap.put("name", "所有分组");
            mapList.add(0, allMap);
        }

        return new ResponseData(mapList);
    }

    /**
     * 根据Id（可选）、name（可选）查询促销分组
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData queryByConditions(Map<String, Object> map) {
        List<Map<String, ?>> groupList = groupDao.selectByIdName(map);
        try {
            SortUtil.listsort(groupList, "priority", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseData(groupList);
    }
}
