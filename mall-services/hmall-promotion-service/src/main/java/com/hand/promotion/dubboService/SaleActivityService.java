package com.hand.promotion.dubboService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hand.dto.ResponseData;
import com.hand.hpromotion.ISaleActivityService;
import com.hand.promotion.pojo.SimpleMessagePojo;
import com.hand.promotion.pojo.activity.ActivityPojo;
import com.hand.promotion.pojo.activity.PromotionActivitiesPojo;
import com.hand.promotion.pojo.enums.MsgMenu;
import com.hand.promotion.service.IPromotionActivityService;
import com.hand.promotion.util.BeanMapExchange;
import com.hand.promotion.util.ResponseReturnUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * MAP促销活动操作接口
 * Created by darkdog on 2018/2/2.
 */
public class SaleActivityService implements ISaleActivityService {

    @Autowired
    private IPromotionActivityService promotionActivityService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 创建促销活动
     *
     * @param map MAP 入参
     * @return
     */
    @Override
    public ResponseData createActivity(Map map){
        ResponseData responseData = null;
        //将入参转换为对应的DTO
        boolean transResult = praseDate((Map) map.get("activity"));
        if (!transResult) {
            return ResponseReturnUtil.returnResp(null, MsgMenu.DATE_FORMATE_ERR, false);
        }
        PromotionActivitiesPojo promotionActivitiesPojo = BeanMapExchange.mapToObject(map, PromotionActivitiesPojo.class);
        logger.info(promotionActivitiesPojo.toString());
        try {
            String userId = JSONObject.parseObject(JSON.toJSONString(map)).getString("userId");
            //保存促销活动
            SimpleMessagePojo createResult = promotionActivityService.createActivity(promotionActivitiesPojo,userId);
            if (createResult.isSuccess()) {
                responseData = ResponseReturnUtil.returnResp(Arrays.asList(createResult.getObj()), MsgMenu.CREATE_ACTIVITY_SUCCESS, true);
            } else {
                responseData = ResponseReturnUtil.transSimpleMessage(createResult);
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseData = ResponseReturnUtil.returnResp(null, MsgMenu.CREATE_ACTIVITY_ERROR, false);
        }
        return responseData;
    }

    /**
     * 将activity中字符串格式的时间转换为时间戳
     *
     * @param activity
     */
    boolean praseDate(Map activity) {
        String startDate = (String) activity.get("startDate");
        String endDate = (String) activity.get("endDate");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setLenient(false);
        try {
            if (!StringUtils.isEmpty(startDate)) {
                activity.put("startDate", simpleDateFormat.parse(startDate));

            }
            if (!StringUtils.isEmpty(endDate)) {
                activity.put("endDate", simpleDateFormat.parse(endDate));

            }
        } catch (ParseException e) {
            logger.error("---时间转换异常---", e);
            return false;
        }
        return true;
    }


    /**
     * 分页查询所有促销活动
     *
     * @param map 可选字段 activityId（规则ID编码） 生效时间（StartDate） endDate(失效时间)
     *            activityName（规则名称）
     *            status（状态）
     */
    @Override
    public ResponseData query(Map<String, Object> map) {
        logger.info("-----------query-----------\n{}", JSON.toJSONString(map));
        try {
            Map data = (Map) map.get("data");
            int pageNum = (int) map.get("page");
            int pageSize = (int) map.get("pageSize");
            praseDate(data);
            ActivityPojo activity = BeanMapExchange.mapToObject(data, ActivityPojo.class);
            PromotionActivitiesPojo condition = new PromotionActivitiesPojo();
            condition.setActivity(activity);
            return promotionActivityService.queryPromotionByCondition(condition, pageNum, pageSize);
        } catch (Exception e) {
            logger.error("查询异常", e);
            return ResponseReturnUtil.returnFalseResponse("查询促销活动异常", "QUERY_PROMOTION_ERR");

        }
    }

    /**
     * 查询促销活动的详细信息
     *
     * @param id
     * @return
     */
    @Override
    public ResponseData detail(String id) {
        logger.info("-----------detail-----------\n{}", JSON.toJSONString(id));
        try {
            PromotionActivitiesPojo detail = promotionActivityService.findByPk(id);
            return ResponseReturnUtil.returnResp(Arrays.asList(detail), MsgMenu.SUCCESS, true);
        } catch (Exception e) {
            logger.error("--———查询促销活动异常——————", e);
            return ResponseReturnUtil.returnFalseResponse("查询促销活动异常", "QUERY_PROMOTION_ERR");
        }
    }

    /**
     * 启用促销活动
     *
     * @param maps
     * @return
     */
    @Override
    public ResponseData enableActivity(List<Map<String, Object>> maps) {
        List<String> errList = new ArrayList<>();
        try {
            for (Map<String, Object> map : maps) {
                String activityId = map.get("id").toString();
                SimpleMessagePojo result = promotionActivityService.enableActivity(activityId);
                if (!result.isSuccess()) {
                    errList.add(activityId);
                    continue;
                }
            }
            if (errList.size() != 0) {
                return ResponseReturnUtil.returnFalseResponse("部分停止异常" + errList.toString(), "ENABLE_ERR");
            }
            return ResponseReturnUtil.returnTrueResp(null);
        } catch (Exception e) {
            logger.error("---启用异常---", e);
            return ResponseReturnUtil.returnFalseResponse(e.getMessage(), "ENABLE_ERR");
        }

    }

    /**
     * 停用促销活动
     *
     * @param maps
     * @return
     */
    @Override
    public ResponseData inactiveActivity(List<Map<String, Object>> maps) {
        List<String> errList = new ArrayList<>();
        try {
            for (Map<String, Object> map : maps) {
                String activityId = map.get("id").toString();
                SimpleMessagePojo result = promotionActivityService.inactiveActivity(activityId);
                if (!result.isSuccess()) {
                    errList.add("<p>" + (String) map.get("activityName") + " " + result.getMessage().getMsg() + "</p>");
                }
            }
            if (errList.size() != 0) {
                errList.set(errList.size() - 1, errList.get(errList.size() - 1) + "停用异常");
                return ResponseReturnUtil.returnResp(errList, MsgMenu.INACTIVT_ERR, false);
            }
            return ResponseReturnUtil.returnTrueResp(null);
        } catch (Exception e) {
            logger.error("---停用异常---", e);
            return ResponseReturnUtil.returnFalseResponse(e.getMessage(), "INACTIVE_ERR");
        }

    }

    /**
     * 删除已失效的促销活动
     *
     * @param maps
     * @return
     */
    @Override
    public ResponseData delete(List<Map<String, Object>> maps) {
        try {
            List<String> errList = new ArrayList<>();
            for (Map<String, Object> map : maps) {
                String activityId = map.get("id").toString();
                SimpleMessagePojo simpleMessagePojo = promotionActivityService.deleteActivity(activityId);
                if (!simpleMessagePojo.isSuccess()) {
                    String activityName = (String) map.get("activityName");
                    errList.add(activityName);
                }
            }
            if (errList.size() != 0) {
                return ResponseReturnUtil.returnFalseResponse("删除异常" + errList.toString() + "促销活动状态应为已失效", MsgMenu.DELETE_ACTIVITY_STATUS_ERR.getCode());
            } else {
                return ResponseReturnUtil.returnTrueResp(null);
            }
        } catch (Exception e) {
            logger.error("促销活动删除异常", e);
            return ResponseReturnUtil.returnFalseResponse("促销活动删除异常", MsgMenu.DELETE_ACTIVITY_STATUS_ERR.getCode());
        }
    }

}
