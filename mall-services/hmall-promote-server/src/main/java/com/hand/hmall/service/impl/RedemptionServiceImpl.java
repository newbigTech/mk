package com.hand.hmall.service.impl;


import com.hand.hmall.annotation.RedisTransaction;
import com.hand.hmall.dao.RedemptionDao;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.service.IRedemptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by shanks on 2017/2/22.
 */
@Service
public class RedemptionServiceImpl implements IRedemptionService {

    @Autowired
    private RedemptionDao redemptionDao;

    @Override
    public ResponseData submitRedemption(Map<String, Object> map) {

        return new ResponseData(redemptionDao.submitRedemption(map));
    }

    @Override
    public void deleteReal(String redemptionId,String type) {
        if(redemptionId!=null) {
            Map<String,?>map=redemptionDao.selectByIdAndType(redemptionId,type);
            if(map!=null) {
                if(map.get("id")!=null){
                    redemptionDao.delete(map.get("id").toString());
                }
            }
        }
    }

    @Override
    @RedisTransaction(clazz = "coupon:redemption")
    public Map<String, ?> selectByIdAndType(String id, String type) {
        return redemptionDao.selectByIdAndType(id,type);
    }

    @Override
    @RedisTransaction(clazz = "coupon:redemption")
    public boolean deleteRedemptionCount(String id, String type, Integer countNumber) {
        return redemptionDao.deleteRedemptionCount(id,type,countNumber);
    }

    @Override
    public ResponseData checkedCouponCount(String couponId,String name, int count) {
        Map<String,Object> redemption= (Map<String, Object>) redemptionDao.selectByIdAndType(couponId,"COUPON");

        if(redemption!=null)
        {
            if(count>(int)redemption.get("number"))
            {
                ResponseData responseData=new ResponseData();
                responseData.setSuccess(false);
                responseData.setMsg("【"+name+"】优惠券分配张数过多");
                return responseData;
            }
        }else
        {
            ResponseData responseData=new ResponseData();
            responseData.setSuccess(false);
            responseData.setMsg("当前未找到该优惠券次数记录");
            return responseData;
        }
        return new ResponseData();
    }
}
