package com.hand.hap.mam.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.mam.dto.MamImageDownload;
import com.hand.hap.mam.service.IMamImageDownloadService;
import com.hand.hap.system.controllers.BaseController;
import com.markor.map.framework.common.interf.entities.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
    public class MamImageDownloadController extends BaseController{

    @Autowired
    private IMamImageDownloadService service;


    @RequestMapping(value = "/hap/mam/image/download/query")
    @ResponseBody
    public ResponseData query(MamImageDownload dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryImageLog(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hap/mam/image/download/downloadImage")
    @ResponseBody
    public Map<String, String> downloadImage(@RequestBody Map map) {
        // retrun map
        Map<String, String> rd;
        // vcode
        String vcode = map.get("vcode").toString();
        rd = service.downloadImage(vcode);
        return rd;
    }
    }