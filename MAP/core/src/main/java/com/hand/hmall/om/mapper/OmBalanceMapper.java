package com.hand.hmall.om.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.om.dto.BalanceInfo;
import com.hand.hmall.om.dto.OmBalance;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OmBalanceMapper extends Mapper<OmBalance> {

    /**
     * 界面列表显示数据查询，根据界面上方填写的搜索条件查询数据
     * @param balanceInfo   请求参数实体对象
     * @return  查询出的数据
     */
    List<BalanceInfo> selectBalances(BalanceInfo balanceInfo);

    /**
     * 筛选出账单与支付记录表匹配的数据
     * (不存在于HMALL_OM_BALANCE表中的数据)
     *
     * @return
     */
    List<OmBalance> selectMatchPaymentInfo(@Param("accountIdList") List<Integer> accountIdList);

    /**
     * 筛选出账单与退款记录表匹配的数据
     * (不存在于HMALL_OM_BALANCE表中的数据)
     *
     * @return
     */
    List<OmBalance> selectMatchRefundInfo(@Param("accountIdList") List<Integer> accountIdList);

    /**
     * 查询出hmall_om_balance表中所有的accountId
     *
     * @return
     */
    List<Integer> getAccountIds();

}