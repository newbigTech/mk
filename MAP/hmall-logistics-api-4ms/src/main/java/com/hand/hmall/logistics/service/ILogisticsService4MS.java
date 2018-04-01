package com.hand.hmall.logistics.service;

import com.hand.hmall.logistics.pojo.ConsignmentInfo;
import com.hand.hmall.logistics.trans.WmsLogisticsInfo;

import java.util.List;
import java.util.Map;

/**
 * @author chenzhigang
 * @version 0.1
 * @name ILogisticsService4MS
 * @description 面向微服务的物流服务接口
 * @date 2017/12/12
 */
public interface ILogisticsService4MS {

    /**
     * 日日顺发货记录更新用service
     *
     * @param consignmentInfo
     * @return
     */
    Map<String, Object> rrsOrderHfs(ConsignmentInfo consignmentInfo);

//    /**
//     * 添加日志
//     *
//     * @param clazz          所在类
//     * @param programDesc    类描述
//     * @param itemId         数据主键
//     * @param returnMessage  返回消息
//     * @param sourcePlatform 平台
//     * @param processStatus  状态
//     */
//    void log(Class<?> clazz, String programDesc, Long itemId, String returnMessage, String sourcePlatform, String processStatus);

//    /**
//     * 添加错误日志
//     *
//     * @param clazz         事务类
//     * @param programDesc   类描述
//     * @param itemId        操作对象
//     * @param returnMessage 返回消息
//     */
//    void logError(Class<?> clazz, String programDesc, Long itemId, String returnMessage);

//    /**
//     * WMS物流信息推送
//     * 根据SAP_CODE查询送货单
//     *
//     * @param sapCode
//     * @return
//     */
//    List<WmsConsignment> queryConsignmentsBySAPCode(String sapCode);

    /**
     * 批量导入WMS物流数据
     *
     * @param logisticsInfo
     */
    void batchImport(List<WmsLogisticsInfo> logisticsInfo);

    Map<String, List<WmsLogisticsInfo>> classifyBySoOrderId(List<WmsLogisticsInfo> logisticsInfo);

    void saveConsignmentLogistics(String soOrderId, List<WmsLogisticsInfo> logisticsInfos);

}
