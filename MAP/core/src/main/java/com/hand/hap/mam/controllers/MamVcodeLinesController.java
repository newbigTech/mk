package com.hand.hap.mam.controllers;

/**
 * @author xiaowei.zhang@hand-china.com
 * @version 0.1
 * @name MamVcodeLinesController
 * @description v码行controller
 * @date 2017/5/28
 */

import com.hand.hap.core.IRequest;
import com.hand.hap.im.exception.ImageDownloadException;
import com.hand.hap.im.service.IImageDownloadService;
import com.hand.hap.mam.dto.*;
import com.hand.hap.mam.service.IMamVcodeHeaderService;
import com.hand.hap.mam.service.IMamVcodeLinesService;
import com.hand.hap.mdm.item.dto.Item;
import com.hand.hap.mdm.item.service.IItemService;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hmall.services.order.service.IHMallOrderService;
import com.hand.hmall.util.StringUtils;
import com.markor.map.framework.common.exception.BusinessException;
import com.markor.map.framework.common.interf.entities.PaginatedList;
import com.markor.map.framework.common.interf.entities.ResponseData;
import com.markor.map.oms.art241.order.service.IARTOrderServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MamVcodeLinesController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(MamVcodeLinesController.class);
    @Autowired
    private IMamVcodeLinesService service;
    @Autowired
    private IImageDownloadService iImageDownloadService;
    @Autowired
    private IHMallOrderService ihMallOrderService;
    @Autowired
    private IMamVcodeHeaderService mamVcodeHeaderService;
    @Autowired
    private IItemService iItemService;
    @Autowired
    private IARTOrderServiceProvider iartOrderServiceProvider;

    /**
     * V码行查询
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/mam/vcode/lines/query")
    @ResponseBody
    public ResponseData query(MamVcodeLines dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectLineData(requestContext, dto, page, pageSize));
    }


    /**
     * 查找选配结果
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     * @author yanjie.zhang@hand-china.com
     * @date 2017/06/02
     */
    @RequestMapping(value = "/hap/mam/vcode/lines/selectMamResult")
    @ResponseBody
    public ResponseData selectMamResult(MamVcodeLines dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        ResponseData rd = null;
        if ("true".equals(dto.getIsQuery())) {
            return new ResponseData(service.selectMamResult(dto, requestContext, page, pageSize));
        } else {
            return new ResponseData();
        }
    }

    /**
     * 通过V码查找选配结果
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     * @author qinzhipeng
     * @date 2017/07/24
     */
    @RequestMapping(value = "/hap/mam/vcode/lines/selectMamResultByVCode")
    @ResponseBody
    public ResponseData selectMamResultByVCode(@RequestBody MamVcodeLines dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                               @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        ResponseData rd = null;
        if ("true".equals(dto.getIsQuery())) {
            return new ResponseData(service.selectMamResultByVCode(dto, requestContext, page, pageSize));
        } else {
            return new ResponseData();
        }
    }


    /**
     * 通过V码查找选配结果
     *
     * @param dto
     * @return
     * @author qinzhipeng
     * @date 2017/07/24
     */
    @RequestMapping(value = "/hap/mam/vcode/lines/selectMamResultReview")
    @ResponseBody
    public ResponseData selectMamResultReview(@RequestBody MamSoApproveHis dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        String result = service.selectMamResultReview(dto, requestContext);
        return returnResult(result);
    }

    /**
     * 查询V码是否被审核
     *
     * @param dto
     * @return
     * @author heng.zhang04
     * @date 2017/07/24
     */
    @RequestMapping(value = "/hap/mam/vcode/lines/checkMamOccupy")
    @ResponseBody
    public ResponseData checkMamOccupy(@RequestBody MamSoApproveHis dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        ResponseData rsd = new ResponseData();
        try {
            Boolean result = service.checkMamOccupy(dto, requestContext);
            if (result) {
                rsd.setSuccess(result);
            }
        } catch (Exception e) {
            rsd.setSuccess(false);
            rsd.setMessage(e.getMessage());
        }
        return rsd;
    }

    /**
     * 审核主推款选配结果
     *
     * @param dto
     * @return
     * @author qinzhipeng
     * @date 2017/07/24
     */
    @RequestMapping(value = "/hap/mam/vcode/lines/selectZDResultReview")
    @ResponseBody
    public ResponseData selectZDResultReview(@RequestBody MamApproveHis dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        String result = service.selectZDResultReview(dto, requestContext);
        return returnResult(result);
    }

    private ResponseData returnResult(String result) {
        if (result.equals("success")) {
            ResponseData responseData = new ResponseData(true);
            responseData.setMessage(result);
            return responseData;
        } else {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(result);
            return responseData;
        }
    }

    /**
     * 校验
     *
     * @param dto
     * @return
     * @author heng.zhang
     * @date 2017/09/08
     */
    @RequestMapping(value = "/hap/mam/vcode/lines/checkOccupy")
    @ResponseBody
    public ResponseData checkOccupy(@RequestBody MamApproveHis dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        ResponseData rsd = new ResponseData();
        try {
            Boolean result = service.checkOccupy(dto, requestContext);
            if (result) {
                rsd.setSuccess(result);
            }
        } catch (Exception e) {
            rsd.setSuccess(false);
            rsd.setMessage(e.getMessage());
        }
        return rsd;
    }

    @RequestMapping(value = "/hap/mam/vcode/lines/img/download")
    public ResponseData downloadImg(String code, String type, HttpServletResponse response) {
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);
        try {
            String result = service.getImageIoStream(type, code);
            List<String> list = new ArrayList<>();
            list.add(result);
            responseData.setRows(list);
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage("读取IO流发生异常！");
            logger.error(responseData.getMessage(), e);
            return responseData;
        }
        return responseData;
    }

    @RequestMapping(value = "/hap/mam/vcode/lines/img/getRemoteImage")
    public ResponseData getRemoteImage(HttpServletRequest request, @RequestParam String vcode, @RequestParam String pin) {
        ResponseData rpd = new ResponseData();
        IRequest iRequest = createRequestContext(request);
        try {
            // 先检查是否为主推款，如果是，返回错误信息
            MamVcodeHeader header = new MamVcodeHeader();
            header.setVcode(vcode);
            List<MamVcodeHeader> list = mamVcodeHeaderService.headerInfo(header);
            if (list.size() > 0 && list.get(0).getTypeCode().contains("D")) {
                String platformCode = list.get(0).getPlatformCode();
                Item item = new Item();
                item.setItemCode(platformCode);
                PaginatedList<Item> platform = iItemService.selectPlatform(item,iRequest,1,Integer.MAX_VALUE);
                if (platform.getTotalCount() > 0) {
                    String brand = platform.getRows().get(0).getBrand();
                    if ("ZEST".equals(brand)) {
                        // 查询此V码的父级V码
                        vcode = ihMallOrderService.getParentVCode(pin, vcode);
                        if (!StringUtils.isEmpty(vcode)) {
                            // 下载图片
                            iImageDownloadService.downloadImg(vcode);
                            rpd.setSuccess(true);
                        } else {
                            rpd.setSuccess(false);
                            rpd.setMessage("不存在此V码与PIN码的订单行。");
                        }
                    } else if ("241".equals(brand)) {
                        String parentSku = iartOrderServiceProvider.getParentSku(pin);
                        Map<String,String> map = new HashMap<>();
                        map.put(vcode, parentSku);
                        iImageDownloadService.downloadImg(map);
                        rpd.setSuccess(true);
                    }
                }
            } else {
                rpd.setSuccess(false);
                rpd.setMessage("此V码为主推款，请在【主推款效果图管理】菜单中重新下载图片。");
            }
        } catch (ImageDownloadException e) {
            rpd.setSuccess(false);
            rpd.setMessage(e.getMessage());
        } catch (BusinessException e) {
            rpd.setSuccess(false);
            rpd.setMessage(e.getMessage());
        }
        return rpd;
    }

    @RequestMapping(value = "/hap/mam/vcode/lines/suitimg/download")
    public ResponseData downloadSuitImg(@RequestBody PinVcodeDto dto) {
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);
        try {
            List<String> list = service.getSuitImageIoStream(dto);
            if (list != null) {
                responseData.setRows(list);
            } else {
                responseData.setSuccess(false);
                responseData.setMessage("没有找到对应的订单行。");
            }
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
            logger.error(responseData.getMessage(), e);
        }

        return responseData;
    }

    @RequestMapping(value = "/hap/mam/vcode/lines/Z/img/download")
    public ResponseData downloadZImg(String type, String code) {
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);
        try {
            List<String> result = service.getZImageIoStream(type, code);
            responseData.setRows(result);
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage("读取IO流发生异常！");
            logger.error(responseData.getMessage(), e);
            return responseData;
        }
        return responseData;
    }
}