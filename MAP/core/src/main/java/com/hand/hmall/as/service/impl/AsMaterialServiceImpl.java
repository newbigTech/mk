package com.hand.hmall.as.service.impl;
import com.github.pagehelper.PageHelper;
import com.markor.map.framework.soapclient.exceptions.WSCallException;
import com.hand.hmall.ws.client.IAsOrderPushClient;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.ServiceRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.ICodeService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.as.dto.AsMaterial;
import com.hand.hmall.as.dto.AsMaterialEntry;
import com.hand.hmall.as.mapper.AsMaterialEntryMapper;
import com.hand.hmall.as.mapper.AsMaterialMapper;
import com.hand.hmall.as.service.IAsMaterialService;
import com.hand.hmall.common.service.ISequenceGenerateService;
import com.hand.hmall.log.dto.LogManager;
import com.hand.hmall.log.service.ILogManagerService;
import com.hand.hmall.mst.dto.Product;
import com.hand.hmall.mst.mapper.MstUserMapper;
import com.hand.hmall.mst.mapper.ProductMapper;
import com.hand.hmall.util.Constants;
import com.hand.hmall.util.DateUtil;
import com.hand.hmall.util.StringUtils;
import com.hand.hmall.ws.entities.*;
import com.markor.map.external.pointservice.dto.PointOfServiceDto;
import com.markor.map.external.pointservice.service.IPointOfServiceExternalService;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class AsMaterialServiceImpl extends BaseServiceImpl<AsMaterial> implements IAsMaterialService {

    private static final Long FIRST_LINE_NUMBER = 10L; // 第一个订单行的lineNumber
    private static final Long LINE_NUMBER_STEP = 10L; // lineNumber以10递增
    @Autowired
    IAsOrderPushClient client;
    @Autowired
    AsMaterialMapper asMaterialMapper;
    //@Autowired
    // private IAsMaterialEntryService asMaterialEntryService;
    @Autowired
    ILogManagerService logManagerService;
    @Autowired
    private ISequenceGenerateService sequenceGenerateService;
    @Autowired
    private IPointOfServiceExternalService iPointOfServiceExternalService;
    @Autowired
    private AsMaterialEntryMapper asMaterialEntryMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private MstUserMapper userMapper;
    @Autowired
    private ICodeService codeService;


    @Override
    public ResponseData materialSyncRetail(IRequest iRequest, AsMaterial asMaterial) {
        ResponseData rdp = new ResponseData();
        AsMaterial material = asMaterialMapper.getMaterialForRetail(asMaterial);
        LogManager log = new LogManager();
        log.setStartTime(new Date());
        log.setDataPrimaryKey(material.getId());
        log.setProgramName(this.getClass().getName());
        log.setSourcePlatform(Constants.SOURCE_PLATFORM_ORDER_JOB);
        String sapCode = material.getSapCode();
        if (StringUtils.isEmpty(sapCode)) {
            log.setProgramDescription("物耗单推送Retail-新增");
        } else {
            log.setProgramDescription("物耗单推送Retail-修改");
        }
        log = logManagerService.logBegin(iRequest, log);
        if (StringUtils.isEmpty(sapCode)) {
            AsCreateRequestBody orderRequestBody = getBodyForOrderToRetail(iRequest, material);
            sapCode = sequenceGenerateService.getNextNumber("sapCode", 9L, "D");
          /*  char[] stringArr = sapCode.toCharArray();
            String str = String.valueOf(stringArr[1]);
            int i = Integer.parseInt(str);
            i = i + 68;
            StringBuffer sb = new StringBuffer(sapCode);
            sb.replace(1, 2, String.valueOf((char) i));
            sapCode = String.valueOf(sb);*/
            orderRequestBody.setLvSo(sapCode);
            log.setMessage(JSONObject.fromObject(orderRequestBody).toString());
            try {
                AsCreateResponseBody res = client.orderPush(orderRequestBody);

                List<AsCreateGdtReturnItems> result = res.getAsCreateGdtReturn().getItems();
                Boolean flag = true;
                String errMessage = "";
                if (CollectionUtils.isNotEmpty(result)) {

                    String returnMessage = "SapCode:" + res.getGdeVbeln();
                    for (AsCreateGdtReturnItems returnItem : result) {
                        if ("E".equals(returnItem.getType())) {
                            flag = false;
                            errMessage += returnItem.getMessage() + "\n";
                        }
                        returnMessage = returnMessage + " 返回信息：" + returnItem.getMessage() + ";";
                    }
                    log.setProcessStatus(flag ? "S" : "E");
                    log.setReturnMessage(returnMessage);
                    if (flag) {
                        AsMaterial dto = new AsMaterial();
                        dto.setId(asMaterial.getId());
                        dto.setSapCode(sapCode);
                        dto.setSyncRetail(Constants.YES);
                        this.updateByPrimaryKeySelective(iRequest, dto);
                    }
                }

                logManagerService.logEnd(iRequest, log);
                if (flag) {
                    rdp.setSuccess(true);
                    rdp.setCode(sapCode);
                    return rdp;
                } else {
                    rdp.setSuccess(false);
                    rdp.setMessage(errMessage);
                    return rdp;
                }

            } catch (WSCallException e) {
                log.setProcessStatus("E");
                log.setReturnMessage(e.getMessage());
                logManagerService.logEnd(iRequest, log);
                rdp.setSuccess(false);
                rdp.setMessage(e.getMessage());
                return rdp;
            }
        } else {
            AsChangeRequestBody asChangeRequestBody = getUpdateBodyForOrderRetail(iRequest, material);
            log.setMessage(JSONObject.fromObject(asChangeRequestBody).toString());
            try {
                AsChangeResponseBody responseBody = client.orderUpdate(asChangeRequestBody);
                List<AsChangeGdtReturnItems> result = responseBody.getAsChangeGdtReturn().getAsChangeGdtReturnItems();
                Boolean flag = true;
                String errMessage = "";
                if (CollectionUtils.isNotEmpty(result)) {

                    String returnMessage = "SapCode:" + material.getSapCode();
                    for (AsChangeGdtReturnItems returnItem : result) {
                        if ("E".equals(returnItem.getType1()) || "E".equals(returnItem.getType())) {
                            errMessage += returnItem.getMsg() + "\n";
                            flag = false;
                        }
                        returnMessage = returnMessage + " 返回信息：" + returnItem.getMsg() + ";";
                    }
                    log.setProcessStatus(flag ? "S" : "E");
                    log.setReturnMessage(returnMessage);
                    if (flag) {
                        AsMaterial dto = new AsMaterial();
                        dto.setId(asMaterial.getId());
                        dto.setSyncRetail(Constants.YES);
                        this.updateByPrimaryKeySelective(iRequest, dto);
                    }
                }

                logManagerService.logEnd(iRequest, log);
                if (flag) {
                    rdp.setSuccess(true);
                    rdp.setCode(sapCode);
                    return rdp;
                } else {
                    rdp.setMessage(errMessage);
                    rdp.setSuccess(false);
                    return rdp;
                }
            } catch (WSCallException e) {
                log.setReturnMessage(e.getMessage());
                log.setProcessStatus("E");
                logManagerService.logEnd(iRequest, log);
                rdp.setSuccess(false);
                rdp.setMessage(e.getMessage());
                return rdp;
            }
        }


    }

    @Override
    @Transactional
    public ResponseData saveMaterialOrder(IRequest iRequest, AsMaterial asMaterial) {
        ResponseData responseData = new ResponseData();
        try {
            AsMaterial materialOrder;
            if (asMaterial.getId() != null) {
                if (asMaterial.getStatus().equals("FINI")) {
                    asMaterial.setFinishTime(new Date());
                }
                asMaterial.setSyncRetail("N");
                materialOrder = this.updateByPrimaryKeySelective(iRequest, asMaterial);
            }//新增
            else {
                asMaterial.setCode(sequenceGenerateService.getNextMaterialCode());
                if (asMaterial.getCustomerid() != null) {
                    if (userMapper.selectByCustomerId(asMaterial.getCustomerid()).size() > 0) {
                        asMaterial.setUserId(userMapper.selectByCustomerId(asMaterial.getCustomerid()).get(0).getUserId());
                    }

                }

                materialOrder = this.insertSelective(iRequest, asMaterial);
            }
            if (asMaterial.getAsMaterialEntries() != null && asMaterial.getAsMaterialEntries().size() > 0) {
                saveMaterialOrderEntry(asMaterial.getAsMaterialEntries(), materialOrder.getId(), iRequest);
            }
            responseData.setSuccess(true);
            responseData.setMessage(String.valueOf(materialOrder.getId()));
            return responseData;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
            return responseData;
        }


    }


    /**
     * 保存物耗单行
     *
     * @param list
     * @param iRequest
     */
    private void saveMaterialOrderEntry(List<AsMaterialEntry> list, Long materialId, IRequest iRequest) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getMaterialEntryId() != null) {
                asMaterialEntryMapper.updateByPrimaryKeySelective(list.get(i));
            } else {
                list.get(i).setCode(sequenceGenerateService.getNextAsCode());
                list.get(i).setMaterialId(materialId);
                list.get(i).setLineNumber(String.valueOf(getNextLineNumber(materialId)));
                asMaterialEntryMapper.insertSelective(list.get(i));
            }
        }
    }


    public AsCreateRequestBody getBodyForOrderToRetail(IRequest iRequest, AsMaterial asMaterial) {
        AsCreateRequestBody body = new AsCreateRequestBody();
        //  body.setLvSo(String.valueOf(asMaterial.getOrderId()));//销售订单号
        AsCreateGdtHeader gdsHeader = new AsCreateGdtHeader();
        gdsHeader.setAuart("S003");//订单类型,默认传“S003”
        gdsHeader.setVkorg("0201");//销售组织,固定传0201（美克国际家居用品股份有限公司）
        gdsHeader.setVtweg("80");//分销渠道,固定传80（售后）
        gdsHeader.setSpart("20");//产品组,默认20
        gdsHeader.setAugru(asMaterial.getOrderReason());
        gdsHeader.setVsbed("D");//装运条件,D 配送(默认)
        gdsHeader.setAutlf("X");//全部交货,默认传"X"
        if (asMaterial.getIsCharge() != null && asMaterial.getIsCharge().equals("Y")) {
            gdsHeader.setLifsk("99");
        } else {
            gdsHeader.setLifsk("10");
        }
        gdsHeader.setKunnr1(asMaterial.getSoldParty());//售达方，根据订单头号[HMALL_AS_MATERIAL.ORDER_ID]，找到订单表中的字段WEBSITE_ID,根据WEBSITE_ID的值在网站表【HMALL_MST_WEBSITE】中取sold_party的值
        gdsHeader.setKunnr2("ONE");//送达方
        gdsHeader.setVkbur(asMaterial.getSalesOffice());//销售办公室、
        gdsHeader.setName1(asMaterial.getName());//送达方一次性客户姓名
        gdsHeader.setTranspZone(asMaterial.getReceiverDistrict());//发送货物的目的地运输区域
        gdsHeader.setStreet(asMaterial.getAddress());//住宅号及街道
        gdsHeader.setTelephone(asMaterial.getMobile());
        if (asMaterial.getAppointmentDate() != null) {
            gdsHeader.setVdatu(DateUtil.getdate(asMaterial.getAppointmentDate(), "yyyy-MM-dd"));//交货日期
            gdsHeader.setZzhopeday(DateUtil.getdate(asMaterial.getAppointmentDate(), "yyyy-MM-dd"));
            gdsHeader.setZzyydat(DateUtil.getdate(asMaterial.getAppointmentDate(), "yyyy-MM-dd"));
        } else {
            gdsHeader.setVdatu("");//交货日期
            gdsHeader.setZzhopeday("");
            gdsHeader.setZzyydat("");
        }
        AsMaterialEntry materialEntry = new AsMaterialEntry();
        materialEntry.setMaterialId(asMaterial.getId());
        List<AsMaterialEntry> asMaterialEntryList = asMaterialEntryMapper.getAllEntryByMaterialId(materialEntry);
        PointOfServiceDto pointOfServiceDto = new PointOfServiceDto();
        pointOfServiceDto.setPointOfServiceId(asMaterialEntryList.get(0).getPointofserviceId());
        gdsHeader.setWerks(iPointOfServiceExternalService.selectByPrimaryKey(pointOfServiceDto).getCode());
        gdsHeader.setZzclubid(asMaterial.getMobile());//会员号码
        gdsHeader.setZzclublevel(asMaterial.getUserLevel());
        gdsHeader.setStoriesNo(asMaterial.getCode());//电商发货单号
        if (asMaterial.getSex() != null && asMaterial.getSex().equals("M")) {
            gdsHeader.setTitle("0002");
        } else if (asMaterial.getSex() != null && asMaterial.getSex().equals("F")) {
            gdsHeader.setTitle("0001");
        } else {
            gdsHeader.setTitle("0001");
        }
        gdsHeader.setExord(asMaterial.getEscOrderCode());//外部系统单号 物耗单号

        gdsHeader.setExsys("MAP");//外部系统名称 默认传值“MAP”
        gdsHeader.setExnid(String.valueOf(iRequest.getUserId()));//外部系统单据创建用户ID
        gdsHeader.setExnam(iRequest.getUserName());//外部系统单据创建用户名称
        String time = null;
        String date1 = null;
        if (asMaterial.getCreationDate() != null) {
            date1 = DateUtil.getdate(asMaterial.getCreationDate(), "yyyy-MM-dd");
            time = DateUtil.getdate(asMaterial.getCreationDate(), "HH:mm:ss");
        }
        gdsHeader.setExdat(date1);//外部系统单据创建日期
        gdsHeader.setExtim(time);//外部系统单据创建时间
        gdsHeader.setExnidc("");
        gdsHeader.setExnamc("");
        gdsHeader.setExdatc("");
        gdsHeader.setExtimc("");
        body.setAsCreateGdtHeader(gdsHeader);
        List<AsGdtItemsItem> list = new ArrayList<>();
        List<AsGdtCondtionItem> condtionList = new ArrayList<>();
        AsGdtItems asGdtItems = new AsGdtItems();
        AsGdtCondtions asGdtCondtions = new AsGdtCondtions();
        setOrderParams(asMaterial, list, condtionList);
        asGdtItems.setItems(list);
        asGdtCondtions.setItems(condtionList);
        body.setAsGdtItems(asGdtItems);
        body.setAsGdtCondtions(asGdtCondtions);
        AsCreateGdtReturn gdtReturn = new AsCreateGdtReturn();
        body.setAsCreateGdtReturn(gdtReturn);


        return body;
    }

    public AsChangeRequestBody getUpdateBodyForOrderRetail(IRequest iRequest, AsMaterial asMaterial) {
        AsChangeRequestBody body = new AsChangeRequestBody();
        AsChangeGdtHeader gdsHeader = new AsChangeGdtHeader();
        gdsHeader.setVbeln(asMaterial.getSapCode());
        gdsHeader.setAuart("S003");//订单类型,默认传“S003”
        gdsHeader.setVkorg("0201");//销售组织,固定传0201（美克国际家居用品股份有限公司）
        gdsHeader.setVtweg("80");//分销渠道,固定传80（售后）
        gdsHeader.setSpart("20");//产品组,默认20
        gdsHeader.setAugru(asMaterial.getOrderReason());
        gdsHeader.setVsbed("D");//装运条件,D 配送(默认)
        gdsHeader.setAutlf("X");//全部交货,默认传"X"
        if (asMaterial.getIsCharge() != null && asMaterial.getIsCharge().equals("Y")) {
            gdsHeader.setLifsk("99");
        } else {
            gdsHeader.setLifsk("10");
        }
        gdsHeader.setKunnr1(asMaterial.getSoldParty());//售达方，根据订单头号[HMALL_AS_MATERIAL.ORDER_ID]，找到订单表中的字段WEBSITE_ID,根据WEBSITE_ID的值在网站表【HMALL_MST_WEBSITE】中取sold_party的值
        gdsHeader.setKunnr2("ONE");//送达方
        //gdsHeader.setVkbur(asMaterial.getSalesOffice());//销售办公室、
        gdsHeader.setName1(asMaterial.getName());//送达方一次性客户姓名
        //gdsHeader.setTranspZone(asMaterial.getReceiverDistrict());//发送货物的目的地运输区域
        gdsHeader.setTelephone(asMaterial.getMobile());
        gdsHeader.setStreet(asMaterial.getAddress());//住宅号及街道
        gdsHeader.setVdatu(DateUtil.getdate(asMaterial.getAppointmentDate(), "yyyy-MM-dd"));//交货日期
        // gdsHeader.setWerks("");
        gdsHeader.setZzclubid(asMaterial.getMobile());//会员号码
        //gdsHeader.setZzclublevel(asMaterial.getUserLevel());
        gdsHeader.setStoriesNo(asMaterial.getCode());//电商发货单号
       /* if (asMaterial.getSex() != null && asMaterial.getSex().equals("M")) {
            gdsHeader.setTitle("0002");
        } else if (asMaterial.getSex() != null && asMaterial.getSex().equals("F")) {
            gdsHeader.setTitle("0001");
        } else {
            gdsHeader.setTitle("0001");
        }*/
        gdsHeader.setExord(asMaterial.getEscOrderCode());//外部系统单号 物耗单号
        gdsHeader.setZzhopeday(DateUtil.getdate(asMaterial.getAppointmentDate(), "yyyy-MM-dd"));
        gdsHeader.setZzyydat(DateUtil.getdate(asMaterial.getAppointmentDate(), "yyyy-MM-dd"));
        gdsHeader.setExsys("MAP");//外部系统名称 默认传值“MAP”
      /*  gdsHeader.setExnid(String.valueOf(iRequest.getUserId()));//外部系统单据创建用户ID
        gdsHeader.setExnam(iRequest.getUserName());//外部系统单据创建用户名称*/
        String time = null;
        String date = null;
        Date currentDate = new Date();
        date = DateUtil.getdate(currentDate, "yyyy-MM-dd");
        time = DateUtil.getdate(currentDate, "HH:mm:ss");
      /*  gdsHeader.setExdat(date1);//外部系统单据创建日期
        gdsHeader.setExtim(time);//外部系统单据创建时间*/
        gdsHeader.setExnidc(String.valueOf(iRequest.getUserId()));
        gdsHeader.setExnamc(iRequest.getUserName());
        gdsHeader.setExdatc(date);//外部系统单据更新日期
        gdsHeader.setExtimc(time);
        body.setAsChangeGdtHeader(gdsHeader);
        List<AsGdtItemsItem> list = new ArrayList<>();
        List<AsGdtCondtionItem> condtionList = new ArrayList<>();
        AsGdtItems asGdtItems = new AsGdtItems();
        AsGdtCondtions asGdtCondtions = new AsGdtCondtions();
        setOrderParams(asMaterial, list, condtionList);
        asGdtItems.setItems(list);
        asGdtCondtions.setItems(condtionList);
        body.setAsGdtItems(asGdtItems);
        body.setAsGdtCondtions(asGdtCondtions);
        AsChangeGdtReturn gdtReturn = new AsChangeGdtReturn();
        body.setAsChangeGdtReturn(gdtReturn);
        return body;
    }

    /**
     * 设置物耗单行信息到请求参数中
     *
     * @param asMaterial
     * @param list
     * @param condtionList
     */
    private void setOrderParams(AsMaterial asMaterial, List<AsGdtItemsItem> list, List<AsGdtCondtionItem> condtionList) {
        AsMaterialEntry entry = new AsMaterialEntry();
        entry.setMaterialId(asMaterial.getId());
        List<AsMaterialEntry> asMaterialEntries = asMaterialEntryMapper.getAllEntryByMaterialId(entry);
        if (asMaterialEntries.size() > 0) {
            for (int i = 0; i < asMaterialEntries.size(); i++) {
                AsMaterialEntry entryInfo = asMaterialEntries.get(i);
                AsGdtItemsItem item = new AsGdtItemsItem();
                AsGdtCondtionItem condtionItem = new AsGdtCondtionItem();
                item.setPosnr(entryInfo.getLineNumber());//项目号码
                condtionItem.setPosnr(entryInfo.getLineNumber());
                condtionItem.setKschl("ZP00");//费用类型
                condtionItem.setKbetr(entryInfo.getUnitFee() * entryInfo.getQuantity() + "");
                condtionItem.setCurrency("RMB");//默认RMB
                item.setPstyv("Z011");//项目类型
                Product product = new Product();
                product.setProductId(entryInfo.getProductId());

                item.setMatnr(productMapper.selectByPrimaryKey(product).getCode());//商品编码
                item.setKwmeng(entryInfo.getQuantity() + "");//销售数量
                item.setVrkme(entryInfo.getUnit());//单位
                item.setLgort("9010");//库存地点,默认传值“9010”
                item.setSdabw("D");//装运条件,跟抬头装运条件保持一致
                AsMaterialEntry materialEntry = new AsMaterialEntry();
                materialEntry.setMaterialId(asMaterial.getId());
                List<AsMaterialEntry> asMaterialEntryList = asMaterialEntryMapper.getAllEntryByMaterialId(materialEntry);
                PointOfServiceDto pointOfServiceDto = new PointOfServiceDto();
                pointOfServiceDto.setPointOfServiceId(asMaterialEntryList.get(0).getPointofserviceId());
                item.setZzwerks(iPointOfServiceExternalService.selectByPrimaryKey(pointOfServiceDto).getCode());
                item.setZy09(entryInfo.getPatchReason());
                item.setPosnr(entryInfo.getLineNumber());
                list.add(item);
                condtionList.add(condtionItem);
            }
        }
    }


    /**
     * 获取下一个物耗单行项目号
     *
     * @param MaterialId
     * @return
     */
    public Long getNextLineNumber(Long MaterialId) {
        AsMaterialEntry entry = new AsMaterialEntry();
        entry.setMaterialId(MaterialId);
        List<AsMaterialEntry> asMaterialEntryList = asMaterialEntryMapper.select(entry);
        if (CollectionUtils.isEmpty(asMaterialEntryList)) {
            return FIRST_LINE_NUMBER;
        } else {
            AsMaterialEntry max = asMaterialEntryList.stream().max((orderEntry1, orderEntry2)
                    -> orderEntry1.getLineNumber().compareTo(orderEntry2.getLineNumber())).get();
            return Long.valueOf(max.getLineNumber()) + LINE_NUMBER_STEP;
        }
    }

    @Override
    public List<AsMaterial> selectMaterialList(IRequest requestContext, String code, String serviceOrderCode, String escOrderCode, String customerid, String mobile, String sapCode, String creationDateStart, String creationDateEnd, String finishTimeStart, String finishTimeEnd, String isCharge, String syncRetail, String[] strMaterialStatus, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return asMaterialMapper.selectMaterialList(code, serviceOrderCode, escOrderCode, customerid, mobile, sapCode, creationDateStart, creationDateEnd, finishTimeStart, finishTimeEnd, isCharge, syncRetail, strMaterialStatus);
    }

    @Override
    public List<AsMaterial> selectMaterialList(IRequest requestContext, String code, String serviceOrderCode, String escOrderCode, String customerid, String mobile, String sapCode, String creationDateStart, String creationDateEnd, String finishTimeStart, String finishTimeEnd, String isCharge, String syncRetail, String[] strMaterialStatus) {
        String responsiblePartyMeaning = null;
        String orderReasonMeaning = null;
        String isChargeMeaning = null;
        String patchReasonMeaning = null;
        List<AsMaterial> asMaterials = asMaterialMapper.selectMaterialList(code, serviceOrderCode, escOrderCode, customerid, mobile, sapCode, creationDateStart, creationDateEnd, finishTimeStart, finishTimeEnd, isCharge, syncRetail, strMaterialStatus);
        for (AsMaterial asMaterial : asMaterials) {
            if (!StringUtils.isEmpty(asMaterial.getResponsibleParty())) {
                responsiblePartyMeaning = codeService.getCodeMeaningByValue(requestContext, "HMALL_AS_RESPONSIBLE_PARTY", asMaterial.getResponsibleParty());
            }
            if (!StringUtils.isEmpty(asMaterial.getOrderReason())) {
                orderReasonMeaning = codeService.getCodeMeaningByValue(requestContext, "HMALL_AS_ORDER_REASON", asMaterial.getOrderReason());
            }
            if (!StringUtils.isEmpty(asMaterial.getIsCharge())) {
                isChargeMeaning = codeService.getCodeMeaningByValue(requestContext, "SYS.YES_NO", asMaterial.getIsCharge());
            }
            if (!StringUtils.isEmpty(asMaterial.getPatchReason())) {
                patchReasonMeaning = codeService.getCodeMeaningByValue(requestContext, "HMALL_AS_PATCH_REASON", asMaterial.getPatchReason());
            }
            asMaterial.setResponsibleParty(responsiblePartyMeaning);
            asMaterial.setOrderReason(orderReasonMeaning);
            asMaterial.setIsCharge(isChargeMeaning);
            asMaterial.setPatchReason(patchReasonMeaning);
        }
        return asMaterials;
    }
}