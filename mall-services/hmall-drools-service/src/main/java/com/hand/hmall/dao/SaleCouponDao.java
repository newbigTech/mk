package com.hand.hmall.dao;

import com.hand.hmall.dto.PagedValues;
import com.hand.hmall.dto.ScoreRange;
import com.hand.hmall.menu.Status;
import com.hand.hmall.util.DateFormatUtil;
import com.hand.hmall.util.redis.Field.FieldType;
import com.hand.hmall.util.redis.dao.BaseDao;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by shanks on 2017/1/5.
 */
@Repository
public class SaleCouponDao extends BaseDao{
    public static final String EMPTY ="empty";

    public SaleCouponDao(){

        this.hashTag = "Sale";
        this.clazz = "SaleCoupon";
        this.idDescriptor = createFieldDescriptor("id", FieldType.TYPE_EQUAL);
        addFieldDescriptor(createFieldDescriptor("couponId",FieldType.TYPE_EQUAL));
        addFieldDescriptor(createFieldDescriptor("couponCode",FieldType.TYPE_EQUAL));
        addFieldDescriptor(createFieldDescriptor("couponName",FieldType.TYPE_MATCH));
        //优惠券描述
        addFieldDescriptor(createFieldDescriptor("couponDes",FieldType.TYPE_EQUAL));
        addFieldDescriptor(createFieldDescriptor("benefit",FieldType.TYPE_EQUAL));
        //优惠券价格
        addFieldDescriptor(createFieldDescriptor("payFee",FieldType.TYPE_EQUAL));
        addFieldDescriptor(createFieldDescriptor("maxRedemptionPerCustomer",FieldType.TYPE_EQUAL));
        addFieldDescriptor(createFieldDescriptor("type",FieldType.TYPE_EQUAL));
        addFieldDescriptor(createFieldDescriptor("maxRedemption",FieldType.TYPE_EQUAL));
        //允许用户领取开始时间
        addFieldDescriptor(createFieldDescriptor("getStartDate",FieldType.TYPE_RANGE));
        //允许用户领取截止时间
        addFieldDescriptor(createFieldDescriptor("getEndDate",FieldType.TYPE_RANGE));
        addFieldDescriptor(createFieldDescriptor("endDate",FieldType.TYPE_RANGE));
        addFieldDescriptor(createFieldDescriptor("startDate",FieldType.TYPE_RANGE));
        addFieldDescriptor(createFieldDescriptor("creationTime",FieldType.TYPE_RANGE));
        addFieldDescriptor(createFieldDescriptor("lastCreationTime",FieldType.TYPE_RANGE));
        addFieldDescriptor(createFieldDescriptor("status",FieldType.TYPE_EQUAL));
        addFieldDescriptor(createFieldDescriptor("range",FieldType.TYPE_EQUAL));
        addFieldDescriptor(createFieldDescriptor("isOverlay",FieldType.TYPE_EQUAL));
        addFieldDescriptor(createFieldDescriptor("creationAt",FieldType.TYPE_EQUAL));
        addFieldDescriptor(createFieldDescriptor("updateAt",FieldType.TYPE_EQUAL));
//        addFieldDescriptor(createFieldDescriptor("operator",FieldType.TYPE_EQUAL));
        addFieldDescriptor(createFieldDescriptor("releaseId",FieldType.TYPE_EQUAL));

        //用于判断该规则是否是最高版本
        addFieldDescriptor(createFieldDescriptor("isUsing",FieldType.TYPE_EQUAL));
        //是否同步
        addFieldDescriptor(createFieldDescriptor("isSyn",FieldType.TYPE_EQUAL));
        //优惠券折扣类型
        addFieldDescriptor(createFieldDescriptor("discountType",FieldType.TYPE_EQUAL));
        //优惠券是否排它
        addFieldDescriptor(createFieldDescriptor("isExclusive", FieldType.TYPE_EQUAL));
        //优惠券领取后生效天数
        addFieldDescriptor(createFieldDescriptor("activeTime", FieldType.TYPE_EQUAL));


    }


    public PagedValues queryCoupon(Map<String,Object> map) throws ParseException {

        Map<String,Object> data= (Map<String, Object>) map.get("data");
        int page=(int) map.get("page");
        int pageSize=(int) map.get("pageSize");
        if(!data.isEmpty()) {
            List<String> keys = new ArrayList<>();

            //equal搜索
            Map<String,List<Object>> equal = new HashMap<>();
            String isUsing = (String) data.get("isUsing");
            if (StringUtils.isEmpty(isUsing)) {
                equal.put("isUsing", Arrays.asList("Y", "N"));
            } else {
                equal.put("isUsing", Arrays.asList(isUsing));
            }


            if (data.get("couponId") != null) {
                equal.put("couponId", Arrays.asList(data.get("couponId").toString()));
            }
            if (data.get("couponCode") != null) {
                equal.put("couponCode", Arrays.asList(data.get("couponCode").toString()));
            }
//            if (data.get("isOverlay") != null) {
//                if(data.get("isOverlay").equals("ALL"))
//                {
//                    equal.put("isOverlay", Arrays.asList("Y","N"));
//                }else {
//                    equal.put("isOverlay", Arrays.asList(data.get("isOverlay").toString()));
//                }
//            }
            if (data.get("status") != null) {
                if(data.get("status").equals("ALL")) {

                    equal.put("status", Arrays.asList(Status.ACTIVITY.getValue(), Status.INACTIVE.getValue(),
                            Status.DELAY.toString(),Status.FAILURE.getValue()));

                } else if (data.get("status").equals("SENDCOUPON")) {
                    equal.put("status", Arrays.asList(Status.ACTIVITY.getValue(),
                            Status.DELAY.toString()));
                } else {
                    equal.put("status", Arrays.asList(data.get("status").toString()));

                }
            }

            //优惠券类型
            if (StringUtils.isNotEmpty((String) data.get("type"))) {
                equal.put("type", Arrays.asList((String) data.get("type")));
            }

            if (data.get("channel") != null) {
                if(data.get("channel").equals("ALL")) {
                    equal.put("channel", Arrays.asList("SHOP","PORTAL"));
                }else {
                    equal.put("channel", Arrays.asList(data.get("channel").toString()));
                }
            }
            if(MapUtils.isNotEmpty(equal)){
                String key1 = this.selectIdsSetByEqual(equal);
                if(key1!=null){
                    keys.add(key1);
                }
            }

            //range匹配
            Map<String,ScoreRange> ranges = new HashMap<>();
            if(data.get("startDate")!=null) {
                Long validStartDate = DateFormatUtil.stringToTimeStamp(data.get("startDate").toString());
                Long validEndDate=null;
                if(data.get("endDate")!=null) {
                    validEndDate= DateFormatUtil.stringToTimeStamp(data.get("endDate").toString());
                }else{
                    validEndDate=DateFormatUtil.stringToTimeStamp("2099-12-31 00:00:00");
                }

                ScoreRange scoreRange = new ScoreRange();
                scoreRange.setMin(validStartDate.toString());
                scoreRange.setMax(validEndDate.toString());
                ranges.put("startDate", scoreRange);
            }else{

                if(data.get("endDate")!=null) {
                    Long validStartDate =DateFormatUtil.stringToTimeStamp("2000-1-1 00:00:00");
                    Long validEndDate = DateFormatUtil.stringToTimeStamp(data.get("endDate").toString());

                    ScoreRange scoreRange = new ScoreRange();
                    scoreRange.setMin(validStartDate.toString());
                    scoreRange.setMax(validEndDate.toString());
                    ranges.put("startDate", scoreRange);
                }
            }
            if(MapUtils.isNotEmpty(ranges)){
                String key2 = this.selectIdsSetByRange(ranges);
                if(key2!=null){
                    keys.add(key2);
                }
            }
            //match模糊匹配

            Map<String, Object> matches = new HashMap<>();

            if (data.get("couponName") != null) {
                matches.put("couponName", data.get("couponName").toString());
            }



            if(MapUtils.isNotEmpty(matches)){
                String key3 = this.selectIdsSetByMatch(matches);
                if(key3!=null){
                    keys.add(key3);
                }
            }
            PagedValues values = this.selectValuesByKeys(keys, (page - 1) * pageSize, pageSize, true);
            return values;
        }else
        {
            List<String> keys = new ArrayList<>();
            Map<String,List<Object>> equal = new HashMap<>();
            equal.put("isUsing",Arrays.asList("Y"));
            String key = this.selectIdsSetByEqual(equal);
            keys.add(key);
            PagedValues values = this.selectValuesByKeys(keys, (page - 1) * pageSize, pageSize, true);

            return values;
        }
    }

    public PagedValues queryCouponActivity(Map<String,Object> map) throws ParseException {

        Map<String,Object> data= (Map<String, Object>) map.get("data");
        List<String> notIn= (List<String>) map.get("notIn");
        int page=(int) map.get("page");;
        int pageSize=(int) map.get("pageSize");
        if(!data.isEmpty()) {
            List<String> keys = new ArrayList<>();

            //equal搜索
            Map<String,List<Object>> equal = new HashMap<>();
            equal.put("isUsing",Arrays.asList("Y"));
            equal.put("status", Arrays.asList(Status.ACTIVITY.getValue()));


            if (data.get("couponId") != null) {
                equal.put("couponId", Arrays.asList(data.get("couponId").toString()));
            }
            if (data.get("couponCode") != null) {
                equal.put("couponCode",Arrays.asList( data.get("couponCode").toString()));
            }

            if (data.get("isOverlay") != null) {
                if(data.get("isOverlay").equals("ALL"))
                {
                    equal.put("isOverlay", Arrays.asList("Y","N"));
                }else {
                    equal.put("isOverlay", Arrays.asList(data.get("isOverlay").toString()));
                }
            }

            if(MapUtils.isNotEmpty(equal)){
                String key1 = this.selectIdsSetByEqual(equal);
                if(key1!=null){
                    keys.add(key1);
                }
            }

            //不重复匹配
            if(!notIn.isEmpty()) {
                String key3 = this.selectIdsSetByNotUniqueEq(notIn);
                if(key3!=null){
                    keys.add(key3);
                }
            }

            //range匹配
            Map<String,ScoreRange> ranges = new HashMap<>();
            if(data.get("startDate")!=null&&data.get("endDate")!=null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date validStartDate = simpleDateFormat.parse(data.get("startDate").toString());
                Long validStartTime = validStartDate.getTime();

                Date validEndDate = simpleDateFormat.parse(data.get("endDate").toString());
                Long validEndTime = validEndDate.getTime();

                ScoreRange scoreRange=new ScoreRange();
                scoreRange.setMin(validStartTime.toString());
                scoreRange.setMax(validEndTime.toString());
                ranges.put("startDate", scoreRange);

            }

            if(MapUtils.isNotEmpty(ranges)){
                String key2 = this.selectIdsSetByRange(ranges);
                if(key2!=null){
                    keys.add(key2);
                }
            }
            //match模糊匹配

            Map<String, Object> matches = new HashMap<>();

            if (data.get("couponName") != null) {
                matches.put("couponName", data.get("couponName").toString());
            }


            if(MapUtils.isNotEmpty(matches)){
                String key3 = this.selectIdsSetByMatch(matches);
                if(key3!=null){
                    keys.add(key3);
                }
            }
            PagedValues values = this.selectValuesByKeys(keys, (page - 1) * pageSize, pageSize, true);
            return values;
        }else
        {
            List<String> keys = new ArrayList<>();
            Map<String,List<Object>> equal = new HashMap<>();
            equal.put("isUsing",Arrays.asList("Y"));
            equal.put("status",Arrays.asList(Status.ACTIVITY.getValue()));
            String key = this.selectIdsSetByEqual(equal);
            keys.add(key);
            PagedValues values = this.selectValuesByKeys(keys, (page - 1) * pageSize, pageSize, true);
            return values;
        }
    }

    public List<Map<String,?>> queryCouponBySyn() throws ParseException{
        List<String> keys = new ArrayList<>();
        Map<String,List<Object>> equal = new HashMap<>();
        equal.put("isSyn",Arrays.asList("N"));
        equal.put("isUsing",Arrays.asList("Y"));
        if(MapUtils.isNotEmpty(equal)){
            String key1 = this.selectIdsSetByEqual(equal);
            if(key1!=null){
                keys.add(key1);
            }
        }
        return this.selectValuesByKeys(keys);
    }

    /**
     * 根据优惠券编码，查询待生效，活动中状态的isUsing为Y的优惠券
     *
     * @param couponCode
     * @return
     */
    public List<Map<String,?>> selectByCodeCanUse(String couponCode){

        List<String> keys = new ArrayList<>();

        //equal搜索
        Map<String,List<Object>> equal = new HashMap<>();
        equal.put("couponCode",Arrays.asList(couponCode));
        equal.put("status",Arrays.asList(Status.ACTIVITY.getValue(),Status.DELAY.toString()));
        equal.put("isUsing",Arrays.asList("Y"));

        if(MapUtils.isNotEmpty(equal)){
            String key1 = this.selectIdsSetByEqual(equal);
            if(key1!=null){
                keys.add(key1);
            }
        }

        return this.selectValuesByKeys(keys);


    }

    /**
     * 查询notIn参数中对应的优惠券参数
     *
     * @param map
     * @return
     */
    public List<Map<String,Object>> queryByNotIn(Map<String,Object> map) {


        List<Map<String,Object>> notIn= (List<Map<String,Object>>) map.get("notIn");
        int page=(int) map.get("page");
        int pageSize=(int) map.get("pageSize");

        List<Map<String,Object>> returnData=new ArrayList<>();
        if(page*pageSize>=notIn.size()) {

            for(int i = (page-1)*pageSize; i<notIn.size(); i++) {

                Map<String,?> mapData=this.select(notIn.get(i).get("id").toString());
                Map<String,Object> notInData=new HashMap<>();
                notInData.put("id",mapData.get("id"));
                notInData.put("couponId",mapData.get("couponId"));
                notInData.put("couponCode",mapData.get("couponCode"));
                notInData.put("couponName",mapData.get("couponName"));
                notInData.put("isOverlay",mapData.get("isOverlay"));
                notInData.put("status",mapData.get("status"));
                notInData.put("startDate",mapData.get("startDate"));
                notInData.put("endDate",mapData.get("endDate"));
                notInData.put("countNumber",notIn.get(i).get("countNumber"));
                returnData.add(notInData);

            }
            return returnData;
        }else {
            for(int i=(page-1)*pageSize; i<page*pageSize; i++) {
                Map<String,?> mapData=this.select(notIn.get(i).get("id").toString());
                Map<String,Object> notInData=new HashMap<>();
                notInData.put("id",mapData.get("id"));
                notInData.put("couponId",mapData.get("couponId"));
                notInData.put("couponCode",mapData.get("couponCode"));
                notInData.put("couponName",mapData.get("couponName"));
                notInData.put("isOverlay",mapData.get("isOverlay"));
                notInData.put("status",mapData.get("status"));
                notInData.put("startDate",mapData.get("startDate"));
                notInData.put("endDate",mapData.get("endDate"));
                notInData.put("countNumber",notIn.get(i).get("countNumber"));
                returnData.add(notInData);
            }
            return returnData;


        }

    }

    /**
     * 保存优惠券
     * @param map
     * @return
     */
    public Map<String,Object> submitCoupon(Map<String,Object> map) {
        map.put("id", UUID.randomUUID());
        this.add(map);
        return map;
    }

    /**
     * 更新优惠券信息
     * @param map
     * @return
     */
    public Map<String,Object> updateCoupon(Map<String,Object> map) {
        this.update(map);
        return map;
    }


    /**
     * 根据优惠券编码Id查询优惠券详细信息
     * @param couponId
     * @return
     */
    public List<Map<String, ?>> selectByCouponId(String couponId) {
        Map<String,List<Object>> equal = new HashMap<>();
        List<String> keys = new ArrayList<>();

        if(couponId!=null){
            equal.put("couponId", Arrays.asList(couponId));
        }

        if(!equal.isEmpty()) {
            String key = this.selectIdsSetByEqual(equal);
            if(key!=null){
                keys.add(key);
            }
        }
        return this.selectValuesByKeys(keys);

    }

    /**
     * 根据优惠券主键查询优惠券详细信息
     * @param id
     * @return
     */
    public Map<String, ?> selectCouponDetail(String id) {
        return this.select(id);
    }

    /**
     * 根据优惠券编码，是否最新字段查询 活动中，待生效，已失效状态的优惠券
     *
     * @param couponCode 优惠券编码
     * @param isUsing    优惠券是否是最新状态
     * @return
     */
    public List<Map<String,?>> selectCouponByCouponCode(String couponCode, String isUsing) {
        Map<String,List<Object>> equal = new HashMap<>();
        List<String> keys = new ArrayList<>();
        equal.put("status", Arrays.asList(Status.ACTIVITY.getValue(),
                Status.DELAY.toString(),Status.FAILURE.getValue()));

        if(StringUtils.isNotEmpty(couponCode)){
            equal.put("couponCode", Arrays.asList(couponCode));
        }
        if(StringUtils.isNotEmpty(isUsing)){
            equal.put("isUsing", Arrays.asList(isUsing));
        }

        if(!equal.isEmpty()) {
            String key = this.selectIdsSetByEqual(equal);
            if(key!=null){
                keys.add(key);
            }
        }
        List<Map<String, ?>> maps= this.selectValuesByKeys(keys);
        return maps;
    }

    /**
     * 根据优惠券编码，和优惠券版本号查询优惠券
     * @param couponCode
     * @param releaseId
     * @return
     */
    public List<Map<String,?>> selectByCodeAndReleaseId(String couponCode, String releaseId){
        List<String> keys = new ArrayList<>();
        Map<String,List<Object>> equal = new HashMap<>();
        equal.put("couponCode",Arrays.asList(couponCode));
        equal.put("releaseId",Arrays.asList(releaseId));
        if(MapUtils.isNotEmpty(equal)){
            String key1 = this.selectIdsSetByEqual(equal);
            if(key1!=null){
                keys.add(key1);
            }
        }

        return this.selectValuesByKeys(keys);
    }

    /**
     * 校验优惠券编码是否存在
     *
     * @param couponCode
     * @return
     */
    public List<Map<String,?>> checkedCodeIsExist(String couponCode) {

        List<String> keys = new ArrayList<>();
        Map<String, List<Object>> equal = new HashMap<>();
        if (couponCode != null) {
            equal.put("couponCode", Arrays.asList(couponCode));
        }

        if(MapUtils.isNotEmpty(equal)){
            String key3 = this.selectIdsSetByEqual(equal);
            if(key3!=null){
                keys.add(key3);
            }
        }
        return this.selectValuesByKeys(keys);

    }


    protected Set<ZSetOperations.TypedTuple<String>> convertKeyToTuple(Set<String> keys){
        Set<ZSetOperations.TypedTuple<String>> tupleSet = new HashSet<>();
        for(String key:keys){
            ZSetOperations.TypedTuple<String> tuple = new DefaultTypedTuple<>(key,0.0);
            tupleSet.add(tuple);
        }
        return tupleSet;
    }

    public String selectIdsSetByNotEq(Map<String,List<Object>> equals)
    {
        if(MapUtils.isEmpty(equals)){
            return null;
        }
        Set<String> notInSet = new HashSet<>();
        for(Map.Entry<String,List<Object>> entry:equals.entrySet()){
            String key = entry.getKey();
            List<Object> values = entry.getValue();
            if(values==null||values.isEmpty()){
                continue;
            }
            for(Object value:values){
                Set<String> tempSet = redisTemplate.boundZSetOps(createPattern(key,objToString(value))).rangeByScore(-Double.MAX_VALUE, Double.MAX_VALUE);
                notInSet.addAll(tempSet);
            }
        }
        BoundHashOperations<String,String,String> hashOperations = redisTemplate.boundHashOps(createPattern());
        Set<String> allkeySet = hashOperations.keys();
        allkeySet.removeAll(notInSet);
        if(allkeySet.isEmpty()){
            return EMPTY;
        }
        Set<ZSetOperations.TypedTuple<String>> tupleSet = convertKeyToTuple(allkeySet);
        String resultKey = createPattern(UUID.randomUUID().toString());
        redisTemplate.boundZSetOps(resultKey).add(tupleSet);
        return resultKey;
    }

    public String selectIdsSetByNotUniqueEq(List<String> ids){
        String hashKey = createPattern();
        BoundHashOperations<String,String,String> hashOperations = redisTemplate.boundHashOps(hashKey);
        Set<String> keys = hashOperations.keys();
        keys.removeAll(ids);
        Set<ZSetOperations.TypedTuple<String>> tuples = convertKeyToTuple(keys);
        String resultKey = createPattern(UUID.randomUUID().toString());
        redisTemplate.boundZSetOps(resultKey).add(tuples);
        return resultKey;
    }
    
    
}
