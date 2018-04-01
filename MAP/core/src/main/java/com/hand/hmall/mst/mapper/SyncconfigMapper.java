package com.hand.hmall.mst.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.mst.dto.Syncconfig;

import java.util.List;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name SyncconfigMapper
 * @description 同步配置
 * @date 2017年5月26日10:52:23
 */
public interface SyncconfigMapper extends Mapper<Syncconfig> {

    /**
     * 同步下拉框数据查询
     *
     * @return
     */
    List<Syncconfig> selectLovData(Syncconfig dto);


    /**
     * 通过catalogVersionId查询
     *
     * @param catalogVersionId
     * @return
     */
    List<Syncconfig> selectByCatalogVersionId(Long catalogVersionId);

}