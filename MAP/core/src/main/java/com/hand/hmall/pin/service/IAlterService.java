package com.hand.hmall.pin.service;

import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.pin.dto.Alter;

/**
 * @author 马君
 * @version 0.1
 * @name IAlterService
 * @description 错误警报表service
 * @date 2017/8/10 17:08
 */
public interface IAlterService extends IBaseService<Alter> {

    Alter selectByEventCode(String eventCode);
}
