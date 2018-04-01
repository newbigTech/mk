package com.hand.hap.im.service;

import com.hand.hap.core.IRequest;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * Item导入导出Service
 *
 * @author alaowan
 * Created at 2017/12/26 23:42
 * @description
 */
public interface IItemInfaceImpExpService {

    /**
     * 导入item excel
     *
     * @param iRequest
     * @param inputStream
     * @return
     * @throws Exception
     * @author yougui.wu@hand-china.com
     */
    Map<String, Object> importItemExcel(IRequest iRequest, InputStream inputStream, String fileName) throws Exception;

    /**
     * 导出item excel模板
     *
     * @param request
     * @param fileName
     * @return
     * @throws Exception
     * @author yougui.wu@hand-china.com
     */
    ByteArrayOutputStream exportItemExcel(IRequest request, String fileName) throws Exception;

    /**
     * 导出item value excel模板
     *
     * @param request
     * @param fileName
     * @return
     * @throws Exception
     * @author yougui.wu@hand-china.com
     */
    ByteArrayOutputStream exportItemValueExcel(IRequest request, String fileName) throws Exception;

    /**
     * 导入item value excel
     *
     * @param iRequest
     * @param inputStream
     * @return
     * @throws Exception
     * @author yougui.wu@hand-china.com
     */
    Map<String, Object> importItemValueExcel(IRequest iRequest, InputStream inputStream, String fileName) throws Exception;
}
