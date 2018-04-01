package com.hand.hmall.mst.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.mst.dto.Media;
import com.hand.hmall.mst.dto.MediaDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 多媒体对象的Mapper
 * @date 2017/7/10 14:37
 */
public interface MediaMapper extends Mapper<Media> {

    /**
     * 推送sync为N的多媒体信息到商城当中
     *
     * @return
     */
    List<MediaDto> selectPushingMedia();

    /**
     * @param dto
     * @description 更新多媒体接口推送标志
     */
    void updateMediaSyncflag(List<MediaDto> dto);

    /**
     * 对应目录版本多媒体信息查询
     *
     * @param dto
     * @return
     */
    Long selectByCodeAndVersion(Media dto);

    /**
     * 查询发生过变动的图片组
     *
     * @param catalogversionId 目录版本
     * @return List<Media> Media中只返回productId和imageGroup两个属性
     */
    List<String> selectUpdatedImageGroup(@Param("catalogversionId") Long catalogversionId, @Param("productId") Long productId);

    /**
     * 查询将要被推送的媒体对象
     *
     * @param catalogversionId 目录版本
     * @param productId        商品id
     * @param imageGroup       图片组
     * @return List<Media>
     */
    List<Media> selectToBePushedMedias(@Param("catalogversionId") Long catalogversionId, @Param("productId") Long productId, @Param("imageGroup") String imageGroup);

    /**
     * @param media
     * @return
     * @description 商品详情页面中，查询商品相关的各种多媒体图片
     */
    List<Media> selectMediaByProduct(Media media);

    /**
     * @param media
     * @return
     * @description 商品详情页面中删除图片关联关系
     */
    int deleteRelationWithProduct(Media media);

    /**
     * 根据商品ID获取对应的多媒体信息
     *
     * @param productId     商品ID
     * @return  对应的多媒体信息
     */
    List<Media> selectMediaByProductId(Long productId);
}