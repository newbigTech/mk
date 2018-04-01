package com.hand.hmall.ws.entities;

import java.io.Serializable;
import java.util.List;

/**
 * @author xuxiaoxue
 * @version 0.1
 * @name MstFabricResponseBody
 * @description 面料等级接口返回信息
 * @date 2017/7/3
 */
public class MstFabricResponseBody implements Serializable {
    List<MstFabricResponseModel> mstFabricResponseList;

    public List<MstFabricResponseModel> getMstFabricResponseList() {
        return mstFabricResponseList;
    }

    public void setMstFabricResponseList(List<MstFabricResponseModel> mstFabricResponseList) {
        this.mstFabricResponseList = mstFabricResponseList;
    }
}
