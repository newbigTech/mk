package com.hand.hmall.mst.service.impl;

import com.markor.map.framework.restclient.RestClient;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.mst.dto.MstFabric;
import com.hand.hmall.mst.mapper.MstFabricMapper;
import com.hand.hmall.mst.service.IMstFabricService;
import com.hand.hmall.util.Constants;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import okhttp3.Response;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name MstFabricServiceImpl
 * @description 面料等级
 * @date 2017年5月26日10:52:23
 */
@Service
@Transactional
public class MstFabricServiceImpl extends BaseServiceImpl<MstFabric> implements IMstFabricService {

    private static final String FABRIC_TO_M3D_URL = "/modules/strawberry/webservice/rankAccept";
    private static final String SUCCESS_STATUS_CODE = "200";

    @Autowired
    private MstFabricMapper mapper;

    @Autowired
    private RestClient restClient;

    /**
     * 推送给M3D
     * @param mstFabricList
     * @throws IOException
     */
    @Override
    public void postToM3D(List<MstFabric> mstFabricList) throws IOException {
        JSONArray jsonArray = new JSONArray();
        for (MstFabric mstFabric : mstFabricList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("FABRIC_CODE", mstFabric.getFabricCode());
            jsonObject.put("FABRIC_LEVEL", mstFabric.getFabricLevel());
            jsonArray.add(jsonObject);
        }
        Response response = restClient.postString(Constants.M3D, FABRIC_TO_M3D_URL, jsonArray.toString(), Constants.MINI_TYPE_JSON, null, null);
        if (response.code() == 200) {
            JSONObject responseObj = RestClient.responseToJSON(response);
            if (SUCCESS_STATUS_CODE.equals(responseObj.getString("status"))) {
                mstFabricList.stream().forEach(mstFabric -> {
                    mstFabric.setSyncflag(Constants.YES);
                    this.updateByPrimaryKeySelective(RequestHelper.newEmptyRequest(), mstFabric);
                });
            } else {
                throw new RuntimeException(responseObj.getString("info"));
            }
        } else {
            throw new RuntimeException(response.message());
        }
    }

    /**
     * 导入面料等级时，验证数据的正确性
     * @param request
     * @param list
     * @return
     */
    @Override
    public String checkFabric(IRequest request, List<MstFabric> list) {
        String result = "";
        for (MstFabric fabric : list) {
            if (fabric.getFabricCode() == null || fabric.getFabricLevel() == null) {
                result = result + "面料编码和面料等级不能为空!";
                continue;
            }

            //验证面料编码的唯一性
            MstFabric mstFabric = new MstFabric();
            mstFabric.setFabricCode(fabric.getFabricCode());
            List<MstFabric> fabricsList = mapper.select(mstFabric);
            if (CollectionUtils.isNotEmpty(fabricsList)) {
                result = result + "面料编码" + fabric.getFabricCode() + "已存在!";
                continue;
            }
            mapper.insertSelective(fabric);
        }
        return result;
    }
}