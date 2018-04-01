package com.hand.hmall.mst.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.mst.dto.MstUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 用户对象的Mapper
 * @date 2017/7/10 14:37
 */
public interface MstUserMapper extends Mapper<MstUser> {


    /**
     * 用户查询界面数据查询
     *
     * @param dto
     * @return
     */
    List<MstUser> queryInfo(MstUser dto);

    /**
     * 根据天猫昵称查询
     * @param tmNickName - 用户天猫昵称
     * @return
     */
    List<MstUser> queryUserByTMNickName(@Param("tmNickName") String tmNickName);

    /**
     * 根据用户的CUSTOMERID字段查询对应的USERID
     * @param customerid
     * @return List<MstUser>
     */
    List<MstUser> selectByCustomerId(@Param("customerid") String customerid);

    /**
     * 根据coustomerid字段查询USER表中的USER_ID,NAME;USERGROUP表中的NAME
     * @param customerId
     * @return
     */
    MstUser selectMsgByCustomerId(String customerId);
}
