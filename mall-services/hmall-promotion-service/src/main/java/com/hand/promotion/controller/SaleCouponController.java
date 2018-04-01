package com.hand.promotion.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.hand.dto.ResponseData;
import com.hand.promotion.pojo.SimpleMessagePojo;
import com.hand.promotion.pojo.coupon.CouponsPojo;
import com.hand.promotion.pojo.coupon.PromotionCouponsPojo;
import com.hand.promotion.pojo.enums.MsgMenu;
import com.hand.promotion.service.ICouponService;
import com.hand.promotion.util.BeanMapExchange;
import com.hand.promotion.util.ResponseReturnUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 优惠券操作Controller
 * Created by xinyang.Mei
 */
@RestController
@RequestMapping(value = "/h/sale/coupon", produces = {MediaType.APPLICATION_JSON_VALUE})
public class SaleCouponController {

    @Autowired
    private ICouponService saleCouponService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 创建优惠券
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData submit(@RequestBody Map<String, Object> map) {
        try {
            logger.info("----create coupon----{}", JSON.toJSONString(map));
            //格式化时间
            boolean praseDateResult = praseDate((Map) map.get("coupon"));
            if (!praseDateResult) {
                return ResponseReturnUtil.returnResp(null, MsgMenu.DATE_FORMATE_ERR, false);
            }
            //将Map转换为对应的pojo
            PromotionCouponsPojo promotionCouponsPojo = BeanMapExchange.mapToObject(map, PromotionCouponsPojo.class);
            String userId = JSONObject.parseObject(JSON.toJSONString(map)).getString("userId");
            //创建优惠券
            return saleCouponService.submit(promotionCouponsPojo, userId);

        } catch (Exception e) {
            logger.error("促销创建/修改异常", e);
            return ResponseReturnUtil.returnFalseResponse("优惠券创建异常", "CREATE_COUPON_ERR");
        }

    }

    /**
     * 将coupon中字符串格式的时间转换为时间戳
     * 控制层内部私有类
     *
     * @param coupon
     */
    boolean praseDate(Map coupon) {
        String startDate = (String) coupon.get("startDate");
        String endDate = (String) coupon.get("endDate");
        String getStartDate = (String) coupon.get("getStartDate");
        String getEndDate = (String) coupon.get("getEndDate");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setLenient(false);
        try {
            if (!StringUtils.isEmpty(startDate)) {
                coupon.put("startDate", simpleDateFormat.parse(startDate));

            }
            if (!StringUtils.isEmpty(endDate)) {
                coupon.put("endDate", simpleDateFormat.parse(endDate));

            }
            if (!StringUtils.isEmpty(startDate)) {
                coupon.put("getStartDate", simpleDateFormat.parse(getStartDate));

            }
            if (!StringUtils.isEmpty(endDate)) {
                coupon.put("getEndDate", simpleDateFormat.parse(getEndDate));

            }
        } catch (ParseException e) {
            logger.error("---时间转换异常---", e);
            return false;
        }
        return true;
    }

    /**
     * 查询优惠券列表 供MAP调用
     *
     * @param map
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData query(@RequestBody Map<String, Object> map) {

        logger.info("-----------query-----------\n{}", JSON.toJSONString(map));
        try {
            Map data = (Map) map.get("data");
            int pageNum = (int) map.get("page");
            int pageSize = (int) map.get("pageSize");
            praseDate(data);
            CouponsPojo couponsPojo = BeanMapExchange.mapToObject(data, CouponsPojo.class);
            PromotionCouponsPojo condition = new PromotionCouponsPojo();
            condition.setCoupon(couponsPojo);
            return saleCouponService.query(condition, pageNum, pageSize);
        } catch (Exception e) {
            logger.error("查询异常", e);
            return ResponseReturnUtil.returnFalseResponse("查询促销活动异常", "QUERY_PROMOTION_ERR");

        }

    }

    /**
     * 查看优惠券详情
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData detail(@RequestParam("id") String id) {
        PromotionCouponsPojo promotionCouponsPojo = saleCouponService.selectCouponDetail(id);
        return new ResponseData(Arrays.asList(promotionCouponsPojo));

    }

    /**
     * 启用优惠券
     * 同时启用已发放的优惠券
     *
     * @param maps
     * @return
     */
    @RequestMapping(value = "/startUsing", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData startUsing(@RequestBody List<Map<String, Object>> maps) {
        List<String> errList = new ArrayList<>();
        try {
            for (Map<String, Object> map : maps) {
                String activityId = map.get("id").toString();
                SimpleMessagePojo result = saleCouponService.startUsing(activityId);
                if (!result.isSuccess()) {
                    errList.add("<p>" + (String) map.get("activityName") + " " + result.getMessage().getMsg() + "</p>");
                }
            }
            if (errList.size() != 0) {
                errList.set(errList.size() - 1, errList.get(errList.size() - 1) + "停用异常");
                return ResponseReturnUtil.returnResp(errList, MsgMenu.INACTIVT_ERR, false);
//                return ResponseReturnUtil.returnFalseResponse("部分停止异常" + errList.toString(), "INACTIVE_ERR");
            }
            return ResponseReturnUtil.returnTrueResp(null);
        } catch (Exception e) {
            logger.error("---停用异常---", e);
            return ResponseReturnUtil.returnFalseResponse(e.getMessage(), "INACTIVE_ERR");
        }
    }

    /**
     * 停用优惠券
     * 同时停用已发放的优惠券
     *
     * @param maps
     * @return
     */
    @RequestMapping(value = "/endUsing", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData endUsing(@RequestBody List<Map<String, Object>> maps) {
        List<String> errList = new ArrayList<>();
        try {
            for (Map<String, Object> map : maps) {
                String couponId = map.get("id").toString();
                SimpleMessagePojo result = saleCouponService.endUsing(couponId);
                if (!result.isSuccess()) {
                    errList.add("<p>" + map.get("couponName") + " " + result.getMessage().getMsg() + "</p>");
                }
            }
            if (errList.size() != 0) {
                errList.set(errList.size() - 1, errList.get(errList.size() - 1) + "停用异常");
                return ResponseReturnUtil.returnResp(errList, MsgMenu.INACTIVT_ERR, false);
            }
            return ResponseReturnUtil.returnTrueResp(null);
        } catch (Exception e) {
            logger.error("---停用异常---", e);
            return ResponseReturnUtil.returnFalseResponse(e.getMessage(), "INACTIVE_ERR");
        }


    }

    /**
     * 删除已失效优惠券
     *
     * @param maps
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData delete(@RequestBody List<Map<String, Object>> maps) {
        List<String> ids = new ArrayList<>(maps.size());
        maps.forEach(map -> {
            ids.add(map.get("id").toString());
        });
        ResponseData responseData = saleCouponService.delete(ids);
        return responseData;
    }

}
