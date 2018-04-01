package com.hand.hmall.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hand.hmall.code.MessageCode;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.logistics.exception.ImportLogisticsException;
import com.hand.hmall.logistics.service.ILogisticsService4MS;
import com.hand.hmall.logistics.trans.WmsLogisticsInfo;
import com.hand.hmall.util.SecretKey;
import com.hand.hmall.util.ThreadLogger;
import com.markor.map.framework.common.exception.DataProcessException;
import com.markor.map.logistics.entities.ConsignmentInfo;
import com.markor.map.logistics.entities.ConsignmentToRRS;
import com.markor.map.logistics.service.ILogisticsService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 唐磊
 * @version 0.1
 * @name:LogisticsController
 * @Description: 物流信息相关controller
 * @date 2017/8/10 14:08
 */
@RestController
@RequestMapping("/i/consignment")
public class LogisticsController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ILogisticsService logisticsService;

    @Autowired
    private ILogisticsService4MS logisticsService4MS;

    /**
     * 第三方物流信息推送中台(回传HFS订单状态接口)
     *
     * @return ResponseData
     */
    @RequestMapping(value = "/rrsOrderHfs", method = RequestMethod.POST)
    @ResponseBody
    public Map rrsOrderHfs(HttpServletRequest request) {
        // 获取请求体
        String reqBody = "";
        // 返回结果
        Map maps = new HashMap<String, Object>();
        try {
            BufferedReader br = request.getReader();
            String str = "";
            while ((str = br.readLine()) != null) {
                reqBody += str;
            }
        } catch (Exception e) {
            maps.put("code", 500);
            maps.put("msg", MessageCode.UR_LOGIN_ERROR_01.getValue());
            return maps;
        }
        // 获取header中的sign type
        String sign_type = request.getHeader("sign-type");
        // 获取header中的sign
        String sign = request.getHeader("sign");
        // 验证签名  错误提示异常
        if ("MD5".equals(sign_type)) {
            if (!StringUtils.isEmpty(sign)) {
//                String newSign = Auth.md5(SecretKey.KEY + reqBody);
                String newSign = new BASE64Encoder().encode(DigestUtils.md5Hex(reqBody + SecretKey.KEY).getBytes());
                if (sign.equals(newSign)) {
                    // 将请求体装入Pojo
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        ConsignmentInfo consignmentInfo = objectMapper.readValue(reqBody, ConsignmentInfo.class);
                        logisticsService.saveAndSendTradeMessage(consignmentInfo);
                        maps.put("code",200);
                        maps.put("msg",MessageCode.UR_LOGIN_SUCCESS_200.getValue());
                        return maps;
                    } catch (Exception e) {
                        maps.put("code", 500);
                        maps.put("msg", MessageCode.UR_LOGIN_ERROR_05.getValue());
                        return maps;
                    }
                } else {
                    maps.put("code", 500);
                    maps.put("msg", MessageCode.UR_LOGIN_ERROR_04.getValue());
                    return maps;
                }
            } else {
                maps.put("code", 500);
                maps.put("msg", MessageCode.UR_LOGIN_ERROR_03.getValue());
                return maps;
            }
        } else {
            maps.put("code", 500);
            maps.put("msg", MessageCode.UR_LOGIN_ERROR_02.getValue());
            return maps;
        }
    }

    /**
     * 批量导入WMS物流数据
     *
     * @param logisticsInfo - WMS物流数据数组
     * @return
     */
    @PostMapping(value = "/wmsLogistics/import")
    public ResponseData batchImport(@RequestBody List<WmsLogisticsInfo> logisticsInfo) {

        ThreadLogger threadLogger = new ThreadLogger();
        threadLogger.append("开始导入物流数据:" + logisticsInfo.toString());

        // 数据导入失败原因
        Map<String, String> failInfo = new HashMap<>();

        try {
            logisticsService4MS.batchImport(logisticsInfo);
            threadLogger.append("logisticsService.batchImport()方法执行成功");
        } catch (ImportLogisticsException e) {
            failInfo.put(e.getMsgCode(), e.getMessage());
            threadLogger.append("ImportLogisticsException: CODE:" + e.getMsgCode() + ", MSG:" + e.getMessage());
        } catch (RuntimeException e) {
            failInfo.put("interface.program.error", "接口程序错误");
            e.printStackTrace();
            threadLogger.append("RuntimeException: CODE: interface.program.error, MSG: 接口程序错误");
        }

        if (failInfo.isEmpty()) {
            threadLogger.append("success");
        } else {
            threadLogger.append("failed");
        }

        logger.info(threadLogger.toString());

        ResponseData response = new ResponseData(failInfo.isEmpty() ? true : false);
        response.setResp(Arrays.asList(failInfo));
        return response;
    }

    @RequestMapping("/sendConsignmentToRRS")
    public List<ResponseData> sendConsignmentToRRS(@RequestBody List<ConsignmentToRRS> list){
        try {
            logisticsService.saveAndSendConsignmentToRRS(list);
        } catch (DataProcessException e) {
            logger.error(e.getMessage(), e);
            ResponseData rpd = new ResponseData(false);
            rpd.setMsg(e.getMessage());
            return Arrays.asList(rpd);
        }
        return Arrays.asList(new ResponseData(true));
    }

    @RequestMapping("/queryTradeByServiceCode")
    public List<ConsignmentInfo> queryTradeByServiceCode(@RequestBody ConsignmentInfo consignmentInfo){
        return logisticsService.queryTradeByServiceCode(consignmentInfo);
    }

}
