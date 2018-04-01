package com.hand.hmall.mst.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.mst.dto.Odtype;
import com.hand.hmall.mst.dto.OdtypeDto;
import com.hand.hmall.mst.mapper.OdtypeMapper;
import com.hand.hmall.mst.service.IOdtypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 频道对象service实现层
 * @date 2017/8/11 17:33
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OdtypeServiceImpl extends BaseServiceImpl<Odtype> implements IOdtypeService {

    @Autowired
    private OdtypeMapper odtypeMapper;

    /**
     * 推送至商城的频道信息数据
     * @return
     */
    @Override
    public List<OdtypeDto> selectOdtypeData() {

        return odtypeMapper.selectOdtypeData();
    }

    /**
     * 根据任何字段来查询结果
     * @param odtype
     * @return
     */
    @Override
    public List<Odtype> selectByCondition(Odtype odtype) {
        return mapper.select(odtype);
    }

    /**
     * 目录版本同步时使用，根据商品ID查询对应的OD关系
     * @param productId
     * @return
     */
    @Override
    public List<Odtype> getOdtypeInfoByProductId(Long productId) {
        return odtypeMapper.getOdtypeInfoByProductId(productId);
    }

    /**
     * 根据商品ID删除对应信息
     * @param productId
     */
    @Override
    public void removeByProductId(Long productId) {
        odtypeMapper.removeByProductId(productId);
    }

    /**
     * 根据定制频道来源和商品ID查询对应信息
     *
     * @param odtype 参数
     * @return 查询信息
     */
    @Override
    public List<Odtype> getByProductAndCustChanSrc(Odtype odtype) {
        return odtypeMapper.getByProductAndCustChanSrc(odtype);
    }
}