package com.hand.hmall.controller;

import com.alibaba.fastjson.JSON;
import com.hand.hmall.client.IPromoteClientService;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.menu.Status;
import com.hand.hmall.model.Coupon;
import com.hand.hmall.service.IRuleTempService;
import com.hand.hmall.service.ISaleCouponService;
import com.hand.hmall.temp.RuleInputTemp;
import com.hand.hmall.util.BeanMapExchange;
import com.hand.hmall.util.CouponCreateJarThread;
import com.hand.hmall.util.ResponseReturnUtil;
import com.hand.hmall.util.SaleCheckedLegalUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * hap优惠券管理界面后台入口
 * Created by shanks on 2017/1/5.
 */
@RestController
@RequestMapping(value = "/h/sale/coupon", produces = {MediaType.APPLICATION_JSON_VALUE})
public class SaleCouponController {

    @Autowired
    private IRuleTempService ruleTempService;
    @Autowired
    private ISaleCouponService saleCouponService;
    @Autowired
    private CouponCreateJarThread couponCreateJarThread;
    @Autowired
    private IPromoteClientService promoteClientService;

    private Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);


    /**
     * 查询优惠券列表 供MAP调用
     *
     * @param map
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData query(@RequestBody Map<String, Object> map) throws ParseException {

        return saleCouponService.query(map);

    }

    @RequestMapping(value = "/queryActivity", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData queryActivity(@RequestBody Map<String, Object> map) throws ParseException {

        return saleCouponService.queryActivity(map);

    }

    /**
     * 查询范围外的优惠券
     *
     * @param map
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/queryByNotIn", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData queryByNotIn(@RequestBody Map<String, Object> map) throws ParseException {

        return saleCouponService.queryByNotIn(map);

    }


    /**
     * 根据优惠码 查询isUsing为Y 活动中，未生效，已失效的优惠券
     *
     * @param couponCode
     * @return
     */
    @RequestMapping(value = "/queryByCode/{couponCode}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData queryByCode(@PathVariable("couponCode") String couponCode) {

        return saleCouponService.selectByCode(couponCode);

    }

    /**
     * 根据优惠码 查询isUsing为Y 活动中，未生效的优惠券
     *
     * @param couponCode
     * @return
     */
    @RequestMapping(value = "/queryByCodeCanUse/{couponCode}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData queryByCodeCanUse(@PathVariable("couponCode") String couponCode) {

        return saleCouponService.selectByCodeCanUse(couponCode);

    }

    /**
     * 根据优惠券主键ID查询优惠券
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/selectById", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData selectById(@RequestParam("id") String id) {

        return saleCouponService.selectById(id);

    }

    @RequestMapping(value = "/selectByCouponId", method = RequestMethod.GET)
    public ResponseData selectByCouponId(@RequestParam("couponId") String couponId) {
        return saleCouponService.selectByCouponId(couponId);
    }

    @RequestMapping(value = "/selectCouponIdById", method = RequestMethod.GET)
    public ResponseData selectCouponIdById(@RequestParam("id") String id) {
        return saleCouponService.selectCouponIdById(id);
    }


    /**
     * hap创建优惠券入口
     *
     * @param map
     * @return
     * @throws ParseException
     * @throws IOException
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData submit(@RequestBody Map<String, Object> map) throws ParseException, IOException {
        logger.info("----create coupon----{}", JSON.toJSONString(map));
        List<String> checked = saleCouponService.checkedCoupon((Map<String, Object>) map.get("coupon"));
        List<String> conditionActionsChecked = SaleCheckedLegalUtil.checkedSaleConditionAction(map);

        ResponseData returnCheckedMessage = SaleCheckedLegalUtil.returnCheckedMessage(checked, conditionActionsChecked);
        if (!returnCheckedMessage.isSuccess()) {
            return returnCheckedMessage;
        }

        RuleInputTemp ruleInputTemp = new RuleInputTemp();
        Map<String, Object> couponGet = (Map<String, Object>) map.get("coupon");


        if (couponGet != null) {
            Coupon coupon = new Coupon();
            BeanMapExchange.mapToBean(couponGet, coupon);
            ruleInputTemp.setCoupon(coupon);
        }


        BeanMapExchange.exchangeConditionAction(map, ruleInputTemp);
        logger.info("------------------------------------ruleInputTemp -----------------------------\n{}", JSON.toJSONString(ruleInputTemp));
        //存储优惠券信息
        ResponseData responseData = saleCouponService.submit(map);

        //存储规则文件信息
        if (responseData.isSuccess()) try {
            Map<String, Object> couponMap = (Map<String, Object>) responseData.getResp().get(0);
            ruleInputTemp.getCoupon().setId(couponMap.get("id").toString());
            ResponseData ruleResp = ruleTempService.createRule(ruleInputTemp);
            if (!ruleResp.isSuccess()) {
                saleCouponService.delete((List<Map<String, Object>>) responseData.getResp());
                return ruleResp;
            }
            if (couponMap.get("status").equals(Status.ACTIVITY.getValue())) {
                couponCreateJarThread.setId(couponMap.get("id").toString());
                new Thread(couponCreateJarThread).start();
            }
        } catch (NullPointerException e) {
            //回滚，如果生成规则文件失败就删除附带的优惠券
            e.printStackTrace();
            saleCouponService.deleteReal((List<Map<String, Object>>) responseData.getResp());
            return ResponseReturnUtil.returnFalseResponse("缺少条件结果数据", "NULL_POINTER");
        } catch (IllegalArgumentException | ClassCastException e) {
            //回滚，如果生成规则文件失败就删除附带的优惠券
            e.printStackTrace();
            saleCouponService.deleteReal((List<Map<String, Object>>) responseData.getResp());
            return ResponseReturnUtil.returnFalseResponse("条件结果数据错误", "NULL_POINTER");
        } catch (InvocationTargetException | IllegalAccessException e) {
            //回滚，如果生成规则文件失败就删除附带的优惠券
            e.printStackTrace();
            saleCouponService.deleteReal((List<Map<String, Object>>) responseData.getResp());
            return ResponseReturnUtil.returnFalseResponse("系统错误", "NULL_POINTER");
        }

        return responseData;
    }

    /**
     *启用优惠券
     *
     * @param maps
     * @return
     */
    @RequestMapping(value = "/startUsing", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData startUsing(@RequestBody List<Map<String, Object>> maps) {

        ResponseData responseData = new ResponseData();
        List<String> errorList = new ArrayList<>();

        for (Map<String, Object> map : maps) {

            ResponseData resp = ruleTempService.releaseCoupon(map.get("id").toString());

            if (!resp.isSuccess()) {
                errorList.add(resp.getMsg());
                logger.debug(responseData.getMsg());
                return responseData;
            } else {
                saleCouponService.startUsing(map);
                promoteClientService.startUsingCoupon(map.get("id").toString());
            }

        }
        if (!errorList.isEmpty()) {
            responseData.setSuccess(false);
            responseData.setResp(errorList);
        }
        return responseData;
    }

    /**
     *停用优惠券
     *
     * @param maps
     * @return
     */
    @RequestMapping(value = "/endUsing", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData endUsing(@RequestBody List<Map<String, Object>> maps) {

        for (Map<String, Object> map : maps) {
            try {
                ruleTempService.removeCoupon(map.get("id").toString());
                saleCouponService.endUsing(map);
                promoteClientService.setInvalidByCid(map.get("id").toString());
            } catch (Exception e) {
                e.printStackTrace();
                String s = "【" + map.get("couponCode") + "】" + map.get("couponName") + "禁用失败";
                return ResponseReturnUtil.returnFalseResponse(s, "ERROR_RULE");
            }
        }
        return new ResponseData(maps);

    }

    /**
     *查看优惠券详情
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData detail(@RequestParam("id") String id) {
        return new ResponseData(saleCouponService.selectCouponDetail(id));

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

        ResponseData responseData = saleCouponService.delete(maps);
        if (responseData.isSuccess()) {
            List<Map<String, Object>> mapList = (List<Map<String, Object>>) responseData.getResp();
            for (Map<String, Object> map : mapList) {
                ruleTempService.removeCoupon(map.get("id").toString());
                promoteClientService.deleteByCid(map.get("id").toString());
            }
        }
        return responseData;
    }


}
