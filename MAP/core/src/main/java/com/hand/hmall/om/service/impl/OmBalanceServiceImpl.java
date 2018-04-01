package com.hand.hmall.om.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.ServiceRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.om.dto.BalanceInfo;
import com.hand.hmall.om.dto.OmBalance;
import com.hand.hmall.om.mapper.OmAccountsMapper;
import com.hand.hmall.om.mapper.OmBalanceMapper;
import com.hand.hmall.om.service.IOmBalanceService;
import com.hand.hmall.util.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class OmBalanceServiceImpl extends BaseServiceImpl<OmBalance> implements IOmBalanceService {

    //支付方式
    private static final String WECHAT_PAYMETHOD = "WECHAT";
    private static final String UNIONPAY_PAYMETHOD = "UNIONPAY";
    private static final String ALIPAY_PAYMETHOD = "ALIPAY";

    @Autowired
    private OmBalanceMapper mapper;

    @Autowired
    private OmAccountsMapper omAccountsMapper;

    /**
     * 界面列表显示数据查询，根据界面上方填写的搜索条件查询数据
     * @param request   请求对象
     * @param dto   请求参数实体对象
     * @param page  页面显示页数
     * @param pagesize      每页显示数据条数
     * @return  查询出的数据
     */
    @Override
    public List<BalanceInfo> selectBalances(IRequest request, BalanceInfo dto, int page, int pagesize) {
        PageHelper.startPage(page, pagesize);
        List<BalanceInfo> result = mapper.selectBalances(dto);
        if (CollectionUtils.isEmpty(result)) {
            return new ArrayList();
        }
        //界面表格中的用户代码字段，如果是微信和银联支付，直接设为9520；
        //通过TRANSACTION字段关联HMALL_OM_PAYMENTINFO表中的NUMBER_CODE字段，找到对应记录后，根据支付信息表中的订单id关联HMALL_OM_ORDER表，找到对应订单信息；然后用订单表中的WEBSITE_ID与网站表HMALL_MST_WEBSITE表中主键关联，取网站表HMALL_MST_WEBSITE中sold_party的值,如果没值默认为9519
        result.stream().map(new Function<BalanceInfo, BalanceInfo>() {
            @Override
            public BalanceInfo apply(BalanceInfo balanceInfo) {
                if (balanceInfo == null) {
                    return null;
                }
                if ((WECHAT_PAYMETHOD).equalsIgnoreCase(balanceInfo.getChannel()) || (UNIONPAY_PAYMETHOD).equalsIgnoreCase(balanceInfo.getChannel())) {
                    balanceInfo.setUserCode("9520");
                } else if (ALIPAY_PAYMETHOD.equalsIgnoreCase(balanceInfo.getChannel())) {
                    if (StringUtils.isNotBlank(balanceInfo.getTransaction())) {
                        String userNumber = omAccountsMapper.getUserNumber(balanceInfo.getAccountId());
                        if (StringUtils.isNotEmpty(userNumber)) {
                            balanceInfo.setUserCode(userNumber);
                        } else {
                            balanceInfo.setUserCode("9519");
                        }
                    }
                } else {
                    balanceInfo.setUserCode("9520");
                }
                return balanceInfo;
            }
        }).collect(Collectors.toList());
        return result;
    }

    /**'
     * 手动对账时，选择好匹配的财务数据和支付/退款数据后，生成一条对账单数据
     * @param accountId     财务表hmall_om_accounts主键
     * @param infoId        支付信息表hmall_om_paymentinfo表主键，或者退款信息表hmall_as_refundinfo表主键
     * @param type           第二个参数来源，如果是支付信息表hmall_om_paymentinfo表，为PAY，如果退款信息表hmall_as_refundinfo表，为REFUND
     * @return  数据成功插入的条数
     */
    @Override
    public int insertBalances(@Param("accountId") String accountId, @Param("infoId") String infoId, @Param("type") String type) {
        OmBalance omBalance = new OmBalance();
        omBalance.setAccountId(Long.valueOf(accountId));
        omBalance.setInfoId(Long.valueOf(infoId));
        omBalance.setType(type);
        omBalance.setIsBalance(Constants.YES);
        return mapper.insertSelective(omBalance);
    }

    /**
     * 中台对账业务处理
     * 根据accounts表查询出paymentInfo、refundInfo关联的数据
     * 将查到的关联数据插入到balance表
     */
    @Override
    public void handleAssociatedData() {
        List<Integer> accountIdList = mapper.getAccountIds();
        List<OmBalance> balanceList = new ArrayList<>();
        balanceList.addAll(mapper.selectMatchPaymentInfo(accountIdList));
        balanceList.addAll(mapper.selectMatchRefundInfo(accountIdList));
        if(null != balanceList && !balanceList.isEmpty()){
            for (OmBalance balance : balanceList) {
                balance.set__status("add");
            }
            this.batchUpdate(new ServiceRequest(),balanceList);
        }
    }
}