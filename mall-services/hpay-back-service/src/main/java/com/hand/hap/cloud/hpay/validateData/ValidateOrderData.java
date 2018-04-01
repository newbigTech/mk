package com.hand.hap.cloud.hpay.validateData;

import com.hand.hap.cloud.hpay.data.OrderData;
import com.hand.hap.cloud.hpay.data.ReturnData;
import com.hand.hap.cloud.hpay.utils.AmonutUtils;
import com.hand.hap.cloud.hpay.utils.PropertiesUtil;
import org.apache.commons.lang.StringUtils;

/**
 * @author jiaxin.jiang
 * @Title com.hand.hap.cloud.hpay.validateData
 * @Description
 * @date 2017/8/8
 */
public class ValidateOrderData {

    /**
     * 验证订单信息
     *
     * @param orderData 订单信息
     * @return
     */
    public ReturnData validateOrderData(OrderData orderData) {

        ReturnData rd = new ReturnData();

        //验证金额是否有误
        if (!AmonutUtils.matcheAmount(orderData.getAmount())) {
            rd.setSuccess(false);
            rd.setMsgCode("data.OrderData.amount.fail");
            rd.setMsg("amount-fail");
            return rd;
        }
        if (StringUtils.isEmpty(orderData.getReturnUrl())) {
            rd.setSuccess(false);
            rd.setMsgCode("data.OrderData.returnUrl.null");
            rd.setMsg("returnUrl-null");
            return rd;
        }
        //验证mode是否为有效数据 mode-wechat alipay union
        if (!PropertiesUtil.getPayInfoValue("mode.unionpay").equals(orderData.getMode()) &&
                !PropertiesUtil.getPayInfoValue("mode.alipay").equals(orderData.getMode()) &&
                !PropertiesUtil.getPayInfoValue("mode.wechatpay").equals(orderData.getMode())) {

            rd.setSuccess(false);
            rd.setMsgCode("data.OrderData.mode.fail");
            rd.setMsg("mode-fail");
            return rd;
        }
        //验证支付模式是否有效 pc NATIVE APP H5 MWEB
        if (!PropertiesUtil.getPayInfoValue("mode.tradeType.js").equals(orderData.getTradeType()) &&
                !PropertiesUtil.getPayInfoValue("mode.tradeType.app").equals(orderData.getTradeType()) &&
                !PropertiesUtil.getPayInfoValue("mode.tradeType.native").equals(orderData.getTradeType()) &&
                !PropertiesUtil.getPayInfoValue("mode.tradeType.mweb").equals(orderData.getTradeType()) &&
                !PropertiesUtil.getPayInfoValue("mode.tradeType.pc").equals(orderData.getTradeType()) &&
                !PropertiesUtil.getPayInfoValue("mode.tradeType.h5").equals(orderData.getTradeType()) &&
                !PropertiesUtil.getPayInfoValue("mode.tradeType.JSAPI").equals(orderData.getTradeType())) {
            rd.setSuccess(false);
            rd.setMsgCode("data.OrderData.tradeType.fail");
            rd.setMsg("tradeType-fail");
            return rd;
        }
        //验证mode-union,tradeType-pc/H5
        if (PropertiesUtil.getPayInfoValue("mode.unionpay").equals(orderData.getMode())) {
            if (!PropertiesUtil.getPayInfoValue("mode.tradeType.pc").equals(orderData.getTradeType()) &&
                    !PropertiesUtil.getPayInfoValue("mode.tradeType.h5").equals(orderData.getTradeType())) {
                rd.setSuccess(false);
                rd.setMsgCode("ERROR-{" + orderData.getMode() + "}-{" + orderData.getTradeType() + "}");
                rd.setMsg("无法找到交易模式:{" + orderData.getMode() + "}-{" + orderData.getTradeType() + "}");
                return rd;
            }
        }
        //验证mode-wechat,tradeType-JS/APP(app支付)/NATIVE(原生扫码pc支付)/MWEB(手机h5支付)/JSAPI(公众号支付)
        if (PropertiesUtil.getPayInfoValue("mode.wechatpay").equals(orderData.getMode())) {
            if (!PropertiesUtil.getPayInfoValue("mode.tradeType.js").equals(orderData.getTradeType()) &&
                    !PropertiesUtil.getPayInfoValue("mode.tradeType.app").equals(orderData.getTradeType()) &&
                    !PropertiesUtil.getPayInfoValue("mode.tradeType.native").equals(orderData.getTradeType()) &&
                    !PropertiesUtil.getPayInfoValue("mode.tradeType.mweb").equals(orderData.getTradeType()) &&
                    !PropertiesUtil.getPayInfoValue("mode.tradeType.JSAPI").equals(orderData.getTradeType())) {
                rd.setSuccess(false);
                rd.setMsgCode("ERROR-{" + orderData.getMode() + "}-{" + orderData.getTradeType() + "}");
                rd.setMsg("无法找到交易模式:{" + orderData.getMode() + "}-{" + orderData.getTradeType() + "}");
                return rd;
            }
            if (PropertiesUtil.getPayInfoValue("mode.tradeType.mweb").equals(orderData.getTradeType())) {
                if (StringUtils.isEmpty(orderData.getIp())) {
                    rd.setSuccess(false);
                    rd.setMsgCode("ERROR-{" + orderData.getMode() + "}-null");
                    rd.setMsg("微信手机支付IP不能为空");
                    return rd;
                }
            }
            if (PropertiesUtil.getPayInfoValue("mode.tradeType.JSAPI").equals(orderData.getTradeType())) {
                if (StringUtils.isEmpty(orderData.getOpenId())) {
                    rd.setSuccess(false);
                    rd.setMsgCode("ERROR-{" + orderData.getMode() + "}-null");
                    rd.setMsg("微信公众号支付openid不能为空");
                    return rd;
                }
            }
        }
        //验证mode-ALIPAY,tradeType-pc/h5
        if (PropertiesUtil.getPayInfoValue("mode.alipay").equals(orderData.getMode())) {
            if (!PropertiesUtil.getPayInfoValue("mode.tradeType.pc").equals(orderData.getTradeType()) &&
                    !PropertiesUtil.getPayInfoValue("mode.tradeType.h5").equals(orderData.getTradeType())) {
                rd.setSuccess(false);
                rd.setMsgCode("ERROR-{" + orderData.getMode() + "}-{" + orderData.getTradeType() + "}");
                rd.setMsg("无法找到交易模式:{" + orderData.getMode() + "}-{" + orderData.getTradeType() + "}");
                return rd;
            }
            //验证mode-ALIPAY,tradeType-pc,qrPayMode-1/2/3/4/0
            if (PropertiesUtil.getPayInfoValue("mode.tradeType.pc").equals(orderData.getTradeType())) {
                if (StringUtils.isEmpty(orderData.getQrPayMode())) {
                    rd.setMsgCode("data.orderData.qrPayMode.null");
                    rd.setMsg("qrPayMode-null");
                    rd.setSuccess(false);
                    return rd;
                }
//            判断支付模式编码是否正确
//            0：订单码-简约前置模式，对应 iframe 宽度不能小于600px，高度不能小于300px；
//            1：订单码-前置模式，对应iframe 宽度不能小于 300px，高度不能小于600px；
//            3：订单码-迷你前置模式，对应 iframe 宽度不能小于 75px，高度不能小于75px；
//            4：订单码-可定义宽度的嵌入式二维码，商户可根据需要设定二维码的大小。
//            跳转模式下，用户的扫码界面是由支付宝生成的，不在商户的域名下。
//            2：订单码-跳转模式
                if (!(PropertiesUtil.getPayInfoValue("mode.alipay.qrPayMode0").equals(orderData.getQrPayMode()))
                        && !(PropertiesUtil.getPayInfoValue("mode.alipay.qrPayMode1").equals(orderData.getQrPayMode()))
                        && !(PropertiesUtil.getPayInfoValue("mode.alipay.qrPayMode2").equals(orderData.getQrPayMode()))
                        && !(PropertiesUtil.getPayInfoValue("mode.alipay.qrPayMode3").equals(orderData.getQrPayMode()))
                        && !(PropertiesUtil.getPayInfoValue("mode.alipay.qrPayMode4").equals(orderData.getQrPayMode()))
                        ) {
                    rd.setMsgCode("data.orderData.qrPayMode.fail");
                    rd.setMsg("qrPayMode-fail");
                    rd.setSuccess(false);
                    return rd;
                }
            }
        }
        rd.setMsgCode(PropertiesUtil.getPayInfoValue("success"));
        rd.setMsg(PropertiesUtil.getPayInfoValue("success"));
        rd.setSuccess(true);
        return rd;
    }
}
