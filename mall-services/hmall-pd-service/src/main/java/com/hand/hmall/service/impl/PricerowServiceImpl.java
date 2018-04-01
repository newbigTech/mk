package com.hand.hmall.service.impl;

import com.hand.hmall.mapper.PricerowMapper;
import com.hand.hmall.model.Pricerow;
import com.hand.hmall.service.IPricerowService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name PricerowServiceImpl
 * @description 价格行服务实现类
 * @date 2017/7/5 11:32
 */
@Service
public class PricerowServiceImpl extends BaseServiceImpl<Pricerow> implements IPricerowService {

    @Autowired
    private PricerowMapper pricerowMapper;

    /**
     * {@inheritDoc}
     *
     * @see IPricerowService#selectPricerows(Long, String, String)
     */
    @Override
    public List<Pricerow> selectPricerows(Long productId, String productGrade, String priceType) {
        Example example = new Example(Pricerow.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productId", productId);
        if (StringUtils.isNotBlank(productGrade)) {
            criteria.andEqualTo("productGrade", productGrade);
        } else {
            criteria.andIsNull("productGrade");
        }
        criteria.andEqualTo("priceType", priceType);
        // 有效时间
        Date current = new Date();
        criteria.andLessThan("startTime", current);
        criteria.andGreaterThan("endTime", current);
        return pricerowMapper.selectByExample(example);
    }

    @Override
    public List<Pricerow> selectPricerows(Long productId, String productGrade, String priceType, String odtype) {
        Example example = new Example(Pricerow.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productId", productId);
        if (StringUtils.isNotBlank(productGrade)) {
            criteria.andEqualTo("productGrade", productGrade);
        } else {
            criteria.andIsNull("productGrade");
        }
        criteria.andEqualTo("priceType", priceType);
        // 有效时间
        Date current = new Date();
        criteria.andLessThan("startTime", current);
        criteria.andGreaterThan("endTime", current);

        if (StringUtils.isBlank(odtype)) {
            criteria.andIsNull("odtype");
        } else {
            criteria.andEqualTo("odtype", odtype);
        }
        return pricerowMapper.selectByExample(example);
    }

    /**
     * {@inheritDoc}
     *
     * @see IPricerowService#selectPricerows(Long, String)
     */
    @Override
    public List<Pricerow> selectPricerows(Long productId, String priceType) {
        Example example = new Example(Pricerow.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productId", productId);
        criteria.andEqualTo("priceType", priceType);
        // 有效时间
        Date current = new Date();
        criteria.andLessThan("startTime", current);
        criteria.andGreaterThan("endTime", current);
        return pricerowMapper.selectByExample(example);
    }

    @Override
    public List<Pricerow> selectGiftPricerows(Long productId, String priceType,String groupType) {
        Example example = new Example(Pricerow.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productId",productId);
        criteria.andEqualTo("priceType",priceType);
        criteria.andEqualTo("priceGroup",groupType);
        return pricerowMapper.selectByExample(example);
    }
}
