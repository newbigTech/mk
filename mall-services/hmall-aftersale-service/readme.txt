hmall-aftersale-service:

1. 售后相关的逻辑实现

2. ServiceOrderController              服务单信息功能
   2.1 createServiceOrder              创建发货单
   2.2 queryByOrderCode                根据订单编号查询服务单及相关售后单信息
   2.3 queryServiceOrder               根据订单单号查询服务单

3. StatusUpdateController              售后单据状态更新
   3.1 queryStatusLogs                 根据服务单id返回状态日志信息