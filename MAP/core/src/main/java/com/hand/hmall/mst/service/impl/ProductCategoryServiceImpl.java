package com.hand.hmall.mst.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.mst.dto.*;
import com.hand.hmall.mst.mapper.ProductCategoryMapper;
import com.hand.hmall.mst.service.ICatalogversionService;
import com.hand.hmall.mst.service.IProductCategoryService;
import com.hand.hmall.mst.service.IProductService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductCategoryServiceImpl extends BaseServiceImpl<ProductCategory> implements IProductCategoryService {

    @Autowired
    ProductCategoryMapper mapper;

    @Autowired
    ICatalogversionService catalogversionService;

    @Autowired
    private IProductService productService;

    @Override
    public List<Category> getInfo() {
        return mapper.getInfo();
    }

    /**
     * 查询商品类别列表(商品类别、商品编码模糊查询)
     * @param productCategory
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public List<ProductCategory> getProductCategoryList(ProductCategory productCategory, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return mapper.getProductCategoryList(productCategory);
    }

    /**
     * 查询超类别列表
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<ProductCategory> querySuperType(ProductCategory dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return mapper.querySuperType(dto);
    }

    /**
     * 查询子类别列表
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<ProductCategory> querySubType(ProductCategory dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return mapper.querySubType(dto);
    }

    /**
     * 根据类别ID查询商品列表
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<Product> queryProductByCategoryId(ProductCategory dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return mapper.queryProductByCategoryId(dto);
    }

    /**
     * 根据类别Code和版本目录查询对应类别
     * @param category
     * @return
     */
    @Override
    public Long selectByCodeAndVersion(ProductCategory category) {
        return mapper.selectByCodeAndVersion(category);
    }

    /**
     * 根据类别ID查询查询超类别子类别都没有的其他类别
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<ProductCategory> queryTypeNotInSuperAndSub(ProductCategory dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return mapper.queryTypeNotInSuperAndSub(dto);
    }

    /**
     * 根据商品ID关联的映射表中的类别Id而查出类别列表
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<ProductCategory> selectProductCategoryList(ProductCategory dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return mapper.selectProductCategoryList(dto);
    }

    /**
     * 根据类别id和目录版本查询类别信息
     * @param dto
     * @return
     */
    @Override
    public ProductCategory queryCategoryByCategoryIdAndVersion(ProductCategory dto) {
        return mapper.queryCategoryByCategoryIdAndVersion(dto);
    }

    /**
     * 根据类别ID查询未包含的商品
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<Product> queryProductNotItself(ProductCategory dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return mapper.queryProductNotItself(dto);
    }


    /**
     * 类别同步
     * @param iRequest
     * @param syncDataList
     * @return
     */
    @Override
    public List<SyncData> sync(IRequest iRequest, List<SyncData> syncDataList)
    {
        if(CollectionUtils.isNotEmpty(syncDataList))
        {
            //获取前端传递的参数
            SyncData syncData = syncDataList.get(0);
            //获取类别list
            List<ProductCategory> categoryList = syncData.getCategoryList();

            String catalogTo = syncData.getCatalogTo();
            String versionTo = syncData.getVersionTo();

            //获取要同步的目录版本
            Catalogversion catalogversion = new Catalogversion();
            catalogversion.setCatalogversion(versionTo);
            catalogversion.setCatalogName(catalogTo);
            Long versionId = catalogversionService.selectCatalogversionId(catalogversion);

            if(CollectionUtils.isNotEmpty(categoryList))
            {
                for(ProductCategory dto : categoryList)
                {
                    //类别同步
                    syncCategory(iRequest,dto,versionId);
                }
            }

            //商品对应的类别同步
            productService.sync(iRequest,syncDataList);
        }

        return syncDataList;
    }

    /**
     * 根据categoryId查询类别
     * @param categoryId
     * @return
     */
    @Override
    public ProductCategory selectByCategoryId(Long categoryId)
    {
        ProductCategory category = new ProductCategory();
        category.setCategoryId(categoryId);
        return mapper.selectByPrimaryKey(category);
    }

    public void syncCategory(IRequest iRequest, ProductCategory dto, Long versionId)
    {
        //查询下一版本类别
        dto.setCatalogVersion(versionId);
        Long nextVersionCategoryId = mapper.selectByCodeAndVersion(dto);

        ProductCategory category = this.selectByPrimaryKey(iRequest, dto);
        category.setCatalogVersion(versionId);

        if(nextVersionCategoryId == null)
        {
            category.setCategoryId(null);
            category = this.insert(iRequest,category);
        }
        else
        {
            category.setCategoryId(nextVersionCategoryId);
            //当前类别的更新日期大于下一版本的更新日期，也就是和下一版本相比，类别更新
            //下一版本
            ProductCategory nextVersionCategory = this.selectByCategoryId(nextVersionCategoryId);
            //当前版本
            ProductCategory lastVersionCategory = this.selectByCategoryId(dto.getCategoryId());
            if(lastVersionCategory.getLastUpdateDate().after(nextVersionCategory.getLastUpdateDate()))
            {
                category = this.updateByPrimaryKeySelective(iRequest,category);

            }
        }

    }


}