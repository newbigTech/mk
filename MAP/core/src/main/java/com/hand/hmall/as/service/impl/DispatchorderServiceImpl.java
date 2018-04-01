package com.hand.hmall.as.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.as.dto.Dispatchorder;
import com.hand.hmall.as.dto.ServiceorderEntry;
import com.hand.hmall.as.service.IDispatchorderService;
import com.hand.hmall.as.service.IServiceorderEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auth shoupeng.wei
 * @Description:派工单详情界面对应的Service类
 * @Create at 2017-7-17 11:41:15
 */
@Service
@Transactional
public class DispatchorderServiceImpl extends BaseServiceImpl<Dispatchorder> implements IDispatchorderService {

    @Autowired
    private IServiceorderEntryService serviceorderEntryService;

    @Override
    public List<ServiceorderEntry> selectDispatchOrderEntry(IRequest requestContext, Long serviceOrderId, int page, int pageSize) {
        PageHelper.startPage(page,pageSize);
        return serviceorderEntryService.selectDispatchOrderEntry(serviceOrderId);
    }

    @Override
    public List<Dispatchorder> saveDispatchOrederInfo(IRequest iRequest, Dispatchorder dto) {
        //判断新增还是修改
        if (dto.getReceiptOrderId() != null) {
            dto = this.updateByPrimaryKeySelective(iRequest, dto);
        } else {
            dto = this.insertSelective(iRequest, dto);

        }
        List<ServiceorderEntry> asDispatchorderEntries = dto.getAsDispatchorderEntryList();
        if(asDispatchorderEntries != null && asDispatchorderEntries.size() > 0){
            for(ServiceorderEntry entry:asDispatchorderEntries){
                //判断行数据修改还是新增
                if(entry.getServiceOrderEntryId() != null){
                    serviceorderEntryService.updateByPrimaryKeySelective(iRequest, entry);
                }else{
                    entry.setServiceOrderId(dto.getReceiptOrderId());
                    serviceorderEntryService.insertSelective(iRequest, entry);
                }
            }
        }

        List<Dispatchorder> result = new ArrayList<Dispatchorder>();
        result.add(dto);
        return result;
    }

}