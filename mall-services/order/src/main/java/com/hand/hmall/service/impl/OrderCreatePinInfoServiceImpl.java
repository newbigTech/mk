/*
 *  Copyright (C) HAND Enterprise Solutions Company Ltd.
 *  All Rights Reserved
 *
 */

package com.hand.hmall.service.impl;

import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.mapper.HmallPinAlterMapper;
import com.hand.hmall.mapper.HmallPinInfoMapper;
import com.hand.hmall.model.HmallOmOrderEntry;
import com.hand.hmall.model.HmallPinAlter;
import com.hand.hmall.model.HmallPinInfo;
import com.hand.hmall.service.IOrderCreatePinInfoService;
import com.hand.hmall.service.IPinAlterInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;

/**
 * @author 李伟
 * @version 1.0
 * @name:OrderCreatePinInfoServiceImpl
 * @Description:
 * @date 2017/8/8 19:51
 */
@Service
public class OrderCreatePinInfoServiceImpl implements IOrderCreatePinInfoService {

    @Resource
    private HmallPinInfoMapper hmallPinInfoMapper;

    @Resource
    private HmallPinAlterMapper hmallPinAlterMapper;

    @Autowired
    private IPinAlterInfoService pinAlterInfoService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResponseData orderCreatePinInfo(HmallOmOrderEntry orderEntry)
    {
        HmallPinInfo hmallPinInfo = new HmallPinInfo();
        ResponseData responseData = new ResponseData();

        //PIN码
        hmallPinInfo.setCode(orderEntry.getPin());
        //订单行号
        hmallPinInfo.setEntryCode(orderEntry.getCode());
        //事件编号
        hmallPinInfo.setEventCode("MAP0100");

        HmallPinAlter hmallPinAlter = hmallPinAlterMapper.getHmallPinAlter("MAP0100");
        String evevtDes = null;
        if(hmallPinAlter == null)
        {
            responseData.setMsgCode("alter.info.null");
            responseData.setMsg("根据eventCode'MAP0100'找不到对应的警报信息");
            responseData.setSuccess(false);
            return responseData;
        }
        else
        {
            evevtDes = hmallPinAlter.getEventDes() == null ? "" : hmallPinAlter.getEventDes();
        }

        //事件描述
        hmallPinInfo.setEventDes(evevtDes);
        //系统
        hmallPinInfo.setSystem("hmall");
        //操作人员（账号)
        hmallPinInfo.setOperator("system");
        //操作人电话
        hmallPinInfo.setMobile("");
        //操作时间
        hmallPinInfo.setOperationTime(orderEntry.getCreationDate());
        //节点信息
        hmallPinInfo.setEventInfo("订单已生成");
        //一级预警标记
        hmallPinInfo.setFlagLevel1(null);
        //二级预警标记
        hmallPinInfo.setFlagLevel2(null);
        //三级预警标记
        hmallPinInfo.setFlagLevel3(null);

        //在PIN码表中插入信息
        hmallPinInfoMapper.insertSelective(hmallPinInfo);
        try {
            pinAlterInfoService.insertPinAlterInfo(hmallPinInfo);
        }catch (ParseException e){
            e.printStackTrace();
        }
        responseData.setMsgCode("pin.insertinfo.success");
        responseData.setMsg("在PIN码表中插入信息成功");
        responseData.setSuccess(true);
        return responseData;
    }
}
