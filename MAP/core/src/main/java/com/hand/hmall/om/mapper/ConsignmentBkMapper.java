package com.hand.hmall.om.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.om.dto.Consignment;
import com.hand.hmall.om.dto.ConsignmentBk;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name ConsignmentBkMapper
 * @description 发货单备份Mapper
 * @date 2017/8/4 10:56
 */
public interface ConsignmentBkMapper extends Mapper<ConsignmentBk> {

    /**
     * 发货单详情页查询
     *
     * @param consignment
     * @return
     */
    List<ConsignmentBk> queryInfo(ConsignmentBk consignment);

    /**
     * 通过ID查询发货单快照信息
     * @param consignmentId - 发货单ID
     * @return
     */
    ConsignmentBk queryById(@Param("consignmentId") Long consignmentId);
}
