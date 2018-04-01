package com.hand.hmall.service.impl;

import com.hand.hmall.model.VCodeHeader;
import com.hand.hmall.service.IVCodeHeaderService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name VCodeHeaderServiceImpl
 * @description v码头服务实现类
 * @date 2017/7/6 18:22
 */
@Service
public class VCodeHeaderServiceImpl
        extends BaseServiceImpl<VCodeHeader> implements IVCodeHeaderService {

    /**
     * {@inheritDoc}
     *
     * @see IVCodeHeaderService#selectOneByVCode
     */
    @Override
    public VCodeHeader selectOneByVCode(String vCode) {
        VCodeHeader vCodeHeader = new VCodeHeader();
        vCodeHeader.setVcode(vCode);
        try {
            return this.selectOne(vCodeHeader);
        } catch (Exception e) {
            return null;
        }
    }
}
