package com.hand.hmall.service.impl;

import com.hand.hmall.code.MessageCode;
import com.hand.hmall.dto.*;
import com.hand.hmall.mapper.HmallFndGlobalVariantMapper;
import com.hand.hmall.mapper.HmallMstCustomerCheckMapper;
import com.hand.hmall.mapper.HmallOmMailteplateMapper;
import com.hand.hmall.service.IUserCreateCheckCodeService;
import com.hand.hmall.util.MD5EncryptionUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 阳赳
 * @name:IUserCreateCheckcodeServiceImpl
 * @Description:验证码的生成
 * @date 2017/6/1 15:32
 */
@Service
public class UserCreateCheckCodeServiceImpl implements IUserCreateCheckCodeService {

    private static final Logger logger = LoggerFactory.getLogger(UserCreateCheckCodeServiceImpl.class);

    @Resource
    private HmallMstCustomerCheckMapper hmallMstCustomerCheckMapper;

    @Autowired
    private HmallOmMailteplateMapper hmallOmMailteplateMapper;

    @Autowired
    private HmallFndGlobalVariantMapper hmallFndGlobalVariantMapper;

    //第三方短信平台的url
    @Value("${data.url}")
    private String url;

    /**
     * 将手机号码和随机生成的验证码绑定并插入数据库,不会发送短信
     */
    @Override
    public ResponseData insertOracle(String mobileNumber,String sendType)
    {
        //获取验证码
        String checkCode = this.createCheckCode();

        //验证手机号码的正则表达式
        String regex = "^((13[0-9])|(14[5,6,7,9])|(15[^4,\\D])|(16[6])|(17[0,1,3,5-8])|(18[0-9]))\\d{8}$";
        ResponseData mobileValiResp = new ResponseData();
        if (!mobileNumber.matches(regex))
        {
            mobileValiResp.setSuccess(false);
            mobileValiResp.setMsg(MessageCode.UR_MOBILE_022.getValue());
            mobileValiResp.setMsgCode("ur.createCheckcode.mobilenumber.illegal");
            return mobileValiResp;
        }

        //根据手机号码和业务类型得到一条验证码信息
        HmallMstCustomerCheck reallCustomer = hmallMstCustomerCheckMapper.selectByMobileAndSendType(mobileNumber, sendType);
        if (reallCustomer == null)
        {
            //插入一条新的验证码信息
            HmallMstCustomerCheck mstCustomerCheck = new HmallMstCustomerCheck();
            mstCustomerCheck.setMobile(mobileNumber);
            mstCustomerCheck.setSendType(sendType);
            mstCustomerCheck.setCheckcode(checkCode);
            hmallMstCustomerCheckMapper.insertSelective(mstCustomerCheck);
        }
        else
        {
            //验证码进行更新操作
            Long customerCheckId = reallCustomer.getCustomercheckId();
            HmallMstCustomerCheck hmallMstCustomerWithCheckCode = new HmallMstCustomerCheck();
            hmallMstCustomerWithCheckCode.setCustomercheckId(customerCheckId);
            hmallMstCustomerWithCheckCode.setCheckcode(checkCode);
            //更新时间
            hmallMstCustomerWithCheckCode.setLastUpdateDate(new Date());
            //更新该字段为:false(短信验证码验证成功后该字段设置为:true)
            hmallMstCustomerWithCheckCode.setAttribute1("false");
            hmallMstCustomerCheckMapper.updateByPrimaryKeySelective(hmallMstCustomerWithCheckCode);
        }

        //将验证码返回给前台
        ResponseData responseData = new ResponseData();
        Map<String, Object> mapCheckCode = new HashMap();
        List list = new ArrayList<>();
        mapCheckCode.put("checkcode",checkCode);
        list.add(mapCheckCode);

        responseData.setMsgCode("1");
        responseData.setMsg("短信验证码发送成功");
        responseData.setResp(list);
        responseData.setSuccess(true);
        return responseData;
    }


    /**
     * 实现发送短信功能
     * @param mobileNumber
     * @param sendType
     * @return
     */
    @Override
    public ResponseData sendMessage(String mobileNumber,String sendType)
    {
        ResponseData responseData = new ResponseData();
        //时间格式化
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //验证手机号码的合法性
        String regex = "^((13[0-9])|(14[5,6,7,9])|(15[^4,\\D])|(16[6])|(17[0,1,3,5-8])|(18[0-9]))\\d{8}$";
        mobileNumber = mobileNumber.replace(" ","");
        if (StringUtils.isBlank(mobileNumber))
        {
            responseData.setMsg("发送人手机号为空");
            responseData.setMsgCode("ur.phone.null");
            responseData.setSuccess(false);
            return responseData;
        }else if(!mobileNumber.matches(regex))
        {
            responseData.setMsg("发送人手机号不合法");
            responseData.setMsgCode("ur.phone.illegal");
            responseData.setSuccess(false);
            return responseData;
        }

        if(StringUtils.isBlank(sendType))
        {
            responseData.setMsg("业务类型为空");
            responseData.setMsgCode("ur.sendtype.null");
            responseData.setSuccess(false);
            return responseData;
        }

        //根据手机号和业务类型获取一条短信验证码信息
        HmallMstCustomerCheck hmallMstCustomerCheck = hmallMstCustomerCheckMapper.selectByMobileAndSendType(mobileNumber,sendType);

        String checkCode = this.createCheckCode(); //验证码
        String sendtime = null; //发送时间
        String msg = null; //短信内容

        if (hmallMstCustomerCheck == null)
        {
            //生成验证码并与手机号进行绑定 首次获取验证码插入数据库
            HmallMstCustomerCheck newCustomerCheck = new HmallMstCustomerCheck();
            newCustomerCheck.setMobile(mobileNumber);
            newCustomerCheck.setCheckcode(checkCode);
            newCustomerCheck.setSendType(sendType);
            try{
                hmallMstCustomerCheckMapper.insertSelective(newCustomerCheck);
            }catch (Exception e) {
                e.printStackTrace();
                logger.error("===msg===:" + e.getMessage());
                responseData.setMsg("已存在和该手机号和业务类型一致的一条短信验证码信息,无法插入");
                responseData.setMsgCode("cc.addcustomercheck.error");
                responseData.setSuccess(false);
                return responseData;
            }
        }
        else
        {
            //不是首次获取验证码进行更新操作
            Long customercheckId = hmallMstCustomerCheck.getCustomercheckId();
            HmallMstCustomerCheck hmallMstCustomerWithCheckCode = new HmallMstCustomerCheck();
            hmallMstCustomerWithCheckCode.setCheckcode(checkCode);
            hmallMstCustomerWithCheckCode.setCustomercheckId(customercheckId);
            //该处数据库最后更新时间不会自动获取 需要手动添加
            hmallMstCustomerWithCheckCode.setLastUpdateDate(new Date());
            //更新该字段为:false(短信验证码验证成功后该字段设置为:true)
            hmallMstCustomerWithCheckCode.setAttribute1("false");
            hmallMstCustomerCheckMapper.updateByPrimaryKeySelective(hmallMstCustomerWithCheckCode);
        }

        HmallMstCustomerCheck customerCheck = hmallMstCustomerCheckMapper.selectByMobileAndSendType(mobileNumber,sendType);
        if(null == customerCheck.getCheckcode())
        {
            responseData.setMsg("验证码为空");
            responseData.setMsgCode("ur.checkcode.null");
            responseData.setSuccess(false);
            return responseData;
        }
        else
        {
            checkCode = customerCheck.getCheckcode();
        }

        if(null == customerCheck.getLastUpdateDate())
        {
            responseData.setMsg("发送时间为空");
            responseData.setMsgCode("ur.sendtime.null");
            responseData.setSuccess(false);
            return responseData;
        }
        else
        {
            //时间格式化
            sendtime = formatter.format(customerCheck.getLastUpdateDate());

            try {
                //时间进行UTF-8转码
                sendtime  = URLEncoder.encode(sendtime,"UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("---date format error---:",e);
            }
        }


        //根据业务类型获取短信模板
        HmallOmMailteplate hmallOmMailteplate = hmallOmMailteplateMapper.selectBySendType(sendType);
        if(hmallOmMailteplate == null)
        {
            responseData.setMsg("不存在该短信模板");
            responseData.setMsgCode("ur.mailteplate.notexist");
            responseData.setSuccess(false);
            return responseData;
        }
        else
        {
            if(null == hmallOmMailteplate.getMsgTemplate())
            {
                responseData.setMsg("短信内容为空");
                responseData.setMsgCode("ur.msg.null");
                responseData.setSuccess(false);
                return responseData;
            }
            else
            {
                //得到短信内容
                msg = hmallOmMailteplate.getMsgTemplate();
                logger.info("---msgOld---:" + msg);

                //把短信模板中的短信内容:XXXXXX替换成验证码
                msg = msg.replace("XXXXXX",checkCode);
                try {
                    //短信内容进行UTF-8
                    msg = URLEncoder.encode(msg,"UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("---msg encode error---:",e);
                }
            }
        }

        //获取任信了账号
        HmallFndGlobalVariant globalVariantUser = hmallFndGlobalVariantMapper.getUserOrPwdByCode("renxinl-user");
        //任信了账号
        String user = null;
        if(globalVariantUser == null)
        {
            responseData.setMsg("不存在任信了账号");
            responseData.setMsgCode("ur.user.null");
            responseData.setSuccess(false);
            return responseData;
        }
        else
        {
            //得到任信了账号
            user = globalVariantUser.getValue();
        }

        //获取任信了密码
        HmallFndGlobalVariant globalVariantPwd = hmallFndGlobalVariantMapper.getUserOrPwdByCode("renxinl-password");
        //任信了密码
        String pwd = null;
        if(globalVariantPwd == null)
        {
            responseData.setMsg("不存在任信了密码");
            responseData.setMsgCode("ur.pwd.null");
            responseData.setSuccess(false);
            return responseData;
        }
        else
        {
            //得到任信了密码
            pwd = globalVariantPwd.getValue();

            //将密码进行MD5加密
            pwd = MD5EncryptionUtil.getMd5(pwd);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("phone",mobileNumber);
        map.put("user",user);
        map.put("pwd",pwd);
        map.put("sendtime",sendtime);
        map.put("msg",msg);

        logger.info("user:" +user+",pwd:"+pwd+",phone:"+mobileNumber+",msg:"+msg+",sendtime:"+sendtime);

        try{
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            requestFactory.setConnectTimeout(200000); //连接时间
            requestFactory.setReadTimeout(200000); //读取时间
            RestTemplate restTemplate = new RestTemplate(requestFactory);


            //短信接口的URL
            logger.info("===url===:" + url);
            //调用短信的接口参数
            String params = "?user="+user+"&pwd="+pwd+"&phone="+mobileNumber+"&msg="+msg+"&sendtime="+sendtime;
            logger.info("===params===:" + params);
            HttpEntity<Map<String,Object>> entity = new HttpEntity<>(map,null);
            //发送请求,获取第三方短信的返回值
            HmallMessageResponseData response = restTemplate.exchange(url+params, HttpMethod.GET,entity,HmallMessageResponseData.class).getBody();

            //请求状态码，取值0000（成功）
            String code = response.getCode();
            //短信唯一标识符
            String msgid = response.getMsgid();
            //状态码相应的解释
            String codemsg = response.getCodemsg();

            logger.info("code:" + code + ",msgid:" + msgid + ",codemsg:" + codemsg);

            Map<String, Object> mapCheckCode = new HashMap();
            List list = new ArrayList<>();
            mapCheckCode.put("checkcode",checkCode);
            list.add(mapCheckCode);

            //发送成功
            if("0000".equals(code))
            {
                responseData.setMsgCode("1");
                responseData.setMsg("手机验证码发送成功");
                responseData.setResp(list);
                responseData.setSuccess(true);
            }else{
                //发送失败
                responseData.setMsgCode("2");
                responseData.setMsg(codemsg);
                responseData.setResp(null);
                responseData.setSuccess(false);
            }
            return  responseData;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("---send error---:",e);
        }

        responseData.setMsgCode("6001");  //发送失败
        responseData.setMsg("参数异常,发送失败");
        responseData.setSuccess(false);
        return responseData;
    }


    /**
     * 随机生成验证码
     */
    @Override
    public String createCheckCode()
    {
        String[] beforeShuffle = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        List<String> list = Arrays.asList(beforeShuffle);
        Collections.shuffle(list);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++)
        {
            sb.append(list.get(i));
        }
        String afterShuffle = sb.toString();

        //生成的验证码
        String checkCode = afterShuffle.substring(3, 9);
        return checkCode;
    }
}
