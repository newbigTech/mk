package com.hand.hmall.common.service.impl;

import com.hand.hmall.common.service.ISequenceGenerateService;
import com.hand.hmall.util.Constants;
import com.markor.map.external.setupservice.dto.SetupSequenceConditionDto;
import com.markor.map.external.setupservice.dto.SetupSequenceHeaderDto;
import com.markor.map.external.setupservice.dto.SetupSequenceLinesDto;
import com.markor.map.external.setupservice.dto.SetupSequenceResponseDto;
import com.markor.map.external.setupservice.service.ISetupSequenceHeaderExternalService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 马君
 * @version 0.1
 * @name SequenceGenerateService
 * @description 生成唯一序列（配货单Code等）
 * @date 2017/6/20 11:00
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class SequenceGenerateServiceImpl implements ISequenceGenerateService {

    public static final String DATE_STYLE = "yyyyMMddHHmmss";
    public static final String INTERFACE_ID_PRE = "2"; // interfaceId生成的前缀
    public static final String INTERFACE_ID_SEQTYPE = "interfaceId"; // interfaceId的序列类型


    @Autowired
    private ISetupSequenceHeaderExternalService iSetupSequenceHeaderExternalService;

    /**
     * 获取下一个唯一序列
     * 序列生成规则为 序列 = 前缀 + n为流水码，如seqLength 9，prefix CON
     * 则生成类似的序列为“CON00000000y”,流水码默认采用36进制，即可是数字和字母
     *
     * @param seqType   序列类型 一个用来区分不同序列的唯一编号
     * @param seqLength 序列长度
     * @param prefix    序列前缀
     * @return
     */
    @Override
    public String getNextSequence(String seqType, long seqLength, String prefix) {
        return getNextByAry(seqType, 36L, seqLength, prefix);
    }

    /**
     * 获取数字序列
     * 序列生成规则为 序列 = 前缀 + n为流水码，如seqLength 9，prefix CON
     * 则生成类似的序列为“CON000000001”,流水码默认采用10进制，即只能是数字
     *
     * @param seqType   序列类型
     * @param seqLength 序列长度
     * @param prefix    序列前缀
     * @return
     */
    @Override
    public String getNextNumber(String seqType, long seqLength, String prefix) {
        return getNextByAry(seqType, 10L, seqLength, prefix);
    }

    /**
     * 获取流水码
     *
     * @param seqType   序列类型
     * @param ary       字符进制
     * @param seqLength 流水码长度
     * @param prefix    序列前缀
     * @return
     */
    @Override
    public String getNextByAry(String seqType, Long ary, long seqLength, String prefix) {
        SetupSequenceConditionDto setupSequenceConditionDto = new SetupSequenceConditionDto();
        //编码头信息
        SetupSequenceHeaderDto setupSequenceHeaderDto = new SetupSequenceHeaderDto();
        setupSequenceHeaderDto.setSeqAry(ary);         //流水码码进制
        setupSequenceHeaderDto.setSeqLength(seqLength);       //流水码长度
        setupSequenceHeaderDto.setSeqType(seqType);    //流水码类型
        //编码行信息
        SetupSequenceLinesDto setupSequenceLinesDto = new SetupSequenceLinesDto();
        setupSequenceLinesDto.setIndex1(prefix);       //流水码前缀，控制字段1
        setupSequenceLinesDto.setIndexFlag1("CP");     //控制并插入

        setupSequenceConditionDto.setSetupSequenceHeader(setupSequenceHeaderDto);
        setupSequenceConditionDto.setSetupSequenceLines(setupSequenceLinesDto);

        return encode(setupSequenceConditionDto);
    }

    /**
     * 获取下一个配货单号
     *
     * @return String
     */
    @Override
    public String getNextConsignmentCode() {
        return getNextSequence("conCode", 9L, "CON");
    }

    /**
     * 获取下一个物耗单号
     *
     * @return String
     */
    @Override
    public String getNextMaterialCode() {
        return getNextSequence("materialCode", 9L, "ME");
    }

    /**
     * 获取下一个退款单号
     *
     * @return String
     */
    @Override
    public String getNextRefundEntryCode() {
        return getNextByAry("refundOrderEntryCode", 16L, 9L, "RFE");
    }

    /**
     * 获取下一个退款单号，规则为 RF+9位数字
     *
     * @return 返回新生成的退款单号
     */
    @Override
    public String getNextRefundOrderCode() {
        return getNextByAry("refundOrderCode", 16L, 9L, "RF");
    }

    /**
     * 获取下一个服务、售后单号
     *
     * @return String
     */
    @Override
    public String getNextAsCode() {
        return getNextByAry("serviceOrderCode", 16L, 9L, "AS");
    }

    /**
     * 获取下一个服务销售单号
     *
     * @return
     */
    @Override
    public String getNextSvCode() {
        return getNextByAry("serviceOrderCode", 16L, 9L, "SV");
    }

    /**
     * 获取下一个服务销售单号
     *
     * @return
     */
    @Override
    public String getNextReturnCode() {
        return getNextByAry("returnOrderCode", 10L, 9L, "RT");
    }

    /**
     * 获取订单行库存占用时的interfaceId参数
     * 生成规则: 2 + yyyyMMddHHmmss + 5位数字流水
     * 生成结果类似: 22017072016200000001
     *
     * @return String
     */
    @Override
    public String getNextInterfaceId() {
        SetupSequenceConditionDto setupSequenceConditionDto = new SetupSequenceConditionDto();
        //编码头信息
        SetupSequenceHeaderDto setupSequenceHeaderDto = new SetupSequenceHeaderDto();
        setupSequenceHeaderDto.setSeqAry(10L);         //流水码码进制
        setupSequenceHeaderDto.setSeqLength(5L);       //流水码长度
        setupSequenceHeaderDto.setSeqType(INTERFACE_ID_SEQTYPE);    //流水码类型
        //编码行信息
        SetupSequenceLinesDto setupSequenceLinesDto = new SetupSequenceLinesDto();
        setupSequenceLinesDto.setIndex1(INTERFACE_ID_PRE);        //流水码前缀，控制字段1
        setupSequenceLinesDto.setIndexFlag1("CP");                 //控制并插入
        setupSequenceLinesDto.setDateStyle(DATE_STYLE);           //时间格式
        setupSequenceLinesDto.setDateFlag("CP");                   //控制并插入

        setupSequenceConditionDto.setSetupSequenceHeader(setupSequenceHeaderDto);
        setupSequenceConditionDto.setSetupSequenceLines(setupSequenceLinesDto);

        return encode(setupSequenceConditionDto);
    }

    /**
     * 生成pin码，如P1000000001
     *
     * @return String
     */
    @Override
    public String getNextPin() {
        return getNextSequence("P1", 9L, "P1");
    }

    @Override
    public String getNextOrderEntryCode() {
        return getNextByAry("ODE", 36L, 9L, "ODE");
    }

    @Override
    public String getNextAsCompensateCode() {
        return getNextByAry("COM", 36L, 9L, "COM");
    }

    /**
     * 生成并获得下一个订单编码
     *
     * @return
     */
    @Override
    public String getNextOrderCode() {
        return getNextSequence("OD", 9, "OD");
    }

    /**
     * 生成并获得下一个IO订单编码
     *
     * @return
     */
    @Override
    public String getNextIOOrderCode() {
        return getNextSequence("IO", 9, "IO");
    }

    @Override
    public String encode(SetupSequenceConditionDto setupSequenceConditionDto) {
        SetupSequenceResponseDto response = iSetupSequenceHeaderExternalService.encode(setupSequenceConditionDto);

        if (Constants.JOB_STATUS_ERROR.equals(response.getFlag())) {
            throw new RuntimeException(response.getMassage());
        }

        if (StringUtils.isBlank(response.getCode())) {
            throw new RuntimeException("返回的流水码为空");
        }

        return response.getCode();
    }
}
