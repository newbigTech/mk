package com.hand.hmall.service.impl;

import com.alibaba.fastjson.JSON;
import com.hand.hmall.dao.*;
import com.hand.hmall.dto.PagedValues;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.menu.CreateJarType;
import com.hand.hmall.menu.SaleType;
import com.hand.hmall.menu.Status;
import com.hand.hmall.service.ICheckedConditionActionService;
import com.hand.hmall.service.IRuleTempService;
import com.hand.hmall.service.ISaleActivityService;
import com.hand.hmall.temp.ActivityTemp;
import com.hand.hmall.temp.RuleInputTemp;
import com.hand.hmall.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shanks on 2017/1/5.
 */
@Service
public class SaleActivityServiceImpl implements ISaleActivityService {
    @Autowired
    private SaleActivityDao saleActivityDao;
    @Autowired
    private SaleConditionActionDao saleConditionActionDao;
    @Autowired
    private SaleOperatorDao saleOperatorDao;
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private SalePromotionCodeDao salePromotionCodeDao;
    @Autowired
    private SaleProductThread saleProductThread;
    @Autowired
    private IRuleTempService ruleTempService;
    @Autowired
    private ActivityCreateJarThread activityCreateJarThread;
    @Autowired
    private ICheckedConditionActionService checkedConditionActionService;

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    /**
     * 分页查询所有促销活动
     *
     * @param map 可选字段 activityId（规则ID编码） 生效时间（StartDate） endDate(失效时间)
     *            activityName（规则名称）
     *            status（状态）
     * @param map
     * @return
     */
    @Override
    public ResponseData query(Map<String, Object> map) {

        try {
            PagedValues values = saleActivityDao.querySaleActivity(map);
            ResponseData responseData = new ResponseData();

            List<Map<String, Object>> returnMap = new ArrayList<>();
            List<Map<String, ?>> valueMaps = values.getValues();
            for (int i = 0; i < valueMaps.size(); i++) {

                Map<String, Object> mapData = (Map<String, Object>) valueMaps.get(i);

//                Map<String,?> getGroup=groupDao.select(mapData.get("group").toString());
//                if(getGroup.get("name")==null) {
//                    mapData.put("group", "获取分组信息错误");
//                }else {
//                    mapData.put("group", getGroup.get("name"));
//                }
                returnMap.add(mapData);
            }

            responseData.setResp(returnMap);
            responseData.setTotal((int) values.getTotal());
            return responseData;
        } catch (ParseException e) {
            logger.error("查询促销信息异常", e);
        }
        return new ResponseData();
    }

    /**
     * 保存促销的描述、条件、结果
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData submit(Map<String, Object> map) {

        Map<String, Object> activity = (Map<String, Object>) map.get("activity");
        //获取conditions actions groups containers数据保存到对应的map
        Map<String, Object> conditionsActions = new HashMap<>();
        List<Map<String, Object>> conditions = (List<Map<String, Object>>) map.get("conditions");
        conditionsActions.put("conditions", conditions);

        List<Map<String, Object>> actions = (List<Map<String, Object>>) map.get("actions");
        conditionsActions.put("actions", actions);

        List<Map<String, Object>> groups = (List<Map<String, Object>>) map.get("groups");
        conditionsActions.put("groups", groups);

        List<Map<String, Object>> containers = (List<Map<String, Object>>) map.get("containers");
        conditionsActions.put("containers", containers);
        //促销创建时间为系统当前时间
        Date creationDate = new Date(System.currentTimeMillis());
        Long creationTime = creationDate.getTime();
        logger.info("------creationTime-----" + creationTime + "-----date----" + DateUtil.formMillstToDate(creationTime, "yyyy-MM-dd HH:mm:ss"));
        //保存的促销时间为时间戳
        Long sysDate = System.currentTimeMillis();
        Long startDate = DateFormatUtil.stringToTimeStamp(activity.get("startDate").toString());
        Long endDate = DateFormatUtil.stringToTimeStamp(activity.get("endDate").toString());
        activity.put("startDate", startDate);
        activity.put("endDate", endDate);
        //判断该促销活动是否存在以确定是创建还是更新操作
        if (activity.get("activityId") == null || activity.get("activityId").equals("")) {

            activity.put("creationTime", creationTime);
            activity.put("lastCreationTime", creationTime);
            //判断促销活动状态
            if (startDate <= sysDate && sysDate < endDate) {
                activity.put("status", Status.ACTIVITY.getValue());
            } else if (startDate > sysDate) {
                activity.put("status", Status.DELAY.toString());
            } else if (endDate <= sysDate) {
                activity.put("status", Status.FAILURE.getValue());
            }
            //增加操作人信息
            String operate = (String) map.get("operate");
            if (StringUtils.isNotEmpty(operate) && "inactive".equals(operate.trim())) {
                activity.put("status", Status.INACTIVE.getValue());
                activity.put("startDate", sysDate);
            }
            activity.put("activityId", UUID.randomUUID());
            activity.put("releaseId", UUID.randomUUID());
            activity.put("isUsing", 'Y');
            //保存促销活动描述信息
            saleActivityDao.submitSaleActivity(activity);
            //保存促销活动条件结果信息
            conditionsActions.put("detailId", activity.get("id"));
            conditionsActions.put("containerFlag", map.get("containerFlag"));
            conditionsActions.put("type", Status.ACTIVITY.getValue());
            saleConditionActionDao.submitSaleCondition(conditionsActions);
            //保存操作人信息
            Map<String, Object> operatorMap = new HashMap<>();
            operatorMap.put("operator", map.get("userId"));
            operatorMap.put("operation", activity.get("activityName"));
            operatorMap.put("changeDate", creationTime);
            operatorMap.put("type", Status.ACTIVITY.getValue());
            operatorMap.put("baseId", activity.get("activityId"));
            operatorMap.put("parentId", activity.get("releaseId"));
            saleOperatorDao.submit(operatorMap);
            //生成保存促销商品关联关系
            saleProductThread.setActivity(activity);
            saleProductThread.setGroups(groups);
            saleProductThread.setConditions(conditions);
            saleProductThread.setContainers(containers);
            saleProductThread.setType("ADD");
            if (StringUtils.isNotEmpty(operate) && "inactive".equals(operate.trim())) {
                saleProductThread.setType("INACTIVE");
            }
            new Thread(saleProductThread).start();

        } else {
            //添加新的促销活动的信息
            activity.put("lastCreationTime", DateFormatUtil.stringToTimeStamp(activity.get("creationTime").toString()));
            activity.put("creationTime", creationTime);
            activity.put("releaseId", UUID.randomUUID());
            activity.put("isUsing", "Y");
            //修改促销活动，时间设置逻辑start
            if (startDate <= sysDate && sysDate < endDate) {
                activity.put("status", Status.ACTIVITY.getValue());
            } else if (startDate > sysDate) {
                activity.put("status", Status.DELAY.toString());
            } else if (endDate <= sysDate) {
                activity.put("status", Status.FAILURE.getValue());
            }
            //获取修改前促销
            List<Map<String, ?>> activities = saleActivityDao.selectByActivityIdAndUsing(activity.get("activityId").toString(), "Y");

//            for (int i = 0; i < activities.size(); i++) {
            if (CollectionUtils.isNotEmpty(activities)) {
                Map<String, Object> activityMap = (Map<String, Object>) activities.get(0);
                Long preStartDate = Long.parseLong(activityMap.get("startDate").toString());
                Long preEndDate = Long.parseLong(activityMap.get("endDate").toString());
                //对于促销活动的修改，默认不能修改生效时间
                if (preStartDate >= startDate) {
                    ResponseData responseData = new ResponseData();
                    responseData.setSuccess(false);
                    responseData.setMsg("修改后的规则起始时间要大于原规则起始时间");
                    return responseData;
                }
                String status = (String) activityMap.get("status");
                if (Status.FAILURE.name().equals(status) || Status.EXPR.name().equals(status) || Status.INACTIVE.name().equals(status)) {
                    ResponseData responseData = new ResponseData();
                    responseData.setSuccess(false);
                    responseData.setMsg("该状态促销不能修改");
                    return responseData;
                } else if (Status.ACTIVITY.name().equals(status)) {
                    //修改活动中促销，原促销生效截止时间换为当前系统时间，新建促销生效时间为用户的选择时间时间
                    activityMap.put("endDate", sysDate);
                    activityMap.put("isUsing", "N");
                    activityMap.put("status", Status.FAILURE.getValue());
                    //更新原促销
                    saleActivityDao.updateSaleActivity(activityMap);

                    //保存修改后促销
                    saleActivityDao.submitSaleActivity(activity);
                    //保存促销的条件与结果
                    conditionsActions.put("detailId", activity.get("id"));
                    conditionsActions.put("containerFlag", map.get("containerFlag"));
                    conditionsActions.put("type", Status.ACTIVITY.getValue());
                    saleConditionActionDao.submitSaleCondition(conditionsActions);
                    //操作人员信息
                    Map<String, Object> operatorMap = new HashMap<>();
                    operatorMap.put("operator", map.get("userId"));
                    operatorMap.put("changeDate", creationTime);
                    operatorMap.put("operation", activity.get("activityName"));
                    operatorMap.put("type", Status.ACTIVITY.getValue());
                    operatorMap.put("baseId", activity.get("activityId"));
                    operatorMap.put("parentId", activity.get("releaseId"));
                    saleOperatorDao.submit(operatorMap);
                    //删除原促销商品关联关系
                    List<Map<String, ?>> salePromotionCodeList = salePromotionCodeDao.selectByActivityId(activityMap.get("activityId").toString());
                    for (Map<String, ?> salePromotionCode : salePromotionCodeList) {
                        salePromotionCodeDao.delete(salePromotionCode.get("id").toString());
                    }

                    //保存商品与促销关联关系
                    saleProductThread.setActivity(activity);
                    saleProductThread.setGroups(groups);
                    saleProductThread.setConditions(conditions);
                    saleProductThread.setContainers(containers);
                    saleProductThread.setType("ADD");
                    new Thread(saleProductThread).start();
                } else {
                    //待生效状态，直接更新促销
                    activity.put("id", activityMap.get("id"));
                    saleActivityDao.updateSaleActivity(activity);
                    Map preConditionActions = saleConditionActionDao.selectByDetailIdAndType((String) activityMap.get("id"), Status.ACTIVITY.getValue());
                    conditionsActions.put("id", preConditionActions.get("id"));
                    conditionsActions.put("detailId", activity.get("id"));
                    conditionsActions.put("containerFlag", map.get("containerFlag"));
                    conditionsActions.put("type", Status.ACTIVITY.getValue());
                    saleConditionActionDao.updateSaleCondition(conditionsActions);
                    Map<String, Object> operatorMap = new HashMap<>();
                    operatorMap.put("operator", map.get("userId"));
                    operatorMap.put("changeDate", creationTime);
                    operatorMap.put("operation", activity.get("activityName"));
                    operatorMap.put("type", Status.ACTIVITY.getValue());
                    operatorMap.put("baseId", activity.get("activityId"));
                    operatorMap.put("parentId", activity.get("releaseId"));
                    saleOperatorDao.submit(operatorMap);
                    //删除原促销商品关联关系
                    List<Map<String, ?>> salePromotionCodeList = salePromotionCodeDao.selectByActivityId(activityMap.get("activityId").toString());
                    for (Map<String, ?> salePromotionCode : salePromotionCodeList) {
                        salePromotionCodeDao.delete(salePromotionCode.get("id").toString());
                    }

                    //保存商品与促销关联关系
                    saleProductThread.setActivity(activity);
                    saleProductThread.setGroups(groups);
                    saleProductThread.setConditions(conditions);
                    saleProductThread.setContainers(containers);
                    saleProductThread.setType("ADD");
                    new Thread(saleProductThread).start();
                }
            }

//            }

        }
        return new ResponseData(activity);


    }

    /**
     * 删除已失效的促销活动
     * 非物理删除，更新促销状态为EXPR（已删除）
     *
     * @param maps
     * @return
     */
    @Override
    public ResponseData delete(List<Map<String, Object>> maps) {

        List<Map<String, Object>> returnMapList = new ArrayList<>();
        List<Map<String, Object>> returnErrorMapList = new ArrayList<>();


        for (int i = 0; i < maps.size(); i++) {
            Map<String, Object> deleteMap = (Map<String, Object>) saleActivityDao.select(maps.get(i).get("id").toString());
            if (deleteMap.get("status").equals(Status.FAILURE.getValue())) {
                deleteMap.put("status", Status.EXPR.getValue());
                saleActivityDao.update(deleteMap);

                returnMapList.add(deleteMap);
                List<Map<String, ?>> mapList = salePromotionCodeDao.selectByActivityId(deleteMap.get("activityId").toString());
                for (Map<String, ?> mapData : mapList) {
                    salePromotionCodeDao.delete(mapData.get("id").toString());
                }
            } else {
                returnErrorMapList.add(deleteMap);
            }
        }

        if (!returnErrorMapList.isEmpty()) {
            String error = "";
            for (Map<String, Object> errorMap : returnErrorMapList) {
                error += "【" + errorMap.get("activityName") + "】状态不是已失效，不能删除!,\n";
            }
            return ResponseReturnUtil.returnFalseResponse(error, null);
        }
        return new ResponseData(returnMapList);

    }

    /**
     * 物理删除，从redis中删除促销记录
     *
     * @param maps
     */
    @Override
    public void deleteReal(List<Map<String, Object>> maps) {
        for (Map<String, Object> map : maps) {
            //删除促销描述信息
            saleActivityDao.delete(map.get("id").toString());
            Map<String, Object> conditionAction = saleConditionActionDao.selectByDetailIdAndType(map.get("id").toString(), SaleType.ACTIVITY.getValue());
            //删除促销条件结果
            saleConditionActionDao.delete(conditionAction.get("id").toString());
            List<Map<String, ?>> mapList = salePromotionCodeDao.selectByActivityId(map.get("activityId").toString());
            //删除促销商品关联关系
            for (Map<String, ?> mapData : mapList) {
                salePromotionCodeDao.delete(mapData.get("id").toString());
            }
        }
    }


    /**
     * 启动促销活动，更新促销活动的状态，更新促销商品关联关系状态
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData activity(Map<String, Object> map) {


        Map saleMap = saleActivityDao.select(map.get("id").toString());
        Long startDate = (Long) saleMap.get("startDate");
        Long endDate = (Long) saleMap.get("endDate");
        Long sysDate = System.currentTimeMillis();
        String status = saleMap.get("status").toString();
        //根据时间判断促销活动状态 比较实际状态与保存状态之间是否有差异，存在则将状态更新为实际状态
        if (startDate <= sysDate && sysDate < endDate) {
            if (!status.equals(Status.ACTIVITY.getValue())) {
                saleMap.put("status", Status.ACTIVITY.getValue());
                saleActivityDao.update(saleMap);
            }
        } else if (startDate > sysDate) {
            if (!status.equals(Status.DELAY.toString())) {
                saleMap.put("status", Status.DELAY.toString());
                saleActivityDao.update(saleMap);
            }
        } else if (endDate <= sysDate) {
            if (!status.equals(Status.FAILURE.getValue())) {
                saleMap.put("status", Status.FAILURE.getValue());
                saleActivityDao.update(saleMap);
            }
        }
        //更新促销商品关联关系状态
        if (saleMap.get("status").equals(Status.ACTIVITY.getValue()) && !status.equals(Status.ACTIVITY.getValue())) {
            List<Map<String, ?>> salePromotionCodeList = salePromotionCodeDao.selectByActivityId(saleMap.get("activityId").toString());
            for (int j = 0; j < salePromotionCodeList.size(); j++) {
                Map<String, Object> data = (Map<String, Object>) salePromotionCodeList.get(j);
                data.put("status", Status.ACTIVITY.getValue());
                salePromotionCodeDao.update(data);
            }
        } else if (!saleMap.get("status").equals(Status.ACTIVITY.getValue()) && status.equals(Status.ACTIVITY.getValue())) {
            List<Map<String, ?>> salePromotionCodeList = salePromotionCodeDao.selectByActivityId(saleMap.get("activityId").toString());
            for (int j = 0; j < salePromotionCodeList.size(); j++) {
                Map<String, Object> data = (Map<String, Object>) salePromotionCodeList.get(j);
                data.put("status", Status.INACTIVE.getValue());
                salePromotionCodeDao.update(data);
            }
        }
        saleActivityDao.updateSaleActivity(saleMap);
        return new ResponseData(map);

    }

    /**
     * 停用促销活动
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData inactive(Map<String, Object> map) {

        //endMap 停用前促销；
        Map endMap = saleActivityDao.select(map.get("id").toString());
        Long endDate = Long.parseLong(endMap.get("endDate").toString());
        Long startDate = Long.parseLong(endMap.get("startDate").toString());
        String status = (String) map.get("status");
        Long sysDate = System.currentTimeMillis();

        //对于活动中的促销，当前时间前的置为失效，之后的置为停用
        if (startDate < sysDate && sysDate < endDate && "ACTIVITY".equals(status)) {
            //先查询出当前促销详情
            Map<String, Object> detailMap = selectActivityDetail(map.get("id").toString());

            endMap.put("status", Status.FAILURE.getValue());
            endMap.put("endDate", sysDate);
            endMap.put("isUsing", "N");
            saleActivityDao.updateSaleActivity(endMap);

            //在原促销基础上创建新的促销，其状态为已停用
            Map activity = (Map) detailMap.get("activity");
            activity.remove("activityId");
            activity.remove("id");
            activity.remove("creationTime");
            activity.remove("lastCreationTime");
            detailMap.put("operate", "inactive");
            activity.put("startDate", DateUtil.formMillstToDate(sysDate, "yyyy-MM-dd HH:mm:ss"));
            activity.put("endDate", DateUtil.formMillstToDate(endDate, "yyyy-MM-dd HH:mm:ss"));
            logger.info("------inactive reall----{}", JSON.toJSONString(detailMap));

            ResponseData responseData = submitActivity(detailMap);
            Map<String, Object> activityMap = (Map<String, Object>) responseData.getResp().get(0);
            endMap = activityMap;
            //更新原促销关联关系
            salePromotionCodeDao.deleteByEq("activityId", map.get("activityId").toString());
        } else {
            endMap.put("status", Status.INACTIVE.getValue());
            saleActivityDao.updateSaleActivity(endMap);
        }
        //更新商品促销关联状态
        List<Map<String, ?>> salePromotionCodeList = salePromotionCodeDao.selectByActivityId(endMap.get("activityId").toString());
        for (int j = 0; j < salePromotionCodeList.size(); j++) {
            Map<String, Object> data = (Map<String, Object>) salePromotionCodeList.get(j);
            data.put("status", Status.INACTIVE.getValue());
            salePromotionCodeDao.update(data);
        }
        return new ResponseData(endMap);
    }

    /**
     * 查询促销活动详细信息
     *
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> selectActivityDetail(String id) {
        Map<String, Object> template = (Map<String, Object>) saleActivityDao.selectActivityDetail(id);
        Map<String, Object> conditionActions = (Map<String, Object>) saleConditionActionDao.selectByDetailIdAndType(id, Status.ACTIVITY.getValue());

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("activity", template);
        if (conditionActions.get("conditions") != null) {
            map.put("conditions", conditionActions.get("conditions"));
        }
        if (conditionActions.get("actions") != null) {
            map.put("actions", conditionActions.get("actions"));
        }
        if (conditionActions.get("groups") != null) {
            map.put("groups", conditionActions.get("groups"));
        }
        if (conditionActions.get("containers") != null) {
            map.put("containers", conditionActions.get("containers"));
        }
        return map;
    }


    /**
     * 校验促销活动属性信息
     *
     * @param map
     * @return
     */
    @Override
    public List<String> checkedActivity(Map<String, Object> map) {
        List<String> message = new ArrayList<>();
        Map<String, Object> data = (Map<String, Object>) map.get("activity");


        if (data.get("activityName") == null || data.get("activityName").toString().trim().equals("")) {
            message.add("规则名称不能为空");
        }
        //判断促销层级字段是否为空
        if (StringUtils.isEmpty(data.get("type") == null ? "" : data.get("type").toString())) {
            message.add("规则层级不能为空");
        } else {
            data.put("type", data.get("type").toString());
            //不为空判断优先级是否存在
            if ("2".equals(data.get("type").toString())) {
                if (StringUtils.isEmpty(data.get("priority") == null ? "" : data.get("priority").toString())) {
                    message.add("商品层级促销优先级不能为空");
                }
            }
        }

        SaleCheckedLegalUtil.checkedDateLegal(data, message);
        return message;
    }

    /**
     * 根据条件查询促销活动
     *
     * @param status  促销状态 可选
     * @param isUsing 是否是最新的促销活动 可选
     * @return
     */
    @Override
    public List<Map<String, ?>> selectByStatusAndIsUsing(String status, String isUsing) {

        return saleActivityDao.selectByStatusAndIsUsing(status, isUsing);
    }

    /**
     * 根据条件查询促销活动
     *
     * @param status  状态集合
     * @param isUsing
     * @return
     */
    @Override
    public List<Map<String, ?>> selectByStatusAndIsUsing(List status, String isUsing) {
        return saleActivityDao.selectByStatusAndIsUsing(status, isUsing);
    }

    /**
     * 根据条件查询促销活动
     * @param status 活动状态
     * @param isUsing 是否是最新版本促销
     * @param type 分组ID
     * @return
     */
    @Override
    public List<Map<String, ?>> selectByStatusAndIsUsingAndGroups(String status, String isUsing, String type) {
        //默认分组查询除免邮分组外所有分组的促销活动
        if (type.equals(CreateJarType.ORIGINAL.getValue())) {
            List<Map<String, ?>> groups = groupDao.selectAll();
            List<Object> groupList = new ArrayList<>();
            for (Map<String, ?> map : groups) {
                if (!map.get("id").equals(CreateJarType.FREE_FREIGHT.getValue())) {
                    groupList.add(map.get("id"));
                }
            }
            return saleActivityDao.selectByStatusAndIsUsingAndGroups(status, isUsing, groupList);
        } else {
            //查询免邮分组促销
            return saleActivityDao.selectByStatusAndIsUsingAndGroups(status, isUsing, Arrays.asList(CreateJarType.FREE_FREIGHT.getValue()));
        }
    }

    /**
     * 根据参数促销活动并加载到内存
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData submitActivity(Map<String, Object> map) {

        logger.info("-----------submit-----------\n{}", JSON.toJSONString(map));

        //对定义的促销活动属性部分进行校验
        List<String> checkedActivity = checkedActivity(map);
        //对结果部分进行非空判断
        List<String> conditionActionsChecked = SaleCheckedLegalUtil.checkedSaleConditionAction(map);
        //对前两个校验得出的信息进行整合以便分析异常原因
        ResponseData returnCheckedMessage = SaleCheckedLegalUtil.returnCheckedMessage(checkedActivity, conditionActionsChecked);
        if (!returnCheckedMessage.isSuccess()) {
            return returnCheckedMessage;
        }


        //将activity参数转换成对应DTO
        Map<String, Object> activityGet = (Map<String, Object>) map.get("activity");
        RuleInputTemp ruleInputTemp = new RuleInputTemp();
        if (activityGet != null) {
            ActivityTemp activity = new ActivityTemp();
            BeanMapExchange.mapToBean(activityGet, activity);
            logger.info("ActivityTemp:{}", activity.toString());
            ruleInputTemp.setActivity(activity);
        }
        //设置condition，action到ruleInputTemp
        BeanMapExchange.exchangeConditionAction(map, ruleInputTemp);

        //存储促销活动中的所有信息
        ResponseData responseData = submit(map);
        //促销基础信息保存成功，拼接drools文件
        if (responseData.isSuccess()) {
            try {
                Map<String, Object> activityMap = (Map<String, Object>) responseData.getResp().get(0);
                ruleInputTemp.getActivity().setId(activityMap.get("id").toString());
                //创建规则内容(获取drools所需要的规则内容中的条件和结果模型)
                ResponseData ruleResp = ruleTempService.createRule(ruleInputTemp);
                if (!ruleResp.isSuccess()) {
                    delete((List<Map<String, Object>>) responseData.getResp());
                    return ruleResp;
                }
                activityCreateJarThread.setActivityMap(activityMap);
                /*生成规则文件(Drools6给我的最大不同就是把rules打包成jar，
                使用端通过kie-ci来动态从maven repo中获取指定rules jar版本，虽然和maven有紧耦合，
                简化以及清晰了rules的使用和动态升级：系统建立2个项目：一个Drools项目来实现规则，验收规则，
                生成jar包，另外一个就是真正要用规则的项目，直接通过引入不同版本的jar包实现规则动态升级)*/
                new Thread(activityCreateJarThread).start();

            } catch (NullPointerException e) {
                //回滚，如果生成规则文件失败就删除附带的促销活动
                e.printStackTrace();
                deleteReal((List<Map<String, Object>>) responseData.getResp());
                return ResponseReturnUtil.returnFalseResponse("缺少条件结果数据", "NULL_POINTER");
            } catch (IllegalArgumentException | ClassCastException e) {
                e.printStackTrace();
                //回滚，如果生成规则文件失败就删除附带的促销活动
                deleteReal((List<Map<String, Object>>) responseData.getResp());
                return ResponseReturnUtil.returnFalseResponse("条件结果数据错误", "NULL_POINTER");

            } catch (InvocationTargetException | IllegalAccessException e) {
                //回滚，如果生成规则文件失败就删除附带的促销活动
                e.printStackTrace();
                deleteReal((List<Map<String, Object>>) responseData.getResp());
                return ResponseReturnUtil.returnFalseResponse("系统错误", "NULL_POINTER");
            }
        }
        return responseData;

    }

    /**
     * 没有实现内容 ，可删除
     * @param map
     */
    @Override
    public void dealForBundle(Map map) {

    }

    /**
     * 获取促销详细信息，包括促销涉及到的的金额，商品code，商品类别，赠品code等
     *
     * @param activity
     * @return
     */

    public Map<String, ?> getActivityDetail(Map activity) {
        //促销详细信息
        Map detail = new HashMap();
        //获取促销的条件/结果数据
        Map conditionAndAction = saleConditionActionDao.selectByDetailIdAndType((String) activity.get("id"), "ACTIVITY");
        //条件数据集合
        List<Map> conditions = (List) conditionAndAction.get("conditions");
        for (Map condition : conditions) {
            checkedConditionActionService.getDetailForCondition(condition, detail);
        }
        //结果数据集合
        List<Map> actions = (List) conditionAndAction.get("actions");
        for (Map action : actions) {
            checkedConditionActionService.getDetailForAction(action, detail);
        }
        String activityType = (String) detail.get("activityType");
        if (StringUtils.isNotEmpty(activityType) && "o_target_price".equals(activityType)) {
            String bundleCode = checkedConditionActionService.getBundleCode((String) activity.get("id"));
            detail.put("productCodes", bundleCode);
        }
        return detail;
    }

    /**
     * 将传入的促销活动转换成推送到Zmall的促销活动
     *
     * @param activity
     * @return
     */
    @Override
    public Map<String, ?> getSynToZmallActivity(Map<String, ?> activity) {
        Map synActivity = new HashMap();

        synActivity.put("activityId", activity.get("id"));
        //活动名
        synActivity.put("activityName", activity.get("activityName"));
        //活动描述
        synActivity.put("activityDescription", activity.get("activityDes"));
        //relaseCode 唯一标识
        synActivity.put("releaseCode", activity.get("activityId"));
        //促销层级
        String type = (String) activity.get("type");
        synActivity.put("activityClass", type);
        //商品层级促销
        if ("2".equals(type.trim())) {
            //优先级
            synActivity.put("activityLevel", activity.get("priority"));
            //是否叠加
            synActivity.put("activityIsExclu", activity.get("isOverlay"));

        }
        //活动开始时间
        synActivity.put("startTime", (Long) activity.get("startDate"));
        //活动结束时间
        synActivity.put("endTime", (Long) activity.get("endDate") / 1000);
        //活动状态
        if (Status.ACTIVITY.name().equals(activity.get("status"))) {
            synActivity.put("status", 1);
        } else {
            synActivity.put("status", 0);
        }
        //促销详细信息
        Map activityDetail = getActivityDetail(activity);
        List details = new ArrayList();
        details.add(activityDetail);
        //促销类型
        synActivity.put("activityType", activityDetail.get("activityType"));
        activityDetail.remove("activityType");
        //详细信息
        synActivity.put("activityDetail", details);


        return synActivity;
    }

}
