package com.hand.hmall.service.impl;

import com.alibaba.fastjson.JSON;
import com.hand.hmall.dao.SaleActivityDao;
import com.hand.hmall.dto.MstBundles;
import com.hand.hmall.dto.MstBundlesMapping;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.mapper.MstBundlesMapper;
import com.hand.hmall.menu.Status;
import com.hand.hmall.service.IMstBundlesService;
import com.hand.hmall.service.IRuleTempService;
import com.hand.hmall.service.ISaleActivityService;
import com.hand.hmall.util.ActivityCreateJarThread;
import com.hand.hmall.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class MstBundlesServiceImpl implements IMstBundlesService {
    @Autowired
    private SaleActivityDao saleActivityDao;
    @Autowired
    private ISaleActivityService saleActivityService;
    @Autowired
    private ActivityCreateJarThread activityCreateJarThread;
    @Autowired
    private IRuleTempService ruleTempService;
    @Autowired
    private MstBundlesMapper mstBundlesMapper;

    private Logger logger = LoggerFactory.getLogger(this.getClass());




    @Override
    public ResponseData checkBundles(MstBundles bundles) {
        return null;
    }

    /**
     * 根据套装DTO生成对应的促销规则
     *
     * @param bundles
     * @return
     */
    @Override
    public ResponseData createActivity(MstBundles bundles) {
        String sysDate = DateUtil.formMillstToDate(System.currentTimeMillis()+30*1000,"yyyy-MM-dd HH:mm:ss");
        List<MstBundlesMapping> bundlesMappings =  bundles.getBundlesMappings();
        //拼接促销规则参数，调用促销规则生成接口
        HashMap bundlsActivity = new HashMap();
        //促销活动相关信息
        HashMap activity = new HashMap();
        //默认可叠加
        if(StringUtils.isEmpty(bundles.getIsOverlay())){
            activity.put("isOverlay", "Y");
        }else {
            activity.put("isOverlay", bundles.getIsOverlay());
        }

        //默认在前台展示
        activity.put("isExcludeShow", "N");
        //活动分组为DEFAULT
        activity.put("group", "DEFAULT");
        //生效时间为系统当前时间
        activity.put("startDate", sysDate);
        logger.info("startDate{}",sysDate);
        activity.put("endDate", "2099-01-01 12:12:12");
        activity.put("activityName", bundles.getName());
        activity.put("activityDes", bundles.getDescription());
        activity.put("type", "2");
        if(bundles.getPriority()==null){
            activity.put("priority", 1);
        }
        activity.put("priority", bundles.getPriority());
        activity.put("pageShowMes", bundles.getDescription());
        activity.put("creationTime",sysDate);
        activity.put("lastCreationTime",sysDate);
        if(StringUtils.isNotEmpty(bundles.getPromotionCode())){
            Map activityMap=saleActivityDao.select(bundles.getPromotionCode());
            activity.put("activityId",activityMap.get("activityId"));
        }

        //促销活动条件
        ArrayList conditions = new ArrayList();

        //促销活动容器
        ArrayList containers = new ArrayList();
        //目标容器参数
        HashMap targetParameters = new HashMap();
        //目标容器
        ArrayList targetValues = new ArrayList();
        for (int i = 0; i < bundlesMappings.size(); i++) {
            MstBundlesMapping mstBundlesMapping = bundlesMappings.get(i);
            HashMap container = new HashMap();
            HashMap targetValue = new HashMap();
            container.put("id", UUID.randomUUID().toString());
            targetValue.put("id", container.get("id"));
            container.put("definitionId", "CONTAINER");
            container.put("meaning", "容器" + i);
            targetValue.put("meaning", "容器" + i);
            targetValue.put("countNumber", mstBundlesMapping.getQuantity());
            targetValues.add(targetValue);
            container.put("flag", 1);
            ArrayList childs = new ArrayList();
            HashMap child = new HashMap();
            child.put("id", UUID.randomUUID().toString());
            child.put("definitionId", "o_product_range");
            child.put("parentId", container.get("id"));
            child.put("meaning", "商品范围");

            HashMap parameters = new HashMap();
            HashMap rangeValue = new HashMap();
            ArrayList values = new ArrayList();
            values.add(mstBundlesMapping.getProductId());
            rangeValue.put("value", values);
            HashMap value = new HashMap();
            value.put("value", mstBundlesMapping.getQuantity());
            HashMap operator = new HashMap();
            operator.put("value", "GEATER_THAN_OR_EQUAL");
            HashMap rangeOperator = new HashMap();
            rangeOperator.put("value", "MEMBER_OF");
            parameters.put("rangeValue", rangeValue);
            parameters.put("value", value);
            parameters.put("operator", operator);
            parameters.put("rangeOperator", rangeOperator);
            child.put("parameters", parameters);
            childs.add(child);
            container.put("child",childs);
            containers.add(container);
            logger.info("---container--{}", JSON.toJSONString(container));
        }
        //促销活动操作
        ArrayList actions = new ArrayList();
        HashMap action = new HashMap();
        action.put("id", UUID.randomUUID().toString());
        action.put("definitionId", "o_target_price");
        action.put("meaning", "目标包价格");
        targetParameters.put("targetValue",targetValues);
        targetParameters.put("value",bundles.getPrice());
        targetParameters.put("operator","MAX_THAN");
        action.put("parameters",targetParameters);
        actions.add(action);

        //促销组关联
        ArrayList groups = new ArrayList();
        bundlsActivity.put("activity",activity);
        bundlsActivity.put("conditions",conditions);
        bundlsActivity.put("containers",containers);
        bundlsActivity.put("actions",actions);
        bundlsActivity.put("groups",groups);
        bundlsActivity.put("containerFlag",1);
        logger.info("----------result of Activity---\n{}",JSON.toJSONString(bundlsActivity));
        //调用促销规则生成接口，生成促销规则
        ResponseData responseData = saleActivityService.submitActivity(bundlsActivity);
        if(responseData.isSuccess()){
            List resp = responseData.getResp();
            Map activityMap = (Map)resp.get(0);
            Map respMap = new HashMap();
            //返回生成的促销规则的主键
            respMap.put("promotionCode",activityMap.get("id"));
            resp.clear();
            resp.add(respMap);
        }
        return responseData;
    }
    @Override
    public ResponseData stopBundleActivity(List<String> promotionIds){
        try{
            List<Map<String,Object>> maps = new ArrayList<>();
            for (String promotionId : promotionIds) {
                Map map = new HashMap();
                map.put("id",promotionId);
                maps.add(map);
            }
            ResponseData responseData = new ResponseData(false,"停用失败");
            for(Map<String,Object> map:maps){
                Map endMap=saleActivityDao.select(map.get("id").toString());
                responseData = saleActivityService.inactive(endMap);
                ruleTempService.removeActivity(endMap.get("id").toString(),endMap.get("activityId").toString());
            }
            if(responseData.isSuccess()){
                List resp = responseData.getResp();
                Map activityMap = (Map)resp.get(0);
                Map respMap = new HashMap();
                respMap.put("promotionCode",activityMap.get("id"));
                resp.clear();
                resp.add(respMap);
            }
            return responseData;
        }catch (Exception e){
            logger.error("停用失败",e);
            return new ResponseData(false,"启用失败"+e.getMessage());
        }

    }

    /**
     * 启用套装的促销活动
     *
     * @param promotionIds
     * @return
     */
    @Override
    public ResponseData startBundleActivity(List<String> promotionIds){
        try{
            ResponseData responseData=new ResponseData();
            List<String> errorList=new ArrayList<>();
            List<Map > maps = new ArrayList<>();
            for (String promotionId : promotionIds) {
                Map map = new HashMap();
                map.put("id",promotionId);
                maps.add(map);
            }
            for(Map<String,Object> map:maps){
                Map endMap=saleActivityDao.select(map.get("id").toString());
                ResponseData resp= ruleTempService.releaseActivity(endMap);
                if(!responseData.isSuccess()){
                    errorList.add(resp.getMsg());
                    logger.debug(responseData.getMsg());
                }else {
                    saleActivityService.activity(endMap);
                }

            }
            if(!errorList.isEmpty()){
                responseData.setSuccess(false);
                responseData.setResp(errorList);
            }
            return responseData;
        }catch (Exception e){
            logger.error("启用失败",e);
            return new ResponseData(false,"启用失败"+e.getMessage());
        }

    }

    /**
     * 删除套装对应促销活动
     * @param promotionIds
     * @return
     */
    @Override
    public ResponseData deleteBundleActivity(List<String> promotionIds) {
        List<Map<String,Object> > maps = new ArrayList<>();
        for (String promotionId : promotionIds) {
            Map map = new HashMap();
            map.put("id",promotionId);
            Map endMap=saleActivityDao.select(map.get("id").toString());
            endMap.put("status", Status.FAILURE.getValue());
            saleActivityDao.update(endMap);
            maps.add(endMap);
        }
        ResponseData responseData=saleActivityService.delete(maps);
        if(responseData.isSuccess()) {
            new Thread(activityCreateJarThread).start();
        }
        return responseData;
    }

    /**
     * 更新套装促销活动
     * @param bundles
     * @param example
     */
    @Override
    public void updateBundle(MstBundles bundles, Object example) {
        mstBundlesMapper.updateByExampleSelective(bundles,example);
    }

    @Override
    public void insertBundel(MstBundles bundles) {
        mstBundlesMapper.insertSelective(bundles);
    }
}