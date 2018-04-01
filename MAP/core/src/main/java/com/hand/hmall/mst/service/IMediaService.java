package com.hand.hmall.mst.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.mybatis.entity.Example;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.mst.dto.Media;
import com.hand.hmall.mst.dto.MediaDto;
import com.hand.hmall.mst.dto.Product;

import java.io.IOException;
import java.util.List;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 多媒体对象的service接口
 * @date 2017/7/10 14:37
 */
public interface IMediaService extends IBaseService<Media>, ProxySelf<IMediaService> {

    /**
     * 推送sync为N的多媒体信息到商城当中
     *
     * @return
     */
    public List<MediaDto> selectPushingMedia();

    /**
     * @param dto
     * @description 更新多媒体接口推送标志
     */
    public void updateMediaSyncflag(List<MediaDto> dto);

    /**
     * 对应目录版本多媒体信息查询
     *
     * @param media
     * @return
     */
    public Long selectByCodeAndVersion(Media media);

    /**
     * 为选中商品生成或更新媒体对象
     *
     * @param products 商品集合，必须包含code属性
     * @return ResponseData
     */
    void createMediaForProduct(List<Product> products);

    /**
     * 根据商品id查询所有markorStaged版本的媒体对象
     *
     * @param productId        商品id
     * @param catalogversionId 目录版本id
     * @return List<Media>
     */
    List<Media> selectAllByProductId(Long productId, Long catalogversionId);

    /**
     * 查询发生过变动的图片组
     *
     * @param catalogversionId 目录版本
     * @return List<Media> Media中只返回productId和imageGroup两个属性
     */
    List<String> selectUpdatedImageGroup(Long catalogversionId, Long productId);

    /**
     * 查询将要被推送的媒体对象
     *
     * @param catalogversionId 目录版本
     * @param productId        商品id
     * @param imageGroup       图片组
     * @return List<Media>
     */
    List<Media> selectToBePushedMedias(Long catalogversionId, Long productId, String imageGroup);

    /**
     * 查询一个商品的所有表删除Flag的媒体对象
     *
     * @param catalogversionId 目录版本
     * @param productId        商品id
     * @return List<Media>
     */
    List<Media> selectToBeDeletedMedias(Long catalogversionId, Long productId);

    /**
     * 根据条件查询
     *
     * @param media 条件
     * @return List<Media>
     */
    List<Media> select(Media media);

    /**
     * 根据example对象查询
     *
     * @param example 查询条件
     * @return List<Media>
     */
    List<Media> selectByExample(Example example);

    /**
     * @param request
     * @param list
     * @return
     * @description 图片导入时，校验图片数据逻辑
     */
    String checkMedia(IRequest request, List<Media> list);

    /**
     * @param request
     * @param dto
     * @return
     * @description 商品详情页面中，查询商品相关的各种多媒体图片
     */
    List<Media> selectMediaByProduct(IRequest request, Media dto, int pageSize, int page);

    /**
     * @param request
     * @param list
     * @return
     * @description 商品详情页面中删除图片关联关系
     */
    List<Media> deleteRelationWithProduct(IRequest request, List<Media> list);

    /**
     * @param request
     * @param filePath
     * @return
     * @description 获取图片的IO流，以便页面显示图片
     */
    String getImageIoStream(IRequest request, String filePath) throws IOException;

    /**
     * 根据商品ID获取对应的多媒体信息
     *
     * @param productId     商品ID
     * @return  对应的多媒体信息
     */
    List<Media> selectMediaByProductId(Long productId);

    /**
     * 根据多媒体Id调用系统方法查询多媒体信息
     *
     * @param iRequest  请求体
     * @param mediaId   多媒体Id
     * @return 查询结果
     */
    Media selectByMediaId(IRequest iRequest, Long mediaId);

    /**
     * 同步多媒体到zmall
     */
    void syncToZmall();
}