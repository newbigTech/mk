package com.hand.hmall.mapper;

import com.hand.hmall.model.HmallMstPointOfService;
import tk.mybatis.mapper.common.Mapper;
/**
 * @author 梅新养
 * @name:HmallMstPointOfServiceMapper
 * @Description:门店信息查询Mapper
 * @version 1.0
 * @date 2017/5/24 14:39
 */
public interface HmallMstPointOfServiceMapper extends Mapper<HmallMstPointOfService> {
    /**
     * 根据门店编码查询渠道信息
     *
     * @param pointOfServiceCode 门店编码
     * @return
     */
    HmallMstPointOfService selectByPointOfServiceCode(String pointOfServiceCode);

}