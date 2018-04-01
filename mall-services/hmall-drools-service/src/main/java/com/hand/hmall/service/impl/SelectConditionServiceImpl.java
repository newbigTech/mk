package com.hand.hmall.service.impl;

import com.hand.hmall.dao.SelectConditionDao;
import com.hand.hmall.dto.PagedValues;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.service.ISelectConditionService;
import com.hand.hmall.util.SortUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by shanks on 2017/3/3.
 * 促销条件结果查询服务
 */
@Service
public class SelectConditionServiceImpl implements ISelectConditionService {
    @Autowired
    private SelectConditionDao selectConditionDao;

    /**
     * 根据条件查询可选条件、结果 按照优先级从小到大排序
     * code
     * type
     * level
     * page
     * pageSize
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData selectByCode(Map<String, Object> map) {
        PagedValues pagedValues = selectConditionDao.selectByCode(map.get("code").toString(), map.get("selectType").toString(), (String) map.get("type"), (int) map.get("page"), (int) map.get("pageSize"));
        if (pagedValues.getValues() != null) {
            List<Map<String, ?>> values = pagedValues.getValues();
            try {
                SortUtil.listsort(values, "priority", true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ResponseData responseData = new ResponseData();
            responseData.setResp(values);
            responseData.setSuccess(true);
            return responseData;
        }
        return new ResponseData();
    }
}
