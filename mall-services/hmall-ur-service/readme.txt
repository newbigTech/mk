hmall-ur-service(用户的上层服务):

1. 主要是用于用户登录,注册,发送短信验证码等接口
  1.1 商城调用hmall-api-gateway,hmall-api-gateway调用hmall-ur-service
  1.2 hmall-ur-service调用user

2. 接口调用入口:UserController
  2.1 query                     用户查询
  2.2 login                     用户登录
  2.3 register                  用户注册
  2.4 update                    用户更新
  2.5 checkMobile               用户手机号校验
  2.6 createCheckcode           用户生成手机验证码并发送短信
  2.7 checkcode                 手机验证码校验
  2.8 sendMSG                   前台发送短信