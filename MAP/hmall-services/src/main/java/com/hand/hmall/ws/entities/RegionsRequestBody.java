package com.hand.hmall.ws.entities;

import java.io.Serializable;
import java.util.List;

/**
 * @author: zhangmeng01
 * @version: 0.1
 * @name: RegionsRequestBody
 * @description:地址推送接口接收数据实体类
 * @Date: 2017/6/21
 * \
 */
public class RegionsRequestBody implements Serializable {
    /**
     * 省信息
     */
    private List<RegionModel> ZRTMDVD004;
    /**
     * 市信息
     */
    private List<RegionModel> ZRTMDVD005;
    /**
     * 地区信息
     */
    private List<RegionModel> ZRTMDVD006;

    public List<RegionModel> getZRTMDVD004() {
        return ZRTMDVD004;
    }

    public void setZRTMDVD004(List<RegionModel> ZRTMDVD004) {
        this.ZRTMDVD004 = ZRTMDVD004;
    }

    public List<RegionModel> getZRTMDVD005() {
        return ZRTMDVD005;
    }

    public void setZRTMDVD005(List<RegionModel> ZRTMDVD005) {
        this.ZRTMDVD005 = ZRTMDVD005;
    }

    public List<RegionModel> getZRTMDVD006() {
        return ZRTMDVD006;
    }

    public void setZRTMDVD006(List<RegionModel> ZRTMDVD006) {
        this.ZRTMDVD006 = ZRTMDVD006;
    }
}
