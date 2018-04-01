package com.hand.hmall.service.impl;

import com.hand.hmall.mapper.ConrelMapper;
import com.hand.hmall.model.Conrel;
import com.hand.hmall.service.IConrelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name ConrelServiceImpl
 * @description 伙伴关系Service
 * @date 2017/9/12 14:04
 */
@Service
public class ConrelServiceImpl extends BaseServiceImpl<Conrel> implements IConrelService {

    @Autowired
    private ConrelMapper conrelMapper;

    @Override
    public List<Conrel> select(String platformCode, String fabric) {
        Conrel conrel = new Conrel();
        conrel.setPlatform(platformCode);
        conrel.setFabric(fabric);
        return conrelMapper.select(conrel);
    }
}
