package com.hand.hmall.om.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.hand.hmall.util.Constants;
import com.markor.map.framework.restclient.RestClient;
import com.hand.common.util.Auth;
import com.hand.common.util.SecretKey;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.om.dto.TradeTrace;
import com.hand.hmall.om.dto.TradeTraceToZmall;
import com.hand.hmall.om.mapper.TradeTraceMapper;
import com.hand.hmall.om.service.ITradeTraceService;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xuxiaoxue
 * @version 0.1
 * @name TradeTraceServiceImpl
 * @description 物流跟踪表 Service实现类
 * @date 2017/8/11
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TradeTraceServiceImpl extends BaseServiceImpl<TradeTrace> implements ITradeTraceService {
    @Autowired
    RestClient restClient;
    @Autowired
    TradeTraceMapper mapper;

    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> sendToZmall() throws Exception {
        List<TradeTraceToZmall> result = mapper.queryForZmall();
        if (result.size() > 0) {
            StringWriter stringWriter = new StringWriter();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(stringWriter, result);
            Response response = restClient.postString(Constants.ZMALL, "/zmallsync/logisticsInfo?token=" + Auth.md5(SecretKey.KEY + stringWriter.toString()),
                    stringWriter.toString(), "application/json", new HashMap<>(), new HashMap<>());
            Map map = objectMapper.readValue(response.body().string(), Map.class);
            map.put("dataJson", stringWriter.toString());
            if ("S".equals(map.get("code"))) {
                mapper.updateSyncFlag(result);
            }
            return map;
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("code", "S");
            map.put("message", "没有可同步的物流信息。");
            return map;
        }
    }

    @Override
    public List <TradeTrace> getTradeTraceInfo(IRequest request, TradeTrace dto, int page, int pagesize) {
        PageHelper.startPage(page, pagesize);
        return mapper.getTradeTraceInfo(dto);
    }

    @Override
    public List <TradeTrace> getTradeTraceInfoByDelivery(IRequest request, TradeTrace dto, int page, int pagesize) {
        PageHelper.startPage(page, pagesize);
        return mapper.getTradeTraceInfoByDelivery(dto);
    }
}