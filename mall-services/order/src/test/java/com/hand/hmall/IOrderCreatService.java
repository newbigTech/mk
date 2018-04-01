/*
package com.hand.hmall;

import com.hand.hmall.model.HmallOmOrder;
import com.hand.hmall.model.HmallOmOrderEntry;
import com.hand.hmall.model.HmallOmPaymentInfo;
import com.hand.hmall.mapper.HmallOmOrderMapper;
import com.hand.hmall.service.IOrderCreateService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

*/
/**
 * @author 阳赳
 * @version 0.1
 * @name:
 * @Description:
 * @date 2017/6/6 13:32
 *//*

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderApplication.class)
public class IOrderCreatService {
    */
/*{
            "escOrderCode":"T521",
            "orderStatus":"Y",
            "userId":"555",
            "currencyId":"520",
            "websiteId":"520",
            "salechannelId":"520",
            "storeId":"520",
            "invoiceType":"520",
            "receiverName":"520",
            "receiverCountry":"520",
            "receiverState":"520",
            "receiverCity":"520",
            "receiverDistrict":"520",
            "payRate":"520",
            "paymentAmount":"520",
            "shippingType":"PICKUP",
            "estimateDeliveryTime":"2001-07-04T12:08:56.235-07:00",
            "estimateConTime":"2001-07-04T12:08:56.235-07:00",
            "isInvoiced":"N",
            "syncflag":"N",
            "totalcon":"N",
            "entryList":[
        {
            "lineNumber":"520",
                "PIN":"520",
                "estimateDeliveryTime":"2001-07-04T12:08:56.235-07:00",
                "estimateConTime":"2001-07-04T12:08:56.235-07:00",

                "productId":"001",
                "vproductCode":"520",
                "shippingType":"PICKUP",
                "pointOfServiceId":"520",
                "quantity":"1",
                "isGift":"N",
                "syncflag":"N"
        }
        ],
        "payList":[
        {
            "payMode":"wPay",
                "payAmount":"520",
                "payTime":"2001-07-04T12:08:56.235-07:00",
                "numberCode":"520",
                "syncflag":"N"
        }
        ]
    }*//*

    @Autowired
    private IOrderCreateService IOrderCreateService;
    @Autowired
    private HmallOmOrderMapper hmallOmOrderMapper;
*/
/**EscOrderCode  StoreId  WebsiteId  SalechannelId组合起来是判断是否重复的唯一标准 *//*

*/
/**下次需要测试需要更改该组数据*//*

    @Test
    @Rollback
    public void testAddOrder(){
        HmallOmOrder hmallOmOrder =new HmallOmOrder();
        String escOrderCode= "EscOrderCode02";
        String websiteId="WebsiteId02";
        String salechannelId = "SalechannelId02";
        String storeId="StoreId02";
        hmallOmOrder.setEscOrderCode(escOrderCode);
        hmallOmOrder.setOrderStatus("Y");
        short id = 555;
        hmallOmOrder.setUserId(id);
        hmallOmOrder.setCurrencyId("555");
        hmallOmOrder.setWebsiteId(websiteId);
        hmallOmOrder.setSalechannelId(salechannelId);
        hmallOmOrder.setStoreId(storeId);
        hmallOmOrder.setInvoiceType("520");
        hmallOmOrder.setReceiverName("520");
        Short countryId = 520;
        hmallOmOrder.setReceiverCountry(countryId);
        Short cityId = 520;
        hmallOmOrder.setReceiverCity(cityId);
        Short stateId = 520;
        hmallOmOrder.setReceiverState(stateId);
        Short districtId= 520;
        hmallOmOrder.setReceiverDistrict(districtId);
        hmallOmOrder.setPayRate("520");
        Short paymentAmount = 520;
        hmallOmOrder.setPaymentAmount(paymentAmount);
        hmallOmOrder.setShippingType("520");
        hmallOmOrder.setEstimateConTime(new Date());
        hmallOmOrder.setEstimateDeliveryTime(new Date());
        hmallOmOrder.setIsInvoiced("N");
        hmallOmOrder.setSyncflag("N");
        hmallOmOrder.setTotalcon("N");
        //entryList List集合
        List<HmallOmOrderEntry> entryList = new ArrayList<>();
        HmallOmOrderEntry hmallOmOrderEntry = new HmallOmOrderEntry();
        Short lineNumber = 520;
        hmallOmOrderEntry.setLineNumber(lineNumber);
        hmallOmOrderEntry.setPin("520");
        hmallOmOrderEntry.setEstimateDeliveryTime(new Date());
        hmallOmOrderEntry.setEstimateConTime(new Date());
        Short productId = 001;
        hmallOmOrderEntry.setProductId(productId);
        hmallOmOrderEntry.setVproductCode("520");
        hmallOmOrderEntry.setShippingType("PICKUP");
        Short pointOfServiceId = 520;
        hmallOmOrderEntry.setPointOfServiceId(pointOfServiceId);
        Short quantity =1;
        hmallOmOrderEntry.setQuantity(quantity);
        hmallOmOrderEntry.setIsGift("N");
        hmallOmOrderEntry.setSyncflag("N");
        entryList.add(hmallOmOrderEntry);
        hmallOmOrder.setEntryList(entryList);
        //Paylist集合
        HmallOmPaymentInfo hmallOmPaymentInfo = new HmallOmPaymentInfo();
        hmallOmPaymentInfo.setPayMode("GCC");
        hmallOmPaymentInfo.setPayTime(new Date());
        hmallOmPaymentInfo.setNumberCode("520");
        hmallOmPaymentInfo.setSyncflag("N");
        List<HmallOmPaymentInfo> payList = new ArrayList<>();
        payList.add(hmallOmPaymentInfo);
       hmallOmOrder.setPayList(payList);
       IOrderCreateService.addOrder(hmallOmOrder);
       HmallOmOrder hmallOmOrder1= IOrderCreateService.selectByMutiItems(escOrderCode,  websiteId, salechannelId, storeId);
       Boolean bol = hmallOmOrder1.equals(null);
       Assert.assertEquals(false,bol);
    }

    */
/**根据EscOrderCode  StoreId  WebsiteId  SalechannelId组合来查询数据库已有对象*//*

    @Test
    @Rollback
    public void testCreateOrder(){
      String  escOrderCode ="TTTT";
      String websiteId = "137";
      String salechannelId="137";
      String storeId="137";
      HmallOmOrder hmallOmOrder= IOrderCreateService.selectByMutiItems(escOrderCode,  websiteId, salechannelId, storeId);
      Boolean bool = hmallOmOrder.equals(null);
      Assert.assertEquals(false,bool);
    }
    */
/**测试更新方法 利用数据库已有对象 EscOrderCode  StoreId  WebsiteId  SalechannelId与数据库中需要更新保持一致*//*

    */
/**更改CurrencyId验证是否进行更新*//*

    @Test
    @Rollback
    public  void testupdateOrder(){
        HmallOmOrder hmallOmOrder =new HmallOmOrder();
        hmallOmOrder.setEscOrderCode("TTTT");
        hmallOmOrder.setOrderStatus("Y");
        short id = 555;
        hmallOmOrder.setUserId(id);
        */
/**更改CurrencyId测试方法是否生效 并在下方s1处设置相同的值*//*

        hmallOmOrder.setCurrencyId("测试更新");
        hmallOmOrder.setWebsiteId("137");
        hmallOmOrder.setSalechannelId("137");
        hmallOmOrder.setStoreId("137");
        hmallOmOrder.setInvoiceType("测试更新");
        hmallOmOrder.setReceiverName("测试更新");
        Short countryId = 520;
        hmallOmOrder.setReceiverCountry(countryId);
        Short cityId = 520;
        hmallOmOrder.setReceiverCity(cityId);
        Short stateId = 520;
        hmallOmOrder.setReceiverState(stateId);
        Short districtId= 520;
        hmallOmOrder.setReceiverDistrict(districtId);
        hmallOmOrder.setPayRate("520");
        Short paymentAmount = 530;
        hmallOmOrder.setPaymentAmount(paymentAmount);
        hmallOmOrder.setShippingType("520");
        hmallOmOrder.setEstimateConTime(new Date());
        hmallOmOrder.setEstimateDeliveryTime(new Date());
        hmallOmOrder.setIsInvoiced("N");
        hmallOmOrder.setSyncflag("N");
        hmallOmOrder.setTotalcon("N");
        //entryList List集合
        List<HmallOmOrderEntry> entryList = new ArrayList<>();
        HmallOmOrderEntry hmallOmOrderEntry = new HmallOmOrderEntry();
        Short lineNumber = 520;
        hmallOmOrderEntry.setLineNumber(lineNumber);
        hmallOmOrderEntry.setPin("520");
        hmallOmOrderEntry.setEstimateDeliveryTime(new Date());
        hmallOmOrderEntry.setEstimateConTime(new Date());
        Short productId = 001;
        hmallOmOrderEntry.setProductId(productId);
        hmallOmOrderEntry.setVproductCode("520");
        hmallOmOrderEntry.setShippingType("PICKUP");
        Short pointOfServiceId = 520;
        hmallOmOrderEntry.setPointOfServiceId(pointOfServiceId);
        Short quantity =1;
        hmallOmOrderEntry.setQuantity(quantity);
        hmallOmOrderEntry.setIsGift("N");
        hmallOmOrderEntry.setSyncflag("N");
        entryList.add(hmallOmOrderEntry);
        hmallOmOrder.setEntryList(entryList);
        Short orderId =10073 ;
        */
/**这个程序中写入在控制层中*//*

        hmallOmOrder.setOrderId(orderId);
        hmallOmOrder.setCode("JB2017060954636578");
        //Paylist集合
        HmallOmPaymentInfo hmallOmPaymentInfo = new HmallOmPaymentInfo();
        hmallOmPaymentInfo.setPayMode("微信支付测试");
        hmallOmPaymentInfo.setPayTime(new Date());
        hmallOmPaymentInfo.setNumberCode("520");
        hmallOmPaymentInfo.setSyncflag("N");
        List<HmallOmPaymentInfo> payList = new ArrayList<>();
        payList.add(hmallOmPaymentInfo);
        hmallOmOrder.setPayList(payList);
        IOrderCreateService.updateOrder(hmallOmOrder);
        String EscOrderCode ="TTTT";
        String  StoreId ="137";
        String  WebsiteId  ="137";
        String SalechannelId = "137";
        HmallOmOrder hmallOmOrder1 = hmallOmOrderMapper.selectByMutiItems(EscOrderCode,StoreId,WebsiteId,SalechannelId);
        String s = hmallOmOrder1.getCurrencyId();
        */
/**此处与更改的CurrencyId保持一致*//*

        String s1 = "测试更新";
        Assert.assertEquals(s,s1);
    }
}*/
