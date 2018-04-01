package com.hand.hmall.controller;

import com.hand.hmall.code.MessageCode;
import com.hand.hmall.dto.HmallMstCustomerCheck;
import com.hand.hmall.dto.HmallMstUser;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.dto.UserPojo;
import com.hand.hmall.mapper.HmallMstCustomerCheckMapper;
import com.hand.hmall.mapper.HmallMstUserGroupMapper;
import com.hand.hmall.service.IUserRegisterService;
import com.hand.hmall.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author XinyangMei
 * @version 1.0
 * @Title UserController
 * @Description 对用户进行操作的入口
 * @date 2017/6/28 15:24
 */
@RestController
@RequestMapping("/i")
public class UserController {

    //注册验证码有效时间
    private final long VALID = 3 * 60 * 1000;
    @Autowired
    private IUserService userService;
    @Autowired
    @Qualifier(value = "IUserRegisterService")
    private IUserRegisterService IUserRegisterService;
    @Autowired
    private HmallMstUserGroupMapper hmallMstUserGroupMapper;
    @Autowired
    private HmallMstCustomerCheckMapper hmallMstCustomerCheckMapper;
    private Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    /**
     * 用户登录
     *
     * @param map 用户登陆信息
     * @Author: noob
     * @Date: 2017/5/24 9:18
     */
    @RequestMapping(value = "/login/normal", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData login(@RequestBody Map<String, Object> map) throws NoSuchAlgorithmException {
        ResponseData responseData = new ResponseData();

        //获取有户名
        String mobileNumber = (String) map.get("mobileNumber");

        //用户名不能为空
        if (StringUtils.isBlank(mobileNumber)) {
            responseData.setSuccess(false);
            responseData.setMsgCode("ur.login.mobilenumber.null");
            responseData.setMsg(MessageCode.UR_LOGIN_002.getValue());
            return responseData;
        }

        //根据用户名获取一个user
        HmallMstUser user = userService.selectByCustomerId(mobileNumber);

        if (user == null) {
            //用户名不存在
            responseData.setSuccess(false);
            responseData.setMsgCode("ur.login.mobilenumber.noexist");
            responseData.setMsg(MessageCode.UR_LOGIN_03.getValue());
            return responseData;
        }

        //获取密码
        String pwd = (String) map.get("password");

        if (StringUtils.isBlank(pwd)) {
            //密码为空
            responseData.setMsgCode("ur.login.password.null");
            responseData.setSuccess(false);
            responseData.setMsg(MessageCode.UR_PASSWORD_023.getValue());
            return responseData;
        }

        Boolean validate = null;
        //验证密码是否正确
        validate = userService.validate(mobileNumber, pwd);
        if (validate == false) {
            responseData.setMsgCode("ur.login.password.error");
            responseData.setMsg(MessageCode.UR_PASSWORD_021.getValue());
            responseData.setSuccess(false);
        } else {
            responseData.setMsgCode("1");
            responseData.setMsg(MessageCode.UR_PASSWORD_003.getValue());
            responseData.setSuccess(true);
        }
        return responseData;
    }

    /**
     * 用户注册
     *
     * @param map 用户注册信息
     * @Author: yang
     * @Date: 2017/5/24 9:18
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseData register(@RequestBody Map<String, Object> map) {

        ResponseData responseData = new ResponseData();

        //用户名
        String mobileNumber = (String) map.get("mobileNumber");
        //业务类型
        String sendType = (String) map.get("sendType");
        //密码
        String pwd = (String) map.get("password");

        //手机号码合法验证
        String check = IUserRegisterService.check(mobileNumber);
        if (!"UR_MOBILE_01".equals(check)) {
            if ("UR_MOBILE_03".equals(check)) {
                //手机号合法但被使用
                responseData.setMsg(MessageCode.UR_MOBILE_015.getValue());
                responseData.setMsgCode("ur.register.mobilenumber.isexist");
                responseData.setSuccess(false);
            } else if ("UR_MOBILE_04".equals(check)) {
                //手机号不能为空
                responseData.setMsg(MessageCode.UR_MOBILE_013.getValue());
                responseData.setMsgCode("ur.register.mobilenumber.null");
                responseData.setSuccess(false);
            } else {
                //手机号不合法
                responseData.setMsg(MessageCode.UR_MOBILE_022.getValue());
                responseData.setMsgCode("ur.register.mobilenumber.illegal");
                responseData.setSuccess(false);
            }
            return responseData;
        }

        //密码合法验证
        if (StringUtils.isBlank(pwd)) {
            //密码为空
            responseData.setSuccess(false);
            responseData.setMsgCode("ur.register.password.null");
            responseData.setMsg(MessageCode.UR_PASSWORD_023.getValue());
            return responseData;
        }

        //要求6-20位，合法字符：大小写字母和数字，并且大小写字母和数字都要有
        String regex = "(?!^\\\\d+$)(?!^[a-zA-Z]+$)(?!^[_#@]+$).{6,}";
        if (!pwd.matches(regex)) {
            //密码不合法
            responseData.setMsg(MessageCode.UR_PASSWORD_004.getValue());
            responseData.setMsgCode("ur.register.password.illegal");
            responseData.setSuccess(false);
            return responseData;
        }

        //性别验证
        String sex = (String) map.get("sex");
        if (!StringUtils.isBlank(sex)) {
            if (!sex.equals("M") && !sex.equals("F") && !sex.equals("N")) {
                //性别不合法
                responseData.setMsgCode("ur.register.sex.illegal");
                responseData.setMsg(MessageCode.UR_SEX_007.getValue());
                responseData.setSuccess(false);
                return responseData;
            }
        }


        /**
         * 暂存代码块，现在需求还未确定，带明确后决定是否删除----------------begin
         */
        /**userGroupCode非空校验*/
            /*String userGroupCode =(String) map.get("userGroupCode");
            if (StringUtils.isBlank(userGroupCode)){
                responseData.setMsg(MessageCode.UR_CODE_008.getValue());
                responseData.setMsgCode("6001");
                responseData.setSuccess(false);
                return responseData;
            }*/

        /**userGroupCode是否存在校验*/
            /*HmallMstUserGroup hmallMstUserGroup =hmallMstUserGroupMapper.selectBycode(userGroupCode);
           if (hmallMstUserGroup!=null){
               responseData.setMsg(MessageCode.UR_CODE_009.getValue());
               responseData.setMsgCode("6001");
               responseData.setSuccess(false);
               return responseData;
           }*/
        /**
         * 暂存代码块，现在需求还未确定，带明确后决定是否删除----------------end
         */


        //手机验证码校验
        //获取前台输入的验证码
        String checkCode = (String) map.get("checkcode");
        if (StringUtils.isBlank(checkCode)) {
            //验证码不能为空
            responseData.setMsgCode("ur.register.checkcode.null");
            responseData.setMsg(MessageCode.UR_CHECK_024.getValue());
            responseData.setSuccess(false);
            return responseData;
        }

        /**
         * 是否跳过短信验证码校验 for MAG-1763
         * 给商城提供无需短信验证码的接口，如果是Z开头的验证码，则认为是非短信验证码，根据算法规则校验是否合法
         */
        boolean skipCheckCodeValidation = false;
        if (checkCode.startsWith("Z")) {
            if (generateSecurityCode(mobileNumber).equals(checkCode)) {
                skipCheckCodeValidation = true;
            }
        }

        if (!skipCheckCodeValidation) {

            //根据手机号和业务类型获取一条验证码信息
            HmallMstCustomerCheck hmallMstCustomerCheck = hmallMstCustomerCheckMapper.selectByMobileAndSendType(mobileNumber, sendType);
            if (hmallMstCustomerCheck == null) {
                //验证码错误
                responseData.setMsgCode("ur.register.checkcode.error");
                responseData.setMsg(MessageCode.UR_CHECK_020.getValue());
                responseData.setSuccess(false);
                return responseData;
            }
            //获取数据库的验证码
            String phoneCheckCode = hmallMstCustomerCheck.getCheckcode();
            if (checkCode.equals(phoneCheckCode)) {
                //获取系统时间
                Long currentTime = System.currentTimeMillis();
                //获取数据库验证码最后更新时间
                Long updateDate = hmallMstCustomerCheck.getLastUpdateDate().getTime();
                if (currentTime - updateDate > VALID) {
                    responseData.setMsgCode("ur.register.checkcode.Invalid");
                    responseData.setMsg(MessageCode.UR_CHECK_011.getValue());
                    responseData.setSuccess(false);
                    return responseData;
                } else {
                    //短信验证码成功后,将短信验证码有效性flag设置为:true
                    hmallMstCustomerCheck.setAttribute1("true");
                    hmallMstCustomerCheckMapper.updateByPrimaryKeySelective(hmallMstCustomerCheck);
                }
            } else {
                responseData.setMsgCode("ur.register.checkcode.error");
                responseData.setMsg(MessageCode.UR_CHECK_020.getValue());
                responseData.setSuccess(false);
                return responseData;
            }
        }

        //邮箱格式校验

        //获取前台输入的邮箱
        String email = (String) map.get("email");
        if (!StringUtils.isBlank(email)) {
            //邮箱的正则表达式
            String rex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
            if (!email.matches(rex)) {
                //邮箱格式不合法
                responseData.setMsgCode("ur.register.email.illegal");
                responseData.setMsg(MessageCode.UR_EMAIL_025.getValue());
                responseData.setSuccess(false);
                return responseData;
            }
        }

        //注册部分
        try {
            IUserRegisterService.register(map);
        } catch (NoSuchAlgorithmException e) {
            logger.info(e.getMessage());
        }

        //注册成功
        responseData.setMsgCode("1");
        responseData.setMsg(MessageCode.UR_REGISTER_026.getValue());
        responseData.setSuccess(true);
        return responseData;
    }

    /**
     * 用户信息查询
     *
     * @param customerId 用户id
     * @Author: noob
     * @Date: 2017/5/24 15:56
     */
    @GetMapping("/query/{customerId}")
    public ResponseData queryByCustomerId(@PathVariable("customerId") String customerId) {
        ResponseData responseData = new ResponseData();
        HmallMstUser user = userService.selectByCustomerId(customerId);
        UserPojo userPojo = new UserPojo();
        if (user != null) {
            List<UserPojo> result = new ArrayList<UserPojo>();
            Date date = user.getBirthday();
            if (date != null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String birthday = simpleDateFormat.format(date);
                userPojo.setBirthday(birthday);
            }
            userPojo.setLabel(user.getLabel());
            userPojo.setCity(user.getCity());
            userPojo.setCountry(user.getCountry());
            userPojo.setDistrict(user.getDistrict());
            userPojo.setRegion(user.getRegion());
            userPojo.setCreationDate(user.getCreationDate());
            userPojo.setEmail(user.getEmail());
            userPojo.setCustomerid(user.getCustomerid());
            userPojo.setIsBlackList(user.getIsBlackList());
            userPojo.setMobileNumber(user.getMobileNumber());
            userPojo.setName(user.getName());
            userPojo.setRemark(user.getRemark());
            userPojo.setUserLevel(user.getUserLevel());
            userPojo.setSex(user.getSex());
            userPojo.setPassword(user.getPassword());
            result.add(userPojo);

            //查询成功
            responseData.setMsgCode("1");
            responseData.setMsg(MessageCode.UR_SELECT_016.getValue());
            responseData.setSuccess(true);
            responseData.setResp(result);
            return responseData;
        } else {
            //用户不存在
            responseData.setMsg(MessageCode.UR_LOGIN_03.getValue());
            responseData.setMsgCode("ur.query.mobilenumber.noexist");
            responseData.setSuccess(false);
            return responseData;
        }
    }

    /**
     * 生成安全校验码，用于支持无短信验证码注册。此算法与商城保持一致，根据手机号码生成唯一的安全码，本地生成结果与商城传过来的值作比对，一致则认为是合法请求。
     *
     * @param mobile
     * @return
     */
    private String generateSecurityCode(String mobile) {
        char[] chars = mobile.toCharArray();
        StringBuilder rearranged = new StringBuilder();
        for (int i = chars.length - 1; i >= 0; i--) {
            char tmp[] = new char[1];
            tmp[0] = chars[i];
            int position = Integer.parseInt(new String(tmp));
            rearranged.append(chars[position]);
        }
        String rearrangedStr = rearranged.toString();
        int calc = Integer.parseInt(rearrangedStr.substring(0, 5)) + Integer.parseInt(rearrangedStr.substring(6, 11)) + Integer.parseInt(rearrangedStr.substring(5, 6));
        return "Z" + calc;
    }
}




