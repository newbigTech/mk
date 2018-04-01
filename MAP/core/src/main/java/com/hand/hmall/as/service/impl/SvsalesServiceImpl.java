package com.hand.hmall.as.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.mybatis.util.StringUtil;
import com.hand.hap.system.service.ICodeService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.as.dto.Serviceorder;
import com.hand.hmall.as.dto.Svsales;
import com.hand.hmall.as.dto.SvsalesEntry;
import com.hand.hmall.as.dto.SvsalesTemplate;
import com.hand.hmall.as.mapper.ServiceorderMapper;
import com.hand.hmall.as.mapper.SvsaleMapper;
import com.hand.hmall.as.mapper.SvsalesEntryMapper;
import com.hand.hmall.as.service.ISvsaleOrderService;
import com.hand.hmall.as.service.ISvsalesEntryService;
import com.hand.hmall.common.service.ISequenceGenerateService;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.om.dto.Order;
import com.hand.hmall.om.dto.OrderEntry;
import com.hand.hmall.om.dto.Paymentinfo;
import com.hand.hmall.om.mapper.OrderEntryMapper;
import com.hand.hmall.om.mapper.OrderMapper;
import com.hand.hmall.om.mapper.PaymentinfoMapper;
import com.hand.hmall.ws.client.IAsOrderPushClient;
import com.hand.hmall.ws.entities.*;
import com.markor.map.external.pointservice.dto.PointOfServiceDto;
import com.markor.map.external.pointservice.service.IPointOfServiceExternalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * author: zhangzilong
 * name: SvsalesServiceImpl.java
 * discription: 服务销售单Service实现类
 * date: 2017/7/18
 * version: 0.1
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SvsalesServiceImpl extends BaseServiceImpl<Svsales> implements ISvsaleOrderService {

    private static final Logger logger = LoggerFactory.getLogger(SvsalesServiceImpl.class);

    private static final Long FIRST_LINE_NUMBER = 10L; // 第一个的lineNumber
    private static final Long LINE_NUMBER_STEP = 10L; // lineNumber以10递增
    @Autowired
    SvsaleMapper svsaleMapper;
    @Autowired
    ISvsalesEntryService entryService;
    @Autowired
    private ISequenceGenerateService sequenceGenerateService;
    @Autowired
    private PaymentinfoMapper paymentinfoMapper;
    @Autowired
    private SvsalesEntryMapper svsalesEntryMapper;
    @Autowired
    private OrderEntryMapper orderEntryMapper;
    @Autowired
    private IPointOfServiceExternalService iPointOfServiceExternalService;
    @Autowired
    private IAsOrderPushClient orderPushClient;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ServiceorderMapper serviceOrderMapper;
    @Autowired
    private ISvsalesEntryService svsalesEntryService;
    @Autowired
    private SvsalesEntryMapper svsaleMapperEntryMapper;
    @Autowired
    private ICodeService codeService;

    /**
     * 保存方法
     *
     * @param dto
     * @param request
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Svsales saveOrUpdate(Svsales dto, IRequest request) throws Exception {
        //保存服务销售单
        if (dto.getAsSvsalesId() == null || svsaleMapper.selectByPrimaryKey(dto) == null) {
            dto.setCode(sequenceGenerateService.getNextSvCode());
            dto.setAppointmentDate(new Date());
            this.insertSelective(request, dto);
        } else {
            dto.setSyncflag("N");
            this.updateByPrimaryKeySelective(request, dto);
        }
        //保存服务销售单行
        //返回到页面上的单行
        List<Long> list = new ArrayList<>();
        for (SvsalesEntry entry : dto.getSvsalesEntries()) {
            //删除
            if ("delete".equals(entry.get__status())) {
                entryService.deleteByPrimaryKey(entry);
                continue;
            }
            //将新增和修改的单行加入返回到页面的list
            if (StringUtils.isEmpty(entry.getAsSvsalesEntryId())) {
                entry.setSvsalesOrderId(dto.getAsSvsalesId());
                entry.setServiceOrderId(dto.getServiceOrderId());
                entry.setLineNumber(getNextLineNumber(dto));
                entry = entryService.insertSelective(request, entry);
            } else {
                entry = entryService.updateByPrimaryKey(request, entry);
            }
            list.add(entry.getAsSvsalesEntryId());
        }
        List<SvsalesEntry> entries = entryService.querySvsalesEntriesInfo(request, dto, 1, 10);
        dto = queryBySvsalesId(dto.getAsSvsalesId(), request).get(0);
        dto.setSvsalesEntries(entries);
        return dto;
    }


    @Override
    public List<Svsales> queryBySvsalesId(Long asSvsalesId, IRequest iRequest) {
        return svsaleMapper.selectSvsalesById(asSvsalesId);
    }

    @Override
    public Svsales newSvsale(Long serviceOrderId, IRequest iRequest) {
        return svsaleMapper.queryForNewSvsale(serviceOrderId);
    }

    /**
     * 服务销售单支付信息接收并保存
     *
     * @param dto
     * @return
     */
    @Override
    public void saveSvsalesPaymentinfo(Paymentinfo dto) throws RuntimeException {
        Svsales svsale = new Svsales();
        svsale.setCode(dto.getOutTradeNo());
        svsale = svsaleMapper.queryByCode(svsale);
        if (svsale.getAsSvsalesId() == null) {
            throw new RuntimeException("没有对应的服务销售单。");
        }
        svsale.setRealPay(dto.getTotalAmount());
        dto.setOrderId(svsale.getAsSvsalesId());
        try {
            paymentinfoMapper.insertSvsalesPaymentInfo(dto);
            svsaleMapper.updatePayStatus(svsale);
        } catch (Exception e) {
            logger.error(this.getClass().getName() + ".saveSvsalesPaymentinfo", e);
            throw new RuntimeException("持久层异常。");
        }
    }

    @Override
    public ResponseData sendToRetail(Long asSvsalesId, IRequest iRequest) {
        //查出数据
        Svsales svsales = svsaleMapper.queryForRetail(asSvsalesId);
        //准备结果
        ResponseData responseData = new ResponseData();
        if (StringUtils.isEmpty(svsales.getSapCode())) {
            try {
                Boolean flag = true;
                String errMessage = "";
                String sapCode = sequenceGenerateService.getNextNumber("sapCode", 9L, "D");
                AsCreateResponseBody responseBody = orderPushClient.orderPush(createRequestConstructor(svsales, iRequest, sapCode));
                List<AsCreateGdtReturnItems> items = responseBody.getAsCreateGdtReturn().getItems();
                String returnMessage = "";
                for (AsCreateGdtReturnItems returnItem : items) {
                    if ("E".equals(returnItem.getType())) {
                        flag = false;
                        errMessage += returnItem.getMessage() + "\n";
                    }
                    returnMessage = returnMessage + " \n" + returnItem.getMessage();
                }
                if (flag) {
                    //更新标识位和SAP_CODE
                    svsales.setSapCode(sapCode);
                    svsaleMapper.updateSyncFlag(svsales);
                    responseData.setSuccess(true);
                    responseData.setMsgCode(sapCode);
                } else {

                    responseData.setSuccess(false);
                }
                responseData.setMsg(flag ? returnMessage : errMessage);
            } catch (Exception e) {
                logger.error(this.getClass().getName(), e);
                responseData.setSuccess(false);
                responseData.setMsg("同步过程中发生异常。");
            }
        } else {
            String sapCode = svsales.getSapCode();
            try {
                Boolean flag = true;
                String errMessage = "";
                AsChangeResponseBody responseBody = orderPushClient.orderUpdate(changeRequestConstructor(svsales, iRequest));
                List<AsChangeGdtReturnItems> items = responseBody.getAsChangeGdtReturn().getAsChangeGdtReturnItems();
                String returnMessage = "";
                for (AsChangeGdtReturnItems returnItem : items) {
                    if ("E".equals(returnItem.getType())) {
                        flag = false;
                        errMessage += returnItem.getMsg() + "\n";
                    }
                    returnMessage = returnMessage + " \n" + returnItem.getMsg();
                }
                if (flag) {
                    svsaleMapper.updateSyncFlag(svsales);
                }
                responseData.setSuccess(flag);
                responseData.setMsgCode(sapCode);
                responseData.setMsg(flag ? returnMessage : errMessage);
            } catch (Exception e) {
                logger.error(this.getClass().getName(), e);
                responseData.setSuccess(false);
                responseData.setMsgCode(sapCode);
                responseData.setMsg("同步过程中发生异常。");
            }
        }

        return responseData;
    }


    /**
     * 拼装创建用的requestBody
     *
     * @param svsales
     * @param iRequest
     * @return
     */
    private AsCreateRequestBody createRequestConstructor(Svsales svsales, IRequest iRequest, String sapCode) throws Exception {
        AsCreateRequestBody requestBody = new AsCreateRequestBody();
        //拼装LV_SO
        requestBody.setLvSo(sapCode);
        //拼装Header
        AsCreateGdtHeader header = new AsCreateGdtHeader();
        header.setAuart("S013");
        header.setVkorg("0201");
        header.setVtweg("80");
        header.setSpart("20");
        header.setAugru("");
        header.setVsbed("D");
        header.setAutlf("X");
        header.setLifsk("99");
        header.setKunnr1(svsales.getSoldParty());
        header.setKunnr2("ONE");
        header.setVkbur(svsales.getSalesOffice());
        header.setName1(svsales.getName());
        header.setTitle(svsales.getSex());
        header.setTranspZone(svsales.getReceiverDistrict());
        header.setStreet(svsales.getAddress());
        header.setTelephone(svsales.getMobile());
        header.setVdatu(svsales.getAppointmentDateString());
        OrderEntry entry = new OrderEntry();
        entry.setOrderId(svsales.getOrderId());
        List<OrderEntry> orderEntryList = orderEntryMapper.queryInfo(entry);
        PointOfServiceDto pointOfServiceDto = new PointOfServiceDto();
        pointOfServiceDto.setPointOfServiceId(orderEntryList.get(0).getPointOfServiceId());
        header.setWerks(iPointOfServiceExternalService.selectByPrimaryKey(pointOfServiceDto).getCode());
        header.setOrderNote(svsales.getNote());
        header.setZzclubid(svsales.getCustomerId());
        header.setStoriesNo(svsales.getCode());
        header.setZzglqhd("");
        header.setZzhopeday(svsales.getAppointmentDateString());
        header.setZzyydat(svsales.getAppointmentDateString());
        header.setZy021(svsales.getResponsibleParty());
        header.setExord(svsales.getEscOrderCode());
        header.setExsys("MAP");
        header.setExnid(iRequest.getUserId().toString());
        header.setExnam(iRequest.getUserName());
        header.setExdat(svsales.getCreationDateString());
        header.setExtim(svsales.getCreationTimeString());
        header.setExnidc("");
        header.setExnamc("");
        header.setExdatc("");
        header.setExtimc("");
        requestBody.setAsCreateGdtHeader(header);
        //拼装单行和价格行
        AsGdtCondtions condtions = new AsGdtCondtions();
        AsGdtItems items = new AsGdtItems();
        List<AsGdtCondtionItem> condtionItems = new ArrayList<>();
        List<AsGdtItemsItem> itemsItems = new ArrayList<>();
        setEntryParams(svsales, condtionItems, itemsItems);
        condtions.setItems(condtionItems);
        items.setItems(itemsItems);
        requestBody.setAsGdtCondtions(condtions);
        requestBody.setAsGdtItems(items);
        //拼装return
        AsCreateGdtReturn gdtReturn = new AsCreateGdtReturn();
        ArrayList<AsCreateGdtReturnItems> returnItems = new ArrayList<>();
        AsCreateGdtReturnItems returnItem = new AsCreateGdtReturnItems();
        returnItems.add(returnItem);
        gdtReturn.setItems(returnItems);
        requestBody.setAsCreateGdtReturn(gdtReturn);
        return requestBody;
    }

    private void setEntryParams(Svsales svsales, List<AsGdtCondtionItem> condtionItems, List<AsGdtItemsItem> itemsItems) throws Exception {
        for (SvsalesEntry entry : svsales.getSvsalesEntries()) {
            AsGdtCondtionItem condtionItem = new AsGdtCondtionItem();
            condtionItem.setPosnr(entry.getLineNumber() + "");
            condtionItem.setKschl("ZP00");
            condtionItem.setKbetr(entry.getTotalFee().toString());
            condtionItem.setCurrency("RMB");
            condtionItems.add(condtionItem);
            AsGdtItemsItem item = new AsGdtItemsItem();
            item.setPosnr(entry.getLineNumber() + "");
            item.setUepos("");
            item.setPstyv("Z010");
            item.setMatnr(entry.getRetailProductCode());
            item.setKwmeng(entry.getQuantity().toString());
            item.setVrkme("PC");
            item.setZzxmbz(entry.getNote());
            item.setLgort("9010");
            item.setZy04("");
            item.setSdabw("");
            item.setAbgru("");
            OrderEntry orderEntry = new OrderEntry();
            orderEntry.setOrderId(svsales.getOrderId());
            List<OrderEntry> orderEntryList = orderEntryMapper.queryInfo(orderEntry);
            PointOfServiceDto pointOfServiceDto = new PointOfServiceDto();
            pointOfServiceDto.setPointOfServiceId(orderEntryList.get(0).getPointOfServiceId());
            item.setZzwerks(iPointOfServiceExternalService.selectByPrimaryKey(pointOfServiceDto).getCode());
            item.setVgbel("");
            item.setVgpos("");
            item.setZzthreason1("");
            item.setZzthreason2("");
            item.setZy010("");
            item.setZy09("");
            itemsItems.add(item);
        }
    }


    /**
     * 拼装更新用的requestBody
     *
     * @param svsales
     * @param iRequest
     * @return
     */
    private AsChangeRequestBody changeRequestConstructor(Svsales svsales, IRequest iRequest) throws Exception {
        AsChangeRequestBody requestBody = new AsChangeRequestBody();
        //拼装Header
        AsChangeGdtHeader header = new AsChangeGdtHeader();
        header.setVbeln(svsales.getSapCode());
        header.setAuart("S013");
        header.setVkorg("0201");
        header.setVtweg("80");
        header.setSpart("20");
        header.setAugru("");
        header.setVsbed("D");
        header.setAutlf("X");
        header.setLifsk("99");
        header.setKunnr1(svsales.getSoldParty());
        header.setKunnr2("ONE");
        header.setVkbur(svsales.getSalesOffice());
        header.setName1(svsales.getName());
        header.setTitle(svsales.getSex());
        header.setTranspZone(svsales.getReceiverDistrict());
        header.setStreet(svsales.getAddress());
        header.setTelephone(svsales.getMobile());
        header.setVdatu(svsales.getAppointmentDateString());
        OrderEntry entry = new OrderEntry();
        entry.setOrderId(svsales.getOrderId());
        List<OrderEntry> orderEntryList = orderEntryMapper.queryInfo(entry);
        PointOfServiceDto pointOfServiceDto = new PointOfServiceDto();
        pointOfServiceDto.setPointOfServiceId(orderEntryList.get(0).getPointOfServiceId());
        header.setWerks(iPointOfServiceExternalService.selectByPrimaryKey(pointOfServiceDto).getCode());
        header.setOrderNote(svsales.getNote());
        header.setZzclubid(svsales.getCustomerId());
        header.setStoriesNo(svsales.getCode());
        header.setZzglqhd("");
        header.setZzhopeday(svsales.getAppointmentDateString());
        header.setZzyydat(svsales.getAppointmentDateString());
        header.setZy021(svsales.getResponsibleParty());
        header.setExord(svsales.getEscOrderCode());
        header.setExsys("MAP");
        header.setExnid("");
        header.setExnam("");
        header.setExdat("");
        header.setExtim("");
        header.setExnidc(iRequest.getUserId().toString());
        header.setExnamc(iRequest.getUserName());
        header.setExdatc(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        header.setExtimc(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        requestBody.setAsChangeGdtHeader(header);
        //拼装单行和价格行
        AsGdtCondtions condtions = new AsGdtCondtions();
        AsGdtItems items = new AsGdtItems();
        List<AsGdtCondtionItem> condtionItems = new ArrayList<>();
        List<AsGdtItemsItem> itemsItems = new ArrayList<>();
        setEntryParams(svsales, condtionItems, itemsItems);
        condtions.setItems(condtionItems);
        items.setItems(itemsItems);
        requestBody.setAsGdtCondtions(condtions);
        requestBody.setAsGdtItems(items);
        //拼装return
        AsChangeGdtReturn gdtReturn = new AsChangeGdtReturn();
        ArrayList<AsChangeGdtReturnItems> returnItems = new ArrayList<>();
        AsChangeGdtReturnItems returnItem = new AsChangeGdtReturnItems();
        returnItems.add(returnItem);
        gdtReturn.setAsChangeGdtReturnItems(returnItems);
        requestBody.setAsChangeGdtReturn(gdtReturn);
        return requestBody;
    }

    public ResponseData updateStatusToProc(Svsales dto) {
        ResponseData rpd = new ResponseData();
        try {
            svsaleMapper.updateStatusToProc(dto);
            rpd.setSuccess(true);
        } catch (Exception e) {
            rpd.setSuccess(false);
        }
        return rpd;
    }

    @Override
    public ResponseData updateStatusToCanc(Svsales svsales) {
        ResponseData rpd = new ResponseData();
        try {
            svsaleMapper.updateStatusToCanc(svsales);
            rpd.setSuccess(true);
        } catch (Exception e) {
            rpd.setSuccess(false);
        }
        return rpd;
    }

    @Override
    public com.hand.hap.system.dto.ResponseData importSvsalesAndSvsalesEntry(List<SvsalesTemplate> infos, IRequest iRequest, boolean importResult, String message) throws Exception {
        //用于存放已经插入的服务销售单头信息
        List<Svsales> saveList = new ArrayList<Svsales>();
        List<SvsalesTemplate> subList = null;
        Svsales asSvsales = null;
        boolean insertFlag;
        for (int i = 0; i < infos.size(); i++) {
            insertFlag = true;
            //EXCEL表格服务销售单序号不能为空
            String svsalesNum = infos.get(i).getSvsalesNum();
            if (StringUtil.isEmpty(svsalesNum)) {
                message = " 第" + (i + 1) + "条数据服务销售单序号不能为空";
                throw new Exception(message);
            }
            //EXCEL表格TM订单编号不能为空
            String tmOrderCode = infos.get(i).getTmOrderCode();
            if (StringUtil.isEmpty(tmOrderCode)) {
                message = " 第" + (i + 1) + "条数据TM订单编号不能为空";
                throw new Exception(message);
            }
            //Excel表格服务单单号不能为空
            String serviceOrderCode = infos.get(i).getServiceOrderCode();
            if (StringUtil.isEmpty(serviceOrderCode)) {
                message = " 第" + (i + 1) + "条数据服务单单号不能为空";
                throw new Exception(message);
            }
            //Excel表格实付金额合计不能为空
            Double realPaySum = infos.get(i).getRealPaySum();
            if (realPaySum == null) {
                message = " 第" + (i + 1) + "条数据实付金额合计不能为空";
                throw new Exception(message);
            }
            //Excel表格受理客服不能为空
            String cs = infos.get(i).getCs();
            if (StringUtil.isEmpty(cs)) {
                message = " 第" + (i + 1) + "条数据受理客服不能为空";
                throw new Exception(message);
            }
            //Excel表格完结时间不能为空
            Date finishTime = infos.get(i).getFinishTime();
            if (finishTime == null) {
                message = " 第" + (i + 1) + "条数据完结时间格式错误";
                throw new Exception(message);
            }
            //Excel表格收费项目不能为空
            String payProject = infos.get(i).getPayProject();
            if (StringUtil.isEmpty(payProject)) {
                message = " 第" + (i + 1) + "条数据收费项目不能为空";
                throw new Exception(message);
            }
            //Excel表格实际价格不能为空
            Double amtPrice = infos.get(i).getAmtPrice();
            if (amtPrice == null) {
                message = " 第" + (i + 1) + "条数据实际价格不能为空";
                throw new Exception(message);
            }

            //Excel表格TM订单编号对应的订单数据必须存在
            List<Order> orderList = orderMapper.selectInfoByEscOrderCodeAndWebsiteId(infos.get(i).getTmOrderCode().replace(",", ""));
            if (orderList.size() == 0) {
                message = " 第" + (i + 1) + "条数据TM订单编号对应的订单数据不存在";
                throw new Exception(message);
            }
            Serviceorder serviceorder = new Serviceorder();
            serviceorder.setCode(serviceOrderCode);
            //Excel表格服务单单号对应的服务单数据必须存在
            List<Serviceorder> serviceOrderList = serviceOrderMapper.selectServiceOrderByCode(serviceorder);
            if (serviceOrderList.size() == 0) {
                message = " 第" + (i + 1) + "条数据服务单单号对应的服务单数据不存在";
                throw new Exception(message);
            }

            Svsales svsales = new Svsales();
            String code = sequenceGenerateService.getNextSvCode();
            svsales.setCode(code);
            svsales.setStatus("FINI");
            svsales.setOrderId(orderList.get(0).getOrderId());
            svsales.setServiceOrderId(serviceOrderList.get(0).getServiceOrderId());
            svsales.setNote(infos.get(i).getNote());
            svsales.setName(serviceOrderList.get(0).getName());
            svsales.setMobile(serviceOrderList.get(0).getMobile());
            svsales.setAddress(serviceOrderList.get(0).getAddress());
            svsales.setRealPay(new BigDecimal(infos.get(i).getRealPaySum()));
            svsales.setCs(infos.get(i).getCs());

            svsales.setFinishTime(finishTime);
            svsales.setSyncflag("N");
            svsales.setPayStatus("Y");
            svsales.setAppointmentDate(finishTime);
            if (i > 0) {
                subList = infos.subList(0, i);
                for (SvsalesTemplate svsalesTemplate : subList) {
                    //如果后面的行和前面的数据服务销售单序号相同,校验B-G字段必须相同
                    if (svsalesTemplate.getSvsalesNum().equals(infos.get(i).getSvsalesNum())) {
                        String subTmOrderCode = svsalesTemplate.getTmOrderCode();
                        String subServiceOrderCode = svsalesTemplate.getServiceOrderCode();
                        String subNote = svsalesTemplate.getNote();
                        String note = infos.get(i).getNote();
                        Double subRealPaySum = svsalesTemplate.getRealPaySum();
                        String subCs = svsalesTemplate.getCs();
                        Date subFinishTime = svsalesTemplate.getFinishTime();
                        if (!subTmOrderCode.equals(tmOrderCode) || !subServiceOrderCode.equals(serviceOrderCode) || !subRealPaySum.equals(realPaySum) || !subNote.equals(note) || !subCs.equals(cs) || !subFinishTime.equals(finishTime)) {
                            //如果服务销售单序号相同的表记录中B-G列的数据不一致,导入报错
                            throw new Exception("服务销售单序号为" + infos.get(i).getSvsalesNum() + "的表记录B-G列值必须相同");
                        } else {
                            //如果服务销售单序号一致，且服务销售单序号相同的表记录中B-G列的数据一致，则不插入新的服务销售单头
                            for (Svsales saveSvsales : saveList) {
                                svsales = new Svsales();
                                svsales.setAsSvsalesId(saveSvsales.getAsSvsalesId());
                            }
                        }
                        insertFlag = false;
                    }
                }
                if (insertFlag == true) {
                    asSvsales = this.insertSelective(iRequest, svsales);
                    //将插入销售赔付单头对应的额销售赔付单序号存入saveList中
                    asSvsales.setSvsalesNum(infos.get(i).getSvsalesNum());
                    saveList.add(asSvsales);
                }
            } else {
                asSvsales = this.insertSelective(iRequest, svsales);
                //将插入销售赔付单头对应的额销售赔付单序号存入saveList中
                asSvsales.setSvsalesNum(infos.get(i).getSvsalesNum());
                saveList.add(asSvsales);
            }
            SvsalesEntry svsalesEntry = new SvsalesEntry();
            svsalesEntry.setSvsalesOrderId(svsales.getAsSvsalesId());
            svsalesEntry.setLineNumber(getNextLineNumber(svsales));
            svsalesEntry.setQuantity(Long.valueOf("1"));
            svsalesEntry.setChargeType(infos.get(i).getPayProject());
            svsalesEntry.setUnitFee(new BigDecimal(infos.get(i).getAmtPrice()));
            //订单行应付金额等于【Unit_FEE】*【QUANTITY】
            svsalesEntry.setTotalFee(svsalesEntry.getUnitFee().multiply(new BigDecimal(svsalesEntry.getQuantity())));
            svsalesEntry.setNote(infos.get(i).getLineNote());
            SvsalesEntry asSvsalesEntry = svsalesEntryService.insertSelective(iRequest, svsalesEntry);
            //将服务销售单中的应付金额合计赋值(等于该服务销售单下的行总价【SVSALES_ENTRY.TOTAL_FEE】之和)
            //服务销售单头的should_pay字段等于该服务销售单下的行总价【SVSALES_ENTRY.TOTAL_FEE】之和
            SvsalesEntry svsalesEntryFor = new SvsalesEntry();
            svsalesEntryFor.setSvsalesOrderId(asSvsalesEntry.getSvsalesOrderId());
            List<SvsalesEntry> svsalesEntrys = svsaleMapperEntryMapper.select(svsalesEntryFor);
            Svsales svsalesInsert = new Svsales();
            if (svsalesEntrys.size() != 0) {
                BigDecimal addShouldPay = new BigDecimal(0);
                for (SvsalesEntry entry : svsalesEntrys) {
                    BigDecimal shouldPay = entry.getTotalFee();
                    addShouldPay = addShouldPay.add(shouldPay);
                }
                svsalesInsert.setShouldPay(addShouldPay);
                svsalesInsert.setAsSvsalesId(asSvsales.getAsSvsalesId());
            }
            this.updateByPrimaryKeySelective(iRequest, svsalesInsert);
            importResult = true;
        }
        com.hand.hap.system.dto.ResponseData responseData = new com.hand.hap.system.dto.ResponseData(importResult);
        responseData.setMessage(message);
        return responseData;
    }

    @Override
    public List<Svsales> querySvsales(IRequest requestContext, String code, String serviceOrderCode, String escOrderCode, String sapCode, String customerId, String mobileNumber, String payStatus, String syncflag, String[] svsaleStatus, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<Svsales> svsales = svsaleMapper.querySvsales(code, serviceOrderCode, escOrderCode, sapCode, customerId, mobileNumber, payStatus, syncflag, svsaleStatus);
        return svsales;
    }

    @Override
    public List<Svsales> querySvsales(IRequest requestContext, String code, String serviceOrderCode, String escOrderCode, String sapCode, String customerId, String mobile, String payStatus, String syncflag, String[] svsaleStatus) {
        List<Svsales> svsales = svsaleMapper.querySvsales(code, serviceOrderCode, escOrderCode, sapCode, customerId, mobile, payStatus, syncflag, svsaleStatus);
        for (Svsales svsale : svsales) {
            if (!StringUtil.isEmpty(svsale.getChargeType())) {
                String chargeType = codeService.getCodeMeaningByValue(requestContext, "HMALL.AS.CHARGE_TYPE", svsale.getChargeType());
                svsale.setChargeType(chargeType);
            }
        }
        return svsales;
    }


    /**
     * 获取下一个服务销售单行项目号
     *
     * @param dto
     * @return
     */
    private Long getNextLineNumber(Svsales dto) {
        SvsalesEntry svsalesEntry = new SvsalesEntry();
        svsalesEntry.setSvsalesOrderId(dto.getAsSvsalesId());
        List<SvsalesEntry> svsalesEntryList = svsaleMapperEntryMapper.select(svsalesEntry);
        if (CollectionUtils.isEmpty(svsalesEntryList)) {
            return FIRST_LINE_NUMBER;
        } else {
            SvsalesEntry max = svsalesEntryList.stream().max((svsalesEntry1, svsalesEntry2)
                    -> svsalesEntry1.getLineNumber().compareTo(svsalesEntry2.getLineNumber())).get();
            return Long.valueOf(max.getLineNumber()) + LINE_NUMBER_STEP;
        }
    }
}
