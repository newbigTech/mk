package com.hand.hmall.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hand.hmall.annotation.RedisTransaction;
import com.hand.hmall.client.IRuleClient;
import com.hand.hmall.client.IUserClient;
import com.hand.hmall.dao.CustomerCouponDao;
import com.hand.hmall.dao.CustomerRedemptionDao;
import com.hand.hmall.dto.PagedValues;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.menu.Status;
import com.hand.hmall.service.ICustomerCouponService;
import com.hand.hmall.util.GetMapValueUtil;
import com.hand.hmall.util.ResponseReturnUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

import static java.util.Comparator.comparing;

/**
 * Created by shanks on 2017/2/7.
 */
@Service
public class CustomerCouponServiceImpl implements ICustomerCouponService {
    @Autowired
    private CustomerCouponDao customerCouponDao;
    @Autowired
    private CustomerRedemptionDao customerRedemptionDao;
    @Autowired
    private IUserClient userClient;
    @Autowired
    private IRuleClient ruleClient;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 根据状态查询用户的优惠券，供Zmall调用
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData queryUserIdAndStatus(Map map) {
        //校验用户Id
        if (StringUtils.isEmpty(map.get("customerId"))) {
            return ResponseReturnUtil.returnFalseResponse("userID字段不能为空");
        }
        //查询用户对应状态下的优惠券
        List customerCouponList = customerCouponDao.queryUserIdAndStatus(map);
        List userCouponList = new ArrayList();
        if (!CollectionUtils.isEmpty(customerCouponList)) {
            for (int i = 0; i < customerCouponList.size(); i++) {
                Map customerCoupon = (Map) customerCouponList.get(i);
                //校验STATUS_01的优惠券是否过期，过期则不返回且更新优惠券状态为STATUS_03。用于限时使用的优惠券
                if (checkAndInvalid(customerCoupon)) {
                    continue;
                }


                List respList = ruleClient.selectById(customerCoupon.get("cid").toString()).getResp();
                if (CollectionUtils.isEmpty(respList)) {
                    ResponseData responseData = new ResponseData();
                    responseData.setSuccess(false);
                    responseData.setMsg("用户不存在优惠券");
                    continue;
                }
                Map coupon = (Map) respList.get(0);

                Map customerRede = customerRedemptionDao.queryByUserIdAndCId(customerCoupon.get("userId").toString(), customerCoupon.get("cid").toString());
                double maxRedemptionPerCustomer = Double.parseDouble(coupon.get("maxRedemptionPerCustomer").toString().trim());
                double number = Double.parseDouble(customerRede.get("number").toString().trim());
                customerCoupon.put("quantity", maxRedemptionPerCustomer - number);
                customerCoupon.put("startTime", customerCoupon.get("startDate"));
                customerCoupon.put("endTime", customerCoupon.get("endDate"));
                customerCoupon.put("description", coupon.get("couponDes"));
                customerCoupon.put("discountType", coupon.get("discountType"));
                customerCoupon.remove("couponId");
                if (customerCoupon.get("orderId") == null) {
                    customerCoupon.put("limitedOrder", "");
                }
                userCouponList.add(customerCoupon);
            }
        }
        sortCoupon(userCouponList);
        return new ResponseData(userCouponList);
    }

    /**
     * 根据状态查询用户的优惠券，供促销执行调用
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData queryUserIdAndStatusForPromote(Map map) {
        Long start = System.currentTimeMillis();
        if (StringUtils.isEmpty(map.get("customerId"))) {
            return ResponseReturnUtil.returnFalseResponse("userID字段不能为空");
        }
        List customerCouponList = customerCouponDao.queryUserIdAndStatus(map);
        Iterator iterator = customerCouponList.iterator();
        while (iterator.hasNext()) {
            Map customerCoupon = (Map) iterator.next();
            if (!checkCouponDate(customerCoupon)) {
                iterator.remove();
            }
        }
        logger.info("---customerCouponDao.queryUserIdAndStatusForPromote--" + (System.currentTimeMillis() - start));
        return new ResponseData(customerCouponList);
    }


    @Override
    public ResponseData queryUserIdAndStatus(String userId, String status) {
        return new ResponseData(customerCouponDao.queryUserIdAndStatus(userId, status));
    }

    /**
     * 根据用户登陆账号、优惠券状态、优惠券使用渠道查询优惠券
     *
     * @return
     */
    @Override
    public ResponseData queryUserIdAndStatusAndRange(String userId, String status, String range) {
        return new ResponseData(customerCouponDao.queryUserIdAndStatusAndRange(userId, status, range));
    }

    /**
     * 根据用户已领取的优惠券（customerCoupon）的主键查询已领取优惠券详细信息
     *
     * @param couponId
     * @return
     */
    @Override
    public ResponseData getByCouponId(String couponId) {
        Map customerCouponMap = customerCouponDao.select(couponId);
        return customerCouponMap == null ? new ResponseData(false, "NULL_POINTER") : new ResponseData(customerCouponMap);
    }


    @Override
    @RedisTransaction(clazz = "coupon:customerRedemption")
    public Map<String, Object> submitCustomerRedemption(Map<String, Object> map) {
        return customerRedemptionDao.submitCustomerRedemption(map);
    }

    @Override
    @RedisTransaction(clazz = "coupon:customerRedemption")
    public boolean redemptionCount(String id, String userId, Integer countNumber) {
        return customerRedemptionDao.redemptionCount(id, userId, countNumber);
    }

    @Override
    @RedisTransaction(clazz = "coupon:customerRedemption")
    public Map<String, ?> queryByUserIdAndCId(String userId, String couponId) {
        return customerRedemptionDao.queryByUserIdAndCId(userId, couponId);
    }

    @Override
    public void setInvalidByCid(String cid) {
        Map<String, Object> query = new HashMap<>();
        query.put("cid", cid);
        query.put("status", Status.STATUS_01.toString());
        List<Map<String, ?>> usableCouponList = customerCouponDao.selectByMutilEqField(query);
        //把cid对应的所有的“可使用”优惠券设置为“已失效”
        for (Map coupon : usableCouponList) {
            if (!Status.STATUS_02.name().equals(coupon.get("status"))) {
                coupon.put("status", Status.STATUS_03.toString());
                customerCouponDao.update(coupon);
            }
        }
//        return new ResponseData();
    }

    @Override
    public void startUsingCoupon(String cid) {
        List<Map<String, ?>> couponList = customerCouponDao.selectByEqField("cid", cid);
        for (Map map : couponList) {
            Long startDate = (Long) map.get("startDate");
            Long endDate = (Long) map.get("endDate");
            Long sysDate = System.currentTimeMillis();
            if (startDate <= sysDate && endDate > startDate) {
                if (!Status.STATUS_02.name().equals(map.get("status"))) {
                    map.put("status", Status.STATUS_01.toString());
                    customerCouponDao.update(map);
                }

            }
            if (sysDate <= startDate) {
                map.put("status", Status.STATUS_01.toString());
                customerCouponDao.update(map);
            }
        }
    }

    /**
     * 根据用户Id和coupon的Id查询 customercoupon
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData queryByCidAndUserIds(Map<String, Object> map) {
        Map<String, Object> data = (Map<String, Object>) map.get("data");
        int page = (int) map.get("page");
        int pageSize = (int) map.get("pageSize");
        if (!StringUtils.isEmpty(data.get("cid"))) {
            //用户名存在
            if (data.get("name") != null || data.get("mobileNumber") != null) {
                ResponseData responseData = userClient.queryByCondition(data);
                if (responseData.isSuccess()) {
                    if (responseData.getResp() != null) {
                        List<Map<String, Object>> userList = (List<Map<String, Object>>) responseData.getResp();
                        List<Object> userIds = new ArrayList<>();
                        for (Map<String, Object> user : userList) {
                            userIds.add(user.get("customerid"));
                        }
                        PagedValues pagedValues = customerCouponDao.queryByCidAndUserId(data.get("cid").toString(), userIds, page, pageSize);
                        List<Map<String, ?>> userCouponList = pagedValues.getValues();
                        List<Map<String, Object>> userReturnList = new ArrayList<>();
                        for (Map<String, ?> userCoupon : userCouponList) {
                            Map<String, Object> userResp = (Map<String, Object>) userClient.getUser(userCoupon.get("userId").toString());
                            JSONObject userRespObj = JSON.parseObject(JSON.toJSONString(userResp));
                            JSONArray userArray = userRespObj.getJSONArray("resp");
                            if (!userArray.isEmpty()) {
                                for (int i = 0; i < userArray.size(); i++) {
                                    JSONObject useObj = userArray.getJSONObject(i);
                                    Map user = new HashMap();
                                    user.put("cid", userCoupon.get("cid"));
                                    user.put("sendDate", userCoupon.get("sendDate"));
                                    user.put("status", userCoupon.get("status"));
                                    user.put("name", useObj.get("name"));
                                    user.put("mobileNumber", useObj.get("mobileNumber"));
                                    user.put("userLevel", useObj.get("userLevel"));
                                    userReturnList.add(user);
                                }
                            }
                        }

                        ResponseData responseData1 = new ResponseData();
                        responseData1.setTotal((int) pagedValues.getTotal());
                        responseData1.setResp(userReturnList);
                        return responseData1;
                    }
                }
            } else {
                //用户名不存在，根据cid查询出与其关联的user。进行查询
                PagedValues pagedValues = customerCouponDao.queryByCidAndUserId(data.get("cid").toString(), null, page, pageSize);

                List<Map<String, ?>> userCouponList = pagedValues.getValues();
                List<Map<String, Object>> userReturnList = new ArrayList<>();
                for (Map<String, ?> userCoupon : userCouponList) {
                    Map<String, Object> userResp = (Map<String, Object>) userClient.getUser(userCoupon.get("userId").toString());
                    JSONObject userRespObj = JSON.parseObject(JSON.toJSONString(userResp));
                    JSONArray userArray = userRespObj.getJSONArray("resp");
                    if (!userArray.isEmpty()) {
                        for (int i = 0; i < userArray.size(); i++) {
                            JSONObject useObj = userArray.getJSONObject(i);
                            Map user = new HashMap();
                            user.put("status", userCoupon.get("status"));
                            user.put("cid", userCoupon.get("cid"));
                            user.put("sendDate", userCoupon.get("sendDate"));
                            user.put("name", useObj.get("name"));
                            user.put("mobileNumber", useObj.get("mobileNumber"));
                            user.put("userLevel", useObj.get("userLevel"));
                            userReturnList.add(user);
                        }
                    }
                }

                ResponseData responseData1 = new ResponseData();
                responseData1.setTotal((int) pagedValues.getTotal());
                responseData1.setResp(userReturnList);
                return responseData1;
            }
        }
        return new ResponseData();
    }

    /**
     * 根据coupon的主键删除用户已领取优惠券
     *
     * @param cid
     * @return
     */
    @Override
    public ResponseData deleteByCid(String cid) {
        customerCouponDao.deleteByEq("cid", cid);
        return new ResponseData();
    }

    /**
     * 对优惠券按照discountType ，benefit字段进行排序，
     *
     * @param couponList
     */
    public void sortCoupon(List couponList) {
        couponList.sort(comparing(coupon -> {
            JSONObject couponJson = JSONObject.parseObject(JSON.toJSONString(coupon));
            String discountType = couponJson.getString("discountType");
            if (StringUtils.isEmpty(discountType)) {
                discountType = "other";
            }
            return discountType;
        }).reversed().thenComparing(coupon -> {
            JSONObject couponJson = JSONObject.parseObject(JSON.toJSONString(coupon));
            Double benefit = couponJson.getDouble("benefit");
            if (benefit == null) {
                benefit = 0.0d;
            }
            return benefit * -1;
        }));
    }

    /**
     * 校验优惠券是否在生效时间内
     *
     * @param customerCoupon
     * @return
     */
    public boolean checkCouponDate(Map customerCoupon) {
        //过滤有失效时长的优惠券
        Long startDate = GetMapValueUtil.getLong(customerCoupon, "startDate");
        Long endDate = GetMapValueUtil.getLong(customerCoupon, "endDate");
        Long currentMills = System.currentTimeMillis();
        if (startDate <= currentMills && currentMills <= endDate) {
            return true;
        }
        if (currentMills <= startDate) {
            return true;
        }
        return false;
    }

    /**
     * 校验状态为STATUS_01（活动中）的customerCoupon是否失效，失效则更新其状态为STATUS_03(过期)
     *
     * @param customerCoupon
     */
    public boolean checkAndInvalid(Map customerCoupon) {
        if (!checkCouponDate(customerCoupon)) {
            String status = (String) customerCoupon.get("status");
            if (Status.STATUS_01.name().equals(status)) {
                customerCoupon.put("status", Status.STATUS_03.name());
                customerCouponDao.update(customerCoupon);
                return true;
            }
        }
        return false;
    }
}
