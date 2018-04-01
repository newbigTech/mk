package com.hand.hmall.om.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.om.dto.HmallTmConsignmentHis;
import com.hand.hmall.om.mapper.TmConsignmentHisMapper;
import com.hand.hmall.om.service.ITmConsignmentHisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 刘宏玺
 * @version 0.1
 * @name ITmConsignmentHisService
 * @description 天猫订单发货信息导出结果用接口
 * @date 2017/5/22
 */
@Service
@Transactional
public class TmConsignmentHisServiceImpl extends BaseServiceImpl<HmallTmConsignmentHis> implements ITmConsignmentHisService {

    @Autowired
    private TmConsignmentHisMapper tmConsignmentHisMapper;

    /**
     * 导出所有的历史记录（倒序）
     *
     * @param request
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    public List<HmallTmConsignmentHis> queryAll(IRequest request, HmallTmConsignmentHis dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return this.tmConsignmentHisMapper.queryAll();
    }

}