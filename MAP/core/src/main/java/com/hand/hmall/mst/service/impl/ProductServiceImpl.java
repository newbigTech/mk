package com.hand.hmall.mst.service.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.hand.common.util.Auth;
import com.hand.common.util.SecretKey;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.mdm.item.dto.Item;
import com.hand.hap.mdm.item.service.IItemService;
import com.hand.hap.mybatis.entity.Example;
import com.hand.hap.system.dto.CodeValue;
import com.hand.hap.system.service.ICodeService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.common.service.IGlobalVariantService;
import com.hand.hmall.log.service.ILogManagerService;
import com.hand.hmall.mst.dto.*;
import com.hand.hmall.mst.mapper.ProductMapper;
import com.hand.hmall.mst.service.*;
import com.hand.hmall.util.Constants;
import com.hand.hmall.util.DateUtil;
import com.markor.map.configurator.api.atp.IAtpProductStockService;
import com.markor.map.configurator.api.atp.entity.AtpProductStock;
import com.markor.map.framework.restclient.RestClient;
import okhttp3.Response;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author zhangmeng
 * @version 0.1
 * @name ProductServiceImpl
 * @description 商品列表查询
 * @date 2017/5/26
 */
@Service
@Transactional
public class ProductServiceImpl extends BaseServiceImpl<Product> implements IProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    private static final String LAST_UPDATE_DATE = "SYNC_ITEM_TO_PRODUCT_LAST_UPDATE_DATE";
    //目录版本标识字段
    private static final String CATALOG_STAGED = "staged";
    private static final String CATALOG_ONLINE = "online";

    @Autowired
    ICatalogversionService catalogversionService;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private IPricerowService pricerowService;

    @Autowired
    private ICategoryMappingService categoryMappingService;

    @Autowired
    private ISuitlineMappingService suitlineMappingService;

    @Autowired
    private IPatchlineMappingService patchlineMappingService;

    @Autowired
    private IMediaService mediaService;

    @Autowired
    private IItemService itemService;

    @Autowired
    private IGlobalVariantService iGlobalVariantService;

    @Autowired
    private ICatalogversionService iCatalogversionService;

    @Autowired
    private ICodeService codeService;

    @Autowired
    private IProductCategoryService productCategoryService;

    @Autowired
    private RestClient restClient;

    @Autowired
    private ILogManagerService iLogManagerService;

    @Autowired
    private IOdtypeService odtypeService;

    @Autowired
    private IProductCategoryService categoryService;

    @Autowired
    private IAtpProductStockService iAtpProductStockService;

    @Override
    public List<ProductDto> selectOnlineProduct() {

        return productMapper.selectPushingProduct();
    }

    @Override
    public List<ProductCodeDto> selectProductCode() {
        return productMapper.selectProductCode();
    }

    @Override
    public void updateProductSyncflag(List<ProductDto> dto) {
        productMapper.updateProductSyncflag(dto);
    }

    /**
     * @param dto      商品实体类
     * @param page
     * @param pageSize
     * @return
     * @description 商品列表查询
     */
    @Override
    public List<Product> selectProductList(Product dto, int page, int pageSize) {
        //使用分页工具进行分页操作
        PageHelper.startPage(page, pageSize);
        return productMapper.selectProductList(dto);
    }

    @Override
    public List<Product> queryInfo(IRequest request, Product dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return productMapper.queryInfo(dto);
    }

    @Override
    public Product selectByPrimaryKey(Product product, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return productMapper.selectByPrimaryKey(product);
    }

    /**
     * @param
     * @param page
     * @param pageSize
     * @return
     * @description 商品列表查询
     */
    @Override
    public List<Product> selectProductListByServiceCode(Product dto, int page, int pageSize) {
        //使用分页工具进行分页操作
        PageHelper.startPage(page, pageSize);
        return productMapper.selectProductListByServiceCode(dto);
    }

    @Override
    public List<Product> selectProductList(Product dto) {
        return productMapper.selectProductList(dto);
    }

    /**
     * @param productId 商品ID
     * @return
     * @description 商品失效
     */
    @Override
    public int unabledProduct(String[] productId) {
        return productMapper.unabledProduct(productId);
    }

    /**
     * 查询当前商品可选套件
     *
     * @param dto      商品实体类
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<Product> selectSuitProduct(Product dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return productMapper.selectSuitProduct(dto);
    }

    /**
     * 删除商品按钮
     *
     * @param productList
     * @return
     */
    @Override
    public List<Product> deleteProduct(List<Product> productList) {
        if (productList != null && productList.size() > 0) {
            for (Product product : productList) {
                //删除价格行中的关系
                pricerowService.deletePricerowByProductId(product);

                //删除商品结构中基础商品和变体商品之间的关系
                productMapper.deleteRelation(product);

                //删除商品时，删除类别映射表中的关系
                CategoryMapping categoryMapping = new CategoryMapping();
                categoryMapping.setProductId(product.getProductId());
                categoryMappingService.deleteCategoryMapping(categoryMapping);

                //删除商品时，删除套件商品关系
                SuitlineMapping suitlineMapping = new SuitlineMapping();
                suitlineMapping.setProductHeadId(product.getProductId());
                suitlineMapping.setComponentId(product.getProductId());
                suitlineMappingService.deleteSuitlineMapping(suitlineMapping);

                //删除商品时，删除补件商品关系
                PatchlineMapping patchlineMapping = new PatchlineMapping();
                patchlineMapping.setProductId(product.getProductId());
                patchlineMapping.setPatchLineId(product.getProductId());
                patchlineMappingService.deletePatchlineMapping(patchlineMapping);

                //删除商品
                productMapper.deleteByPrimaryKey(product);
            }
        }
        return productList;
    }

    @Override
    public List<Product> selectProductLov(Product dto) {
        return productMapper.selectProductLov(dto);
    }

    /**
     * @param dto
     * @return
     * @description 新增商品和修改商品时确定商品编码和版本目录唯一性
     */
    @Override
    public List<Product> selectByCodeAndCatalogVersion(Product dto) {
        return productMapper.selectByCodeAndCatalogVersion(dto);
    }

    /**
     * 根据map中key value值查询商品
     *
     * @param map
     * @return List<Product>
     */
    @Override
    public List<Product> selectProductByOptionMap(Map<String, Object> map) {
        return productMapper.selectProductByOptionMap(map);
    }

    /**
     * 查询当前商品可选补件
     *
     * @param dto      商品实体类
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<Product> selectPatchProduct(Product dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return productMapper.selectPatchProduct(dto);
    }

    @Override
    public int deleteRelation(Product dto) {
        // TODO Auto-generated method stub
        return productMapper.deleteRelation(dto);
    }

    /**
     * 根据基础商品Id查询商品列表
     *
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<Product> queryProductList(Long baseProductId, Product dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return productMapper.queryProductList(baseProductId);
    }

    /**
     * 查询数据是否已存在
     *
     * @param iRequest
     * @param list
     * @return
     */
    @Override
    public List<Product> selectCount(IRequest iRequest, List<Product> list) {
        List<Product> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (Product dto : list) {
                int count = productMapper.selectCountByCodeAndVersion(dto);
                if (count == 0) {
                    dto.setStatus("add");
                } else {
                    Long pId = productMapper.selectIdByCodeAndVersion(dto);
                    dto.setVersionProductId(pId);
                    dto.setStatus("update");
                }
                result.add(dto);
            }
        }
        return result;
    }

    @Override
    public void syncItemToProduct() {

        Date lastUpdateDate = iGlobalVariantService.getDate(LAST_UPDATE_DATE);
        Date currentDate = new Date();

        List<Item> items;
        if (lastUpdateDate != null) {
            items = itemService.queryByLastUpdateDate(lastUpdateDate);
        } else {
            items = itemService.queryAll();
        }

        if (CollectionUtils.isNotEmpty(items)) {
            Catalogversion catalogversion = new Catalogversion();
            catalogversion.setCatalogName(Constants.CATALOG_VERSION_MASTER);
            catalogversion.setCatalogversion(Constants.CATALOG_VERSION_STAGED);
            Long catalogversionId = iCatalogversionService.selectCatalogversionId(catalogversion);
            for (Item item : items) {
                Product product = new Product();
                product.setCode(item.getItemCode());
                product.setName(item.getItemName());
                product.setNameEn(item.getItemName());
                product.setMatkl(item.getMatkl());
                product.setMtart(item.getMtart());
                product.setCatalogversionId(catalogversionId);

                List<Product> productList = productMapper.selectByCodeAndCatalogVersion(product);
                if (CollectionUtils.isNotEmpty(productList)) {
                    product.setProductId(productList.get(0).getProductId());
                    productMapper.updateByPrimaryKeySelective(product);
                } else {
                    productMapper.insertSelective(product);
                }
            }
        }

        iGlobalVariantService.setDate(LAST_UPDATE_DATE, currentDate);

        iLogManagerService.logTrace(this.getClass(), "零部件同步HMALL任务JOB", null, "发现" + CollectionUtils.size(items) + "个零部件并同步");
    }

    /**
     * 商品同步
     *
     * @param iRequest 请求对象
     * @param synclist 前端封装的格式，包含了要同步的商品及要同步的目录版本信息
     * @return
     */
    @Override
    public List<SyncData> sync(IRequest iRequest, List<SyncData> synclist) {
        List<Product> result = new ArrayList<>();
        //强制更新标识(强制同步和普通同步)
        Boolean forceFlag = false;
        //目录版本标识（staged版本和online版本）
        String catalogFlag = null;

        if (CollectionUtils.isNotEmpty(synclist)) {
            //获取前端传递的参数
            SyncData syncData = synclist.get(0);
            List<Product> lists = syncData.getList();
            String catalogTo = syncData.getCatalogTo();
            String versionTo = syncData.getVersionTo();

            //获取要同步的目录版本
            Catalogversion catalogversion = new Catalogversion();
            catalogversion.setCatalogversion(versionTo);
            catalogversion.setCatalogName(catalogTo);
            Long versionId = catalogversionService.selectCatalogversionId(catalogversion);

            if (("markor").equals(catalogTo) && ("staged").equals(versionTo)) {
                catalogFlag = CATALOG_STAGED;
            } else if (("markor").equals(catalogTo) && ("online").equals(versionTo)) {
                catalogFlag = CATALOG_ONLINE;
            }

            if (CollectionUtils.isNotEmpty(lists)) {
                for (Product dto : lists) {
                    syncProduct(iRequest, dto, versionId, forceFlag, catalogFlag);
                }
            }
        }
        return synclist;
    }

    /**
     * 全量同步
     *
     * @param iRequest
     * @param synclist
     * @return
     */
    @Override
    public List<SyncData> batchSync(IRequest iRequest, List<SyncData> synclist) {
        List<Product> result = new ArrayList<>();
        //强制更新标识(强制同步和普通同步)
        Boolean forceFlag = false;
        //目录版本标识（staged版本和online版本）
        String catalogFlag = null;
        if (CollectionUtils.isNotEmpty(synclist)) {
            SyncData syncData = synclist.get(0);

            String catalogTo = syncData.getCatalogTo();
            String versionTo = syncData.getVersionTo();
            String catalogFrom = syncData.getCatalogFrom();
            String versionFrom = syncData.getVersionFrom();

            Catalogversion catalogversion = new Catalogversion();
            catalogversion.setCatalogversion(versionFrom);
            catalogversion.setCatalogName(catalogFrom);

            //查询要同步的商品,staged到online时（非检查状态）商品
            List<Product> listsCategory1 = productMapper.selectBatchSyncProductCase1(catalogversion);
            List<Product> listsCategory2 = productMapper.selectBatchSyncProductCase2(catalogversion);
            List<Product> listsCategory3 = productMapper.selectBatchSyncProductCase3(catalogversion);

            catalogversion.setCatalogversion(versionTo);
            catalogversion.setCatalogName(catalogTo);
            Long versionId = catalogversionService.selectCatalogversionId(catalogversion);

            if (("markor").equals(catalogTo) && ("staged").equals(versionTo)) {
                catalogFlag = CATALOG_STAGED;
            } else if (("markor").equals(catalogTo) && ("online").equals(versionTo)) {
                catalogFlag = CATALOG_ONLINE;
            }

            //每个线程要处理的产品数量
            int tempSize = 50;
            //控制线程的数量
            ExecutorService executorService = Executors.newFixedThreadPool(40);
            //切割list
            logger.info("***********************" + listsCategory1.size() + " " + listsCategory2.size() + " " + listsCategory3.size());

            if (CollectionUtils.isNotEmpty(listsCategory1)) {
                List<List<Product>> splitList1 = splitProductList(listsCategory1, tempSize);
                syncProductList(splitList1, executorService, iRequest, versionId, forceFlag, catalogFlag);
            }

            if (CollectionUtils.isNotEmpty(listsCategory2)) {
                List<List<Product>> splitList2 = splitProductList(listsCategory2, tempSize);
                syncProductList(splitList2, executorService, iRequest, versionId, forceFlag, catalogFlag);
            }

            if (CollectionUtils.isNotEmpty(listsCategory3)) {
                List<List<Product>> splitList3 = splitProductList(listsCategory3, tempSize);
                syncProductList(splitList3, executorService, iRequest, versionId, forceFlag, catalogFlag);
            }

            executorService.shutdown();

        }
        return synclist;
    }

    /**
     * 对每个子list进行多线程的同步
     *
     * @param lists
     * @param executorService
     * @param iRequest
     * @param versionId
     * @param forceFlag
     * @param catalogFlag
     */
    private void syncProductList(List<List<Product>> lists, ExecutorService executorService, IRequest iRequest, Long versionId, Boolean forceFlag, String catalogFlag) {
        final int[] count = {0};
        lists.forEach(list -> count[0] += list.size());
        //以该组所有产品数量做计数器，全部同步完允许往下执行
        CountDownLatch countDownLatch = new CountDownLatch(count[0]);
        if (CollectionUtils.isEmpty(lists))
            return;
        for (List<Product> list : lists) {
            //每一个切割好的list执行一个线程
            executorService.submit(() -> {
                //循环切割好的list 进行同步
                list.forEach(dto -> {
                    try {
                        syncProduct(iRequest, dto, versionId, forceFlag, catalogFlag);
                        logger.info("------------------............." + dto.getProductId());
                    } finally {
                        countDownLatch.countDown();
                    }
                });
            });

        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            logger.error("同步错误", e);
        }
    }

    /**
     * 将list切割成规定大小的若干个子list
     *
     * @param lists
     * @param size
     * @return
     */
    private List<List<Product>> splitProductList(List<Product> lists, int size) {
        List<List<Product>> total = new ArrayList<>();
        if (CollectionUtils.isEmpty(lists))
            return null;
        for (int i = 0; i < lists.size(); i = i + size) {
            try {
                total.add(lists.subList(i, i + size));
            } catch (Exception e) {
                //切割list出错，超出最大长度
                total.add(lists.subList(i, lists.size()));
            }
        }
        return total;
    }

    /**
     * 强制同步
     *
     * @param iRequest
     * @param synclist
     * @return
     */
    @Override
    public List<SyncData> forceSync(IRequest iRequest, List<SyncData> synclist) {
        List<Product> result = new ArrayList<>();
        //强制更新标识(强制同步和普通同步)
        Boolean forceFlag = true;
        //目录版本标识（staged版本和online版本）
        String catalogFlag = null;
        if (CollectionUtils.isNotEmpty(synclist)) {
            SyncData syncData = synclist.get(0);

            List<Product> lists = syncData.getList();
            String catalogTo = syncData.getCatalogTo();
            String versionTo = syncData.getVersionTo();

            Catalogversion catalogversion = new Catalogversion();
            catalogversion.setCatalogversion(versionTo);
            catalogversion.setCatalogName(catalogTo);
            Long versionId = catalogversionService.selectCatalogversionId(catalogversion);

            if (("markor").equals(catalogTo) && ("staged").equals(versionTo)) {
                catalogFlag = CATALOG_STAGED;
            } else if (("markor").equals(catalogTo) && ("online").equals(versionTo)) {
                catalogFlag = CATALOG_ONLINE;
            }

            if (CollectionUtils.isNotEmpty(lists)) {
                for (Product dto : lists) {
                    syncProduct(iRequest, dto, versionId, forceFlag, catalogFlag);
                }
            }
        }
        return synclist;
    }

    /**
     * 单个商品的同步过程
     *
     * @param iRequest
     * @param dto         要同步的商品
     * @param versionId   要同步的目录版本
     * @param forceFlag   强制更新标识
     * @param catalogFlag 当前目录标识
     */
    private void syncProduct(IRequest iRequest, Product dto, Long versionId, Boolean forceFlag, String catalogFlag) {

        //查询下一版本目录商品
        dto.setCatalogversionId(versionId);
        Long nextVersionProductId = productMapper.selectIdByCodeAndVersion(dto);

        //同步商品，同步到staged和online需求字段不一致，要同步的内容也不一致
        Product product = new Product();
        if ((CATALOG_STAGED).equals(catalogFlag)) {
            product = productMapper.selectStagedSpecifiedById(dto.getProductId());
        } else if ((CATALOG_ONLINE).equals(catalogFlag)) {
            product = productMapper.selectOnlineSpecifiedById(dto.getProductId());
        }
        product.setCatalogversionId(versionId);
        if (nextVersionProductId == null) {
            product.setVersionProductId(dto.getProductId());
            product.setProductId(null);
            product = this.insertSelective(iRequest, product);
        } else {
            product.setVersionProductId(dto.getProductId());
            product.setProductId(nextVersionProductId);
            //如果是强制更新
            if (forceFlag) {
                product = this.updateByPrimaryKeySelective(iRequest, product);
            } else {
                //当前商品的更新日期大于下一版本的更新日期，也就是和下一版本相比，初始商品 又 更新了
                Product nextVersionProduct = this.selectByProductId(nextVersionProductId);
                Product lastVersionProduct = this.selectByProductId(dto.getProductId());
                if (lastVersionProduct.getLastUpdateDate().after(nextVersionProduct.getLastUpdateDate())) {
                    product = this.updateByPrimaryKeySelective(iRequest, product);
                }
            }
        }

        //同步基础商品
        Product baseSyncProduct = new Product();
        if (dto.getBaseProductId() != null && !dto.getProductId().equals(dto.getBaseProductId())) {
            baseSyncProduct = syncBaseProduct(iRequest, product, versionId, forceFlag, catalogFlag);
        } else if (dto.getBaseProductId() != null && dto.getProductId().equals(dto.getBaseProductId())) {
            baseSyncProduct = product;
        }

        //同步后的商品与同步后的基础商品关联
        if (baseSyncProduct != null && baseSyncProduct.getProductId() != null && baseSyncProduct.getProductId() > 0) {
            Product linkProduct = new Product();
            linkProduct.setProductId(product.getProductId());
            linkProduct.setBaseProductId(baseSyncProduct.getProductId());
            this.updateByPrimaryKeySelective(iRequest, linkProduct);
        }

        //同步套件
        syncSuitProduct(iRequest, product, versionId, forceFlag, catalogFlag);
        //同步补件
        syncPatchProduct(iRequest, product, versionId, forceFlag, catalogFlag);
        //同步odtype
        syncOdtype(iRequest, product);

        //只有下一版本是online版本时，同步价格行，多媒体，目录版本
        if ((CATALOG_ONLINE).equals(catalogFlag)) {
            //同步价格行
            syncPriceRow(iRequest, product, versionId);
            //同步多媒体
            syncMedia(iRequest, product, versionId, forceFlag);
            //同步商品类别
            syncCategory(iRequest, product);
        }
    }

    /**
     * 商品界面中同步商品时需要先同步关联的套件商品
     * <p>
     * 查询出商品关联的套件商品
     * 遍历套件商品
     * 判断套件商品和原商品是不是同一商品
     * 如果是同一商品，判断下一目录版本是否存在，不存在则新增
     * 不是同一商品，判断下一目录版本是否存在，不存在，新增套件商品，新增映射
     * 存在，判断映射，不存在则新增
     *
     * @param iRequest
     * @param dto         同步后的商品
     * @param versionId   要同步的目录版本
     * @param forceFlag   强制更新标识
     * @param catalogFlag 当前目录标识
     */
    private void syncSuitProduct(IRequest iRequest, Product dto, Long versionId, Boolean forceFlag, String catalogFlag) {
        SuitlineMapping parameMapping = new SuitlineMapping();
        Product suitProduct = new Product();
        Product parameProduct = new Product();
        Long suitProductId = null;
        parameMapping.setProductHeadId(dto.getVersionProductId());
        //获取原商品对应的组件商品的映射信息
        List<SuitlineMapping> suitProductInfos = suitlineMappingService.selectInfo(iRequest, parameMapping);
        if (CollectionUtils.isEmpty(suitProductInfos))
            return;
        //遍历
        for (SuitlineMapping sm : suitProductInfos) {
            sm = suitlineMappingService.selectByPrimaryKey(iRequest, sm);
            //判断商品和补件关系，是不是商品本身
            if (sm.getProductHeadId().equals(sm.getComponentId())) {
                sm.setProductHeadId(dto.getProductId());
                sm.setComponentId(dto.getProductId());
                Long suitlineMappingId = suitlineMappingService.selectBysuitlineAndProductId(sm);
                if (suitlineMappingId == null) {
                    sm.setMappingId(null);
                    suitlineMappingService.insertSelective(iRequest, sm);
                }
            } else {
                parameProduct = this.selectByProductId(sm.getComponentId());
                parameProduct.setCatalogversionId(versionId);
                suitProductId = productMapper.selectIdByCodeAndVersion(parameProduct);
                //根据目录版本选择字段
                if ((CATALOG_STAGED).equals(catalogFlag)) {
                    parameProduct = productMapper.selectStagedSpecifiedById(sm.getComponentId());
                } else if ((CATALOG_ONLINE).equals(catalogFlag)) {
                    parameProduct = productMapper.selectOnlineSpecifiedById(sm.getComponentId());
                }
                parameProduct.setCatalogversionId(versionId);
                //如果不存在
                if (suitProductId == null) {
                    suitProduct = this.insertSelective(iRequest, parameProduct);
                    sm.setProductHeadId(dto.getProductId());
                    sm.setComponentId(suitProduct.getProductId());
                    sm.setMappingId(null);
                    suitlineMappingService.insert(iRequest, sm);
                } else {
                    if (forceFlag) {
                        suitProduct = this.updateByPrimaryKeySelective(iRequest, parameProduct);
                    } else {
                        suitProduct = this.selectByProductId(sm.getComponentId());
                        Product nextVersionProduct = this.selectByProductId(suitProductId);
                        Product lastVersionProduct = this.selectByProductId(sm.getComponentId());
                        if (lastVersionProduct.getLastUpdateDate().after(nextVersionProduct.getLastUpdateDate())) {
                            suitProduct = this.updateByPrimaryKeySelective(iRequest, parameProduct);
                        }
                    }
                    //判断是否需要插入映射关系
                    sm.setProductHeadId(dto.getProductId());
                    sm.setComponentId(suitProductId);
                    Long suitlineMappingId = suitlineMappingService.selectBysuitlineAndProductId(sm);
                    if (suitlineMappingId == null) {
                        sm.setMappingId(null);
                        suitlineMappingService.insertSelective(iRequest, sm);
                    }
                }

            }
        }

    }

    /**
     * 商品界面中同步商品时需要先同步关联的补件商品
     *
     * @param iRequest
     * @param dto         同步后的商品
     * @param versionId   要同步的目录版本
     * @param forceFlag   强制更新标识
     * @param catalogFlag 当前目录标识
     */
    private void syncPatchProduct(IRequest iRequest, Product dto, Long versionId, Boolean forceFlag, String catalogFlag) {
        PatchlineMapping parameMapping = new PatchlineMapping();
        Product patchProduct = new Product();
        Product parameProduct = new Product();
        Long patchProductId = null;
        parameMapping.setProductId(dto.getVersionProductId());
        //获取原商品对应的补件商品的映射信息
        List<PatchlineMapping> patchProductInfos = patchlineMappingService.selectInfo(iRequest, parameMapping);
        if (CollectionUtils.isEmpty(patchProductInfos))
            return;
        //遍历
        for (PatchlineMapping pm : patchProductInfos) {
            pm = patchlineMappingService.selectByPrimaryKey(iRequest, pm);
            //判断商品和补件关系，是不是商品本身
            if (pm.getProductId().equals(pm.getPatchLineId())) {
                pm.setProductId(dto.getProductId());
                pm.setPatchLineId(dto.getProductId());
                Long patchlineMappingId = patchlineMappingService.selectByPatchineMappingAndProductId(pm);
                if (patchlineMappingId == null) {
                    pm.setMappingId(null);
                    patchlineMappingService.insertSelective(iRequest, pm);
                }
            } else {
                parameProduct = this.selectByProductId(pm.getPatchLineId());
                parameProduct.setCatalogversionId(versionId);
                patchProductId = productMapper.selectIdByCodeAndVersion(parameProduct);
                //根据目录版本选择字段
                if ((CATALOG_STAGED).equals(catalogFlag)) {
                    parameProduct = productMapper.selectStagedSpecifiedById(pm.getPatchLineId());
                } else if ((CATALOG_ONLINE).equals(catalogFlag)) {
                    parameProduct = productMapper.selectOnlineSpecifiedById(pm.getPatchLineId());
                }
                parameProduct.setCatalogversionId(versionId);
                //如果不存在
                if (patchProductId == null) {
                    patchProduct = this.insertSelective(iRequest, parameProduct);
                    pm.setProductId(dto.getProductId());
                    pm.setPatchLineId(patchProduct.getProductId());
                    pm.setMappingId(null);
                    patchlineMappingService.insert(iRequest, pm);
                } else {
                    if (forceFlag) {
                        this.updateByPrimaryKeySelective(iRequest, parameProduct);
                    } else {
                        this.selectByProductId(pm.getPatchLineId());
                        Product nextVersionProduct = this.selectByProductId(patchProductId);
                        Product lastVersionProduct = this.selectByProductId(pm.getPatchLineId());
                        if (lastVersionProduct.getLastUpdateDate().after(nextVersionProduct.getLastUpdateDate())) {
                            this.updateByPrimaryKeySelective(iRequest, parameProduct);
                        }
                    }
                    //判断是否需要插入映射关系
                    pm.setProductId(dto.getProductId());
                    pm.setPatchLineId(patchProductId);
                    Long patchlineMappingId = patchlineMappingService.selectByPatchineMappingAndProductId(pm);
                    if (patchlineMappingId == null) {
                        pm.setMappingId(null);
                        patchlineMappingService.insertSelective(iRequest, pm);
                    }
                }

            }
        }

    }

    /**
     * 商品界面中同步商品时需要先同步关联的基础商品
     *
     * @param iRequest
     * @param dto         同步后的商品
     * @param versionId   要同步的目录版本
     * @param forceFlag   强制更新标识
     * @param catalogFlag 当前目录标识
     * @return
     */
    private Product syncBaseProduct(IRequest iRequest, Product dto, Long versionId, Boolean forceFlag, String catalogFlag) {
        Product preProduct = this.selectByProductId(dto.getVersionProductId());
        Long baseProductId = preProduct.getBaseProductId();
        Product baseProduct = new Product();

        //版本选择
        if ((CATALOG_STAGED).equals(catalogFlag)) {
            baseProduct = productMapper.selectStagedSpecifiedById(baseProductId);
        } else if ((CATALOG_ONLINE).equals(catalogFlag)) {
            baseProduct = productMapper.selectOnlineSpecifiedById(baseProductId);
        }
        baseProduct.setCatalogversionId(versionId);
        baseProductId = productMapper.selectIdByCodeAndVersion(baseProduct);
        //下一版本不存在，新增，存在判断是否强制更新
        if (baseProductId == null) {
            baseProduct.setVersionProductId(baseProduct.getProductId());
            baseProduct.setProductId(null);
            baseProduct = this.insertSelective(iRequest, baseProduct);
        } else {
            //如果是强制更新
            if (forceFlag) {
                baseProduct = this.updateByPrimaryKeySelective(iRequest, baseProduct);
            } else {
                Product nextVersionProduct = this.selectByProductId(baseProductId);
                Product lastVersionProduct = this.selectByProductId(preProduct.getBaseProductId());
                if (lastVersionProduct.getLastUpdateDate().after(nextVersionProduct.getLastUpdateDate()))
                    baseProduct = this.updateByPrimaryKeySelective(iRequest, baseProduct);
                else
                    baseProduct = this.selectByProductId(baseProductId);
            }
        }

        return baseProduct;
    }

    /**
     * 同步商品的OD关系
     *
     * @param iRequest
     * @param dto
     */
    private void syncOdtype(IRequest iRequest, Product dto) {
        //获取商品对应的价格行
        List<Odtype> odtypes = null;
        odtypes = odtypeService.getOdtypeInfoByProductId(dto.getProductId());
        if (CollectionUtils.isNotEmpty(odtypes))
            odtypeService.batchDelete(odtypes);
        odtypes = odtypeService.getOdtypeInfoByProductId(dto.getVersionProductId());
        if (CollectionUtils.isEmpty(odtypes))
            return;
        for (Odtype o : odtypes) {
            o.setProductId(dto.getProductId());
            o.setOdtypeId(null);
            odtypeService.insertSelective(iRequest, o);
        }

    }

    /**
     * 同步商品关联的类别
     *
     * @param iRequest
     * @param dto
     */
    private void syncCategory(IRequest iRequest, Product dto) {
        ProductCategory parameCategory = new ProductCategory();

        //商品对应的类别映射关系
        List<CategoryMapping> mappingInfos = categoryMappingService.getMappingInfoByProductId(dto.getVersionProductId());
        if (CollectionUtils.isEmpty(mappingInfos))
            return;
        for (CategoryMapping mapping : mappingInfos) {
            CategoryMapping parameCategoryMapping = new CategoryMapping();
            parameCategory.setCategoryId(mapping.getCategoryId());
            ProductCategory category = categoryService.selectByPrimaryKey(iRequest, parameCategory);
            parameCategory.setCategoryCode(category.getCategoryCode());
            parameCategory.setCatalogVersion(dto.getCatalogversionId());
            Long categoryId = categoryService.selectByCodeAndVersion(parameCategory);
            if (categoryId == null) {
                category.setCategoryId(null);
                category.setCatalogVersion(dto.getCatalogversionId());
                category.setObjectVersionNumber(null);
                category = categoryService.insertSelective(iRequest, category);
                mapping.setMappingId(null);
                mapping.setProductId(dto.getProductId());
                mapping.setCategoryId(category.getCategoryId());
                categoryMappingService.insertSelective(iRequest, mapping);
            } else {
                parameCategoryMapping.setCategoryId(categoryId);
                parameCategoryMapping.setProductId(dto.getProductId());
                parameCategoryMapping = categoryMappingService.selectByCategoryAndProductId(parameCategoryMapping);
                if (parameCategoryMapping == null) {
                    mapping.setProductId(dto.getProductId());
                    mapping.setCategoryId(categoryId);
                    categoryMappingService.insertSelective(iRequest, mapping);
                }
            }
        }
    }

    /**
     * 商品同步完成后，需要将商品关联的价格行也同步
     */
    private void syncPriceRow(IRequest iRequest, Product dto, Long versionId) {
        Pricerow pricerow = new Pricerow();
        pricerow.setProductId(dto.getProductId());
        //获取商品对应的价格行
        List<Pricerow> pricerows = pricerowService.queryInfo(pricerow);
        if (CollectionUtils.isNotEmpty(pricerows))
            pricerowService.deletePricerowByProductId(dto);
        pricerow.setProductId(dto.getVersionProductId());
        pricerows = pricerowService.queryInfo(pricerow);
        if (CollectionUtils.isEmpty(pricerows))
            return;
        for (Pricerow pr : pricerows) {
            pr = pricerowService.selectByPrimaryKey(iRequest, pr);
            pr.setProductId(dto.getProductId());
            pr.setPricerowId(null);
            pr.setCatalogversionId(versionId);
            pricerowService.insertSelective(iRequest, pr);
        }
    }

    /**
     * 商品同步完成后，需要将商品关联的多媒体也同步
     */
    private void syncMedia(IRequest iRequest, Product dto, Long versionId, Boolean forceFlag) {
        Long preProductId = dto.getVersionProductId();
        List<Media> mediaList = mediaService.selectMediaByProductId(preProductId);
        if (CollectionUtils.isEmpty(mediaList))
            return;
        for (Media media : mediaList) {
            media.setCatalogversionId(versionId);
            //判断下一目录版本商品
            Long mediaId = mediaService.selectByCodeAndVersion(media);
            //查询当前多媒体信息
            Media media1 = mediaService.selectByPrimaryKey(iRequest, media);
            media1.setProductId(dto.getProductId());
            media1.setCatalogversionId(versionId);
            media1.setObjectVersionNumber(null);
            //如果下一版本的多媒体存在
            if (mediaId == null) {
                media1.setMediaId(null);
                mediaService.insertSelective(iRequest, media1);
            } else {
                //如果是强制更新
                media1.setMediaId(mediaId);
                if (forceFlag) {
                    mediaService.updateByPrimaryKeySelective(iRequest, media1);
                } else {
                    //查询下一版本的多媒体信息
                    Media media2 = mediaService.selectByMediaId(iRequest, mediaId);
                    if (media1.getLastUpdateDate().after(media2.getLastUpdateDate())) {
                        mediaService.updateByPrimaryKeySelective(iRequest, media1);
                    }
                }

            }

        }

    }

    @Override
    public List<Product> jobData(Product dto) {
        return productMapper.selectJobData(dto);
    }


    /**
     * 将master-staged版本商品同步到markor-staged版本
     *
     * @param productList
     * @param versionId
     * @return
     */
    @Override
    public List<Product> syncJob(IRequest iRequest, List<Product> productList, Long versionId) {
        if (CollectionUtils.isNotEmpty(productList)) {
            System.out.println("*****************" + productList.size());
//            for (Product dto : productList) {
//                dto.setCatalogversionId(versionId);
//                Long id = productMapper.selectIdByCodeAndVersion(dto);
//                if (id == null) {
//                    dto.set__status("add");
//                    dto.setProductId(null);
//                } else {
//                    dto.set__status("update");
//                    dto.setProductId(id);
//                }
//            }
//            return this.batchUpdate(iRequest, productList);

            List<Product> results = productList.stream().peek(new Consumer<Product>() {
                @Override
                public void accept(Product product) {
                    syncProduct(iRequest, product, versionId, false, CATALOG_STAGED);
                    logger.info(product.getCode() + "  " + product.getCatalogversion());
                }
            }).collect(Collectors.toList());
            return results;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see IProductService#selectByProductId(Long)
     */
    @Override
    public Product selectByProductId(Long productId) {
        Product product = new Product();
        product.setProductId(productId);
        return mapper.selectByPrimaryKey(product);
    }

    /**
     * 自动上架
     *
     * @param productList
     */
    @Override
    public String autoUpShelf(List<Product> productList, String odtype) {
        //封装参数，日期格式转换
        Product product = new Product();
        if (CollectionUtils.isEmpty(productList))
            return null;
        product = productList.get(0);
        product.setListingTime(DateUtil.getStrToDateTime(product.getCreationDateFrom()));
        product.setDelistingTime(DateUtil.getStrToDateTime(product.getCreationDateTo()));
        //获取满足查询条件的所有数据
        List<Product> queryResult = this.selectProductList(product);
        return upShelf(queryResult, odtype);
    }

    /**
     * 自动下架
     *
     * @param productList
     */
    @Override
    public String autoDownShelf(List<Product> productList, String odtype) {
        //请求参数封装
        Product productCon = new Product();
        StringBuffer unsuitableSb = new StringBuffer("不满足自动下架条件的商品：[");
        if (CollectionUtils.isNotEmpty(productList)) {
            productCon = productList.get(0);
            productCon.setListingTime(DateUtil.getStrToDateTime(productCon.getCreationDateFrom()));
            productCon.setDelistingTime(DateUtil.getStrToDateTime(productCon.getCreationDateTo()));
        }
        //满足自动下架的商品
        List<Product> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(this.selectProductList(productCon))) {
            for (Product product : this.selectProductList(productCon)) {
                if (!judgmentFun(product)) {
                    result.add(product);
                } else {
                    unsuitableSb.append(product.getCode() + ",");
                }
            }
        }
        if (CollectionUtils.isEmpty(result))
            throw new RuntimeException("商品不满足自动下架条件");
        return downShelf(result, odtype) + unsuitableSb.toString() + "]";
    }

    /**
     * 上架
     *
     * @param productList
     */
    @Override
    public String upShelf(List<Product> productList, String odtype) {
        return judgmentProductType(productList, odtype);
    }

    /**
     * 下架
     *
     * @param productList
     */
    @Override
    public String downShelf(List<Product> productList, String odtype) {
        IRequest iRequest = RequestHelper.newEmptyRequest();
        StringBuffer successSb = new StringBuffer("下架成功的商品：[");
        StringBuffer failSb = new StringBuffer("下架失败的商品：[");
        if (CollectionUtils.isEmpty(productList) || odtype == null)
            return null;

        if (("main").equals(odtype)) {
            for (Product product : productList) {
                product.setApprovalStatus("UNAPPROVED");
                if (this.updateByPrimaryKeySelective(iRequest, product) != null) {
                    updateMediaSyncflag(product, Constants.YES);
                    successSb.append(product.getCode() + ",");
                } else {
                    failSb.append(product.getCode() + ",");
                }
            }
        } else if (("normal").equals(odtype)) {
            Odtype paramOdtype = new Odtype();
            for (Product product : productList) {
                Odtype odtype1 = getByProductAndCustChanSrc(product, "1");
                if (odtype1 == null)
                    continue;
                if (("Y").equals(odtype1.getIsUsed())) {
                    odtype1.setApprovalStatus("UNAPPROVED");
                    if (odtypeService.updateByPrimaryKeySelective(iRequest, odtype1) != null) {
                        updateMediaSyncflag(product, Constants.YES);
                        successSb.append(product.getCode() + ",");
                    } else
                        failSb.append(product.getCode() + ",");
                }
            }
        } else if (("super").equals(odtype)) {
            Odtype paramOdtype = new Odtype();
            for (Product product : productList) {
                Odtype odtype1 = getByProductAndCustChanSrc(product, "2");
                if (odtype1 == null)
                    continue;
                if (("Y").equals(odtype1.getIsUsed())) {
                    odtype1.setApprovalStatus("UNAPPROVED");
                    if (odtypeService.updateByPrimaryKeySelective(iRequest, odtype1) != null) {
                        updateMediaSyncflag(product, Constants.YES);
                        successSb.append(product.getCode() + ",");
                    } else
                        failSb.append(product.getCode() + ",");
                } else {

                }
            }
        }
        return successSb.toString() + "]" + failSb.toString() + "]";
    }

    /**
     * 商品下架时，自动将商品id在HMALL_MST_MEDIA表中对应多媒体记录的SYNCFLAG置为‘Y’
     *
     * @param product
     */
    private void updateMediaSyncflag(Product product, String syncflag) {
        List<Media> mediaList = mediaService.selectAllByProductId(product.getProductId(), product.getCatalogversionId());
        if (CollectionUtils.isNotEmpty(mediaList)) {
            for (Media media : mediaList) {
                media.setSyncflag(syncflag);
                mediaService.updateByPrimaryKeySelective(RequestHelper.newEmptyRequest(), media);
            }
        }
    }

    /**
     * 商品类型判断
     *
     * @return 上架商品
     */
    private String judgmentProductType(List<Product> productList, String odtype) {
        IRequest iRequest = RequestHelper.newEmptyRequest();
        StringBuffer successSb = new StringBuffer("上架成功的商品：[");
        StringBuffer failSb = new StringBuffer("上架失败的商品：[");
        StringBuffer unsuitableSb = new StringBuffer("不满足条件的商品：[");

        //商品上架缓冲期
        List<CodeValue> listingTimeData = codeService.selectCodeValuesByCodeName(iRequest, "HMALL.PRODUCT.LISTING_TIME");
        if (CollectionUtils.isEmpty(listingTimeData)) {
            return "商品上架缓冲期快码值没有维护，不能上架商品";
        }
        int listingTimeValue = Integer.parseInt(listingTimeData.get(0).getValue());

        if (odtype == null || CollectionUtils.isEmpty(productList)) {
            return null;
        }
        //校验多媒体数据后可以上架的商品list
        List<Product> checkList = new ArrayList<>();
        for (Product product : productList) {
            //商品上架时，校验商品id在HMALL_MST_MEDIA表中是否存在记录，如果没有记录则不允许上架
            List<Media> mediaList = mediaService.selectAllByProductId(product.getProductId(), product.getCatalogversionId());
            if (CollectionUtils.isNotEmpty(mediaList)) {
                if (product.getListingTime() != null) {
                    Date now = new Date();
                    //当缓冲期设置为0时 即必须到上市时间放可生效
                    if (listingTimeValue == 0) {
                        if (!product.getListingTime().after(now)) {
                            checkList.add(product);
                        } else {
                            unsuitableSb.append(product.getCode() + "上架时间不在允许的时间范围内,");
                        }
                    } else {
                        //判断商品上市日期前推缓冲期后 是否大于等于当前日期
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(product.getListingTime());
                        calendar.add(Calendar.DATE, -listingTimeValue);
                        Date listingTime = calendar.getTime();
                        if (!now.before(listingTime)) {
                            checkList.add(product);
                        } else {
                            unsuitableSb.append(product.getCode() + "上架时间不在允许的时间范围内,");
                        }
                    }
                } else {
                    unsuitableSb.append(product.getCode() + "上市时间为空,");
                }
            } else {
                unsuitableSb.append(product.getCode() + "多媒体数据为空,");
            }
        }

        if (("main").equals(odtype)) {
            for (Product product : checkList) {
                if (judgmentFun(product)) {
                    product.setApprovalStatus("APPROVED");
                    if (this.updateByPrimaryKeySelective(iRequest, product) != null) {
                        updateMediaSyncflag(product, Constants.NO);
                        successSb.append(product.getCode() + ",");
                    } else
                        failSb.append(product.getCode() + ",");
                } else {
                    unsuitableSb.append(product.getCode() + ",");
                }
            }
        } else if (("normal").equals(odtype)) {
            for (Product product : checkList) {
                if (!judgmentFun(product)) {
                    unsuitableSb.append(product.getCode() + ",");
                    continue;
                }
                Odtype odtype1 = getByProductAndCustChanSrc(product, "1");
                if (odtype1 == null)
                    continue;
                if (("Y").equals(odtype1.getIsUsed())) {
                    odtype1.setApprovalStatus("APPROVED");
                    if (odtypeService.updateByPrimaryKeySelective(iRequest, odtype1) != null) {
                        successSb.append(product.getCode() + ",");
                        updateMediaSyncflag(product, Constants.NO);
                    } else
                        failSb.append(product.getCode() + ",");
                } else {
                    unsuitableSb.append(product.getCode() + ",");
                }
            }
        } else if (("super").equals(odtype)) {
            for (Product product : checkList) {
                if (!judgmentFun(product)) {
                    unsuitableSb.append(product.getCode() + ",");
                    continue;
                }
                Odtype odtype1 = getByProductAndCustChanSrc(product, "2");
                if (odtype1 == null)
                    continue;
                if (("Y").equals(odtype1.getIsUsed())) {
                    odtype1.setApprovalStatus("APPROVED");
                    if (odtypeService.updateByPrimaryKeySelective(iRequest, odtype1) != null) {
                        successSb.append(product.getCode() + ",");
                        updateMediaSyncflag(product, Constants.NO);
                    } else
                        failSb.append(product.getCode() + ",");
                } else {
                    unsuitableSb.append(product.getCode() + ",");
                }
            }
        }
        return successSb.toString() + "]" + failSb.toString() + "]" + unsuitableSb.toString() + "]";
    }

    /**
     * 获取Odtype对象
     *
     * @param product     商品中的Id
     * @param custChanSrc 定制类型
     * @return 查询对象
     */
    private Odtype getByProductAndCustChanSrc(Product product, String custChanSrc) {
        Odtype result = new Odtype();
        result.setProductId(product.getProductId());
        result.setCustChanSrc(custChanSrc);
        List<Odtype> odtypeInfo = odtypeService.getByProductAndCustChanSrc(result);
        if (CollectionUtils.isNotEmpty(odtypeInfo))
            result = odtypeInfo.get(0);
        return result;
    }

    /**
     * 上架条件判断
     *
     * @param product
     * @return
     */
    private Boolean judgmentFun(Product product) {
        Boolean flag = false;
        product = this.selectByProductId(product.getProductId());
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //获取当前时间
            Date now = df.parse(df.format(new Date()));
            //上市时间
            Date listingTime = null;
            if (product.getListingTime() == null) {
                throw new RuntimeException("编码为" + product.getCode() + "的商品上市时间没有指定");
            } else {
                listingTime = df.parse(df.format(product.getListingTime()));
            }

            //退市时间
            Date delistingTime = null;
            if (product.getDelistingTime() == null) {
                product.setDelistingTime(df.parse("2099-01-01 12:00:00"));
            }
            delistingTime = df.parse(df.format(product.getDelistingTime()));

            //销售状态
            String saleStatus = product.getSaleStatus() == null ? "" : product.getSaleStatus().toString();

            //产品状态
            String productStatus = product.getProductStatus() == null ? "" : product.getProductStatus().toString();

            //是否单独销售
            String isSinSale = product.getIsSinSale() == null ? "" : product.getIsSinSale().toString();

            if (("A1".equalsIgnoreCase(saleStatus) || "A2".equalsIgnoreCase(saleStatus))
                    && delistingTime.after(now)
                    && ("A1".equalsIgnoreCase(productStatus) || "A2".equalsIgnoreCase(productStatus))
                    && "Y".equalsIgnoreCase(isSinSale)) {
                flag = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public String checkProduct(IRequest iRequest, List<Product> list) {
        //存储正确的商品数据，以便更新商品
        List<Product> productList = new ArrayList<>();

        //校验输入的值是否在快码表中存在
        List<CodeValue> yesOrNOData = codeService.selectCodeValuesByCodeName(iRequest, "SYS.YES_NO");
        List<CodeValue> mainColorData = codeService.selectCodeValuesByCodeName(iRequest, "HMALL.PRODUCT.MAINCOLOR");
        List<CodeValue> shippingTypeData = codeService.selectCodeValuesByCodeName(iRequest, "HMALL.SHIPPING_TYPE");
        List<CodeValue> specialFunctionData = codeService.selectCodeValuesByCodeName(iRequest, "HMALL.PRODUCT.SPECIALFUNCTION");
        List<CodeValue> softCombinationData = codeService.selectCodeValuesByCodeName(iRequest, "HMALL.PRODUCT.SOFTCOMBINATION");
        List<CodeValue> filterData = codeService.selectCodeValuesByCodeName(iRequest, "HMALL.PRODUCT.FILTER");
        List<CodeValue> suitableUsersData = codeService.selectCodeValuesByCodeName(iRequest, "HMALL.PRODUCT.SUITABLE_USERS");
        List<CodeValue> materialData = codeService.selectCodeValuesByCodeName(iRequest, "HMALL.PRODUCT.MATERIAL");
        List<CodeValue> woodenCraftsData = codeService.selectCodeValuesByCodeName(iRequest, "HMALL.PRODUCT.WOODEN_CRAFTS");
        List<CodeValue> designElementData = codeService.selectCodeValuesByCodeName(iRequest, "HMALL.PRODUCT.DESIGN_ELEMENT");
        List<CodeValue> leatherRangeData = codeService.selectCodeValuesByCodeName(iRequest, "HMALL.PRODUCT.LEATHER_RANGE");
        List<CodeValue> leatherCraftData = codeService.selectCodeValuesByCodeName(iRequest, "HMALL.PRODUCT.LEATHER_CRAFT");
        List<CodeValue> drawerRailNumData = codeService.selectCodeValuesByCodeName(iRequest, "HMALL.PRODUCT.DRAWER_RAIL_NUM");
        List<CodeValue> paintCraftData = codeService.selectCodeValuesByCodeName(iRequest, "HMALL.PRODUCT.PAINT_CRAFT");
        List<CodeValue> drawerNumbermData = codeService.selectCodeValuesByCodeName(iRequest, "HMALL.PRODUCT.DRAWER_NUMBER");
        List<CodeValue> seriesNameData = codeService.selectCodeValuesByCodeName(iRequest, "HMALL.PRODUCT.SERIES_NAME");
        List<CodeValue> customChannelSourceData = codeService.selectCodeValuesByCodeName(iRequest, "HMALL.PRODUCT.CUSTOM_CHANNEL");
        List<CodeValue> customSupportTypeData = codeService.selectCodeValuesByCodeName(iRequest, "HMALL.PRODUCT.CUSTOM_SUPPORT");

        String result = "";
        if (CollectionUtils.isNotEmpty(list)) {
            for (Product product : list) {

                if (product.getCode() == null) {
                    result = "商品编码不能为空!";
                    continue;
                }

                //验证版本目录
                List<Catalogversion> catalogList = iCatalogversionService.selectCatalogVersion();
                for (Catalogversion catalogversion : catalogList) {
                    if (product.getVersionCode() != null) {
                        if (product.getVersionCode().equals(catalogversion.getCatalogName())) {
                            product.setCatalogversionId(catalogversion.getCatalogversionId());
                            break;
                        }
                    } else {
                        //目录版本不填，默认为ZMALL商城-staged
                        if ("ZMALL商城-staged".equals(catalogversion.getCatalogName())) {
                            product.setCatalogversionId(catalogversion.getCatalogversionId());
                            break;
                        }
                    }
                }

                //更新操作，确保数据不为空，以及唯一性
                List<Product> pList = this.selectByCodeAndCatalogVersion(product);
                if (CollectionUtils.isEmpty(pList)) {
                    result = result + product.getVersionCode() + "的" + product.getCode() + "商品不存在！";
                    continue;
                } else if (pList.size() > 1) {
                    result = result + product.getVersionCode() + "的" + product.getCode() + "商品不唯一！";
                    continue;
                } else {
                    //设置商品主键
                    product.setProductId(pList.get(0).getProductId());
                }
                //验证枚举值标志位
                boolean checkFlag = true;
                //验证是否字段
                for (CodeValue codeValue : yesOrNOData) {
                    //是否积分商品
                    if (product.getRewardProduct() != null && product.getRewardProduct().equals(codeValue.getMeaning())) {
                        product.setRewardProduct(codeValue.getValue());
                        checkFlag = false;
                        break;
                    }
                }
                if (checkFlag && product.getRewardProduct() != null) {
                    result = result + product.getVersionCode() + "的" + product.getCode() + "是否积分商品字段不符合名称范围";
                }

                checkFlag = true;
                for (CodeValue codeValue : yesOrNOData) {
                    //是否需要组装
                    if (product.getIsPackaging() != null && product.getIsPackaging().equals(codeValue.getMeaning())) {
                        product.setIsPackaging(codeValue.getValue());
                        checkFlag = false;
                        break;
                    }
                }
                if (checkFlag && product.getIsPackaging() != null) {
                    result = result + product.getVersionCode() + "的" + product.getCode() + "是否需要组装字段不符合名称范围";
                }

                checkFlag = true;
                for (CodeValue codeValue : yesOrNOData) {
                    //是否带储物空间
                    if (product.getIsStorage() != null && product.getIsStorage().equals(codeValue.getMeaning())) {
                        product.setIsStorage(codeValue.getValue());
                        checkFlag = false;
                        break;
                    }
                }
                if (checkFlag && product.getIsStorage() != null) {
                    result = result + product.getVersionCode() + "的" + product.getCode() + "是否带储物空间字段不符合名称范围";
                }

                checkFlag = true;
                for (CodeValue codeValue : yesOrNOData) {
                    //是否可拆洗
                    if (product.getIsWashable() != null && product.getIsWashable().equals(codeValue.getMeaning())) {
                        product.setIsWashable(codeValue.getValue());
                        checkFlag = false;
                        break;
                    }
                }
                if (checkFlag && product.getIsWashable() != null) {
                    result = result + product.getVersionCode() + "的" + product.getCode() + "是否可拆洗字段不符合名称范围";
                }

                checkFlag = true;
                for (CodeValue codeValue : yesOrNOData) {
                    //是否带软靠
                    if (product.getIsSoftCushion() != null && product.getIsSoftCushion().equals(codeValue.getMeaning())) {
                        product.setIsSoftCushion(codeValue.getValue());
                        checkFlag = false;
                        break;
                    }
                }
                if (checkFlag && product.getIsSoftCushion() != null) {
                    result = result + product.getVersionCode() + "的" + product.getCode() + "是否可拆洗字段不符合名称范围";
                }

                checkFlag = true;
                for (CodeValue codeValue : yesOrNOData) {
                    //是否带滚轮
                    if (product.getIsWheel() != null && product.getIsWheel().equals(codeValue.getMeaning())) {
                        product.setIsWheel(codeValue.getValue());
                        checkFlag = false;
                        break;
                    }
                }
                if (checkFlag && product.getIsWheel() != null) {
                    result = result + product.getVersionCode() + "的" + product.getCode() + "是否带滚轮字段不符合名称范围";
                }

                checkFlag = true;
                for (CodeValue codeValue : yesOrNOData) {
                    //是否带阻尼
                    if (product.getIsDamping() != null && product.getIsDamping().equals(codeValue.getMeaning())) {
                        product.setIsDamping(codeValue.getValue());
                        checkFlag = false;
                        break;
                    }
                }
                if (checkFlag && product.getIsDamping() != null) {
                    result = result + product.getVersionCode() + "的" + product.getCode() + "是否带阻尼字段不符合名称范围";
                }

                checkFlag = true;
                for (CodeValue codeValue : yesOrNOData) {
                    //是否主商品
                    if (product.getIsMain() != null && product.getIsMain().equals(codeValue.getMeaning())) {
                        product.setIsMain(codeValue.getValue());
                        checkFlag = false;
                        break;
                    }
                }
                if (checkFlag && product.getIsMain() != null) {
                    result = result + product.getVersionCode() + "的" + product.getCode() + "是否主商品字段不符合名称范围";
                }

                checkFlag = true;
                //主颜色
                for (CodeValue codeValue : mainColorData) {
                    if (product.getMainColor() != null && product.getMainColor().equals(codeValue.getMeaning())) {
                        product.setMainColor(codeValue.getValue());
                        checkFlag = false;
                        break;
                    }
                }
                if (checkFlag && product.getMainColor() != null) {
                    result = result + product.getVersionCode() + "的" + product.getCode() + "主颜色字段不符合名称范围";
                }

                checkFlag = true;
                //运送方式
                for (CodeValue codeValue : shippingTypeData) {
                    if (product.getDefaultDelivery() != null && product.getDefaultDelivery().equals(codeValue.getMeaning())) {
                        product.setDefaultDelivery(codeValue.getValue());
                        checkFlag = false;
                        break;
                    }
                }
                if (checkFlag && product.getDefaultDelivery() != null) {
                    result = result + product.getVersionCode() + "的" + product.getCode() + "运送方式字段不符合名称范围";
                }

                //附加功能
                checkFlag = true;
                for (CodeValue codeValue : specialFunctionData) {
                    if (product.getAddFunction1() != null && product.getAddFunction1().equals(codeValue.getMeaning())) {
                        product.setAddFunction1(codeValue.getValue());
                        checkFlag = false;
                        break;
                    }
                }
                if (checkFlag && product.getAddFunction1() != null) {
                    result = result + product.getVersionCode() + "的" + product.getCode() + "特殊功能1字段不符合名称范围";
                }

                checkFlag = true;
                for (CodeValue codeValue : specialFunctionData) {
                    if (product.getAddFunction2() != null && product.getAddFunction2().equals(codeValue.getMeaning())) {
                        product.setAddFunction2(codeValue.getValue());
                        checkFlag = false;
                        break;
                    }
                }
                if (checkFlag && product.getAddFunction2() != null) {
                    result = result + product.getVersionCode() + "的" + product.getCode() + "特殊功能2字段不符合名称范围";
                }

                checkFlag = true;
                for (CodeValue codeValue : specialFunctionData) {
                    if (product.getAddFunction3() != null && product.getAddFunction3().equals(codeValue.getMeaning())) {
                        product.setAddFunction3(codeValue.getValue());
                        checkFlag = false;
                        break;
                    }
                }
                if (checkFlag && product.getAddFunction3() != null) {
                    result = result + product.getVersionCode() + "的" + product.getCode() + "特殊功能3字段不符合名称范围";
                }

                checkFlag = true;
                //沙发组合方式
                for (CodeValue codeValue : softCombinationData) {
                    if (product.getSofaCombiningForm() != null && product.getSofaCombiningForm().equals(codeValue.getMeaning())) {
                        product.setSofaCombiningForm(codeValue.getValue());
                        checkFlag = false;
                        break;
                    }
                }
                if (checkFlag && product.getSofaCombiningForm() != null) {
                    result = result + product.getVersionCode() + "的" + product.getCode() + "沙发组合方式字段不符合名称范围";
                }

                checkFlag = true;
                //填充物
                for (CodeValue codeValue : filterData) {
                    if (product.getFiller1() != null && product.getFiller1().equals(codeValue.getMeaning())) {
                        product.setFiller1(codeValue.getValue());
                        checkFlag = false;
                        break;
                    }
                }
                if (checkFlag && product.getFiller1() != null) {
                    result = result + product.getVersionCode() + "的" + product.getCode() + "填充物1字段不符合名称范围";
                }

                checkFlag = true;
                for (CodeValue codeValue : filterData) {
                    if (product.getFiller2() != null && product.getFiller2().equals(codeValue.getMeaning())) {
                        product.setFiller2(codeValue.getValue());
                        checkFlag = false;
                        break;
                    }
                }
                if (checkFlag && product.getFiller2() != null) {
                    result = result + product.getVersionCode() + "的" + product.getCode() + "填充物2字段不符合名称范围";
                }

                checkFlag = true;
                for (CodeValue codeValue : filterData) {
                    if (product.getFiller3() != null && product.getFiller3().equals(codeValue.getMeaning())) {
                        product.setFiller3(codeValue.getValue());
                        checkFlag = false;
                        break;
                    }
                }
                if (checkFlag && product.getFiller3() != null) {
                    result = result + product.getVersionCode() + "的" + product.getCode() + "填充物3字段不符合名称范围";
                }

                checkFlag = true;
                //适用对象
                for (CodeValue codeValue : suitableUsersData) {
                    if (product.getSuitableUsers() != null && product.getSuitableUsers().equals(codeValue.getMeaning())) {
                        product.setSuitableUsers(codeValue.getValue());
                        checkFlag = false;
                        break;
                    }
                }
                if (checkFlag && product.getSuitableUsers() != null) {
                    result = result + product.getVersionCode() + "的" + product.getCode() + "适用对象字段不符合名称范围";
                }

                checkFlag = true;
                //材质
                for (CodeValue codeValue : materialData) {
                    if (product.getMaterial() != null && product.getMaterial().equals(codeValue.getMeaning())) {
                        product.setMaterial(codeValue.getValue());
                        checkFlag = false;
                        break;
                    }
                }
                if (checkFlag && product.getMaterial() != null) {
                    result = result + product.getVersionCode() + "的" + product.getCode() + "材质字段不符合名称范围";
                }

                checkFlag = true;
                //木质工艺
                for (CodeValue codeValue : woodenCraftsData) {
                    if (product.getWoodenCrafts1() != null && product.getWoodenCrafts1().equals(codeValue.getMeaning())) {
                        product.setWoodenCrafts1(codeValue.getValue());
                        checkFlag = false;
                        break;
                    }
                }
                if (checkFlag && product.getWoodenCrafts1() != null) {
                    result = result + product.getVersionCode() + "的" + product.getCode() + "木质工艺1不符合名称范围";
                }

                checkFlag = true;
                for (CodeValue codeValue : woodenCraftsData) {
                    if (product.getWoodenCrafts2() != null && product.getWoodenCrafts2().equals(codeValue.getMeaning())) {
                        product.setWoodenCrafts2(codeValue.getValue());
                        checkFlag = false;
                        break;
                    }
                }
                if (checkFlag && product.getWoodenCrafts2() != null) {
                    result = result + product.getVersionCode() + "的" + product.getCode() + "木质工艺2不符合名称范围";
                }

                checkFlag = true;
                for (CodeValue codeValue : woodenCraftsData) {
                    if (product.getWoodenCrafts3() != null && product.getWoodenCrafts3().equals(codeValue.getMeaning())) {
                        product.setWoodenCrafts3(codeValue.getValue());
                        checkFlag = false;
                        break;
                    }
                }
                if (checkFlag && product.getWoodenCrafts3() != null) {
                    result = result + product.getVersionCode() + "的" + product.getCode() + "木质工艺3不符合名称范围";
                }

                checkFlag = true;
                //设计元素
                for (CodeValue codeValue : designElementData) {
                    if (product.getDesignElement1() != null && product.getDesignElement1().equals(codeValue.getMeaning())) {
                        product.setDesignElement1(codeValue.getValue());
                        checkFlag = false;
                        break;
                    }
                }
                if (checkFlag && product.getDesignElement1() != null) {
                    result = result + product.getVersionCode() + "的" + product.getCode() + "设计元素1不符合名称范围";
                }

                checkFlag = true;
                for (CodeValue codeValue : designElementData) {
                    if (product.getDesignElement2() != null && product.getDesignElement2().equals(codeValue.getMeaning())) {
                        product.setDesignElement2(codeValue.getValue());
                        checkFlag = false;
                        break;
                    }
                }
                if (checkFlag && product.getDesignElement2() != null) {
                    result = result + product.getVersionCode() + "的" + product.getCode() + "设计元素2不符合名称范围";
                }

                checkFlag = true;
                for (CodeValue codeValue : designElementData) {
                    if (product.getDesignElement3() != null && product.getDesignElement3().equals(codeValue.getMeaning())) {
                        product.setDesignElement3(codeValue.getValue());
                        checkFlag = false;
                        break;
                    }
                }
                if (checkFlag && product.getDesignElement3() != null) {
                    result = result + product.getVersionCode() + "的" + product.getCode() + "设计元素3不符合名称范围";
                }

                checkFlag = true;
                //皮革饰面范围
                for (CodeValue codeValue : leatherRangeData) {
                    if (product.getLeatherRange1() != null && product.getLeatherRange1().equals(codeValue.getMeaning())) {
                        product.setLeatherRange1(codeValue.getValue());
                        checkFlag = false;
                        break;
                    }
                }
                if (checkFlag && product.getLeatherRange1() != null) {
                    result = result + product.getVersionCode() + "的" + product.getCode() + "皮革饰面范围1不符合名称范围";
                }

                checkFlag = true;
                for (CodeValue codeValue : leatherRangeData) {
                    if (product.getLeatherRange2() != null && product.getLeatherRange2().equals(codeValue.getMeaning())) {
                        product.setLeatherRange2(codeValue.getValue());
                        checkFlag = false;
                        break;
                    }
                }
                if (checkFlag && product.getLeatherRange2() != null) {
                    result = result + product.getVersionCode() + "的" + product.getCode() + "皮革饰面范围2不符合名称范围";
                }

                checkFlag = true;
                for (CodeValue codeValue : leatherRangeData) {
                    if (product.getLeatherRange3() != null && product.getLeatherRange3().equals(codeValue.getMeaning())) {
                        product.setLeatherRange3(codeValue.getValue());
                        checkFlag = false;
                        break;
                    }
                }
                if (checkFlag && product.getLeatherRange3() != null) {
                    result = result + product.getVersionCode() + "的" + product.getCode() + "皮革饰面范围3不符合名称范围";
                }

                checkFlag = true;
                //皮革饰面工艺
                for (CodeValue codeValue : leatherCraftData) {
                    if (product.getLeatherCraft1() != null && product.getLeatherCraft1().equals(codeValue.getMeaning())) {
                        product.setLeatherCraft1(codeValue.getValue());
                        checkFlag = false;
                        break;
                    }
                }
                if (checkFlag && product.getLeatherCraft1() != null) {
                    result = result + product.getVersionCode() + "的" + product.getCode() + "皮革饰面工艺1不符合名称范围";
                }

                checkFlag = true;
                for (CodeValue codeValue : leatherCraftData) {
                    if (product.getLeatherCraft2() != null && product.getLeatherCraft2().equals(codeValue.getMeaning())) {
                        product.setLeatherCraft2(codeValue.getValue());
                        checkFlag = false;
                        break;
                    }
                }
                if (checkFlag && product.getLeatherCraft2() != null) {
                    result = result + product.getVersionCode() + "的" + product.getCode() + "皮革饰面工艺2不符合名称范围";
                }

                checkFlag = true;
                for (CodeValue codeValue : leatherCraftData) {
                    if (product.getLeatherCraft3() != null && product.getLeatherCraft3().equals(codeValue.getMeaning())) {
                        product.setLeatherCraft3(codeValue.getValue());
                        checkFlag = false;
                        break;
                    }
                }
                if (checkFlag && product.getLeatherCraft3() != null) {
                    result = result + product.getVersionCode() + "的" + product.getCode() + "皮革饰面工艺3不符合名称范围";
                }

                checkFlag = true;
                //抽屉导轨节数
                for (CodeValue codeValue : drawerRailNumData) {
                    if (product.getDrawerRailNum() != null && product.getDrawerRailNum().equals(codeValue.getMeaning())) {
                        product.setDrawerRailNum(codeValue.getValue());
                        checkFlag = false;
                        break;
                    }
                }
                if (checkFlag && product.getDrawerRailNum() != null) {
                    result = result + product.getVersionCode() + "的" + product.getCode() + "抽屉导轨节数不符合名称范围";
                }

                checkFlag = true;
                //饰面油漆工艺
                for (CodeValue codeValue : paintCraftData) {
                    if (product.getPaintCraft() != null && product.getPaintCraft().equals(codeValue.getMeaning())) {
                        product.setPaintCraft(codeValue.getValue());
                        checkFlag = false;
                        break;
                    }
                }
                if (checkFlag && product.getPaintCraft() != null) {
                    result = result + product.getVersionCode() + "的" + product.getCode() + "抽屉导轨节数不符合名称范围";
                }

                checkFlag = true;
                //抽屉数量
                for (CodeValue codeValue : drawerNumbermData) {
                    if (product.getDrawerNumber() != null && product.getDrawerNumber().equals(codeValue.getMeaning())) {
                        product.setDrawerNumber(codeValue.getValue());
                        checkFlag = false;
                        break;
                    }
                }
                if (checkFlag && product.getDrawerNumber() != null) {
                    result = result + product.getVersionCode() + "的" + product.getCode() + "抽屉数量不符合名称范围";
                }

                checkFlag = true;
                //系列名称
                for (CodeValue codeValue : seriesNameData) {
                    if (product.getSeriesName() != null && product.getSeriesName().equals(codeValue.getMeaning())) {
                        product.setSeriesName(codeValue.getValue());
                        checkFlag = false;
                        break;
                    }
                }
                if (checkFlag && product.getSeriesName() != null) {
                    result = result + product.getVersionCode() + "的" + product.getCode() + "系列名称不符合名称范围";
                }

                checkFlag = true;
                //定制支持类型
                for (CodeValue codeValue : customSupportTypeData) {
                    if (product.getCustomSupportType() != null && product.getCustomSupportType().equals(codeValue.getMeaning())) {
                        product.setCustomSupportType(codeValue.getValue());
                        checkFlag = false;
                        break;
                    }
                }
                if (checkFlag && product.getCustomSupportType() != null) {
                    result = result + product.getVersionCode() + "的" + product.getCode() + "定制支持类型不符合名称范围";
                }

                checkFlag = true;
                //定制频道来源
                for (CodeValue codeValue : customChannelSourceData) {
                    if (product.getCustomChannelSource() != null && product.getCustomChannelSource().equals(codeValue.getMeaning())) {
                        product.setCustomChannelSource(codeValue.getValue());
                        checkFlag = false;
                        break;
                    }
                }
                if (checkFlag && product.getCustomChannelSource() != null) {
                    result = result + product.getVersionCode() + "的" + product.getCode() + "定制频道来源不符合名称范围";
                }

                //操作类别编码
                if (product.getCategoryList() != null) {
                    List<Long> categoryIdList = new ArrayList<>(); //获取类别编码id
                    String[] categoryList = product.getCategoryList().split(";");
                    for (String categoryCode : categoryList) {
                        //通过类别编码和版本目录去查找类别编码id
                        ProductCategory productCategory = new ProductCategory();
                        productCategory.setCatalogVersion(product.getCatalogversionId());
                        productCategory.setCategoryCode(categoryCode);
                        ProductCategory category = productCategoryService.queryCategoryByCategoryIdAndVersion(productCategory);
                        if (category == null) {
                            result = result + "您输入的" + product.getVersionCode() + "版本的类别编码" + categoryCode + "不存在！";
                            break;
                        } else {
                            //获取表格中输入的所有类别编码的id
                            categoryIdList.add(category.getCategoryId());

                            //若输入的对应的版本目录的类别编码存在，则去判断类别映射表中是否存在对应关系
                            CategoryMapping categoryMapping = new CategoryMapping();
                            categoryMapping.setProductId(product.getProductId());
                            categoryMapping.setCategoryId(category.getCategoryId());
                            List<CategoryMapping> categoryMappingsList = categoryMappingService.select(iRequest, categoryMapping, 1, 10);
                            if (CollectionUtils.isEmpty(categoryMappingsList)) {
                                //若数据库中不存在该对应关系，则插入
                                categoryMappingService.insertSelective(iRequest, categoryMapping);
                            }
                        }
                    }

                    if (StringUtils.isEmpty(result) && CollectionUtils.isNotEmpty(categoryIdList)) {
                        //删除之前不在现在表格内的类别
                        Map<String, Object> map = new HashMap<>();
                        map.put("productId", product.getProductId());
                        map.put("categoryIdList", categoryIdList);
                        categoryMappingService.deleteCategoryMappingRelationShip(map);
                    }

                }
                //是否定制
                String hscustom=product.getHascustom();
                if(!(hscustom==null||hscustom.equals("1")||hscustom.equals("0"))){
                    result=result+"是否定制只可以选择1、0、或者为空";
                }
                //若所有的字段数据都合法，则保存在productList中，以便更新
                if (StringUtils.isEmpty(result)) {
                    productList.add(product);
                }
            }
        }
        //更新所有正确商品的数据
        if (CollectionUtils.isNotEmpty(productList))

        {
            for (Product p : productList) {
                this.updateByPrimaryKeySelective(iRequest, p);
                //当传入的是否定制为空时，更新HMALL_MST_PRODUCT表中的是否定制字段为空
                if(p.getHascustom()==null&&p.getProductId()!=null){
                    productMapper.updateSetHascustomIsNull(p.getProductId());
                }
                //this.updateByPrimaryKey()
                //操作频道表
                for (CodeValue codeValue : yesOrNOData) {
                    if (p.getIsNormalUsed() != null && p.getIsNormalUsed().equals(codeValue.getMeaning())) {
                        p.setIsNormalUsed(codeValue.getValue());
                        Odtype o = new Odtype();
                        o.setCustChanSrc(Constants.PRODUCT_CUSTOM_CHANNEL_NORMAL); //普通定制
                        o.setProductId(p.getProductId());
                        List<Odtype> odtypesList = odtypeService.selectByCondition(o);
                        if (CollectionUtils.isNotEmpty(odtypesList)) {
                            Odtype odtype = odtypesList.get(0);
                            odtype.setIsUsed(p.getIsNormalUsed());
                            odtypeService.updateByPrimaryKeySelective(iRequest, odtype);
                        }
                    }
                    if (p.getIsSuperUsed() != null && p.getIsSuperUsed().equals(codeValue.getMeaning())) {
                        p.setIsSuperUsed(codeValue.getValue());
                        Odtype o = new Odtype();
                        o.setCustChanSrc(Constants.PRODUCT_CUSTOM_CHANNEL_SUPER); //超级定制
                        o.setProductId(p.getProductId());
                        List<Odtype> odtypesList = odtypeService.selectByCondition(o);
                        if (CollectionUtils.isNotEmpty(odtypesList)) {
                            Odtype odtype = odtypesList.get(0);
                            odtype.setIsUsed(p.getIsSuperUsed());
                            odtypeService.updateByPrimaryKeySelective(iRequest, odtype);
                        }
                    }
                }

            }
        }

        return result;
    }

    @Override
    public Product selectUniqueByVCode(String vCode, Long catalogversionId) {
        Product product = new Product();
        product.setvProductCode(vCode);
        product.setCatalogversionId(catalogversionId);
        try {
            return productMapper.selectOne(product);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Product selectCustomByCode(String code, Long catalogversionId) {
        Example example = new Example(Product.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("code", code);
        criteria.andEqualTo("catalogversionId", catalogversionId);
        criteria.andNotEqualTo("customType", Constants.PRODUCT_CUSTOM_TYPE_REGULAR);
        List<Product> productList = productMapper.selectByExample(example);
        return CollectionUtils.isEmpty(productList) ? null : productList.get(0);
    }

    @Override
    public List<Product> selectProductWithMediaUpdated(Long catalogversionId, int limit) {
        PageHelper.startPage(1, limit);
        List<Product> productList = productMapper.selectProductWithMediaUpdated(catalogversionId);
        return productList;
    }

    /**
     * 推送商品基础关联关系至ZMALL
     * Author: zhangzilong
     *
     * @param dtos
     * @return 接口返回信息
     */
    @Override
    public Map sendProductRelationToZmall(List<ProductDto> dtos) throws Exception {
        dtos = productMapper.queryProductRelationForZmall(dtos);
        if (dtos.size() > 0) {
            StringWriter stringWriter = new StringWriter();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.writeValue(stringWriter, dtos);

            Response response = restClient.postString(Constants.ZMALL, "/zmallsync/variantprd?token=" + Auth.md5(SecretKey.KEY + stringWriter.toString()),
                    stringWriter.toString(), "application/json", null, null);
            Map map = objectMapper.readValue(response.body().string(), Map.class);
            map.put("dataJson", stringWriter.toString());
            return map;
        } else {
            Map map = new HashMap();
            map.put("code", "S");
            map.put("message", "没有可发送的商品关联关系");
            return map;
        }
    }

    @Override
    public List<Product> selectProductByCode(String code) {
        return productMapper.selectProductByCode(code);
    }

    @Override
    public Product selectUniqueByCode(String code, Long catalogversionId) {
        Product product = new Product();
        product.setCode(code);
        product.setCatalogversionId(catalogversionId);
        return productMapper.selectOne(product);
    }

    @Override
    public List<Product> selectProductForFetchMedia() {
        return productMapper.selectProductForFetchMedia();
    }

    @Override
    public List<Product> selectProByCode(String code) {
        return productMapper.selectProByCode(code);
    }

    @Override
    public List<AtpProductInvInfoExport> selectIsSinSaleCode() {
        return productMapper.selectIsSinSaleCode();
    }

    /**
     * 查询可用库存
     * 1.根据筛选出的商品编码，在商品表中找到其商品主键（PRODUCT_ID）
     * 2.该商品主键在套件表中（HMALL_MST_SUITLINE_MAPPING）存储字段为套件产品（PRODUCT_HEAD_ID），查询库存表中是否有这些套件商品 如果有一件没有则库存数量为0
     * 3.通过该套件产品找到与该套件产品相关联的所有组件产品的主键（COMPONENT_ID）
     * 4.以上找出的套件产品（PRODUCT_HEAD_ID）以及组件产品（COMPONENT_ID）在库存表中（HAP_ATP_PRODUCT_INV_INFO）的存储字段为物料编码（MATNR）
     * 5.通过物料编码得到所有物料对应的可用库存，这里的可用库存为所有库存地点（INVENTORY_LOCATIONS）的可用库存（AVAILABLE_QUANTITY）总和
     * 6.将上面找出的所有物料编码的可用库存进行比较，取最小值作为最终导出的可用库存
     *
     * @return
     */
    @Override
    public List<AtpProductInvInfoExport> selectProcuctInvInfo() {
        //查询可单独销售的商品code
        List<AtpProductInvInfoExport> isSinSaleCodeList = productMapper.selectIsSinSaleCode();
        if (CollectionUtils.isNotEmpty(isSinSaleCodeList)) {
            //查询可单独销售的商品在套件表中的套件商品
            for (int i = 0; i < isSinSaleCodeList.size(); i++) {
                //若为N，直接在库存表中查找其库存数量
                if ("N".equals(isSinSaleCodeList.get(i).getIsSuit())) {
                    //根据物料编码查询库存
                    List<AtpProductStock> atpProductReservedVList = iAtpProductStockService.queryAvailableStockList(isSinSaleCodeList.get(i).getMatnr(), null);

                    if (CollectionUtils.isNotEmpty(atpProductReservedVList)) {
                        Double quantity = 0d;
                        //将库存累加
                        for (AtpProductStock atpProductStock : atpProductReservedVList) {
                            quantity = quantity + atpProductStock.getAvailableQuantity();
                        }
                        isSinSaleCodeList.get(i).setQuantity(quantity.intValue());
                    } else {
                        isSinSaleCodeList.get(i).setQuantity(0);
                    }
                } else {
                    boolean productHeadIdFlag = true;
                    List<AtpProductInvInfoExport> productHeadIdList = productMapper.selectInfoByProductHeadId(isSinSaleCodeList.get(i));
                    //如果满足条件，说明该商品不是套件
                    if (productHeadIdList.size() == 1 && productHeadIdList.get(0) == null) {
                        productHeadIdList = new ArrayList<>();
                        List<AtpProductInvInfoExport> result = productMapper.selectProcuctInvInfoByMatnr(isSinSaleCodeList.get(i));
                        if (CollectionUtils.isNotEmpty(result)) {
                            Double totalNum = new Double(0);
                            for (AtpProductInvInfoExport o : result) {
                                totalNum = totalNum + o.getAvailableQuantity();
                            }
                            isSinSaleCodeList.get(i).setAvailableQuantity(totalNum);
                        } else {
                            isSinSaleCodeList.get(i).setAvailableQuantity(0.0);
                        }
                    }
//                productHeadIdList.add(isSinSaleCodeList.get(i));
                    //对于非套件产品，可直接取其库存，套件产品套箭头无库存，其库存应该取其组件产品最小值
                    if ("Y".equals(isSinSaleCodeList.get(i).getIsSuit())) {
                        productHeadIdList = null;
                    }
                    //查询库存表中是否有商品库存
                    if (CollectionUtils.isNotEmpty(productHeadIdList)) {
                        for (int j = 0; j < productHeadIdList.size(); j++) {
                            if (CollectionUtils.isEmpty(productMapper.selectProcuctInvInfoByMatnr(productHeadIdList.get(j)))) {
                                productHeadIdFlag = false;
                                break;
                            }
                        }
                    }
                    //如果套件商品在库存表中不存在则将库存改为0
                    if (!productHeadIdFlag) {
                        isSinSaleCodeList.get(i).setAvailableQuantity(0.0);
                    }
                    //查询库存取最小值
                    if (isSinSaleCodeList.get(i).getAvailableQuantity() == null) {
                        AtpProductInvInfoExport condition = new AtpProductInvInfoExport();
                        condition.setProductId(isSinSaleCodeList.get(i).getProductId());
                        List<AtpProductInvInfoExport> minList = productMapper.selectProcuctInvInfo(condition);
                        if (CollectionUtils.isNotEmpty(minList)) {
                            Long productHeadId = Long.parseLong(isSinSaleCodeList.get(i).getProductId());
                            List<SuitlineMapping> num = suitlineMappingService.getCountByProductHeadId(productHeadId);
                            if (num.size() > minList.size()) {
                                isSinSaleCodeList.get(i).setAvailableQuantity(0.0);
                            } else
                                isSinSaleCodeList.get(i).setAvailableQuantity(minList.get(0).getAvailableQuantity());
                        } else {
                            isSinSaleCodeList.get(i).setAvailableQuantity(0.0);
                        }
                    }
                    //数值转换
                    if (isSinSaleCodeList.get(i).getAvailableQuantity() != null) {
                        isSinSaleCodeList.get(i).setQuantity(isSinSaleCodeList.get(i).getAvailableQuantity().intValue());
                    }
                }
            }
        }
        return isSinSaleCodeList;
    }

    @Override
    public Long selectIdByCodeAndVersion(Product product) {
        return productMapper.selectIdByCodeAndVersion(product);
    }

    @Override
    public Product selectProductByCodeAndVersion(Product product) {
        return productMapper.selectProductByCodeAndVersion(product);
    }
}
