package com.hand.hmall.service.impl;

import com.hand.hmall.common.Constants;
import com.hand.hmall.common.ResponseType;
import com.hand.hmall.model.DiscountEntry;
import com.hand.hmall.model.Product;
import com.hand.hmall.model.VCodeHeader;
import com.hand.hmall.pojo.PriceRequest;
import com.hand.hmall.pojo.PriceRequestData;
import com.hand.hmall.pojo.PriceResponse;
import com.hand.hmall.pojo.PriceResponseRow;
import com.hand.hmall.service.*;
import com.hand.hmall.util.PriceFormule;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name PriceCalculateCXFServiceImpl
 * @description 价格计算CXF服务实现类
 * @date 2017/7/19 16:06
 */
@Service
public class PriceCalculateCXFServiceImpl implements IPriceCalculateCXFService {

    private static final Logger logger = LoggerFactory.getLogger(PriceCalculateCXFServiceImpl.class);

    @Autowired
    private IPriceCalculateService iPriceCalculateService;

    @Autowired
    private IVCodeHeaderService ivCodeHeaderService;

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IDiscountEntryService iDiscountEntryService;

    @Override
    @Deprecated
    public PriceResponse calculatePrice(PriceRequest priceRequest) {

        List<PriceRequestData> priceRequestDataList = new ArrayList<>();
        for (String vCode : priceRequest.getvCodeList()) {
            Product product = iProductService.selectMarkorOnlineByVCode(vCode);
            if (product == null) {
                return new PriceResponse(false, ResponseType.FAILURE.CODE, "v码[" + vCode + "]对应商品不存在");
            }
            String odtype = product.getCustomChannelSource();
            PriceRequestData priceRequestData = new PriceRequestData();
            priceRequestData.setOdtype(odtype);
            priceRequestData.setvCode(vCode);
            priceRequestDataList.add(priceRequestData);
        }

        if (Constants.PRICE_TYPE_ORDER_PRICE.equals(priceRequest.getPriceType())) {
            return calculateOrderPrice(priceRequestDataList);
        } else {
            return calculateSalePrice(priceRequestDataList);
        }
    }

    @Override
    public PriceResponse calculateOrderPrice(List<PriceRequestData> priceRequestDataList) {

        if (CollectionUtils.isEmpty(priceRequestDataList)) {
            return new PriceResponse(false, ResponseType.INVALID_PARAMETER.CODE, "传入数据不能为空");
        }
        List<PriceResponseRow> priceResponseRowList = new ArrayList<>();
        for (PriceRequestData priceRequestData : priceRequestDataList) {
            // 参数校验
            String error = validate(priceRequestData);
            if (StringUtils.isNotBlank(error)) {
                priceResponseRowList.add(new PriceResponseRow(false, error, priceRequestData));
                continue;
            }

            if (StringUtils.isNotBlank(priceRequestData.getvCode())) {

                // 检查v码是否存在
                VCodeHeader vCodeHeader = ivCodeHeaderService.selectOneByVCode(priceRequestData.getvCode());
                if (vCodeHeader == null) {
                    priceResponseRowList.add(new PriceResponseRow(false, "v码[" + priceRequestData.getvCode() + "]在v码头表中不存在", priceRequestData));
                    continue;
                }

                // 检查平台号是否存在
                Product product = iProductService.selectMarkorOnlineByCode(vCodeHeader.getPlatformCode());
                if (product == null) {
                    priceResponseRowList.add(new PriceResponseRow(false, "平台号[" + vCodeHeader.getPlatformCode() + "]不存在", priceRequestData));
                    continue;
                }

                if (Constants.YES.equals(product.getIsSuit())) {
                    // 检查配置器是否维护了平台号的套件关系
                    if (StringUtils.isBlank(vCodeHeader.getSegment())) {
                        priceResponseRowList.add(new PriceResponseRow(false, "平台号[" + vCodeHeader.getPlatformCode() + "]的套件组件不存在", priceRequestData));
                        continue;
                    }

                    // 按照@分割segment字段获得组件关系
                    String[] vCodeArr = vCodeHeader.getSegment().split(Constants.SEGMENT_SPLIT_CHAR);
                    Double totalPrice = 0D;
                    try {
                        for (String vCodeEle : vCodeArr) {
                            // 计算每个组件的价格并累加
                            Double suitLinePrice = iPriceCalculateService.calOrderPrice(vCodeEle, priceRequestData.getOdtype());
                            totalPrice += suitLinePrice;
                        }
                        priceResponseRowList.add(new PriceResponseRow(true, PriceFormule.getCalculateFormule(totalPrice, priceRequestData.getvCode()),
                                totalPrice, priceRequestData));
                    } catch (Exception e) {
                        priceResponseRowList.add(new PriceResponseRow(false, e.getMessage(), priceRequestData));
                    }
                } else {
                    // 如果不是套件，直接v码的价格
                    try {
                        Double totalPrice = iPriceCalculateService.calOrderPrice(priceRequestData.getvCode(), priceRequestData.getOdtype());
                        priceResponseRowList.add(new PriceResponseRow(true, PriceFormule.getCalculateFormule(totalPrice, priceRequestData.getvCode()),
                                totalPrice, priceRequestData));
                    } catch (Exception e) {
                        priceResponseRowList.add(new PriceResponseRow(false, e.getMessage(), priceRequestData));
                    }
                }
            } else {
                // 如果传入的商品编码，直接根据商品编码计算价格
                try {
                    Double totalPrice = iPriceCalculateService.calculatePriceByProductCode(priceRequestData.getProductCode(), Constants.ORDER_PRICE_VALUE);
                    priceResponseRowList.add(new PriceResponseRow(true, "采购价计算成功", totalPrice, priceRequestData));
                } catch (Exception e) {
                    priceResponseRowList.add(new PriceResponseRow(false, e.getMessage(), priceRequestData));
                }
            }
        }

        return new PriceResponse(true, ResponseType.SUCCESS.CODE, ResponseType.SUCCESS.MESSAGE, priceResponseRowList);
    }

    @Override
    public PriceResponse calculateSalePrice(List<PriceRequestData> priceRequestDataList) {
        if (CollectionUtils.isEmpty(priceRequestDataList)) {
            return new PriceResponse(false, ResponseType.INVALID_PARAMETER.CODE, "传入数据不能为空");
        }
        List<PriceResponseRow> priceResponseRowList = new ArrayList<>();
        for (PriceRequestData priceRequestData : priceRequestDataList) {
            // 参数校验
            String error = validate(priceRequestData);
            if (StringUtils.isNotBlank(error)) {
                priceResponseRowList.add(new PriceResponseRow(false, error, priceRequestData));
                continue;
            }

            if (StringUtils.isNotBlank(priceRequestData.getvCode())) {

                // 检查v码是否存在
                VCodeHeader vCodeHeader = ivCodeHeaderService.selectOneByVCode(priceRequestData.getvCode());
                if (vCodeHeader == null) {
                    priceResponseRowList.add(new PriceResponseRow(false, "v码[" + priceRequestData.getvCode() + "]在v码头表中不存在", priceRequestData));
                    continue;
                }

                // 检查平台号是否存在
                Product product = iProductService.selectMarkorOnlineByCode(vCodeHeader.getPlatformCode());
                if (product == null) {
                    priceResponseRowList.add(new PriceResponseRow(false, "平台号[" + vCodeHeader.getPlatformCode() + "]不存在", priceRequestData));
                    continue;
                }

                if (Constants.YES.equals(product.getIsSuit())) {
                    // 检查配置器是否维护了平台号的套件关系
                    if (StringUtils.isBlank(vCodeHeader.getSegment())) {
                        priceResponseRowList.add(new PriceResponseRow(false, "平台号[" + vCodeHeader.getPlatformCode() + "]的套件组件不存在", priceRequestData));
                        continue;
                    }

                    // 按照@分割segment字段获得组件关系
                    String[] vCodeArr = vCodeHeader.getSegment().split(Constants.SEGMENT_SPLIT_CHAR);
                    Double totalPrice = 0D;
                    try {
                        for (String vCodeEle : vCodeArr) {
                            // 计算每个组件的价格并累加
                            Double suitLinePrice = iPriceCalculateService.calSalePrice(vCodeEle, priceRequestData.getOdtype());
                            totalPrice += suitLinePrice;
                        }
                        //计算折扣行价格
                        DiscountEntry discountEntry = iDiscountEntryService.discountPrice(priceRequestData, totalPrice, "1");

                        priceResponseRowList.add(new PriceResponseRow(true, PriceFormule.getCalculateFormule(totalPrice, priceRequestData.getvCode()),
                                totalPrice, priceRequestData, discountEntry.getDiscountPrice(), discountEntry.getStartTimeStr(), discountEntry.getEndTimeStr(), discountEntry.getType()));
                    } catch (Exception e) {
                        priceResponseRowList.add(new PriceResponseRow(false, e.getMessage(), priceRequestData));
                    }
                } else {
                    // 如果不是套件，直接v码的价格
                    try {
                        Double totalPrice = iPriceCalculateService.calSalePrice(priceRequestData.getvCode(), priceRequestData.getOdtype());
                        //计算折扣行价格
                        DiscountEntry discountEntry = iDiscountEntryService.discountPrice(priceRequestData, totalPrice, "1");

                        priceResponseRowList.add(new PriceResponseRow(true, PriceFormule.getCalculateFormule(totalPrice, priceRequestData.getvCode()),
                                totalPrice, priceRequestData, discountEntry.getDiscountPrice(), discountEntry.getStartTimeStr(), discountEntry.getEndTimeStr(), discountEntry.getType()));
                    } catch (Exception e) {
                        priceResponseRowList.add(new PriceResponseRow(false, e.getMessage(), priceRequestData));
                    }
                }
            } else {
                // 如果传入的商品编码，直接根据商品编码计算价格
                try {
                    Double totalPrice = iPriceCalculateService.calculatePriceByProductCode(priceRequestData.getProductCode(), Constants.SALE_PRICE_VALUE);
                    //计算折扣行价格
                    DiscountEntry discountEntry = iDiscountEntryService.discountPrice(priceRequestData, totalPrice, "2");

                    priceResponseRowList.add(new PriceResponseRow(true, "销售价计算成功", totalPrice, priceRequestData, discountEntry.getDiscountPrice(), discountEntry.getStartTimeStr(), discountEntry.getEndTimeStr(), discountEntry.getType()));
                } catch (Exception e) {
                    priceResponseRowList.add(new PriceResponseRow(false, e.getMessage(), priceRequestData));
                }
            }
        }
        return new PriceResponse(true, ResponseType.SUCCESS.CODE, ResponseType.SUCCESS.MESSAGE, priceResponseRowList);
    }

    /**
     * 验证PriceRequestData是否合法
     *
     * @param priceRequestData 价格请求对象
     * @return 错误消息
     */
    private String validate(PriceRequestData priceRequestData) {
        String vCode = priceRequestData.getvCode();
        String productCode = priceRequestData.getProductCode();
        if (StringUtils.isEmpty(vCode) && StringUtils.isBlank(productCode)) {
            return "v码和商品编码至少一个不为空";
        }
        return "";
    }

    /**
     * 检查价格类型是否合法
     *
     * @param priceType 价格类型
     * @return boolean
     */
    private boolean checkPriceType(String priceType) {
        return Constants.PRICE_TYPE_SALE_PRICE.equals(priceType) || Constants.PRICE_TYPE_ORDER_PRICE.equals(priceType);
    }

    /**
     * 获取价格类型的具体值
     *
     * @param priceType 价格类型
     * @return String
     */
    private String getPriceTypeValue(String priceType) {
        return Constants.PRICE_TYPE_SALE_PRICE.equals(priceType) ? Constants.SALE_PRICE_VALUE : Constants.ORDER_PRICE_VALUE;
    }
}
