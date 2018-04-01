package com.hand.hmall.ws.impl;

import com.hand.hmall.services.ws.IFndRegionsServer4Retail;
import com.hand.hmall.ws.entities.RegionModel;
import com.hand.hmall.ws.entities.RegionsRequestBody;
import com.markor.map.external.fndregionservice.dto.CommonRegionModel;
import com.markor.map.external.fndregionservice.dto.RegionResultModel;
import com.markor.map.external.fndregionservice.service.IFndRegionsCommonExternalService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhangmeng01
 * @version: 0.1
 * @name: FndRegionsServerImpl
 * @description:
 * @Date: 2017/6/21
 */
public class FndRegionsServerImpl implements IFndRegionsServer4Retail {
    @Autowired
    private IFndRegionsCommonExternalService iFndRegionsCommonExternalService;

    /**
     * 将retail传递过来的参数转换成通用model参数并返回
     *
     * @param regionModel
     * @return
     */
    private static CommonRegionModel copyRegionModel(RegionModel regionModel) {
        CommonRegionModel commonRegionModel = new CommonRegionModel();
        commonRegionModel.setAreaCode(regionModel.getZADDR());
        commonRegionModel.setNationalKeyValue(regionModel.getLAND1());
        commonRegionModel.setName(regionModel.getBEZEI());
        commonRegionModel.setDelFlag(regionModel.getZZEDL());
        commonRegionModel.setParentCode(regionModel.getADDR_UP());
        commonRegionModel.setFullAddressName(regionModel.getZZDZQC());
        return commonRegionModel;
    }

    @Override
    public RegionResultModel getFndRegionsFromRetail(RegionsRequestBody r) {
        //省
        List<CommonRegionModel> proviicialInformationList = new ArrayList<>();
        for (RegionModel regionModel : r.getZRTMDVD004()) {
            proviicialInformationList.add(copyRegionModel(regionModel));
        }
        //市
        List<CommonRegionModel> cityInformationList = new ArrayList<>();
        for (RegionModel regionModel : r.getZRTMDVD005()) {
            cityInformationList.add(copyRegionModel(regionModel));
        }
        //区
        List<CommonRegionModel> regionInformationList = new ArrayList<>();
        for (RegionModel regionModel : r.getZRTMDVD006()) {
            regionInformationList.add(copyRegionModel(regionModel));
        }
        return iFndRegionsCommonExternalService.saveFndRegions(proviicialInformationList, cityInformationList, regionInformationList);
    }
}
