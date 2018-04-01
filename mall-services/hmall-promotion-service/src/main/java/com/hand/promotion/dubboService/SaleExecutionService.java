package com.hand.promotion.dubboService;

import com.alibaba.dubbo.config.annotation.Service;

import com.hand.dto.ResponseData;
import com.hand.hpromotion.ISaleExecutionService;
import com.hand.promotion.pojo.activity.SalePromotionCodePojo;
import com.hand.promotion.pojo.enums.MsgMenu;
import com.hand.promotion.pojo.order.OrderPojo;
import com.hand.promotion.service.IExecutionService;
import com.hand.promotion.service.ISalePromotionCodeService;
import com.hand.promotion.util.BeanMapExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * c
 * <p>
 * Created by darkdog on 2018/2/2.
 * 促销执行接口
 */
public class SaleExecutionService implements ISaleExecutionService {

    @Autowired
    private ISalePromotionCodeService salePromotionCodeService;
    @Autowired
    private IExecutionService executionService;

    static Logger logger = LoggerFactory.getLogger(SaleExecutionService.class);

    /**
     * @param orderMap 前台促销执行参数
     * @return
     */
    @Override
    public ResponseData promoteOrder(Map<String, Object> orderMap) {
        long currentTime = System.currentTimeMillis();
        OrderPojo orderPojo = BeanMapExchange.mapToObject(orderMap, OrderPojo.class);
        ResponseData responseData = executionService.promoteOrder(orderPojo);
        logger.info("############促销执行耗时:{}", System.currentTimeMillis() - currentTime);
        return responseData;
    }


    /**
     * 查询商品对应的促销活动
     *
     * @param productMap
     * @return
     */
    @Override
    public ResponseData getActivity(Map productMap) {
        ResponseData responseData = new ResponseData();
        String code = productMap.get("code").toString();
        List<SalePromotionCodePojo> codeUsefulPromos = salePromotionCodeService.findCodeUsefulPromo(code);
        List list = new ArrayList<>();
        for (SalePromotionCodePojo codeUsefulPromo : codeUsefulPromos) {
            Map activityMid = new HashMap<>();
            activityMid.put("activityId", codeUsefulPromo.getActivityId());
            activityMid.put("activityName", codeUsefulPromo.getMeaning());
            activityMid.put("activityDes", codeUsefulPromo.getMeaning());
            activityMid.put("pageShowMes", codeUsefulPromo.getMeaning());
            list.add(activityMid);
        }
        responseData.setMsg(MsgMenu.SUCCESS.getMsg());
        responseData.setMsgCode(MsgMenu.SUCCESS.getCode());
        responseData.setResp(list);
        return responseData;
    }

}
