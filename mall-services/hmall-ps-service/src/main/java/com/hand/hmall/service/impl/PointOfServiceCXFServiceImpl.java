package com.hand.hmall.service.impl;

import com.hand.hmall.common.ResponseType;
import com.hand.hmall.pojo.PointOfServiceData;
import com.hand.hmall.pojo.PsReceiveResponse;
import com.hand.hmall.service.IPointOfServiceCXFService;
import com.hand.hmall.validator.PointOfServiceDataValidator;
import com.hand.hmall.validator.ValidateResult;
import com.markor.map.external.pointservice.dto.PointOfServiceDto;
import com.markor.map.external.pointservice.service.IPointOfServiceExternalService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name PointOfServiceCXFServiceImpl
 * @description 服务点接口实现类
 * @date 2017/6/21 18:06
 */
@Service
public class PointOfServiceCXFServiceImpl implements IPointOfServiceCXFService {

    private static final Logger logger = LoggerFactory.getLogger(PointOfServiceCXFServiceImpl.class);

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private PointOfServiceDataValidator pointOfServiceDataValidator;

    @Autowired
    private IPointOfServiceExternalService iPointOfServiceExternalService;

    /**
     * 从retail接收服务点信息
     *
     * @param pointOfServiceDatas 服务点
     * @return PsReceiveResponse
     */
    public PsReceiveResponse receivePsListFromRetail(List<PointOfServiceData> pointOfServiceDatas) {
        PsReceiveResponse response = new PsReceiveResponse();
        if (CollectionUtils.isEmpty(pointOfServiceDatas)) {
            response.setLOGID("NO_DATA_FOUND");
            response.setMASSAGE("传入数据不能为空");
            response.setTYPE(ResponseType.ERROR);
            return response;
        }

        List<PointOfServiceDto> pointOfServiceDtoList = new ArrayList<>();
        for (int i = 0; i < pointOfServiceDatas.size(); i++) {
            PointOfServiceData pointOfServiceData = pointOfServiceDatas.get(i);
            ValidateResult validateResult = pointOfServiceDataValidator.validator(pointOfServiceData);
            if (validateResult.isValid()) {
                PointOfServiceDto pointOfServiceDto = new PointOfServiceDto();
                BeanUtils.copyProperties(pointOfServiceData, pointOfServiceDto);
                pointOfServiceDtoList.add(pointOfServiceDto);
            } else {
                response.setLOGID("INVALID_PARAMETER");
                response.setMASSAGE("数据校验失败，第" + (i + 1) + "行数据" + validateResult.getMessage());
                response.setTYPE(ResponseType.ERROR);
                return response;
            }
        }

        final int[] errorLine = new int[1];
        try {
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                public void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                    for (int i = 0; i < pointOfServiceDtoList.size(); i++) {
                        errorLine[0] = i + 1;
                        PointOfServiceDto pointOfServiceDto = pointOfServiceDtoList.get(i);
                        //查询符合条件数据,是否存在
                        PointOfServiceDto qualified = iPointOfServiceExternalService.selectPointOfServiceForCode(pointOfServiceDto.getCode());
                        if (null == qualified) {//不存在,插入
//                            pointOfServiceMapper.insertSelective(pointOfServiceDto);
                            iPointOfServiceExternalService.insertSelective(pointOfServiceDto);
                        } else {//存在,更新
                            pointOfServiceDto.setPointOfServiceId(qualified.getPointOfServiceId());
                            pointOfServiceDto.setLastUpdateDate(new Date());
//                            pointOfServiceMapper.updateByPrimaryKeySelective(pointOfService);
                            iPointOfServiceExternalService.updateByPrimaryKeySelective(pointOfServiceDto);
                        }
                    }
                }
            });

        } catch (Exception e) {
            logger.error(e.getMessage());
            response.setLOGID("ERROR");
            response.setMASSAGE("存储失败，在保存第" + errorLine[0] + "行数据是发生错误");
            response.setTYPE(ResponseType.ERROR);
            return response;
        }

        response.setLOGID("SUCCESS");
        response.setMASSAGE("存储成功");
        response.setTYPE(ResponseType.SUCCESS);

        return response;
    }
}
