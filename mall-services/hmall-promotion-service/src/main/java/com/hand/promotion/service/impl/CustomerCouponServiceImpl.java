package com.hand.promotion.service.impl;

import com.hand.dto.ConvertCouponParm;
import com.hand.dto.OperateCouponParm;
import com.hand.dto.ResponseData;
import com.hand.promotion.dao.CustomerCouponDao;
import com.hand.promotion.pojo.SimpleMessagePojo;
import com.hand.promotion.pojo.coupon.AdminConvertParm;
import com.hand.promotion.pojo.coupon.CouponRedeemCodePojo;
import com.hand.promotion.pojo.coupon.CouponRedemptionPojo;
import com.hand.promotion.pojo.coupon.CouponsPojo;
import com.hand.promotion.pojo.coupon.CustomerCouponPojo;
import com.hand.promotion.pojo.coupon.CustomerRedeptionPojo;
import com.hand.promotion.pojo.coupon.PromotionCouponsPojo;
import com.hand.promotion.pojo.enums.CouponStatus;
import com.hand.promotion.pojo.enums.CouponType;
import com.hand.promotion.pojo.enums.MsgMenu;
import com.hand.promotion.pojo.enums.OperatorCouponMenu;
import com.hand.promotion.pojo.enums.PromotionConstants;
import com.hand.promotion.pojo.enums.Status;
import com.hand.promotion.service.ICouponRedeemCodeService;
import com.hand.promotion.service.ICouponRedeptionService;
import com.hand.promotion.service.ICouponService;
import com.hand.promotion.service.ICustomerCouponService;
import com.hand.promotion.service.ICustomerRedeptionService;
import com.hand.promotion.util.BeanValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2018/1/11
 * @description 用户已发放优惠券Service
 */
@Service
public class CustomerCouponServiceImpl implements ICustomerCouponService {

    @Autowired
    private ICouponService saleCouponService;
    @Autowired
    private ICouponRedeptionService couponRedeptionService;
    @Autowired
    private ICustomerRedeptionService customerRedeptionService;
    @Autowired
    private CustomerCouponDao customerCouponDao;
    @Autowired
    private ICouponRedeemCodeService couponRedeemCodeService;

    /**
     * 兑换码发券张数
     */
    private final int REDEEM_COUNT = 1;


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 用户领取优惠券,购买
     *
     * @return
     */
    @Override
    public SimpleMessagePojo convertCoupon(ConvertCouponParm convertCouponParm) {
        //检验兑换参数
        SimpleMessagePojo checkResult = checkConvertParm(convertCouponParm);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }
        //根据优惠券编码couponCode领取优惠券
        if (!StringUtils.isEmpty(convertCouponParm.getCouponCode())) {
            return convertByCode(convertCouponParm.getCouponCode(), convertCouponParm.getCustomerId(), convertCouponParm.getCount());
        } else {
            //根据优惠券兑换码兑换优惠券
            return convertByRedeem(convertCouponParm.getCouponValue(), convertCouponParm.getCustomerId());
        }
    }

    /**
     * 管理员发放优惠券
     *
     * @param adminConvertParm
     * @return
     */
    @Override
    public SimpleMessagePojo convertCouponByAdmin(AdminConvertParm adminConvertParm) {
        Long currentDate = System.currentTimeMillis();
        PromotionCouponsPojo promotionCouponsPojo = saleCouponService.selectCouponDetail(adminConvertParm.getCouponId());
        //校验优惠券信息
        SimpleMessagePojo checkResult = checkCouponData(promotionCouponsPojo, currentDate, CouponType.COUPON_TYPE_04);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }
        List<Map<String, Object>> convertData = adminConvertParm.getConvertData();
        List<Map> errList = new ArrayList<>(convertData.size());
        StringBuffer errMsg = new StringBuffer();
        for (int i = 0; i < convertData.size(); i++) {
            Map perConvert = convertData.get(i);
            //校验客户最大兑换量
            CouponsPojo coupon = (CouponsPojo) checkResult.getObj();
            checkResult = checkCustomerRede(coupon, (String) perConvert.get("userId"), (int) perConvert.get("count"));
            if (!checkResult.isSuccess()) {
                errMsg.append("<P>").append(perConvert.get("userId") + checkResult.getCheckMsg()).append("</p>");
                errList.add(perConvert);
                continue;
            }

            CustomerRedeptionPojo customerRedeptionPojo = (CustomerRedeptionPojo) checkResult.getObj();


            //判断优惠券还能兑换的数量
            checkResult = checkCouponReede(coupon, (int) perConvert.get("count"));
            if (!checkResult.isSuccess()) {
                errMsg.append("<P>").append(perConvert.get("userId") + checkResult.getCheckMsg()).append("</p>");
                errList.add(perConvert);
                break;
            }
            CouponRedemptionPojo couponRedemptionPojo = (CouponRedemptionPojo) checkResult.getObj();

            //发券操作
            List<CustomerCouponPojo> customerCouponPojos = generateCustomerCoupons(coupon, (String) perConvert.get("userId"), (int) perConvert.get("count"));
            //插入已兑换优惠券信息到MongoDB,客户最大兑换数,优惠券最大兑换数记录减去count
            batchInsert(customerCouponPojos);
            customerRedeptionService.subCount(customerRedeptionPojo, (int) perConvert.get("count"));
            couponRedeptionService.subCount(couponRedemptionPojo, (int) perConvert.get("count"));

        }
        if (errList.size() == 0) {
            return new SimpleMessagePojo(true, MsgMenu.SUCCESS, null).setCheckMsg("兑换成功");
        } else {
            return new SimpleMessagePojo(false, MsgMenu.CONVER_ERR, errList).setCheckMsg(errList.toString());
        }
    }

    @Override
    public ResponseData queryByUserId(String userId, int page, int pagesize) {
        return customerCouponDao.findByCondition(userId, null, page, pagesize);
    }

    /**
     * 根据发券账户和优惠券状态查询已发放优惠券
     *
     * @param userId 发券账户
     * @param status 优惠券状态
     * @return
     */
    @Override
    public List<CustomerCouponPojo> queryByUserIdAndStatus(String userId, String status)  {
        CustomerCouponPojo condition = new CustomerCouponPojo();
        condition.setStatus(status);
        condition.setUserId(userId);
        List<CustomerCouponPojo> customerCouponPojos = customerCouponDao.findByPojo(condition);
        return customerCouponPojos;
    }


    /**
     * 根据发券账户和关联的优惠券主键查询已发放优惠券
     *
     * @param userId 发券账户
     * @param id     已发放优惠券主键
     * @return
     */
    @Override
    public List<CustomerCouponPojo> queryByUserIdAndId(String userId, String id)  {
        CustomerCouponPojo condition = new CustomerCouponPojo();
        condition.setId(id);
        condition.setUserId(userId);
        List<CustomerCouponPojo> customerCouponPojos = customerCouponDao.findByPojo(condition);
        return customerCouponPojos;
    }

    /**
     * 执行优惠券时查询用户使用的的优惠券
     * 查询标准: status 为STATUS_01(可使用) startDate(生效时间)<currentTime(当前时间)<endDate(失效时间)
     *
     * @param userId 发券账户
     * @return 可用优惠券
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @Override
    public List<CustomerCouponPojo> queryUsefulCusCoupon(String userId) {
        return customerCouponDao.findByCondition(userId, System.currentTimeMillis(), CouponStatus.STATUS_01.getValue());
    }

    @Override
    public ResponseData queryByCidAndUserIds(String mobileNum, String cid, String name, int page, int pagesize) {
        ResponseData byCondition = customerCouponDao.findByCondition(mobileNum, cid, page, pagesize);
        List<CustomerCouponPojo> resp = (List<CustomerCouponPojo>) byCondition.getResp();
        List<Map> collect = resp.stream().map(customerCouponPojo -> {
            Map map = new HashMap(20);
            map.put("mobileNumber", customerCouponPojo.getUserId());
            map.put("sendDate", customerCouponPojo.getSendDate());
            map.put("status", customerCouponPojo.getStatus());
            return map;
        }).collect(Collectors.toList());
        byCondition.setResp(collect);
        return byCondition;
    }

    /**
     * 根据优惠券状态更新已发放优惠券状态
     *
     * @param couponsPojo 要更新的已发放优惠券关联的优惠券
     */
    @Override
    public void updateCustomerCouponStatus(CouponsPojo couponsPojo) {
        String status = couponsPojo.getStatus();
        //优惠券已失效或已停用,更新已发放优惠券为已失效
        if (Status.FAILURE.getValue().equalsIgnoreCase(status) || Status.INACTIVE.getValue().equalsIgnoreCase(status)) {
            customerCouponDao.updateUnusedStatus(couponsPojo.getId(), CouponStatus.STATUS_03);
        } else if (Status.ACTIVITY.getValue().equalsIgnoreCase(status)) {
            //更新优惠券状态为活动中时,校验优惠券是否有使用时效
            if (null != couponsPojo.getActiveTime()) {
                //查询出所有已发放优惠券
                CustomerCouponPojo condition = new CustomerCouponPojo();
                condition.setCid(couponsPojo.getId());
                List<CustomerCouponPojo> customerCouponPojos = customerCouponDao.findByPojo(condition);
                Long currentTime = System.currentTimeMillis();
                customerCouponPojos.forEach(customerCouponPojo -> {
                    if (CouponStatus.STATUS_02.getValue().equals(customerCouponPojo.getStatus())) {
                        return;
                    }
                    Long startDate = customerCouponPojo.getStartDate();
                    Long endDate = customerCouponPojo.getEndDate();
                    if (endDate < currentTime) {
                        customerCouponDao.updateStatus(customerCouponPojo.getId(), CouponStatus.STATUS_03);
                    } else {
                        customerCouponDao.updateStatus(customerCouponPojo.getId(), CouponStatus.STATUS_01);

                    }
                });
            } else {
                customerCouponDao.updateUnusedStatus(couponsPojo.getId(), CouponStatus.STATUS_01);
            }

        }
    }

    /**
     * 更新具有时效性的已发放优惠券状态
     * Status_01(可使用)-->Status_03(已失效)
     *
     * @param cid 要更新的已发放优惠券关联的优惠券信息
     */
    @Override
    public void updateActiveCouponStatus(String cid){
        //查询出所有已发放优惠券
        CustomerCouponPojo condition = new CustomerCouponPojo();
        condition.setCid(cid);
        //可使用状态
        condition.setStatus(CouponStatus.STATUS_01.getValue());
        List<CustomerCouponPojo> customerCouponPojos = customerCouponDao.findByPojo(condition);
        Long currentTime = System.currentTimeMillis();
        customerCouponPojos.forEach(customerCouponPojo -> {
            Long startDate = customerCouponPojo.getStartDate();
            Long endDate = customerCouponPojo.getEndDate();
            if (endDate < currentTime) {
                customerCouponDao.updateStatus(customerCouponPojo.getId(), CouponStatus.STATUS_03);
            }
        });
    }

    /**
     * 占用释放优惠券
     *
     * @param operateCouponParm 占用释放优惠券相关参数
     * @return
     */
    @Override
    public SimpleMessagePojo setUsed(OperateCouponParm operateCouponParm) {
        SimpleMessagePojo validate = BeanValidationUtil.validate(operateCouponParm);
        if (!validate.isSuccess()) {
            return validate;
        }
        OperatorCouponMenu operate = OperatorCouponMenu.getByValue(operateCouponParm.getOperation());
        if (null == OperatorCouponMenu.getByValue(operateCouponParm.getOperation())) {
            return new SimpleMessagePojo(false, MsgMenu.OPERATE_COUPON_ERR, null).setCheckMsg("操作码不合法");
        }
        //获取要操作的优惠券详细信息
        CustomerCouponPojo customerCoupon = customerCouponDao.findByPK(operateCouponParm.getCouponId(), CustomerCouponPojo.class);
        if (null == customerCoupon) {
            return new SimpleMessagePojo(false, MsgMenu.OPERATE_COUPON_ERR, null).setCheckMsg("要操作的优惠券数据不存在");
        }
        if (!operateCouponParm.getCustomerId().equals(customerCoupon.getUserId())) {
            return new SimpleMessagePojo(false, MsgMenu.OPERATE_COUPON_ERR, null).setCheckMsg("优惠券不属于该用户");

        }
        SimpleMessagePojo resultMsg;
        switch (operate) {
            case USED:
                resultMsg = useCoupon(customerCoupon);
                break;
            case UN_USED:
                resultMsg = unUseCoupon(customerCoupon);
                break;
            default:
                return new SimpleMessagePojo(false, MsgMenu.SUCCESS, null);
        }

        return resultMsg;
    }

    /**
     * 生成优惠券兑换码
     *
     * @param code 要生成兑换码的优惠券编码
     * @param num  生成的兑换码数量
     * @return
     */
    @Override
    public SimpleMessagePojo generateRedeemCode(String code, int num)  {
        //校验兑换信息
        if (StringUtils.isEmpty(code) || num <= 0) {
            return new SimpleMessagePojo(false, MsgMenu.GENERATE_REDEEM_CODE_ERR, null).setCheckMsg("入参不合法");
        }
        //获取要生成兑换码的优惠券信息
        PromotionCouponsPojo promotionCouponsPojo = saleCouponService.queryByCouponCode(code, PromotionConstants.Y);
        //校验优惠券是否可生成兑换码
        if (promotionCouponsPojo == null) {
            return new SimpleMessagePojo(false, MsgMenu.COUPON_IS_NULL, null).setCheckMsg("要兑换的优惠券信息不存在");
        }
        CouponsPojo coupon = promotionCouponsPojo.getCoupon();
        SimpleMessagePojo checkResult = checkCouponData(promotionCouponsPojo, System.currentTimeMillis(), CouponType.COUPON_TYPE_01);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }

        checkResult = checkCouponReede(promotionCouponsPojo.getCoupon(), num);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }
        CouponRedemptionPojo couponRedemption = (CouponRedemptionPojo) checkResult.getObj();
        //生成兑换码数据
        List<CouponRedeemCodePojo> redeemCodes = new LinkedList<CouponRedeemCodePojo>();
        //减去兑换量
        couponRedeptionService.subCount(couponRedemption, num);
        for (int i = 0; i < num; i++) {
            CouponRedeemCodePojo redeemCode = new CouponRedeemCodePojo();
            redeemCode.setCouponCode(coupon.getCouponCode());
            redeemCode.setUsed(PromotionConstants.N);
            //todo ::兑换码生成
            redeemCode.setCode(UUID.randomUUID().toString());
            redeemCodes.add(redeemCode);
        }
        //插入生成的兑换码到数据库
        couponRedeemCodeService.batchInsert(redeemCodes);

        return new SimpleMessagePojo(true, MsgMenu.SUCCESS, redeemCodes);
    }

    /**
     * 根据优惠券编码查询已生成的兑换码
     *
     * @param couponCode 优惠券编码
     * @return
     */
    @Override
    public SimpleMessagePojo getRedeemCodes(String couponCode) {
        List<CouponRedeemCodePojo> redeemCodePojos = couponRedeemCodeService.quertyByCouponCode(couponCode);
        return new SimpleMessagePojo(true,MsgMenu.SUCCESS,redeemCodePojos);
    }

    /**
     * 占用优惠券,更新优惠券状态为已停用
     *
     * @param customerCouponPojo
     * @return
     */
    public SimpleMessagePojo useCoupon(CustomerCouponPojo customerCouponPojo) {
        if(!CouponStatus.STATUS_01.getValue().equals(customerCouponPojo.getStatus())){
            return new SimpleMessagePojo(false, MsgMenu.OPERATE_COUPON_ERR, null).setCheckMsg("只有可使用的优惠券才能占用");
        }
        customerCouponDao.updateStatus(customerCouponPojo.getId(), CouponStatus.STATUS_02);
        return new SimpleMessagePojo(true, MsgMenu.SUCCESS, null).setCheckMsg("优惠券占用成功");
    }

    /**
     * 释放优惠券,根据优惠券时间重新更新优惠券状态
     *
     * @param customerCouponPojo
     * @return
     */
    public SimpleMessagePojo unUseCoupon(CustomerCouponPojo customerCouponPojo) {
        if(!CouponStatus.STATUS_02.getValue().equals(customerCouponPojo.getStatus())){
            return new SimpleMessagePojo(true, MsgMenu.SUCCESS, null).setCheckMsg("优惠券已经是非占用状态");
        }
        if(!CouponType.COUPON_TYPE_05.getValue().equals(customerCouponPojo.getType())){
            new SimpleMessagePojo(true, MsgMenu.SUCCESS, null).setCheckMsg("非购买优惠券释放后状态不变");
        }
        Long startDate = customerCouponPojo.getStartDate();
        Long endDate = customerCouponPojo.getEndDate();
        Long currentTime = System.currentTimeMillis();
        //购买优惠券进行释放

        if (startDate < currentTime && currentTime < endDate) {
            customerCouponDao.updateStatus(customerCouponPojo.getId(), CouponStatus.STATUS_01);
        } else if (currentTime < startDate) {
            customerCouponDao.updateStatus(customerCouponPojo.getId(), CouponStatus.STATUS_01);
        } else {
            customerCouponDao.updateStatus(customerCouponPojo.getId(), CouponStatus.STATUS_03);
        }
        return new SimpleMessagePojo(true, MsgMenu.SUCCESS, null).setCheckMsg("优惠券释放成功");
    }


    @Override
    public void insert(CustomerCouponPojo customerCouponPojo) {
        customerCouponDao.insertPojo(customerCouponPojo);
    }

    @Override
    public void batchInsert(List<CustomerCouponPojo> customerCouponPojos) {
        customerCouponDao.insertPojos(customerCouponPojos);

    }

    /**
     * 用优惠券编码领取优惠券
     *
     * @param couponCode 优惠券编码
     * @param userId     领取账号
     * @return
     */
    public SimpleMessagePojo convertByCode(String couponCode, String userId, Integer count){
        Long currentDate = System.currentTimeMillis();
        //校验要兑换的优惠券信息
        PromotionCouponsPojo promotionCouponsPojo = saleCouponService.queryByCouponCode(couponCode, PromotionConstants.Y);
        SimpleMessagePojo checkResult = checkCouponData(promotionCouponsPojo, currentDate, CouponType.COUPON_TYPE_02, CouponType.COUPON_TYPE_05);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }
        //校验客户最大兑换量
        CouponsPojo coupon = (CouponsPojo) checkResult.getObj();
        checkResult = checkCustomerRede(coupon, userId, count);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }
        CustomerRedeptionPojo customerRedeptionPojo = (CustomerRedeptionPojo) checkResult.getObj();

        //判断优惠券还能兑换的数量
        checkResult = checkCouponReede(coupon, count);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }
        CouponRedemptionPojo couponRedemptionPojo = (CouponRedemptionPojo) checkResult.getObj();

        //发券操作
        List<CustomerCouponPojo> customerCouponPojos = generateCustomerCoupons(coupon, userId, count);
        //插入已兑换优惠券信息到MongoDB,客户最大兑换数,优惠券最大兑换数记录减去count
        batchInsert(customerCouponPojos);
        customerRedeptionService.subCount(customerRedeptionPojo, count);
        couponRedeptionService.subCount(couponRedemptionPojo, count);
        return new SimpleMessagePojo(true, MsgMenu.SUCCESS, null).setCheckMsg("优惠券领取成功");
    }

    /**
     * 用优惠券兑换码兑换优惠券
     *
     * @param redeemCode 兑换码
     * @param userId     领券账号
     * @return
     */
    public SimpleMessagePojo convertByRedeem(String redeemCode, String userId){
        Long currentDate = System.currentTimeMillis();
        CouponRedeemCodePojo couponRedeemCodePojo = couponRedeemCodeService.queryByCode(redeemCode);
        if (couponRedeemCodePojo == null) {
            return new SimpleMessagePojo(false, MsgMenu.REDEEM_COUPON_ERR, null).setCheckMsg("兑换码不存在");
        }
        if (PromotionConstants.Y.equalsIgnoreCase(couponRedeemCodePojo.getUsed())) {
            return new SimpleMessagePojo(false, MsgMenu.REDEEM_COUPON_ERR, null).setCheckMsg("兑换码已使用");
        }
        String couponCode = couponRedeemCodePojo.getCouponCode();
        //校验要兑换的优惠券信息
        PromotionCouponsPojo promotionCouponsPojo = saleCouponService.queryByCouponCode(couponCode, PromotionConstants.Y);

        SimpleMessagePojo checkResult = checkCouponData(promotionCouponsPojo, currentDate, CouponType.COUPON_TYPE_01);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }
        //校验客户最大兑换量
        CouponsPojo coupon = (CouponsPojo) checkResult.getObj();
        //兑换码的兑换量只能是1
        checkResult = checkCustomerRede(coupon, userId, REDEEM_COUNT);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }
        CustomerRedeptionPojo customerRedeptionPojo = (CustomerRedeptionPojo) checkResult.getObj();

        //判断优惠券还能兑换的数量
        checkResult = checkCouponReede(coupon, REDEEM_COUNT);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }
        CouponRedemptionPojo couponRedemptionPojo = (CouponRedemptionPojo) checkResult.getObj();

        //发券操作
        List<CustomerCouponPojo> customerCouponPojos = generateCustomerCoupons(coupon, userId, REDEEM_COUNT);
        //插入已兑换优惠券信息到MongoDB,客户最大兑换数,优惠券最大兑换数记录减去count
        batchInsert(customerCouponPojos);
        customerRedeptionService.subCount(customerRedeptionPojo, REDEEM_COUNT);
        couponRedeemCodeService.occupyRedeemCode(couponRedeemCodePojo.getId());
        return new SimpleMessagePojo();
    }

    /**
     * 生成要保存的优惠券数据
     *
     * @param coupon 要领取的优惠券
     * @param userId 领券账号
     **/
    public CustomerCouponPojo generateCustomerCoupon(CouponsPojo coupon, String userId) {
        CustomerCouponPojo customerCouponPojo = new CustomerCouponPojo();
        customerCouponPojo.setCid(coupon.getId());
        customerCouponPojo.setCouponCode(coupon.getCouponCode());
        customerCouponPojo.setSendDate(System.currentTimeMillis());
        String status = null;
        if (Status.ACTIVITY.getValue().equalsIgnoreCase(coupon.getStatus()) || Status.DELAY.getValue().equalsIgnoreCase(coupon.getStatus())) {
            status = CouponStatus.STATUS_01.getValue();
        } else {
            status = CouponStatus.STATUS_03.getValue();
        }
        customerCouponPojo.setStatus(status);
        customerCouponPojo.setUserId(userId);
        customerCouponPojo.setStartDate(coupon.getStartDate());
        customerCouponPojo.setEndDate(coupon.getEndDate());
        customerCouponPojo.setBenefit(coupon.getBenefit());
        customerCouponPojo.setId(UUID.randomUUID().toString());
        customerCouponPojo.setRange(coupon.getRange());
        customerCouponPojo.setIsOverlay(coupon.getIsOverlay());
        customerCouponPojo.setType(coupon.getType());

        //校验限时券,设置限时券的生效时间,初始状态
        Double activeTime = coupon.getActiveTime();
        if (activeTime != null && activeTime > 0) {
            long startDate = coupon.getStartDate();
            long endDate = coupon.getEndDate();
            Long sysDate = System.currentTimeMillis();
            //一天的毫秒数
            final int ONE_DAT_MILLS = 86400000;
            long activeMills = Math.round(activeTime * ONE_DAT_MILLS);
            //优惠券（coupon）已生效，customerCoupon（用户领取的优惠券）生效时间为领取时间。否则为coupon生效时间
            if (startDate < sysDate) {
                startDate = sysDate;
            }
            //customerCoupon（用户领取的优惠券）过期时间不能晚于coupon过期时间
            if (startDate + activeMills <= endDate) {
                endDate = startDate + activeMills;
            }
            customerCouponPojo.setStartDate(startDate);
            customerCouponPojo.setEndDate(endDate);
            customerCouponPojo.setActiveTime(endDate - startDate);
        }
        return customerCouponPojo;
    }

    /**
     * 校验优惠券兑换参数
     *
     * @param convertCouponParm 要检验的兑换参数
     * @return
     */
    public SimpleMessagePojo checkConvertParm(ConvertCouponParm convertCouponParm) {
        if (null == convertCouponParm) {
            return new SimpleMessagePojo(false, MsgMenu.CONVERT_PARM_ERR, null).setCheckMsg("兑换参数为空");
        }
        if (StringUtils.isEmpty(convertCouponParm.getCouponCode()) && StringUtils.isEmpty(convertCouponParm.getCouponValue())) {
            return new SimpleMessagePojo(false, MsgMenu.CONVERT_PARM_ERR, null).setCheckMsg("没有要兑换的优惠券信息");
        }
        if (!StringUtils.isEmpty(convertCouponParm.getCouponCode()) && !StringUtils.isEmpty(convertCouponParm.getCouponValue())) {
            return new SimpleMessagePojo(false, MsgMenu.CONVERT_PARM_ERR, null).setCheckMsg("couponCode与couponValue只能选择一个");
        }
        if (StringUtils.isEmpty(convertCouponParm.getCustomerId())) {
            return new SimpleMessagePojo(false, MsgMenu.CONVERT_PARM_ERR, null).setCheckMsg("兑换的用户账号不能为空");
        }
        //判断兑换数量,默认赋值为1
        if (convertCouponParm.getCount() == null) {
            convertCouponParm.setCount(1);
        }

        return new SimpleMessagePojo();
    }

    /**
     * 校验优惠券信息
     *
     * @param promotionCouponsPojo 要校验的优惠券信息
     * @param currentDate          当前时间,发券时间
     * @param sendType             发放类型
     * @return
     */
    public SimpleMessagePojo checkCouponData(PromotionCouponsPojo promotionCouponsPojo, Long currentDate, CouponType... sendType) {
        //根据优惠券编码查出最新版本的优惠券信息
        if (null == promotionCouponsPojo) {
            return new SimpleMessagePojo(false, MsgMenu.COUPON_IS_NULL, null).setCheckMsg("查询不到要兑换的优惠券信息");
        }
        //校验优惠券状态
        CouponsPojo coupon = promotionCouponsPojo.getCoupon();
        if (!Status.DELAY.getValue().equalsIgnoreCase(coupon.getStatus()) && !Status.ACTIVITY.getValue().equalsIgnoreCase(coupon.getStatus())) {
            return new SimpleMessagePojo(false, MsgMenu.COUPON_STATUS_ERR, null).setCheckMsg("只有活动中,待生效的优惠券可领取");
        }
        //校验允许领取时间
        if (currentDate < coupon.getGetStartDate() || currentDate > coupon.getGetEndDate()) {
            return new SimpleMessagePojo(false, MsgMenu.COUPON_DATE_ERR, null).setCheckMsg("不在优惠券领取时间范围[" + coupon.getGetStartDate() + "---" + coupon.getGetEndDate() + "]");

        }
        boolean containFlag = false;
        for (int i = 0; i < sendType.length; i++) {
            //判断发放类型
            if (sendType[i].getValue().equalsIgnoreCase(coupon.getType())) {
                containFlag = true;
                break;
            }
        }
        if (!containFlag) {
            return new SimpleMessagePojo(false, MsgMenu.COUPON_STATUS_ERR, null).setCheckMsg("该优惠券只能由" + CouponType.valueOf(coupon.getType()).getName());
        }

        return new SimpleMessagePojo(true, MsgMenu.SUCCESS, coupon);
    }


    /**
     * 校验用户能兑换的优惠券数量
     *
     * @param coupon 要领取的优惠券
     * @param userId 领取账号
     * @param count  领取张数
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public SimpleMessagePojo checkCustomerRede(CouponsPojo coupon, String userId, int count)  {
        CustomerRedeptionPojo customerRedeptionPojo = null;
        //判断用户还能兑换数量
        List<CustomerRedeptionPojo> customerRedeptionPojos = customerRedeptionService.queryByCouponIdAndUid(coupon.getId(), userId);
        if (!CollectionUtils.isEmpty(customerRedeptionPojos) && customerRedeptionPojos.size() == 1) {
            customerRedeptionPojo = customerRedeptionPojos.get(0);
            Integer redeptionNum = customerRedeptionPojo.getNumber();
            if (redeptionNum <= 0) {
                return new SimpleMessagePojo(false, MsgMenu.CONVERT_COUNT_ERR, null).setCheckMsg("用户领取次数已达上限");
            }
            if (count > redeptionNum) {
                return new SimpleMessagePojo(false, MsgMenu.CONVERT_COUNT_ERR, null).setCheckMsg("领取数量超出用户领取上限");

            }
        } else {
            customerRedeptionPojo = new CustomerRedeptionPojo();
            customerRedeptionPojo.setUserId(userId);
            customerRedeptionPojo.setCid(coupon.getId());
            customerRedeptionPojo.setNumber(coupon.getMaxRedemptionPerCustomer());
            customerRedeptionPojo.setId(UUID.randomUUID().toString());
            customerRedeptionService.insert(customerRedeptionPojo);
        }
        return new SimpleMessagePojo(true, MsgMenu.SUCCESS, customerRedeptionPojo);
    }

    /**
     * 校验优惠券最大兑换量
     *
     * @return
     */
    public SimpleMessagePojo checkCouponReede(CouponsPojo coupon, int count)  {
        //判断优惠券还能兑换的数量
        CouponRedemptionPojo couponRedemptionPojo = couponRedeptionService.queryByCid(coupon.getId());
        if (null == couponRedemptionPojo) {
            return new SimpleMessagePojo(false, MsgMenu.CONVERT_COUNT_ERR, null).setCheckMsg("[ERR]优惠券最大剩余兑换量不存在");
        }
        if (couponRedemptionPojo.getNumber() <= 0) {
            return new SimpleMessagePojo(false, MsgMenu.CONVERT_COUNT_ERR, null).setCheckMsg("优惠券领取次数已达上限");
        }
        if (count > couponRedemptionPojo.getNumber()) {
            return new SimpleMessagePojo(false, MsgMenu.CONVERT_COUNT_ERR, null).setCheckMsg("领取数量超出优惠券领取上限");
        }
        return new SimpleMessagePojo(true, MsgMenu.SUCCESS, couponRedemptionPojo);
    }

    /**
     * 生成count个要保存的优惠券数据
     *
     * @param coupon 要领取的优惠券
     * @param userId 领券账号
     * @param count  领取的张数
     * @return
     */
    public List<CustomerCouponPojo> generateCustomerCoupons(CouponsPojo coupon, String userId, int count) {
        List<CustomerCouponPojo> customerCouponPojos = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            CustomerCouponPojo customerCouponPojo = generateCustomerCoupon(coupon, userId);
            customerCouponPojos.add(customerCouponPojo);
        }
        return customerCouponPojos;
    }
}
