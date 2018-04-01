package com.hand.hmall.as.service.impl;

import com.github.pagehelper.PageHelper;
import com.markor.map.framework.soapclient.exceptions.WSCallException;
import com.hand.hap.core.IRequest;
import com.hand.hmall.ws.client.IRefundInfoPushClient;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.as.dto.AsRefundinfo;
import com.hand.hmall.as.mapper.AsRefundinfoMapper;
import com.hand.hmall.as.service.IAsRefundinfoService;
import com.hand.hmall.log.service.ILogManagerService;
import com.hand.hmall.util.Constants;
import com.hand.hmall.util.DateUtil;
import com.hand.hmall.ws.entities.RefundInfoRequestBody;
import com.hand.hmall.ws.entities.RefundInfoResponseBody;
import com.hand.hmall.ws.entities.ltItem;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * @author shoupeng.wei@hang-china.com
 * @version 0.1
 * @name OrderSyncStore
 * @description 退款信息推送Retail
 * @date 2017年8月1日20:30:22
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AsRefundinfoServiceImpl extends BaseServiceImpl<AsRefundinfo> implements IAsRefundinfoService{

    private static final String BUKRS = "0201"; // 公司代码
    private static final String ZKUNNR = "9520"; // 用户代码
    private static final String SUCCESS = "保存成功";
    private static final String ERROR = "保存失败";

    @Autowired
    private ILogManagerService logManagerService;

    @Autowired
    private IRefundInfoPushClient iRefundInfoPushClient;

    @Autowired
    private AsRefundinfoMapper refundinfoMapper;

    @Override
    public List<AsRefundinfo> getInfoForBalance(IRequest request, AsRefundinfo dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return refundinfoMapper.getInfoForBalance(dto);
    }

    /**
     * 退款信息推送Retail,将退款标识为 N 的退款信息推送到 retail，如果推送成功，将标识字段 refundSyncflag 置为 Y
     *
     * @param refundinfoList   要推送的退款单信息
     * @throws WSCallException   接口调用抛出的异常信息
     */
    @Override
    public void sendInfoToRetail(List<AsRefundinfo> refundinfoList) throws WSCallException {
        if(CollectionUtils.isEmpty(refundinfoList)){
            return;
        }
        //接口请求的信息实体类
        RefundInfoRequestBody refundInfoRequestBody = new RefundInfoRequestBody();
        ltItem ltItem = new ltItem();
        List<ltItem.refundEntryItem> itemList = new ArrayList<>();
        //封装退款单信息
        for(AsRefundinfo refundinfo:refundinfoList){
            ltItem.refundEntryItem item = new ltItem.refundEntryItem();
            item.setZSHDDH(refundinfo.getNumberCode());
            item.setZCKH(refundinfo.getRefundinfoId());
            item.setZYWLX("退款");
            item.setZZFLX(refundinfo.getPayMode());
            item.setBUKRS(BUKRS);
            item.setZKUNNR(ZKUNNR);
            item.setEXORD(refundinfo.getEscOrderCode());
            item.setZJYRQ(DateUtil.getdate(refundinfo.getRefundTime(), "yyyy-MM-dd"));
            item.setZJYSJ(DateUtil.getdate(refundinfo.getRefundTime(), "HH:mm:ss"));
            item.setZZJE(refundinfo.getActualSum().toString());
            item.setZY06(refundinfo.getOutTradeNo());
            item.setZY09("X");
            item.setZY01(refundinfo.getOrderType());
            itemList.add(item);
        }
        ltItem.setItems(itemList);
        refundInfoRequestBody.setLtItem(ltItem);

        RefundInfoResponseBody responseBody = iRefundInfoPushClient.paymentPush(refundInfoRequestBody);

        //退款单信息请求成功后，更新退款标识为 Y
        if (SUCCESS.equals(responseBody.getlMsg())) {
            for(AsRefundinfo refundinfo:refundinfoList){
                refundinfo.setRefundFlag(Constants.YES);
                this.updateByPrimaryKeySelective(RequestHelper.newEmptyRequest(), refundinfo);
            }
            logManagerService.logTrace(this.getClass(), "退款信息推送Retail", null, SUCCESS);
        }else{
            logManagerService.logTrace(this.getClass(), "退款信息推送Retail", null, ERROR);
        }
    }

    /**
     *
     *
     * @param refundinfo
     * @return
     */
    @Override
    public List<AsRefundinfo> getSuitSyncInfo(AsRefundinfo refundinfo) {
        return refundinfoMapper.getSuitSyncInfo(refundinfo);
    }
}