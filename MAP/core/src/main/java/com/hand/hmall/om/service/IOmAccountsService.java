package com.hand.hmall.om.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.om.dto.OmAccounts;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IOmAccountsService extends IBaseService<OmAccounts>, ProxySelf<IOmAccountsService> {

    /**
     * 支付宝账单解析
     *
     * @return 返回信息
     */
    ResponseData AlipayBillAnalysisService(Date billDate);

    /**
     * 微信账单解析
     *
     * @return 返回信息
     */
    ResponseData WechatBillAnalysisService(Date billDate);

    /**
     * 银联账单解析
     *
     * @return 返回信息
     */
    ResponseData UnionBillAnalysisService(Date billDate);

    /**
     * 组织将要发送到Retail的账单数据并返回
     * @return
     */
    Map<String,Object> organizationBillDataToRetail();

    /**
     * 组织将要发送到Retail账单的对账数据并返回
     * @return
     */
    Map<String,Object> checkBillDataToRetail();

    /**
     * 财务上载界面列表查询
     *
     * @param dto
     * @return
     */
    List<OmAccounts> queryAccountsList(IRequest request, OmAccounts dto, int page, int pagesize);

    /**
     * 文件上传
     * @param requestContext
     * @param excelList
     * @throws Exception
     */
    void insertAllValue(IRequest requestContext, ArrayList<List<String>> excelList) throws Exception;

    /**
     * 账单上载专用解析文件方法
     */
    ArrayList FilesAnalysis(MultipartFile files) throws IOException;

    /**
     * 手工对账界面手工对账数据
     * @param dto
     * @return
     */
    List<OmAccounts> getAccountsForBalance(IRequest request, OmAccounts dto, int page, int pagesize);

    /**
     * 银联清算求和
     * 1、将每日清算的金额TYPE为"6"并且SYNC_FLAG为"N"的求和，如果大于0，生成一条TYPE为"5"的清算数据插入数据库
     * 2、将TYPE为"6"的数据的SYNC_FLAG设为"Y"
     */
    void unionLiquidationSum();

}