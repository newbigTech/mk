package com.hand.hmall.controller;

import com.alibaba.fastjson.JSON;
import com.hand.hmall.code.MessageCode;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.menu.Status;
import com.hand.hmall.service.IMstBundlesMappingService;
import com.hand.hmall.service.IMstBundlesService;
import com.hand.hmall.service.IRuleTempService;
import com.hand.hmall.service.ISaleActivityService;
import com.hand.hmall.util.ActivityCreateJarThread;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;

import static java.util.Comparator.comparing;

/**
 * Created by shanks on 2017/1/5.
 * 促销活动管理controller
 */
@RestController
@RequestMapping(value = "/h/sale/activity", produces = {MediaType.APPLICATION_JSON_VALUE})
public class SaleActivityController {

    @Autowired
    private ISaleActivityService saleActivityService;
    @Autowired
    private IRuleTempService ruleTempService;
    @Autowired
    private ActivityCreateJarThread activityCreateJarThread;

    private Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);


    /**
     * 分页查询所有促销活动
     *
     * @param map 可选字段 activityId（规则ID编码） 生效时间（StartDate） endDate(失效时间)
     *            activityName（规则名称）
     *            status（状态）
     */
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData query(@RequestBody Map<String, Object> map) {
        logger.info("-----------query-----------\n{}", JSON.toJSONString(map));
        ResponseData responseData = saleActivityService.query(map);
        logger.info("resp-----" + JSON.toJSONString(responseData));
        return responseData;
    }

    /**
     * 查询activity头信息，详细信息用于商城活动数据同步
     *
     * @return
     */
    @RequestMapping(value = "/queryForZmall", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData queryForZmall() {
        ResponseData responseData = new ResponseData();
        logger.info("-----------query-----------\n");
        //获取需要同步的促销活动信息
        List<Map<String, ?>> activities = saleActivityService.selectByStatusAndIsUsing(Arrays.asList(Status.FAILURE, Status.ACTIVITY, Status.INACTIVE), "Y");
        List<Map> synList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(activities)) {
            for (Map<String, ?> activity : activities) {
                Map<String, ?> synToZmallActivity = saleActivityService.getSynToZmallActivity(activity);

                String activityType = (String) synToZmallActivity.get("activityType");
                if (StringUtils.isNotEmpty(activityType) && "o_target_price".equals(activityType)) {
                    List<Map> details = (List) synToZmallActivity.get("activityDetail");
                    Map detail = details.get(0);
                    String prodctCodes = (String) detail.get("productCodes");
                    if (StringUtils.isEmpty(prodctCodes))
                        continue;
                }
                synList.add(synToZmallActivity);
//
            }
            synList.sort(comparing((Map map) -> {
                Integer priority = (Integer) map.get("activityLevel");
                if (priority == null) {
                    priority = 0;
                }
                return priority;
            }).thenComparing(comparing(map -> {
                Long startTime = (Long) map.get("startTime");
                if (startTime == null) {
                    startTime = 0L;
                }
                return startTime;
            })).thenComparing(comparing(map -> {
                String id = map.get("activityId").toString();
                return id;
            })));
            for (int i = 0; i < synList.size(); i++) {
                Map synActivity = synList.get(i);
                Long endTime = (Long) synActivity.get("endTime");
                endTime = endTime > 2147483647 ? 2147483647 : endTime;
                synActivity.put("endTime", endTime);
                synActivity.put("startTime", (Long) synActivity.get("startTime") / 1000);
                synActivity.put("activityLevel", i + 1);
            }
            responseData.setResp(synList);

        } else {
            responseData.setMsgCode(MessageCode.NO_ACTIVITY.name());
            responseData.setMsg(MessageCode.NO_ACTIVITY.getValue());
        }
        return responseData;
    }


    /**
     * 新增和修改促销信息并且生成规则字符
     *
     * @param map
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData submit(@RequestBody Map<String, Object> map) throws ParseException {
        if (isBundlesForSubmit(map)) {
            ResponseData errResp = new ResponseData(false);
            errResp.setMsg("请在【捆绑套装】界面维护捆绑促销");
            return errResp;
        }
        return saleActivityService.submitActivity(map);

    }

    /**
     * 查询促销活动的详细信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData detail(@RequestParam("id") String id) {
        logger.info("-----------detail-----------\n{}", JSON.toJSONString(id));

        return new ResponseData(saleActivityService.selectActivityDetail(id));
    }

    /**
     * 启用促销活动
     * @param maps
     * @return
     */
    @RequestMapping(value = "/startUsing", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData activity(@RequestBody List<Map<String, Object>> maps) {
        logger.info("-----------startUsing-----------\n{}", JSON.toJSONString(maps));
        if (isBundlesForUpdate(maps)) {
            ResponseData errResp = new ResponseData(false);
            errResp.setMsg("请在【捆绑套装】界面维护捆绑促销");
            return errResp;
        }
        ResponseData responseData = new ResponseData();
        List<String> errorList = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            //加载促销活动jar包到内存
            ResponseData resp = ruleTempService.releaseActivity(map);
            if (!responseData.isSuccess()) {
                errorList.add(resp.getMsg());
                logger.debug(responseData.getMsg());
            } else {
                //启动促销活动，更新促销活动的状态，更新促销商品关联关系状态
                saleActivityService.activity(map);
            }

        }
        if (!errorList.isEmpty()) {
            responseData.setSuccess(false);
            responseData.setResp(errorList);
        }
        return responseData;
    }

    /**
     * 停用促销活动
     * @param maps
     * @return
     */
    @RequestMapping(value = "/endUsing", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData inactive(@RequestBody List<Map<String, Object>> maps) {
        logger.info("-----------endUsing-----------\n{}", JSON.toJSONString(maps));
        if (isBundlesForUpdate(maps)) {
            ResponseData errResp = new ResponseData(false);
            errResp.setMsg("请在【捆绑套装】界面维护捆绑促销");
            return errResp;
        }
        ResponseData responseData = new ResponseData();
        for (Map<String, Object> map : maps) {
            responseData = saleActivityService.inactive(map);
            ruleTempService.removeActivity(map.get("id").toString(), map.get("activityId").toString());
        }

        return responseData;

    }

    /**
     * 删除已失效的促销活动
     * @param maps
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData delete(@RequestBody List<Map<String, Object>> maps) {
        if (isBundlesForUpdate(maps)) {
            ResponseData errResp = new ResponseData(false);
            errResp.setMsg("请在【捆绑套装】界面维护捆绑促销");
            return errResp;
        }
        logger.info("-----------delete-----------\n{}", JSON.toJSONString(maps));
        ResponseData responseData = saleActivityService.delete(maps);
        if (responseData.isSuccess()) {
            new Thread(activityCreateJarThread).start();
        }
        return responseData;
    }


    /**
     * 校验促销操作是否针对套装固定价格促销
     *
     * @param mapList
     * @return
     */
    protected boolean isBundlesForUpdate(List<Map<String, Object>> mapList) {
//        for (Map map : mapList) {
//            Map<String, Object> detail = saleActivityService.selectActivityDetail(map.get("id").toString());
//            //根据actions条件中是只包含一个目标包价格判断促销是否是套装促销
//            return isBundlesForSubmit(detail);
//        }
        return false;
    }

    /**
     * 校验新建修改是否针对套装固定价格促销
     *
     * @param map
     * @return
     */
    protected boolean isBundlesForSubmit(Map map) {
//        List<Map> atcions = (List) map.get("actions");
//        //根据actions条件中是只包含一个目标包价格判断促销是否是套装促销
//        if (CollectionUtils.isNotEmpty(atcions) && atcions.size() == 1) {
//            Map action = atcions.get(0);
//            String definitionId = (String) action.get("definitionId");
//            if ("o_target_price".equals(definitionId)) {
//                return true;
//            }
//            return false;
//
//        }
        return false;
    }
}
