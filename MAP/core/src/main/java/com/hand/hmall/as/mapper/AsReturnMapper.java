package com.hand.hmall.as.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.as.dto.AsReturn;
import com.hand.hmall.services.as.dto.AsReturnForRetail;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liuhongxi
 * @version 0.1
 * @name AsReturnMapper
 * @description 退回单Mapper
 * @date 2017/5/24
 */
public interface AsReturnMapper extends Mapper<AsReturn> {

    /**
     * 通过sap单号更新退货单状态
     *
     * @param ar
     * @return
     */
    int updateStatus(AsReturnForRetail ar);

    /**
     * 根据退货单id查询对应的退货单
     *
     * @param dto
     */
    List<AsReturn> selectReturnById(AsReturn dto);


    /**
     * 根据退货单ID查询对应的退货单（推送至Retail）
     *
     * @param asReturnId
     * @return
     */
    AsReturn queryForRetail(Long asReturnId);

    /**
     * 更新退货单SAP_CODE和SYNC_FLAG
     *
     * @param dto
     */
    void updateSyncFlag(AsReturn dto);

    /**
     * 根据退货单ID查询订单和服务单信息
     *
     * @param dto
     * @return
     */
    List<AsReturn> selectOrderAndServiceOrderInfoByReturnId(AsReturn dto);

    /**
     * 根据订单ID查询用户信息
     *
     * @param asReturn
     * @return
     */
    List<AsReturn> selectUserInfoByOrderId(AsReturn asReturn);

    /**
     * @return
     * @description 筛选退货单同步retail的数据
     */
    List<AsReturn> selectDateForReturnToRetail();

    /**
     * 根据订单ID查询状态【return.status】不为“CANC”的退货单的“建议退款金额”【reference_fee】
     *
     * @param asReturn
     * @return
     */
    BigDecimal selectReturnByOrderId(AsReturn asReturn);

    /**
     * 根据服务单ID，查找退货单列表
     *
     * @param serviceOrderId 服务单ID
     * @return
     */
    List<AsReturn> selectReturnOrdersByServiceOrderId(@Param("serviceOrderId") Long serviceOrderId);
}