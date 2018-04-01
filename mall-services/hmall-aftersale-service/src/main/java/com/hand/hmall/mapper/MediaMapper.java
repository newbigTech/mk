package com.hand.hmall.mapper;

import com.hand.hmall.dto.Media;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @version 1.0
 * @name ExchangeOrderEntryMapper
 * @Describe 媒体资源持久层接口
 * @Author chenzhigang
 * @Date 2017/7/23
 */
public interface MediaMapper extends Mapper<Media> {

    List<Media> queryAll();

}
