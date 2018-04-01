package com.hand.hmall.om.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.om.dto.Consignment;
import com.hand.hmall.om.dto.ConsignmentToRRS;
import org.apache.ibatis.annotations.Param;

import java.security.CodeSigner;
import java.util.List;
import java.util.Map;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name ConsignmentMapper
 * @description 发货单mapper
 * @date 2017年6月5日13:54:47
 */
public interface ConsignmentMapper extends Mapper<Consignment> {

    /**
     * 推送retail接口数据查询
     *
     * @return
     */
    List<Consignment> selectSendRetailData(Map map);

    /**
     * 发货单列表页面查询
     *
     * @param logisticsNumber   快递单号
     * @param escOrderCode  发货单号
     * @param provice   省份
     * @param city  城市
     * @param receiverMobile    收货人手机号
     * @param startTime     下单开始时间
     * @param endTime       结束时间
     * @param strOrderStatus        订单状态
     * @param strDistribution       配送方式
     * @param corporateName         快递公司
     * @param csApproved            是否审核
     * @param confirmReceiving      是否收货标识（Y已收获；N未收货）
     * @return
     */
    List<Consignment> selectConsignmentList(@Param("logisticsNumber") String logisticsNumber,
                                @Param("code") String code,
                                @Param("provice") String provice,
                                @Param("city") String city,
                                @Param("receiverMobile") String receiverMobile,
                                @Param("startTime") String startTime, @Param("endTime") String endTime,
                                @Param("strOrderStatus") String[] strOrderStatus,
                                @Param("strDistribution") String[] strDistribution,
                                @Param("strOrderTypes") String[] strOrderTypes,
                                @Param("corporateName") String corporateName,
                                @Param("csApproved") String csApproved,
                                @Param("bomApproved") String bomApproved,
                                @Param("pause") String pause,
                                @Param("escOrderCode") String escOrderCode,
                                @Param("confirmReceiving") String confirmReceiving);

    /**
     * 发货单详情页查询
     *
     * @param consignment
     * @return
     */
    List<Consignment> queryInfo(Consignment consignment);


    /**
     * 根据发货单ID更新状态
     *
     * @param consignmentId
     * @param status
     */
    void updateStatus(@Param("consignmentId") int consignmentId, @Param("status") String status);


    /**
     * 审核按钮
     *
     * @param list
     */
    void examinestatus(List<Consignment> list);

    /**
     * 推送ZMALL数据准备
     * author: 张子龙
     *
     * @return
     */
    List<Consignment> queryForZmall();

    /**
     * 推送ZMALL后修改SYNCFLAY标识位
     * author：张子龙
     *
     * @param list
     */
    void updateSyncZmall(List<Consignment> list);

    /**
     * 推送发货单信息至日日顺
     * author: 张子龙
     *
     * @return
     */
    List<ConsignmentToRRS> queryConsignmentForRRS();

    void updateSyncThirdLogistics(ConsignmentToRRS dtos);

    /**
     * 查询可以发货单发货单
     * @return List<Consignment>
     */
    List<Consignment> selectCanBeShippedConsignments();
}