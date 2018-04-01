package com.hand.hmall.logistics.exception;

/**
 * @author chenzhigang
 * @version 0.1
 * @name ImportLogisticsException
 * @Description: 导入WMS物流数据时，特定销售订单数据导入失败时抛出此异常
 * @date 2017/8/18
 */
public class ImportLogisticsException extends RuntimeException {

    /**
     * 物流数据导入失败原因编码
     */
    private String msgCode;

    public ImportLogisticsException(String msgCode, String message) {
        super(message);
        this.msgCode = msgCode;
    }

    public String getMsgCode() {
        return msgCode;
    }
}
