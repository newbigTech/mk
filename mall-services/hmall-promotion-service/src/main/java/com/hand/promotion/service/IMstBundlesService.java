package com.hand.promotion.service;


import com.hand.dto.ResponseData;
import com.hand.promotion.pojo.bundles.MstBundles;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface IMstBundlesService {

    ResponseData checkBundles(MstBundles bundles);

    ResponseData createActivity(MstBundles bundles) throws NoSuchMethodException, InterruptedException, UnsupportedEncodingException, IllegalAccessException, RemotingException, MQClientException, MQBrokerException, InvocationTargetException;

    ResponseData stopBundleActivity(List<String> promotionIds);

    ResponseData startBundleActivity(List<String> promotionIds);

    ResponseData deleteBundleActivity(List<String> promotionIds) throws NoSuchMethodException, InterruptedException, UnsupportedEncodingException, IllegalAccessException, MQBrokerException, RemotingException, MQClientException, InvocationTargetException;

  /*  void updateBundle(MstBundles bundles, Object example);

    void insertBundel(MstBundles bundles);*/
}