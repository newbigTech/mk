package com.hand.hmall.om.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.om.dto.OmEdPromo;
import com.hand.hmall.om.mapper.OmEdPromoMapper;
import com.hand.hmall.om.service.IOmEdPromoService;
import com.hand.hmall.util.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class OmEdPromoServiceImpl extends BaseServiceImpl<OmEdPromo> implements IOmEdPromoService {
    @Autowired
    private OmEdPromoMapper omEdPromoMapper;
    @Autowired
    private IOmEdPromoService iOmEdPromoService;

    /**
     * 查询事后促销规则
     * @param page
     * @param pageSize
     * @param omEdPromo
     * @return
     */
    @Override
    public List<OmEdPromo> selectOmEdPromo(int page, int pageSize, OmEdPromo omEdPromo) {
        PageHelper.startPage(page, pageSize);
        return omEdPromoMapper.selectOmEdPromo(omEdPromo);
    }

    /**
     * 保存新增事后促销规则
     * @param iRequest
     * @param dto
     * @return
     */
    @Override
    public ResponseData saveOmEdPromo(IRequest iRequest, List<OmEdPromo> dto) {
        ResponseData responseData = new ResponseData();
        try {
            OmEdPromo omEdPromo = null;
            if (CollectionUtils.isNotEmpty(dto) && dto.get(0) != null) {
                dto.get(0).setStatus(Constants.OM_PROMO_RECODE_STATUS_ACTIVITY);
                //单人最高名额为空则将名额存入该字段
                if (dto.get(0).getMax() == null) {
                    dto.get(0).setMax(dto.get(0).getSpace());
                }
                if (iOmEdPromoService.selectByPrimaryKey(iRequest, dto.get(0)) == null) {
                    omEdPromo = iOmEdPromoService.insertSelective(iRequest, dto.get(0));
                } else {
                    omEdPromo = iOmEdPromoService.updateByPrimaryKeySelective(iRequest, dto.get(0));
                }
                List<OmEdPromo> list = new ArrayList<>();
                list.add(omEdPromo);
                responseData.setSuccess(true);
                responseData.setRows(list);
            }
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage("系统错误");
            throw new RuntimeException();
        }
        return responseData;
    }

    /**
     * 启用停用
     * @param dto
     * @param flag
     * @return
     */
    @Override
    public ResponseData updateStatus(List<OmEdPromo> dto, String flag) {
        ResponseData responseData = new ResponseData();
        try {
            if (CollectionUtils.isNotEmpty(dto)) {
                //启用
                if (flag.equals(Constants.ONE)) {
                    for (OmEdPromo omEdPromo : dto) {
                        omEdPromo.setStatus(Constants.OM_PROMO_RECODE_STATUS_ACTIVITY);
                        omEdPromoMapper.updateByPrimaryKeySelective(omEdPromo);
                    }
                }
                //停用
                else if (flag.equals(Constants.ZERO)) {
                    for (OmEdPromo omEdPromo : dto) {
                        omEdPromo.setStatus(Constants.OM_PROMO_RECODE_STATUS_INACTIVE);
                        omEdPromoMapper.updateByPrimaryKeySelective(omEdPromo);
                    }
                }
                responseData.setSuccess(true);
            }
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage("修改失败");
        }
        return responseData;
    }

    /**
     * 保存促销规则
     * @param dto
     * @return
     */
    @Override
    public ResponseData deleteOmEdPromoById(List<OmEdPromo> dto) {
        ResponseData responseData = new ResponseData();
        if (CollectionUtils.isNotEmpty(dto)) {
            for (OmEdPromo omEdPromo : dto) {
                omEdPromoMapper.deleteByPrimaryKey(omEdPromo);
            }
            responseData.setSuccess(true);
        } else {
            responseData.setSuccess(false);
            responseData.setMessage("删除失败");
        }
        return responseData;
    }

    /**
     * 根据订单时间判断状态
     *
     * @param omEdPromo
     * @return
     */
    private String checkStatus(OmEdPromo omEdPromo) {
        if (omEdPromo != null && omEdPromo.getOrderStartTime() != null && omEdPromo.getOrderEndTime() != null) {
            long orderStartTime = omEdPromo.getOrderStartTime().getTime();
            long orderEndTime = omEdPromo.getOrderEndTime().getTime();
            long now = new Date().getTime();
            //活动中
            if (now >= orderStartTime && now <= orderEndTime) {
                return "ACTIVITY";
            }
            //待生效
            else if (now < orderStartTime) {
                return "DELAY";
            }
            //已失效
            else if (now > orderEndTime) {
                return "FAILURE";
            }

        }
        return "";
    }

    /**
     * 赠品发放列表查询
     * @param request
     * @param omEdPromo
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<OmEdPromo> queryEdPromoListInfo(IRequest request, OmEdPromo omEdPromo, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return omEdPromoMapper.queryEdPromoListInfo(omEdPromo);
    }
}