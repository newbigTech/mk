package com.hand.markor.configurator.art241.price.controllers;

import com.hand.common.util.ExcelUtil;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hmall.services.utils.BeanConvertUtil;
import com.hand.markor.configurator.art241.price.dto.ArtPriceRowDto;
import com.markor.map.configurator.art241.price.dto.MstPriceRow;
import com.markor.map.configurator.art241.price.service.IMstPriceService;
import com.markor.map.framework.common.exception.BusinessException;
import com.markor.map.framework.common.exception.InvalidDataException;
import com.markor.map.framework.common.interf.entities.ResponseData;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @Author:zhangyanan
 * @Description: 241价格行维护导入界面
 * @Date:Crated in 16:31 2018/2/27
 * @Modified By:
 */
@RestController
public class ArtPriceRowController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(ArtPriceRowController.class);

    @Autowired
    private IMstPriceService mstPriceService;

    @RequestMapping(value = "/markor/art241/price/artPriceRow/query")
    public ResponseData query(HttpServletRequest request, MstPriceRow mstPriceRow,
                              @RequestParam(defaultValue = DEFAULT_PAGE) int pageNum, @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(mstPriceService.queryList(requestContext, mstPriceRow, pageNum, pageSize));
    }

    /**
     * 下载价格维护导入Excel模板
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/markor/art241/price/artPriceRow/downloadExcelTemplate", method = RequestMethod.GET)
    public void downloadExcelTemplate(HttpServletRequest request, HttpServletResponse response) {
        try {
            new ExcelUtil(ArtPriceRowDto.class)
                    .downloadExcelModel(request, response, "template/241价格行维护导入模板.xlsx");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 价格行导入
     *
     * @param file
     * @return ResponseData
     * @throws Exception
     */
    @RequestMapping(value = "/markor/art241/price/artPriceRow/importExcel", method = RequestMethod.POST)
    public ResponseData importExcel(MultipartFile file) {
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);
        responseData.setMessage("excel导入成功！");
        List<ArtPriceRowDto> list = null;
        try {
            //将excel数据转换为List
            list = new ExcelUtil<>(ArtPriceRowDto.class).importExcel(file.getOriginalFilename(), ArtPriceRowDto.DEFAULT_SHEET_NAME, file.getInputStream());
        } catch (Exception e) {
            logger.error("excel解析失败:", e);
            responseData.setSuccess(false);
            responseData.setMessage("excel解析失败,请联系管理员！");
            return responseData;
        }
        //判断excel数据是否非空
        if (CollectionUtils.isNotEmpty(list)) {
            try {
                //校验excel数据合法性
                checkExcelData(list);
                List<MstPriceRow> mstPriceRowList = BeanConvertUtil.copyListProperties(list, MstPriceRow.class);
                //保存数据
                mstPriceService.saveArtPriceRow(mstPriceRowList);
            } catch (InvalidDataException e) {
                logger.error("数据校验失败:", e);
                responseData.setSuccess(false);
                responseData.setMessage(e.getMessage());
                return responseData;
            } catch (BusinessException e) {
                logger.error("业务校验失败:", e);
                responseData.setSuccess(false);
                responseData.setMessage(e.getMessage());
                return responseData;
            }
        } else {
            responseData.setSuccess(false);
            responseData.setMessage("请下载Excel模板并输入数据！");
        }
        return responseData;
    }

    /**
     * Excel数据合法性校验
     *
     * @param artPriceRowDtoList
     * @throws InvalidDataException
     */
    private void checkExcelData(List<ArtPriceRowDto> artPriceRowDtoList) throws InvalidDataException {
        checkIsNotNull(artPriceRowDtoList);
        checkAttributeLength(artPriceRowDtoList);
    }

    /**
     * 判断Excel价格行非空校验
     *
     * @param artPriceRowDtoList
     * @throws InvalidDataException
     */
    private void checkIsNotNull(List<ArtPriceRowDto> artPriceRowDtoList) throws InvalidDataException {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < artPriceRowDtoList.size(); i++) {
            ArtPriceRowDto artPriceRowDto = artPriceRowDtoList.get(i);
            if (null == artPriceRowDto.getProductCode() || "".equals(artPriceRowDto.getProductCode().trim())) {
                sb.append("第" + (i + 2) + "行数据的商品编码不能为空！");
            }
            if (null == artPriceRowDto.getBasePrice()) {
                sb.append("第" + (i + 2) + "行数据的基础销售价格不能为空！");
            }
            if (null == artPriceRowDto.getSaleUnit() || "".equals(artPriceRowDto.getSaleUnit().trim())) {
                sb.append("第" + (i + 2) + "行数据的销售使用单位不能为空！");
            }
            if (null == artPriceRowDto.getPriceType() || "".equals(artPriceRowDto.getPriceType().trim())) {
                sb.append("第" + (i + 2) + "行数据的价格类型不能为空！");
            }
            if (null == artPriceRowDto.getPriceGroup() || "".equals(artPriceRowDto.getPriceGroup().trim())) {
                sb.append("第" + (i + 2) + "行数据的价目表不能为空！");
            }
            if (null == artPriceRowDto.getFlag() || "".equals(artPriceRowDto.getFlag().trim())) {
                sb.append("第" + (i + 2) + "行数据的是否覆盖原纪录不能为空！");
            }
        }
        if (sb.length() > 0) {
            throw new InvalidDataException(sb.toString());
        }
    }

    /**
     * 校验Excel属性值是否合法
     *
     * @param artPriceRowDtoList
     * @throws InvalidDataException
     */
    private void checkAttributeLength(List<ArtPriceRowDto> artPriceRowDtoList) throws InvalidDataException {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < artPriceRowDtoList.size(); i++) {
            ArtPriceRowDto artPriceRowDto = artPriceRowDtoList.get(i);
            if (!matchingPriceFormat(artPriceRowDto.getBasePrice())) {
                sb.append("第" + (i + 2) + "行数据的基础销售价格字段有误,必须为数字且最多有两位小数！");
            }
            if (null != artPriceRowDto.getBottomPrice()) {
                if (!matchingPriceFormat(artPriceRowDto.getBottomPrice())) {
                    sb.append("第" + (i + 2) + "行数据最低价字段有误,必须为数字且最多有两位小数！");
                }
            }
            if (StringUtils.isNotEmpty(artPriceRowDto.getEnableTime())) {
                if (!matchingDateFormat(artPriceRowDto.getEnableTime())) {
                    sb.append("第" + (i + 2) + "行数据的开始时间格式有误,时间格式为：XXXX-XX-XX！");
                }
            }
            if (StringUtils.isNotEmpty(artPriceRowDto.getDisableTime())) {
                if (!matchingDateFormat(artPriceRowDto.getDisableTime())) {
                    sb.append("第" + (i + 2) + "行数据的结束时间格式有误,时间格式为：XXXX-XX-XX！");
                }
            }
            if (StringUtils.isNotEmpty(artPriceRowDto.getIsBottom())) {
                if (!StringUtils.isNotEmpty(artPriceRowDto.getIsBottom())
                        && ("Y".equals(artPriceRowDto.getIsBottom().trim()) || "N".equals(artPriceRowDto.getIsBottom().trim()))) {
                    sb.append("第" + (i + 2) + "行数据的一口价的赋值有误,应为Y或N！");
                }
            }
        }
        if (sb.length() > 0) {
            throw new InvalidDataException(sb.toString());
        }
    }

    /**
     * 验证时间格式是否正确
     *
     * @param time
     * @return
     */
    private boolean matchingDateFormat(String time) {
        Pattern pattern = Pattern.compile("\\d{4}(\\-)\\d{1,2}\\1\\d{1,2}$");
        return pattern.matcher(time).matches();
    }

    /**
     * 验证价格格式是否正确
     *
     * @param price
     * @return
     */
    private boolean matchingPriceFormat(BigDecimal price) {
        Pattern pattern = Pattern.compile("^\\d*(\\.)?\\d{1,2}$");
        return pattern.matcher(String.valueOf(price)).matches();
    }
}
