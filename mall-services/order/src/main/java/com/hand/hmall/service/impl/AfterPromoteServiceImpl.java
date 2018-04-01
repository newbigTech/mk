package com.hand.hmall.service.impl;

import com.hand.hmall.client.AfterPromoteClient;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.model.HmallOmOrder;
import com.hand.hmall.model.HmallOmOrderEntry;
import com.hand.hmall.service.IAfterPromoteService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 李伟
 * @version 1.0
 * @name:AfterPromoteServiceImpl
 * @Description: 事后促销的返回信息
 * @date 2017/10/18 16:26
 */
@Service
public class AfterPromoteServiceImpl implements IAfterPromoteService
{

    @Autowired
    private AfterPromoteClient afterPromoteClient;

    @Override
    public ResponseData checkAfterPromote(HmallOmOrder order)
    {
        ResponseData responseData = afterPromoteClient.checkAfterPromote(order);
        return responseData;
    }

    @Override
    public  ResponseData checkAfterPromoteWithCofrim(HmallOmOrder order)
    {
        ResponseData responseData = afterPromoteClient.checkAfterPromoteWithCofrim(order);
        return responseData;
    }

    @Override
    public Map returnAfterPromoteAndPinInfo(HmallOmOrder order, ResponseData responseData)
    {

        //返回的resp信息
        Map<String, Object> respMap = new HashMap();

        if(responseData.isSuccess() && CollectionUtils.isNotEmpty(responseData.getResp()))
        {
            //事后促销ID
            int promoId  =  (Integer) ((Map<String, Object>) responseData.getResp().get(0)).get("promoId");
            //记录ID
            int recordId  =  (Integer) ((Map<String, Object>) responseData.getResp().get(0)).get("recordId");
            //编码
            String code  =  (String) ((Map<String, Object>) responseData.getResp().get(0)).get("code");
            //描述
            String description  =  (String) ((Map<String, Object>) responseData.getResp().get(0)).get("description");
            //获取资格提示信息
            String info  =  (String) ((Map<String, Object>) responseData.getResp().get(0)).get("info");
            //名称
            String name  =  (String) ((Map<String, Object>) responseData.getResp().get(0)).get("name");
            //userStatus
            String userStatus =  (String) ((Map<String, Object>) responseData.getResp().get(0)).get("userStatus");

            respMap.put("promoId",promoId);
            respMap.put("recordId",recordId);
            respMap.put("code",code);
            respMap.put("description",description);
            respMap.put("info",info);
            respMap.put("name",name);
            respMap.put("userStatus",userStatus);
        }

        return respMap;
    }
}
