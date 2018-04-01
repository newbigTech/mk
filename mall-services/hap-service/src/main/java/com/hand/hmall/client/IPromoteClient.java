package com.hand.hmall.client;

import com.hand.hmall.client.impl.PromoteClientImpl;
import com.hand.hmall.dto.ResponseData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * @author qinsheng.wang@hand-china.com
 */
@FeignClient(value="hmall-promote-server",fallback = PromoteClientImpl.class)
public interface IPromoteClient {

	@RequestMapping(value = "/h/promotion/coupon/listUserId/{userId}/{page}/{pagesize}", method = RequestMethod.GET)
	 public ResponseData queryByUserId(@PathVariable("userId") String userId, @PathVariable("page") int page, @PathVariable("pagesize") int pagesize);

	@RequestMapping(value = "/h/promotion/coupon/convert/admin" , method = RequestMethod.POST)
	public ResponseData convertByAdmin(@RequestBody Map<String, Object> convertMap);

	@RequestMapping(value = "/h/redemption/checkedCouponCount" , method = RequestMethod.POST)
	public ResponseData checkedCount(@RequestBody List<Map<String, Object>> notInCounts);

	@RequestMapping(value = "/h/promotion/coupon/queryByCidAndUserIds" , method = RequestMethod.POST)
	public ResponseData queryByCidAndUserIds(@RequestBody Map<String, Object> map);

}
 