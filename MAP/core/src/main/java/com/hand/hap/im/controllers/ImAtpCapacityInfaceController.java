package com.hand.hap.im.controllers;

/**
 * @author yanjie.zhang@hand-china.com
 * @version 0.1
 * @name ImAtpCapacityInfaceController
 * @description ATP基础数据Controller
 * @date 2017/6/21
 **/

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;

//此controller为自动生成的类，因影响接口测试，为此先注释掉，该类方法以后可能启用  所以先留下
@Controller
public class ImAtpCapacityInfaceController extends BaseController {

    /* @Autowired
    private IImAtpCapacityInfaceService service;


   @RequestMapping(value = "/hap/im/atp/capacity/inface/query")
    @ResponseBody
    public ResponseData query(ImAtpCapacityInface dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hap/im/atp/capacity/inface/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<ImAtpCapacityInface> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hap/im/atp/capacity/inface/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<ImAtpCapacityInface> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }*/
}