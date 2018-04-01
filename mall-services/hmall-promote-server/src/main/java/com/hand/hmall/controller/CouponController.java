package com.hand.hmall.controller;

import com.hand.hmall.client.IOrderPromoteClient;
import com.hand.hmall.dto.CurrentUser;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.menu.CouponType;
import com.hand.hmall.service.ICouponService;
import com.hand.hmall.service.ICouponTempService;
import com.hand.hmall.service.ICustomerCouponService;
import com.hand.hmall.util.GetMapValueUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/promotion/coupon")
public class CouponController {
    @Autowired
    private ICouponService couponService;
    @Autowired
    private ICustomerCouponService customerCouponService;
    @Autowired
    private ICouponTempService couponTempService;
    @Autowired
    private IOrderPromoteClient orderPromoteClient;

    @GetMapping("/list/{status}")
    public ResponseData selectAll(CurrentUser currentUser, @PathVariable String status) {
        return couponService.getAll(currentUser.getUserId(), status);
    }

    /**
     * 用户兑换优惠券
     * @param convertMap
     * @return
     */
    @PostMapping("/convert")
    public ResponseData convert(@RequestBody Map convertMap) {
        if (convertMap.isEmpty()) {
            return new ResponseData(false, "NULL_DATA");
        } else {
            return couponService.convertCoupon(convertMap);
        }
    }



    /**
     * 项目未使用
     *
     * @param couponTemps
     * @param currentUser
     * @return
     */
    @PostMapping("/usedByTempId")
    public ResponseData setUsedByTempIdAndDistributionId(@RequestBody List<Map<String, Object>> couponTemps, CurrentUser currentUser) {
        return couponTempService.addCouponTemp(couponTemps);
    }

    /**
     * 优惠券占用与释放接口
     * @param map
     * @return
     */
    @PostMapping("/operate")
    public ResponseData setUsed(@RequestBody Map map) {
        String couponId = (String) map.get("couponId");
        String orderId = (String) map.get("orderId");
        String operate = (String) map.get("operation");
        String customerId = (String) map.get("customerId");
        return couponService.setUsed(couponId, orderId, operate,customerId);
    }

    /**
     * 根据已领取优惠券（customerCoupon）的主键查询详细信息
     * @param couponId
     * @return
     */
    @GetMapping("/{couponId}")
    public ResponseData query(@PathVariable String couponId) {
        return couponService.select(couponId);
    }


    /**
     * HAP 根据userId查询该用户下的优惠券
     *
     * @param
     * @return
     */
    @PostMapping("/queryByUserIdAndStatus")
    public ResponseData queryByUserId(@RequestBody Map<String, Object> map) {
        ResponseData responseData = customerCouponService.queryUserIdAndStatus(map);
        List list = responseData.getResp();
        if (CollectionUtils.isNotEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                Map customerCoupon = (Map) list.get(i);
                customerCoupon.remove("isOverlay");
                customerCoupon.remove("range");
//                customerCoupon.remove("couponId");
                customerCoupon.remove("type");
                customerCoupon.remove("userId");
                customerCoupon.remove("cid");
                customerCoupon.remove("version");
//                customerCoupon.remove("benefit");
                customerCoupon.remove("sendDate");
                customerCoupon.remove("startDate");
                customerCoupon.remove("endDate");
            }
        }
        return responseData;
    }

    @PostMapping("/queryByUserIdAndStatusForPromote")
    public ResponseData queryByUserIdForPromote(@RequestBody Map<String, Object> map) {
        ResponseData responseData = customerCouponService.queryUserIdAndStatusForPromote(map);
        List list = responseData.getResp();
        List<String> respList = new ArrayList();
        if (CollectionUtils.isNotEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                Map customerCoupon = (Map) list.get(i);
                respList.add((String)customerCoupon.get("couponId"));
            }
        }
        responseData.setResp(respList);
        return responseData;
    }

    @PostMapping("/drools/queryByUserIdAndStatus")
    public ResponseData queryByUserIdAndStatus(@RequestBody Map<String, Object> map) {
        return customerCouponService.queryUserIdAndStatus(map);
    }

    /**
     * 根据用户登陆账号、优惠券状态、优惠券使用渠道查询优惠券
     * @param map
     * @return
     */
    @PostMapping("/queryByUserIdAndStatusAndRange")
    public ResponseData queryByUserIdAndStatusAndRange(@RequestBody Map<String, Object> map) {
        return customerCouponService.queryUserIdAndStatusAndRange(map.get("userId").toString(), map.get("status").toString(), map.get("range").toString());
    }

    /**
     * 根据 couponId userId  查出对应的数据
     *
     */

    @GetMapping("/selectCouponById")
    public ResponseData selectCouponById(@RequestParam("couponId") String couponId, @RequestParam("userId") String userId) {
        ResponseData responseData = couponService.selectCouponById(couponId, userId);
        return responseData;
    }

    @PostMapping("/generateRedeemCode")
    public ResponseData generateRedeemCode(@RequestBody Map map){
        String code = (String)map.get("couponCode");
        int num = (int)map.get("num");
        if(StringUtils.isEmpty(code)||num<=0){
            return new ResponseData(false,"兑换数量要大于0");
        }
       return couponService.generateRedeemCode(code,num);
    }

    /**
     * 查询优惠券码下的所有兑换码
     *
     * @param map
     * @return
     */
    @PostMapping("/getRedeemCodes")
    public ResponseData getRedeemCodes(@RequestBody Map map){
        String couponCode = (String)map.get("couponCode");
        if(StringUtils.isEmpty(couponCode)){
            return new ResponseData(false,"优惠券码不能为空");
        }
        return couponService.getRedeemCodes(couponCode);
    }

}
