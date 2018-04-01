package com.hand.hmall.activites.controllers;

import com.hand.hmall.activites.service.IWorkflowService;
import com.hand.hmall.as.service.IAsRefundService;
import com.hand.hmall.dto.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * @author chenzhigang
 * @version 0.1
 * @name WorkflowController
 * @description 工作流控制器
 * @date 2017/11/10
 */
@RestController
@RequestMapping("/workflow")
public class WorkflowController {

    @Autowired
    private IWorkflowService service;

    @Autowired
    private IAsRefundService refundService;

    /**
     * 根据退款单ID查询退款单信息，如果查不到，根据流程实例ID查询
     * @param asRefundOrderId
     * @param procinstId
     * @return
     */
    @GetMapping("/asRefundInfo")
    public ResponseData queryAsRefundInfo(@RequestParam(value = "asRefundOrderId", defaultValue = "-1") Long asRefundOrderId,
                                          @RequestParam(value = "procinstId", defaultValue = "-1") Long procinstId) {
        return new ResponseData(Arrays.asList(asRefundOrderId > -1
                ? service.queryAsRefundInfo(asRefundOrderId)
                : service.queryAsRefundInfoByProcinstId(procinstId)));
    }

}
