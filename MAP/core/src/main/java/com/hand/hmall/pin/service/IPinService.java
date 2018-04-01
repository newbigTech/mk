package com.hand.hmall.pin.service;

import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.om.dto.OrderEntry;
import com.hand.hmall.pin.dto.Pin;
import com.hand.hmall.pin.dto.PinInfo4SendZmall;

import java.util.List;
import java.util.Map;

/**
 * @author chenzhigang
 * @version 0.1
 * @name IPinServiceForRetail
 * @description PIN码信息服务接口
 * @date 2017/8/4
 */
public interface IPinService extends IBaseService<Pin> {

    /**
     * 根据code查询PIN码
     *
     * @param code - PIN码
     * @return
     */
    List<Pin> queryByCode(String code);

    /**
     * 查询全部非同步状态PIN码
     *
     * @return
     */
    List<PinInfo4SendZmall> queryNotSyncPinInfo();

    /**
     * 更新Zmall同步标志位
     *
     * @param syncFlag - true表示Y，false表示N
     */
    void updateSynvZmallFlag(boolean syncFlag);

    /**
     * 推送PIN码至ZMall
     *
     * @return 推送PIN码信息的ZMall接口地址，以及推送PIN码的JSON格式数据
     */
    Map sendPin2ZMall();

    /**
     * 保存pin码
     * @param orderEntries 订单行
     * @param approvedBy 审核人
     * @param eventCode 事件编号
     * @param eventInfo 事件描述
     */
    void savePinInfos(List<OrderEntry> orderEntries, String approvedBy, String eventCode, String eventInfo);
}
