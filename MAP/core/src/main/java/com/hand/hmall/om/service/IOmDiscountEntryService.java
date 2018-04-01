package com.hand.hmall.om.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.om.dto.OmDiscountEntry;
import com.hand.hmall.om.dto.OmDiscountEntryTemplate;

import java.util.List;

/**
 * @author zhangmeng
 * @name IOmDiscountEntryService
 * @description 折扣行
 * @date 2017/11/28
 */
public interface IOmDiscountEntryService extends IBaseService<OmDiscountEntry>, ProxySelf<IOmDiscountEntryService> {
    /**
     * 查询折扣价格列表
     *
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    List<OmDiscountEntry> queryDiscountEntryInfo(OmDiscountEntry dto, int page, int pageSize);

    /**
     * 同步折扣行至M3D
     *
     * @param omDiscountEntryList
     * @return
     */
    ResponseData syncM3DDiscountEntry(List<OmDiscountEntry> omDiscountEntryList);

    /**
     * 删除折扣行
     *
     * @param omDiscountEntryList
     * @return
     */
    ResponseData delDiscountEntry(List<OmDiscountEntry> omDiscountEntryList);


    /**
     * 保存折扣行
     *
     * @param omDiscountEntryList
     * @return
     */
    ResponseData saveDiscountEntry(List<OmDiscountEntry> omDiscountEntryList);

    /**
     * 同步折扣行
     * flag为1 同步选择的折扣行 flag为2全部同步
     *
     * @param omDiscountEntryList
     * @param flag
     * @return
     */
    ResponseData syncDiscountEntry(IRequest request, List<OmDiscountEntry> omDiscountEntryList, String flag);

    /**
     * 根据PRODUCT_ID查询IS_HANDLE状态为Y折扣类型为3的折扣价格行记录
     *
     * @param dto
     * @return
     */
    List<OmDiscountEntry> queryDiscountEntryByProductIdAndDiscountType(OmDiscountEntry dto);

    /**
     * 折扣单行的导入
     *
     * @param infos
     * @throws Exception
     */
    void importDiscountEntry(List<OmDiscountEntryTemplate> infos) throws Exception;

}