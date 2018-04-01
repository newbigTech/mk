package com.hand.hmall.mapper;

import com.hand.hmall.model.HmallMstWebsite;
import tk.mybatis.mapper.common.Mapper;
/**
 * @author 梅新养
 * @name:HmallMstWebsiteMapper
 * @Description:网站信息查询Mapper
 * @version 1.0
 * @date 2017/5/24 14:39
 */
public interface HmallMstWebsiteMapper extends Mapper<HmallMstWebsite> {

    /**
     * 根据网站编码查询网站信息
     *
     * @param websiteCode 网站编码
     * @return
     */
    HmallMstWebsite selectWebsiteByWebsitecode(String websiteCode);
}