package com.hand.hmall.om.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.code.ISystemCodeService;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.mam.dto.MamVcodeHeader;
import com.hand.hap.mam.service.IMamVcodeHeaderService;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.mst.dto.Product;
import com.hand.hmall.mst.mapper.CatalogversionMapper;
import com.hand.hmall.mst.service.IProductService;
import com.hand.hmall.om.dto.OmDiscountEntry;
import com.hand.hmall.om.dto.OmDiscountEntryTemplate;
import com.hand.hmall.om.dto.OmDiscountNotice;
import com.hand.hmall.om.mapper.OmDiscountEntryMapper;
import com.hand.hmall.om.service.IOmDiscountEntryService;
import com.hand.hmall.om.service.IOmDiscountNoticeService;
import com.hand.hmall.util.Constants;
import com.markor.map.framework.common.interf.entities.PaginatedList;
import com.markor.map.framework.restclient.RestClient;
import okhttp3.Response;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zhangmeng
 * @name OmDiscountEntryServiceImpl
 * @description 折扣行
 * @date 2017/11/28
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OmDiscountEntryServiceImpl extends BaseServiceImpl<OmDiscountEntry> implements IOmDiscountEntryService {

    @Autowired
    OmDiscountEntryMapper mapper;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private RestClient restClient;
    @Autowired
    private OmDiscountEntryMapper omDiscountEntryMapper;
    @Autowired
    private CatalogversionMapper catalogversionMapper;
    @Autowired
    private IProductService productService;
    @Autowired
    private IMamVcodeHeaderService iMamVcodeHeaderService;
    @Autowired
    private IOmDiscountNoticeService omDiscountNoticeService;
    @Autowired
    private ISystemCodeService iSystemCodeServiceProvider;

    private Logger logger = LoggerFactory.getLogger(getClass());

    private Date now = new Date();

    @Override
    public List<OmDiscountEntry> queryDiscountEntryInfo(OmDiscountEntry dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return mapper.queryDiscountEntryInfo(dto);
    }

    /**
     * 同步折扣行至M3D
     *
     * @param omDiscountEntryList
     * @return
     */
    @Override
    public ResponseData syncM3DDiscountEntry(List<OmDiscountEntry> omDiscountEntryList) {
        ResponseData responseData = new ResponseData();
        if (omDiscountEntryList != null && omDiscountEntryList.size() > 0) {
            //调用的相对地址
            String url = "/modules/strawberry/webservice/discountpriceAccept";
            //请求参数
            String jsonString = JSON.toJSONString(omDiscountEntryList);
            Map<String, String> map = new HashMap<>();
            Map<String, String> headers = new HashMap<>();
            Response response;
            try {
                response = restClient.postString(Constants.M3D, url, jsonString, "json", map, headers);
                if (response.code() == 200) {
                    responseData.setSuccess(true);
                } else {
                    responseData.setMessage("同步失败");
                    responseData.setSuccess(false);
                }
            } catch (IOException e) {
                e.printStackTrace();
                responseData.setSuccess(false);
                responseData.setMessage(e.toString());
            }
        } else {
            responseData.setSuccess(false);
            responseData.setMessage("没有需要同步的折扣行");
        }
        return responseData;
    }

    /**
     * 删除折扣行
     *
     * @param omDiscountEntryList
     * @return
     */
    @Override
    public ResponseData delDiscountEntry(List<OmDiscountEntry> omDiscountEntryList) {
        ResponseData responseData = new ResponseData();
        List<OmDiscountEntry> syncList = new ArrayList<>();
        //查询美克stage版本ID
        Long catalogversionId = selectCatalogversionId("markor", "staged");

        if (omDiscountEntryList != null && omDiscountEntryList.size() > 0) {
            //将启用标志位改为N
            for (OmDiscountEntry omDiscountEntry : omDiscountEntryList) {
                //乐观锁
                OmDiscountEntry check = omDiscountEntryMapper.selectByPrimaryKey(omDiscountEntry);

                if (check == null) {
                    responseData.setMessage("数据错误，请先保存数据");
                    responseData.setSuccess(false);
                    throw new RuntimeException("数据错误，请先保存数据");
                }

                //只有staged版本可以编辑
                if ((long) catalogversionId != omDiscountEntry.getCatalogversionId()) {
                    responseData.setMessage("只能编辑staged版本数据");
                    responseData.setSuccess(false);
                    throw new RuntimeException("只能编辑staged版本数据");
                }

                if (check != null && (check.getObjectVersionNumber() == omDiscountEntry.getObjectVersionNumber())) {
                    omDiscountEntry.setIsHandle("N");
                    omDiscountEntry.setSyncflag("Y");
                    omDiscountEntryMapper.updateByPrimaryKeySelective(omDiscountEntry);

                    //查找是否有online数据
                    omDiscountEntry.setStagedDiscountId(omDiscountEntry.getDiscountId());
                    List<OmDiscountEntry> omListStaged = omDiscountEntryMapper.queryDiscountEntryByOnline(omDiscountEntry);
                    //将online数据状态改为停用 并推送给M3D
                    if (CollectionUtils.isNotEmpty(omListStaged)) {
                        omListStaged.get(0).setIsHandle("N");
                        omListStaged.get(0).setSyncflag("Y");
                        omDiscountEntryMapper.updateByPrimaryKeySelective(omListStaged.get(0));
                        syncList.add(omListStaged.get(0));

                        //将通知列表中的数据删除
                        OmDiscountNotice omDiscountNotice = new OmDiscountNotice();
                        omDiscountNotice.setDiscountId(omListStaged.get(0).getDiscountId());

                        //查询当前折扣行数据 并根据V码去重
                        List<OmDiscountNotice> omDiscountNoticeList = omDiscountNoticeService.select(RequestHelper.newEmptyRequest(), omDiscountNotice, 1, Integer.MAX_VALUE);
                        if (CollectionUtils.isNotEmpty(omDiscountEntryList)) {
                            omDiscountNoticeList = removeDuplicateOmDiscountNotice(omDiscountNoticeList);
                            //将以前的数据删除
                            omDiscountNoticeService.deleteByDiscountId(omDiscountNotice);

                            for (OmDiscountNotice om : omDiscountNoticeList) {
                                //记录一条按照当前系统时间价格推送信息
                                om.setNoticeTime(new Date());
                                om.setNoticeId(null);
                                omDiscountNoticeService.insertSelective(RequestHelper.newEmptyRequest(), om);
                            }
                        }
                    }
                } else {
                    responseData.setSuccess(false);
                    responseData.setMessage("请刷新页面重试");
                    throw new RuntimeException("数据发生变化，请刷新页面重试");
                }

            }
            //推送M3D
            if (CollectionUtils.isNotEmpty(syncList)) {
                responseData = syncM3DDiscountEntry(syncList);
                if (!responseData.isSuccess()) {
                    throw new RuntimeException("同步失败，请刷新页面重试");
                }
            }
            responseData.setSuccess(true);
        } else {
            responseData.setSuccess(false);
            responseData.setMessage("折扣行为空");
        }
        return responseData;
    }

    /**
     * 保存折扣行
     *
     * @param omDiscountEntryList
     * @return
     */
    @Override
    public ResponseData saveDiscountEntry(List<OmDiscountEntry> omDiscountEntryList) {
        ResponseData responseData = new ResponseData();
        //查询美克stage版本ID
        Long catalogversionId = selectCatalogversionId("markor", "staged");

        //查询美克online版本ID
        Long onlineId = selectCatalogversionId("markor", "online");

        if (CollectionUtils.isNotEmpty(omDiscountEntryList)) {
            //数据校验
            for (OmDiscountEntry omDiscountEntry : omDiscountEntryList) {
                responseData.setSuccess(false);

                if (omDiscountEntry.getStartTime() == null) {
                    responseData.setMessage("开始时间不能为空");
                    return responseData;
                }

                if (omDiscountEntry.getEndTime() == null) {
                    responseData.setMessage("结束时间不能为空");
                    return responseData;
                }

                //判断时间范围
                if (omDiscountEntry.getStartTime().after(omDiscountEntry.getEndTime())) {
                    responseData.setMessage("开始时间应小于结束时间");
                    return responseData;
                }
                //折扣值应大于0
                if (omDiscountEntry.getDiscount() < 0) {
                    responseData.setMessage("折扣值不能小于0");
                    return responseData;
                }

                //平台打折折扣值应大于0小于1
                if (omDiscountEntry.getDiscountType().intValue() == 2 && (omDiscountEntry.getDiscount() < 0 || omDiscountEntry.getDiscount() > 1)) {
                    responseData.setMessage("平台打折折扣值应大于0小于1");
                    return responseData;
                }

                //根据商品CODE查询商品定制类型
                Product product = new Product();
                product.setCode(omDiscountEntry.getProductCode());
                product.setCatalogversionId(onlineId);
                product = productService.selectProductByCodeAndVersion(product);

                //当custom_suppor_type=1或3时，折扣类型只能为商品直降
                if (Constants.ONE.equals(product.getCustomSupportType()) || Constants.THREE.equals(product.getCustomSupportType())) {
                    if (omDiscountEntry.getDiscountType().intValue() != 3) {
                        responseData.setMessage("商品为主推或饰品，折扣类型只能为商品直降");
                        return responseData;
                    }
                }

                //当DISCOUNT_TYPE为3(商品直降)，且CODE为定制平台(商品表RODUCT_ID对应CUSTOM_SUPPORT_TYPE=2，则VCODE为必填项
                if (omDiscountEntry.getDiscountType().intValue() == 3 && Constants.TWO.equals(product.getCustomSupportType())) {
                    if (StringUtils.isEmpty(omDiscountEntry.getVcode())) {
                        responseData.setMessage("定制支持类型为定制平台且折扣类型为定制直降的商品 商品V码不能为空");
                        return responseData;
                    }
                    //填入的VCODE需要在HAP_MAM_VCODE_HEADER进行查询；
                    //当product_id对应商品表中CODE=HAP_MAM_VCODE_HEADER中PLATFROM_CODE时，取出HAP_MAM_VCODE_HEADER中该平台号下所有Vcode，若填入的VCODE不包含在内，则报错；
                    else {
                        if (!checkVcode(omDiscountEntry.getProductCode(), omDiscountEntry.getVcode())) {
                            responseData.setMessage("商品V码在V码头表中不存在");
                            return responseData;
                        }
                    }
                }

                //当DISCOUNT_TYPE为3(商品直降)，且CODE为定制平台(商品表RODUCT_ID对应CUSTOM_SUPPORT_TYPE=2，则VCODE为必填项
                if ((omDiscountEntry.getDiscountType().intValue() == 1 || omDiscountEntry.getDiscountType().intValue() == 2) && Constants.TWO.equals(product.getCustomSupportType())) {
                    if (StringUtils.isNotEmpty(omDiscountEntry.getVcode())) {
                        responseData.setMessage("折扣类型为平台直降或者平台打折时，且商品为定制平台，V码必须为空");
                        return responseData;
                    }
                }

                //当折扣类型为“商品直降”时 当custom_suppor_type=1或3时，V码不可填
                if (omDiscountEntry.getDiscountType().intValue() == 3 && (Constants.ONE.equals(product.getCustomSupportType()) || Constants.THREE.equals(product.getCustomSupportType()))) {
                    if (StringUtils.isNotEmpty(omDiscountEntry.getVcode())) {
                        responseData.setMessage("折扣类型为商品直降时，且商品主推或饰品，V码必须为空");
                        return responseData;
                    }
                }


                //判断时间段唯一性
                omDiscountEntry.setCatalogversionId(catalogversionId);
                List<OmDiscountEntry> checkTimeList = omDiscountEntryMapper.queryDiscountEntryByTime(omDiscountEntry);
                for (OmDiscountEntry time : checkTimeList) {
                    if (isOverlap(omDiscountEntry.getStartTime(), omDiscountEntry.getEndTime(), time.getStartTime(), time.getEndTime())) {
                        responseData.setMessage("同一商品,同一时间段内,只能有一条折扣行信息");
                        return responseData;
                    }
                }
                OmDiscountEntry check = omDiscountEntryMapper.selectByPrimaryKey(omDiscountEntry);
                if (check != null) {
                    //商品编码不能修改
                    Product checkProduct = new Product();
                    checkProduct.setProductId(check.getProductId());
                    checkProduct = productService.selectByPrimaryKey(RequestHelper.newEmptyRequest(), checkProduct);
                    if (!checkProduct.getCode().equals(omDiscountEntry.getProductCode())) {
                        responseData.setMessage("商品编码不能修改，请添加一条新数据");
                        return responseData;
                    }
                    //V码不能修改
                    if (StringUtils.isNotEmpty(check.getVcode())) {
                        if (!check.getVcode().equals(omDiscountEntry.getVcode() != null ? omDiscountEntry.getVcode() : "")) {
                            responseData.setMessage("V码不能修改，请添加一条新数据");
                            return responseData;
                        }
                    }
                    //乐观锁
                    if (check.getObjectVersionNumber() != omDiscountEntry.getObjectVersionNumber()) {
                        responseData.setMessage("数据发生变化，请刷新页面重试");
                        return responseData;
                    }
                    //只有staged版本可以编辑
                    if ((long) catalogversionId != omDiscountEntry.getCatalogversionId()) {
                        responseData.setMessage("只能编辑staged版本数据");
                        return responseData;
                    }
                }
            }

            //保存数据
            for (OmDiscountEntry omDiscountEntry : omDiscountEntryList) {
                OmDiscountEntry check = omDiscountEntryMapper.selectByPrimaryKey(omDiscountEntry);
                if (check == null) {
                    omDiscountEntry.setCatalogversionId(catalogversionId);
                    omDiscountEntryMapper.insertSelective(omDiscountEntry);
                } else {
                    omDiscountEntry.setSyncflag("N");
                    omDiscountEntryMapper.updateByPrimaryKeySelective(omDiscountEntry);
                }
            }
            responseData.setSuccess(true);
        }
        return responseData;
    }

    /**
     * 同步折扣行
     * flag为1 同步选择的折扣行 flag为2全部同步
     *
     * @param omDiscountEntryList
     * @param flag
     * @return
     */
    @Override
    public ResponseData syncDiscountEntry(IRequest request, List<OmDiscountEntry> omDiscountEntryList, String flag) {
        ResponseData responseData = new ResponseData();
        List<OmDiscountEntry> syncList = new ArrayList<>();

        //查询美克online版本ID
        Long onlineId = selectCatalogversionId("markor", "online");

        //查询美克staged版本ID
        Long stagedId = selectCatalogversionId("markor", "staged");
        //选择同步
        if ("1".equals(flag)) {
            //数据校验
            for (OmDiscountEntry omDiscountEntry : omDiscountEntryList) {
                responseData.setSuccess(false);
                //校验版本
                if ((long) onlineId == (long) omDiscountEntry.getCatalogversionId()) {
                    responseData.setMessage("请选择staged版本数据同步");
                    return responseData;
                }

                OmDiscountEntry check = omDiscountEntryMapper.selectByPrimaryKey(omDiscountEntry);
                if (check != null) {
                    //乐观锁
                    if ((long) check.getObjectVersionNumber() != (long) omDiscountEntry.getObjectVersionNumber()) {
                        responseData.setMessage("数据发生变化，请刷新页面重试");
                        return responseData;
                    }
                } else {
                    responseData.setMessage("数据发生变化，请刷新页面重试");
                    return responseData;
                }
            }
            //同步数据
            for (OmDiscountEntry omDiscountEntry : omDiscountEntryList) {
                syncList.add(syncOnline(omDiscountEntry, request, onlineId));
            }
            //推送M3D
            responseData = syncM3DDiscountEntry(syncList);
            if (!responseData.isSuccess()) {
                throw new RuntimeException("同步失败，请刷新页面重试");
            }
            return responseData;
        }
        //全部同步
        else {
            //查询所有staged版本未同步数据
            OmDiscountEntry condition = new OmDiscountEntry();
            condition.setCatalogversionId(stagedId);
            condition.setSyncflag("N");
            List<OmDiscountEntry> omListStaged = omDiscountEntryMapper.select(condition);

            if (CollectionUtils.isNotEmpty(omListStaged)) {
                for (OmDiscountEntry omDiscountEntry : omListStaged) {
                    syncList.add(syncOnline(omDiscountEntry, request, onlineId));
                }
            }
        }
        //推送M3D
        responseData = syncM3DDiscountEntry(syncList);
        if (!responseData.isSuccess()) {
            throw new RuntimeException("同步失败，请刷新页面重试");
        }
        return responseData;
    }

    /**
     * 从stage同步至online
     *
     * @param omDiscountEntry
     * @param request
     * @param onlineId
     * @return
     */

    private OmDiscountEntry syncOnline(OmDiscountEntry omDiscountEntry, IRequest request, Long onlineId) {
        OmDiscountEntry result;
        omDiscountEntry.setStagedDiscountId(omDiscountEntry.getDiscountId());
        List<OmDiscountEntry> omListStaged = omDiscountEntryMapper.queryDiscountEntryByOnline(omDiscountEntry);
        if (CollectionUtils.isNotEmpty(omListStaged)) {
            //更新online版本
            omListStaged.get(0).setDiscount(omDiscountEntry.getDiscount());
            omListStaged.get(0).setDiscountType(omDiscountEntry.getDiscountType());
            omListStaged.get(0).setStartTime(omDiscountEntry.getStartTime());
            omListStaged.get(0).setEndTime(omDiscountEntry.getEndTime());
            omListStaged.get(0).setCatalogversionId(onlineId);
            omListStaged.get(0).setIsHandle("Y");
            omDiscountEntryMapper.updateByPrimaryKeySelective(omListStaged.get(0));
            result = omListStaged.get(0);
            //修改同步标志位
            omDiscountEntry.setSyncflag("Y");
            omDiscountEntry.setStagedDiscountId(null);
            omDiscountEntryMapper.updateByPrimaryKeySelective(omDiscountEntry);
        } else {
            //修改同步标志位
            omDiscountEntry.setSyncflag("Y");
            omDiscountEntry.setStagedDiscountId(null);
            omDiscountEntryMapper.updateByPrimaryKeySelective(omDiscountEntry);
            //新建online数据
            omDiscountEntry.setStagedDiscountId(omDiscountEntry.getDiscountId());
            omDiscountEntry.setDiscountId(null);
            omDiscountEntry.setCatalogversionId(onlineId);
            omDiscountEntry.setIsHandle("Y");
            result = this.insertSelective(request, omDiscountEntry);
        }

        if (result != null) {
            //查询商品V码平台号
            Product product = new Product();
            product.setProductId(result.getProductId());
            product = productService.selectByPrimaryKey(request, product);

            if (StringUtils.isNotEmpty(result.getVcode())) {
                result.setPlatformCode(result.getProductCode());
                result.setvProductCode(result.getVcode());
            } else {
                if (product != null && StringUtils.isNotEmpty(product.getvProductCode())) {
                    result.setPlatformCode(product.getPlatformCode());
                    result.setvProductCode(product.getvProductCode());
                } else {
                    result.setPlatformCode(product.getCode());
                    result.setvProductCode("");
                }
            }

            //只有CustomSupportType为2时才推送
            if (Constants.TWO.equals(product.getCustomSupportType())) {
                OmDiscountNotice omDiscountNotice = new OmDiscountNotice();
                omDiscountNotice.setDiscountId(result.getDiscountId());

                //删除标志位 如果促销不在时间段内 则不再执行删除
                boolean deleteFlag = true;

                //判断促销是否在生效时间内
                if (!(!now.before(omDiscountEntry.getStartTime()) && !now.after(omDiscountEntry.getEndTime()))) {
                    //修改后促销结束了 删除以前保存的通知信息，并立刻推送一条数据
                    List<OmDiscountNotice> omDiscountNoticeList = removeDuplicateOmDiscountNotice(omDiscountNoticeService.select(RequestHelper.newEmptyRequest(), omDiscountNotice, 1, Integer.MAX_VALUE));

                    //将以前的数据删除
                    omDiscountNoticeService.deleteByDiscountId(omDiscountNotice);

                    if (CollectionUtils.isNotEmpty(omDiscountNoticeList)) {
                        for (OmDiscountNotice om : omDiscountNoticeList) {
                            //记录一条按照当前系统时间价格推送信息
                            om.setNoticeTime(new Date());
                            om.setNoticeId(null);
                            omDiscountNoticeService.insertSelective(RequestHelper.newEmptyRequest(), om);
                        }
                    }
                    deleteFlag = false;
                }

                if (deleteFlag) {
                    //将以前的数据删除
                    omDiscountNoticeService.deleteByDiscountId(omDiscountNotice);
                }

                //判断VCODE列是否为空，不为空，则通知该V码下的价格
                if (StringUtils.isNotEmpty(result.getVcode())) {
                    insertDiscountNotice(omDiscountNotice, product.getCode(), result.getVcode(), result);
                }
                //若V码为空，则从HAP_MAM_VCODE_HEADER查询所有对应平台下，非主推款V码（TYPE_CODE=D/DT)；
                else {
                    List<MamVcodeHeader> headerList = iMamVcodeHeaderService.selectByPlatformCodeAndTypeCode(product.getCode());
                    if (CollectionUtils.isNotEmpty(headerList)) {
                        for (MamVcodeHeader mamVcodeHeader1 : headerList) {
                            insertDiscountNotice(omDiscountNotice, mamVcodeHeader1.getPlatformCode(), mamVcodeHeader1.getVcode(), result);
                        }
                    }
                }
            }
        }
        return result;
    }

    private void insertDiscountNotice(OmDiscountNotice omDiscountNotice, String platFormCode, String vcode, OmDiscountEntry result) {
        //新增数据：折扣行主键、平台号、通知时间（分开始时间和结束时间同时记录两条数据，对于促销结束时间对应的通知时间=结束时间+10分钟）
        omDiscountNotice.setPlatfromCode(platFormCode);
        omDiscountNotice.setVcode(vcode);
        omDiscountNotice.setNoticeTime(result.getStartTime());
        omDiscountNoticeService.insertSelective(RequestHelper.newEmptyRequest(), omDiscountNotice);

        //结束时间的数据
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(result.getEndTime());
        calendar.add(Calendar.MINUTE, 10);
        omDiscountNotice.setNoticeTime(calendar.getTime());
        omDiscountNoticeService.insertSelective(RequestHelper.newEmptyRequest(), omDiscountNotice);
    }

    @Override
    public List<OmDiscountEntry> queryDiscountEntryByProductIdAndDiscountType(OmDiscountEntry dto) {
        return mapper.queryDiscountEntryByProductIdAndDiscountType(dto);
    }

    @Override
    public void importDiscountEntry(List<OmDiscountEntryTemplate> infos) throws Exception {
        int i = 0;
        boolean importResult = false;
        Long productId = null;
        List<OmDiscountEntry> discountEntries = new ArrayList<OmDiscountEntry>();
        //用来检验EXCEL中同一商品时间范围是否重叠
        List<OmDiscountEntryTemplate> discountEntriesTemplateForNow = new ArrayList<OmDiscountEntryTemplate>();

        //查询美克staged版本ID
        Long stagedId = selectCatalogversionId("markor", "staged");
        //查询美克online版本ID
        Long onlineId = selectCatalogversionId("markor", "online");
        // 文件输入流
        InputStream is = null;
        if (infos.size() == 0) {
            throw new Exception("请下载Excel模板并输入数据");
        }
        for (OmDiscountEntryTemplate info : infos) {
            i++;
            OmDiscountEntry omDiscountEntry = new OmDiscountEntry();
            String discountType = iSystemCodeServiceProvider.getCodeValueByMeaning("HMALL_DISCOUNT_TYPE", info.getDiscountType()).getValue();
            Product product = new Product();
            product.setCode(info.getProductCode());
            product.setCatalogversionId(onlineId);
            product = productService.selectProductByCodeAndVersion(product);
            if (product != null) {
                productId = product.getProductId();
            }
            uploadValidate(info, omDiscountEntry, discountEntriesTemplateForNow, discountType, product, i);
            //将正确的数据插入数据库
            omDiscountEntry.setProductId(productId);
            omDiscountEntry.setCatalogversionId(stagedId);
            omDiscountEntry.setDiscountType(Long.valueOf(discountType));
            omDiscountEntry.setDiscount(info.getDiscount());
            omDiscountEntry.setStartTime(info.getStartTime());
            omDiscountEntry.setEndTime(info.getEndTime());
            omDiscountEntry.setVcode(info.getVcode());
            discountEntries.add(omDiscountEntry);
        }
        importResult = true;

        if (importResult == true) {
            mapper.batchInsertDiscountEntry(discountEntries);
        }
    }


    /**
     * 校验两个时间段是否有交集
     * true为有交集
     *
     * @param leftStartDate
     * @param leftEndDate
     * @param rightStartDate
     * @param rightEndDate
     * @return
     */
    private boolean isOverlap(Date leftStartDate, Date leftEndDate, Date rightStartDate, Date rightEndDate) {
        return
                ((leftStartDate.getTime() >= rightStartDate.getTime())
                        && leftStartDate.getTime() < rightEndDate.getTime())
                        ||
                        ((leftStartDate.getTime() > rightStartDate.getTime())
                                && leftStartDate.getTime() <= rightEndDate.getTime())
                        ||
                        ((rightStartDate.getTime() >= leftStartDate.getTime())
                                && rightStartDate.getTime() < leftEndDate.getTime())
                        ||
                        ((rightStartDate.getTime() > leftStartDate.getTime())
                                && rightStartDate.getTime() <= leftEndDate.getTime());

    }

    /**
     * 查询版本ID
     *
     * @param code
     * @param catalogversion
     * @return
     */
    private Long selectCatalogversionId(String code, String catalogversion) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("code", code);
        paramMap.put("catalogversion", catalogversion);
        return catalogversionMapper.getOnlineCatalogversionId(paramMap);
    }

    /**
     * 验证V码
     *
     * @param code
     * @param vcode
     * @return
     */
    private boolean checkVcode(String code, String vcode) {
        //填入的VCODE需要在HAP_MAM_VCODE_HEADER进行查询；
        //当product_id对应商品表中CODE=HAP_MAM_VCODE_HEADER中PLATFROM_CODE时，取出HAP_MAM_VCODE_HEADER中该平台号下所有Vcode，若填入的VCODE不包含在内，则报错；
        MamVcodeHeader mamVcodeHeader = new MamVcodeHeader();
        mamVcodeHeader.setPlatformCode(code);
        PaginatedList<MamVcodeHeader> headerList = iMamVcodeHeaderService.selectList(mamVcodeHeader, RequestHelper.newEmptyRequest(), 1, Integer.MAX_VALUE);

        List<MamVcodeHeader> mamVcodeHeaderList = headerList.getRows();
        for (MamVcodeHeader mamVcodeHead : mamVcodeHeaderList) {
            if (vcode.equals(mamVcodeHead.getVcode())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 校验导入数据
     *
     * @param info
     * @param omDiscountEntry
     * @param discountEntriesTemplateForNow
     * @param discountType
     * @param product
     * @param i
     * @throws Exception
     */
    private void uploadValidate(OmDiscountEntryTemplate info, OmDiscountEntry omDiscountEntry, List<OmDiscountEntryTemplate> discountEntriesTemplateForNow, String discountType, Product product, int i) throws Exception {
        //查询美克staged版本ID
        Long stagedId = selectCatalogversionId("markor", "staged");
        if (info.getProductCode() == null || info.getProductCode().trim().equals("")) {
            throw new Exception("第" + (i + 1) + "条数据的商品编号不能为空");
        }
        if (info.getDiscountType() == null || info.getDiscountType().trim().equals("")) {
            throw new Exception("第" + (i + 1) + "条数据的折扣类型不能为空");
        }
        if (info.getDiscount() == null) {
            throw new Exception("第" + (i + 1) + "条数据的折扣不能为空且只能是数字");
        }
        if (info.getStartTime() == null) {
            throw new Exception("第" + (i + 1) + "条数据的开始时间不能为空");
        }
        if (info.getEndTime() == null) {
            throw new Exception("第" + (i + 1) + "条数据的结束时间不能为空");
        }
        //根据导入的商品编码和目录版本判断是否存在对应的商品,不存在则报错
        if (product == null) {
            throw new Exception("第" + (i + 1) + "条数据markor-online目录版本商品编码对应的商品不存在");
        }
        if (discountType == null) {
            throw new Exception("第" + (i + 1) + "条数据的折扣类型不正确");
        }

        //当custom_suppor_type=1或3时，折扣类型只能为商品直降
        if (Constants.ONE.equals(product.getCustomSupportType()) || Constants.THREE.equals(product.getCustomSupportType())) {
            if (!Constants.THREE.equals(discountType)) {
                throw new Exception("第" + (i + 1) + "条数据商品为主推或饰品，折扣类型只能为商品直降");
            }
        }

        /* 当DISCOUNT_TYPE为3(商品直降)，且商品编码对应的CUSTOM_SUPPORT_TYPE为定制平台(CUSTOM_SUPPORT_TYPE=2)，则VCODE为必填项,当VCODE必填时,VCODE需要在HAP_MAM_VCODE_HEADER进行查询,
           当商品表中CODE=HAP_MAM_VCODE_HEADER中PLATFROM_CODE时，取出HAP_MAM_VCODE_HEADER中该平台号下所有Vcode，若填入的VCODE不包含在内，则报错；*/
        if (discountType.equals("3") && product.getCustomSupportType().equals("2")) {
            if (StringUtil.isEmpty(info.getVcode())) {
                throw new Exception("第" + (i + 1) + "条数据定制支持类型为定制平台且折扣类型为定制直降的商品 商品V码不能为空");
            } else {
                if (!checkVcode(info.getProductCode(), info.getVcode())) {
                    throw new Exception("第" + (i + 1) + "条数据商品的商品V码在V码头表中不存在");
                }
            }
        }

        //当DISCOUNT_TYPE为3(商品直降)，且CODE为定制平台(商品表RODUCT_ID对应CUSTOM_SUPPORT_TYPE=2，则VCODE为必填项
        if ((Constants.ONE.equals(discountType) || Constants.TWO.equals(discountType)) && Constants.TWO.equals(product.getCustomSupportType())) {
            if (StringUtils.isNotEmpty(info.getVcode())) {
                throw new Exception("第" + (i + 1) + "条数据折扣类型为平台直降或者平台打折时，且商品为定制平台，V码必须为空");
            }
        }

        //当折扣类型为“商品直降”时 当custom_suppor_type=1或3时，V码不可填
        if (Constants.THREE.equals(discountType) && (Constants.ONE.equals(product.getCustomSupportType()) || Constants.THREE.equals(product.getCustomSupportType()))) {
            if (StringUtils.isNotEmpty(info.getVcode())) {
                throw new Exception("第" + (i + 1) + "条数据折扣类型为商品直降时，且商品主推或饰品，V码必须为空");
            }
        }


        //Excel中输入的结束时间必须大于起始时间
        if (info.getEndTime().getTime() <= info.getStartTime().getTime()) {
            throw new Exception("第" + (i + 1) + "条数据的结束时间必须大于起始时间");
        }
        //折扣为数字，最多显示两位小数点
        Double discount = info.getDiscount();
        //获取折扣字段小数点后有几位数字
        int length = (discount + "").length() - ((discount + "").indexOf(".") + 1);
        if (length > 2) {
            throw new Exception("第" + (i + 1) + "条数据的折扣小数点后最多支持两位数字");
        }
        //如果折扣类型小于0,则返回错误提示
        if (discount < 0) {
            throw new Exception("第" + (i + 1) + "条数据的折扣要求大于0");
        }
        if (discountType.equals("2")) {
            if (discount < 0 || discount > 1) {
                throw new Exception("第" + (i + 1) + "条数据折扣类型为平台打折的折扣必须在0-1之间");
            }
        }
        //状态不为已删除的相同商品编码及相同商品V码的商品开始时间与结束时间不能重复,
        for (OmDiscountEntryTemplate omDiscountEntryTemplate : discountEntriesTemplateForNow) {
            if (omDiscountEntryTemplate.getProductCode().equals(info.getProductCode()) && omDiscountEntryTemplate.getVcode().equals(info.getVcode())) {
                if (isOverlap(info.getStartTime(), info.getEndTime(), omDiscountEntryTemplate.getStartTime(), omDiscountEntryTemplate.getEndTime())) {
                    throw new Exception("第" + (i + 1) + "条数据相同商品编码及商品V码的数据在该日期范围已存在");
                }
            }
        }
        discountEntriesTemplateForNow.add(info);
        //该对象作为参数用来查询导入的该行商品在数据库中相同商品编码和商品v码的日期范围是否存在
        OmDiscountEntry discountEntryFor = new OmDiscountEntry();
        discountEntryFor.setProductId(product.getProductId());
        discountEntryFor.setVcode(info.getVcode());
        if (StringUtils.isEmpty(info.getVcode())) {
            discountEntryFor.setVcode(null);
        }
        discountEntryFor.setCatalogversionId(stagedId);
        List<OmDiscountEntry> omDiscountEntries = mapper.queryDiscountEntryByTime(discountEntryFor);
        for (OmDiscountEntry discountEntry : omDiscountEntries) {
            if (isOverlap(info.getStartTime(), info.getEndTime(), discountEntry.getStartTime(), discountEntry.getEndTime())) {
                throw new Exception("第" + (i + 1) + "条数据的商品数据在该日期范围已存在");
            }
        }
    }

    /**
     * 根据V码去重
     *
     * @param omDiscountNotices
     * @return
     */
    private ArrayList<OmDiscountNotice> removeDuplicateOmDiscountNotice(List<OmDiscountNotice> omDiscountNotices) {
        Set<OmDiscountNotice> set = new TreeSet<>((o1, o2) -> o1.getVcode().compareTo(o2.getVcode()));
        set.addAll(omDiscountNotices);
        return new ArrayList<>(set);
    }
}