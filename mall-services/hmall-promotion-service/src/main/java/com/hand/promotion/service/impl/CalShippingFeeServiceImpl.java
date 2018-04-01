package com.hand.promotion.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.AtomicDouble;
import com.hand.dto.ResponseData;
import com.hand.promotion.client.IProductClientService;
import com.hand.promotion.dto.FndRegionB;
import com.hand.promotion.dto.HamllMstMainCarriage;
import com.hand.promotion.dto.HamllMstSubCarriage;
import com.hand.promotion.dto.MstPointOfService;
import com.hand.promotion.model.HmallMstUnit;
import com.hand.promotion.pojo.enums.ShippingType;
import com.hand.promotion.pojo.order.OrderEntryPojo;
import com.hand.promotion.pojo.order.OrderPojo;
import com.hand.promotion.service.*;
import com.hand.promotion.util.DecimalCalculate;
import com.hand.promotion.util.ThreadPoolUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2018/2/6
 * @description 运费计算Service
 */
@Service
public class CalShippingFeeServiceImpl implements ICalShippingFeeService {

    @Resource
    private IProductClientService iProductClientService;
    @Autowired
    private IMainCarriageService iMainCarriageService;
    @Autowired
    private ISubCarriageService iSubCarriageService;
    @Autowired
    private HmallMstUnitService hmallMstUnitService;
    @Autowired
    private IMstPointofServiceService pointofServiceService;
    @Autowired
    private IHmallFndRegionBService regionBService;

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());


    /**
     * 计算快递与物流的运费
     *
     * @param order 要进行运费计算的订单数据
     * @return
     */
    @Override
    public ResponseData caculateFreight(OrderPojo order) {
        long startTime = System.currentTimeMillis();
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
            if (ShippingType.LOGISTICS.getValue().equals(shipType.trim())) {
                //物流
                logisticQuantity += entryPojo.getQuantity();
                logisticEntrys.add(entryPojo);
            } else if (ShippingType.EXPRESS.getValue().equals(shipType.trim())) {
                //快递
                expressQuantity += entryPojo.getQuantity();
                expressEntrys.add(entryPojo);
            }
        }
        //计算快递的运费
        Future<Double> expressThread = ThreadPoolUtil.submit(new CaculateExpressFegiht(expressEntrys, order, expressQuantity, ShippingType.EXPRESS.getValue(), responseData));
        //计算物流的运费
        Future<Double> logisThread = ThreadPoolUtil.submit(new CaculateLogisticFegiht(logisticEntrys, order, logisticQuantity, ShippingType.LOGISTICS.getValue(), responseData));

        Double logisFeight = 0d;
        try {
            Double expressFeight = expressThread.get();
            if (!responseData.isSuccess()) {
                return responseData;
            }
            order.setEpostFee(expressFeight);

            logisFeight = logisThread.get();
            if (!responseData.isSuccess()) {
                return responseData;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        order.setPostFee(logisFeight);
        order.setQuantity(expressQuantity + logisticQuantity);
        logger.info("#####运费计算耗时:{}ms", System.currentTimeMillis() - startTime);
        return responseData;
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
                Double entryFee = DecimalCalculate.mul(entryPojo.getQuantity(), entryPojo.getBasePrice());
                totalFee = DecimalCalculate.add(totalFee, entryFee);
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
                        sumEntryVolume = DecimalCalculate.add(sumEntryVolume, entryVolume);
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
                        entryWight = DecimalCalculate.mul(DecimalCalculate.mul(weight, unitRate), entryPojo.getQuantity());
                        sumEntryWeight = DecimalCalculate.add(sumEntryWeight, entryWight);
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
        Double mainCarrage = DecimalCalculate.add(DecimalCalculate.mul(volumeOrWeight, mainPriceRate, 2, "UP"), mainBasicExpense);
        mainCarrage = mainCarrage > mainLeastCarriage ? mainCarrage : mainLeastCarriage;
        Double mainDifferenceFee = DecimalCalculate.mul(totalFee, Double.valueOf(mainDifference), 2, "UP");
        mainCarrage = mainCarrage > mainDifferenceFee ? DecimalCalculate.sub(mainCarrage, mainDifferenceFee) : 0;


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
        Double subCarrage = DecimalCalculate.add(DecimalCalculate.mul(volumeOrWeight, subPriceRate), subBasicExpense);
        subCarrage = subCarrage > subLeastCarriage ? subCarrage : subLeastCarriage;
        Double subDifferenceFee = DecimalCalculate.mul(totalFee, Double.valueOf(subDifference));
        subCarrage = subCarrage > subDifferenceFee ? DecimalCalculate.sub(subCarrage, subDifferenceFee) : 0;

        Double feight = DecimalCalculate.add(mainCarrage, subCarrage);
        expressFeight.getAndAdd(DecimalCalculate.add(expressFeight.get(), feight));
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
            entryFee = DecimalCalculate.mul(entryPojo.getQuantity(), entryPojo.getBasePrice());
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
                    entryWeight = DecimalCalculate.mul(DecimalCalculate.mul(weight, unitRate), entryPojo.getQuantity());
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
        Double mainCarrage = DecimalCalculate.add(DecimalCalculate.mul(volumeOrWeight, mainPriceRate, 2, "UP"), mainBasicExpense);
        mainCarrage = mainCarrage > mainLeastCarriage ? mainCarrage : mainLeastCarriage;
        Double mainDifferenceFee = DecimalCalculate.mul(entryFee, Double.valueOf(mainDifference), 2, "UP");
        mainCarrage = mainCarrage > mainDifferenceFee ? DecimalCalculate.sub(mainCarrage, mainDifferenceFee) : 0;
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
        Double subCarrage = DecimalCalculate.add(DecimalCalculate.mul(volumeOrWeight, subPriceRate), subBasicExpense);
        subCarrage = subCarrage > subLeastCarriage ? subCarrage : subLeastCarriage;
        Double subDifferenceFee = DecimalCalculate.mul(entryFee, Double.valueOf(subDifference));
        subCarrage = subCarrage > subDifferenceFee ? DecimalCalculate.sub(subCarrage, subDifferenceFee) : 0;
        entryPojo.setSubCarriage(subCarrage);

        Double feight = DecimalCalculate.add(mainCarrage, subCarrage);
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
        com.hand.hmall.dto.ResponseData productInfo = iProductClientService.selectProductByCode(productCode);
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
                        mapToDouble(s -> DecimalCalculate.div(Integer.parseInt(s), 1000d)).reduce((total, p) -> {
                    return DecimalCalculate.mul(total, p);
                }).getAsDouble();
                entryvolume = DecimalCalculate.add(entryvolume, value);
                logger.info("--------{}-----packingVolume----{}", packageSize, packingVolume);
            }
            //计算订单行总包装体积
            entryvolume = DecimalCalculate.mul(packingVolume, entryPojo.getQuantity(), 2, "UP");
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
            entryvolume = DecimalCalculate.mul(DecimalCalculate.mul(packingVolume, unitRate), entryPojo.getQuantity());
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
                    ThreadPoolUtil.submit(() -> {
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
                    ThreadPoolUtil.submit(() -> {
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
            errorList.add("cityCode:" + cityCode + "缺少主线运费数据");
            logger.info("cityCode:" + cityCode + "缺少主线费数据");
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
                entryPojo.setShippingFee(DecimalCalculate.sub(shippingFees, useFee));
            } else {
                int quantity = entryPojo.getQuantity();
                double shippingFee = DecimalCalculate.mul(shippingFees, DecimalCalculate.div(quantity, totalQuantity, 2), 2, "UP");
                useFee = DecimalCalculate.add(useFee, shippingFee);
                entryPojo.setShippingFee(shippingFee);
            }

        }
    }


}
