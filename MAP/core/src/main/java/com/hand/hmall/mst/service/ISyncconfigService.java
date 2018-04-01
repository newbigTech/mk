package com.hand.hmall.mst.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.mst.dto.Syncconfig;

import java.util.List;
/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name Syncconfig
 * @description 同步配置
 * @date 2017年5月26日10:52:23
 */
public interface ISyncconfigService extends IBaseService<Syncconfig>, ProxySelf<ISyncconfigService>{
    /**
     * 同步下拉框数据查询
     * @return
     */
    List<Syncconfig> selectLovData(Syncconfig dto);

    /**
     * 通过catalogVersionId查询
     * @param catalogVersionId
     * @return
     */
    List<Syncconfig> selectByCatalogVersionId(Long catalogVersionId);

}