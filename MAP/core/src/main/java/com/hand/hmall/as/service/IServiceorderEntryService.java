package com.hand.hmall.as.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.as.dto.ChangeGoodDto;
import com.hand.hmall.as.dto.ServiceorderEntry;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zhangmeng
 * @version 0.1
 * @name IServiceorderEntryService
 * @description 服务单行Service接口
 * @date 2017/7/19
 */
public interface IServiceorderEntryService extends IBaseService<ServiceorderEntry>, ProxySelf<IServiceorderEntryService> {
    /**
     * 查询售后单关联的售后单行
     *
     * @param dto
     * @return
     */
    List<ServiceorderEntry> queryServiceOrderInfo(IRequest iRequest, ServiceorderEntry dto, int page, int pagesize);

    /**
     * 根据服务单ID查询其对应的全部服务单行
     *
     * @param serviceOrderId - 服务单ID
     * @return
     */
    List<ServiceorderEntry> queryOrderEntriesByOrderId(long serviceOrderId);

    /**
     * 根据退款单ID查询其对应的全部服务单行  selectDispatchOrderEntry
     *
     * @param serviceOrderId - 退款单ID
     * @return
     */
    List<ServiceorderEntry> selectRefundOrderEntry(Long serviceOrderId);

    /**
     * 根据派工单ID查询其对应的全部服务单行
     *
     * @param serviceOrderId - 派工单ID
     * @return
     */
    List<ServiceorderEntry> selectDispatchOrderEntry(Long serviceOrderId);

    /**
     * 根据服务单id查询退货单对应的所有服务单行
     *
     * @param dto
     * @param page
     * @param pageSize
     * @return List<ServiceorderEntry>
     */
    List<ServiceorderEntry> queryReturnOrder(ServiceorderEntry dto, int page, int pageSize);

    /**
     * 获取服务单对应的订单行排除不想要的产品
     *
     * @param dto
     * @return
     */
    List<ServiceorderEntry> getServiceOrderListExcludeProductId(ServiceorderEntry dto, int page, int pageSize);

    /**
     * @param request
     * @param serviceorderEntryList
     * @return
     * @description 创建退换货界面中，通过勾选的服务单行来查找商品编码，商品名称以及数量
     */
    List<ServiceorderEntry> selectByOrderServiceOrderEntryIdList(IRequest request, List<ServiceorderEntry> serviceorderEntryList);

    /**
     * @param request
     * @param changeGoodDto
     * @description 生成换发单逻辑处理
     */
    Map<String, Object> createChangeGoodOrder(IRequest request, ChangeGoodDto changeGoodDto);

    /**
     * @param request
     * @param changeGoodDto
     * @return
     * @description 生成换发单时调用atp接口，获得交付日期
     */
    List<Date> getAtp(IRequest request, ChangeGoodDto changeGoodDto);
}