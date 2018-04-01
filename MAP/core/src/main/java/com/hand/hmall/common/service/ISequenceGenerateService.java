package com.hand.hmall.common.service;


import com.markor.map.external.setupservice.dto.SetupSequenceConditionDto;

/**
 * @author 马君
 * @version 0.1
 * @name ISequenceGenerateService
 * @description 生成唯一序列公共Service
 * @date 2017/7/13 9:49
 */
public interface ISequenceGenerateService {

    /**
     * 获取下一个唯一序列
     * 序列生成规则为 序列 = 前缀 + n为流水码，如seqLength 9，prefix CON
     * 则生成类似的序列为“CON00000000y”,流水码默认采用36进制，即可是数字和字母
     *
     * @param seqType   序列类型 一个用来区分不同序列的唯一编号
     * @param seqLength 序列长度
     * @param prefix    序列前缀
     * @return String
     */
    String getNextSequence(String seqType, long seqLength, String prefix);

    /**
     * 获取数字序列
     * 序列生成规则为 序列 = 前缀 + n为流水码，如seqLength 9，prefix CON
     * 则生成类似的序列为“CON000000001”,流水码默认采用10进制，即只能是数字
     *
     * @param seqType   序列类型
     * @param seqLength 序列长度
     * @param prefix    序列前缀
     * @return String
     */
    String getNextNumber(String seqType, long seqLength, String prefix);

    /**
     * 获取流水码
     *
     * @param seqType   序列类型
     * @param ary       字符进制
     * @param seqLength 流水码长度
     * @param prefix    序列前缀
     * @return String
     */
    String getNextByAry(String seqType, Long ary, long seqLength, String prefix);

    /**
     * 获取下一个发货单号
     *
     * @return String
     */
    String getNextConsignmentCode();

    /**
     * 获取下一个服务、售后单号
     *
     * @return String
     */
    String getNextAsCode();

    /**
     * 获取下一个服务销售单号
     *
     * @return
     */
    String getNextSvCode();

    /**
     * 获取下一个物耗单号
     *
     * @return String
     */
    String getNextMaterialCode();

    /**
     * 获取下一个退货单号
     *
     * @return String
     */
    String getNextReturnCode();


    /**
     * 获取下一个退款单行号
     *
     * @return String
     */
    String getNextRefundEntryCode();

    /**
     * 获取下一个退款单号，规则为 RF+9位数字
     *
     * @return 返回新生成的退款单号
     */
    String getNextRefundOrderCode();

    /**
     * 获取订单行库存占用时的interfaceId参数
     *
     * @return String
     */
    String getNextInterfaceId();

    /**
     * 生成pin码
     *
     * @return String
     */
    String getNextPin();

    /**
     * 生成订单行编码
     *
     * @return
     */
    String getNextOrderEntryCode();

    /**
     * 生成销售赔付头编码
     *
     * @return
     */
    String getNextAsCompensateCode();

    /**
     * 生成并获得下一个订单编码
     *
     * @return
     */
    String getNextOrderCode();

    /**
     * 生成并获得下一个IO订单编码
     *
     * @return
     */
    String getNextIOOrderCode();


    /**
     * 生成并获取下一个序列
     *
     * @param setupSequenceConditionDto 条件
     * @return String
     */
    String encode(SetupSequenceConditionDto setupSequenceConditionDto);
}
