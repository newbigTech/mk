order(订单的下层服务):

1. 主要用于订单下载/更新的逻辑实现
  1.1 hmall-od-service调用order
  1.2 order调用hmall-thirdparty-service

2. OrderCreateController(订单下载/更新接口入口)
  2.1 addOrder      订单下载
  2.2 updateOrder   订单更新
  2.3 validateEWCS  校验 平台订单号(escOrderCode)、网站(webSiteCode)、渠道(channelCode)、店铺(storeCode) 是否为空

3. PaymentInfoController
  3.1 insertPaymentInfo   用户购买优惠券支付信息插入接口