package com.hand.hmall.mst.controllers;

import com.hand.common.util.ExcelUtil;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.mst.dto.Pricerow;
import com.hand.hmall.mst.service.IPricerowService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 价格行对象的Controller层
 * @date 2017/7/10 14:37
 */
@Controller
public class PricerowController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IPricerowService service;

    @RequestMapping(value = "/hmall/mst/pricerow/query")
    @ResponseBody
    public ResponseData query(Pricerow dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/mst/pricerow/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<Pricerow> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hmall/mst/pricerow/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<Pricerow> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }


    /**
     * 商品详情页查询价格详情
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hmall/mst/pricerow/queryInfo")
    @ResponseBody
    public ResponseData queryInfo(Pricerow dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                  @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryInfo(dto, page, pageSize));
    }

    /**
     * @param request
     * @param dto
     * @return
     * @description 商品详情信息中 删除价格行关联
     */
    @RequestMapping(value = "/hmall/mst/pricerow/deleteRelation")
    @ResponseBody
    public ResponseData deleteRelation(HttpServletRequest request, @RequestBody List<Pricerow> dto) {
        IRequest requestCtx = createRequestContext(request);
        if (dto != null && dto.size() > 0) {
            for (int i = 0; i < dto.size(); i++) {
                service.updatePricerow(dto.get(i));
            }
        }
        return new ResponseData();
    }

    /**
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     * @description 价格维护界面查询功能
     */
    @RequestMapping(value = "/hmall/mst/pricerow/selectPricerow")
    @ResponseBody
    public ResponseData selectPricerow(Pricerow dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                       @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.selectPricerow(requestCtx, dto, page, pageSize));
    }

    /**
     * 下载价格行excel模板
     *
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "/hmall/mst/pricerow/downloadExcelModel", method = RequestMethod.GET)
    public void importExcel(HttpServletRequest request, HttpServletResponse response) {
        try {
            new ExcelUtil(Pricerow.class).downloadExcelModel(request, response, "template/pricerow.xlsx");
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * 价格行信息Excel导入
     *
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "/hmall/mst/pricerow/importExcel", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData importExcel(HttpServletRequest request, MultipartFile file) {
        IRequest iRequest = this.createRequestContext(request);
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);

        List<Pricerow> list = null;
        try {
            list = new ExcelUtil<Pricerow>(Pricerow.class).importExcel(file.getOriginalFilename(), "价格行信息列表", file.getInputStream());
        } catch (Exception e) {
            logger.error(e.getMessage());
            responseData.setSuccess(false);
            responseData.setMessage("excel解析失败,请联系管理员！");
            return responseData;
        }

        if (list != null && list.size() > 0) {
            try {
                Map<String,Object> map =  service.checkPricerow(iRequest, list);
                String result =(String)map.get("result");
                List<Pricerow> pricerowList = (List<Pricerow>)map.get("pricerowList");

                if(CollectionUtils.isNotEmpty(pricerowList)){
                    for (Pricerow pricerow : pricerowList) {
                        try {
                            service.insertSelective(iRequest, pricerow);
                        } catch (Exception e) {
                            logger.error(e.getMessage());
                            responseData.setSuccess(false);
                            responseData.setMessage("插入价格行信息数据失败！");
                            return responseData;
                        }

                    }
                }
                if (result != null) {
                    responseData.setSuccess(false);
                    responseData.setMessage(result);
                    return responseData;
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                responseData.setSuccess(false);
                responseData.setMessage("数据校验失败！");
                return responseData;
            }

        }

        return responseData;
    }

}