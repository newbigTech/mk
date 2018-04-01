package com.hand.hmall.client.impl;

import com.hand.hmall.client.IPromoteClient;
import com.hand.hmall.dto.ResponseData;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @author shengli.yuan@hand-china.com
 */
public class PromoteClientImpl implements IPromoteClient {

	@Override
	public ResponseData queryByUserId(String userId, int page, int pagesize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseData convertByAdmin(@RequestBody Map<String, Object> convertMap) {
		return null;
	}

	@Override
	public ResponseData checkedCount(@RequestBody List<Map<String, Object>> notInCounts) {
		return null;
	}

	@Override
	public ResponseData queryByCidAndUserIds(@RequestBody Map<String, Object> map) {
		return null;
	}



}
