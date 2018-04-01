package com.hand.hmall.om.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.om.dto.Consignment;
import com.hand.hmall.om.dto.ConsignmentBk;

import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name IConsignmentBkService
 * @description 发货单备份Service接口
 * @date 2017/8/4 11:37
 */
public interface IConsignmentBkService extends IBaseService<ConsignmentBk> {

    /**
     * 保存发货单备份
     * @param consignmentBk
     */
    void saveBk(ConsignmentBk consignmentBk);

    /**
     * 发货单详情页查询
     *
     * @param iRequest  请求体
     * @param dto   参数封装对象
     * @param page  页数
     * @param pageSize  显示数量
     * @return  查询结果
     */
    List<ConsignmentBk> queryInfo(IRequest iRequest, ConsignmentBk dto, int page, int pageSize);

    /**
     * 通过ID查询发货单快照信息
     * @param consignmentId
     * @return
     */
    ConsignmentBk queryById(Long consignmentId);
}
