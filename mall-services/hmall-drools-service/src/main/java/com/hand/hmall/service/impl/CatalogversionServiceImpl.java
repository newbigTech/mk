package com.hand.hmall.service.impl;

import com.hand.hmall.dto.Catalogs;
import com.hand.hmall.dto.Catalogversion;
import com.hand.hmall.service.ICatalogsService;
import com.hand.hmall.service.ICatalogversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 马君
 * @version 0.1
 * @name CatalogversionServiceImpl
 * @description 目录版本服务实现类
 * @date 2017/6/30 9:56
 */
@Service
public class CatalogversionServiceImpl extends
        BaseServiceImpl<Catalogversion> implements ICatalogversionService {

    @Autowired
    private ICatalogsService iCatalogsService;

    /**
     * {@inheritDoc}
     *
     * @see ICatalogversionService#selectMasterStaged()
     */
    @Override
    public Catalogversion selectMasterStaged() {
        Catalogs master = iCatalogsService.selectMaster();
        Catalogversion masterStaged = new Catalogversion();
        masterStaged.setCatalog(master.getCatalogsId());
        masterStaged.setCatalogversion("staged");
        return this.selectOne(masterStaged);
    }

    /**
     * {@inheritDoc}
     *
     * @see ICatalogversionService#selectMarkorStaged()
     */
    @Override
    public Catalogversion selectMarkorStaged() {
        Catalogs markor = iCatalogsService.selectMarkor();
        Catalogversion markorStaged = new Catalogversion();
        markorStaged.setCatalog(markor.getCatalogsId());
        markorStaged.setCatalogversion("staged");
        return this.selectOne(markorStaged);
    }

    /**
     * {@inheritDoc}
     *
     * @see ICatalogversionService#selectMarkorOnline()
     */
    @Override
    public Catalogversion selectMarkorOnline() {
        Catalogs markor = iCatalogsService.selectMarkor();
        Catalogversion markorOnline = new Catalogversion();
        markorOnline.setCatalog(markor.getCatalogsId());
        markorOnline.setCatalogversion("online");
        return this.selectOne(markorOnline);
    }
}
