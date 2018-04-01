package com.hand.hmall.as.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.as.dto.Svsales;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * author: zhangzilong
 * name: SvsaleMapper.java
 * discription: 服务销售单 Mapper
 * date: 2017/7/18
 * version: 0.1
 */
public interface SvsaleMapper extends Mapper<Svsales> {

    List<Svsales> selectSvsalesById(Long asSvsalesId);

    Svsales queryForNewSvsale(Long serviceOrderId);

    /**
     * 根据服务销售单ID查询对应的服务销售单（推送至Retail）
     *
     * @param asSvsalesId
     * @return
     */
    Svsales queryForRetail(Long asSvsalesId);

    /**
     * 更新服务销售单SAP_CODE和SYNC_FLAG
     *
     * @param dto
     */
    void updateSyncFlag(Svsales dto);

    Svsales queryByCode(Svsales dto);

    void updatePayStatus(Svsales dto);

    void updateStatusToProc(Svsales dto);

    void updateStatusToCanc(Svsales dto);

    /**
     * 查询服务销售单列表(带分页)
     *
     * @param code
     * @param serviceOrderCode
     * @param escOrderCode
     * @param sapCode
     * @param customerId
     * @param mobile
     * @param payStatus
     * @param syncflag
     * @param svsaleStatus
     * @return
     */
    List<Svsales> querySvsales(@Param("code") String code,
                               @Param("serviceOrderCode") String serviceOrderCode,
                               @Param("escOrderCode") String escOrderCode,
                               @Param("sapCode") String sapCode,
                               @Param("customerId") String customerId,
                               @Param("mobile") String mobile,
                               @Param("payStatus") String payStatus,
                               @Param("syncflag") String syncflag,
                               @Param("svsaleStatus") String[] svsaleStatus);
}
