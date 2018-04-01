package com.hand.hmall.service;

import com.hand.hmall.dto.HmallMstUser;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.exception.UserUpdateHistoryException;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

/**
 * @author 阳赳
 * @name:IUserInfoServiceImpl
 * @Description:实现用户信息相关业务逻辑
 * @date 2017/5/25 16:56
 * @version: 1.0
 */
public interface IUserInfoService {
    /**
     * 更新用户信息
     *
     * @param Info 更新信息
     * @return
     * @throws UserUpdateHistoryException
     */
    ResponseData update(Map Info) throws UserUpdateHistoryException, NoSuchAlgorithmException;

    List<HmallMstUser> selectUserByCondition(Map map);
    List<HmallMstUser> selectByUserIds(List<String> ids);
    List<HmallMstUser> selectByNotIn(List<String> notIn);
    List<HmallMstUser> matchUserByCondition(Map map);

}
