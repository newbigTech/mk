package com.hand.hmall.om.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.om.dto.OrderBk;
import com.hand.hmall.om.service.IOrderBkService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.hand.hmall.util.StringUtils.isNumeric;

/**
 * @Destription : 订单备份Controller类
 *
 * @Author : shoupeng.wei@hand-china.com
 * @Created at : 2017年8月14日9:00:51
 * @version : 1.0.0
 */
@Controller
public class OrderBkController extends BaseController {

    @Autowired
    private IOrderBkService service;

    /**
     * 订单快照查询
     *
     * @param maps
     * @return 订单列表集合
     */
    @RequestMapping(value = "/hmall/om/orderBk/query")
    @ResponseBody
    public ResponseData query(@RequestParam Map maps) {
        String str = (String) maps.get("data");
        JSONObject jsonObject = JSONObject.fromObject(str);
        JSONObject status = jsonObject.getJSONObject("status");
        //订单状态
        JSONArray orderStatus_ = status.getJSONArray("orderStatus_");
        String[] strOrderStatus = new String[orderStatus_.size()];
        for (int i = 0; i < orderStatus_.size(); i++) {
            strOrderStatus[i] = orderStatus_.get(i).toString();
        }
        //配送方式
        JSONArray distribution = status.getJSONArray("distribution");
        String[] strDistribution = new String[distribution.size()];
        for (int i = 0; i < distribution.size(); i++) {
            strDistribution[i] = distribution.get(i).toString();
        }
        //订单类型
        JSONArray orderTypes = status.getJSONArray("orderTypes__");
        String[] strOrderTypes = new String[orderTypes.size()];
        for (int i = 0; i < orderTypes.size(); i++) {
            strOrderTypes[i] = orderTypes.get(i).toString();
        }

        Map<String, Object> data = (Map<String, Object>) jsonObject.get("pages");
        //订单号
        String code = "";
        if (data.get("code") != null) {
            code = data.get("code").toString();
           /* if (!isNumeric(orderId)) {
                orderId = "";
            }*/
        }
        //用户Id
        String userId = "";
        if (data.get("userId") != null) {
            userId = data.get("userId").toString();
            if (!isNumeric(userId)) {
                userId = "";
            }
        }
        //订单是否锁定
        String locked = "";
        if (data.get("locked") != null) {
            locked = data.get("locked").toString();
        }
        String payRate = "";
        if (data.get("payRate") != null) {
            payRate = data.get("payRate").toString();
        }
        //收货人手机号
        String receiverMobile = "";
        if (data.get("receiverMobile") != null) {
            receiverMobile = data.get("receiverMobile").toString();
            if (!isNumeric(receiverMobile)) {
                receiverMobile = "";
            }
        }
        //开始时间
        String startTime = "";
        if (data.get("startTime") != null) {
            startTime = data.get("startTime").toString();
        }
        //结束时间
        String endTime = "";
        if (data.get("endTime") != null) {
            endTime = data.get("endTime").toString();
        }
        //变式物料号
        String vproduct = "";
        if (data.get("vproduct") != null) {
            vproduct = data.get("vproduct").toString();
        }
        String escOrderCode = "";
        if (data.get("escOrderCode") != null) {
            escOrderCode = data.get("escOrderCode").toString();
        }
        //商品编码
        String productId = "";
        if (data.get("productId") != null) {
            productId = data.get("productId").toString();
        }
        /*add by heng.zhang 20170922 MAG-1200  PIN码*/
        String pinCode = "";
        if (data.get("pinCode") != null) {
            pinCode = data.get("pinCode").toString();
        }
        /*end*/
        int page = 0;
        if (data.get("page") != null) {
            page = Integer.parseInt(data.get("page").toString());
        }
        int pagesize = 0;
        if (data.get("pagesize") != null) {
            pagesize = Integer.parseInt(data.get("pagesize").toString());
        }
        List<OrderBk> list = service.selectOrderBkList(page, pagesize, code, escOrderCode, userId, locked, receiverMobile, startTime, endTime, strOrderStatus, strDistribution, strOrderTypes, vproduct, productId, payRate, pinCode);
        return new ResponseData(list);
    }

    @RequestMapping(value = "/hmall/om/orderBk/queryInfo")
    @ResponseBody
    public ResponseData query(OrderBk dto) {
        return new ResponseData(service.selectInfoByOrderBkId(dto));
    }

    @RequestMapping(value = "/hmall/om/orderBk/queryByVersionAndCode")
    @ResponseBody
    public ResponseData queryByVersionAndCode(OrderBk dto) {
        ResponseData rd = new ResponseData(Arrays.asList(service.queryByVersionAndCode(dto)));
        return rd;
    }
}
