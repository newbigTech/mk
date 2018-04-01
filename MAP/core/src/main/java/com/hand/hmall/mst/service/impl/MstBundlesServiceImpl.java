package com.hand.hmall.mst.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hand.common.util.Auth;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.ExcelUtil;
import com.hand.hap.util.FormatUtils;
import com.hand.hmall.mst.dto.MstBundles;
import com.hand.hmall.mst.dto.MstBundlesMapping;
import com.hand.hmall.mst.dto.Product;
import com.hand.hmall.mst.mapper.MstBundlesMapper;
import com.hand.hmall.mst.mapper.MstBundlesMappingMapper;
import com.hand.hmall.mst.mapper.ProductMapper;
import com.hand.hmall.mst.service.IMstBundlesMappingService;
import com.hand.hmall.mst.service.IMstBundlesService;
import com.hand.hmall.util.StringUtils;
import org.apache.ibatis.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @descrption 商品套装service
 * Created by heng.zhang04@hand-china.com
 * 2017/8/30
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MstBundlesServiceImpl extends BaseServiceImpl<MstBundles> implements IMstBundlesService {
    @Autowired
    private MstBundlesMappingMapper mstBundlesMappingMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private MstBundlesMapper mstBundlesMapper;
    @Autowired
    private IMstBundlesMappingService bundlesMappingService;
    @Autowired
    private RestTemplate restTemplate;
    @Value("#{configProperties['baseUri']}")
    private String baseUri;

    @Value("#{configProperties['modelUri']}")
    private String modelUri;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public ArrayList<MstBundles> selectBundlesBySyncFlag(MstBundles mstBundles) {
        ArrayList<MstBundles> mstBundlesList = mstBundlesMapper.selectBundlesBySyncFlag(mstBundles);
        if (!mstBundlesList.isEmpty()) {
            return mstBundlesList;
        }
        return null;
    }

    /**
     * 查找计算商品套装数据
     *
     * @param requestContext
     * @param dto
     * @return
     */
    @Override
    public List<MstBundles> selectMappingData(IRequest requestContext, MstBundles dto) {
        double originalPrice = 0.0;//原价
        double discountRate = 0.0;//折扣率
        List<MstBundles> mstBundlesList = mstBundlesMapper.select(dto);
        double fixedPrice = (mstBundlesList.get(0).getPrice()).doubleValue();//固定价格
        MstBundlesMapping mstBundlesMapping = new MstBundlesMapping();
        mstBundlesMapping.setBundlesId(dto.getBundlesId());
        List<MstBundlesMapping> mstBundlesMappingList = mstBundlesMappingMapper.selectProduct(mstBundlesMapping);

        List<MstBundlesMapping> mstBundlesMappingList2;
        for (MstBundlesMapping mstBundlesMapp : mstBundlesMappingList) {
            //获取价格
            mstBundlesMappingList2 = mstBundlesMappingMapper.queryPrice(mstBundlesMapp);
            if (mstBundlesMappingList2 != null && mstBundlesMappingList2.size() != 0) {
                originalPrice += mstBundlesMappingList2.get(0).getProductPrice() * mstBundlesMapp.getQuantity();
            }
        }
        if (originalPrice != 0.0) {
            BigDecimal fixedPricDec = new BigDecimal(Double.toString(fixedPrice));
            BigDecimal originalPriceDec = new BigDecimal(Double.toString(originalPrice));
            //discountRate = fixedPricDec.divide(originalPriceDec).doubleValue();
            discountRate = fixedPricDec.divide(originalPriceDec, 2, BigDecimal.ROUND_UP).doubleValue();
        }
        mstBundlesList.get(0).setOriginalPrice(originalPrice);
        mstBundlesList.get(0).setDiscountRate(discountRate);
        mstBundlesList.get(0).setFixedPrice(fixedPrice);
        return mstBundlesList;
    }

    /**
     * excel数据导入
     *
     * @param requestContext
     * @param excelList
     */
    @Override
    public void insertAllValue(IRequest requestContext, ArrayList<List<String>> excelList) throws Exception {
        ExcelUtil excelUtil = new ExcelUtil();
        FormatUtils formatUtils = new FormatUtils();
        StringBuffer errMsg = new StringBuffer("");

        int n;
        long bundlesId;
        List<String> data;

        List<JSONObject> successBundles = new ArrayList<>();
        for (int i = 0; i < excelList.size(); i++) {
            String __status = "add";
            n = i + 2;
            data = excelList.get(i);
            if (excelUtil.chekckNullCol(data)) {
                continue;
            }
            /*获取数据*/

            String code = (data.get(0)).trim();//套件编码
            String name = (data.get(1)).trim();//套件名称
            String description = (data.get(2)).trim();//套件描述
            String priority = (data.get(3)).trim();//套件优先级
            String isOverlay = (data.get(4)).trim();//是否叠加
            String price = (data.get(5)).trim();//套件价格
            String bundlesCodes = (data.get(6)).trim();//套件组成
            String productCount = (data.get(7)).trim();//套件商品数量
            String status = (data.get(8)).trim();//状态
            MstBundles mstBundles = new MstBundles();
            if (code != null && !"".equals(code)) {
                mstBundles.setCode(code);
                //查找数据库中套件编码是否存在
                List<MstBundles> MstBundlesMapperList = mstBundlesMapper.select(mstBundles);
                if (MstBundlesMapperList != null && MstBundlesMapperList.size() != 0) {
                    __status = "update";
                    bundlesId = MstBundlesMapperList.get(0).getBundlesId();
                    mstBundles.setBundlesId(bundlesId);
                }
            } else {
                errMsg.append("第" + n + "行，套件编码为空;");
            }
            mstBundles.setName(name);
            mstBundles.setDescription(description);
            if (!StringUtils.isEmpty(priority)) {
                if (formatUtils.isNum(priority.trim())) {
                    Integer bPriority = Integer.valueOf(priority);
                    mstBundles.setPriority(bPriority.intValue());
                } else {
                    errMsg.append("第" + n + "行，套件优先级格式不对;");
                }
            }

            if (price != null && !"".equals(price.trim())) {
                //价格转换
                if (formatUtils.isNum(price)) {
                    BigDecimal bPrice = BigDecimal.valueOf(Double.parseDouble(price));
                    mstBundles.setPrice(bPrice);
                } else {
                    errMsg.append("第" + n + "行，套件价格格式不对;");
                }

            } else {
                errMsg.append("第" + n + "行，价格为空;");
            }

            if (status != null && !"".equals(status)) {
                mstBundles.setStatus(status);
            } else {
                errMsg.append("第" + n + "行，状态为空;");
            }
            if (StringUtils.isEmpty(isOverlay)) {
                mstBundles.setIsOverlay("Y");
            } else {
                mstBundles.setIsOverlay(isOverlay);
            }
            if (StringUtils.isEmpty(bundlesCodes)) {
                errMsg.append("第" + n + "行，套件组合为空;");
            }

            //校验商品编码、数量是否对应
            if (StringUtils.isEmpty(productCount)) {
                errMsg.append("第" + n + "行，套件商品数量为空;");
            } else if (productCount.split(";").length != bundlesCodes.split(";").length) {
                errMsg.append("第" + n + "行，套件商品编码与商品数量不是一一对应;");
            }

            if (bundlesCodes != null && !"".equals(bundlesCodes)) {

                if (errMsg.length() == 0) {
                    JSONObject bundles = insertData(mstBundles, __status, bundlesCodes, productCount, errMsg, n);
                    successBundles.add(bundles);
                } else {
                    continue;
                }


            }

        }

    }

    /**
     * 将状态更新为停用，并将数据同步到远端接口
     *
     * @param mstBundlesList
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)//保存 异常时回滚
    public void batchEndUsing(List<MstBundles> mstBundlesList) throws Exception {
        StringBuffer errB = new StringBuffer("");
        for (MstBundles mstBundles : mstBundlesList) {
            if (!StringUtils.isEmpty(mstBundles.getPromotionCode())) {
                try {
                    StringBuffer url = new StringBuffer("/hmall-drools-service/h/sale/bundle/endUsing");
                    HttpEntity<List<String>> entity = new HttpEntity<>(Arrays.asList(mstBundles.getPromotionCode()), null);
                    com.hand.hmall.dto.ResponseData bundleActivityResp = restTemplate.exchange(baseUri + url.toString(), HttpMethod.POST, entity, com.hand.hmall.dto.ResponseData.class).getBody();
                    if (!bundleActivityResp.isSuccess()) {
                        logger.info("编码为：" + mstBundles.getCode() + "停用失败；");
                        errB.append("编码为：" + mstBundles.getCode() + "停用失败；");
                    } else {
                        try {
                            Map respMap = (Map) bundleActivityResp.getResp().get(0);
                            String promotionCode = (String) respMap.get("promotionCode");
                            mstBundles.setPromotionCode(promotionCode);
                            mstBundles.setSyncFlag("N");
                            mstBundlesMapper.updateByPrimaryKeySelective(mstBundles);
                        } catch (Exception e) {
                            errB.append("更新数据失败：" + e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    errB.append("调用接口有误，请联系管理员：" + e.getMessage());
                }


            } else {

                errB.append("关联促销编码为空,请校验数据完整性，编码：" + mstBundles.getCode());
            }
        }
        if (errB.length() > 0) {
            throw new Exception(errB.toString());
        }

    }

    /**
     * 将状态更新为启用，并将数据同步到远端接口
     *
     * @param dto
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)//保存 异常时回滚
    public void batchstartUsing(List<MstBundles> dto) throws Exception {
        StringBuffer errB = new StringBuffer("");
        for (MstBundles mstBundles : dto) {

            if (!StringUtils.isEmpty(mstBundles.getPromotionCode())) {
                try {
                    StringBuffer url = new StringBuffer("/hmall-drools-service/h/sale/bundle/startUsing");
                    HttpEntity<List<String>> entity = new HttpEntity<>(Arrays.asList(mstBundles.getPromotionCode()), null);
                    com.hand.hmall.dto.ResponseData bundleActivityResp = restTemplate.exchange(baseUri + url.toString(), HttpMethod.POST, entity, com.hand.hmall.dto.ResponseData.class).getBody();
                    if (!bundleActivityResp.isSuccess()) {
                        logger.info("启用失败");
                        errB.append("编码为：" + mstBundles.getCode() + "启用失败；");
                    } else {
                        try {
                            mstBundles.setSyncFlag("N");
                            mstBundlesMapper.updateByPrimaryKeySelective(mstBundles);
                        } catch (Exception e) {
                            errB.append("更新数据失败：" + e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    errB.append("调用接口有误，请联系管理员：" + e.getMessage());
                }

            } else {
                errB.append("关联促销编码为空,请校验数据完整性，编码：" + mstBundles.getCode());
            }
        }
        if (errB.length() > 0) {
            throw new Exception(errB.toString());
        }
    }

    /**
     * 对数据的增改操作
     *
     * @param dto
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)//保存 异常时回滚
    public List<MstBundles> batchUpdateData(List<MstBundles> dto) throws Exception {
        List<MstBundles> MstBundlesList = new ArrayList<>();
        StringBuffer errB = new StringBuffer("");
        String url = "/hmall-drools-service/h/sale/bundle/saveBundle";
        for (MstBundles mstBundles : dto) {
            if (mstBundles.getBundlesId() != null && !StringUtils.isEmpty(mstBundles.getPromotionCode())) {
                MstBundlesMapping bundlesMapping = new MstBundlesMapping();
                bundlesMapping.setBundlesId(mstBundles.getBundlesId());
                ArrayList<MstBundlesMapping> mstBundlesMappings = bundlesMappingService.selectBundlesMappingByBundlesId(bundlesMapping);
                JSONObject bundleObj = JSON.parseObject(JSON.toJSONString(mstBundles));
                JSONArray bundleMapping = JSONArray.parseArray(JSON.toJSONString(mstBundlesMappings));
                bundleObj.put("bundlesMappings", bundleMapping);
                HttpEntity<Map<String, Object>> entity = new HttpEntity<>(bundleObj, null);
                try {
                    com.hand.hmall.dto.ResponseData bundleActivityResp = restTemplate.exchange(baseUri + url, HttpMethod.POST, entity, com.hand.hmall.dto.ResponseData.class).getBody();
                    if (bundleActivityResp.isSuccess()) {

                        Map respMap = (Map) bundleActivityResp.getResp().get(0);
                        String promotionCode = (String) respMap.get("promotionCode");
                        mstBundles.setSyncFlag("N");
                        mstBundles.setPromotionCode(promotionCode);
                        mstBundlesMapper.updateByPrimaryKeySelective(mstBundles);
                        MstBundlesList.add(mstBundles);
                    } else {
                        errB.append("套装规则修改异常,对应编码:" + mstBundles.getCode());
                    }
                } catch (Exception e) {
                    errB.append("调用修改接口有误，请联系管理员：" + e.getMessage());
                }

            } else {
                mstBundles.setSyncFlag("N");
                List<MstBundles> mstBundlesL = this.batchUpdate(RequestHelper.newEmptyRequest(), Arrays.asList(mstBundles));
                MstBundlesList.addAll(mstBundlesL);
            }
        }
        if (errB.length() > 0) {
            throw new Exception(errB.toString());
        }
        return MstBundlesList;
    }

    /**
     * 批量删除数据并同步到接口
     *
     * @param dto
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)//保存 异常时回滚
    public void batchDeleteData(List<MstBundles> dto) throws Exception {
        StringBuffer errB = new StringBuffer("");
        StringBuffer url = new StringBuffer("/hmall-drools-service/h/sale/bundle/delete");
        for (MstBundles bundles : dto) {
            MstBundles bundlesFromDB = this.selectByPrimaryKey(RequestHelper.newEmptyRequest(), bundles);
            StringBuffer promotionCode = new StringBuffer();
            if (bundlesFromDB != null && !StringUtils.isEmpty(bundlesFromDB.getPromotionCode())) {
                //如果关联编码为空，则
                promotionCode.append(bundlesFromDB.getPromotionCode());
            } else {
                try {
                    this.batchDeleteBundles(bundles);
                } catch (Exception e) {
                    errB.append("删除" + bundles.getBundlesId() + "编码：" + bundles.getCode() + "套装规则行失败," + e.getMessage());
                }
                continue;
            }
            HttpEntity<List<String>> entity = new HttpEntity<>(Arrays.asList(promotionCode.toString()), null);
            try {
                com.hand.hmall.dto.ResponseData bundleActivityResp = restTemplate.exchange(baseUri + url.toString(), HttpMethod.POST, entity, com.hand.hmall.dto.ResponseData.class).getBody();
                if (!bundleActivityResp.isSuccess()) {
                    logger.info("删除失败" + bundleActivityResp.getMsg());
                    errB.append("删除" + bundles.getBundlesId() + "编码：" + bundles.getCode() + "套装规则失败," + bundleActivityResp.getMsg());
                } else {
                    try {
                        this.batchDeleteBundles(bundles);
                    } catch (Exception e) {
                        errB.append("删除" + bundles.getBundlesId() + "编码：" + bundles.getCode() + "套装规则行失败," + bundleActivityResp.getMsg());
                    }

                }
            } catch (Exception e) {
                errB.append("调用删除接口失败，对应编码：" + bundles.getCode() + e.getMessage());
            }

        }
        if (errB.length() > 0) {
            throw new Exception(errB.toString());
        }
    }

    /**
     * 删除头行信息
     *
     * @param bundles
     */
    private void batchDeleteBundles(MstBundles bundles) throws Exception {
        MstBundlesMapping mBundlesmapping = new MstBundlesMapping();
        mBundlesmapping.setBundlesId(bundles.getBundlesId());
       /* 删除对应行*/
        mstBundlesMappingMapper.delete(mBundlesmapping);
        mstBundlesMapper.delete(bundles);

    }

    /**
     * 向数据库插值
     *
     * @param mstBundles
     * @param status
     * @param bundlesCodes
     */
    @Transactional(rollbackFor = Exception.class)//保存 异常时回滚
    private JSONObject insertData(MstBundles mstBundles, String status, String bundlesCodes, String bundlesQuantity, StringBuffer errMsg, int n) {
        long bundlesId;
        List<MstBundlesMapping> MstBundlesMappingList = new ArrayList<>();
        MstBundlesMapping mstBundlesMapping = new MstBundlesMapping();
        if ("add".equals(status)) {
            mstBundlesMapper.insertSelective(mstBundles);
            bundlesId = mstBundles.getBundlesId();
        } else {
            bundlesId = mstBundles.getBundlesId();
            mstBundlesMapper.updateByPrimaryKeySelective(mstBundles);
            //清空商品套装产品映射表 中对应数据
            mstBundlesMapping.setBundlesId(bundlesId);
            mstBundlesMappingMapper.delete(mstBundlesMapping);
        }
        mstBundlesMapping.setBundlesId(bundlesId);
        //解析套件组成
        String[] codeStr = bundlesCodes.split(";");//获取BOM节点ID|选配项物料编码
        String[] quantityStr = bundlesQuantity.split(";");//获取BOM节点ID|选配项物料编码数量
        Product product = new Product();
        for (int i = 0; i < codeStr.length; i++) {
            List<Product> productList = productMapper.selectProductByCode(codeStr[i]);
            if (productList != null && productList.size() != 0) {
                product = productList.get(0);
                mstBundlesMapping.setProductId(product.getProductId());
                mstBundlesMapping.setQuantity(Long.parseLong(quantityStr[i]));
                try {
                    mstBundlesMappingMapper.insertSelective(mstBundlesMapping);
                } catch (Exception e) {
                    errMsg.append("第" + n + "行插入商品套装产品映射表出错，对应套件编码：" + mstBundles.getCode() + ";套件组成：" + bundlesCodes);
                }

            } else {
                errMsg.append("第" + n + "行,找不到对应商品，编码：" + codeStr[i]);
            }
        }
        ArrayList<MstBundlesMapping> mstBundlesMappings = bundlesMappingService.selectBundlesMappingByBundlesId(mstBundlesMapping);
        JSONObject bundleObj = JSON.parseObject(JSON.toJSONString(mstBundles));
        JSONArray bundleMapping = JSONArray.parseArray(JSON.toJSONString(mstBundlesMappings));
        bundleObj.put("bundlesMappings", bundleMapping);

        MstBundles insertedBundle = new MstBundles();
        insertedBundle.setBundlesId(Long.parseLong(bundleObj.get("bundlesId").toString()));
        insertedBundle = mstBundlesMapper.selectByPrimaryKey(insertedBundle);
        String promotionCode = insertedBundle.getPromotionCode();
        if (!StringUtils.isEmpty(promotionCode)) {
            bundleObj.put("promotionCode", promotionCode);
        }
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(bundleObj, null);
        String url = "/hmall-drools-service/h/sale/bundle/saveBundle";
        com.hand.hmall.dto.ResponseData bundleActivityResp = restTemplate.exchange(baseUri + url, HttpMethod.POST, entity, com.hand.hmall.dto.ResponseData.class).getBody();
        if (bundleActivityResp.isSuccess()) {

            Map respMap = (Map) bundleActivityResp.getResp().get(0);
            promotionCode = (String) respMap.get("promotionCode");
            mstBundles.setPromotionCode(promotionCode);
            mstBundles.setSyncFlag("N");
            mstBundlesMapper.updateByPrimaryKeySelective(mstBundles);
        } else {
            errMsg.append("导入生成促销规则时异常\"+bundleActivityResp.getMsg()+\"，套装编码：" + bundleObj.get("code"));
            throw new RuntimeException("导入生成促销规则时异常" + bundleActivityResp.getMsg() + "，套装编码：" + bundleObj.get("code"));
        }
        return bundleObj;
    }
}