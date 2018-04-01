package com.hand.hmall.mst.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.mst.dto.MstUser;

import java.util.List;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 用户对象的service接口
 * @date 2017/7/10 14:37
 */
public interface IMstUserService extends IBaseService<MstUser>, ProxySelf<IMstUserService> {

    /**
     * 用户查询界面数据查询
     *
     * @param dto
     * @return
     */
    List<MstUser> queryInfo(IRequest iRequest, MstUser dto, int page, int pageSize);

    /**
     * 根据用户的CUSTOMERID字段查询对应的USERID
     * @param customerid
     * @return List<MstUser>
     */
    List<MstUser> selectByCustomerId(String customerid);

    /**
     * 根据coustomerid字段查询USER表中的USER_ID,NAME;USERGROUP表中的NAME
     * @param customerId
     * @return
     */
    MstUser selectMsgByCustomerId(String customerId);
}