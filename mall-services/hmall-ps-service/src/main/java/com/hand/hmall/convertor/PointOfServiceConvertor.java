package com.hand.hmall.convertor;

import com.hand.hmall.pojo.PointOfServiceData;
import com.markor.map.external.pointservice.dto.PointOfServiceDto;

/**
 * @author 马君
 * @version 0.1
 * @name PointOfServiceConvertor
 * @description PointOfServiceData转换器
 * @date 2017/6/9 11:34
 */
public class PointOfServiceConvertor implements Convertor<PointOfServiceData, PointOfServiceDto> {
    /**
     * {@inheritDoc}
     *
     * @see Convertor#convert(Object)
     */
    @Override
    public PointOfServiceDto convert(PointOfServiceData pointOfServiceData) {
        return convert(pointOfServiceData, new PointOfServiceDto());
    }

    /**
     * {@inheritDoc}
     *
     * @see Convertor#convert(Object, Object)
     */
    @Override
    public PointOfServiceDto convert(PointOfServiceData pointOfServiceData, PointOfServiceDto pointOfServiceDto) {
        // 服务点编号
        pointOfServiceDto.setCode(pointOfServiceData.getCode());
        // 服务点名称
        pointOfServiceDto.setDisplayname(pointOfServiceData.getDisplayname());
        // 地址
        pointOfServiceDto.setAddress(pointOfServiceData.getAddress());
        // 公司代码
        pointOfServiceDto.setBukrs(pointOfServiceData.getBukrs());
        // 联系电话
        pointOfServiceDto.setContactNumber(pointOfServiceData.getContactNumber());
        // 店面状态
        pointOfServiceDto.setShopstatus(pointOfServiceData.getShopstatus());
        // 服务点类型
        pointOfServiceDto.setType(pointOfServiceData.getType());

        // 发送货物的目的地运输区域
        pointOfServiceDto.setZone1(pointOfServiceData.getZone1());
        // 服务中心/分拨中心
        pointOfServiceDto.setZzwerks(pointOfServiceData.getZzwerks());

        return pointOfServiceDto;
    }
}
