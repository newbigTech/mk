package com.hand.hmall.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hand.hmall.dto.PagedValues;
import com.hand.hmall.dto.ScoreRange;
import com.hand.hmall.menu.Status;
import com.hand.hmall.util.DateFormatUtil;
import com.hand.hmall.util.redis.Field.FieldType;
import com.hand.hmall.util.redis.dao.BaseDao;
import org.apache.commons.collections.MapUtils;
import org.codehaus.plexus.logging.LoggerManager;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;

/**
 * Created by shanks on 2017/1/5.
 * 对促销活动描述信息进行CRUD操作
 */
@Repository
public class SaleActivityDao extends BaseDao {
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    @Resource
    private GroupDao groupDao;

    public SaleActivityDao() {
        this.hashTag = "Sale";
        this.clazz = "SaleActivity";
        this.idDescriptor = createFieldDescriptor("id", FieldType.TYPE_EQUAL);
        //促销活动的编码ID
        addFieldDescriptor(createFieldDescriptor("activityId", FieldType.TYPE_EQUAL));
        //规则名称
        addFieldDescriptor(createFieldDescriptor("activityName", FieldType.TYPE_MATCH));
        //规则描述
        addFieldDescriptor(createFieldDescriptor("activityDes", FieldType.TYPE_MATCH));
        //规则优先级
        addFieldDescriptor(createFieldDescriptor("priority", FieldType.TYPE_EQUAL));
        //是否叠加
        addFieldDescriptor(createFieldDescriptor("isOverlay", FieldType.TYPE_EQUAL));
        //促销所在规则组
        addFieldDescriptor(createFieldDescriptor("group", FieldType.TYPE_EQUAL));
        //促销是否在前台显示
        addFieldDescriptor(createFieldDescriptor("isExcludeShow", FieldType.TYPE_EQUAL));
        //前台展示信息
        addFieldDescriptor(createFieldDescriptor("pageShowMes", FieldType.TYPE_MATCH));
        //促销状态
        addFieldDescriptor(createFieldDescriptor("status", FieldType.TYPE_EQUAL));
        //促销生效时间
        addFieldDescriptor(createFieldDescriptor("startDate", FieldType.TYPE_RANGE));
        //促销截止时间
        addFieldDescriptor(createFieldDescriptor("endDate", FieldType.TYPE_RANGE));
        //促销创建时间
        addFieldDescriptor(createFieldDescriptor("creationTime", FieldType.TYPE_RANGE));
        //最近一次操作时间
        addFieldDescriptor(createFieldDescriptor("lastCreationTime", FieldType.TYPE_RANGE));
        //版本ID 加了促销从算后该字段已弃用
        addFieldDescriptor(createFieldDescriptor("isUsing", FieldType.TYPE_EQUAL));
        //促销用于订单层级还是商品层级
        addFieldDescriptor(createFieldDescriptor("type", FieldType.TYPE_EQUAL));
        //该字段已弃用
        addFieldDescriptor(createFieldDescriptor("productCode", FieldType.TYPE_EQUAL));


    }

    /**
     * 检查促销分组是否有促销活动使用
     *
     * @param groupId
     * @return
     */
    public boolean checkedGroupIsUsing(String groupId) {
        List<String> keys = new ArrayList<>();

        //equal搜索
        Map<String, List<Object>> equal = new HashMap<>();
        equal.put("group", Arrays.asList(groupId));

        if (MapUtils.isNotEmpty(equal)) {
            String key1 = this.selectIdsSetByEqual(equal);
            if (key1 != null) {
                keys.add(key1);
            }
        }
        List<Map<String, ?>> maps = this.selectValuesByKeys(keys);

        if (maps.isEmpty()) {
            return false;
        } else {

            return true;
        }
    }


    /**
     * 分页查询所有促销活动
     *
     * @param map 可选字段 activityId（规则ID编码） 生效时间（StartDate） endDate(失效时间)
     *            activityName（规则名称）
     *            status（状态）
     */
    public PagedValues querySaleActivity(Map<String, Object> map) throws ParseException {
        Map<String, Object> data = (Map<String, Object>) map.get("data");
        int page = Integer.parseInt(map.get("page").toString());
        int pageSize = Integer.parseInt(map.get("pageSize").toString());
        if (!data.isEmpty()) {
            List<String> keys = new ArrayList<>();

            //equal搜索
            Map<String, List<Object>> equal = new HashMap<>();
            equal.put("isUsing", Arrays.asList("Y"));

            if (data.get("activityId") != null && !StringUtils.isEmpty(data.get("activityId").toString().trim())) {
                equal.put("activityId", Arrays.asList(data.get("activityId").toString()));
            }

            if (data.get("isOverlay") != null) {
                if (data.get("isOverlay").equals("ALL")) {
                    equal.put("isOverlay", Arrays.asList("Y", "N"));
                } else {
                    equal.put("isOverlay", Arrays.asList(data.get("isOverlay").toString()));
                }
            }

            if (data.get("status") != null) {
                if (data.get("status").equals("ALL")) {
                    equal.put("status", Arrays.asList(Status.ACTIVITY.getValue(),
                            Status.INACTIVE.getValue(), Status.DELAY.toString(), Status.FAILURE.getValue()));
                } else {
                    equal.put("status", Arrays.asList(data.get("status").toString()));

                }
            }
            //group
            if (data.get("group") != null) {
                if (data.get("group").equals("ALL")) {
                    List groups = groupDao.selectAll();
                    logger.info(">>>>>>groups>>>>>>" + JSON.toJSONString(groups));
                    JSONArray groupArray = JSON.parseArray(JSON.toJSONString(groups));
                    List groupPama = new ArrayList();
                    for (int i = 0; i < groupArray.size(); i++) {
                        JSONObject group = groupArray.getJSONObject(i);
                        groupPama.add(group.get("id"));
                    }
                    equal.put("group", groupPama);
                } else {
                    equal.put("group", Arrays.asList(data.get("group").toString()));
                }
            }
            if (MapUtils.isNotEmpty(equal)) {
                String key1 = this.selectIdsSetByEqual(equal);
                if (key1 != null) {
                    keys.add(key1);
                }
            }

            //range匹配
            Map<String, ScoreRange> ranges = new HashMap<>();
            if (data.get("startDate") != null) {
                Long validStartDate = DateFormatUtil.stringToTimeStamp(data.get("startDate").toString());
                Long validEndDate = null;
                if (data.get("endDate") != null) {
                    validEndDate = DateFormatUtil.stringToTimeStamp(data.get("endDate").toString());
                } else {
                    validEndDate = DateFormatUtil.stringToTimeStamp("2099-12-31 00:00:00");
                }

                ScoreRange scoreRange = new ScoreRange();
                scoreRange.setMin(validStartDate.toString());
                scoreRange.setMax(validEndDate.toString());
                ranges.put("startDate", scoreRange);
            } else {

                if (data.get("endDate") != null) {
                    Long validStartDate = DateFormatUtil.stringToTimeStamp("2000-1-1 00:00:00");
                    Long validEndDate = DateFormatUtil.stringToTimeStamp(data.get("endDate").toString());

                    ScoreRange scoreRange = new ScoreRange();
                    scoreRange.setMin(validStartDate.toString());
                    scoreRange.setMax(validEndDate.toString());
                    ranges.put("startDate", scoreRange);
                }
            }

            if (MapUtils.isNotEmpty(ranges)) {
                String key2 = this.selectIdsSetByRange(ranges);
                if (key2 != null) {
                    keys.add(key2);
                }
            }
            //match模糊匹配

            Map<String, Object> matches = new HashMap<>();
            logger.info(">>>>>>获取到的activityName：{}", data.get("activityName"));
            if (data.get("activityName") != null && !StringUtils.isEmpty(data.get("activityName").toString().trim())) {
                matches.put("activityName", data.get("activityName").toString());
            }

            if (MapUtils.isNotEmpty(matches)) {
                String key3 = this.selectIdsSetByMatch(matches);
                if (key3 != null) {
                    keys.add(key3);
                }
            }
            PagedValues values = this.selectValuesByKeys(keys, (page - 1) * pageSize, pageSize, true);
            return values;
        } else {
            List<String> keys = new ArrayList<>();
            Map<String, List<Object>> equal = new HashMap<>();
            equal.put("isUsing", Arrays.asList("Y"));
            String key = this.selectIdsSetByEqual(equal);
            keys.add(key);
            PagedValues values = this.selectValuesByKeys(keys, (page - 1) * pageSize, pageSize, true);
            return values;
        }
    }

    /**
     * 保存促销描述信息
     *
     * @param map
     * @return
     */
    public Map<String, Object> submitSaleActivity(Map<String, Object> map) {
        map.put("id", UUID.randomUUID());
        this.add(map);
        return map;
    }

    /**
     * 更新促销规则
     *
     * @param map
     * @return
     */
    public Map<String, Object> updateSaleActivity(Map<String, Object> map) {
        this.update(map);
        return map;
    }

    /**
     * 查询促销详情
     *
     * @param id 促销活动id，该字段唯一标识一条促销活动
     * @return
     */
    public Map<String, ?> selectActivityDetail(String id) {
        return this.select(id);
    }

    /**
     * 查询历史版本
     *
     * @param activityId 对于修改的促销活动，原促销会设置成过期，修改后的促销活动会重新生成id字段，activityId字段保持不变。
     * @return
     */
    public List<Map<String, ?>> selectByActivityId(String activityId) {
        Map<String, List<Object>> equal = new HashMap<>();
        List<String> keys = new ArrayList<>();

        if (activityId != null) {
            equal.put("activityId", Arrays.asList(activityId));
        }

        if (!equal.isEmpty()) {
            String key = this.selectIdsSetByEqual(equal);
            if (key != null) {
                keys.add(key);
            }
        }
        return this.selectValuesByKeys(keys);
    }

    /**
     * 根据促销编码Id 和是否是最新版本促销查询促销描述信息
     *
     * @param activityId
     * @param using
     * @return
     */
    public List<Map<String, ?>> selectByActivityIdAndUsing(String activityId, String using) {
        Map<String, List<Object>> equal = new HashMap<>();
        List<String> keys = new ArrayList<>();

        if (activityId != null) {
            equal.put("activityId", Arrays.asList(activityId));
        }
        if (StringUtils.isNotEmpty(using)) {
            equal.put("isUsing", Arrays.asList(using));

        }

        if (!equal.isEmpty()) {
            String key = this.selectIdsSetByEqual(equal);
            if (key != null) {
                keys.add(key);
            }
        }
        return this.selectValuesByKeys(keys);
    }


    /**
     * @param priority 促销活动优先级
     * @param isUsing  促销是否使用
     * @return
     */
    public List<Map<String, ?>> selectByPriorityAndIsUsing(int priority, String isUsing) {
        Map<String, List<Object>> equal = new HashMap<>();
        List<String> keys = new ArrayList<>();


        equal.put("priority", Arrays.asList(priority));
        equal.put("status", Arrays.asList(Status.ACTIVITY.getValue(), Status.INACTIVE.getValue()));
        equal.put("isUsing", Arrays.asList(isUsing));

        if (!equal.isEmpty()) {
            String key = this.selectIdsSetByEqual(equal);
            if (key != null) {
                keys.add(key);
            }
        }
        return this.selectValuesByKeys(keys);
    }

    /**
     * 根据以下字段查询促销
     *
     * @return
     */
    public List<Map<String, ?>> selectActivityForOrder() {
        Map<String, List<Object>> equal = new HashMap<>();
        List<String> keys = new ArrayList<>();
        equal.put("type", Arrays.asList("1"));
        equal.put("status", Arrays.asList(Status.ACTIVITY.getValue(), Status.FAILURE.getValue()));
        if (!equal.isEmpty()) {
            String key = this.selectIdsSetByEqual(equal);
            if (key != null) {
                keys.add(key);
            }
        }

        return this.selectValuesByKeys(keys);
    }

    /**
     * 根据以下字段查询促销
     *
     * @param status  促销状态
     * @param isUsing 促销活动现在是否可用
     * @return
     */
    public List<Map<String, ?>> selectByStatusAndIsUsing(String status, String isUsing) {
        Map<String, List<Object>> equal = new HashMap<>();
        List<String> keys = new ArrayList<>();

        if (StringUtils.isNotEmpty(status)) {
            equal.put("status", Arrays.asList(status));
        }
        if (isUsing != null) {
            equal.put("isUsing", Arrays.asList(isUsing));
        }


        if (!equal.isEmpty()) {
            String key = this.selectIdsSetByEqual(equal);
            if (key != null) {
                keys.add(key);
            }
        }

        return this.selectValuesByKeys(keys);
    }

    /**
     * 根据以下字段查询促销
     *
     * @param status  促销状态
     * @param isUsing 促销活动现在是否可用
     * @return
     */
    public List<Map<String, ?>> selectByStatusAndIsUsing(List status, String isUsing) {
        Map<String, List<Object>> equal = new HashMap<>();
        List<String> keys = new ArrayList<>();

        if (status != null) {
            equal.put("status", status);
        }
        if (isUsing != null) {
            equal.put("isUsing", Arrays.asList(isUsing));
        }


        if (!equal.isEmpty()) {
            String key = this.selectIdsSetByEqual(equal);
            if (key != null) {
                keys.add(key);
            }
        }

        return this.selectValuesByKeys(keys);
    }

    /**
     * 根据字段属性查询可用促销活动
     *
     * @param status  促销活动状态
     * @param isUsing 促销是否在使用
     * @param type    促销活动层级，1为订单层级，2为订单行层级
     * @return
     */
    public List<Map<String, ?>> selectByStatusAndIsUsingAndType(String status, String isUsing, String type) {
        Map<String, List<Object>> equal = new HashMap<>();
        List<String> keys = new ArrayList<>();

        if (status != null) {
            equal.put("status", Arrays.asList(status));
        }
        if (isUsing != null) {
            equal.put("isUsing", Arrays.asList(isUsing));
        }
        if (StringUtils.isNotEmpty(type)) {
            equal.put("type", Arrays.asList(type));
        }


        if (!equal.isEmpty()) {
            String key = this.selectIdsSetByEqual(equal);
            if (key != null) {
                keys.add(key);
            }
        }

        return this.selectValuesByKeys(keys);
    }


    /**
     * @param status  促销活动状态
     * @param isUsing 促销使用状态
     * @param groups  促销规则组
     * @return
     */
    public List<Map<String, ?>> selectByStatusAndIsUsingAndGroups(String status, String isUsing, List<Object> groups) {
        Map<String, List<Object>> equal = new HashMap<>();
        List<String> keys = new ArrayList<>();

        if (status != null) {
            equal.put("status", Arrays.asList(status));
        }
        if (isUsing != null) {
            equal.put("isUsing", Arrays.asList(isUsing));
        }
        if (groups.size() > 0) {
            equal.put("group", groups);

        }

        if (!equal.isEmpty()) {
            String key = this.selectIdsSetByEqual(equal);
            if (key != null) {
                keys.add(key);
            }
        }

        return this.selectValuesByKeys(keys);
    }

    /**
     * 查询促销活动
     *
     * @param status    促销活动状态
     * @param isUsing   是否使用
     * @param group     分组
     * @param isOverlay 是否可叠加
     * @param type      活动类型
     * @return
     */
    public List<Map<String, ?>> selectByGroup(String status, String isUsing, String group, String type, String isOverlay) {
        Map<String, List<Object>> equal = new HashMap<>();
        List<String> keys = new ArrayList<>();

        if (StringUtils.isNotEmpty(status)) {
            equal.put("status", Arrays.asList(status));
        }
        if (StringUtils.isNotEmpty(isUsing)) {
            equal.put("isUsing", Arrays.asList(isUsing));
        }
        if (StringUtils.isNotEmpty(group)) {
            equal.put("group", Arrays.asList(group));
        }
        if (StringUtils.isNotEmpty(isOverlay)) {
            equal.put("isOverlay", Arrays.asList(isOverlay));

        }
        if (StringUtils.isNotEmpty(type)) {
            equal.put("type", Arrays.asList(type));
        }

        if (!equal.isEmpty()) {
            String key = this.selectIdsSetByEqual(equal);
            if (key != null) {
                keys.add(key);
            }
        }

        return this.selectValuesByKeys(keys);
    }

    /**
     * 查找订单行促销
     *
     * @param group
     * @return
     */
    public List<Map<String, ?>> selectActivityForEntry(String group) {
        Map<String, List<Object>> equal = new HashMap<>();
        List<String> keys = new ArrayList<>();
        equal.put("status", Arrays.asList(Status.FAILURE.getValue(), Status.ACTIVITY.getValue()));
        if (StringUtils.isNotEmpty(group)) {
            equal.put("group", Arrays.asList(group));
        }
        equal.put("type", Arrays.asList("2"));

        if (!equal.isEmpty()) {
            String key = this.selectIdsSetByEqual(equal);
            if (key != null) {
                keys.add(key);
            }
        }

        return this.selectValuesByKeys(keys);
    }

    /**
     * 该方法已弃用
     *
     * @param productCode
     * @return
     */
    public List<Map<String, ?>> selectByProductCode(String productCode) {
        Map<String, List<Object>> equal = new HashMap<>();
        List<String> keys = new ArrayList<>();

        if (productCode != null) {
            equal.put("productCode", Arrays.asList(productCode));
        }

        if (!equal.isEmpty()) {
            String key = this.selectIdsSetByEqual(equal);
            if (key != null) {
                keys.add(key);
            }
        }

        return this.selectValuesByKeys(keys);
    }
}
