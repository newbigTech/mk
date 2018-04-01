package com.hand.hmall.mst.controllers;

import com.hand.common.util.ExcelUtil;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.mst.dto.Maincarriage;
import com.hand.hmall.mst.dto.Subcarriage;
import com.hand.hmall.mst.service.ISubcarriageService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 支线运费的Controller层
 * @date 2017/7/10 14:37
 */
@Controller
public class SubcarriageController extends BaseController {

    @Autowired
    private ISubcarriageService service;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/hmall/mst/subcarriage/query")
    @ResponseBody
    public ResponseData query(Subcarriage dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/mst/subcarriage/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<Subcarriage> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hmall/mst/subcarriage/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<Subcarriage> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    /**
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     * @description 支线运费查询界面
     */
    @RequestMapping(value = "/hmall/mst/subcarriage/selectSubcarriage")
    @ResponseBody
    public ResponseData selectSubcarriage(Subcarriage dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                          @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectSubcarriage(requestContext, dto, page, pageSize));
    }

    /**
     * 下载主干运费列表excel模板
     *
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "/hmall/mst/subcarriage/downloadExcelModel", method = RequestMethod.GET)
    public void importExcel(HttpServletRequest request, HttpServletResponse response) {
        new ExcelUtil(Subcarriage.class).downloadExcelModel(request, response, "支线运费列表导入模板.xlsx", "支线运费信息列表");
    }

    /**
     * 支线运费信息Excel导入
     *
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "/hmall/mst/subcarriage/importExcel", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData importExcel(HttpServletRequest request, MultipartFile file) {
        IRequest iRequest = this.createRequestContext(request);
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);

        List<Subcarriage> list = null;
        try {
            list = new ExcelUtil<Subcarriage>(Subcarriage.class).importExcel(file.getOriginalFilename(), "支线运费信息列表", file.getInputStream());
        } catch (Exception e) {
            responseData.setMessage("excel解析失败,请联系管理员！");
            responseData.setSuccess(false);
            logger.error(this.getClass().getCanonicalName() + "：：" + responseData.getMessage(), e);
            return responseData;
        }

        Map<String, Object> currentMap = new ConcurrentHashMap<String, Object>();
        StringBuffer errorBuffer = new StringBuffer();
        //添加excel数据中是否存在重复校验
        //通过承运商编码+承运商类型+区编码+始发地+计价方式+差额比例 确认唯一记录
        if (list != null && list.size() > 0) {
            for (Subcarriage m : list) {
                String logisticscoCode = m.getLogisticscoCode();
                String shippingType = m.getShippingType();
                String districtCode = m.getDistrictCode();
                String origin = m.getOrigin();
                String priceMode = m.getPriceMode();
                StringBuffer sb = new StringBuffer();
                sb.append(logisticscoCode);
                sb.append("-");
                sb.append(shippingType);
                sb.append("-");
                sb.append(districtCode);
                sb.append("-");
                sb.append(origin);
                sb.append("-");
                sb.append(priceMode);
                Object o = currentMap.get(sb.toString());
                if (o == null) {
                    currentMap.put(sb.toString(), m);
                } else {
                    errorBuffer.append(sb.toString());
                    errorBuffer.append("\r\n");
                }
            }
        }
        if (errorBuffer.toString() != null && !errorBuffer.toString().equals("")) {
            logger.error(errorBuffer.toString());
            responseData.setSuccess(false);
            responseData.setMessage(errorBuffer.toString());
            return responseData;
        }

        if (list != null && list.size() > 0) {
            Map<String, Object> map = null;
            try {
                map = service.checkSubcarriage(iRequest, list);
            } catch (InvocationTargetException e) {
                responseData.setSuccess(false);
                responseData.setMessage("处理异常,请联系管理员！");
                logger.error(this.getClass().getCanonicalName() + "：：" + responseData.getMessage(), e);
            } catch (IllegalAccessException e) {
                responseData.setSuccess(false);
                responseData.setMessage("处理异常,请联系管理员！！");
                logger.error(this.getClass().getCanonicalName() + "：：" + responseData.getMessage(), e);
            }
            String result = (String) map.get("result");
            List<Subcarriage> subcarriageList = (List<Subcarriage>) map.get("subcarriageList");

            if (CollectionUtils.isNotEmpty(subcarriageList)) {
                for (Subcarriage subcarriage : subcarriageList) {
                    //插入或更新数据
                    try {
                        if (subcarriage.getSubcarriageId() != null) {
                            service.updateByPrimaryKeySelective(iRequest, subcarriage);
                        } else {
                            service.insertSelective(iRequest, subcarriage);
                        }
                    } catch (Exception e) {
                        responseData.setSuccess(false);
                        responseData.setMessage("插入支线运费数据失败！");
                        logger.error(this.getClass().getCanonicalName() + "：：" + responseData.getMessage(), e);
                    }
                }
            }
            if (result != null) {
                responseData.setSuccess(false);
                responseData.setMessage(result);
                return responseData;
            }
        }
        return responseData;
    }

}