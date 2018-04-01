package com.hand.hmall.services.as;

import com.hand.hmall.as.mapper.AsReturnMapper;
import com.hand.hmall.services.as.dto.AsReturnBack;
import com.hand.hmall.services.as.dto.AsReturnForRetail;
import com.hand.hmall.services.as.service.IAsReturnServiceForRetail;
import com.hand.hmall.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * author: zhangzilong
 * name: AsReturnServiceForRetailImpl.java
 * discription:
 * date: 2018/1/2
 * version: 0.1
 */
public class AsReturnServiceForRetailImpl implements IAsReturnServiceForRetail{

    @Autowired
    private AsReturnMapper asReturnMapper;

    /**
     * 通过sap单号更新退货单状态
     *
     * @param arl
     * @return
     */
    @Override
    public List<AsReturnBack> updateStatus(List<AsReturnForRetail> arl) {
        List<AsReturnBack> returnList = new ArrayList<AsReturnBack>();
        if (arl.size() == 0) {
            AsReturnBack returnValue = new AsReturnBack();
            returnValue.setSapCode("");
            returnValue.setStatus("");
            returnValue.setNote("");
            returnValue.setCode("500");
            returnValue.setCodeMsg("传入数据为空");
            returnList.add(returnValue);
            return returnList;
        }

        for (AsReturnForRetail ar : arl) {
            AsReturnBack returnValue = new AsReturnBack();
            if (StringUtils.isEmpty(ar.getStatus())) {
                returnValue.setSapCode(ar.getSapCode());
                returnValue.setStatus(ar.getStatus());
                returnValue.setNote(ar.getNote());
                returnValue.setCode("500");
                returnValue.setCodeMsg("状态不能为空");
                return returnList;
            }

            if (StringUtils.isEmpty(ar.getSapCode())) {
                returnValue.setSapCode(ar.getSapCode());
                returnValue.setStatus(ar.getStatus());
                returnValue.setNote(ar.getNote());
                returnValue.setCode("500");
                returnValue.setCodeMsg("SAP单号不能为空");
                return returnList;
            }
            //判断回传状态是否为已完成 若为已完成且完成时间为空添加完成时间
            if (Constants.FINI.equals(ar.getStatus()) && ar.getFinishTime() == null) {
                ar.setFinishTime(new Date());
            }
            int updateCount = asReturnMapper.updateStatus(ar);


            if (updateCount == 1) {
                returnValue.setSapCode(ar.getSapCode());
                returnValue.setStatus(ar.getStatus());
                returnValue.setNote(ar.getNote());
                returnValue.setCode("200");
                returnValue.setCodeMsg("处理成功");
            } else {
                returnValue.setSapCode(ar.getSapCode());
                returnValue.setStatus(ar.getStatus());
                returnValue.setNote(ar.getNote());
                returnValue.setCode("500");
                returnValue.setCodeMsg("该条数据不存在");
            }
            returnList.add(returnValue);
        }
        return returnList;
    }
}
