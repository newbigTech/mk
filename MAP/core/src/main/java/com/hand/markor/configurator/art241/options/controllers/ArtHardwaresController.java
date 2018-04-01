package com.hand.markor.configurator.art241.options.controllers;

import com.hand.common.util.BeanUtilsExtends;
import com.hand.common.util.ExcelUtil;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.markor.configurator.art241.options.dto.ArtHardwaresDto;
import com.markor.map.configurator.art241.options.dto.ArtHardwares;
import com.markor.map.configurator.art241.options.service.IArtHardwaresService;
import com.markor.map.framework.common.exception.BusinessException;
import com.markor.map.framework.common.exception.InvalidDataException;
import com.markor.map.framework.common.interf.entities.ResponseData;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Author:zhangyanan
 * @Description: 五金选项Controller, 提供页面查询和excel导入等功能
 * @Date:Crated in 16:01 2018/1/29
 * @Modified By:
 */
@RestController
public class ArtHardwaresController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(ArtHardwaresController.class);

    @Autowired
    private IArtHardwaresService iArtHardwaresService;

    @RequestMapping(value = "/markor/art241/options/artHardwares/query")
    public ResponseData query(HttpServletRequest request, ArtHardwares artHardwares,
                              @RequestParam(defaultValue = DEFAULT_PAGE) int page, @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(iArtHardwaresService.queryList(requestContext, artHardwares, page, pageSize));
    }

    /**
     * 删除五金选配行
     *
     * @param metalsList
     * @return
     */
    @RequestMapping(value = "/markor/art241/options/artHardwares/delArtHardwares")
    public ResponseData delArtHardwaresEntry(@RequestBody List<ArtHardwares> metalsList) {
        iArtHardwaresService.batchDelete(metalsList);
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);
        responseData.setMessage("删除成功");
        return responseData;
    }

    /**
     * 保存五金选配行
     *
     * @param artHardwaresList
     * @return
     */
    @RequestMapping(value = "/markor/art241/options/artHardwares/saveArtHardwares")
    public ResponseData saveArtHardwares(@RequestBody List<ArtHardwares> artHardwaresList) {
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);
        responseData.setMessage("保存成功");
        List<ArtHardwaresDto> artHardwaresDtoList = BeanUtilsExtends.copyListProperties(artHardwaresList, ArtHardwaresDto.class);
        try {
            //校验传入数据的指纹码验证数据是否唯一
            checkDataUnique(artHardwaresDtoList);
            //保存数据
            iArtHardwaresService.saveArtHardwares(artHardwaresList);
        } catch (InvalidDataException e) {
            logger.error("保存失败：", e);
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
            return responseData;
        } catch (BusinessException e) {
            logger.error("保存失败：", e);
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }

    /**
     * 下载五金选项导入Excel模板
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/markor/art241/options/artHardwares/downloadExcelTemplate", method = RequestMethod.GET)
    public void downloadExcelTemplate(HttpServletRequest request, HttpServletResponse response) {
        new ExcelUtil(ArtHardwaresDto.class)
                .downloadExcelModel(request, response, ArtHardwaresDto.DEFAULT_EXCEL_FILE_NAME, ArtHardwaresDto.DEFAULT_SHEET_NAME);
    }

    /**
     * 五金选项导入
     *
     * @param file
     * @return ResponseData
     * @throws Exception
     */
    @RequestMapping(value = "/markor/art241/options/artHardwares/importExcel", method = RequestMethod.POST)
    public ResponseData importExcel(MultipartFile file) {
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);
        responseData.setMessage("excel导入成功！");
        List<ArtHardwaresDto> list = null;
        try {
            //将excel数据转换为List
            list = new ExcelUtil<>(ArtHardwaresDto.class).importExcel(file.getOriginalFilename(), ArtHardwaresDto.DEFAULT_SHEET_NAME, file.getInputStream());
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
                List<ArtHardwares> artHardwaresList = BeanUtilsExtends.copyListProperties(list, ArtHardwares.class);
                //保存数据
                iArtHardwaresService.saveArtHardwares(artHardwaresList);
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
     * @param artHardwaresDtoList
     * @throws InvalidDataException
     */
    private void checkExcelData(List<ArtHardwaresDto> artHardwaresDtoList) throws InvalidDataException {
        checkIsNotNull(artHardwaresDtoList);
        checkAttributeLength(artHardwaresDtoList);
        checkDataUnique(artHardwaresDtoList);
    }

    /**
     * 判断Excel五金选项非空校验
     *
     * @param artHardwaresDtoList
     * @throws InvalidDataException
     */
    private void checkIsNotNull(List<ArtHardwaresDto> artHardwaresDtoList) throws InvalidDataException {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < artHardwaresDtoList.size(); i++) {
            ArtHardwaresDto artHardwaresDto = artHardwaresDtoList.get(i);
            if (null == artHardwaresDto.getPlatformCode() || "".equals(artHardwaresDto.getPlatformCode().trim())) {
                sb.append("第" + (i + 2) + "行数据的平台编码不能为空！");
            }
            if (null == artHardwaresDto.getHandlesComCode() || "".equals(artHardwaresDto.getHandlesComCode().trim())) {
                sb.append("第" + (i + 2) + "行数据的拉手组合不能为空！");
            }
            if (null == artHardwaresDto.getHandlesColor() || "".equals(artHardwaresDto.getHandlesColor().trim())) {
                sb.append("第" + (i + 2) + "行数据的拉手颜色不能为空！");
            }
        }
        if (sb.length() > 0) {
            throw new InvalidDataException(sb.toString());
        }
    }

    /**
     * 校验Excel属性长度是否合法
     *
     * @param artHardwaresDtoList
     * @throws InvalidDataException
     */
    private void checkAttributeLength(List<ArtHardwaresDto> artHardwaresDtoList) throws InvalidDataException {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < artHardwaresDtoList.size(); i++) {
            ArtHardwaresDto artHardwaresDto = artHardwaresDtoList.get(i);
            if (artHardwaresDto.getPlatformCode().length() > 10) {
                sb.append("第" + (i + 2) + "行数据的平台编码字段长度有误,不能超过10位！");
            }
            if (artHardwaresDto.getHandlesComCode().length() > 3) {
                sb.append("第" + (i + 2) + "行数据的拉手组合字段长度有误,不能超过3位！");
            }
            if (artHardwaresDto.getHandlesColor().length() > 1) {
                sb.append("第" + (i + 2) + "行数据的拉手颜色字段长度有误,不能超过1位！");
            }
            if (artHardwaresDto.getOptionMatCode().length() > 10) {
                sb.append("第" + (i + 2) + "行数据的配置包编码长度有误,不能超过10位！");
            }
            if (artHardwaresDto.getOptionValueMatCode().length() > 10) {
                sb.append("第" + (i + 2) + "行数据的材料编码长度有误,不能超过10位！");
            }
        }
        if (sb.length() > 0) {
            throw new InvalidDataException(sb.toString());
        }
    }

    /**
     * 根据指纹码校验行数据是否重复
     *
     * @param artHardwaresDtoList
     * @throws InvalidDataException
     */
    private void checkDataUnique(List<ArtHardwaresDto> artHardwaresDtoList) throws InvalidDataException {
        Set<String> set = new HashSet<>();
        Map<Integer, String> treeMap = new HashMap<>();
        StringBuffer sb = new StringBuffer();
        for (int i = artHardwaresDtoList.size(); i > 0; i--) {
            ArtHardwaresDto artHardwaresDto = artHardwaresDtoList.get(i - 1);
            /**artHardwaresDtoList
             * 指纹码，用于确认唯一一行数据内容。
             * 指纹码生成规则： fingerprint = 平台编码 + 拉手组合码 + 拉手颜色 + 配置包编码 + 材料编码
             */
            String fingerprint = artHardwaresDto.getPlatformCode() + artHardwaresDto.getHandlesComCode()
                    + artHardwaresDto.getHandlesColor() + artHardwaresDto.getOptionMatCode()
                    + artHardwaresDto.getOptionValueMatCode();
            if (!set.contains(fingerprint)) {
                set.add(fingerprint);
            } else {
                treeMap.put(i, "第" + i + "行数据重复,请删除此行再重新导入！");
            }
        }
        if (treeMap.size() > 0) {
            for (Map.Entry<Integer, String> entry : treeMap.entrySet()) {
                sb.append(entry.getValue());
            }
            throw new InvalidDataException(sb.toString());
        }
    }
}