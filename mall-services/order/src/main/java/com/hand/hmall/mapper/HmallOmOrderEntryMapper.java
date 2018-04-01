package com.hand.hmall.mapper;

import com.hand.hmall.model.HmallOmOrderEntry;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
/**
 * @author 梅新养
 * @name:HmallOmOrderEntryMapper
 * @Description:订单行查询Mapper
 * @version 1.0
 * @date 2017/5/24 14:39
 */
public interface HmallOmOrderEntryMapper extends Mapper<HmallOmOrderEntry> {

    /**
     * 查询指定订单id关联的所有订单行
     * @param orderId 订单id
     * @return
     */
    List<HmallOmOrderEntry> selectByOrderId(Long orderId);

    /**
     * 查询指定订单编号与订单行序号的订单行
     * @param orderId 订单id
     * @param lineNumber 订单行序号
     * @return
     */
    HmallOmOrderEntry selectByLineNumber(@Param("orderId") Long orderId, @Param("lineNumber") Long lineNumber);

    /**
     * 查询指定订单编号与平台订单行号的订单行
     * @param orderId 订单id
     * @param escLineNumber 平台订单行号
     * @return
     */
    HmallOmOrderEntry selectByEscLineNumber(@Param("orderId") Long orderId, @Param("escLineNumber") String escLineNumber);

    /**
     * 查询指定订单id关联的所有订单行中最大的订单行号
     * @param orderId 订单id
     * @return
     */
    Long selectMaxLineNumberByOrderId(@Param("orderId") Long orderId);

}