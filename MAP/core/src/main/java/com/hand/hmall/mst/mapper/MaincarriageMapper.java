package com.hand.hmall.mst.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.mst.dto.Maincarriage;

import java.util.List;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 主干对象的Mapper
 * @date 2017/7/10 14:37
 */
public interface MaincarriageMapper extends Mapper<Maincarriage> {

    /**
     * @param dto
     * @return
     * @description 主干运费界面查询
     */
    List<Maincarriage> selectMaincarriage(Maincarriage dto);

    /**
     * @param dto
     * @return
     * @description 通过承运商编码+承运商类型+区编码确认唯一记录
     */
    List<Maincarriage> selectUnique(Maincarriage dto);
}