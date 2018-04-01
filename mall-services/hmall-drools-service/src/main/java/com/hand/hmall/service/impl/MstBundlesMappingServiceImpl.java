package com.hand.hmall.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;

import com.hand.hmall.mapper.MstBundlesMappingMapper;
import com.hand.hmall.service.IMstBundlesMappingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @descrption 商品套装映射service 可删除
 * Created by heng.zhang04@hand-china.com
 * 2017/8/30
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MstBundlesMappingServiceImpl  implements IMstBundlesMappingService {

    @Autowired
    private MstBundlesMappingMapper mstBundlesMappingMapper;

}