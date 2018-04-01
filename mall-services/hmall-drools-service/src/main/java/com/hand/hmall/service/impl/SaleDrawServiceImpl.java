package com.hand.hmall.service.impl;

import com.hand.hmall.dao.*;
import com.hand.hmall.dto.PagedValues;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.menu.SaleType;
import com.hand.hmall.service.ISaleDrawService;
import com.hand.hmall.util.DateFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by cw on 2017/2/28.
 */
@Service
public class SaleDrawServiceImpl implements ISaleDrawService {
    @Autowired
    private SaleDrawDao saleDrawDao;
    @Autowired
    private SaleConditionActionDao saleConditionActionDao;
    @Autowired
    private SaleOperatorDao saleOperatorDao;
    @Autowired
    private SaleCouponDao saleCouponDao;
    @Autowired
    private AwardProDao awardProDao;
    @Autowired
    private AwardDao awardDao;

    @Override
    public ResponseData query(Map<String, Object> map) {
        try {
            PagedValues values = saleDrawDao.querySaleDraw(map);
            List<Map<String, ?>> values1 = values.getValues();
            for(Map<String, ?> map1 :values1) {
                Map<String, Object> map2 = (Map<String, Object>) map1;
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                map2.put("validTime", simpleDateFormat.format(map1.get("startDate")) + " - " + simpleDateFormat.format(map1.get("endDate")));
                long now = System.currentTimeMillis();
                if ((Long)map1.get("startDate") > now ) {
                    map2.put("drawStatus", "INACTIVE");
                    saleDrawDao.update(map2);
                } else if ((Long)map1.get("endDate") < now){
                    map2.put("drawStatus", "EXPIRED");
                    saleDrawDao.update(map2);
                }
            }
            ResponseData responseData = new ResponseData(values1);
            responseData.setTotal((int) values.getTotal());
            return responseData;
        } catch (ParseException e) {
            e.printStackTrace();
            return new ResponseData(false);
        }
    }

    @Override
    public ResponseData getAward(Map<String, Object> map) {
        PagedValues values = awardDao.queryAward(map);
        ResponseData responseData = new ResponseData(values.getValues());
        responseData.setTotal((int) values.getTotal());
        return responseData;
    }

    @Override
    public ResponseData submit(Map<String, Object> map) {

        Map<String, Object> draw = (Map<String, Object>) map.get("draw");
        Map<String, Object> conditionsActions = new HashMap<>();
        conditionsActions.put("conditions",  map.get("conditions"));
        conditionsActions.put("actions", map.get("actions"));

        Long creationTime = System.currentTimeMillis();

        draw.put("startDate", DateFormatUtil.stringToTimeStamp(draw.get("startDate").toString()));
        draw.put("endDate", DateFormatUtil.stringToTimeStamp(draw.get("endDate").toString()));

        if(map.get("drawId") == null || "".equals(map.get("drawId"))) {
            String drawId = UUID.randomUUID().toString();
            draw.put("creationTime", creationTime);
            draw.put("lastModifyTime", creationTime);
            draw.put("drawId", drawId);
            draw.put("releaseId", UUID.randomUUID());
            draw.put("isUsing",'Y');
            draw.put("id", UUID.randomUUID());
            saleDrawDao.add(draw);

            conditionsActions.put("detailId", draw.get("id"));
            conditionsActions.put("type", SaleType.DRAW.getValue());
            saleConditionActionDao.submitSaleCondition(conditionsActions);

            Map<String, Object> operatorMap = new HashMap<>();
            operatorMap.put("operator", map.get("userId"));
            operatorMap.put("operation", draw.get("drawName"));
            operatorMap.put("changeDate", creationTime);
            operatorMap.put("type", SaleType.DRAW.getValue());
            operatorMap.put("baseId", draw.get("drawId"));
            operatorMap.put("parentId", draw.get("releaseId"));
            saleOperatorDao.submit(operatorMap);

            List<Map<String, ?>> maps = awardProDao.selectByEqField("drawId", "-1");
            if(maps != null && maps.size() >0) {
                for (Map<String, ?> map1 : maps) {
                    Map<String, Object> map2 = (Map<String, Object>) map1;
                    map2.put("drawId", drawId);
                    map2.put("id", UUID.randomUUID());
                    awardProDao.add(map2);
                }
            }

        }else {

            draw.put("lastModifyTime", creationTime);
            draw.put("creationTime", DateFormatUtil.stringToTimeStamp(draw.get("creationTime").toString()));
            draw.put("releaseId", UUID.randomUUID());
            draw.put("isUsing","Y");

            List<Map<String, ?>> draws = saleDrawDao.selectByDrawId(draw.get("drawId").toString());
            for(int i=0; i<draws.size(); i++ ) {
                Map<String,Object> drawMap = (Map<String, Object>) draws.get(i);
                if(drawMap.get("isUsing") != null) {
                    drawMap.put("isUsing", "N");
                }
                saleDrawDao.update(drawMap);
            }
            draw.put("id", UUID.randomUUID());
            saleDrawDao.add(draw);

            conditionsActions.put("detailId", draw.get("id"));
            conditionsActions.put("type", SaleType.DRAW.getValue());
            saleConditionActionDao.submitSaleCondition(conditionsActions);

            Map<String ,Object> operatorMap = new HashMap<>();
            operatorMap.put("operator", map.get("userId"));
            operatorMap.put("changeDate", creationTime);
            operatorMap.put("operation", draw.get("drawName"));
            operatorMap.put("type", SaleType.DRAW.getValue());
            operatorMap.put("baseId", draw.get("drawId"));
            operatorMap.put("parentId", draw.get("releaseId"));
            saleOperatorDao.submit(operatorMap);

        }
        return new ResponseData(draw);
    }

    @Override
    public ResponseData delete(List<Map<String, Object>> maps) {
        for(Map<String, Object> map : maps) {

//            if (!map.get("drawStatus").equals("EXPIRED")) {
//                return ReturnResponseUtil.returnFalseResponse(map.get("drawName") + "尚未失效，不可删除！", null);
//            }else {
                List<Map<String, ?>> maps1 = saleDrawDao.selectByEqField("drawId", (String) map.get("drawId"));
                if(maps1 != null && maps1.size() >0) {
                    Map<String, Object> _map = (Map<String, Object>) maps1.get(0);
                    _map.put("drawStatus", "DELETED");
                    saleDrawDao.update(_map);
                }
            }
//        }
        return new ResponseData();
    }

    @Override
    public ResponseData active(List<Map<String, Object>> maps) {
        for(Map<String, Object> _map : maps) {
            List<Map<String, ?>> maps1 = saleDrawDao.selectByEqField("drawId", (String) _map.get("drawId"));
            if(maps1 != null && maps1.size() >0) {
                Map<String, Object> map = (Map<String, Object>) maps1.get(0);
                map.put("drawStatus", "ACTIVE");
                saleDrawDao.update(map);
            }
        }
        return new ResponseData();
    }

    @Override
    public ResponseData inactive(List<Map<String, Object>> maps) {
        for(Map<String, Object> _map : maps) {
            List<Map<String, ?>> maps1 = saleDrawDao.selectByEqField("drawId", (String) _map.get("drawId"));
            if(maps1 != null && maps1.size() >0) {
                Map<String, Object> map = (Map<String, Object>) maps1.get(0);
                map.put("drawStatus", "STOPPED");
                saleDrawDao.update(map);
            }
        }
        return new ResponseData();
    }

    @Override
    public Map<String, Object>  selectDrawDetail(String id) {

        List<Map<String, ?>> draws = saleDrawDao.selectByEqField("drawId", id);
        if (draws != null &&draws.size() >0) {
            Map<String, ?> draw = draws.get(0);
            Map<String, Object> conditionActions = saleConditionActionDao.selectByDetailIdAndType((String)draw.get("id"), SaleType.DRAW.getValue());
            Map<String, Object> map = new HashMap<>();
            map.put("draw", draw);
            if(conditionActions.get("conditions") != null) {
                map.put("conditions", conditionActions.get("conditions"));
            }
            if(conditionActions.get("actions") != null) {
                map.put("actions", conditionActions.get("actions"));
            }
            return map;
        }
        return new HashMap<>();
    }

    @Override
    public ResponseData awardPro(Map<String, Object> map) {
        List<Map<String, ?>> maps = saleCouponDao.selectByEqField("type", "COUPON_TYPE_03");
        Map<String, Object> awardPro = new HashMap<>();
        if (maps != null && maps.size() >0) {
            for (Map<String, ?> _map : maps) {
                String couponId = (String) _map.get("couponId");
                List<Map<String, ?>> couponIdList = awardProDao.selectByEqField("couponId", couponId);
                if (couponIdList == null || couponIdList.size() == 0) {
                    awardPro.put("id", UUID.randomUUID());
                    awardPro.put("drawId", "-1");
                    awardPro.put("couponId", couponId);
                    awardPro.put("couponName", _map.get("couponName"));
                    awardPro.put("awardProbility", 1);
                    awardPro.put("awardProStatus", "INACTIVE");
                    awardProDao.add(awardPro);
                }
            }
        }
        PagedValues values = awardProDao.queryAwardPro(map);
        List<Map<String, ?>> values1 = values.getValues();
        ResponseData responseData = new ResponseData(values1);
        responseData.setTotal((int) values.getTotal());
        return responseData;
    }

    @Override
    public ResponseData submitAwardPro(List<Map<String, Object>> list) {
        for (Map<String, Object> map : list) {
//            String drawId = (String) map.get("drawId");
//            if ("-1".equals(drawId)) {
//                List<Map<String, ?>> maps = awardProDao.selectByEqField("couponId", (String) map.get("couponId"));
//                Map<String, Object> awardPro = (Map<String, Object>) maps.get(0);
//                awardPro.put("awardProStatus", map.get("awardProStatus"));
//                awardPro.put("awardProbility", map.get("awardProbility"));
//                awardPro.put("drawId", drawId);
//                awardProDao.add(awardPro);
//            } else {
                Map<String, Object> m = new HashMap<>();
                m.put("couponId", map.get("couponId"));
                m.put("drawId", map.get("drawId"));
                List<Map<String, ?>> maps = awardProDao.selectByMutilEqField(m);
                Map<String, Object> awardPro = (Map<String, Object>) maps.get(0);
                awardPro.put("awardProStatus", map.get("awardProStatus"));
                awardPro.put("awardProbility", map.get("awardProbility"));
                awardProDao.update(awardPro);
//            }

        }
        return new ResponseData();
    }

    @Override
    public ResponseData startUsingAwardPro(Map<String, Object> _map) {
        Map<String, Object> map1 = new HashMap<>();
        String drawId = _map.get("drawId").toString();
        if ("undefined".equals(drawId)) {
            drawId = "-1";
        }
        map1.put("drawId", drawId);
        map1.put("couponId", _map.get("couponId"));
        List<Map<String, ?>> maps1 = awardProDao.selectByMutilEqField(map1);
        if(maps1 != null && maps1.size() >0) {
            Map<String, Object> map = (Map<String, Object>) maps1.get(0);
            map.put("awardProStatus", "ACTIVE");
            awardProDao.update(map);
        }
        return new ResponseData();
    }

    @Override
    public ResponseData endUsingAwardPro(Map<String, Object> _map) {
        Map<String, Object> map1 = new HashMap<>();
        map1.put("drawId", _map.get("drawId"));
        map1.put("couponId", _map.get("couponId"));
        List<Map<String, ?>> maps1 = awardProDao.selectByMutilEqField(map1);
        if(maps1 != null && maps1.size() >0) {
            Map<String, Object> map = (Map<String, Object>) maps1.get(0);
            map.put("awardProStatus", "INACTIVE");
            awardProDao.update(map);
        }
        return new ResponseData();
    }

    @Override
    public ResponseData getAwardpro(String drawId) {
        Map<String, Object> map = new HashMap<>();
        map.put("drawId", drawId);
        map.put("awardProStatus", "ACTIVE");
        List<Map<String, ?>> maps = awardProDao.selectByMutilEqField(map);
        return new ResponseData(maps);
    }

    @Override
    public ResponseData addAwardRecord(Map<String, Object> map) {
        Map<String, ?> draw = saleDrawDao.selectByEqField("drawId", (String) map.get("drawId")).get(0);
        Map<String, Object> draw1 = (Map<String, Object>) draw;
        int drawTotal = Integer.parseInt(draw.get("drawTotal").toString());
        draw1.put("drawTotal", String.valueOf(drawTotal - 1));
        saleDrawDao.update(draw1);
        if (!map.get("couponId").toString().equals("")) {
            map.put("id", UUID.randomUUID());
            awardDao.add(map);
        }
        return new ResponseData();
    }
}
