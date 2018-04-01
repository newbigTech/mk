package com.hand.hmall.fnd.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.mybatis.entity.Example;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.fnd.mapper.FndBusinessLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hmall.fnd.dto.FndBusinessLog;
import com.hand.hmall.fnd.service.IFndBusinessLogService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class FndBusinessLogServiceImpl extends BaseServiceImpl<FndBusinessLog> implements IFndBusinessLogService{

    @Autowired
    private FndBusinessLogMapper businessLogMapper;

    /**
     * 操作日志标签页根据订单ID查询数据
     * @param dto   请求参数，包含订单ID
     * @param page  显示数据页数
     * @param pageSize     每页显示数据条数
     * @param requestContext   请求体
     * @return  查询结果
     */
    @Override
    public List<FndBusinessLog> query(IRequest requestContext, FndBusinessLog dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<FndBusinessLog> result = businessLogMapper.selectByOrderId(dto);
        result.stream().forEach(new Consumer <FndBusinessLog>() {
            @Override
            public void accept(FndBusinessLog fndBusinessLog) {
                boolean notOnly = fndBusinessLog.getOperationType().equals("中台订单下载") && result.size() > 1;
                if(fndBusinessLog.getLastVersion() == null && fndBusinessLog.getCurrentVersion() == null){
                    if(notOnly){
                        fndBusinessLog.setLastVersion(0L);
                        fndBusinessLog.setCurrentVersion(1L);
                    }
                }
            }
        });
        return result;
    }
}