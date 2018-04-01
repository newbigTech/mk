package com.hand.hmall.om.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.om.dto.OmPromoRecord;

import java.util.List;
/**
 * @author yuxiaoli
 * @version 0.1
 * @name OmPromoRecordMapper
 * @description 事后促销记录mapper类
 * @date 2017/10/13
 */
public interface OmPromoRecordMapper extends Mapper<OmPromoRecord> {

    /**
     * @param dto
     * @return
     * @description 根据传进来的参数俩判断是符合条件用户还是候补用户
     */
    List<OmPromoRecord> selectPromoRecord(OmPromoRecord dto);

    /**
     * @param dto
     * @return
     * @description 查询某事后促销规则下'WAIT_FINI', 'FINISH'两个状态的记录个数
     */
    List<OmPromoRecord> selectFinishCount(OmPromoRecord dto);

}