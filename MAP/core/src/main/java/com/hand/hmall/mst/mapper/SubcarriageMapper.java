package com.hand.hmall.mst.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.mst.dto.Subcarriage;

import java.util.List;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 支线运费对象的Mapper
 * @date 2017/7/10 14:37
 */
public interface SubcarriageMapper extends Mapper<Subcarriage> {

    /**
     * @param dto
     * @return
     * @description 支线运费维护界面查询
     */
    List<Subcarriage> selectSubcarriage(Subcarriage dto);

    /**
     * @param dto
     * @return
     * @description 通过承运商编码+承运商类型+区编码确认唯一记录
     */
    List<Subcarriage> selectUnique(Subcarriage dto);

}