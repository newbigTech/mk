package com.hand.hmall.ws.impl;


import com.hand.hmall.mst.dto.MstFabric;
import com.hand.hmall.mst.mapper.MstFabricMapper;
import com.hand.hmall.services.ws.IRetailFabricServer4Retail;
import com.hand.hmall.ws.entities.MstFabricModel;
import com.hand.hmall.ws.entities.MstFabricRequstBody;
import com.hand.hmall.ws.entities.MstFabricResponseBody;
import com.hand.hmall.ws.entities.MstFabricResponseModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xuxiaoxue
 * @version 0.1
 * @name IRetailFabricServerImpl
 * @description hmall提供的获取Retial面料等级信息（Dubbo）接口，map-api-adapter调用
 * @date 2017/6/30
 */
public class RetailFabricServerImpl implements IRetailFabricServer4Retail {


    @Autowired
    private MstFabricMapper mstFabricMapper;


    @Override
    public MstFabricResponseBody getFabricInfo(MstFabricRequstBody r) {
        List<MstFabricResponseModel> list = new ArrayList<MstFabricResponseModel>();
        MstFabricResponseBody responseBody = new MstFabricResponseBody();
        if (r != null) {
            for (int i = 0; i < r.getMstFabricModelList().size(); i++) {

                MstFabricModel mstFabricModelFromWs = r.getMstFabricModelList().get(i);
                //如果数据没有传入物料编码字段值，返回错误信息"物料编码不存在"
                if (mstFabricModelFromWs.getFabricCode() == null || mstFabricModelFromWs.getFabricCode().equals("")) {
                    list.add(addMstFabricResponseBody("", "E", "物料编码字段缺失"));
                    responseBody.setMstFabricResponseList(list);
                    //如果数据没有传入物料编码字段值，返回错误信息"面料等级不存在"
                } else if (mstFabricModelFromWs.getFabricLevel() == null || mstFabricModelFromWs.getFabricLevel().equals("")) {
                    list.add(addMstFabricResponseBody(mstFabricModelFromWs.getFabricCode(), "E", "面料等级字段缺失"));
                    responseBody.setMstFabricResponseList(list);
                } else {
                    MstFabric fabric = new MstFabric();
                    fabric.setFabricCode(mstFabricModelFromWs.getFabricCode());
                    //这里根据FabricCode字段去数据库中查询,根据对应的记录是否存在来决定执行插入或更新操作
                    List<MstFabric> existList = mstFabricMapper.select(fabric);
                    fabric.setFabricLevel(mstFabricModelFromWs.getFabricLevel());
                    if (existList.size() == 0) {
                        //如果没有的话插入
                        mstFabricMapper.insertSelective(fabric);
                    } else {
                        //如果有的话更新
                        mstFabricMapper.updateByFabricCode(fabric);
                    }

                    list.add(addMstFabricResponseBody(mstFabricModelFromWs.getFabricCode(), "S", "处理成功"));
                    responseBody.setMstFabricResponseList(list);
                }
            }
            return responseBody;
        } else {
            list.add(addMstFabricResponseBody("", "E", "系统异常"));
            responseBody.setMstFabricResponseList(list);
            return responseBody;
        }
    }


    /**
     * 获得返回Model
     *
     * @param MATNR
     * @param M
     * @param MSG
     * @return MstFabricResponseBody
     */
    public MstFabricResponseModel addMstFabricResponseBody(String MATNR, String M, String MSG) {

        MstFabricResponseModel mstFabricResponseModel = new MstFabricResponseModel();

        try {
            mstFabricResponseModel.setMatnr(MATNR);
            mstFabricResponseModel.setM(M);
            mstFabricResponseModel.setMsg(new String(MSG.getBytes("utf-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return mstFabricResponseModel;
    }
}
