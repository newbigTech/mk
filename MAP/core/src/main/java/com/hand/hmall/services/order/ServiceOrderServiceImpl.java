package com.hand.hmall.services.order;

import com.hand.hmall.as.service.ISvsaleOrderService;
import com.hand.hmall.om.dto.Paymentinfo;
import com.hand.hmall.services.order.dto.PaymentInfo;
import com.hand.hmall.services.order.service.IServiceOrderService;
import com.hand.hmall.services.utils.BeanConvertUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author alaowan
 * Created at 2017/12/28 18:47
 * @description 服务销售单服务
 */
public class ServiceOrderServiceImpl implements IServiceOrderService {

    @Autowired
    private ISvsaleOrderService innerService;

    public ServiceOrderServiceImpl() {
        BeanConvertUtil.registerBeanConvertors();
    }

    @Override
    public void saveServOrderPaymentInfo(PaymentInfo paymentInfo) {
        Paymentinfo info = new Paymentinfo();
        try {
            BeanUtils.copyProperties(info, paymentInfo);
            innerService.saveSvsalesPaymentinfo(info);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
