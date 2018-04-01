package com.hand.hmall.mst.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.CodeValue;
import com.hand.hap.system.service.ICodeService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.mst.dto.Logisticsco;
import com.hand.hmall.mst.dto.Subcarriage;
import com.hand.hmall.mst.mapper.SubcarriageMapper;
import com.hand.hmall.mst.service.ILogisticscoService;
import com.hand.hmall.mst.service.ISubcarriageService;
import com.markor.map.external.fndregionservice.dto.RegionDto;
import com.markor.map.external.fndregionservice.service.IFndRegionsCommonExternalService;
import com.markor.map.framework.common.interf.entities.PaginatedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 支线运费的service实现类
 * @date 2017/7/10 14:37
 */
@Service
@Transactional
public class SubcarriageServiceImpl extends BaseServiceImpl<Subcarriage> implements ISubcarriageService {

    @Autowired
    private SubcarriageMapper subcarriageMapper;

    @Autowired
    private ICodeService codeService;

    @Autowired
    private ILogisticscoService logisticscoService;

    @Autowired
    private IFndRegionsCommonExternalService iFndRegionsCommonExternalService;

    /**
     * 支线运费维护界面查询
     *
     * @param iRequest
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<Subcarriage> selectSubcarriage(IRequest iRequest, Subcarriage dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return subcarriageMapper.selectSubcarriage(dto);
    }

    /**
     * 通过承运商编码+承运商类型+区编码确认唯一记录
     *
     * @param dto
     * @return
     */
    @Override
    public List<Subcarriage> selectUnique(Subcarriage dto) {
        return subcarriageMapper.selectUnique(dto);
    }

    /**
     * 导入支线运费时，校验数据的正确性
     *
     * @param request
     * @param list
     * @return
     */
    @Override
    public Map<String, Object> checkSubcarriage(IRequest request, List<Subcarriage> list) throws InvocationTargetException, IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        List<Subcarriage> subcarriageList = new ArrayList<>();

        //校验输入的值是否在快码表中存在
        List<CodeValue> shippingTypeData = codeService.selectCodeValuesByCodeName(request, "HMALL.SHIPPING_TYPE");
        List<CodeValue> statusData = codeService.selectCodeValuesByCodeName(request, "HMALL.INSTALLATION.STATUS");
        List<CodeValue> priceModeData = codeService.selectCodeValuesByCodeName(request, "HMALL.OM.CHARGING");
        String result = null;

        if (list != null && list.size() > 0) {
            for (Subcarriage subcarriage : list) {

                //非空验证
                if (subcarriage.getDistrictCode() == null || subcarriage.getLogisticscoCode() == null || subcarriage.getShippingType() == null) {
                    result = "承运商编码，承运商类型，市编码不能为空！";
                    continue;
                }

                //验证承运商类型
                int flag = 1;
                for (CodeValue codeValue : shippingTypeData) {
                    if (subcarriage.getShippingType().equals(codeValue.getMeaning())) {
                        subcarriage.setShippingType(codeValue.getValue());
                        flag = 0;
                        break;
                    }
                }
                if (flag == 1) {
                    result = "您输入的承运商类型不存在!";
                    continue;
                }

                //验证状态
                flag = 1;
                for (CodeValue codeValue : statusData) {
                    if (subcarriage.getStatus().equals(codeValue.getMeaning())) {
                        subcarriage.setStatus(codeValue.getValue());
                        flag = 0;
                        break;
                    }
                }
                if (flag == 1) {
                    result = "您输入的状态不存在!";
                    continue;
                }
                //验证计价方式
                flag = 1;
                for (CodeValue codeValue : priceModeData) {
                    if (subcarriage.getPriceMode().equals(codeValue.getMeaning())) {
                        subcarriage.setPriceMode(codeValue.getValue());
                        flag = 0;
                        break;
                    }
                }
                if (flag == 1) {
                    result = "您输入的计价方式不存在!";
                    continue;
                }

                //验证承运商编码
                Logisticsco logisticsco = new Logisticsco();
                logisticsco.setCode(subcarriage.getLogisticscoCode());
                List<Logisticsco> logisticscoList = logisticscoService.logisticsoLov(request, logisticsco);
                if (logisticscoList == null || logisticscoList.size() == 0) {
                    result = "您输入的承运商编码不存在!";
                    continue;
                } else {
                    subcarriage.setLogisticscoId(logisticscoList.get(0).getLogisticscoId());
                }

                //验证区编码
                RegionDto regionDto = new RegionDto();
                regionDto.setRegionCode(subcarriage.getDistrictCode());
                regionDto.setRegionType("AREA");
                PaginatedList<RegionDto> regionDtoList = iFndRegionsCommonExternalService.selectAllCity(regionDto, 1, -1);
                if (regionDtoList == null || regionDtoList.getRows().size() == 0) {
                    result = "您输入的区编码不存在!";
                    continue;
                }

                //通过承运商编码+承运商类型+区编码确认唯一记录
                List<Subcarriage> sList = this.selectUnique(subcarriage);
                if (sList != null && sList.size() > 0) {
                    subcarriage.setSubcarriageId(sList.get(0).getSubcarriageId());
                }
                subcarriageList.add(subcarriage);
            }
        }
        map.put("subcarriageList", subcarriageList);
        map.put("result", result);
        return map;
    }
}