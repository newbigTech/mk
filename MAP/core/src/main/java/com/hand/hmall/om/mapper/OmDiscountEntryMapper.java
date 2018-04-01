package com.hand.hmall.om.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.mst.dto.Product;
import com.hand.hmall.om.dto.OmDiscountEntry;

import java.util.List;

public interface OmDiscountEntryMapper extends Mapper<OmDiscountEntry> {
    /**
     * 查询折扣价格列表
     *
     * @param dto
     * @return
     */
    List<OmDiscountEntry> queryDiscountEntryInfo(OmDiscountEntry dto);

    /**
     * 查询时间范围内同一商品是否存在折扣行
     *
     * @param dto
     * @return
     */
    List<OmDiscountEntry> queryDiscountEntryByTime(OmDiscountEntry dto);

    /**
     * 查询
     *
     * @param dto
     * @return
     */
    List<OmDiscountEntry> queryDiscountEntryByOnline(OmDiscountEntry dto);


    /**
     * 根据PRODUCT_ID查询IS_HANDLE状态为Y的折扣价格行记录
     *
     * @param dto
     * @return
     */
    List<OmDiscountEntry> queryDiscountEntryByProductId(Product dto);

    /**
     * 根据PRODUCT_ID查询IS_HANDLE状态为Y折扣类型为3的折扣价格行记录
     *
     * @param dto
     * @return
     */
    List<OmDiscountEntry> queryDiscountEntryByProductIdAndDiscountType(OmDiscountEntry dto);

    /**
     * 批量导入折扣单行信息
     *
     * @param omDiscountEntryList
     */
    void batchInsertDiscountEntry(List<OmDiscountEntry> omDiscountEntryList);

    /**
     * 根据PRODUCT_ID和VCODE查询开始和结束时间
     *
     * @return
     */
    List<OmDiscountEntry> queryDiscountEntryByProductIdAndVcode(OmDiscountEntry discountEntry);
}