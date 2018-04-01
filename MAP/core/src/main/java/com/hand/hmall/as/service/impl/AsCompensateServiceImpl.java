package com.hand.hmall.as.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.mybatis.util.StringUtil;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.as.dto.AsCompensate;
import com.hand.hmall.as.dto.AsCompensateEntry;
import com.hand.hmall.as.dto.AsCompensateTemplate;
import com.hand.hmall.as.dto.Serviceorder;
import com.hand.hmall.as.mapper.AsCompensateEntryMapper;
import com.hand.hmall.as.mapper.AsCompensateMapper;
import com.hand.hmall.as.mapper.ServiceorderMapper;
import com.hand.hmall.as.service.IAsCompensateEntryService;
import com.hand.hmall.as.service.IAsCompensateService;
import com.hand.hmall.common.service.ISequenceGenerateService;
import com.hand.hmall.log.dto.LogManager;
import com.hand.hmall.log.service.ILogManagerService;
import com.hand.hmall.om.dto.HmallSoChangeLog;
import com.hand.hmall.om.dto.Order;
import com.hand.hmall.om.dto.OrderEntry;
import com.hand.hmall.om.mapper.OrderEntryMapper;
import com.hand.hmall.om.mapper.OrderMapper;
import com.hand.hmall.om.service.IOrderService;
import com.hand.hmall.util.Constants;
import com.hand.hmall.util.DateUtil;
import com.hand.hmall.ws.client.IAsOrderPushClient;
import com.hand.hmall.ws.entities.*;
import com.markor.map.external.pointservice.dto.PointOfServiceDto;
import com.markor.map.external.pointservice.service.IPointOfServiceExternalService;
import com.markor.map.framework.soapclient.exceptions.WSCallException;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author xuxiaoxue
 * @version 0.1
 * @name AsCompensateServiceImpl
 * @description 销售赔付单头表Service实现类
 * @date 2017/10/11
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AsCompensateServiceImpl extends BaseServiceImpl<AsCompensate> implements IAsCompensateService {

    private static final Long FIRST_LINE_NUMBER = 10L; // 第一个销售赔付单行的lineNumber
    private static final Long LINE_NUMBER_STEP = 10L; // lineNumber以10递增
    @Autowired
    ILogManagerService logManagerService;
    @Autowired
    IAsOrderPushClient client;
    @Autowired
    private AsCompensateMapper asCompensateMapper;
    @Autowired
    private AsCompensateEntryMapper asCompensateEntryMapper;
    @Autowired
    private IAsCompensateService compensateService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ServiceorderMapper serviceOrderMapper;
    @Autowired
    private ISequenceGenerateService sequenceGenerateService;
    @Autowired
    private IAsCompensateEntryService asCompensateEntryService;
    @Autowired
    private IPointOfServiceExternalService iPointOfServiceExternalService;
    @Autowired
    private OrderEntryMapper orderEntryMapper;
    @Autowired
    private IOrderService orderService;

    @Override
    public List<AsCompensate> selectCompensateById(AsCompensate dto) {
        return asCompensateMapper.selectCompensateById(dto);
    }

    @Override
    public List<AsCompensate> selectMstUnit() {
        return asCompensateMapper.selectMstUnit();
    }

    /**
     * 保存赔付单信息 赔付单行信息
     *
     * @param iRequest
     * @param dto
     * @return
     */
    @Override
    public ResponseData saveCompensate(IRequest iRequest, List<AsCompensate> dto) {
        AsCompensate asCompensate = null;
        Long compensateId = null;
        ResponseData responseData = new ResponseData();

        //检验赔付单行数据
        if (CollectionUtils.isNotEmpty(dto.get(0).getAsCompensateEntryList())) {
            for (AsCompensateEntry asCompensateEntry : dto.get(0).getAsCompensateEntryList()) {
                if (asCompensateEntry.getUnitFee() == null) {
                    responseData.setSuccess(false);
                    responseData.setMessage("请将赔付单行信息填写完整");
                    return responseData;
                }
            }
        }
        try {
            //修改
            if (dto.get(0).getCompensateId() != null) {
                asCompensate = compensateService.selectByPrimaryKey(iRequest, dto.get(0));
                //更新
                if (asCompensate != null) {
                    dto.get(0).setSyncflag("N");
                    asCompensate = compensateService.updateByPrimaryKeySelective(iRequest, dto.get(0));
                    compensateId = asCompensate.getCompensateId();
                }
            }
            //保存赔付单行
            if (compensateId != null) {
                String result = saveCompensateEntry(dto.get(0).getAsCompensateEntryList(), compensateId);
                if (Constants.ZERO.equals(result)) {
                    responseData.setSuccess(false);
                    responseData.setMessage("系统错误");
                } else {
                    List<AsCompensate> list = new ArrayList<>();
                    list.add(asCompensate);
                    responseData.setSuccess(true);
                    responseData.setRows(list);
                }
            }
            //增加书面记录
            if (dto.get(0).getCompensateId() != null) {
                asCompensate = compensateService.selectByPrimaryKey(iRequest, dto.get(0));
                compensateAddSoChangeLog(asCompensate);

            }
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage("系统错误");
            throw new RuntimeException();
        }
        return responseData;
    }

    /**
     * 保存赔付单行
     *
     * @param asCompensateEntryList
     * @param compensateId
     * @return
     */
    private String saveCompensateEntry(List<AsCompensateEntry> asCompensateEntryList, long compensateId) {
        try {
            if (CollectionUtils.isNotEmpty(asCompensateEntryList)) {
                //计算行序号
                AsCompensateEntry condition = new AsCompensateEntry();
                condition.setCompensateId(compensateId);
                List<AsCompensateEntry> list = asCompensateEntryMapper.selectCompensateEntryById(condition);
                int lineNumber = 10;
                if (CollectionUtils.isNotEmpty(list)) {
                    lineNumber = Integer.parseInt(list.get(list.size() - 1).getLineNumber()) + 10;
                }

                BigDecimal compensateFee = new BigDecimal(0);
                for (AsCompensateEntry asCompensateEntry : asCompensateEntryList) {
                    if (asCompensateEntry.getUnitFee() != null) {
                        //总价
                        BigDecimal quantity = new BigDecimal(Double.toString(asCompensateEntry.getQuantity() != null ? asCompensateEntry.getQuantity() : 1.0));
                        BigDecimal unitFee = asCompensateEntry.getUnitFee();
                        asCompensateEntry.setTotalFee(quantity.multiply(unitFee));
                        //计算赔付金额
                        compensateFee = compensateFee.add(asCompensateEntry.getTotalFee());
                    }
                    //修改
                    if (asCompensateEntry.getCompensateEntryId() != null) {
                        asCompensateEntryMapper.updateByPrimaryKeySelective(asCompensateEntry);
                    }
                    //新增
                    else {
                        if (StringUtils.isEmpty(asCompensateEntry.getCompensateType())) {
                            asCompensateEntry.setCompensateType("COMPENSATE_FEE");
                        }
                        if (StringUtils.isEmpty(asCompensateEntry.getUnit())) {
                            asCompensateEntry.setUnit("PC");
                        }
                        asCompensateEntry.setQuantity(1.0);
                        asCompensateEntry.setCompensateId(compensateId);
                        asCompensateEntry.setLineNumber(lineNumber + "");
                        lineNumber = lineNumber + 10;
                        asCompensateEntryMapper.insertSelective(asCompensateEntry);
                    }
                }
                //保存赔付金额
                AsCompensate asCompensate = new AsCompensate();
                asCompensate.setCompensateId(compensateId);
                asCompensate.setCompensateFee(compensateFee);
                asCompensateMapper.updateByPrimaryKeySelective(asCompensate);
            }
            return Constants.ONE;
        } catch (Exception e) {
            return Constants.ZERO;
        }
    }

    @Override
    public ResponseData importCompensateAndCompensateEntry(List<AsCompensateTemplate> infos, IRequest iRequest, Boolean importResult, String message) throws Exception {
        //用于存放已经插入的赔付单头信息
        List<AsCompensate> saveList = new ArrayList<AsCompensate>();
        List<AsCompensateTemplate> subList = null;
        AsCompensate asCompensate = null;
        boolean insertFlag;
        for (int i = 0; i < infos.size(); i++) {
            insertFlag = true;
            //EXCEL表格销售赔付单序号不能为空
            String compensateNum = infos.get(i).getCompensateNum();
            if (StringUtil.isEmpty(compensateNum)) {
                message = " 第" + (i + 1) + "条数据销售赔付单序号不能为空";
                throw new Exception(message);
            }
            //EXCEL表格订单编号不能为空
            String orderCode = infos.get(i).getOrderCode();
            if (StringUtil.isEmpty(orderCode)) {
                message = " 第" + (i + 1) + "条数据订单编号不能为空";
                throw new Exception(message);
            }
            //Excel表格服务单单号不能为空
            String serviceOrderCode = infos.get(i).getServiceOrderCode();
            if (StringUtil.isEmpty(serviceOrderCode)) {
                message = " 第" + (i + 1) + "条数据服务单单号不能为空";
                throw new Exception(message);
            }
            //Excel表格受理客服不能为空
            String cs = infos.get(i).getCs();
            if (StringUtil.isEmpty(cs)) {
                message = " 第" + (i + 1) + "条数据受理客服不能为空";
                throw new Exception(message);
            }
            //Excel表格赔付金额不能为空
            Double payFee = infos.get(i).getPayFee();
            if (payFee == null) {
                message = " 第" + (i + 1) + "条数据赔付金额不能为空";
                throw new Exception(message);
            }
            //Excel表格完结时间不能为空
            Date finishTime = infos.get(i).getFinishTime();

            if (finishTime == null) {
                message = " 第" + (i + 1) + "条数据完结时间格式错误";
                throw new Exception(message);
            }
            //Excel表格订单编号对应的订单数据必须存在
            List<Order> orderList = orderMapper.selectOrderByCode(orderCode);
            if (orderList.size() == 0) {
                message = " 第" + (i + 1) + "条数据订单编号对应的订单数据不存在";
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
            AsCompensate compensate = new AsCompensate();
            String compensateCode = sequenceGenerateService.getNextAsCompensateCode();
            compensate.setCode(compensateCode);
            compensate.setStatus("FINI");
            compensate.setOrderId(orderList.get(0).getOrderId());
            compensate.setServiceId(serviceOrderList.get(0).getServiceOrderId());
            compensate.setNote(infos.get(i).getNote());
            compensate.setName(serviceOrderList.get(0).getName());
            compensate.setMobile(serviceOrderList.get(0).getMobile());
            compensate.setAddress(serviceOrderList.get(0).getAddress());
            compensate.setCs(infos.get(i).getCs());
            compensate.setFinishTime(finishTime);
            compensate.setSyncflag("N");
            compensate.setAppointmentDate(finishTime);

            if (i > 0) {
                subList = infos.subList(0, i);
                for (AsCompensateTemplate asCompensateTemplate : subList) {
                    //如果后面的行和前面的数据销售赔付单序号相同,校验A1，B1，C1，D1，E1字段必须相同
                    if (asCompensateTemplate.getCompensateNum().equals(infos.get(i).getCompensateNum())) {
                        String subOrderCode = asCompensateTemplate.getOrderCode();
                        String subServiceOrderCode = asCompensateTemplate.getServiceOrderCode();
                        String subNote = asCompensateTemplate.getNote();
                        String note = infos.get(i).getNote();
                        String subCs = asCompensateTemplate.getCs();
                        Date subFinishTime = asCompensateTemplate.getFinishTime();
                        if (!subOrderCode.equals(orderCode) || !subServiceOrderCode.equals(serviceOrderCode) || !subNote.equals(note) || !subCs.equals(cs) || !subFinishTime.equals(finishTime)) {
                            //如果销售赔付单序号相同的表记录中A-E列的数据不一致,导入报错
                            throw new Exception("销售赔付单序号为" + infos.get(i).getCompensateNum() + "的表记录B-F列值必须相同");
                        } else {
                            //如果销售赔付单序号一致，且销售赔付单序号相同的表记录中A-E列的数据一致，则不插入新的销售赔付单头
                            for (AsCompensate saveCompensate : saveList) {
                                if (saveCompensate.getCompensateNum().equals(infos.get(i).getCompensateNum())) {
                                    asCompensate = new AsCompensate();
                                    asCompensate.setCompensateId(saveCompensate.getCompensateId());
                                }
                            }
                        }
                        insertFlag = false;
                    }
                }
                if (insertFlag) {
                    asCompensate = this.insertSelective(iRequest, compensate);
                    //将插入销售赔付单头对应的额销售赔付单序号存入saveList中
                    asCompensate.setCompensateNum(infos.get(i).getCompensateNum());
                    saveList.add(asCompensate);
                }
            } else {
                asCompensate = this.insertSelective(iRequest, compensate);
                //将插入销售赔付单头对应的额销售赔付单序号存入saveList中
                asCompensate.setCompensateNum(infos.get(i).getCompensateNum());
                saveList.add(asCompensate);
            }


            AsCompensateEntry compensateEntry = new AsCompensateEntry();
            compensateEntry.setCompensateId(asCompensate.getCompensateId());
            compensateEntry.setLineNumber(getNextLineNumber(asCompensate.getCompensateId()));
            compensateEntry.setCompensateType("COMPENSATE_FEE");
            compensateEntry.setQuantity(Double.valueOf("1"));
            compensateEntry.setUnit("PC");
            compensateEntry.setUnitFee(new BigDecimal(infos.get(i).getPayFee()));
            //总价等于【Unit_FEE】*【QUANTITY】
            compensateEntry.setTotalFee(new BigDecimal(infos.get(i).getPayFee()).multiply(new BigDecimal(compensateEntry.getQuantity())));
            compensateEntry.setNote(infos.get(i).getLineNote());
            AsCompensateEntry asCompensateEntry = asCompensateEntryService.insertSelective(iRequest, compensateEntry);
            AsCompensateEntry asCompensateEntryFor = new AsCompensateEntry();
            asCompensateEntryFor.setCompensateId(asCompensateEntry.getCompensateId());
            List<AsCompensateEntry> AsCompensateEntrySelect = asCompensateEntryMapper.select(asCompensateEntryFor);
            AsCompensate asCompensateInsert = new AsCompensate();
            if (AsCompensateEntrySelect.size() != 0) {
                BigDecimal addTotalFee = new BigDecimal(0);
                for (AsCompensateEntry entry : AsCompensateEntrySelect) {
                    BigDecimal totalFee = entry.getTotalFee();
                    addTotalFee = addTotalFee.add(totalFee);
                }

                asCompensateInsert.setCompensateFee(addTotalFee);
                asCompensateInsert.setCompensateId(asCompensate.getCompensateId());
            }
            this.updateByPrimaryKeySelective(iRequest, asCompensateInsert);
            importResult = true;
        }
        for (AsCompensate compensate : saveList) {
            compensateAddSoChangeLog(compensate);
        }
        ResponseData responseData = new ResponseData(importResult);
        responseData.setMessage(message);
        return responseData;
    }

    /**
     * 增加书面记录
     *
     * @param asCompensate
     */
    @Override
    public void compensateAddSoChangeLog(AsCompensate asCompensate) {
        //增加书面记录
        HmallSoChangeLog soChangeLog = new HmallSoChangeLog();
        soChangeLog.setOrderId(asCompensate.getCompensateId());
        soChangeLog.setOrderType("3");
        soChangeLog.setQuantity(0);
        soChangeLog.setChangeQuantity(0);
        if (asCompensate.getStatus().equals("FINI")) {
            soChangeLog.setTotalFee(new BigDecimal("0").subtract(new BigDecimal(asCompensate.getCompensateFee().toString())));
            soChangeLog.setChangeFee(new BigDecimal("0").subtract(new BigDecimal(asCompensate.getCompensateFee().toString())));
        } else {
            soChangeLog.setTotalFee(new BigDecimal(asCompensate.getCompensateFee().toString()));
            soChangeLog.setChangeFee(new BigDecimal(asCompensate.getCompensateFee().toString()));
        }
        soChangeLog.setParentOrderId(asCompensate.getOrderId());
        orderService.addSoChangeLog(soChangeLog);
    }

    /**
     * 获取下一个销售赔付单行项目号
     *
     * @param compensateId
     * @return
     */
    public String getNextLineNumber(Long compensateId) {
        AsCompensateEntry asCompensateEntry = new AsCompensateEntry();
        asCompensateEntry.setCompensateId(compensateId);
        List<AsCompensateEntry> asCompensateEntryList = asCompensateEntryMapper.select(asCompensateEntry);
        if (org.apache.cxf.common.util.CollectionUtils.isEmpty(asCompensateEntryList)) {
            return FIRST_LINE_NUMBER.toString();
        } else {
            AsCompensateEntry max = asCompensateEntryList.stream().max((compensatEntry1, compensatEntry2)
                    -> compensatEntry1.getLineNumber().compareTo(compensatEntry2.getLineNumber())).get();
            return String.valueOf(Long.valueOf(max.getLineNumber()) + LINE_NUMBER_STEP);
        }
    }

    @Override
    public String compensateSyncRetail(IRequest iRequest, AsCompensate asCompensate) {

        AsCompensate compensate = asCompensateMapper.getCompensateForRetail(asCompensate);
        LogManager log = new LogManager();
        log.setStartTime(new Date());
        log.setDataPrimaryKey(compensate.getCompensateId());
        log.setProgramName(this.getClass().getName());
        log.setSourcePlatform(Constants.SOURCE_PLATFORM_SYNC);
        String sapCode = compensate.getSapCode();
        if (StringUtils.isEmpty(sapCode)) {
            log.setProgramDescription("赔付单推送Retail-新增");
        } else {
            log.setProgramDescription("赔付单推送Retail-修改");
        }
        log = logManagerService.logBegin(iRequest, log);
        if (StringUtils.isEmpty(sapCode) && compensate.getStatus().equals("FINI")) {
            try {
                AsCreateRequestBody orderRequestBody = getBodyForOrderToRetail(iRequest, compensate);
                sapCode = sequenceGenerateService.getNextNumber("sapCode", 9L, "D");

                orderRequestBody.setLvSo(sapCode);
                log.setMessage(JSONObject.fromObject(orderRequestBody).toString());
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
                        AsCompensate dto = new AsCompensate();
                        dto.setCompensateId(compensate.getCompensateId());
                        dto.setSapCode(sapCode);
                        dto.setSyncflag(Constants.YES);
                        this.updateByPrimaryKeySelective(iRequest, dto);
                    }
                    logManagerService.logEnd(iRequest, log);
                }


                if (flag) {
                    return "success";
                } else {
                    return errMessage;
                }

            } catch (WSCallException e) {
                log.setProcessStatus("E");
                log.setReturnMessage(e.getMessage());
                logManagerService.logEnd(iRequest, log);
                return e.getMessage();
            } catch (Exception e) {
                log.setProcessStatus("E");
                log.setReturnMessage(e.getMessage());
                logManagerService.logEnd(iRequest, log);
                return e.getMessage();
            }
        } else if (compensate.getStatus().equals("FINI") || compensate.getStatus().equals("CANC")) {
            try {
                AsChangeRequestBody asChangeRequestBody = getUpdateBodyForOrderRetail(iRequest, compensate);
                log.setMessage(JSONObject.fromObject(asChangeRequestBody).toString());
                AsChangeResponseBody responseBody = client.orderUpdate(asChangeRequestBody);
                List<AsChangeGdtReturnItems> result = responseBody.getAsChangeGdtReturn().getAsChangeGdtReturnItems();
                Boolean flag = true;
                String errMessage = "";
                if (CollectionUtils.isNotEmpty(result)) {

                    String returnMessage = "SapCode:" + compensate.getSapCode();
                    for (AsChangeGdtReturnItems returnItem : result) {
                        if ("E".equals(returnItem.getType1()) || "E".equals(returnItem.getType())) {
                            flag = false;
                            errMessage += returnItem.getMsg() + "\n";
                        }
                        returnMessage = returnMessage + " 返回信息：" + returnItem.getMsg() + ";";
                    }
                    log.setProcessStatus(flag ? "S" : "E");
                    log.setReturnMessage(returnMessage);
                    if (flag) {
                        AsCompensate dto = new AsCompensate();
                        dto.setCompensateId(compensate.getCompensateId());
                        dto.setSyncflag(Constants.YES);
                        this.updateByPrimaryKeySelective(iRequest, dto);
                    }
                    logManagerService.logEnd(iRequest, log);
                }


                if (flag) {
                    return "success";
                } else {
                    return errMessage;
                }
            } catch (WSCallException e) {
                log.setProcessStatus("E");
                log.setReturnMessage(e.getMessage());
                logManagerService.logEnd(iRequest, log);
                return e.getMessage();
            } catch (Exception e) {
                log.setProcessStatus("E");
                log.setReturnMessage(e.getMessage());
                logManagerService.logEnd(iRequest, log);
                return e.getMessage();
            }

        }
        return "";
    }

    @Override
    public List<AsCompensate> selectSendRetailData(AsCompensate dto) {
        return asCompensateMapper.selectSendRetailData(dto);
    }


    public AsCreateRequestBody getBodyForOrderToRetail(IRequest iRequest, AsCompensate asCompensate) {
        AsCreateRequestBody body = new AsCreateRequestBody();
        AsCreateGdtHeader gdsHeader = new AsCreateGdtHeader();
        gdsHeader.setAuart("SR08");//订单类型,默认传“SRO8”
        gdsHeader.setVkorg("0201");//销售组织,固定传0201（美克国际家居用品股份有限公司）
        gdsHeader.setVtweg("80");//分销渠道,固定传80（售后）
        gdsHeader.setSpart("20");//产品组,默认20
        gdsHeader.setVsbed("D");//装运条件,D 配送(默认)
        gdsHeader.setAutlf("X");//全部交货,默认传"X"
        gdsHeader.setLifsk("99");

        gdsHeader.setKunnr1(asCompensate.getSoldParty());//售达方 根据订单头号[HMALL_AS_RETURN.ORDER_ID]，找到订单表中的字段WEBSITE_ID,根据WEBSITE_ID的值在网站表【HMALL_MST_WEBSITE】中取sold_party的值
        gdsHeader.setKunnr2("ONE");//送达方
        gdsHeader.setVkbur(asCompensate.getSalesOffice());//销售办公室、
        gdsHeader.setName1(asCompensate.getName());//送达方一次性客户姓名
        gdsHeader.setTranspZone(asCompensate.getReceiverDistrict());//发送货物的目的地运输区域
        gdsHeader.setStreet(asCompensate.getAddress());//住宅号及街道
        gdsHeader.setTelephone(asCompensate.getMobile());
        if (asCompensate.getAppointmentDate() != null) {
            gdsHeader.setVdatu(DateUtil.getdate(asCompensate.getAppointmentDate(), "yyyy-MM-dd"));//交货日期
            gdsHeader.setZzhopeday(DateUtil.getdate(asCompensate.getAppointmentDate(), "yyyy-MM-dd"));
            gdsHeader.setZzyydat(DateUtil.getdate(asCompensate.getAppointmentDate(), "yyyy-MM-dd"));
        } else {
            gdsHeader.setVdatu("");//交货日期
            gdsHeader.setZzhopeday("");
            gdsHeader.setZzyydat("");
        }
        OrderEntry orderEntry = new OrderEntry();
        orderEntry.setOrderId(asCompensate.getOrderId());
        List<OrderEntry> orderEntryList = orderEntryMapper.select(orderEntry);
        PointOfServiceDto pointOfServiceDto = new PointOfServiceDto();
        pointOfServiceDto.setPointOfServiceId(orderEntryList.get(0).getPointOfServiceId());
        gdsHeader.setWerks(iPointOfServiceExternalService.selectByPrimaryKey(pointOfServiceDto).getCode());
        gdsHeader.setZzclubid(asCompensate.getMobile());//会员号码
        gdsHeader.setStoriesNo(asCompensate.getCode());//电商发货单号
        if (asCompensate.getSex() != null && asCompensate.getSex().equals("M")) {
            gdsHeader.setTitle("0002");
        } else if (asCompensate.getSex() != null && asCompensate.getSex().equals("F")) {
            gdsHeader.setTitle("0001");
        } else {
            gdsHeader.setTitle("0001");
        }
        gdsHeader.setExord(asCompensate.getEscOrderCode());//外部系统单号 物耗单号

        gdsHeader.setExsys("MAP");//外部系统名称 默认传值“MAP”
        gdsHeader.setExnid("MAP");//外部系统单据创建用户ID
        gdsHeader.setExnam("MAP");//外部系统单据创建用户名称
        String time = null;
        String date1 = null;
        if (asCompensate.getCreationDate() != null) {
            date1 = DateUtil.getdate(asCompensate.getCreationDate(), "yyyy-MM-dd");
            time = DateUtil.getdate(asCompensate.getCreationDate(), "HH:mm:ss");
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
        setOrderParams(asCompensate, list, condtionList);
        asGdtItems.setItems(list);
        asGdtCondtions.setItems(condtionList);
        body.setAsGdtItems(asGdtItems);
        body.setAsGdtCondtions(asGdtCondtions);
        AsCreateGdtReturn gdtReturn = new AsCreateGdtReturn();
        body.setAsCreateGdtReturn(gdtReturn);


        return body;
    }

    public AsChangeRequestBody getUpdateBodyForOrderRetail(IRequest iRequest, AsCompensate asCompensate) {
        AsChangeRequestBody body = new AsChangeRequestBody();
        AsChangeGdtHeader gdsHeader = new AsChangeGdtHeader();
        gdsHeader.setVbeln(asCompensate.getSapCode());

        gdsHeader.setAuart("SR08");//订单类型,默认传“SRO8”
        gdsHeader.setVkorg("0201");//销售组织,固定传0201（美克国际家居用品股份有限公司）
        gdsHeader.setVtweg("80");//分销渠道,固定传80（售后）
        gdsHeader.setSpart("20");//产品组,默认20
        gdsHeader.setVsbed("D");//装运条件,D 配送(默认)
        gdsHeader.setAutlf("X");//全部交货,默认传"X"
        gdsHeader.setLifsk("99");

        gdsHeader.setKunnr1(asCompensate.getSoldParty());//售达方 根据订单头号[HMALL_AS_RETURN.ORDER_ID]，找到订单表中的字段WEBSITE_ID,根据WEBSITE_ID的值在网站表【HMALL_MST_WEBSITE】中取sold_party的值
        gdsHeader.setKunnr2("ONE");//送达方
        gdsHeader.setVkbur(asCompensate.getSalesOffice());//销售办公室、
        gdsHeader.setName1(asCompensate.getName());//送达方一次性客户姓名
        gdsHeader.setTranspZone(asCompensate.getReceiverDistrict());//发送货物的目的地运输区域
        gdsHeader.setStreet(asCompensate.getAddress());//住宅号及街道
        gdsHeader.setTelephone(asCompensate.getMobile());
        if (asCompensate.getAppointmentDate() != null) {
            gdsHeader.setVdatu(DateUtil.getdate(asCompensate.getAppointmentDate(), "yyyy-MM-dd"));//交货日期
            gdsHeader.setZzhopeday(DateUtil.getdate(asCompensate.getAppointmentDate(), "yyyy-MM-dd"));
            gdsHeader.setZzyydat(DateUtil.getdate(asCompensate.getAppointmentDate(), "yyyy-MM-dd"));
        } else {
            gdsHeader.setVdatu("");//交货日期
            gdsHeader.setZzhopeday("");
            gdsHeader.setZzyydat("");
        }
        OrderEntry orderEntry = new OrderEntry();
        orderEntry.setOrderId(asCompensate.getOrderId());
        List<OrderEntry> orderEntryList = orderEntryMapper.select(orderEntry);
        PointOfServiceDto pointOfServiceDto = new PointOfServiceDto();
        pointOfServiceDto.setPointOfServiceId(orderEntryList.get(0).getPointOfServiceId());
        gdsHeader.setWerks(iPointOfServiceExternalService.selectByPrimaryKey(pointOfServiceDto).getCode());
        gdsHeader.setZzclubid(asCompensate.getMobile());//会员号码
        gdsHeader.setStoriesNo(asCompensate.getCode());//电商发货单号
        if (asCompensate.getSex() != null && asCompensate.getSex().equals("M")) {
            gdsHeader.setTitle("0002");
        } else if (asCompensate.getSex() != null && asCompensate.getSex().equals("F")) {
            gdsHeader.setTitle("0001");
        } else {
            gdsHeader.setTitle("0001");
        }
        gdsHeader.setExord(asCompensate.getEscOrderCode());//外部系统单号 物耗单号
        gdsHeader.setZzhopeday(DateUtil.getdate(asCompensate.getAppointmentDate(), "yyyy-MM-dd"));
        gdsHeader.setZzyydat(DateUtil.getdate(asCompensate.getAppointmentDate(), "yyyy-MM-dd"));
        gdsHeader.setExsys("MAP");//外部系统名称 默认传值“MAP”
        String time = null;
        String date = null;
        Date currentDate = new Date();
        date = DateUtil.getdate(currentDate, "yyyy-MM-dd");
        time = DateUtil.getdate(currentDate, "HH:mm:ss");

        gdsHeader.setExnidc("MAP");
        gdsHeader.setExnamc("MAP");
        gdsHeader.setExdatc(date);//外部系统单据更新日期
        gdsHeader.setExtimc(time);
        body.setAsChangeGdtHeader(gdsHeader);
        List<AsGdtItemsItem> list = new ArrayList<>();
        List<AsGdtCondtionItem> condtionList = new ArrayList<>();
        AsGdtItems asGdtItems = new AsGdtItems();
        AsGdtCondtions asGdtCondtions = new AsGdtCondtions();
        setOrderParams(asCompensate, list, condtionList);
        asGdtItems.setItems(list);
        asGdtCondtions.setItems(condtionList);
        body.setAsGdtItems(asGdtItems);
        body.setAsGdtCondtions(asGdtCondtions);
        AsChangeGdtReturn gdtReturn = new AsChangeGdtReturn();
        body.setAsChangeGdtReturn(gdtReturn);
        return body;
    }

    /**
     * 设置销售赔付单行信息到请求参数中
     *
     * @param asCompensate
     * @param list
     * @param condtionList
     */
    private void setOrderParams(AsCompensate asCompensate, List<AsGdtItemsItem> list, List<AsGdtCondtionItem> condtionList) {
        AsCompensateEntry entry = new AsCompensateEntry();
        entry.setCompensateId(asCompensate.getCompensateId());
        List<AsCompensateEntry> asCompensateEntries = asCompensateEntryMapper.selectCompensateEntryForRetail(entry);
        if (asCompensateEntries.size() > 0) {
            for (int i = 0; i < asCompensateEntries.size(); i++) {
                AsCompensateEntry entryInfo = asCompensateEntries.get(i);
                AsGdtItemsItem item = new AsGdtItemsItem();
                AsGdtCondtionItem condtionItem = new AsGdtCondtionItem();
                item.setPosnr(entryInfo.getLineNumber());//项目号码
                condtionItem.setPosnr(entryInfo.getLineNumber());
                condtionItem.setKschl("ZP00");//费用类型
                condtionItem.setKbetr(entryInfo.getTotalFee() + "");
                condtionItem.setCurrency("RMB");//默认RMB
                item.setPstyv("ZR18");//项目类型


                item.setMatnr(entryInfo.getSapCode());//商品编码
                item.setKwmeng(entryInfo.getQuantity() + "");//销售数量
                item.setVrkme(entryInfo.getUnit());//单位
                item.setLgort("9030");//库存地点,默认传值“9030”
                item.setSdabw("D");//装运条件,跟抬头装运条件保持一致
                if (asCompensate.getStatus().equals("CANC")) {
                    item.setAbgru("DE");
                }
                if (asCompensate.getShippingType() != null) {
                    if (asCompensate.getShippingType().equals("LOGISTICS")) {
                        item.setZy04("20");
                    } else {
                        item.setZy04("30");
                    }
                } else {
                    item.setZy04("");
                }
                if (asCompensate.getTradeFinishTime() != null) {
                    item.setZy010(DateUtil.getdate(asCompensate.getTradeFinishTime(), "yyyy-MM-dd"));
                } else {
                    item.setZy010("");
                }
                OrderEntry orderEntry = new OrderEntry();
                orderEntry.setOrderId(asCompensate.getOrderId());
                List<OrderEntry> orderEntryList = orderEntryMapper.select(orderEntry);
                PointOfServiceDto pointOfServiceDto = new PointOfServiceDto();
                pointOfServiceDto.setPointOfServiceId(orderEntryList.get(0).getPointOfServiceId());
                item.setZzwerks(iPointOfServiceExternalService.selectByPrimaryKey(pointOfServiceDto).getCode());
                item.setPosnr(entryInfo.getLineNumber());
                list.add(item);
                condtionList.add(condtionItem);
            }
        }
    }
}