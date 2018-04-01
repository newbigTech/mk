hmall-od-service(订单的上层服务):

1. 主要是用于将商城的订单下载到中台提供接口
  1.1 商城调用hmall-api-gateway,hmall-api-gateway调用hmall-od-service
  1.2 hmall-od-service调用order

2. 接口调用入口:OrderCreateController
  2.1 addOrder      订单下载
  2.2 updateOrder   订单更新