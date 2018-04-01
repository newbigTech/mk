package com.hand.hmall.pin.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hand.common.util.Auth;
import com.hand.common.util.SecretKey;
import com.hand.common.util.ThreadLogger;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.om.dto.OrderEntry;
import com.hand.hmall.pin.dto.Alter;
import com.hand.hmall.pin.dto.Pin;
import com.hand.hmall.pin.dto.PinInfo4SendZmall;
import com.hand.hmall.pin.mapper.PinMapper;
import com.hand.hmall.pin.service.IAlterService;
import com.hand.hmall.pin.service.IPinAlterInfoService;
import com.hand.hmall.pin.service.IPinService;
import com.hand.hmall.util.Constants;
import com.markor.map.framework.restclient.RestClient;
import net.sf.json.JSONObject;
import okhttp3.Response;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenzhigang
 * @version 0.1
 * @name PinServiceImpl
 * @description PIN码信息服务接口实现类
 * @date 2017/8/4
 */
@Service
public class PinServiceImpl extends BaseServiceImpl<Pin> implements IPinService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PinMapper pinMapper;

    @Autowired
    private RestClient restClient;

    @Autowired
    private IAlterService iAlterService;

    @Autowired
    private IPinAlterInfoService iPinAlterInfoService;

    /**
     * 根据code查询PIN码
     *
     * @param code - PIN码
     * @return
     */
    @Override
    public List<Pin> queryByCode(String code) {
        return pinMapper.queryByCode(code);
    }


    /**
     * 查询全部非同步状态PIN码
     *
     * @return
     */
    @Override
    public List<PinInfo4SendZmall> queryNotSyncPinInfo() {
        return pinMapper.queryNotSyncPinInfo();
    }

    @Override
    public void updateSynvZmallFlag(boolean syncFlag) {
        pinMapper.updateSynvZmallFlag(syncFlag ? "Y" : "N");
    }

    @Override
    public Map sendPin2ZMall() {

        Map result = new HashMap();

        ThreadLogger threadLogger = new ThreadLogger();
        threadLogger.append("推送PIN码到商城: begin");

        List<PinInfo4SendZmall> notSyncPinInfo = queryNotSyncPinInfo();

        Map<String, String> contentType = new HashMap<>();
        contentType.put("Content-Type", "application/json");

        Response response;
        JSONObject jsonResult;
        StringWriter sw = new StringWriter();
        try {
            new ObjectMapper().writeValue(sw, notSyncPinInfo);
        } catch (IOException e) {
            threadLogger.append("PIN码数据实体转换JSON格式报错。此次job执行异常退出。");
            logger.info(threadLogger.toString());
            throw new RuntimeException("PIN码数据实体转换JSON格式报错。此次job执行异常退出。", e);
        }

        threadLogger.append("开始推送PIN: start");

        try {
            String url = "/zmallsync/pinInfo?token=" + Auth.md5(SecretKey.KEY + sw.toString());

            result.put("推送到商城接口", url);
            result.put("推送PIN码列表", notSyncPinInfo);

            response = restClient.postString(Constants.ZMALL, url, sw.toString(), "json", new HashMap<>(), contentType);
            threadLogger.append("结束推送PIN: end");
        } catch (IOException e) {
            threadLogger.append("推送PIN时调用Zmall接口报错。此次job执行异常退出。");
            logger.info(threadLogger.toString());
            throw new RuntimeException("推送PIN时调用Zmall接口报错。此次job执行异常退出。", e);
        }


        try {

            jsonResult = JSONObject.fromObject(response.body().string());

            String zMallExeResult = "此次job执行结果 code: " + jsonResult.get("code") + ", message: " + jsonResult.get("message")
                    + ", resp: " + jsonResult.get("resp") + ", total: " + jsonResult.get("total");
            threadLogger.append(zMallExeResult);

            Map zMallExeResultMap = new HashMap();
            zMallExeResultMap.put("code", jsonResult.get("code") + ".");
            zMallExeResultMap.put("message", jsonResult.get("message") + ".");
            zMallExeResultMap.put("resp", jsonResult.get("resp") + ".");
            zMallExeResultMap.put("total", jsonResult.get("total") + ".");
            result.put("执行结果", zMallExeResultMap);

        } catch (IOException e) {
            threadLogger.append("将Response字符串转换成JSON格式对象时报错。此次job执行异常退出。");
            logger.info(threadLogger.toString());
            throw new RuntimeException("将Response字符串转换成JSON格式对象时报错。此次job执行异常退出。", e);
        }

        if ("S".equalsIgnoreCase(jsonResult.get("code").toString())) {
            // 将Zmall同步标志位更新为Y
            updateSynvZmallFlag(true);
            threadLogger.append("将Zmall同步标志位更新为Y, 完成。");
            threadLogger.append("推送PIN码到商城: success");
        } else {
            threadLogger.append("PIN码推送不成功, 不更新Zmall同步标志位。");
            threadLogger.append("推送PIN码到商城: failed");
        }

        logger.info(threadLogger.toString());
        threadLogger.clean();

        return result;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void savePinInfos(List<OrderEntry> orderEntries, String approvedBy, String eventCode, String eventInfo) {
        if (CollectionUtils.isEmpty(orderEntries)) {
            return ;
        }
        for (OrderEntry orderEntry : orderEntries) {
            Pin pin = new Pin();
            pin.setCode(orderEntry.getPin());
            pin.setEntryCode(orderEntry.getCode());
            Assert.notNull(orderEntry.getCode(), "订单行[" + orderEntry.getOrderEntryId() + "]编码code不能为空");
            pin.setEventCode(eventCode);
            Alter alter = iAlterService.selectByEventCode(eventCode);
            if (alter != null) {
                pin.setEventDes(alter.getEventDes());
            }
            pin.setOperator(approvedBy);
            pin.setSystem(Constants.SOURCE_PLATFORM_HAMLL);
            pin.setOperationTime(new Date());
            pin.setEventInfo(eventInfo);
            this.insertSelective(RequestHelper.newEmptyRequest(), pin);
            try {
                iPinAlterInfoService.insertPinAlterInfo(pin);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
