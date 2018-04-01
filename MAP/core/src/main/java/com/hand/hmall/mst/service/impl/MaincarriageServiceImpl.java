package com.hand.hmall.mst.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.CodeValue;
import com.hand.hap.system.service.ICodeService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.mst.dto.Logisticsco;
import com.hand.hmall.mst.dto.Maincarriage;
import com.hand.hmall.mst.mapper.MaincarriageMapper;
import com.hand.hmall.mst.service.ILogisticscoService;
import com.hand.hmall.mst.service.IMaincarriageService;
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
 * @Description: 主干运费对象的service实现类
 * @date 2017/7/10 14:37
 */
@Service
@Transactional
public class MaincarriageServiceImpl extends BaseServiceImpl<Maincarriage> implements IMaincarriageService {

    @Autowired
    private MaincarriageMapper maincarriageMapper;

    @Autowired
    private ICodeService codeService;

    @Autowired
    private ILogisticscoService logisticscoService;

    @Autowired
    private IFndRegionsCommonExternalService iFndRegionsCommonExternalService;

    /**
     * 主干运费界面查询
     *
     * @param iRequest
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<Maincarriage> selectMaincarriage(IRequest iRequest, Maincarriage dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return maincarriageMapper.selectMaincarriage(dto);
    }

    /**
     * 通过承运商编码+承运商类型+区编码确认唯一记录
     *
     * @param dto
     * @return
     */
    @Override
    public List<Maincarriage> selectUnique(Maincarriage dto) {
        return maincarriageMapper.selectUnique(dto);
    }

    /**
     * 导入主干运费时，验证数据的正确性
     *
     * @param iRequest
     * @param list
     * @return
     */
    @Override
    public Map<String, Object> checkMaincarriage(IRequest iRequest, List<Maincarriage> list) throws InvocationTargetException, IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        List<Maincarriage> maincarriageList = new ArrayList<>();

        //校验输入的值是否在快码表中存在
        List<CodeValue> shippingTypeData = codeService.selectCodeValuesByCodeName(iRequest, "HMALL.SHIPPING_TYPE");
        List<CodeValue> statusData = codeService.selectCodeValuesByCodeName(iRequest, "HMALL.INSTALLATION.STATUS");
        List<CodeValue> priceModeData = codeService.selectCodeValuesByCodeName(iRequest, "HMALL.OM.CHARGING");
        String result = null;

        if (list != null && list.size() > 0) {
            for (Maincarriage maincarriage : list) {

                //非空验证
                if (maincarriage.getCityCode() == null || maincarriage.getLogisticscoCode() == null || maincarriage.getShippingType() == null) {
                    result = "承运商编码，承运商类型，市编码不能为空！";
                    continue;
                }
                //验证承运商类型
                int flag = 1;
                for (CodeValue codeValue : shippingTypeData) {
                    if (maincarriage.getShippingType().equals(codeValue.getMeaning())) {
                        maincarriage.setShippingType(codeValue.getValue());
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
                    if (maincarriage.getStatus().equals(codeValue.getMeaning())) {
                        maincarriage.setStatus(codeValue.getValue());
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
                    if (maincarriage.getPriceMode().equals(codeValue.getMeaning())) {
                        maincarriage.setPriceMode(codeValue.getValue());
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
                logisticsco.setCode(maincarriage.getLogisticscoCode());
                List<Logisticsco> logisticscoList = logisticscoService.logisticsoLov(iRequest, logisticsco);
                if (logisticscoList == null || logisticscoList.size() == 0) {
                    result = "您输入的承运商编码" + maincarriage.getLogisticscoCode() + "不存在!";
                    continue;
                } else {
                    maincarriage.setLogisticscoId(logisticscoList.get(0).getLogisticscoId());
                }

                //验证市编码
                RegionDto regionDto = new RegionDto();
                regionDto.setRegionCode(maincarriage.getCityCode());
                regionDto.setRegionType("CITY");
                PaginatedList<RegionDto> regionDtoList = iFndRegionsCommonExternalService.selectAllCity(regionDto, 1, -1);
                if (regionDtoList == null || regionDtoList.getRows().size() == 0) {
                    result = "您输入的市编码" + maincarriage.getCityCode() + "不存在!";
                    continue;
                }

                //通过承运商编码+承运商类型+区编码确认唯一记录
                List<Maincarriage> mList = this.selectUnique(maincarriage);
                if (mList != null && mList.size() > 0) {
                    maincarriage.setMaincarriageId(mList.get(0).getMaincarriageId());
                }
                maincarriageList.add(maincarriage);
            }
        }
        map.put("maincarriageList", maincarriageList);
        map.put("result", result);
        return map;
    }
}