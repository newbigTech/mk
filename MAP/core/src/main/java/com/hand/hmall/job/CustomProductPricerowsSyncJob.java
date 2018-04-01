package com.hand.hmall.job;

import com.hand.hap.job.AbstractJob;
import com.hand.hmall.log.service.ILogManagerService;
import com.hand.hmall.mst.dto.Catalogversion;
import com.hand.hmall.mst.dto.Pricerow;
import com.hand.hmall.mst.service.ICatalogversionService;
import com.hand.hmall.mst.service.IPricerowService;
import com.hand.hmall.util.Constants;
import com.markor.map.framework.remotejob.client.annotation.RemoteJob;
import com.markor.map.framework.remotejob.client.common.JobResult;
import com.markor.map.framework.remotejob.client.common.RemoteJobTask;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name CustomProductPricerowsSyncJob
 * @description 订制品价格信息推送M3D
 * @date 2017/8/17 11:22
 */
@RemoteJob
public class CustomProductPricerowsSyncJob extends AbstractJob implements RemoteJobTask {

    private static final String JOB_DESC = "订制品价格信息推送M3D";
    private static final String URL = "/priceAccept";

    @Autowired
    private ICatalogversionService iCatalogversionService;

    @Autowired
    private ILogManagerService iLogManagerService;

    @Autowired
    private IPricerowService iPricerowService;

    @Override
    public void safeExecute(JobExecutionContext jobExecutionContext) throws Exception {

        iLogManagerService.logTrace(this.getClass(), JOB_DESC, null, "启动" + JOB_DESC);

        Catalogversion catalogversion = new Catalogversion();
        catalogversion.setCatalogName(Constants.CATALOG_VERSION_MARKOR);
        catalogversion.setCatalogversion(Constants.CATALOG_VERSION_ONLINE);
        Long markorOnlineId = iCatalogversionService.selectCatalogversionId(catalogversion);

        List<Pricerow> pricerowList = iPricerowService.selectUnsyncPricerows(markorOnlineId);
        iLogManagerService.logTrace(this.getClass(), JOB_DESC, null, "发现[" + CollectionUtils.size(pricerowList) + "]个待同步价格行");

        if (CollectionUtils.isEmpty(pricerowList)) {
            return;
        }

        try {
            iPricerowService.postToM3D(pricerowList);
            iLogManagerService.logSuccess(this.getClass(), JOB_DESC, null, "[" + CollectionUtils.size(pricerowList) + "]条价格行已同步成功");
        } catch (Exception e) {
            iLogManagerService.logError(this.getClass(), JOB_DESC, null, e.getMessage());
        }
    }

    @Override
    public JobResult execute() throws Exception {
        safeExecute(null);
        return JobResult.success(null);
    }
}
