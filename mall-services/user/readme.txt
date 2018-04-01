hmall-ur-service(用户的下层服务):

1. 主要是用于用户登录,注册,发送短信验证码等接口的逻辑实现
   1.1 hmall-ur-service调用user

2. UserController
   2.1 queryByCustomerId         用户查询(根据用户id查询)
   2.2 login                     用户登录
   2.3 register                  用户注册

3. UserCheckCodeController
   3.1 checkcode                 手机验证码校验

4. UserCreateCheckCodeController
   4.1 createCheckCode           用户生成手机验证码并发送短信

5. UserInfoController
   5.1 updateInfo                用户更新
   5.2 queryByCondition          中台促销查询用户信息
   5.3 queryNotEqual             中台促销批量查询已选用户信息

6. UserMobileCheckController
   6.1 checkMobile               手机号码校验

7. UserSendMessageController
   7.1 sendMSG                   前台发送短信