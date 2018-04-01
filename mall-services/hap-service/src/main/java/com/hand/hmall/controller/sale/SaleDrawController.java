package com.hand.hmall.controller.sale;

import com.hand.hmall.client.sale.ISaleDrawClient;
import com.hand.hmall.dto.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by cw on 2017/2/28.
 */

@RestController
@RequestMapping(value = "/h/sale/draw", produces = { MediaType.APPLICATION_JSON_VALUE })
public class SaleDrawController {
    @Autowired
    private ISaleDrawClient saleDrawClient;

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public ResponseData query(@RequestBody Map<String, Object> map) {
        return saleDrawClient.query(map);
    }

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public ResponseData submit(@RequestBody Map<String, Object>map)
    {
        return saleDrawClient.submit(map);
    }

    @RequestMapping(value = "/startUsing", method = RequestMethod.POST)
    public ResponseData active(@RequestBody List<Map<String, Object>> maps)
    {
        return saleDrawClient.active(maps);
    }

    @RequestMapping(value = "/endUsing", method = RequestMethod.POST)
    public ResponseData inactive(@RequestBody List<Map<String, Object>> maps)
    {
        return saleDrawClient.inactive(maps);
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public ResponseData detail(@RequestParam("id") String id)
    {
        return saleDrawClient.detail(id);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseData delete(@RequestBody List<Map<String, Object>> maps)
    {
        return saleDrawClient.delete(maps);
    }

    @RequestMapping(value = "/awardPro", method = RequestMethod.POST)
    public ResponseData awardPro(@RequestBody Map<String, Object> map) {
        return saleDrawClient.awardPro(map);
    }

    @RequestMapping(value = "/submitAwardPro", method = RequestMethod.POST)
    public ResponseData submitAwardPro(@RequestBody List<Map<String, Object>> list) {
        return saleDrawClient.submitAwardPro(list);
    }

    @RequestMapping(value = "/startUsingAwardPro", method = RequestMethod.POST)
    public ResponseData startUsingAwardPro(@RequestBody Map<String, Object> map) {
        return saleDrawClient.startUsingAwardPro(map);
    }

    @RequestMapping(value = "/endUsingAwardPro", method = RequestMethod.POST)
    public ResponseData endUsingAwardPro(@RequestBody Map<String, Object> map) {
        return saleDrawClient.endUsingAwardPro(map);
    }

    @RequestMapping(value = "/getAward", method = RequestMethod.POST)
    public ResponseData getAward(@RequestBody Map<String, Object> map) {
        return saleDrawClient.getAward(map);
    }
}
