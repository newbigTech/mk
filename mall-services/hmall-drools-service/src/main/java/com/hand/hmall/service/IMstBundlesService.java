package com.hand.hmall.service;


import com.hand.hmall.dto.MstBundles;
import com.hand.hmall.dto.ResponseData;

import java.util.List;

public interface IMstBundlesService {

    ResponseData checkBundles(MstBundles bundles);

    ResponseData createActivity(MstBundles bundles);

    ResponseData stopBundleActivity(List<String> promotionIds);

    ResponseData startBundleActivity(List<String> promotionIds);

    ResponseData deleteBundleActivity(List<String> promotionIds);

    void updateBundle(MstBundles bundles, Object example);

    void insertBundel(MstBundles bundles);
}