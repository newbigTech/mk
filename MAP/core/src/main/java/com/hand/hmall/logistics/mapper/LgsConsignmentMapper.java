package com.hand.hmall.logistics.mapper;


import com.hand.hmall.logistics.pojo.LgsConsignment;
import org.apache.ibatis.annotations.Param;
//import tk.mybatis.mapper.common.Mapper;

/**
 * @author 刘宏玺
 * @version 0.1
 * @name ConsignmentMapper
 * @description 发货单mapper
 * @date 2017年6月5日13:54:47
 */
public interface LgsConsignmentMapper /*extends Mapper<LgsConsignment>*/ {

    /**
     * 根据code查询发货单Id
     *
     * @param code
     * @return
     */
    LgsConsignment selectConsignmentId(@Param("code") String code);

    void updateConsignmentStatusToSigned(LgsConsignment consignment);
}