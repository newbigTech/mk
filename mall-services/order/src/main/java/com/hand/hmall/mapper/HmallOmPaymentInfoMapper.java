package com.hand.hmall.mapper;

import com.hand.hmall.model.HmallOmPaymentInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
/**
 * @author 梅新养
 * @name:HmallOmPaymentInfoMapper
 * @Description:支付信息查询Mapper
 * @version 1.0
 * @date 2017/5/24 14:39
 */
public interface HmallOmPaymentInfoMapper extends Mapper<HmallOmPaymentInfo> {

    /**
     * 判断是否存在指定流水号的支付信息
     * @param numberCode 流水号
     * @return
     */
    int countPayInfo(String numberCode);

    /**
     * 根据支付渠道、流水号查询支付信息
     * @param payMode 支付渠道
     * @param numberCode 流水号
     * @return
     */
    HmallOmPaymentInfo selectByPaymodeAndNumbercode(@Param("payMode")String payMode, @Param("numberCode") String numberCode);

}