package com.hand.hmall.om.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.om.dto.BalanceInfo;
import com.hand.hmall.om.dto.OmBalance;

import java.util.List;

public interface IOmBalanceService extends IBaseService<OmBalance>, ProxySelf<IOmBalanceService>{

    /**
     * 界面列表显示数据查询，根据界面上方填写的搜索条件查询数据
     * @param request   请求对象
     * @param balanceInfo   请求参数实体对象
     * @param page  页面显示页数
     * @param pagesize      每页显示数据条数
     * @return  查询出的数据
     */
    List<BalanceInfo> selectBalances(IRequest request, BalanceInfo balanceInfo, int page, int pagesize);

    /**
     * 手工对账插入数据
     *
     * @param accountId     财务表hmall_om_accounts主键
     * @param infoId        支付信息表hmall_om_paymentinfo表主键，或者退款信息表hmall_as_refundinfo表主键
     * @param type           第二个参数来源，如果是支付信息表hmall_om_paymentinfo表，为PAY，如果退款信息表hmall_as_refundinfo表，为REFUND
     * @return
     */
    int insertBalances(String accountId, String infoId, String type);

    /**
     * 中台对账业务处理
     * 根据accounts表查询出paymentInfo、refundInfo关联的数据
     * 将查到的关联数据插入到balance表
     */
    void handleAssociatedData();
}