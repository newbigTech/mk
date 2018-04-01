package com.hand.hmall.controller;

import com.hand.hmall.client.IPromoteClientService;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.service.ISaleDrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cw on 2017/2/28.
 * 抽奖活动管理controller 未使用
 */

@RestController
@RequestMapping(value = "/h/sale/draw", produces = { MediaType.APPLICATION_JSON_VALUE })
public class SaleDrawController {
    @Autowired
    private ISaleDrawService saleDrawService;

    @Autowired
    private IPromoteClientService promoteClientService;

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public ResponseData query(@RequestBody Map<String, Object> map) {
        return saleDrawService.query(map);
    }


    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public ResponseData submit(@RequestBody Map<String, Object> map) throws ParseException {
        return saleDrawService.submit(map);
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public ResponseData detail(@RequestParam("id") String id) {
        return new ResponseData(saleDrawService.selectDrawDetail(id));
    }

    @RequestMapping(value = "/startUsing", method = RequestMethod.POST)
    public ResponseData active(@RequestBody List<Map<String, Object>> maps)  {
        return saleDrawService.active(maps);
    }

    @RequestMapping(value = "/endUsing", method = RequestMethod.POST)
    public ResponseData inactive(@RequestBody List<Map<String, Object>> maps)  {
        return saleDrawService.inactive(maps);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseData delete(@RequestBody List<Map<String, Object>> maps)  {
        return saleDrawService.delete(maps);
    }

    @RequestMapping(value = "/awardPro", method = RequestMethod.POST)
    public ResponseData awardPro(@RequestBody Map<String, Object> map) {
        return saleDrawService.awardPro(map);
    }

    @RequestMapping(value = "/submitAwardPro", method = RequestMethod.POST)
    public ResponseData submitAwardPro(@RequestBody List<Map<String, Object>> list) {
        return saleDrawService.submitAwardPro(list);
    }

    @RequestMapping(value = "/startUsingAwardPro", method = RequestMethod.POST)
    public ResponseData startUsingAwardPro(@RequestBody Map<String, Object> map) {
        return saleDrawService.startUsingAwardPro(map);
    }

    @RequestMapping(value = "/endUsingAwardPro", method = RequestMethod.POST)
    public ResponseData endUsingAwardPro(@RequestBody Map<String, Object> map) {
        return saleDrawService.endUsingAwardPro(map);
    }

    @RequestMapping(value = "/getAwardpro/{drawId}", method = RequestMethod.GET)
    public ResponseData getAwardpro(@PathVariable("drawId") String drawId) {
        return saleDrawService.getAwardpro(drawId);
    }

    @RequestMapping(value = "/addAwardRecord", method = RequestMethod.POST)
    public ResponseData addAwardRecord(@RequestBody Map<String, Object> map) {
        String couponId = (String) map.get("couponId");
        Map<String, Object> award = new HashMap<>();
        if ("".equals(couponId)) {
            award.put("drawId", map.get("drawId"));
            award.put("couponId", couponId);
        } else {
            if(promoteClientService.convertByDraw(map).isSuccess()) {
                award.put("awardStatus", "ISSUED");
            } else {
                award.put("awardStatus", "UNISSUED");
            }
            List<Map<String,Object>> convertData = (List<Map<String, Object>>) map.get("convertData");
            award.put("couponName", map.get("couponName"));
            award.put("drawId", map.get("drawId"));
            award.put("mobileNumber", convertData.get(0).get("mobileNumber"));
            award.put("clientName", convertData.get(0).get("name"));
        }
        return saleDrawService.addAwardRecord(award);
    }

    @RequestMapping(value = "/getAward", method = RequestMethod.POST)
    public ResponseData getAward(@RequestBody Map<String, Object> map) {
        return saleDrawService.getAward(map);
    }
}
