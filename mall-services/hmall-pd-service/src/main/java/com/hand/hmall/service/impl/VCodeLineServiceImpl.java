package com.hand.hmall.service.impl;

import com.hand.hmall.model.VCodeLine;
import com.hand.hmall.service.IVCodeLineService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name VCodeLineServiceImpl
 * @description v码行服务实现类
 * @date 2017/7/7 8:57
 */
@Service
public class VCodeLineServiceImpl
        extends BaseServiceImpl<VCodeLine> implements IVCodeLineService {

    /**
     * {@inheritDoc}
     *
     * @see IVCodeLineService#selectByHeaderId
     */
    @Override
    public List<VCodeLine> selectByHeaderId(Long headerId) {
        VCodeLine vCodeLine = new VCodeLine();
        vCodeLine.setHeaderId(headerId);
        return this.select(vCodeLine);
    }
}
