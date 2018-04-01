package com.hand.hmall.mst.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.mst.dto.MstUser;
import com.hand.hmall.mst.mapper.MstUserMapper;
import com.hand.hmall.mst.service.IMstUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 用户的service实现类
 * @date 2017/7/10 14:37
 */
@Service
@Transactional
public class MstUserServiceImpl extends BaseServiceImpl<MstUser> implements IMstUserService {

    @Autowired
    MstUserMapper mapper;

    /**
     * 用户查询界面数据查询
     * @param iRequest
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<MstUser> queryInfo(IRequest iRequest, MstUser dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return mapper.queryInfo(dto);
    }

    /**
     * 根据用户的CUSTOMERID字段查询对应的USERID
     * @param customerid
     * @return
     */
    @Override
    public List<MstUser> selectByCustomerId(String customerid) {
        List<MstUser> mstUsers = mapper.selectByCustomerId(customerid);
        return mstUsers;
    }

    /**
     * 根据coustomerid字段查询USER表中的USER_ID,NAME;USERGROUP表中的NAME
     * @param customerId
     * @return
     */
    @Override
    public MstUser selectMsgByCustomerId(String customerId) {
        return mapper.selectMsgByCustomerId(customerId);
    }
}