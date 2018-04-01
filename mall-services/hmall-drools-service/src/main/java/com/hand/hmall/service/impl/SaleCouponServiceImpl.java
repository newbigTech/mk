package com.hand.hmall.service.impl;

import com.hand.hmall.client.IPromoteClientService;
import com.hand.hmall.dao.SaleConditionActionDao;
import com.hand.hmall.dao.SaleCouponDao;
import com.hand.hmall.dao.SaleOperatorDao;
import com.hand.hmall.dto.PagedValues;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.menu.SaleType;
import com.hand.hmall.menu.Status;
import com.hand.hmall.service.ISaleCouponService;
import com.hand.hmall.util.DateFormatUtil;
import com.hand.hmall.util.ResponseReturnUtil;
import com.hand.hmall.util.SaleCheckedLegalUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shanks on 2017/1/5.
 * @desp 优惠券处理Service
 */
@Service
public class SaleCouponServiceImpl implements ISaleCouponService {
    @Autowired
    private SaleCouponDao saleCouponDao;

    @Autowired
    private SaleConditionActionDao saleConditionActionDao;

    @Autowired
    private SaleOperatorDao saleOperatorDao;

    @Autowired
    private IPromoteClientService promoteClientService;


    /**
     * 查询优惠券列表 可选字段CouponId(优惠券Id编码)、couponName（优惠券名称）
     * couponCode（优惠券编码）、status（状态）、startDate（生效时间）、endDate（截止时间）
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData query(Map<String, Object> map) {


        try {
            PagedValues pagedValues = saleCouponDao.queryCoupon(map);

            ResponseData responseData = new ResponseData();
            responseData.setResp(pagedValues.getValues());
            responseData.setTotal((int) pagedValues.getTotal());
            return responseData;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        ResponseData responseData = new ResponseData();
        responseData.setMsg("读取失败");
        responseData.setSuccess(false);
        return responseData;

    }

    @Override
    public ResponseData queryAll() {
//        return saleCouponDao.queryCoupon();
        return null;
    }

    @Override
    public ResponseData queryActivity(Map<String, Object> map) throws ParseException {
        try {
            PagedValues pagedValues = saleCouponDao.queryCouponActivity(map);
            ResponseData responseData = new ResponseData();
            responseData.setResp(pagedValues.getValues());
            responseData.setTotal((int) pagedValues.getTotal());
            return responseData;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ResponseData responseData = new ResponseData();
        responseData.setMsg("读取失败");
        responseData.setSuccess(false);
        return responseData;
    }

    @Override
    public ResponseData queryByNotIn(Map<String, Object> map) {
        return new ResponseData(saleCouponDao.queryByNotIn(map));
    }


    @Override
    public ResponseData submit(Map<String, Object> map) {


        Map<String, Object> coupon = (Map<String, Object>) map.get("coupon");
        Map<String, Object> conditionsActions = new HashMap<>();

        conditionsActions.put("conditions", map.get("conditions"));
        conditionsActions.put("actions", map.get("actions"));
        conditionsActions.put("groups", map.get("groups"));
        conditionsActions.put("container", map.get("containers"));

        Date creationDate = new Date(System.currentTimeMillis());
        Long creationTime = creationDate.getTime();

        Long startDate = DateFormatUtil.stringToTimeStamp(coupon.get("startDate").toString());
        Long endDate = DateFormatUtil.stringToTimeStamp(coupon.get("endDate").toString());
        Long getStartDate = DateFormatUtil.stringToTimeStamp((String) coupon.get("getStartDate"));
        Long getEndDate = DateFormatUtil.stringToTimeStamp((String) coupon.get("getEndDate"));
        Long sysDate = System.currentTimeMillis();
        coupon.put("startDate", startDate);
        coupon.put("endDate", endDate);
        coupon.put("getStartDate", getStartDate);
        coupon.put("getEndDate", getEndDate);

        //couponId为空则为新建
        if (coupon.get("couponId") == null || coupon.get("couponId").equals("")) {

            coupon.put("creationTime", creationTime);
            coupon.put("lastCreationTime", creationTime);
            coupon.put("couponId", UUID.randomUUID());
            coupon.put("releaseId", UUID.randomUUID());

            if (startDate <= sysDate && sysDate < endDate) {
                coupon.put("status", Status.ACTIVITY.getValue());
            } else if (startDate > sysDate) {
                coupon.put("status", Status.DELAY.toString());
            } else if (endDate <= sysDate) {
                coupon.put("status", Status.FAILURE.getValue());
            }

            coupon.put("isUsing", 'Y');
            coupon.put("isSyn", "N");
            Map<String, Object> coup = saleCouponDao.submitCoupon(coupon);

            conditionsActions.put("detailId", coup.get("id"));
            conditionsActions.put("type", SaleType.COUPON.getValue());
            saleConditionActionDao.submitSaleCondition(conditionsActions);

            Map<String, Object> operatorMap = new HashMap<>();
            operatorMap.put("operator", map.get("userId"));
            operatorMap.put("operation", coupon.get("couponName"));
            operatorMap.put("changeDate", creationTime);
            operatorMap.put("type", SaleType.COUPON.getValue());
            operatorMap.put("baseId", coupon.get("couponId"));
            operatorMap.put("parentId", coupon.get("releaseId"));
            saleOperatorDao.submit(operatorMap);

            Map<String, Object> redemptionDao = new HashMap<>();
            redemptionDao.put("redemptionId", coupon.get("id").toString());
            redemptionDao.put("type", SaleType.COUPON.getValue());
            redemptionDao.put("number", coupon.get("maxRedemption"));
            promoteClientService.submitRedemption(redemptionDao);

        } else {

            coupon.put("lastCreationTime", DateFormatUtil.stringToTimeStamp(coupon.get("creationTime").toString()));
            coupon.put("creationTime", creationTime);
            coupon.put("releaseId", UUID.randomUUID());
            coupon.put("isUsing", "Y");

            if (startDate <= sysDate && sysDate < endDate) {
                coupon.put("status", Status.ACTIVITY.getValue());
            } else if (startDate > sysDate) {
                coupon.put("status", Status.DELAY.toString());
            } else if (endDate <= sysDate) {
                coupon.put("status", Status.FAILURE.getValue());
            }

            List<Map<String, ?>> coupons = saleCouponDao.selectByCouponId(coupon.get("couponId").toString());
            for (int i = 0; i < coupons.size(); i++) {
                Map<String, Object> updateCoupon = (Map<String, Object>) coupons.get(i);

                if (updateCoupon.get("isUsing") != null) {
                    updateCoupon.put("isUsing", 'N');
//                    updateCoupon.put("isSyn","Y");
                }
                saleCouponDao.updateCoupon(updateCoupon);
            }
            coupon.put("isSyn", "N");
            saleCouponDao.submitCoupon(coupon);

            conditionsActions.put("detailId", coupon.get("id"));
            conditionsActions.put("type", SaleType.COUPON.getValue());
            saleConditionActionDao.submitSaleCondition(conditionsActions);

            Map<String, Object> operatorMap = new HashMap<>();
            operatorMap.put("operator", map.get("userId"));
            operatorMap.put("changeDate", creationTime);
            operatorMap.put("operation", coupon.get("couponName"));
            operatorMap.put("type", SaleType.COUPON.getValue());
            operatorMap.put("baseId", coupon.get("couponId"));
            operatorMap.put("parentId", coupon.get("releaseId"));
            saleOperatorDao.submit(operatorMap);

            Map<String, Object> redemptionDao = new HashMap<>();
            redemptionDao.put("redemptionId", coupon.get("id").toString());
            redemptionDao.put("type", SaleType.COUPON.getValue());
            redemptionDao.put("number", coupon.get("maxRedemption"));
            promoteClientService.submitRedemption(redemptionDao);

        }

        return new ResponseData(coupon);

    }

    @Override
    public ResponseData delete(List<Map<String, Object>> maps) {

        List<Map<String, Object>> mapList = new ArrayList<>();
        List<Map<String, Object>> mapErrorList = new ArrayList<>();
        for (Map<String, Object> map : maps) {

            Map<String, Object> deleteMap = (Map<String, Object>) saleCouponDao.select(map.get("id").toString());
            if (deleteMap.get("status").equals(Status.FAILURE.getValue())) {
                deleteMap.put("status", Status.EXPR.getValue());
                saleCouponDao.update(deleteMap);

                mapList.add(deleteMap);
            } else {
                mapErrorList.add(deleteMap);
            }
        }
        if (!mapErrorList.isEmpty()) {
            String error = "";
            for (Map<String, Object> errorMap : mapErrorList) {
                error += "【" + errorMap.get("couponName") + "】不能删除!\n";
            }
            return ResponseReturnUtil.returnFalseResponse(error, null);
        }

        return new ResponseData(mapList);
    }

    @Override
    public void deleteReal(List<Map<String, Object>> maps) {

        for (Map<String, Object> map : maps) {
            saleCouponDao.delete(map.get("id").toString());
            Map<String, Object> conditionAction = saleConditionActionDao.selectByDetailIdAndType(map.get("id").toString(), SaleType.COUPON.getValue());
            saleConditionActionDao.delete(conditionAction.get("id").toString());
            promoteClientService.deleteRedemption(map.get("id").toString(), SaleType.COUPON.getValue());
        }
    }

    @Override
    public Map<String, Object> startUsing(Map<String, Object> map) {
        Map couponMap = saleCouponDao.select(map.get("id").toString());
        couponMap.put("status", Status.ACTIVITY.getValue());
        couponMap.put("isSyn", "N");
        saleCouponDao.updateCoupon(couponMap);
        return couponMap;
    }

    @Override
    public Map<String, Object> endUsing(Map<String, Object> map) {
        Map couponMap = saleCouponDao.select(map.get("id").toString());
        couponMap.put("status", Status.INACTIVE.getValue());
        couponMap.put("isSyn", "N");
        saleCouponDao.updateCoupon(couponMap);
        return couponMap;
    }

    @Override
    public Map<String, Object> selectCouponDetail(String id) {

        Map<String, Object> coupon = (Map<String, Object>) saleCouponDao.selectCouponDetail(id);
        Map<String, Object> conditionActions = saleConditionActionDao.selectByDetailIdAndType(id, SaleType.COUPON.getValue());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("coupon", coupon);
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
        if (conditionActions.get("id") != null) {
            map.put("conditionsId", conditionActions.get("id"));
        }
        return map;

    }

    @Override
    public ResponseData selectByCode(String couponCode) {
        List coupons = saleCouponDao.selectCouponByCouponCode(couponCode, "Y");
        return coupons.isEmpty() ? new ResponseData(false, "PROMOTE_COUPON_001") : new ResponseData(coupons);
    }

    /**
     * 查询可以领取的优惠券
     *
     * @param couponCode
     * @return
     */
    @Override
    public ResponseData selectByCodeCanUse(String couponCode) {
        return new ResponseData(saleCouponDao.selectByCodeCanUse(couponCode));
    }

    /**
     * 根据优惠券Id查询优惠券
     *
     * @param id
     * @return
     */
    @Override
    public ResponseData selectById(String id) {
        List<Map<String, ?>> maps = new ArrayList<>();
        Map<String, ?> coupon = saleCouponDao.select(id);
        if (coupon != null) {
            if (!coupon.isEmpty()) {
                maps.add(coupon);
            }
        }
        return new ResponseData(maps);
    }

    /**
     * 根据主键查询优惠券编码Id
     *
     * @param id
     * @return
     */
    @Override
    public ResponseData selectCouponIdById(String id) {
        Map<String, ?> coupon = saleCouponDao.select(id);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("isUsing", "Y");
        parameters.put("couponId", coupon.get("couponId"));
        return new ResponseData(saleCouponDao.selectByMutilEqField(parameters));

    }

    /**
     * 根据主键查询isUsing为Y的优惠券
     *
     * @param couponId
     * @return
     */
    @Override
    public ResponseData selectByCouponId(String couponId) {
        List<Map<String, ?>> maps = new ArrayList<>();
        Map<String, Object> query = new HashedMap();
        query.put("id", couponId);
        query.put("isUsing", "Y");

        Map<String, ?> coupon = saleCouponDao.select(couponId);
        if (coupon != null) {
            if (!coupon.isEmpty()) {
                maps.add(coupon);
            }
        }
        return new ResponseData(maps);
    }

    /**
     * 校验优惠券是否是排它券（霸王券）
     *
     * @return
     */
    @Override
    public boolean checkExclusiveCoupon(String couponId) {
        ResponseData responseData = promoteClientService.getByCouponId(couponId);
        Map customerCoupon = (Map) ResponseReturnUtil.getRespObj(responseData);
        if (customerCoupon != null) {
            String cid = (String) customerCoupon.get("cid");
            Map<String, ?> coupon = saleCouponDao.select(cid);
            String isExclisive = (String) coupon.get("isExclusive");
            if ("Y".equals(isExclisive)) {
                return true;
            } else {
                return false;
            }
        } else {
            throw new RuntimeException("要执行优惠券不存在");
        }
    }

    /**
     * 创建优惠券时，校验前台参数信息
     *
     * @param coupon
     * @return
     */
    @Override
    public List<String> checkedCoupon(Map<String, Object> coupon) {

        List<String> message = new ArrayList<>();
        if (coupon.get("couponName") == null || coupon.get("couponName").toString().trim().equals("")) {
            message.add("优惠券名称不能为空");
        }

        if (coupon.get("couponCode") == null || coupon.get("couponCode").toString().trim().equals("")) {
            message.add("优惠券编码不能为空");
        } else {

            List<Map<String, ?>> mapList = saleCouponDao.checkedCodeIsExist(coupon.get("couponCode").toString());
            if (mapList != null) {
                if (StringUtils.isEmpty((String) coupon.get("couponId"))) {
                    if (!mapList.isEmpty() && mapList.size() > 0) {
                        message.add("优惠券编码已经存在");
                    }
                }
            }
        }
        if (coupon.get("maxRedemptionPerCustomer") == null || coupon.get("maxRedemptionPerCustomer").toString().trim().equals("")) {
            message.add("客户最大兑换数不能为空");
        } else {
            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher isNum = pattern.matcher(coupon.get("maxRedemptionPerCustomer").toString());
            if (!isNum.matches()) {
                message.add("客户最大兑换数需为大于0的数字");
            } else {
                if (Integer.parseInt(coupon.get("maxRedemptionPerCustomer").toString()) <= 0) {
                    message.add("客户最大兑换数需为大于0的数字");
                }
            }
        }
        if (coupon.get("maxRedemption") == null || coupon.get("maxRedemption").toString().trim().equals("")) {
            message.add("卷最大兑换数不能为空");
        } else {
            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher isNum = pattern.matcher(coupon.get("maxRedemption").toString());
            if (!isNum.matches()) {
                message.add("卷最大兑换数需为大于0的数字");
            } else {
                if (Integer.parseInt(coupon.get("maxRedemption").toString()) <= 0) {
                    message.add("卷最大兑换数需为大于0的数字");
                }
            }
        }

        SaleCheckedLegalUtil.checkedDateLegal(coupon, message);

        return message;
    }


}
