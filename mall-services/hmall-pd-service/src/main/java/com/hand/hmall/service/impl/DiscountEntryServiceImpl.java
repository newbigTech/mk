package com.hand.hmall.service.impl;

import com.hand.hmall.mapper.DiscountEntryMapper;
import com.hand.hmall.model.DiscountEntry;
import com.hand.hmall.model.Product;
import com.hand.hmall.model.VCodeHeader;
import com.hand.hmall.pojo.PriceRequestData;
import com.hand.hmall.service.IDiscountEntryService;
import com.hand.hmall.service.IProductService;
import com.hand.hmall.service.IVCodeHeaderService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author zhangmeng
 * @version 0.1
 * @name DiscountEntryServiceImpl
 * @description 价格折扣行
 * @date 2017/11/27
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DiscountEntryServiceImpl extends BaseServiceImpl<DiscountEntry> implements IDiscountEntryService {

    @Autowired
    private DiscountEntryMapper discountEntryMapper;

    @Autowired
    private IVCodeHeaderService ivCodeHeaderService;

    @Autowired
    private IProductService iProductService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 计算折扣价
     *
     * @param priceRequestData
     * @return
     */
    @Override
    public DiscountEntry discountPrice(PriceRequestData priceRequestData, Double totalPrice, String flag) {

        DiscountEntry discountEntry = new DiscountEntry();
        Product product = null;

        Example example = new Example(DiscountEntry.class);
        Example.Criteria criteria = example.createCriteria();
        criteria = condition(criteria);

        //查询折扣行时是否使用V码标志位
        boolean vCodeFlag = false;

        //使用V码查询
        if ("1".equals(flag)) {
            vCodeFlag = true;
            //使用V码查询V码头表
            VCodeHeader vCodeHeader = ivCodeHeaderService.selectOneByVCode(priceRequestData.getvCode());
            //判断该V码是否为主推款 typeCode为Z或者ZT为主推款
            if ("Z".equals(vCodeHeader.getTypeCode()) || "ZT".equals(vCodeHeader.getTypeCode())) {
                //取对应ZTMATNR，作为HMALL_MST_PRODUCT表中CODE，匹配商品表中PRODUCT_ID，用于折扣行计算
                product = iProductService.selectMarkorOnlineByCode(vCodeHeader.getZtmatnr());
                vCodeFlag = false;
            }
            //当TYPE_CODE≠Z/ZT，则判定为定制品，先用VCODE，匹配价格折扣行HMALL_OM_DISCOUNT_ENTRY中VCODE是否存在该V码
            else {
                //v码
                criteria.andEqualTo("vcode", priceRequestData.getvCode());
                //查询价格折扣行
                List<DiscountEntry> vcodeList = discountEntryMapper.selectByExample(example);
                //v码存在则直接使用该VCODE的促销价
                if (CollectionUtils.isNotEmpty(vcodeList)) {
                    product = new Product();
                    product.setProductId(vcodeList.get(0).getProductId());
                }
                //使用平台号查询商品
                else {
                    vCodeFlag = false;
                    product = iProductService.selectMarkorOnlineByCode(vCodeHeader.getPlatformCode());
                }
            }
        }
        //直接用商品CODE计算
        else if ("2".equals(flag)) {
            //使用平台号查询商品
            product = iProductService.selectMarkorOnlineByCode(priceRequestData.getProductCode());
        }

        if (product != null) {
            Example productExample = new Example(DiscountEntry.class);
            Example.Criteria productCriteria = productExample.createCriteria();
            productCriteria = condition(productCriteria);
            //商品ID
            productCriteria.andEqualTo("productId", product.getProductId());

            if (vCodeFlag) {
                //V码
                productCriteria.andEqualTo("vcode", priceRequestData.getvCode());
            } else {
                productCriteria.andIsNull("vcode");
            }

            //查询价格折扣行
            List<DiscountEntry> discountEntryList = discountEntryMapper.selectByExample(productExample);
            if (CollectionUtils.isNotEmpty(discountEntryList) && discountEntryList.size() == 1) {
                //同一时间段内只有一条折扣行
                discountEntry = calculationDiscountPrice(discountEntryList.get(0), totalPrice);
            } else {
                //如果没有折扣行返回原价
                discountEntry.setDiscountPrice(totalPrice);
                discountEntry.setType("base");
            }
        } else {
            //如果没有折扣行返回原价
            discountEntry.setDiscountPrice(totalPrice);
            discountEntry.setType("base");
        }
        return discountEntry;
    }

    /**
     * 创建查询条件
     *
     * @param criteria
     * @return
     */
    private Example.Criteria condition(Example.Criteria criteria) {
        // 有效时间
        Date current = new Date();
        //大于开始时间
        criteria.andLessThanOrEqualTo("startTime", current);
        //小于结束时间
        criteria.andGreaterThanOrEqualTo("endTime", current);
        //启用标志位
        criteria.andEqualTo("isHandle", "Y");
        //online版本
        criteria.andEqualTo("catalogversionId", 10022);

        return criteria;
    }

    /**
     * 计算折扣价格
     *
     * @param discountEntry
     * @param totalPrice
     */
    private DiscountEntry calculationDiscountPrice(DiscountEntry discountEntry, Double totalPrice) {
        //判断折扣类型
        //1-平台直降：最终价格=原价格计算逻辑计算的价格 - 折扣（偏移量）；
        if (1 == discountEntry.getDiscountType()) {
            discountEntry.setDiscountPrice(BigDecimal.valueOf(totalPrice).subtract(BigDecimal.valueOf(discountEntry.getDiscount())).doubleValue());
            discountEntry.setType("promotion");
        }
        // 2-平台折扣：最终价格=原价格计算逻辑计算的价格 * 折扣（偏移量） ；
        else if (2 == discountEntry.getDiscountType()) {
            discountEntry.setDiscountPrice(BigDecimal.valueOf(totalPrice).multiply(BigDecimal.valueOf(discountEntry.getDiscount())).doubleValue());
            discountEntry.setType("promotion");
        }
        //3-V码固定价格；最终价格=V码固定价格；
        else if (3 == discountEntry.getDiscountType()) {
            discountEntry.setDiscountPrice(discountEntry.getDiscount());
            discountEntry.setType("special");
        }
        //折扣执行时间
        discountEntry.setStartTimeStr(sdf.format(discountEntry.getStartTime()));
        discountEntry.setEndTimeStr(sdf.format(discountEntry.getEndTime()));
        return discountEntry;
    }
}