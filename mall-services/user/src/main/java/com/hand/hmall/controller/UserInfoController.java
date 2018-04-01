package com.hand.hmall.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.hand.hmall.dto.HmallMstUser;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.service.IUserInfoService;
import com.hand.hmall.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @author 阳赳
 * @name:UserInfoController
 * @Description:中台用户信息查询
 * @date 2017/6/1 16:11
 */
@RestController
@RequestMapping(value = "/i/customer/info", produces = {MediaType.APPLICATION_JSON_VALUE})
public class UserInfoController {
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private IUserService userService;
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    /**
     * 更新用户信息入口
     *
     * @param Info 更新信息
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseData updateInfo(@RequestBody Map Info) {
        try {
            return userInfoService.update(Info);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData(false, "User_Update_History_Error");
        }
    }

    /**
     * 中台促销查询用户信息
     * @param map
     * @return
     */
    @PostMapping(value = "/queryByCondition" )
    public ResponseData queryByCondition(@RequestBody Map map){
        HmallMstUser user =  userService.selectByCustomerId(map.get("mobileNumber").toString());
        return new ResponseData(Arrays.asList(user));
    }

    /**
     * 中台优惠券发放界面查询 根据用户姓名，手机号码模糊查询用户信息
     * 查询不在发券候选去的用户信息
     * @param map
     * @return
     */
    @RequestMapping(value = "/queryNotEqual", method = RequestMethod.POST)
    public ResponseData queryNotEqual(@RequestBody Map<String, Object> map){
        logger.info("------map-----{}----", map.toString());
        List notIn = (List)map.get("notIn");
        Map userInfo = (Map)map.get("data");
        int page = (int)map.get("page");
        int pageSize = (int)map.get("pageSize");
        PageHelper.startPage(page,pageSize);
        String mobileNumber = (String)userInfo.get("mobileNumber");
        String name = (String) userInfo.get("name");
        if (!StringUtils.isEmpty(mobileNumber) || !StringUtils.isEmpty(name)) {
            List<HmallMstUser> userList = userInfoService.matchUserByCondition(userInfo);
            List<Map> respList = new ArrayList<>();
            getRespUserList(userList, respList);
            ResponseData responseData = new ResponseData(respList);
            Page<HmallMstUser> pages = (Page<HmallMstUser>) userList;
            int total = (int)pages.getTotal();
            responseData.setTotal(total);
            return responseData;
        }else {
            List<HmallMstUser> userList = userInfoService.selectByNotIn(notIn);
            List<Map> respList = new ArrayList<>();
            getRespUserList(userList, respList);
            Page<HmallMstUser> pages = (Page<HmallMstUser>) userList;
            int total = (int) pages.getTotal();
            ResponseData responseData = new ResponseData(respList);
            responseData.setTotal(total);
            return responseData;
        }

    }

    /**
     * 处理要返回给优惠券发放界面的用户数据
     *
     * @param userList
     * @param respList
     */
    public void getRespUserList(List<HmallMstUser> userList, List<Map> respList) {
        for (HmallMstUser user : userList) {
            Map respUser = getRespUser(user);
            respList.add(respUser);
        }
    }

    /**
     * 处理要返回给优惠券发放界面的用户数据
     *
     * @param user
     * @return
     */
    public Map getRespUser(HmallMstUser user) {
        Map respUser = new HashMap();
        respUser.put("name", user.getName());
        respUser.put("userLevel", user.getUserLevel());
        respUser.put("mobileNumber", user.getMobileNumber());
        respUser.put("userId", user.getCustomerid());
        return respUser;
    }


    /**
     * 优惠券发放界面查询候选区的用户是否合法
     * @param map
     * @return
     */
    @RequestMapping(value = "/queryByUserIds", method = RequestMethod.POST)
    public ResponseData queryByUserIds(@RequestBody Map<String, Object> map){
        logger.info("------map-----{}----", map.toString());
        List userIds = (List)map.get("userIds");
        List<HmallMstUser> userList = userInfoService.selectByUserIds(userIds);
        List<Map> respList = new ArrayList<>();
        for (HmallMstUser user : userList) {
            Map respUser = getRespUser(user);
            respList.add(respUser);
        }
        return new ResponseData(respList);
    }
}
