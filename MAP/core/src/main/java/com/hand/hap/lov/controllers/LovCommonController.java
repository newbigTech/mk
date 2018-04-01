package com.hand.hap.lov.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.mdm.item.dto.Item;
import com.hand.hap.mdm.item.service.IItemService;
import com.hand.hap.system.controllers.BaseController;
import com.markor.map.external.fndregionservice.dto.RegionDto;
import com.markor.map.external.fndregionservice.service.IFndRegionsCommonExternalService;
import com.markor.map.external.pointservice.dto.PointOfServiceDto;
import com.markor.map.external.pointservice.service.IPointOfServiceExternalService;
import com.markor.map.framework.common.interf.entities.PaginatedList;
import com.markor.map.framework.common.interf.entities.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * @author baihua
 * @version 0.1
 * @name LovController$  lov 自定义请求通用接口
 * @description $END$
 * @date 2017/12/28$
 */
@Controller
public class LovCommonController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(LovCommonController.class);

    @Autowired
    private IPointOfServiceExternalService iPointOfServiceExternalService;
    @Autowired
    private IFndRegionsCommonExternalService iFndRegionsCommonExternalService;
    @Autowired
    private IItemService iItemService;

    /**
     * 提货地点
     *
     * @param code
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/sys/lov/getInfoForLov")
    @ResponseBody
    public ResponseData getInfoForLov(String code, @RequestParam(defaultValue = DEFAULT_PAGE) int page, @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        if (code == null || "".equals(code))
            return new ResponseData(iPointOfServiceExternalService.selectAll());
        else {
            PointOfServiceDto pointOfServiceDto = iPointOfServiceExternalService.selectByCode(code);
            return new ResponseData(Arrays.asList(pointOfServiceDto));
        }
    }

    /**
     * 全部区域编码查询
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/sys/lov/selectAllCity")
    @ResponseBody
    public ResponseData selectAllCity(RegionDto dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page, @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        try {
            PaginatedList<RegionDto> regionDtos = iFndRegionsCommonExternalService.selectAllCity(dto, page, pageSize);
            return new ResponseData(regionDtos);
        } catch (InvocationTargetException e) {
            logger.error(this.getClass().getCanonicalName(), e);
        } catch (IllegalAccessException e) {
            logger.error(this.getClass().getCanonicalName(), e);
        }
        return null;
    }


    /**
     * 查找平台
     *
     * @param item
     * @param page
     * @param pageSize
     * @param request
     * @return
     * @date 2017/05/23
     */
    @RequestMapping(value = "/sys/lov/selectPlatform")
    @ResponseBody
    public ResponseData selectPlatform(Item item, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                       @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(iItemService.selectPlatform(item, requestContext, page, pageSize));

    }


}
