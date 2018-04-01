package com.hand.hmall.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.AtomicDouble;
import com.hand.hmall.client.IHapService;
import com.hand.hmall.client.IPdClientService;
import com.hand.hmall.client.IProductClientService;
import com.hand.hmall.dto.*;
import com.hand.hmall.model.*;
import com.hand.hmall.pojo.OrderEntryPojo;
import com.hand.hmall.pojo.OrderPojo;
import com.hand.hmall.service.*;
import com.hand.hmall.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kie.api.KieServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.hand.hmall.util.NumFormatUtil.format;
import static com.hand.hmall.util.NumFormatUtil.formatNotCheck;
import static java.util.Comparator.comparing;

/**
 * @author xinyang.Mei
 * @desp 订单促销相关计算
 */
@Service
public class PromoteCaculateServiceImp implements PromoteCaculateService {


    @Resource
    private IProductClientService iProductClientService;
    @Autowired
    private IMainCarriageService iMainCarriageService;
    @Autowired
    private ISubCarriageService iSubCarriageService;
    @Autowired
    private ICategoryMappingService iCategoryMappingService;
    @Autowired
    private HmallMstInstallationService hmallMstInstallationService;
    @Autowired
    private HmallMstUnitService hmallMstUnitService;
    @Autowired
    private IProductClientService productClientService;
    @Autowired
    private IPdClientService pdClientService;
    @Autowired
    private IMstPointofServiceService pointofServiceService;
    @Autowired
    private IHmallFndRegionBService regionBService;
    @Autowired
    private IHapService hapService;

    private KieServices kieServices = KieServices.Factory.get();
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    /**
     * @param orderMap
     * @param order
     * @desp 前台请求参数转换为DTO
     */
    @Override
    public void mapToOrder(Map orderMap, OrderPojo order) {
        if (StringUtils.isEmpty((String) orderMap.get("created"))) {
            throw new RuntimeException("订单创建时间为空");
        }
        if (StringUtils.isEmpty((String) orderMap.get("isCaculate"))) {
            throw new RuntimeException("订单执行促销标识isCalculate为空");
        }
        MapToBean.transMap2Bean(orderMap, order);
        List<Map> orderEntryMapList = (List) order.getOrderEntryList();
        List<OrderEntryPojo> list = new ArrayList<>();
        for (Map map : orderEntryMapList) {
            OrderEntryPojo orderEntry = new OrderEntryPojo();
            MapToBean.transMap2Bean(map, orderEntry);
            //擦除入参干扰数据，将订单行相关的fee置为0
            orderEntry.initEntry();
            orderEntry.setBundleRemainder(orderEntry.getQuantity());
            //判断传入订单行是否为赠品
            String isGift = orderEntry.getIsGift();
            if (StringUtils.isNotEmpty(isGift) && "N".equals(isGift)) {
                list.add(orderEntry);
            }
        }
        order.setOrderEntryList(list);
        //将订单相关fee置为0
        order.initOrder();
    }


    /**
     * 安装费计算
     *
     * @param order
     * @return
     */
    @Override
    public ResponseData caculateInstallationPay(OrderPojo order) throws InterruptedException {
        DecimalFormat df = new DecimalFormat("#.00");
        ResponseData responseData = new ResponseData();
        List<OrderEntryPojo> orderEntryPojos = order.getOrderEntryList();
        AtomicDouble totalFee = new AtomicDouble(0);
        CountDownLatch latch = new CountDownLatch(orderEntryPojos.size());
        List<String> errorList = Collections.synchronizedList(new ArrayList<>());
        AtomicBoolean errorFlag = new AtomicBoolean(true);
        for (OrderEntryPojo orderEntryPojo : orderEntryPojos) {
            ThreadFactory.getThreadPool().submit(new CalcInstallationFeeTask(latch, errorList, errorFlag, orderEntryPojo, order, totalFee));
        }
        latch.await();
        logger.info("--caculateInstallationPay errFlag--{}", errorFlag.get());
        if (errorFlag.get() == false) {
            responseData.setSuccess(false);
            responseData.setMsg(JSON.toJSONString(errorList));
            return responseData;
        }
        order.setFixFee(Double.valueOf(df.format(totalFee.get())));
        responseData.setSuccess(true);
        responseData.setResp(new ArrayList<>(Arrays.asList(order)));
        return responseData;
    }

    /**
     * 计算快递与物流的运费
     *
     * @param order
     * @return
     */
    @Override
    public ResponseData caculateExpressAndLogisticsFreight(OrderPojo order) throws ExecutionException, InterruptedException {
        ResponseData responseData = new ResponseData();
        //计算运费前数据准备
        List<OrderEntryPojo> orderEntrys = order.getOrderEntryList();
        List<OrderEntryPojo> logisticEntrys = Collections.synchronizedList(new ArrayList<>());
        List<OrderEntryPojo> expressEntrys = Collections.synchronizedList(new ArrayList<>());
        int expressQuantity = 0;
        int logisticQuantity = 0;
        for (OrderEntryPojo entryPojo : orderEntrys) {
            String shipType = entryPojo.getShippingType();
            //根据shipType分类,存到不同list
            if ("LOGISTICS".equals(shipType.trim())) {
                //物流
                logisticQuantity += entryPojo.getQuantity();
                logisticEntrys.add(entryPojo);
            } else if ("EXPRESS".equals(shipType.trim())) {
                //快递
                expressQuantity += entryPojo.getQuantity();
                expressEntrys.add(entryPojo);
            }
        }
        //计算快递的运费
        Future<Double> expressThread = ThreadFactory.getThreadPool().submit(new CaculateExpressFegiht(expressEntrys, order, expressQuantity, "EXPRESS", responseData));
        //计算物流的运费
        Future<Double> logisThread = ThreadFactory.getThreadPool().submit(new CaculateLogisticFegiht(logisticEntrys, order, logisticQuantity, "LOGISTICS", responseData));

        Double expressFeight = expressThread.get();
        if (!responseData.isSuccess()) {
            return responseData;
        }
        order.setEpostFee(expressFeight);

        Double logisFeight = logisThread.get();
        if (!responseData.isSuccess()) {
            return responseData;
        }
        order.setPostFee(logisFeight);
        order.setQuantity(expressQuantity + logisticQuantity);
        return responseData;
    }

    /**
     * 促销金额分摊
     *
     * @param orderPojo
     * @return
     */
    @Override
    public ResponseData apportionPromotion(OrderPojo orderPojo) {
        if (orderPojo.isExclusive()) {
            dealExclusiveCoupon(orderPojo);
        }
        ResponseData responseData = new ResponseData();
        //订单头上的优惠总金额
        double orderDiscountFee = orderPojo.getDiscountFee();
        //订单头与行优惠总金额
        double orderAndEntrysDiscountFee = orderDiscountFee;
        List<OrderEntryPojo> orderEntryPojos = orderPojo.getOrderEntryList();
        //sumEntryAmount 保存订单行促销后应付金额之和
        double sumEntryAmount = 0.0D;
        for (OrderEntryPojo orderEntryPojo : orderEntryPojos) {
            if ("N".equals(orderEntryPojo.getIsGift().trim())) {
                //每一个订单行总金额-减去折扣金额
                double entryAmount = CalculateUtil.sub(CalculateUtil.mul(orderEntryPojo.getQuantity(), orderEntryPojo.getBasePrice(), 2, "UP"), orderEntryPojo.getDiscountFee());
                sumEntryAmount = CalculateUtil.add(sumEntryAmount, entryAmount);
            }
        }
        orderPojo.setDiscountFee(orderAndEntrysDiscountFee);
        double netAmount = 0.0D;
        //订单行优惠金额分摊
        double usedDiscountFee = 0;
        double usedCouponFee = 0;
        double realCouponFee = 0.0;
        for (int i = 0; i < orderEntryPojos.size(); i++) {

            OrderEntryPojo orderEntryPojo = orderEntryPojos.get(i);
            //赠品不会参与到金额分摊，对订单行进行校验
            if ("N".equals(orderEntryPojo.getIsGift().trim())) {
                //订单行促销优惠金额
                Double discountFee = orderEntryPojo.getDiscountFee();
                //订单行商品基础价格
                Double basePrice = orderEntryPojo.getBasePrice();
                //订单行商品数量
                Integer quantity = orderEntryPojo.getQuantity();
                //每一个订单行总金额-减去折扣金额。
                Double entryAmount = CalculateUtil.sub(CalculateUtil.mul(basePrice, quantity), discountFee);
                //discountFeeL 保存订单头分摊到行的折扣金额。分摊比率为订单行费用与所有订单行费用之和的的比值
                Double discountFeeL = 0.0;
                Double couponFee = 0.0;
                if (i == orderEntryPojos.size() - 1) {
                    discountFeeL = CalculateUtil.sub(orderDiscountFee, usedDiscountFee);
                    couponFee = CalculateUtil.sub(orderPojo.getCouponFee(), usedCouponFee);
                } else {
                    discountFeeL = CalculateUtil.div(CalculateUtil.mul(orderDiscountFee, entryAmount), sumEntryAmount, 2, "UP");
                    //couponFee 优惠券分摊到行的优惠金额。分摊率同上
                    couponFee = CalculateUtil.div(CalculateUtil.mul(orderPojo.getCouponFee(), entryAmount), sumEntryAmount, 2, "UP");
                    usedDiscountFee = CalculateUtil.add(usedDiscountFee, discountFeeL);
                    usedCouponFee = CalculateUtil.add(usedCouponFee, couponFee);
                }

//                每个订单行应付总金额
                Double totalFee = CalculateUtil.mul(basePrice, quantity);
                totalFee = CalculateUtil.sub(totalFee, discountFee);
                totalFee = CalculateUtil.sub(totalFee, discountFeeL);
                totalFee = CalculateUtil.sub(totalFee, couponFee);
                totalFee = Double.valueOf(format(totalFee));
                orderAndEntrysDiscountFee = CalculateUtil.add(orderAndEntrysDiscountFee, orderEntryPojo.getDiscountFee());
                //订单行中每件商品应付金额
                Double unitFee = CalculateUtil.div(totalFee, quantity, 2, "UP");
                //累加计算订单净额
                netAmount = CalculateUtil.add(netAmount, CalculateUtil.sub(CalculateUtil.mul(basePrice, quantity), discountFee));
                //累加计算订单总优惠
                Double totalDiscountFee = CalculateUtil.add(CalculateUtil.add(discountFee, discountFeeL), couponFee);
                orderEntryPojo.setDiscountFeel(discountFeeL);
                orderEntryPojo.setTotalFee(totalFee);
                orderEntryPojo.setUnitFee(Double.valueOf(format(unitFee)));
                orderEntryPojo.setDiscountFee(Double.valueOf(formatNotCheck(discountFee)));
                orderEntryPojo.setTotalDiscount(Double.valueOf(formatNotCheck(totalDiscountFee)));
                if (couponFee == 0) {
                    realCouponFee = ArithUtil.add(realCouponFee, orderEntryPojo.getCouponFee());
                } else {
                    orderEntryPojo.setCouponFee(couponFee);
                }
            }
        }
        orderPojo.setDiscountFee(orderAndEntrysDiscountFee);
        orderPojo.setTotalDiscount(CalculateUtil.add(orderPojo.getDiscountFee(), orderPojo.getCouponFee()));
        netAmount = CalculateUtil.sub(CalculateUtil.sub(netAmount, orderDiscountFee), orderPojo.getCouponFee());
        orderPojo.setNetAmount(netAmount > 0 ? netAmount : 0);
        double orderAmount = CalculateUtil.add(CalculateUtil.add(orderPojo.getNetAmount(), orderPojo.getEpostFee()), CalculateUtil.add(orderPojo.getPostFee(), orderPojo.getFixFee()));
        orderPojo.setOrderAmount(orderAmount > 0 ? orderAmount : 0);
        responseData.setResp(new ArrayList<>(Arrays.asList(orderPojo)));
        return responseData;
    }

    /**
     * @param orderPojo
     * @return
     * @desp 商品换购逻辑，暂未启用
     */
    @Override
    public void addCheaperProduct(OrderPojo orderPojo) {
        //获取需要换购的商品列表cheaperList，该字段用于返回给调用者可选的换购商品列表。同时存储调用者选择的换购商品。
        List<Map<String, Object>> cheaperList = orderPojo.getCheaperList();
        //用户未选择换购商品，初始化列表
        if (CollectionUtils.isEmpty(cheaperList)) {
            cheaperList = new ArrayList<>();
            orderPojo.setCheaperList(cheaperList);
        } else {
            List<OrderEntryPojo> orderEntryList = orderPojo.getOrderEntryList();
            //遍历换购商品列表，将其添加到订单行
            for (Map<String, Object> cheaperProduct : cheaperList) {
                ResponseData productResp = productClientService.selectProductByCode((String) cheaperProduct.get("productCode"));
                //查询商品
                Map productMap = (Map) productResp.getResp().get(0);
                //查询商品价格
                ResponseData priceResp = pdClientService.getProductPrice((Integer) productMap.get("productId"));
                Map priceMap = (Map) priceResp.getResp().get(0);
                OrderEntryPojo orderEntry = new OrderEntryPojo();
                orderEntry.setProductId(productMap.get("productId").toString());
                orderEntry.setProduct((String) cheaperProduct.get("productCode"));
                orderEntry.setVproduct((String) productMap.get("vProductCode"));
                orderEntry.setShippingType((String) productMap.get("defaultDelivery"));
                orderEntry.setBasePrice((Double) priceMap.get("basePrice"));
                orderEntry.setQuantity((Integer) cheaperProduct.get("quantity"));
                orderEntryList.add(orderEntry);
            }
            cheaperList.clear();
        }
    }

    /**
     * 查询分类的所有父级分类
     *
     * @param categoryId
     * @param parentIds
     */
    @Override
    public void getParentIds(Long categoryId, List<String> parentIds) {
        ResponseData parentIdResp = productClientService.queryParentId(categoryId);
        List<?> parentIdList = parentIdResp.getResp();
        if (CollectionUtils.isEmpty(parentIdList))
            return;
        else {
            Map parentCategory = (Map) parentIdList.get(0);
            parentIds.add(parentCategory.get("categoryId").toString());
            getParentIds(Long.parseLong(parentCategory.get("categoryId").toString()), parentIds);
        }

    }

    /**
     * 对促销返回字段进行处理，包括移除多余字段，字段排序
     *
     * @param order
     * @return
     */
    @Override
    public JSONObject getPromoteResult(OrderPojo order) {
        JSONObject orderJson = JSONObject.parseObject(JSON.toJSONString(order));
        JSONArray entrys = orderJson.getJSONArray("orderEntryList");
        JSONArray coupons = orderJson.getJSONArray("couponList");
        if (CollectionUtils.isNotEmpty(coupons)) {
            coupons.sort(comparing(map -> {
                JSONObject map1 = (JSONObject) map;
                String discountType = map1.getString("discountType");
                if (StringUtils.isEmpty(discountType)) {
                    discountType = "other";
                }
                return discountType;
            }).reversed().thenComparing(comparing(map -> {
                JSONObject map1 = (JSONObject) map;
                Double benefit = map1.getDouble("benefit");
                if (benefit == null) {
                    benefit = 0.0;
                }
                return benefit * -1;
            })).reversed().thenComparing(comparing(map -> {
                JSONObject map1 = (JSONObject) map;
                String couponId = map1.getString("couponId");
                if (StringUtils.isEmpty(couponId)) {
                    couponId = "defultCouponId";
                }
                return couponId;
            })));
        }
        if (CollectionUtils.isNotEmpty(entrys)) {
            entrys.stream().forEach(e -> {
                JSONObject entry = (JSONObject) e;
                entry.remove("productId");
                entry.remove("categoryId");
                entry.remove("bundleRemainder");
//                entry.remove("cateList");
            });
        }
        orderJson.remove("usedCoupon");
        orderJson.remove("cheaperList");
        return orderJson;
    }

    /**
     * 计算订单行安装费线程
     */
    class CalcInstallationFeeTask implements Runnable {

        private CountDownLatch latch;

        private List<String> errors;

        private AtomicBoolean errorFalg;

        private AtomicDouble totalFee;

        private OrderEntryPojo orderEntryPojo;

        private OrderPojo order;

        /**
         * 计算订单行运费
         *
         * @param latch
         * @param errors
         * @param errorFalg
         * @param orderEntryPojo
         * @param order
         * @param totalFee
         */
        public CalcInstallationFeeTask(CountDownLatch latch, List<String> errors, AtomicBoolean errorFalg, OrderEntryPojo orderEntryPojo, OrderPojo order, AtomicDouble totalFee) {
            this.latch = latch;
            this.errors = errors;
            this.errorFalg = errorFalg;
            this.orderEntryPojo = orderEntryPojo;
            this.order = order;
            this.totalFee = totalFee;
        }

        @Override
        public void run() {
            try {
                ResponseData productInfo = iProductClientService.selectProductByCode(orderEntryPojo.getProduct());
                List resp = productInfo.getResp();
                //数据校验
                if (CollectionUtils.isEmpty(resp)) {
                    errors.add("code为" + orderEntryPojo.getProduct() + "的商品不存在");
                    logger.info("code为" + orderEntryPojo.getProduct() + "的商品不存在");
                    errorFalg.set(false);
                    return;
                }
                Object object = resp.get(0);
                if (object == null) {
                    errors.add("code为" + orderEntryPojo.getProduct() + "的商品不存在");
                    logger.info("code为" + orderEntryPojo.getProduct() + "的商品不存在");
                    errorFalg.set(false);
                    return;
                }
                String hmallMstProductString = JSON.toJSONString(object);
                JSONObject hmallMstProduct = JSON.parseObject(hmallMstProductString);
                long productId = hmallMstProduct.getLong("productId");
                String customSupportType = hmallMstProduct.getString("customSupportType");
                orderEntryPojo.setCustomSupportType(customSupportType);
                orderEntryPojo.setProductId(hmallMstProduct.getString("productId"));
                CategoryMapping categoryMapping = iCategoryMappingService.getCategoryByProductId(productId);
                if (categoryMapping == null) {
                    errors.add("prodcutId为" + productId + "品类映射数据不存在");
                    logger.info("prodcutId为" + productId + "品类映射数据不存在");
                    errorFalg.set(false);
                    return;
                }

                //初始化cateList
                orderEntryPojo.setCategoryId(categoryMapping.getCategoryId().toString());
//                List<String> cateList = new ArrayList<>(5);
//                cateList.add(categoryMapping.getCategoryId().toString());
//                getParentIds(categoryMapping.getCategoryId(), cateList);
                orderEntryPojo.setCateList(categoryMapping.getCategoryId().toString());

                orderEntryPojo.setPointOfServiceCode(hmallMstProduct.getString("warehouse"));

                HmallMstInstallation hmallMstInstallation = hmallMstInstallationService.getInstallationByCategoryIdAndStatus(categoryMapping.getCategoryId(), "Y");
                //查询商品对应分类下的安装费
                if (null != hmallMstInstallation) {
                    double entryFee = CalculateUtil.mul(hmallMstInstallation.getInstallationFee(), orderEntryPojo.getQuantity());
                    totalFee.addAndGet(entryFee);
                    orderEntryPojo.setInstallationFee(entryFee);
                } else {
                    errors.add("查询不到类别ID为" + categoryMapping.getCategoryId() + "的安装费");
                    logger.info("查询不到类别ID为" + categoryMapping.getCategoryId() + "的安装费");
                    errorFalg.set(false);
                    return;
                }

            } catch (Exception e) {
                errorFalg.set(false);
                errors.add("查询" + orderEntryPojo.getProduct() + "的安装费异常");
                logger.info("查询" + orderEntryPojo.getProduct() + "的安装费异常");
                logger.error("安装费计算异常" + e.getMessage(), e);
            } finally {
                latch.countDown();
            }

        }
    }

    /**
     * 根据门店对订单行进行分组
     *
     * @param entryList
     * @return
     */
    protected Map groupList(List<OrderEntryPojo> entryList) {
        Map<String, List<OrderEntryPojo>> groupMap = new HashMap();
        entryList.forEach(entry -> {

            if (groupMap.containsKey(entry.getPointOfServiceCode())) {
                List<OrderEntryPojo> groupEntryList = groupMap.get(StringUtils.isEmpty(entry.getPointOfServiceCode()) ? "null" : entry.getPointOfServiceCode());
                groupEntryList.add(entry);
            } else {
                List<OrderEntryPojo> groupEntryList = new ArrayList<>();
                groupEntryList.add(entry);
                groupMap.put(StringUtils.isEmpty(entry.getPointOfServiceCode()) ? "null" : entry.getPointOfServiceCode(), groupEntryList);
            }
        });
        return groupMap;
    }

    /**
     * 计算门店信息相同的的订单行的运费
     * 将订单行进行累加，当成一个行计算
     */
    public Double calFreihgtWithPOS(List<OrderEntryPojo> entryPojos, OrderPojo order, String shippingType, AtomicDouble expressFeight, List<String> errorList) {
        //门店信息
        MstPointOfService pointOfService = null;
        //主干运费信息
        HamllMstMainCarriage hamllMstMainCarriage = null;
        //支干运费
        HamllMstSubCarriage subCarriage = null;
        //ALL订单行的体积
        Double sumEntryVolume = 0.0;
        //ALL订单行重量
        Double sumEntryWeight = 0.0;
        //ALL订单行的费用
        Double totalFee = 0.0;
        //遍历订单行,计算订单行总体积
        for (OrderEntryPojo entryPojo : entryPojos) {
            JSONObject product = getProduct(entryPojo.getProduct(), errorList);
            if (CollectionUtils.isNotEmpty(errorList)) {
                logger.info("查询商品数据异常");
                return 0.0;

            } else {
                //订单行总金额
                Double entryFee = CalculateUtil.mul(entryPojo.getQuantity(), entryPojo.getBasePrice());
                totalFee = CalculateUtil.add(totalFee, entryFee);
                if (pointOfService == null || hamllMstMainCarriage == null || subCarriage == null) {
                    String pointOfServiceCode = entryPojo.getPointOfServiceCode();
                    if (StringUtils.isEmpty(pointOfServiceCode)) {
                        errorList.add(entryPojo.getProduct() + "门店信息为空");
                        return 0.0;
                    }
                    pointOfService = getPointOfService(pointOfServiceCode, errorList);
                    if (CollectionUtils.isNotEmpty(errorList)) {
                        return 0.0;

                    }
                    String regionName = pointOfService.getDelCity().substring(0, 2);
                    FndRegionB fndRegionB = regionBService.selectCodeByName(regionName);
                    String regionCode = fndRegionB.getRegionCode();
                    hamllMstMainCarriage = getMaincarriage(order.getCityCode(), regionCode, shippingType, errorList);

                    subCarriage = getSubCarriage(order.getDistrictCode(), regionCode, shippingType, errorList);
                    if (CollectionUtils.isNotEmpty(errorList)) {
                        return 0.0;

                    }

                }
                if (CollectionUtils.isEmpty(errorList)) {
                    String mainPriceModel = hamllMstMainCarriage.getPriceMode();
                    String subPriceModel = subCarriage.getPriceMode();
                    if ("VOLUME".equalsIgnoreCase(mainPriceModel) || "VOLUME".equalsIgnoreCase(subPriceModel)) {
                        Double entryVolume = getEntryVolume(entryPojo, product, errorList);
                        if (CollectionUtils.isNotEmpty(errorList)) {
                            return 0.0;
                        }
                        sumEntryVolume = CalculateUtil.add(sumEntryVolume, entryVolume);
                    }
                    if ("WEIGHT".equalsIgnoreCase(mainPriceModel) || "WEIGHT".equalsIgnoreCase(subPriceModel)) {
                        Double entryWight = 0.0;
                        Double weight = product.getDouble("grossWeight");
                        HmallMstUnit unit = hmallMstUnitService.getUnitByCode(product.getString("weightUnit"));
                        if (unit == null) {
                            logger.info("所选商品" + entryPojo.getProduct() + "缺少Unit数据");
                            errorList.add("所选商品" + entryPojo.getProduct() + "缺少Unit数据");
                            return null;
                        }
                        Double unitRate = unit.getRate();
                        //订单行的包装体积
                        entryWight = CalculateUtil.mul(CalculateUtil.mul(weight, unitRate), entryPojo.getQuantity());
                        sumEntryWeight = CalculateUtil.add(sumEntryWeight, entryWight);
                    }
                } else {
                    return 0.0;
                }
            }
        }

        String mainPriceModel = hamllMstMainCarriage.getPriceMode();
        String subPriceModel = subCarriage.getPriceMode();
        double volumeOrWeight = 0.0;
        if ("WEIGHT".equalsIgnoreCase(mainPriceModel)) {
            volumeOrWeight = sumEntryWeight;
        } else {
            volumeOrWeight = sumEntryVolume;
        }

        //主干体积运费
        Double mainPriceRate = hamllMstMainCarriage.getPriceRate();
        //基础运费
        Double mainBasicExpense = hamllMstMainCarriage.getBasicExpense();
        //差额
        String mainDifference = hamllMstMainCarriage.getDifference();
        //起送运费
        Double mainLeastCarriage = hamllMstMainCarriage.getLeastCarriage();
        //主干运费
        Double mainCarrage = CalculateUtil.add(CalculateUtil.mul(volumeOrWeight, mainPriceRate, 2, "UP"), mainBasicExpense);
        mainCarrage = mainCarrage > mainLeastCarriage ? mainCarrage : mainLeastCarriage;
        Double mainDifferenceFee = CalculateUtil.mul(totalFee, Double.valueOf(mainDifference), 2, "UP");
        mainCarrage = mainCarrage > mainDifferenceFee ? CalculateUtil.sub(mainCarrage, mainDifferenceFee) : 0;


        if ("WEIGHT".equalsIgnoreCase(subPriceModel)) {
            volumeOrWeight = sumEntryWeight;
        } else {
            volumeOrWeight = sumEntryVolume;
        }

        //支干体积运费
        Double subPriceRate = subCarriage.getPriceRate();
        //基础运费
        Double subBasicExpense = subCarriage.getBasicExpense();
        //差额
        String subDifference = subCarriage.getDifference();
        //起送运费
        Double subLeastCarriage = subCarriage.getLeastCarriage();
        //支干运费
        Double subCarrage = CalculateUtil.add(CalculateUtil.mul(volumeOrWeight, subPriceRate), subBasicExpense);
        subCarrage = subCarrage > subLeastCarriage ? subCarrage : subLeastCarriage;
        Double subDifferenceFee = CalculateUtil.mul(totalFee, Double.valueOf(subDifference));
        subCarrage = subCarrage > subDifferenceFee ? CalculateUtil.sub(subCarrage, subDifferenceFee) : 0;

        Double feight = CalculateUtil.add(mainCarrage, subCarrage);
        expressFeight.getAndAdd(CalculateUtil.add(expressFeight.get(), feight));
        return feight;
    }

    /**
     * 单独计算每一行的运费，然后累加
     *
     * @param entryPojo
     * @param order
     * @param shippingType
     * @param expressFeight
     * @param errorList
     * @return
     */
    public Double calShippingFee(OrderEntryPojo entryPojo, OrderPojo order, String shippingType, AtomicDouble expressFeight, List<String> errorList) {
        //门店信息
        MstPointOfService pointOfService = null;
        //主干运费信息
        HamllMstMainCarriage hamllMstMainCarriage = null;
        //支干运费
        HamllMstSubCarriage subCarriage = null;
        //订单行体积
        Double entryVolume = 0.0;
        //订单行重量
        Double entryWeight = 0.0;
        //订单行金额
        Double entryFee = 0.0;
        //遍历订单行,计算订单行总体积
        JSONObject product = getProduct(entryPojo.getProduct(), errorList);
        if (CollectionUtils.isNotEmpty(errorList)) {
            logger.info("查询商品数据异常");
            return 0.0;

        } else {
            //订单行总金额
            entryFee = CalculateUtil.mul(entryPojo.getQuantity(), entryPojo.getBasePrice());
            if (pointOfService == null || hamllMstMainCarriage == null || subCarriage == null) {
                String pointOfServiceCode = entryPojo.getPointOfServiceCode();
                if (StringUtils.isEmpty(pointOfServiceCode)) {
                    errorList.add(entryPojo.getProduct() + "门店信息为空");
                    return 0.0;
                }
                pointOfService = getPointOfService(pointOfServiceCode, errorList);
                if (CollectionUtils.isNotEmpty(errorList)) {
                    return 0.0;

                }
                String regionName = pointOfService.getDelCity().substring(0, 2);
                FndRegionB fndRegionB = regionBService.selectCodeByName(regionName);
                String regionCode = fndRegionB.getRegionCode();
                hamllMstMainCarriage = getMaincarriage(order.getCityCode(), regionCode, shippingType, errorList);

                subCarriage = getSubCarriage(order.getDistrictCode(), regionCode, shippingType, errorList);
                if (CollectionUtils.isNotEmpty(errorList)) {
                    return 0.0;

                }

            }
            if (CollectionUtils.isEmpty(errorList)) {
                String mainPriceModel = hamllMstMainCarriage.getPriceMode();
                String subPriceModel = subCarriage.getPriceMode();
                if ("VOLUME".equalsIgnoreCase(mainPriceModel) || "VOLUME".equalsIgnoreCase(subPriceModel)) {
                    entryVolume = getEntryVolume(entryPojo, product, errorList);
                    if (CollectionUtils.isNotEmpty(errorList)) {
                        return 0.0;
                    }
                }
                if ("WEIGHT".equalsIgnoreCase(mainPriceModel) || "WEIGHT".equalsIgnoreCase(subPriceModel)) {
                    Double weight = product.getDouble("grossWeight");
                    HmallMstUnit unit = hmallMstUnitService.getUnitByCode(product.getString("weightUnit"));
                    if (unit == null) {
                        logger.info("所选商品" + entryPojo.getProduct() + "缺少Unit数据");
                        errorList.add("所选商品" + entryPojo.getProduct() + "缺少Unit数据");
                        return null;
                    }
                    Double unitRate = unit.getRate();
                    //订单行的包装体积
                    entryWeight = CalculateUtil.mul(CalculateUtil.mul(weight, unitRate), entryPojo.getQuantity());
                }
            } else {
                return 0.0;
            }
        }

        String mainPriceModel = hamllMstMainCarriage.getPriceMode();
        String subPriceModel = subCarriage.getPriceMode();
        double volumeOrWeight = 0.0;
        if ("WEIGHT".equalsIgnoreCase(mainPriceModel)) {
            volumeOrWeight = entryWeight;
        } else {
            volumeOrWeight = entryVolume;
        }

        //主干体积运费
        Double mainPriceRate = hamllMstMainCarriage.getPriceRate();
        //基础运费
        Double mainBasicExpense = hamllMstMainCarriage.getBasicExpense();
        //差额
        String mainDifference = hamllMstMainCarriage.getDifference();
        //起送运费
        Double mainLeastCarriage = hamllMstMainCarriage.getLeastCarriage();
        //主干运费
        Double mainCarrage = CalculateUtil.add(CalculateUtil.mul(volumeOrWeight, mainPriceRate, 2, "UP"), mainBasicExpense);
        mainCarrage = mainCarrage > mainLeastCarriage ? mainCarrage : mainLeastCarriage;
        Double mainDifferenceFee = CalculateUtil.mul(entryFee, Double.valueOf(mainDifference), 2, "UP");
        mainCarrage = mainCarrage > mainDifferenceFee ? CalculateUtil.sub(mainCarrage, mainDifferenceFee) : 0;
        entryPojo.setMainCarriage(mainCarrage);

        if ("WEIGHT".equalsIgnoreCase(subPriceModel)) {
            volumeOrWeight = entryWeight;
        } else {
            volumeOrWeight = entryVolume;
        }

        //支干体积运费
        Double subPriceRate = subCarriage.getPriceRate();
        //基础运费
        Double subBasicExpense = subCarriage.getBasicExpense();
        //差额
        String subDifference = subCarriage.getDifference();
        //起送运费
        Double subLeastCarriage = subCarriage.getLeastCarriage();
        //支干运费
        Double subCarrage = CalculateUtil.add(CalculateUtil.mul(volumeOrWeight, subPriceRate), subBasicExpense);
        subCarrage = subCarrage > subLeastCarriage ? subCarrage : subLeastCarriage;
        Double subDifferenceFee = CalculateUtil.mul(entryFee, Double.valueOf(subDifference));
        subCarrage = subCarrage > subDifferenceFee ? CalculateUtil.sub(subCarrage, subDifferenceFee) : 0;
        entryPojo.setSubCarriage(subCarrage);

        Double feight = CalculateUtil.add(mainCarrage, subCarrage);
        expressFeight.getAndAdd(feight);
        return feight;
    }

    /**
     * 获取计算运费所需的商品信息
     *
     * @param productCode
     * @return
     */
    JSONObject getProduct(String productCode, List<String> errorList) {
        //查询商品
        ResponseData productInfo = iProductClientService.selectProductByCode(productCode);
        List productList = productInfo.getResp();
        if (CollectionUtils.isEmpty(productList)) {
            errorList.add(productCode + "对应的商品数据不存在");
            return null;
        }
        Object productObject = productList.get(0);
        JSONObject hmallMstProduct = JSONObject.parseObject(JSON.toJSONString(productObject));
        if (hmallMstProduct == null) {
            errorList.add(productCode + "对应的商品数据不存在");
            return null;
        }
        if (hmallMstProduct.get("packingVolume") == null) {
            errorList.add(productCode + "对应的商品packingVolume数据不存在");
            return null;
        }

        if (hmallMstProduct.get("packageVolUnit") == null) {

            logger.info("所选商品" + productCode + "在缺少packageVolUnit数据,默认维护M3");
            hmallMstProduct.put("packageVolUnit", "M3");
        }
        if (hmallMstProduct.get("grossWeight") == null) {
            errorList.add(productCode + "对应的商品grossWetght数据不存在");
            return null;
        }
        if (hmallMstProduct.get("weightUnit") == null) {
            errorList.add(productCode + "对应的商品weightUnit数据不存在");
            return null;
        }
        if (hmallMstProduct.get("warehouse") == null) {
            errorList.add(productCode + "对应的商品warehouse数据不存在");
            return null;
        }
        return hmallMstProduct;
    }

    /**
     * 根据门店Code获取计算运费所需的门店信息
     *
     * @param code
     */
    protected MstPointOfService getPointOfService(String code, List<String> errList) {

        MstPointOfService mstPointOfService = pointofServiceService.queryByCode(code);
        if (mstPointOfService == null) {
            logger.info("根据" + code + "查询不到门店信息");
            errList.add("根据" + code + "查询不到门店信息");
            return null;
        }
        String regionCode = mstPointOfService.getDelCity();
        if (StringUtils.isEmpty(regionCode)) {
            logger.info("根据" + code + "查询的门店信息缺少DelCity数据");
            errList.add("根据" + code + "查询的门店信息缺少DelCity数据");
        }
        return mstPointOfService;
    }

    /**
     * 计算订单行包装体积
     *
     * @param entryPojo
     * @return
     */
    protected Double getEntryVolume(OrderEntryPojo entryPojo, JSONObject product, List<String> errorList) {
        String productPackSize = entryPojo.getProductPackageSize();
        Double entryvolume = 0.0;
        if (StringUtils.isNotEmpty(productPackSize)) {
            //拆分productPackSize字段,格式为1000X1000X100/2000X990X1101
            String[] packages = productPackSize.split("/");
            Double packingVolume = 0.0;
            for (String packageSize : packages) {
                logger.info("--------packageSize-----" + packageSize);
                String[] values = packageSize.split("\\D+");
                //计算商品包装体积
                Double value = Arrays.stream(values).
                        mapToDouble(s -> CalculateUtil.div(Integer.parseInt(s), 1000d)).reduce((total, p) -> {
                    return CalculateUtil.mul(total, p);
                }).getAsDouble();
                entryvolume = CalculateUtil.add(entryvolume, value);
                logger.info("--------{}-----packingVolume----{}", packageSize, packingVolume);
            }
            //计算订单行总包装体积
            entryvolume = CalculateUtil.mul(packingVolume, entryPojo.getQuantity(), 2, "UP");
        } else {

            //找商品中packingVolume字段
            Double packingVolume = product.getDouble("packingVolume");
            //查询体积unit，找rate字段
            HmallMstUnit unit = hmallMstUnitService.getUnitByCode(product.getString("packageVolUnit"));
            if (unit == null) {
                logger.info("所选商品" + entryPojo.getProduct() + "缺少packageVolUnit数据");
                errorList.add("所选商品" + entryPojo.getProduct() + "缺少packageVolUnit数据");
                return null;
            }
            Double unitRate = unit.getRate();
            //订单行的包装体积
            entryvolume = CalculateUtil.mul(CalculateUtil.mul(packingVolume, unitRate), entryPojo.getQuantity());
        }
        return entryvolume;
    }

    /**
     * 计算快递运费多线程，shippingType传“EXPRESS”
     */
    class CaculateExpressFegiht implements Callable<Double> {
        private List<OrderEntryPojo> entrys;

        private ResponseData responseData;

        private OrderPojo order;

        private String shippingType;

        private int totalQuantity;

        CaculateExpressFegiht(List<OrderEntryPojo> entrys, OrderPojo order, int quantity, String shippingType, ResponseData responseData) {
            this.entrys = entrys;
            this.responseData = responseData;
            this.order = order;
            this.shippingType = shippingType;
            totalQuantity = quantity;
        }

        @Override
        public Double call() throws Exception {
            if (CollectionUtils.isEmpty(entrys)) {
                logger.info("---------------------{}的订单行数量为0-------", shippingType);
                return 0.0;
            }
            try {
                //将订单行按照门店进行分组
                Map map = groupList(entrys);
                AtomicDouble expressFeight = new AtomicDouble(0.0);
                Collection groupEntrys = map.values();
                CountDownLatch countDownLatch = new CountDownLatch(groupEntrys.size());
                Iterator iterator = groupEntrys.iterator();
                List errList = Collections.synchronizedList(new ArrayList<>());
                while (iterator.hasNext()) {
                    List entryPojos = (List<OrderEntryPojo>) iterator.next();
                    //计算相同门店订单行的运费
                    ThreadFactory.getThreadPool().submit(() -> {
                        try {
                            calFreihgtWithPOS(entryPojos, order, shippingType, expressFeight, errList);

                        } catch (Exception e) {
                            errList.add("---计算快递运费异常----" + e.getMessage());
                            logger.error("---计算快递运费异常----", e);
                        } finally {
                            countDownLatch.countDown();
                        }
                    });

                }
                countDownLatch.await();
                if (CollectionUtils.isNotEmpty(errList)) {
                    responseData.setMsg(JSON.toJSONString(errList));
                    responseData.setSuccess(false);
                    return 0.0;
                }
                //计算订单行的数量
                //已分摊运费，将计算好的总运费按订单行上商品数量的比例分摊
                apportionShippingFee(entrys, expressFeight.get(), totalQuantity);

                return expressFeight.get();
            } catch (Exception e) {
                responseData.setSuccess(false);
                responseData.setMsg(e.getMessage());
                e.printStackTrace();
                return 0.0;

            }
        }
    }

    /**
     * 计算物流运费多线程
     */
    class CaculateLogisticFegiht implements Callable<Double> {
        private List<OrderEntryPojo> entrys;

        private ResponseData responseData;

        private OrderPojo order;

        private String shippingType;

        private int totalQuantity;

        CaculateLogisticFegiht(List<OrderEntryPojo> entrys, OrderPojo order, int quantity, String shippingType, ResponseData responseData) {
            this.entrys = entrys;
            this.responseData = responseData;
            this.order = order;
            this.shippingType = shippingType;
            totalQuantity = quantity;
        }

        @Override
        public Double call() throws Exception {
            if (CollectionUtils.isEmpty(entrys)) {
                logger.info("---------------------{}的订单行数量为0-------", shippingType);
                return 0.0;
            }
            try {
                AtomicDouble logisticFeight = new AtomicDouble(0.0);
                CountDownLatch countDownLatch = new CountDownLatch(entrys.size());
                List errList = Collections.synchronizedList(new ArrayList<>());
                for (OrderEntryPojo entry : entrys) {
                    //计算相同门店订单行的运费
                    ThreadFactory.getThreadPool().submit(() -> {
                        try {
                            calShippingFee(entry, order, shippingType, logisticFeight, errList);

                        } catch (Exception e) {
                            logger.error("---计算快递运费异常----", e);
                            errList.add("---计算快递运费异常----" + e.getMessage());
                        } finally {
                            countDownLatch.countDown();
                        }
                    });
                }
                countDownLatch.await();
                if (CollectionUtils.isNotEmpty(errList)) {
                    responseData.setMsg(JSON.toJSONString(errList));
                    responseData.setSuccess(false);
                    return 0.0;
                }
                //计算订单行的数量
                //已分摊运费，将计算好的总运费按订单行上商品数量的比例分摊
                apportionShippingFee(entrys, logisticFeight.get(), totalQuantity);
                return logisticFeight.get();
            } catch (Exception e) {
                responseData.setSuccess(false);
                responseData.setMsg(e.getMessage());
                e.printStackTrace();
                return 0.0;

            }
        }
    }

    /**
     * 获取计算运费所需支线运费
     *
     * @param districeCode
     * @param regionCode
     * @param shippingType
     * @param errorList
     * @return
     */
    HamllMstSubCarriage getSubCarriage(String districeCode, String regionCode, String shippingType, List errorList) {
        HamllMstSubCarriage subCarriage = iSubCarriageService.getSubCarriageByDistinctRegionAndShipType(districeCode, regionCode, shippingType, "Y");
        if (null == subCarriage) {
            errorList.add("districeCode:" + districeCode + "缺少支线运费数据");
            logger.info("districeCode:" + districeCode + "缺少支线费数据");
            return null;
        }
        String priceModel = subCarriage.getPriceMode();
        if (StringUtils.isEmpty(priceModel)) {
            errorList.add("districeCode:" + districeCode + "缺少priceModel数据");
            logger.info("districeCode:" + districeCode + "缺少priceModel数据");
            return null;
        }
        Double priceRate = subCarriage.getPriceRate();
        if (priceRate == null) {
            errorList.add("districeCode:" + districeCode + "缺少priceRate数据");
            logger.info("districeCode:" + districeCode + "缺少priceRate数据");
            return null;
        }
        Double basicExpense = subCarriage.getBasicExpense();
        if (basicExpense == null) {
            errorList.add("districeCode:" + districeCode + "缺少basicExpense数据");
            logger.info("districeCode:" + districeCode + "缺少basicExpense数据");
            return null;
        }
        String difference = subCarriage.getDifference();
        if (StringUtils.isEmpty(difference)) {
            errorList.add("districeCode:" + districeCode + "缺少difference数据");
            logger.info("districeCode:" + districeCode + "缺少difference数据");
            return null;
        }
        Double leastCarriage = subCarriage.getLeastCarriage();
        if (null == leastCarriage) {
            errorList.add("districeCode:" + districeCode + "缺少leastCarriage数据");
            logger.info("districeCode:" + districeCode + "缺少leastCarriage数据");
            return null;
        }
        return subCarriage;
    }

    /**
     * 获取计算运费所需主线运费
     *
     * @param cityCode
     * @param regionCode
     * @param shippingType
     * @param errorList
     * @return
     */
    HamllMstMainCarriage getMaincarriage(String cityCode, String regionCode, String shippingType, List errorList) {
        HamllMstMainCarriage mainCarriage = iMainCarriageService.getMainCarriageByCityRegionAndShipType(cityCode, regionCode, shippingType, "Y");
        if (null == mainCarriage) {
            errorList.add("cityCode:" + cityCode + "缺少支线运费数据");
            logger.info("cityCode:" + cityCode + "缺少支线费数据");
            return null;
        }
        String priceModel = mainCarriage.getPriceMode();
        if (StringUtils.isEmpty(priceModel)) {
            errorList.add("cityCode:" + cityCode + "缺少priceModel数据");
            logger.info("cityCode:" + cityCode + "缺少priceModel数据");
            return null;
        }
        Double priceRate = mainCarriage.getPriceRate();
        if (priceRate == null) {
            errorList.add("cityCode:" + cityCode + "缺少priceRate数据");
            logger.info("cityCode:" + cityCode + "缺少priceRate数据");
            return null;
        }
        Double basicExpense = mainCarriage.getBasicExpense();
        if (basicExpense == null) {
            errorList.add("cityCode:" + cityCode + "缺少basicExpense数据");
            logger.info("cityCode:" + cityCode + "缺少basicExpense数据");
            return null;
        }
        String difference = mainCarriage.getDifference();
        if (StringUtils.isEmpty(difference)) {
            errorList.add("cityCode:" + cityCode + "缺少difference数据");
            logger.info("cityCode:" + cityCode + "缺少difference数据");
            return null;
        }
        Double leastCarriage = mainCarriage.getLeastCarriage();
        if (null == leastCarriage) {
            errorList.add("cityCode:" + cityCode + "缺少leastCarriage数据");
            logger.info("cityCode :" + cityCode + "缺少leastCarriage数据");
            return null;
        }
        return mainCarriage;
    }

    void apportionShippingFee(List<OrderEntryPojo> entrys, Double shippingFees, int totalQuantity) {
        Double useFee = 0.0;
        for (int i = 0; i < entrys.size(); i++) {
            OrderEntryPojo entryPojo = entrys.get(i);
            //最后一行，取余下的运费
            if (i == entrys.size() - 1) {
                entryPojo.setShippingFee(CalculateUtil.sub(shippingFees, useFee));
            } else {
                int quantity = entryPojo.getQuantity();
                double shippingFee = CalculateUtil.mul(shippingFees, CalculateUtil.div(quantity, totalQuantity, 2), 2, "UP");
                useFee = CalculateUtil.add(useFee, shippingFee);
                entryPojo.setShippingFee(shippingFee);
            }

        }
    }

    /**
     * 将订单行basePrice，替换为数据库只能怪查询的订单行金额
     *
     * @param order
     */
    public void getRealPrice(OrderPojo order) {
        //获取订单行实际价格,
        order.getOrderEntryList().forEach(entryPojo -> {
            String customSupportType = entryPojo.getCustomSupportType();
            Double realPrice = 0.0;
            if (StringUtils.isNotEmpty(customSupportType)) {
                //非订制品
                if ("1".equals(customSupportType) || "3".equals(customSupportType)) {
                    ResponseData productPrice = pdClientService.getProductPrice(Integer.parseInt(entryPojo.getProductId()));
                    Map price = (Map) ResponseReturnUtil.getRespObj(productPrice);
                    realPrice = (Double) price.get("basePrice");
                } else if("2".equals(customSupportType)){
                    //霸王券逻辑 订制品
                    realPrice = getHapCalculateSalePrice(entryPojo.getVproduct(),entryPojo.getOdtype());
                } else {
                    realPrice = entryPojo.getBasePrice();
                }
            }
            entryPojo.setPrePrice(entryPojo.getBasePrice());
            entryPojo.setBasePrice(realPrice);
        });
    }


    /**
     * V码获取价格
     * @param vcode
     * @return
     */
    public Double getHapCalculateSalePrice(String vcode,String odtype){
        List<PriceRequestData> priceRequestDataList = new ArrayList<PriceRequestData>();
        PriceRequestData  p = new PriceRequestData();
        p.setvCode(vcode);
        //暂时写死是1 默认订制品  2的超级定制有待解决
        p.setOdtype(odtype);
        priceRequestDataList.add(p);
        ResponseData responseData = hapService.calculateSalePrice(priceRequestDataList);
        Map price = (Map) ResponseReturnUtil.getRespObj(responseData);
        return (Double) price.get("totalPrice");
    }
    /**
     * 处理霸王券的优惠金额
     *
     * @param orderPojo
     */
    @Override
    public void dealExclusiveCoupon(OrderPojo orderPojo) {
        double preOrderAmount = 0;
        //将商品基础价格置为传入的金额
        for (OrderEntryPojo entryPojo : orderPojo.getOrderEntryList()) {
            double temp = entryPojo.getPrePrice();
            entryPojo.setPrePrice(entryPojo.getBasePrice());
            entryPojo.setBasePrice(temp);
            //计算传入的订单总额
            preOrderAmount = ArithUtil.add(preOrderAmount, ArithUtil.mul(entryPojo.getQuantity(), temp));
        }

        Double couponFee = getCouponFee(orderPojo);
        double amountDifference = ArithUtil.sub(orderPojo.getOrderAmount(), preOrderAmount);
        double realCouponFee = ArithUtil.sub(couponFee, amountDifference);
        orderPojo.setCouponFee(realCouponFee);
    }

    /**
     * 获取订单的总优惠金额
     *
     * @param orderPojo
     * @return
     */
    public Double getCouponFee(OrderPojo orderPojo) {
        Double couponFee = orderPojo.getCouponFee();
        //订单头上优惠金额为0或空，则执行的可能为行上的优惠券，将行上优惠金额汇总到头上
        if (couponFee == null || couponFee == 0) {
            for (OrderEntryPojo entryPojo : orderPojo.getOrderEntryList()) {
                couponFee = ArithUtil.add(entryPojo.getCouponFee(), couponFee);
            }
        }
        return couponFee;
    }
}