package com.hand.hmall.ws.entities;

import java.io.Serializable;
import java.util.List;

/**
 * @author xuxiaoxue
 * @version 0.1
 * @name MstFabricRequstBody
 * @description 面料等级同步接口接收信息
 * @date 2017/7/3
 */
public class MstFabricRequstBody implements Serializable {
    private List<MstFabricModel> mstFabricModelList;

    public List<MstFabricModel> getMstFabricModelList() {
        return mstFabricModelList;
    }

    public void setMstFabricModelList(List<MstFabricModel> mstFabricModelList) {
        this.mstFabricModelList = mstFabricModelList;
    }
}
