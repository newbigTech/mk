package com.hand.hmall.as.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.as.dto.AsReturn;
import com.hand.hmall.as.dto.AsReturnEntry;
import com.hand.hmall.as.dto.AsTmrefund;
import com.hand.hmall.as.mapper.AsTmrefundMapper;
import com.hand.hmall.as.service.IAsReturnEntryService;
import com.hand.hmall.as.service.IAsReturnService;
import com.hand.hmall.as.service.IAsTmrefundService;
import com.hand.hmall.common.service.ISequenceGenerateService;
import com.hand.hmall.om.mapper.OrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
/**
 * @author xuxiaoxue
 * @version 0.1
 * @name AsTmrefundServiceImpl
 * @description 天猫退款单导入ServiceImpl类
 * @date 2017/9/14
 */
public class AsTmrefundServiceImpl extends BaseServiceImpl<AsTmrefund> implements IAsTmrefundService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AsTmrefundMapper mapper;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    private ISequenceGenerateService sequenceGenerateService;

    @Autowired
    private IAsReturnService asReturnService;

    @Autowired
    private IAsReturnEntryService asReturnEntryService;

    @Override
    public List<AsTmrefund> selectByCode(String code) {
        return mapper.selectByCode(code);
    }

    @Override
    public void batchInsertAsTmrefund(IRequest iRequest, List<AsTmrefund> asTmrefundList) {
        mapper.batchInsertAsTmrefund(asTmrefundList);
    }

    /**
     * 天猫退款单查询list
     *
     * @param asTmrefund
     * @return
     */
    @Override
    public List<AsTmrefund> queryAsTmrefundList(AsTmrefund asTmrefund, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return mapper.queryAsTmrefundList(asTmrefund);
    }

    @Override
    public List<AsTmrefund> selectDataForTmRefundToReturnJob(IRequest request) {
        return mapper.selectDataForTmRefundToReturnJob();
    }

    @Override
    public AsTmrefund tmRefundToReturn(IRequest request, AsTmrefund asTmrefund) {
        AsReturn asReturn = new AsReturn();

        asReturn.setCode(sequenceGenerateService.getNextReturnCode()); //系统自动生成
        asReturn.setStatus("FINI");
        asReturn.setOrderId(asTmrefund.getOrderId());
        asReturn.setName(asTmrefund.getReceiverName());
        asReturn.setMobile(asTmrefund.getReceiverMobile());
        asReturn.setAddress(asTmrefund.getAddress());
        asReturn.setCs(request.getUserName());
        asReturn.setFinishTime(asTmrefund.getRefundFinishTime());
        asReturn.setSyncflag("N");
        asReturn.setAppointmentDate(asTmrefund.getRefundFinishTime());
        asReturn.setExecutionDate(asTmrefund.getRefundFinishTime());
        asReturn.setReturnType("R02");
        if (asTmrefund.getRefundFee() != null) {
            asReturn.setReturnFee(asTmrefund.getRefundFee().doubleValue());
        } else {
            asReturn.setReturnFee(0D);
        }
        if (asTmrefund.getRefundFee() != null) {
            asReturn.setReferenceFee(asTmrefund.getRefundFee().doubleValue());
        } else {
            asReturn.setReferenceFee(0D);
        }
        asReturnService.insertSelective(request, asReturn);
        logger.info("退货单头" + asReturn.getAsReturnId() + "已生成!");

        AsReturnEntry asReturnEntry = new AsReturnEntry();

        asReturnEntry.setAsReturnId(asReturn.getAsReturnId());
        asReturnEntry.setOrderEntryId(asTmrefund.getOrderentryId());
        asReturnEntry.setLineNumber(asTmrefund.getLineNumber());
        asReturnEntry.setParentLine(asTmrefund.getParentLine());
        asReturnEntry.setQuantity(asTmrefund.getQuantity());
        asReturnEntry.setUnit(asTmrefund.getUnit());
        asReturnEntry.setBasePrice(asTmrefund.getBasePrice());
        if (asTmrefund.getRefundFee() != null) {
            asReturnEntry.setReturnFee(asTmrefund.getRefundFee().doubleValue());
        } else {
            asReturnEntry.setReturnFee(0D);
        }
        asReturnEntry.setIsGift(asTmrefund.getIsGift());
        if (asTmrefund.getProductId() != null) {
            asReturnEntry.setProductId(String.valueOf(asTmrefund.getProductId()));
        }
        asReturnEntry.setVproduct(asTmrefund.getvProductCode());
        asReturnEntry.setSuitCode(asTmrefund.getSuitCode());
        asReturnEntry.setInternalPrice(asTmrefund.getInternalPrice());
        asReturnEntry.setReturnentryStatus("NORMAL");
        asReturnEntryService.insertSelective(request, asReturnEntry);
        logger.info("退货单行" + asReturnEntry.getAsReturnEntryId() + "已生成!");

        asTmrefund.setCreatreturn("Y");
        asTmrefund.setReturnId(asReturn.getAsReturnId());
        mapper.updateByPrimaryKeySelective(asTmrefund);

        return asTmrefund;
    }


}