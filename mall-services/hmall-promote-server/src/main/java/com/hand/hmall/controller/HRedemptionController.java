package com.hand.hmall.controller;

import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.service.IRedemptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by shanks on 2017/2/22.
 * 用于操作优惠券（coupon）兑换数量字段
 */
@RestController
@RequestMapping(value = "/h/redemption")
public class HRedemptionController {

    @Autowired
    private IRedemptionService redemptionService;

    /**
     * 保存优惠券的最大兑换数量，用于新建优惠券
     *
     * @param map
     * @return
     */
    @PostMapping("/submitRedemption")
    public ResponseData submitRedemption(@RequestBody Map<String,Object> map) {
        return redemptionService.submitRedemption(map);
    }

    /**
     * 删除优惠券最大兑换数量，用于创建优惠券异常时回滚数据
     * @param redemptionId
     * @param type
     */
    @GetMapping("/deleteRedemption")
    public void submitRedemption(@RequestParam("redemptionId") String redemptionId, @RequestParam("type") String type) {
        redemptionService.deleteReal(redemptionId,type);
    }

    /**
     * 赠品发放界面，校验每单可发放赠品及促销总共赠送的赠品
     * @param mapList
     * @return
     */
    @PostMapping("/checkedCouponCount")
    ResponseData checkedCouponCount(@RequestBody List<Map<String,Object>> mapList) {

        for(Map<String,Object> map:mapList ) {
            if((int)map.get("countNumber")>0) {
                ResponseData responseData = redemptionService.checkedCouponCount(map.get("id").toString(), map.get("couponName").toString(),
                        (int) map.get("countNumber"));
                if (!responseData.isSuccess()) {
                    return responseData;
                }
            }else{
                ResponseData responseData=new ResponseData();
                responseData.setSuccess(false);
                responseData.setMsg("请填写大于0的数字");
                return responseData;
            }
        }
        return new ResponseData(mapList);
    }
}
