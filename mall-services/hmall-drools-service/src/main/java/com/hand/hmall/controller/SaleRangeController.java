package com.hand.hmall.controller;

import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.service.IGroupService;
import com.hand.hmall.service.ISelectConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 促销条件、结果、分组信息后台操作入口
 * <p>
 * Created by shanks on 2017/2/15.
 */
@RestController
@RequestMapping("/h/sale/range")
public class SaleRangeController {

    @Autowired
    private IGroupService groupService;

    @Autowired
    private ISelectConditionService selectConditionService;


    /**
     * 根据条件查询可选条件、结果
     * code 用途，用于条件、结果、容器、分组
     * type 用于促销还是优惠券
     * level 用于商品层级还是订单层级
     * page 分页起始页
     * pageSize 每页显示条数
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/condition/query", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData conditionQuery(@RequestBody Map<String, Object> map) {


        return selectConditionService.selectByCode(map);

    }

    /**
     * 促销编辑页面，分组下拉框调用。查询系统默认的分组信息
     *
     * @return
     */
    @RequestMapping(value = "/group", method = RequestMethod.GET)
    @ResponseBody
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
    @RequestMapping(value = "/group/submit", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData groupSubmit(@RequestBody List<Map<String, Object>> maps) {
        return groupService.addNewGroup(maps);
    }

    /**
     * 分组无促销活动使用则删除该分组
     *
     * @param maps
     * @return
     */
    @RequestMapping(value = "/group/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData groupDelete(@RequestBody List<Map<String, Object>> maps) {
        return groupService.groupDelete(maps);
    }

    /**
     * 根据type查询促销分组 type为CREATE 在所有分组的基础上追加“+ 添加新分组”
     * 不为type追加“所有分组”
     *
     * @param type
     * @return
     */
    @RequestMapping(value = "/group/queryByType", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData groupQuery(@RequestParam("type") String type) {
        return groupService.selectAllGroup(type);
    }

    /**
     * 根据Id（可选）、name（可选）查询促销分组
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/group/queryByConditions", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData queryByConditions(@RequestBody Map<String, Object> map) {
        return groupService.queryByConditions(map);
    }

}
