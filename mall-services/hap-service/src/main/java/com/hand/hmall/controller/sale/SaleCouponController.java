package com.hand.hmall.controller.sale;

import com.hand.hmall.client.IPromoteClient;
import com.hand.hmall.client.sale.ISaleCouponClient;
import com.hand.hmall.client.sale.ISaleRangeClient;
import com.hand.hmall.dto.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by shanks on 2017/2/15.
 */
@RestController
@RequestMapping(value = "/h/sale/coupon", produces = { MediaType.APPLICATION_JSON_VALUE })
public class SaleCouponController {
    @Autowired
    private ISaleCouponClient saleCouponClient;
    @Autowired
    private ISaleRangeClient saleRangeClient;
    @Autowired
    private IPromoteClient promoteClient;

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public ResponseData query(@RequestBody Map<String, Object> maps)
    {

        return saleCouponClient.query(maps);
    }

    @RequestMapping(value = "/queryActivity", method = RequestMethod.POST)
    public ResponseData queryActivity(@RequestBody Map<String, Object> maps)
    {
        return saleCouponClient.queryActivity(maps);
    }

    @RequestMapping(value = "/queryByNotIn", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData queryByNotIn(@RequestBody Map<String, Object> maps)
    {
        return saleCouponClient.queryByNotIn(maps);
    }

    @RequestMapping(value = "/submitCountNumber" , method = RequestMethod.POST)
    @ResponseBody
    public ResponseData submitCountNumber(@RequestBody List<Map<String,Object>> maps)
    {
        return promoteClient.checkedCount(maps);
    }

    @RequestMapping(value = "/submit" , method = RequestMethod.POST)
    @ResponseBody
    public ResponseData submit(@RequestBody Map<String,Object> map)
    {
        return saleCouponClient.submit(map);
    }

    @RequestMapping(value = "/startUsing" , method = RequestMethod.POST)
    @ResponseBody
    public ResponseData startUsing(@RequestBody List<Map<String,Object>> datas)
    {
        return  saleCouponClient.startUsing(datas);
    }

    @RequestMapping(value = "/endUsing" , method = RequestMethod.POST)
    @ResponseBody
    public ResponseData endUsing( @RequestBody List<Map<String,Object>> datas)
    {
        return saleCouponClient.endUsing(datas);
    }

    @RequestMapping(value = "/detail" , method = RequestMethod.GET)
    @ResponseBody
    public ResponseData detail(@RequestParam("id")String id)
    {
        return saleCouponClient.detail(id);
    }

    @RequestMapping(value = "/delete" , method = RequestMethod.POST)
    @ResponseBody
    public ResponseData delete(@RequestBody List<Map<String,Object>> maps)
    {
        return saleCouponClient.delete(maps);
    }

    @RequestMapping(value = "/convert/admin" , method = RequestMethod.POST)
    @ResponseBody
    public ResponseData convertByAdmin(@RequestBody Map<String,Object> convertMap){
        return promoteClient.convertByAdmin(convertMap);
    }



}
