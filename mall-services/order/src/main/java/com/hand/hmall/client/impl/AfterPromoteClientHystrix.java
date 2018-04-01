package com.hand.hmall.client.impl;

import com.hand.hmall.client.AfterPromoteClient;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.model.HmallOmOrder;
import org.springframework.stereotype.Service;

/**
 * @author 李伟
 * @version 1.0
 * @name:AfterPromoteClientHystrix
 * @Description: 订单下载返回事后促销信息 实现类
 * @date 2017/10/18 16:09
 */
@Service
public class AfterPromoteClientHystrix implements AfterPromoteClient {

    @Override
    public ResponseData checkAfterPromote(HmallOmOrder order){
        return new ResponseData(false);
    }

    @Override
    public ResponseData checkAfterPromoteWithCofrim(HmallOmOrder order){
        return new ResponseData(false);
    }
}
