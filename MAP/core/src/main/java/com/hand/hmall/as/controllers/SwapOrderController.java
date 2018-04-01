package com.hand.hmall.as.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.as.dto.ServiceorderEntry;
import com.hand.hmall.as.dto.SwapOrder;
import com.hand.hmall.as.service.IServiceorderEntryService;
import com.hand.hmall.as.service.ISwapOrderService;
import com.hand.hmall.common.service.ISequenceGenerateService;
import com.hand.hmall.mst.dto.ProductCategory;
import com.hand.hmall.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author: zhangmeng01
 * @version: 0.1
 * @name: SwapOrderController
 * @description: 换货单controller 换货单使用了新的表 上线前不做了 上线之后在做 2017/7/24
 * @Date: 2017/7/21
 */
@Controller
public class SwapOrderController extends BaseController {
    @Autowired
    private ISwapOrderService service;

    @Autowired
    private IServiceorderEntryService serviceorderEntryService;

    @Autowired
    private ISequenceGenerateService sequenceGenerateService;

    /**
     * 查询换货单详细信息
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hmall/as/swapOrder/selectSwapOrderById")
    @ResponseBody
    public ResponseData selectSwapOrderById(SwapOrder dto) {
        return new ResponseData(service.selectSwapOrderById(dto));
    }

    /**
     * 保存换货单信息
     *
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hmall/as/swapOrder/saveSwapOrder")
    @ResponseBody
    public SwapOrder saveCategory(HttpServletRequest request, @RequestBody List<SwapOrder> dto) {
        IRequest iRequest = this.createRequestContext(request);
        SwapOrder swapOrder = null;
        if (dto != null) {
            swapOrder = service.selectByPrimaryKey(iRequest, dto.get(0));
            //更新
            if (swapOrder != null) {
                if (Constants.FINI.equals(dto.get(0).getStatus()) && dto.get(0).getFinishTime() == null) {
                    dto.get(0).setFinishTime(new Date());
                }
                swapOrder = service.updateByPrimaryKeySelective(iRequest, dto.get(0));
            }//新增
            else {
                //暂时写死
                Random r = new Random();
                dto.get(0).setCode((r.nextInt(999999) + 1) + "");
                swapOrder = service.insertSelective(iRequest, dto.get(0));
            }
            if (swapOrder != null) {
                //保存退货单行
                if (dto.get(0).getReturnOrderEntries() != null && dto.get(0).getReturnOrderEntries().size() > 0) {
                    saveSwapOrderEntry(dto.get(0).getReturnOrderEntries(), swapOrder, iRequest);
                }
                //保存换货单行
                if (dto.get(0).getChangeOrderEntries() != null && dto.get(0).getChangeOrderEntries().size() > 0) {
                    saveSwapOrderEntry(dto.get(0).getChangeOrderEntries(), swapOrder, iRequest);
                }
            }
        }
        return swapOrder;
    }

    /**
     * 查询商品
     *
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/hmall/as/swapOrder/queryProduct")
    @ResponseBody
    public ResponseData queryProduct(ProductCategory dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                     @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        return new ResponseData(service.queryProduct(dto, page, pageSize));
    }

    /**
     * 保存售后单行
     *
     * @param list      售后单行list
     * @param swapOrder 换货单对象
     * @param iRequest  request
     */
    private void saveSwapOrderEntry(List<ServiceorderEntry> list, SwapOrder swapOrder, IRequest iRequest) {
        ServiceorderEntry serviceorderEntry;
        for (int i = 0; i < list.size(); i++) {
            serviceorderEntry = serviceorderEntryService.selectByPrimaryKey(iRequest, list.get(i));
            if (serviceorderEntry != null) {
                serviceorderEntryService.updateByPrimaryKeySelective(iRequest, list.get(i));
            } else {
                list.get(i).setServiceOrderId(swapOrder.getReceiptOrderId());
                serviceorderEntryService.insertSelective(iRequest, list.get(i));
            }
        }
    }
}
