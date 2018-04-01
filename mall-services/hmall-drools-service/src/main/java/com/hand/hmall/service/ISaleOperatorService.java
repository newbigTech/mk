package com.hand.hmall.service;

import com.hand.hmall.dto.ResponseData;


/**
 * Created by shanks on 2017/1/19.
 */
public interface ISaleOperatorService {

    ResponseData queryByBaseId(String baseId, int page, int pageSize);

}
