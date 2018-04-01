package com.hand.hmall.service;

import com.hand.hmall.pojo.PointOfServiceData;
import com.hand.hmall.pojo.PsReceiveResponse;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name IPointOfServiceCXFService
 * @description 服务点 CXFService
 * @date 2017/6/21 17:57
 */
@WebService
public interface IPointOfServiceCXFService {

    /**
     * 从retail接收服务点信息
     * @param pointOfServiceDatas
     * @return
     */
    @WebMethod
    PsReceiveResponse receivePsListFromRetail(
            @WebParam(name = "pointOfServiceData") List<PointOfServiceData> pointOfServiceDatas);
}
