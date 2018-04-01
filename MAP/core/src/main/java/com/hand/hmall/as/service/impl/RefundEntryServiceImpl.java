package com.hand.hmall.as.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.as.dto.AsRefundinfo;
import com.hand.hmall.as.dto.RefundEntry;
import com.hand.hmall.as.mapper.AsRefundinfoMapper;
import com.hand.hmall.as.mapper.RefundEntryMapper;
import com.hand.hmall.as.service.IRefundEntryService;
import com.hand.hmall.om.dto.Paymentinfo;
import com.hand.hmall.om.mapper.PaymentinfoMapper;
import com.hand.hmall.util.Constants;
import com.markor.map.framework.restclient.RestClient;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * author: zhangzilong
 * name: RefundEntryServiceImpl.java
 * discription: 退款单行Service实现类
 * date: 2017/8/7
 * version: 0.1
 */
@Service
@Transactional
public class RefundEntryServiceImpl extends BaseServiceImpl<RefundEntry> implements IRefundEntryService {

    @Autowired
    private RefundEntryMapper refundEntryMapper;

    @Autowired
    private RestClient restClient;

    @Autowired
    private PaymentinfoMapper paymentinfoMapper;

    @Autowired
    private AsRefundinfoMapper asRefundinfoMapper;

    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public HashMap sendToHPAY(RefundEntry dto) throws Exception {

        dto = refundEntryMapper.queryForHPAY(dto);
        if (dto.getPayAmount() == null){
            HashMap map = new HashMap();
            map.put("success",false);
            map.put("msg","退款金额为空，请先填写退款金额，保存后再执行退款。");
            return map;
        }
        StringWriter stringWriter = new StringWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        // 将DTO转换为接口需要的字段
        objectMapper.writeValue(stringWriter, dto.toInterface());
        String str = stringWriter.toString();
        Response response = restClient.postString(Constants.HPAY, "/hpay/v1/Refund", str, "application/json", new HashMap<String, String>(), new HashMap<String, String>());
        HashMap resultMap = objectMapper.readValue(response.body().string(), HashMap.class);
        if ((Boolean)resultMap.get("success")){
            Paymentinfo paymentinfo = new Paymentinfo();
            paymentinfo.setPaymentinfoId(dto.getPaymentinfoId());
            paymentinfo.setRefundAmount(BigDecimal.valueOf(dto.getPayAmount()));
            paymentinfoMapper.updateRefundAmount(paymentinfo);
            refundEntryMapper.updatePayStatus(dto);
            AsRefundinfo ard = new AsRefundinfo();
            HashMap<String,String> map = ((HashMap<String,String>)((ArrayList)resultMap.get("resp")).get(0));
            ard.setPayMode(map.get("mode"));
            ard.setRequestSum(BigDecimal.valueOf(dto.getPayAmount()));
            ard.setRefundTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(map.get("tradePayTime")));
            ard.setNumberCode(map.get("tradeNo"));
            ard.setAsRefundEntryId(dto.getAsRefundEntryId());
            ard.setOutTradeNo(map.get("outTradeNo"));
            ard.setActualSum(new BigDecimal(Integer.valueOf(map.getOrDefault("buyerPayAmount","0"))).divide(new BigDecimal(100)));
            asRefundinfoMapper.insertSelective(ard);
        }
        return resultMap;
    }

    @Override
    public List<RefundEntry> selectRefundOrderEntry(IRequest iRequest, Long asRefundId, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return refundEntryMapper.selectRefundOrderEntry(asRefundId);
    }

    @Override
    public int updateRefundEntry(RefundEntry dto) {
        return refundEntryMapper.updateByPrimaryKeySelective(dto);
    }
}
