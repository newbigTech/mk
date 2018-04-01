package com.hand.hmall.as.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.as.dto.AsTmrefund;

import java.util.List;

/**
 * @author xuxiaoxue
 * @version 0.1
 * @name AsTmrefundMapper
 * @description 天猫退款单导入Mapper类
 * @date 2017/9/14
 */
public interface AsTmrefundMapper extends Mapper<AsTmrefund> {
    /**
     * 根据code查询天猫退货单表
     *
     * @param code
     * @return
     */
    List<AsTmrefund> selectByCode(String code);


    void batchInsertAsTmrefund(List<AsTmrefund> asTmrefundList);

    /**
     * 天猫退款单查询list
     *
     * @param asTmrefund
     * @return
     */
    List<AsTmrefund> queryAsTmrefundList(AsTmrefund asTmrefund);

    /**
     * @return
     * @description TM退款单生成退货单的数据筛选
     */
    List<AsTmrefund> selectDataForTmRefundToReturnJob();
}