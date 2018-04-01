package com.hand.hmall.job;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.job.AbstractJob;
import com.hand.hmall.common.service.IGlobalVariantService;
import com.hand.hmall.log.dto.LogManager;
import com.hand.hmall.log.service.ILogManagerService;
import com.hand.hmall.mst.dto.Catalogversion;
import com.hand.hmall.mst.dto.Product;
import com.hand.hmall.mst.service.ICatalogversionService;
import com.hand.hmall.mst.service.IProductService;
import com.hand.hmall.util.Constants;
import com.hand.hmall.util.DateUtil;
import com.hand.hmall.util.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name ProductSyncJob
 * @description 商品同步JOB
 * @date 2017年6月29日14:47:27
 */
public class ProductSyncJob extends AbstractJob {
    @Autowired
    IProductService productService;

    @Autowired
    ICatalogversionService catalogversionService;

    @Autowired
    IGlobalVariantService globalVariantService;

    @Autowired
    ILogManagerService logManagerService;

    @Override
    public void safeExecute(JobExecutionContext jobExecutionContext) throws Exception {
        IRequest iRequest = RequestHelper.newEmptyRequest();
        iRequest.setRoleId(Constants.JOB_DEFAULT_USERID);
        iRequest.setLocale(Constants.JOB_DEFAULT_LANG);
        //todo 传参待确认
       // String catalog = jobExecutionContext.getMergedJobDataMap().getString("catalog");
        String catalog = Constants.CATALOG_VERSION_MARKOR;
        //master同步到staged
        LogManager log = new LogManager();
        log.setStartTime(new Date());
        log.setMessage("no data found");
        log.setProgramName(this.getClass().getName());
        log.setSourcePlatform(Constants.SOURCE_PLATFORM_SYNC);
        log = logManagerService.logBegin(iRequest, log);

        if (catalog != null) {
            Product dto = new Product();
            String strDate = globalVariantService.getValue("SYNC_DATE");
            Date date = new Date();
            if (StringUtils.isEmpty(strDate)) {
                globalVariantService.setValue("SYNC_DATE", DateUtil.getStartTime());
                date = DateUtil.getStrToDateTime(Constants.SYNC_JOB_DATE);
            } else {
                date = DateUtil.getStrToDateTime(strDate);
            }
            dto.setSyncData(date);
            //查询master-staged 的数据
            List<Product> list = productService.jobData(dto);
            String message = "";
            for (Product p : list) {
                message = message + "   " + p.getCode();
            }
            log.setMessage(message);
            Catalogversion catalogversion = new Catalogversion();
            catalogversion.setCatalogversion(Constants.CATALOG_VERSION_STAGED);
            catalogversion.setCatalogName(catalog);
            Long versionId = catalogversionService.selectCatalogversionId(catalogversion);
            List<Product> result = productService.syncJob(iRequest, list, versionId);
            if (CollectionUtils.isNotEmpty(result)) {
                String returnMessage = "";
            for (Product p : result) {
                returnMessage = returnMessage + "  " + p.getCode();
            }
            log.setProcessStatus(Constants.JOB_STATUS_SUCCESS);
            log.setReturnMessage(returnMessage);
            globalVariantService.setValue("SYNC_DATE", DateUtil.getStartTime());
        }
        logManagerService.logEnd(iRequest, log);
        } else {
            log.setProcessStatus(Constants.JOB_STATUS_ERROR);
            log.setReturnMessage("catalog 参数未填写");
            logManagerService.logEnd(iRequest, log);
        }
    }
}
