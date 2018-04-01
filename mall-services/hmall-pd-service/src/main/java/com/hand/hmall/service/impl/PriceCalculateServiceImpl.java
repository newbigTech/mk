package com.hand.hmall.service.impl;

import com.hand.hmall.common.Constants;
import com.hand.hmall.model.*;
import com.hand.hmall.service.*;
import com.hand.hmall.util.PriceFormule;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 马君
 * @version 0.1
 * @name PriceCalculateServiceImpl
 * @description 价格计算服务实现类
 * @date 2017/7/5 11:40
 */
@Service
public class PriceCalculateServiceImpl implements IPriceCalculateService {
    private static final Logger logger = LoggerFactory.getLogger(PriceCalculateServiceImpl.class);
    private static final String SOFA_BODY_PACKAGE = "20627900"; // 沙发主体包
    private final DecimalFormat decimalFormat = new DecimalFormat("#.00");
    private static final String ORDER_PRICE_FORMAT = "%s(%s)[platformPrice[%s] + totalPartPrice[%s] + specialPrice[%s] + optPackPrice[%s]]";
    private static final String SALE_PRICE_FORMAT = "%s(%s)[platformPrice[%s] + totalPartPrice[%s] + optPackPrice[%s]]";

    @Autowired
    private IPricerowService iPricerowService;

    @Autowired
    private IFabricService iFabricService;

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IVCodeHeaderService ivCodeHeaderService;

    @Autowired
    private IVCodeLineService ivCodeLineService;

    @Autowired
    private IConrelService iConrelService;

    public final ThreadLocal<List<String>> calculatFormule = new ThreadLocal<>();

    @Override
    public Double getOptimalPrice(List<Pricerow> pricerowList) {
        if (CollectionUtils.isEmpty(pricerowList)) {
            return 0D;
        }
        Pricerow activityPricerow = pricerowList.stream()
                .filter(pricerow -> Constants.PRICE_GROUP_ACTIVITY_PRICE.equals(pricerow.getPriceGroup()))
                .sorted((pricerow1, pricerow2) -> pricerow2.getCreationDate().compareTo(pricerow1.getCreationDate()))
                .findFirst().orElse(null); // 获取活动价的价格行
        if (activityPricerow == null || activityPricerow.getBasePrice() == null) {
            // 如果没有活动价，则取原价
            Pricerow orignPricerow = pricerowList.stream()
                    .filter(pricerow -> Constants.PRICE_GROUP_ORIGN_PRICE.equals(pricerow.getPriceGroup()))
                    .sorted((pricerow1, pricerow2) -> pricerow2.getCreationDate().compareTo(pricerow1.getCreationDate()))
                    .findFirst().orElse(null);
            if (orignPricerow == null || orignPricerow.getBasePrice() == null) {
                return 0D; // 如果没有活动价和原价则返回0
            }
            return orignPricerow.getBasePrice();
        } else {
            return activityPricerow.getBasePrice();
        }
    }

    @Override
    public Double calculatePrice(String vCode, String priceType) {

        VCodeHeader vCodeHeader = ivCodeHeaderService.selectOneByVCode(vCode);
        if (vCodeHeader == null) {
            throw new RuntimeException("v码[" + vCode + "]在v码头表不存在");
        }

        String platformCode = vCodeHeader.getPlatformCode(); // 平台号
        Product platformProduct = iProductService.selectMarkorOnlineByCode(platformCode);
        if (platformProduct == null) {
            throw new RuntimeException("平台号[" + platformCode + "]不存在");
        }

        // 根据v码头查询v码行，v码行对应一个零部件
        List<VCodeLine> vCodeLines = ivCodeLineService.selectByHeaderId(vCodeHeader.getHeaderId());

        String fabricLevel = null; // 面料等级

        Map<Product, Integer> productRelation = new HashMap<>(); // 零部件商品与数量的对应关系
        if (CollectionUtils.isNotEmpty(vCodeLines)) {
            for (VCodeLine vCodeLine : vCodeLines) {
                if (vCodeLine.getQuantity() == null) {
                    throw new RuntimeException("零部件[" + vCodeLine.getIdnrk() + "]数量不能为空");
                }
                Product partProduct = iProductService.selectMarkorOnlineByCode(vCodeLine.getIdnrk());
                if (partProduct == null) {
                    throw new RuntimeException("零部件[" + vCodeLine.getIdnrk() + "]不存在");
                }
                productRelation.put(partProduct, vCodeLine.getQuantity());
            }

            Product material = productRelation.keySet().stream()
                    .filter(product -> Constants.MATERIAL.equals(product.getMtart()))
                    .findFirst().orElse(null);

            if (material != null) {
                Fabric fabric = iFabricService.selectByFabricCode(material.getCode());
                if (fabric != null) {
                    fabricLevel = fabric.getFabricLevel();
                }
            }
        }

        // 计算平台号的价格
        Double totalPrice = 0D;
        List<Pricerow> pricerowList = iPricerowService.selectPricerows(platformProduct.getProductId(), fabricLevel, priceType);

        if (CollectionUtils.isNotEmpty(pricerowList)) {
            Double platformPrice = getOptimalPrice(pricerowList);
            if (platformPrice == null) {
                throw new RuntimeException("平台号[" + platformProduct.getProductId()
                        + "]、面料等级[" + fabricLevel
                        + "]、价格类型[" + priceType + "]没有维护活动价和原价");
            }
            totalPrice += platformPrice; // 框架只有一个
        } else {
            throw new RuntimeException("平台号[" + platformProduct.getProductId()
                    + "]、面料等级[" + fabricLevel
                    + "]、价格类型[" + priceType + "]没有维护价格行");
        }

        // 计算零部件的价格
        for (Product product : productRelation.keySet()) {
            List<Pricerow> pricerowList1 = iPricerowService.selectPricerows(product.getProductId(), priceType);
            Double partPrice = getOptimalPrice(pricerowList1);
            // 零部件可以没有价格，可以免费送
            if (partPrice != null) {
                totalPrice += partPrice * productRelation.get(product);
                logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + product.getProductId() + "/" + productRelation.get(product) + "/" + partPrice);
            }
        }

        return totalPrice;
    }

    @Override
    public Double calculateOrderPrice(String vCode, String odType) {
        // 检查v码是否存在
        VCodeHeader vCodeHeader = ivCodeHeaderService.selectOneByVCode(vCode);
        if (vCodeHeader == null)
            throw new RuntimeException("v码[" + vCode + "]在v码头表不存在");

        // 检查平台号是否存在
        String platformCode = vCodeHeader.getPlatformCode();
        Product platformProduct = iProductService.selectMarkorOnlineByCode(platformCode);
        if (platformProduct == null)
            throw new RuntimeException("平台号[" + platformCode + "]不存在");

        // 根据v码头查询v码行，v码行对应一个零部件
        List<VCodeLine> vCodeLines = ivCodeLineService.selectByHeaderId(vCodeHeader.getHeaderId());

        // 构造Product(HMALL)与VCodeLine(配置器)之间的映射关系
        Map<Product, VCodeLine> vCodeLineMap = buildProductVCodeLineMap(vCodeLines);
        // 计算面料等级
        String fabricLevel = getFabricLevelOfPlatformProduct(vCodeLineMap);

        // 计算平台号的价格
        List<Pricerow> platformPricerows = iPricerowService.selectPricerows(platformProduct.getProductId(), fabricLevel, Constants.ORDER_PRICE_VALUE, odType);
        Double platformPrice = getOptimalPrice(platformPricerows);

        // 计算零部件的价格
        Double totalPartPrice = calculatePartPrice(vCodeLineMap, Constants.ORDER_PRICE_VALUE);

        // 如果odtype传入为2，则累加特殊工艺价格
        Double specialPrice = calculateSpecialPrice(platformProduct, odType);

        // 累加可选配包价格
        Double totalOptPackPrice = calculateOptPackPrice(vCodeLineMap, Constants.ORDER_PRICE_VALUE);

        // 计算总价格 =  平台价格 + 零部件价格 + 特殊工艺价格 + 可选配包价格
        Double totalPrice = platformPrice + totalPartPrice + specialPrice + totalOptPackPrice;

        String vCodeFormula = String.format(ORDER_PRICE_FORMAT, totalPrice, vCode, platformPrice, totalPartPrice, specialPrice, totalOptPackPrice);
        if (calculatFormule.get() == null)
            calculatFormule.set(new ArrayList<>());
        calculatFormule.get().add(vCodeFormula);

        return Double.valueOf(decimalFormat.format(totalPrice));
    }

    @Override
    public Double calOrderPrice(String vCode, String odType) {
        // 检查v码是否存在
        VCodeHeader vCodeHeader = ivCodeHeaderService.selectOneByVCode(vCode);
        Assert.notNull(vCodeHeader, "v码[" + vCode + "]在v码头表不存在");

        // 检查平台号是否存在
        String platformCode = vCodeHeader.getPlatformCode();
        Product platformProduct = iProductService.selectMarkorOnlineByCode(platformCode);
        Assert.notNull(platformProduct, "平台号[" + platformCode + "]不存在");

        // 根据v码头查询v码行，v码行对应一个零部件，该零部件集合可能为空
        List<VCodeLine> vCodeLines = ivCodeLineService.selectByHeaderId(vCodeHeader.getHeaderId());

        // 采购价格的组成：总价格 = 平台价格 + 零部件价格 + 特殊工艺价格
        Double platformPrice = 0D;  // 平台价格
        Double totalPartPrice = 0D; // 总的零部件价格

        // 如果定制类型为超级定制，计算特殊工艺价格
        Double specialPrice = Constants.ODTYPE_SUPER.equals(odType) ? (platformProduct.getSpecialPrice() == null ? 0 : platformProduct.getSpecialPrice()) : 0;

        // 如果平台号有零部件，则根据零部件计算零部件价格和平台号价格
        if (CollectionUtils.isNotEmpty(vCodeLines)) {

            // 检查零部件是否存在，如果存在构建product和vcodeline之间的对应关系，便于获取零部件的数量和伙伴关系
            Map<Product, VCodeLine> vCodeLineMap = buildProductVCodeLineMap(vCodeLines);

            // 筛选物料类型为面料的零部件
            List<Product> fabricList = vCodeLineMap.keySet().stream()
                    .filter(product -> Constants.MATERIAL.equals(product.getMtart()))
                    .collect(Collectors.toList());

            // 筛选物料类型中的普通组（非面料）
            List<Product> regularList = vCodeLineMap.keySet().stream()
                    .filter(product -> !Constants.MATERIAL.equals(product.getMtart()))
                    .collect(Collectors.toList());


            // 保存已经进行进行价格统计的零部件或平台号
            List<String> hasCaledProducts = new ArrayList<>();

            // 如果没有面料则不计算零部件价格，平台价格计算任务面料等级为null
            if (CollectionUtils.isEmpty(fabricList)) {
                // 计算平台号的价格
                List<Pricerow> platformPricerows = iPricerowService.selectPricerows(platformProduct.getProductId(), null, Constants.ORDER_PRICE_VALUE, odType);
                platformPrice = getOptimalPrice(platformPricerows);

                // 如果没有面料，则根据hasCaledProducts得到没有计算的零部件，统一进行计算
                totalPartPrice = 0D;
            } else {

                // 如果面料存在则根据面料计算零部件的价格
                for (Product product : fabricList) {
                    // 计算物料零部件的面料等级
                    Fabric fabric = iFabricService.selectByFabricCode(product.getCode());
                    String fabricLevel = fabric == null ? null : fabric.getFabricLevel();

                    // 获取物料的可选配包，如果可选配包不存在，则忽略此零部件价格
                    String optinal = vCodeLineMap.get(product).getPotx1();
                    if (StringUtils.isBlank(optinal)) {
                        continue;
                    }

                    // 根据面料等级和可选配包查询伙伴关系，面料的伙伴关系业务含义为该面料为哪些零部件所选（一对多）
                    // 目前的逻辑不存在一个零部件存在多个面料的情况
                    List<Conrel> conrelList = iConrelService.select(platformCode, optinal);

                    // 查出所有普通组的potx1的集合
                    List<String> regularCodeList = regularList.stream().filter(regular -> vCodeLineMap.get(regular).getPotx1() != null)
                            .map(regular -> vCodeLineMap.get(regular).getPotx1()).collect(Collectors.toList());

                    // 过滤掉伙伴关系不是平台号或者普通组的potx1的记录，即面料必须是为平台号或普通组所选
                    conrelList = conrelList.stream()
                            .filter(conrel -> regularCodeList.contains(conrel.getPart()) || conrel.getPart().equals(platformCode))
                            .collect(Collectors.toList());

                    // 如果过滤后没有找到伙伴关系，则不统计该零部件价格
                    if (CollectionUtils.isEmpty(conrelList)) {
                        continue;
                    }

                    // 根据伙伴关系统计零部件价格
                    for (Conrel conrel : conrelList) {
                        // 判断该伙伴关系是否已经进行了价格计算，这个判断主要是为了避免数据维护错误导致，一个零部件维护了多个面料的情况
                        if (hasCaledProducts.contains(conrel.getPart())) {
                            // 如果已经计算过，则跳过，避免重复计算
                            continue;
                        }

                        // 判断该伙伴关系是零部件还是平台号
                        if (regularCodeList.contains(conrel.getPart())) {
                            // 如果该伙伴关系为零部件，则找到该零部件
                            Product partProduct = regularList.stream()
                                    .filter(regular -> conrel.getPart() != null && conrel.getPart().equals(vCodeLineMap.get(regular).getPotx1()))
                                    .findFirst().get();

                            // 查询该零部件对应面料等级、采购价格的价格行
                            List<Pricerow> partPricerowList = iPricerowService.selectPricerows(partProduct.getProductId(), fabricLevel, Constants.ORDER_PRICE_VALUE);

                            // 该数量为普通组零部件的数量，而非面料组面料的数量
                            Integer quantity = vCodeLineMap.get(partProduct).getQuantity() == null ? 0 : vCodeLineMap.get(partProduct).getQuantity();

                            totalPartPrice += getOptimalPrice(partPricerowList) * quantity;

                            // 将已经计算过价格的普通组零部件放入到集合中，避免由于数据原因导致重复计算零部件的价格
                            hasCaledProducts.add(partProduct.getCode());
                        } else {
                            // 该伙伴关系为平台号
                            List<Pricerow> platformPricerowList = iPricerowService.selectPricerows(platformProduct.getProductId(), fabricLevel, Constants.ORDER_PRICE_VALUE, odType);

                            // 平台号累加价格默认数量为1
                            platformPrice += getOptimalPrice(platformPricerowList);

                            // 将平台号放入到集合中，避免由于平台多面料的情况，导致重复计算平台号的价格
                            hasCaledProducts.add(platformProduct.getCode());
                        }
                    }
                }

                // 判断平台号是否已经进行价格计算
                if (!hasCaledProducts.contains(platformCode)) {
                    // 如果此判断为真，表明面料组的伙伴关系中没有平台号，需要额外计算平台号的价格，这个是面料等级为null
                    List<Pricerow> platformPricerowList = iPricerowService.selectPricerows(platformProduct.getProductId(), null, Constants.ORDER_PRICE_VALUE, odType);

                    platformPrice += getOptimalPrice(platformPricerowList);
                }
            }

            // 累加没有计算的零部件价格
            List<Product> notCaledParts = regularList.stream().filter(regular -> !hasCaledProducts.contains(regular.getCode())).collect(Collectors.toList());
            totalPartPrice += getNotCaledPartsPrice(notCaledParts, vCodeLineMap, hasCaledProducts, Constants.ORDER_PRICE_VALUE);
        } else {
            // 如果没有发现零部件，则认为面料等级为null，计算平台号的价格
            List<Pricerow> platformPricerowList = iPricerowService.selectPricerows(platformProduct.getProductId(), null, Constants.ORDER_PRICE_VALUE, odType);
            platformPrice += getOptimalPrice(platformPricerowList);

            // 零部件的价格即为0
            totalPartPrice = 0D;
        }

        // 累加总价格
        Double totalPrice = platformPrice + totalPartPrice + specialPrice;

        // 计算组件的价格公式，该计算公式用于拼装套件的价格计算公式
        PriceFormule.setOrderPrice(totalPrice, vCode, platformPrice, totalPartPrice, specialPrice);

        return Double.valueOf(decimalFormat.format(totalPrice));
    }

    @Override
    public Double calculateSalePrice(String vCode, String odType) {
        // 检查v码是否存在
        VCodeHeader vCodeHeader = ivCodeHeaderService.selectOneByVCode(vCode);
        if (vCodeHeader == null)
            throw new RuntimeException("v码[" + vCode + "]在v码头表不存在");

        // 检查平台号是否存在
        String platformCode = vCodeHeader.getPlatformCode(); // 平台号
        Product platformProduct = iProductService.selectMarkorOnlineByCode(platformCode);
        if (platformProduct == null)
            throw new RuntimeException("平台号[" + platformCode + "]不存在");

        // 根据v码头查询v码行，v码行对应一个零部件
        List<VCodeLine> vCodeLines = ivCodeLineService.selectByHeaderId(vCodeHeader.getHeaderId());

        // 构造Product(HMALL)与VCodeLine(配置器)之间的映射关系
        Map<Product, VCodeLine> vCodeLineMap = buildProductVCodeLineMap(vCodeLines);
        // 计算面料等级
        String fabricLevel = getFabricLevelOfPlatformProduct(vCodeLineMap);

        // 计算平台号的价格
        List<Pricerow> pricerowList = iPricerowService.selectPricerows(platformProduct.getProductId(), fabricLevel, Constants.SALE_PRICE_VALUE, odType);
        Double platformPrice = getOptimalPrice(pricerowList);

        // 计算零部件的价格
        Double totalPartPrice = calculatePartPrice(vCodeLineMap, Constants.SALE_PRICE_VALUE);

        // 计算可选配包价格
        Double totalOptPackPrice = calculateOptPackPrice(vCodeLineMap, Constants.SALE_PRICE_VALUE);

        // 计算总价格 = 平台价格 + 零部件价格 + 可选配包价格
        Double totalPrice = platformPrice + totalPartPrice + totalOptPackPrice;

        // 保存计算公式
        String vCodeFormula = String.format(SALE_PRICE_FORMAT, totalPrice, vCode, platformPrice, totalPartPrice, totalOptPackPrice);
        if (calculatFormule.get() == null)
            calculatFormule.set(new ArrayList<>());
        calculatFormule.get().add(vCodeFormula);

        return Double.valueOf(decimalFormat.format(totalPrice));
    }

    @Override
    public Double calSalePrice(String vCode, String odType) {
        // 检查v码是否存在
        VCodeHeader vCodeHeader = ivCodeHeaderService.selectOneByVCode(vCode);
        Assert.notNull(vCodeHeader, "v码[" + vCode + "]在v码头表不存在");

        // 检查平台号是否存在
        String platformCode = vCodeHeader.getPlatformCode();
        Product platformProduct = iProductService.selectMarkorOnlineByCode(platformCode);
        Assert.notNull(platformProduct, "平台号[" + platformCode + "]不存在");

        // 根据v码头查询v码行，v码行对应一个零部件，该零部件集合可能为空
        List<VCodeLine> vCodeLines = ivCodeLineService.selectByHeaderId(vCodeHeader.getHeaderId());

        // 销售价格的组成：总价格 = 平台价格 + 零部件价格
        Double platformPrice = 0D;  // 平台价格
        Double totalPartPrice = 0D; // 总的零部件价格
        if (CollectionUtils.isNotEmpty(vCodeLines)) {

            // 检查零部件是否存在，如果存在构建product和vcodeline之间的对应关系，便于获取零部件的数量和伙伴关系
            Map<Product, VCodeLine> vCodeLineMap = buildProductVCodeLineMap(vCodeLines);

            // 筛选物料类型为面料的零部件
            List<Product> fabricList = vCodeLineMap.keySet().stream()
                    .filter(product -> Constants.MATERIAL.equals(product.getMtart()))
                    .collect(Collectors.toList());

            // 筛选物料类型中的普通组（非面料）
            List<Product> regularList = vCodeLineMap.keySet().stream()
                    .filter(product -> !Constants.MATERIAL.equals(product.getMtart()))
                    .collect(Collectors.toList());


            // 保存已经进行进行价格统计的零部件或平台号
            List<String> hasCaledProducts = new ArrayList<>();

            // 如果没有面料则不计算零部件价格，平台价格计算任务面料等级为null
            if (CollectionUtils.isEmpty(fabricList)) {
                // 计算平台号的价格
                List<Pricerow> platformPricerows = iPricerowService.selectPricerows(platformProduct.getProductId(), null, Constants.SALE_PRICE_VALUE, odType);
                platformPrice = getOptimalPrice(platformPricerows);

                // 如果没有面料，则根据hasCaledProducts得到没有计算的零部件，统一进行计算
                totalPartPrice = 0D;
            } else {

                // 如果面料存在则根据面料计算零部件的价格
                for (Product product : fabricList) {
                    // 计算物料零部件的面料等级
                    Fabric fabric = iFabricService.selectByFabricCode(product.getCode());
                    String fabricLevel = fabric == null ? null : fabric.getFabricLevel();

                    // 获取物料的可选配包，如果可选配包不存在，则忽略此零部件价格
                    String optinal = vCodeLineMap.get(product).getPotx1();
                    if (StringUtils.isBlank(optinal)) {
                        continue;
                    }

                    // 根据面料等级和可选配包查询伙伴关系
                    List<Conrel> conrelList = iConrelService.select(platformCode, optinal);

                    // 查出所有普通组的potx1的集合
                    List<String> regularCodeList = regularList.stream().filter(regular -> vCodeLineMap.get(regular).getPotx1() != null)
                            .map(regular -> vCodeLineMap.get(regular).getPotx1()).collect(Collectors.toList());

                    // 过滤掉伙伴关系不是平台号或者普通组的potx1的记录，即面料必须是为平台号或普通组所选
                    conrelList = conrelList.stream()
                            .filter(conrel -> regularCodeList.contains(conrel.getPart()) || conrel.getPart().equals(platformCode))
                            .collect(Collectors.toList());

                    // 如果过滤后没有找到伙伴关系，则不统计该零部件价格
                    if (CollectionUtils.isEmpty(conrelList)) {
                        continue;
                    }

                    // 根据伙伴关系统计零部件价格
                    for (Conrel conrel : conrelList) {
                        // 判断该伙伴关系是否已经进行了价格计算
                        if (hasCaledProducts.contains(conrel.getPart())) {
                            // 如果已经计算过，则跳过，避免重复计算
                            continue;
                        }

                        // 判断该伙伴关系是零部件还是平台号
                        if (regularCodeList.contains(conrel.getPart())) {
                            // 如果该伙伴关系为零部件，则找到该零部件
                            Product partProduct = regularList.stream()
                                    .filter(regular -> conrel.getPart() != null && conrel.getPart().equals(vCodeLineMap.get(regular).getPotx1()))
                                    .findFirst().get();

                            List<Pricerow> partPricerowList = iPricerowService.selectPricerows(partProduct.getProductId(), fabricLevel, Constants.SALE_PRICE_VALUE);

                            // 该数量为普通组零部件的数量，而非面料组面料的数量
                            Integer quantity = vCodeLineMap.get(partProduct).getQuantity() == null ? 0 : vCodeLineMap.get(partProduct).getQuantity();

                            totalPartPrice += getOptimalPrice(partPricerowList) * quantity;

                            // 将已经计算过价格的普通组零部件放入到集合中，避免由于数据原因导致重复计算零部件的价格
                            hasCaledProducts.add(partProduct.getCode());
                        } else {
                            // 该伙伴关系为平台号
                            List<Pricerow> platformPricerowList = iPricerowService.selectPricerows(platformProduct.getProductId(), fabricLevel, Constants.SALE_PRICE_VALUE, odType);

                            // 平台号累加价格默认数量为1
                            platformPrice += getOptimalPrice(platformPricerowList);

                            // 将平台号放入到集合中，避免由于平台多面料的情况，导致重复计算平台号的价格
                            hasCaledProducts.add(platformProduct.getCode());
                        }
                    }
                }

                // 判断平台号是否已经进行价格计算
                if (!hasCaledProducts.contains(platformCode)) {
                    // 如果此判断为真，表明面料组的伙伴关系中没有平台号，需要额外计算平台号的价格，且面料等级为null
                    List<Pricerow> platformPricerowList = iPricerowService.selectPricerows(platformProduct.getProductId(), null, Constants.SALE_PRICE_VALUE, odType);

                    platformPrice += getOptimalPrice(platformPricerowList);
                }
            }


            // 累加没有计算的零部件价格
            List<Product> notCaledParts = regularList.stream().filter(regular -> !hasCaledProducts.contains(regular.getCode())).collect(Collectors.toList());
            totalPartPrice += getNotCaledPartsPrice(notCaledParts, vCodeLineMap, hasCaledProducts, Constants.SALE_PRICE_VALUE);
        } else {
            // 如果没有发现零部件，则认为面料等级为null，计算平台号的价格
            List<Pricerow> platformPricerowList = iPricerowService.selectPricerows(platformProduct.getProductId(), null, Constants.SALE_PRICE_VALUE, odType);
            platformPrice += getOptimalPrice(platformPricerowList);

            // 零部件的价格即为0
            totalPartPrice = 0D;
        }

        // 累加总价格
        Double totalPrice = platformPrice + totalPartPrice;

        // 计算组件的价格公式，该计算公式用于拼装套件的价格计算公式
        PriceFormule.setSalePrice(totalPrice, vCode, platformPrice, totalPartPrice);

        return Double.valueOf(decimalFormat.format(totalPrice));
    }

    /**
     * 统计尚未计算的零部件价格
     * @param productList 未计算的零部件
     * @param vCodeLineMap v码行映射
     * @param hasCaledProducts 已经计算的商品集合
     * @param priceType 价格类型
     * @return Double
     */
    private Double getNotCaledPartsPrice(List<Product> productList, Map<Product, VCodeLine> vCodeLineMap, List<String> hasCaledProducts, String priceType) {
        Double totalPartPrice = 0D;
        if (CollectionUtils.isNotEmpty(productList)) {
            for (Product product : productList) {
                // 查询该零部件对应面料等级、采购价格的价格行
                List<Pricerow> partPricerowList = iPricerowService.selectPricerows(product.getProductId(), priceType);

                // 该数量为普通组零部件的数量，而非面料组面料的数量
                Integer quantity = vCodeLineMap.get(product).getQuantity() == null ? 0 : vCodeLineMap.get(product).getQuantity();

                totalPartPrice += getOptimalPrice(partPricerowList) * quantity;

                // 将已经计算过价格的普通组零部件放入到集合中，避免由于数据原因导致重复计算零部件的价格
                hasCaledProducts.add(product.getCode());
            }
        }

        return totalPartPrice;
    }

    @Override
    public Double calculatePriceByProductCode(String productCode, String priceType) {
        Product product = iProductService.selectMarkorOnlineByCode(productCode);
        if (product == null)
            throw new RuntimeException("平台号[" + productCode + "]不存在");
        List<Pricerow> pricerowList = iPricerowService.selectPricerows(product.getProductId(), priceType);
        return getOptimalPrice(pricerowList);
    }

    /**
     * 计算平台号的面料等级
     *
     * @param vCodeLineMap 零部件行映射
     * @return String
     */
    private String getFabricLevelOfPlatformProduct(Map<Product, VCodeLine> vCodeLineMap) {
        Product material = null;
        String fabricLevel = null;
        // 查询零部件中mtart为Z08（面料）的零部件
        List<Product> materialList = vCodeLineMap.keySet().stream()
                .filter(product -> Constants.MATERIAL.equals(product.getMtart())).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(materialList)) {
            if (CollectionUtils.size(materialList) == 1) {
                // 如果只有一个为面料，则取该面料
                material = materialList.get(0);
            } else {
                // 如果有多个面料则取potx1（可选配包）为20627900（沙发主体包）的面料，
                List<Product> sofaBodyPacks = materialList.stream().filter(product -> SOFA_BODY_PACKAGE.equals(vCodeLineMap.get(product).getPotx1()))
                        .collect(Collectors.toList());
                if (CollectionUtils.isEmpty(sofaBodyPacks)) {
                    // 如果没有20627900的面料则取第一个面料（或随机）
                    material = materialList.get(0);
                } else {
                    // 如果有或多个，取一个
                    material = sofaBodyPacks.get(0);
                }
            }
        }

        if (material != null) {
            Fabric fabric = iFabricService.selectByFabricCode(material.getCode());
            if (fabric != null) {
                fabricLevel = fabric.getFabricLevel();
            }
        }

        // 如果没有查询的面料，则返回面料等级为null，null也是一个合法的面料等级

        return fabricLevel;
    }

    @Override
    public Double calculateOptPackPrice(Map<Product, VCodeLine> vCodeLineMap, String priceType) {
        List<Product> materialList = getMaterialList(vCodeLineMap);
        if (CollectionUtils.isEmpty(materialList)) {
            return 0D;
        }

        Double totalOptPackPrice = 0D;
        for (Product product : materialList) {
            // 获取零部件的可选配包(Optional Package)
            String optPack = vCodeLineMap.get(product).getPotx1();
            if (StringUtils.isNotBlank(optPack)) {
                // 查询可选配包对应的商品
                Product optProduct = iProductService.selectMarkorOnlineByCode(optPack);
                if (optProduct == null) throw new RuntimeException("可选配包[" + optPack + "]不存在");

                // 查询可选配包的面料等级
                Fabric fabric = iFabricService.selectByFabricCode(optPack);
                String optPackLevel = (fabric == null ? null : fabric.getFabricLevel());

                // 查询可选配包的价格行
                List<Pricerow> optPackPricerows = iPricerowService.selectPricerows(optProduct.getProductId(), optPackLevel, priceType);
                // 计算可选配包的最优价格并累加
                totalOptPackPrice += getOptimalPrice(optPackPricerows);
            }
        }

        return totalOptPackPrice;
    }

    /**
     * 获取零部件中为物料的商品
     *
     * @param vCodeLineMap 零部件映射关系
     * @return List<Product>
     */
    private List<Product> getMaterialList(Map<Product, VCodeLine> vCodeLineMap) {
        return vCodeLineMap.keySet().stream().filter(product ->
                Constants.MATERIAL.equals(product.getMtart())).collect(Collectors.toList());
    }

    @Override
    public Double calculatePartPrice(Map<Product, VCodeLine> vCodeLineMap, String priceType) {
        Double totalPartPrice = 0D;
        for (Product product : vCodeLineMap.keySet()) {
            List<Pricerow> partPricerows = iPricerowService.selectPricerows(product.getProductId(), priceType);
            Double partPrice = getOptimalPrice(partPricerows);
            Integer quantity = vCodeLineMap.get(product).getQuantity() == null ? 0 : vCodeLineMap.get(product).getQuantity();
            totalPartPrice += partPrice * quantity;
        }
        return totalPartPrice;
    }

    @Override
    public Double calculateSpecialPrice(Product platformProduct, String odtype) {
        if (Constants.ODTYPE_SUPER.equals(odtype)) {
            if (platformProduct.getSpecialPrice() != null) {
                return platformProduct.getSpecialPrice();
            }
        }
        return 0D;
    }

    @Override
    public Map<Product, VCodeLine> buildProductVCodeLineMap(List<VCodeLine> vCodeLines) {
        Map<Product, VCodeLine> vCodeLineMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(vCodeLines)) {
            for (VCodeLine vCodeLine : vCodeLines) {
                Product partProduct = iProductService.selectMarkorOnlineByCode(vCodeLine.getIdnrk());
                if (partProduct == null)
                    throw new RuntimeException("零部件[" + vCodeLine.getIdnrk() + "]不存在");
                vCodeLineMap.put(partProduct, vCodeLine);
            }
        }
        return vCodeLineMap;
    }

    @Override
    public String buildPriceFormula(Double totalPrice, String vCode) {
        StringBuffer sb = new StringBuffer();
        sb.append(totalPrice).append("(").append(vCode).append(") = ");
        calculatFormule.get().stream().forEach(formula -> sb.append(formula).append(" + "));
        sb.delete(sb.lastIndexOf(" + "), sb.length());
        calculatFormule.remove();
        return sb.toString();
    }
}
