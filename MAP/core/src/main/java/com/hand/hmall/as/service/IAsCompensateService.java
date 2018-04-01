package com.hand.hmall.as.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.as.dto.AsCompensate;
import com.hand.hmall.as.dto.AsCompensateEntry;
import com.hand.hmall.as.dto.AsCompensateTemplate;

import java.util.List;

/**
 * @author xuxiaoxue
 * @version 0.1
 * @name IAsCompensateService
 * @description 销售赔付单头表Service接口
 * @date 2017/10/11
 */
public interface IAsCompensateService extends IBaseService<AsCompensate>, ProxySelf<IAsCompensateService> {

    /**
     * 根据赔付单ID查询赔付单信息
     *
     * @param dto
     */
    List<AsCompensate> selectCompensateById(AsCompensate dto);

    /**
     * 查询单位
     */
    List<AsCompensate> selectMstUnit();

    /**
     * 保存赔付单信息 赔付单行信息
     *
     * @param iRequest
     * @param dto
     * @return
     */
    ResponseData saveCompensate(IRequest iRequest, List<AsCompensate> dto);

    /**
     * 导入销售赔付单头和行信息
     *
     * @param infos
     */
    ResponseData importCompensateAndCompensateEntry(List<AsCompensateTemplate> infos, IRequest iRequest, Boolean importResult, String message) throws Exception;

    /**
     * 同步销售赔付单到retail
     *
     * @param iRequest
     * @param asCompensate
     * @return
     */
    String compensateSyncRetail(IRequest iRequest, AsCompensate asCompensate);

    /**
     * 获取需要发送到retail的数据
     *
     * @param dto
     * @return
     */
    List<AsCompensate> selectSendRetailData(AsCompensate dto);

    /**
     * 赔付单增加书面记录
     *
     * @param asCompensate
     */
    void compensateAddSoChangeLog(AsCompensate asCompensate);
}