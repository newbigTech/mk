package com.hand.hmall.service.impl;

import com.hand.hmall.model.Catalogs;
import com.hand.hmall.service.ICatalogsService;
import org.springframework.stereotype.Service;

/**
 * @author 马君
 * @version 0.1
 * @name CatalogsServiceImpl
 * @description 目录服务实现类
 * @date 2017/6/30 10:19
 */
@Service
public class CatalogsServiceImpl extends
        BaseServiceImpl<Catalogs> implements ICatalogsService {

    /**
     * {@inheritDoc}
     *
     * @see ICatalogsService#selectMaster()
     */
    @Override
    public Catalogs selectMaster() {
        Catalogs catalogs = new Catalogs();
        catalogs.setCode("master");
        return this.selectOne(catalogs);
    }

    /**
     * {@inheritDoc}
     *
     * @see ICatalogsService#selectMarkor()
     */
    @Override
    public Catalogs selectMarkor() {
        Catalogs catalogs = new Catalogs();
        catalogs.setCode("markor");
        return this.selectOne(catalogs);
    }
}
