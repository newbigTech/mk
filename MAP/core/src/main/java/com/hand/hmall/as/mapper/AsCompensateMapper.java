package com.hand.hmall.as.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.as.dto.AsCompensate;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author xuxiaoxue
 * @version 0.1
 * @name AsCompensateMapper
 * @description 销售赔付单头表Mapper接口
 * @date 2017/10/11
 */
public interface AsCompensateMapper extends Mapper<AsCompensate> {

    /**
     * 根据赔付单ID查询赔付单信息
     *
     * @param dto
     */
    List<AsCompensate> selectCompensateById(AsCompensate dto);

    /**
     * 查询赔付金额
     *
     * @param dto
     */
    BigDecimal selectCompensateFeeById(AsCompensate dto);

    /**
     * 查询单位
     */
    List<AsCompensate> selectMstUnit();

    /**
     * 获取同步到retail的赔付单数据
     * @param dto
     * @return
     */
    AsCompensate getCompensateForRetail(AsCompensate dto);

    /**
     * 获取需要发送到retail的数据
     * @param dto
     * @return
     */
    List<AsCompensate> selectSendRetailData(AsCompensate dto);
}