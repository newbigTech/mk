package com.hand.hmall.util;

import com.hand.hmall.dao.SalePromotionCodeDao;
import com.hand.hmall.menu.Status;
import com.hand.hmall.service.ISalePromotionCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 生成商品促销关联关系
 * Created by shanks on 2017/3/27.
 */
@Component
public class SaleProductThread implements Runnable {

    @Autowired
    private ISalePromotionCodeService salePromotionCodeService;
    @Autowired
    private SalePromotionCodeDao salePromotionCodeDao;

    private List<Map<String, Object>> conditions;
    private List<Map<String, Object>> groups;
    private List<Map<String, Object>> containers;
    private Map<String, Object> activity;

    private String type;

    public void setConditions(List<Map<String, Object>> conditions) {
        this.conditions = conditions;
    }

    public void setGroups(List<Map<String, Object>> groups) {
        this.groups = groups;
    }

    public void setContainers(List<Map<String, Object>> containers) {
        this.containers = containers;
    }

    public void setActivity(Map<String, Object> activity) {
        this.activity = activity;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void run() {
        //type为ADD保存关联关系；type为INACTIVE用于停用促销，将商品促销关联关系状态置为INACTIVE
        switch (type) {
            case "ADD":
                salePromotionCodeService.saveProductRelevance(this.conditions, this.groups,
                        this.containers, this.activity);
                break;
            case "INACTIVE": {
                salePromotionCodeService.saveProductRelevance(this.conditions, this.groups,
                        this.containers, this.activity);
                List<Map<String, ?>> salePromotionCodeList = salePromotionCodeDao.selectByActivityId(this.activity.get("activityId").toString());
                salePromotionCodeList.stream().map(stringMap -> (Map<String, Object>) stringMap).forEach(data -> {
                    data.put("status", Status.INACTIVE.getValue());
                    salePromotionCodeDao.update(data);
                });
                break;
            }
            default: {
                List<Map<String, ?>> salePromotionCodeList = salePromotionCodeDao.selectByActivityId(this.activity.get("activityId").toString());
                for (Map<String, ?> salePromotionCode : salePromotionCodeList) {
                    salePromotionCodeDao.delete(salePromotionCode.get("id").toString());
                }
                salePromotionCodeService.saveProductRelevance(this.conditions, this.groups,
                        this.containers, this.activity);
                break;
            }
        }
    }
}
