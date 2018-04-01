package com.hand.hmall.pin.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.pin.dto.Alter;
import com.hand.hmall.pin.mapper.AlterMapper;
import com.hand.hmall.pin.service.IAlterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 马君
 * @version 0.1
 * @name AlterServiceImpl
 * @description PIN码实体类的Service实现
 * @date 2017/8/10 17:09
 */
@Service
public class AlterServiceImpl extends BaseServiceImpl<Alter>
        implements IAlterService {

    @Autowired
    private AlterMapper alterMapper;

    @Override
    public Alter selectByEventCode(String eventCode) {
        Alter alter = new Alter();
        alter.setEventCode(eventCode);
        return alterMapper.selectOne(alter);
    }
}
