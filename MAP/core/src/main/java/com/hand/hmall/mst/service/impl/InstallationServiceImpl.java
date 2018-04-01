package com.hand.hmall.mst.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.CodeValue;
import com.hand.hap.system.service.ICodeService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.mst.dto.Catalogversion;
import com.hand.hmall.mst.dto.Installation;
import com.hand.hmall.mst.dto.ProductCategory;
import com.hand.hmall.mst.mapper.InstallationMapper;
import com.hand.hmall.mst.service.ICatalogversionService;
import com.hand.hmall.mst.service.IInstallationService;
import com.hand.hmall.mst.service.IProductCategoryService;
import com.hand.hmall.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 安装费对象的service实现类
 * @date 2017/7/10 14:37
 */
@Service
@Transactional
public class InstallationServiceImpl extends BaseServiceImpl<Installation> implements IInstallationService {

    @Autowired
    private InstallationMapper installationMapper;

    @Autowired
    private ICodeService codeService;

    @Autowired
    private ICatalogversionService catalogversionService;

    @Autowired
    private IProductCategoryService categoryService;

    /**
     * 安装费维护界面查询功能
     * @param request
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<Installation> selectInstallation(IRequest request, Installation dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return installationMapper.selectInstallation(dto);
    }

    /**
     * 安装费导入时，校验数据的正确性
     * @param request
     * @param list
     * @return
     */
    @Override
    public Map<String,Object> checkInstallation(IRequest request, List<Installation> list) {
        Map<String,Object> map = new HashMap<>();
        List<Installation> installationList = new ArrayList<>();
        String result = null;
        if (list != null && list.size() > 0) {
            for (Installation installation : list) {

                if (installation.getCategoryCode() == null) {
                    result = "类别编码不能为空！";
                    continue;
                }

                //验证类别编码是否存在
                ProductCategory productCategory = new ProductCategory();

                //查找online版本的版本catalogversionId
                Catalogversion catalogversion = new Catalogversion();
                catalogversion.setCatalogversion(Constants.CATALOG_VERSION_ONLINE);
                catalogversion.setCatalogName(Constants.CATALOG_VERSION_MARKOR);
                Long catalogversionId = catalogversionService.selectCatalogversionId(catalogversion);
                //通过版本目录的ID和类别编码来查找唯一的类别id
                productCategory.setCatalogVersion(catalogversionId);
                productCategory.setCategoryCode(installation.getCategoryCode().trim());
                Long categoryId = categoryService.selectByCodeAndVersion(productCategory);
                if (categoryId == null) {
                    result = "您输入的类别编码不存在！";
                    continue;
                } else {
                    installation.setCategoryId(categoryId);
                }

                //验证类别编码唯一性
                Installation installation1 = new Installation();
                installation1.setCategoryCode(installation.getCategoryCode());
                List<Installation> installations = installationMapper.selectInstallation(installation1);
                if (installations != null && installations.size() > 0) {
                    installation.setInstallationId(installations.get(0).getInstallationId());
                }
                installationList.add(installation);
            }
        }
        map.put("installationList",installationList);
        map.put("result",result);
        return map;
    }
}