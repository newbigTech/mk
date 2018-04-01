package com.hand.hmall.mst.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.mst.dto.Odtype;
import com.hand.hmall.mst.dto.OdtypeDto;

import java.util.List;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 频道对象mapper
 * @date 2017/8/11 17:33
 */
public interface OdtypeMapper extends Mapper<Odtype> {

    /**
     * @return
     * @description 推送至商城的频道信息数据
     */
    List<OdtypeDto> selectOdtypeData();

    /**
     * 目录版本同步时使用，根据商品ID查询对应的OD关系
     *
     * @param productId
     * @return
     */
    List<Odtype> getOdtypeInfoByProductId(Long productId);

    /**
     * 根据商品ID删除对应信息
     *
     * @param productId
     */
    void removeByProductId(Long productId);

    /**
     * 根据定制频道来源和商品ID查询对应信息
     *
     * @param odtype 参数
     * @return 查询信息
     */
    List<Odtype> getByProductAndCustChanSrc(Odtype odtype);

}