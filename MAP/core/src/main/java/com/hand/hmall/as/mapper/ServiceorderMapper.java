package com.hand.hmall.as.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.as.dto.Serviceorder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xuxiaoxue
 * @version 0.1
 * @name ServiceorderMapper
 * @description 服务单列表页面 Mapper接口
 * @date 2017/7/17
 */
public interface ServiceorderMapper extends Mapper<Serviceorder> {
    /**
     * 根据查询条件查询符合条件的服务单列表
     *
     * @param dto
     * @return List<Serviceorder>
     */
    List<Serviceorder> queryServiceOrderList(Serviceorder dto);

    /**
     * 根据订单ID查询服务单信息
     *
     * @param dto
     * @return List<Serviceorder>
     */
    List<Serviceorder> queryServiceOrderListBySaleCode(Serviceorder dto);

    /**
     * 查询服务单详细信息
     *
     * @param dto
     * @return
     */
    List<Serviceorder> selectServiceOrderByCode(Serviceorder dto);

    /**
     * 根据订单ID查询用户信息
     *
     * @param dto
     * @return
     */
    List<Serviceorder> selectUserInfoByOrderId(Serviceorder dto);

    /**
     * 根据服务单ID查询多媒体中的图片信息
     *
     * @param dto
     * @return
     */
    List<Serviceorder> queryMediaByServiceOrderId(Serviceorder dto);

    /**
     * 设置服务单归属信息
     * @param soIds_ - 服务单ID列表
     * @param employeeId - 雇员ID
     */
    void setAssiging(@Param("soIds_") List<Long> soIds_, @Param("employeeId") Long employeeId);
}