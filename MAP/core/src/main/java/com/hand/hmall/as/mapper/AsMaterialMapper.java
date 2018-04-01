package com.hand.hmall.as.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.as.dto.AsMaterial;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AsMaterialMapper extends Mapper<AsMaterial> {

    AsMaterial getMaterialForRetail(AsMaterial asMaterial);

    List<AsMaterial> queryInfo(AsMaterial dto);

    /**
     * 查询物耗单列表
     *
     * @param code
     * @param serviceOrderCode
     * @param escOrderCode
     * @param customerid
     * @param mobile
     * @param sapCode
     * @param creationDateStart
     * @param creationDateEnd
     * @param finishTimeStart
     * @param finishTimeEnd
     * @return
     */
    List<AsMaterial> selectMaterialList(@Param("code") String code,
                                        @Param("serviceOrderCode") String serviceOrderCode,
                                        @Param("escOrderCode") String escOrderCode,
                                        @Param("customerid") String customerid,
                                        @Param("mobile") String mobile,
                                        @Param("sapCode") String sapCode,
                                        @Param("creationDateStart") String creationDateStart,
                                        @Param("creationDateEnd") String creationDateEnd,
                                        @Param("finishTimeStart") String finishTimeStart,
                                        @Param("finishTimeEnd") String finishTimeEnd,
                                        @Param("isCharge") String isCharge,
                                        @Param("syncRetail") String syncRetail,
                                        @Param("strMaterialStatus") String[] strMaterialStatus
    );
}