package com.hand.hmall.service.impl;

import com.hand.hmall.client.IOrderPromoteClient;
import com.hand.hmall.client.IRuleClient;
import com.hand.hmall.dao.CouponRedeemCodeDao;
import com.hand.hmall.dao.CustomerCouponDao;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.menu.CouponType;
import com.hand.hmall.menu.RespMsgCode;
import com.hand.hmall.menu.Status;
import com.hand.hmall.service.ICouponService;
import com.hand.hmall.service.ICustomerCouponService;
import com.hand.hmall.service.IRedemptionService;
import com.hand.hmall.util.GenerateRedeemCodeUtil;
import com.hand.hmall.util.GetMapValueUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
public class CouponServiceImpl implements ICouponService {

    @Autowired
    private CustomerCouponDao customerCouponDao;
    @Autowired
    private IRuleClient ruleClient;
    @Autowired
    private ICustomerCouponService customerCouponService;
    @Autowired
    private IRedemptionService redemptionService;

    @Autowired
    private IOrderPromoteClient orderPromoteClient;
    @Autowired
    private CouponRedeemCodeDao couponRedeemCodeDao;
    @Autowired
    private GenerateRedeemCodeUtil generateRedeemCodeUtil;

    /**
     * 用户领取优惠券
     *
     * @return
     */
    @Override
    public ResponseData convertCoupon(Map convertMap) {
        //用户名
        String userId = (String) convertMap.get("customerId");
        //优惠券码
        String couponCode = (String) convertMap.get("couponCode");

        Map paymentInfo = (Map) convertMap.get("payment");
        Map<String, ?> coupon = new HashMap();
        //校验参数
        ResponseData checkResp = checkConvertCoupon(convertMap);
        if (!checkResp.isSuccess())
            return checkResp;
        else {
            coupon = (Map<String, ?>) checkResp.getResp().get(0);
        }


        //保存用户对优惠券的可兑换信息
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        //用户还能兑换优惠券的次数
        map.put("number", coupon.get("maxRedemptionPerCustomer"));
        String id = coupon.get("id").toString();
        map.put("cid", id);
        customerCouponService.submitCustomerRedemption(map);

        //保存用户优惠券关联关系
        Map<String, Object> customerCoupon = initCustomerCoupon(userId, coupon);

        //处理限时使用优惠券
        convertActiveTimeCoupon(coupon, customerCoupon);
        customerCouponDao.add(customerCoupon);
        //购买优惠券兑换
        if (CouponType.COUPON_TYPE_05.name().equals(coupon.get("type"))) {
            if (!CollectionUtils.isEmpty(paymentInfo)) {
                //优惠券支付信息关联用户优惠券id
                paymentInfo.put("couponId", customerCoupon.get("couponId"));
                ResponseData payment = orderPromoteClient.insertPayment(paymentInfo);
                if (!payment.isSuccess()) {
                    return payment;
                }
            }
        }

        //用户可兑换数量-1
        customerCouponService.redemptionCount(id, userId, 1);
        //优惠券可兑换数量减1
        if (StringUtils.isNotEmpty(couponCode)) {
            redemptionService.deleteRedemptionCount(id, "COUPON", 1);
        }
        //返回兑换信息
        customerCoupon.remove("couponId");
        customerCoupon.remove("range");
        customerCoupon.remove("type");
        customerCoupon.remove("startDate");
        customerCoupon.remove("endDate");
        customerCoupon.remove("isOverlay");
        customerCoupon.remove("cid");
        customerCoupon.remove("userId");
        customerCoupon.remove("benefit");
        customerCoupon.remove("status");
        customerCoupon.remove("version");
        customerCoupon.remove("couponCode");
        customerCoupon.remove("sendDate");
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);
        responseData.setResp(Arrays.asList(customerCoupon));
        responseData.setMsg("兑换成功");
        return responseData;
    }


    @Override
    public ResponseData convertCouponByAdmin(List<Map<String, Object>> convertData, String couponId) {

        Integer count = 0;
        ResponseData responseData = ruleClient.selectByCouponId(couponId);
        if (responseData.isSuccess()) {
            List<Map<String, Object>> couponList = (List<Map<String, Object>>) responseData.getResp();
            if (couponList != null) {
                if (!couponList.isEmpty()) {
                    Map<String, Object> coupon = couponList.get(0);
                    if (coupon.get("status").equals("ACTIVITY") || coupon.get("status").equals("DELAY")) {

                        for (Map<String, Object> map : convertData) {
                            String customerId = (String) map.get("mobileNumber");
                            if (map.get("count") != null && (int) map.get("count") > 0) {
                                //查询出每个用户还能兑换该优惠券的张数
                                Map<String, ?> customerRedemptionMap = customerCouponService.queryByUserIdAndCId(customerId, couponId);

                                if (customerRedemptionMap != null) {
                                    //当该优惠用户计数记录存在
                                    Integer numberPerCustomer = Integer.parseInt(customerRedemptionMap.get("number").toString());
                                    int userCount = (int) map.get("count");
                                    if (numberPerCustomer == 0 || numberPerCustomer < userCount || userCount < 0) {
                                        ResponseData resp = new ResponseData();
                                        resp.setSuccess(false);
                                        resp.setMsg("【" + customerId + "】该优惠券兑换过多");
                                        return resp;
                                    }
                                } else {
                                    if ((int) coupon.get("maxRedemptionPerCustomer") >= (int) map.get("count")) {
                                        Map<String, Object> saveMap = new HashMap<>();
                                        saveMap.put("userId", customerId);
                                        saveMap.put("number", coupon.get("maxRedemptionPerCustomer"));
                                        saveMap.put("cid", couponId);
                                        customerCouponService.submitCustomerRedemption(saveMap);
                                    } else {
                                        ResponseData resp = new ResponseData();
                                        resp.setSuccess(false);
                                        resp.setMsg("【" + customerId + "】该优惠券兑换过多");
                                        return resp;
                                    }
                                }
                                count += (int) map.get("count");
                            } else {
                                ResponseData resp = new ResponseData();
                                resp.setSuccess(false);
                                resp.setMsg(customerId + "优惠券数不能为空或者0");
                                return resp;
                            }
                        }
                        Map<String, ?> redemptionMap = redemptionService.selectByIdAndType(coupon.get("id").toString(), "COUPON");
                        String number = redemptionMap.get("number").toString();
                        if (Integer.parseInt(number) >= count) {
                            Long date = System.currentTimeMillis();
                            //合法范围
                            for (Map<String, Object> map : convertData) {
                                Map<String, Object> customerCoupon = new HashedMap();
                                for (int i = 0; i < (int) map.get("count"); i++) {
                                    customerCoupon.put("couponId", UUID.randomUUID().toString());
                                    customerCoupon.put("userId", map.get("mobileNumber"));
                                    customerCoupon.put("status", Status.STATUS_01);
                                    customerCoupon.put("couponCode", coupon.get("couponCode"));
                                    customerCoupon.put("couponName", coupon.get("couponName"));
                                    customerCoupon.put("range", coupon.get("range"));
                                    customerCoupon.put("type", coupon.get("type"));
                                    customerCoupon.put("startDate", coupon.get("startDate"));
                                    customerCoupon.put("endDate", coupon.get("endDate"));
                                    customerCoupon.put("benefit", coupon.get("benefit"));
                                    customerCoupon.put("isOverlay", coupon.get("isOverlay"));
                                    customerCoupon.put("sendDate", date);
                                    customerCoupon.put("cid", couponId);
                                    convertActiveTimeCoupon(coupon, customerCoupon);
                                    customerCouponDao.add(customerCoupon);
                                }
                                //用户兑换数量-1
                                customerCouponService.redemptionCount(couponId, map.get("mobileNumber").toString(), (int) map.get("count"));

                                //总兑换数量-1
                                redemptionService.deleteRedemptionCount(couponId, "COUPON", (int) map.get("count"));
                            }

                        } else {
                            ResponseData resp = new ResponseData();
                            resp.setSuccess(false);
                            resp.setMsg("当前优惠券大于最大发放值");
                            return resp;
                        }
                        return new ResponseData(convertData);
                    } else {
                        ResponseData respReturn = new ResponseData();
                        respReturn.setSuccess(false);
                        respReturn.setMsg("该规则处于不能分配状态");
                        return respReturn;
                    }
                } else {
                    ResponseData respReturn = new ResponseData();
                    respReturn.setSuccess(false);
                    respReturn.setMsg("优惠券获取失败");
                    return respReturn;
                }
            } else {
                ResponseData respReturn = new ResponseData();
                respReturn.setSuccess(false);
                respReturn.setMsg("优惠券获取失败");
                return respReturn;
            }
        }
        ResponseData respReturn = new ResponseData();
        respReturn.setSuccess(false);
        respReturn.setMsg("赠送优惠券失败");
        return respReturn;
    }

    /**
     * 优惠券占用释放操作
     *
     * @param couponId   优惠券id
     * @param orderId
     * @param operate
     * @param custometId
     * @return
     */
    @Override
    public ResponseData setUsed(String couponId, String orderId, String operate, String custometId) {
        Map coupon = customerCouponDao.select(couponId);
        try {
            if (!coupon.get("userId").equals(custometId)) {
                ResponseData responseData = new ResponseData();
                responseData.setSuccess(false);
                responseData.setMsg("优惠券不属于此用户");
                return responseData;
            }
            if ("1".equals(operate.trim())) {
                coupon.put("status", Status.STATUS_02);
                //关联优惠券所应用的orderId
                coupon.put("orderId", orderId);
                customerCouponDao.update(coupon);
                ResponseData responseData = new ResponseData();
                responseData.setSuccess(true);
                responseData.setMsg("优惠券占用完成");
                return responseData;
            } else if ("2".equals(operate.trim())) {
                if ("COUPON_TYPE_05".equals(coupon.get("type").toString().trim())) {
                    coupon.put("status", Status.STATUS_01);
                } else {
                    coupon.put("status", Status.STATUS_03);
                }
                coupon.put("orderId", "");
                customerCouponDao.update(coupon);
                ResponseData responseData = new ResponseData();
                responseData.setSuccess(true);
                responseData.setMsg("优惠券释放操作完成");
                return responseData;
            } else {
                ResponseData responseData = new ResponseData();
                responseData.setSuccess(false);
                responseData.setMsg("操作码不正确");
                return responseData;
            }

        } catch (Exception e) {
            ResponseData responseData = new ResponseData();
            responseData.setSuccess(false);
            responseData.setMsg("优惠券释放异常");
            return responseData;
        }

    }


    @Override
    public ResponseData getAll(String userId, String status) {
        Map query = new HashedMap();

        query.put("userId", userId);
        query.put("status", status);
        List couponList = customerCouponDao.selectByMutilEqField(query);
        return new ResponseData(couponList);
    }

    @Override
    public ResponseData select(String id) {
        List resp = new ArrayList();
        resp.add(customerCouponDao.select(id));
        return new ResponseData(resp);
    }

    @Override
    public ResponseData delete(String couponId) {
        customerCouponDao.delete(couponId);
        return new ResponseData();
    }

    /**
     * 查询该用户下的优惠券
     * return ResponseData
     */
    @Override
    public ResponseData queryByUserId(String userId, int page, int pagesize) {
        List<Map<String, ?>> pomote = customerCouponDao.selectByPageField("userId", userId, (page - 1) * pagesize, pagesize, true);
        long total = customerCouponDao.countPagesByPagingField("userId", userId);
        if (pomote == null || pomote.size() == 0) {
            return new ResponseData(Collections.emptyList());
        }

        ResponseData responseData = new ResponseData();
        responseData.setResp(pomote);
        responseData.setTotal(Integer.parseInt(String.valueOf(total)));
        return responseData;
    }

    @Override
    public List<Map<String, ?>> queryUserIdAndStatus(String userId, String status) {
        return customerCouponDao.queryUserIdAndStatus(userId, status);
    }


    @Override
    public ResponseData selectCouponById(String couponId, String userId) {
        ResponseData responseData = new ResponseData();
        Map<String, ?> customerCoupon = customerCouponDao.select(couponId);
        if (customerCoupon == null || customerCoupon.isEmpty()) {
            responseData.setSuccess(false);
            responseData.setMsg("未获取到优惠券和用户信息");
        } else {
            if (customerCoupon.get("userId").equals(userId)) {
                String status = (String) customerCoupon.get("status");    //
                if (!"STATUS_01".equals(status)) {      //如果为status_01    该优惠券可用
                    responseData.setSuccess(false);
                    responseData.setMsg("优惠券不可用");
                }
            } else {
                responseData.setSuccess(false);
                responseData.setMsg("优惠券不属于该用户");
            }
        }
        responseData.setResp(Arrays.asList(customerCoupon));
        return responseData;
    }

    /**
     * 根据优惠券码生成优惠券兑换码
     *
     * @param couponCode 优惠券编码
     * @param redeemNum  兑换量
     * @return
     */
    @Override
    public ResponseData generateRedeemCode(String couponCode, int redeemNum) {
        //获取优惠券信息
        ResponseData couponResp = ruleClient.queryByCodeCanUse(couponCode);
        if (couponResp.isSuccess()) {
            Map coupon = couponResp.getResp().size() > 0 ? (Map) couponResp.getResp().get(0) : null;
            //校验获取到的优惠券是否为空，以及优惠券类型是否为兑换优惠券
            if (coupon == null || !CouponType.COUPON_TYPE_01.name().equals(coupon.get("type").toString().trim())) {
                ResponseData errResp = new ResponseData(false);
                errResp.setMsg("【只有“优惠券兑换”类优惠券可生成兑换码】");
                return errResp;
            }
            Map<String, ?> redemptionMap = redemptionService.selectByIdAndType(coupon.get("id").toString(), "COUPON");
            String number = redemptionMap.get("number").toString();
            //校验生成的兑换码是否超出优惠券最大限度
            if (Integer.parseInt(number) < redeemNum) {
                ResponseData errResp = new ResponseData(false);
                errResp.setMsg("优惠码生成量超出优惠券最大生成量" + number);
                return errResp;
            } else {
                List redeemCodeList = new LinkedList();
                while (redeemNum > 0) {
                    Map redeemCode = new HashMap();
                    String code = null;
                    try {
                        code = generateRedeemCodeUtil.getRedeemCode();
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                        continue;
                    }
                    Map<String, ?> isExist = couponRedeemCodeDao.select(code);
                    if (!CollectionUtils.isEmpty(isExist)) {
                        continue;
                    }
                    redeemCode.put("code", code);
                    redeemCode.put("couponCode", coupon.get("couponCode"));
                    redeemCode.put("used", "N");
                    couponRedeemCodeDao.add(redeemCode);
                    redeemCodeList.add(code);
                    redemptionService.deleteRedemptionCount(coupon.get("id").toString(), "COUPON", 1);
                    redeemNum--;
                }
                ResponseData responseData = new ResponseData(redeemCodeList);
                responseData.setTotal(redeemCodeList.size());
                return responseData;
            }
        } else {
            return couponResp;
        }
    }

    /**
     * 获取优惠券下可用兑换码
     *
     * @param couponCode
     * @return
     */
    @Override
    public ResponseData getRedeemCodes(String couponCode) {
        if (StringUtils.isEmpty(couponCode)) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMsg("优惠券码不能为空");
            return responseData;
        }
        List<Map<String, ?>> redeemCodes = couponRedeemCodeDao.queryUsefulRedeemCode(couponCode);
        return new ResponseData(redeemCodes);
    }


    /**
     * 优惠券兑换，数据校验
     *
     * @param convertMap
     * @return
     */
    protected ResponseData checkConvertCoupon(Map convertMap) {

        //获取兑换参数

        //用户名
        String userId = (String) convertMap.get("customerId");
        //优惠券码
        String couponCode = (String) convertMap.get("couponCode");
        //优惠券兑换码
        String couponValue = (String) convertMap.get("couponValue");
        Map paymentInfo = (Map) convertMap.get("payment");

        //区分是优惠券兑换还是兑换码兑换
        if (StringUtils.isNotEmpty(couponValue)) {
            //获取兑换码信息
            Map redeemMap = couponRedeemCodeDao.select(couponValue);
            //校验兑换码
            if (CollectionUtils.isEmpty(redeemMap)) {
                ResponseData responseData = new ResponseData(false);
                responseData.setMsg("兑换码不存在");
                return responseData;
            } else if ("Y".equals(redeemMap.get("used"))) {
                ResponseData responseData = new ResponseData(false);
                responseData.setMsg("兑换码已使用");
                return responseData;
            } else {
                couponCode = (String) redeemMap.get("couponCode");
                //兑换码信息更新为已使用
                redeemMap.put("used", "Y");
                couponRedeemCodeDao.update(redeemMap);
            }
        } else if (StringUtils.isEmpty(couponCode)) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMsg("输入信息不完整，请校验");
            return responseData;
        }

        //查找到当前优惠券所对应的规则逻辑
        String id = "";
        ResponseData resp = ruleClient.queryByCodeCanUse(couponCode);
        Map coupon = new HashMap();
        if (resp.isSuccess()) {
            if (!CollectionUtils.isEmpty(resp.getResp())) {
                coupon = (Map) resp.getResp().get(0);
                //获得优惠券规则定义部分的id值
                id = coupon.get("id").toString();
            } else {
                ResponseData responseData = new ResponseData();
                responseData.setSuccess(false);
                responseData.setMsg("【优惠券已过期】");
                return responseData;
            }
        } else {
            return resp;
        }
        if (CollectionUtils.isEmpty(paymentInfo) && CouponType.COUPON_TYPE_05.name().equals(coupon.get("type"))) {
            ResponseData responseData = new ResponseData();
            responseData.setSuccess(false);
            responseData.setMsg("获取不到购券支付信息");
            return responseData;
        }
        //获得该优惠券用户最大的兑换数量
        Map<String, ?> customerRedemptionMap = customerCouponService.queryByUserIdAndCId(userId, id);

        //获得总最大兑换数量
        Map<String, ?> redemptionMap = redemptionService.selectByIdAndType(id, "COUPON");

        //管理员发放优惠券
        if (coupon.get("type").equals(CouponType.COUPON_TYPE_04.name())) {
            //兑换码有误
            ResponseData responseData = new ResponseData();
            responseData.setSuccess(false);
            responseData.setMsg("该优惠券只能由管理员发放");
            return responseData;
        }
        //用户兑换优惠券
        if (coupon.get("type").equals(CouponType.COUPON_TYPE_01.name()) && StringUtils.isEmpty(couponValue)) {
            //兑换码有误
            ResponseData responseData = new ResponseData();
            responseData.setSuccess(false);
            responseData.setMsg("该优惠券只能由兑换码兑换");
            return responseData;
        }

        if (redemptionMap != null) {
            //优惠券被抢光了
            if (Integer.parseInt(redemptionMap.get("number").toString()) == 0 && !coupon.get("type").equals(CouponType.COUPON_TYPE_01.name())) {
                ResponseData responseData = new ResponseData();
                responseData.setSuccess(false);
                responseData.setMsg(RespMsgCode.PROMOTE_COUPON_002.getValue());
                return responseData;
            }
        }
        if (customerRedemptionMap != null) {
            //兑换次数已达到上限
            if (Integer.parseInt(customerRedemptionMap.get("number").toString()) == 0) {
                ResponseData responseData = new ResponseData();
                responseData.setSuccess(false);
                responseData.setMsg(RespMsgCode.PROMOTE_COUPON_003.getValue());
                return responseData;
            }
        }

        Long currentTime = System.currentTimeMillis();
        Long getStartDate = (Long) coupon.get("getStartDate");
        Long getEndDate = (Long) coupon.get("getEndDate");
        if (getStartDate > currentTime) {
            ResponseData responseData = new ResponseData();
            responseData.setSuccess(false);
            responseData.setMsg("该优惠券未到领取时间");
            return responseData;
        } else if (currentTime > getEndDate) {
            ResponseData responseData = new ResponseData();
            responseData.setSuccess(false);
            responseData.setMsg("该优惠券已过领取时间");
            return responseData;
        }
        return new ResponseData(Arrays
                .asList(coupon));
    }

    public Map initCustomerCoupon(String userId, Map coupon) {
        Map<String, Object> customerCoupon = new HashedMap();
        customerCoupon.put("couponId", UUID.randomUUID().toString());
        customerCoupon.put("userId", userId);
        customerCoupon.put("status", Status.STATUS_01);
        customerCoupon.put("couponCode", coupon.get("couponCode"));
        customerCoupon.put("couponName", coupon.get("couponName"));
        customerCoupon.put("range", coupon.get("range"));
        customerCoupon.put("type", coupon.get("type"));
        customerCoupon.put("startDate", coupon.get("startDate"));
        customerCoupon.put("endDate", coupon.get("endDate"));
        customerCoupon.put("benefit", coupon.get("benefit"));
        customerCoupon.put("isOverlay", coupon.get("isOverlay"));
        customerCoupon.put("sendDate", System.currentTimeMillis());
        customerCoupon.put("cid", coupon.get("id"));
        return customerCoupon;
    }

    /**
     * 兑换限时优惠券
     *
     * @param coupon         优惠券
     * @param customerCoupon 用户领取的优惠券
     */
    public void convertActiveTimeCoupon(Map coupon, Map customerCoupon) {
        Double activeTime = GetMapValueUtil.getDouble(coupon, "activeTime");
        if (activeTime != null && activeTime > 0) {
            long startDate = GetMapValueUtil.getLong(coupon, "startDate");
            long endDate = GetMapValueUtil.getLong(coupon, "endDate");
            Long sysDate = System.currentTimeMillis();
            long activeMills = Math.round(activeTime * 86400000);
            //优惠券（coupon）已生效，customerCoupon（用户领取的优惠券）生效时间为领取时间。否则为coupon生效时间
            if (startDate < sysDate) {
                startDate = sysDate;
            }
            //customerCoupon（用户领取的优惠券）过期时间不能晚于coupon过期时间
            if (startDate + activeMills <= endDate) {
                endDate = startDate + activeMills;
            }
            customerCoupon.put("activeTime", activeMills);
            customerCoupon.put("startDate", startDate);
            customerCoupon.put("endDate", endDate);
        }
    }
}
