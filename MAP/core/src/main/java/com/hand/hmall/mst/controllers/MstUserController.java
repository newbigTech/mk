package com.hand.hmall.mst.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.mst.dto.MstUser;
import com.hand.hmall.mst.service.IMstUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 用户对象的Controller层
 * @date 2017/7/10 14:37
 */
@Controller
public class MstUserController extends BaseController {

    @Autowired
    private IMstUserService service;


    @RequestMapping(value = "/hmall/mst/user/query")
    @ResponseBody
    public ResponseData query(MstUser dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/mst/user/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<MstUser> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hmall/mst/user/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<MstUser> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    /**
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     * @description 用户查询界面数据查询, 关联了用户组表，用户映射表
     */
    @RequestMapping(value = "/hmall/mst/user/queryInfo")
    @ResponseBody
    public ResponseData queryInfo(MstUser dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                  @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryInfo(requestContext, dto, page, pageSize));
    }
}