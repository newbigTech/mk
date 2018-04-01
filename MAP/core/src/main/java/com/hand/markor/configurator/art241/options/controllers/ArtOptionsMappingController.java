package com.hand.markor.configurator.art241.options.controllers;

import com.hand.common.util.ExcelUtil;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.markor.configurator.art241.options.dto.ArtOptionsMappingDto;
import com.hand.markor.configurator.art241.options.dto.ArtOptionsMappingStatus;
import com.markor.map.configurator.art241.options.dto.ArtOptionsMapping;
import com.markor.map.configurator.art241.options.service.IARTOptionsMappingService;
import com.markor.map.framework.common.exception.DataProcessException;
import com.markor.map.framework.common.interf.entities.ResponseData;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author baihua
 * @version 0.1
 * @name ItemValueOptionsController$
 * @description $END$ 选项定义Controller
 * @date 2018/1/29$
 */
@Controller
public class ArtOptionsMappingController extends BaseController {
    @Autowired
    private IARTOptionsMappingService iartOptionsMappingService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 分页查询列表
     *
     * @param artOptionsMapping
     * @param page
     * @param pageSize
     * @param request
     */
    @RequestMapping(value = "/markor/art241/options/selectArtOptionsMapping")
    @ResponseBody
    public ResponseData selectArtOptionsMapping(ArtOptionsMapping artOptionsMapping, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                                @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(iartOptionsMappingService.queryOptionMappings(artOptionsMapping, requestContext, page, pageSize));

    }


    /**
     * 创建修改
     *
     * @param artOptionsMappings
     * @param request
     */
    @RequestMapping(value = "/markor/art241/options/submitArtOptionsMapping")
    @ResponseBody
    public ResponseData submitArtOptionsMapping(HttpServletRequest request, @RequestBody List<ArtOptionsMapping> artOptionsMappings) {
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);
        IRequest requestContext = createRequestContext(request);
        List<String> resultList = checkItemValueOptions(artOptionsMappings);
        if (!resultList.isEmpty()) {
            responseData.setSuccess(false);
            responseData.setMessage("创建信息失败");
            responseData.setRows(resultList);
            return responseData;
        }
        try {
            List<String> serviceResultList = iartOptionsMappingService.batchUpdateOptionsMappings(requestContext, artOptionsMappings);
            if (!serviceResultList.isEmpty()) {
                responseData.setSuccess(false);
                responseData.setMessage("执行失败！");
                responseData.setRows(serviceResultList);
                return responseData;
            }
        } catch (DataProcessException e) {
            logger.error(ArtOptionsMappingController.class.getCanonicalName() + "异常", e);
            responseData.setSuccess(false);
            responseData.setMessage("执行失败！");
            return responseData;
        }
        return responseData;
    }

    /**
     * 删除
     *
     * @param request
     * @param artOptionsMappings
     * @return
     */
    @RequestMapping(value = "/markor/art241/options/removeArtOptionsMapping")
    @ResponseBody
    public ResponseData removeArtOptionsMapping(HttpServletRequest request, @RequestBody List<ArtOptionsMapping> artOptionsMappings) {
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);
        try {
            iartOptionsMappingService.batchDelete(artOptionsMappings);
        } catch (Exception e) {
            logger.error(ArtOptionsMappingController.class.getCanonicalName() + ":选项定义删除失败", e);
            responseData.setSuccess(false);
            responseData.setMessage("选项定义添加失败,请联系管理员！");
        }
        return responseData;
    }


    /**
     * 导出选项定义Excel模板
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/markor/art241/options/downloadExcelTemplate", method = RequestMethod.GET)
    public void downloadExcelTemplate(HttpServletRequest request, HttpServletResponse response) {
        List<ArtOptionsMappingDto> tempList = new ArrayList<>();
        tempList.add(new ArtOptionsMappingDto());
        new ExcelUtil(ArtOptionsMappingDto.class)
                .exportExcel(tempList, ArtOptionsMappingDto.DEFAULT_SHEET_NAME, 0,
                        request, response, ArtOptionsMappingDto.DEFAULT_EXCEL_FILE_NAME);
    }


    /**
     * itemValueOptions Excel导入
     *
     * @param request
     * @param file
     * @return
     */
    @RequestMapping(value = "/markor/art241/options/artOptionsMappingImportExcel", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData artOptionsMappingImportExcel(HttpServletRequest request, MultipartFile file) {
        IRequest iRequest = this.createRequestContext(request);
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);
        //存储实际传递操作对象
        List<ArtOptionsMapping> realList = new ArrayList<ArtOptionsMapping>();
        List<ArtOptionsMappingDto> list;
        try {
            list = new ExcelUtil<>(ArtOptionsMappingDto.class).importExcel(file.getOriginalFilename(), "", file.getInputStream());
        } catch (Exception e) {
            logger.error(ArtOptionsMappingController.class.getCanonicalName() + "异常", e);
            responseData.setSuccess(false);
            responseData.setMessage("excel解析失败,请联系管理员！");
            return responseData;
        }
        if (list != null && list.size() > 0) {
            //校验参数完整性
            List<String> resultList = checkItemValueOptions(list, realList);
            if (resultList != null && resultList.size() > 0) {
                responseData.setSuccess(false);
                responseData.setMessage("导入信息错误");
                responseData.setRows(resultList);
                return responseData;
            }
        }
        try {
            List<String> resultList = iartOptionsMappingService.batchUpdateOptionsMappings(iRequest, realList);
            if (!resultList.isEmpty()) {
                responseData.setSuccess(false);
                responseData.setMessage("导入选项定义数据失败！");
                responseData.setRows(resultList);
                return responseData;
            }
        } catch (Exception e) {
            logger.error(ArtOptionsMappingController.class.getCanonicalName() + "异常", e);
            responseData.setSuccess(false);
            responseData.setMessage("导入选项定义数据失败！");
            return responseData;
        }
        return responseData;
    }


    /**
     * 导入DTO转换
     *
     * @param artOptionsMappingDto
     * @return
     */
    public ArtOptionsMapping convertData(ArtOptionsMappingDto artOptionsMappingDto) {
        ArtOptionsMapping artOptionsMapping = new ArtOptionsMapping();
        artOptionsMapping.set__status("add");
        artOptionsMapping.setOptionCode(artOptionsMappingDto.getOptionCode());
        artOptionsMapping.setOptionMatCode(artOptionsMappingDto.getOptionMatCode());
        artOptionsMapping.setOptionValueMatCode(artOptionsMappingDto.getOptionValueMatCode());
        artOptionsMapping.setPlatformCode(artOptionsMappingDto.getPlatformCode());
        artOptionsMapping.setOptionType(artOptionsMappingDto.getOptionType());
        artOptionsMapping.setFingerprint(artOptionsMappingDto.getFingerprint());
        artOptionsMapping.setEntryNum(artOptionsMappingDto.getEntryNum());
        return artOptionsMapping;
    }


    /**
     * 新建参数完整性校验
     *
     * @param realList
     * @return
     */
    public List<String> checkItemValueOptions(List<ArtOptionsMapping> realList) {

        List<String> resultList = new ArrayList<String>();
        for (int i = 0; i < realList.size(); i++) {
            //校验参数是否为空
            ArtOptionsMapping artOptionsMapping = realList.get(i);
            //记录新建行号
            artOptionsMapping.setEntryNum(i + 1);
            if (StringUtils.isEmpty(artOptionsMapping.getPlatformCode())) {
                resultList.add("第" + i + 1 + "行平台编码不能为空");
            }
            if (StringUtils.isEmpty(artOptionsMapping.getOptionType())) {
                resultList.add("第" + i + 1 + "行选项类型不能为空");
            } else {
                if (!artOptionsMapping.getOptionType().equals(ArtOptionsMappingStatus.FB.name()) && !artOptionsMapping.getOptionType().equals(ArtOptionsMappingStatus.FN.name())) {
                    resultList.add("第" + i + 1 + "行选项类型填写错误");
                }
            }
            if (StringUtils.isEmpty(artOptionsMapping.getOptionCode())) {
                resultList.add("第" + i + 1 + "行选项编码不能为空");
            } else {
                if (artOptionsMapping.getOptionCode().length() != 2) {
                    resultList.add("第" + i + 1 + "行选项编码长度不正确");
                }
            }
            //指纹码生成规则： 平台编码、选项类型、选项编码、配置包编码、物料编码 五个字段值字符串拼接
            String pingerprint = artOptionsMapping.getPlatformCode() + artOptionsMapping.getOptionType() + artOptionsMapping.getOptionCode() + artOptionsMapping.getOptionMatCode() + artOptionsMapping.getOptionValueMatCode();
            artOptionsMapping.setFingerprint(pingerprint);
            //判断创建修改
            if (artOptionsMapping.getId() == null) {
                artOptionsMapping.set__status("add");
            } else {
                artOptionsMapping.set__status("update");
            }
        }
        return resultList;
    }


    /**
     * 导入参数完整性与重复校验
     *
     * @param list
     * @return
     */
    public List<String> checkItemValueOptions(List<ArtOptionsMappingDto> list, List<ArtOptionsMapping> realList) {
        List<String> checkFingerprint = new ArrayList<String>();
        List<String> resultList = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            //校验参数是否为空
            ArtOptionsMappingDto artOptionsMappingDto = list.get(i);
            //记录导入行号
            artOptionsMappingDto.setEntryNum(i + 1);
            if (StringUtils.isEmpty(artOptionsMappingDto.getPlatformCode())) {
                resultList.add("第" + i + 1 + "行平台编码不能为空");
            }
            if (StringUtils.isEmpty(artOptionsMappingDto.getOptionType())) {
                resultList.add("第" + i + 1 + "行选项类型不能为空");
            } else {
                if (!artOptionsMappingDto.getOptionType().equals(ArtOptionsMappingStatus.FB.name()) && !artOptionsMappingDto.getOptionType().equals(ArtOptionsMappingStatus.FN.name())) {
                    resultList.add("第" + i + 1 + "行选项类型填写错误");
                }
            }
            if (StringUtils.isEmpty(artOptionsMappingDto.getOptionCode())) {
                resultList.add("第" + i + 1 + "行选项编码不能为空");
            } else {
                if (artOptionsMappingDto.getOptionCode().length() != 2) {
                    resultList.add("第" + i + 1 + "行选项编码长度不正确");
                }
            }
            //指纹码生成规则： 平台编码、选项类型、选项编码、配置包编码、物料编码 五个字段值字符串拼接
            String pingerprint = artOptionsMappingDto.getPlatformCode() + artOptionsMappingDto.getOptionType() + artOptionsMappingDto.getOptionCode() + artOptionsMappingDto.getOptionMatCode() + artOptionsMappingDto.getOptionValueMatCode();
            if (checkFingerprint.contains(pingerprint)) {
                int index = checkFingerprint.indexOf(pingerprint);
                resultList.add("第" + i + 1 + "行与第" + index + "行重复");
            } else {
                checkFingerprint.add(pingerprint);
            }
            artOptionsMappingDto.setFingerprint(pingerprint);
            realList.add(convertData(artOptionsMappingDto));
        }
        return resultList;
    }
}
