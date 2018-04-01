package com.hand.hmall.fnd.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.markor.map.external.fndregionservice.service.IFndRegionsCommonExternalService;
import com.markor.map.fndregionservice.dto.Region;
import com.markor.map.fndregionservice.service.IRegionService;
import com.markor.map.framework.common.interf.entities.PaginatedList;
import com.markor.map.framework.common.interf.entities.ResponseData;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name RegionsController
 * @description 地区controllers
 * @date 2017年5月26日10:52:23
 */
@Controller
public class RegionsController extends BaseController {

    @Autowired
    private IRegionService iRegionService;
    @Autowired
    private IFndRegionsCommonExternalService iFndRegionsCommonExternalService;

    /**
     * 保存添加地区信息
     *
     * @param regions 区域对象集合
     * @param request 请求
     * @return 返回保存的结果
     */
    @RequestMapping(value = "/fnd/regions/insert")
    @ResponseBody
    public ResponseData insert(HttpServletRequest request, @RequestBody List<Region> regions) {
        IRequest requestCtx = this.createRequestContext(request);
        ResponseData rd = new ResponseData();
        if (CollectionUtils.isEmpty(regions)) {
            rd.setSuccess(false);
            rd.setMessage("没有要保存的数据");
            return rd;
        }
        for (Region region : regions) {
            if (("add").equals(region.get__status())) {
                iRegionService.addRegionBInfo(requestCtx, region);
            }
        }
        return new ResponseData(regions);
    }

    /**
     * 保存修改地区信息
     *
     * @param regions 区域对象集合
     * @param request 请求
     * @return 返回修改的结果
     */
    @RequestMapping(value = "/fnd/regions/update")
    @ResponseBody
    public ResponseData update(@RequestBody List<Region> regions, HttpServletRequest request) {
        IRequest requestCtx = this.createRequestContext(request);
        ResponseData rd = new ResponseData();
        if (CollectionUtils.isEmpty(regions)) {
            rd.setSuccess(false);
            rd.setMessage("没有要保存的数据");
            return rd;
        }
        for (Region region : regions) {
            if ("update".equals(region.get__status())) {
                iRegionService.addRegionBInfo(requestCtx, region);
            }
        }
        return new ResponseData(regions);
    }

    /**
     * 按parentId，用户输入的地区名称 查找地区
     *
     * @param region   区域对象
     * @param request  请求
     * @param page     页数
     * @param pagesize 每页显示数量
     * @return 返回查询出的结果
     */
    @RequestMapping(value = "/fnd/regions/findByCondition")
    @ResponseBody
    public ResponseData findByCondition(Region region, HttpServletRequest request, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pagesize) {
        ResponseData rd = new ResponseData();
        try {
            IRequest requestContext = this.createRequestContext(request);
            String lang = requestContext.getLocale();
            region.setLang(lang);
            PaginatedList<Region> regionses = iRegionService.findByCondition(region, page, pagesize);
            rd = new ResponseData(regionses);
        } catch (Exception e) {
            rd.setSuccess(false);
            rd.setMessage(e.getMessage());
            e.printStackTrace();
        }
        return rd;

    }

    /**
     * 查询出所有国家信息
     *
     * @return 返回查询出的结果
     */
    @RequestMapping(value = "/fnd/regions/getCountry")
    @ResponseBody
    public ResponseData getCountry() {
        return new ResponseData(iFndRegionsCommonExternalService.getCountry());
    }

    /**
     * 根据国家编码查询出对应的省份
     *
     * @param countryCode 国家编码
     * @return 返回查询出的结果
     */
    @RequestMapping(value = "/fnd/regions/getState")
    @ResponseBody
    public ResponseData getState(@RequestParam(value = "countryCode") String countryCode) {
        return new ResponseData(iFndRegionsCommonExternalService.getState(countryCode));
    }

    /**
     * 根据省份查询出对应的城市
     *
     * @param stateCode 省份编码
     * @return 返回查询出的结果
     */
    @RequestMapping(value = "/fnd/regions/getCity")
    @ResponseBody
    public ResponseData getCity(@RequestParam(value = "stateCode") String stateCode) {
        return new ResponseData(iFndRegionsCommonExternalService.getCity(stateCode));
    }

    /**
     * 根据城市查询出对应的区县
     *
     * @param cityCode 城市编码
     * @return 返回查询出的结果
     */
    @RequestMapping(value = "/fnd/regions/getArea")
    @ResponseBody
    public ResponseData getArea(@RequestParam(value = "cityCode") String cityCode) {
        return new ResponseData(iFndRegionsCommonExternalService.getArea(cityCode));
    }
}
