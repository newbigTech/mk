package com.hand.hmall.om.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.om.dto.OmAccounts;

import java.util.List;

public interface OmAccountsMapper extends Mapper<OmAccounts>{

    /**
     * 根据同步Retail标记获得将要推送Retail系统的账单记录
     * @param omAccounts
     * @return
     */
    List<OmAccounts> getBillListBySyncFlag(OmAccounts omAccounts);

    /**
     * 根据第三方交易号获取SOLD_PARTY
     * @param transaction
     */
    String getSoldPartyByTransaction(String transaction);

    /**
     * 根据账单表中的transaction获得订单表中的ESC_ORDER_CODE
     * @param transaction
     * @return
     */
    String getEscOrderCodeBytransaction(String transaction);

    /**
     * 获取已发送Retail系统的账单信息
     * @return
     */
    List<OmAccounts> getCheckBillData();

    /**
     * 财务上载界面列表查询
     *
     * @param dto
     * @return
     */
    List<OmAccounts> queryAccountsList(OmAccounts dto);

    /**
     * 支付宝支付方式中获取用户代码
     *
     * @param accountsId   财务ID
     * @return  用户代码
     */
    String getUserNumber(Long accountsId);

    /**
     * 查询数据重复性
     * @param omAccounts
     * @return
     */
    Long checkIsExict(OmAccounts omAccounts);

    /**
     * 手工对账界面手工对账数据
     * @param dto
     * @return
     */
    List<OmAccounts> getAccountsForBalance(OmAccounts dto);

}